package com.xercesblue.onlinebankexampo;

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

public class Custom_AppRegistration {

	private final static int LAUNCHES_UNTIL_PROMPT = 10;


	public static boolean app_launched(Context mContext) {
		Boolean returnStatus = false;
		SharedPreferences prefs = mContext.getSharedPreferences("appregister", 0);
		if (prefs.getBoolean("register_dontshowagain", false)) {
			return returnStatus;
		}

		SharedPreferences.Editor editor = prefs.edit();

		// Increment launch counter
		long launch_count = prefs.getLong("launch_count_register", 0) + 1;
		editor.putLong("launch_count_register", launch_count);

		

		// Wait at least n days before opening
		if (launch_count % Custom_AppShare.LAUNCHES_UNTIL_PROMPT != 0)
			if (launch_count % Custom_AppRater.LAUNCHES_UNTIL_PROMPT != 0)
				if (launch_count %LAUNCHES_UNTIL_PROMPT == 0) {
				
					showRegisterDialog(mContext, editor);
				returnStatus = true;
				}

		editor.commit();
		return returnStatus;
	}

	public static void showRegisterDialog(final Context mContext,
			final SharedPreferences.Editor editor) {
		final Dialog dialog = new Dialog(mContext);
		dialog.setTitle("Register With Us For Free" );

		LinearLayout ll = new LinearLayout(mContext);
		ll.setOrientation(LinearLayout.VERTICAL);

		TextView tv = new TextView(mContext);
		tv.setText("Dear User, you have been using this app for a while. If you like you can register with us and we will sent you free study material and monthly current affairs PDF");
		tv.setWidth(240);
		tv.setPadding(4, 0, 4, 10);
		ll.addView(tv);

		Button b1 = new Button(mContext);
		b1.setText("Register");
		b1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
				/*
				if (editor != null) {
					editor.putBoolean("register_dontshowagain", true);
					editor.commit();
				}
				*/
				if(mContext!=null){
					Intent i = new Intent(mContext, Activity_Settings_Registration.class);
					mContext.startActivity(i);
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
					editor.putBoolean("register_dontshowagain", true);
					editor.commit();
				}
				dialog.dismiss();
			}
		});
		ll.addView(b3);

		dialog.setContentView(ll);
		dialog.show();
	}
}

