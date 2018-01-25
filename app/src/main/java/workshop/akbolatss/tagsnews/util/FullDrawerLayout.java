package workshop.akbolatss.tagsnews.util;

import android.content.Context;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

public class FullDrawerLayout extends AdvancedDrawerLayout {

    private List<View> childViews;
    private static final int MIN_DRAWER_MARGIN = 0; // dp

    public FullDrawerLayout(Context context) {
        this(context, null);
    }

    public FullDrawerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FullDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        final int childCount = getChildCount();
        childViews = new ArrayList<>();
        for (int i = 1; i < childCount; i++) {
            childViews.add(getChildAt(i));
        }
    }

    @Override
    public boolean isDrawerOpen(View drawer) {
        return super.isDrawerOpen(drawer);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode != MeasureSpec.EXACTLY || heightMode != MeasureSpec.EXACTLY) {
            throw new IllegalArgumentException(
                    "AdvancedDrawerLayout must be measured with MeasureSpec.EXACTLY.");
        }

        setMeasuredDimension(widthSize, heightSize);

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getLayoutParams();
        params.topMargin = getStatusBarHeight();
        setLayoutParams(params);

        // Gravity value for each drawer we've seen. Only one of each permitted.
//        int foundDrawers = 0;
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);

            if (child.getVisibility() == GONE) {
                continue;
            }

            final LayoutParams lp = (LayoutParams) child.getLayoutParams();

            if (isContentView(child)) {
                // Content views get measured at exactly the layout's size.
                final int contentWidthSpec = MeasureSpec.makeMeasureSpec(
                        widthSize - lp.leftMargin - lp.rightMargin, MeasureSpec.EXACTLY);
                final int contentHeightSpec = MeasureSpec.makeMeasureSpec(
                        heightSize - lp.topMargin - lp.bottomMargin, MeasureSpec.EXACTLY);
                child.measure(contentWidthSpec, contentHeightSpec);
            } else if (isDrawerView(child)) {
                final int childGravity =
                        getDrawerViewGravity(child) & Gravity.HORIZONTAL_GRAVITY_MASK;
//                if ((foundDrawers & childGravity) != 0) {
//                    throw new IllegalStateException("Child drawer has absolute gravity " +
//                            gravityToString(childGravity) + " but this already has a " +
//                            "drawer view along that edge");
//                }
                final int drawerWidthSpec = getChildMeasureSpec(widthMeasureSpec,
                        MIN_DRAWER_MARGIN + lp.leftMargin + lp.rightMargin,
                        lp.width);
                final int drawerHeightSpec = getChildMeasureSpec(heightMeasureSpec,
                        lp.topMargin + lp.bottomMargin,
                        lp.height);
                child.measure(drawerWidthSpec, drawerHeightSpec);
            } else {
                throw new IllegalStateException("Child " + child + " at index " + i +
                        " does not have a valid layout_gravity - must be Gravity.LEFT, " +
                        "Gravity.RIGHT or Gravity.NO_GRAVITY");
            }
        }
    }

    boolean isContentView(View child) {
        return ((LayoutParams) child.getLayoutParams()).gravity == Gravity.NO_GRAVITY;
    }

    boolean isDrawerView(View child) {
        final int gravity = ((LayoutParams) child.getLayoutParams()).gravity;
        final int absGravity = Gravity.getAbsoluteGravity(gravity,
                child.getLayoutDirection());
        return (absGravity & (Gravity.LEFT | Gravity.RIGHT)) != 0;
    }

    int getDrawerViewGravity(View drawerView) {
        final int gravity = ((LayoutParams) drawerView.getLayoutParams()).gravity;
        return Gravity.getAbsoluteGravity(gravity, drawerView.getLayoutDirection());
    }

    static String gravityToString(int gravity) {
        if ((gravity & Gravity.LEFT) == Gravity.LEFT) {
            return "LEFT";
        }
        if ((gravity & Gravity.RIGHT) == Gravity.RIGHT) {
            return "RIGHT";
        }
        return Integer.toHexString(gravity);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

//    private class DragHelperCallback extends ViewDragHelper.Callback {
//        @Override
//        public boolean tryCaptureView(View child, int pointerId) {
////            if (mDragCapture) {
////                return child == mDragView1;
////            }
//            return true;
//        }
//
//        @Override
//        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
//            invalidate();
//        }
//
//        @Override
//        public void onViewCaptured(View capturedChild, int activePointerId) {
//            super.onViewCaptured(capturedChild, activePointerId);
//        }
//
//        @Override
//        public void onViewReleased(View releasedChild, float xvel, float yvel) {
//            super.onViewReleased(releasedChild, xvel, yvel);
//        }
//
//        @Override
//        public void onEdgeTouched(int edgeFlags, int pointerId) {
//            super.onEdgeTouched(edgeFlags, pointerId);
//        }
//
//        @Override
//        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
////            if (mDragEdge) {
////                mDragHelper.captureChildView(mDragView1, pointerId);
////            }
//        }
//
//        @Override
//        public int clampViewPositionHorizontal(View child, int left, int dx) {
////            if (mDragHorizontal || mDragCapture || mDragEdge) {
////                final int leftBound = getPaddingLeft();
////                final int rightBound = getWidth() - mDragView1.getWidth();
////
////                final int newLeft = Math.min(Math.max(left, leftBound), rightBound);
////
////                return newLeft;
////            }
//            return super.clampViewPositionHorizontal(child, left, dx);
//        }
//    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_CANCEL || ev.getAction() == MotionEvent.ACTION_UP) {
//            mDragHelper.cancel();
//            return false;
//        }
//        return mDragHelper.shouldInterceptTouchEvent(ev);
//    }

//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        mDragHelper.processTouchEvent(ev);
//        return true;
//    }
}