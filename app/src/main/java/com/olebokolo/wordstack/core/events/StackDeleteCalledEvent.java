package com.olebokolo.wordstack.core.events;

import com.olebokolo.wordstack.core.model.Stack;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@AllArgsConstructor(suppressConstructorProperties = true)
@EqualsAndHashCode(callSuper = true)
public class StackDeleteCalledEvent extends BaseStackEvent {
    private Stack stack;
}
