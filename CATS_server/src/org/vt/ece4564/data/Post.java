package org.vt.ece4564.data;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Key;

/**
 * This class models a post
 * @author Ashley
 *
 */
@PersistenceCapable
public class Post {

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	@Persistent
	private String groupName; //group this post was posted in
	
	@Persistent
	private String username; //user that posted this post
	
	@Persistent
	private String text; //body of post
	
	@Persistent
	private GeoPt GPSLocation; //location of user when posting
	
	@Persistent
	private Date datePosted; //time that post was written
	
	/**
	 * Constructor
	 * @param user username that posted this post
	 * @param post body of the post
	 * @param lat latitude posted at
	 * @param lon longitude posted at
	 */
	public Post(String user,String post, float lat, float lon) {
		username = user;
		text = post;
		GPSLocation = new GeoPt(lat, lon);
		datePosted = new Date();
	}
	
	/**
	 * Returns key
	 * @return
	 */
	public Key getKey(){
		return key;
	}
	
	/**
	 * Sets group name that this post belongs to
	 * @param g
	 */
	public void setGroup(Group g){
		groupName = g.getName();
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
	public GeoPt getCoordinates(){
		return GPSLocation;
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
		return datePosted;
	}
	
	/**
	 * Returns group name
	 * @return
	 */
	public String getGroupName(){
		return groupName;
	}

}
