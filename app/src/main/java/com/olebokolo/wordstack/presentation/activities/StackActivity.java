package com.olebokolo.wordstack.presentation.activities;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.olebokolo.wordstack.R;
import com.olebokolo.wordstack.core.app.WordStack;
import com.olebokolo.wordstack.core.cards.CardsService;
import com.olebokolo.wordstack.core.events.CardAddedEvent;
import com.olebokolo.wordstack.core.events.CardEditRequestEvent;
import com.olebokolo.wordstack.core.events.CardEditedEvent;
import com.olebokolo.wordstack.core.events.CardSayWordEvent;
import com.olebokolo.wordstack.core.events.CardSpeakOutEvent;
import com.olebokolo.wordstack.core.events.CardStopSpeakingOutEvent;
import com.olebokolo.wordstack.core.events.LanguageAvailabilityDiscoveredEvent;
import com.olebokolo.wordstack.core.languages.flags.FlagService;
import com.olebokolo.wordstack.core.languages.services.LanguageService;
import com.olebokolo.wordstack.core.model.Card;
import com.olebokolo.wordstack.core.model.Language;
import com.olebokolo.wordstack.core.model.Stack;
import com.olebokolo.wordstack.core.user.settings.services.UserSettingsService;
import com.olebokolo.wordstack.core.utils.TypefaceCollection;
import com.olebokolo.wordstack.core.utils.TypefaceManager;
import com.olebokolo.wordstack.presentation.dialogs.CardAddDialog;
import com.olebokolo.wordstack.presentation.dialogs.CardEditDialog;
import com.olebokolo.wordstack.presentation.lists.cards.CardItem;
import com.olebokolo.wordstack.presentation.lists.cards.CardItemAdapter;
import com.olebokolo.wordstack.presentation.navigation.ActivityNavigator;
import com.orm.SugarRecord;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

public class StackActivity extends AppCompatActivity {

