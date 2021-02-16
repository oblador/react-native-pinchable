<p align="center">
  <h1 align="center">React Native Pinchable</h1>
  <h3 align="center">Instagram like pinch to zoom for React Native.</h3>
</p>

## Demo

![screencast](https://user-images.githubusercontent.com/378279/50738295-9610d280-11d2-11e9-9dba-c0005fa9bfaf.gif)

See [`Example`](https://github.com/oblador/react-native-pinchable/tree/master/Example) folder.

## Sponsors

If you find the library useful, please consider [sponsoring on Github](https://github.com/sponsors/oblador). 

---

<a href="https://bit.ly/2LV9E98"><img src="https://x.klarnacdn.net/payment-method/assets/badges/generic/klarna.svg" width="135" height="75" alt="Klarna" /><a>

Klarna aims to make online shopping frictionless and are [hiring engineers in Stockholm, Berlin and Milan](https://bit.ly/2LV9E98). Join me to work on one of the largest greenfield React Native apps in the community.

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

| Prop                   | Description                     | Default |
| ---------------------- | ------------------------------- | ------- |
| **`minimumZoomScale`** | The minimum allowed zoom scale. | `1`     |
| **`maximumZoomScale`** | The maximum allowed zoom scale. | `3`     |

## Limitations

On Android it's not possible to receive touch events on the views inside the `Pinchable` component.

## License

[MIT License](http://opensource.org/licenses/mit-license.html). © Joel Arvidsson 2019 - present
