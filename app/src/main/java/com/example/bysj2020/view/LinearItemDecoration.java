package com.example.bysj2020.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 自定义线性ItemDecoration
 */
public class LinearItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDivider;
    private boolean mShowLastLine;
    private int mSpanSpace = 1;
    private int mLeftPadding;
    private int mRightPadding;

    public LinearItemDecoration(int span, int leftPadding, int rightPadding, int color, boolean show) {
        this.mSpanSpace = span;
        this.mShowLastLine = show;
        this.mLeftPadding = leftPadding;
        this.mRightPadding = rightPadding;
        this.mDivider = new ColorDrawable(color);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int count = mShowLastLine ? parent.getAdapter().getItemCount() : parent.getAdapter().getItemCount() - 1;
        if (isVertical(parent)) {
            if (parent.getChildAdapterPosition(view) < count) {
                outRect.set(0, 0, 0, mSpanSpace);
            } else {
                outRect.set(0, 0, 0, 0);
            }
        } else {
            if (parent.getChildAdapterPosition(view) < count) {
                outRect.set(0, 0, mSpanSpace, 0);
            } else {
                outRect.set(0, 0, 0, 0);
            }
        }
    }

    /**
     * item是否是垂直排列
     *
     * @param parent
     * @return
     */
    private boolean isVertical(RecyclerView parent) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            int orientation = ((LinearLayoutManager) layoutManager).getOrientation();
            return orientation == LinearLayoutManager.VERTICAL;
        }
        return false;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (isVertical(parent)) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    /**
     * 画水平排列分割线
     *
     * @param c
     * @param parent
     */
    private void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + params.rightMargin + Math.round(ViewCompat.getTranslationX(child));
            final int right = left + mSpanSpace;
            int count = mShowLastLine ? parent.getAdapter().getItemCount() : parent.getAdapter().getItemCount() - 1;
            if (i < count) {
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }

    /**
     * 画垂直排列分割线
     *
     * @param c
     * @param parent
     */
    private void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft() + mLeftPadding;
        final int right = parent.getWidth() - parent.getPaddingRight() - mRightPadding;
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin + Math.round(ViewCompat.getTranslationY(child));
            final int bottom = top + mSpanSpace;
            int count = mShowLastLine ? parent.getAdapter().getItemCount() : parent.getAdapter().getItemCount() - 1;
            if (i < count) {
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            } else {
                mDivider.setBounds(left, top, right, top);
                mDivider.draw(c);
            }
        }
    }

    /**
     * builder模式
     */
    public static class Builder {
        private Context mContext;
        private Resources mResources;
        private int mSpanSpace;
        private boolean mShowLastLine;
        private int mLeftPadding;
        private int mRightPadding;
        private int mColor;

        public Builder(Context context) {
            this.mContext = context;
            mResources = context.getResources();
            mSpanSpace = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PT, 1f, context.getResources().getDisplayMetrics());
            mLeftPadding = 0;
            mRightPadding = 0;
            mShowLastLine = false;
            mColor = Color.BLACK;
        }

        /**
         * 是否最后一条显示分割线
         *
         * @param show
         * @return
         */
        public Builder setmShowLastLine(boolean show) {
            this.mShowLastLine = show;
            return this;
        }

        /**
         * 设置分割线宽（高）度
         *
         * @param pixels
         * @return
         */
        public Builder setSpan(float pixels) {
            mSpanSpace = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, pixels, mResources.getDisplayMetrics());
            return this;
        }

        /**
         * 设置分割线宽（高）度
         *
         * @param resource
         * @return
         */
        public Builder setSpan(@DimenRes int resource) {
            mSpanSpace = mResources.getDimensionPixelSize(resource);
            return this;
        }

        /**
         * 设置左右间距
         *
         * @param pixels
         * @return
         */
        public Builder setPadding(float pixels) {
            setLeftPadding(pixels);
            setRightPadding(pixels);
            return this;
        }

        /**
         * 设置左右间距
         *
         * @param resource
         * @return
         */
        public Builder setPadding(@DimenRes int resource) {
            setLeftPadding(resource);
            setRightPadding(resource);
            return this;
        }

        /**
         * 设置左间距
         *
         * @param pixelPadding
         * @return
         */
        public Builder setLeftPadding(float pixelPadding) {
            mLeftPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, pixelPadding, mResources.getDisplayMetrics());
            return this;
        }

        /**
         * 设置右间距
         *
         * @param pixelPadding
         * @return
         */
        public Builder setRightPadding(float pixelPadding) {
            mRightPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, pixelPadding, mResources.getDisplayMetrics());
            return this;
        }

        /**
         * 通过资源id设置左间距
         *
         * @param resource
         * @return
         */
        public Builder setLeftPadding(@DimenRes int resource) {
            mLeftPadding = mResources.getDimensionPixelSize(resource);
            return this;
        }

        /**
         * 通过资源id设置左间距
         *
         * @param resource
         * @return
         */
        public Builder setRightPadding(@DimenRes int resource) {
            mRightPadding = mResources.getDimensionPixelSize(resource);
            return this;
        }

        /**
         * 通过资源id设置颜色
         *
         * @param resource
         * @return
         */
        @SuppressLint("ResourceType")
        public Builder setColorResource(@ColorRes int resource) {
            setColor(ContextCompat.getColor(mContext, resource));
            return this;
        }

        /**
         * 设置颜色
         *
         * @param color
         * @return
         */
        public Builder setColor(@ColorRes int color) {
            mColor = color;
            return this;
        }

        public LinearItemDecoration build() {
            return new LinearItemDecoration(mSpanSpace, mLeftPadding, mRightPadding, mColor, mShowLastLine);
        }
    }
}
