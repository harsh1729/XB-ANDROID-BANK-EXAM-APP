package com.xercesblue.onlinebankexampo;

import java.util.ArrayList;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class Activity_Favourites extends Activity_Parent {

	private ArrayList<Object_QuesCategory> arrayOfQuesCatObj = null;
	private Boolean firstTime = true;
	private TextView txtTopicHeader;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favourites);
		setFooterAndHeader(-1,getResources().getString(R.string.header_favourites));	
		txtTopicHeader = (TextView)findViewById(R.id.txtTopicHeader);
		txtTopicHeader.setTextSize(Globals.getAppFontSize_Large(this));
		firstTime = true;
	}

	@Override
	protected void onResume() {
		super.onResume();

		if( Activity_PopUp_SelectExamCategory.selectedCategoryId != -1){
			populateFavouriteQuestionsList(Activity_PopUp_SelectExamCategory.selectedCategoryId);
			Activity_PopUp_SelectExamCategory.selectedCategoryId = -1;
		}

	}

	private void createDynammicControls(){
		DBHandler_QuestionCategory dbh = new DBHandler_QuestionCategory(this);

		arrayOfQuesCatObj =dbh.getAllQuesCategories(true);
		System.out.print(arrayOfQuesCatObj);

		if(arrayOfQuesCatObj == null){
			Globals.showAlertDialogError(this);
			return;
		}
		if(arrayOfQuesCatObj.size()>0)
			populateFavouriteQuestionsList(arrayOfQuesCatObj.get(0).id);

	}


	private void populateFavouriteQuestionsList(int catId){
		String childCatName = Globals.getChildCatName(catId, arrayOfQuesCatObj);
		
		txtTopicHeader.setText(childCatName);
		ListView lv = (ListView)findViewById(R.id.listQuestionsFourites);

		DBHandler_Questions dbH = new DBHandler_Questions(this);
		ArrayList<Object_Question> listQues = dbH.getFavouritesQuestions(catId);
		ArrayList<Object_Row_Item_Questions_List> listItem = new ArrayList<Object_Row_Item_Questions_List>();

		if(listQues.size() == 0){
			OnClickListener listnerPositive = new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int arg1) {
					dialog.dismiss();
				}
			};
			Globals.showAlertDialogOneButton("Alert", "No questions selected as favourite for "+childCatName, this, "OK", listnerPositive, true);
		}

		for(Object_Question objQ : listQues){
			listItem.add(getItem(objQ));
		}

		Custom_ArrayAdaptor_Favourites_Questions_List adp=new Custom_ArrayAdaptor_Favourites_Questions_List(this,R.layout.row_listview_questions_list,listItem); 
		lv.setAdapter(adp);

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos,
					long id) {
				Object_Row_Item_Questions_List item =(Object_Row_Item_Questions_List)adapter.getItemAtPosition(pos);
				navigateToQuestionDetail(item.quesId);
			}
		});
	}

	private Object_Row_Item_Questions_List getItem(Object_Question objQues){

		Object_Row_Item_Questions_List item=new Object_Row_Item_Questions_List();
		item.quesId = objQues.quesId;
		//item.quesNo = objQues.questionNo;
		//item.noOfOptions = objQues.arrayOptions.size();
		item.quesCatId = objQues.catId;
		//item.correctOption = objQues.correctOption;
		item.quesText = objQues.question;
		return item;
	}

	private void navigateToQuestionDetail(long qId){

		Activity_Favourite_Question_Detail.quesId = qId;
		Intent i = new Intent(this,Activity_Favourite_Question_Detail.class);
		startActivity(i);

	}

	private void resizeImageButtons(){

		RelativeLayout rlytHeader = (RelativeLayout)findViewById(R.id.rlytHeaderChild);
		ImageButton imgBtnPlus = (ImageButton)findViewById(R.id.imgBtnHeaderPlus);

		int headerImageXY = rlytHeader.getHeight() *7/10;
		imgBtnPlus.getLayoutParams().height = headerImageXY;
		imgBtnPlus.getLayoutParams().width = headerImageXY;


	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if(firstTime)
		{
			firstTime = false;			
			resizeImageButtons();	
			createDynammicControls();
		}
	}
	public void onClickPlus(View v){
		Intent i = new Intent(this,Activity_PopUp_SelectExamCategory.class);
		Activity_PopUp_SelectExamCategory.arrayQuesCat  = arrayOfQuesCatObj;
		startActivity(i);
	}
}
