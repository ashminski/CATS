package org.vt.ece4564.data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

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
	private String groupName; //User-entered name for group
	
	@Persistent
	private String password; //password for group
	
	@Persistent
	private User owner; //User that created group - only one who can delete
	
	@Persistent
	private Set<Key> members; //Users that belong to this group
	
	@Persistent(mappedBy = "group")
	private List<Post> posts;
	
	/**
	 * Constructor
	 * @param name Name of the group
	 * @param pass Password for group
	 * @param o user that created group
	 */
	public Group(String name, String pass, User o) {
		groupName = name;
		password = pass;
		owner = o;
		members = new HashSet<Key>();
		members.add(o.getKey());
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

}
