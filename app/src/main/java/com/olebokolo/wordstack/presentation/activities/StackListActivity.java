package com.olebokolo.wordstack.presentation.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.olebokolo.wordstack.R;
import com.olebokolo.wordstack.core.app.WordStack;
import com.olebokolo.wordstack.core.events.ReanimateStackEnterEvent;
import com.olebokolo.wordstack.core.events.StackActionsDialogCalledEvent;
import com.olebokolo.wordstack.core.events.StackAddedEvent;
import com.olebokolo.wordstack.core.events.StackDeleteCalledEvent;
import com.olebokolo.wordstack.core.events.StackDetailsRequestedEvent;
import com.olebokolo.wordstack.core.events.StackRenamedEvent;
import com.olebokolo.wordstack.core.languages.flags.FlagService;
import com.olebokolo.wordstack.core.languages.services.LanguageService;
import com.olebokolo.wordstack.core.model.Card;
import com.olebokolo.wordstack.core.model.Language;
import com.olebokolo.wordstack.core.model.Stack;
import com.olebokolo.wordstack.core.model.UserSettings;
import com.olebokolo.wordstack.core.user.settings.services.UserSettingsService;
import com.olebokolo.wordstack.core.utils.TypefaceCollection;
import com.olebokolo.wordstack.core.utils.TypefaceManager;
import com.olebokolo.wordstack.presentation.dialogs.StackActionsDialog;
import com.olebokolo.wordstack.presentation.dialogs.StackAddDialog;
import com.olebokolo.wordstack.presentation.lists.stacks.StackAdapter;
import com.olebokolo.wordstack.presentation.lists.stacks.StackItem;
import com.olebokolo.wordstack.presentation.navigation.ActivityNavigator;
import com.orm.SugarRecord;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StackListActivity extends AppCompatActivity {

    // dependencies
    public ActivityNavigator navigator;
    public FlagService flagService;
    public LanguageService languageService;
    public UserSettingsService settingsService;
    public TypefaceManager typefaceManager;
    public TypefaceCollection typefaceCollection;
    // views
    private ImageView frontLangIcon;
    private ImageView backLangIcon;
    private ViewGroup backToolbarButton;
    private Animation deleteAnimation;
    private FloatingActionButton addStackButton;
    // data
    private Long frontLangId;
    private Long backLangId;
    private List<StackItem> stackItems;
    private StackAdapter stackAdapter;
    private List<Stack> stacks;
    private ListView stackList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stack_list);
        WordStack.getInstance().injectDependenciesTo(this);
        EventBus.getDefault().register(this);
        findViews();
        setupTypeface();
        setupGoBackButton();
        setupLanguages();
        setupLanguagesIcons();
        setupAddStackButton();
        setupListView();
        setupDeleteAnimation();
    }

    private void setupDeleteAnimation() {
        deleteAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_out_to_right_fast);
    }

    @Subscribe
    public void onEvent(StackAddedEvent event) {
        Log.i("WordStack", ">>> " + event.getMessage());
        findStacksForChosenLanguages();
        int itemToAnimatePosition = getStackPosition(stacks.get(stacks.size() - 1));
        EventBus.getDefault().post(new ReanimateStackEnterEvent(itemToAnimatePosition));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                stackAdapter.notifyDataSetChanged();
            }
        }, 100);
    }

    @Subscribe
    public void onEvent(StackActionsDialogCalledEvent event) {
        Log.i("WordStack", ">>> " + event.getMessage());
        Stack stack = stacks.get(event.getStackPosition());
        new StackActionsDialog(StackListActivity.this, stack).show();
    }

    @Subscribe
    public void onEvent(StackRenamedEvent event) {
        findStacksForChosenLanguages();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                stackAdapter.notifyDataSetChanged();
            }
        });
    }

    @Subscribe
    public void onEvent(final StackDetailsRequestedEvent event) {
        new Handler().post(new Runnable() {

            @Override
            public void run() {
                Map<String, Serializable> extras = new HashMap<>();
                Stack stack = getStackFromEvent();
                extras.put("stack", stack);
                navigator.goForwardWithSlideAnimation(StackListActivity.this, StackActivity.class, extras);
            }

            private Stack getStackFromEvent() {
                Stack stack = event.getStack();
                return stack != null ? stack : stacks.get(event.getPosition());
            }

        });
    }

    @Subscribe
    public void onEvent(StackDeleteCalledEvent event) {
        final Stack stack = event.getStack();
        int position = getStackPosition(stack);
        final View stackItemView = getListItemViewForStack(position);
        stackItemView.startAnimation(deleteAnimation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SugarRecord.delete(stack);
                findStacksForChosenLanguages();
                stackAdapter.notifyDataSetChanged();
            }
        }, 200);
    }

    private View getListItemViewForStack(int position) {
        final int firstListItemPosition = stackList.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + stackList.getChildCount() - 1;
        if (position < firstListItemPosition || position > lastListItemPosition ) {
            return stackList.getAdapter().getView(position, null, stackList);
        } else {
            final int childIndex = position - firstListItemPosition;
            return stackList.getChildAt(childIndex);
        }
    }

    private int getStackPosition(Stack searched) {
        for (int i = 0; i < stacks.size(); i++) {
            Stack stack = stacks.get(i);
            if (stack.getId().equals(searched.getId())) return i;
        }
        return -1;
    }

    private void setupListView() {
        stackItems = new ArrayList<>();
        findStacksForChosenLanguages();
        stackAdapter = new StackAdapter(stackItems);
        stackList.setAdapter(stackAdapter);
        stackList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                EventBus.getDefault().post(StackDetailsRequestedEvent.builder().position(i).build());
            }
        });
    }

    private List<StackItem> getStackItemsFrom(List<Stack> stacks) {
        List<StackItem> stackItems = new ArrayList<>();
        for (Stack s : stacks) {
            String stackId = String.valueOf(s.getId());
            int cards = SugarRecord.find(Card.class, "stack_Id = ?", stackId).size();
            stackItems.add(new StackItem(s.getName(), cards));
        }
        return stackItems;
    }

    private void findStacksForChosenLanguages() {
        String frontLangId = String.valueOf(this.frontLangId);
        String backLangId = String.valueOf(this.backLangId);
        stacks = SugarRecord.find(Stack.class, "front_Lang_Id = ? and back_Lang_Id = ?", frontLangId, backLangId);
        updateStackItems(stacks);
    }

    private void updateStackItems(List<Stack> stacks) {
        stackItems.clear();
        stackItems.addAll(getStackItemsFrom(stacks));
    }

    private void setupAddStackButton() {
        addStackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new StackAddDialog(StackListActivity.this).show();
            }
        });
    }

    private void setupTypeface() {
        typefaceManager.setTypefaceForContainer(backToolbarButton, typefaceCollection.getRalewayMedium());
    }

    private void findViews() {
        backToolbarButton = (ViewGroup) findViewById(R.id.back_toolbar_button);
        frontLangIcon = (ImageView) findViewById(R.id.front_lang_icon);
        backLangIcon = (ImageView) findViewById(R.id.back_lang_icon);
        addStackButton = (FloatingActionButton) findViewById(R.id.save_stack_button);
        stackList = (ListView) findViewById(R.id.stack_list);
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
        navigator.goBackWithSlideAnimation(StackListActivity.this, MainMenuActivity.class);
    }

}
