package com.olebokolo.wordstack.core.utils;

import android.content.Context;
import android.graphics.Typeface;

import lombok.Getter;

public class TypefaceCollection {

    private Context context;

    @Getter private Typeface ralewayLight;
    @Getter private Typeface ralewayThin;
    @Getter private Typeface ralewayMedium;

    private Typeface getFromAsset(final String fontName) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/" + fontName + ".ttf");
    }

    public TypefaceCollection(Context context) {
        this.context = context;
        this.ralewayLight = getFromAsset("raleway_light");
        this.ralewayThin = getFromAsset("raleway_thin");
        this.ralewayMedium = getFromAsset("raleway_medium");
    }
    
}
