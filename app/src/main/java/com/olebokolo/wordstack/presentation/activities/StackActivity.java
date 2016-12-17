package com.olebokolo.wordstack.presentation.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.olebokolo.wordstack.R;
import com.olebokolo.wordstack.core.app.WordStack;
import com.olebokolo.wordstack.core.events.CardAddedEvent;
import com.olebokolo.wordstack.core.events.CardEditedEvent;
import com.olebokolo.wordstack.core.events.ReanimateCardEnterEvent;
import com.olebokolo.wordstack.core.events.ReanimateUpdatedCardEvent;
import com.olebokolo.wordstack.core.languages.flags.FlagService;
import com.olebokolo.wordstack.core.languages.services.LanguageService;
import com.olebokolo.wordstack.core.model.Card;
import com.olebokolo.wordstack.core.model.Language;
import com.olebokolo.wordstack.core.model.Side;
import com.olebokolo.wordstack.core.model.Stack;
import com.olebokolo.wordstack.core.model.UserSettings;
import com.olebokolo.wordstack.core.user.settings.services.UserSettingsService;
import com.olebokolo.wordstack.core.utils.TypefaceCollection;
import com.olebokolo.wordstack.core.utils.TypefaceManager;
import com.olebokolo.wordstack.presentation.dialogs.CardAddDialog;
import com.olebokolo.wordstack.presentation.dialogs.CardEditDialog;
import com.olebokolo.wordstack.presentation.lists.cards.CardAdapter;
import com.olebokolo.wordstack.presentation.lists.cards.CardItem;
import com.olebokolo.wordstack.presentation.navigation.ActivityNavigator;
import com.orm.SugarRecord;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StackActivity extends AppCompatActivity {

    // constants
    private static final String TAG = "WordStack";
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
    private ViewGroup rootLayout;
    private ListView cardList;
    private CardAdapter cardAdapter;
    // data
    private Long frontLangId;
    private Long backLangId;
    private Language frontLanguage;
    private Language backLanguage;
    private Stack stack;
    private List<Card> cards;
    private List<CardItem> cardItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stack);
        getStackFromIntentExtras();
        WordStack.getInstance().injectDependenciesTo(this);
        EventBus.getDefault().register(this);
        findViews();
        setupTypeface();
        setupGoBackButton();
        setupAddCardButton();
        setupLanguages();
        setupCardItems();
        setupCardList();

        reloadCards();
    }

    private void setupCardItems() {
        cardItems = new ArrayList<>();
    }

    private void setupCardList() {
        cardAdapter = new CardAdapter(cardItems);
        cardList.setAdapter(cardAdapter);
        cardList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                new CardEditDialog(StackActivity.this, cards.get(i)).show();
            }
        });
    }

    @Subscribe
    public void onEvent(CardEditedEvent event) {
        reloadCards();
        postEventToAnimateItemUpdate(event.getCard());
        new Handler().postDelayed(new Runnable() { @Override public void run() { cardAdapter.notifyDataSetChanged(); } }, 100);
    }

    private void postEventToAnimateItemUpdate(Card card) {
        int position = cards.indexOf(card);
        EventBus.getDefault().post(new ReanimateUpdatedCardEvent(position));
    }

    @Subscribe
    public void onEvent(CardAddedEvent event) {
        Log.i(TAG, ">>>> Got card created event!");
        reloadCards();
        postEventToAnimateLastListItemEnter();
        new Handler().postDelayed(new Runnable() { @Override public void run() { cardAdapter.notifyDataSetChanged(); } }, 100);
    }

    private void postEventToAnimateLastListItemEnter() {
        int itemToAnimatePosition = getCardPosition(cards.get(cards.size() - 1));
        Log.i(TAG, "reanimating " + itemToAnimatePosition);
        EventBus.getDefault().post(new ReanimateCardEnterEvent(itemToAnimatePosition));
    }

    private int getCardPosition(Card searched) {
        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            if (card.getId().equals(searched.getId())) return i;
        }
        return -1;
    }

    private void reloadCards() {
        cards = getCardsOf(stack);
        cardItems.clear();
        cardItems.addAll(getCardItemsFrom(cards));
    }

    private List<CardItem> getCardItemsFrom(List<Card> cards) {
        List<CardItem> cardItems = new ArrayList<>();
        for (Card card : cards) cardItems.add(getCardItemFrom(card));
        return cardItems;
    }

    private CardItem getCardItemFrom(Card c) {
        Side frontSide = SugarRecord.findById(Side.class, c.getFrontSideId());
        Side backSide = SugarRecord.findById(Side.class, c.getBackSideId());
        Log.i(TAG, "frontSide: " + frontSide);
        Log.i(TAG, "backSide: " + backSide);
        return CardItem.builder()
                .frontLangText(frontSide.getContent())
                .backLangText(backSide.getContent())
                .frontLangFlagResource(getFlagFor(frontLanguage))
                .backLangFlagResource(getFlagFor(backLanguage))
                .build();
    }

    private int getFlagFor(Language language) {
        return flagService.getFlagByLanguageShortName(language.getShortName());
    }

    private List<Card> getCardsOf(Stack stack) {
        return SugarRecord.find(Card.class, "stack_Id = ?", String.valueOf(stack.getId()));
    }

    private void getStackFromIntentExtras() {
        Serializable serializable = getIntent().getSerializableExtra("stack");
        if (serializable != null) {
            stack = (Stack) serializable;
            setToolbarTitle(stack.getName());
        }
    }

    private void setToolbarTitle(String name) {
        ((TextView) findViewById(R.id.toolbar_title)).setText(name);
    }

    private void setupAddCardButton() {
        findViewById(R.id.save_card_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CardAddDialog(StackActivity.this, stack).show();
            }
        });
    }

    private void setupLanguages() {
        UserSettings userSettings = settingsService.getUserSettings();
        frontLangId = userSettings.getFrontLangId();
        backLangId = userSettings.getBackLangId();
        frontLanguage = languageService.findById(frontLangId);
        backLanguage = languageService.findById(backLangId);
    }

    private void setupTypeface() {
        typefaceManager.setTypefaceForContainer(rootLayout, typefaceCollection.getRalewayLight());
        typefaceManager.setTypefaceForContainer(backToolbarButton, typefaceCollection.getRalewayMedium());
    }

    private void findViews() {
        frontLangIcon = (ImageView) findViewById(R.id.front_lang_icon);
        backLangIcon = (ImageView) findViewById(R.id.back_lang_icon);
        cardList = (ListView) findViewById(R.id.card_list);
        rootLayout = (ViewGroup) findViewById(R.id.root_layout);
        backToolbarButton = (ViewGroup) findViewById(R.id.back_toolbar_button);
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

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        Serializable serializable = savedInstanceState.getSerializable("stack");
        if (serializable != null) {
            stack = (Stack) serializable;
            setToolbarTitle(stack.getName());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putSerializable("stack", stack);
    }

    private void goBack() {
        navigator.goBackWithSlideAnimation(StackActivity.this, StackListActivity.class);
    }



}
