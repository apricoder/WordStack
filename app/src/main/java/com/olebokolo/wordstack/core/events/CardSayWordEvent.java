package com.olebokolo.wordstack.core.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(suppressConstructorProperties = true)
public class CardSayWordEvent {

    public final static int FRONT_LANG = 0;
    public final static int BACK_LANG = 1;

    private String text;
    private int side;
}
