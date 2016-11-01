package com.olebokolo.wordstack.core.events;

import com.olebokolo.wordstack.core.model.Stack;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StackDetailsRequestedEvent {
    private Stack stack;
    private Integer position;
}
