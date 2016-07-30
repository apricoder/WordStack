package com.olebokolo.wordstack.presentation.navigation;

import android.app.Activity;
import android.content.Intent;

import com.olebokolo.wordstack.R;

public class ActivityNavigator {

    public void goForwardWithSlideAnimation(Activity fromActivity, Class targetActivityClass) {
        animateSlide(fromActivity, targetActivityClass, true);
    }

    public void goBackWithSlideAnimation(Activity fromActivity, Class nextActivity) {
        animateSlide(fromActivity, nextActivity, false);
    }

    private void animateSlide(Activity fromActivity, Class nextActivity, boolean directionForward) {
        Intent navigation = new Intent(fromActivity, nextActivity);
        navigation.putExtra("previousActivity", fromActivity.getClass());
        fromActivity.startActivity(navigation);
        animateWithDirection(fromActivity, directionForward);
        fromActivity.finish();
    }

    private void animateWithDirection(Activity fromActivity, boolean slideForward) {
        if (slideForward) animateSlideForward(fromActivity); else animateSlideBack(fromActivity);
    }

    private void animateSlideBack(Activity activity) {
        activity.overridePendingTransition(R.anim.slide_in_from_left_fast, R.anim.slide_out_to_right_fast);
    }

    private void animateSlideForward(Activity activity) {
        activity.overridePendingTransition(R.anim.slide_in_from_right_fast, R.anim.slide_out_to_left_fast);
    }

    public void goForwardWithSlideAnimationAndFurtherActivity(Activity fromActivity, Class nextActivity, Class furtherActivity, NavigationDirection furtherSlideDirection) {
        Intent navigation = new Intent(fromActivity, nextActivity);
        navigation.putExtra("previousActivity", fromActivity.getClass());
        navigation.putExtra("furtherActivity", furtherActivity);
        navigation.putExtra("furtherSlideDirection", furtherSlideDirection);
        fromActivity.startActivity(navigation);
        animateWithDirection(fromActivity, true);
        fromActivity.finish();
    }
}
