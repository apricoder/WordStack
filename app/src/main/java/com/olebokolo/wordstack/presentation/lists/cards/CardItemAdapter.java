package com.olebokolo.wordstack.presentation.lists.cards;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.olebokolo.wordstack.R;
import com.olebokolo.wordstack.core.app.WordStack;
import com.olebokolo.wordstack.core.events.CardEditRequestEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class CardItemAdapter extends RecyclerView.Adapter<CardItemAdapter.CardItemHolder> {

    private List<CardItem> cardItems;
    private final Animation sliding;
    private final Animation fading;
    private View.OnClickListener itemClick;

    public CardItemAdapter(List<CardItem> cardItems) {
        this.cardItems = cardItems;
        this.sliding = AnimationUtils.loadAnimation(WordStack.getInstance(), R.anim.slide_in_from_left);
        this.fading = AnimationUtils.loadAnimation(WordStack.getInstance(), R.anim.fade_in_long);
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
        holder.frontLangIcon.setImageResource(cardItem.getFrontLangFlagResource());
        holder.backLangIcon.setImageResource(cardItem.getBackLangFlagResource());
        holder.container.setOnClickListener(getHolderClick(holder));
    }

    private View.OnClickListener getHolderClick(final CardItemHolder holder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new CardEditRequestEvent(holder.getAdapterPosition()));
            }
        };
    }

    @Override
    public int getItemCount() {
        return cardItems.size();
    }

    public class CardItemHolder extends RecyclerView.ViewHolder {
        public View container;
        public TextView frontLangText, backLangText;
        public ImageView frontLangIcon, backLangIcon;

        public CardItemHolder(View view) {
            super(view);
            container = view;
            frontLangText = (TextView) view.findViewById(R.id.front_lang_text);
            frontLangIcon = (ImageView) view.findViewById(R.id.front_lang_icon);
            backLangText = (TextView) view.findViewById(R.id.back_lang_text);
            backLangIcon = (ImageView) view.findViewById(R.id.back_lang_icon);
        }
    }
}
