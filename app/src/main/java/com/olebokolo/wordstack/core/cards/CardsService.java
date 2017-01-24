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

public class CardsService {

    public FlagService flagService;
    public LanguageService languageService;
    private Language frontLanguage;
    private Language backLanguage;

    public List<CardItem> getCardItemsFrom(List<Card> cards) {
        List<CardItem> cardItems = new ArrayList<>();
        for (Card card : cards) cardItems.add(getCardItemFrom(card));
        return cardItems;
    }

    private void initLanguagesOf(Card card) {
        if (frontLanguage == null && backLanguage == null && card != null) {
            Side frontSide = SugarRecord.findById(Side.class, card.getFrontSideId());
            Side backSide = SugarRecord.findById(Side.class, card.getBackSideId());
            frontLanguage = languageService.findById(frontSide.getLanguageId());
            backLanguage = languageService.findById(backSide.getLanguageId());
        }
    }

    public CardItem getCardItemFrom(Card card) {
        initLanguagesOf(card);
        Side frontSide = SugarRecord.findById(Side.class, card.getFrontSideId());
        Side backSide = SugarRecord.findById(Side.class, card.getBackSideId());
        return CardItem.builder()
                .frontLangText(frontSide.getContent())
                .backLangText(backSide.getContent())
                .frontLangFlagResource(getFlagFor(frontLanguage))
                .backLangFlagResource(getFlagFor(backLanguage))
                .build();
    }

    private int getFlagFor(Language language) {
        return flagService.getFlagByLanguageShortName(language.getShortName());
    }

}
