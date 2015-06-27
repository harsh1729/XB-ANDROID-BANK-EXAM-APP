package com.xercesblue.onlinebankexampo;

import java.util.ArrayList;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class Activity_Select_Exam_Category extends Activity_Parent {

	static Boolean isPractice = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_exam_type);
		setFooterAndHeader(-1,getResources().getString(R.string.header_exam_category));
		createDynamicControls();
		
	}

	public static Object_QuesCategory quesCategory = null;;
	
	
	@SuppressLint("NewApi")
	private void createDynamicControls(){

		DBHandler_QuestionCategory db = new DBHandler_QuestionCategory(this);

		int width = Globals.getScreenSize(this).x;
		int btnSpace = (int) (width/6);
		int btnWidth = (width - btnSpace*3)/2 ;
		int btnPadding = Globals.getButtonsPadding(btnWidth);
		//btnWidth = btnWidth - 2 *btnPadding;

		if(isPractice == null){
			isPractice = false;
		}
		ArrayList<Object_QuesCategory> listCat = db.getAllQuesCategories(isPractice);
		LinearLayout llMain = (LinearLayout)findViewById(R.id.llytMainBody);

		LinearLayout llRow = null;	
		LayoutParams llParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);		
		llParams.topMargin =3*btnSpace/4;

		for(int i = 0 ; i<listCat.size();i++){

			if(i%2 == 0){

				llRow = new LinearLayout(this);
				llRow.setOrientation(LinearLayout.HORIZONTAL);


				if(i == listCat.size()-1 || i == listCat.size()-2 ){
					llParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
					llParams.topMargin = 3*btnSpace/4;
					llParams.bottomMargin = 3*btnSpace/4;
				}

				llRow.setLayoutParams(llParams);
				llMain.addView(llRow);
			}

			if(llRow != null){
				LinearLayout llRowItem = new LinearLayout(this);
				llRowItem.setOrientation(LinearLayout.VERTICAL);
				//llRowItem.setBackgroundResource(android.R.color.darker_gray);
				LayoutParams llItemParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				llItemParams.leftMargin=btnSpace;
				llRowItem.setLayoutParams(llItemParams);
				llRow.addView(llRowItem);

				Object_QuesCategory cat = listCat.get(i);		
				ImageButton imgBtn = new ImageButton(this);
				//imgBtn.setBackgroundColor(getResources().getColor(android.R.color.transparent));
				imgBtn.setBackgroundResource(0);
				Options options = new BitmapFactory.Options();
				options.inScaled = false;
				Bitmap bitImage = BitmapFactory.decodeByteArray(cat.iconImage, 0,
						cat.iconImage.length,options);
				Bitmap scaledBitImage = Globals.scaleToWidth(bitImage, btnWidth - 2 *btnPadding);
				//imgBtn.setImageBitmap(bitImage);
				Drawable drawable = new BitmapDrawable(this.getResources(),scaledBitImage);
				imgBtn.setImageDrawable(drawable);
				imgBtn.setOnTouchListener(new Custom_ViewOnTouchListener_WhiteBG(imgBtn,this));
				LayoutParams btnParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);//(btnWidth, btnWidth);
				imgBtn.setId(cat.id);
				imgBtn.setLayoutParams(btnParams);
				imgBtn.setOnClickListener(btnClickListener);
				imgBtn.setPadding(btnPadding, btnPadding,btnPadding, btnPadding);
				llRowItem.addView(imgBtn);

				TextView txtView = new TextView(this);
				System.out.println("Text is "+cat.name);
				txtView.setText(cat.name);
				txtView.setTextColor(getResources().getColor(R.color.app_black));
				txtView.setTextSize( Globals.getAppFontSize(this));
				LayoutParams txtParams = new LayoutParams(btnWidth,LayoutParams.WRAP_CONTENT);
				//txtParams.leftMargin = btnPadding;
				//txtParams.rightMargin = btnPadding;
				txtView.setGravity(Gravity.CENTER);
				//txtView.setBackgroundResource(android.R.color.black);
				txtView.setPadding(0, 0, 0, 0);
				txtView.setLayoutParams(txtParams);
				llRowItem.addView(txtView);


			}
		}
	}

	OnClickListener btnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			DBHandler_QuestionCategory dbH = new DBHandler_QuestionCategory(Activity_Select_Exam_Category.this);

			//Object_QuesCategory obj = dbH.getQuesCategory(v.getId());
			quesCategory = dbH.getQuesCategory(v.getId());
			Intent i = new Intent(Activity_Select_Exam_Category.this, Activity_Instructions.class);
			//if(obj != null){
				//i.putExtra(Activity_Instructions.KEY_QUES_CAT, obj);
			//}
			startActivity(i);
		}
	};

}
