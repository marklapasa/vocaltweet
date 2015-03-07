package net.lapasa.vocaltweet.fragments;


import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import net.lapasa.vocaltweet.R;
import net.lapasa.vocaltweet.models.SearchTermsModel;
import net.lapasa.vocaltweet.models.TweetsModel;
import net.lapasa.vocaltweet.models.entities.SearchTermRecord;

import java.util.Observable;
import java.util.Observer;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends Fragment implements Observer
{

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    private static final String CLOSE_NAV_DRAWER = "CLOSE_NAV_DRAWER" ;


    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout navDrawer;
    private ListView recentSearchesListView;
    private View fragContainer;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;
    private RecentSearchesAdapter adapter;
    private EditText searchEditText;
    private SearchTermsModel model = SearchTermsModel.getInstance();
    private ImageButton clearCancelBtn;


    public NavigationDrawerFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null)
        {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        // Select either the default item (0) or the last selected item.
        selectItem(mCurrentSelectedPosition);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);


        // User

        // Search Field
        initSearchField(view);

        // Recent Searches
        initRecentSearches(inflater, view);

        return view;
    }

    private void initSearchField(View view)
    {
        clearCancelBtn = (ImageButton) view.findViewById(R.id.clearCancelBtn);

        searchEditText = (EditText) view.findViewById(R.id.searchTermEditText);
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (event != null && v.getText().length() > 0)
                {
                    executeSearch();
                }
                return true;
            }
        });
        searchEditText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

                if (s.length() == 0)
                {
                    clearCancelBtn.setVisibility(View.GONE);
                }
                else
                {
                    clearCancelBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                // Do nothing
            }
        });
/*
        searchBtn = (Button) view.findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                executeSearch();
            }
        });


*/

        clearCancelBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                searchEditText.setText(null);
            }
        });
    }

    private void executeSearch()
    {
        // Close the Navigation drawer
        navDrawer.closeDrawer(fragContainer);


        // Persist the SearchTerm if it does not already exist
        String term = String.valueOf(searchEditText.getText());
        SearchTermRecord record = null;

        record = SearchTermRecord.getRecord(term);

        if (record == null)
        {
            record = new SearchTermRecord(term);
            record.save();
        }

        // Update the title
        getActionBar().setTitle(term);

        // Launch a search for tweets

        // Clear the edit text field
        searchEditText.setText(null);

        model.loadRecentSearchTerms();

        TweetsModel.getInstance().loadTweets(record, getActivity());

        navDrawer.closeDrawer(fragContainer);

    }

    private void initRecentSearches(LayoutInflater inflater, View view)
    {
        recentSearchesListView = (ListView) view.findViewById(R.id.recentSearchesListView);
        recentSearchesListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                selectItem(position);
            }
        });

        adapter = new RecentSearchesAdapter(getActivity().getLayoutInflater());

        recentSearchesListView.setAdapter(adapter);
        recentSearchesListView.setItemChecked(mCurrentSelectedPosition, true);

//        recentSearchesListView.addHeaderView(inflater.inflate(R.layout.recent_searches_header, null));

        recentSearchesListView.setEmptyView(view.findViewById(R.id.emptyView));
    }


    public boolean isDrawerOpen()
    {
        return navDrawer != null && navDrawer.isDrawerOpen(fragContainer);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's recent_searches_empty.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout)
    {
        fragContainer = getActivity().findViewById(fragmentId);
        navDrawer = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        navDrawer.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(),                    /* host Activity */
                navDrawer,                    /* DrawerLayout object */
                R.drawable.ic_drawer,             /* nav drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */)
        {
            @Override
            public void onDrawerClosed(View drawerView)
            {
                super.onDrawerClosed(drawerView);
                if (!isAdded())
                {
                    return;
                }

                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);
                if (!isAdded())
                {
                    return;
                }

                if (!mUserLearnedDrawer)
                {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).commit();
                }

                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState)
        {
            navDrawer.openDrawer(fragContainer);
        }

        // Defer code dependent on restoration of previous instance state.
        navDrawer.post(new Runnable()
        {
            @Override
            public void run()
            {
                mDrawerToggle.syncState();
            }
        });

        navDrawer.setDrawerListener(mDrawerToggle);
    }

    private void selectItem(int position)
    {
        mCurrentSelectedPosition = position;
        if (recentSearchesListView != null)
        {
            recentSearchesListView.setItemChecked(position, true);
        }
        if (navDrawer != null)
        {
            navDrawer.closeDrawer(fragContainer);
        }
        //        if (mCallbacks != null)
        //        {
        //            mCallbacks.onNavigationDrawerItemSelected(position);
        //        }

        if (adapter != null)
        {
            String title = (String) adapter.getItem(0);
            getActionBar().setTitle(title);
        }

    }


    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (navDrawer != null && isDrawerOpen())
        {
            inflater.inflate(R.menu.global, menu);
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }


    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private void showGlobalContextActionBar()
    {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(R.string.app_name);
    }

    private ActionBar getActionBar()
    {
        return getActivity().getActionBar();
    }


    @Override
    public void update(Observable observable, Object data)
    {
        if (data == SearchTermsModel.SEARCH_TERMS_LOADED)
        {
            adapter.notifyDataSetInvalidated();
        }
        else if (data == CLOSE_NAV_DRAWER)
        {
            navDrawer.closeDrawers();
        }

    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        model.addObserver(this);
    }

    public void closeDrawer()
    {
        navDrawer.closeDrawers();
    }

}
