package com.olebokolo.wordstack.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.olebokolo.wordstack.R;
import com.olebokolo.wordstack.core.AppHeart;
import com.olebokolo.wordstack.core.user.settings.factory.UserSettingsComponentsFactory;
import com.olebokolo.wordstack.core.user.settings.services.UserSettingsService;

public class StackListActivity extends AppCompatActivity {

    private UserSettingsService settingsService;

    public StackListActivity() {
        AppHeart appHeart = AppHeart.getInstanceFor(this);
        UserSettingsComponentsFactory factory = appHeart.getUserSettingsComponentsFactory();
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
