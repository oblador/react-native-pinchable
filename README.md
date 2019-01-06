# react-native-pinchable

Instagram like pinch to zoom for React Native. 

*Android not yet supported, PR appreciated*

## Installation

`$ yarn add react-native-pinchable`

### Option: With `react-native link`

`$ react-native link react-native-pinchable`

### Option: Manually

Add `ios/RNPinchable.xcodeproj` to **Libraries** and add `libRNPinchable.a` to **Link Binary With Libraries** under **Build Phases**. [More info and screenshots about how to do this is available in the React Native documentation](http://facebook.github.io/react-native/docs/linking-libraries-ios.html#content).

### Option: With [CocoaPods](https://cocoapods.org/)

Add the following to your `Podfile` and run `pod update`:

```
pod 'react-native-pinchable', :path => 'node_modules/react-native-pinchable'
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

## License

[MIT License](http://opensource.org/licenses/mit-license.html). © Joel Arvidsson 2019 - present
