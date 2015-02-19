package com.xercesblue.onlinebankexampo;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class Activity_Settings_ShareUs extends Activity_Parent {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings_share_us);
		
		setFooterAndHeader(-1,
				getResources().getString(R.string.header_share_app));
		createDynamicControls();
	}
	
	private void createDynamicControls() {

		LinearLayout l1 = (LinearLayout) findViewById(R.id.llytMainBodyDownload);
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		Point btnSize = Globals.getAppButtonSize(this);
		LayoutParams lpbtn = new LayoutParams(LayoutParams.WRAP_CONTENT, btnSize.y);// (btnWidth,
		// btnWidth)

		Point sceenSize = Globals.getScreenSize(this);
		TextView tv = new TextView(this);
		tv.setText("If you liked our efforts, please take a moment to share the link of this app with your friends");
		tv.setTextColor(this.getResources().getColor(R.color.app_white));
		tv.setTextSize(Globals.getAppFontSize(this));

		tv.setLayoutParams(lp);
		l1.addView(tv);
		
		lpbtn.gravity = Gravity.CENTER;
		lpbtn.topMargin = sceenSize.y / 10; // only for first button
		
		Button btnShare = new Button(this);
		btnShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				shareClick();
			}
		});
		btnShare.setText("Share App");
		btnShare.setTypeface(null, Typeface.BOLD);
		btnShare.setTextColor(this.getResources().getColor(R.color.app_black));
		btnShare.setLayoutParams(lpbtn);
		btnShare.setTextSize(Globals.getAppFontSize_Large(this));
		btnShare.setBackgroundResource(R.drawable.bg_white_black_border);
		btnShare.setOnTouchListener(new Custom_ButtonOnTouchListener_GreenBG(btnShare,
				this));
		l1.addView(btnShare);
		
	}

	public void shareClick(){
		
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, ServerURL.getShareAppMsg()+ "\n "+ServerURL.getShareLinkGeneric());
		//sendIntent.setPackage("com.whatsapp");
		sendIntent.setType("text/plain");
		startActivity(sendIntent);
	}
}
