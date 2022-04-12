import { HostComponent, ViewProps } from 'react-native'
interface PinchableViewProps  extends ViewProps {
  /** The minimum allowed zoom scale
   * @default 1
   **/
  minimumZoomScale?: number;
  /** The maximum allowed zoom scale
   * @default 3
   **/
  maximumZoomScale?: number;
}

/**
 * Instagram like pinch to zoom for React Native
 * @limitations On Android it's not possible to receive touch events on the views inside the Pinchable component.
 */
declare const PinchableView: HostComponent<PinchableViewProps>;
export default PinchableView;
