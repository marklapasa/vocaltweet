package net.lapasa.vocaltweet.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.twitter.sdk.android.tweetui.CompactTweetView;
import com.twitter.sdk.android.tweetui.TweetViewAdapter;

import net.lapasa.vocaltweet.R;
import net.lapasa.vocaltweet.models.TweetsModel;

import java.util.Observable;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 */
public class TweetsListFragment extends BaseFragment implements AbsListView.OnItemClickListener, AdapterView.OnItemSelectedListener, View.OnClickListener
{

    private static final String TAG = TweetsListFragment.class.getName();
    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView listView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    //    private TweetViewFetchAdapter adapter;
    private TweetViewAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TweetsListFragment()
    {
        // Empty
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        //        adapter = new TweetViewFetchAdapter<CompactTweetView>(getActivity());
        adapter = new CustomTweetViewAdapter<CompactTweetView>(getActivity());
        ((AdapterView<ListAdapter>) listView).setAdapter(adapter);

        // Define the container that will store the data
        // This container will always be reused
        adapter.setTweets(model.getTweets());

        setEmptyText("Please Wait...");
        model.loadTweets(null, getActivity());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_tweets, container, false);
        listView = (AbsListView) view.findViewById(android.R.id.list);
        listView.setSmoothScrollbarEnabled(true);

        // Set OnItemClickListener so we can be notified on item clicks
        listView.setOnItemClickListener(this);
        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Log.d(TAG, "onItemSelected");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                Log.d(TAG, "onNothingSelected");
            }
        });
        listView.setEmptyView(view.findViewById(R.id.emptyView));


        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                model.loadTweets(model.getActiveSearchTerm(), getActivity());
            }
        });
        return view;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        // Open Tweet Details fragment
        act.loadFragment(new TweetDetailsFragment());
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText)
    {
        View emptyView = listView.getEmptyView();

        if (emptyView instanceof TextView)
        {
            ((TextView) emptyView).setText(emptyText);
        }
    }


    /**
     * Monitors Tweets model for changes
     *
     * @param observable
     * @param data
     */
    @Override
    public void update(Observable observable, Object data)
    {
        if (data == TweetUtteranceProgressListener.UTTERANCE_COMPLETE)
        {
            // Disable hightlights
            //            listView.setSelection(-1);
        }
        else if (data == TweetUtteranceProgressListener.UTTERANCE_STARTED)
        {
            // Show tweet in list highlighted
            //            listView.setSelection(model.getSelectedIndex());

            Log.i(TAG, "Scroll to tweet #" + model.getSelectedIndex());

            listView.post(new Runnable()
            {
                @Override
                public void run()
                {
                    listView.smoothScrollToPositionFromTop(model.getSelectedIndex(), 0);
                    CompactTweetView vg = (CompactTweetView) listView.getChildAt(model.getSelectedIndex());
                    vg.setBackgroundColor(0xff0000);
                    //                    listView.setSelection(model.getSelectedIndex());
                }
            });
        }
        else if (data == TweetsModel.NO_TWEETS_AVAILABLE)
        {
            setEmptyText("No tweets are available\nPull to refresh");
            swipeRefreshLayout.setRefreshing(false);
            refreshAdapter();
        }
        else if (data == TweetsModel.NEW_TWEETS_RECEIVED)
        {
            swipeRefreshLayout.setRefreshing(false);
            refreshAdapter();
        }
    }

    private void refreshAdapter()
    {
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                adapter.notifyDataSetInvalidated();
            }
        });
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        getActivity().getActionBar().setTitle(R.string.app_name);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        act.loadFragment(new TweetDetailsFragment());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }

    @Override
    public void onClick(View v)
    {
        v.animate();
    }
}
