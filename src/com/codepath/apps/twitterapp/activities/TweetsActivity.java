package com.codepath.apps.twitterapp.activities;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.twitterapp.DummyActivity;
import com.codepath.apps.twitterapp.R;
import com.codepath.apps.twitterapp.fragments.HomeTimelineFragment;
import com.codepath.apps.twitterapp.fragments.MentionsFragment;
import com.codepath.apps.twitterapp.fragments.TweetsListFragment;
import com.codepath.apps.twitterapp.models.Tweet;

public class TweetsActivity extends DummyActivity implements TabListener {
	// declare the views
	// ListView
	//

	// private TweetsAdapter tweetLvAdapter;
	// private MenuItem refreshItem;
	TweetsListFragment fragmentTweets;

	HomeTimelineFragment fragmentHtl = null;
	MentionsFragment fragmentMentions = null;
	int since_id = 0;
	int max_id = 0;
	SharedPreferences pref;
	String userScreenName;

//	private boolean pbShow;


//	private MenuItem refreshItem;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tweets);
		setupNavigationTabs();

		// fragmentTweets =
		// (TweetsListFragment)
		// getSupportFragmentManager().findFragmentById(R.id.fragmentTweets);
		pref = PreferenceManager.getDefaultSharedPreferences(this);
		userScreenName = pref.getString("userScreenName", "default");

		// addCurrentUserToActionBar();

	}

	private void setupNavigationTabs() {

		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);
		Tab tabHome = actionBar.newTab().setText("Home")
				.setTag("HomeTimelineFragment").setIcon(R.drawable.ic_home)
				.setTabListener(this);
		Tab tabMentions = actionBar.newTab().setText("Mentions")
				.setTag("MentionsTimelineFragment")
				.setIcon(R.drawable.ic_mentions).setTabListener(this);
		actionBar.addTab(tabHome);
		actionBar.addTab(tabMentions);
		actionBar.selectTab(tabHome);
	}

	// public void onProfileView (MenuItem mi) {
	// Intent i = new Intent(this, ProfileActivity.class);
	// startActivity(i);
	// }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.time_line, menu);
