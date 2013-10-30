package com.codepath.apps.twitterapp.models;

import java.util.ArrayList;

import org.json.JSONObject;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

public class User{
//	JSONObject jsonObject;
	String screenName;
	String userName;
	String profileImageUrl;
	private int followersCount;
	private int friendsCount;
	private String description;
	
	public User() {
		super();
	}
    public String getUserName() {
        return this.userName; 
    }

//    public long getUserId() {
//        return getLong("id");
//    }

    public String getScreenName() {
        return this.screenName;
    }
    
    public String getProfileImageUrl() {
        return this.profileImageUrl;
    }
//    public String getProfileBackgroundImageUrl() {
//        return getString("profile_background_image_url");
//    }

//    public int getNumTweets() {
//        return getInt("statuses_count");
//    }
//
    public int getFollowersCount() {
        return this.followersCount;
    }

    public int getFriendsCount() {
        return this.friendsCount;
    }

    public static User fromJson(JSONObject json) {
        User u = new User();

        try {
//            u.jsonObject = json;
            u.screenName = json.getString("screen_name");
            u.userName = json.getString("name");
            u.followersCount = json.getInt("followers_count");
            u.friendsCount = json.getInt("friends_count");
            u.description = json.getString("description");
            u.profileImageUrl = json.getString("profile_image_url");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return u;
    }
//    private String getString(String name) {
//        try {
//            return jsonObject.getString(name);
//        } catch (JSONException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
	public CharSequence getTagline() {
		// TODO Auto-generated method stub
		return this.description;
	}
    
//    private long getLong(String name) {
//        try {
//            return jsonObject.getLong(name);
//        } catch (JSONException e) {
//            e.printStackTrace();
//            return 0;
//        }
//    }
    
//    protected int getInt(String name) {
//        try {
//            return jsonObject.getInt(name);
//        } catch (JSONException e) {
//            e.printStackTrace();
//            return 0;
//        }
//    }

//	public static ArrayList<Tweet> loadAllTweetsFromDB( ) {
//		return new Select()
//			.from(Tweet.class)
//			.orderBy("TweetCreatedTimeLong DEC")
//			.execute();
//	}
//	
//	public static void dumpUserToDB(User user) {
//		//if table exists, delete all records first
//		//ActiveAndroid.getDatabase();
////		new Delete().from(Tweet.class).execute();
//		//save to data base
//		user.save();
//	}
}
