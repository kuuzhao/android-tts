package com.example.liang.lztts;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.speech.tts.TextToSpeech;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class MainActivity extends Activity implements TextToSpeech.OnInitListener, View.OnClickListener {

    EditText input;
    Button button_clear, button_speak;
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        input = (EditText) findViewById(R.id.input);
        // input.setText("听说一个效益变差的国企，市场萎缩，支持业绩差的员工，回家去，以后效益好的，可以再回来");
        input.setText("denote");
        button_clear = (Button) findViewById(R.id.button_clear);
        button_speak = (Button) findViewById(R.id.button_speak);

        button_clear.setOnClickListener(this);
        button_speak.setOnClickListener(this);

        tts = new TextToSpeech(this, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.button_clear:
            input.setText("");
            break;
        case R.id.button_speak:
            String text = input.getText().toString();
            if (!text.isEmpty()) {
                // tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "speak id");

                File parentDir = this.getFilesDir();
                Log.d("TTS", parentDir.getAbsolutePath());
                File wavFile = new File(parentDir, "denote-patts.wav");
                tts.synthesizeToFile(text, null, wavFile, "out file id");

                /*
                try {
                    Set<String> libs = new HashSet<String>();
                    String mapsFile = "/proc/" + android.os.Process.myPid() + "/maps";
                    BufferedReader reader = new BufferedReader(new FileReader(mapsFile));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.endsWith(".so")) {
                            int n = line.lastIndexOf(" ");
                            libs.add(line.substring(n + 1));
                        }
                    }
                    Log.d("Ldd", libs.size() + " libraries:");
                    for (String lib : libs) {
                        Log.d("Ldd", lib);
                    }
                } catch (FileNotFoundException e) {
                    // Do some error handling...
                } catch (IOException e) {
                    // Do some error handling...
                }
                */
            }
            break;
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            Set<Locale> langs = tts.getAvailableLanguages();
            List<TextToSpeech.EngineInfo> engines = tts.getEngines();

            //Locale lang = Locale.SIMPLIFIED_CHINESE;
            Locale lang = Locale.US;

            if (langs.contains(lang)) {
                int result = tts.setLanguage(lang);
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "Language not supported");
                }
            }
        } else {
            Log.e("TTS", "Initialization failed");
        }
    }
}
