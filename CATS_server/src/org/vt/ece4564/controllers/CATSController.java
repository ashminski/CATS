package org.vt.ece4564.controllers;

import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.vt.ece4564.data.Group;
import org.vt.ece4564.data.Post;
import org.vt.ece4564.data.User;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

@Controller
public class CATSController {

	//validate user password
	@RequestMapping("/user/validate")
	public void validateUserPass(@RequestParam("username") String user, @RequestParam("pass") String password,
			HttpServletResponse response) throws IOException{

		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery("select from " + User.class.getName() + " where username==user");
		q.declareParameters("String user");
		List<User> users = (List<User>) q.execute(user);
		response.setContentType("text/plain");

		if(users == null || users.isEmpty()){
			//return invalid
			response.getWriter().write("Invalid");
		}
		else if(users.get(0).getPassword().equals(password)){
			//return valid
			response.getWriter().write("Valid");
		}
		else{
			//return invalid
			response.getWriter().write("Invalid");
		}
	}

	//create new account
	@RequestMapping("/user/new")
	public void createAccount(@RequestParam("username") String user, @RequestParam("password") String pass,
			@RequestParam("number") String num, HttpServletResponse response) throws IOException{

		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery("select from " + User.class.getName() + " where username==user");
		q.declareParameters("String user");
		List<User> users = (List<User>) q.execute(user);
		response.setContentType("text/plain");

		if(users == null || users.isEmpty()){
			User newUser = new User(user, pass, num);
			pm.makePersistent(newUser);
			response.getWriter().write("Success");
		}
		else{
			response.getWriter().write("Failure");
		}
	}

	//return groups per user
	@RequestMapping("/user/groups")
	public void getUserGroups(@RequestParam("username") String user, 
			HttpServletResponse response) throws JSONException, IOException{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery("select from " + User.class.getName() + " where username==user");
		q.declareParameters("String user");
		List<User> users = (List<User>) q.execute(user);
		User u = users.get(0);
		//send back JSON string of groups
		JSONArray j = new JSONArray();

		for(Key k : u.getGroups()){
			Group g = PMF.get().getPersistenceManager().getObjectById(Group.class, k);
			JSONObject jo = new JSONObject();
			Query qu = pm.newQuery("select from " + Post.class.getName() + " where groupName==gname");
			qu.declareParameters("String gname");
			qu.setOrdering("datePosted descending");
			List<Post> posts = (List<Post>) qu.execute(g.getName());
			jo.put("groupName", g.getName());
			if(posts != null && !posts.isEmpty() && posts.size() > 0){
				jo.put("newest", posts.get(0).getDatePosted().toString());
			}
			j.put(jo);
		}

		response.setContentType("application/json");
		response.getWriter().write(j.toString());
	}

	//validate group password
	@RequestMapping("/group/validate")
	public void validateGroupPass(@RequestParam("groupName") String name, @RequestParam("password") String pass,
			HttpServletResponse response) throws IOException{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery("select from " + Group.class.getName() + " where groupName==gname");
		q.declareParameters("String gname");
		List<Group> groups = (List<Group>) q.execute(name);
		response.setContentType("text/plain");

		if(groups == null || groups.isEmpty()){
			response.getWriter().write("Invalid");
		}
		else if(groups.get(0).getPassword().equals(pass)){
			response.getWriter().write("Valid");
		}
		else{
			response.getWriter().write("Invalid");
		}
	}

	//add user to group
	@RequestMapping("/group/add")
	public void addToGroup(@RequestParam("groupname") String groupName, @RequestParam("password") String pass,
			@RequestParam("username") String username, HttpServletResponse response) throws IOException{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery("select from " + Group.class.getName() + " where groupName==name");
		q.declareParameters("String name");
		q.setUnique(true);
		Group g = (Group) q.execute(groupName);
		response.setContentType("text/plain");

		if(g != null && g.getPassword().equals(pass)){
			q = pm.newQuery("select from " + User.class.getName() + " where username==user");
			q.declareParameters("String user");
			q.setUnique(true);
			User u = (User) q.execute(username);
			if(g.getMembers().contains(u.getKey())){
				response.getWriter().write("Duplicate");
				return;
			}
			u.addGroup(g);
			g.addUser(u);
			response.getWriter().write("Valid");
		}
		else{
			response.getWriter().write("Invalid");
		}
	}

