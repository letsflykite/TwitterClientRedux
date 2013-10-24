package com.codepath.apps.twitterapp.activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ClipData.Item;
import android.content.SharedPreferences.Editor;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.activeandroid.ActiveAndroid;
import com.codepath.apps.twitterapp.MyTwitterClientApp;
import com.codepath.apps.twitterapp.R;
import com.codepath.apps.twitterapp.TweetsAdapter;
import com.codepath.apps.twitterapp.listeners.EndlessScrollListener;
import com.codepath.apps.twitterapp.models.Tweet;
import com.codepath.apps.twitterapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class TimeLineActivity extends Activity {
	// declare the views
	// ListView
	ListView lvTweets;
	ArrayList<Tweet> tweets;
	private TweetsAdapter tweetLvAdapter;
	// private MenuItem refreshItem;

	int count = 25;
	int since_id = 0;
	int max_id = 0;
	SharedPreferences pref;
	String userScreenName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_line);
		pref = PreferenceManager.getDefaultSharedPreferences(this);
		userScreenName = pref.getString("userScreenName", "default");

		addCurrentUserToActionBar();
		setupViews();
		tweets = new ArrayList<Tweet>();
		tweetLvAdapter = new TweetsAdapter(getBaseContext(), tweets);
		lvTweets.setAdapter(tweetLvAdapter);
		loadAPIData();
		// MyTwitterClientApp.getRestClient().getHomeTimeline(null, new
		// JsonHttpResponseHandler(){
		// //before call back, deserialize into json
		// //if no JSONArray, might have to select a JSONObject
		// @Override
		// public void onSuccess(JSONArray jsonTweets) {
		// Log.d("DEBUG", jsonTweets.toString());
		// tweetLvAdapter.addAll(Tweet.fromJson(jsonTweets));
		// Log.d("DEBUG", tweets.toString());
		// }
		// });
		// Attach the listener to the AdapterView in onCreate
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				// Triggered only when new data needs to be appended to the list
				// Add whatever code is needed to append new items to your
				// AdapterView
				// loadDataFromAPI(totalItemsCount);
				loadAPIData();
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.time_line, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_refresh:
			// refreshItem = item;
			// refreshItem.setActionView(R.layout.action_refresh);
			// refreshItem.expandActionView();
			tweets.clear();
			loadAPIData();
			// RequestParams rparams = null;
			// if(tweets != null && !tweets.isEmpty() && tweets.size() < 200){
			// rparams = new RequestParams();
			// //rparams.put("max_id", String.valueOf(tweets.get(tweets.size() -
			// 1).getTweetId()));
			// rparams.put("since_id", String.valueOf(tweets.get(0).getId()));
			// } else {
			// //max limit of tweets for timeline that rest api supports
			// tweets.clear();
			// }

			// scroll to top
			lvTweets.smoothScrollToPosition(0);

			// get timeline feed
			// MyTwitterClientApp.getRestClient().getHomeTimeline(rparams, new
			// JsonHttpResponseHandler() {
			// @Override
			// public void onSuccess(JSONArray jsonTweets){
			// tweets.addAll(Tweet.fromJson(jsonTweets));
			// Collections.sort(tweets);
			// tweetLvAdapter.notifyDataSetChanged();
			// // refreshItem.collapseActionView();
			// // refreshItem.setActionView(null);
			// Log.d("DEBUG", Arrays.deepToString(tweets.toArray()));
			// }
			// });
			break;

		case R.id.menu_compose:
			Intent i = new Intent(getBaseContext(), ComposeActivity.class);
			startActivityForResult(i, ComposeActivity.COMPOSE_TWEET_ACTIVITY_ID);
			break;

		default:
			break;
		}
		return true;
	}

	private void addCurrentUserToActionBar() {
		// get current user
		userScreenName = pref.getString("userScreenName", "default");
		setTitle("@" + userScreenName);

		// User currentUser;
		MyTwitterClientApp.getRestClient().getCurrentUser(null,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject userInfo) {
						User currentUser = User.fromJson(userInfo);
						if (currentUser != null) {
							// set action bar

							userScreenName = currentUser.getScreenName();
							setTitle("@" + userScreenName);

							// //load up the pictures
							// ImageLoader.getInstance().displayImage(currentUser.getProfileImageUrl(),
							// ivMyProfilePicture);

							// //show screen name
							// String formattedName = "<b>" +
							// currentUser.getName() + "</b>" +
							// " <small><font color='#777777'>@" +
							// currentUser.getScreenName() + "</font></small>";
							// tvMyScreenName.setText(Html.fromHtml(formattedName));
					        Editor edit = pref.edit();
					        edit.putString("userScreenName", userScreenName);
					        edit.commit(); 
						}
					}
					@Override
					public void onFailure(Throwable e,
							JSONObject errorResponse) {
						Log.d("DEBUG", "getUser API failure");
						userScreenName = pref.getString("userScreenName", "default");
						setTitle("@" + userScreenName);
						//load from database
						tweets = Tweet.loadAllTweetsFromDB();
					}
				});
	}

	// find the views by Id in xml
	public void setupViews() {
		lvTweets = (ListView) findViewById(R.id.lvTweets);
	}

	private void loadAPIData() {
		//load from database before trying database
		tweetLvAdapter.addAll (Tweet.loadAllTweetsFromDB());
		// Subtract 1 from the lowest Tweet ID returned from the previous
		// request and use this for the value of max_id.
		// setting the since_id parameter to the greatest ID of all the
		// Tweets your application has already processed
		// if(tweets != null && !tweets.isEmpty() && tweets.size() < count){
		if (tweets != null) {
			RequestParams rparams = new RequestParams();
			rparams.put("count", String.valueOf(count));
			if (tweets.size() > 0) {
				rparams.put("since_id", String.valueOf(tweets.get(0).getTweetId()));
				rparams.put("max_id", String.valueOf(tweets.get(
						tweets.size() - 1).getTweetId() - 1));
			}

			// get timeline feed
			MyTwitterClientApp.getRestClient().getHomeTimeline(rparams,
					new JsonHttpResponseHandler() {
						// before call back, deserialize into json
						// if no JSONArray, might have to select a JSONObject

						@Override
						public void onSuccess(JSONArray jsonTweets) {
							// tweetLvAdapter.addAll(Tweet.fromJson(jsonTweets));
							tweets.addAll(Tweet.fromJson(jsonTweets));
	        			    Collections.sort(tweets);
							tweetLvAdapter.notifyDataSetChanged();
							Log.d("DEBUG",
									Arrays.deepToString(tweets.toArray()));
//							if (tweets.size() > count)
//								Toast.makeText(getBaseContext(),
//										"End of tweets ...", Toast.LENGTH_SHORT)
//										.show();
							Tweet.dumpTweetsToDB(tweets);
						}

						@Override
						public void onFailure(Throwable e,
								JSONObject errorResponse) {
							Log.d("DEBUG", "API failure");
							//load from database
							tweets = Tweet.loadAllTweetsFromDB();
						}
					});

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK
				&& requestCode == ComposeActivity.COMPOSE_TWEET_ACTIVITY_ID) {
			Tweet newTweet = null;
			if (data.hasExtra("tweet")) {
				// Tweet newTweet = (Tweet)data.getSerializableExtra("tweet");
				String newTweetStr = data.getExtras().getString("tweet");

				// newTweetStr
				try {

					JSONObject obj = new JSONObject(newTweetStr);
					newTweet = Tweet.fromJson(obj);
					Log.d("My App", obj.toString());

				} catch (Throwable t) {
					Log.e("My App", "Could not parse malformed JSON: \""
							+ newTweetStr + "\"");
				}
			}
			if (newTweet != null)
				tweets.add(0, newTweet);
			// colorSelected = data.getExtras().getString("colorSelected");
			// RequestParams rparams = null;
			// if(tweets != null && !tweets.isEmpty() && tweets.size() < 200){
			// rparams = new RequestParams();
			// rparams.put("since_id", String.valueOf(tweets.get(0).getId()));
			// } else {
			// //max limit of tweets for timeline that rest api supports
			// tweets.clear();
			// }

			// scroll to top
			lvTweets.smoothScrollToPosition(0);

			// get timeline feed
			loadAPIData();
			// MyTwitterClientApp.getRestClient().getHomeTimeline(rparams, new
			// JsonHttpResponseHandler() {
			// @Override
			// public void onSuccess(JSONArray jsonTweets){
			// tweets.addAll(Tweet.fromJson(jsonTweets));
			// Collections.sort(tweets);
			// tweetLvAdapter.notifyDataSetChanged();
			// Log.d("DEBUG", Arrays.deepToString(tweets.toArray()));
			// }
			// });
		}
	}


}
