package com.olebokolo.wordstack.core.events;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class LanguageAvailabilityDiscoveredEvent {
    private int language;
    private boolean supported;
}
