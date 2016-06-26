package com.olebokolo.wordstack.core.utils;

import android.app.Activity;
import android.content.Intent;

import com.olebokolo.wordstack.R;

public class ActivityNavigator {

    public void goWithSlideAnimation(Activity fromActivity, Class targetActivityClass) {
        Intent navigation = new Intent(fromActivity, targetActivityClass);
        fromActivity.startActivity(navigation);
        animateSlide(fromActivity);
        fromActivity.finish();
    }

    private void animateSlide(Activity activity) {
        activity.overridePendingTransition(R.anim.slide_in_from_right_fast, R.anim.slide_out_to_left_fast);
    }
}
