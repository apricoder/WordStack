package com.olebokolo.wordstack.core.languages.services;

import com.olebokolo.wordstack.core.model.Language;

import java.util.List;
import java.util.Locale;

public interface LanguageService {
    boolean areLanguagesInDatabase();
    boolean noLanguagesInDatabase();
    List<Language> getAllLanguages();
    Language findByShortName(String shortName);
    Language findById(Long id);
    Locale getLocaleForLanguage(Language language);
}
