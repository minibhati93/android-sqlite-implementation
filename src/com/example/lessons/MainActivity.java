package com.example.lessons;


import java.util.ArrayList;

import android.support.v7.app.ActionBarActivity;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends ActionBarActivity {
	private DataBaseOperation db;
	private ListView obj;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#33cc5c")));
		
		setContentView(R.layout.activity_main);
		db = new DataBaseOperation(this);
		ArrayList<String> array_list=db.getAllLessonPlans();
		ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,array_list);
		obj = (ListView)findViewById(R.id.listView1);
		obj.setAdapter(arrayAdapter);
		obj.setOnItemClickListener(new OnItemClickListener(){
			
		     
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d("Main", "clicked "+position);
				Bundle dataBundle = new Bundle();
		        dataBundle.putInt("id", (position+1));
		        Intent intent = new Intent(getApplicationContext(),com.example.lessons.DisplayLesson.class);
		        intent.putExtras(dataBundle);
		        startActivity(intent);
				
				
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		//return true;
		return super.onCreateOptionsMenu(menu);
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
		else if(id== R.id.action_add){
			Intent openStart=new Intent("com.example.lessons.ADDLESSONPLAN");
			startActivity(openStart);
		}
		return super.onOptionsItemSelected(item);
	}
}
