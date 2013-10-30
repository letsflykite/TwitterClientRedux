package com.codepath.apps.twitterapp.activities;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.twitterapp.MyTwitterClientApp;
import com.codepath.apps.twitterapp.R;
import com.codepath.apps.twitterapp.models.Tweet;
import com.codepath.apps.twitterapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

/*
 *  One optional feature is displaying a character count for the twitter 
 *  client as the user types their tweet. Each tweet can only be a maximum 
 *  of 140 characters. To achieve this, you can add a TextView to your 
 *  ComposeActivity and then attach a listener for when the user types text 
 *  into the EditText. See the event handling cliffnotes to see how to use 
 *  the addTextChangedListener to fire events whenever the user types into 
 *  the text field. If the total characters exceeds 140, consider disabling 
 *  the submit button or making the count red to indicate that the tweet 
 *  isn't valid.
 */
public class ComposeActivity extends Activity {

	public static final int COMPOSE_TWEET_ACTIVITY_ID = 101;
	public static final int MAX_TWEET_SIZE = 140;
	// private User currentUser;
	ImageView ivMyProfilePicture;
	TextView tvMyScreenName;
	EditText etTweetBody;
	int tweetCharactersLeft;
	JSONObject jsonTweetSent = null;
	Tweet tweetSent;
	TextView tvCharacterLeft;

	// ActionBar actionBar = getActionBar();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose);
		// save off layout handles
		ivMyProfilePicture = (ImageView) findViewById(R.id.ivMyProfilePicture);
		etTweetBody = (EditText) findViewById(R.id.etTweetBody);
		tvMyScreenName = (TextView) findViewById(R.id.tvMyScreenName);
		tvCharacterLeft = (TextView) findViewById(R.id.tvCharLeft);
		registerTweetSizeCounter();

		// get current user
		MyTwitterClientApp.getRestClient().getCurrentUser(null,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject userInfo) {
						User currentUser = User.fromJson(userInfo);
						if (currentUser != null) {
							// set action bar

							setTitle(currentUser.getScreenName());

							// load up the pictures
							ImageLoader.getInstance().displayImage(
									currentUser.getProfileImageUrl(),
									ivMyProfilePicture);

							// show screen name
							String formattedName = "<b>"
									+ currentUser.getUserName() + "</b>"
									+ " <small><font color='#777777'>@"
									+ currentUser.getScreenName()
									+ "</font></small>";
							tvMyScreenName.setText(Html.fromHtml(formattedName));
						}
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compose, menu);
		return true;
	}

	private void registerTweetSizeCounter() {
		etTweetBody.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable textView) {

				// Fires right after the text has changed
				int len = etTweetBody.length();
				tweetCharactersLeft = MAX_TWEET_SIZE - len;
				if (len <= 140) {
					tvCharacterLeft.setText(String.valueOf(tweetCharactersLeft)
							+ " characters remaining");

				} else {
					tvCharacterLeft.setText(String.valueOf(tweetCharactersLeft)
							+ " too many characters");

				}
				// When an event occurs and you want to perform a menu update,
				// you must call invalidateOptionsMenu() to request that the
				// system call onPrepareOptionsMenu().
				invalidateOptionsMenu();
				Log.d("DEBUG", "Left Characters: " + tweetCharactersLeft);
				return;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// Fires right before text is changing
				return;
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int count,
					int after) {
				// Fires right as the text is being changed (even supplies the
				// range of text)
				return;
			}
		});
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem tweetItem = menu.findItem(R.id.menu_tweet);
		if (tweetCharactersLeft < 0)
			tweetItem.setEnabled(false);
		else
			tweetItem.setEnabled(true);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_tweet:
			String tweetBody = etTweetBody.getText().toString();
			if (tweetBody.length() == 0) {
				Toast.makeText(getApplicationContext(), "Tweet Discarded",
						Toast.LENGTH_SHORT).show();
				Intent data = new Intent();
				if (getParent() == null) {
					setResult(Activity.RESULT_OK, data);
				} else {
					getParent().setResult(Activity.RESULT_OK, data);
				}
				finish();
			} else if (tweetBody.length() > 140) {
				Toast.makeText(getApplicationContext(), "Tweet cannot be sent",
						Toast.LENGTH_SHORT).show();

			} else {
				RequestParams params = new RequestParams();
				params.put("status", tweetBody);

				MyTwitterClientApp.getRestClient().postStatusUpdate(params,
						new JsonHttpResponseHandler() {
							@Override
							public void onSuccess(JSONObject jsonTweets) {
								tweetSent = Tweet.fromJson(jsonTweets);
								jsonTweetSent = jsonTweets;
								Toast.makeText(getApplicationContext(),
										"Tweet Sent", Toast.LENGTH_SHORT)
										.show();
							}

							@Override
							public void onFinish() {
								Intent data = new Intent();
								// data.putExtra("tweet", tweetSent);
								if (jsonTweetSent != null)
									data.putExtra("tweet",
											jsonTweetSent.toString());
								if (getParent() == null) {
									setResult(Activity.RESULT_OK, data);
								} else {
									getParent().setResult(Activity.RESULT_OK,
											data);
								}
								finish();
							}

							@Override
							public void onFailure(Throwable error,
									String content) {
								Toast.makeText(getApplicationContext(),
										"Service Unavailable!!!",
										Toast.LENGTH_SHORT).show();
							}
						});
			}
			break;

		default:
			break;
		}
		return true;
	}

	// @Override
	// public void onBackPressed() {
	// Toast.makeText(getApplicationContext(), "Tweet Discarded",
	// Toast.LENGTH_SHORT).show();
	// Intent data = new Intent();
	// if (getParent() == null) {
	// setResult(Activity.RESULT_OK, data);
	// } else {
	// getParent().setResult(Activity.RESULT_OK, data);
	// }
	// finish();
	// super.onBackPressed();
	// }

}
