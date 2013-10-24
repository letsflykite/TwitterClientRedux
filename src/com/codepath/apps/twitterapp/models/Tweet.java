package com.codepath.apps.twitterapp.models;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

@Table(name = "tweets")
public class Tweet extends Model implements Comparable<Tweet>, Serializable  {
	// Define table fields
	private static final long serialVersionUID = 5177222050535318633L;
	private static final String CREATED_AT_FORMAT ="EEE MMM dd HH:mm:ss ZZZZZ yyyy";
	SimpleDateFormat sdf = new SimpleDateFormat(CREATED_AT_FORMAT, Locale.US);
	
	@Column(name = "UserName")
    private String userName;
	
	@Column(name = "UserScreenName")
    private String userScreenName;
    
    @Column(name = "UserProfileImgUrl")
    private String userProfileImgUrl;

    @Column(name = "TweetBody")
    private String tweetBody;    
    
    @Column(name = "TweetCreatedTimeLong")
    private long createdTime;
    
    @Column(name = "TweetId")
    private long tweetId;
    
    @Column(name = "TweetCreatedAtStr")
    private String strCreatedAt;

    public Tweet() {
		super();
	}
	// Getters
	public String getUserName() {
		return this.userName;
	}
	public String getUserScreenName(){
		return this.userScreenName;
	}
    public String getTweetBody() {
    	return this.tweetBody;
//        return getString("text");
    }

    public long getTweetId() {
        return this.tweetId;
    }
    
    public String getUserProfileImageUrl() {
    	return this.userProfileImgUrl;
    }
    public Date getDateCreatedAt(){
    	Date createdDate = null;
    	sdf.setLenient(true);
    	try {
			createdDate = sdf.parse(this.strCreatedAt);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return createdDate;
    }
//    public boolean isFavorited() {
//        return getBoolean("favorited");
//    }
//
//    public boolean isRetweeted() {
//        return getBoolean("retweeted");
//    }

    public static Tweet fromJson(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        try {
            tweet.strCreatedAt = jsonObject.getString("created_at");
        	tweet.userName = jsonObject.getJSONObject("user").getString("name");
        	tweet.userScreenName = jsonObject.getJSONObject("user").getString("screen_name");
        	tweet.userProfileImgUrl = jsonObject.getJSONObject("user").getString("profile_image_url");
            tweet.tweetBody = jsonObject.getString("text");
            tweet.createdTime = tweet.getDateCreatedAt().getTime();
            tweet.tweetId = jsonObject.getLong("id");

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return tweet;
    }

    public static ArrayList<Tweet> fromJson(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());

        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject tweetJson = null;
            try {
                tweetJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Tweet tweet = Tweet.fromJson(tweetJson);
            if (tweet != null) {
                tweets.add(tweet);
            }
        }

        return tweets;
    }
    
    
//    private long getLong(String name) {
//        try {
//            return jsonObject.getLong(name);
//        } catch (JSONException e) {
//            e.printStackTrace();
//            return 0;
//        }
//    }
//
//    private boolean getBoolean(String name) {
//        try {
//            return jsonObject.getBoolean(name);
//        } catch (JSONException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

    @Override
	public int compareTo(Tweet another) {
		return (int) (another.createdTime - this.createdTime);
		//return (int) (another.tweetId - this.tweetId);
	}  
    
	// Record Finders
	public static Tweet byId(long id) {
	   return new Select().from(Tweet.class).where("id = ?", id).executeSingle();
	}
	
	public static ArrayList<Tweet> recentItems() {
      return new Select().from(Tweet.class).orderBy("id DESC").limit("300").execute();
	}
	
	public static ArrayList<Tweet> loadAllTweetsFromDB( ) {
		return new Select()
			.from(Tweet.class)
			.orderBy("TweetCreatedTimeLong DESC")
			.execute();
	}
	
	public static void dumpTweetsToDB(ArrayList<Tweet> tweets) {
		//if table exists, delete all records first
		//ActiveAndroid.getDatabase();
//		new Delete().from(Tweet.class).execute();
		//save to data base
		ActiveAndroid.beginTransaction();
		try {
		        for (Tweet tweet: tweets) {
		            tweet.save();
		        }
		        ActiveAndroid.setTransactionSuccessful();
		}
		finally {
		        ActiveAndroid.endTransaction();
		}
	}
}
