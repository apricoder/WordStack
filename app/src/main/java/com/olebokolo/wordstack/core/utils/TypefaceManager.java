package com.olebokolo.wordstack.core.utils;

import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class TypefaceManager {

    public void setTypefaceForContainer(ViewGroup contentLayout, Typeface typeface) {
        for (int i=0; i < contentLayout.getChildCount(); i++) {
            View view = contentLayout.getChildAt(i);
            if (view instanceof TextView)
                setTypeface((TextView) view, typeface);
            else if (view instanceof ViewGroup)
                setTypefaceForContainer((ViewGroup) view, typeface);
        }
    }

    public void setTypeface(TextView textView, Typeface typeface) {
        textView.setTypeface(typeface);
    }

    public void setTypeface(Button button, Typeface typeface) {
        button.setTypeface(typeface);
    }
}
