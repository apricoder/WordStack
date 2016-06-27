package com.olebokolo.wordstack.core.languages.factory;

import com.olebokolo.wordstack.core.languages.init.LanguagesInitService;
import com.olebokolo.wordstack.core.languages.repositories.LanguageRepository;
import com.olebokolo.wordstack.core.languages.services.LanguageService;

public interface LanguageComponentsFactory {
    LanguageService getLanguageService();
    LanguageRepository getLanguageRepository();
    LanguagesInitService getLanguagesInitService();
}
