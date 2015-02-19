package com.xercesblue.onlinebankexampo;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler_Exam extends SQLiteOpenHelper{

	//CREATE TABLE Exam (ExamId integer NOT NULL PRIMARY KEY AUTOINCREMENT,ExamName text,ExamDate text, 
	//QuesCategoryId integer, TotalQues integer, DurationMins integer, DurationHours integer,ExamLanguage text)
		
		final String TABLE_NAME_EXAM = "Exam";
		final String KEY_EXAM_ID = "ExamId";
		final String KEY_EXAM_NAME = "ExamName";
		final String KEY_EXAM_DATE = "ExamDate";
		final String KEY_EXAM_QUES_CAT_ID = "QuesCategoryId";
		final String KEY_EXAM_TOTAL_QUES= "TotalQues";
		final String KEY_EXAM_DURATION_MINS = "DurationMins";
		final String KEY_EXAM_DURATION_HOURS = "DurationHours";
		final String KEY_EXAM_LANGUAGE = "ExamLanguage";

		public DBHandler_Exam (Context context) {
			super(context, DBHandler_Main.DB_NAME, null, DBHandler_Main.DB_VERSION);
		}
		@Override
		public void onCreate(SQLiteDatabase arg0) {

		}

		@Override
		public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

		}
		
		public long startNewExam(Object_Exam objExam){		
			
			SQLiteDatabase db = this.getWritableDatabase();		
			
			ContentValues cv = new ContentValues();
			cv.put(KEY_EXAM_NAME, objExam.examName);
			cv.put(KEY_EXAM_DATE, objExam.examDate);
			cv.put(KEY_EXAM_QUES_CAT_ID, objExam.quesCategoryId);
			cv.put(KEY_EXAM_DURATION_HOURS, objExam.durationHours);
			cv.put(KEY_EXAM_DURATION_MINS, objExam.durationMins);
			cv.put(KEY_EXAM_TOTAL_QUES, objExam.totalQues);
			cv.put(KEY_EXAM_LANGUAGE, objExam.examLang);
			
			long rowID = db.insert(TABLE_NAME_EXAM, null, cv);
			db.close();
			return rowID; //-1 if error occurs
		}
	
		public int getLastExamID(){
			//SELECT MAX(ExamId)  as MaxId FROM Exam;
			String maxId = "MaxId";
			SQLiteDatabase db = this.getReadableDatabase();
			String sqlQuery = "SELECT MAX("+KEY_EXAM_ID+ ") as "+maxId+" FROM "+  TABLE_NAME_EXAM;
			Cursor cursor = db.rawQuery(sqlQuery, null);
			int lastExamID = 0;
			if(cursor != null)
				if(cursor.moveToFirst()){
					lastExamID = cursor.getInt(cursor.getColumnIndex(maxId));
				}
			db.close();
			return lastExamID;
		}
		
		public ArrayList<Object_Exam> getExamsList(long examId){
			//SELECT E.* ,QC.MarksEachQues,QC.NegativeMarkEachQues ,
			//(SELECT  Count (*) FROM ExamsQuestion EQ WHERE EQ.ExamId  = E.ExamId AND  EQ.OptionNoSelected > 0) 
			//AS attemptedQuestion ,  (SELECT Count (*) FROM ExamsQuestion  EQ   
			//INNER JOIN Questions Q ON    EQ.QuesId = Q.QuesId  AND EQ.OptionNoSelected =
			//Q.CorrectOption WHERE EQ.ExamId  = E.ExamId) AS correctQuestions FROM  Exam E 
			//INNER JOIN QuestionCategory QC ON  E.QuesCategoryId = QC.QuesCatId
			
			ArrayList<Object_Exam> examList = new ArrayList<Object_Exam>();
			String keyAttempted = "AttemptedQuestion";
			String keyCorrect = "CorrectQuestions";
			
			SQLiteDatabase db = this.getReadableDatabase();
			String sqlQuery = "SELECT E.* ,QC."+DBHandler_QuestionCategory.KEY_QCAT_MARK+
					",QC."+DBHandler_QuestionCategory.KEY_QCAT_NAME+
					",QC."+ DBHandler_QuestionCategory.KEY_QCAT_NEGATIVE_MARK+", ( SELECT  Count (*) FROM "+
					DBHandler_ExamQuestions.TABLE_NAME_EXAMQUESTIONS +" EQ WHERE EQ."+
					DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_EXAM_ID +" = E."+KEY_EXAM_ID+
					" AND EQ."+DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_OPTION_NO_SELECTED+" > 0) AS "+keyAttempted+
					" , (SELECT Count (*) FROM "+ DBHandler_ExamQuestions.TABLE_NAME_EXAMQUESTIONS + " EQ INNER JOIN "+
					DBHandler_Questions.TABLE_NAME_QUES +" Q ON EQ."+DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_QUES_ID +
					" = Q."+DBHandler_Questions.KEY_QUES_ID+ " AND EQ."+DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_OPTION_NO_SELECTED+
					" = Q."+DBHandler_Questions.KEY_QUES_CORRECT_OPTION +"  WHERE EQ."+
					DBHandler_ExamQuestions.KEY_EXAMQUESTIONS_EXAM_ID +" = E."+KEY_EXAM_ID+" ) AS "+keyCorrect+
					" FROM "+TABLE_NAME_EXAM +" E INNER JOIN "+ DBHandler_QuestionCategory.TABLE_NAME_QCATEGORY+" QC ON E."+
					KEY_EXAM_QUES_CAT_ID +" = QC."+DBHandler_QuestionCategory.KEY_QCAT_ID;
			if(examId > 0){
				sqlQuery += " WHERE "+KEY_EXAM_ID +" = "+examId;
			}
			Cursor cursor = db.rawQuery(sqlQuery, null);
			if(cursor != null){
				if(cursor.moveToFirst())
				do{
					Object_Exam obj = new Object_Exam();
					
					obj.examId =  cursor.getInt(cursor.getColumnIndex(KEY_EXAM_ID));
					obj.durationHours =  cursor.getInt(cursor.getColumnIndex(KEY_EXAM_DURATION_HOURS));
					obj.durationMins =  cursor.getInt(cursor.getColumnIndex(KEY_EXAM_DURATION_MINS));
					obj.totalQues =  cursor.getInt(cursor.getColumnIndex(KEY_EXAM_TOTAL_QUES));
					obj.quesCategoryId =  cursor.getInt(cursor.getColumnIndex(KEY_EXAM_QUES_CAT_ID));
					obj.examName = cursor.getString(cursor.getColumnIndex(KEY_EXAM_NAME));
					obj.examDate = cursor.getString(cursor.getColumnIndex(KEY_EXAM_DATE));
					obj.attemptedQuestion = cursor.getInt(cursor.getColumnIndex(keyAttempted));
					obj.correctQuestion= cursor.getInt(cursor.getColumnIndex(keyCorrect));
					obj.quesCatName = cursor.getString(cursor.getColumnIndex(DBHandler_QuestionCategory.KEY_QCAT_NAME));
					obj.markEachQues = cursor.getFloat(cursor.getColumnIndex(DBHandler_QuestionCategory.KEY_QCAT_MARK));
					obj.negativeMarkEachQues  = cursor.getFloat(cursor.getColumnIndex(DBHandler_QuestionCategory.KEY_QCAT_NEGATIVE_MARK));
					obj.examLang = cursor.getString(cursor.getColumnIndex(KEY_EXAM_LANGUAGE));
					examList.add(obj);
				}while(cursor.moveToNext());
			}
			cursor.close();
			db.close();
			
			return examList;
		}
		
		public void deleteExam(int examId){
			SQLiteDatabase db = this.getWritableDatabase();
			
			db.delete(TABLE_NAME_EXAM, KEY_EXAM_ID +" = "+examId, null);
		}
		
		
		//To delete and reset sequence
		///DELETE FROM Exam;
		//DELETE FROM SQLITE_SEQUENCE WHERE NAME ='Exam';
		//To copy from one database to other
		//INSERT INTO TutorialsTopics (TutorialTopicId, CatId, Topics) 
		//SELECT * FROM db2.TutorialsTopics;
}
