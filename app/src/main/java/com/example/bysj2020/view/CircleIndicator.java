package com.example.bysj2020.view;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;

import androidx.annotation.AnimatorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.example.bysj2020.R;

/**
 * Author : wxz
 * Time   : 2020/02/18
 * Desc   : 指示器
 */
public class CircleIndicator extends LinearLayout {

    private final static int DEFAULT_INDICATOR_WIDTH = 5;
    private ViewPager mViewPager;
    private int mIndicatorMargin = -1;
    private int mIndicatorWidth = -1;
    private int mIndicatorHeight = -1;
    private int mAnimatorResId = R.animator.scale_with_alpha;
    private int mAnimatorReverseResId = 0;
    private int mIndicatorBackgroundResId = R.drawable.indicator_radius;
    private int mIndicatorUnselectedBackgroundResId = R.drawable.indicator_radius;
    private Animator mAnimatorOut;
    private Animator mAnimatorIn;
    private Animator mImmediateAnimatorOut;
    private Animator mImmediateAnimatorIn;

    private int mLastPosition = -1;

    public CircleIndicator(Context context) {
        super(context);
        init(context, null);
    }

    public CircleIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircleIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        handleTypedArray(context, attrs);
        checkIndicatorConfig(context);
        setClipToPadding(false);
        setClipChildren(false);
    }

    private void handleTypedArray(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleIndicator);
        mIndicatorWidth = typedArray.getDimensionPixelSize(R.styleable.CircleIndicator_ci_width, -1);
        mIndicatorHeight = typedArray.getDimensionPixelSize(R.styleable.CircleIndicator_ci_height, -1);
        mIndicatorMargin = typedArray.getDimensionPixelSize(R.styleable.CircleIndicator_ci_margin, -1);
        mAnimatorResId = typedArray.getResourceId(R.styleable.CircleIndicator_ci_animator, R.animator.scale_with_alpha);
        mAnimatorReverseResId = typedArray.getResourceId(R.styleable.CircleIndicator_ci_animator_reverse, 0);
        mIndicatorBackgroundResId = typedArray.getResourceId(R.styleable.CircleIndicator_ci_drawable_selected, R.drawable.indicator_radius);
        mIndicatorUnselectedBackgroundResId = typedArray.getResourceId(R.styleable.CircleIndicator_ci_drawable_unselected, mIndicatorBackgroundResId);
        int orientation = typedArray.getInt(R.styleable.CircleIndicator_orientation, -1);
        setOrientation(orientation == VERTICAL ? VERTICAL : HORIZONTAL);

        int gravity = typedArray.getInt(R.styleable.CircleIndicator_gravity, -1);
        setGravity(gravity >= 0 ? gravity : Gravity.CENTER);

        typedArray.recycle();
    }

    /**
     * Create and configure Indicator in Java code.
     */
    public void configureIndicator(int indicatorWidth, int indicatorHeight, int indicatorMargin) {
        configureIndicator(indicatorWidth, indicatorHeight, indicatorMargin, R.animator.scale_with_alpha, 0, R.drawable.indicator_radius, R.drawable.indicator_radius);
    }

    private void configureIndicator(int indicatorWidth, int indicatorHeight, int indicatorMargin,
                                    @AnimatorRes int animatorId, @AnimatorRes int animatorReverseId,
                                    @DrawableRes int indicatorBackgroundId, @DrawableRes int indicatorUnselectedBackgroundId) {
        mIndicatorWidth = indicatorWidth;
        mIndicatorHeight = indicatorHeight;
        mIndicatorMargin = indicatorMargin;
        mAnimatorResId = animatorId;
        mAnimatorReverseResId = animatorReverseId;
        mIndicatorBackgroundResId = indicatorBackgroundId;
        mIndicatorUnselectedBackgroundResId = indicatorUnselectedBackgroundId;

        checkIndicatorConfig(getContext());
    }

    private void checkIndicatorConfig(Context context) {
        mAnimatorResId = (mAnimatorResId == 0) ? R.animator.scale_with_alpha : mAnimatorResId;

        mAnimatorOut = createAnimatorOut(context);
        mImmediateAnimatorOut = createAnimatorOut(context);
        mImmediateAnimatorOut.setDuration(0);

        mAnimatorIn = createAnimatorIn(context);
        mImmediateAnimatorIn = createAnimatorIn(context);
        mImmediateAnimatorIn.setDuration(0);

        mIndicatorBackgroundResId = (mIndicatorBackgroundResId == 0) ? R.drawable.indicator_radius : mIndicatorBackgroundResId;
        mIndicatorUnselectedBackgroundResId = (mIndicatorUnselectedBackgroundResId == 0) ? mIndicatorBackgroundResId : mIndicatorUnselectedBackgroundResId;
    }

    private Animator createAnimatorOut(Context context) {
        return AnimatorInflater.loadAnimator(context, mAnimatorResId);
    }

    private Animator createAnimatorIn(Context context) {
        Animator animatorIn;
        if (mAnimatorReverseResId == 0) {
            animatorIn = AnimatorInflater.loadAnimator(context, mAnimatorResId);
            animatorIn.setInterpolator(new ReverseInterpolator());
        } else {
            animatorIn = AnimatorInflater.loadAnimator(context, mAnimatorReverseResId);
        }
        return animatorIn;
    }

    public void setmViewPager(ViewPager viewPager) {
        mViewPager = viewPager;
        if (mViewPager != null && mViewPager.getAdapter() != null) {
            mLastPosition = -1;
            createIndicators();
            mViewPager.setOnPageChangeListener(mInternalPageChangeListener);
            mInternalPageChangeListener.onPageSelected(mViewPager.getCurrentItem());
        }
    }

    private final ViewPager.OnPageChangeListener mInternalPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (mViewPager.getAdapter() == null || mViewPager.getAdapter().getCount() <= 0) {
                return;
            }
            if (mAnimatorIn.isRunning()) {
                mAnimatorIn.end();
                mAnimatorIn.cancel();
            }
            if (mAnimatorOut.isRunning()) {
                mAnimatorOut.end();
                mAnimatorOut.cancel();
            }

            View currentIndicator;
            if (mLastPosition >= 0 && (currentIndicator = getChildAt(mLastPosition)) != null) {
                currentIndicator.setBackgroundResource(mIndicatorUnselectedBackgroundResId);
                mAnimatorIn.setTarget(currentIndicator);
                mAnimatorIn.start();
            }

            View selectedIndicator = getChildAt(position);
            if (selectedIndicator != null) {
                selectedIndicator.setBackgroundResource(mIndicatorBackgroundResId);
                mAnimatorOut.setTarget(selectedIndicator);
                mAnimatorOut.start();
            }
            mLastPosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public DataSetObserver getDataSetObserver() {
        return mInternalDataSetObserver;
    }

    private DataSetObserver mInternalDataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            if (mViewPager == null) {
                return;
            }
            int newCount = mViewPager.getAdapter().getCount();
            int currentCount = getChildCount();
            if (newCount == currentCount) {
                return;
            } else if (mLastPosition < newCount) {
                mLastPosition = mViewPager.getCurrentItem();
            } else {
                mLastPosition = -1;
            }
            createIndicators();
        }
    };

    /**
     * @deprecated User ViewPager addOnPageChangeListener
     */
    @Deprecated
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        if (mViewPager == null) {
            throw new NullPointerException("can not find ViewPager , setViewPager first");
        }
        mViewPager.setOnPageChangeListener(onPageChangeListener);
    }

    public void createIndicators() {
        removeAllViews();
        int count = mViewPager.getAdapter().getCount();
        if (count <= 0) {
            return;
        }
        int currentItem = mViewPager.getCurrentItem();

        for (int i = 0; i < count; i++) {
            if (currentItem == i) {
                addIndicator(mIndicatorBackgroundResId, mImmediateAnimatorOut);
            } else {
                addIndicator(mIndicatorUnselectedBackgroundResId, mImmediateAnimatorIn);
            }
        }
    }

    private void addIndicator(@DrawableRes int backgroundDrawableId, Animator animator) {
        if (animator.isRunning()) {
            animator.end();
            animator.cancel();
        }

        View Indicator = new View(getContext());
        Indicator.setBackgroundResource(backgroundDrawableId);
        addView(Indicator, mIndicatorWidth, mIndicatorHeight);
        LayoutParams lp = (LayoutParams) Indicator.getLayoutParams();
        lp.leftMargin = mIndicatorMargin;
        lp.rightMargin = mIndicatorMargin;
        lp.topMargin = mIndicatorMargin;
        lp.bottomMargin = mIndicatorMargin;
        Indicator.setLayoutParams(lp);

        animator.setTarget(Indicator);
        animator.start();
    }

    private class ReverseInterpolator implements Interpolator {
        @Override
        public float getInterpolation(float value) {
            return Math.abs(1.0f - value);
        }
    }
}

