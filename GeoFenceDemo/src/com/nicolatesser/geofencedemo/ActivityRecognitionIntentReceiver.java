package com.nicolatesser.geofencedemo;



import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

public class ActivityRecognitionIntentReceiver extends BroadcastReceiver {

	private final String[] mActivities = { "IN_VEHICLE", "ON_BICYCLE",
			"ON_FOOT", "STILL", "UNKNOWN", "TILTING" };

	private final int[] mImage_Ids = { R.drawable.car, R.drawable.cycle,
			R.drawable.walk, R.drawable.still, R.drawable.unknown,
			R.drawable.tilt };

	private final int mMaxRows = 200;

	@Override
	public void onReceive(Context context, Intent intent) {
		if (!intent.getAction().equals(
				LocationActivity.ACTION_ACTIVITY_RECOGNITION)) {
			Log.v(LocationActivity.TAG, "onReceive return" + intent.getAction());
			return;
		}

		int activity = intent.getIntExtra("activity_type", -1);
		Log.v(LocationActivity.TAG, "onReceive Activity_type == " + activity);

		if (activity == -1) {
			return;
		}

		// Add a new row to the table_layout. A row contains a text and an
		// icon for the corresponding activity both contained in a
		// horizontal linear_layout.
		LinearLayout linear_layout = new LinearLayout(context);
		linear_layout.setOrientation(LinearLayout.HORIZONTAL);

		Time now = new Time();
		now.setToNow();
		TextView txt = new TextView(context);
		txt.setText(now.format("%H:%M:%S") + " " + mActivities[activity] + " ");
		txt.setTextSize(16);
		txt.setPadding(10, 20, 20, 10);
		txt.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

		ImageView img = new ImageView(context);
		img.setImageResource(mImage_Ids[activity]);

		linear_layout.addView(txt);
		linear_layout.addView(img);
		TableLayout tableLayout = (TableLayout) ((Activity) context)
				.findViewById(R.id.tableLayout1);
		if (tableLayout.getChildCount() > mMaxRows) {
			tableLayout.removeAllViews();
		}
		tableLayout.addView(linear_layout, 0);

		LocationLoggerService.getInstance().writeActivity(DemoConstant.NAME,
				mActivities[activity]);

	}
}

