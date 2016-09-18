package com.olebokolo.wordstack.presentation.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.olebokolo.wordstack.R;
import com.olebokolo.wordstack.core.app.WordStack;
import com.olebokolo.wordstack.core.utils.TypefaceCollection;
import com.olebokolo.wordstack.core.utils.TypefaceManager;
import com.olebokolo.wordstack.presentation.navigation.ActivityNavigator;
import com.olebokolo.wordstack.presentation.navigation.NavigationDirection;

public class GreetingActivity extends AppCompatActivity {

    public ActivityNavigator navigator;
    public TypefaceCollection typefaceCollection;
    public TypefaceManager typefaceManager;

    private Button goNextButton;
    private TextView toolbarTitle;
    private ViewGroup rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greeting);
        WordStack.getInstance().injectDependenciesTo(this);
        findViews();
        setupTypefaces();
        setupGoNextButton();
    }

    private void findViews() {
        rootLayout = (ViewGroup) findViewById(R.id.root_layout);
        goNextButton = (Button) findViewById(R.id.go_next_button);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
    }

    private void setupTypefaces() {
        typefaceManager.setTypefaceForContainer(rootLayout, typefaceCollection.getRalewayLight());
        typefaceManager.setTypeface(goNextButton, typefaceCollection.getRalewayMedium());
        typefaceManager.setTypeface(toolbarTitle, typefaceCollection.getRalewayMedium());
    }

    @SuppressWarnings("ConstantConditions")
    private void setupGoNextButton() {
        goNextButton.setOnClickListener(goNextClickListener);
    }

    private View.OnClickListener goNextClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            navigator.goForwardWithSlideAnimationAndFurtherActivity(
                    GreetingActivity.this,
                    LanguagesActivity.class,
                    MainMenuActivity.class,
                    NavigationDirection.FORWARD);
        }
    };

}
