package edu.vt.ece4564.cats_app;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class NewPostActivity extends Activity implements OnClickListener, LocationListener{

	private EditText postTextField;
	private CheckBox smsCheckBox;
	private Button submitPostButton;
	private String username;
	private String groupName;
	private LocationManager locManager;
	private Location lastLocation; //Stores previous location

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

		locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		String provider = locManager.getBestProvider(criteria, false);
		lastLocation = locManager.getLastKnownLocation(provider);

		submitPostButton.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_new_post, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		String lat;
		String lon;
		if(lastLocation == null){
			lat = "";
			lon = "";
		}
		else{
			try { 
				lat = URLEncoder.encode(String.valueOf(lastLocation.getLatitude()), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				lat = "";
				e.printStackTrace();
			}
			try {
				lon = URLEncoder.encode(String.valueOf(lastLocation.getLongitude()), "UTF-8");
				Log.i("lon", lon);
			} catch (UnsupportedEncodingException e) {
				lon = "";
				e.printStackTrace();
			}
		}

		String postText;
		try {
			postText = postTextField.getText().toString();
			String postTextEncoded = URLEncoder.encode(postText, "UTF-8");
			String url = "http://chatallthestuff.appspot.com/group/post?username=" + username +
					"&groupName=" + groupName + "&text=" + postTextEncoded + "&lat=" + lat + "&lon=" + lon;
			SendRequestTask request = new SendRequestTask();
			request.execute(url);
			Log.i("url", url);
			try {
				String result = request.get();
				if(!result.contains("Success")){
					Toast toast = Toast.makeText(getApplicationContext(), 
							"Something went wrong, try again!",Toast.LENGTH_SHORT);
					toast.show();
				}
				else{
					if(smsCheckBox.isChecked()){
						//Gets numbers in group
						url = "http://chatallthestuff.appspot.com/group/numbers?groupName=" + groupName;
						SendRequestTask request2 = new SendRequestTask();
						request2.execute(url);
						String numbersJson = request2.get();
						Log.i("wtfnumbers", numbersJson);
						JSONArray j = new JSONArray(numbersJson);
						List<String> numbers = new ArrayList<String>();
						for(int k = 0; k < j.length(); k++){
							JSONObject jo = (JSONObject) j.get(k);
							numbers.add(jo.getString("number"));
						}
						//Send text message to each number
						for(String num : numbers){
							SmsManager smsManager = SmsManager.getDefault();
							smsManager.sendTextMessage(num, null, "CATS! " + username + " has posted in "
									+ groupName + ": "+ postText, null, null);
						}
						
					}
					this.finish();
				}
			} catch (InterruptedException e) {
				Toast toast = Toast.makeText(getApplicationContext(), 
						"Something went wrong, try again!",Toast.LENGTH_SHORT);
				toast.show();
				e.printStackTrace();
			} catch (ExecutionException e) {
				Toast toast = Toast.makeText(getApplicationContext(), 
						"Something went wrong, try again!",Toast.LENGTH_SHORT);
				toast.show();
				e.printStackTrace();
			} catch (JSONException e) {
				Toast toast = Toast.makeText(getApplicationContext(), 
						"Something went wrong, try again!",Toast.LENGTH_SHORT);
				toast.show();
				e.printStackTrace();
			}
		} catch (UnsupportedEncodingException e) {
			Toast toast = Toast.makeText(getApplicationContext(), 
					"Something went wrong, try again!",Toast.LENGTH_SHORT);
			toast.show();
			e.printStackTrace();
		}	
	}

	@Override
	public void onLocationChanged(Location location) {
		lastLocation = location;
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}
}
