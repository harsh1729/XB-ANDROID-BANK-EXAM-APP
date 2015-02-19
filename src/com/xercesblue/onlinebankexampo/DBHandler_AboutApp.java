package com.xercesblue.onlinebankexampo;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHandler_AboutApp extends SQLiteOpenHelper {
	final static String TABLE_NAME_ABOUTAPP = "AboutApp";
	final static String KEY_ABOUTAPP_ID = "id";
	final static String KEY_ABOUTAPP_MESSAGE = "message";
	

	public DBHandler_AboutApp(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	public DBHandler_AboutApp (Context context) {
		super(context, DBHandler_Main.DB_NAME, null, DBHandler_Main.DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}
	
	ArrayList<String > getMessage(){
	 ArrayList<String> list = new ArrayList<String>();

		String selectQuery = " SELECT * FROM " + TABLE_NAME_ABOUTAPP ;
         Log.i("query","row"+selectQuery);
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor != null){
			
			if (cursor.moveToFirst()) {
				do{

					list.add( cursor.getString(cursor
							.getColumnIndex(KEY_ABOUTAPP_MESSAGE)));
					Log.i("ListItem", "list"+list);

			
			}while(cursor.moveToNext());
			
			}
		}
		db.close();
		return list;
	}
}
				