package com.olebokolo.wordstack.presentation.lists.cards;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.olebokolo.wordstack.R;
import com.olebokolo.wordstack.core.app.WordStack;
import com.olebokolo.wordstack.core.events.CardEditRequestEvent;
import com.olebokolo.wordstack.core.utils.TypefaceCollection;
import com.olebokolo.wordstack.core.utils.TypefaceManager;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class CardItemAdapter extends RecyclerView.Adapter<CardItemAdapter.CardItemHolder> {

    // dependencies
    public TypefaceCollection typefaceCollection;
    public TypefaceManager typefaceManager;
    // data
    private List<CardItem> cardItems;

    public CardItemAdapter(List<CardItem> cardItems) {
        WordStack.getInstance().injectDependenciesTo(this);
        this.cardItems = cardItems;
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
        typefaceManager.setTypefaceForContainer((ViewGroup) holder.container, typefaceCollection.getRalewayMedium());
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
