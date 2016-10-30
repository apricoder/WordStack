package com.olebokolo.wordstack.core.events;

import com.olebokolo.wordstack.core.model.Stack;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class StackDetailsRequestedEvent {
    private Stack stack;
}
