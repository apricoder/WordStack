package com.olebokolo.wordstack.presentation.lists.stacks;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor(suppressConstructorProperties = true)
public class StackItem {
    private String stackName;
    private int cardsCount;
}
