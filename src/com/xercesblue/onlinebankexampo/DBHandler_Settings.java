package com.xercesblue.onlinebankexampo;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler_Settings extends SQLiteOpenHelper {
	//CREATE TABLE Settings (Id integer NOT NULL PRIMARY KEY DEFAULT null,
			//Name text NOT NULL,Icon blob NOT NULL,OrderNo integer)
	final static String TABLE_NAME_Settings = "Settings";
	final static String KEY_SETTINGS_ID = "Id";
	final static String KEY_SETTINGS_NAME = "Name";
	final static String KEY_SETTINGS_IMAGE = "Icon";
	final static String KEY_SETTINGS_ORDER_NO = "OrderNo";
	
	public DBHandler_Settings(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	public DBHandler_Settings (Context context) {
		super(context, DBHandler_Main.DB_NAME, null, DBHandler_Main.DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}
public ArrayList<Object_Settings> getSettings(){
		
		String sqlQuery = "SELECT * FROM "+TABLE_NAME_Settings + " ORDER BY "+KEY_SETTINGS_ORDER_NO;//+" WHERE "+KEY_OPTIONS_QUES_ID+" = "+quesId;
//		sqlQuery+=" ORDER BY "+KEY_OPTIONS_OPTION_NO+ " ASC;";
		ArrayList<Object_Settings> list = new ArrayList<Object_Settings>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(sqlQuery, null);
		
		if(c!=null){
			if(c.moveToFirst()){
				do{
					Object_Settings obj = new Object_Settings();
					obj.id = c.getInt(c.getColumnIndex(KEY_SETTINGS_ID));
					obj.name = c.getString(c.getColumnIndex(KEY_SETTINGS_NAME));
					obj.iconImage = c.getBlob(c.getColumnIndex(KEY_SETTINGS_IMAGE));
					
					list.add(obj);
				}while(c.moveToNext());
			}
		}
		db.close();
		return list;
}
}
