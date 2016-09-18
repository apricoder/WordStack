package com.olebokolo.wordstack.presentation.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.olebokolo.wordstack.R;
import com.olebokolo.wordstack.core.app.WordStack;
import com.olebokolo.wordstack.core.languages.flags.FlagService;
import com.olebokolo.wordstack.core.languages.services.LanguageService;
import com.olebokolo.wordstack.core.model.Language;
import com.olebokolo.wordstack.core.model.Stack;
import com.olebokolo.wordstack.core.model.UserSettings;
import com.olebokolo.wordstack.core.user.settings.services.UserSettingsService;
import com.olebokolo.wordstack.core.utils.TypefaceCollection;
import com.olebokolo.wordstack.core.utils.TypefaceManager;
import com.olebokolo.wordstack.presentation.navigation.ActivityNavigator;

import java.util.List;

public class StackListActivity extends AppCompatActivity {

    // dependencies
    public ActivityNavigator navigator;
    public FlagService flagService;
    public LanguageService languageService;
    public UserSettingsService settingsService;
    public TypefaceManager typefaceManager;
    public TypefaceCollection typefaceCollection;
    // views
    private ImageView frontLangIcon;
    private ImageView backLangIcon;
    private ViewGroup backToolbarButton;
    // data
    private List<Stack> stackList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stack_list);
        WordStack.getInstance().injectDependenciesTo(this);
        findViews();
        setupTypeface();
        setupGoBackButton();
        setupLanguageIcons();
    }

    private void setupTypeface() {
        typefaceManager.setTypefaceForContainer(backToolbarButton, typefaceCollection.getRalewayMedium());
    }

    private void findViews() {
        backToolbarButton = (ViewGroup) findViewById(R.id.back_toolbar_button);
        frontLangIcon = (ImageView) findViewById(R.id.front_lang_icon);
        backLangIcon = (ImageView) findViewById(R.id.back_lang_icon);
    }

    private void setupGoBackButton() {
        backToolbarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                goBack();
            }
        });
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

    @Override
    public void onBackPressed() {
        goBack();
    }

    private void goBack() {
        navigator.goBackWithSlideAnimation(StackListActivity.this, MainMenuActivity.class);
    }

}
