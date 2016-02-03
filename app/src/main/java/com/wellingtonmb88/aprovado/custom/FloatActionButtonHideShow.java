package com.wellingtonmb88.aprovado.custom;

import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.lang.ref.WeakReference;

public class FloatActionButtonHideShow {

    private static final int TRANSLATE_DURATION_MILLIS = 300;
    private final Interpolator mInterpolator;
    private final WeakReference<View> mView;
    private boolean mVisible;

    public FloatActionButtonHideShow(View view) {
        mView = new WeakReference<>(view);
        mVisible = true;
        mInterpolator = new AccelerateDecelerateInterpolator();
    }

    public void show() {
        show(true);
    }

    public void hide() {
        hide(true);
    }

    public void show(boolean animate) {
        toggle(true, animate, false);
    }

    public void hide(boolean animate) {
        toggle(false, animate, false);
    }

    private void toggle(final boolean visible, final boolean animate, boolean force) {
        if (mVisible != visible || force) {
            mVisible = visible;
            final View viewRef = mView.get();
            if (viewRef != null) {
                int height = viewRef.getHeight();
                if (height == 0 && !force) {
                    ViewTreeObserver vto = viewRef.getViewTreeObserver();
                    if (vto.isAlive()) {
                        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                            @Override
                            public boolean onPreDraw() {
                                ViewTreeObserver currentVto = viewRef.getViewTreeObserver();
                                if (currentVto.isAlive()) {
                                    currentVto.removeOnPreDrawListener(this);
                                }
                                toggle(visible, animate, true);
                                return true;
                            }
                        });
                        return;
                    }
                }
                int translationY = visible ? 0 : height + viewRef.getBottom();
                if (animate) {
                    ViewPropertyAnimator.animate(viewRef).setInterpolator(mInterpolator)
                            .setDuration(TRANSLATE_DURATION_MILLIS)
                            .translationY(translationY);
                } else {
                    ViewHelper.setTranslationY(viewRef, translationY);
                }
            }

        }
    }

}
