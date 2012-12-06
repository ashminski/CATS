package edu.vt.ece4564.cats_app;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class ShowPostsActivity extends Activity {

	private Button newPostButton;
	private String username;
	private Spinner groupListSpinner;
	private ListView postListView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_posts);
        
        newPostButton = (Button) findViewById(R.id.newPostButton);
        groupListSpinner = (Spinner) findViewById(R.id.groupListSpinner);
        postListView = (ListView) findViewById(R.id.postListView);
        
        Intent i = getIntent();
		username = i.getStringExtra("username");
		String groupName = i.getStringExtra("groupName");
		
		//TODO add spinner with other group names
		String url = "http://chatallthestuff.appspot.com/user/groups?username=" + username;
		SendRequestTask request = new SendRequestTask();
		request.execute(url);
		String result;
		try {
			result = request.get();
			JSONArray j = new JSONArray(result);
			List<String> groupNames = new ArrayList<String>();
			for(int k = 0; k < j.length(); k++){
				JSONObject jo = (JSONObject) j.get(k);
				groupNames.add(jo.getString("groupName"));
			}
			ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
					groupNames);
			groupListSpinner.setAdapter(spinAdapter);
		} catch (InterruptedException e1) {
			Toast toast = Toast.makeText(getApplicationContext(), 
					"Problem loading groups.",Toast.LENGTH_SHORT);
			toast.show();
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			Toast toast = Toast.makeText(getApplicationContext(), 
					"Problem loading groups.",Toast.LENGTH_SHORT);
			toast.show();
			e1.printStackTrace();
		} catch (JSONException e) {
			Toast toast = Toast.makeText(getApplicationContext(), 
					"Problem loading groups.",Toast.LENGTH_SHORT);
			toast.show();
			e.printStackTrace();
		}
		
		url = "http://chatallthestuff.appspot.com/group/posts?groupname=" + groupName;
		request = new SendRequestTask();
		request.execute(url);
		try {
			result = request.get();
			Log.i("posts", result);
			JSONArray j = new JSONArray(result);
			List<Post> posts = new ArrayList<Post>();
			for(int k = 0; k < j.length(); k++){
				JSONObject jo = (JSONObject) j.get(k);
				Post p = new Post(jo.getString("postedBy"), jo.getString("postBody"),
						jo.getLong("latitude"), jo.getLong("longitude"), jo.getString("postedAt"));
				posts.add(p);
			}
			
			MyAdapter adapter = new MyAdapter(this, posts);
			postListView.setAdapter(adapter);
			
		} catch (InterruptedException e) {
			Toast toast = Toast.makeText(getApplicationContext(), 
					"Problem loading posts.",Toast.LENGTH_SHORT);
			toast.show();
			e.printStackTrace();
		} catch (ExecutionException e) {
			Toast toast = Toast.makeText(getApplicationContext(), 
					"Problem loading posts.",Toast.LENGTH_SHORT);
			toast.show();
			e.printStackTrace();
		} catch (JSONException e) {
			Toast toast = Toast.makeText(getApplicationContext(), 
					"Problem loading posts.",Toast.LENGTH_SHORT);
			toast.show();
			e.printStackTrace();
		}
		
		//TODO onclick for new post button
		
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_show_posts, menu);
        return true;
    }
}
