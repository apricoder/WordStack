package com.olebokolo.wordstack.presentation.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.olebokolo.wordstack.R;
import com.olebokolo.wordstack.core.app.WordStack;
import com.olebokolo.wordstack.core.events.StackRenamedEvent;
import com.olebokolo.wordstack.core.model.Stack;
import com.olebokolo.wordstack.core.utils.TypefaceCollection;
import com.olebokolo.wordstack.core.utils.TypefaceManager;
import com.orm.SugarRecord;

import org.greenrobot.eventbus.EventBus;

public class StackRenameDialog extends Dialog {

    // dependencies
    public TypefaceManager typefaceManager;
    public TypefaceCollection typefaceCollection;
    // views
    private EditText stackNameField;
    // data
    private final Context context;
    private final Stack stack;

    public StackRenameDialog(Context context, Stack stack) {
        super(context);
        this.context = context;
        this.stack = stack;
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getWindow().getAttributes().windowAnimations = R.style.FadeDialogAnimation;
        WordStack.getInstance().injectDependenciesTo(this);
        setContentView(R.layout.dialog_stack_rename);
        findViews();
        setupFonts();
        setupContent();
        setupCloseButton();
        setupBackButton();
        setupRenameButton();
    }

    private void setupRenameButton() {
        findViewById(R.id.rename_stack_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredName = String.valueOf(stackNameField.getText());
                if (thereIsStackCalled(enteredName)) showStackExistsAlertFor(enteredName);
                else updateStackNameTo(enteredName);
                hideKeyboardAndDismiss();
            }
        });
    }

    private boolean thereIsStackCalled(String name) {
        return SugarRecord.find(Stack.class, "name = ?", name).size() > 0;
    }

    private void showStackExistsAlertFor(String stackName) {
        final String title = "Can't rename stack!";
        final Spanned content = Html.fromHtml("The stack with name <strong>" + stackName + "</strong> already exists!");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new StackAlert(getContext(), title, content).show();
            }
        }, 300);
    }

    private void updateStackNameTo(String name) {
        stack.setName(name);
        stack.save();
        EventBus.getDefault().post(new StackRenamedEvent());
    }

    private void findViews() {
        stackNameField = (EditText) findViewById(R.id.stack_name);
    }

    private void setupContent() {
        stackNameField.setText(stack.getName());
        stackNameField.setSelection(stackNameField.getText().length());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void setupBackButton() {
        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboardAndDismiss();
            }
        });
    }

    private void setupCloseButton() {
        findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboardAndDismiss();
            }
        });
    }

    private void hideKeyboardAndDismiss() {
        hideKeyboard();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, 200);
    }

    private void setupFonts() {
        typefaceManager.setTypefaceForContainer((ViewGroup) findViewById(R.id.root_layout), typefaceCollection.getRalewayMedium());
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(findViewById(R.id.root_layout).getWindowToken(), 0);
    }

}
