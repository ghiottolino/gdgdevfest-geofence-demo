package com.nicolatesser.geofencedemo;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

public class ActivityRecognitionIntentService extends IntentService {
	public ActivityRecognitionIntentService() {
		super("ActivityRecognitionIntentService");
	}

	public ActivityRecognitionIntentService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (intent.getAction() != LocationActivity.ACTION_ACTIVITY_RECOGNITION) {
			return;
		}

		if (ActivityRecognitionResult.hasResult(intent)) {
			ActivityRecognitionResult result = ActivityRecognitionResult
					.extractResult(intent);
			DetectedActivity detected_activity = result
					.getMostProbableActivity();
			int activity_type = detected_activity.getType();

			Log.v(LocationActivity.TAG, "activity_type == " + activity_type);

			// Put the activity_type as an intent extra and send a broadcast.
			Intent send_intent = new Intent(
					LocationActivity.ACTION_ACTIVITY_RECOGNITION);
			send_intent.putExtra("activity_type", activity_type);
			sendBroadcast(send_intent);
		}
	}
}

