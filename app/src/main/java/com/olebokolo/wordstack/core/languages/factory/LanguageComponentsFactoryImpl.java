package com.olebokolo.wordstack.core.languages.factory;

import com.olebokolo.wordstack.core.app.WordStack;
import com.olebokolo.wordstack.core.languages.dao.LanguageDaoImpl;
import com.olebokolo.wordstack.core.languages.flags.FlagServiceImpl;
import com.olebokolo.wordstack.core.languages.init.LanguageRowParser;
import com.olebokolo.wordstack.core.languages.init.LanguagesInitServiceImpl;
import com.olebokolo.wordstack.core.languages.services.LanguageService;
import com.olebokolo.wordstack.core.languages.services.LanguageServiceImpl;
import com.olebokolo.wordstack.core.resources.drawables.DrawableService;
import com.olebokolo.wordstack.core.resources.factory.DrawableComponentsFactory;
import com.olebokolo.wordstack.core.utils.Comparator;

import lombok.Getter;

public class LanguageComponentsFactoryImpl implements LanguageComponentsFactory {

    @Getter private LanguagesInitServiceImpl languagesInitService;
    @Getter private LanguageDaoImpl languageDao;
    @Getter private FlagServiceImpl flagService;
    private LanguageServiceImpl languageService;
    private DrawableService drawableService;
    private LanguageRowParser languageRowParser;
    private Comparator comparator;
    private WordStack application;

    @Override
    public LanguageService getLanguageService() {
        languagesInitService.initLanguagesInDatabaseIfNeeded();
        return languageService;
    }

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
        languageDao = new LanguageDaoImpl();
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
        languageService.setDao(languageDao);
        languageService.setComparator(comparator);
    }

    private void setupDrawableService() {
        DrawableComponentsFactory factory = application.getDrawableComponentsFactory();
        drawableService = factory.getDrawableService();
    }

    private void setupFlagService() {
        flagService.setDrawableService(drawableService);
    }
}
