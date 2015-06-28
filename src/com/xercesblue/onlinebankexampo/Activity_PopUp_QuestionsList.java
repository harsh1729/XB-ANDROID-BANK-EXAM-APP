package com.xercesblue.onlinebankexampo;

import java.util.ArrayList;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;


public class Activity_PopUp_QuestionsList extends Activity {

	private long examId;
	private Object_QuesCategory quesCatObj = null;
	private final int KEY_NO_OF_OPTIONS = 4;
	public static int selectedQuestionNo = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_popup_questions_list);	
		
	}
	@Override
	protected void onResume() {
		super.onResume();
		createQuestionsList();
	}
	
	private void createQuestionsList(){
		examId = this.getIntent().getLongExtra(Activity_QuestionView.KEY_EXAM_ID, -1);
		quesCatObj =Activity_Select_Exam_Category.quesCategory;//(Object_QuesCategory) this.getIntent().getExtras().get(Activity_Instructions.KEY_QUES_CAT);

		if(quesCatObj == null || examId == -1){
			Globals.showAlertDialogError(this);
			return;
		}

		setQuestionsArray();
	}


	private Object_Row_Item_Questions_List getItem(Object_Question objQues,int quesNo,int catId){

		Object_Row_Item_Questions_List item=new Object_Row_Item_Questions_List();
		if(objQues == null){
			item.quesNo = quesNo;
			item.attemptLater = 0;
			item.selectedOptionNo = 0;
			item.noOfOptions = KEY_NO_OF_OPTIONS;
			item.isSeen = false;
			item.quesCatId = catId;
		}else{

			item.quesNo = objQues.questionNo;
			item.attemptLater = objQues.attemptLater;
			item.selectedOptionNo = objQues.optionSelected;
			item.noOfOptions = objQues.arrayOptions.size();
			item.isSeen = true;
			item.quesCatId = objQues.catId;
		}

		return item;
	}

	private void addCategoryQuestions(int catId , ArrayList<Object_Row_Item_Questions_List> list ,Boolean isHidden){

		LinearLayout llMainBody = (LinearLayout)findViewById(R.id.llytMainBody);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.setMargins(0, 0, 0, 0);		

		Custom_Button_ListView_Container btn = new Custom_Button_ListView_Container(this,isHidden);
		btn.setLayoutParams(lp);
		btn.setPadding(10, 10, 10, 10);
		btn.setText(Globals.getChildCatName(catId, quesCatObj));
		btn.setTextColor(this.getResources().getColor(R.color.app_white));
		btn.setTypeface(null,Typeface.BOLD);
		btn.setBackgroundResource(R.drawable.bg_white_black_border);
		btn.setOnTouchListener(new Custom_ButtonOnTouchListener_GreenBG(btn, this));
		btn.setTextSize(Globals.getAppFontSize(this));
		//btn.quesCatId = catId;
		btn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View view) {
				Custom_Button_ListView_Container btnV = (Custom_Button_ListView_Container)view;
				if(btnV.isHidden){
					btnV.llytCatContainer.setVisibility(View.VISIBLE);
				}else{
					btnV.llytCatContainer.setVisibility(View.GONE);
				}
				btnV.isHidden = !btnV.isHidden;
			}
		});
		llMainBody.addView(btn);

		int containerWidth =  Globals.getScreenSize(this).x - 50  ; // 50 is padding
		int btnSpace = containerWidth/12;
		int btnWidth = (containerWidth - (KEY_NO_OF_OPTIONS+1)*btnSpace)/KEY_NO_OF_OPTIONS;
		
		LinearLayout llytContainer = new LinearLayout(this);
		llytContainer.setOrientation(LinearLayout.VERTICAL);
		llytContainer.setBackgroundResource(R.color.app_gray);
		llytContainer.setPadding(0, btnSpace/2, 0, 0);
		LayoutParams lpLinearLayout = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);		
		llytContainer.setLayoutParams(lpLinearLayout);
		llMainBody.addView(llytContainer);
		btn.llytCatContainer = llytContainer;
		int size = list.size();
		LinearLayout llytRow = null;
		
		for (int i = 0; i < size; i++) {
			Object_Row_Item_Questions_List item = list.get(i);
			LayoutParams lpBtnQ = new LayoutParams(btnWidth, btnWidth);
			lpBtnQ.setMargins(btnSpace, 0, 0, btnSpace/2);
			if(i%KEY_NO_OF_OPTIONS == 0){
				llytRow = new LinearLayout(this);
				llytRow.setOrientation(LinearLayout.HORIZONTAL);
				llytRow.setGravity(Gravity.CENTER_HORIZONTAL);
				llytRow.setLayoutParams(lpLinearLayout);
				llytContainer.addView(llytRow);
				lpBtnQ.setMargins(0, 0, 0, btnSpace/2);
			}
			
			
			Button btnQ = new Button(this);
			
			btnQ.setLayoutParams(lpBtnQ);
			//btnQ.setPadding(10, 10, 10, 10);
			btnQ.setText(item.quesNo+"");
			btnQ.setId(item.quesNo);
			btnQ.setPadding(0, 0, 0, 0);
			btnQ.setTypeface(null,Typeface.BOLD);
			if(item.isSeen)
			{
				
				if(item.attemptLater > 0 && item.selectedOptionNo>0){
					btnQ.setBackgroundResource(R.drawable.question_marked_answered);
				}else if(item.attemptLater > 0){
					btnQ.setBackgroundResource(R.drawable.question_marked_selected);
				}else if(item.selectedOptionNo>0){
					btnQ.setBackgroundResource(R.drawable.question_answered);
				}else{
					btnQ.setBackgroundResource(R.drawable.question_not_answered);
				}
			}else
			{
				btnQ.setBackgroundResource(R.drawable.question_unseen);
			}
			
			btnQ.setOnTouchListener(new Custom_ButtonOnTouchListener_WhiteText(btn, this));
			btnQ.setTextSize(Globals.getAppFontSize_Small(this));
			//btn.quesCatId = catId;
			btnQ.setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View view) {
					
					selectedQuestionNo = view.getId();
					Activity_PopUp_QuestionsList.this.finish();
				}
			});
			llytRow.addView(btnQ);
		}
		
		if(isHidden){
			llytContainer.setVisibility(View.GONE);
		}else{
			llytContainer.setVisibility(View.VISIBLE);
		}
		
		/*
		ListView lv = new ListView(this);		
		LayoutParams lpList = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lv.setLayoutParams(lpList);

		int btnWidth = Globals.getScreenSize(this).x/12;

		Custom_ArrayAdaptor_PopUp_Questions_List adp=new Custom_ArrayAdaptor_PopUp_Questions_List(this,R.layout.row_listview_questions_list,list,btnWidth); 
		lv.setAdapter(adp);
		lv.getLayoutParams().height = Globals.getHeightofListView(lv);
		lv.setVerticalScrollBarEnabled(false); 
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos,
					long id) {
				// TODO Auto-generated method stub
				Object_Row_Item_Questions_List item =(Object_Row_Item_Questions_List)adapter.getItemAtPosition(pos);
				selectedQuestionNo = item.quesNo;
				Activity_PopUp_QuestionsList.this.finish();
			}
		});
		llMainBody.addView(lv);
		btn.list = lv;

		if(isHidden){
			lv.setVisibility(View.GONE);
		}else{
			lv.setVisibility(View.VISIBLE);
		}
		
		*/

	}

	private Object_Question getQuestionFromList(int qNo , ArrayList<Object_Question> listExamQuestions){
		Object_Question objQues = null;
		
		for(Object_Question obj : listExamQuestions){
			if(obj.questionNo == qNo){
				objQues  = obj;
				break;
			}
		}
		return objQues;
		
	}
	private void setQuestionsArray(){

		DBHandler_Questions dbh = new DBHandler_Questions(this);
		ArrayList<Object_Question> listExamQuestions = dbh.getExamQuestions(examId);
		
		int questionNo =1;
		Boolean firstCat = true;
		if(quesCatObj.arrayChildrenCat != null)
		{
			for(Object_QuesCategory quesCat : quesCatObj.arrayChildrenCat){

				ArrayList<Object_Row_Item_Questions_List> listCategoryWise = new ArrayList<Object_Row_Item_Questions_List>();
				for(int catQuesCounter = 1;catQuesCounter <= quesCat.totalQues;catQuesCounter++,questionNo++){

					Object_Question objQues = getQuestionFromList(questionNo , listExamQuestions);	//dbh.getQuestionWithNumber(questionNo, examId);//
					listCategoryWise.add(getItem(objQues, questionNo, quesCat.id));

				}
				addCategoryQuestions(quesCat.id, listCategoryWise,!firstCat);
				if(firstCat)
					firstCat = false;
			}
		}

		else{
			ArrayList<Object_Row_Item_Questions_List> listCategoryWise = new ArrayList<Object_Row_Item_Questions_List>();
			for(int catQuesCounter = 1;catQuesCounter <= quesCatObj.totalQues;catQuesCounter++,questionNo++){

				Object_Question objQues =getQuestionFromList(questionNo , listExamQuestions); // dbh.getQuestionWithNumber(questionNo, examId);
				listCategoryWise.add(getItem(objQues, questionNo, quesCatObj.id));

			}
			addCategoryQuestions(quesCatObj.id, listCategoryWise,false);
		}

	}

}
