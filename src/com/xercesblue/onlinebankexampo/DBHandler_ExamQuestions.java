package com.xercesblue.onlinebankexampo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler_ExamQuestions extends SQLiteOpenHelper {

	//CREATE TABLE ExamsQuestion (Id integer NOT NULL PRIMARY KEY AUTOINCREMENT,ExamId integer,
	//QuesId integer,AttemptLater integer DEFAULT 0,OptionNoSelected integer DEFAULT 0, QuestionNo integer)
	final static String TABLE_NAME_EXAMQUESTIONS = "ExamsQuestion";
	final static String KEY_EXAMQUESTIONS_ID = "Id";
	final static String KEY_EXAMQUESTIONS_EXAM_ID = "ExamId";
	final static String KEY_EXAMQUESTIONS_QUES_ID  = "QuesId";
	final static String KEY_EXAMQUESTIONS_ATTEMPT_LATER  = "AttemptLater";
	final static String KEY_EXAMQUESTIONS_OPTION_NO_SELECTED  = "OptionNoSelected";
	final static String KEY_EXAMQUESTIONS_QUESTION_NO = "QuestionNo";
	Context context;
	public DBHandler_ExamQuestions (Context context) {
		super(context, DBHandler_Main.DB_NAME, null, DBHandler_Main.DB_VERSION);
		this.context = context;
	}
	@Override
	public void onCreate(SQLiteDatabase arg0) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}

	public long insertExamQuestion(long examId,long quesId,int questionNo){
		SQLiteDatabase db = this.getWritableDatabase();		
		
		ContentValues cv = new ContentValues();
		cv.put(KEY_EXAMQUESTIONS_EXAM_ID, examId);
		cv.put(KEY_EXAMQUESTIONS_QUES_ID, quesId);
		cv.put(KEY_EXAMQUESTIONS_QUESTION_NO, questionNo);
		
		long id = db.insert(TABLE_NAME_EXAMQUESTIONS, null, cv);
		
		db.close();
		return id;
	}
	
	public void updateOption(int optionNo,long examQuestionId){
		SQLiteDatabase db = this.getWritableDatabase();		
		
		ContentValues cv = new ContentValues();
		
		cv.put(KEY_EXAMQUESTIONS_OPTION_NO_SELECTED, optionNo);	
		
		db.update(TABLE_NAME_EXAMQUESTIONS, cv, KEY_EXAMQUESTIONS_ID + " = "+examQuestionId, null);
		
		db.close();
	}
	
	public void updateAttempLater(Boolean attemptLater,long examQuestionId){
		SQLiteDatabase db = this.getWritableDatabase();		
		
		ContentValues cv = new ContentValues();
		cv.put(KEY_EXAMQUESTIONS_ATTEMPT_LATER,Globals.getIntFromBool(attemptLater));		
		db.update(TABLE_NAME_EXAMQUESTIONS, cv, KEY_EXAMQUESTIONS_ID + " = "+examQuestionId, null);
		
		db.close();
	}
	
	
	public long questionsRemaining(int quesCatID,int isParentCategory){
		//SELECT  COUNT(*) AS QuestionsLeft  FROM Questions WHERE QuesId NOT IN 
		//(SELECT QuesId FROM ExamsQuestion) AND QuesCatId = 1 And LangId = 1
		
		String quesLeft = "QuestionsLeft";
		SQLiteDatabase db = this.getReadableDatabase();
		String sqlQuery = "SELECT  COUNT(*) AS "+ quesLeft+" FROM "+DBHandler_Questions.TABLE_NAME_QUES+" WHERE "+DBHandler_Questions.KEY_QUES_ID +" NOT IN  (SELECT "+KEY_EXAMQUESTIONS_QUES_ID+" FROM "+ TABLE_NAME_EXAMQUESTIONS+" )"; 
		
		if(isParentCategory == 0 ){ 
			sqlQuery+= " AND "+DBHandler_Questions.KEY_QUES_CAT_ID +" = " + quesCatID;
		}
		if(quesCatID != 2)
		sqlQuery+=" AND "+DBHandler_Questions.KEY_QUES_LANG_ID+" = "+Globals.getAppConfig(context).selectedLangId;
		
		Cursor cursor = db.rawQuery(sqlQuery, null);
		int questionsRem = 0;
		if(cursor != null)
			if(cursor.moveToFirst()){
				questionsRem = cursor.getInt(cursor.getColumnIndex(quesLeft));
			}
		db.close();
		return questionsRem;
	}
	
}
