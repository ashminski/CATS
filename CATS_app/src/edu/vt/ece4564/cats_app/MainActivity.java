package edu.vt.ece4564.cats_app;

import java.util.concurrent.ExecutionException;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity implements OnClickListener {
	
	private EditText usernameField;
	private EditText passwordField;
	private EditText phoneField;
	private Button loginButton;
	private Button createAccountButton;
	private Button submitButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        usernameField = (EditText) findViewById(R.id.usernameField);
        passwordField = (EditText) findViewById(R.id.passwordField);
        phoneField = (EditText) findViewById(R.id.phoneField);
        loginButton = (Button) findViewById(R.id.loginButton);
        createAccountButton = (Button) findViewById(R.id.newAccountButton);
        submitButton = (Button) findViewById(R.id.submitButton);
        
        loginButton.setOnClickListener(this);
        createAccountButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	@Override
	public void onClick(View arg0) {
		if(arg0.getId() == R.id.loginButton){
			//validate login information, if good, then pass to next activity
			String username = usernameField.getText().toString();
			String password = passwordField.getText().toString();
			String url = "http://chatallthestuff.appspot.com/user/validate?username="
					+ username + "&pass=" + password;
			SendRequestTask request = new SendRequestTask();
			request.execute(url);
			try {
				String result = request.get();
				if(result.equals("Valid")){
					// TODO Send to next activity
				}
				else{
					// TODO Pop up dialog saying user is a tard
				}
			} catch (InterruptedException e) {
				// TODO Pop up dialog informing user of issue
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Pop up dialog informing user of issue
				e.printStackTrace();
			}
		}
		else if(arg0.getId() == R.id.newAccountButton){
			//prompt for phone number
			phoneField.setVisibility(View.VISIBLE);
			createAccountButton.setVisibility(View.GONE);
			submitButton.setVisibility(View.VISIBLE);
		}
		else if(arg0.getId() == R.id.submitButton){
			//save new account, go to next activity
			String username = usernameField.getText().toString();
			String password = passwordField.getText().toString();
			String number = phoneField.getText().toString();
			
			String url = "http://chatallthestuff.appspot.com/user/new?username=" +
					username + "&password=" + password + "&number=" + number;
			SendRequestTask request = new SendRequestTask();
			request.execute(url);
			try {
				String result = request.get();
				if(result.equals("Success")){
					// TODO Send to next activity
				}
				else{
					// TODO Pop up dialog informing user of issue
				}
			} catch (InterruptedException e) {
				// TODO Pop up dialog informing user of issue
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Pop up dialog informing user of issue
				e.printStackTrace();
			}
		}
	}
}
