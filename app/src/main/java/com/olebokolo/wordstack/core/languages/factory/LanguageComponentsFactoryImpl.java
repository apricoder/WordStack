package com.olebokolo.wordstack.core.languages.factory;

import com.olebokolo.wordstack.core.app.WordStack;
import com.olebokolo.wordstack.core.languages.init.LanguageRowParser;
import com.olebokolo.wordstack.core.languages.init.LanguagesInitService;
import com.olebokolo.wordstack.core.languages.init.LanguagesInitServiceImpl;
import com.olebokolo.wordstack.core.languages.flags.FlagServiceImpl;
import com.olebokolo.wordstack.core.languages.repositories.LanguageRepository;
import com.olebokolo.wordstack.core.languages.repositories.LanguageRepositoryImpl;
import com.olebokolo.wordstack.core.languages.services.LanguageService;
import com.olebokolo.wordstack.core.languages.services.LanguageServiceImpl;
import com.olebokolo.wordstack.core.resources.drawables.DrawableService;
import com.olebokolo.wordstack.core.resources.factory.DrawableComponentsFactory;
import com.olebokolo.wordstack.core.utils.Comparator;

public class LanguageComponentsFactoryImpl implements LanguageComponentsFactory {

    private LanguagesInitServiceImpl languagesInitService;
    private LanguageServiceImpl languageService;
    private LanguageRepositoryImpl languageRepository;
    private FlagServiceImpl flagService;
    private DrawableService drawableService;
    private LanguageRowParser languageRowParser;
    private Comparator comparator;
    private WordStack application;

    public LanguageComponentsFactoryImpl() {
        initFields();
        setupLanguageInitService();
        setupLanguageService();
        setupDrawableService();
        setupFlagService();
    }

    private void initFields() {
        languagesInitService = new LanguagesInitServiceImpl();
        languageService = new LanguageServiceImpl();
        languageRepository = new LanguageRepositoryImpl();
        languageRowParser = new LanguageRowParser();
        flagService = new FlagServiceImpl();
        comparator = new Comparator();
        application = WordStack.getInstance();
    }

    private void setupLanguageInitService() {
        languagesInitService.setLanguageService(languageService);
        languagesInitService.setLanguageRowParser(languageRowParser);
        languagesInitService.setComparator(comparator);
        languagesInitService.setContext(application);
    }

    private void setupLanguageService() {
        languageService.setInitService(languagesInitService);
        languageService.setRepository(languageRepository);
        languageService.setComparator(comparator);
    }

    private void setupDrawableService() {
        DrawableComponentsFactory factory = application.getDrawableComponentsFactory();
        drawableService = factory.getDrawableService();
    }

    private void setupFlagService() {
        flagService.setDrawableService(drawableService);
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
