package com.xercesblue.onlinebankexampo;

import java.util.ArrayList;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class Activity_Settings_AboutApp extends Activity_Parent {

static ArrayList<String> arrayAboutApp=null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings_about_app);
		
		setFooterAndHeader(-1,getResources().getString(R.string.header_about_app));
		createDynamicControls();
	}
	private void createDynamicControls(){
		
		DBHandler_AboutApp dbh=new DBHandler_AboutApp(this);
		arrayAboutApp=dbh.getMessage();
		LinearLayout l1=(LinearLayout)findViewById(R.id.llytMainBodyAboutApp);
		LayoutParams lp=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.bottomMargin=5;
		for(String message : arrayAboutApp){
			
		
			
		TextView tv=new TextView(this);
		 tv.setText(message);
		 tv.setPadding(0, 10, 0, 10);
		tv.setTextSize(Globals.getAppFontSize_Large(this));
		tv.setTextColor(this.getResources().getColor(R.color.app_black));
		tv.setLayoutParams(lp);
		l1.addView(tv);
		
		
			
		}
		
	}
	
}
