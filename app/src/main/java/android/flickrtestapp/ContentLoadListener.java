package android.flickrtestapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.view.View;

/**
 * Created by Sola2Be on 14.09.2015.
 */
public class ContentLoadListener {

    private View mContentView;
    private View mLoadView;
    private int countAnim = 0;

    public ContentLoadListener(View content, View progress){
        mContentView = content;
        mLoadView = progress;
        mContentView.setVisibility(View.GONE);
    }


    public void setViewsToInitialState(){
        mContentView.setVisibility(View.GONE);
        mLoadView.setVisibility(View.VISIBLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            mContentView.setAlpha(1f);
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            mLoadView.setAlpha(1f);
        }
    }

    public void showContentOrLoadingIndicator(boolean contentLoaded) {
        // Decide which view to hide and which to show.
        final View showView = contentLoaded ? mContentView : mLoadView;
        final View hideView = contentLoaded ? mLoadView : mContentView;
        countAnim++;
        // Set the "show" view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            showView.setAlpha(0f);
        showView.setVisibility(View.VISIBLE);

        // Animate the "show" view to 100% opacity, and clear any animation listener set on
        // the view. Remember that listeners are not limited to the specific animation
        // describes in the chained method calls. Listeners are set on the
        // ViewPropertyAnimator object for the view, which persists across several
        // animations.
        showView.animate()
                .alpha(1f)
                .setDuration(300)
                .setListener(null);

        // Animate the "hide" view to 0% opacity. After the animation ends, set its visibility
        // to GONE as an optimization step (it won't participate in layout passes, etc.)
        hideView.animate()
                .alpha(0f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        hideView.setVisibility(View.GONE);
                    }
                });
    }

    public int getCountAnim(){
        return countAnim;
    }
}
