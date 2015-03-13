package net.lapasa.vocaltweet.commands;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.twitter.sdk.android.core.models.Tweet;

import net.lapasa.vocaltweet.ITweetModelActivity;
import net.lapasa.vocaltweet.fragments.TweetUtteranceProgressListener;

import java.util.HashMap;

/**
 * Converts a tweet's text to speech
 */
public class PlayTweetCommand
{
    private static final String TAG = PlayTweetCommand.class.getName();
    public static final String SILENCE_ID = "_SILENCE_ID";
    private boolean isStopTTS;
    private Tweet tweet = null;
    private TweetUtteranceProgressListener listener = null;
    private TextToSpeech tts = null;
    private String audibleText;
    private Context context;

    /**
     * When both tweet and listener = null, it will stop the playing speech and flush the queue
     *
     * @param context
     * @param tweet
     * @param listener
     */
    public PlayTweetCommand(Context context, Tweet tweet, TweetUtteranceProgressListener listener)
    {
        this.context = context;
        this.tts = ((ITweetModelActivity) context).getTextToSpeech();

        if (tweet == null && listener == null)
        {
            isStopTTS = true;
        }
        else
        {
            this.tweet = tweet;
            this.listener = listener;
        }
    }

    @SuppressLint("NewApi")
    public void execute()
    {
        if (isStopTTS)
        {
            tts.stop();
            Log.i(TAG, "Stopping audio, queue is flushed");
        }
        else if(tweet != null)
        {
            composeAudibleText();


            tts.setOnUtteranceProgressListener(listener);
/*
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
            {
                tts.setOnUtteranceProgressListener(listener);
            }
            else
            {
                tts.setOnUtteranceCompletedListener(listener);
            }
*/

            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP)
            {
                tts.speak(audibleText, TextToSpeech.QUEUE_ADD, getTTSParamsMap(tweet.idStr));
                Log.i(TAG, tweet.user.name);
            } else
            {
                tts.speak(audibleText, TextToSpeech.QUEUE_ADD, getTTSParamsBundle(), tweet.idStr);
            }
        }
        else
        {
            // Force refresh of playback controls
            listener.onDone(null);
        }


    }

    public static HashMap<String, String> getTTSParamsMap(String idStr)
    {
        String audioStream = String.valueOf(AudioManager.STREAM_MUSIC);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, idStr);

        map.put(TextToSpeech.Engine.KEY_PARAM_STREAM, audioStream);
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
        audibleText = audibleText.replace("<3", "love");
        audibleText += "...";
    }
}
