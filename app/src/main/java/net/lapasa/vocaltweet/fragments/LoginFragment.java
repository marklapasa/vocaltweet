package net.lapasa.vocaltweet.fragments;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import net.lapasa.vocaltweet.MainActivity;
import net.lapasa.vocaltweet.R;


/**
 * This is displayed to the user if they have not already logged into Twitter
 */
public class LoginFragment extends Fragment
{
    public TwitterLoginButton loginBtn;

    public LoginFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
//        getActivity().getActionBar().hide();
        View v = inflater.inflate(R.layout.fragment_login, null);
        loginBtn = (TwitterLoginButton) v.findViewById(R.id.login_button);
        getActivity().getActionBar().hide();

        if (getActivity().getActionBar().isShowing())
        {
            ((MainActivity) getActivity()).getNavDrawer().closeDrawer();
        }

        loginBtn.setCallback(new Callback<TwitterSession>()
        {
            @Override
            public void success(Result<TwitterSession> result)
            {
                Toast.makeText(getActivity(), "Logged into Twitter successfully", Toast.LENGTH_LONG);
                getActivity().getActionBar().show();
            }

            @Override
            public void failure(TwitterException exception)
            {
                Toast.makeText(getActivity(), "Failed to log into Twitter successfully", Toast.LENGTH_LONG);
            }

        });

        return v;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        loginBtn.onActivityResult(requestCode, resultCode, data);
    }
}
