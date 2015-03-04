package net.lapasa.vocaltweet;

import android.test.ActivityInstrumentationTestCase2;

import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;

import net.lapasa.vocaltweet.models.entities.Book;
import net.lapasa.vocaltweet.models.entities.TweetRecord;

import java.util.List;

/**
 * Created by mlapasa on 2/24/2015.
 */
public class TestTweetRecord extends ActivityInstrumentationTestCase2<MainActivity>
{
    private static final String DUMMY_USER_NAME = "Dummy User Name";
    private static final String DUMMY_USER_SCREEN_NAME = "Dummy User Screen Name";
    private static final String PROFILE_IMAGE_URL = "http://pbs.twimg.com/profile_images/571891730891288576/7WSWjYsr_normal.jpeg";
    private static final String TEXT = "RT @AccurateText: I'm shy at first but.. I do the stupidest random things when I get comfortable with someone.";
    private static final String ID_STR = "571931824798228480";
    private static final long ID = 571931824798228480L;
    private static final String CREATED_AT = "Sun Mar 01 07:34:31 +0000 2015";
    private MainActivity act;

    public TestTweetRecord()
    {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
//        setActivityInitialTouchMode(false);
//        this.act = getActivity();

        TweetRecord.format();
        Book.deleteAll(Book.class);
    }

    @Override
    protected void tearDown() throws Exception
    {
        super.tearDown();

        TweetRecord.format();
        Book.deleteAll(Book.class);
    }


    public void testInsertSingleTweet()
    {
        Tweet tweet = new Tweet(null,CREATED_AT,null,null,null,false,null,ID,ID_STR,null,-1,null,-1,null,null,null,false,null,-1,false,null,null,TEXT,false,getDummyUser(),false,null,null);

        TweetRecord record = new TweetRecord(tweet);

        record.save();

        // Query for persisted record
        List<Tweet> allTweets = TweetRecord.getAllTweets();
        assertEquals(1, allTweets.size());

        Tweet tweetResult = allTweets.get(0);
        assertNotNull(tweetResult);
        assertNotNull(tweetResult.user);
        assertNotNull(tweetResult.text);
        assertEquals(DUMMY_USER_NAME, tweetResult.user.name);
        assertEquals(DUMMY_USER_SCREEN_NAME, tweetResult.user.screenName);
        assertEquals(ID, tweet.id);
        assertEquals(ID_STR, tweet.idStr);
    }

    private User getDummyUser()
    {
        User u = new User(false,null,false,false, null,null,null,-1, false, -1,-1, false, 1, "1", false, null, -1, null, DUMMY_USER_NAME, null, null, null, false, null, PROFILE_IMAGE_URL, null, null, null, null, null, false, false, DUMMY_USER_SCREEN_NAME, false, null, -1, null, null, -1, false, null, null);
        return u;
    }


/*
    public void testBookInsert2()
    {
//        Application application = act.getApplication();
        Book b = new Book("Bible");
        b.save();

//        List<Book> results = Book.find(Book.class, "title = Bible", null);
        List<Book> results = Book.listAll(Book.class);
        assertTrue(results.size() > 0);
    }
*/

}
