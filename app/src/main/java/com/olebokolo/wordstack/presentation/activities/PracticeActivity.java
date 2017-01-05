package com.olebokolo.wordstack.presentation.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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

public class PracticeActivity extends AppCompatActivity {

    // dependencies
    public TypefaceCollection typefaceCollection;
    public TypefaceManager typefaceManager;
    public ActivityNavigator navigator;
    public LanguageService languageService;
    public FlagService flagService;
    public UserSettingsService settingsService;
    // views
    private ImageView frontLangIcon;
    private ImageView backLangIcon;
    private ViewGroup backToolbarButton;
    private FloatingActionButton practiceButton;
    // data
    private Long frontLangId;
    private Long backLangId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        WordStack.getInstance().injectDependenciesTo(this);
        // EventBus.getDefault().register(this);
        findViews();
        setupTypeface();
        setupGoBackButton();
        setupLanguages();
        setupLanguagesIcons();
    }

    private void findViews() {
        backToolbarButton = (ViewGroup) findViewById(R.id.back_toolbar_button);
        frontLangIcon = (ImageView) findViewById(R.id.front_lang_icon);
        backLangIcon = (ImageView) findViewById(R.id.back_lang_icon);
        practiceButton = (FloatingActionButton) findViewById(R.id.practice_button);
    }

    private void setupLanguagesIcons() {
        Language frontLanguage = languageService.findById(frontLangId);
        Language backLanguage = languageService.findById(backLangId);
        frontLangIcon.setImageResource(flagService.getFlagByLanguageShortName(frontLanguage.getShortName()));
        backLangIcon.setImageResource(flagService.getFlagByLanguageShortName(backLanguage.getShortName()));
    }

    private void setupLanguages() {
        UserSettings userSettings = settingsService.getUserSettings();
        frontLangId = userSettings.getFrontLangId();
        backLangId = userSettings.getBackLangId();
    }

    private void setupTypeface() {
        typefaceManager.setTypefaceForContainer(backToolbarButton, typefaceCollection.getRalewayMedium());
    }

    private void setupGoBackButton() {
        backToolbarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                goBack();
            }
        });
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    private void goBack() {
        navigator.goBackWithSlideAnimation(PracticeActivity.this, MainMenuActivity.class);
    }

}
