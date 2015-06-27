package com.xercesblue.onlinebankexampo;

import java.util.ArrayList;
import android.annotation.SuppressLint;
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

public class Activity_SelectLanguage extends Activity_Parent {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_language);
		setFooterAndHeader(-1,getResources().getString(R.string.header_select_languaae));
		createDynamicControls();
	}

	@SuppressLint("NewApi")
	private void createDynamicControls(){
		try{
			DBHandler_Language dbH = new DBHandler_Language(this);
			ArrayList<Object_Language> listLang = dbH.getLanguages(-1);
			
			int screenHeight = Globals.getScreenSize(this).y;
			
			Point btnSize = Globals.getAppButtonSize(this);
			LinearLayout llMain = (LinearLayout)findViewById(R.id.llytMainBody);

			TextView txtView = new TextView(this);
			
			txtView.setText("Select your preferred exam language");
			txtView.setTextColor(getResources().getColor(R.color.app_darkblue));
			txtView.setTextSize( Globals.getAppFontSize(this));
			LayoutParams txtParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			txtParams.setMargins(0, 0, 0, 0);
			txtParams.gravity = Gravity.CENTER;
			txtView.setGravity(Gravity.CENTER);
			txtView.setLayoutParams(txtParams);
			txtView.setPadding(0, 0, 0, 0);
			llMain.addView(txtView,0);
			
			for(int i = 0 ; i<listLang.size();i++){

				Object_Language objLang = listLang.get(i);		
				Button btn = new Button(this);
				
				btn.setBackgroundResource(R.drawable.bg_white_black_border);
				//btn.setBackgroundColor(this.getResources().getColor(R.color.app_green));
				btn.setText(objLang.langName);
				btn.setTextSize( Globals.getAppFontSize(this));
				btn.setTextColor(this.getResources().getColor(R.color.app_white));
				btn.setTypeface(null, Typeface.BOLD);
				btn.setPadding(0, 0, 0, 0);
				btn.setOnClickListener(btnClickListener);
				btn.setId(objLang.langId);
				btn.setOnTouchListener(new Custom_ButtonOnTouchListener_GreenBG(btn, this));
				LayoutParams btnParams = new LayoutParams(btnSize.x,btnSize.y );
				btnParams.gravity = Gravity.CENTER;
				if(i==0)
					btnParams.topMargin = screenHeight/10; // only for first button
				btnParams.bottomMargin = screenHeight/10;
				btnParams.leftMargin = 0;
				btnParams.rightMargin = 0; 
				btn.setLayoutParams(btnParams);
				llMain.addView(btn, i+1);
			}
		}catch(Exception ex){
			Globals.showAlertDialogError(this);
			
		}
	}

	OnClickListener btnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {

			DBHandler_AppConfig dbH = new DBHandler_AppConfig(Activity_SelectLanguage.this);
			dbH.setLanguage(v.getId());
			Intent i = new Intent(Activity_SelectLanguage.this, Activity_Home.class);
			startActivity(i);
			finish();

		}
	};
	
	
	/*private void createDynamicControls(){
		DBHandler_Language dbH = new DBHandler_Language(this);
		ArrayList<Object_Language> listLang = dbH.getLanguages();

		Globals gm = new Globals(this);
		
		int screenWidth = gm.getScreenSize().x;
		int screenHeight = gm.getScreenSize().y;
		
		int btnWidth = 3*screenWidth/10;
		LinearLayout llMain = (LinearLayout)findViewById(R.id.llytMainBody);

		for(int i = 0 ; i<listLang.size();i++){

			Object_Language objLang = listLang.get(i);		
			ImageButton imgBtn = new ImageButton(this);
			imgBtn.setBackgroundResource(0);
			Options options = new BitmapFactory.Options();
			options.inScaled = false;
			Bitmap bitImage = BitmapFactory.decodeResource(this.getResources(), R.drawable.btn_language_select, options);
			Bitmap scaledBitImage = gm.scaleToWidth(bitImage, btnWidth);
			scaledBitImage = gm.writeTextOnDrawable(scaledBitImage, objLang.langName,Color.BLACK);

			//Drawable drawable = new BitmapDrawable(this.getResources(),scaledBitImage);
			imgBtn.setImageBitmap(scaledBitImage);
			imgBtn.setScaleType(ScaleType.FIT_XY);
			imgBtn.setOnTouchListener(new Custom_ViewHighlightOnTouchListener(imgBtn,this));
			LayoutParams btnParams = new LayoutParams(scaledBitImage.getWidth()+20, scaledBitImage.getHeight()+20);
			//LayoutParams btnParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			btnParams.gravity = Gravity.CENTER;
			btnParams.topMargin = 10;
			btnParams.bottomMargin = 10;
			btnParams.leftMargin = 0;
			btnParams.rightMargin = 0;
			imgBtn.setId(objLang.langId);
			imgBtn.setLayoutParams(btnParams);
			imgBtn.setOnClickListener(btnClickListener);
			imgBtn.setPadding(10, 10, 10, 10);
			llMain.addView(imgBtn, i+1);
		}
	}
	*/
}
