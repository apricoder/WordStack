package com.olebokolo.wordstack.core.resources.drawables;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import lombok.Setter;

@Setter
public class DrawableServiceImpl implements DrawableService{

    private Context context;
    private String type = "drawable";

    @Override
    @SuppressWarnings("deprecation")
    public Drawable getDrawableByName(String name) {
        Resources resources = context.getResources();
        String packageName = context.getPackageName();
        Integer resourceId = resources.getIdentifier(name, type, packageName);
        return resources.getDrawable(resourceId);
    }
}
