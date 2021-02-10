# react-native-pinchable

Instagram like pinch to zoom for React Native. 

## Installation

```bash
# Add dependency
yarn add react-native-pinchable
# Link iOS dependency
pod install --project-directory=ios
# Compile project
react-native run-ios # or run-android
```

## Usage

```js
import Pinchable from 'react-native-pinchable';

<Pinchable>
  <Image source={...}>
</Pinchable>
```

### Properties

| Prop | Description | Default |
|------|-------------|---------|
|**`minimumZoomScale`**|The minimum allowed zoom scale.|`1`|
|**`maximumZoomScale`**|The maximum allowed zoom scale.|`3`|


## Demo

![screencast](https://user-images.githubusercontent.com/378279/50738295-9610d280-11d2-11e9-9dba-c0005fa9bfaf.gif)

See `Example` folder.

## License

[MIT License](http://opensource.org/licenses/mit-license.html). © Joel Arvidsson 2019 - present
