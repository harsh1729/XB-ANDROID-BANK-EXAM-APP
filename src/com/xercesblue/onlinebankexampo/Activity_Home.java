package com.xercesblue.onlinebankexampo;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class Activity_Home extends Activity_Parent {

	final int KEY_TAKE_TEST_CAT_ID = 4;
	final int KEY_TAKE_PRACTICE_ID = 1;
	final int KEY_EXAMALERT_ID = 3;
	final int KEY_FAVORITE_CAT_ID = 5;
	final int KEY_DOWNLOAD_MORE_ID = 50;
	final int KEY_CURRENT_GK_ID = 49;
	
	static String promotionalText = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		setFooterAndHeader(R.id.imgBtnFooterHome,getResources().getString(R.string.header_home));	
		createDynamicControls();
		Custom_ConnectionDetector cd = new Custom_ConnectionDetector(this);
		
		Boolean popUpShown = false;
		if(cd.isConnectingToInternet()){
			//if(Globals.current_version_code>Globals.getversion_code(this))
			//{
				//popUpShown = Custom_Versionchek.app_launched(this);
			//}
			
			if(!popUpShown)
				popUpShown = Custom_AppRater.app_launched(this);
			
			if(!popUpShown){
				Custom_AppShare cas = new Custom_AppShare();
				cas.app_launched(this, savedInstanceState);
			}
			
			if(!popUpShown)
				Custom_AppRegistration.app_launched(this);
		}
		
		Custom_AppEulaClass eula = new Custom_AppEulaClass(this);
		eula.show();
	}

	
	@SuppressLint("NewApi")
	private void createDynamicControls(){

		DBHandler_Category db = new DBHandler_Category(this);
		
		int width = Globals.getScreenSize(this).x;
		//int height = Globals.getScreenSize(this).y;
		
		int btnSpace = (int) (width/6);
		int btnWidth = (width - btnSpace*3)/2 ;
		Log.i("HARSH", "TotalWidth" + width);
		Log.i("HARSH", "IconWidth" + btnWidth);
		int btnPadding = Globals.getButtonsPadding(btnWidth);
		Log.i("HARSH", "IconPAdding" + btnPadding);
		//btnWidth = btnWidth - 2 *btnPadding;

		ArrayList<Object_Category> listCat = db.getCategories();
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
				
				Object_Category cat = listCat.get(i);		
				ImageButton imgBtn = new ImageButton(this);
				imgBtn.setBackgroundResource(0);
				Options options = new BitmapFactory.Options();
				options.inScaled = false;
				Bitmap bitImage = BitmapFactory.decodeByteArray(cat.iconImage, 0,
						cat.iconImage.length,options);
				Bitmap scaledBitImage = Globals.scaleToWidth(bitImage, btnWidth- (2 *btnPadding));
				//imgBtn.setImageBitmap(bitImage);
				Drawable drawable = new BitmapDrawable(this.getResources(),scaledBitImage);
				imgBtn.setImageDrawable(drawable);
				imgBtn.setOnTouchListener(new Custom_ViewOnTouchListener_WhiteBG(imgBtn,this));
				LayoutParams btnParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);//(btnWidth, btnWidth);
				imgBtn.setId(cat.id);
				imgBtn.setLayoutParams(btnParams);
				imgBtn.setOnClickListener(btnClickListener);
				imgBtn.setPadding(btnPadding, btnPadding, btnPadding, btnPadding);
				llRowItem.addView(imgBtn);
				
				TextView txtView = new TextView(this);
				System.out.println("Text is "+cat.name);
				txtView.setText(cat.name);
				txtView.setTextColor(getResources().getColor(R.color.app_black));
				//txtView.setTextSize( getResources().getDimension(R.dimen.txt_app_buttons_name_fontsize));
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
		
		TextView txtPromotoinal = (TextView)findViewById(R.id.txtPromotional);
		if(!promotionalText.isEmpty()){

			txtPromotoinal.setText(promotionalText);
			txtPromotoinal.setVisibility(View.VISIBLE);
		}else{
			txtPromotoinal.setText("");
			txtPromotoinal.setVisibility(View.GONE);
		}
	}

	OnClickListener btnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			Class<?> nextclass = getNextActivty(v.getId());
			if(nextclass != null){
				Intent i = new Intent(Activity_Home.this, nextclass);
				startActivity(i);
			}
			
		}
	};
	
	Class<?> getNextActivty(int id){
		Class<?> cls= null;
		switch (id) {
		case KEY_TAKE_TEST_CAT_ID:
			Activity_Select_Exam_Category.isPractice = false;
			cls = Activity_Select_Exam_Category.class;
			break;
		case KEY_TAKE_PRACTICE_ID:
			Activity_Select_Exam_Category.isPractice = true;
			cls = Activity_Select_Exam_Category.class;
			break;
		case KEY_EXAMALERT_ID:
			cls = Activity_ExamAlert.class;
			break;
		case KEY_FAVORITE_CAT_ID:
			cls = Activity_Favourites.class;
			break;
		case KEY_DOWNLOAD_MORE_ID:
			cls = Activity_Settings_DownloadMore.class;
			break;
		case KEY_CURRENT_GK_ID:
			cls = Activity_Current_GK_Type_Select.class;
			break;

		default:
			break;
		}
		return cls;
	}
	
	
}
