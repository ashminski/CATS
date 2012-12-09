package edu.vt.ece4564.cats_app;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.ProgressDialog;
import android.os.AsyncTask;

/**
 * This tasks executes a GET request for the given URL
 * @author Ashley
 *
 */
public class SendRequestTask extends AsyncTask<String, Void, String> {
	
	@Override
	protected String doInBackground(String... url) {
		HttpGet get = new HttpGet(url[0]);
		HttpClient client = new DefaultHttpClient();
		
		try {
			HttpResponse response = client.execute(get);
			String line = null;
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuilder sb = new StringBuilder();

			while((line = reader.readLine()) != null){
				sb.append(line + "\n");
			}
			
			return sb.toString();

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
