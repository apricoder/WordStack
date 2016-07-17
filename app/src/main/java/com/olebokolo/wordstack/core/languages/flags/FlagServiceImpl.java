package com.olebokolo.wordstack.core.languages.flags;

import com.olebokolo.wordstack.core.resources.drawables.DrawableService;

import lombok.Setter;


@Setter
public class FlagServiceImpl implements FlagService {

    private DrawableService drawableService;
    private String prefix = "flag_";

    @Override
    public int getFlagByLanguageShortName(String langShortName) {
        String fileName = getFileName(langShortName);
        return drawableService.getDrawableByName(fileName);
    }

    String getFileName(String langShortName) {
        return prefix + langShortName;
    }


}
