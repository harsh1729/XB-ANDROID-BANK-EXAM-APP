package com.xercesblue.onlinebankexampo;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_Current_GK_Type_Select extends Activity_Parent {

	private static int date = 0;
	private static int month = 0;
	private static int year = 2014;
	
	public static String getSelectedDate(){
		if(date < 10){
			return "0"+date;
		}
		
		return date+"";
	}
	
	public static String getSelectedMonth(){
		if(month <10){
			return "0"+month;
		}
		
		return month+"";
	}
	
	public static String getSelectedYear(){	
		
		return year+"";
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_current_gk_type_select);
		setFooterAndHeader(-1,getResources().getString(R.string.header_cuurent_gk_type_select));
		initView();
	}

	private void initView(){
		
		final String[] MONTHS = { "Janaury", "February", "March", "April", "May",
				"Jun", "July", "August", "September", "October", "November", "December" };
			
		Calendar c = Calendar.getInstance();

		int currentYear = c.get(Calendar.YEAR) ;
		int curentMonth =c.get(Calendar.MONTH) ;
		final int baseYear= 2014;
		
		ArrayList<String> listMonths = new ArrayList<String>();
		int selectIndex = 0;
		for(int i =  baseYear; i <= currentYear ; i++){
			
			for(int m = 0 ;m< MONTHS.length;m++){
				
				listMonths.add(MONTHS[m] + " "+i);
				if((i == currentYear) && (m == curentMonth)){
					selectIndex = listMonths.size() -1;
					month = curentMonth + 1;
					year = currentYear;
					break;
				}
				
			}
		}
		
	        Spinner My_spinner = (Spinner) findViewById(R.id.spinner1);
	       
	        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
	                android.R.layout.simple_spinner_item,listMonths);
	 
	        // Drop down layout style - list view with radio button
	        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	 
	        // attaching data adapter to spinner
	       My_spinner.setAdapter(dataAdapter);
	       My_spinner.setSelection(selectIndex);
		My_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			@Override
			public void onItemSelected(AdapterView<?> arg0, View v, int pos,
					long id) {
				
			  month = pos % 12 + 1;
			  year = baseYear + pos/12;
			
			 // Toast.makeText(getApplicationContext(), "Month : "+Activity_Current_GK.month + " Year :" + Activity_Current_GK.year, Toast.LENGTH_SHORT).show();
		}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		
		Button btn=(Button)findViewById(R.id.btnTestGk);
		Point btnSize = Globals.getAppButtonSize(this);
		btn.getLayoutParams().width = btnSize.x;
		btn.getLayoutParams().height = btnSize.y;
		btn.setTextSize( Globals.getAppFontSize_Large(this));
		btn.setBackgroundResource(R.drawable.bg_white_black_border);
		btn.setTextColor(this.getResources().getColor(R.color.app_white));
		btn.setOnTouchListener(new Custom_ButtonOnTouchListener_GreenBG(btn, this));
		
		
		Button btn2=(Button)findViewById(R.id.btnReadGk);
		btn2.getLayoutParams().width = btnSize.x;
		btn2.getLayoutParams().height = btnSize.y;
		btn2.setBackgroundResource(R.drawable.bg_white_black_border);
		btn2.setTextColor(this.getResources().getColor(R.color.app_white));
		btn2.setTextSize( Globals.getAppFontSize_Large(this));
		btn2.setOnTouchListener(new Custom_ButtonOnTouchListener_GreenBG(btn2, this));
	}
	
	public void btnTestGkClick(View v){
		Intent i = new Intent(this, Activity_Current_GK_Test.class);
		startActivity(i);
	}
	
	public void btnReadGkClick(View v){
		
		Intent i = new Intent(this, Activity_Current_GK_Read.class);
		startActivity(i);
	}
}
