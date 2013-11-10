package com.nicolatesser.geofencedemo;

import java.util.ArrayList;
import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;

public class GeoFenceIntentReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		if (!intent.getAction().equals(LocationActivity.ACTION_GEOFENCE)) {
			return;
		}

		boolean hasError = LocationClient.hasError(intent);

		if (hasError) {
			// This is an intent that indicates error.
			Log.v(LocationActivity.TAG, "hasError == true");
			return;
		}

		int transition = LocationClient.getGeofenceTransition(intent);
		List<Geofence> list = LocationClient.getTriggeringGeofences(intent);

		if (transition == -1 || list == null) {
			Log.v(LocationActivity.TAG, "list == null OR " + transition);
			return;
		}

		Log.v(LocationActivity.TAG, "geo_fence transition == " + transition);
		ArrayList<String> requestIds = new ArrayList<String>();

		for (Geofence geoFence : list) {
			requestIds.add(geoFence.getRequestId());
		}

		Bundle bundle = new Bundle();
		bundle.putStringArrayList("request_ids", requestIds);

		// Create a new intent and set extra arguments which contain the
		// request_ids of geofences triggered and corresponding transition.
		Intent myIntent = new Intent(context, LocationActivity.class);
		myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		myIntent.putExtra("RECEIVER_STARTED", true);
		myIntent.putExtra("geo_fences", bundle);
		myIntent.putExtra("transition", transition);

		if (LocationActivity.isAppForeground) {
			if (transition == Geofence.GEOFENCE_TRANSITION_ENTER) {
				LocationLoggerService.getInstance().writeActivity(
						DemoConstant.NAME, "Entered in Geofence");
			} else {
				LocationLoggerService.getInstance().writeActivity(
						DemoConstant.NAME, "Exited from Geofence");
			}
			context.startActivity(myIntent);
		} else {
			// Send a notification when the app is in the background
			if (transition == Geofence.GEOFENCE_TRANSITION_ENTER) {
				LocationLoggerService.getInstance().writeActivity(
						DemoConstant.NAME, "Entered in Geofence");
			} else {
				LocationLoggerService.getInstance().writeActivity(
						DemoConstant.NAME, "Exited from Geofence");
			}

			// Send a notification when the app is in the background
			String transitionText = transition == Geofence.GEOFENCE_TRANSITION_ENTER ? context
					.getResources().getString(R.string.enter) : context
					.getResources().getString(R.string.leave);

			transitionText = transitionText += " (" + requestIds.toString()
					+ ")";

			Bitmap genFenceBitmap = BitmapFactory.decodeResource(
					context.getResources(), R.drawable.geofence);

			Notification n = new NotificationCompat.Builder(context)
					.setContentTitle(
							context.getResources().getString(R.string.app_name))
					.setSmallIcon(R.drawable.ic_notify)
					.setDefaults(Notification.DEFAULT_SOUND)
					.setStyle(
							new NotificationCompat.BigPictureStyle()
									.bigPicture(genFenceBitmap).setSummaryText(
											transitionText))
					.setContentText(transitionText)
					.setContentIntent(
							PendingIntent.getActivity(context, 0, myIntent,
									PendingIntent.FLAG_UPDATE_CURRENT))
					.setAutoCancel(true).build();

			NotificationManager nm = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			nm.notify(1, n);

		}
	}
	

}

