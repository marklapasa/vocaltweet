package net.lapasa.vocaltweet.fragments;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.UtteranceProgressListener;
import android.widget.Toast;

import net.lapasa.vocaltweet.commands.PlayTweetCommand;
import net.lapasa.vocaltweet.models.TweetsModel;

/**
 * Created by mlapasa on 2/12/2015.
 */
public class TweetUtteranceProgressListener extends UtteranceProgressListener // implements TextToSpeech.OnUtteranceCompletedListener
{
    public static int SILENCE = -100;
    public static int UTTERANCE_STARTED = 10;
    public static int UTTERANCE_COMPLETE = 11;

    private Context context = null;
    private int silenceGap = 0;

    public TweetsModel model;
    private Handler handler;

    public TweetUtteranceProgressListener(Context context, TweetsModel model, int silenceGap)
    {
        this.context = context;
        this.model = model;
        this.silenceGap = silenceGap;
    }

    @Override
    public void onStart(String utteranceId)
    {
        // Maybe a good time to modify the TweetDetailsView's tweet view
        if (!utteranceId.equals(PlayTweetCommand.SILENCE_ID))
        {
            model.notifyObservers(UTTERANCE_STARTED);
        }
    }


    @Override
    public void onDone(String utteranceId)
    {
        if (model.hasNextTweet() && model.isPlaying)
        {
            model.notifyObservers(SILENCE);
            handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    new PlayTweetCommand(context, model.getNextTweet(), TweetUtteranceProgressListener.this).execute();

                }
            },silenceGap);


        }
        else if (!model.hasNextTweet())
        {
            model.isPlaying = false;
            handler.removeCallbacksAndMessages(null);
        }
        model.notifyObservers(UTTERANCE_COMPLETE);
    }


    @Override
    public void onError(String utteranceId)
    {
        Toast.makeText(context, "Failed to speak Tweet", Toast.LENGTH_LONG).show();
    }

    public void refreshControls()
    {
        model.notifyObservers(UTTERANCE_COMPLETE);
    }

    /**
     * Support for devices that are on API 18 or less
     * @param utteranceId

    @Override
    public void onUtteranceCompleted(String utteranceId)
    {
        onDone(utteranceId);
    }
     */
}
