package net.lapasa.vocaltweet.fragments;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import net.lapasa.vocaltweet.R;
import net.lapasa.vocaltweet.commands.PlayTweetCommand;
import net.lapasa.vocaltweet.models.TweetsModel;

import java.util.Observable;


/**
 * Allows the user to play the current, next or previous tweet.
 */
public class PlaybackControlsFragment extends BaseFragment implements View.OnClickListener
{
    private TextToSpeech.OnUtteranceCompletedListener supportListener;

    private ImageButton nextBtn;
    private ImageButton prevBtn;
    private CheckBox playPauseBtn;
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

        TweetUtteranceProgressListener listener = new TweetUtteranceProgressListener(getActivity(), model);
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
                c = new PlayTweetCommand(getActivity(), null, null);
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
        if (data == TweetsModel.NO_TWEETS_AVAILABLE)
        {
            getView().setVisibility(View.GONE);
            getActivity().getActionBar().hide();
        }
        else
        {
            getView().setVisibility(View.VISIBLE);
            getActivity().getActionBar().show();
        }

        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                nextBtn.setEnabled(model.hasNextTweet());
                prevBtn.setEnabled(model.hasPrevTweet());

                progressBar.setMax(model.getTweets().size());
                progressBar.setProgress(model.getSelectedIndex() + 1);
            }
        });
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

//        getView().setVisibility(View.GONE);

        this.nextBtn = (ImageButton) getView().findViewById(R.id.nextTweetBtn);
        this.prevBtn = (ImageButton) getView().findViewById(R.id.prevTweetBtn);
        this.playPauseBtn = (CheckBox) getView().findViewById(R.id.playPauseTweetBtn);
        this.progressBar = (ProgressBar) getView().findViewById(R.id.progressBar);

        nextBtn.setOnClickListener(this);
        prevBtn.setOnClickListener(this);
        playPauseBtn.setOnClickListener(this);


/*
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR2)
        {
            supportListener = new TweetUtteranceProgressListener(getActivity(), model);
        }
        else
        {
            listener = new TweetUtteranceProgressListener(getActivity(), model);
        }
*/
    }
}
