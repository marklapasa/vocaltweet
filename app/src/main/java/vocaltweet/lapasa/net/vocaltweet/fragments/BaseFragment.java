package vocaltweet.lapasa.net.vocaltweet.fragments;


import android.app.Activity;
import android.app.Fragment;

import java.util.Observer;

import vocaltweet.lapasa.net.vocaltweet.ITweetModelActivity;
import vocaltweet.lapasa.net.vocaltweet.R;
import vocaltweet.lapasa.net.vocaltweet.models.TweetsModel;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment implements Observer
{
    private String oldTitle;
    protected TweetsModel model;
    protected ITweetModelActivity act;
    protected PlaybackControlsFragment playbackControlsFragment;

    public BaseFragment()
    {
        // Required empty public constructor
    }


    @Override
    public void onDestroy()
    {
        model.deleteObserver(this);
        super.onDestroy();
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try
        {
            act = (ITweetModelActivity) activity;
            model = act.getModel();
            model.addObserver(this);
            playbackControlsFragment = (PlaybackControlsFragment) activity.getFragmentManager().findFragmentById(R.id.playbackControls);
        } catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString()
                    + " must implement ITweetModelActivity");
        }
    }


    @Override
    public void onDetach()
    {
        super.onDetach();
        act = null;
    }


}
