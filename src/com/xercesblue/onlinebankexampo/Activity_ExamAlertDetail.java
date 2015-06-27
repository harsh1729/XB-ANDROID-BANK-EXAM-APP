package com.xercesblue.onlinebankexampo;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class Activity_ExamAlertDetail extends Activity_Parent {
	static Object_ExamAlert ExamDetails;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exam_alert_detail);
		setFooterAndHeader(R.id.imgBtnFooterHome,ExamDetails.examname);	
		createDynammicControl();

	}


	public void createDynammicControl()
	{

		LinearLayout l1=(LinearLayout)findViewById(R.id.llytMainBodyExamAlertDetail);
		LayoutParams lp2=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		lp2.leftMargin=10;
		lp2.topMargin=10;

		/*
		 LayoutParams lp1=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		TextView tv1=new TextView(this);
		tv1.setText(ExamDetails.examname);
		tv1.setBackgroundResource(R.drawable.bg_gradient_light_to_dark);
		tv1.setGravity(Gravity.CENTER);
		tv1.setPadding(5, 10, 5, 10);
		tv1.setTextSize(Globals.getAppFontSize_Large(width));
		tv1.setTextColor(this.getResources().getColor(R.color.app_green));

		tv1.setLayoutParams(lp1);
		l1.addView(tv1);
		*/
		TextView tv2=new TextView(this);
		tv2.setText(ExamDetails.getDetails());
		tv2.setTextSize(Globals.getAppFontSize_Large(this));
		tv2.setTextColor(this.getResources().getColor(R.color.app_black));
		

		tv2.setLayoutParams(lp2);
		l1.addView(tv2);

	}

}
