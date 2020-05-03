package com.example.lhd_2019;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private TextToSpeech converter;
    private EditText editor;
    private SeekBar pitchbar;
    private SeekBar speedbar;
    private Button hearbutton;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        hearbutton = findViewById(R.id.button_speak);

        converter = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){
                   int result = converter.setLanguage(Locale.ENGLISH);

                   if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                    Log.e("TTS", "Language not supported");
                   } else{
                       hearbutton.setEnabled(true);
                   }
                }
                else{
                    Log.e("TTS", "Initialization failed");
                }
            }
        });

        editor = findViewById(R.id.edit_text);
        pitchbar = findViewById(R.id.seek_bar_pitch);
        speedbar = findViewById(R.id.seek_bar_speed);

        hearbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
            }
        });

    }

    private void speak(){
        String text = editor.getText().toString();
        float pitch = (float) pitchbar.getProgress() / 50;
        if(pitch < 0.1) pitch = 0.1f;
        float speed = (float) speedbar.getProgress() / 50;
        if(speed < 0.1) speed = 0.1f;

        converter.setPitch(pitch);
        converter.setSpeechRate(speed);

        converter.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    protected void onDestroy(){
        if(converter != null){
            converter.stop();
            converter.shutdown();
        }
        super.onDestroy();
    }


}
