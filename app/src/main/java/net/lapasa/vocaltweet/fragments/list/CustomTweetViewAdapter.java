package net.lapasa.vocaltweet.fragments.list;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.models.MediaEntity;
import com.twitter.sdk.android.core.models.Tweet;

import net.lapasa.vocaltweet.R;
import net.lapasa.vocaltweet.models.TweetComparator;

import java.util.Calendar;
import java.util.List;

public class CustomTweetViewAdapter extends BaseAdapter
{
    private static final String TAG = CustomTweetViewAdapter.class.getName();
    private final Context context;
    private final LayoutInflater inflator;

    public List<Tweet> getTweets()
    {
        return tweets;
    }

    public void setTweets(List<Tweet> tweets)
    {
        this.tweets = tweets;
    }

    private List<Tweet> tweets;

    /**
     * Constructor
     *
     * @param context
     */
    public CustomTweetViewAdapter(Context context)
    {
        this.context = context;
        this.inflator = ((Activity) context).getLayoutInflater();
    }

    @Override
    public int getCount()
    {
        return (tweets != null) ? tweets.size() : -1;
    }

    @Override
    public Tweet getItem(int position)
    {
        if (tweets != null)
        {
            return tweets.get(position);
        } else
        {
            return null;
        }
    }

    @Override
    public long getItemId(int position)
    {
        if (tweets != null)
        {
            Tweet t = (Tweet) tweets.get(position);
            return t.id;
        } else
        {
            return -1;
        }

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Tweet model = tweets.get(position);

        View view = convertView;

        if (view == null)
        {
            if (position % 2 == 0)
            {
                view = inflator.inflate(R.layout.tweet_list_item, null);
            } else
            {
                view = inflator.inflate(R.layout.tweet_list_item_alt, null);
            }
        }
/*        else
        {
            int oldPosMod = ((int) view.getTag()) % 2;
            int newPosMod = position % 2;

            if (oldPosMod != newPosMod)
            {
                if (newPosMod == 0)
                {
                    view = inflator.inflate(R.layout.tweet_list_item, null);
                }
                else
                {
                    view = inflator.inflate(R.layout.tweet_list_item_alt, null);
                }
            }
        }*/

        view.setTag(position);

        // Image

        ImageView tweetMsgImg = (ImageView) view.findViewById(R.id.tweetMsgImg);
        tweetMsgImg.setVisibility(View.GONE);
        tweetMsgImg.setImageResource(0);


        List<MediaEntity> media = model.entities.media;
        if (media != null)
        {
            for (int i = 0; i < media.size(); i++)
            {
                MediaEntity entity = media.get(i);
                Log.i(TAG, "entity.type = " + entity.type);

                if (entity.type.equals("photo"))
                {
                    tweetMsgImg.setVisibility(View.VISIBLE);
                    Picasso.with(context).load(entity.mediaUrl).into(tweetMsgImg);
                }
            }
        }

        // Tweets User Profile Image
        ImageView imageView = (ImageView) view.findViewById(R.id.userProfilePic);
        String profileImageUrl = model.user.profileImageUrl.replaceAll("normal", "bigger");
        Picasso.with(context).load(profileImageUrl).placeholder(R.drawable.ic_default_profile_pic).error(R.drawable.ic_loading_error).into(imageView);

        // Tweet message body
        TextView msgText = (TextView) view.findViewById(R.id.msgText);
        msgText.setText(model.text);

        // Tweet footer
        Calendar createdAt = TweetComparator.getDateFromString(model.createdAt);
        String tweetAge = (String) DateUtils.getRelativeDateTimeString(context, createdAt.getTimeInMillis(), DateUtils.MINUTE_IN_MILLIS, DateUtils.DAY_IN_MILLIS, DateUtils.FORMAT_ABBREV_TIME);
        TextView footer = (TextView) view.findViewById(R.id.footerText);
        footer.setText(model.user.name + " • " + tweetAge);

        return view;
    }
}
