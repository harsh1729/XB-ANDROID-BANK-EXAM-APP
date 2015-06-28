package com.xercesblue.onlinebankexampo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class Activity_PopUp_SelectExamCategory extends Activity {

	final static String KEY_ARRAY_QUES_CAT = "ArrayQuesCat";
	static int selectedCategoryId = -1;
	
	public static ArrayList<Object_QuesCategory> arrayQuesCat;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_popup_select_exam_category);	
		initializeView();
		
	}
	
	public void initializeView(){
		//if(Activity_Select_Exam_Category.quesCategory != null)
		//arrayQuesCat = Activity_Select_Exam_Category.quesCategory.arrayChildrenCat; //(ArrayList<Object_QuesCategory>) this.getIntent().getExtras().get(KEY_ARRAY_QUES_CAT);
		
		if(arrayQuesCat == null){
			Globals.showAlertDialogError(this);
			return;
		}
		
		 ListView lv = (ListView)findViewById(R.id.listExamCategories);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int pos,
					long id) {
				selectedCategoryId = arrayQuesCat.get(pos).id;
				Activity_PopUp_SelectExamCategory.this.finish();
			}
		});
		
		ArrayList<Map<String, String>>data = new ArrayList<Map<String,String>>();
		for(Object_QuesCategory obj:arrayQuesCat)
		{
			Map<String, String> map = new HashMap<String, String>();
			map.put("TEXT", obj.name);
			data.add(map);
		}
		SimpleAdapter adp=new SimpleAdapter(this,data,R.layout.row_listview_select_exam_categories,new String[]{"TEXT"},new int[]{R.id.txtRowText}); 
		lv.setAdapter(adp);
	}

	public void onClickPopView(View v){
		this.finish();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		arrayQuesCat = null;
	}
}
