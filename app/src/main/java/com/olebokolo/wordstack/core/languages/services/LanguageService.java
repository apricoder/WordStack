package com.olebokolo.wordstack.core.languages.services;

import com.olebokolo.wordstack.core.model.Language;

import java.util.List;

public interface LanguageService {
    boolean areLanguagesInDatabase();
    boolean noLanguagesInDatabase();
    List<Language> getAllLanguages();
    Language findByShortName(String shortName);
}
