package net.lapasa.vocaltweet.commands;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;

import com.twitter.sdk.android.core.models.Tweet;

import java.util.HashMap;

import net.lapasa.vocaltweet.ITweetModelActivity;
import net.lapasa.vocaltweet.fragments.TweetUtteranceProgressListener;

/**
 * Converts a tweet's text to speech
 */
public class PlayTweetCommand
{
    private static final String MSG_ID = "msgId";
    private Tweet tweet = null;
    private TweetUtteranceProgressListener listener = null;
    private TextToSpeech tts = null;
    private String audibleText;
    private Context context;

    /**
     * Use this for API > 18
     *
     * @param context
     * @param tweet
     * @param listener
     */
    public PlayTweetCommand(Context context, Tweet tweet, TweetUtteranceProgressListener listener)
    {
        this.context = context;
        this.tts = ((ITweetModelActivity) context).getTextToSpeech();
        this.tweet = tweet;
        this.listener = listener;
    }

    @SuppressLint("NewApi")
    public void execute()
    {
        if(tweet != null)
        {
            composeAudibleText();

            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
            {
                tts.setOnUtteranceCompletedListener(listener);
                tts.speak(audibleText, TextToSpeech.QUEUE_ADD, getTTSParamsMap());

            } else
            {
                tts.setOnUtteranceProgressListener(listener);
                tts.speak(audibleText, TextToSpeech.QUEUE_ADD, getTTSParamsBundle(), MSG_ID);
            }
        }
        else
        {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
            {
                tts.speak("", TextToSpeech.QUEUE_FLUSH, getTTSParamsMap());

            } else
            {
                tts.playSilentUtterance(1,TextToSpeech.QUEUE_FLUSH, MSG_ID);
            }
        }
    }

    private HashMap<String, String> getTTSParamsMap()
    {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, MSG_ID);
        return map;
    }

    private Bundle getTTSParamsBundle()
    {
//        Bundle b = new Bundle();
//        b.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, MSG_ID);
//        return b;
        return null;
    }

    private void composeAudibleText()
    {
        audibleText = tweet.user.name + " says ... " + tweet.text;
        audibleText = audibleText.replaceAll("RT\\s?", "Rhee Tweeets");
        audibleText = audibleText.replaceAll("((http[s]?|ftp):\\/)?\\/?([^:\\/\\s]+)((\\/\\w+)*\\/)([\\w\\-\\.]+[^#?\\s]+)(.*)?(#[\\w\\-]+)?", "");
        audibleText += "...";
    }
}
