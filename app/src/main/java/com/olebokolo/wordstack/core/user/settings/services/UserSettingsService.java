package com.olebokolo.wordstack.core.user.settings.services;

import com.olebokolo.wordstack.core.model.UserSettings;

public interface UserSettingsService {
    boolean userChoseLanguages();
    UserSettings getUserSettings();
}
