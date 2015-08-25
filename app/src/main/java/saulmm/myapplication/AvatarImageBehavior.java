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

    private float mFinalLeftAvatarPadding;
    private float mStartPosition;

    public AvatarImageBehavior(Context context, AttributeSet attrs) {
        mContext = context;
        init();

        mFinalLeftAvatarPadding = context.getResources().getDimension(
            R.dimen.abc_action_bar_navigation_padding_start_material);
    }

    private void init() {
        bindDimensions();
    }

    private void bindDimensions() {
        mAvatarMaxSize = mContext.getResources().getDimension(R.dimen.image_width);
        mMarginTop = mContext.getResources().getDimension(R.dimen.image_margin);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, CircleImageView child, View dependency) {
        return dependency instanceof Toolbar;
    }

    // Child startPosition
    int startPosition = 0;

    // Toolbar half position
    int finalPosition = 0;

    int finalHeight = 130;

    int startHeight = 0;

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, CircleImageView child, View dependency) {

        // Called once
        if (startPosition == 0)
            startPosition = (int) (child.getY() + (child.getHeight() / 2));

        if (finalPosition == 0)
            finalPosition = (dependency.getHeight() /2);

        if (startHeight == 0)
            startHeight = child.getHeight();


        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();

        final int maxScrollDistance = (int) (mMarginTop - getStatusBarHeight());
        float expandedPercentageFactor = dependency.getY() / maxScrollDistance;

        float distanceYToSubtract = ((startPosition - finalPosition) * (1f - expandedPercentageFactor)) + (child.getHeight()/2);
        float heightToSubtract = ((startHeight - finalHeight) * (1f - expandedPercentageFactor));

        child.setY(startPosition - distanceYToSubtract);

        int proportionalAvatarSize = (int) (mAvatarMaxSize * (expandedPercentageFactor));

        lp.width = (int) (startHeight - heightToSubtract);
        lp.height = (int) (startHeight - heightToSubtract);

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