package com.olebokolo.wordstack.core.languages.repositories;

import com.olebokolo.wordstack.core.model.Language;

import java.util.List;

public interface LanguageRepository {
    List<Language> getAll();
    Language findByShortName(String shortName);
}
