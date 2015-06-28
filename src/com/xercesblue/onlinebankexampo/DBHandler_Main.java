package com.xercesblue.onlinebankexampo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHandler_Main extends SQLiteOpenHelper {

	public  final static int DB_VERSION = 1;
	public static final String DB_NAME = "OnlineExamDB1.4.sqlite";
	private  String DB_PATH = "";
	public static final String DB_NAME_OLD = "OnlineExamDB1.3.sqlite";
	private  String DB_PATH_OLD = "";
	private  Context myContext;

	public DBHandler_Main(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		this.myContext = context;
		try{
			DB_PATH = this.myContext.getDatabasePath(DB_NAME).getPath();
			DB_PATH_OLD = this.myContext.getDatabasePath(DB_NAME_OLD).getPath();
		}catch(Exception ex){
			
		}
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		
	}

	public void deleteOldDB(Context context)
	{
		
	    try{
	    		Log.i("HARSH", "Deleting Database");
	    		if(!context.deleteDatabase(DB_NAME_OLD)){
	    			File file = new File(DB_PATH_OLD);
	    			if(file.delete()){
	    				Log.i("HARSH", "Database deleted");
	    			}
	    		}else{
	    			Log.i("HARSH", "Database deleted");
	    		}
	    	
	            
	    }catch(Exception ex)
	    {
	    	Log.i("HARSH", "Error Database deleted");
	    }
	}
	
	public void createDataBase() throws IOException{

		boolean dbExist = checkDataBase();

		if(dbExist){
			//do nothing - database already exist
			return;
		}

		//By calling this method an empty database will be created into the default system path
		//of your application so we will be able to overwrite that database with our database.
		this.getReadableDatabase();

		
		try {

			copyDataBase();

		} catch (IOException e) {

			//throw new Error("Error copying database");
			throw new IOException("Error copying database");

		}

	}

	private void copyDataBase() throws IOException{

		InputStream myInput = myContext.getAssets().open(DB_NAME);
		OutputStream myOutput = new FileOutputStream(DB_PATH );

		//transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer))>0){
			myOutput.write(buffer, 0, length);
		}

		//Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

		Log.i("DBHandler_Test"," Database copied Succesfully !");

	}

	private boolean checkDataBase(){
		SQLiteDatabase checkDB = null;

		try{
			checkDB = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READONLY);
			Log.i("DBHandler_Test","Datbase Exists !");

		}//catch(SQLiteCantOpenDatabaseException e){
		catch(SQLiteException e){
			Log.e("DBHandler_Test","ERROR ERROR");
			//database does't exist yet.

		}

		if(checkDB != null){

			checkDB.close();
			return true;

		}

		return false;
	}
}
