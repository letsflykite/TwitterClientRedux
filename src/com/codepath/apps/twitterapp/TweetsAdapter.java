package com.codepath.apps.twitterapp;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterapp.models.Tweet;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetsAdapter extends ArrayAdapter<Tweet> {
	// constructor
	// getView
	public TweetsAdapter(Context context, List<Tweet> tweets) {
		super(context, 0, tweets);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.tweet_item, null);
		}

		Tweet tweet = getItem(position);

		ImageView imageView = (ImageView) view.findViewById(R.id.ivProfile);
		ImageLoader.getInstance().displayImage(
				tweet.getUserProfileImageUrl(), imageView);

		TextView nameView = (TextView) view.findViewById(R.id.tvName);
		String formattedName = "<b>" + tweet.getUserName() + "</b>"
				+ " <small><font color='#777777'>@"
				+ tweet.getUserScreenName() + "</font></small>";
		nameView.setText(Html.fromHtml(formattedName));

		TextView tvTweetBody = (TextView) view.findViewById(R.id.tvBody);
		tvTweetBody.setText(Html.fromHtml(tweet.getTweetBody()));

		TextView timeView = (TextView) view.findViewById(R.id.tvCreatedAt);
		// String time = String.valueOf(tweet.getCreatedAt());
		// try{
		// time = pT.format(tweet.getCreatedAt());
		// }catch(Exception e){
		// Log.d("DEBUG", "Created At: " + tweet.getCreatedAt());
		// }
		Date timeToDisplay = tweet.getDateCreatedAt();
		// public static CharSequence getRelativeDateTimeString (Context c, long
		// time, long minResolution, long transitionResolution, int flags)

		String strRelativeTime = DateUtils.getRelativeDateTimeString(
				getContext(), // Suppose you are in an activity or other Context
								// subclass
				timeToDisplay.getTime(), // The time to display
				DateUtils.MINUTE_IN_MILLIS, // The resolution. This will display
											// only minutes
				// (no "3 seconds ago"
				DateUtils.WEEK_IN_MILLIS, // The maximum resolution at which the
											// time will switch
				// to default date instead of spans. This will not
				// display "3 weeks ago" but a full date instead
				0).toString(); // Eventual flags

		timeView.setText(strRelativeTime);
		return view;
	}

}
