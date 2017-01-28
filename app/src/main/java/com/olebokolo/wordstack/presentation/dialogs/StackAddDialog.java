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
import android.widget.TextView;

import com.olebokolo.wordstack.R;
import com.olebokolo.wordstack.core.app.WordStack;
import com.olebokolo.wordstack.core.events.StackAddedEvent;
import com.olebokolo.wordstack.core.model.Stack;
import com.olebokolo.wordstack.core.model.UserSettings;
import com.olebokolo.wordstack.core.user.settings.services.UserSettingsService;
import com.olebokolo.wordstack.core.utils.TypefaceCollection;
import com.olebokolo.wordstack.core.utils.TypefaceManager;
import com.olebokolo.wordstack.presentation.activities.StackListActivity;
import com.orm.SugarRecord;

import org.greenrobot.eventbus.EventBus;

public class StackAddDialog extends Dialog {

    // dependencies
    public UserSettingsService settingsService;
    public TypefaceManager typefaceManager;
    public TypefaceCollection typefaceCollection;
    // views
    private EditText stackNameField;
    private TextView addStackButton;
    private Long frontLangId;
    private Long backLangId;

    public StackAddDialog(StackListActivity activity) {
        super(activity);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().getAttributes().windowAnimations = R.style.FadeDialogAnimation;
        WordStack.getInstance().injectDependenciesTo(this);
        setContentView(R.layout.dialog_stack_edit);
        findViews();
        setupFonts();
        setupLanguages();
        setupBackButton();
        setupAddStackButton();
        setupCloseButton();
        focusOnInputAndShowKeyboard();
    }

    private void focusOnInputAndShowKeyboard() {
        stackNameField.postDelayed(new Runnable() {
            public void run() {
                stackNameField.requestFocusFromTouch();
                InputMethodManager lManager = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                lManager.showSoftInput(stackNameField, 0);
            }
        }, 450);

    }

    private void setupAddStackButton() {
        addStackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        String stackName = stackNameField.getText().toString();
                        if (thereIsStackCalled(stackName)) showStackExistsAlertFor(stackName);
                        else createNewStack(stackName);
                        hideKeyboardAndDismiss();
                    }
                });
            }
        });
    }

    private void createNewStack(String stackName) {
        Stack.builder().name(stackName).frontLangId(frontLangId).backLangId(backLangId).build().save();
        String eventMessage = "New stack with name \"" + stackName + "\"has been successfully created";
        EventBus.getDefault().post(new StackAddedEvent(eventMessage));
    }

    private void setupLanguages() {
        UserSettings userSettings = settingsService.getUserSettings();
        frontLangId = userSettings.getFrontLangId();
        backLangId = userSettings.getBackLangId();
    }

    private void showStackExistsAlertFor(String stackName) {
        final String title = "Can't create stack!";
        final Spanned content = Html.fromHtml("The stack with name <strong>" + stackName + "</strong> already exists!");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new StackAlert(getContext(), title, content).show();
            }
        }, 300);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(findViewById(R.id.root_layout).getWindowToken(), 0);
    }

    private boolean thereIsStackCalled(String name) {
        return SugarRecord.find(Stack.class,
                "name = ? and front_Lang_Id = ? and back_Lang_Id = ?",
                name, String.valueOf(frontLangId), String.valueOf(backLangId)
        ).size() > 0;
    }

    private void setupFonts() {
        typefaceManager.setTypefaceForContainer((ViewGroup) findViewById(R.id.root_layout), typefaceCollection.getRalewayMedium());
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

    private void findViews() {
        stackNameField = (EditText) findViewById(R.id.stack_name);
        addStackButton = (TextView) findViewById(R.id.save_stack_button);
    }
}
