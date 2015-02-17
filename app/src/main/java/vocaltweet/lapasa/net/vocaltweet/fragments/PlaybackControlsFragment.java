package vocaltweet.lapasa.net.vocaltweet.fragments;

import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ToggleButton;

import java.util.Observable;

import vocaltweet.lapasa.net.vocaltweet.R;
import vocaltweet.lapasa.net.vocaltweet.commands.PlayTweetCommand;


/**
 * Allows the user to play the current, next or previous tweet.
 */
public class PlaybackControlsFragment extends BaseFragment implements View.OnClickListener
{
    private TweetUtteranceProgressListener listener;
    private TextToSpeech.OnUtteranceCompletedListener supportListener;

    private Button nextBtn;
    private Button prevBtn;
    private ToggleButton playPauseBtn;
    private ProgressBar progressBar;

    /**
     * Constructor
     */
    public PlaybackControlsFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_playback_controls, container, false);
    }


    @Override
    public void onClick(View v)
    {
        PlayTweetCommand c = null;

        if (v.equals(nextBtn))
        {
            c = new PlayTweetCommand(getActivity(), model.getNextTweet(), listener);
        }
        else if (v.equals(prevBtn))
        {
            c = new PlayTweetCommand(getActivity(), model.getPrevTweet(), listener);
        }
        else if (v.equals(playPauseBtn))
        {
            if (!playPauseBtn.isChecked())
            {
                model.isPlaying = false;
                c = new PlayTweetCommand(getActivity(), null, listener);
            }
            else
            {
                model.isPlaying = true;
                c = new PlayTweetCommand(getActivity(), model.getSelectedTweet(), listener);

            }
        }

        c.execute();

    }


    @Override
    public void update(Observable observable, Object data)
    {
        nextBtn.setEnabled(model.hasNextTweet());
        prevBtn.setEnabled(model.hasPrevTweet());


        progressBar.setMax(model.getTweets().size());
        progressBar.setProgress(model.getSelectedIndex());

        if (data == TweetUtteranceProgressListener.UTTERANCE_COMPLETE)
        {
            if(model.hasNextTweet())
            {
                new PlayTweetCommand(getActivity(), model.getNextTweet(), listener).execute();
            }
            else
            {
                new PlayTweetCommand(getActivity(), null, listener).execute();
            }
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        this.nextBtn = (Button) getView().findViewById(R.id.nextTweetBtn);
        this.prevBtn = (Button) getView().findViewById(R.id.prevTweetBtn);
        this.playPauseBtn = (ToggleButton) getView().findViewById(R.id.playPauseTweetBtn);
        this.progressBar = (ProgressBar) getView().findViewById(R.id.progressBar);

        nextBtn.setOnClickListener(this);
        prevBtn.setOnClickListener(this);
        playPauseBtn.setOnClickListener(this);

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR2)
        {
            supportListener = new TweetUtteranceProgressListener(getActivity(), model);
        }
        else
        {
            listener = new TweetUtteranceProgressListener(getActivity(), model);
        }
    }
}
