package com.olebokolo.wordstack.core.languages.repositories;

import com.olebokolo.wordstack.core.model.Language;
import com.orm.SugarRecord;

import java.util.List;

public class LanguageRepositoryImpl implements LanguageRepository {
    @Override
    public List<Language> getAll() {
        return Language.listAll(Language.class);
    }

    @Override
    public Language findByShortName(String shortName) {
        return SugarRecord.find(Language.class, "shortName = ?", shortName).get(0);
    }
}
