package net.lapasa.vocaltweet.fragments;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.widget.Toast;

import net.lapasa.vocaltweet.commands.PlayTweetCommand;
import net.lapasa.vocaltweet.models.TweetsModel;

/**
 * Created by mlapasa on 2/12/2015.
 */
public class TweetUtteranceProgressListener extends UtteranceProgressListener implements TextToSpeech.OnUtteranceCompletedListener
{
    public static int UTTERANCE_STARTED = 10;
    public static int UTTERANCE_COMPLETE = 11;

    private final Context context;
    public TweetsModel model;

    public TweetUtteranceProgressListener(Context context, TweetsModel model)
    {
        this.context = context;
        this.model = model;
    }

    @Override
    public void onStart(String utteranceId)
    {
        // Maybe a good time to modify the TweetDetailsView's tweet view
        model.notifyObservers(UTTERANCE_STARTED);
    }

    @Override
    public void onDone(String utteranceId)
    {
        if (model.hasNextTweet() && model.isPlaying)
        {
            new PlayTweetCommand(context, model.getNextTweet(), this).execute();
        }
        else if (!model.hasNextTweet())
        {
            model.isPlaying = false;
        }

        model.notifyObservers(UTTERANCE_COMPLETE);
    }

    @Override
    public void onError(String utteranceId)
    {
        Toast.makeText(context, "Failed to speak Tweet", Toast.LENGTH_LONG).show();
    }

    /**
     * This is for suppose for devices that are on API 18 or less
     * @param utteranceId
     */
    @Override
    public void onUtteranceCompleted(String utteranceId)
    {
        onDone(utteranceId);
    }
}
