package com.olebokolo.wordstack.core.languages.factory;

import android.content.Context;

import com.olebokolo.wordstack.core.languages.init.LanguageRowParser;
import com.olebokolo.wordstack.core.languages.init.LanguagesInitService;
import com.olebokolo.wordstack.core.languages.init.LanguagesInitServiceImpl;
import com.olebokolo.wordstack.core.languages.repositories.LanguageRepository;
import com.olebokolo.wordstack.core.languages.repositories.LanguageRepositoryImpl;
import com.olebokolo.wordstack.core.languages.services.LanguageService;
import com.olebokolo.wordstack.core.languages.services.LanguageServiceImpl;
import com.olebokolo.wordstack.core.utils.Comparator;

public class LanguageComponentsFactoryImpl implements LanguageComponentsFactory {

    private LanguagesInitServiceImpl languagesInitService;
    private LanguageServiceImpl languageService;
    private LanguageRepositoryImpl languageRepository;
    private LanguageRowParser languageRowParser;
    private Comparator comparator;
    private Context context;

    public LanguageComponentsFactoryImpl(Context context) {
        initFields(context);
        setupLanguageInitService();
        setupLanguageService();
    }

    private void initFields(Context context) {
        this.languagesInitService = new LanguagesInitServiceImpl();
        this.languageService = new LanguageServiceImpl();
        this.languageRepository = new LanguageRepositoryImpl();
        this.languageRowParser = new LanguageRowParser();
        this.comparator = new Comparator();
        this.context = context;
    }

    private void setupLanguageInitService() {
        languagesInitService.setLanguageService(languageService);
        languagesInitService.setLanguageRowParser(languageRowParser);
        languagesInitService.setComparator(comparator);
        languagesInitService.setContext(context);
    }

    private void setupLanguageService() {
        languageService.setInitService(languagesInitService);
        languageService.setRepository(languageRepository);
        languageService.setComparator(comparator);
    }

    @Override
    public LanguagesInitService getLanguagesInitService() {
        return languagesInitService;
    }

    @Override
    public LanguageService getLanguageService() {
        return languageService;
    }

    @Override
    public LanguageRepository getLanguageRepository() {
        return languageRepository;
    }
}
