package com.wuzx.atest.tts;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.wuzx.atest.R;
import com.wuzx.atest.databinding.TtsLayoutBinding;

import java.util.Locale;

public class TtsActivity extends AppCompatActivity implements View.OnClickListener {
    TTS tts;
    TtsLayoutBinding ttsLayoutBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ttsLayoutBinding = DataBindingUtil.setContentView(this, R.layout.tts_layout);
        tts = new TTS(this, Locale.KOREAN);
        ttsLayoutBinding.buttonTextToSpeech.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        tts.speak(ttsLayoutBinding.editText.getText().toString());
    }

    @Override
    protected void onDestroy() {
        if (tts != null && tts.isSpeaking()) {
            tts.stop();
        }

        if (tts != null) {
            tts.shutdown();
        }
        tts = null;
        super.onDestroy();
    }
}
