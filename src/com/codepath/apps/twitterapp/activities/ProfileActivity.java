package com.codepath.apps.twitterapp.activities;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterapp.DummyActivity;
import com.codepath.apps.twitterapp.MyTwitterClientApp;
import com.codepath.apps.twitterapp.R;
import com.codepath.apps.twitterapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends DummyActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
	    String screenName = getScreenNameFromIntent() ;
	    //get screen name from intent
	    if(screenName != null){
		    RequestParams rparams = null;
		    rparams = new RequestParams();
			rparams.put("screen_name", screenName);	    
			loadUserProfileInfo(rparams);
	    }
	    else
	    	loadCurrentProfileInfo();
	}

	private void loadCurrentProfileInfo() {
		MyTwitterClientApp.getRestClient().getCurrentUser(null, 
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject userInfo) {
						User u = User.fromJson(userInfo);
						getActionBar().setTitle("@"+u.getScreenName());
						populateProfileHeader(u);

					}
				}
		);
	}
	
	private void loadUserProfileInfo(RequestParams rparams) {
		MyTwitterClientApp.getRestClient().getUser(rparams, 
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject userInfo) {
						User u = User.fromJson(userInfo);
						getActionBar().setTitle("@"+u.getScreenName());
						populateProfileHeader(u);

					}
				}
		);
	}
	
	private void populateProfileHeader(User u) {
		TextView tvName = (TextView)findViewById(R.id.tvName);
		TextView tvTagline = (TextView)findViewById(R.id.tvTagline);
		TextView tvFollowers = (TextView)findViewById(R.id.tvFollowers);
		TextView tvFollowing = (TextView)findViewById(R.id.tvFollowing);
		ImageView ivProfileImage = (ImageView)findViewById(R.id.ivMyProfileImage);
		tvName.setText(u.getUserName());
		tvTagline.setText(u.getTagline());
		tvFollowers.setText(u.getFollowersCount()+" Followers");
		tvFollowing.setText(u.getFriendsCount()+" Following");
		ImageLoader.getInstance().displayImage(u.getProfileImageUrl(), ivProfileImage);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}
	
	public String getScreenNameFromIntent () {
		//find screen name if possible
		String screenName = null;
		Intent i = getIntent();
		if(i != null && i.getExtras() != null){
			    	screenName = i.getStringExtra("screen_name");
		}
		return screenName;
	}
}
