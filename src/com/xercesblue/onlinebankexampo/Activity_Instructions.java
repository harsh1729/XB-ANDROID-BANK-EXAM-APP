package com.xercesblue.onlinebankexampo;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class Activity_Instructions extends Activity_Parent {

	static final String KEY_QUES_CAT = "QuesCat";

	private Object_QuesCategory quesCatObj = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_instructions);
		setFooterAndHeader(-1,getResources().getString(R.string.header_exam_instructions));
		initializeInstructions();
	}

	private void initializeInstructions(){
		quesCatObj = Activity_Select_Exam_Category.quesCategory; //(Object_QuesCategory) this.getIntent().getExtras().get(KEY_QUES_CAT);

		if(quesCatObj == null){
			Globals.showAlertDialogError(this);
			return;
		}
		
		int screenHeight = Globals.getScreenSize(this).y;
		
		String btnText = "Start Exam";
		if(Activity_Select_Exam_Category.isPractice)
		{	
			createInstructionsForPractice();
			btnText = "Start Practice";
		}else{
			createInstructionsForExam();
		}
		
		LinearLayout llMain = (LinearLayout)findViewById(R.id.llytButtonContainer);
		
		Button btn = new Button(this);			
		//btn.setBackgroundColor(this.getResources().getColor(R.color.app_green));
		btn.setBackgroundResource(R.drawable.bg_white_black_border);
		btn.setText(btnText);
		btn.setTextSize(  Globals.getAppFontSize(this));
		btn.setTextColor(this.getResources().getColor(R.color.app_white));
		btn.setTypeface(null, Typeface.BOLD);
		btn.setOnClickListener(btnClickListener);
		btn.setOnTouchListener(new Custom_ButtonOnTouchListener_GreenBG(btn, this));
		Point btnSize = Globals.getAppButtonSize(this);
		LayoutParams btnParams = new LayoutParams(btnSize.x,btnSize.y); //btnWidth,btnHeight );
		btnParams.gravity = Gravity.CENTER;
		btnParams.topMargin = screenHeight/10; // only for first button
		btnParams.bottomMargin = screenHeight/10;
		btnParams.leftMargin = 0;
		btnParams.rightMargin = 0;
		btn.setLayoutParams(btnParams);
		llMain.addView(btn);
	}

	OnClickListener btnClickListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			if(quesCatObj != null){

				DBHandler_ExamQuestions dbH = new DBHandler_ExamQuestions(Activity_Instructions.this);


				long noOfQues = dbH.questionsRemaining(quesCatObj.id,quesCatObj.isParentCategory);

				if(noOfQues >= quesCatObj.totalQues){
					navigateToQuestionView();
				}else{
					DialogInterface.OnClickListener listenerP = new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// Write your code here to execute after dialog closed
							dialog.cancel();
							navigateToQuestionView();
						}
					};
					DialogInterface.OnClickListener listenerN = new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// Write your code here to execute after dialog closed
							dialog.cancel();
							navigateToDownloadMore();
						}
					};
					
					String head ="Only "+noOfQues+" New Questions Left";// "Only "+noOfQues+" new questions left";
						if(noOfQues<1)
							head ="No New Questions Left";
					Globals.showAlertDialog(head, "You can download more questions from Settings-Download More .\nProceed to test with repeated questions ?", Activity_Instructions.this, "YES", listenerP, "NO", listenerN, false);
				}
			}

		}
	};

	private void createInstructionsForExam(){
		
		String instruction1 = "This exam consists of " + quesCatObj.totalQues+ " questions";
		String instruction2 = "Each correct answer carries " + quesCatObj.marksEachQues;
		if(quesCatObj.marksEachQues == 1){
			instruction2+=  " mark";
		}else{
			instruction2+=  " marks";
		}
		if(quesCatObj.negativeMarkEackQues >0){
			instruction2 += " and each wrong answer carries -"+quesCatObj.negativeMarkEackQues+" marks";
		}
		String instruction3 = "Total Duration of exams is ";
		Log.i("HARSH","hours -->"+quesCatObj.durationHours + " Mins "+quesCatObj.durationMins);
		int minutes = quesCatObj.durationMins;
		int hours = minutes/60 + quesCatObj.durationHours;
		minutes = minutes %60;
		
		if(hours > 0){
			instruction3+=hours + " hours ";
		}
		if(minutes > 0){
			instruction3+=minutes+ " minutes ";
		}

		String instruction4 = "Below is description of icons used in app";
		
		ArrayList<String>listOfInstructions = new ArrayList<String>();
		listOfInstructions.add(instruction1);
		listOfInstructions.add(instruction2);
		listOfInstructions.add(instruction3);
		listOfInstructions.add(instruction4);

		int[] arrayImageIds = new int[]{R.drawable.question_unseen,R.drawable.question_not_answered,R.drawable.question_answered,
				R.drawable.question_marked_selected,R.drawable.question_marked_answered,R.drawable.question_favorite_selected};
		String[] arrayImagesDescrition = new String[]{"You have not seen the question yet.","You have not answered the question.","You have answered the question.",
				"You have NOT answered the question but marked it for review.","You have answered the question and marked it for review.","You have added the question to your favorite list."};
		
		addInstructionDynamicControls(listOfInstructions, arrayImageIds, arrayImagesDescrition);
		
	}
	private void createInstructionsForPractice(){
		String instruction1 = "This practice set consists of " + quesCatObj.totalQues+ " questions.";
		String instruction2 = "Each correct answer carries " + quesCatObj.marksEachQues;
		if(quesCatObj.marksEachQues == 1){
			instruction2+=  " mark.";
		}else{
			instruction2+=  " marks.";
		}
		if(quesCatObj.negativeMarkEackQues >0){
			instruction2 += " and each wrong answer carries -"+quesCatObj.negativeMarkEackQues+" marks.";
		}
		
		

		String instruction4 = "Below is description of icons used in app";
		
		ArrayList<String>listOfInstructions = new ArrayList<String>();
		listOfInstructions.add(instruction1);
		listOfInstructions.add(instruction2);
		listOfInstructions.add(instruction4);

		
		int[] arrayImageIds = new int[]{R.drawable.question_get_ans_selected,
				R.drawable.question_favorite_selected};
		String[] arrayImagesDescrition = new String[]{"You have asked to show you correct answer","You have added the question to your favorite list."};
		
		addInstructionDynamicControls(listOfInstructions, arrayImageIds, arrayImagesDescrition);
	}
	
	private void addInstructionDynamicControls(ArrayList<String> listOfInstructions ,int[] arrayImageIds ,String[] arrayImagesDescrition){
		LinearLayout llMain = (LinearLayout)findViewById(R.id.llytMainBody);
		for(int cnt =0;cnt<listOfInstructions.size();cnt++){		
			String instruction = listOfInstructions.get(cnt);
			TextView txtView = new TextView(this);

			txtView.setText((cnt+1)+"."+instruction);
			txtView.setTextColor(getResources().getColor(R.color.app_black));
			//txtView.setTextSize( getResources().getDimension(R.dimen.txt_app_buttons_name_fontsize));
			txtView.setTextSize( Globals.getAppFontSize(this));
			LayoutParams txtParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			txtParams.setMargins(0, 0, 0, 0);
			txtView.setPadding(10, 5, 10, 5);
			txtView.setLayoutParams(txtParams);
			llMain.addView(txtView);	
		}
		
		for(int j =0 ;j <arrayImageIds.length;j++){
			LinearLayout llRow = new LinearLayout(this);
			llRow.setOrientation(LinearLayout.HORIZONTAL);
			LayoutParams lpRow = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			lpRow.setMargins(20, 5, 5, 5);
			llRow.setLayoutParams(lpRow);
			llMain.addView(llRow);
			
			int btnWidth = Globals.getScreenSize(this).x/10;
			
			ImageView imgV = new ImageView(this);
			LayoutParams lPImageView = new LayoutParams(btnWidth,btnWidth);
			imgV.setLayoutParams(lPImageView);
			imgV.setImageResource(arrayImageIds[j]);
			llRow.addView(imgV);
			
			TextView txtView = new TextView(this);

			txtView.setText(arrayImagesDescrition[j]);
			txtView.setTextColor(getResources().getColor(R.color.app_black));
			//txtView.setTextSize( getResources().getDimension(R.dimen.txt_app_buttons_name_fontsize));
			txtView.setTextSize( Globals.getAppFontSize(this));
			LayoutParams txtParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			txtParams.setMargins(5, 0, 0, 0);
			txtView.setLayoutParams(txtParams);
			llRow.addView(txtView);
		}
	}
	private void navigateToQuestionView(){
		DBHandler_Exam dbH = new DBHandler_Exam(this);
		
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR) ;
		String month = getTwoDigitString(c.get(Calendar.MONTH)+1);
		String day = getTwoDigitString(c.get(Calendar.DAY_OF_MONTH));
		String date = day + "/"+month+"/"+year;

		int lastExamID = dbH.getLastExamID();	
		
		Object_Exam objExam = new Object_Exam();
		if(!Activity_Select_Exam_Category.isPractice)
			objExam.examName = "Exam"+(lastExamID+1);
		else
			objExam.examName = "Practice"+(lastExamID+1);
			
		objExam.examDate = date;
		objExam.quesCategoryId = quesCatObj.id;
		objExam.totalQues = quesCatObj.totalQues;
		objExam.durationHours = quesCatObj.durationHours;
		objExam.durationMins = quesCatObj.durationMins;
		DBHandler_Language dbH2 = new DBHandler_Language(this);
		ArrayList<Object_Language>  listLang = dbH2.getLanguages(Globals.getAppConfig(this).selectedLangId);
		if(listLang.size() > 0){
			objExam.examLang = listLang.get(0).langName;
		}
		long newExamID = dbH.startNewExam(objExam);

		if(newExamID >=0){
			Intent i = new Intent(Activity_Instructions.this,Activity_QuestionView.class);	
			i.putExtra(Activity_QuestionView.KEY_EXAM_ID, newExamID);
			startActivity(i);
		}else{
			Globals.showAlertDialogError(this,"Some Error Occured");
		}
	}
	
	private void navigateToDownloadMore(){
		Intent i = new Intent(Activity_Instructions.this,Activity_Settings_DownloadMore.class);			
		startActivity(i);
	}
	
	public String getTwoDigitString(int val){
		if(val<10)
			return "0"+val;

		return val+"";
	}

}
