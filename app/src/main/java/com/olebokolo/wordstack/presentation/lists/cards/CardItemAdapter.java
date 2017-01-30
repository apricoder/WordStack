package com.olebokolo.wordstack.presentation.lists.cards;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.olebokolo.wordstack.R;
import com.olebokolo.wordstack.core.app.WordStack;
import com.olebokolo.wordstack.core.events.CardEditRequestEvent;
import com.olebokolo.wordstack.core.events.CardSayWordEvent;
import com.olebokolo.wordstack.core.events.CardSpeakOutEvent;
import com.olebokolo.wordstack.core.events.CardStopSpeakingOutEvent;
import com.olebokolo.wordstack.core.events.LanguageAvailabilityDiscoveredEvent;
import com.olebokolo.wordstack.core.utils.TypefaceCollection;
import com.olebokolo.wordstack.core.utils.TypefaceManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CardItemAdapter extends RecyclerView.Adapter<CardItemAdapter.CardItemHolder> {

    // dependencies
    public TypefaceCollection typefaceCollection;
    public TypefaceManager typefaceManager;
    // data
    private List<CardItem> cardItems;
    private boolean speakingOutLoud;
    private boolean allShownSpeakerIcon;
    private boolean frontLangAvailable;
    private boolean backLangAvailable;

    public CardItemAdapter(List<CardItem> cardItems) {
        WordStack.getInstance().injectDependenciesTo(this);
        EventBus.getDefault().register(this);
        this.cardItems = cardItems;
    }

    @Subscribe
    public void onEvent(CardSpeakOutEvent event) {
        speakingOutLoud = true;
        notifyDataSetChanged();
    }

    @Subscribe
    public void onEvent(CardStopSpeakingOutEvent event) {
        speakingOutLoud = false;
        notifyDataSetChanged();
    }

    @Subscribe
    public void onEvent(LanguageAvailabilityDiscoveredEvent event) {
        if (event.getLanguage() == CardSayWordEvent.FRONT_LANG)
            frontLangAvailable = event.isSupported();
        else backLangAvailable = event.isSupported();
    }

    @Override
    public CardItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card, parent, false);
        return new CardItemHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final CardItemHolder holder, int position) {
        CardItem cardItem = cardItems.get(position);
        holder.frontLangText.setText(cardItem.getFrontLangText());
        holder.backLangText.setText(cardItem.getBackLangText());
        if (allShownSpeakerIcon) showSpeakerIcon(holder); else showFlagIcon(holder, cardItem);
        typefaceManager.setTypefaceForContainer((ViewGroup) holder.container, typefaceCollection.getRalewayMedium());
        holder.frontLangLayout.setOnClickListener(getHolderClick(holder, CardSayWordEvent.FRONT_LANG));
        holder.backLangLayout.setOnClickListener(getHolderClick(holder, CardSayWordEvent.BACK_LANG));
        showSpeakersInAnimationIfNeeded(holder);
        showSpeakersOutAnimationIfNeeded(holder, cardItem);
    }

    private void showSpeakersOutAnimationIfNeeded(final CardItemHolder holder, final CardItem cardItem) {
        if (!speakingOutLoud && !holder.shownFlagIcon && allShownSpeakerIcon) {
            final ObjectAnimator fadeFrontIconOut = ObjectAnimator.ofFloat(holder.frontLangIcon, "alpha", 1, 0);
            final ObjectAnimator fadeBackIconOut = ObjectAnimator.ofFloat(holder.backLangIcon, "alpha", 1, 0);
            final ObjectAnimator fadeFrontIconIn = ObjectAnimator.ofFloat(holder.frontLangIcon, "alpha", 0, 1);
            final ObjectAnimator fadeBackIconIn = ObjectAnimator.ofFloat(holder.backLangIcon, "alpha", 0, 1);
            fadeFrontIconIn.setDuration(200);
            fadeBackIconIn.setDuration(200);
            fadeFrontIconOut.setDuration(200);
            fadeBackIconOut.setDuration(200);
            fadeFrontIconOut.start();
            fadeBackIconOut.start();
            fadeFrontIconOut.addListener(new Animator.AnimatorListener() {
                @Override public void onAnimationStart(Animator animator) { }
                @Override public void onAnimationCancel(Animator animator) { }
                @Override public void onAnimationRepeat(Animator animator) { }
                @Override public void onAnimationEnd(Animator animator) {
                    showFlagIcon(holder, cardItem);
                    fadeFrontIconIn.start();
                    fadeBackIconIn.start();
                    holder.shownFlagIcon = true;
                    holder.shownSpeakerIcon = false;
                    allShownSpeakerIcon = false;
                    notifyDataSetChanged();
                }
            });
        }
    }

    private void showSpeakersInAnimationIfNeeded(final CardItemHolder holder) {
        if (speakingOutLoud && !holder.shownSpeakerIcon && !allShownSpeakerIcon) {
            final ObjectAnimator fadeFrontIconOut = ObjectAnimator.ofFloat(holder.frontLangIcon, "alpha", 1, 0);
            final ObjectAnimator fadeFrontIconIn = ObjectAnimator.ofFloat(holder.frontLangIcon, "alpha", 0, 1);
            final ObjectAnimator fadeBackIconOut = ObjectAnimator.ofFloat(holder.backLangIcon, "alpha", 1, 0);
            final ObjectAnimator fadeBackIconIn = ObjectAnimator.ofFloat(holder.backLangIcon, "alpha", 0, 1);
            fadeFrontIconIn.setDuration(200);
            fadeBackIconIn.setDuration(200);
            fadeFrontIconOut.setDuration(200);
            fadeBackIconOut.setDuration(200);
            fadeFrontIconOut.start();
            fadeBackIconOut.start();
            fadeFrontIconOut.addListener(new Animator.AnimatorListener() {
                @Override public void onAnimationStart(Animator animator) { }
                @Override public void onAnimationCancel(Animator animator) { }
                @Override public void onAnimationRepeat(Animator animator) { }
                @Override public void onAnimationEnd(Animator animator) {
                    showSpeakerIcon(holder);
                    fadeFrontIconIn.start();
                    fadeBackIconIn.start();
                    holder.shownSpeakerIcon = true;
                    holder.shownFlagIcon = false;
                    allShownSpeakerIcon = true;
                    notifyDataSetChanged();
                }
            });
        }
    }

    private void showFlagIcon(CardItemHolder holder, CardItem cardItem) {
        holder.frontLangIcon.setImageResource(cardItem.getFrontLangFlagResource());
        holder.frontLangIcon.setBorderColor(WordStack.getInstance().getResources().getColor(R.color.mainBlue));
        holder.backLangIcon.setImageResource(cardItem.getBackLangFlagResource());
        holder.backLangIcon.setBorderColor(WordStack.getInstance().getResources().getColor(R.color.mainBlue));
    }

    private void showSpeakerIcon(CardItemHolder holder) {
        holder.frontLangIcon.setImageResource(frontLangAvailable ? R.drawable.c_volume_blue : R.drawable.c_volume_off_light_light_blue);
        holder.frontLangIcon.setBorderColor(WordStack.getInstance().getResources().getColor(R.color.transparent));
        holder.backLangIcon.setImageResource(backLangAvailable ? R.drawable.c_volume_blue : R.drawable.c_volume_off_light_light_blue);
        holder.backLangIcon.setBorderColor(WordStack.getInstance().getResources().getColor(R.color.transparent));
    }

    private View.OnClickListener getHolderClick(final CardItemHolder holder, final int side) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allShownSpeakerIcon) {
                    if ((side == CardSayWordEvent.FRONT_LANG && frontLangAvailable) || (side == CardSayWordEvent.BACK_LANG && backLangAvailable))
                        EventBus.getDefault().post(new CardSayWordEvent(
                                side == CardSayWordEvent.FRONT_LANG
                                        ? holder.frontLangText.getText().toString()
                                        : holder.backLangText.getText().toString()
                                , side));
                }
                else EventBus.getDefault().post(new CardEditRequestEvent(holder.getAdapterPosition()));
            }
        };
    }

    @Override
    public int getItemCount() {
        return cardItems.size();
    }

    class CardItemHolder extends RecyclerView.ViewHolder {
        View container;
        View frontLangLayout;
        View backLangLayout;
        TextView frontLangText;
        TextView backLangText;
        CircleImageView frontLangIcon;
        CircleImageView backLangIcon;
        boolean shownSpeakerIcon;
        boolean shownFlagIcon;

        CardItemHolder(View view) {
            super(view);
            container = view;
            frontLangLayout = view.findViewById(R.id.front_lang_layout);
            backLangLayout = view.findViewById(R.id.back_lang_layout);
            frontLangText = (TextView) view.findViewById(R.id.front_lang_text);
            frontLangIcon = (CircleImageView) view.findViewById(R.id.front_lang_icon);
            backLangText = (TextView) view.findViewById(R.id.back_lang_text);
            backLangIcon = (CircleImageView) view.findViewById(R.id.back_lang_icon);
        }
    }
}
