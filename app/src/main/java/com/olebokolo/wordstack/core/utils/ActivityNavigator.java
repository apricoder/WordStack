package com.olebokolo.wordstack.core.utils;

import android.app.Activity;
import android.content.Intent;

import com.olebokolo.wordstack.R;

public class ActivityNavigator {

    public void goForwardWithSlideAnimation(Activity fromActivity, Class targetActivityClass) {
        boolean directionForward = true;
        animateSlide(fromActivity, targetActivityClass, directionForward);
    }

    public void goBackWithSlideAnimation(Activity fromActivity, Class targetActivity) {
        boolean directionForward = false;
        animateSlide(fromActivity, targetActivity, directionForward);
    }

    private void animateSlide(Activity fromActivity, Class targetActivityClass, boolean directionForward) {
        Intent navigation = new Intent(fromActivity, targetActivityClass);
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
}
