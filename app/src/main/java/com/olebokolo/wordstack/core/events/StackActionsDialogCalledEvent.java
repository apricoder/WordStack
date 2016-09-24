package com.olebokolo.wordstack.core.events;

import lombok.Data;

@Data
public class StackActionsDialogCalledEvent extends BaseStackEvent {
    private int stackPosition;
    public StackActionsDialogCalledEvent(String message, int stackPosition) {
        super(message);
        this.stackPosition = stackPosition;
    }
}
