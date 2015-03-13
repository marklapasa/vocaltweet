package net.lapasa.vocaltweet.fragments.playback;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;

import net.lapasa.vocaltweet.R;
import net.lapasa.vocaltweet.commands.PlayTweetCommand;
import net.lapasa.vocaltweet.fragments.TweetUtteranceProgressListener;
import net.lapasa.vocaltweet.models.TweetsModel;

public class PlayPauseController implements View.OnClickListener
{
    private final View view;
    private final View playIcon;
    private final View pauseIcon;
    private final Context context;
    private final TweetsModel model;
    private boolean _isPlaying;
    private View nextBtn;
    private View prevBtn;
    private IProgressBarClient client;
    public int silenceGap;

    public PlayPauseController(Context context, View view, TweetsModel model, ImageButton nextBtn, ImageButton prevBtn, IProgressBarClient client)
    {
        this.context = context;
        this.view = view;
        this.model = model;
        this.nextBtn = nextBtn;
        this.prevBtn = prevBtn;
        this.client = client;

        this.view.setOnClickListener(this);
        playIcon = view.findViewById(R.id.playIcon);
        pauseIcon = view.findViewById(R.id.pauseIcon);


        // Default position is paused
        stop();
    }


    public View getView()
    {
        return view;
    }

    public boolean isPlaying()
    {
        return _isPlaying;
    }

    public void play()
    {
        this.pauseIcon.setVisibility(View.VISIBLE);
        this.playIcon.setVisibility(View.GONE);
        _isPlaying = true;
    }

    public void stop()
    {
        this.pauseIcon.setVisibility(View.GONE);
        this.playIcon.setVisibility(View.VISIBLE);

        _isPlaying = false;
    }

    /**
     * Assmption: Next or previous button should not be accessible if there is no tweet to play back
     *
     * @param view
     */
    public void onClick(View view)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        silenceGap = Integer.parseInt(sharedPreferences.getString(context.getString(R.string.pref_audio_gap), "0"));


        TweetUtteranceProgressListener listener = new TweetUtteranceProgressListener(context, model, silenceGap);

        if (view.equals(nextBtn))
        {
            new PlayTweetCommand(context, null, null).execute();
            new PlayTweetCommand(context, model.getNextTweet(), listener).execute();
        }
        else if (view.equals(prevBtn))
        {
            new PlayTweetCommand(context, null, null).execute();
            new PlayTweetCommand(context, model.getPrevTweet(), listener).execute();
        }
        else if (view.equals(getView()))
        {
            if (model.isPlaying)
            {
                // Stop whatever is playing and display the Pause icon
                stop();
                model.isPlaying = false;
                new PlayTweetCommand(context, null, null).execute();
            }
            else
            {
                // Begin to speak the text of the selected tweet, display the Play icon
                play();
                model.isPlaying = true;
                if (model.getSelectedIndex() == -1)
                {
                    client.updateProgressBar();
                }

                new PlayTweetCommand(context, model.getSelectedTweet(), listener).execute();
            }
        }
    }
}
