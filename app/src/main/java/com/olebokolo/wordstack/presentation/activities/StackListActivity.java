package com.olebokolo.wordstack.presentation.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.olebokolo.wordstack.R;
import com.olebokolo.wordstack.core.app.WordStack;
import com.olebokolo.wordstack.presentation.navigation.ActivityNavigator;

public class StackListActivity extends AppCompatActivity {

    private ActivityNavigator navigator;
    private View backToolbarButton;

    public StackListActivity() {
        WordStack application = WordStack.getInstance();
        navigator = application.getActivityNavigator();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stack_list);
        findViews();
        setupGoBackButton();
    }

    private void setupGoBackButton() {
        backToolbarButton.setOnClickListener(backClickListener);
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    private View.OnClickListener backClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View button) {
            goBack();
        }
    };

    private void goBack() {
        navigator.goBackWithSlideAnimation(StackListActivity.this, MainMenuActivity.class);
    }

    private void findViews() {
        backToolbarButton = findViewById(R.id.back_toolbar_button);
    }
}
