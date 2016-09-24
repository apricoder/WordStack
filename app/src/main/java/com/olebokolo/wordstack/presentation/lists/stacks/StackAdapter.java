package com.olebokolo.wordstack.presentation.lists.stacks;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.olebokolo.wordstack.R;
import com.olebokolo.wordstack.core.app.WordStack;
import com.olebokolo.wordstack.core.events.StackActionsDialogCalledEvent;
import com.olebokolo.wordstack.core.utils.TypefaceCollection;
import com.olebokolo.wordstack.core.utils.TypefaceManager;
import com.olebokolo.wordstack.presentation.lists.holders.TextTextHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class StackAdapter extends ArrayAdapter<StackItem> {

    // constants
    public static final Integer ANIMATED_KEY = R.anim.slide_in_from_right;
    public static final String ANIMATED = "ANIMATED";
    // dependencies
    public TypefaceCollection typefaceCollection;
    public TypefaceManager typefaceManager;
    // data
    private final List<StackItem> stackItems;
    private final Application context;
    private final Animation sliding;

    public StackAdapter(List<StackItem> stackItems) {
        super(WordStack.getInstance(), R.layout.item_stack, stackItems);
        WordStack.getInstance().injectDependenciesTo(this);
        this.stackItems = stackItems;
        this.context = WordStack.getInstance();
        this.sliding = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_from_right);
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {
        TextTextHolder holder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.item_stack, parent, false);
            holder = new TextTextHolder();
            holder.setMainText((TextView) row.findViewById(R.id.stack_name));
            holder.setSecondaryText((TextView) row.findViewById(R.id.cards_count));
            row.setTag(holder);
        } else holder = (TextTextHolder) row.getTag();
        setupContextMenuClick(row, position);
        typefaceManager.setTypeface(holder.getMainText(), typefaceCollection.getRalewayMedium());
        typefaceManager.setTypeface(holder.getSecondaryText(), typefaceCollection.getRalewayLight());
        StackItem stackItem = stackItems.get(position);
        String cardsCount = String.valueOf(stackItem.getCardsCount()) + " cards";
        holder.getMainText().setText(stackItem.getStackName());
        holder.getSecondaryText().setText(cardsCount);
        animateSlideIfFirstTime(row);
        return row;
    }

    private void setupContextMenuClick(View row, final int stackPosition) {
        row.findViewById(R.id.stack_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new StackActionsDialogCalledEvent("Actions dialog called!", stackPosition));
            }
        });
    }

    private void animateSlideIfFirstTime(final View row) {
        if (row.getTag(ANIMATED_KEY) != ANIMATED) {
            row.setVisibility(View.INVISIBLE);
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    row.startAnimation(sliding);
                    row.setTag(ANIMATED_KEY, ANIMATED);
                    row.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    public StackItem getItem(int i) {
        return stackItems.get(i);
    }

    @Override
    public int getCount() {
        return stackItems.size();
    }
}
