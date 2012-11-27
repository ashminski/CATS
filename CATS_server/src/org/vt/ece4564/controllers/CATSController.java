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
import org.vt.ece4564.data.User;

import com.google.appengine.labs.repackaged.org.json.JSONArray;

@Controller
public class CATSController {

	public CATSController() {
		// TODO Auto-generated constructor stub
	}

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
	
	//return groups per user
	@RequestMapping("/user/groups")
	public void getUserGroups(@RequestParam("username") String user, HttpServletResponse response){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery("select from " + User.class.getName() + " where username==user");
		q.declareParameters("String user");
		List<User> users = (List<User>) q.execute(user);
		User u = users.get(0);
		//send back JSON string of groups
		JSONArray j = new JSONArray();
		//TODO: figure out how to get group name out of set of group keys
	}
	
	//validate group password
	@RequestMapping("/group/validate")
	public void validateGroupPass(@RequestParam("groupId") int id, @RequestParam("password") String pass,
			HttpServletResponse response) throws IOException{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery("select from " + Group.class.getName() + " where id==gid");
		q.declareParameters("String gid");
		List<Group> groups = (List<Group>) q.execute(id);
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
	
	//return posts in group
	
	//add new group
	
	//add new post
	
}
