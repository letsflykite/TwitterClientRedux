package com.codepath.apps.twitterapp.fragments;

import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.twitterapp.MyTwitterClientApp;
import com.codepath.apps.twitterapp.activities.ProfileActivity;
import com.codepath.apps.twitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class UserTimelineFragment extends TweetsListFragment {


			
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
//		if (!Utils.hasNetworkConnection(getActivity()))
//		loadTweetsFromDBtoAdapter();
		loadAPIData(0);
//		String screenName = null;
//		ProfileActivity callingActivity = ((ProfileActivity)getActivity());
//		if (callingActivity!=null)
//			screenName = callingActivity.getScreenNameFromIntent();
//		RequestParams rparams = null;
//		if (screenName != null) {
//			rparams = new RequestParams();
//			rparams.put("screen_name", screenName);
//		}
//		//make a call to get user timeline
//		MyTwitterClientApp.getRestClient().getUserTimeline(rparams, 
//				new JsonHttpResponseHandler() {
//					@Override
//					public void onSuccess(JSONArray jsonTweets) {
//						getAdapter().addAll(Tweet.fromJson(jsonTweets));
//					}
//				}
//		);
	}
	
	//loadUserTimelineAPIData
	@Override
	public void loadAPIData(int page) {
		super.loadAPIData(page);
		String screenName = null;
		ProfileActivity callingActivity = ((ProfileActivity)getActivity());
		if (callingActivity!=null)
			screenName = callingActivity.getScreenNameFromIntent();
		RequestParams rparams = null;
		if (screenName != null) {
			rparams = new RequestParams();
			rparams.put("screen_name", screenName);
		}
		if (tweets != null) {
			//make a call to get user timeline
			MyTwitterClientApp.getRestClient().getUserTimeline(rparams,
					new JsonHttpResponseHandler() {
						// before call back, deserialize into json
						// if no JSONArray, might have to select a JSONObject

						@Override
						public void onSuccess(JSONArray jsonTweets) {

							// after API call is finished, access to the
							// Fragment
							// add the tweets from this API call into the
							// Adapter from the Fragment
							getAdapter().addAll(Tweet.fromJson(jsonTweets));
							Log.d("DEBUG",
									Arrays.deepToString(tweets.toArray()));
							// if (tweets.size() > count)
							// Toast.makeText(getBaseContext(),
							// "End of tweets ...", Toast.LENGTH_SHORT)
							// .show();
//							Tweet.dumpTweetsToDB(tweets);
							// scroll to top
							lvTweets.smoothScrollToPosition(0);
							lvTweets.onRefreshComplete();
							
							hideProgressBar();
						}

						@Override
						public void onFailure(Throwable e,
								JSONObject errorResponse) {
			                Log.d("DEBUG", "Fetch user timeline error: " + e.toString());
						}
					});

		}
	}
	
	/*
	private String getScreenNameFromActivityIntent () {
		//find screen name if possible
		String screenName = null;
		Intent i = getActivity().getIntent();
		if(i != null && i.getExtras() != null){
			    	screenName = i.getStringExtra("screen_name");
		}
		return screenName;
	}
	*/
	
	@Override
	public void insertComposedTweet(Tweet newTweet) {
		super.insertComposedTweet(newTweet);
	}
}
