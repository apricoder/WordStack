package com.olebokolo.wordstack.presentation.lists.stacks;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor(suppressConstructorProperties = true)
@RequiredArgsConstructor(suppressConstructorProperties = true)
public class PracticeStackItem {
    @NonNull private Long id;
    @NonNull private String stackName;
    @NonNull private int cardsCount;
    private boolean checked;
}
