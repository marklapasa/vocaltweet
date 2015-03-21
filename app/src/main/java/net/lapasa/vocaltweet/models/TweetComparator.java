package net.lapasa.vocaltweet.models;

import com.twitter.sdk.android.core.models.Tweet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Locale;

/**
 * Created by mlapasa on 3/19/2015.
 */
public class TweetComparator implements Comparator<Tweet>
{

    @Override
    public int compare(Tweet lhs, Tweet rhs)
    {
        if (getDateFromString(lhs.createdAt).getTimeInMillis() < getDateFromString(rhs.createdAt).getTimeInMillis())
        {
            return 1;
        }
        else
        {
            return -1;
        }
    }

    public static Calendar getDateFromString(String dateStr)
    {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);
        try
        {
            cal.setTime(sdf.parse(dateStr));
            return cal;
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        return null;
    }


}
