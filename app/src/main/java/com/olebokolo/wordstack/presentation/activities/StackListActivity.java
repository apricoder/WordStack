package com.olebokolo.wordstack.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.olebokolo.wordstack.R;
import com.olebokolo.wordstack.core.app.WordStack;
import com.olebokolo.wordstack.core.user.settings.factory.UserSettingsComponentsFactory;
import com.olebokolo.wordstack.core.user.settings.services.UserSettingsService;

public class StackListActivity extends AppCompatActivity {

    private UserSettingsService settingsService;

    public StackListActivity() {
        WordStack application = WordStack.getInstance();
        UserSettingsComponentsFactory factory = application.getUserSettingsComponentsFactory();
        settingsService = factory.getUserSettingsService();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (settingsService.userChoseLanguages()) showThisActivity();
        else showGreetingActivity();

    }

    private void showThisActivity() {
        setContentView(R.layout.activity_stack_list);
    }

    private void showGreetingActivity() {
        Intent toShowGreeting = new Intent(this, GreetingActivity.class);
        startActivity(toShowGreeting);
        finish();
    }
}
