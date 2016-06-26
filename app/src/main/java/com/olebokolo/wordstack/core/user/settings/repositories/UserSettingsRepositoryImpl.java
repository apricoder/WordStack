package com.olebokolo.wordstack.core.user.settings.repositories;

import com.olebokolo.wordstack.core.model.UserSettings;

import java.util.List;

public class UserSettingsRepositoryImpl implements UserSettingsRepository{

    @Override
    public UserSettings getUserSettings() {
        List<UserSettings> settingsList = UserSettings.listAll(UserSettings.class);
        return  settingsList.isEmpty() ? null : settingsList.get(0);
    }

}
