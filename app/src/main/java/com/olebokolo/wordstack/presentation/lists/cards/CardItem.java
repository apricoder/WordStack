package com.olebokolo.wordstack.presentation.lists.cards;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor(suppressConstructorProperties = true)
public class CardItem {
    private Long id;
    private Integer frontLangFlagResource;
    private Integer backLangFlagResource;
    private String frontLangText;
    private String backLangText;
}
