package com.olebokolo.wordstack.core.user.settings.services;

import com.olebokolo.wordstack.core.user.settings.dao.UserSettingsDao;
import com.olebokolo.wordstack.core.utils.Comparator;

import lombok.AllArgsConstructor;

@AllArgsConstructor(suppressConstructorProperties = true)
public class UserSettingsServiceImpl implements UserSettingsService{

    private UserSettingsDao settingsRepository;
    private Comparator comparator;

    @Override
    public boolean userChoseLanguages() {
        return comparator.saysNotNull(settingsRepository.getUserSettings());
    }

}
