package net.lapasa.vocaltweet.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import net.lapasa.vocaltweet.R;
import net.lapasa.vocaltweet.models.entities.SearchTermRecord;
import net.lapasa.vocaltweet.models.entities.TweetRecord;
import net.lapasa.vocaltweet.models.entities.UserRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class TweetsModel extends Observable
{
    public static final int NEW_TWEETS_RECEIVED = 0;
    public static final int NO_TWEETS_AVAILABLE = 1;

    private static int COUNT = 3;
    private static final String TAG = TweetsModel.class.getName();
    StatusesService statusesService;

    final private List<Tweet> tweets = new ArrayList<Tweet>();
    private int selectedIndex = -1;
    public boolean isPlaying;
    private static TweetsModel _instance;
    private SharedPreferences sharedPreferences;
    private SearchTermRecord activeSearchTerm = null;

    private TweetsModel()
    {
        notifyObservers(NO_TWEETS_AVAILABLE);
    }

    public static TweetsModel getInstance()
    {
        if (_instance == null)
        {
            _instance = new TweetsModel();
        }
        return _instance;
    }


    public void setTweets(List<Tweet> tweets)
    {
        this.tweets.clear();

        for (Tweet t : tweets)
        {
            this.tweets.add(t);
        }

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


    /**
     * This will only load if there is a successuful login into Twitter
     *
     * @param record
     * @param context
     */
    public void loadTweets(SearchTermRecord record, Context context)
    {
        activeSearchTerm = record;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        setTweetMax(context);
        trimOldTweets(context);

        if (record != null)
        {
            record.getTerm();
            // Get tweets that correspond to search term
        }
        else
        {
            goOfflineMode();
/*
            try
            {
                TwitterApiClient apiClient = TwitterCore.getInstance().getApiClient();

                statusesService = apiClient.getStatusesService();

                statusesService.homeTimeline(COUNT + 1, null, null, null, null, null, null, new Callback<List<Tweet>>()
                {
                    @Override
                    public void success(Result<List<Tweet>> listResult)
                    {
                        List<Tweet> tweets = listResult.data;
                        setTweets(tweets);

                        // Persist the tweets for offline
                        persistTweets(tweets);
                    }

                    @Override
                    public void failure(TwitterException e)
                    {
                        goOfflineMode();
                    }
                });

            } catch (IllegalStateException e) // java.lang.IllegalStateException: Must have valid session. Did you authenticate with Twitter?
            {
                goOfflineMode();
            }
*/
        }


    }

    private void trimOldTweets(Context context)
    {
        int age = Integer.parseInt(sharedPreferences.getString(context.getString(R.string.pref_tweet_cache_age), "-1"));
        TweetRecord.deleteOldRecords(age);

    }

    private void setTweetMax(Context context)
    {
        COUNT = Integer.parseInt(sharedPreferences.getString(context.getString(R.string.pref_define_max_tweet), "25"));
    }

    private void goOfflineMode()
    {
        Log.e(TAG, "Could not load tweets from remote server, will load locally persisted tweets");

        List<Tweet> tweets = loadTweetsFromDB();
        setTweets(tweets);
    }

    private List<Tweet> loadTweetsFromDB()
    {
        return TweetRecord.getAllTweets();
    }

    private void persistTweets(List<Tweet> tweets)
    {
        // Before storing the tweet, make sure that it does not already exist in the DB

        // First Pass: Check each ID
        // Second Pass: Query by list of ID's

        // First Pass: Check each ID
        for (Tweet tweet : tweets)
        {
            String remoteIdStr = tweet.idStr;

            List<TweetRecord> results = TweetRecord.findWithQuery(TweetRecord.class, "Select * from TWEET_RECORD where ID_STR = " + remoteIdStr);

            if (!results.isEmpty())
            {
                // If the tweet already exists, don't attenpt to persist it again
                continue;
            }

            TweetRecord tweetRecord = new TweetRecord(tweet);
            tweetRecord.save();
        }
    }

    public int getSelectedIndex()
    {
        return selectedIndex;
    }

    public Tweet getSelectedTweet()
    {
        if (selectedIndex >= 0)
        {
            return tweets.get(selectedIndex);
        }
        else
        {
            return null;
        }
    }

    @Override
    public void notifyObservers(Object data)
    {
        setChanged();
        super.notifyObservers(data);
    }

    public void clearCache(Context context)
    {
        TweetRecord.deleteAll(TweetRecord.class);
        UserRecord.deleteAll(UserRecord.class);
        loadTweets(null, context);
    }

    public SearchTermRecord getActiveSearchTerm()
    {
        return activeSearchTerm;
    }
}
