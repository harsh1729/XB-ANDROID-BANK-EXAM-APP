package com.xercesblue.onlinebankexampo;



import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Activity_Results_Exam extends Activity_Parent {

	public static Object_Exam objExam;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_results_exam);
		setFooterAndHeader(-1,objExam.examName);
		
		initializeControls();
	}

	
	
	public void onClickShowDetails(View v){
		
		Intent i = new Intent(this,Activity_Results_Exam_Questions_List.class);
		startActivity(i);
	}
	
	private void initializeControls(){
		TextView txtHeader = (TextView)findViewById(R.id.txtHeader);
		txtHeader.setText(objExam.examName);	
		
		TextView txtTotalQues = (TextView)findViewById(R.id.txtTotalQuesValue);
		TextView txtAttemptedQues = (TextView)findViewById(R.id.txtAttemptedQuesValue);
		TextView txtCorrectQues = (TextView)findViewById(R.id.txtCorrectQuesValue);
		TextView txtMarksQues = (TextView)findViewById(R.id.txtMarksValue);
		TextView txtTotalMarksQues = (TextView)findViewById(R.id.txtTotalMarksValue);
		TextView txtExamLang = (TextView)findViewById(R.id.txtLangValue);
		TextView txtExamCategory = (TextView)findViewById(R.id.txtCategoryValue);;
		
		txtTotalQues.setText(objExam.totalQues+"");
		txtAttemptedQues.setText(objExam.attemptedQuestion+"");
		txtCorrectQues.setText(objExam.correctQuestion+"");
		txtExamLang.setText(objExam.examLang);
		txtExamCategory.setText(objExam.quesCatName);
		
		float marks = objExam.correctQuestion * objExam.markEachQues - ((objExam.attemptedQuestion - objExam.correctQuestion)*objExam.negativeMarkEachQues);
        float totalmarks = objExam.totalQues*objExam.markEachQues;
		txtMarksQues.setText(String.format("%.2f", marks));
		txtTotalMarksQues.setText(String.format("%.2f", totalmarks));
		
		Button btnDelete = (Button)findViewById(R.id.btnDeleteExam);
		Point btnSize = Globals.getAppButtonSize(this);
		btnDelete.getLayoutParams().width = btnSize.x;
		btnDelete.getLayoutParams().height = btnSize.y;
		btnDelete.setOnTouchListener(new Custom_ButtonOnTouchListener_GreenBG(btnDelete, this));
		btnDelete.setTextSize(Globals.getAppFontSize(this));
		
		Button btnDetails = (Button)findViewById(R.id.btnDetailsExam);
		btnDetails.getLayoutParams().width = btnSize.x;
		btnDetails.getLayoutParams().height = btnSize.y;
		btnDetails.setOnTouchListener(new Custom_ButtonOnTouchListener_GreenBG(btnDetails, this));
		btnDetails.setTextSize(Globals.getAppFontSize(this));
	
		
	}
	
	
	private void deleteExam(){
		DBHandler_Exam dbH = new DBHandler_Exam(this);
		dbH.deleteExam(objExam.examId);
		
		Intent i = new Intent(this , Activity_Results_List.class);
		startActivity(i);
		this.finish();
	}
	public void onClickDeleteExam(View v){
		
		OnClickListener listnerPositive = new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				deleteExam();
				
			}
		};
		OnClickListener listnerNegative = new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		};
		Globals.showAlertDialog("Alert","Are you sure to delete this exam ?", this, "YES", listnerPositive, "No", listnerNegative, false);
		
		
		
	}
	
}
