package com.xercesblue.onlinebankexampo;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler_Options extends SQLiteOpenHelper {

	//CREATE TABLE Options (OptionId integer NOT NULL PRIMARY KEY AUTOINCREMENT,
	//QuesId integer,Option text,OptionNo integer, Image blob)

	final static String TABLE_NAME_OPTIONS = "Options";
	final static String KEY_OPTIONS_ID = "OptionId";
	final static String KEY_OPTIONS_QUES_ID = "QuesId";
	final static String KEY_OPTIONS_TEXT = "Option";
	final static String KEY_OPTIONS_OPTION_NO = "OptionNo";
	final static String KEY_OPTIONS_IMAGE = "Image";

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	public DBHandler_Options (Context context) {
		super(context, DBHandler_Main.DB_NAME, null, DBHandler_Main.DB_VERSION);
	}

	public ArrayList<Object_Options> getOptions(long quesId){

		String sqlQuery = "SELECT * FROM "+TABLE_NAME_OPTIONS+" WHERE "+KEY_OPTIONS_QUES_ID+" = "+quesId;
		sqlQuery+=" GROUP BY "+ KEY_OPTIONS_OPTION_NO +" ORDER BY "+KEY_OPTIONS_OPTION_NO+ " ASC;";
		ArrayList<Object_Options> list = new ArrayList<Object_Options>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(sqlQuery, null);

		try {
			if(c!=null){
				if(c.moveToFirst()){
					do{
						Object_Options obj = new Object_Options();
						obj.optionId = c.getLong(c.getColumnIndex(KEY_OPTIONS_ID));
						obj.quesId = c.getLong(c.getColumnIndex(KEY_OPTIONS_QUES_ID));
						obj.optionNo = c.getInt(c.getColumnIndex(KEY_OPTIONS_OPTION_NO));
						obj.optionText = c.getString(c.getColumnIndex(KEY_OPTIONS_TEXT));
						obj.image = c.getBlob(c.getColumnIndex(KEY_OPTIONS_IMAGE));

						list.add(obj);
					}while(c.moveToNext());
				}
			}

		}finally{
			if (c != null)
				c.close();
		}
		db.close();
		return list;
	}

}
