package com.olebokolo.wordstack.presentation.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.olebokolo.wordstack.presentation.dialogs.CardAddDialog;
import com.olebokolo.wordstack.presentation.navigation.ActivityNavigator;

import java.io.Serializable;

public class StackActivity extends AppCompatActivity {

    public TypefaceCollection typefaceCollection;
    public TypefaceManager typefaceManager;
    public ActivityNavigator navigator;
    public LanguageService languageService;
    public FlagService flagService;
    public UserSettingsService settingsService;

    private ImageView frontLangIcon;
    private ImageView backLangIcon;
    private ViewGroup backToolbarButton;
    private ViewGroup rootLayout;

    private Long frontLangId;
    private Long backLangId;
    private Stack stack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stack);
        getStackFromIntentExtras();
        WordStack.getInstance().injectDependenciesTo(this);
        //EventBus.getDefault().register(this);
        findViews();
        setupTypeface();
        setupGoBackButton();
        setupAddCardButton();
        setupLanguages();
        setupLanguagesIcons();
    }

    private void getStackFromIntentExtras() {
        Serializable serializable = getIntent().getSerializableExtra("stack");
        if (serializable != null) {
            stack = (Stack) serializable;
            setToolbarTitle(stack.getName());
        }
    }

    private void setToolbarTitle(String name) {
        ((TextView) findViewById(R.id.toolbar_title)).setText(name);
    }

    private void setupAddCardButton() {
        findViewById(R.id.add_card_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CardAddDialog(StackActivity.this, stack).show();
            }
        });
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
        typefaceManager.setTypefaceForContainer(rootLayout, typefaceCollection.getRalewayLight());
        typefaceManager.setTypefaceForContainer(backToolbarButton, typefaceCollection.getRalewayMedium());
    }

    private void findViews() {
        frontLangIcon = (ImageView) findViewById(R.id.front_lang_icon);
        backLangIcon = (ImageView) findViewById(R.id.back_lang_icon);
        rootLayout = (ViewGroup) findViewById(R.id.root_layout);
        backToolbarButton = (ViewGroup) findViewById(R.id.back_toolbar_button);
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

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        Serializable serializable = savedInstanceState.getSerializable("stack");
        if (serializable != null) {
            stack = (Stack) serializable;
            setToolbarTitle(stack.getName());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putSerializable("stack", stack);
    }

    private void goBack() {
        navigator.goBackWithSlideAnimation(StackActivity.this, StackListActivity.class);
    }



}
