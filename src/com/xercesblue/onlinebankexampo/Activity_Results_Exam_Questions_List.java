package com.xercesblue.onlinebankexampo;
import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class Activity_Results_Exam_Questions_List extends Activity_Parent {

	private Object_Exam objExam;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_results_exam_questions_list);
		
		objExam = Activity_Results_Exam.objExam;
		setFooterAndHeader(-1,objExam.examName);	
		
		populateQuestionsList();
	}

	private void populateQuestionsList(){
		ListView lv = (ListView)findViewById(R.id.listQuestionsResult);
		
		DBHandler_Questions dbH = new DBHandler_Questions(this);
		ArrayList<Object_Question> listQues = dbH.getExamQuestions(objExam.examId);
		ArrayList<Object_Row_Item_Questions_List> listItem = new ArrayList<Object_Row_Item_Questions_List>();
		
		for(Object_Question objQ : listQues){
			listItem.add(getItem(objQ));
		}
		int btnWidth = Globals.getScreenSize(this).x/12;
		Custom_ArrayAdaptor_Results_Questions_List adp=new Custom_ArrayAdaptor_Results_Questions_List(this,R.layout.row_listview_questions_list,listItem,btnWidth); 
		lv.setAdapter(adp);
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos,
					long id) {
				// TODO Auto-generated method stub
				Object_Row_Item_Questions_List item =(Object_Row_Item_Questions_List)adapter.getItemAtPosition(pos);
				navigateToQuestionDetail(item.quesNo);
			}
		});
	}
	
	private void navigateToQuestionDetail(int qNo){
		
		Activity_Results_Exam_Questions_Detail.qNo = qNo;
		Intent i = new Intent(this,Activity_Results_Exam_Questions_Detail.class);
		startActivity(i);
		
	}
	private Object_Row_Item_Questions_List getItem(Object_Question objQues){

		Object_Row_Item_Questions_List item=new Object_Row_Item_Questions_List();
		item.quesNo = objQues.questionNo;
		item.attemptLater = objQues.attemptLater;
		item.selectedOptionNo = objQues.optionSelected;
		item.noOfOptions = objQues.arrayOptions.size();
		item.isSeen = true;
		item.quesCatId = objQues.catId;
		item.correctOption = objQues.correctOption;

		return item;
	}

}
