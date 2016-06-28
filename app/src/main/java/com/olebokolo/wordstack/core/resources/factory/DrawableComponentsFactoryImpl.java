package com.olebokolo.wordstack.core.resources.factory;

import android.content.Context;

import com.olebokolo.wordstack.core.app.WordStack;
import com.olebokolo.wordstack.core.resources.drawables.DrawableService;
import com.olebokolo.wordstack.core.resources.drawables.DrawableServiceImpl;

public class DrawableComponentsFactoryImpl implements DrawableComponentsFactory{

    private DrawableServiceImpl drawableService;
    private Context context;

    public DrawableComponentsFactoryImpl() {
        initFields();
        setupDrawableService();
    }

    private void initFields() {
        drawableService = new DrawableServiceImpl();
        context = WordStack.getInstance();
    }

    private void setupDrawableService() {
        drawableService.setContext(context);
    }

    @Override
    public DrawableService getDrawableService() {
        return drawableService;
    }
}
