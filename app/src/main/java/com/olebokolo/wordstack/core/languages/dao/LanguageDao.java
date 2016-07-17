package com.olebokolo.wordstack.core.languages.dao;

import com.olebokolo.wordstack.core.model.Language;

import java.util.List;

public interface LanguageDao {
    List<Language> getAll();
    Language findByShortName(String shortName);
}
