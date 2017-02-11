package com.olebokolo.wordstack.presentation.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.olebokolo.wordstack.R;
import com.olebokolo.wordstack.core.app.WordStack;
import com.olebokolo.wordstack.core.events.PracticeQuitEvent;
import com.olebokolo.wordstack.core.events.PracticeTurnOverCardsAndRestartEvent;
import com.olebokolo.wordstack.core.utils.TypefaceCollection;
import com.olebokolo.wordstack.core.utils.TypefaceManager;

import org.greenrobot.eventbus.EventBus;

public class PracticeFinishedDialog extends Dialog {

    // dependencies
    public TypefaceCollection typefaceCollection;
    public TypefaceManager typefaceManager;
    // views
    private TextView content;
    private View cancelButton;
    private View practiceButton;
    private View closeButton;
    // data
    private Activity activity;

    public PracticeFinishedDialog(Activity activity) {
        super(activity);
        this.activity = activity;
        this.setCanceledOnTouchOutside(false);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WordStack.getInstance().injectDependenciesTo(this);
        setContentView(R.layout.dialog_practice_finished);
        findViews();
        setupFonts();
        setupCancelButton();
        setupPracticeButton();
        setupCloseButton();
    }

    private void setupPracticeButton() {
        practiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new PracticeTurnOverCardsAndRestartEvent());
                dismiss();
            }
        });
    }

    private void setupCancelButton() {
        cancelButton.setOnClickListener(cancelClick);
    }

    private void setupCloseButton() {
        closeButton.setOnClickListener(cancelClick);
    }

    private View.OnClickListener cancelClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            EventBus.getDefault().post(new PracticeQuitEvent());
            dismiss();
        }
    };

    private void findViews() {
        content = (TextView) findViewById(R.id.dialog_content);
        cancelButton = findViewById(R.id.cancel_button);
        practiceButton = findViewById(R.id.practice_button);
        closeButton = findViewById(R.id.close_button);
    }

    private void setupFonts() {
        typefaceManager.setTypefaceForContainer((ViewGroup) findViewById(R.id.root_layout), typefaceCollection.getRalewayMedium());
        typefaceManager.setTypeface(content, typefaceCollection.getRalewayLight());
    }

    @Override
    public void onBackPressed() {
        activity.onBackPressed();
    }
}
