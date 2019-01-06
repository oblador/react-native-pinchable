//
//  RNPinchableView.m
//  RNPinchable
//
//  Created by Joel Arvidsson on 2019-01-06.
//  Copyright © 2019 Joel Arvidsson. All rights reserved.
//

#import "RNPinchableView.h"

@implementation RNPinchableView

BOOL isActive = NO;
CGFloat lastScale;
NSUInteger lastNumberOfTouches;
UIView *initialSuperView;
NSUInteger initialIndex;
CGRect initialFrame;
CGPoint initialTouchPoint;
UIView *backgroundView;

- (instancetype)initWithFrame:(CGRect)frame
{
  if ((self = [super initWithFrame:frame])) {
    _minimumZoomScale = 1.0;
    _maximumZoomScale = 3.0;
    [self resetGestureState];
    [self setupGesture];
  }
  return self;
}

- (void)resetGestureState
{
  isActive = NO;
  lastScale = 1.;
  lastNumberOfTouches = 0;
  initialSuperView = nil;
  initialIndex = -1;
  initialFrame = CGRectZero;
  initialTouchPoint = CGPointZero;
  backgroundView = nil;
}

- (void)setupGesture
{
  UIPinchGestureRecognizer *gestureRecognizer = [[UIPinchGestureRecognizer alloc] initWithTarget:self action:@selector(handlePinchGesture:)];
  gestureRecognizer.delegate = self;
  [self addGestureRecognizer:gestureRecognizer];
}

- (BOOL)gestureRecognizerShouldBegin:(UIGestureRecognizer *)gestureRecognizer
{
  return !isActive;
}

- (void)handlePinchGesture:(UIPinchGestureRecognizer *)gestureRecognizer
{
  UIView *view = gestureRecognizer.view;
  if (gestureRecognizer.state == UIGestureRecognizerStateBegan) {
    UIWindow *window = UIApplication.sharedApplication.keyWindow;
    lastNumberOfTouches = gestureRecognizer.numberOfTouches;
    initialFrame = view.frame;
    initialTouchPoint = [gestureRecognizer locationInView:view];
    CGPoint absoluteOrigin = [view.superview convertPoint:view.frame.origin toView:window];
    CGPoint anchorPoint = CGPointMake(initialTouchPoint.x/initialFrame.size.width, initialTouchPoint.y/initialFrame.size.height);
    isActive = YES;
    initialSuperView = view.superview;
    initialIndex = [initialSuperView.subviews indexOfObject:view];
    
    backgroundView = [UIView new];
    backgroundView.backgroundColor = UIColor.blackColor;
    backgroundView.frame = window.frame;
    [window addSubview:backgroundView];
    [window addSubview:view];
    
    view.layer.anchorPoint = anchorPoint;
    view.center = initialTouchPoint;
    view.frame = CGRectMake(absoluteOrigin.x, absoluteOrigin.y, initialFrame.size.width, initialFrame.size.height);
    [initialSuperView setNeedsLayout];
    [view setNeedsLayout];
  }
  
  if (gestureRecognizer.state == UIGestureRecognizerStateBegan ||
      gestureRecognizer.state == UIGestureRecognizerStateChanged) {
    
    CGFloat currentScale = [[gestureRecognizer.view.layer valueForKeyPath:@"transform.scale"] floatValue];
    CGPoint currentTouchPoint = [gestureRecognizer locationInView:view];
    
    CGFloat deltaScale = 1 - (lastScale - gestureRecognizer.scale);
    deltaScale = MIN(deltaScale, _maximumZoomScale / currentScale);
    deltaScale = MAX(deltaScale, _minimumZoomScale / currentScale);
    
    CGFloat deltaX = currentTouchPoint.x - initialTouchPoint.x;
    CGFloat deltaY = currentTouchPoint.y - initialTouchPoint.y;

    if (lastNumberOfTouches != gestureRecognizer.numberOfTouches) {
      lastNumberOfTouches = gestureRecognizer.numberOfTouches;
      initialTouchPoint = CGPointMake(initialTouchPoint.x + deltaX, initialTouchPoint.y + deltaY);
      deltaX = 0;
      deltaY = 0;
    }

    CGAffineTransform transform = gestureRecognizer.view.transform;
    transform = CGAffineTransformScale(transform, deltaScale, deltaScale);
    transform = CGAffineTransformTranslate(transform, deltaX, deltaY);
    
    view.transform = transform;

    lastScale = gestureRecognizer.scale;
    backgroundView.layer.opacity = MIN(lastScale - 1., .7);
  }

  if (gestureRecognizer.state == UIGestureRecognizerStateEnded ||
      gestureRecognizer.state == UIGestureRecognizerStateCancelled) {
    [UIView animateWithDuration:0.4 delay:0. usingSpringWithDamping:1 initialSpringVelocity:.6 options:0 animations:^{
      gestureRecognizer.view.transform = CGAffineTransformIdentity;
      backgroundView.layer.opacity = 0.;
    } completion:^(BOOL finished) {
      [backgroundView removeFromSuperview];
      [initialSuperView insertSubview:view atIndex:initialIndex];
      view.frame = initialFrame;
      [self resetGestureState];
    }];
  }
}

@end
