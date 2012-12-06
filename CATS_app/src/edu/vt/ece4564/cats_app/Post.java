package edu.vt.ece4564.cats_app;

public class Post {

	private String groupName; //group this post was posted in
	private String username; //user that posted this post
	private String text; //body of post
	private long latitude; //latitude of user posting
	private long longitude; //longitude of user posting
	private String datePosted; //time that post was written
	
	/**
	 * Constructor
	 * @param user username that posted this post
	 * @param post body of the post
	 * @param lat latitude posted at
	 * @param lon longitude posted at
	 */
	public Post(String user,String post, long lat, long lon, String date) {
		username = user;
		text = post;
		latitude = lat;
		longitude = lon;
		datePosted = date;
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
	public String getDatePosted(){
		return datePosted;
	}

}
