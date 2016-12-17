package com.olebokolo.wordstack.presentation.lists.cards;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.olebokolo.wordstack.R;
import com.olebokolo.wordstack.core.app.WordStack;
import com.olebokolo.wordstack.core.events.ReanimateCardEnterEvent;
import com.olebokolo.wordstack.core.events.ReanimateUpdatedCardEvent;
import com.olebokolo.wordstack.core.utils.TypefaceCollection;
import com.olebokolo.wordstack.core.utils.TypefaceManager;
import com.olebokolo.wordstack.presentation.lists.holders.DoubleIconLabelHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class CardAdapter extends ArrayAdapter<CardItem> {

    // dependencies
    public TypefaceCollection typefaceCollection;
    public TypefaceManager typefaceManager;
    // data
    private final List<CardItem> cardItems;
    private final WordStack context;
    private final Animation sliding;
    private final Animation fading;
    private int reanimatingEnterIndex = Integer.MAX_VALUE;
    private int reanimatingUpdateIndex = Integer.MAX_VALUE;

    public CardAdapter(List<CardItem> cardItems) {
        super(WordStack.getInstance(), R.layout.item_card, cardItems);
        WordStack.getInstance().injectDependenciesTo(this);
        EventBus.getDefault().register(this);
        this.cardItems = cardItems;
        this.context = WordStack.getInstance();
        this.sliding = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_from_left);
        this.fading = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_long);
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {
        DoubleIconLabelHolder holder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.item_card, parent, false);
            holder = new DoubleIconLabelHolder();
            holder.setFirstIcon((ImageView) row.findViewById(R.id.front_lang_icon));
            holder.setFirstLabel((TextView) row.findViewById(R.id.front_lang_text));
            holder.setSecondIcon((ImageView) row.findViewById(R.id.back_lang_icon));
            holder.setSecondLabel((TextView) row.findViewById(R.id.back_lang_text));
            row.setTag(holder);
        } else holder = (DoubleIconLabelHolder) row.getTag();
        typefaceManager.setTypefaceForContainer((ViewGroup) row, typefaceCollection.getRalewayMedium());
        CardItem cardItem = cardItems.get(position);
        holder.getFirstIcon().setImageResource(cardItem.getFrontLangFlagResource());
        holder.getSecondIcon().setImageResource(cardItem.getBackLangFlagResource());
        holder.getFirstLabel().setText(cardItem.getFrontLangText());
        holder.getSecondLabel().setText(cardItem.getBackLangText());
        animateEnterSlideIfNeeded(row, position);
        animateUpdateIfNeeded(row, position);
        return row;
    }

    private void animateUpdateIfNeeded(final View row, int position) {
        if (position == reanimatingUpdateIndex) {
            reanimatingUpdateIndex = Integer.MAX_VALUE;
            row.setVisibility(View.INVISIBLE);
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    row.startAnimation(fading);
                    row.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    private void animateEnterSlideIfNeeded(final View row, int position) {
        if (position == reanimatingEnterIndex) {
            reanimatingEnterIndex = Integer.MAX_VALUE;
            row.setVisibility(View.INVISIBLE);
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    row.startAnimation(sliding);
                    row.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Subscribe
    public void onEvent(ReanimateCardEnterEvent event) {
        reanimatingEnterIndex = event.getPosition();
    }

    @Subscribe
    public void onEvent(ReanimateUpdatedCardEvent event) {
        reanimatingUpdateIndex = event.getPosition();
    }

    @Override
    public CardItem getItem(int i) {
        return cardItems.get(i);
    }

    @Override
    public int getCount() {
        return cardItems.size();
    }

}
