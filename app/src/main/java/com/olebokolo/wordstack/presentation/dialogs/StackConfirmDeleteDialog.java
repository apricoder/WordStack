package com.olebokolo.wordstack.presentation.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.olebokolo.wordstack.R;
import com.olebokolo.wordstack.core.app.WordStack;
import com.olebokolo.wordstack.core.events.StackDeleteCalledEvent;
import com.olebokolo.wordstack.core.model.Card;
import com.olebokolo.wordstack.core.model.Stack;
import com.olebokolo.wordstack.core.utils.TypefaceCollection;
import com.olebokolo.wordstack.core.utils.TypefaceManager;
import com.orm.SugarRecord;

import org.greenrobot.eventbus.EventBus;

public class StackConfirmDeleteDialog extends Dialog {
    // dependencies
    public TypefaceCollection typefaceCollection;
    public TypefaceManager typefaceManager;
    // data
    private Context context;
    private Stack stack;
    private TextView content;

    public StackConfirmDeleteDialog(Context context, Stack stack) {
        super(context);
        this.stack = stack;
        this.context = context;
        WordStack.getInstance().injectDependenciesTo(this);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        setContentView(R.layout.dialog_stack_confirm_delete);
        findViews();
        setupContent();
        setupFonts();
        setupCloseButton();
        setupCancelButton();
        setupDeleteButton();
    }

    private void setupDeleteButton() {
        findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new StackDeleteCalledEvent(stack));
                onBackPressed();
            }
        });
    }

    private void setupCancelButton() {
        findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setupCloseButton() {
        findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void findViews() {
        content = (TextView) findViewById(R.id.dialog_content);
    }

    private void setupFonts() {
        typefaceManager.setTypefaceForContainer((ViewGroup) findViewById(R.id.root_layout), typefaceCollection.getRalewayMedium());
        typefaceManager.setTypeface(content, typefaceCollection.getRalewayLight());
    }

    private void setupContent() {
        int cardsInside = getCardsCountOf(stack);
        Spanned deleteQuestion = buildConfirmDeleteQuestionFor(stack.getName(), cardsInside);
        content.setText(deleteQuestion);
    }

    private Spanned buildConfirmDeleteQuestionFor(String stackName, int count) {
        return Html.fromHtml("Do You really want to delete stack " +
                "<strong>" + stackName + "</strong> " +
                "with <strong>" + count + "</strong> cards inside?");
    }

    private int getCardsCountOf(Stack stack) {
        String stackId = String.valueOf(stack.getId());
        return SugarRecord.find(Card.class, "stack_Id = ?", stackId).size();
    }

}
