package com.olebokolo.wordstack.presentation.lists.holders;

import android.widget.ImageView;
import android.widget.TextView;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DoubleIconLabelHolder {
    private ImageView firstIcon;
    private TextView firstLabel;
    private ImageView secondIcon;
    private TextView secondLabel;
}
