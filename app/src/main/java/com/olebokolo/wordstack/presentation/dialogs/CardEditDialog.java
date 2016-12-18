package com.olebokolo.wordstack.presentation.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.olebokolo.wordstack.R;
import com.olebokolo.wordstack.core.app.WordStack;
import com.olebokolo.wordstack.core.events.CardEditedEvent;
import com.olebokolo.wordstack.core.languages.flags.FlagService;
import com.olebokolo.wordstack.core.languages.services.LanguageService;
import com.olebokolo.wordstack.core.model.Card;
import com.olebokolo.wordstack.core.model.Language;
import com.olebokolo.wordstack.core.model.Side;
import com.olebokolo.wordstack.core.model.UserSettings;
import com.olebokolo.wordstack.core.user.settings.services.UserSettingsService;
import com.olebokolo.wordstack.core.utils.TypefaceCollection;
import com.olebokolo.wordstack.core.utils.TypefaceManager;
import com.orm.SugarRecord;

import org.greenrobot.eventbus.EventBus;

public class CardEditDialog extends Dialog {
    // dependencies
    public TypefaceCollection typefaceCollection;
    public TypefaceManager typefaceManager;
    public LanguageService languageService;
    public FlagService flagService;
    public UserSettingsService settingsService;
    // views
    private ImageView frontLangIcon;
    private ImageView backLangIcon;
    private TextInputEditText frontLangText;
    private TextInputEditText backLangText;
    // data
    private Card card;
    private Long frontLangId;
    private Long backLangId;
    private Side frontSide;
    private Side backSide;
    private String frontLangInitialText;
    private String backLangInitialText;

    public CardEditDialog(Context activity, Card card) {
        super(activity);
        this.card = card;
        WordStack.getInstance().injectDependenciesTo(this);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        setContentView(R.layout.dialog_card_edit);
        findViews();
        setupCardSides();
        setupInputsContent();
        setupLanguages();
        setupLanguagesIcons();
        setupFonts();
        setupCloseButton();
        setupBackButton();
        setupAddCardButton();
    }

    private void setupCardSides() {
        frontSide = SugarRecord.findById(Side.class, card.getFrontSideId());
        backSide = SugarRecord.findById(Side.class, card.getBackSideId());
    }

    private void setupInputsContent() {
        frontLangInitialText = frontSide.getContent();
        frontLangText.setText(frontLangInitialText);
        backLangInitialText = backSide.getContent();
        backLangText.setText(backSide.getContent());
    }

    private void setupAddCardButton() {
        findViewById(R.id.save_card_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCardSides();
                postCardEditedEventIfEdited();
                hideKeyboardAndDismiss();
            }
        });
    }

    private void postCardEditedEventIfEdited() {
        String newBackContent = backLangText.getText().toString();
        String newFrontContent = frontLangText.getText().toString();
        if (!(backLangInitialText.equals(newBackContent) && frontLangInitialText.equals(newFrontContent)))
            EventBus.getDefault().post(new CardEditedEvent(card));
    }

    private void updateCardSides() {
        updateSide(frontSide, frontLangText.getText().toString());
        updateSide(backSide, backLangText.getText().toString());
    }

    private void updateSide(Side side, String content) {
        side.setContent(content);
        side.save();
    }

    private void findViews() {
        frontLangIcon = (ImageView) findViewById(R.id.front_lang_icon);
        backLangIcon = (ImageView) findViewById(R.id.back_lang_icon);
        frontLangText = (TextInputEditText) findViewById(R.id.front_lang_text);
        backLangText = (TextInputEditText) findViewById(R.id.back_lang_text);
    }

    private void setupLanguagesIcons() {
        Language frontLanguage = languageService.findById(frontLangId);
        Language backLanguage = languageService.findById(backLangId);
        frontLangIcon.setImageResource(flagService.getFlagByLanguageShortName(frontLanguage.getShortName()));
        backLangIcon.setImageResource(flagService.getFlagByLanguageShortName(backLanguage.getShortName()));
    }

    private void setupLanguages() {
        UserSettings userSettings = settingsService.getUserSettings();
        frontLangId = userSettings.getFrontLangId();
        backLangId = userSettings.getBackLangId();
    }

    private void setupFonts() {
        typefaceManager.setTypefaceForContainer((ViewGroup) findViewById(R.id.root_layout), typefaceCollection.getRalewayMedium());
    }

    private void setupCloseButton() {
        findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboardAndDismiss();
            }
        });
    }

    private void setupBackButton() {
        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
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

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(findViewById(R.id.root_layout).getWindowToken(), 0);
    }

}
