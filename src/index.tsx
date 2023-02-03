import { NativeModules, NativeEventEmitter, Platform } from 'react-native'

const { Detector } = NativeModules

enum EventsName {
  UserDidTakeScreenshot = 'UIApplicationUserDidTakeScreenshotNotification',
  UserDidTakeScreenRecord = 'UIApplicationUserDidTakeScreenRecordNotification',
}

const detectorEventEmitter = new NativeEventEmitter(Detector)

type Unsubscribe = () => void

const commonAddScreenshotListener = (listener: () => void): Unsubscribe => {
  const eventSubscription = detectorEventEmitter.addListener(
    EventsName.UserDidTakeScreenshot,
    () => listener(),
    {}
  )

  return () => {
    eventSubscription.remove()
  }
}

const commonAddScreenrecordListener = (listener: () => void): Unsubscribe => {
  const eventSubscription = detectorEventEmitter.addListener(
    EventsName.UserDidTakeScreenRecord,
    () => listener(),
    {}
  )

  return () => {
    eventSubscription.remove()
  }
}

const getListenersCount = (): number => {
  return (
    // React Native 0.64+
    // @ts-ignore
    detectorEventEmitter.listenerCount?.(EventsName.UserDidTakeScreenshot) ??
    // React Native < 0.64
    // @ts-ignore
    detectorEventEmitter.listeners?.(EventsName.UserDidTakeScreenshot).length ??
    0
  )
}

const getRecordListenersCount = (): number => {
  return (
    // React Native 0.64+
    // @ts-ignore
    detectorEventEmitter.listenerCount?.(EventsName.UserDidTakeScreenRecord) ??
    // React Native < 0.64
    // @ts-ignore
    detectorEventEmitter.listeners?.(EventsName.UserDidTakeScreenRecord)
      .length ??
    0
  )
}

export const startScreenshotDetection = () => {
  Detector.startScreenshotDetection()
}

export const stopScreenshotDetection = () => {
  Detector.stopScreenshotDetection()
}

export const startScreenrecordDetection = () => {
  Detector.startScreenrecordDetection()
}

export const stopScreenrecordDetection = () => {
  Detector.stopScreenrecordDetection()
}

export const isScreenRecording = async (): Promise<boolean | undefined> => {
  if (Platform.OS !== 'ios') {
    return
  }

  return await Detector?.isScreenRecording?.()
}

export const addScreenshotListener = Platform.select<
  (listener: () => void) => Unsubscribe
>({
  default: (): Unsubscribe => () => {},
  ios: commonAddScreenshotListener,
  android: (listener: () => void): Unsubscribe => {
    if (getListenersCount() === 0) {
      Detector.startScreenshotDetection()
    }

    const unsubscribe: Unsubscribe = commonAddScreenshotListener(listener)

    return () => {
      unsubscribe()

      if (getListenersCount() === 0) {
        Detector.stopScreenshotDetection()
      }
    }
  },
})

export const addScreenrecordListener = Platform.select<
  (listener: () => void) => Unsubscribe
>({
  default: (): Unsubscribe => () => {},
  ios: (): Unsubscribe => () => {},
  android: (listener: () => void): Unsubscribe => {
    if (getRecordListenersCount() === 0) {
      Detector.startScreenrecordDetection()
    }

    const unsubscribe: Unsubscribe = commonAddScreenrecordListener(listener)

    return () => {
      unsubscribe()

      if (getRecordListenersCount() === 0) {
        Detector.stopScreenrecordDetection()
      }
    }
  }
})