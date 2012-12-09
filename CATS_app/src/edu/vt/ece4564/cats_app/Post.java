package edu.vt.ece4564.cats_app;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.util.Log;

public class Post {

	private String groupName; //group this post was posted in
	private String username; //user that posted this post
	private String text; //body of post
	private double latitude; //latitude of user posting
	private double longitude; //longitude of user posting
	private Date datePosted; //time that post was written
	
	/**
	 * Constructor
	 * @param user username that posted this post
	 * @param post body of the post
	 * @param lat latitude posted at
	 * @param lon longitude posted at
	 */
	public Post(String user,String post, double lat, double lon, String date) {
		username = user;
		text = post;
		latitude = lat;
		longitude = lon;
		Date javaDate;
		try {
			javaDate = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH).parse(date);
			datePosted = javaDate;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 * Returns user that posted this post
	 * @return
	 */
	public String getPostedBy(){
		return username;
	}
	
	/**
	 * Returns location that was posted at
	 * @return
	 */
	public String getCoordinates(){
		return latitude + ", " + longitude;
	}
	
	/**
	 * Returns body of post
	 * @return
	 */
	public String getText(){
		return text;
	}
	
	/**
	 * Returns date posted
	 * @return
	 */
	public Date getDatePosted(){
		Log.i("date", String.valueOf(datePosted.getTime()));
		return datePosted;
	}

}
