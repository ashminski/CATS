package edu.vt.ece4564.cats_app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class SelectGroupActivity extends Activity implements OnClickListener {

	private ListView groupListView;
	private EditText groupNameBox;
	private EditText groupPassBox;
	private Button submitGroupButton;
	private Button createGroupButton;
	private String username;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_group);
        
        Intent i = getIntent();
        username = i.getStringExtra("username");
        
        groupListView = (ListView) findViewById(R.id.groupListView);
        groupNameBox = (EditText) findViewById(R.id.groupNameBox);
        groupPassBox = (EditText) findViewById(R.id.groupPassBox);
        submitGroupButton = (Button) findViewById(R.id.submitGroupButton);
        createGroupButton = (Button) findViewById(R.id.createGroupButton);
        
        //TODO populate groupListView with user's groups
        String url = "http://chatallthestuff.appspot.com/user/groups?username=" + username;
        SendRequestTask request = new SendRequestTask();
        request.execute(url);
        try {
			String result = request.get();
			
			JSONArray j = new JSONArray(result);
			
			List<Map<String,String>> places = new ArrayList<Map<String,String>>();
			for(int k = 0; k < j.length(); k++){
				JSONObject jo = (JSONObject) j.get(k);
				Map<String,String> row = new HashMap<String, String>();
				row.put("id", jo.getString("groupName")); //Big text is group name
				places.add(row);
			}
			SimpleAdapter a = new SimpleAdapter(
					this,
					places,
					android.R.layout.simple_list_item_2,
					new String[] {"id", ""},
					new int[] {android.R.id.text1,android.R.id.text2});
			groupListView.setAdapter(a);
			//TODO Somewhere near here need to add onclicklistener for each listitem
			//On click, send to activity that displays posts in group
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_select_group, menu);
        return true;
    }

	@Override
	public void onClick(View arg0) {
		if(arg0.getId() == R.id.submitGroupButton){
			//TODO validate group name & pass
			//TODO send to group viewing activity for that group
		}
		else if(arg0.getId() == R.id.createGroupButton){
			// TODO send to create screen/dialog
		}
	}
}
