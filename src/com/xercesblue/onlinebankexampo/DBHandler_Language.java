package com.xercesblue.onlinebankexampo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler_Language extends SQLiteOpenHelper {

	// CREATE TABLE Language (LangId integer NOT NULL PRIMARY KEY AUTOINCREMENT,
	// Lang text, LangCode text)
	final String TABLE_NAME_LANGUAGE = "Language";
	final String KEY_LANG_ID = "LangId";
	final String KEY_LANG_NAME = "Lang";
	final String KEY_LANG_CODE = "LangCode";

	// final String KEY_LANG_IS_SELECTED = "Selected";

	public DBHandler_Language(Context context) {
		super(context, DBHandler_Main.DB_NAME, null, DBHandler_Main.DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}

	ArrayList<Object_Language> getLanguages(int langId) {
		ArrayList<Object_Language> list = new ArrayList<Object_Language>();

		String selectQuery = "SELECT * FROM " + TABLE_NAME_LANGUAGE;

		if (langId > 0) {
			selectQuery += " WHERE " + KEY_LANG_ID + " = " + langId;
		}
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor != null)
			if (cursor.moveToFirst()) {
				do {
					Object_Language obj = new Object_Language();
					obj.langId = cursor.getInt(cursor
							.getColumnIndex(KEY_LANG_ID));
					obj.langName = cursor.getString(cursor
							.getColumnIndex(KEY_LANG_NAME));
					obj.langCode = cursor.getString(cursor
							.getColumnIndex(KEY_LANG_CODE));

					list.add(obj);
				} while (cursor.moveToNext());
			}
		db.close();
		return list;
	}

	public String getLangCode(int langId){
		String langCode = "";
		
		String selectQuery = "SELECT "+KEY_LANG_CODE+" FROM " + TABLE_NAME_LANGUAGE;

		if (langId > 0) {
			selectQuery += " WHERE " + KEY_LANG_ID + " = " + langId;
		}
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor != null)
			if (cursor.moveToFirst()) {
				
					langCode =	cursor.getString(cursor
							.getColumnIndex(KEY_LANG_CODE));
			}
		db.close();
		return langCode;
	}
	public Map<String, Integer> getLangMap() {
		Map<String, Integer> langMqp = new HashMap<String, Integer>();

		String getLangIdQuery = "SELECT * FROM " + TABLE_NAME_LANGUAGE;

		SQLiteDatabase db;
		db = this.getWritableDatabase();

		Cursor cur = db.rawQuery(getLangIdQuery, null);
		if (cur != null) {
			if (cur.moveToFirst()) {
				do {
					langMqp.put(
							cur.getString(cur.getColumnIndex(KEY_LANG_CODE)),
							cur.getInt(cur.getColumnIndex(KEY_LANG_ID)));
				} while (cur.moveToNext());
			}
		}
		db.close();
		return langMqp;
	}

}
