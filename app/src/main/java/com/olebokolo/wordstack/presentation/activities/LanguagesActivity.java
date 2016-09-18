package com.olebokolo.wordstack.presentation.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.olebokolo.wordstack.presentation.dialogs.LanguageDialog;
import com.olebokolo.wordstack.presentation.navigation.ActivityNavigator;
import com.olebokolo.wordstack.presentation.navigation.NavigationDirection;

public class LanguagesActivity extends AppCompatActivity implements LanguageListener {

    public FlagService flagService;
    public LanguageService languageService;
    public ActivityNavigator navigator;
    public UserSettingsService settingsService;
    public TypefaceCollection typefaceCollection;
    public TypefaceManager typefaceManager;

    private LinearLayout languageIKnowLayout;
    private LinearLayout languageILearnLayout;
    private Button goNextButton;
    private boolean choosingLanguageIKnow;
    private Language languageIKnow;
    private Language languageILearn;
    private ViewGroup backToolbarButton;
    private ViewGroup rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_languages);
        WordStack.getInstance().injectDependenciesTo(this);
        findViews();
        setupGoBackButton();
        setupGoNextButton();
        setupTypefaces();
        setupLanguageChoosing();
        setupLanguages();
        updateLanguageLayouts();
    }

    private void setupTypefaces() {
        typefaceManager.setTypefaceForContainer(rootLayout, typefaceCollection.getRalewayLight());
        typefaceManager.setTypefaceForContainer(backToolbarButton, typefaceCollection.getRalewayMedium());
        typefaceManager.setTypeface((TextView) languageIKnowLayout.findViewById(R.id.lang_name), typefaceCollection.getRalewayMedium());
        typefaceManager.setTypeface((TextView) languageILearnLayout.findViewById(R.id.lang_name), typefaceCollection.getRalewayMedium());
        typefaceManager.setTypeface(goNextButton, typefaceCollection.getRalewayMedium());
    }

    private void setupLanguages() {
        if (settingsService.userChoseLanguages()) {
            UserSettings userSettings = settingsService.getUserSettings();
            languageIKnow = languageService.findById(userSettings.getFrontLangId());
            languageILearn = languageService.findById(userSettings.getBackLangId());
        } else {
            languageIKnow = languageService.findByShortName("en");
            languageILearn = languageService.findByShortName("fr");
        }
    }

    private void findViews() {
        rootLayout = (ViewGroup) findViewById(R.id.root_layout);
        backToolbarButton = (ViewGroup)findViewById(R.id.back_toolbar_button);
        languageIKnowLayout = (LinearLayout) findViewById(R.id.front_lang_layout);
        languageILearnLayout = (LinearLayout) findViewById(R.id.back_lang_layout);
        goNextButton = (Button) findViewById(R.id.go_next_button);
    }

    @Override
    public void languageReceived(Language chosenLanguage) {
        if (choosingLanguageIKnow) {
            if (chosenLanguage.equals(languageILearn)) languageILearn = copyLanguage(languageIKnow);
            languageIKnow = chosenLanguage;
        } else {
            if (chosenLanguage.equals(languageIKnow)) languageIKnow = copyLanguage(languageILearn);
            languageILearn = chosenLanguage;
        }
        updateLanguageLayouts();
    }

    private Language copyLanguage(Language language) {
        return new Language(language.getId(), language.getName(), language.getShortName());
    }

    private void updateLanguageLayouts() {
        fillLayout(languageIKnowLayout, languageIKnow);
        fillLayout(languageILearnLayout, languageILearn);
    }

    private void fillLayout(View languageLayout, Language language) {
        ImageView languageIcon = (ImageView) languageLayout.findViewById(R.id.lang_icon);
        TextView languageName = (TextView) languageLayout.findViewById(R.id.lang_name);
        languageIcon.setImageResource(flagService.getFlagByLanguageShortName(language.getShortName()));
        languageName.setText(language.getName());
    }

    private void setupGoNextButton() {
        if (settingsService.userChoseLanguages()) goNextButton.setText("Confirm");
        goNextButton.setOnClickListener(goNextClickListener);
    }

    private View.OnClickListener goNextClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            saveChosenLanguages();

            Bundle extras = getIntent().getExtras();
            Class furtherActivity = (Class) extras.getSerializable("furtherActivity");
            NavigationDirection furtherDirection = (NavigationDirection) extras.get("furtherSlideDirection");
            if (furtherDirection != null && furtherDirection.equals(NavigationDirection.BACK)) navigator.goBackWithSlideAnimation(LanguagesActivity.this, furtherActivity);
            else navigator.goForwardWithSlideAnimation(LanguagesActivity.this, furtherActivity);
        }

        private void saveChosenLanguages() {
            UserSettings userSettings = settingsService.getUserSettings();
            if (userSettings == null) userSettings = new UserSettings();
            userSettings.setFrontLangId(languageIKnow.getId());
            userSettings.setBackLangId(languageILearn.getId());
            userSettings.save();
        }
    };

    private void setupLanguageChoosing() {
        languageIKnowLayout.setOnClickListener(languageClickListener);
        languageILearnLayout.setOnClickListener(languageClickListener);
    }

    private View.OnClickListener languageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View clickedLanguage) {
            String dialogTitle = matchDialogTitle(clickedLanguage);
            showChooseLanguageDialog(dialogTitle);
        }

        private String matchDialogTitle(View clickedLanguage) {
            updateLanguageTrigger(clickedLanguage);
            if (choosingLanguageIKnow) return "Specify language\nYou know";
            else return "Choose language\nYou want to learn";
        }
    };

    private void updateLanguageTrigger(View clickedLanguage) {
        choosingLanguageIKnow = clickedLanguage == languageIKnowLayout;
    }

    private void showChooseLanguageDialog(String dialogTitle) {
        LanguagesActivity parent = this;
        LanguageDialog languageDialog = new LanguageDialog(parent, dialogTitle);
        languageDialog.show();
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
        Bundle extras = getIntent().getExtras();
        Class previousActivity = (Class) extras.getSerializable("previousActivity");
        navigator.goBackWithSlideAnimation(this, previousActivity);
    }
}
