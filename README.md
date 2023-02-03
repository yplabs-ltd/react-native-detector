# @yplabs-ltd/react-native-detector

a simply and easy to use screenshot detector for react native.

## Installation

yarn

```sh
yarn add @yplabs-ltd/react-native-detector
```

npm

```sh
npm install @yplabs-ltd/react-native-detector
```

### iOS

```sh
cd ios && pod install
```

### android

for Android you need to have access for `READ_EXTERNAL_STORAGE` to detect screenshots by user to do that you just need to add this line in `AndroidManifest.xml`

```xml
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

and get user permission

```js
import { PermissionsAndroid } from 'react-native';

//...
const requestPermission = async () => {
  await PermissionsAndroid.request(
    PermissionsAndroid.PERMISSIONS.READ_EXTERNAL_STORAGE,
    {
      title: 'Get Read External Storage Access',
      message: 'get read external storage access for detecting screenshots',
      buttonNeutral: 'Ask Me Later',
      buttonNegative: 'Cancel',
      buttonPositive: 'OK',
    }
  );
};
```

## Usage

```js
import {
  addScreenshotListener,
  addScreenrecordListener,
} from '@yplabs-ltd/react-native-detector';

// ...
React.useEffect(() => {
  const userDidScreenshot = () => {
    console.log('User took screenshot');
  };
  const unsubscribe = addScreenshotListener(userDidScreenshot);
  return () => {
    unsubscribe();
  };
}, []);
```

## Roadmap

|                        Status                         | Goal                                   |
| :---------------------------------------------------: | :------------------------------------- |
|                          ✅                           | iOS version of screenshot detector     |
| ✅ (Thanks to [@mhssn95](https://github.com/mhssn95)) | Android version of screenshot detector |
|                          ✅                           | IOS Screen recording detecting         |
|                          ✅                           | Android Screen recording detecting     |
|                          🚧                           | Calls detector                         |

## Contributing

Based on the project [react-native-detector](https://www.npmjs.com/package/react-native-detector) to develop more.
See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT
