package com.olebokolo.wordstack.core.utils;

import java.util.Collection;

public class Comparator {

    public boolean saysNotNull(Object object) {
        return object != null;
    }

    public boolean saysNotEmpty(Collection collection) {
        return !collection.isEmpty();
    }

    public boolean saysNull(Object object) {
        return object == null;
    }
}
