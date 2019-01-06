import { Platform, requireNativeComponent, View } from 'react-native';

export default Platform.OS === 'ios' ? requireNativeComponent('RNPinchableView') : View;
