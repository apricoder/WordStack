package com.olebokolo.wordstack.presentation.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.olebokolo.wordstack.R;
import com.olebokolo.wordstack.core.app.WordStack;
import com.olebokolo.wordstack.core.events.CardAddedEvent;
import com.olebokolo.wordstack.core.languages.flags.FlagService;
import com.olebokolo.wordstack.core.languages.services.LanguageService;
import com.olebokolo.wordstack.core.model.Card;
import com.olebokolo.wordstack.core.model.Language;
import com.olebokolo.wordstack.core.model.Side;
import com.olebokolo.wordstack.core.model.Stack;
import com.olebokolo.wordstack.core.model.UserSettings;
import com.olebokolo.wordstack.core.user.settings.services.UserSettingsService;
import com.olebokolo.wordstack.core.utils.TypefaceCollection;
import com.olebokolo.wordstack.core.utils.TypefaceManager;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class CardAddDialog extends Dialog {

    public TypefaceCollection typefaceCollection;
    public TypefaceManager typefaceManager;
    public LanguageService languageService;
    public FlagService flagService;
    public UserSettingsService settingsService;
    public OkHttpClient client;

    private Activity activity;
    private ImageView frontLangIcon;
    private ImageView backLangIcon;
    private EditText frontLangText;
    private EditText backLangText;
    private CheckBox autoTranslateCheckBox;

    private Stack stack;
    private Language frontLanguage;
    private Language backLanguage;
    private Long frontLangId;
    private Long backLangId;
    private String apiKey;
    private String apiUrl;
    private Handler handler;
    private int delay  = 1000;
    private boolean autoTranslation;
    private boolean editingFrontWord;
    private boolean editingBackWord;

    public CardAddDialog(Activity activity, Stack stack) {
        super(activity);
        this.stack = stack;
        this.activity = activity;
        WordStack.getInstance().injectDependenciesTo(this);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        setContentView(R.layout.dialog_card_edit);
        findViews();
        setupLanguages();
        setupLanguagesIcons();
        setupFonts();
        setupCloseButton();
        setupBackButton();
        setupFrontLangFocusListener();
        setupBackLangFocusListener();
        setupAddCardButton();
        setupAutoTranslationOfFrontWord();
        setupAutoTranslationOfBackWord();
        setupAutoTranslateTrigger();
        setupAutoTranslationStatus();
    }

    private void setupBackLangFocusListener() {
        backLangText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                editingBackWord = b;
            }
        });
    }

    private void setupFrontLangFocusListener() {
        frontLangText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                editingFrontWord = b;
            }
        });
    }

    private void setupAutoTranslationStatus() {
        autoTranslation = autoTranslateCheckBox.isChecked();
    }

    private void setupAutoTranslationOfFrontWord() {
        frontLangText.addTextChangedListener(frontLangTextWatcher);
    }

    private void setupAutoTranslationOfBackWord() {
        backLangText.addTextChangedListener(backLangTextWatcher);
    }

    private void setupAutoTranslateTrigger() {
        autoTranslateCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean newValue) {
                setupAutoTranslationStatus();
                if (newValue && editingFrontWord) autoTranslateFrontWord(frontLangText.getText());
                if (newValue && editingBackWord) autoTranslateBackWord(backLangText.getText());
            }
        });
    }

    private Runnable onStoppedTypingFrontWord = new Runnable() {
        @Override
        public void run() {
            autoTranslateFrontWord(frontLangText.getText());
        }
    };

    private Runnable onStoppedTypingBackWord = new Runnable() {
        @Override
        public void run() {
            autoTranslateBackWord(backLangText.getText());
        }
    };

    private TextWatcher frontLangTextWatcher = new TextWatcher() {
        @Override public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) { }
        @Override public void onTextChanged(CharSequence newText, int start, int count, int after) { if (handler != null) handler.removeCallbacks(onStoppedTypingFrontWord); }
        @Override public void afterTextChanged(Editable editable) {
            if (autoTranslation && editingFrontWord) {
                handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(onStoppedTypingFrontWord, delay);
            }
        }
    };

    private TextWatcher backLangTextWatcher = new TextWatcher() {
        @Override public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) { }
        @Override public void onTextChanged(CharSequence newText, int start, int count, int after) { if (handler != null) handler.removeCallbacks(onStoppedTypingFrontWord); }
        @Override public void afterTextChanged(Editable editable) {
            if (autoTranslation && editingBackWord) {
                handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(onStoppedTypingBackWord, delay);
            }
        }
    };

    private void autoTranslateFrontWord(CharSequence newText) {
        Log.d("WordStack", "Auto translating " + newText.toString());
        if (!newText.toString().isEmpty()) {
            Request request = getRequest(String.valueOf(newText));
            client.newCall(request).enqueue(frontWordTranslated);
        } else setBackWord("");
    }

    private Callback frontWordTranslated = new Callback() {
        @Override public void onFailure(Request request, IOException e) { showToast("Failure!"); }
        @Override public void onResponse(Response response) throws IOException {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                try {
                    String translation = new JSONObject(responseBody).getJSONArray("text").getString(0);
                    setBackWord(translation);
                } catch (JSONException e) { showToast("Couldn't parse"); }
            } else showToast("Unsuccessful!"); }
    };

    private Callback backWordTranslated = new Callback() {
        @Override public void onFailure(Request request, IOException e) { showToast("Failure!"); }
        @Override public void onResponse(Response response) throws IOException {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                try {
                    String translation = new JSONObject(responseBody).getJSONArray("text").getString(0);
                    setFrontWord(translation);
                } catch (JSONException e) { showToast("Couldn't parse"); }
            } else showToast("Unsuccessful!"); }
    };

    private void autoTranslateBackWord(Editable newText) {
        Log.d("WordStack", "Auto translating " + newText.toString());
        if (!newText.toString().isEmpty() && autoTranslation) {
            Request request = getRequest(String.valueOf(newText));
            client.newCall(request).enqueue(backWordTranslated);
        } else setFrontWord("");
    }

    private void setFrontWord(final String text) {
        activity.runOnUiThread(new Runnable() {
            @Override public void run() { frontLangText.setText(text); }
        });
    }

    private void setBackWord(final String text) {
        activity.runOnUiThread(new Runnable() {
            @Override public void run() { backLangText.setText(text); }
        });
    }

    private void showToast(final String text) {
        activity.runOnUiThread(new Runnable() {
            @Override public void run() { Toast.makeText(activity, text, Toast.LENGTH_SHORT).show(); }
        });
    }

    private Request getRequest(String text) {
        apiKey = activity.getString(R.string.yandex_api_key);
        apiUrl = activity.getString(R.string.yandex_api_url);
        String fromLanguage = editingFrontWord ? frontLanguage.getShortName() : backLanguage.getShortName();
        String toLanguage = editingFrontWord ? backLanguage.getShortName() : frontLanguage.getShortName();
        String url = apiUrl + "?key=" + apiKey + "&text=" + text + "&lang=" + fromLanguage + "-" + toLanguage;
        Log.d("WordStack", url);
        return new Request.Builder().url(url).build();
    }

    private void setupAddCardButton() {
        findViewById(R.id.save_card_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCardFromEnteredData().save();
                EventBus.getDefault().post(new CardAddedEvent());
                hideKeyboardAndDismiss();
            }
        });
    }

    private Card getCardFromEnteredData() {
        String frontWord = frontLangText.getText().toString();
        String backWord = backLangText.getText().toString();
        Long frontSideId = saveCardSide(frontWord, frontLangId);
        Long backSideId = saveCardSide(backWord, backLangId);
        return Card.builder()
                .stackId(stack.getId())
                .frontSideId(frontSideId)
                .backSideId(backSideId)
                .build();
    }

    private long saveCardSide(String text, Long langId) {
        return Side.builder()
                .languageId(langId)
                .content(text)
                .build().save();
    }

    private void findViews() {
        frontLangIcon = (ImageView) findViewById(R.id.front_lang_icon);
        backLangIcon = (ImageView) findViewById(R.id.back_lang_icon);
        frontLangText = (EditText) findViewById(R.id.front_lang_text);
        backLangText = (EditText) findViewById(R.id.back_lang_text);
        autoTranslateCheckBox = (CheckBox) findViewById(R.id.auto_translate_checkbox);
    }

    private void setupLanguagesIcons() {
        frontLanguage = languageService.findById(frontLangId);
        backLanguage = languageService.findById(backLangId);
        frontLangIcon.setImageResource(flagService.getFlagByLanguageShortName(frontLanguage.getShortName()));
        backLangIcon.setImageResource(flagService.getFlagByLanguageShortName(backLanguage.getShortName()));
    }

    private void setupLanguages() {
        UserSettings userSettings = settingsService.getUserSettings();
        frontLangId = userSettings.getFrontLangId();
        backLangId = userSettings.getBackLangId();
    }

    private void setupFonts() {
        typefaceManager.setTypefaceForContainer((ViewGroup) findViewById(R.id.root_layout), typefaceCollection.getRalewayMedium());
    }

    private void setupCloseButton() {
        findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboardAndDismiss();
            }
        });
    }

    private void setupBackButton() {
        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboardAndDismiss();
            }
        });
    }

    private void hideKeyboardAndDismiss() {
        hideKeyboard();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, 200);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(findViewById(R.id.root_layout).getWindowToken(), 0);
    }


}
