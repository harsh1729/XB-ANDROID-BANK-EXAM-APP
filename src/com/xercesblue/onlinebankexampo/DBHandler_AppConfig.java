package com.xercesblue.onlinebankexampo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHandler_AppConfig extends SQLiteOpenHelper {

	// CREATE TABLE AppConfiguration (Id integer NOT NULL PRIMARY KEY
	// AUTOINCREMENT,
	// SelectedLangId integer DEFAULT 0,ShowAdds integer DEFAULT 1,
	// AddTypeId integer DEFAULT 1)

	final static String TABLE_NAME_APP_CONFIG = "AppConfiguration";
	final static String KEY_APP_CONFIG_SELECTED_LANG_ID = "SelectedLangId";
	
	final String KEY_APP_CONFIG_ID = "Id";
	final String KEY_APP_CONFIG_SHOWS_ADD = "ShowAdds";
	final String KEY_APP_CONFIG_ADTYPE_ID = "AdTypeId";
	final String KEY_APP_CONFIG_ADTYPE_INTER_ID = "AdTypeIntertialId";
	
	Context context;

	public DBHandler_AppConfig(Context context) {
		super(context, DBHandler_Main.DB_NAME, null, DBHandler_Main.DB_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}

	public void updateAppConfigurationAds(int showAds) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();

		cv.put(KEY_APP_CONFIG_SHOWS_ADD, showAds);

		db.update(TABLE_NAME_APP_CONFIG, cv, null, null);
		db.close();

		Globals.getAppConfig(context).showAdds = showAds;
	}
	
	public void updateAppConfigurationAds(int showAds , int adTypeId , int adTypeInterId) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();

		cv.put(KEY_APP_CONFIG_SHOWS_ADD, showAds);
		cv.put(KEY_APP_CONFIG_ADTYPE_ID, adTypeId);
		cv.put(KEY_APP_CONFIG_ADTYPE_INTER_ID, adTypeInterId);
		db.update(TABLE_NAME_APP_CONFIG, cv, null, null);
		db.close();

		Globals.getAppConfig(context).showAdds = showAds;
		Globals.getAppConfig(context).adTypeId = adTypeId;
		Globals.getAppConfig(context).adTypeInterId = adTypeInterId;
	}

	/*
	 * public void updateAppConfiguration(Object_AppConfig obj){ SQLiteDatabase
	 * db = this.getWritableDatabase(); ContentValues cv = new ContentValues();
	 * 
	 * cv.put(KEY_APP_CONFIG_SELECTED_LANG_ID, obj.selectedLangId);
	 * cv.put(KEY_APP_CONFIG_ADTYPE_ID, obj.adTypeId);
	 * cv.put(KEY_APP_CONFIG_SHOWS_ADD, obj.showAdds);
	 * 
	 * db.update(TABLE_NAME_APP_CONFIG, cv, KEY_APP_CONFIG_ID + " = "+obj.id,
	 * null); db.close();
	 * 
	 * }
	 */

	public Object_AppConfig getAppConfiguration() {

		Object_AppConfig obj = new Object_AppConfig();

		String selectQuery = " SELECT * FROM " + TABLE_NAME_APP_CONFIG
				+ " LIMIT 1";
		Log.i("query", "row" + selectQuery);
		SQLiteDatabase db = this.getReadableDatabase();

		try {
			Cursor cursor = db.rawQuery(selectQuery, null);
			if (cursor != null) {

				if (cursor.moveToFirst()) {
					obj = new Object_AppConfig();
					obj.id = cursor.getInt(cursor
							.getColumnIndex(KEY_APP_CONFIG_ID));
					obj.selectedLangId = cursor.getInt(cursor
							.getColumnIndex(KEY_APP_CONFIG_SELECTED_LANG_ID));
					obj.showAdds = cursor.getInt(cursor
							.getColumnIndex(KEY_APP_CONFIG_SHOWS_ADD));
					obj.adTypeId = cursor.getInt(cursor
							.getColumnIndex(KEY_APP_CONFIG_ADTYPE_ID));
					obj.adTypeInterId = cursor.getInt(cursor
							.getColumnIndex(KEY_APP_CONFIG_ADTYPE_INTER_ID));

				}
			}
		} catch (SQLiteException ex) {
			Log.e("HARSH"," SQLite Error in getAppConfiguration , DBHandler_AppConfig");
		}

		db.close();
		return obj;

	}

	/*
	 * int getSelectedLanguageId(){ String selectQuery = "SELECT "
	 * +KEY_APP_CONFIG_SELECTED_LANG_ID+" FROM " + TABLE_NAME_APP_CONFIG ;
	 * //" WHERE "+KEY_LANG_IS_SELECTED + " <> "+Object_Language.NOT_SELECTED;
	 * SQLiteDatabase db = this.getReadableDatabase(); Cursor cursor =
	 * db.rawQuery(selectQuery, null); int selectedLangId = -1;
	 * 
	 * if (cursor.moveToFirst()){ selectedLangId = cursor.getInt(cursor
	 * .getColumnIndex(DBHandler_AppConfig.KEY_APP_CONFIG_SELECTED_LANG_ID)); }
	 * db.close(); return selectedLangId; }
	 */

	void setLanguage(int langId) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();

		cv.put(KEY_APP_CONFIG_SELECTED_LANG_ID, langId);
		db.update(TABLE_NAME_APP_CONFIG, cv, null, null);

		Globals.getAppConfig(context).selectedLangId = langId;
		db.close();
	}
}
