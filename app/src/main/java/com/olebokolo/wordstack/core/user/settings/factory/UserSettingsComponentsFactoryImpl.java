package com.olebokolo.wordstack.core.user.settings.factory;

import com.olebokolo.wordstack.core.user.settings.dao.UserSettingsDao;
import com.olebokolo.wordstack.core.user.settings.dao.UserSettingsDaoImpl;
import com.olebokolo.wordstack.core.user.settings.services.UserSettingsService;
import com.olebokolo.wordstack.core.user.settings.services.UserSettingsServiceImpl;
import com.olebokolo.wordstack.core.utils.Comparator;

public class UserSettingsComponentsFactoryImpl implements UserSettingsComponentsFactory {

    @Override
    public UserSettingsDao getUserSettingsDao() {
        return new UserSettingsDaoImpl();
    }

    @Override
    public UserSettingsService getUserSettingsService() {
        UserSettingsDao repository = getUserSettingsDao();
        Comparator comparator = new Comparator();
        return new UserSettingsServiceImpl(repository, comparator);
    }
}
