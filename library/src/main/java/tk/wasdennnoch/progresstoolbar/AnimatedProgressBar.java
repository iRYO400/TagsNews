package tk.wasdennnoch.progresstoolbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

class AnimatedProgressBar extends MaterialProgressBar {

    private static final Interpolator FAST_OUT_LINEAR_IN = new FastOutLinearInInterpolator();
    private static final Interpolator LINEAR_OUT_SLOW_IN = new LinearOutSlowInInterpolator();

    public AnimatedProgressBar(Context context) {
        super(context);
    }

    public AnimatedProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimatedProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AnimatedProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        final Toolbar.LayoutParams lp = (Toolbar.LayoutParams) getLayoutParams();
        setPivotY((lp.gravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.TOP ? 0 : getHeight());
    }

    void setVisibilityAnimated(int v) {
        if (getVisibility() == v) return;
        setVisibility(VISIBLE);
        final ViewPropertyAnimator animator = animate();
        animator.cancel();
        animator.setDuration(400L);
        animator.setInterpolator(FAST_OUT_LINEAR_IN);
        switch (v) {
            case VISIBLE:
                setAlpha(0);
                setScaleY(0);
                animator
                        .setDuration(300L)
                        .setInterpolator(LINEAR_OUT_SLOW_IN)
                        .alpha(1)
                        .scaleY(1)
                        .setListener(null);
                break;
            case INVISIBLE:
                setAlpha(1);
                setScaleY(1);
                animator.alpha(0).setListener(mInvisibleListener);
                break;
            case GONE:
                setAlpha(1);
                setScaleY(1);
                animator.alpha(0).scaleY(0).setListener(mGoneListener);
                break;
        }
        animator.start();
    }

    private final AnimatorListenerAdapter mInvisibleListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            setVisibility(INVISIBLE);
        }
    };

    private final AnimatorListenerAdapter mGoneListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            setVisibility(GONE);
        }
    };

}
