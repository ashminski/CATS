package edu.vt.ece4564.cats_app;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreateGroupActivity extends Activity implements OnClickListener{

	private Button addNumberButton;
	private Button createGroupButton;
	private TextView newNumbersView;
	private EditText newGroupNameText;
	private EditText newPasswordText;
	private EditText newNumberText;

	private ArrayList<String> numbersToAdd = new ArrayList<String>();
	private String newGroupName = "";
	private String newGroupPassword = "";
	private String username;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_group);
		Intent i = getIntent();
		newGroupName = i.getStringExtra("gName");
		newGroupPassword = i.getStringExtra("gPassword");
		username = i.getStringExtra("uName");

		addNumberButton = (Button) findViewById(R.id.addPerson);
		createGroupButton = (Button) findViewById(R.id.createGroupButtonC);

		newNumbersView = (TextView) findViewById(R.id.numberView);

		newGroupNameText = (EditText) findViewById(R.id.newGroupName);
		newPasswordText = (EditText) findViewById(R.id.newPassword);
		newNumberText = (EditText) findViewById(R.id.newPhone);

		newGroupNameText.setText(newGroupName);
		newPasswordText.setText(newGroupPassword);

		addNumberButton.setOnClickListener(this);
		createGroupButton.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_create_group, menu);
		return true;
	}

	@Override
	public void onClick(View arg0) {
		if (arg0.getId() == R.id.addPerson) {
			String newNumber = newNumberText.getText().toString();
			if (numbersToAdd.contains(newNumber)) {
				Toast toast = Toast.makeText(getApplicationContext(), 
						"FOOL! You already added " + newNumber,Toast.LENGTH_SHORT);
				toast.show();
			}
			else if (newNumber.length() != 10) {
				Toast toast = Toast.makeText(getApplicationContext(), 
						"FOOL! " + newNumber + " is not 10 digits!",Toast.LENGTH_SHORT);
				toast.show();
			}
			else {
				numbersToAdd.add(newNumber);
			}
			String numbersToAddString = new String();
			for (int i = 0; i < numbersToAdd.size(); i++) {
				numbersToAddString += numbersToAdd.get(i) + "\n";
			}
			newNumbersView.setText(numbersToAddString);
			newNumberText.setText("");
		}
		else if (arg0.getId() == R.id.createGroupButtonC) {
			String newGroupName = newGroupNameText.getText().toString().trim().toLowerCase();
			if(!newGroupName.matches("^[a-z0-9]$")){
				Toast toast = Toast.makeText(getApplicationContext(), 
						"Please only use alphanumeric characters.",
						Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.TOP, 0, 10);
				toast.show();
				return;
			}
			String url;
			try {
				url = "http://chatallthestuff.appspot.com/group/new?groupname=" +
						newGroupName + "&password=" + URLEncoder.encode(newPasswordText.getText().toString(), "UTF-8")
						+ "&owner=" + username;

				SendRequestTask request = new SendRequestTask();
				request.execute(url);
				try {
					String result = request.get();
					Log.i("group creation", result);
					if(result.contains("Success")){
						Toast toast = Toast.makeText(getApplicationContext(), 
								"Your group has been created.",Toast.LENGTH_SHORT);
						toast.show();

						for (int i = 0; i < numbersToAdd.size(); i++) {
							SmsManager smsMan = SmsManager.getDefault();
							smsMan.sendTextMessage(numbersToAdd.get(i), null, 
									username + " has added you to the group " + newGroupNameText.getText().toString() + 
									" with the password " + newPasswordText.getText().toString()
									, null, null);
						}

						//Group has been created.
						Intent i = new Intent(this, ShowPostsActivity.class);
						i.putExtra("username", username);
						i.putExtra("groupName", newGroupNameText.getText().toString());
						startActivity(i);
					}
					else{
						Toast toast = Toast.makeText(getApplicationContext(), 
								"FOOL! Group name is alread in use.",Toast.LENGTH_LONG);
						toast.setGravity(Gravity.TOP, 0, 0);
						toast.show();
					}
				} catch (InterruptedException e) {
					Toast toast = Toast.makeText(getApplicationContext(), 
							"Something went wrong! Please try again.",Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.TOP, 0, 0);
					toast.show();
					e.printStackTrace();
				} catch (ExecutionException e) {
					Toast toast = Toast.makeText(getApplicationContext(), 
							"Something went wrong! Please try again.",Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.TOP, 0, 0);
					toast.show();
					e.printStackTrace();
				}
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
