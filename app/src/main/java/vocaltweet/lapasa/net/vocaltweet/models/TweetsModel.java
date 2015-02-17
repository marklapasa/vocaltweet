package vocaltweet.lapasa.net.vocaltweet.models;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created by mlapasa on 2/12/2015.
 */
public class TweetsModel extends Observable
{
    public static final int NEW_TWEETS_RECEIVED = 0;
    public static final int NO_TWEETS_AVAILABLE = 1;

    private static final Integer COUNT = 20;
    StatusesService statusesService;

    final private List<Tweet> tweets = new ArrayList<Tweet>();
    private int selectedIndex = -1;
    public boolean isPlaying;

    public void setTweets(List<Tweet> tweets)
    {
        this.tweets.clear();

        for (Tweet t : tweets)
        {
            this.tweets.add(t);
        }

        setChanged();
        if (tweets.size() > 0)
        {
            notifyObservers(NEW_TWEETS_RECEIVED);
        }
        else if (tweets.size() == 0)
        {
            notifyObservers(NO_TWEETS_AVAILABLE);
        }

        selectedIndex = 0;
    }

    public List<Tweet> getTweets()
    {
        return tweets;
    }

    public Tweet getNextTweet()
    {
        if (!tweets.isEmpty() && hasNextTweet())
        {
            return tweets.get(++selectedIndex);
        }
        return null;
    }

    public Tweet getPrevTweet()
    {
        if (!tweets.isEmpty() && hasPrevTweet())
        {
            return tweets.get(--selectedIndex);
        }
        return null;
    }




    public boolean hasNextTweet()
    {
        return (selectedIndex + 1 < tweets.size());
    }

    public boolean hasPrevTweet()
    {
        return (selectedIndex - 1 >= 0);
    }

    public void loadTweets()
    {
        TwitterApiClient apiClient = TwitterCore.getInstance().getApiClient();
        statusesService = apiClient.getStatusesService();

        statusesService.homeTimeline(COUNT, null, null, null,null,null,null, new Callback<List<Tweet>>()
        {
            @Override
            public void success(Result<List<Tweet>> listResult)
            {
                List<Tweet> tweets = listResult.data;
                setTweets(tweets);
            }

            @Override
            public void failure(TwitterException e)
            {
                e.printStackTrace();
            }
        });

    }

    public int getSelectedIndex()
    {
        return selectedIndex;
    }

    public Tweet getSelectedTweet()
    {
        return tweets.get(selectedIndex);
    }
}
