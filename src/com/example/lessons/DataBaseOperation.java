package com.example.lessons;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseOperation extends SQLiteOpenHelper {

	   
	   public static final String DATABASE_NAME = "LessonPlan";
	   public static final String LESSON_TABLE_NAME = "Lessons";
	   public static final String LESSON_COLUMN_ID = "id";
	   private static final String LESSON_COLUMN_NAME = "subject";
	   private static final String LESSON_COLUMN_TASK = "task";
	   
	   public DataBaseOperation(Context context) {
			super(context, DATABASE_NAME , null, 1);
			
		}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		 db.execSQL(
			      "create table "+LESSON_TABLE_NAME+"" +
			      "(id integer primary key, "+getLessonColumnName()+" text,"+getLessonColumnTask()+" text)"
			      );
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		 
		db.execSQL("DROP TABLE IF EXISTS "+LESSON_TABLE_NAME+"");
	    onCreate(db);
		
	}
	  
	public Cursor getData(int id){
	      SQLiteDatabase db = this.getReadableDatabase();
	      Cursor res =  db.rawQuery( "select * from "+LESSON_TABLE_NAME+" where id="+id+"", null );
	      return res;
	   }
	

	
	public ArrayList<String> getAllLessonPlans()
	   {
	      ArrayList<String> array_list = new ArrayList<String>();
	      //hp = new HashMap();
	      SQLiteDatabase db = this.getReadableDatabase();
	      Cursor res =  db.rawQuery( "select * from "+LESSON_TABLE_NAME+"", null );
	      res.moveToFirst();
	      while(res.isAfterLast() == false){
	      array_list.add(res.getString(res.getColumnIndex(getLessonColumnName())));
	      res.moveToNext();
	      }
	   return array_list;
	   }
	
	public boolean insertLessonPlan(String getSub,String getTask){
		SQLiteDatabase db = this.getWritableDatabase();
	    ContentValues contentValues = new ContentValues();
	    contentValues.put(getLessonColumnName(), getSub);
	    contentValues.put(getLessonColumnTask(), getTask);
	    db.insert(LESSON_TABLE_NAME, null, contentValues);
        return true;
		
		
	}

	public static String getLessonColumnName() {
		return LESSON_COLUMN_NAME;
	}

	public static String getLessonColumnTask() {
		return LESSON_COLUMN_TASK;
	}
	
	 
}
