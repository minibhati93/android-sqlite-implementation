package com.example.lessons;

import android.support.v7.app.ActionBarActivity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddLessonPlan extends ActionBarActivity {

	DataBaseOperation db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_lesson_plan);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_lesson_plan, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		else if(id== R.id.action_save){
			save();
		}
		return super.onOptionsItemSelected(item);
	}
	public void showTimePickerDialog(View v) {
	    DialogFragment newFragment = new TimePickerFragment();
	    newFragment.show(getFragmentManager(), "timePicker");
	    
	}
	public void showDatePickerDialog(View v) {
	    DialogFragment newFragment = new DatePickerFragment();
	    newFragment.show(getFragmentManager(), "datePicker");
	}
	private void save(){
		Log.d("AddLessonPlan", "save function running");
		
//		Context context = getApplicationContext();
//		CharSequence text = "Hello toast!";
//		int duration = Toast.LENGTH_SHORT;
//
//		Toast toast = Toast.makeText(context, text, duration);
//		toast.show();
		db = new DataBaseOperation(this);
		EditText editText=(EditText) findViewById(R.id.subject);
		String subject=editText.getText().toString();
		Log.d("AddLessonPlan", "subject is : "+subject);
		
		EditText editText1=(EditText) findViewById(R.id.task);
		String task=editText1.getText().toString();
		Log.d("AddLessonPlan", "task is : "+task);
		
		boolean flag=db.insertLessonPlan(subject, task);
		if(flag==true){
			Log.d("AddLessonPlan", "Data is inserted");
			Context context = getApplicationContext();
			CharSequence text = "Inserted!";
			int duration = Toast.LENGTH_SHORT;
	
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
		else{
			Context context = getApplicationContext();
			CharSequence text = "Not Inserted!";
			int duration = Toast.LENGTH_SHORT;
	
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
		
		
	}
}
