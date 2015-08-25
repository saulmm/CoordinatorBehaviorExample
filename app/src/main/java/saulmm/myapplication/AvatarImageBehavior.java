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

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, CircleImageView child, View dependency) {

        // Called once
        if (startPosition == 0)
            startPosition = (int) (child.getY() + (child.getHeight() / 2));

        if (finalPosition == 0)
            finalPosition = (dependency.getHeight() /2);

        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();

        final int maxNumber = (int) (mMarginTop - getStatusBarHeight());

        // Assume that the final position will be at the top
        final int topPosition = 0;

        float currentPositionY = child.getY() + child.getHeight()/2;
        float expandedPercentageFactor = dependency.getY() / maxNumber;


        child.setY(startPosition - (((startPosition - finalPosition) * (1f - expandedPercentageFactor)) + (child.getHeight()/2)));
        int proportionalAvatarSize = (int) (mAvatarMaxSize * (expandedPercentageFactor));//
//
        if (expandedPercentageFactor >= MIN_AVATAR_PERCENTAGE_SIZE) {
            lp.width = proportionalAvatarSize;
            lp.height = proportionalAvatarSize;
        }


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