//		MenuItem mi = menu.findItem(R.id.menu_refresh);
//		mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		
//		menu.findItem(R.id.menu_refresh).getActionView().setVisibility(ProgressBar.INVISIBLE);
//		ProgressBar pb = (ProgressBar) (menu.findItem(R.id.menu_refresh).getActionView());
//		pb.setVisibility(ProgressBar.INVISIBLE);
		return true;
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// FragmentManager manager = getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction fts = getSupportFragmentManager()
				.beginTransaction();
		if (tab.getTag() == "HomeTimelineFragment") {
			// set the fragment in framelayout to home timeline
			fragmentHtl = new HomeTimelineFragment();
			fts.replace(R.id.frame_container, fragmentHtl);
		} else {
			// set the fragment in the frame layout to the mentions timeline
			fragmentMentions = new MentionsFragment();
			fts.replace(R.id.frame_container, fragmentMentions);

		}
		fts.commit();
		//invalidateOptionsMenu();	
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		/*
		case R.id.menu_refresh:
//			refreshItem = item;
//			refreshItem.setActionView(R.layout.action_refresh);
//			refreshItem.expandActionView();
		ActionBar actionBar = getActionBar();
		Tab selectedTab = actionBar.getSelectedTab();
		if ("HomeTimelineFragment".equals(selectedTab.getTag()
						.toString())) {
			invalidateOptionsMenu();	
			fragmentHtl.loadAPIData(0);
		}
		else {
			invalidateOptionsMenu();	
			fragmentMentions.loadAPIData(0);
		}
		// fragmentTweets =
		// (TweetsListFragment)
		// getSupportFragmentManager().findFragmentById(R.id.fragmentTweets);

		// refreshItem = item;
		// refreshItem.setActionView(R.layout.action_refresh);
		// refreshItem.expandActionView();
		// tweets.clear();
		// loadAPIData();
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
		// lvTweets.smoothScrollToPosition(0);

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
*/
		case R.id.menu_compose:
			Intent i = new Intent(getBaseContext(), ComposeActivity.class);
			startActivityForResult(i, ComposeActivity.COMPOSE_TWEET_ACTIVITY_ID);
			break;

		case R.id.menu_profile:
			Intent profile = new Intent(getBaseContext(), ProfileActivity.class);
			startActivity(profile);
			break;

		default:
			break;
		}
		return true;
	}

	/*
	 * private void addCurrentUserToActionBar() { // get current user
	 * userScreenName = pref.getString("userScreenName", "default");
	 * setTitle("@" + userScreenName);
	 * 
	 * // User currentUser;
	 * MyTwitterClientApp.getRestClient().getCurrentUser(null, new
	 * JsonHttpResponseHandler() {
	 * 
	 * @Override public void onSuccess(JSONObject userInfo) { User currentUser =
	 * User.fromJson(userInfo); if (currentUser != null) { // set action bar
	 * 
	 * userScreenName = currentUser.getScreenName(); setTitle("@" +
	 * userScreenName);
	 * 
	 * // //load up the pictures //
	 * ImageLoader.getInstance().displayImage(currentUser.getProfileImageUrl(),
	 * // ivMyProfilePicture);
	 * 
	 * // //show screen name // String formattedName = "<b>" + //
	 * currentUser.getName() + "</b>" + // " <small><font color='#777777'>@" +
	 * // currentUser.getScreenName() + "</font></small>"; //
	 * tvMyScreenName.setText(Html.fromHtml(formattedName)); Editor edit =
	 * pref.edit(); edit.putString("userScreenName", userScreenName);
	 * edit.commit(); } }
	 * 
	 * @Override public void onFailure(Throwable e, JSONObject errorResponse) {
	 * Log.d("DEBUG", "getUser API failure"); userScreenName =
	 * pref.getString("userScreenName", "default"); setTitle("@" +
	 * userScreenName); //load from database tweets =
	 * Tweet.loadAllTweetsFromDB(); } }); }
	 * 
	 * // find the views by Id in xml // public void setupViews() { // lvTweets
	 * = (ListView) findViewById(R.id.lvTweets); // }
	 */

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK
				&& requestCode == ComposeActivity.COMPOSE_TWEET_ACTIVITY_ID) {

			// htf.getTweetsByInvoction(GET.ON_REFRESH);
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
			if (newTweet != null) {
				ActionBar actionBar = getActionBar();
				Tab selectedTab = actionBar.getSelectedTab();
				if ("HomeTimelineFragment".equals(selectedTab.getTag()
						.toString())) {
					fragmentHtl.insertComposedTweet(newTweet);
					fragmentHtl.loadAPIData(0);
				} 
			}
		}
	}

//	@Override
//	public boolean onPrepareOptionsMenu (Menu menu) {
//
////		MenuItem mi = menu.findItem(R.id.menu_refresh);
////		mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
////		ProgressBar pb = (ProgressBar)refreshItem.getActionView();
////		pb.setVisibility(ProgressBar.VISIBLE);
////		
////		// on some click or some loading we need to wait for...
////		ProgressBar pb = (ProgressBar) findViewById(R.id.pbLoading);
////		pb.setVisibility(ProgressBar.VISIBLE);
////		// run a background job and once complete
////		pb.setVisibility(ProgressBar.INVISIBLE);
//		if (pbShow)
//			addRefreshMenuItem(menu);
//		else {
//			
//		}
//	    return true;
//	}
//	
//	public void setPbFlag() {
//		pbShow = true;
//	}
//	
//	public void resetPbFlag() {
//		pbShow = false;
//	}
	
//	private void addRefreshMenuItem(Menu menu){
//	    MenuItem mnu1 = menu.add(0, REFRESH_MENU_ITEM_ID, 0, "Refresh");
//	    {
//	    	mnu1.setActionView(R.layout.action_refresh);
//	    	mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//	    }
//	}
//	private void delRefreshMenuItem(Menu menu){
//	    MenuItem mnu1 = menu.findItem(REFRESH_MENU_ITEM_ID);
//	    if (mnu1!=null)
//	    	mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
//	}
}
