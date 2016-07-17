package com.olebokolo.wordstack.core.languages.dao;

import com.olebokolo.wordstack.core.model.Language;
import com.olebokolo.wordstack.core.utils.Comparator;
import com.orm.SugarRecord;

import java.util.List;

public class LanguageDaoImpl implements LanguageDao {

    private Comparator comparator;

    public LanguageDaoImpl() {
        comparator = new Comparator();
    }

    @Override
    public List<Language> getAll() {
        return Language.listAll(Language.class);
    }

    @Override
    public Language findByShortName(String shortName) {
        List<Language> languages = SugarRecord.find(Language.class, "short_Name = ?", shortName);
        return comparator.saysNotEmpty(languages) ? languages.get(0) : null;
    }
}
