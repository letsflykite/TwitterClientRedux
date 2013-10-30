package com.codepath.apps.twitterapp.fragments;

import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;

import com.codepath.apps.twitterapp.MyTwitterClientApp;
import com.codepath.apps.twitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.os.Bundle;
import android.util.Log;

public class MentionsFragment extends TweetsListFragment {

//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		//make a call to get mentions
//		MyTwitterClientApp.getRestClient().getMentions(
//				new JsonHttpResponseHandler() {
//					@Override
//					public void onSuccess(JSONArray jsonTweets) {
//						getAdapter().addAll(Tweet.fromJson(jsonTweets));
//					}
//				}
//		);
//	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
//		if (!Utils.hasNetworkConnection(getActivity()))
//			loadTweetsFromDBtoAdapter();
		loadAPIData(0);
	}
	
	//loadMentionsAPIData
	@Override
	public void loadAPIData(int page) {
		super.loadAPIData(page);
		if (tweets != null) {
			// get mentions feed
			MyTwitterClientApp.getRestClient().getMentions(
					new JsonHttpResponseHandler() {
						// before call back, deserialize into json
						// if no JSONArray, might have to select a JSONObject

						@Override
						public void onSuccess(JSONArray jsonTweets) {

							// after API call is finished, access to the
							// Fragment
							// add the tweets from this API call into the
							// Adapter from the Fragment
							// tweetLvAdapter.addAll(Tweet.fromJson(jsonTweets));
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
			                Log.d("DEBUG", "Fetch mentions error: " + e.toString());
						}
					});

		}
	}

}
