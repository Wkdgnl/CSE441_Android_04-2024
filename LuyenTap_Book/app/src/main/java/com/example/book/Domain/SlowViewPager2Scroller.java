package com.example.book.Domain;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.animation.Interpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.recyclerview.widget.LinearSmoothScroller;

public class SlowViewPager2Scroller extends LinearSmoothScroller {

    private static final float MILLISECONDS_PER_INCH = 4000f;

    public SlowViewPager2Scroller(Context context) {
        super(context);
    }

    @Override
    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
        return MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
    }
}
