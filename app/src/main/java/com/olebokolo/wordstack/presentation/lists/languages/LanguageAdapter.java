package com.olebokolo.wordstack.presentation.lists.languages;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.olebokolo.wordstack.R;
import com.olebokolo.wordstack.presentation.lists.holders.IconLabelHolder;

public class LanguageAdapter extends ArrayAdapter<LanguageItem> {

    private LanguageItem[] languages;
    private Integer itemResource;
    private Activity activity;

    public LanguageAdapter(Activity activity, int itemResource, LanguageItem[] languages) {
        super(activity, itemResource, languages);
        initFields(activity, itemResource, languages);
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {
        IconLabelHolder holder;
        if (row == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            row = inflater.inflate(itemResource, parent, false);
            holder = new IconLabelHolder();
            holder.setIcon((ImageView)row.findViewById(R.id.lang_icon));
            holder.setLabel((TextView) row.findViewById(R.id.lang_name));
            row.setTag(holder);
        } else holder = (IconLabelHolder) row.getTag();
        LanguageItem languageItem = languages[position];
        holder.getLabel().setText(languageItem.getLanguage().getName());
        holder.getIcon().setImageResource(languageItem.getFlagId());
        return row;
    }

    private void initFields(Activity activity, int itemResource, LanguageItem[] languages) {
        this.itemResource = itemResource;
        this.activity = activity;
        this.languages = languages;
    }
}
