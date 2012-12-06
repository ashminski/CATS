package edu.vt.ece4564.cats_app;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

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
		
		//TODO send request with group name
		String url = "http://chatallthestuff.appspot.com/group/posts?=" + groupName;
		SendRequestTask request = new SendRequestTask();
		request.execute(url);
		//TODO parse json from return, put into Post objects
		
		
		//TODO build list
		//MyAdapter adapter = new MyAdapter(this, listName);
		//postListView.setAdapter(adapter);
		
		//TODO onclick for new post button
		
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_show_posts, menu);
        return true;
    }
}
