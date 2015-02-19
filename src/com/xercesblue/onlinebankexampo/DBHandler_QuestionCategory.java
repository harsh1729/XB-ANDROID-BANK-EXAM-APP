package com.xercesblue.onlinebankexampo;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler_QuestionCategory extends SQLiteOpenHelper {

	//CREATE TABLE QuestionCategory (QuesCatId integer NOT NULL PRIMARY KEY AUTOINCREMENT,
	//QuesCat text,Image blob, DisplayOrder integer, TotalQuestions integer, 
	//MarksEachQues real, NegativeMarkEachQues real, DurationHours integer, DurationMin integer, 
	//IsCategory integer DEFAULT 1)
	static final String TABLE_NAME_QCATEGORY = "QuestionCategory";
	static final String KEY_QCAT_ID = "QuesCatId";
	static final String KEY_QCAT_NAME = "QuesCat";
	final String KEY_QCAT_IMAGE = "Image";
	final String KEY_QCAT_DISPLAY_ORDER = "DisplayOrder";
	final String KEY_QCAT_TOTAL_QUES = "TotalQuestions";
	static final String KEY_QCAT_MARK = "MarksEachQues";
	static final String KEY_QCAT_NEGATIVE_MARK = "NegativeMarkEachQues";
	final String KEY_QCAT_DURATION_HOURS = "DurationHours";
	final String KEY_QCAT_DURATION_MIN = "DurationMin";
	final String KEY_QCAT_IS_PARENT_CATEGORY = "IsParentCategory";

	public DBHandler_QuestionCategory (Context context) {
		super(context, DBHandler_Main.DB_NAME, null, DBHandler_Main.DB_VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase arg0) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}

	ArrayList<Object_QuesCategory> getAllQuesCategories(Boolean onlyChildren){

		ArrayList<Object_QuesCategory> list = new ArrayList<Object_QuesCategory>();
		String selectQuery = "SELECT * FROM " + TABLE_NAME_QCATEGORY ;
		if(onlyChildren){
			selectQuery += " WHERE "+KEY_QCAT_IS_PARENT_CATEGORY +" = 0";
		}
		selectQuery +=" ORDER BY "+KEY_QCAT_DISPLAY_ORDER;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor != null)
			if (cursor.moveToFirst()) {
				do {
					Object_QuesCategory  obj = getQuesCategory(cursor);
					// not setting child categories detail because these objects will be used only to show data.
					list.add(obj);

				} while (cursor.moveToNext());
			}

		db.close();
		return list;
	}


	Object_QuesCategory getQuesCategory(int catID){

		Object_QuesCategory obj = null;
		String selectQuery = "SELECT * FROM " + TABLE_NAME_QCATEGORY +" WHERE "+KEY_QCAT_ID + " = "+catID +" ORDER BY "+KEY_QCAT_DISPLAY_ORDER;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor != null)
			if (cursor.moveToFirst()) {

				obj = getQuesCategory(cursor);
						
			}

		db.close();
		if(obj != null){
			if(obj.isParentCategory == 1){
				obj.arrayChildrenCat = getChildrenQuesCategories();
				int totalQ = 0;
				int totalMin = 0;
				int totalHours = 0;
				if(obj.arrayChildrenCat != null){
					for(Object_QuesCategory objChild : obj.arrayChildrenCat){
						totalQ+= objChild.totalQues;
						totalMin+= objChild.durationMins;
						totalHours+= objChild.durationHours;
					}
				}
				obj.totalQues = totalQ;
				obj.durationHours = totalHours;
				obj.durationMins = totalMin;
			}
		}
		return obj;
	}
	
	public ArrayList<Object_QuesCategory> getChildrenQuesCategories(){
		ArrayList<Object_QuesCategory> list = new ArrayList<Object_QuesCategory>();
		String selectQuery = "SELECT * FROM " + TABLE_NAME_QCATEGORY 
				+" WHERE "+KEY_QCAT_IS_PARENT_CATEGORY +" = 0  ORDER BY "+KEY_QCAT_DISPLAY_ORDER;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor != null)
			if (cursor.moveToFirst()) {
				do {
					Object_QuesCategory  obj = new Object_QuesCategory();
					obj.id = cursor.getInt(cursor
							.getColumnIndex(KEY_QCAT_ID));
					obj.displayOrder = cursor.getInt(cursor
							.getColumnIndex(KEY_QCAT_DISPLAY_ORDER));
					obj.name =  cursor.getString(cursor
							.getColumnIndex(KEY_QCAT_NAME));
					obj.marksEachQues = cursor.getFloat(cursor
							.getColumnIndex(KEY_QCAT_MARK));
					obj.negativeMarkEackQues = cursor.getFloat(cursor
							.getColumnIndex(KEY_QCAT_NEGATIVE_MARK));
					obj.totalQues = cursor.getInt(cursor
							.getColumnIndex(KEY_QCAT_TOTAL_QUES));
					obj.durationHours = cursor.getInt(cursor
							.getColumnIndex(KEY_QCAT_DURATION_HOURS));
					obj.durationMins = cursor.getInt(cursor
							.getColumnIndex(KEY_QCAT_DURATION_MIN));
					list.add(obj);


				} while (cursor.moveToNext());
			}

		db.close();
		return list;
	}
	private Object_QuesCategory getQuesCategory(Cursor cursor){
		
		Object_QuesCategory obj = new Object_QuesCategory();
		obj.id = cursor.getInt(cursor
				.getColumnIndex(KEY_QCAT_ID));
		obj.displayOrder = cursor.getInt(cursor
				.getColumnIndex(KEY_QCAT_DISPLAY_ORDER));
		obj.name =  cursor.getString(cursor
				.getColumnIndex(KEY_QCAT_NAME));

		obj.iconImage = cursor.getBlob(cursor.getColumnIndex(KEY_QCAT_IMAGE));
		obj.marksEachQues = cursor.getFloat(cursor
				.getColumnIndex(KEY_QCAT_MARK));
		obj.negativeMarkEackQues = cursor.getFloat(cursor
				.getColumnIndex(KEY_QCAT_NEGATIVE_MARK));
		obj.totalQues = cursor.getInt(cursor
				.getColumnIndex(KEY_QCAT_TOTAL_QUES));
		obj.durationHours = cursor.getInt(cursor
				.getColumnIndex(KEY_QCAT_DURATION_HOURS));
		obj.durationMins = cursor.getInt(cursor
				.getColumnIndex(KEY_QCAT_DURATION_MIN));
		obj.isParentCategory =  cursor.getInt(cursor
				.getColumnIndex(KEY_QCAT_IS_PARENT_CATEGORY));
		
		return obj;
	}
	
	public void updateQuesCategory(Object_QuesCategory obj)
	{
		System.out.println("update called!");
		SQLiteDatabase db = this.getWritableDatabase();		
		
		ContentValues values = new ContentValues();
		
		values.put(KEY_QCAT_TOTAL_QUES, obj.totalQues);
		values.put(KEY_QCAT_DURATION_HOURS, obj.durationHours);
		values.put(KEY_QCAT_DURATION_MIN, obj.durationMins);
		
		
		int rows = db.update(TABLE_NAME_QCATEGORY, values, KEY_QCAT_ID +" = "+obj.id, null );
		System.out.println(rows);
}
}
