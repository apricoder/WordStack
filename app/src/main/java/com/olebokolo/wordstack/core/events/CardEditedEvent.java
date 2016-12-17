package com.olebokolo.wordstack.core.events;

import com.olebokolo.wordstack.core.model.Card;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class CardEditedEvent {
    private Card card;
}
