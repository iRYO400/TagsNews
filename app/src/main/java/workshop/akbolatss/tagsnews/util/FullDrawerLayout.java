package workshop.akbolatss.tagsnews.util;

import android.content.Context;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

public class FullDrawerLayout extends DrawerLayout{

    private static final int MIN_DRAWER_MARGIN = 0; // dp

    public FullDrawerLayout(Context context) {
        super(context);
    }

    public FullDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FullDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        final int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode != View.MeasureSpec.EXACTLY || heightMode != View.MeasureSpec.EXACTLY) {
            throw new IllegalArgumentException(
                    "DrawerLayout must be measured with MeasureSpec.EXACTLY.");
        }

        setMeasuredDimension(widthSize, heightSize);

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getLayoutParams();
        params.topMargin = getStatusBarHeight();
        setLayoutParams(params);

        // Gravity value for each drawer we've seen. Only one of each permitted.
        int foundDrawers = 0;
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);

            if (child.getVisibility() == GONE) {
                continue;
            }

            final DrawerLayout.LayoutParams lp = (DrawerLayout.LayoutParams) child.getLayoutParams();

            if (isContentView(child)) {
                // Content views get measured at exactly the layout's size.
                final int contentWidthSpec = View.MeasureSpec.makeMeasureSpec(
                        widthSize - lp.leftMargin - lp.rightMargin, View.MeasureSpec.EXACTLY);
                final int contentHeightSpec = View.MeasureSpec.makeMeasureSpec(
                        heightSize - lp.topMargin - lp.bottomMargin, View.MeasureSpec.EXACTLY);
                child.measure(contentWidthSpec, contentHeightSpec);
            } else if (isDrawerView(child)) {
                final int childGravity =
                        getDrawerViewGravity(child) & Gravity.HORIZONTAL_GRAVITY_MASK;
                if ((foundDrawers & childGravity) != 0) {
                    throw new IllegalStateException("Child drawer has absolute gravity " +
                            gravityToString(childGravity) + " but this already has a " +
                            "drawer view along that edge");
                }
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
        return ((DrawerLayout.LayoutParams) child.getLayoutParams()).gravity == Gravity.NO_GRAVITY;
    }

    boolean isDrawerView(View child) {
        final int gravity = ((DrawerLayout.LayoutParams) child.getLayoutParams()).gravity;
        final int absGravity = Gravity.getAbsoluteGravity(gravity,
                child.getLayoutDirection());
        return (absGravity & (Gravity.LEFT | Gravity.RIGHT)) != 0;
    }

    int getDrawerViewGravity(View drawerView) {
        final int gravity = ((DrawerLayout.LayoutParams) drawerView.getLayoutParams()).gravity;
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
}