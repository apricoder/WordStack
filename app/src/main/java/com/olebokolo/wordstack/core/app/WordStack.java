package com.olebokolo.wordstack.core.app;

import android.app.Application;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.olebokolo.wordstack.core.languages.factory.LanguageComponentsFactory;
import com.olebokolo.wordstack.core.languages.factory.LanguageComponentsFactoryImpl;
import com.olebokolo.wordstack.core.resources.factory.DrawableComponentsFactory;
import com.olebokolo.wordstack.core.resources.factory.DrawableComponentsFactoryImpl;
import com.olebokolo.wordstack.core.user.settings.factory.UserSettingsComponentsFactory;
import com.olebokolo.wordstack.core.user.settings.factory.UserSettingsComponentsFactoryImpl;
import com.olebokolo.wordstack.core.utils.TypefaceCollection;
import com.olebokolo.wordstack.core.utils.TypefaceManager;
import com.olebokolo.wordstack.presentation.activities.GreetingActivity;
import com.olebokolo.wordstack.presentation.activities.LanguagesActivity;
import com.olebokolo.wordstack.presentation.activities.MainMenuActivity;
import com.olebokolo.wordstack.presentation.activities.SettingsActivity;
import com.olebokolo.wordstack.presentation.activities.StackListActivity;
import com.olebokolo.wordstack.presentation.dialogs.AddStackDialog;
import com.olebokolo.wordstack.presentation.navigation.ActivityNavigator;
import com.orm.SugarContext;

import lombok.Getter;

public class WordStack extends Application {

    @Getter private static WordStack instance = new WordStack();
    @Getter private UserSettingsComponentsFactory userSettingsComponentsFactory;
    @Getter private LanguageComponentsFactory languageComponentsFactory;
    @Getter private DrawableComponentsFactory drawableComponentsFactory;
    @Getter private ActivityNavigator activityNavigator;
    private TypefaceManager typefaceManager;
    private TypefaceCollection typefaceCollection;

    public WordStack() {
        instance = this;
        initFields();
    }

    void initFields() {
        userSettingsComponentsFactory = new UserSettingsComponentsFactoryImpl();
        drawableComponentsFactory = new DrawableComponentsFactoryImpl();
        languageComponentsFactory = new LanguageComponentsFactoryImpl();
        activityNavigator = new ActivityNavigator();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
        typefaceManager = new TypefaceManager();
        typefaceCollection = new TypefaceCollection(this);
    }

    public void injectDependenciesTo(StackListActivity stackListActivity) {
        stackListActivity.typefaceCollection = this.typefaceCollection;
        stackListActivity.typefaceManager = this.typefaceManager;
        stackListActivity.navigator = activityNavigator;
        stackListActivity.languageService = languageComponentsFactory.getLanguageService();
        stackListActivity.flagService = languageComponentsFactory.getFlagService();
        stackListActivity.settingsService = userSettingsComponentsFactory.getUserSettingsService();
    }


    public void injectDependenciesTo(GreetingActivity greetingActivity) {
        greetingActivity.navigator = this.activityNavigator;
        greetingActivity.typefaceCollection = this.typefaceCollection;
        greetingActivity.typefaceManager = this.typefaceManager;
    }

    public void injectDependenciesTo(LanguagesActivity languagesActivity) {
        languagesActivity.typefaceCollection = this.typefaceCollection;
        languagesActivity.typefaceManager = this.typefaceManager;
        languagesActivity.navigator = activityNavigator;
        languagesActivity.languageService = languageComponentsFactory.getLanguageService();
        languagesActivity.flagService = languageComponentsFactory.getFlagService();
        languagesActivity.settingsService = userSettingsComponentsFactory.getUserSettingsService();
    }

    public void injectDependenciesTo(MainMenuActivity mainMenuActivity) {
        mainMenuActivity.typefaceCollection = this.typefaceCollection;
        mainMenuActivity.typefaceManager = this.typefaceManager;
        mainMenuActivity.navigator = activityNavigator;
        mainMenuActivity.languageService = languageComponentsFactory.getLanguageService();
        mainMenuActivity.flagService = languageComponentsFactory.getFlagService();
        mainMenuActivity.settingsService = userSettingsComponentsFactory.getUserSettingsService();
    }

    public void injectDependenciesTo(SettingsActivity settingsActivity) {
        settingsActivity.typefaceCollection = this.typefaceCollection;
        settingsActivity.typefaceManager = this.typefaceManager;
        settingsActivity.navigator = activityNavigator;
        settingsActivity.languageService = languageComponentsFactory.getLanguageService();
        settingsActivity.flagService = languageComponentsFactory.getFlagService();
        settingsActivity.settingsService = userSettingsComponentsFactory.getUserSettingsService();
    }

    public void injectDependenciesTo(AddStackDialog addStackDialog) {

    }
}
