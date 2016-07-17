package com.olebokolo.wordstack.presentation.dialogs;

import android.app.Dialog;
import android.widget.GridView;
import android.widget.TextView;

import com.olebokolo.wordstack.R;
import com.olebokolo.wordstack.core.app.WordStack;
import com.olebokolo.wordstack.core.languages.factory.LanguageComponentsFactory;
import com.olebokolo.wordstack.core.languages.flags.FlagService;
import com.olebokolo.wordstack.core.languages.services.LanguageService;
import com.olebokolo.wordstack.core.model.Language;
import com.olebokolo.wordstack.presentation.activities.LanguagesActivity;
import com.olebokolo.wordstack.presentation.lists.languages.LanguageAdapter;
import com.olebokolo.wordstack.presentation.lists.languages.LanguageItem;

import java.util.List;

import lombok.Setter;

public class LanguageDialog extends Dialog {

    private GridView languageList;
    private TextView dialogTitle;

    @Setter private LanguagesActivity parent;
    private FlagService flagService;
    private LanguageService languageService;

    public LanguageDialog(LanguagesActivity activity, String title) {
        super(activity);
        setParent(activity);
        setupServices();
        setupView();
        setTitleText(title);

        List<Language> languages = languageService.getAllLanguages();
        LanguageItem[] languageItems = getListItemsFrom(languages);
        LanguageAdapter languageAdapter = new LanguageAdapter(activity, R.layout.item_language, languageItems);
        languageList.setAdapter(languageAdapter);
    }

    private void setupView() {
        setContentView(R.layout.dialog_language);
        languageList = (GridView) findViewById(R.id.flag_list);
        dialogTitle = (TextView) findViewById(R.id.dialog_title);
    }

    private LanguageItem[] getListItemsFrom(List<Language> languages) {
        LanguageItem[] items = new LanguageItem[languages.size()];
        for (int i = 0; i < languages.size(); i++) {
            Language language = languages.get(i);
            Integer icon = getFlagFor(language);
            items[i] = new LanguageItem(language, icon);
        }
        return items;
    }

    private int getFlagFor(Language language) {
        return flagService.getFlagByLanguageShortName(language.getShortName());
    }

    private void setTitleText(String titleText) {
        dialogTitle.setText(titleText);
    }

    private void setupServices() {
        WordStack application = WordStack.getInstance();
        LanguageComponentsFactory factory = application.getLanguageComponentsFactory();
        flagService = factory.getFlagService();
        languageService = factory.getLanguageService();
    }

}
