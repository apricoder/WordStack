package com.olebokolo.wordstack.core.app;

import android.app.Application;

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
import com.olebokolo.wordstack.presentation.activities.StackActivity;
import com.olebokolo.wordstack.presentation.activities.StackListActivity;
import com.olebokolo.wordstack.presentation.dialogs.CardAddDialog;
import com.olebokolo.wordstack.presentation.dialogs.StackActionsDialog;
import com.olebokolo.wordstack.presentation.dialogs.StackAddDialog;
import com.olebokolo.wordstack.presentation.dialogs.StackAlert;
import com.olebokolo.wordstack.presentation.dialogs.StackConfirmDeleteDialog;
import com.olebokolo.wordstack.presentation.dialogs.StackRenameDialog;
import com.olebokolo.wordstack.presentation.lists.stacks.StackAdapter;
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

    public void injectDependenciesTo(StackAddDialog stackAddDialog) {
        stackAddDialog.typefaceCollection = this.typefaceCollection;
        stackAddDialog.typefaceManager = this.typefaceManager;
        stackAddDialog.settingsService = this.userSettingsComponentsFactory.getUserSettingsService();
    }

    public void injectDependenciesTo(StackAdapter stackAdapter) {
        stackAdapter.typefaceCollection = this.typefaceCollection;
        stackAdapter.typefaceManager = this.typefaceManager;
    }

    public void injectDependenciesTo(StackActionsDialog stackActionsDialog) {
        stackActionsDialog.typefaceCollection = this.typefaceCollection;
        stackActionsDialog.typefaceManager = this.typefaceManager;
    }

    public void injectDependenciesTo(StackConfirmDeleteDialog stackConfirmDeleteDialog) {
        stackConfirmDeleteDialog.typefaceCollection = this.typefaceCollection;
        stackConfirmDeleteDialog.typefaceManager = this.typefaceManager;
    }

    public void injectDependenciesTo(StackRenameDialog stackRenameDialog) {
        stackRenameDialog.typefaceCollection = this.typefaceCollection;
        stackRenameDialog.typefaceManager = this.typefaceManager;
    }

    public void injectDependenciesTo(StackAlert stackAlert) {
        stackAlert.typefaceCollection = this.typefaceCollection;
        stackAlert.typefaceManager = this.typefaceManager;
    }

    public void injectDependenciesTo(StackActivity stackActivity) {
        stackActivity.typefaceCollection = this.typefaceCollection;
        stackActivity.typefaceManager = this.typefaceManager;
        stackActivity.navigator = this.activityNavigator;
        stackActivity.languageService = languageComponentsFactory.getLanguageService();
        stackActivity.flagService = languageComponentsFactory.getFlagService();
        stackActivity.settingsService = userSettingsComponentsFactory.getUserSettingsService();
    }

    public void injectDependenciesTo(CardAddDialog cardAddDialog) {
        cardAddDialog.typefaceCollection = this.typefaceCollection;
        cardAddDialog.typefaceManager = this.typefaceManager;
        cardAddDialog.languageService = languageComponentsFactory.getLanguageService();
        cardAddDialog.flagService = languageComponentsFactory.getFlagService();
        cardAddDialog.settingsService = userSettingsComponentsFactory.getUserSettingsService();
    }
}
