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
import com.olebokolo.wordstack.core.events.StackDetailsRequestedEvent;
import com.olebokolo.wordstack.core.model.Stack;
import com.olebokolo.wordstack.core.utils.TypefaceCollection;
import com.olebokolo.wordstack.core.utils.TypefaceManager;

import org.greenrobot.eventbus.EventBus;

public class StackActionsDialog extends Dialog {

    // constants
    private static final int DELAY_MILLIS = 100;
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
        setupEditCardsButton();
        setupCloseButton();
        setupRenameButton();
        setupDeleteButton();
    }

    private void setupEditCardsButton() {
        findViewById(R.id.edit_stack_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        EventBus.getDefault().post(StackDetailsRequestedEvent.builder().stack(stack).build());
                    }
                }, DELAY_MILLIS);
            }
        });
    }

    private void setupRenameButton() {
        findViewById(R.id.rename_stack_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new StackEditDialog(context, stack).show();
                    }
                }, DELAY_MILLIS);
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
                    public void run() {
                        new StackConfirmDeleteDialog(context, stack).show();
                    }
                }, DELAY_MILLIS);
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
        ((TextView) findViewById(R.id.dialog_title)).setText(stack.getName());
    }

    private void setupFonts() {
        typefaceManager.setTypefaceForContainer((ViewGroup) findViewById(R.id.root_layout), typefaceCollection.getRalewayMedium());
    }
}
