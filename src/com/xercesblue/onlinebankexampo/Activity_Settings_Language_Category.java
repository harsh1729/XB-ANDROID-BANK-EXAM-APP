package com.xercesblue.onlinebankexampo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class Activity_Settings_Language_Category extends Activity_Parent {
	ArrayList<Object_Language> arrayObjLang = null;
	
	Boolean firstTime=true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings_language_category);
		setFooterAndHeader(R.id.imgBtnFooterHome,getResources().getString(R.string.header_choose_languaae));
		
		loadData();
		 ListView lv=(ListView)findViewById(R.id.listLangCat);
		 lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int pos,
					long id) {
				DBHandler_AppConfig dbh=new DBHandler_AppConfig(Activity_Settings_Language_Category.this);
				int langId=arrayObjLang.get(pos).langId;
				dbh.setLanguage(langId);
				setCheckImage();
			}
		});
		 
	}

	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if(firstTime)
		{
			setCheckImage();	
			firstTime = false;
		}
	}
	
	private void setCheckImage(){
		 
		 ListView lv=(ListView)findViewById(R.id.listLangCat);
		 
		for(int i=0;i<lv.getChildCount();i++){
			
			View v = lv.getChildAt(i);
			int id = arrayObjLang.get(i).langId;
			
			ImageView iv=(ImageView)v.findViewById(R.id.imageLang);
			int imageXY =lv.getHeight()/14;
			
			iv.getLayoutParams().height = imageXY;
			if(id == Globals.getAppConfig(this).selectedLangId){
				iv.setImageResource(R.drawable.checkbox_white_green);
			}else{
				iv.setImageResource(R.drawable.checkbox_white);
			}
			
		}
		
	}
	public void loadData(){
		
		ListView lv=(ListView)findViewById(R.id.listLangCat);
		DBHandler_Language dbh=new DBHandler_Language(this);

		arrayObjLang=dbh.getLanguages(-1);

	List<Map<String,String>> list=new ArrayList<Map<String,String>>();
	for(Object_Language ol:arrayObjLang){
		HashMap<String,String> map=new HashMap<String,String>(2);
		map.put("name", ol.langName);	
		   
		list.add(map);
	}	
	
	SimpleAdapter adp=new SimpleAdapter(this,list,R.layout.row_listview_settings_select_language,new String[]{"name"},new int[]{R.id.txtRowLang});
	lv.setAdapter(adp);
	}

}
