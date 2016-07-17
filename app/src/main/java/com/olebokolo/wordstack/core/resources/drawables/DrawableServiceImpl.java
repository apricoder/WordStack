package com.olebokolo.wordstack.core.resources.drawables;

import android.content.Context;
import android.content.res.Resources;

import lombok.Setter;

@Setter
public class DrawableServiceImpl implements DrawableService{

    private Context context;
    private String type = "drawable";

    @Override
    public int getDrawableByName(String name) {
        Resources resources = context.getResources();
        String packageName = context.getPackageName();
        return resources.getIdentifier(name, type, packageName);
    }
}
