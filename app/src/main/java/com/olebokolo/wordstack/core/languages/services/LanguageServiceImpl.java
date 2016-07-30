package com.olebokolo.wordstack.core.languages.services;

import com.olebokolo.wordstack.core.languages.dao.LanguageDao;
import com.olebokolo.wordstack.core.languages.init.LanguagesInitService;
import com.olebokolo.wordstack.core.model.Language;
import com.olebokolo.wordstack.core.utils.Comparator;

import java.util.List;

import lombok.Setter;

@Setter
public class LanguageServiceImpl implements LanguageService{

    private LanguageDao dao;
    private LanguagesInitService initService;
    private Comparator comparator;

    @Override
    public boolean areLanguagesInDatabase() {
        List<Language> languageList = dao.getAll();
        return comparator.saysNotEmpty(languageList);
    }

    @Override
    public boolean noLanguagesInDatabase() {
        return !areLanguagesInDatabase();
    }

    @Override
    public List<Language> getAllLanguages() {
        initService.initLanguagesInDatabaseIfNeeded();
        return dao.getAll();
    }

    @Override
    public Language findByShortName(String shortName) {
        return dao.findByShortName(shortName);
    }

    @Override
    public Language findById(Long id) {
        return Language.findById(Language.class, id);
    }

}
