package com.olebokolo.wordstack.presentation.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.olebokolo.wordstack.R;
import com.olebokolo.wordstack.core.app.WordStack;
import com.olebokolo.wordstack.core.cards.CardsService;
import com.olebokolo.wordstack.core.events.CardAddedEvent;
import com.olebokolo.wordstack.core.events.CardEditRequestEvent;
import com.olebokolo.wordstack.core.events.CardEditedEvent;
import com.olebokolo.wordstack.core.languages.flags.FlagService;
import com.olebokolo.wordstack.core.languages.services.LanguageService;
import com.olebokolo.wordstack.core.model.Card;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    // data
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
        setupCardItems();
        setupCardList();
        setupCardListDivider();
        setupRemovalOnSwipe();

        reloadCards();
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
