package net.lapasa.vocaltweet;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterSession;

import net.lapasa.vocaltweet.fragments.LoginFragment;
import net.lapasa.vocaltweet.fragments.NavigationDrawerFragment;
import net.lapasa.vocaltweet.fragments.TweetsListFragment;
import net.lapasa.vocaltweet.models.SearchTermsModel;
import net.lapasa.vocaltweet.models.TweetsModel;

import java.util.Locale;

import io.fabric.sdk.android.Fabric;


public class MainActivity extends Activity implements ITweetModelActivity, TextToSpeech.OnInitListener
{

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "tsFoNCmPymFlWaz0SmldK3pOr";
    private static final String TWITTER_SECRET = "gOw50hNRjIRvEmfS2qM5cdtU8Tetwklj0z6kit7t8UX5miyUyD";


    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment navDrawerFrag;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private TweetsModel model;
    private TextToSpeech tts;
    private SharedPreferences prefs;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        setContentView(R.layout.activity_main);

        // Set up the drawer.
        navDrawerFrag = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);
        navDrawerFrag.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));



        mTitle = getTitle();

        // Configure Text-to-Speech
        this.tts = new TextToSpeech(this, this);




        TwitterSession session = Twitter.getSessionManager().getActiveSession();
        if (session == null)
        {
            // User is not logged into Twitter so use Fabric to get a valid session
            loadFragment(new LoginFragment());
        }
        else
        {
            // Use this session to get the user's tweets
            loadFragment(new TweetsListFragment());
        }

        prefs = getPreferences(MODE_PRIVATE);

    }

    /**
     * Render target fragment to the main container
     *
     * @param frag
     */
    public void loadFragment(Fragment frag)
    {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction txn = fm.beginTransaction();
        txn.replace(R.id.container, frag, frag.getClass().getName());
        txn.commit();
    }


    /**
     * Used by the NavigationDrawer to display the previous title
     */
    public void restoreActionBar()
    {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            Intent settingsIntent = new Intent();
            settingsIntent.setClass(this, SettingsActivity.class);
            startActivity(settingsIntent);
            navDrawerFrag.closeDrawer();
            return true;
        }
        else if(id == R.id.action_clear_recent_searches)
        {
            SearchTermsModel.getInstance().clearCache();
            return true;
        }
        else if (id == R.id.action_clear_cached_tweets)
        {
            TweetsModel.getInstance().clearCache(this);
            return true;
        }
        else if (id == R.id.action_about)
        {

        }
        else if (id == R.id.action_sign_out)
        {
            TwitterSession session = Twitter.getSessionManager().getActiveSession();
            Twitter.getSessionManager().clearActiveSession();
            navDrawerFrag.closeDrawer();
            loadFragment(new LoginFragment());
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * When the user returns from loging into twitter, launch the fragment that will display
     * the user's tweets.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);  // requestCode=140, resultCode=0

        // The Login With Twitter button needs to receive this in order to clean itself up
        Fragment frag = getFragmentManager().findFragmentByTag(LoginFragment.class.getName());
        if (frag != null)
        {
            frag.onActivityResult(requestCode, resultCode, data);
        }

        loadFragment(new TweetsListFragment());
    }


    @Override
    public TweetsModel getModel()
    {
       return TweetsModel.getInstance();
    }

    @Override
    public TextToSpeech getTextToSpeech()
    {
        return tts;
    }


    /**
     * Text to speech Initialization Callback that gets fired when TTS is ready for use
     *
     * @param status
     */
    @Override
    public void onInit(int status)
    {
        Log.d(MainActivity.class.getName(), "Text To Speech Engine is ready for use");
        tts.setLanguage(Locale.US);
        tts.setSpeechRate(0.6f);
    }


    /**
     * Clean up Text To Speech resources so that they don't take up memory
     */
    @Override
    protected void onDestroy()
    {
        tts.shutdown();
        super.onDestroy();
    }
}