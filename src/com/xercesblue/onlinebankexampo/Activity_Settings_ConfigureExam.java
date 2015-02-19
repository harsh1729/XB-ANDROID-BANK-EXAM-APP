package com.xercesblue.onlinebankexampo;

import java.util.ArrayList;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Activity_Settings_ConfigureExam extends Activity_Parent {
	ArrayList<Object_QuesCategory> arrayQuesCat = null;
	final int MAX_QUESTIONS = 100;
	final int MIN_QUESTIONS = 10;
	final int MAX_MINS = 59;
	final int MAX_HOURS = 3;
	int rowId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings_configure_exam);
		setFooterAndHeader(-1,getResources().getString(R.string.header_configure_exam));
		Button btn=(Button)findViewById(R.id.btnSave);
		Point btnSize = Globals.getAppButtonSize(this);
		btn.getLayoutParams().width = btnSize.x;
		btn.getLayoutParams().height = btnSize.y;
		btn.setTextSize( Globals.getAppFontSize_Large(this));
		btn.setOnTouchListener(new Custom_ButtonOnTouchListener_GreenBG(btn, this));
		DBHandler_QuestionCategory dbh=new DBHandler_QuestionCategory(this);
		
     
        //String name=ob.name;
	 arrayQuesCat = dbh.getChildrenQuesCategories();
		
		ArrayList<String> my_array = new ArrayList<String>();
		
		for(Object_QuesCategory obj : arrayQuesCat){
			my_array.add(obj.name);
		}
		
        Spinner My_spinner = (Spinner) findViewById(R.id.spinner1);
       
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,my_array);
 
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
 
        // attaching data adapter to spinner
       My_spinner.setAdapter(dataAdapter);
    
	My_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		
		@Override
		public void onItemSelected(AdapterView<?> arg0, View v, int pos,
				long id) {
			// TODO Auto-generated method stub
			EditText ed1=(EditText)findViewById(R.id.edtNoOfQuestions);
			EditText ed2=(EditText)findViewById(R.id.edtHours);
			EditText ed3=(EditText)findViewById(R.id.edtMinutes);
			
			DBHandler_QuestionCategory dbh=new DBHandler_QuestionCategory(Activity_Settings_ConfigureExam.this);
			arrayQuesCat = dbh.getChildrenQuesCategories();
			Log.i("id1", "row"+pos);
			
			rowId=arrayQuesCat.get(pos).id;
			Log.i("id", "row"+rowId);
		   
			
	    Object_QuesCategory obj=new Object_QuesCategory();
		   obj = dbh.getQuesCategory(rowId);
		   Log.i("obj", "objdata"+obj);
			
		   ed1.setText(obj.totalQues+"");
		   ed2.setText(obj.durationHours+"");
		   ed3.setText(obj.durationMins+"");
		  
		
	}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
		
	});
	
    
	}
	public void Edit(View v)
	{
		if(!validateEntries()){
			return;
		}
		EditText ed1=(EditText)findViewById(R.id.edtNoOfQuestions);
		EditText ed2=(EditText)findViewById(R.id.edtHours);
		EditText ed3=(EditText)findViewById(R.id.edtMinutes);
		System.out.println("edit function called!");

		Object_QuesCategory obj=new Object_QuesCategory();
		
		obj.totalQues=Integer.parseInt(ed1.getText().toString());
		obj.durationHours=Integer.parseInt(ed2.getText().toString());
		obj.durationMins=Integer.parseInt(ed3.getText().toString());
		obj.id = rowId;
		
		DBHandler_QuestionCategory dbh=new DBHandler_QuestionCategory(this);
		dbh.updateQuesCategory(obj);
		Toast.makeText(this, "Successfully Updated",Toast.LENGTH_SHORT).show();
		
	}


	
	Boolean validateEntries(){
		
		EditText ed1=(EditText)findViewById(R.id.edtNoOfQuestions);
		EditText ed2=(EditText)findViewById(R.id.edtHours);
		EditText ed3=(EditText)findViewById(R.id.edtMinutes);
		int totalQuestions = Integer.parseInt(ed1.getText().toString());
		int hours = Integer.parseInt(ed2.getText().toString());
		int minutes = Integer.parseInt(ed3.getText().toString());
		
		if(totalQuestions>MAX_QUESTIONS){
			Toast.makeText(this, "Questions cannot be greater than "+MAX_QUESTIONS, Toast.LENGTH_SHORT).show();
			return false;
		}
		if(totalQuestions < MIN_QUESTIONS){
			Toast.makeText(this, "Questions should be greater than "+MIN_QUESTIONS, Toast.LENGTH_SHORT).show();
			return false;
		}
		
		if(minutes>MAX_MINS){
			Toast.makeText(this, "Minutes Cannot be greater than "+MAX_MINS, Toast.LENGTH_SHORT).show();
			return false;
		}
		if(hours>MAX_HOURS){
			Toast.makeText(this, "Hours Cannot be greater than "+MAX_HOURS, Toast.LENGTH_SHORT).show();
			return false;
		}
		
		if(minutes == 0 && hours == 0){
			Toast.makeText(this, "Both minutes and hours cannot be zero", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		return true;
	}
	
}
