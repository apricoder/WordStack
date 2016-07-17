package com.olebokolo.wordstack.presentation.lists.languages;

import com.olebokolo.wordstack.core.model.Language;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor(suppressConstructorProperties = true)
public class LanguageItem {
    private Language language;
    private int flagId;
}
