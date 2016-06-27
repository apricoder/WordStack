package com.olebokolo.wordstack.core;

import android.content.Context;

import com.olebokolo.wordstack.core.languages.factory.LanguageComponentsFactory;
import com.olebokolo.wordstack.core.languages.factory.LanguageComponentsFactoryImpl;
import com.olebokolo.wordstack.core.utils.ActivityNavigator;
import com.olebokolo.wordstack.core.user.settings.factory.UserSettingsComponentsFactory;
import com.olebokolo.wordstack.core.user.settings.factory.UserSettingsComponentsFactoryImpl;

import lombok.Getter;

public class AppHeart {

    @Getter private UserSettingsComponentsFactory userSettingsComponentsFactory;
    @Getter private LanguageComponentsFactory languageComponentsFactory;
    @Getter private ActivityNavigator navigator;
    @Getter private Context context;
    private static AppHeart instance;

    public static AppHeart getInstanceFor(Context context) {
        createNewIfNeeded(context);
        return instance;
    }

    private static void createNewIfNeeded(Context context) {
        if (instance == null) {
            instance = new AppHeart(context);
        }
    }

    private AppHeart(Context context) {
        this.userSettingsComponentsFactory = new UserSettingsComponentsFactoryImpl();
        this.languageComponentsFactory = new LanguageComponentsFactoryImpl(context);
        this.navigator = new ActivityNavigator();
        this.context = context;
    }

}
