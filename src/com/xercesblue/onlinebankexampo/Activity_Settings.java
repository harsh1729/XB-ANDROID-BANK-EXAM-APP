package com.xercesblue.onlinebankexampo;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class Activity_Settings extends Activity_Parent {

	ArrayList<Object_Settings> values;
	final int SETTINGS_CHOOSE_LANGUAGE_ID = 1;
	final int SETTINGS_EXAM_CONFIGURATION_ID = 2;
	final int SETTINGS_DOWNLOADMORE_ID = 3;
	final int SETTINGS_ABOUTAPP_ID = 4;
	final int SETTINGS_CONTACT_ID = 5;
	final int SETTINGS_SHARE_US = 6;
	final int SETTINGS_REGISTER = 7;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		setFooterAndHeader(R.id.imgBtnFooterSettings,getResources().getString(R.string.header_settings));
		final ListView lv = (ListView)findViewById(R.id.listSettings);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int rowNo,
					long id) {
				navigatetoDetailActivity(rowNo);
			}


		});	
		setOptions();

	}

	public void navigatetoDetailActivity(int rowno){
		Object_Settings objS = values.get(rowno);
		Class<?> nextClass = null;

		switch (objS.id){

		case SETTINGS_CHOOSE_LANGUAGE_ID:
			nextClass = Activity_Settings_Language_Category.class;
			break;
		case SETTINGS_EXAM_CONFIGURATION_ID:
			nextClass = Activity_Settings_ConfigureExam.class;
			break;
		case SETTINGS_DOWNLOADMORE_ID:
			nextClass = Activity_Settings_DownloadMore.class;
			break;
		case SETTINGS_ABOUTAPP_ID:
			nextClass = Activity_Settings_AboutApp.class;
			break;	
		case SETTINGS_CONTACT_ID:
			nextClass = Activity_Settings_ContactUs.class;
			break;
		case SETTINGS_SHARE_US:
			nextClass = Activity_Settings_ShareUs.class;
			break;
		case SETTINGS_REGISTER:
			nextClass = Activity_Settings_Registration.class;
			break;
		default:
			break;

		}

		if(nextClass != null){
			Intent i = new Intent(this,nextClass);
			startActivity(i);
		}
	}



	private void setOptions(){
		ListView lv = (ListView)findViewById(R.id.listSettings);
		DBHandler_Settings dbh = new DBHandler_Settings(this);

		ArrayList<Object_Row_Item_Options> data = new ArrayList<Object_Row_Item_Options>();
		values = dbh.getSettings();

		for(Object_Settings obj:values)
		{
			Object_Row_Item_Options item=new Object_Row_Item_Options();
			item.text = obj.name;
			if(obj.iconImage!=null){
				Bitmap bit = BitmapFactory.decodeByteArray(obj.iconImage, 0,
						obj.iconImage.length);
				item.iconBitmap = bit;
			}else{
				item.iconBitmap = null;
			}
			data.add(item);

		}
		Custom_ArrayAdaptor_Settings_Home adp=new Custom_ArrayAdaptor_Settings_Home(this,R.layout.row_listview_settings,data); 
		lv.setAdapter(adp);

	}

	
}
