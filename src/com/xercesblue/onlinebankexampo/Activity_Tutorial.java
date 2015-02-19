
package com.xercesblue.onlinebankexampo;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Activity_Tutorial extends Activity_Parent {
	private ArrayList<Object_QuesCategory> arrayOfQuesCatObj = null;
	//private Object_QuesCategory ob = null;
	private ArrayList<Object_TutorialTopics> listTutorialTopic=null;
	private ArrayList<Object_TutorialTopicDetails> listTutorialTopicDetail=null;

	private Boolean firstTime = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tutorial);
		setFooterAndHeader(R.id.imgBtnFooterTutorial,getResources().getString(R.string.header_tutorial));	
		createDynammicControls();
	}
	@Override
	protected void onResume() {
		super.onResume();

		if( Activity_PopUp_SelectExamCategory.selectedCategoryId != -1){
			showTutorialForCategory(Activity_PopUp_SelectExamCategory.selectedCategoryId);
			Activity_PopUp_SelectExamCategory.selectedCategoryId = -1;
		}
	}

	private void createDynammicControls(){
		DBHandler_QuestionCategory dbh = new DBHandler_QuestionCategory(this);

		firstTime = true;
		arrayOfQuesCatObj =dbh.getAllQuesCategories(true);
		System.out.print(arrayOfQuesCatObj);

		if(arrayOfQuesCatObj == null){
			Globals.showAlertDialogError(this);
			return;
		}

		if(arrayOfQuesCatObj.size()>0)
			showTutorialForCategory(arrayOfQuesCatObj.get(0).id);

	}

	
	private void showTutorialForCategory(int catId)
	{
		DBHandler_Tutorial db = new DBHandler_Tutorial(this);
		listTutorialTopic = db.getTutorialTopics(catId);
		
		LinearLayout llMain = (LinearLayout)findViewById(R.id.llytMainBodyTutorial);
		llMain.removeAllViews();

		LayoutParams txtParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		TextView tv1=new TextView(this);
		Object_QuesCategory objc=Globals.getQuesCatObj(catId,arrayOfQuesCatObj);
		if(objc!=null)
			tv1.setText(objc.name);
		tv1.setPadding(10, 10, 10, 10);
		tv1.setGravity(Gravity.CENTER);
		tv1.setBackgroundResource(R.drawable.bg_gradient_light_to_dark);
		tv1.setTextColor(getResources().getColor(R.color.app_white));
		tv1.setTextSize( Globals.getAppFontSize_Large(this));
		tv1.setLayoutParams(txtParams);

		llMain.addView(tv1);

         LayoutParams lt2 = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		
		int count=1;
		for( Object_TutorialTopics obj:listTutorialTopic)
		{

			TextView t1=new TextView(this);
			t1.setText(count+". "+ obj.TopicName);
			t1.setTextColor(getResources().getColor(R.color.app_green));
			t1.setTextSize( Globals.getAppFontSize_Large(this));
			DBHandler_Tutorial dbh =new DBHandler_Tutorial(this);
			listTutorialTopicDetail =dbh.getTutorialTopicDetails(obj.Id);
             lt2.topMargin=10;
			t1.setLayoutParams(lt2);
			llMain.addView(t1);
			count++;
			for(Object_TutorialTopicDetails obj1:listTutorialTopicDetail){
				TextView t2=new TextView(this);
				t2.setText(obj1.TopicDetailName);
				t2.setTextColor(getResources().getColor(R.color.app_white));
				t2.setTextSize( Globals.getAppFontSize(this));
                 lt2.leftMargin=20;

				t2.setLayoutParams(lt2);

				llMain.addView(t2); 
			}
		}
	}

	private void resizeImageButtons(){
		if(firstTime)
		{
			firstTime = false;
			RelativeLayout rlytHeader = (RelativeLayout)findViewById(R.id.rlytHeaderChild);
			ImageButton imgBtnPlus = (ImageButton)findViewById(R.id.imgBtnHeaderPlus);

			int headerImageXY = rlytHeader.getHeight() *5/10;
			imgBtnPlus.getLayoutParams().height = headerImageXY;
			imgBtnPlus.getLayoutParams().width = headerImageXY;

		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		resizeImageButtons();		
	}

	public void onClickPlus(View v){
		Intent i = new Intent(this,Activity_PopUp_SelectExamCategory.class);
		Activity_PopUp_SelectExamCategory.arrayQuesCat  = arrayOfQuesCatObj;
		startActivity(i);
	}
	
}
