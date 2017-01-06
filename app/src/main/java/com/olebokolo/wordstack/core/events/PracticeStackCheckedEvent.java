package com.olebokolo.wordstack.core.events;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class PracticeStackCheckedEvent {
    private int position;
    private boolean checked;
}
