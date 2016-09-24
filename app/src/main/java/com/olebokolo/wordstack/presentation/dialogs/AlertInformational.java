package com.olebokolo.wordstack.presentation.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;

import com.olebokolo.wordstack.R;

public class AlertInformational extends Dialog {

    public AlertInformational(Context context, String title, Spanned content) {
        super(context);
        getWindow().getAttributes().windowAnimations = R.style.FadeDialogAnimation;
        setContentView(R.layout.dialog_informational_alert);
        setupTitle(title);
        setupContent(content);
        setupOkButton();
        setupCloseButton();
    }

    private void setupTitle(String title) {
        ((TextView)findViewById(R.id.alert_title)).setText(title);
    }

    private void setupContent(Spanned content) {
        ((TextView)findViewById(R.id.alert_content)).setText(content);
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
