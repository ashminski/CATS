package edu.vt.ece4564.cats_app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Shows the posts within the selected group
 * @author Ashley
 *
 */
public class ShowPostsActivity extends Activity implements OnClickListener, OnItemSelectedListener{

	private Button newPostButton;
	private String username;
	private String groupName;
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
		groupName = i.getStringExtra("groupName");
		
		groupListSpinner.setOnItemSelectedListener(this);
		
		//Populates spinner
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
			int pos = spinAdapter.getPosition(groupName);
			groupListSpinner.setSelection(pos); //selects group to load correct posts
		} catch (InterruptedException e1) {
			Toast toast = Toast.makeText(getApplicationContext(), 
					"Problem loading groups.",Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.TOP, 0, 100);
			toast.show();
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			Toast toast = Toast.makeText(getApplicationContext(), 
					"Problem loading groups.",Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.TOP, 0, 100);
			toast.show();
			e1.printStackTrace();
		} catch (JSONException e) {
			Toast toast = Toast.makeText(getApplicationContext(), 
					"Problem loading groups.",Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.TOP, 0, 100);
			toast.show();
			e.printStackTrace();
		}		
		
		newPostButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_show_posts, menu);
        return true;
    }

	@Override
	public void onClick(View arg0) { //Sends to new post screen
		Intent i = new Intent(this, NewPostActivity.class);
		i.putExtra("username", username);
		i.putExtra("groupName", groupName);
		startActivity(i);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		groupName = groupListSpinner.getSelectedItem().toString();
		String url = "http://chatallthestuff.appspot.com/group/posts?groupname=" + 
			groupListSpinner.getSelectedItem().toString();
		SendRequestTask request2 = new SendRequestTask();
		request2.execute(url);
		String result2;
		try {
			result2 = request2.get();
			Log.i("posts", result2);
			JSONArray j = new JSONArray(result2);
			List<Post> posts = new ArrayList<Post>();
			for(int k = 0; k < j.length(); k++){
				JSONObject jo = (JSONObject) j.get(k);
				Post p = new Post(jo.getString("postedBy"), jo.getString("postBody"),
						jo.getDouble("latitude"), jo.getDouble("longitude"), jo.getString("postedAt"));
				posts.add(p);
			}
			
			Collections.sort(posts, Collections.reverseOrder(new Comparator<Post>(){
				  public int compare(Post s1, Post s2) {
				    return s1.getDatePosted().compareTo(s2.getDatePosted());
				  }
				}));
			
			MyAdapter adapter = new MyAdapter(this, posts);
			postListView.setAdapter(adapter);
			
		} catch (InterruptedException e) {
			Toast toast = Toast.makeText(getApplicationContext(), 
					"Problem loading posts (InterruptedException e).",Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.TOP, 0, 100);
			toast.show();
			e.printStackTrace();
		} catch (ExecutionException e) {
			Toast toast = Toast.makeText(getApplicationContext(), 
					"Problem loading posts (ExecutionException e).",Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.TOP, 0, 100);
			toast.show();
			e.printStackTrace();
		} catch (JSONException e) {
			Toast toast = Toast.makeText(getApplicationContext(), 
					"Problem loading posts. (JSONException e)",Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.TOP, 0, 100);
			toast.show();
			e.printStackTrace();
		}
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}
