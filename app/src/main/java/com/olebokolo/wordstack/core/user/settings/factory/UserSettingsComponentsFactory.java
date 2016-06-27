package com.olebokolo.wordstack.core.user.settings.factory;

import com.olebokolo.wordstack.core.user.settings.repositories.UserSettingsRepository;
import com.olebokolo.wordstack.core.user.settings.services.UserSettingsService;

public interface UserSettingsComponentsFactory {
    UserSettingsRepository getUserSettingsRepository();
    UserSettingsService getUserSettingsService();
}
