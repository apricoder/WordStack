package com.olebokolo.wordstack.presentation.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.olebokolo.wordstack.R;
import com.olebokolo.wordstack.core.app.WordStack;
import com.olebokolo.wordstack.core.events.PracticeStartEvent;
import com.olebokolo.wordstack.core.events.PracticeTurnOverCardsEvent;
import com.olebokolo.wordstack.core.languages.flags.FlagService;
import com.olebokolo.wordstack.core.languages.services.LanguageService;
import com.olebokolo.wordstack.core.model.Language;
import com.olebokolo.wordstack.core.model.UserSettings;
import com.olebokolo.wordstack.core.user.settings.services.UserSettingsService;
import com.olebokolo.wordstack.core.utils.TypefaceCollection;
import com.olebokolo.wordstack.core.utils.TypefaceManager;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;

public class PracticeChooseFaceSideDialog extends Dialog {

    // constants
    private static final int DELAY_MILLIS = 100;
    // dependencies
    public TypefaceCollection typefaceCollection;
    public TypefaceManager typefaceManager;
    public LanguageService languageService;
    public UserSettingsService settingsService;
    public FlagService flagService;
    // views
    private ImageView frontLangIcon;
    private ImageView backLangIcon;
    private TextView frontLangName;
    private TextView backLangName;
    private View frontLangLayout;
    private View backLangLayout;
    // data
    private Activity activity;
    private Long frontLangId;
    private Long backLangId;


    public PracticeChooseFaceSideDialog(Activity activity) {
        super(activity);
        this.activity = activity;
        this.setCanceledOnTouchOutside(false);
        WordStack.getInstance().injectDependenciesTo(this);
        setContentView(R.layout.dialog_choose_face_language);
        findViews();
        setupFonts();
        setupLanguages();
        setupLanguagesClicks();
    }

    private void setupLanguagesClicks() {
        frontLangLayout.setOnClickListener(languageClick);
        backLangLayout.setOnClickListener(languageClick);
    }

    private View.OnClickListener languageClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.back_lang_layout) turnOverCardsAndStartPractice();
            else closeDialogAndStartPractice();
        }
    };

    private void turnOverCardsAndStartPractice() {
        EventBus.getDefault().post(new PracticeTurnOverCardsEvent());
        dismiss();
    }

    private void closeDialogAndStartPractice() {
        EventBus.getDefault().post(new PracticeStartEvent());
        dismiss();
    }

    private void findViews() {
        frontLangLayout = findViewById(R.id.front_lang_layout);
        backLangLayout = findViewById(R.id.back_lang_layout);
        frontLangIcon = (ImageView) findViewById(R.id.front_lang_icon);
        frontLangName = (TextView) findViewById(R.id.front_lang_name);
        backLangIcon = (ImageView) findViewById(R.id.back_lang_icon);
        backLangName = (TextView) findViewById(R.id.back_lang_name);
    }

    private void setupLanguages() {
        UserSettings userSettings = settingsService.getUserSettings();
        frontLangId = userSettings.getFrontLangId();
        backLangId = userSettings.getBackLangId();
        Language frontLanguage = languageService.findById(frontLangId);
        Language backLanguage = languageService.findById(backLangId);
        frontLangIcon.setImageResource(flagService.getFlagByLanguageShortName(frontLanguage.getShortName()));
        backLangIcon.setImageResource(flagService.getFlagByLanguageShortName(backLanguage.getShortName()));
        frontLangName.setText(StringUtils.capitalize(frontLanguage.getName()));
        backLangName.setText(StringUtils.capitalize(backLanguage.getName()));
    }

    private void setupFonts() {
        typefaceManager.setTypefaceForContainer((ViewGroup) findViewById(R.id.root_layout), typefaceCollection.getRalewayMedium());
    }

    @Override
    public void onBackPressed() {
        activity.onBackPressed();
    }
}
