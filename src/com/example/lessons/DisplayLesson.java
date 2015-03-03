package com.example.lessons;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayLesson extends ActionBarActivity{
	
	private static final int REQUEST_ENABLE_BT = 1;
	private DataBaseOperation db;
	private TextView Lesson_Sub ;
	private TextView Lesson_Task ;
	private String names[] ={"Dropbox","Box","Email","Evernote","Bluetooth"};
	
	private BluetoothAdapter mBluetoothAdapter;
    private AlertDialog.Builder alertDialog;
	
    
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#33cc5c")));
		setContentView(R.layout.display_lesson_plan);
		Lesson_Sub = (TextView) findViewById(R.id.editText1);
		Lesson_Task = (TextView) findViewById(R.id.editText2);
		
		db = new DataBaseOperation(this);
		Bundle extras = getIntent().getExtras(); 
		if(extras !=null) {
			int val=extras.getInt("id");
			if(val>0){
				Cursor rs=db.getData(val);
				rs.moveToFirst();
	            String nam = rs.getString(rs.getColumnIndex(DataBaseOperation.getLessonColumnName()));
	            String phon = rs.getString(rs.getColumnIndex(DataBaseOperation.getLessonColumnTask()));
	            
	            if (!rs.isClosed()) 
	            {
	               rs.close();
	            }
	            Lesson_Sub.setText((CharSequence)nam);
	            Lesson_Sub.setFocusable(false);
	            Lesson_Sub.setClickable(false);

	            Lesson_Task.setText((CharSequence)phon);
	            Lesson_Task.setFocusable(false); 
	            Lesson_Task.setClickable(false);
	            
	            this.setTitle(nam);

			}
	    }
		
		
	
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_lesson, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_edit) {
			return true;
		}
		else if(id==R.id.action_share){
			showAlert();
		}
//		else if(id== R.id.action_add){
//			Intent openStart=new Intent("com.example.lessons.ADDLESSONPLAN");
//			startActivity(openStart);
//		}
		return super.onOptionsItemSelected(item);
	}
	
	protected void onActivityResult(int requestCode, int resultCode,
            Intent data) {
   
            
	   Log.d("Main " , "resultCode: "+resultCode);
        switch (requestCode) {
//        case REQUEST_CONNECT_DEVICE:
//            // When DeviceListActivity returns with a device to connect
//            if (resultCode == Activity.RESULT_OK) {
//                // Get the device MAC address
//                String address = data.getExtras()
//                                     .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
//                // Get the BLuetoothDevice object
//                BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
//                // Attempt to connect to the device
//                mChatService.connect(device);
//            }
//            break;
        case REQUEST_ENABLE_BT:
            // When the request to enable Bluetooth returns
            if (resultCode == Activity.RESULT_OK) {
                // Bluetooth is now enabled,
            	
            	 
            	Intent i=new Intent("com.example.lessons.DEVICELIST");
    			startActivity(i);
            	 

                 
            } else {
                // User did not enable Bluetooth or an error occured
            	 
            	Toast.makeText(getApplicationContext(),"Bluetooth connection failed"  ,Toast.LENGTH_LONG).show();
                finish();
            }
        }
        
	 }
	
	
	private void showAlert() {
		
		alertDialog = new AlertDialog.Builder(DisplayLesson.this);
		LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.list_alert_dialog, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("Share via");
        ListView lv = (ListView) convertView.findViewById(R.id.listView1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,names);
        lv.setAdapter(adapter);
        alertDialog.show();
		
        lv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(position == 2){
					Log.d("Main", "clicked Email");
					//Intent intent=new Intent("com.example.lessons.SENDEMAIL");
	    			//startActivity(intent);
					startComposeEmail();
				}
				else if(position==4){
					
					Log.d("Main", "clicked Bluetooth");
					startBlueTooth();
					 
					 
					
				}
			}

			

	
		});
	}
	protected void startComposeEmail() {
		// TODO Auto-generated method stub
		
		Log.i("email", "starting email composer");
		Calendar c = Calendar.getInstance();
		//System.out.println("Current time => " + c.getTime());
		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
		String formattedDate = df.format(c.getTime());

	      //String emailContent = Lesson_Sub+" "+Lesson_Task;
	      Intent emailIntent = new Intent(Intent.ACTION_SEND);
	      emailIntent.setData(Uri.parse("mailto:"));
	      emailIntent.setType("text/plain");


	      emailIntent.putExtra(Intent.EXTRA_EMAIL, "");
	      emailIntent.putExtra(Intent.EXTRA_CC, "");
	      emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Lesson Planner: "+formattedDate);
	      emailIntent.putExtra(Intent.EXTRA_TEXT, "");

	      try {
	         startActivity(Intent.createChooser(emailIntent, "Send mail..."));
	         finish();
	         Log.i("email", "Finished sending email");
	         //showToast("Email sent");
	      } catch (android.content.ActivityNotFoundException ex) {
	    	  
	         showToast("No email client installed");
	         
	      }
	}
	private void startBlueTooth() {
		 
	    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			 Toast.makeText(getApplicationContext(),"Not supported"  ,Toast.LENGTH_LONG).show();
		}
		if (!mBluetoothAdapter.isEnabled()) {
			//Bluetooth is not enable
			
		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		  
		     
		}
		else{
			Intent i=new Intent("com.example.lessons.DEVICELIST");
			startActivity(i);
		}
		
	}
	public void showToast(String message){
		Context context = getApplicationContext();
		CharSequence text = message;
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
	
	 

	 
}

