package com.oblador.pinchable;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;

import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.bridge.ReactApplicationContext;

import java.util.Objects;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
public class PinchableViewManager extends ViewGroupManager<PinchableView> {
    private static final String REACT_CLASS = "PinchableView";
    private static final float defaultMinimumZoomScale = 1.0f;
    private static final float defaultMaximumZoomScale = 3.0f;

    ReactApplicationContext mCallerContext;

    public PinchableViewManager(ReactApplicationContext reactContext) {
        mCallerContext = reactContext;
    }

    @Override
    @NonNull
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    public @Nonnull PinchableView createViewInstance(@Nonnull ThemedReactContext ctx) {
        PinchableView view = new PinchableView(ctx);
        return view;
    }

    @ReactProp(name = "minimumZoomScale", defaultFloat = defaultMinimumZoomScale)
    public void setMinimumZoomScale(PinchableView view, float minimumZoomScale) {
        view.setMinimumZoomScale(minimumZoomScale);
    }

    @ReactProp(name = "maximumZoomScale", defaultFloat = defaultMaximumZoomScale)
    public void setMaximumZoomScale(PinchableView view, float maximumZoomScale) {
        view.setMaximumZoomScale(maximumZoomScale);
    }
}
