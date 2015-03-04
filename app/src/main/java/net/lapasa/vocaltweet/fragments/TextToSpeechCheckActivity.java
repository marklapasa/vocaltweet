package net.lapasa.vocaltweet.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;

import net.lapasa.vocaltweet.MainActivity;
import net.lapasa.vocaltweet.R;

public class TextToSpeechCheckActivity extends Activity
{

    private static final int TTS_CHECK_CODE = 2015;
    private static final String TTS_ALREADY_INSTALLED = "TTS_ALREADY_INSTALLED";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        String ttsInstalled = sharedPreferences.getString(TTS_ALREADY_INSTALLED, "n");
        boolean isTTSInstalled = sharedPreferences.getBoolean(TTS_ALREADY_INSTALLED, false);

        if (ttsInstalled.equals("n"))
        {
            setContentView(R.layout.activity_text_to_speech_check);

            Intent checkIntent = new Intent();
            checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
            startActivityForResult(checkIntent, TTS_CHECK_CODE);
        }
        else
        {
            launchMainApp();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == TTS_CHECK_CODE)
        {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS)
            {
                // Remember that TTS is already installed
                sharedPreferences.edit().putString(TTS_ALREADY_INSTALLED, "y");
                sharedPreferences.edit().apply();
                sharedPreferences.edit().commit();

                // Proceed to main application since TTS is already installed
                launchMainApp();
            }
            else
            {
                // Prompt the user to install TTS
                Intent installIntent = new Intent();
                installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            }
        }
    }

    private void launchMainApp()
    {
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
