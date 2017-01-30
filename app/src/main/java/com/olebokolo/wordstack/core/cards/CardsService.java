package com.olebokolo.wordstack.core.cards;

import com.olebokolo.wordstack.core.languages.flags.FlagService;
import com.olebokolo.wordstack.core.languages.services.LanguageService;
import com.olebokolo.wordstack.core.model.Card;
import com.olebokolo.wordstack.core.model.Language;
import com.olebokolo.wordstack.core.model.Side;
import com.olebokolo.wordstack.presentation.lists.cards.CardItem;
import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public class CardsService {

    public FlagService flagService;
    public LanguageService languageService;

    public List<CardItem> getCardItemsFrom(List<Card> cards) {
        LanguagePair languagePair = getLanguagesOf(cards);
        List<CardItem> cardItems = new ArrayList<>();
        for (Card card : cards) cardItems.add(getCardItemFrom(card, languagePair));
        return cardItems;
    }

    private LanguagePair getLanguagesOf(List<Card> cards) {
        if (!cards.isEmpty()) {
            Card card = cards.get(0);
            Side frontSide = SugarRecord.findById(Side.class, card.getFrontSideId());
            Side backSide = SugarRecord.findById(Side.class, card.getBackSideId());
            Language frontLanguage = languageService.findById(frontSide.getLanguageId());
            Language backLanguage = languageService.findById(backSide.getLanguageId());
            return new LanguagePair(frontLanguage, backLanguage);
        } else return new LanguagePair();
    }

    private CardItem getCardItemFrom(Card card, LanguagePair languagePair) {
        Side frontSide = SugarRecord.findById(Side.class, card.getFrontSideId());
        Side backSide = SugarRecord.findById(Side.class, card.getBackSideId());
        return CardItem.builder()
                .id(card.getId())
                .frontLangText(frontSide.getContent())
                .backLangText(backSide.getContent())
                .frontLangFlagResource(getFlagFor(languagePair.frontLanguage))
                .backLangFlagResource(getFlagFor(languagePair.backLanguage))
                .build();
    }

    private int getFlagFor(Language language) {
        return flagService.getFlagByLanguageShortName(language.getShortName());
    }

    @NoArgsConstructor
    @AllArgsConstructor(suppressConstructorProperties = true)
    private class LanguagePair {
        Language frontLanguage;
        Language backLanguage;
    }

}
