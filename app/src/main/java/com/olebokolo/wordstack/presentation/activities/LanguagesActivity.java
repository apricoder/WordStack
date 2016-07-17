package com.olebokolo.wordstack.presentation.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
import com.olebokolo.wordstack.core.utils.ActivityNavigator;
import com.olebokolo.wordstack.presentation.dialogs.LanguageDialog;

public class LanguagesActivity extends AppCompatActivity implements LanguageListener {

    private FlagService flagService;
    private LanguageService languageService;
    private UserSettingsService userSettingsService;
    private ActivityNavigator navigator;
    private LinearLayout languageIKnowLayout;
    private LinearLayout languageILearnLayout;
    private View goNextButton;
    private boolean choosingLanguageIKnow;
    private Language languageIKnow;
    private Language languageILearn;

    public LanguagesActivity() {
        WordStack application = WordStack.getInstance();
        navigator = application.getActivityNavigator();
        LanguageComponentsFactory languageComponentsFactory = application.getLanguageComponentsFactory();
        languageService = languageComponentsFactory.getLanguageService();
        flagService = languageComponentsFactory.getFlagService();
        UserSettingsComponentsFactory userSettingsComponentsFactory = application.getUserSettingsComponentsFactory();
        userSettingsService = userSettingsComponentsFactory.getUserSettingsService();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_languages);
        findViews();
        setupGoNextButton();
        setupLanguageChoosing();
        setupLanguages();
        updateLanguageLayouts();
    }

    private void setupLanguages() {
        languageIKnow = languageService.findByShortName("en");
        languageILearn = languageService.findByShortName("fr");
    }

    private void findViews() {
        languageIKnowLayout = (LinearLayout) findViewById(R.id.front_lang_layout);
        languageILearnLayout = (LinearLayout) findViewById(R.id.back_lang_layout);
        goNextButton = findViewById(R.id.go_next_button);
    }

    @Override
    public void languageReceived(Language chosenLanguage) {
        if (choosingLanguageIKnow) {
            if (chosenLanguage.equals(languageILearn)) languageILearn = copyLanguage(languageIKnow);
            languageIKnow = chosenLanguage;
        }
        else {
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
        goNextButton.setOnClickListener(goNextClickListener);
    }

    private View.OnClickListener goNextClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            saveChosenLanguages();
            navigator.goWithSlideAnimation(LanguagesActivity.this, StackListActivity.class);
        }

        private void saveChosenLanguages() {
            UserSettings.builder()
                    .frontLangId(languageIKnow.getId())
                    .backLangId(languageILearn.getId())
                    .build().save();
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
}
