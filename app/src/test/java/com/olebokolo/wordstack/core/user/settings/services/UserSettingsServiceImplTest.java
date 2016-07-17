package com.olebokolo.wordstack.core.user.settings.services;

import com.olebokolo.wordstack.core.model.UserSettings;
import com.olebokolo.wordstack.core.user.settings.dao.UserSettingsDao;
import com.olebokolo.wordstack.core.utils.Comparator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class UserSettingsServiceImplTest {

    @Spy
    @InjectMocks
    private UserSettingsServiceImpl service;

    @Mock
    private UserSettingsDao repository;

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