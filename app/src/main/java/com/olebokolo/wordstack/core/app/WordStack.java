package com.olebokolo.wordstack.core.app;

import android.app.Application;
import android.graphics.drawable.Drawable;

import com.olebokolo.wordstack.core.languages.factory.LanguageComponentsFactory;
import com.olebokolo.wordstack.core.languages.factory.LanguageComponentsFactoryImpl;
import com.olebokolo.wordstack.core.resources.factory.DrawableComponentsFactory;
import com.olebokolo.wordstack.core.resources.factory.DrawableComponentsFactoryImpl;
import com.olebokolo.wordstack.core.user.settings.factory.UserSettingsComponentsFactory;
import com.olebokolo.wordstack.core.user.settings.factory.UserSettingsComponentsFactoryImpl;
import com.olebokolo.wordstack.core.utils.ActivityNavigator;
import com.orm.SugarContext;

import lombok.Getter;

public class WordStack extends Application {

    @Getter private static WordStack instance = new WordStack();
    @Getter private UserSettingsComponentsFactory userSettingsComponentsFactory;
    @Getter private LanguageComponentsFactory languageComponentsFactory;
    @Getter private DrawableComponentsFactory drawableComponentsFactory;
    @Getter private ActivityNavigator activityNavigator;

    public WordStack() {
        initFields();
        instance = this;
    }

    private void initFields() {
        userSettingsComponentsFactory = new UserSettingsComponentsFactoryImpl();
        languageComponentsFactory = new LanguageComponentsFactoryImpl();
        drawableComponentsFactory = new DrawableComponentsFactoryImpl();
        activityNavigator = new ActivityNavigator();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
    }

}
