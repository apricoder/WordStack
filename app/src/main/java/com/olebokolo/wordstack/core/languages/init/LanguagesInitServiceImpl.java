package com.olebokolo.wordstack.core.languages.init;

import android.content.Context;

import com.olebokolo.wordstack.R;
import com.olebokolo.wordstack.core.languages.services.LanguageService;
import com.olebokolo.wordstack.core.model.Language;
import com.olebokolo.wordstack.core.utils.Comparator;

import lombok.Setter;

@Setter
public class LanguagesInitServiceImpl implements LanguagesInitService {

    private LanguageService languageService;
    private LanguageRowParser languageRowParser;
    private Comparator comparator;
    private Context context;

    void initLanguages() {
        String[] languageRows = getLanguageRowsFromResources();
        for (String row : languageRows) {
            Language language = languageRowParser.parse(row);
            saveIfNotFound(language);
        }
    }

    private void saveIfNotFound(Language language) {
        Language foundInDatabase = languageService.findByShortName(language.getShortName());
        if (comparator.saysNull(foundInDatabase)) {
            language.save();
        }
    }

    private String[] getLanguageRowsFromResources() {
        return context.getResources().getStringArray(R.array.languages);
    }

    @Override
    public void initLanguagesInDatabaseIfNeeded() {
        if (languageService.noLanguagesInDatabase()) {
            initLanguages();
        }
    }
}
