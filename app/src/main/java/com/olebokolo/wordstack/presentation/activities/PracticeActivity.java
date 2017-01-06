package com.olebokolo.wordstack.presentation.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.olebokolo.wordstack.R;
import com.olebokolo.wordstack.core.app.WordStack;
import com.olebokolo.wordstack.core.events.PracticeStackCheckedEvent;
import com.olebokolo.wordstack.core.languages.flags.FlagService;
import com.olebokolo.wordstack.core.languages.services.LanguageService;
import com.olebokolo.wordstack.core.model.Card;
import com.olebokolo.wordstack.core.model.Language;
import com.olebokolo.wordstack.core.model.Stack;
import com.olebokolo.wordstack.core.model.UserSettings;
import com.olebokolo.wordstack.core.user.settings.services.UserSettingsService;
import com.olebokolo.wordstack.core.utils.TypefaceCollection;
import com.olebokolo.wordstack.core.utils.TypefaceManager;
import com.olebokolo.wordstack.presentation.lists.stacks.PracticeStackItem;
import com.olebokolo.wordstack.presentation.lists.stacks.PracticeStackItemAdapter;
import com.olebokolo.wordstack.presentation.navigation.ActivityNavigator;
import com.orm.SugarRecord;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

public class PracticeActivity extends AppCompatActivity {

    // dependencies
    public TypefaceCollection typefaceCollection;
    public TypefaceManager typefaceManager;
    public ActivityNavigator navigator;
    public LanguageService languageService;
    public FlagService flagService;
    public UserSettingsService settingsService;
    // views
    private ImageView frontLangIcon;
    private ImageView backLangIcon;
    private ViewGroup backToolbarButton;
    private FloatingActionButton practiceButton;
    private PracticeStackItemAdapter stackAdapter;
    private RecyclerView stackRecycler;
    // data
    private Long frontLangId;
    private Long backLangId;
    private List<Stack> stacks;
    private List<PracticeStackItem> stackItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        WordStack.getInstance().injectDependenciesTo(this);
        EventBus.getDefault().register(this);
        findViews();
        setupTypeface();
        setupGoBackButton();
        setupLanguages();
        setupLanguagesIcons();
        setupStackItems();
        setupStackList();

        reloadStacks();
    }

    @Subscribe
    public void onEvent(PracticeStackCheckedEvent event) {
        stackItems.get(event.getPosition()).setChecked(event.isChecked());
        updatePracticeButtonVisibility();
    }

    private void updatePracticeButtonVisibility() {
        boolean v = getNextPracticeButtonVisibility();
        if (v) practiceButton.show();
        else practiceButton.hide();
    }

    private boolean getNextPracticeButtonVisibility() {
        boolean nextVisibility = false;
        for (PracticeStackItem item : stackItems)
            if (item.isChecked()) {
                nextVisibility = true;
                break;
            }
        return nextVisibility;
    }

    private void reloadStacks() {
        stacks = getStacksForChosenLanguages();
        stackItems.clear();
        stackItems.addAll(getStackItemsFrom(stacks));
    }

    private List<PracticeStackItem> getStackItemsFrom(List<Stack> stacks) {
        List<PracticeStackItem> stackItems = new ArrayList<>();
        for (Stack stack : stacks) stackItems.add(getStackItemFrom(stack));
        return stackItems;
    }

    private PracticeStackItem getStackItemFrom(Stack stack) {
        String stackId = String.valueOf(stack.getId());
        int cards = SugarRecord.find(Card.class, "stack_Id = ?", stackId).size();
        return new PracticeStackItem(stack.getName(), cards);
    }

    private List<Stack> getStacksForChosenLanguages() {
        String frontLangId = String.valueOf(this.frontLangId);
        String backLangId = String.valueOf(this.backLangId);
        return SugarRecord.find(Stack.class, "front_Lang_Id = ? and back_Lang_Id = ?", frontLangId, backLangId);
    }

    private void setupStackItems() {
        stackItems = new ArrayList<>();
    }

    private void setupStackList() {
        stackAdapter = new PracticeStackItemAdapter(stackItems);
        stackRecycler.setLayoutManager(new LinearLayoutManager(this));
        stackRecycler.setAdapter(stackAdapter);
        stackRecycler.setItemAnimator(new SlideInLeftAnimator());
        stackRecycler.getItemAnimator().setChangeDuration(1000);
    }

    private void findViews() {
        backToolbarButton = (ViewGroup) findViewById(R.id.back_toolbar_button);
        frontLangIcon = (ImageView) findViewById(R.id.front_lang_icon);
        backLangIcon = (ImageView) findViewById(R.id.back_lang_icon);
        practiceButton = (FloatingActionButton) findViewById(R.id.practice_button);
        stackRecycler = (RecyclerView) findViewById(R.id.stack_list);
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

    private void setupTypeface() {
        typefaceManager.setTypefaceForContainer(backToolbarButton, typefaceCollection.getRalewayMedium());
    }

    private void setupGoBackButton() {
        backToolbarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                goBack();
            }
        });
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    private void goBack() {
        navigator.goBackWithSlideAnimation(PracticeActivity.this, MainMenuActivity.class);
    }

}
