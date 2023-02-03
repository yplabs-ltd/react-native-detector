#import "Detector.h"

@implementation Detector

RCT_EXPORT_MODULE();

- (NSArray<NSString *> *)supportedEvents {
    return @[@"UIApplicationUserDidTakeScreenshotNotification"];
}

- (void)startObserving {
    NSNotificationCenter *center = [NSNotificationCenter defaultCenter];
    [center addObserver:self
    selector:@selector(sendNotificationToRN:)
    name:UIApplicationUserDidTakeScreenshotNotification
    object:nil];

}

- (void)stopObserving {
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

- (void)sendNotificationToRN:(NSNotification *)notification {
    [self sendEventWithName:notification.name
                   body:nil];
}

RCT_REMAP_METHOD(isScreenRecording,
    isScreenRecordingResolver: (RCTPromiseResolveBlock)resolve
    isScreenRecordingRejector: (RCTPromiseRejectBlock)reject
) {
    resolve(@([[[UIScreen mainScreen] valueForKey:@"isCaptured"] boolValue]));
}

@end
