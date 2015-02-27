package net.lapasa.vocaltweet;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterSession;

import net.lapasa.vocaltweet.fragments.LoginFragment;
import net.lapasa.vocaltweet.fragments.NavigationDrawerFragment;
import net.lapasa.vocaltweet.fragments.TweetsListFragment;
import net.lapasa.vocaltweet.models.TweetsModel;

import java.util.Locale;

import io.fabric.sdk.android.Fabric;


public class MainActivity extends Activity implements ITweetModelActivity, TextToSpeech.OnInitListener
{

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "bPF4yqwkclyclnYmzwfuzCOsl";
    private static final String TWITTER_SECRET = "T1BWzi4DZkXzwocCyXClCvsU9DHz693ziPOYYA3DgSwc2lD5GF";


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


        // Customize toolbar
//        getActionBar().set
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
    public boolean onCreateOptionsMenu(Menu menu)
    {
        if (!navDrawerFrag.isDrawerOpen())
        {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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
            return true;
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
        super.onActivityResult(requestCode, resultCode, data);

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
        if (model == null)
        {
            this.model = new TweetsModel();
        }
        return model;
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
