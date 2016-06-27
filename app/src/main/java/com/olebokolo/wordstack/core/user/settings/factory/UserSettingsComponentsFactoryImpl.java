package com.olebokolo.wordstack.core.user.settings.factory;

import com.olebokolo.wordstack.core.user.settings.repositories.UserSettingsRepository;
import com.olebokolo.wordstack.core.user.settings.repositories.UserSettingsRepositoryImpl;
import com.olebokolo.wordstack.core.user.settings.services.UserSettingsService;
import com.olebokolo.wordstack.core.user.settings.services.UserSettingsServiceImpl;
import com.olebokolo.wordstack.core.utils.Comparator;

public class UserSettingsComponentsFactoryImpl implements UserSettingsComponentsFactory {

    @Override
    public UserSettingsRepository getUserSettingsRepository() {
        return new UserSettingsRepositoryImpl();
    }

    @Override
    public UserSettingsService getUserSettingsService() {
        UserSettingsRepository repository = getUserSettingsRepository();
        Comparator comparator = new Comparator();
        return new UserSettingsServiceImpl(repository, comparator);
    }
}
