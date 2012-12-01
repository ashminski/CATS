package edu.vt.ece4564.cats_app;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class SelectGroupActivity extends Activity implements OnClickListener {

	private ListView groupListView;
	private EditText groupNameBox;
	private EditText groupPassBox;
	private Button submitGroupButton;
	private Button createGroupButton;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_group);
        
        //TODO: should probably pass user name via intent
        
        groupListView = (ListView) findViewById(R.id.groupListView);
        groupNameBox = (EditText) findViewById(R.id.groupNameBox);
        groupPassBox = (EditText) findViewById(R.id.groupPassBox);
        submitGroupButton = (Button) findViewById(R.id.submitGroupButton);
        createGroupButton = (Button) findViewById(R.id.createGroupButton);
        
        //TODO populate groupListView with user's groups
        
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
