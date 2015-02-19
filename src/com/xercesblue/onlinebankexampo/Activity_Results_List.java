package com.xercesblue.onlinebankexampo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class Activity_Results_List extends Activity_Parent_IntertialAds {

	private ArrayList<Object_Exam> listExams;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_results_list);
		setFooterAndHeader(R.id.imgBtnFooterResult,getResources().getString(R.string.header_result));	
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		populateExamList();
	}
	private void populateExamList(){
		ListView lv = (ListView)findViewById(R.id.listResults);
		DBHandler_Exam dbH = new DBHandler_Exam(this);
		listExams = dbH.getExamsList(-1);
		
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        
		for(Object_Exam obj : listExams){
	            Map<String, String> map = new HashMap<String, String>();
	            map.put("name", obj.examName);
	            map.put("type", obj.quesCatName);
	            map.put("date", obj.examDate);
	            float marks = obj.correctQuestion * obj.markEachQues - ((obj.attemptedQuestion - obj.correctQuestion)*obj.negativeMarkEachQues);
	            //float totalmarks = obj.totalQues*obj.markEachQues;
	            map.put("marks", String.format("%.2f", marks));//+" / "+String.format("%.2f", totalmarks));
	            
	            data.add(map);
	        }
		
		SimpleAdapter adapter = new SimpleAdapter(this, data,
        		R.layout.row_listview_result_list,
                                                  new String[] {"name", "type","date","marks"},
                                                  new int[] {R.id.col1,
				R.id.col2,R.id.col3,R.id.col4});
        lv.setAdapter(adapter);
        
        lv.setOnItemClickListener(new OnItemClickListener() {

        	@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos,
					long id) {
				// TODO Auto-generated method stub
				navigateToExamInfo(pos);
			}
		});
	}		

	private void navigateToExamInfo(int pos){

		Object_Exam objExam =listExams.get(pos);
		Intent i = new Intent(this, Activity_Results_Exam.class);
		Activity_Results_Exam.objExam = objExam;
		startActivity(i);
	}
}
