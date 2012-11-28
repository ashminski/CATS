package org.vt.ece4564.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.datanucleus.annotations.Unowned;

/**
 * This class models a user
 * @author Ashley
 *
 */
@PersistenceCapable
public class User {

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	@Persistent
	private String username; //user chosen name
	
	@Persistent
	private String password; //user chosen password
	
	@Persistent
	private String phoneNumber; //user phone number, used for SMS messages
	
	@Persistent
	@Unowned
	private Set<Key> groups; //set of groups user is in
	
	//@Persistent(mappedBy = "user")
	//private List<Post> posts;
	
	
	public User(String name, String pass, String num) {
		username = name;
		password = pass;
		phoneNumber = num;
		groups = new HashSet<Key>();
		//posts = new ArrayList<Post>();
	}

	/**
	 * Returns unique key
	 * @return
	 */
	public Key getKey(){
		return key;
	}
	
	/**
	 * Returns user password
	 * @return
	 */
	public String getPassword(){
		return password;
	}
	
	public Set<Key> getGroups(){
		return groups;
	}
	
	public void addGroup(Group g){
		groups.add(g.getKey());
	}
	
	/**
	 * Returns username
	 * @return
	 */
	public String getUsername(){
		return username;
	}
}
