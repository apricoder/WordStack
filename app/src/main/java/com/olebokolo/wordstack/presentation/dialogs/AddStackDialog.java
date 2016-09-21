package com.olebokolo.wordstack.presentation.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.olebokolo.wordstack.R;
import com.olebokolo.wordstack.core.app.WordStack;
import com.olebokolo.wordstack.core.events.StackAddedEvent;
import com.olebokolo.wordstack.core.model.Stack;
import com.olebokolo.wordstack.core.model.UserSettings;
import com.olebokolo.wordstack.core.user.settings.services.UserSettingsService;
import com.olebokolo.wordstack.presentation.activities.StackListActivity;
import com.orm.SugarRecord;

import org.greenrobot.eventbus.EventBus;

public class AddStackDialog extends Dialog {

    // dependencies
    public UserSettingsService settingsService;
    // views
    private EditText stackNameField;
    private TextView addStackButton;
    private TextView backButton;
    private View closeButton;

    public AddStackDialog(StackListActivity activity) {
        super(activity);
        getWindow().getAttributes().windowAnimations = R.style.FadeDialogAnimation;
        WordStack.getInstance().injectDependenciesTo(this);
        setContentView(R.layout.dialog_add_stack);
        findViews();
        setupBackButton();
        setupAddStackButton();
        setupCloseButton();
    }

    private void setupAddStackButton() {
        addStackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String stackName = stackNameField.getText().toString();
                        if (thereIsStackCalled(stackName)) showStackExistsAlertFor(stackName);
                        else createNewStack(stackName);
                        closeDialog();
                    }
                }, 200);
            }
        });
    }

    private void createNewStack(String stackName) {
        UserSettings userSettings = settingsService.getUserSettings();
        Long frontLangId = userSettings.getFrontLangId();
        Long backLangId = userSettings.getBackLangId();
        Stack.builder().name(stackName).frontLangId(frontLangId).backLangId(backLangId).build().save();
        String eventMessage = "New stack with name \"" + stackName + "\"has been successfully created";
        EventBus.getDefault().post(new StackAddedEvent(eventMessage));
    }

    private void showStackExistsAlertFor(String stackName) {
        final String title = "Can't create stack!";
        final Spanned content = Html.fromHtml("The stack with name <strong>" + stackName + "</strong> already exists!");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new InformationalAlert(getContext(), title, content).show();
            }
        }, 300);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(findViewById(R.id.root_layout).getWindowToken(), 0);
    }

    private boolean thereIsStackCalled(String name) {
        return SugarRecord.find(Stack.class, "name = ?", name).size() > 0;
    }

    private void setupCloseButton() {
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeDialog();
            }
        });
    }

    private void setupBackButton() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeDialog();
            }
        });
    }

    private void closeDialog() {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() { dismiss(); }
        }, 100);
    }

    private void findViews() {
        stackNameField = (EditText) findViewById(R.id.stack_name);
        addStackButton = (TextView) findViewById(R.id.add_stack_button);
        backButton = (TextView) findViewById(R.id.back_button);
        closeButton = findViewById(R.id.close_button);
    }
}
