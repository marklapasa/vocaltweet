package net.lapasa.vocaltweet.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TweetView;

import net.lapasa.vocaltweet.R;

import java.util.Observable;



/**
 * A simple {@link Fragment} subclass.
 */
public class TweetDetailsFragment extends BaseFragment
{


    private Tweet tweet;
    private TweetView tweetDetailsView;

    public TweetDetailsFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the recent_searches_empty for this fragment
        View v = inflater.inflate(R.layout.fragment_tweet_details, container, false);
        this.tweetDetailsView = (TweetView) v.findViewById(R.id.tweetDetailView);
        return v;
    }


    @Override
    public void update(Observable observable, Object data)
    {
        this.tweet = model.getSelectedTweet();
        tweetDetailsView.setTweet(tweet);

        getActivity().getActionBar().setTitle(tweet.user.name);

/*        if (data == TweetUtteranceProgressListener.UTTERANCE_STARTED)
        {

        }*/
    }

}
