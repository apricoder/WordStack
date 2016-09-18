package com.olebokolo.wordstack.presentation.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.olebokolo.wordstack.R;
import com.olebokolo.wordstack.core.app.WordStack;
import com.olebokolo.wordstack.core.languages.factory.LanguageComponentsFactory;
import com.olebokolo.wordstack.core.languages.flags.FlagService;
import com.olebokolo.wordstack.core.languages.services.LanguageService;
import com.olebokolo.wordstack.core.model.Language;
import com.olebokolo.wordstack.core.model.UserSettings;
import com.olebokolo.wordstack.core.user.settings.factory.UserSettingsComponentsFactory;
import com.olebokolo.wordstack.core.user.settings.services.UserSettingsService;
import com.olebokolo.wordstack.core.utils.TypefaceCollection;
import com.olebokolo.wordstack.core.utils.TypefaceManager;
import com.olebokolo.wordstack.presentation.navigation.ActivityNavigator;
import com.olebokolo.wordstack.presentation.navigation.NavigationDirection;

public class SettingsActivity extends AppCompatActivity {

    public FlagService flagService;
    public LanguageService languageService;
    public UserSettingsService settingsService;
    public ActivityNavigator navigator;
    public TypefaceCollection typefaceCollection;
    public TypefaceManager typefaceManager;

    private ViewGroup backToolbarButton;
    private ImageView frontLangIcon;
    private ImageView backLangIcon;
    private ViewGroup languagesSettings;
    private ViewGroup rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WordStack.getInstance().injectDependenciesTo(this);
        setContentView(R.layout.activity_settings);
        findViews();
        setupGoBackButton();
        setupLanguageIcons();
        setupTypefaces();
        setupLanguageSettings();
    }

    private void setupTypefaces() {
        typefaceManager.setTypefaceForContainer(rootLayout, typefaceCollection.getRalewayLight());
        typefaceManager.setTypefaceForContainer(backToolbarButton, typefaceCollection.getRalewayMedium());
        typefaceManager.setTypefaceForContainer(languagesSettings, typefaceCollection.getRalewayMedium());
    }

    private void setupLanguageSettings() {
        languagesSettings.setOnClickListener(languagesSettingsClick);
    }

    private View.OnClickListener languagesSettingsClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            navigator.goForwardWithSlideAnimationAndFurtherActivity(
                    SettingsActivity.this,
                    LanguagesActivity.class,
                    SettingsActivity.class,
                    NavigationDirection.BACK);
        }
    };

    private void setupLanguageIcons() {
        UserSettings userSettings = settingsService.getUserSettings();
        Long frontLangId = userSettings.getFrontLangId();
        Long backLangId = userSettings.getBackLangId();
        Language frontLanguage = languageService.findById(frontLangId);
        Language backLanguage = languageService.findById(backLangId);
        frontLangIcon.setImageResource(flagService.getFlagByLanguageShortName(frontLanguage.getShortName()));
        backLangIcon.setImageResource(flagService.getFlagByLanguageShortName(backLanguage.getShortName()));
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
        navigator.goBackWithSlideAnimation(SettingsActivity.this, MainMenuActivity.class);
    }

    private void findViews() {
        rootLayout = (ViewGroup) findViewById(R.id.root_layout);
        backToolbarButton = (ViewGroup) findViewById(R.id.back_toolbar_button);
        languagesSettings = (ViewGroup) findViewById(R.id.languages_settings);
        frontLangIcon = (ImageView) findViewById(R.id.front_lang_icon);
        backLangIcon = (ImageView) findViewById(R.id.back_lang_icon);
    }
}
