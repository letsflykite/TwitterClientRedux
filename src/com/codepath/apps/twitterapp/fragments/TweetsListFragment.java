package com.codepath.apps.twitterapp.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.twitterapp.DummyActivity;
import com.codepath.apps.twitterapp.R;
import com.codepath.apps.twitterapp.adapters.TweetsAdapter;
import com.codepath.apps.twitterapp.listeners.EndlessScrollListener;
import com.codepath.apps.twitterapp.models.Tweet;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;
//android.support.v4.app.Fragment works across all versions
//Fragment is modular component
//not only the view is in the Fragment, we want the logic in it too

/*
 * The most common are onCreateView which is in almost every fragment to setup the view, 
 * onCreate for any initialization and onActivityCreated used for setting up things that 
 * can only take place once the Activity is created.
 */
//display tweets of any type
public class TweetsListFragment extends Fragment {
	protected ArrayList<Tweet> tweets; //tweets array
	protected TweetsAdapter tweetLvAdapter; // tweets adapter
//	protected ListView lvTweets; // listview
	protected PullToRefreshListView lvTweets;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		tweets = new ArrayList<Tweet>();
	}
	
	//onCreateView method must be defined for every fragment, this is where you inflate the xml file
	@Override
	public View onCreateView(LayoutInflater inf, ViewGroup parent, Bundle savedInstanceState) {
		View v = inf.inflate(R.layout.fragment_tweets_lists, parent, false);
		lvTweets = (PullToRefreshListView)v.findViewById(R.id.lvTweets);
		// Attach the listener to the AdapterView in onCreate
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				// Triggered only when new data needs to be appended to the list
				// Add whatever code is needed to append new items to your
				// AdapterView
				// loadDataFromAPI(totalItemsCount);
				loadAPIData(0);
			}
		});
		lvTweets.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list contents
                // Make sure you call listView.onRefreshComplete()
                // once the loading is done. This can be done from here or any
                // place such as when the network request has completed successfully.
//                fetchTweetsAsync(0);
                loadAPIData(0);
            }
        });
		return v;
	}
	
	protected void loadAPIData(int page) {
//		MenuItem mi = (MenuItem)getActivity().findViewById(R.id.menu_refresh);
//		if (mi!=null)
//			mi.getActionView().setVisibility(ProgressBar.VISIBLE);
//		ProgressBar pbLoading = (ProgressBar)(getActivity().findViewById(R.id.pbLoading));
//		if (pbLoading!=null)
//		pbLoading.setVisibility(ProgressBar.VISIBLE);

		((DummyActivity)getActivity()).setPbFlag();
		getActivity().invalidateOptionsMenu();
	}

	public void hideProgressBar(){
		((DummyActivity)getActivity()).resetPbFlag();
		getActivity().invalidateOptionsMenu();
//		ProgressBar pbLoading = (ProgressBar)(getActivity().findViewById(R.id.pbLoading));
//		if (pbLoading!=null)
//		pbLoading.setVisibility(ProgressBar.INVISIBLE);	
	}
	//onActivityCreated can be deleted here. It is fired whenever the fragment is being
	//displayed on screen and the activity exists
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//within the onActivityCreated, you can access the activity by getActivity() which gives you
		// a handle to the activity that owns the fragment
		//getActivity().;
		//Logic to load the adapter

//		tweets = Tweet.fromJson(jsonTweets);
		
		tweetLvAdapter = new TweetsAdapter(getActivity(), tweets);
//		lvTweets = (PullToRefreshListView) getActivity().findViewById(R.id.lvTweets);
		lvTweets.setAdapter(tweetLvAdapter);
		

	}
	
	//get access to the adapter from the Fragment
	public TweetsAdapter getAdapter() {
		return tweetLvAdapter;
	}
	
//    protected void fetchTweetsAsync(int page) {
//    }
    
	protected void loadTweetsFromDBtoAdapter() {

	}
	
	protected void insertComposedTweet(Tweet newTweet) {
//		tweets.add(0, newTweet);
	}
}
