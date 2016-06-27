package com.olebokolo.wordstack.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.olebokolo.wordstack.R;
import com.olebokolo.wordstack.activities.dialogs.LanguageDialog;
import com.olebokolo.wordstack.core.AppHeart;
import com.olebokolo.wordstack.core.utils.ActivityNavigator;

public class LanguagesActivity extends AppCompatActivity {

    private final ActivityNavigator navigator;

    public LanguagesActivity() {
        AppHeart appHeart = AppHeart.getInstanceFor(this);
        navigator = appHeart.getNavigator();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_languages);
        setUpGoNextButton();
        setUpLanguageChoosing();
    }

    @SuppressWarnings("ConstantConditions")
    private void setUpGoNextButton() {
        View goNextButton = findViewById(R.id.go_next_button);
        goNextButton.setOnClickListener(goNextClickListener);
    }

    private View.OnClickListener goNextClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            navigator.goWithSlideAnimation(LanguagesActivity.this, StackListActivity.class);
        }
    };

    @SuppressWarnings("ConstantConditions")
    private void setUpLanguageChoosing() {
        View frontLangRow = findViewById(R.id.front_lang_layout);
        View backLangRow = findViewById(R.id.back_lang_layout);
        frontLangRow.setOnClickListener(languageClickListener);
        backLangRow.setOnClickListener(languageClickListener);
    }

    private View.OnClickListener languageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View clickedLanguage) {
            String dialogTitle = matchDialogTitle(clickedLanguage.getId());
            showChooseLanguageDialog(dialogTitle);
        }

        @NonNull
        private String matchDialogTitle(int clickedView) {
            if (clickedView == R.id.front_lang_layout) return "Specify language\nYou know";
            else return "Choose language\nYou want to learn";
        }
    };

    private void showChooseLanguageDialog(String dialogTitle) {
        LanguagesActivity parent = this;
        LanguageDialog languageDialog = new LanguageDialog(parent, dialogTitle);
        languageDialog.show();
    }
}
