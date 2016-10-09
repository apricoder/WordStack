package com.olebokolo.wordstack.presentation.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.olebokolo.wordstack.R;
import com.olebokolo.wordstack.core.app.WordStack;
import com.olebokolo.wordstack.core.model.Stack;
import com.olebokolo.wordstack.core.utils.TypefaceCollection;
import com.olebokolo.wordstack.core.utils.TypefaceManager;

public class StackActionsDialog extends Dialog {
    // dependencies
    public TypefaceCollection typefaceCollection;
    public TypefaceManager typefaceManager;
    // data
    private Context context;
    private Stack stack;

    public StackActionsDialog(Context context, Stack stack) {
        super(context);
        this.stack = stack;
        this.context = context;
        WordStack.getInstance().injectDependenciesTo(this);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        setContentView(R.layout.dialog_stack_actions);
        setupFonts();
        setupTitle();
        setupCloseButton();
        setupRenameButton();
        setupDeleteButton();
    }

    private void setupRenameButton() {
        findViewById(R.id.rename_stack_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                new Handler().postDelayed(new Runnable() { @Override public void run() { new StackRenameDialog(context, stack).show(); } }, 100);
            }
        });
    }

    private void setupDeleteButton() {
        findViewById(R.id.delete_stack_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() { new StackConfirmDeleteDialog(context, stack).show(); } }, 100);
            }
        });
    }

    private void setupCloseButton() {
        findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void setupTitle() {
        ((TextView)findViewById(R.id.dialog_title)).setText(stack.getName());
    }

    private void setupFonts() {
        typefaceManager.setTypefaceForContainer((ViewGroup) findViewById(R.id.root_layout), typefaceCollection.getRalewayMedium());
    }
}
