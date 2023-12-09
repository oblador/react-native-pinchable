package com.oblador.pinchable;

import java.lang.Math;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Bitmap;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.animation.DecelerateInterpolator;

import com.facebook.react.views.view.ReactViewGroup;

public class PinchableView extends ReactViewGroup implements OnTouchListener {
    private final int animationDuration = 400;
    private float minScale = 1f;
    private float maxScale = 3f;
    private boolean active = false;
    private int sourceLocation[] = new int[2];
    private int sourceDimensions[] = new int[2];
    private PointF initialTouchPoint = new PointF();
    private float initialSpacing = 0f;
    private PointF translation = new PointF();
    private float scale = 1;
    private ValueAnimator currentAnimator = null;
    private ColorDrawable backdrop = null;
    private BitmapDrawable clone = null;

    public PinchableView(Context context) {
        super(context);
        this.setOnTouchListener(this);
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // Block touch events on children
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_POINTER_DOWN:
                if (!active && event.getPointerCount() >= 2) {
                    startGesture(v, event);
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                if (active && event.getPointerCount() < 2) {
                    endGesture(event);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (active) {
                    if (event.getPointerCount() < 2) {
                        endGesture(event);
                    } else {
                        moveGesture(event);
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (active) {
                    endGesture(event);
                }
                break;
            default:
                break;
        }

        return true;
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private void midPoint(PointF point, MotionEvent event)  {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    private void setCloneTransforms(float translateX, float translateY, float scale) {
        int x = (int) (sourceLocation[0] + translateX - sourceDimensions[0] * (scale - 1) / 2);
        int y = (int) (sourceLocation[1] + translateY - sourceDimensions[1] * (scale - 1) / 2);
        int width = (int) (sourceDimensions[0] * scale);
        int height = (int) (sourceDimensions[1] * scale);
        clone.setBounds(x, y, x + width, y + height);
        backdrop.setAlpha((int) (255 * Math.min(scale - 1, 0.7)));
    }

    private void startGesture(View v, MotionEvent event) {
        if (currentAnimator != null) {
            currentAnimator.cancel();
        }
        View rootView = this.getRootView();
        if (clone != null) {
            rootView.getOverlay().remove(clone);
            clone = null;
        }
        
        Bitmap snapshot = null;
        try {
            snapshot = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(snapshot);
            v.draw(canvas);
            clone = new BitmapDrawable(this.getContext().getResources(), snapshot);
        } catch(Exception ex) {
            ex.printStackTrace();
            return;
        }

        active = true;

        if (backdrop == null) {
            backdrop = new ColorDrawable(Color.BLACK);
            backdrop.setAlpha(0);
            backdrop.setBounds(0, 0, rootView.getWidth(), rootView.getHeight());
            rootView.getOverlay().add(backdrop);
        }

        v.getLocationInWindow(sourceLocation);
        sourceDimensions[0] = v.getWidth();
        sourceDimensions[1] = v.getHeight();
        setCloneTransforms(0, 0, 1);
        rootView.getOverlay().add(clone);
        setVisibility(View.INVISIBLE);
        midPoint(initialTouchPoint, event);
        initialSpacing = spacing(event);
        translation.set(0, 0);
        scale = 1;
        this.getParent().requestDisallowInterceptTouchEvent(true);
    }

    private void moveGesture(MotionEvent event) {
        PointF current = new PointF();
        midPoint(current, event);
        float deltaX = current.x - initialTouchPoint.x;
        float deltaY = current.y - initialTouchPoint.y;
        scale = Math.min(maxScale, Math.max(minScale, spacing(event) / initialSpacing));
        setCloneTransforms(deltaX, deltaY, scale);
        translation.set(deltaX, deltaY);
    }

    private void endGesture(MotionEvent event) {
        active = false;
        this.getParent().requestDisallowInterceptTouchEvent(false);
        if (currentAnimator != null) {
            currentAnimator.cancel();
        }

        ValueAnimator animator = ValueAnimator.ofFloat(1, 0);
        animator.setDuration(animationDuration);
        animator.setInterpolator(new DecelerateInterpolator(1.5f));
        animator.addUpdateListener(updatedAnimation -> {
            float animatedValue = (float)updatedAnimation.getAnimatedValue();
            setCloneTransforms(translation.x * animatedValue, translation.y * animatedValue, 1 + (scale - 1) * animatedValue);
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                endAnimation();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                endAnimation();
            }
        });
        animator.start();
        currentAnimator = animator;
    }

    private void endAnimation() {
        currentAnimator = null;
        View rootView = this.getRootView();
        if (backdrop != null) {
            rootView.getOverlay().remove(backdrop);
            backdrop = null;
        }
        if (clone != null) {
            rootView.getOverlay().remove(clone);
            clone = null;
        }
        setVisibility(View.VISIBLE);
    }

    public void setMinimumZoomScale(float minimumZoomScale) {
        minScale = minimumZoomScale;
    }

    public void setMaximumZoomScale(float maximumZoomScale) {
        maxScale = maximumZoomScale;
    }
}
