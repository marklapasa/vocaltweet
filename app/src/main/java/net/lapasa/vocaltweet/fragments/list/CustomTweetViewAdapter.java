package net.lapasa.vocaltweet.fragments.list;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.twitter.sdk.android.tweetui.CompactTweetView;
import com.twitter.sdk.android.tweetui.TweetViewAdapter;

/**
 * Created by mlapasa on 3/5/2015.
 */
public class CustomTweetViewAdapter<T> extends TweetViewAdapter
{
    public CustomTweetViewAdapter(Context context)
    {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        CompactTweetView vg = (CompactTweetView) super.getView(position, convertView, parent);
        vg.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        vg.setClickable(false);
        return vg;
    }
}
