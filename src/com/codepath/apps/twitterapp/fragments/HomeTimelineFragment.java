package com.codepath.apps.twitterapp.fragments;

import java.util.Arrays;
import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.twitterapp.MyTwitterClientApp;
import com.codepath.apps.twitterapp.models.Tweet;
import com.codepath.apps.twitterapp.utils.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

//display home time line tweets
public class HomeTimelineFragment extends TweetsListFragment {
	int count = 25;
	private boolean needCleanTweets = false;
	// no need a xml

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (!Utils.hasNetworkConnection(getActivity()))
			loadTweetsFromDBtoAdapter();
		loadAPIData(0);

	}
	
	//loadHomeTimeLineAPIData
	@Override
	public void loadAPIData(int page) {
		super.loadAPIData(page);
		// Subtract 1 from the lowest Tweet ID returned from the previous
		// request and use this for the value of max_id.
		// setting the since_id parameter to the greatest ID of all the
		// Tweets your application has already processed
		// if(tweets != null && !tweets.isEmpty() && tweets.size() < count){
		if (tweets != null) {
			RequestParams rparams = new RequestParams();
			rparams.put("count", String.valueOf(count));
			if (tweets.size() > 0) {
				rparams.put("since_id",
						String.valueOf(tweets.get(0).getTweetId()));
				rparams.put("max_id", String.valueOf(tweets.get(
						tweets.size() - 1).getTweetId() - 1));
			}

			// get home timeline feed
			MyTwitterClientApp.getRestClient().getHomeTimeline(rparams,
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
//							tweets.clear();							
//							getAdapter().addAll(Tweet.fromJson(jsonTweets));

							tweets.addAll(Tweet.fromJson(jsonTweets));
							
							if (needCleanTweets) {
								tweets = Utils.removeDuplicates(tweets);
								Collections.sort(tweets);
								needCleanTweets = false;
							}
							getAdapter().notifyDataSetChanged();
							Log.d("DEBUG",
									Arrays.deepToString(tweets.toArray()));
							// if (tweets.size() > count)
							// Toast.makeText(getBaseContext(),
							// "End of tweets ...", Toast.LENGTH_SHORT)
							// .show();
							Tweet.dumpTweetsToDB(tweets);
							// scroll to top
							lvTweets.smoothScrollToPosition(0);
							lvTweets.onRefreshComplete();
							
							hideProgressBar();

						}

						@Override
						public void onFailure(Throwable e,
								JSONObject errorResponse) {
			                Log.d("DEBUG", "Fetch home timeline error: " + e.toString());
						}
					});

		}
	}


	@Override
	protected void loadTweetsFromDBtoAdapter() {
//		ArrayList<Tweet> tweetsFromDB = Tweet.loadAllTweetsFromDB();
//		if (tweetLvAdapter!=null && tweetsFromDB!= null)
//		    tweetLvAdapter.addAll(tweetsFromDB);
		tweets = Tweet.loadAllTweetsFromDB();
		if (tweetLvAdapter!=null && tweets!= null)
		    tweetLvAdapter.addAll(tweets);
		// scroll to top
		lvTweets.smoothScrollToPosition(0);
	}
	
	@Override
	public void insertComposedTweet(Tweet newTweet) {
		tweets.add(0, newTweet);
		needCleanTweets  = true;
	}
}
