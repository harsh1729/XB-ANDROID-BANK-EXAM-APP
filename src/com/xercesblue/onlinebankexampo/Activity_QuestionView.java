package com.xercesblue.onlinebankexampo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;



public class Activity_QuestionView extends Activity_Parent_Banner_Ads {

	private Object_QuesCategory quesCatObj = null;
	Boolean firstTime = false;
	///static Boolean repeat;
	private long examId;
	private Object_Question currentQues;
	//private int selectedOptionPos = -1;
	public static String KEY_EXAM_ID = "ExamId";
	private CountDownTimer timer;
	private TextView txtTimer;
	private Boolean isPractice;
	private ArrayList<Integer>arrayQuesNoAnsShown;
	long totalmillisec = 0;
	ScrollView scrollConatinerSolutions ;
	

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question_view);	
		
		isPractice = Activity_Select_Exam_Category.isPractice;
		scrollConatinerSolutions = (ScrollView)findViewById(R.id.scrolViewSolution);
		initializeControls();
		resizeImageButtons(true);	
		
		
		
	}

	@Override
	public void onBackPressed() {
		//do nothing
		onClickStop(null);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		if(!isPractice){
			startTimer();
			//Toast.makeText(getApplicationContext(), "Timer Started", Toast.LENGTH_SHORT).show();
		}
			

		if(Activity_PopUp_SelectExamCategory.selectedCategoryId != -1){
			moveToCategory(Activity_PopUp_SelectExamCategory.selectedCategoryId);
			Activity_PopUp_SelectExamCategory.selectedCategoryId = -1;
		}

		if(Activity_PopUp_QuestionsList.selectedQuestionNo > 0){
			setCurrentQuestionWithNumber(Activity_PopUp_QuestionsList.selectedQuestionNo);
			Activity_PopUp_QuestionsList.selectedQuestionNo = 0;
		}
		
		
	}

	@Override
	public void onPause() {
		
		if(timer != null){
			timer.cancel();
			timer = null;
			//Toast.makeText(getApplicationContext(), "Timer Stopped", Toast.LENGTH_SHORT).show();
		}
		super.onPause();
	}

	/** Called before the activity is destroyed. */
	@Override
	public void onDestroy() {
		
		if(timer != null){
			timer.cancel();
			timer = null;
		}
		super.onDestroy();
	}
	
	

	private void initializeControls(){

		firstTime = true;
		quesCatObj =Activity_Select_Exam_Category.quesCategory;//(Object_QuesCategory) this.getIntent().getExtras().get(Activity_Instructions.KEY_QUES_CAT);

		examId = this.getIntent().getLongExtra(KEY_EXAM_ID, -1);

		if(quesCatObj == null || examId == -1){
			Globals.showAlertDialogError(this);
			return;
		}

		TextView txtHeader = (TextView)findViewById(R.id.txtHeader);
		txtHeader.setText(quesCatObj.name);	
		TextView txtQ = (TextView)findViewById(R.id.txtQuestion);
	
		txtQ.setTextColor(getResources().getColor(R.color.app_white));
		txtQ.setTextSize( Globals.getAppFontSize(this));


		final ListView lv = (ListView)findViewById(R.id.listOptions);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int pos,
					long id) {

				if(currentQues.isAnsShown){
					return;
				}
				/*
				if(selectedOptionPos == pos){
					//view.setSelected(false);
					selectedOptionPos = -1;// in db it will enter 0 means deselect all
				}else{
					view.setSelected(true);
					selectedOptionPos = pos;

				}*/
				//updateOptionSelection(selectedOptionPos);
				updateOptionSelection(pos);
				/* No need to unselect it does automatically
				for(int i =0;i<lv.getChildCount();i++){
					//lv.getChildAt(i).setSelected(false);
				}*/

			}
		});


		txtTimer = (TextView)findViewById(R.id.txtTimer);
		if(isPractice){
			txtTimer.setVisibility(View.INVISIBLE);
			arrayQuesNoAnsShown = new ArrayList<Integer>();
		}else{
			txtTimer.setVisibility(View.VISIBLE);

			totalmillisec = ((quesCatObj.durationHours*60) +(quesCatObj.durationMins))*60*1000;	
		}

		
	}
	
	
	
	
	private void updateOptionSelection(int pos){
		//option no is pos +1;
		if(currentQues != null){
			int selectedOptionNo = 0;
			if(pos>=0){
				Object_Options objOption = currentQues.arrayOptions.get(pos);
				selectedOptionNo = objOption.optionNo;
			}

			DBHandler_ExamQuestions dbH = new DBHandler_ExamQuestions(this);
			dbH.updateOption(selectedOptionNo, currentQues.examQuestionId);
			currentQues.optionSelected = selectedOptionNo;
			
			ListView lv = (ListView)findViewById(R.id.listOptions);
			//Parcelable state = lv.onSaveInstanceState();
			int index = lv.getFirstVisiblePosition();
			View v = lv.getChildAt(0);
			int top = (v == null) ? 0 : v.getTop();
			
			setOptions();
			
			lv.setSelectionFromTop(index, top);
			//lv.onRestoreInstanceState(state);
		}else{
			Globals.showAlertDialogError(this);
		}
	}
	private void setQuestionView(){
		if(currentQues == null)
		{
			Globals.showAlertDialogError(this, "Error occured , Please start exam again.");
		}
		TextView txtQ = (TextView)findViewById(R.id.txtQuestion);
		ImageView imgQ = (ImageView)findViewById(R.id.imgViewQuestion);
		ImageButton imgBtnAttemptLater = (ImageButton)findViewById(R.id.imgBtnAttemptLater);
		ImageButton imgBtnFav = (ImageButton)findViewById(R.id.imgBtnFavorite);
		ImageButton imgBtnNext = (ImageButton)findViewById(R.id.imgBtnNext);
		ImageButton imgBtnPrev = (ImageButton)findViewById(R.id.imgBtnPrev);

		if(currentQues.questionNo == 1){
			imgBtnPrev.setEnabled(false);
		}else{
			imgBtnPrev.setEnabled(true);
		}

		if(currentQues.questionNo == quesCatObj.totalQues){
			imgBtnNext.setEnabled(false);
		}else{
			imgBtnNext.setEnabled(true);
		}
		txtQ.setText("Q."+ currentQues.questionNo+ "   "+currentQues.question.trim());
		if(currentQues.image != null){
			Bitmap bm = BitmapFactory.decodeByteArray(currentQues.image, 0,currentQues.image.length);
			imgQ.setImageBitmap(bm);
		}else{
			imgQ.setImageDrawable(null);
		}
		if(isPractice){
			imgBtnAttemptLater.setSelected(isAnsShown(currentQues.questionNo));
		}else{
			imgBtnAttemptLater.setSelected(Globals.getBoolFromInt(currentQues.attemptLater));
		}
		imgBtnFav.setSelected(Globals.getBoolFromInt(currentQues.isFav));

		if(quesCatObj.isParentCategory == 1){
			TextView txtHeader = (TextView)findViewById(R.id.txtHeader);				
			txtHeader.setText(Globals.getChildCatName(currentQues.catId,quesCatObj));
		}

		ScrollView scrollQues = (ScrollView)findViewById(R.id.scrolViewQuestion);
		scrollQues.smoothScrollTo(0, 0);
		setOptions();
	}


	private void setOptions(){
		
		ListView lv = (ListView)findViewById(R.id.listOptions);
		
		//DBHandler_Options dbh = new DBHandler_Options(this);

		ArrayList<Object_Row_Item_Options> data = new ArrayList<Object_Row_Item_Options>();

		//selectedOptionPos = -1;
		
		//int counter = 0;
		for(Object_Options obj:currentQues.arrayOptions)
		{
			Object_Row_Item_Options item=new Object_Row_Item_Options();
			item.text = obj.optionText.trim();
			if(obj.image!=null){
				Bitmap bit = BitmapFactory.decodeByteArray(obj.image, 0,
						obj.image.length);
				item.iconBitmap = bit;
			}else{
				item.iconBitmap = null;
			}
			
			item.isCorrect = false;
			item.isWrong = false;
			item.isSelected = false;

			if(currentQues.optionSelected == obj.optionNo){
				item.isSelected = true;
			}
			
			if(currentQues.correctOption == obj.optionNo){
				item.isCorrect = true;
			}else if(currentQues.optionSelected == obj.optionNo){
				item.isWrong = true;
			}

			data.add(item);
		}

		Custom_ArrayAdaptor_Options adp=new Custom_ArrayAdaptor_Options(this,R.layout.row_listview_options_question,data,currentQues.isAnsShown); 
		lv.setAdapter(adp);
		

	}
	private void resizeImageButtons(Boolean hide){

		RelativeLayout rlytHeader = (RelativeLayout)findViewById(R.id.rlytHeaderChild);
		ImageButton imgBtnPlus = (ImageButton)findViewById(R.id.imgBtnHeaderPlus);
		ImageButton imgBtnPopUp = (ImageButton)findViewById(R.id.imgBtnHeaderPopup);			
		ImageButton imgBtnAttemptLater = (ImageButton)findViewById(R.id.imgBtnAttemptLater);
		ImageButton imgBtnFav = (ImageButton)findViewById(R.id.imgBtnFavorite);
		ImageButton imgBtnNext = (ImageButton)findViewById(R.id.imgBtnNext);
		ImageButton imgBtnPrev = (ImageButton)findViewById(R.id.imgBtnPrev);
		ImageButton imgBtnStop = (ImageButton)findViewById(R.id.imgBtnStop);
		ImageButton imgBtnReport = (ImageButton)findViewById(R.id.imgBtnReport);
		
		TextView txtAttemptLater = (TextView)findViewById(R.id.txtAttemptLater);
		TextView txtFavorite = (TextView)findViewById(R.id.txtFavorites);
		if(hide){
			rlytHeader.setVisibility(View.INVISIBLE);
			imgBtnPlus.setVisibility(View.INVISIBLE);
			imgBtnPopUp.setVisibility(View.INVISIBLE);			
			imgBtnAttemptLater.setVisibility(View.INVISIBLE);
			imgBtnFav.setVisibility(View.INVISIBLE);
			imgBtnNext.setVisibility(View.INVISIBLE);
			imgBtnPrev.setVisibility(View.INVISIBLE);
			imgBtnStop.setVisibility(View.INVISIBLE);
			imgBtnReport.setVisibility(View.INVISIBLE);
			txtAttemptLater.setVisibility(View.INVISIBLE);
			txtFavorite.setVisibility(View.INVISIBLE);
		}else{
			rlytHeader.setVisibility(View.VISIBLE);
			imgBtnPlus.setVisibility(View.VISIBLE);
			imgBtnPopUp.setVisibility(View.VISIBLE);			
			imgBtnAttemptLater.setVisibility(View.VISIBLE);
			imgBtnFav.setVisibility(View.VISIBLE);
			imgBtnNext.setVisibility(View.VISIBLE);
			imgBtnPrev.setVisibility(View.VISIBLE);
			imgBtnStop.setVisibility(View.VISIBLE);
			imgBtnReport.setVisibility(View.VISIBLE);
			txtAttemptLater.setVisibility(View.VISIBLE);
			txtFavorite.setVisibility(View.VISIBLE);

			int headerImageXY = rlytHeader.getHeight() *5/10;

			if(quesCatObj.isParentCategory != 1 || isPractice){
				imgBtnPlus.setVisibility(View.GONE);
			}else{
				imgBtnPlus.setVisibility(View.VISIBLE);
				imgBtnPlus.getLayoutParams().height = headerImageXY;
				imgBtnPlus.getLayoutParams().width = headerImageXY;
			}



			RelativeLayout rlytNextPrev = (RelativeLayout)findViewById(R.id.rlytNextPrev);

			int imageBtnSizeNextPrev = rlytNextPrev.getHeight() *7/10;
			int imageBtnSizeOptions = rlytNextPrev.getHeight() *6/10;


			imgBtnFav.getLayoutParams().height = imageBtnSizeOptions;
			imgBtnFav.getLayoutParams().width = imageBtnSizeOptions;

			imgBtnAttemptLater.getLayoutParams().height = imageBtnSizeOptions;
			imgBtnAttemptLater.getLayoutParams().width = imageBtnSizeOptions;

			imgBtnReport.getLayoutParams().height = imageBtnSizeOptions;
			imgBtnReport.getLayoutParams().width = imageBtnSizeOptions;
			
			imgBtnNext.getLayoutParams().height = imageBtnSizeNextPrev;
			imgBtnNext.getLayoutParams().width = imageBtnSizeNextPrev;
			imgBtnPrev.getLayoutParams().height = imageBtnSizeNextPrev;
			imgBtnPrev.getLayoutParams().width = imageBtnSizeNextPrev;
			imgBtnStop.getLayoutParams().height = imageBtnSizeNextPrev;
			imgBtnStop.getLayoutParams().width = imageBtnSizeNextPrev;
			
			if( isPractice){
				imgBtnPopUp.setVisibility(View.GONE);
				//imgBtnAttemptLater.setVisibility(View.GONE);
				txtAttemptLater.setText("Get Ans");
				imgBtnAttemptLater.setImageResource(R.drawable.selector_question_get_ans);
			}else{
				imgBtnPopUp.setVisibility(View.VISIBLE);
				//imgBtnAttemptLater.setVisibility(View.VISIBLE);

				imgBtnPopUp.getLayoutParams().height = headerImageXY;
				imgBtnPopUp.getLayoutParams().width = headerImageXY;
				txtAttemptLater.setText("Marked");

			}

			onClickNext(null);
		}

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if(firstTime)
		{	resizeImageButtons(false);	
		firstTime = false;
		}


	}


	private Boolean isAnsShown(int qNo){
		for(Integer i : arrayQuesNoAnsShown){
			if(i.intValue() == qNo){
				return true;
			}
		}
		return false;
	}
	private void setCurrentQuestion(Object_Question objQ, int catId){
		if(objQ != null){		
			currentQues = objQ;
			if(isPractice)
				currentQues.isAnsShown = isAnsShown(currentQues.questionNo);
			
			setQuestionView();
			
		}else{
			//Globals.showAlertDialogError(this,"No more questions available, Download more from settings.");
			DialogInterface.OnClickListener listenerP = new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// Write your code here to execute after dialog closed
					dialog.cancel();
					navigateToSettings();
				}
			};
			DialogInterface.OnClickListener listenerN = new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// Write your code here to execute after dialog closed
					dialog.cancel();
				}
			};
			Globals.showAlertDialog("Not enough questions for exam", "Quit exam and download more?", this, "YES", listenerP, "NO", listenerN, false);
		}	
	}

	public void moveToCategory(int catId){
		int catBaseQuestion = getCatWiseBaseQuestionNo(catId);

		DBHandler_Questions dbH = new DBHandler_Questions(this);
		Object_Question objQ = dbH.getQuestionWithNumber(catBaseQuestion,examId); //getFirstQuestionInCategory(catId,examId);

		if(objQ == null){
			objQ = getNewQuestion(catId, catBaseQuestion);
		}

		setCurrentQuestion(objQ, catId);
	}

	private Object_Question getNewQuestion(int catId, int newQuestionNo){
		DBHandler_Questions dbH = new DBHandler_Questions(this);
		Object_Question objQ = dbH.getNewQuestion(catId, examId);
		if(objQ != null){
			objQ.questionNo = newQuestionNo;
			DBHandler_ExamQuestions dbH2 = new DBHandler_ExamQuestions(this);
			objQ.examQuestionId = dbH2.insertExamQuestion(examId, objQ.quesId,objQ.questionNo);
		}	

		return objQ;
	}
	

	private int getCatWiseBaseQuestionNo(int catId){
		int baseQuestionNo = 1;
		if( quesCatObj.isParentCategory==1 && quesCatObj.arrayChildrenCat != null)
			for(Object_QuesCategory objCat : quesCatObj.arrayChildrenCat)
			{

				if(objCat.id == catId){
					break;
				}
				baseQuestionNo+= objCat.totalQues;
			}
		return baseQuestionNo;
	}

	private int getCatIdForQuestionNo(int qNo){
		int id = 0;
		if(quesCatObj.isParentCategory == 0){
			id = quesCatObj.id;
		}else{
			//currentQues.questionNo 
			int toatlQ =0;
			if( quesCatObj.arrayChildrenCat != null)
				for(Object_QuesCategory objCat : quesCatObj.arrayChildrenCat)
				{
					toatlQ+= objCat.totalQues;
					if(currentQues == null){
						id = objCat.id;
						break;
					}else{

						if(qNo <= toatlQ){
							id = objCat.id;
							break;
						}
					}
				}
		}

		return id;
	}
	private void navigateToSettings(){
		Intent i = new Intent(this,Activity_Settings.class);			
		startActivity(i);
		this.finish();
	}
	private void setCurrentQuestionWithNumber(int quesNo){

		Object_Question objQ = null;
		if(currentQues!=null)
			if(currentQues.questionNo == quesNo)
				return;

		DBHandler_Questions dbH = new DBHandler_Questions(this);
		objQ = dbH.getQuestionWithNumber(quesNo, examId);

		int catId = getCatIdForQuestionNo(quesNo);

		if(objQ == null){

			objQ = getNewQuestion(catId, quesNo);	
		}

		setCurrentQuestion(objQ, catId);
		setSolution();
	}
	private void setSolution()
	{
		if(currentQues != null){
	
			if(currentQues.isAnsShown)
			{
				if(!currentQues.solution.equals(""))
				{
					
					TextView txtsol =(TextView)findViewById(R.id.txtSolution);
					txtsol.setTextSize(Globals.getAppFontSize(this));
					txtsol.setText("Solution :\n"+currentQues.solution);
					
					scrollConatinerSolutions.setVisibility(View.VISIBLE);
				}
				
			}
			else
			{
				scrollConatinerSolutions.setVisibility(View.GONE);
			}
			
		}
		
	}
	public void onClickNext(View v){
		
	
		int qNo = 1;
		if(currentQues != null){
			qNo = currentQues.questionNo+1;
		}

		if(qNo <= quesCatObj.totalQues)
		{
			setCurrentQuestionWithNumber(qNo);

		}
	
	
	}
	public void onClickPrev(View v){
		
		
	
		//scrollConatinerSolutions.setVisibility(View.VISIBLE);
		int qNo = 1;

		if(currentQues != null){
			qNo = currentQues.questionNo-1;
		}
		if(qNo > 0)
		{
			setCurrentQuestionWithNumber(qNo);
		}
		

	}
	public void onClickStop(View v){

		String header = "Quit Exam!";
		if(isPractice)
			header = "Quit Practice!";
		ExamOver(header, "Do you want to quit & see results ?",false);
	}
	public void onClickFav(View v){
		v.setSelected(!v.isSelected());
		if(currentQues != null){
			DBHandler_Questions dbH = new DBHandler_Questions(this);
			dbH.updateFav(v.isSelected(), currentQues.quesId);
		}

	}
	public void onClickAttempLater(View v){
		if(isPractice){
			if(!currentQues.isAnsShown){
				currentQues.isAnsShown = true;
				v.setSelected(true);
			
				setOptions();
				
				Boolean haveSolution =false;
				if(!currentQues.solution.equals(""))
				{
					TextView txtsol =(TextView)findViewById(R.id.txtSolution);
					txtsol.setTextSize(Globals.getAppFontSize(this));
					txtsol.setText("Solution :  \n"+currentQues.solution);
					haveSolution =true;
					
				}
				if(haveSolution)
					scrollConatinerSolutions.setVisibility(View.VISIBLE);
				else
					scrollConatinerSolutions.setVisibility(View.GONE);
				arrayQuesNoAnsShown.add(Integer.valueOf(currentQues.questionNo));
			}

		}else{
			v.setSelected(!v.isSelected());
			DBHandler_ExamQuestions dbH = new DBHandler_ExamQuestions(this);
			dbH.updateAttempLater(v.isSelected(), currentQues.examQuestionId);
		}
	}

	public void onClickPlus(View v){
		Intent i = new Intent(this,Activity_PopUp_SelectExamCategory.class);
		//if(quesCatObj.arrayChildrenCat != null ){
		//i.putExtra(Activity_SelectExamCategory.KEY_ARRAY_QUES_CAT, quesCatObj.arrayChildrenCat);
		//}
		Activity_PopUp_SelectExamCategory.arrayQuesCat = quesCatObj.arrayChildrenCat;
		startActivity(i);
	}

	public void onClickPopUp(View v){
		Intent i = new Intent(this,Activity_PopUp_QuestionsList.class);
		//if(quesCatObj != null){
		//i.putExtra(Activity_Instructions.KEY_QUES_CAT, quesCatObj);
		//}
		if(examId != -1){
			i.putExtra(KEY_EXAM_ID, examId);
		}
		startActivity(i);
	}

	
	public void onClickReport(View v){
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog closed
				
					
					String url = ServerURL.getBug_Report_Wrong_Question_link(Activity_QuestionView.this);
					StringRequest postRequest = new StringRequest(Request.Method.POST, url, 
					    new Response.Listener<String>() 
					    {
					        @Override
					        public void onResponse(String response) {
					            // response
					        	 Log.i("HARSH","Response "+ response.toString());
					        	 Toast.makeText(Activity_QuestionView.this, "Successfully Reported Question.", Toast.LENGTH_SHORT).show();
					        	 
					        }
					    }, 
					    new Response.ErrorListener() 
					    {
					         @Override
					         public void onErrorResponse(VolleyError error) {
					             // error
					             //Log.d("HARSH","Error Response "+ error.networkResponse.toString());
					             Toast.makeText(Activity_QuestionView.this, "Error while submission , please try again.", Toast.LENGTH_SHORT).show();
					       }
					    }
					) {     
					    @Override
					    protected Map<String, String> getParams() 
					    {  
					            Map<String, String>  params = new HashMap<String, String>();  
					            params.put("quesID", currentQues.quesId+"");  
					            params.put("gcmID", Globals.GCM_REG_ID);  
					            params.put("bugDetail", Globals.getAlertMessage()); 
					            
					            return params;  
					    }
					};
					Globals.getRequestQueue(Activity_QuestionView.this).add(postRequest);

				dialog.cancel();
			}
		};
		final EditText et = new EditText(this);
		
		Globals.showAlertDialogEditText("Wrong Question", "If you find any error in question or its options or given solution, please report this question. We will correct it in next update, Thanks for your support.", this, "REPORT", listener, "CANCEL", null, false);
	
	}
	private String getTimeString(long milliSecs){

		int seconds = (int) (milliSecs / 1000);
		int minutes = seconds / 60;
		int hours = minutes/60;
		minutes = minutes %60;
		seconds = seconds % 60;

		return getTwoDigit(hours)+":"+getTwoDigit(minutes)+":"+getTwoDigit(seconds);
	}
	private String getTwoDigit(int num){
		String s;
		if(num <10){
			s = "0"+num;
		}else{
			s = num+"";
		}

		return s;
	}



	private void ExamOver(String title , String msg , final Boolean isTimeOver){
		if(timer!= null ){
			timer.cancel();
			timer = null;
		}
		OnClickListener listnerPositive = new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();
				showResult();
			}
		};
		OnClickListener listnerNegative = new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if(isTimeOver)
					navigateToHome();
				else if(!isPractice)
					startTimer();
			}
		};
		Globals.showAlertDialog(title,msg, this, "YES", listnerPositive, "NO", listnerNegative, false);
	}

	private void navigateToHome(){
		Intent i = new Intent(this,Activity_Home.class);
		startActivity(i);
	}
	private void showResult(){
		DBHandler_Exam dbH = new DBHandler_Exam(this);
		ArrayList<Object_Exam> listExams = dbH.getExamsList(examId);
		if(listExams.size() >0){

			Intent i = new Intent(this, Activity_Results_Exam.class);
			Activity_Results_Exam.objExam = listExams.get(0);
			startActivity(i);
			this.finish();
		}else{
			Globals.showAlertDialogError(this);
		}

	}
	private void startTimer(){  
		timer = new CountDownTimer(totalmillisec, 1000) {

			public void onTick(long millisUntilFinished) {
				txtTimer.setText(getTimeString(millisUntilFinished));
				Activity_QuestionView.this.totalmillisec = millisUntilFinished;
				if((millisUntilFinished/1000) <=30) // 30 secs left
				{	
					txtTimer.setTextColor(Activity_QuestionView.this.getResources().getColor(R.color.app_red));
				}

			}

			public void onFinish() {
				txtTimer.setText("Time Up");
				txtTimer.setTextColor(Activity_QuestionView.this.getResources().getColor(R.color.app_green));
				ExamOver("Time Up",  "Do you want to see result ?",true);
			}
		};
		timer.start();

	}  
	
	
}
