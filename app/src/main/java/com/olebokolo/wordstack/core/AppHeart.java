package com.olebokolo.wordstack.core;

import com.olebokolo.wordstack.core.utils.ActivityNavigator;
import com.olebokolo.wordstack.core.user.settings.factory.UserSettingsFactory;
import com.olebokolo.wordstack.core.user.settings.factory.UserSettingsFactoryImpl;

import lombok.Getter;

public class AppHeart {

    @Getter private UserSettingsFactory userSettingsFactory;
    @Getter private ActivityNavigator navigator;
    @Getter private static AppHeart instance = new AppHeart();

    private AppHeart() {
        userSettingsFactory = new UserSettingsFactoryImpl();
        navigator = new ActivityNavigator();
    }

}
