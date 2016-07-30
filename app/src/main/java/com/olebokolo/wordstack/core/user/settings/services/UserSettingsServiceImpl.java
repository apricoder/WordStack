package com.olebokolo.wordstack.core.user.settings.services;

import com.olebokolo.wordstack.core.model.UserSettings;
import com.olebokolo.wordstack.core.user.settings.dao.UserSettingsDao;
import com.olebokolo.wordstack.core.utils.Comparator;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor(suppressConstructorProperties = true)
public class UserSettingsServiceImpl implements UserSettingsService{

    private UserSettingsDao settingsRepository;
    private Comparator comparator;

    @Override
    public boolean userChoseLanguages() {
        return comparator.saysNotNull(settingsRepository.getUserSettings());
    }

    @Override
    public UserSettings getUserSettings() {
        List<UserSettings> settingsList = UserSettings.listAll(UserSettings.class);
        return comparator.saysNotEmpty(settingsList) ? settingsList.get(0) : null;
    }

}
