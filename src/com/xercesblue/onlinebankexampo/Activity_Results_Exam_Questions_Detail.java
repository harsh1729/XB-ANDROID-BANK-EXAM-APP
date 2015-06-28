package com.xercesblue.onlinebankexampo;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class Activity_Results_Exam_Questions_Detail extends Activity_Parent {

	static int qNo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_results_exam_questions_detail);
		setFooterAndHeader(-1,"Q No."+qNo);
		showQuestion();
	}

	private void showQuestion(){
		DBHandler_Questions dbH = new DBHandler_Questions(this);
		Object_Question objQues = dbH.getQuestionWithNumber(qNo, Activity_Results_Exam.objExam.examId);
		
		TextView txtQ = (TextView)findViewById(R.id.txtQuestion);
		ImageView imgQ = (ImageView)findViewById(R.id.imgViewQuestion);
		txtQ.setTextSize(Globals.getAppFontSize(this));
		txtQ.setText("Q."+ objQues.questionNo+ "  "+objQues.question.trim());
		if(objQues.image != null){
			Bitmap bm = BitmapFactory.decodeByteArray(objQues.image, 0,objQues.image.length);
			imgQ.setImageBitmap(bm);
		}else{
			imgQ.setImageDrawable(null);
		}
		
		//Hide Next Prev Relative Layout
		
		RelativeLayout rlyt = (RelativeLayout)findViewById(R.id.rlytNextPrev);
		rlyt.setVisibility(View.GONE);
		
		ImageButton reportBtn  = (ImageButton)findViewById(R.id.imgBtnReport);
		reportBtn.setVisibility(View.GONE);
		
		///Show Solution
		ScrollView scrollConatinerSolutions = (ScrollView)findViewById(R.id.scrolViewSolution);
		Boolean haveSolution = false;
		
		
		if(objQues.solution != null && !objQues.solution.trim().equals("")){
			TextView txtSolution= (TextView)findViewById(R.id.txtSolution);
			txtSolution.setTextSize(Globals.getAppFontSize(this));
			txtSolution.setText("Solution :  \n"+objQues.solution);
			haveSolution = true;
		}
		
		ImageView imgSolution = (ImageView)findViewById(R.id.imgViewSolution);
		if(objQues.solutionImage != null){
			Bitmap bm = BitmapFactory.decodeByteArray(objQues.solutionImage, 0,objQues.solutionImage.length);
			imgSolution.setImageBitmap(bm);
			haveSolution = true;
		}else{
			imgSolution.setImageDrawable(null);
		}
		
		if(haveSolution)
			scrollConatinerSolutions.setVisibility(View.VISIBLE);
		else
			scrollConatinerSolutions.setVisibility(View.GONE);
		
		///Show Options
		ListView lv = (ListView)findViewById(R.id.listOptions);
		ArrayList<Object_Row_Item_Options> dataOptions = new ArrayList<Object_Row_Item_Options>();
		
		for(Object_Options obj:objQues.arrayOptions)
		{
			Object_Row_Item_Options item=new Object_Row_Item_Options();
			item.text = obj.optionText;
			if(obj.image!=null){
				Bitmap bit = BitmapFactory.decodeByteArray(obj.image, 0,
						obj.image.length);
				item.iconBitmap = bit;
			}else{
				item.iconBitmap = null;
			}
			item.isCorrect = false;
			item.isWrong = false;
			
			if(objQues.correctOption == obj.optionNo){
				item.isCorrect = true;
			}else if(objQues.optionSelected == obj.optionNo){
				item.isWrong = true;
			}
			dataOptions.add(item);
		}

		Custom_ArrayAdaptor_Result_Question_Options adp=new Custom_ArrayAdaptor_Result_Question_Options(this,R.layout.row_listview_options_question,dataOptions); 
		lv.setAdapter(adp);
		
	}
	
}