    // dependencies
    public TypefaceCollection typefaceCollection;
    public TypefaceManager typefaceManager;
    public ActivityNavigator navigator;
    public LanguageService languageService;
    public FlagService flagService;
    public UserSettingsService settingsService;
    public CardsService cardsService;
    // views
    private ViewGroup backToolbarButton;
    private ViewGroup rootLayout;
    private RecyclerView cardRecycler;
    private CardItemAdapter cardAdapter;
    private ImageView speakButton;
    private ImageView searchButton;
    private ExpandableLayout searchLayout;
    private EditText searchField;
    // data
    private Stack stack;
    private List<Card> cards;
    private List<CardItem> cardItems;
    private boolean speakingOutLoud;
    private TextToSpeech frontLanguageSpeaker;
    private TextToSpeech backLanguageSpeaker;
    private boolean frontLangSpeechSupported;
    private boolean backLangSpeechSupported;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stack);
        getStackFromIntentExtras();
        WordStack.getInstance().injectDependenciesTo(this);
        EventBus.getDefault().register(this);
        findViews();
        setupSpeakers();
        setupTypeface();
        setupGoBackButton();
        setupAddCardButton();
        setupCardItems();
        setupCardList();
        setupCardListDivider();
        setupRemovalOnSwipe();
        setupSpeakButton();
        setupSearchButton();

        reloadCards();
    }

    private void setupSearchButton() {

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final int iconResource;

                if (!searchLayout.isExpanded()) {
                    searchLayout.expand();
                    searchField.requestFocusFromTouch();
                    InputMethodManager lManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    lManager.showSoftInput(searchField, 0);
                    iconResource = R.drawable.c_arrow_up;
                } else {
                    searchLayout.collapse();
                    hideKeyboard();
                    iconResource = R.drawable.c_magnify_white;
                }

                final ObjectAnimator fadeOut = ObjectAnimator.ofFloat(searchButton, "alpha", 1, 0);
                final ObjectAnimator fadeIn = ObjectAnimator.ofFloat(searchButton, "alpha", 0, 1);
                fadeOut.setDuration(200); fadeIn.setDuration(200);
                fadeOut.addListener(new Animator.AnimatorListener() {
                    @Override public void onAnimationStart(Animator animator) { }
                    @Override public void onAnimationCancel(Animator animator) { }
                    @Override public void onAnimationRepeat(Animator animator) { }
                    @Override public void onAnimationEnd(Animator animator) {
                        searchButton.setImageResource(iconResource);
                        fadeIn.start();
                    }
                });
                fadeOut.start();
            }
        });

    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(findViewById(R.id.root_layout).getWindowToken(), 0);
    }

    @Override
    protected void onDestroy() {
        if (frontLanguageSpeaker != null) frontLanguageSpeaker.shutdown();
        if (backLanguageSpeaker != null) backLanguageSpeaker.shutdown();
        super.onDestroy();
    }

    private void setupSpeakers() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                setupFrontSpeaker();
                setupBackSpeaker();
            }
        }).start();
    }

    private void setupSpeakButton() {
        speakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speakingOutLoud = !speakingOutLoud;
                handleSpeakingOutChange();
            }
        });
    }

    private void handleSpeakingOutChange() {
        if (speakingOutLoud) EventBus.getDefault().post(new CardSpeakOutEvent());
        else EventBus.getDefault().post(new CardStopSpeakingOutEvent());
        cardRecycler.getAdapter().notifyDataSetChanged();
        final int iconResource = speakingOutLoud ? R.drawable.c_close_white : R.drawable.c_volume_white;

        final ObjectAnimator fadeOut = ObjectAnimator.ofFloat(speakButton, "alpha", 1, 0);
        final ObjectAnimator fadeIn = ObjectAnimator.ofFloat(speakButton, "alpha", 0, 1);
        fadeOut.setDuration(200); fadeIn.setDuration(200);
        fadeOut.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animator) { }
            @Override public void onAnimationCancel(Animator animator) { }
            @Override public void onAnimationRepeat(Animator animator) { }
            @Override public void onAnimationEnd(Animator animator) {
                speakButton.setImageResource(iconResource);
                fadeIn.start();
            }
        });
        fadeOut.start();
    }
    
    @Subscribe
    public void onEvent(CardSayWordEvent event) {
        String text = event.getText();
        Integer side = event.getSide();
        if (side == CardSayWordEvent.FRONT_LANG) sayWith(frontLanguageSpeaker, text);
        else sayWith(backLanguageSpeaker, text);
    }

    private void sayWith(TextToSpeech speaker, String word) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            speaker.speak(word, TextToSpeech.QUEUE_FLUSH, null, null);
        else frontLanguageSpeaker.speak(word, TextToSpeech.QUEUE_FLUSH, null);
    }

    private void setupFrontSpeaker() {
        frontLanguageSpeaker = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    Language frontLanguage = languageService.findById(stack.getFrontLangId());
                    Locale locale = languageService.getLocaleForLanguage(frontLanguage);
                    int result = frontLanguageSpeaker.setLanguage(locale);
                    frontLangSpeechSupported = !(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED);
                    EventBus.getDefault().post(new LanguageAvailabilityDiscoveredEvent(CardSayWordEvent.FRONT_LANG, frontLangSpeechSupported));
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void setupBackSpeaker() {
        backLanguageSpeaker = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    Language backLanguage = languageService.findById(stack.getBackLangId());
                    Locale locale = languageService.getLocaleForLanguage(backLanguage);
                    int result = backLanguageSpeaker.setLanguage(locale);
                    backLangSpeechSupported = !(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED);
                    EventBus.getDefault().post(new LanguageAvailabilityDiscoveredEvent(CardSayWordEvent.BACK_LANG, backLangSpeechSupported));} else {
                }
            }
        });
    }


    private void setupCardItems() {
        cardItems = new ArrayList<>();
    }

    private void setupCardList() {
        cardAdapter = new CardItemAdapter(cardItems);
        cardRecycler.setLayoutManager(new LinearLayoutManager(this));
        cardRecycler.setAdapter(cardAdapter);
        cardRecycler.setItemAnimator(new SlideInLeftAnimator());
        cardRecycler.getItemAnimator().setChangeDuration(1000);
    }

    private void setupRemovalOnSwipe() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                cards.get(position).delete();
                cardAdapter.notifyItemRemoved(position);
                reloadCards();
            }
        }).attachToRecyclerView(cardRecycler);
    }

    @Subscribe
    public void onEvent(final CardEditRequestEvent event) {
        Card card = cards.get(event.getPosition());
        if (this.isFinishing()) return;
        final CardEditDialog dialog = new CardEditDialog(this, card);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        }, 150);
    }

    @Subscribe
    public void onEvent(CardEditedEvent event) {
        reloadCards();
        int position = getCardPosition(event.getCard());
        cardAdapter.notifyItemChanged(position);
    }

    @Subscribe
    public void onEvent(CardAddedEvent event) {
        reloadCards();
        int last = getCardPosition(cards.get(cards.size() - 1));
        cardAdapter.notifyItemInserted(last);
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
        cardItems.addAll(cardsService.getCardItemsFrom(cards));
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

    private void setupCardListDivider() {
        cardRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void setupAddCardButton() {
        findViewById(R.id.save_card_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CardAddDialog(StackActivity.this, stack).show();
            }
        });
    }

    private void setupTypeface() {
        typefaceManager.setTypefaceForContainer(rootLayout, typefaceCollection.getRalewayLight());
        typefaceManager.setTypefaceForContainer(backToolbarButton, typefaceCollection.getRalewayMedium());
    }

    private void findViews() {
        cardRecycler = (RecyclerView) findViewById(R.id.card_list);
        rootLayout = (ViewGroup) findViewById(R.id.root_layout);
        backToolbarButton = (ViewGroup) findViewById(R.id.back_toolbar_button);
        speakButton = (ImageView) findViewById(R.id.speak_button);
        searchButton = (ImageView) findViewById(R.id.search_button);
        searchLayout = (ExpandableLayout) findViewById(R.id.search_layout);
        searchField = (EditText) findViewById(R.id.search_text);
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
        navigator.goBackWithSlideAnimation(StackActivity.this, StackListActivity.class);
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


}
