package com.olebokolo.wordstack.core.languages.services;

import android.support.annotation.NonNull;

import com.olebokolo.wordstack.core.languages.repositories.LanguageRepository;
import com.olebokolo.wordstack.core.model.Language;
import com.olebokolo.wordstack.core.utils.Comparator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LanguageServiceImplTest {

    @Spy
    @InjectMocks
    private LanguageServiceImpl service;

    @Mock
    private LanguageRepository repository;

    @Spy
    private Comparator comparator;

    @Test
    public void areLanguagesInDatabase_should_call_repository() throws Exception {
        service.areLanguagesInDatabase();
        verify(repository).getAll();
    }

    @Test
    public void areLanguagesInDatabase_returns_true_if_languages_found() throws Exception {
        List<Language> twoLanguagesList = getTwoLanguagesList();
        makeRepositoryReturnAsAllLanguages(twoLanguagesList);
        assertTrue(service.areLanguagesInDatabase());
    }

    @Test
    public void areLanguagesInDatabase_returns_false_if_no_languages_found() throws Exception {
        List<Language> emptyList = Collections.emptyList();
        makeRepositoryReturnAsAllLanguages(emptyList);
        assertFalse(service.areLanguagesInDatabase());
    }

    @Test
    public void getAll_should_call_repository() throws Exception {
        service.getAllLanguages();
        verify(repository).getAll();
    }

    private void makeRepositoryReturnAsAllLanguages(List<Language> languages) {
        doReturn(languages).when(repository).getAll();
    }

    @NonNull
    private List<Language> getTwoLanguagesList() {
        return Arrays.asList(new Language(), new Language());
    }
}