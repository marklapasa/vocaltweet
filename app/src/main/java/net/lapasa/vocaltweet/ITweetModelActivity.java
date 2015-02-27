package net.lapasa.vocaltweet;

import android.app.Fragment;
import android.speech.tts.TextToSpeech;

import net.lapasa.vocaltweet.models.TweetsModel;

/**
 * Created by mlapasa on 2/12/2015.
 */
public interface ITweetModelActivity
{
    TweetsModel getModel();

    TextToSpeech getTextToSpeech();

    void loadFragment(Fragment fragment);
}
