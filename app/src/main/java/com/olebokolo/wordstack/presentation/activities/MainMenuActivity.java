package com.olebokolo.wordstack.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.olebokolo.wordstack.R;
import com.olebokolo.wordstack.core.app.WordStack;
import com.olebokolo.wordstack.core.languages.flags.FlagService;
import com.olebokolo.wordstack.core.languages.services.LanguageService;
import com.olebokolo.wordstack.core.model.Language;
import com.olebokolo.wordstack.core.model.UserSettings;
import com.olebokolo.wordstack.core.user.settings.services.UserSettingsService;
import com.olebokolo.wordstack.core.utils.TypefaceCollection;
import com.olebokolo.wordstack.core.utils.TypefaceManager;
import com.olebokolo.wordstack.presentation.navigation.ActivityNavigator;

public class MainMenuActivity extends AppCompatActivity {

    public UserSettingsService settingsService;
    public ActivityNavigator navigator;
    public TypefaceCollection typefaceCollection;
    public TypefaceManager typefaceManager;
    public LanguageService languageService;
    public FlagService flagService;

    private Button settingsButton;
    private Button practiceButton;
    private Button editStacksButton;
    private ViewGroup rootLayout;
    private TextView toolbarTitle;
    private ImageView frontLangIcon;
    private ImageView backLangIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WordStack.getInstance().injectDependenciesTo(this);
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
        setupTypefaces();
        setupLanguageIcons();
        setupButtons();
    }

    void findViews() {
        frontLangIcon = (ImageView) findViewById(R.id.front_lang_icon);
        backLangIcon = (ImageView) findViewById(R.id.back_lang_icon);
        rootLayout = (ViewGroup) findViewById(R.id.root_layout);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        settingsButton = (Button) findViewById(R.id.settings_button);
        practiceButton = (Button) findViewById(R.id.practice_button);
        editStacksButton = (Button) findViewById(R.id.edit_stacks_button);
    }

    private void setupTypefaces() {
        typefaceManager.setTypefaceForContainer(rootLayout, typefaceCollection.getRalewayLight());
        typefaceManager.setTypeface(settingsButton, typefaceCollection.getRalewayMedium());
        typefaceManager.setTypeface(practiceButton, typefaceCollection.getRalewayMedium());
        typefaceManager.setTypeface(editStacksButton, typefaceCollection.getRalewayMedium());
        typefaceManager.setTypeface(toolbarTitle, typefaceCollection.getRalewayMedium());
    }

    void setupButtons() {
        settingsButton.setOnClickListener(menuButtonsClick);
        practiceButton.setOnClickListener(menuButtonsClick);
        editStacksButton.setOnClickListener(menuButtonsClick);
    }

    private void setupLanguageIcons() {
        UserSettings userSettings = settingsService.getUserSettings();
        Long frontLangId = userSettings.getFrontLangId();
        Long backLangId = userSettings.getBackLangId();
        Language frontLanguage = languageService.findById(frontLangId);
        Language backLanguage = languageService.findById(backLangId);
        frontLangIcon.setImageResource(flagService.getFlagByLanguageShortName(frontLanguage.getShortName()));
        backLangIcon.setImageResource(flagService.getFlagByLanguageShortName(backLanguage.getShortName()));
    }


    private View.OnClickListener menuButtonsClick = new View.OnClickListener() {
        @Override
        public void onClick(View clickedButton) {
            Class target;
            switch (clickedButton.getId()) {
                default:
                case R.id.practice_button: target = MainMenuActivity.class; break;
                case R.id.edit_stacks_button: target = StackListActivity.class; break;
                case R.id.settings_button: target = SettingsActivity.class; break;
            }
            navigator.goForwardWithSlideAnimation(MainMenuActivity.this, target);
        }
    };
}
