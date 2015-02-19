package com.xercesblue.onlinebankexampo;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

public class Activity_Current_GK_Test extends Activity_Parent_Banner_Ads {


	private Object_Question currentQues = null;
	Boolean firstTime = true;
	private ProgressDialog mDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_current_gk_test);
		setFooterAndHeader(-1,getResources().getString(R.string.header_current_gk_test));
		//initializeControls();
		resizeImageButtons(true);	
		initCurrentTest();
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);

		if(firstTime)
		{	resizeImageButtons(false);	
			firstTime = false;
			getQuesFromServer(1);
		}
	}
	
	private void resizeImageButtons(Boolean hide){
		
		ImageButton imgBtnShowAns = (ImageButton)findViewById(R.id.imgBtnShowAns);
		ImageButton imgBtnNext = (ImageButton)findViewById(R.id.imgBtnNext);
		ImageButton imgBtnPrev = (ImageButton)findViewById(R.id.imgBtnPrev);
		ImageButton imgBtnStop = (ImageButton)findViewById(R.id.imgBtnStop);
		
		TextView txtShowAnswer = (TextView)findViewById(R.id.txtShowAns);
		if(hide){
			imgBtnShowAns.setVisibility(View.INVISIBLE);
			imgBtnNext.setVisibility(View.INVISIBLE);
			imgBtnPrev.setVisibility(View.INVISIBLE);
			imgBtnStop.setVisibility(View.INVISIBLE);
			txtShowAnswer.setVisibility(View.INVISIBLE);
		}else{
			imgBtnShowAns.setVisibility(View.VISIBLE);
			imgBtnNext.setVisibility(View.VISIBLE);
			imgBtnPrev.setVisibility(View.VISIBLE);
			imgBtnStop.setVisibility(View.VISIBLE);
			txtShowAnswer.setVisibility(View.VISIBLE);


			RelativeLayout rlytNextPrev = (RelativeLayout) findViewById(R.id.rlytNextPrev);

			int imageBtnSizeNextPrev = rlytNextPrev.getHeight() * 7 / 10;
			
			int imageBtnSizeOptions = rlytNextPrev.getHeight() *6/10;


			imgBtnShowAns.getLayoutParams().height = imageBtnSizeOptions;
			imgBtnShowAns.getLayoutParams().width = imageBtnSizeOptions;


			imgBtnNext.getLayoutParams().height = imageBtnSizeNextPrev;
			imgBtnNext.getLayoutParams().width = imageBtnSizeNextPrev;
			imgBtnPrev.getLayoutParams().height = imageBtnSizeNextPrev;
			imgBtnPrev.getLayoutParams().width = imageBtnSizeNextPrev;
			imgBtnStop.getLayoutParams().height = imageBtnSizeNextPrev;
			imgBtnStop.getLayoutParams().width = imageBtnSizeNextPrev;

		}

	}
	
	private void initCurrentTest(){


		firstTime = true;
		
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
				updateOptionSelection(pos);
				

			}
		});

	}
	
	private void updateOptionSelection(int pos){
		//option no is pos +1;
		if(currentQues != null){
			int selectedOptionNo = 0;
			if(pos>=0){
				Object_Options objOption = currentQues.arrayOptions.get(pos);
				selectedOptionNo = objOption.optionNo;
			}

			currentQues.optionSelected = selectedOptionNo;
			
			ListView lv = (ListView)findViewById(R.id.listOptions);
			int index = lv.getFirstVisiblePosition();
			View v = lv.getChildAt(0);
			int top = (v == null) ? 0 : v.getTop();
			
			setOptions();
			
			lv.setSelectionFromTop(index, top);
		}else{
			Globals.showAlertDialogOneButton("Error", "Some Error has occured, try again.",Activity_Current_GK_Test.this, "OK", null, true);
		}
	}
	
	public void getQuesFromServer(final int qNo)
	 {
		Custom_ConnectionDetector cd = new Custom_ConnectionDetector(
				this);
		
		if(!cd.isConnectingToInternet()){
			Globals.showAlertDialogError(this, "No Internet Connectivity");
			return;
		}
		DBHandler_Language dbH = new DBHandler_Language(this);
		Object_AppConfig appConfig = Globals.getAppConfig(this);

		String langCode = dbH.getLangCode(appConfig.selectedLangId);
	  
		String date = Activity_Current_GK_Type_Select.getSelectedDate();
		String month = Activity_Current_GK_Type_Select.getSelectedMonth();
		String year = Activity_Current_GK_Type_Select.getSelectedYear();
		
		mDialog = new ProgressDialog(this);
        mDialog.setMessage("Please wait...");
        mDialog.setCancelable(false);
        mDialog.show();
        
		final String url = ServerURL.getCurrent_GK_Test_link(qNo,langCode,date,month,year); 
		Log.i("HARSH", "URL IS -->" + url);
		
		// prepare the Request
		JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
		    new Response.Listener<JSONObject>() 
		    {
		        @Override
		        public void onResponse(JSONObject response) {   
		        	
		        	if(mDialog!= null){
		        		mDialog.dismiss();
		        	}
		        	
		            Log.i("HARSH","Response "+ response.toString());
		            
		            if(response.has("errorNoMoreQuestions")){
		            	
		            	try {
							Globals.showAlertDialogOneButton("Error ", response.getString("errorNoMoreQuestions"), Activity_Current_GK_Test.this, "OK", null, false);
							
		            	} catch (JSONException e) {
							e.printStackTrace();
						}
		            	
		            	return;
		            }else if(response.has("errorNoQuestions")){
		            	
		            	try {
		            		OnClickListener listnerPositive = new OnClickListener() {

		            			@Override
		            			public void onClick(DialogInterface dialog, int arg1) {
		            				dialog.dismiss();
		            				Activity_Current_GK_Test.this.finish();
		            			}
		            		};
		            		
							Globals.showAlertDialogOneButton("Error ", response.getString("errorNoQuestions"), Activity_Current_GK_Test.this, "OK", listnerPositive, false);
							
		            	} catch (JSONException e) {
							e.printStackTrace();
						}
		            	
		            	return;
		            }
		            
		            createQuestionObject(response,qNo);
		            		            
		        }
		    }, 
		    new Response.ErrorListener() 
		    {
		    	
		         @Override
		         public void onErrorResponse(VolleyError error) {    
		        	 if(mDialog!= null){
			        		mDialog.dismiss();
			        	}
		            //Log.i("HARSH","Error.Response"+ error.networkResponse.toString());
		            Globals.showAlertDialogOneButton("Error", "Some Error has occured, try again.",Activity_Current_GK_Test.this, "OK", null, true);
		       }
		    }
		);
		 
		// add it to the RequestQueue   
		Globals.getRequestQueue(this).add(getRequest);
	 }

	
	private void createQuestionObject(JSONObject response , int qNo){
		
		Object_Question ques = new Object_Question();
        try{
        	
        	ques.questionNo = qNo;
        	ques.question = response.getString("Question"); 
        	if(response.has("Solution"))
        		ques.solution = response.getString("Solution");
        	else
        		ques.solution ="";
        	ques.arrayOptions = new ArrayList<Object_Options>();
        	ques.correctOption = response.getInt("correctOption");
        
        	JSONArray arrayJson = response.getJSONArray("options");
        
        for (int i = 0; i < arrayJson.length(); i++) {
        	Object_Options obj = new Object_Options();
        	
        	obj.optionNo = i+1;
        	obj.optionText = arrayJson.getString(i);
        	
        	ques.arrayOptions.add(obj);
        }
         
        }catch(Exception ex){
       	 Log.i("HARSH","Exception in parsing Question JSON "+ response.toString());
       	 ques = null;
       	 Globals.showAlertDialogOneButton("Error", "Some Error has occured, try again.",this, "OK", null, true);
       	 return;
       }
				
        
        showQuestionOnView(ques);

		ImageButton imgBtnPrev = (ImageButton)findViewById(R.id.imgBtnPrev);
        if( qNo == 1){
			imgBtnPrev.setEnabled(false);
		}else{
			imgBtnPrev.setEnabled(true);
		}

	}
	
	private void showQuestionOnView(Object_Question ques){
		currentQues = ques;
		
		TextView txtQ = (TextView)findViewById(R.id.txtQuestion);
		txtQ.setText("Q."+ currentQues.questionNo+ "   "+currentQues.question.trim());
		
		ScrollView scrollQues = (ScrollView)findViewById(R.id.scrolViewQuestion);
		scrollQues.smoothScrollTo(0, 0);
		
		ImageButton imgBtnShowAns = (ImageButton)findViewById(R.id.imgBtnShowAns);
		imgBtnShowAns.setSelected(false);
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
	
	public void onClickNext(View v){

		int qNo = 1;
		if(currentQues != null){
			qNo = currentQues.questionNo+1;
		}

		getQuesFromServer(qNo);
		
		
	}
	public void onClickPrev(View v){

		int qNo = 1;

		if(currentQues != null){
			qNo = currentQues.questionNo-1;
		}
		if(qNo > 0)
			getQuesFromServer(qNo);
		
		
		

	}
	
	@Override
	public void onBackPressed() {
		//do nothing
		onClickStop(null);
	}
	
	public void onClickShowAns(View v){
		//v.setSelected(!v.isSelected());
		if(currentQues != null){
			//DBHandler_Questions dbH = new DBHandler_Questions(this);
			//DBHandler_Language dbH2 = new DBHandler_Language(this);
			//dbH.insertNewQuestion(currentQues, dbH2.getLangMap());
			
			if(!currentQues.isAnsShown){
				currentQues.isAnsShown = true;
				v.setSelected(true);
				setOptions();
				
				if(currentQues.solution != null && !currentQues.solution.equals("")){
					Globals.showAlertDialogOneButton("Solution", currentQues.solution, this, "OK", null, false);
				}
			}
		}

	}
	
	public void onClickStop(View v){

		String header = "Quit Test!";
		
		ExamOver(header, "Stop Test & go back to previous screen?",false);
	}
	
	private void ExamOver(String title , String msg , final Boolean isTimeOver){
		
		OnClickListener listnerPositive = new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();
				Activity_Current_GK_Test.this.finish();
			}
		};
		OnClickListener listnerNegative = new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				
			}
		};
		Globals.showAlertDialog(title,msg, this, "YES", listnerPositive, "NO", listnerNegative, false);
	}
}
