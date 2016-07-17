package com.olebokolo.wordstack.core.user.settings.dao;

import com.olebokolo.wordstack.core.model.UserSettings;

import java.util.List;

public class UserSettingsDaoImpl implements UserSettingsDao {

    @Override
    public UserSettings getUserSettings() {
        List<UserSettings> settingsList = UserSettings.listAll(UserSettings.class);
        return  settingsList.isEmpty() ? null : settingsList.get(0);
    }

}
