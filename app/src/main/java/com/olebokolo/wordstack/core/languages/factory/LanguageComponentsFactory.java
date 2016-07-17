package com.olebokolo.wordstack.core.languages.factory;

import com.olebokolo.wordstack.core.languages.dao.LanguageDao;
import com.olebokolo.wordstack.core.languages.flags.FlagService;
import com.olebokolo.wordstack.core.languages.init.LanguagesInitService;
import com.olebokolo.wordstack.core.languages.services.LanguageService;

public interface LanguageComponentsFactory {
    LanguageService getLanguageService();
    LanguageDao getLanguageDao();
    LanguagesInitService getLanguagesInitService();
    FlagService getFlagService();
}
