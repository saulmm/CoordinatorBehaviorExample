package saulmm.myapplication;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;

import de.hdodenhof.circleimageview.CircleImageView;

@SuppressWarnings("unused")
public class AvatarImageBehavior extends CoordinatorLayout.Behavior<CircleImageView> {

    private final static float MIN_AVATAR_PERCENTAGE_SIZE   = 0.3f;
    private final static int EXTRA_FINAL_AVATAR_PADDING     = 80;

    private final static String TAG = "behavior";
    private final Context mContext;
    private float mAvatarMaxSize;
    private float mMarginTop;

    public AvatarImageBehavior(Context context, AttributeSet attrs) {
        mContext = context;
        init();
    }

    private void init() {
        mAvatarMaxSize = mContext.getResources().getDimension(R.dimen.image_width);
        mMarginTop = mContext.getResources().getDimension(R.dimen.image_margin);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, CircleImageView child, View dependency) {

        return dependency instanceof Toolbar;
    }

    @Override
    public boolean onMeasureChild(CoordinatorLayout parent, CircleImageView child,
                                  int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {

        return super.onMeasureChild(parent, child, parentWidthMeasureSpec, widthUsed,
                parentHeightMeasureSpec, heightUsed);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, CircleImageView child, View dependency) {

        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();

        final int maxNumber = (int) (mMarginTop - getStatusBarHeight());

        float percentageFactor = dependency.getY() / maxNumber;
        int proportionalAvatarSize = (int) (mAvatarMaxSize * (percentageFactor));

        float childMarginTop    = dependency.getY() - (child.getHeight() / 2);
        float childMarginLeft   = (dependency.getWidth() / 2) - (child.getWidth() / 2);
        float pChildMarginLeft  = childMarginLeft * percentageFactor;

        int extraFinalPadding = (int) (EXTRA_FINAL_AVATAR_PADDING * (1f - percentageFactor));

        if (percentageFactor >= MIN_AVATAR_PERCENTAGE_SIZE) {
            lp.width = proportionalAvatarSize;
            lp.height = proportionalAvatarSize;
        }

        lp.setMargins(
            (int) pChildMarginLeft + extraFinalPadding,
            (int) childMarginTop + extraFinalPadding,
            lp.rightMargin,
            lp.bottomMargin
        );

        child.setLayoutParams(lp);
        return true;
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");

        if (resourceId > 0) {
            result = mContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}