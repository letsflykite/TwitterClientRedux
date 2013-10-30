package com.codepath.apps.twitterapp.utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.codepath.apps.twitterapp.models.Tweet;

public class Utils {

	public static boolean hasNetworkConnection(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null
				&& activeNetwork.isConnectedOrConnecting();

		return isConnected;
	}
	
	public static ArrayList<Tweet> removeDuplicates(ArrayList<Tweet> l) {
	    // ... the list is already populated
	    Set<Tweet> s = new TreeSet<Tweet>(new Comparator<Tweet>() {

	        @Override
	        public int compare(Tweet o1, Tweet o2) {
	            // ... compare the two object according to your requirements
	        	return (int)(o1.getTweetId()-o2.getTweetId());
	        }
	    });
	    s.addAll(l);
	    ArrayList<Tweet> res = new ArrayList<Tweet>(s);
	    return res;
	}
}
