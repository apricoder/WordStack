package com.olebokolo.wordstack.core.languages.init;

import com.olebokolo.wordstack.core.model.Language;

public class LanguageRowParser {

    public Language parse(String languageRow) {
        String langShortName = getLanguageShortName(languageRow);
        String langName = getLanguageName(languageRow);
        return buildLanguage(langShortName, langName);
    }

    private String getLanguageName(String languageRow) {
        return languageRow.split("_")[1];
    }

    private String getLanguageShortName(String languageRow) {
        return languageRow.split("_")[0];
    }

    private Language buildLanguage(String langShortName, String langName) {
        return Language.builder()
                .shortName(langShortName)
                .name(langName)
                .build();
    }
}
