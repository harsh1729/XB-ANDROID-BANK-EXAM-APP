package com.xercesblue.onlinebankexampo;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler_Category extends SQLiteOpenHelper {

	//CREATE TABLE Category (CategoryId integer NOT NULL PRIMARY KEY AUTOINCREMENT,
	//CategoryName text,Image blob,DisplayOrder integer)
	final String TABLE_NAME_CATEGORY = "Category";
	final String KEY_CAT_ID = "CategoryId";
	final String KEY_CAT_NAME = "CategoryName";
	final String KEY_CAT_IMAGE = "Image";
	final String KEY_CAT_DISPLAY_ORDER = "DisplayOrder";

	public DBHandler_Category (Context context) {
		super(context, DBHandler_Main.DB_NAME, null, DBHandler_Main.DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}

	ArrayList<Object_Category> getCategories(){
		ArrayList<Object_Category> list = new ArrayList<Object_Category>();

		String selectQuery = "SELECT * FROM " + TABLE_NAME_CATEGORY +" ORDER BY "+KEY_CAT_DISPLAY_ORDER;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor != null)
			if (cursor.moveToFirst()) {
				do {
					Object_Category obj = new Object_Category();
					obj.id = cursor.getInt(cursor
							.getColumnIndex(KEY_CAT_ID));
					obj.displayOrder = cursor.getInt(cursor
							.getColumnIndex(KEY_CAT_DISPLAY_ORDER));
					obj.name =  cursor.getString(cursor
							.getColumnIndex(KEY_CAT_NAME));

					obj.iconImage = cursor.getBlob(cursor.getColumnIndex(KEY_CAT_IMAGE));

					list.add(obj);


				} while (cursor.moveToNext());
			}

		db.close();
		return list;
	}

}
