package com.olebokolo.wordstack.core.user.settings.factory;

import com.olebokolo.wordstack.core.user.settings.dao.UserSettingsDao;
import com.olebokolo.wordstack.core.user.settings.services.UserSettingsService;

public interface UserSettingsComponentsFactory {
    UserSettingsDao getUserSettingsDao();
    UserSettingsService getUserSettingsService();
}
