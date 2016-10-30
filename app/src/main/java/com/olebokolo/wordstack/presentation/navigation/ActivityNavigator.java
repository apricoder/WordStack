package com.olebokolo.wordstack.presentation.navigation;

import android.app.Activity;
import android.content.Intent;

import com.olebokolo.wordstack.R;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ActivityNavigator {

    // constants
    private static final boolean DIRECTION_FORWARD = true;
    private static final boolean DIRECTION_BACKWARDS = false;
    private static final Map<String, Serializable> EMPTY_EXTRAS = new HashMap<>();

    public void goForwardWithSlideAnimation(Activity fromActivity, Class targetActivityClass, Map<String, Serializable> extras) {
        animateSlide(fromActivity, targetActivityClass, extras, true);
    }

    public void goForwardWithSlideAnimation(Activity fromActivity, Class targetActivityClass) {
        animateSlide(fromActivity, targetActivityClass, EMPTY_EXTRAS, DIRECTION_FORWARD);
    }

    public void goBackWithSlideAnimation(Activity fromActivity, Class nextActivity) {
        animateSlide(fromActivity, nextActivity, EMPTY_EXTRAS, DIRECTION_BACKWARDS);
    }

    private void animateSlide(Activity fromActivity, Class nextActivity, Map<String, Serializable> extras, boolean direction) {
        Intent navigation = new Intent(fromActivity, nextActivity);
        navigation.putExtra("previousActivity", fromActivity.getClass());
        for (String key : extras.keySet()) navigation.putExtra(key, extras.get(key));
        fromActivity.startActivity(navigation);
        animateWithDirection(fromActivity, direction);
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
