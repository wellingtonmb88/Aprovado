package com.wellingtonmb88.aprovado.custom;

import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

/**
 * Created by Wellington on 27/05/2015.
 */
public class FloatActionButtonHideShow {

    private static final int TRANSLATE_DURATION_MILLIS = 300;
    private final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();
    private View mView;
    private boolean mVisible;

    public FloatActionButtonHideShow(View view){
        mView = view;
        mVisible = true;
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
            int height = mView.getHeight();
            if (height == 0 && !force) {
                ViewTreeObserver vto = mView.getViewTreeObserver();
                if (vto.isAlive()) {
                    vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            ViewTreeObserver currentVto = mView.getViewTreeObserver();
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
            int translationY = visible ? 0 : height + mView.getBottom();
            if (animate) {
                ViewPropertyAnimator.animate(mView).setInterpolator(mInterpolator)
                        .setDuration(TRANSLATE_DURATION_MILLIS)
                        .translationY(translationY);
            } else {
                ViewHelper.setTranslationY(mView, translationY);
            }

        }
    }

}