	//return posts in group
	@RequestMapping("/group/posts")
	public void getPosts(@RequestParam("groupname") String name, HttpServletResponse response) 
			throws JSONException, IOException{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery("select from " + Group.class.getName() + " where groupName==gname");
		q.setUnique(true);
		q.declareParameters("String gname");
		Group g = (Group)q.execute(name);

		JSONArray j = new JSONArray();
		for(Key k : g.getPosts()){
			Post p = PMF.get().getPersistenceManager().getObjectById(Post.class, k);
			JSONObject jo = new JSONObject();
			jo.put("groupName", g.getName());
			jo.put("postedBy", p.getPostedBy());
			jo.put("latitude", p.getCoordinates().getLatitude());
			jo.put("longitude", p.getCoordinates().getLongitude());
			jo.put("postBody", p.getText());
			jo.put("postedAt", p.getDatePosted().toString());
			j.put(jo);
		}
		response.setContentType("application/json");
		response.getWriter().write(j.toString());
	}

	//add new group
	@RequestMapping("/group/new")
	public void createNewGroup(@RequestParam("groupname") String name, @RequestParam("password") String pass,
			@RequestParam("owner") String user, HttpServletResponse response) throws IOException{

		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery("select from " + Group.class.getName() + " where groupName==gname");
		q.declareParameters("String gname");
		List<Group> groups = (List<Group>) q.execute(name);
		if(groups == null || groups.isEmpty()){

			q = pm.newQuery("select from " + User.class.getName() + " where username==name");
			q.declareParameters("String name");
			q.setUnique(true);
			User u = (User) q.execute(user);
			Group g = new Group(name, pass);
			pm.makePersistent(g);
			u.addGroup(g);
			g.addUser(u);
			g.setCreatedBy(u.getUsername());

			response.setContentType("text/plain");
			response.getWriter().write("Success");
		}
		else{
			response.setContentType("text/plain");
			response.getWriter().write("Failure");
		}
	}

	//add new post
	@RequestMapping("/group/post")
	public void addPost(@RequestParam("username") String user, @RequestParam("groupName") String group,
			@RequestParam("text") String text, @RequestParam("lat") float lat, 
			@RequestParam("lon") float lon, HttpServletResponse response) throws IOException{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery("select from " + Group.class.getName() + " where groupName==gname");
		q.declareParameters("String gname");
		q.setUnique(true);
		Group g = (Group) q.execute(group);

		q = pm.newQuery("select from " + User.class.getName() + " where username==name");
		q.declareParameters("String name");
		q.setUnique(true);
		User u = (User) q.execute(user);

		Post p = new Post(u.getUsername(),text, lat, lon);
		pm.makePersistent(p);

		u.addPost(p);
		p.setGroup(g);
		g.addPost(p);

		response.setContentType("text/plain");
		response.getWriter().write("Success");
	}

	@RequestMapping("/group/numbers")
	public void getGroupNumbers(@RequestParam("groupName") String group, 
			HttpServletResponse response) throws JSONException, IOException{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery("select from " + Group.class.getName() + " where groupName==gname");
		q.declareParameters("String gname");
		q.setUnique(true);
		Group g = (Group) q.execute(group);

		JSONArray j = new JSONArray();
		for(Key k : g.getMembers()){
			User u = PMF.get().getPersistenceManager().getObjectById(User.class, k);
			JSONObject jo = new JSONObject();
			jo.put("name", u.getUsername());
			jo.put("number", u.getNumber());
			j.put(jo);
		}

		response.setContentType("application/json");
		response.getWriter().write(j.toString());
	}

	@RequestMapping("/group/newest")
	public void getMostRecentPost(@RequestParam("groupName") String group, 
			HttpServletResponse response) throws JSONException, IOException{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery("select from " + Post.class.getName() + " where groupName==gname");
		q.declareParameters("String gname");
		q.setOrdering("datePosted descending");
		List<Post> posts = (List<Post>) q.execute(group);
		JSONObject j = new JSONObject();
		for(Post p : posts){
			if(p.getGroupName().equals(group)){
				j.put("date", posts.get(0).getDatePosted().toString());
				j.put("text", posts.get(0).getText());
				break;
			}
		}

		response.setContentType("application/json");
		response.getWriter().write(j.toString());
	}

}
