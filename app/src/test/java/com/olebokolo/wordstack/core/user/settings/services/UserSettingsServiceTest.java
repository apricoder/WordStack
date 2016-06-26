package com.olebokolo.wordstack.core.user.settings.services;

import com.olebokolo.wordstack.core.model.UserSettings;
import com.olebokolo.wordstack.core.user.settings.repositories.UserSettingsRepository;
import com.olebokolo.wordstack.core.utils.Comparator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserSettingsServiceTest {

    @Spy
    @InjectMocks
    private UserSettingsServiceImpl service;

    @Mock
    private UserSettingsRepository repository;

    @Spy
    private Comparator comparator;

    @Test
    public void userChoseLanguages_returns_true_if_repository_finds_user_settings() throws Exception {
        UserSettings sampleSettings = getSampleSettings();
        makeRepositoryReturnAsUserSettings(sampleSettings);
        assertTrue(service.userChoseLanguages());
    }

    @Test
    public void userChoseLanguages_returns_false_if_repository_finds_no_user_settings() throws Exception {
        makeRepositoryReturnAsUserSettings(null);
        assertFalse(service.userChoseLanguages());
    }

    private void makeRepositoryReturnAsUserSettings(UserSettings settings) {
        doReturn(settings).when(repository).getUserSettings();
    }

    private UserSettings getSampleSettings() {
        return UserSettings.builder().build();
    }


}