package com.olebokolo.wordstack.core.user.settings.services;

import com.olebokolo.wordstack.core.user.settings.repositories.UserSettingsRepository;
import com.olebokolo.wordstack.core.utils.Comparator;

import lombok.AllArgsConstructor;

@AllArgsConstructor(suppressConstructorProperties = true)
public class UserSettingsServiceImpl implements UserSettingsService{

    private UserSettingsRepository settingsRepository;
    private Comparator comparator;

    @Override
    public boolean userChoseLanguages() {
        return comparator.saysNotNull(settingsRepository.getUserSettings());
    }

}
