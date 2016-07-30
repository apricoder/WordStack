package com.olebokolo.wordstack.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.olebokolo.wordstack.R;
import com.olebokolo.wordstack.core.app.WordStack;
import com.olebokolo.wordstack.core.user.settings.factory.UserSettingsComponentsFactory;
import com.olebokolo.wordstack.core.user.settings.services.UserSettingsService;
import com.olebokolo.wordstack.presentation.navigation.ActivityNavigator;

public class MainMenuActivity extends AppCompatActivity {

    private UserSettingsService settingsService;
    private ActivityNavigator navigator;
    private Button settingsButton;
    private Button practiceButton;
    private Button editStacksButton;

    public MainMenuActivity() {
        WordStack application = WordStack.getInstance();
        navigator = application.getActivityNavigator();
        UserSettingsComponentsFactory factory = application.getUserSettingsComponentsFactory();
        settingsService = factory.getUserSettingsService();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!settingsService.userChoseLanguages()) showGreetingActivity();
        else showThisActivity();
    }

    private void showGreetingActivity() {
        Intent toShowGreeting = new Intent(this, GreetingActivity.class);
        startActivity(toShowGreeting);
        finish();
    }

    void showThisActivity() {
        setContentView(R.layout.activity_main_menu);
        findViews();
        setupButtons();
    }

    void findViews() {
        settingsButton = (Button) findViewById(R.id.settings_button);
        practiceButton = (Button) findViewById(R.id.practice_button);
        editStacksButton = (Button) findViewById(R.id.edit_stacks_button);
    }

    void setupButtons() {
        settingsButton.setOnClickListener(menuButtonsClick);
        practiceButton.setOnClickListener(menuButtonsClick);
        editStacksButton.setOnClickListener(menuButtonsClick);
    }


    private View.OnClickListener menuButtonsClick = new View.OnClickListener() {
        @Override
        public void onClick(View clickedButton) {
            Class target;
            switch (clickedButton.getId()) {
                default:
                case R.id.practice_button: target = MainMenuActivity.class; break;
                case R.id.edit_stacks_button: target = MainMenuActivity.class; break;
                case R.id.settings_button: target = SettingsActivity.class; break;
            }
            navigator.goForwardWithSlideAnimation(MainMenuActivity.this, target);
        }
    };
}
