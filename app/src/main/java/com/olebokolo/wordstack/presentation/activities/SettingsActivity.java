package com.olebokolo.wordstack.presentation.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.olebokolo.wordstack.R;
import com.olebokolo.wordstack.core.app.WordStack;
import com.olebokolo.wordstack.core.utils.ActivityNavigator;

public class SettingsActivity extends AppCompatActivity {

    private ActivityNavigator navigator;
    private View backToolbarButton;

    public SettingsActivity() {
        WordStack application = WordStack.getInstance();
        navigator = application.getActivityNavigator();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        findViews();
        setupBackButton();
    }

    private void setupBackButton() {
        backToolbarButton.setOnClickListener(backClickListener);
    }

    private View.OnClickListener backClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View button) {
            navigator.goBackWithSlideAnimation(SettingsActivity.this, MainMenuActivity.class);
        }
    };

    private void findViews() {
        backToolbarButton = findViewById(R.id.back_toolbar_button);
    }
}
