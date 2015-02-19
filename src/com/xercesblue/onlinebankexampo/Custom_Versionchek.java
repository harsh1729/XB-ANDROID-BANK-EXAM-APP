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

public class Custom_Versionchek {
	private final static String APP_PNAME = "com.xercesblue.onlinebankexampo";


	public static boolean app_launched(Context mContext) {
		
		Boolean returnStatus = false;
		SharedPreferences prefs = mContext.getSharedPreferences("appupdate", 0);
		if (prefs.getBoolean("update_dontshowagain", false)) {
			return returnStatus;
		}

		SharedPreferences.Editor editor = prefs.edit();

		// Increment launch counter
		
		// Wait at least n days before opening
	
		showVersionCheckDialog(mContext, editor);
		returnStatus = true;
		editor.commit();
		return returnStatus;
	}

	public static void showVersionCheckDialog(final Context mContext,
			final SharedPreferences.Editor editor) {
		final Dialog dialog = new Dialog(mContext);
		dialog.setTitle("Update Latest version of App" );

		LinearLayout ll = new LinearLayout(mContext);
		ll.setOrientation(LinearLayout.VERTICAL);

		TextView tv = new TextView(mContext);
		tv.setText("Dear User, you have been using this app for a while. If you like you can register with us and we will sent you free study material and monthly current affairs PDF");
		tv.setWidth(240);
		tv.setPadding(4, 0, 4, 10);
		ll.addView(tv);

		Button b1 = new Button(mContext);
		b1.setText("Update Your App");
		b1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri
						.parse("market://details?id=" + APP_PNAME)));
				dialog.dismiss();
				if (editor != null) {
					editor.putBoolean("update_dontshowagain", true);
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
					editor.putBoolean("update_dontshowagain", true);
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
