package com.nicolatesser.geofencedemo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.util.Log;

public class LocationLoggerService {

	private static final String TAG = "LocationLoggerService";

	private static LocationLoggerService instance;

	private HttpClient httpclient;
	private HttpPost httppost;

	private static String currentActivity;

	public static LocationLoggerService getInstance() {
		if (instance == null)
			instance = new LocationLoggerService();
		return instance;
	}

	public LocationLoggerService() {
		httpclient = new DefaultHttpClient();
		httppost = new HttpPost(
				"https://docs.google.com/forms/d/1Lx2iVIn05mhAImSmruyHD4lHuyfRY9DS8zj3Itxh5QM/formResponse");

	}
	
	private class PostFormTask extends AsyncTask<String, Void, String> {
	    @Override
	    protected String doInBackground(String... params) {
	    	try {
				// Add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
				nameValuePairs
.add(new BasicNameValuePair("entry.713917350",
						params[0]));
				nameValuePairs.add(new BasicNameValuePair("entry.503469039",
						params[1]));
				nameValuePairs.add(new BasicNameValuePair("pageHistory", "0"));
				nameValuePairs.add(new BasicNameValuePair("draftResponse", "[]"));

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);
				currentActivity = params[1];
			// } catch (ClientProtocolException e) {
			// // nothing
			} catch (IOException e) {
				// nothing
			}

	    	return "OK";
	  }
	}

	

	public void writeActivity(String name, String activity) {
		
		Log.i(TAG, "Logging event " + activity);

		// if (!activity.equalsIgnoreCase(currentActivity)) {
		// PostFormTask task = new PostFormTask();
		// task.execute(new String[] { name, activity });
		// }
		
		
	}

}
