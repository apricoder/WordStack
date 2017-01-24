package com.olebokolo.wordstack.presentation.activities;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.olebokolo.wordstack.R;
import com.olebokolo.wordstack.core.app.WordStack;
import com.olebokolo.wordstack.core.cards.CardsService;
import com.olebokolo.wordstack.core.events.PracticeStartEvent;
import com.olebokolo.wordstack.core.events.PracticeTurnOverCardsEvent;
import com.olebokolo.wordstack.core.languages.flags.FlagService;
import com.olebokolo.wordstack.core.languages.services.LanguageService;
import com.olebokolo.wordstack.core.model.Card;
import com.olebokolo.wordstack.core.user.settings.services.UserSettingsService;
import com.olebokolo.wordstack.core.utils.TypefaceCollection;
import com.olebokolo.wordstack.core.utils.TypefaceManager;
import com.olebokolo.wordstack.presentation.dialogs.PracticeChooseFaceSideDialog;
import com.olebokolo.wordstack.presentation.lists.cards.CardItem;
import com.olebokolo.wordstack.presentation.navigation.ActivityNavigator;
import com.orm.SugarRecord;
import com.tekle.oss.android.animation.AnimationFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PracticeActivity extends AppCompatActivity {

    // dependencies
    public TypefaceCollection typefaceCollection;
    public TypefaceManager typefaceManager;
    public ActivityNavigator navigator;
    public LanguageService languageService;
    public FlagService flagService;
    public UserSettingsService settingsService;
    public CardsService cardsService;
    // view
    private ViewAnimator cardFlipper;
    private ViewGroup backToolbarButton;
    private TextView checkAllText;
    private TextView cardsCountText;
    private TextView frontLangWord;
    private TextView backLangWord;
    private FloatingActionButton checkButton;
    private FloatingActionButton yesButton;
    private FloatingActionButton noButton;
    private View shuffleButton;
    private ObjectAnimator fadeInCheckButtonOnAnswer;
    private ObjectAnimator moveYesButtonLeftOnCheck;
    private ObjectAnimator moveYesButtonRightOnAnswer;
    private ObjectAnimator fadeOutYesButtonOnAnswer;
    private ObjectAnimator moveNoButtonRightOnCheck;
    private ObjectAnimator moveNoButtonLeftOnAnswer;
    private ObjectAnimator fadeOutNoButtonOnAnswer;
    private ObjectAnimator moveCardLeft;
    private ObjectAnimator moveCardFromRight;
    private ObjectAnimator moveAccrossTheScreen;
    private int buttonsAnimationTime = 150;
    private int buttonsTranslationOnX = 200;
    private int cardTranslationOnX = 800;
    private int cardMoveTime = 150;
    // data
    private List<CardItem> allCardItems;
    private List<CardItem> currentCardItems;
    private CardItem topCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        WordStack.getInstance().injectDependenciesTo(this);
        EventBus.getDefault().register(this);
        findViews();
        setupTypeface();
        setupGoBackButton();
        setupAnimations();
        setupCheckButton();
        setupYesButton();
        setupNoButton();
        setupShuffleButton();
        getStacksFromIntentExtras();
        updateCurrentCardsCount();
        chooseCardsFaceSide();
        hideActionButtons();
    }

    private void hideActionButtons() {
        noButton.hide();
        yesButton.hide();
        checkButton.hide();
    }

    private void showCheckButton() {
        checkButton.show();
        checkButton.setVisibility(View.VISIBLE);
        fadeInCheckButtonOnAnswer.start();
    }

    private void setupShuffleButton() {
        shuffleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideActionButtons();
                shuffleButton.postDelayed(new Runnable() {
                    @Override
                    public void run() { currentCardItems = shuffle(currentCardItems); setupNextCard(); if (cardFlipper.getDisplayedChild() > 0) flipCardIn(0); moveAccrossTheScreen.start(); } }, cardMoveTime);
                shuffleButton.postDelayed(new Runnable() { @Override public void run() { currentCardItems = shuffle(currentCardItems); setupNextCard(); moveAccrossTheScreen.start(); } }, cardMoveTime * 2);
                shuffleButton.postDelayed(new Runnable() { @Override public void run() { currentCardItems = shuffle(currentCardItems); setupNextCard(); moveAccrossTheScreen.start(); } }, cardMoveTime * 3);
                shuffleButton.postDelayed(new Runnable() { @Override public void run() { currentCardItems = shuffle(currentCardItems); setupNextCard(); moveNextCardIn(); showCheckButton();} }, cardMoveTime * 4);
            }
        });
    }

    @Subscribe
    public void onStartPracticeEvent(PracticeStartEvent e) {
        checkButton.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!currentCardItems.isEmpty()) {
                    setupNextCard();
                    moveNextCardIn();
                    showCheckButton();
                }
            }
        }, buttonsAnimationTime);
    }

    @Subscribe
    public void onTurnOverCardsEvent(PracticeTurnOverCardsEvent e) {
        turnOverCards();
        onStartPracticeEvent(new PracticeStartEvent());
    }

    private void turnOverCards() {
        ArrayList<CardItem> allCardItemsCopy = new ArrayList<>(allCardItems);
        allCardItems.clear();
        for (CardItem c : allCardItemsCopy)
            allCardItems.add(new CardItem(
                    c.getBackLangFlagResource(), c.getFrontLangFlagResource(),
                    c.getBackLangText(), c.getFrontLangText()));
        currentCardItems = new ArrayList<>(allCardItems);
    }

    private void chooseCardsFaceSide() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new PracticeChooseFaceSideDialog(PracticeActivity.this).show();
            }
        }, buttonsAnimationTime);
    }

    private void setupAnimations() {
        moveAccrossTheScreen = ObjectAnimator.ofFloat(cardFlipper, "translationX", cardTranslationOnX, -cardTranslationOnX);
        moveCardLeft = ObjectAnimator.ofFloat(cardFlipper, "translationX", 0, -cardTranslationOnX);
        moveCardFromRight = ObjectAnimator.ofFloat(cardFlipper, "translationX", cardTranslationOnX, 0);

        moveAccrossTheScreen.setDuration(cardMoveTime);
        moveCardLeft.setDuration(cardMoveTime);
        moveCardFromRight.setDuration(cardMoveTime);

        fadeInCheckButtonOnAnswer = ObjectAnimator.ofFloat(checkButton, "alpha", 0, 1);
        moveYesButtonLeftOnCheck = ObjectAnimator.ofFloat(yesButton, "translationX", 0, -buttonsTranslationOnX);
        moveYesButtonRightOnAnswer = ObjectAnimator.ofFloat(yesButton, "translationX", -buttonsTranslationOnX, 0);
        fadeOutYesButtonOnAnswer = ObjectAnimator.ofFloat(yesButton, "alpha", 1, 0);
        moveNoButtonRightOnCheck = ObjectAnimator.ofFloat(noButton, "translationX", 0, buttonsTranslationOnX);
        moveNoButtonLeftOnAnswer = ObjectAnimator.ofFloat(noButton, "translationX", buttonsTranslationOnX, 0);
        fadeOutNoButtonOnAnswer = ObjectAnimator.ofFloat(noButton, "alpha", 1, 0);

        fadeInCheckButtonOnAnswer.setDuration(100);
        moveYesButtonLeftOnCheck.setDuration(buttonsAnimationTime);
        moveYesButtonRightOnAnswer.setDuration(buttonsAnimationTime);
        fadeOutYesButtonOnAnswer.setDuration(buttonsAnimationTime);
        moveNoButtonLeftOnAnswer.setDuration(buttonsAnimationTime);
        moveNoButtonRightOnCheck.setDuration(buttonsAnimationTime);
        fadeOutNoButtonOnAnswer.setDuration(buttonsAnimationTime);

        fadeInCheckButtonOnAnswer.addListener(fadeInCheckButtonOnAnswerAnimationListener);
        moveCardLeft.addListener(cardHiddenAnimationListener);
    }

    private void moveCurrentCardOut() {
        moveCardLeft.start();
    }

    private void setNextCard(CardItem card) {
        frontLangWord.setText(card.getFrontLangText());
        backLangWord.setText(card.getBackLangText());
    }

    private void moveNextCardIn() {
        moveCardFromRight.start();
        cardFlipper.setVisibility(View.VISIBLE);
    }

    private void finishExercise() {
        checkButton.hide();
        yesButton.hide();
        noButton.hide();
    }

    private Animator.AnimatorListener cardHiddenAnimationListener = new Animator.AnimatorListener() {
        @Override public void onAnimationStart(Animator animator) { }
        @Override public void onAnimationCancel(Animator animator) { }
        @Override public void onAnimationRepeat(Animator animator) { }
        @Override public void onAnimationEnd(Animator animator) {
            cardFlipper.setVisibility(View.INVISIBLE);
            if (cardFlipper.getDisplayedChild() > 0) flipCardIn(0);
            if (!currentCardItems.isEmpty()) {
                setupNextCard();
                moveNextCardIn();
            } else finishExercise();
        }
    };

    private Animator.AnimatorListener fadeInCheckButtonOnAnswerAnimationListener = new Animator.AnimatorListener() {
        @Override public void onAnimationStart(Animator animator) { }
        @Override public void onAnimationCancel(Animator animator) { }
        @Override public void onAnimationRepeat(Animator animator) { }
        @Override public void onAnimationEnd(Animator animator) {
            checkButton.setVisibility(View.VISIBLE);
            checkButton.setAlpha(1.0f);
            yesButton.setAlpha(1.0f);
            noButton.setAlpha(1.0f);
        }
    };

    private void setupNextCard() {
        topCard = currentCardItems.get(0);
        setNextCard(topCard);
    }

    private void setupYesButton() {
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentCardItems.remove(topCard);
                updateCurrentCardsCount();
                hideAnswerButtonsAndShowCheckButton();
                moveCurrentCardOut();
            }
        });
    }

    private void setupNoButton() {
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentCardItems.remove(topCard);
                currentCardItems.add(topCard);
                hideAnswerButtonsAndShowCheckButton();
                moveCurrentCardOut();
            }
        });
    }

    private void hideAnswerButtonsAndShowCheckButton() {
        moveNoButtonLeftOnAnswer.start();
        moveYesButtonRightOnAnswer.start();
        fadeInCheckButtonOnAnswer.start();
        fadeOutNoButtonOnAnswer.start();
        fadeOutYesButtonOnAnswer.start();
    }

    private void setupCheckButton() {
        cardFlipper.setVisibility(View.INVISIBLE);
        checkButton.setOnClickListener(checkClick);
        frontLangWord.setOnClickListener(checkClick);
    }

    private View.OnClickListener checkClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            flipCardIn(buttonsAnimationTime);
            yesButton.show();
            yesButton.setAlpha(1.0f);
            yesButton.setVisibility(View.VISIBLE);
            noButton.show();
            noButton.setVisibility(View.VISIBLE);
            noButton.setAlpha(1.0f);
            moveYesButtonLeftOnCheck.start();
            moveNoButtonRightOnCheck.start();
            checkButton.setVisibility(View.INVISIBLE);
        }
    };

    private void flipCardIn(int duration) {
        AnimationFactory.flipTransition(cardFlipper, AnimationFactory.FlipDirection.BOTTOM_TOP, duration);
    }

    private void updateCurrentCardsCount() {
        cardsCountText.setText(String.valueOf(currentCardItems.size()));
    }

    private void getStacksFromIntentExtras() {
        Serializable serializable = getIntent().getSerializableExtra("stacks");
        if (serializable != null) {
            String stacksString = serializable.toString();
            String[] stackIds = stacksString.split(",");
            List<Card> cards = getCardsWith(stackIds);
            allCardItems = shuffle(cardsService.getCardItemsFrom(cards));
            currentCardItems = new ArrayList<>(allCardItems);
        }
    }

    private List<CardItem> shuffle(List<CardItem> items) {
        ArrayList<CardItem> copy = new ArrayList<>(items);
        Collections.shuffle(copy);
        return copy;
    }

    private List<Card> getCardsWith(String[] stackIds) {
        List<Card> cards = new ArrayList<>();
        for (String id : stackIds) cards.addAll(SugarRecord.find(Card.class, "stack_Id = ?", id));
        return cards;
    }

    private void findViews() {
        backToolbarButton = (ViewGroup) findViewById(R.id.back_toolbar_button);
        checkAllText = (TextView) findViewById(R.id.check_all_text);
        cardsCountText = (TextView) findViewById(R.id.cards_count);
        frontLangWord = (TextView) findViewById(R.id.front_lang_word);
        backLangWord = (TextView) findViewById(R.id.back_lang_word);
        cardFlipper = (ViewAnimator) findViewById(R.id.card_flipper);
        checkButton = (FloatingActionButton) findViewById(R.id.check_button);
        yesButton = (FloatingActionButton) findViewById(R.id.yes_button);
        noButton = (FloatingActionButton) findViewById(R.id.no_button);
        shuffleButton = findViewById(R.id.shuffle_button);
    }

    private void setupTypeface() {
        typefaceManager.setTypefaceForContainer(backToolbarButton, typefaceCollection.getRalewayMedium());
        typefaceManager.setTypeface(checkAllText, typefaceCollection.getRalewayMedium());
        typefaceManager.setTypeface(frontLangWord, typefaceCollection.getRalewayLight());
        typefaceManager.setTypeface(backLangWord, typefaceCollection.getRalewayLight());
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
