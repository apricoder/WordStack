package com.olebokolo.wordstack.core.languages.services;

import com.olebokolo.wordstack.core.languages.init.LanguagesInitService;
import com.olebokolo.wordstack.core.languages.repositories.LanguageRepository;
import com.olebokolo.wordstack.core.model.Language;
import com.olebokolo.wordstack.core.utils.Comparator;

import java.util.List;

import lombok.Setter;

@Setter
public class LanguageServiceImpl implements LanguageService{

    private LanguageRepository repository;
    private LanguagesInitService initService;
    private Comparator comparator;

    @Override
    public boolean areLanguagesInDatabase() {
        List<Language> languageList = repository.getAll();
        return comparator.saysNotEmpty(languageList);
    }

    @Override
    public boolean noLanguagesInDatabase() {
        return !areLanguagesInDatabase();
    }

    @Override
    public List<Language> getAllLanguages() {
        initService.initLanguagesInDatabaseIfNeeded();
        return repository.getAll();
    }

    @Override
    public Language findByShortName(String shortName) {
        return repository.findByShortName(shortName);
    }

}
