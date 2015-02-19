package com.xercesblue.onlinebankexampo;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHandler_Tutorial extends SQLiteOpenHelper {
	final String TABLE_NAME_QCATEGORY = "QuestionCategory";
	final String KEY_QCAT_ID = "QuesCatId";
	final String KEY_QCAT_DISPLAY_ORDER = "DisplayOrder";
	
	final String TABLE_NAME_TUTORIAL_TOPICS = "TutorialsTopics";
	final String KEY_TUTORIAL_TOPICS_ID = "TutorialTopicId";
	final String KEY_TUTORIAL_TOPICS_CAT_ID = "CatId";
	final String KEY_TUTORIAL_TOPICS_NAME = "Topics";
	
	
	final String TABLE_NAME_TUTORIAL_TOPICS_DETAILS = "TutorialTopicsDetails";
	final String KEY_TUTORIAL_TOPICS_DETAIL_ID = "TopicDetailId";
	final String KEY_TUTORIAL_TOPIC_DETAIL = "TopicDetail";
	final String KEY_TUTORIAL_TOPIC_ID = "TopicId";
	//private Context context;
	

	public DBHandler_Tutorial (Context context) {
		super(context, DBHandler_Main.DB_NAME, null, DBHandler_Main.DB_VERSION);
		//this.context = context;
	}
	

	@Override
	public void onCreate(SQLiteDatabase arg0) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}
	ArrayList<Object_TutorialTopics> getTutorialTopics(int id){
		ArrayList<Object_TutorialTopics> list = new ArrayList<Object_TutorialTopics>();
		
		//SELECT  TT.Topics FROM TutorialsTopics TT INNER JOIN  QuestionCategory QC ON 
		// QC.QuesCatId=TT.CatId
		 
		String selectQuery = " Select * FROM " + TABLE_NAME_TUTORIAL_TOPICS;        
		Log.i("query","Row"+selectQuery);
		 if(id>0)
		 {
			 selectQuery+= " WHERE " +KEY_TUTORIAL_TOPICS_CAT_ID+" = "+id;
		 }
		 //selectQuery+=" ORDER BY "+KEY_QCAT_DISPLAY_ORDER;
		 
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor != null)
			if (cursor.moveToFirst()) {
				do {
				Object_TutorialTopics obj = new Object_TutorialTopics();
					obj.Id = cursor.getInt(cursor
							.getColumnIndex(KEY_TUTORIAL_TOPICS_ID));
					obj.TopicName = cursor.getString(cursor
							.getColumnIndex(KEY_TUTORIAL_TOPICS_NAME));
					
					obj.CatId = cursor.getInt(cursor.getColumnIndex(KEY_TUTORIAL_TOPICS_CAT_ID));

					list.add(obj);

				
				} while (cursor.moveToNext());
			}
		 
		db.close();
		return list;
	}
	
	ArrayList<Object_TutorialTopicDetails> getTutorialTopicDetails(int rowId){
		ArrayList<Object_TutorialTopicDetails> list1 = new ArrayList<Object_TutorialTopicDetails>();
		//SELECT  TT.Topics,TTD.TopicDetails FROM TutorialTopics TT INNER JOIN 
		//QuestionCategory QC,TutorialTopicsDetails TTD ON TT.TutorialTopicsId=TTD.TopicId 
		//and QC.QuesCatId=TT.CatId
		String selectQuery = " Select *  FROM " + TABLE_NAME_TUTORIAL_TOPICS_DETAILS ;
		 Log.i("query","Row"+selectQuery);
		 if(rowId>0)
		 {
			 selectQuery+= " WHERE " +KEY_TUTORIAL_TOPIC_ID+" = "+rowId;
		 }
		 //selectQuery+=" ORDER BY "+KEY_QCAT_DISPLAY_ORDER;
		 
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor != null)
			if (cursor.moveToFirst()) {
				do {
					Object_TutorialTopicDetails obj = new Object_TutorialTopicDetails();
					obj.TopicDetailId = cursor.getInt(cursor
							.getColumnIndex(KEY_TUTORIAL_TOPICS_DETAIL_ID));
					obj.TopicId = cursor.getInt(cursor
							.getColumnIndex(KEY_TUTORIAL_TOPIC_ID));
			
            obj.TopicDetailName = cursor.getString(cursor.getColumnIndex(KEY_TUTORIAL_TOPIC_DETAIL));

					list1.add(obj);


				} while (cursor.moveToNext());
			}

		db.close();
		return list1;
	}


}
//String selectQuery = " Select TT. "+KEY_TUTORIAL_TOPICS_NAME+",TTD. "+KEY_TUTORIAL_TOPIC_DETAIL+ " FROM " + TABLE_NAME_TUTORIAL_TOPICS +" TT INNER JOIN "+TABLE_NAME_QCATEGORY+" QC, "+TABLE_NAME_TUTORIAL_TOPICS_DETAILS+" TTD ON TT."+KEY_TUTORIAL_TOPICS_ID+" = TTD. "+KEY_TUTORIAL_TOPIC_ID+" AND QC. "+KEY_QCAT_ID+" = TT. "+KEY_TUTORIAL_TOPICS_CAT_ID+ " ORDER BY "+KEY_QCAT_DISPLAY_ORDER;
