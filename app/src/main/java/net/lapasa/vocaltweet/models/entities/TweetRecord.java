package net.lapasa.vocaltweet.models.entities;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import com.twitter.sdk.android.core.models.Coordinates;
import com.twitter.sdk.android.core.models.MediaEntity;
import com.twitter.sdk.android.core.models.Place;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.TweetEntities;
import com.twitter.sdk.android.core.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TweetRecord extends SugarRecord<TweetRecord>
{
    private static final String TAG = TweetRecord.class.getName();

    private String withheldScope;

    private boolean withheldCopyright;

    private boolean truncated;
    private String text;
    private String source;

    private boolean retweeted;
    private int retweetCount;

    private boolean possiblySensitive;

    private String lang;
    private String inReplyToUserIdStr;
    private long inReplyToUserId;
    private String inReplyToStatusIdStr;
    private long inReplyToStatusId;
    private String inReplyToScreenName;
    private String idStr;
    private long tweetId;
    private String filterLevel;
    private boolean favorited;
    String createdAt;
    String tweetIdStr;

    /////////////////
    // Coordinates //
    /////////////////
    private Double coordinate_Latitude;
    private Double coordinate_Longitude;
    private String coordinates_Type;


    int favoriteCount;

    /**
     * A single tweet can be a host to many urls, media, mentions and hastags
     */
    @Ignore
    TweetEntities entities;

    @Ignore
    Object currentUserRetweet;

    ///////////
    // Place //
    ///////////
    @Ignore
    private Place place;

    @Ignore
    private Place.BoundingBox place_boundingbox;

    private String place_country;
    private String place_countryCode;
    private String place_fullName;
    private String place_id;
    private String place_name;
    private String place_placeType;
    private String place_url;
    private Map<String, String> place_attributes;

    @Ignore
    private Object scopes;

    @Ignore
    private Tweet retweetedStatus;

    /**
     * See UserRecord which wraps this instance
     */
    @Ignore
    private User user;

    UserRecord userRecord;

    @Ignore
    private List<String> withheldInCountries;

    // TODO: Create relationship between SearchTerm and Tweets (1 to many)
    //    SearchTermRecord searchTerm;

    public TweetRecord()
    {
        // Empty constructor required
    }

    /**
     * Constructor
     */
    public TweetRecord(Tweet tweet)
    {

        // Extrapolate the values out of each field on the tweet
//        this.cord = tweet.coordinates;
        this.createdAt = tweet.createdAt;
        this.currentUserRetweet = tweet.currentUserRetweet;
//        this.entities = tweet.entities;

        this.favorited = tweet.favorited;

        this.filterLevel = tweet.filterLevel;
        this.tweetId = tweet.id;
        this.idStr = tweet.idStr;
        this.inReplyToScreenName = tweet.inReplyToScreenName;
        this.inReplyToStatusId = tweet.inReplyToStatusId;
        this.inReplyToStatusIdStr = tweet.inReplyToStatusIdStr;

        this.inReplyToUserId = tweet.inReplyToUserId;
        this.inReplyToUserIdStr = tweet.inReplyToUserIdStr;
        this.lang = tweet.lang;
//        this.place = tweet.place;
        this.possiblySensitive = tweet.possiblySensitive;
        this.scopes = tweet.scopes;
        this.retweetCount = tweet.retweetCount;

        this.retweeted = tweet.retweeted;
        this.retweetedStatus = tweet.retweetedStatus;
        this.source = tweet.source;
        this.text = tweet.text;
        this.truncated = tweet.truncated;
//        this.user = tweet.user;
        this.withheldCopyright = tweet.withheldCopyright;

        this.withheldInCountries = tweet.withheldInCountries;
        this.withheldScope = tweet.withheldScope;

        setFavoriteCount(tweet.favoriteCount);
        setUser(tweet.user);
        setCoordinates(tweet.coordinates);
        setPlace(tweet.place);
        setEntities(tweet.entities);
    }

    private void setEntities(TweetEntities entities)
    {
        if (entities != null)
        {
            List<MediaEntity> media = entities.media;
            if (entities.media != null)
            {
                MediaEntityRecord.setMediaEntryList(this, entities.media);
            }
            //        entities.hashtags;
            //        entities.urls;
            //        entities.userMentions;
        }
    }

    private TweetEntities getEntities()
    {
        return new TweetEntities(null,null,MediaEntityRecord.getMediaEntityList(this),null);
    }

    private void setPlace(Place place)
    {
        if (place != null)
        {
            this.place_attributes = place.attributes;
            this.place_boundingbox = place.boundingBox;
            this.place_country = place.country;
            this.place_countryCode = place.countryCode;
            this.place_fullName = place.fullName;
            this.place_id = place.id;
            this.place_name = place.name;
            this.place_placeType = place.placeType;
            this.place_url = place.url;
        }
    }

    private Place getPlace()
    {
        return new Place(place_attributes, place_boundingbox, place_country, place_countryCode, place_fullName, place_id, place_name, place_placeType, place_url);
    }

    private void setCoordinates(Coordinates coordinates)
    {
        if (coordinates != null)
        {
            this.coordinate_Latitude = coordinates.getLatitude();
            this.coordinate_Longitude = coordinates.getLongitude();
            this.coordinates_Type = coordinates.type;
        }
    }

    private Coordinates getCoordinates()
    {
        return new Coordinates(coordinate_Longitude, coordinate_Latitude, coordinates_Type);
    }


    /*
        Favorite Count
     */

    private void setFavoriteCount(Integer favoriteCount)
    {
        if (favoriteCount == null)
        {
            this.favoriteCount = 0;
        }
        else
        {
            this.favoriteCount = favoriteCount;
        }
    }

    private Integer getFavoriteCount()
    {
        return new Integer(this.favoriteCount);
    }

    /*
        User
     */
    private void setUser(User user)
    {
        userRecord = UserRecord.findExistingUserRecord(user.id);

        if (userRecord != null)
        {
            this.user = userRecord.getUser();
        }
        else
        {
            userRecord = new UserRecord(user);
            userRecord.save();
            this.user = userRecord.getUser();
        }



    }

    private User getUser()
    {
        return user;
    }



    ////////////
    // Create //
    ////////////


    /**
     * Only save if the record does not already exist

     @Override public void save()
     {
     String whereClause = "tweetIdStr = " + this.tweetIdStr;

     List<TweetRecord> tweetRecords = null;
     try
     {
     tweetRecords = TweetRecord.find(TweetRecord.class, whereClause);
     } catch (SQLiteException e)
     {
     tweetRecords = new ArrayList<TweetRecord>();
     }


     if (tweetRecords.size() == 0)
     {
     super.save();
     }
     else
     {
     Log.e(TAG, "Will not persist, tweetId: " + this.tweetIdStr + " as already been persisted");
     }
     }
     */

    //////////
    // Read //
    //////////

    // find(Class<T> type, String whereClause, String[] whereArgs, String groupBy, String orderBy, String limit)


    /**
     * @param searchTermRecord
     * @param limit            Limit return results
     * @return
     */
    public static List<Tweet> getTweetsBySearchTerm(SearchTermRecord searchTermRecord, int limit)
    {
        return null;
    }

    public static List<Tweet> getAllTweets()
    {
        List<TweetRecord> tmp = listAll(TweetRecord.class);

        List<Tweet> list = new ArrayList<Tweet>();
        for (TweetRecord record : tmp)
        {
            list.add(record.getTweet());
        }

        return list;
    }


    ////////////
    // Update //
    ////////////

    // No update methods required

    ////////////
    // Delete //
    ////////////

    /**
     * Delete all the records that are in this list
     *
     * @param list the records marked to be deleted
     * @return the number of records deleted
     */
    public static int deleteByList(List<Tweet> list)
    {
        return -1;
    }

    public Tweet getTweet()
    {
        return new Tweet(getCoordinates(), createdAt, currentUserRetweet, getEntities(), getFavoriteCount(), favorited, filterLevel, tweetId, idStr, inReplyToScreenName, inReplyToStatusId, inReplyToStatusIdStr, inReplyToUserId, inReplyToUserIdStr, lang, getPlace(), possiblySensitive, scopes, retweetCount, retweeted, retweetedStatus, source, text, truncated, getUser(), withheldCopyright, withheldInCountries, withheldScope);
    }

    /**
     * Delete all TweetRecords and related tables
     */
    public static void format()
    {
        UserRecord.deleteAll(UserRecord.class);
        MediaEntityRecord.deleteAll(MediaEntityRecord.class);
        TweetRecord.deleteAll(TweetRecord.class);
    }
}
