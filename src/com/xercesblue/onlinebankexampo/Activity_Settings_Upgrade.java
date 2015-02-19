package com.xercesblue.onlinebankexampo;

import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class Activity_Settings_Upgrade extends Activity_Parent {
	 Button btn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings_upgrade);
		//int width = Globals.getScreenSize(this).x;
		setFooterAndHeader(-1,getResources().getString(R.string.header_upgrade_app));
		
		 createDynamicControls();
	}
	private void createDynamicControls(){
		 
       LinearLayout l1=(LinearLayout)findViewById(R.id.llytMainBodyDownload);
       LayoutParams lp=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
       Point btnSize = Globals.getAppButtonSize(this);
       LayoutParams lpbtn=new LayoutParams(btnSize.x,btnSize.y);//(btnWidth, btnWidth)
       
       Point sceenSize = Globals.getScreenSize(this);
       TextView tv=new TextView(this);
       tv.setText("Annoyed with ads ?\nClick on below button to upgrade app with NO ADS and download unlimited number of questions for only Rs 10.\nAmount will be charged to your mobile service provider, no CARD required.");
       tv.setTextColor(this.getResources().getColor(R.color.app_white));
       tv.setTextSize( Globals.getAppFontSize(this));
     
       tv.setLayoutParams(lp);
       l1.addView(tv);
        btn=new Button(this);
       btn.setOnClickListener(new OnClickListener() {
    		 
           @Override
           public void onClick(View arg0) {
        	   UpgradeApp();
           }
       });
       btn.setText("Upgrade");
       btn.setTypeface(null, Typeface.BOLD);
       btn.setTextColor(this.getResources().getColor(R.color.app_black));
      
       lpbtn.gravity = Gravity.CENTER;
       lpbtn.topMargin = sceenSize.y/10; // only for first button
		
       
       btn.setLayoutParams(lpbtn);
       
       btn.setTextSize(Globals.getAppFontSize_Large(this));
       
       btn.setBackgroundResource(R.drawable.bg_white_black_border);
       btn.setOnTouchListener(new Custom_ButtonOnTouchListener_GreenBG(btn, this));
       l1.addView(btn);
      
   	
              
	}

	private void UpgradeApp(){
		
		Custom_ConnectionDetector cd = new Custom_ConnectionDetector(getApplicationContext());

		if(cd.isConnectingToInternet()){
			
		}
		else{
			Log.i("HARSH", "NO INTERNET");
			Globals.showAlertDialogOneButton("Alert", "No Internet connection", this, "OK", null, true);
		}
		
		
	}

}
