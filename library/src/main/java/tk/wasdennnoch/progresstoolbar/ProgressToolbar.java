/*
 * Copyright (C) 2016 MrWasdennnoch
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tk.wasdennnoch.progresstoolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;

/**
 * A subclass of {@link Toolbar} which allows a {@link ProgressBar} to be displayed
 * inside of it, either at the top or the bottom. It automatically uses a
 * material-styled ProgressBar to persist the same look across various Android versions.
 * It also forwards methods to directly control the most important values of a
 * ProgressBar such as the max value, current value, indeterminate state and visibility.
 * Aditionally a direct reference to the ProgressBa can be aqured using {@link #getProgressBar()}.
 */
public class ProgressToolbar extends Toolbar {

    private int mGravity = GravityCompat.START | Gravity.CENTER_VERTICAL;
    private final AnimatedProgressBar mProgressBar;

    public ProgressToolbar(Context context) {
        this(context, null);
    }

    public ProgressToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.toolbarStyle);
    }

    @SuppressLint("PrivateResource")
    public ProgressToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final TypedArray toolbarAttrs = context.obtainStyledAttributes(attrs,
                android.support.v7.appcompat.R.styleable.Toolbar, defStyleAttr, 0);
        mGravity = toolbarAttrs.getInteger(android.support.v7.appcompat.R.styleable.Toolbar_android_gravity, mGravity);
        toolbarAttrs.recycle();

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ProgressToolbar);
        final boolean atTop = a.getBoolean(R.styleable.ProgressToolbar_ptb_progressAtTop, false);
        final int progHeight = a.getDimensionPixelSize(R.styleable.ProgressToolbar_ptb_progressHeight, -1);
        final boolean indeterminate = a.getBoolean(R.styleable.ProgressToolbar_android_indeterminate, false);
        final int max = a.getInt(R.styleable.ProgressToolbar_android_max, 100);
        final int progress = a.getInt(R.styleable.ProgressToolbar_android_progress, 0);
        final int secondaryProgress = a.getInt(R.styleable.ProgressToolbar_android_secondaryProgress, 0);

        final LayoutParams lp = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                progHeight != -1 ? progHeight : context.getResources().getDimensionPixelSize(R.dimen.toolbar_progress_height),
                Gravity.START | (atTop ? Gravity.TOP : Gravity.BOTTOM));
        mProgressBar = new AnimatedProgressBar(context, null, 0, R.style.ToolbarProgress);
        mProgressBar.setLayoutParams(lp);

        if (a.hasValue(R.styleable.ProgressToolbar_android_tint))
            mProgressBar.setProgressTintList(a.getColorStateList(R.styleable.ProgressToolbar_android_tint));

        mProgressBar.setIndeterminate(indeterminate);
        mProgressBar.setMax(max);
        mProgressBar.setProgress(progress);
        mProgressBar.setSecondaryProgress(secondaryProgress);
        addView(mProgressBar);

        a.recycle();
    }

    /**
     * @return The ProgressBar displayed in the Toolbar to allow further customization
     */
    @NonNull
    public ProgressBar getProgressBar() {
        return mProgressBar;
    }

    /**
     * @param atTop Whether the ProgressBar should be displayed at the top edge of
     *              the Toolbar or at the bottom edge
     */
    public void setProgressAtTop(boolean atTop) {
        final LayoutParams lp = (LayoutParams) mProgressBar.getLayoutParams();
        int desiredGravity = Gravity.START | (atTop ? Gravity.TOP : Gravity.BOTTOM);
        if (lp.gravity != desiredGravity) {
            lp.gravity = desiredGravity;
            mProgressBar.setLayoutParams(lp);
        }
    }

    /**
     * @return true if the ProgressBar is currently anchored to the top of the Toolbar
     */
    public boolean isProgressAtTop() {
        final LayoutParams lp = (LayoutParams) mProgressBar.getLayoutParams();
        return (lp.gravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.TOP;
    }

    /**
     * @param progressHeight The height of the ProgressBar in pixels
     */
    public void setProgressHeight(int progressHeight) {
        final LayoutParams lp = (LayoutParams) mProgressBar.getLayoutParams();
        if (lp.height != progressHeight) {
            lp.height = progressHeight;
            mProgressBar.setLayoutParams(lp);
        }
    }

    /**
     * @return The current height of the ProgressBar in pixels
     */
    public int getProgressHeight() {
        return mProgressBar.getLayoutParams().height;
    }

    /**
     * Applies a tint to the progress drawable.
     *
     * @param list The tint to apply, may be {@code null} to clear tint
     */
    public void setProgressTintList(@Nullable ColorStateList list) {
        mProgressBar.setProgressTintList(list);
    }

    /**
     * Return the tint applied to the progress drawable, if specified.
     *
     * @return The tint applied to the progress drawable
     */
    public ColorStateList getProgressTintList() {
        return mProgressBar.getProgressTintList();
    }

    /**
     * Hides the ProgressBar without animation. Same as calling {@code getProgressBar().setVisibility(GONE)}.
     */
    public void hideProgress() {
        hideProgress(false);
    }

    /**
     * Hides the ProgressBar.
     *
     * @param animate Whether to animate the visibility change by scaling the ProgressBar and
     *                modifying the alpha value
     */
    public void hideProgress(boolean animate) {
        if (animate)
            mProgressBar.setVisibilityAnimated(GONE);
        else
            mProgressBar.setVisibility(GONE);
    }

    /**
     * Shows the ProgressBar without animation. Same as calling {@code getProgressBar().setVisibility(VISIBLE)}.
     */
    public void showProgress() {
        showProgress(false);
    }

    /**
     * Shows the ProgressBar.
     *
     * @param animate Whether to animate the visibility change by scaling the ProgressBar and
     *                modifying the alpha value
     */
    public void showProgress(boolean animate) {
        if (animate)
            mProgressBar.setVisibilityAnimated(VISIBLE);
        else
            mProgressBar.setVisibility(VISIBLE);
    }

    public void setMax(int max) {
        mProgressBar.setMax(max);
    }

    public int getMax() {
        return mProgressBar.getMax();
    }

    public void setProgress(int progress) {
        mProgressBar.setProgress(progress);
    }

    public int getProgress() {
        return mProgressBar.getProgress();
    }

    public void setSecondaryProgress(int secondaryProgress) {
        mProgressBar.setSecondaryProgress(secondaryProgress);
    }

    public int getSecondaryProgress() {
        return mProgressBar.getSecondaryProgress();
    }

    public void setIndeterminate(boolean indeterminate) {
        mProgressBar.setIndeterminate(indeterminate);
    }

    public boolean isIndeterminate() {
        return mProgressBar.isIndeterminate();
    }


    /*
       Normally the Toolbar positions all custom children between the action buttons and the title,
       resizing the title if necessary. We have to intercept this behavior by removing the
       ProgressBar before the Toolbar measures its children and add it back afterwards to let
       our own measure method do the job. This also applies to the layout phase.
    */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        removeView(mProgressBar);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        addView(mProgressBar);
        measureChildCollapseMargins(mProgressBar, widthMeasureSpec, heightMeasureSpec);
    }

    private void measureChildCollapseMargins(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
                getPaddingLeft() + getPaddingRight(), lp.width);
        final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec,
                getPaddingTop() + getPaddingBottom() + lp.topMargin + lp.bottomMargin, lp.height);
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        removeView(mProgressBar);
        super.onLayout(changed, l, t, r, b);
        addView(mProgressBar);
        layoutChildLeft(mProgressBar, l);
    }

    private int layoutChildLeft(View child, int left) {
        final LayoutParams lp = (LayoutParams) child.getLayoutParams();
        final int l = lp.leftMargin;
        left += Math.max(0, l);
        final int top = getChildTop(child);
        final int childWidth = child.getMeasuredWidth();
        child.layout(left, top, left + childWidth, top + child.getMeasuredHeight());
        left += childWidth + lp.rightMargin;
        return left;
    }

    private int getChildTop(View child) {
        final LayoutParams lp = (LayoutParams) child.getLayoutParams();
        final int childHeight = child.getMeasuredHeight();
        switch (getChildVerticalGravity(lp.gravity)) {
            case Gravity.TOP:
                return getPaddingTop();
            case Gravity.BOTTOM:
                return getHeight() - getPaddingBottom() - childHeight - lp.bottomMargin;
            default:
            case Gravity.CENTER_VERTICAL:
                final int paddingTop = getPaddingTop();
                final int paddingBottom = getPaddingBottom();
                final int height = getHeight();
                final int space = height - paddingTop - paddingBottom;
                int spaceAbove = (space - childHeight) / 2;
                if (spaceAbove < lp.topMargin) {
                    spaceAbove = lp.topMargin;
                } else {
                    final int spaceBelow = height - paddingBottom - childHeight - spaceAbove - paddingTop;
                    if (spaceBelow < lp.bottomMargin) {
                        spaceAbove = Math.max(0, spaceAbove - (lp.bottomMargin - spaceBelow));
                    }
                }
                return paddingTop + spaceAbove;
        }
    }

    private int getChildVerticalGravity(int gravity) {
        final int vgrav = gravity & Gravity.VERTICAL_GRAVITY_MASK;
        switch (vgrav) {
            case Gravity.TOP:
            case Gravity.BOTTOM:
            case Gravity.CENTER_VERTICAL:
                return vgrav;
            default:
                return mGravity & Gravity.VERTICAL_GRAVITY_MASK;
        }
    }

}
