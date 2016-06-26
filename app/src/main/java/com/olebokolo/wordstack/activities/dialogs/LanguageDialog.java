package com.olebokolo.wordstack.activities.dialogs;

import android.app.Dialog;
import android.widget.TextView;

import com.olebokolo.wordstack.R;
import com.olebokolo.wordstack.activities.LanguagesActivity;

import lombok.Setter;

public class LanguageDialog extends Dialog {

    @Setter
    private LanguagesActivity parent;

    public LanguageDialog(LanguagesActivity activity, String title) {
        super(activity);
        setParent(activity);
        setContentView(R.layout.dialog_language);
        setTitleText(title);
    }

    private void setTitleText(String titleText) {
        TextView dialogTitle = (TextView) findViewById(R.id.dialog_title);
        dialogTitle.setText(titleText);
    }

}
