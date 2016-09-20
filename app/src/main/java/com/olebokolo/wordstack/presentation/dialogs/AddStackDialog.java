package com.olebokolo.wordstack.presentation.dialogs;

import android.app.Dialog;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.olebokolo.wordstack.R;
import com.olebokolo.wordstack.core.app.WordStack;
import com.olebokolo.wordstack.presentation.activities.StackListActivity;

public class AddStackDialog extends Dialog {

    private EditText stackNameField;
    private TextView addStackButton;
    private TextView backButton;

    public AddStackDialog(StackListActivity activity) {
        super(activity);
        getWindow().getAttributes().windowAnimations = R.style.WordStackDialogAnimation;
        WordStack.getInstance().injectDependenciesTo(this);
        setContentView(R.layout.dialog_add_stack);
        findViews();
        setupBackButton();
        setupAddStackButton();
    }

    private void setupAddStackButton() {
        addStackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void setupBackButton() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() { dismiss(); }
                }, 100);
            }
        });
    }

    private void findViews() {
        stackNameField = (EditText) findViewById(R.id.stack_name);
        addStackButton = (TextView) findViewById(R.id.add_stack_button);
        backButton = (TextView) findViewById(R.id.back_button);
    }
}
