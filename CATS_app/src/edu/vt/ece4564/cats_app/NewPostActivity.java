package edu.vt.ece4564.cats_app;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class NewPostActivity extends Activity implements OnClickListener{

	private EditText postTextField;
	private CheckBox smsCheckBox;
	private Button submitPostButton;
	private String username;
	private String groupName;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        
        Intent i = getIntent();
        username = i.getStringExtra("username");
        groupName = i.getStringExtra("groupName");
        
        postTextField = (EditText) findViewById(R.id.postTextField);
        smsCheckBox = (CheckBox) findViewById(R.id.smsCheckBox);
        submitPostButton = (Button) findViewById(R.id.submitPostButton);
        
        submitPostButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_new_post, menu);
        return true;
    }

	@Override
	public void onClick(View v) {
		
		//TODO get fucking GPS shit... don't wanna do this
		
		// TODO send request to server to do post
		String postText = postTextField.getText().toString();
		
		
		// TODO send post via SMS to other numbers
	}
}
