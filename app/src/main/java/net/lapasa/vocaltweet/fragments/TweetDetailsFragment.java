package net.lapasa.vocaltweet.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.CompactTweetView;

import java.util.Observable;



/**
 * A simple {@link Fragment} subclass.
 */
public class TweetDetailsFragment extends BaseFragment
{


    private Tweet tweet;
    private CompactTweetView tweetDetailsView;

    public TweetDetailsFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        this.tweetDetailsView = new CompactTweetView(inflater.getContext(), model.getSelectedTweet());
        return tweetDetailsView;
    }


    @Override
    public void update(Observable observable, Object data)
    {
        this.tweet = model.getSelectedTweet();

        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                tweetDetailsView.setTweet(tweet);
                getActivity().getActionBar().setTitle(tweet.user.name);
            }
        });

    }

}
