package com.olebokolo.wordstack.presentation.dialogs;

import android.app.Dialog;
import android.view.View;
import android.widget.AdapterView;
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

public class LanguageDialog extends Dialog {

    private FlagService flagService;
    private LanguageService languageService;
    private GridView languageList;
    private LanguageItem[] languageItems;
    private LanguagesActivity languagesActivity;
    private TextView dialogTitle;
    private String title;

    public LanguageDialog(LanguagesActivity languagesActivity, String title) {
        super(languagesActivity);
        initFields(languagesActivity, title);
        setupServices();
        setupView();
        setupLanguagesList();
    }

    private void setupLanguagesList() {
        List<Language> languages = languageService.getAllLanguages();
        languageItems = getListItemsFrom(languages);
        LanguageAdapter languageAdapter = new LanguageAdapter(this.languagesActivity, R.layout.item_language, languageItems);
        languageList.setAdapter(languageAdapter);
        languageList.setOnItemClickListener(languageItemClick);
    }

    private AdapterView.OnItemClickListener languageItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            sendChosenLanguageAndHide(languageItems[position].getLanguage());
        }
    };

    private void sendChosenLanguageAndHide(Language language) {
        languagesActivity.languageReceived(language);
        dismiss();
    }

    private void initFields(LanguagesActivity activity, String title) {
        this.languagesActivity = activity;
        this.title = title;
    }

    private void setupView() {
        setContentView(R.layout.dialog_language);
        findViews();
        setTitleText();
    }

    private void findViews() {
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

    private void setTitleText() {
        dialogTitle.setText(title);
    }

    private void setupServices() {
        WordStack application = WordStack.getInstance();
        LanguageComponentsFactory factory = application.getLanguageComponentsFactory();
        flagService = factory.getFlagService();
        languageService = factory.getLanguageService();
    }

}
