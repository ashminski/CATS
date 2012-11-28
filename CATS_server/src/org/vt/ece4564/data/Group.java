package org.vt.ece4564.data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Unique;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.datanucleus.annotations.Unowned;

/**
 * This class models a group
 * @author Ashley
 *
 */
@PersistenceCapable
public class Group {

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	@Persistent
	@Unique
	private String groupName; //User-entered name for group
	
	@Persistent
	private String password; //password for group
	
	@Persistent
	private String createdBy; //User that created group - only one who can delete
	
	@Persistent
	@Unowned
	private Set<Key> members; //Users that belong to this group
	
	@Persistent
	private Set<Key> posts; //Posts in this group
	
	/**
	 * Constructor
	 * @param name Name of the group
	 * @param pass Password for group
	 * @param o user that created group
	 */
	public Group(String name, String pass) {
		groupName = name;
		password = pass;
		//owner = o;
		members = new HashSet<Key>();
		//members.add(o.getKey());
		posts = new HashSet<Key>();
	}
	
	/**
	 * Returns unique key for group
	 * @return
	 */
	public Key getKey(){
		return key;
	}
	
	/**
	 * Returns group password
	 * @return
	 */
	public String getPassword(){
		return password;
	}
	
	/**
	 * Returns group name
	 * @return
	 */
	public String getName(){
		return groupName;
	}
	
	/**
	 * Adds user as a member of this group
	 * @param u
	 */
	public void addUser(User u){
		members.add(u.getKey());
	}
	
	/**
	 * Sets user who created group
	 * @param name
	 */
	public void setCreatedBy(String name){
		createdBy = name;
	}
	
	/**
	 * Adds post to group
	 * @param p
	 */
	public void addPost(Post p){
		posts.add(p.getKey());
	}
	
	/**
	 * Returns keys to posts in this group
	 * @return
	 */
	public Set<Key> getPosts(){
		return posts;
	}

}
