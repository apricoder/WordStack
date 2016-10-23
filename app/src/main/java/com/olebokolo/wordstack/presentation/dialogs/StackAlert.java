package com.olebokolo.wordstack.presentation.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.olebokolo.wordstack.R;
import com.olebokolo.wordstack.core.app.WordStack;
import com.olebokolo.wordstack.core.utils.TypefaceCollection;
import com.olebokolo.wordstack.core.utils.TypefaceManager;

public class StackAlert extends Dialog {

    public TypefaceCollection typefaceCollection;
    public TypefaceManager typefaceManager;

    public StackAlert(Context context, String title, Spanned content) {
        super(context);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().getAttributes().windowAnimations = R.style.FadeDialogAnimation;
        WordStack.getInstance().injectDependenciesTo(this);
        setContentView(R.layout.dialog_informational_alert);
        setupFonts();
        setupTitle(title);
        setupContent(content);
        setupOkButton();
        setupCloseButton();
    }

    private void setupFonts() {
        typefaceManager.setTypefaceForContainer((ViewGroup) findViewById(R.id.root_layout), typefaceCollection.getRalewayMedium());
    }

    private void setupTitle(String title) {
        ((TextView)findViewById(R.id.dialog_title)).setText(title);
    }

    private void setupContent(Spanned content) {
        ((TextView)findViewById(R.id.dialog_content )).setText(content);
    }

    private void setupCloseButton() {
        findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeAlert();
            }
        });
    }

    private void setupOkButton() {
        findViewById(R.id.ok_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeAlert();
            }
        });
    }

    private void closeAlert() {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() { dismiss(); }
        }, 100);
    }

}
