package edu.vt.ece4564.cats_app;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

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
			try { //TODO find way to encode decimal for URL
				lat = URLEncoder.encode(String.valueOf(lastLocation.getLatitude()), "UTF-8").replace(".", "%2E");
			} catch (UnsupportedEncodingException e) {
				lat = "";
				e.printStackTrace();
			}
			try {
				lon = URLEncoder.encode(String.valueOf(lastLocation.getLongitude()), "UTF-8").replace(".", "%2E");
				Log.i("lon", lon);
			} catch (UnsupportedEncodingException e) {
				lon = "";
				e.printStackTrace();
			}
		}
		
		// TODO send request to server to do post
		String postText;
		try {
			postText = URLEncoder.encode(postTextField.getText().toString(), "UTF-8").replace(".", "%2E");
			String url = "http://chatallthestuff.appspot.com/group/post?username=" + username +
					"&groupName=" + groupName + "&text=" + postText + "&lat=" + lat + "&lon=" + lon;
			SendRequestTask request = new SendRequestTask();
			request.execute(url);
			Log.i("url", url);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// TODO send post via SMS to other numbers
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
