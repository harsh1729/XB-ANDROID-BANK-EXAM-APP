package com.xercesblue.onlinebankexampo;

import java.util.ArrayList;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHandler_Questions extends SQLiteOpenHelper {

	//CREATE TABLE Questions (QuesId integer NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
	//QuesCatId integer,Question text,CorrectOption integer,Hint text,Solution text,
	//LangId integer, IsFav integer DEFAULT 0, Image blob,SolutionImage blob,BankName text,ExamYear text)

	final static String TABLE_NAME_QUES = "Questions";
	final static String KEY_QUES_ID = "QuesId";
	final static String KEY_QUES_CAT_ID = "QuesCatId";
	final String KEY_QUES_IMAGE = "Image";
	final String KEY_QUES_TEXT = "Question";
	final static String KEY_QUES_CORRECT_OPTION = "CorrectOption";
	final String KEY_QUES_HINT = "Hint";
	final String KEY_QUES_SOLUTION = "Solution";
	final static String KEY_QUES_LANG_ID = "LangId";
	final String KEY_QUES_IF_FAV = "IsFav";
	final String KEY_SOLUTION_IMAGE = "SolutionImage";
	final String KEY_BANK_NAME = "BankName";
	final String KEY_EXAM_YEAR = "ExamYear";

	private Context context;

	@Override
	public void onCreate(SQLiteDatabase arg0) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}

	public DBHandler_Questions (Context context) {
		super(context, DBHandler_Main.DB_NAME, null, DBHandler_Main.DB_VERSION);
		this.context = context;
	}

	public void updateFav(Boolean isFav,long quesId){
		SQLiteDatabase db = this.getWritableDatabase();		

		ContentValues cv = new ContentValues();

		cv.put(KEY_QUES_IF_FAV, Globals.getIntFromBool(isFav));			
		db.update(TABLE_NAME_QUES, cv, KEY_QUES_ID + " = "+quesId, null);		
		db.close();
	}
	public Object_Question getQuestionWithNumber(int quesNo, long examId ){
		//SELECT  Q.* ,E.AttemptLater,E.OptionNoSelected,E.QuestionNo  FROM Questions Q,
		//ExamsQuestion E Where E.Id = 10 AND E.ExamId=3 AND Q.QuesId = E.QuesId And Q.LangId = 1

		//OR

		//SELECT  Q.* ,E.AttemptLater,E.OptionNoSelected,E.QuestionNo  FROM ExamsQuestion E 
		//INNER JOIN Questions Q ON Q.QuesId = E.QuesId Where E.Id = 10 AND E.ExamId=3 AND Q.LangId = 1

		Object_Question obj = null;
		SQLiteDatabase db = this.getReadableDatabase();
		String sqlQuery = "SELECT  Q.* ,E."+DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_ATTEMPT_LATER+
				" , E."+DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_OPTION_NO_SELECTED+
				" , E."+DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_QUESTION_NO+
				" , E."+DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_ID+
				" FROM "+DBHandler_ExamQuestions.TABLE_NAME_EXAMQUESTIONS +" E "+
				" INNER JOIN "+TABLE_NAME_QUES +" Q ON Q."+KEY_QUES_ID+
				" = E."+DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_QUES_ID +
				" WHERE E."+DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_QUESTION_NO+" = "+quesNo+
				" AND E."+DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_EXAM_ID+" = "+examId;
		//" AND Q."+KEY_QUES_LANG_ID+" = "+Globals.langId;
		Cursor cursor = db.rawQuery(sqlQuery, null);
		if(cursor != null){
			if(cursor.moveToFirst()){

				obj = getQuestion(cursor);
				obj.questionNo = cursor.getInt(cursor.getColumnIndex(DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_QUESTION_NO));
				obj.attemptLater = cursor.getInt(cursor.getColumnIndex(DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_ATTEMPT_LATER));
				obj.optionSelected = cursor.getInt(cursor.getColumnIndex(DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_OPTION_NO_SELECTED));
				obj.examQuestionId =  cursor.getInt(cursor.getColumnIndex(DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_ID));

			}

			cursor.close();
		}
		db.close();
		return obj;
	}

	/*
	public int getQuestionCount(int quesCatID,long examID){
		String strCount = "QuesCount";
		//SELECT COUNT(*) AS QuesCount FROM ExamsQuestion WHERE ExamId = 4 AND QuesId IN ( SELECT  QuesId FROM Questions WHERE  QuesCatId=1)

		SQLiteDatabase db = this.getReadableDatabase();
		String sqlQuery = "SELECT COUNT(*) AS "+strCount+" FROM "+DBHandler_ExamQuestions.TABLE_NAME_EXAMQUESTIONS+
				" WHERE "+DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_EXAM_ID + " = "+examID+
				" AND "+ DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_QUES_ID +" IN ( SELECT "+
				KEY_QUES_ID +" FROM "+TABLE_NAME_QUES + " WHERE "+ KEY_QUES_CAT_ID +" = "+quesCatID+");";
		int cnt = 0;
		Cursor cursor = db.rawQuery(sqlQuery, null);
		if(cursor != null)
			if(cursor.moveToFirst()){			
				cnt = cursor.getInt(cursor.getColumnIndex(strCount));
			}
		db.close();

		return cnt;
	}
	public Object_Question getFirstQuestionInCategory(int catId,long examId){
		//SELECT  Q.* ,E.AttemptLater,E.OptionNoSelected,E.QuestionNo  FROM Questions Q,
		//ExamsQuestion E Where  E.ExamId=3 AND Q.QuesCatId = 1 AND E.QuesId = Q.QuesId


		Object_Question obj = null;
		SQLiteDatabase db = this.getReadableDatabase();
		String sqlQuery = "SELECT  Q.* ,E."+DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_ATTEMPT_LATER+
				" , E."+DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_OPTION_NO_SELECTED+
				" , E."+DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_QUESTION_NO+
				" , E."+DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_ID+
				" FROM "+DBHandler_ExamQuestions.TABLE_NAME_EXAMQUESTIONS +" E , "+
				TABLE_NAME_QUES +" Q "+
				" WHERE Q."+KEY_QUES_CAT_ID+" = "+catId+
				" AND E."+DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_EXAM_ID+" = "+examId+
				" AND E."+DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_QUES_ID+" = Q."+ KEY_QUES_ID+
				" ORDER BY "+ DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_QUESTION_NO +" ASC LIMIT 1;";
		Cursor cursor = db.rawQuery(sqlQuery, null);
		if(cursor != null)
			if(cursor.moveToFirst()){

				obj = getQuestion(cursor);
				obj.questionNo = cursor.getInt(cursor.getColumnIndex(DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_QUESTION_NO));
				obj.attemptLater = cursor.getInt(cursor.getColumnIndex(DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_ATTEMPT_LATER));
				obj.optionSelected = cursor.getInt(cursor.getColumnIndex(DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_OPTION_NO_SELECTED));
				obj.examQuestionId =  cursor.getInt(cursor.getColumnIndex(DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_ID));
			}
		db.close();
		return obj;
	}

	 */
	public  ArrayList<Object_Question> getFavouritesQuestions(long catId){

		//SELECT  Q.* ,E.AttemptLater,E.OptionNoSelected,E.QuestionNo,E.Id  FROM ExamsQuestion E 
		//INNER JOIN Questions Q ON Q.QuesId = E.QuesId WHERE E.ExamId=3 AND Q.LangId = 1
		ArrayList<Object_Question> arrayQues = new ArrayList<Object_Question>();
		Object_Question obj = null;
		SQLiteDatabase db = this.getReadableDatabase();
		String sqlQuery = "SELECT  *  FROM "+TABLE_NAME_QUES +
				" WHERE "+KEY_QUES_CAT_ID +" = "+catId+
				" AND "+KEY_QUES_IF_FAV+" = 1 "+
				//" AND "+KEY_QUES_LANG_ID+" = "+Globals.langId+
				" ORDER BY "+KEY_QUES_ID;
		Cursor cursor = db.rawQuery(sqlQuery, null);

		try{
			if(cursor != null){

				if(cursor.moveToFirst()){
					do{

						obj = getQuestion(cursor);
						arrayQues.add(obj);
					}while(cursor.moveToNext());
				}
			}
		}
		finally{
			if (cursor != null)
				cursor.close();
		}


		db.close();

		return arrayQues;
	}

	public  Object_Question getQuestionWithId(long quesId){

		//SELECT  * FROM Question Where QuesId = 

		Object_Question obj = null;
		SQLiteDatabase db = this.getReadableDatabase();
		String sqlQuery = "SELECT  *  FROM "+TABLE_NAME_QUES +
				" WHERE "+KEY_QUES_ID +" = "+quesId;
		
		Cursor cursor = db.rawQuery(sqlQuery, null);
		try{
			if(cursor != null){

				if(cursor.moveToFirst()){
					obj = getQuestion(cursor);
				}
			}
		}
		finally{
			if (cursor != null)
				cursor.close();
		}
		db.close();

		return obj;
	}
	
	public  long getMaxQuestionId(){
		
		SQLiteDatabase db = this.getReadableDatabase();
		long quesId = 0;
		String sqlQuery = "SELECT MAX("+KEY_QUES_ID+") as MaxId FROM  "+TABLE_NAME_QUES +
				" WHERE "+KEY_QUES_LANG_ID+" = "+Globals.getAppConfig(context).selectedLangId;
		
		Cursor cursor = db.rawQuery(sqlQuery, null);
		try{
			if(cursor != null){

				if(cursor.moveToFirst()){
					quesId = cursor.getLong(cursor.getColumnIndex("MaxId"));
				}
			}
		}
		finally{
			if (cursor != null)
				cursor.close();
		}
		db.close();

		return quesId;
	}

	public  ArrayList<Object_Question> getExamQuestions(long examId){

		//SELECT  Q.* ,E.AttemptLater,E.OptionNoSelected,E.QuestionNo,E.Id  FROM ExamsQuestion E 
		//INNER JOIN Questions Q ON Q.QuesId = E.QuesId WHERE E.ExamId=3 AND Q.LangId = 1
		ArrayList<Object_Question> arrayQues = new ArrayList<Object_Question>();
		Object_Question obj = null;
		SQLiteDatabase db = this.getReadableDatabase();
		String sqlQuery = "SELECT  Q.* ,E."+DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_ATTEMPT_LATER+
				" , E."+DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_OPTION_NO_SELECTED+
				" , E."+DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_QUESTION_NO+
				" , E."+DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_ID+
				" FROM "+DBHandler_ExamQuestions.TABLE_NAME_EXAMQUESTIONS +" E "+
				" INNER JOIN "+TABLE_NAME_QUES +" Q ON Q."+KEY_QUES_ID+
				" = E."+DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_QUES_ID +
				" WHERE  E."+DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_EXAM_ID+" = "+examId +
				//" AND Q."+KEY_QUES_LANG_ID+" = "+Globals.langId+
				" ORDER BY E."+DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_QUESTION_NO;
		Cursor cursor = db.rawQuery(sqlQuery, null);

		try{
			if(cursor != null){

				if(cursor.moveToFirst()){
					do{

						obj = getQuestion(cursor);
						obj.questionNo = cursor.getInt(cursor.getColumnIndex(DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_QUESTION_NO));
						obj.attemptLater = cursor.getInt(cursor.getColumnIndex(DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_ATTEMPT_LATER));
						obj.optionSelected = cursor.getInt(cursor.getColumnIndex(DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_OPTION_NO_SELECTED));
						obj.examQuestionId =  cursor.getInt(cursor.getColumnIndex(DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_ID));
						arrayQues.add(obj);
					}while(cursor.moveToNext());
				}
			}
		}
		finally{
			if (cursor != null)
				cursor.close();
		}


		db.close();

		return arrayQues;
	}

	public  Object_Question getNewQuestion(int quesCatID ,long examId){
		//SELECT  *  FROM Questions
		///////////////,
		///////////////////(Select IFNULL(Max(QuestionNo) + 1,1) as QuestionNo FROM ExamsQuestion Where ExamId = 4 )
		//WHERE QuesId NOT IN (SELECT QuesId FROM ExamsQuestion) 
		//AND QuesCatId = 1 AND LangId = 1 ORDER BY QuesId  LIMIT 1 

		SQLiteDatabase db = this.getReadableDatabase();
		String sqlQuery = "SELECT  *  FROM "+ TABLE_NAME_QUES+
				//",(Select IFNULL(Max("+DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_QUESTION_NO+") + 1,1)" +
				//" AS "+DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_QUESTION_NO+
				//" FROM "+DBHandler_ExamQuestions.TABLE_NAME_EXAMQUESTIONS+
				//" WHERE "+DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_EXAM_ID+" ="+examID+") "+
				" WHERE "+KEY_QUES_ID +" NOT IN  (SELECT "+DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_QUES_ID+
				" FROM "+ DBHandler_ExamQuestions.TABLE_NAME_EXAMQUESTIONS+" )"+
				" AND "+KEY_QUES_CAT_ID +" = " + quesCatID;
		if(quesCatID != 2)
			sqlQuery+= " AND "+KEY_QUES_LANG_ID+" = "+Globals.getAppConfig(context).selectedLangId;
				
		sqlQuery+=" ORDER BY "+KEY_QUES_ID+" LIMIT 1 ";

		Cursor cursor = db.rawQuery(sqlQuery, null);
		Object_Question obj = null;
		if(cursor != null){
			if(cursor.moveToFirst()){
				obj = getQuestion(cursor);

			}

			cursor.close();
		}
		db.close();
		if(obj == null){
			obj = getNewRepeatedQuestion(quesCatID,examId);

		}
		return obj;
	}

	private  Object_Question getNewRepeatedQuestion(int quesCatID ,long examID){
		//SELECT  *  FROM Questions,
		/////////////////////////////////////////////////////////////////(Select IFNULL(Max(QuestionNo) + 1,1) as QuestionNo FROM ExamsQuestion Where ExamId = 4 )
		//WHERE QuesId =(SELECT QuesId FROM (SELECT QuesID,ExamId,COUNT(*) 
		//FROM ExamsQuestion  GROUP BY QuesId ORDER BY COUNT(*)  ASC ) WHERE ExamId <> 5 AND 
		//QuesId IN ( SELECT  QuesId FROM Questions WHERE  QuesCatId=1 AND LangId = 1) LIMIT 1)

		SQLiteDatabase db = this.getReadableDatabase();
		String sqlQuery = "SELECT  *  FROM "+ TABLE_NAME_QUES+
				//",(Select IFNULL(Max("+DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_QUESTION_NO+") + 1,1)" +
				//" AS "+DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_QUESTION_NO+
				//" FROM "+DBHandler_ExamQuestions.TABLE_NAME_EXAMQUESTIONS+
				//" WHERE "+DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_EXAM_ID+" ="+examID+") "+
				" WHERE "+KEY_QUES_ID +" = (SELECT "+
				DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_QUES_ID+" FROM (SELECT "+
				DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_QUES_ID+" , "+
				DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_EXAM_ID+" , COUNT(*) FROM "+
				DBHandler_ExamQuestions.TABLE_NAME_EXAMQUESTIONS +" GROUP BY "+
				DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_QUES_ID +
				" ORDER BY COUNT(*)  ASC ) WHERE "+
				DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_EXAM_ID +" <> " + examID+
				" AND "+DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_QUES_ID +" IN ( SELECT " +
				KEY_QUES_ID+" FROM "+TABLE_NAME_QUES+" WHERE "+KEY_QUES_CAT_ID +" = "+ quesCatID;
		if(quesCatID != 2)
			sqlQuery+= " AND "+KEY_QUES_LANG_ID+" = "+Globals.getAppConfig(context).selectedLangId;
				
				
		sqlQuery+=	") LIMIT 1)";
		
		Cursor cursor = db.rawQuery(sqlQuery, null);
		Object_Question obj = null;
		if(cursor != null){
			if(cursor.moveToFirst()){
				obj = getQuestion(cursor);

			}
			cursor.close();
		}

		db.close();
		return obj;
	}

	public void insertNewQuestionWithID(Object_Question question , Map<String,Integer> langSelect)
	{
		SQLiteDatabase db;
		db = this.getWritableDatabase();

		db.beginTransaction();
		try
		{
			ContentValues valuePairs = new ContentValues();
			////*************************INSERT QUESTION DETAILS IN QUESTION TABLE   ***********/////////////////////
			valuePairs.put(KEY_QUES_ID,question.quesId );
			valuePairs.put(KEY_QUES_CAT_ID, question.catId);
			valuePairs.put(KEY_QUES_TEXT, question.question.trim());
			valuePairs.put(KEY_QUES_CORRECT_OPTION, question.correctOption);
			valuePairs.put(KEY_QUES_SOLUTION, question.solution.trim());
			valuePairs.put(KEY_QUES_LANG_ID, langSelect.get(question.langCode));
			valuePairs.put(KEY_BANK_NAME, question.bankName);
			valuePairs.put(KEY_EXAM_YEAR, question.examYear);
			valuePairs.put(KEY_QUES_IMAGE, question.image);
			valuePairs.put(KEY_SOLUTION_IMAGE, question.solutionImage);

			long rowinsert = db.insert(TABLE_NAME_QUES, null, valuePairs);
			Log.i("HARSH", "inserted row Id: "+rowinsert);

			//////////********************INSERT OPTIONS DETAILS IN OPTIONS TABLE  			*******************/////////////////
			valuePairs.clear();
			//for(int i=0;i<question.arrayOptions.size();i++)
			for( Object_Options option : question.arrayOptions)
			{
				valuePairs.put(DBHandler_Options.KEY_OPTIONS_QUES_ID, question.quesId);
				valuePairs.put(DBHandler_Options.KEY_OPTIONS_TEXT, option.optionText);
				valuePairs.put(DBHandler_Options.KEY_OPTIONS_OPTION_NO,option.optionNo);
				valuePairs.put(DBHandler_Options.KEY_OPTIONS_IMAGE,option.image);
				
				long optionInsert = db.insert(DBHandler_Options.TABLE_NAME_OPTIONS, null, valuePairs);
				
				Log.i("HARSH", "option inserted with Id: "+optionInsert);
			}
			db.setTransactionSuccessful();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Log.i("HARSH", "Eror whi8ole inserting -- "+e.getMessage());
		}
		finally
		{
			db.endTransaction();			 
		}
		db.close();
	}

	

	private Object_Question getQuestion(Cursor cursor){

		Object_Question obj = new Object_Question();
		obj.quesId = cursor.getLong(cursor.getColumnIndex(KEY_QUES_ID));
		obj.catId = cursor.getInt(cursor.getColumnIndex(KEY_QUES_CAT_ID));
		obj.langId = cursor.getInt(cursor.getColumnIndex(KEY_QUES_LANG_ID));
		obj.correctOption= cursor.getInt(cursor.getColumnIndex(KEY_QUES_CORRECT_OPTION));
		obj.isFav= cursor.getInt(cursor.getColumnIndex(KEY_QUES_IF_FAV));
		//obj.questionNo = cursor.getInt(cursor.getColumnIndex(DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_QUESTION_NO));
		obj.optionSelected = 0;
		obj.attemptLater = 0;

		obj.question = cursor.getString(cursor.getColumnIndex(KEY_QUES_TEXT));
		obj.solution= cursor.getString(cursor.getColumnIndex(KEY_QUES_SOLUTION));
		obj.hint = cursor.getString(cursor.getColumnIndex(KEY_QUES_HINT));

		obj.image = cursor.getBlob(cursor.getColumnIndex(KEY_QUES_IMAGE));

		obj.solutionImage = cursor.getBlob(cursor.getColumnIndex(KEY_SOLUTION_IMAGE));
		obj.bankName = cursor.getString(cursor.getColumnIndex(KEY_BANK_NAME));
		obj.examYear= cursor.getString(cursor.getColumnIndex(KEY_EXAM_YEAR));

		DBHandler_Options dbH = new DBHandler_Options(context);
		obj.arrayOptions = dbH.getOptions(obj.quesId);

		return obj;
	}



}
