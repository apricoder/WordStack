package com.olebokolo.wordstack.presentation.lists.stacks;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.olebokolo.wordstack.R;
import com.olebokolo.wordstack.core.app.WordStack;
import com.olebokolo.wordstack.core.events.PracticeStackCheckedEvent;
import com.olebokolo.wordstack.core.utils.TypefaceCollection;
import com.olebokolo.wordstack.core.utils.TypefaceManager;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class PracticeStackItemAdapter extends RecyclerView.Adapter<PracticeStackItemAdapter.StackItemHolder> {

    // dependencies
    public TypefaceCollection typefaceCollection;
    public TypefaceManager typefaceManager;
    // data
    private List<PracticeStackItem> stackItems;

    public PracticeStackItemAdapter(List<PracticeStackItem> stackItems) {
        WordStack.getInstance().injectDependenciesTo(this);
        this.stackItems = stackItems;
    }

    @Override
    public StackItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_practice_stack, parent, false);
        return new StackItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final StackItemHolder holder, int position) {
        PracticeStackItem stackItemItem = stackItems.get(position);
        holder.stackNameText.setText(stackItemItem.getStackName());
        holder.cardsCountText.setText(stackItemItem.getCardsCount() + " cards");
        typefaceManager.setTypeface(holder.stackNameText, typefaceCollection.getRalewayMedium());
        typefaceManager.setTypeface(holder.cardsCountText, typefaceCollection.getRalewayLight());
        holder.container.setOnClickListener(getHolderClick(holder));
    }

    private View.OnClickListener getHolderClick(final StackItemHolder holder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = !holder.checkBox.isChecked();
                holder.checkBox.setChecked(isChecked);
                EventBus.getDefault().post(new PracticeStackCheckedEvent(holder.getAdapterPosition(), isChecked));
            }
        };
    }

    @Override
    public int getItemCount() {
        return stackItems.size();
    }

    class StackItemHolder extends RecyclerView.ViewHolder {
        View container;
        TextView stackNameText, cardsCountText;
        CheckBox checkBox;

        StackItemHolder(View view) {
            super(view);
            container = view;
            stackNameText = (TextView) view.findViewById(R.id.stack_name);
            cardsCountText = (TextView) view.findViewById(R.id.cards_count);
            checkBox = (CheckBox) view.findViewById(R.id.checkbox);
        }
    }
}
