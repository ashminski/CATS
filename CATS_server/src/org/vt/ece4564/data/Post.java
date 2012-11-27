package org.vt.ece4564.data;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.google.appengine.api.datastore.GeoPt;

@PersistenceCapable
public class Post {

	@Persistent
	private Group group; //group this post was posted in
	
	@Persistent
	private User user; //user that posted this post
	
	@Persistent
	private String text; //body of post
	
	@Persistent
	private GeoPt GPSLocation; //location of user when posting
	
	public Post() {

	}

}
