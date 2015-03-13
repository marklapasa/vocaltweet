package net.lapasa.vocaltweet.fragments.playback;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.pascalwelsch.holocircularprogressbar.HoloCircularProgressBar;

import net.lapasa.vocaltweet.R;
import net.lapasa.vocaltweet.fragments.BaseFragment;
import net.lapasa.vocaltweet.fragments.TweetUtteranceProgressListener;
import net.lapasa.vocaltweet.models.TweetsModel;

import java.util.Observable;


/**
 * Allows the user to play the current, next or previous tweet.
 */
public class PlaybackControlsFragment extends BaseFragment implements IProgressBarClient
{
    private static final int DEFAULT_PROGRESS_DURATION = 1000;
    private ImageButton nextBtn;
    private ImageButton prevBtn;
    private PlayPauseController playPauseController;
    private HoloCircularProgressBar circularProgressBar;
    private HoloCircularProgressBar silenceCircularProgressBar;
    private ObjectAnimator objAnimatorProgress;
    private ObjectAnimator objAnimatorSilenece;


    /**
     * Constructor
     */
    public PlaybackControlsFragment()
    {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_playback_controls, container, false);
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

        if (data == TweetUtteranceProgressListener.UTTERANCE_STARTED)
        {
            updateProgressBar();
        }
        else if (data == TweetUtteranceProgressListener.UTTERANCE_COMPLETE)
        {
            updateSilenceProgressBar();
        }
    }

    public void updateProgressBar()
    {
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                animateCircularProgressBar(0, silenceCircularProgressBar, 1000);

                nextBtn.setEnabled(model.hasNextTweet());
                prevBtn.setEnabled(model.hasPrevTweet());

                float selectedIndex = model.getSelectedIndex() + 1;
                float size = (float) model.getTweets().size();

                if (selectedIndex == size)
                {
                    animateCircularProgressBar(0, circularProgressBar, DEFAULT_PROGRESS_DURATION);
                }
                else if (model.getTweets().size() > 0 && selectedIndex > 0)
                {
                    float value = (float) (selectedIndex / size);
                    animateCircularProgressBar(value, circularProgressBar, DEFAULT_PROGRESS_DURATION);
                }
                else if (selectedIndex == 0)
                {
                    // When tweets are loaded and there is no selected tweet yet, make progress bar appear full
                    animateCircularProgressBar(1, circularProgressBar, DEFAULT_PROGRESS_DURATION);
                }
            }
        });
    }

    @Override
    public void updateSilenceProgressBar()
    {
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                animateCircularProgressBar(1, silenceCircularProgressBar, playPauseController.silenceGap);
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
        playPauseController = new PlayPauseController(getActivity(), getView().findViewById(R.id.playPauseTweetBtn), model, nextBtn, prevBtn, this);
        this.circularProgressBar = (HoloCircularProgressBar) getView().findViewById(R.id.holoCircularProgressBar);
        this.silenceCircularProgressBar = (HoloCircularProgressBar) getView().findViewById(R.id.holoCircularProgressBarSilence);

        nextBtn.setOnClickListener(playPauseController);
        prevBtn.setOnClickListener(playPauseController);
    }


    private void animateCircularProgressBar(final float progress, final HoloCircularProgressBar targetProgressBar, final int duration)
    {
        ObjectAnimator circularProgressBarAnimator = ObjectAnimator.ofFloat(targetProgressBar, "progress", progress);
        circularProgressBarAnimator.setDuration(duration);

        if (targetProgressBar == circularProgressBar)
        {
            circularProgressBarAnimator.setStartDelay(250);
        }

        circularProgressBarAnimator.addListener(new Animator.AnimatorListener()
        {

            @Override
            public void onAnimationCancel(final Animator animation){}

            @Override
            public void onAnimationEnd(final Animator animation)
            {
                targetProgressBar.setProgress(progress);
            }

            @Override
            public void onAnimationRepeat(final Animator animation)
            {
            }

            @Override
            public void onAnimationStart(final Animator animation)
            {
            }
        });

        circularProgressBarAnimator.reverse();
        circularProgressBarAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {

            @Override
            public void onAnimationUpdate(final ValueAnimator animation)
            {
                targetProgressBar.setProgress((Float) animation.getAnimatedValue());
            }
        });
        circularProgressBarAnimator.start();
    }
}