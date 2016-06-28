package com.olebokolo.wordstack.core.languages.flags;

import android.graphics.drawable.Drawable;

import com.olebokolo.wordstack.core.resources.drawables.DrawableService;

import lombok.Setter;


@Setter
public class FlagServiceImpl implements FlagService {

    private DrawableService drawableService;
    private String prefix = "flag_";
    private String extension = ".png";

    @Override
    public Drawable getFlagByLanguageShortName(String langShortName) {
        String fileName = getFileName(langShortName);
        return drawableService.getDrawableByName(fileName);
    }

    String getFileName(String langShortName) {
        return prefix + langShortName + extension;
    }


}
