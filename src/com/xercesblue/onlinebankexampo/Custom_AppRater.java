package com.xercesblue.onlinebankexampo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class Custom_AppRater {

	private final static String APP_TITLE = "Online Bank Exam App";
	private final static String APP_PNAME = "com.xercesblue.onlinebankexampo";

	private final static int DAYS_UNTIL_PROMPT = 0;
	public final static int LAUNCHES_UNTIL_PROMPT = 3;

	public static boolean app_launched(Activity mContext) {
		Boolean returnStatus = false;
		
		SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
		if (prefs.getBoolean("dontshowagain", false)) {
			return returnStatus;
		}

		SharedPreferences.Editor editor = prefs.edit();

		// Increment launch counter
		long launch_count = prefs.getLong("launch_count", 0) + 1;
		editor.putLong("launch_count", launch_count);

		// Get date of first launch
		Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
		if (date_firstLaunch == 0) {
			date_firstLaunch = System.currentTimeMillis();
			editor.putLong("date_firstlaunch", date_firstLaunch);
		}

		// Wait at least n days before opening
		if (launch_count % Custom_AppShare.LAUNCHES_UNTIL_PROMPT != 0)
			if (launch_count %LAUNCHES_UNTIL_PROMPT == 0) {
				if (System.currentTimeMillis() >= date_firstLaunch
						+ (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
					showRateDialog(mContext, editor);
					returnStatus = true;
				}
			}

		editor.commit();
		
		return returnStatus;
	}

	@SuppressLint("NewApi")
	public static void showRateDialog(final Activity mContext,
			final SharedPreferences.Editor editor) {
		
		AlertDialog.Builder dialogBuilder;
		if (android.os.Build.VERSION.SDK_INT >= 11) 
		{
			dialogBuilder = new AlertDialog.Builder(
				 mContext ,AlertDialog.THEME_HOLO_LIGHT);
		}else{
			dialogBuilder = new AlertDialog.Builder(
					mContext);
		}
		
		dialogBuilder.setTitle("Rate " + APP_TITLE);
		
		final Dialog dialog = dialogBuilder.create();
		LinearLayout ll = new LinearLayout(mContext);
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setBackgroundResource(R.color.app_darkoffwhite);
		
		ll.setPadding(30, 20, 30, 20);

		TextView tv = new TextView(mContext);
		tv.setText("If you liked this app and our efforts, please take a moment to rate it five stars. Thanks for your support!");
		tv.setWidth(240);
		tv.setTextColor(mContext.getResources().getColor(R.color.app_darkblue));
		tv.setPadding(4, 0, 4, 10);
		ll.addView(tv);

		Button b1 = new Button(mContext);
		b1.setText("Rate this app");
		b1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri
						.parse("market://details?id=" + APP_PNAME)));
				dialog.dismiss();
				if (editor != null) {
					editor.putBoolean("dontshowagain", true);
					editor.commit();
				}
			}
		});
		ll.addView(b1);
		
		Button b2 = new Button(mContext);
		b2.setText("Remind me later");
		b2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		ll.addView(b2);

		Button b3 = new Button(mContext);
		b3.setText("No, thanks");
		b3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (editor != null) {
					editor.putBoolean("dontshowagain", true);
					editor.commit();
				}
				dialog.dismiss();
			}
		});
		ll.addView(b3);
		
		dialog.show();
		dialog.setContentView(ll);
		
	}
}
