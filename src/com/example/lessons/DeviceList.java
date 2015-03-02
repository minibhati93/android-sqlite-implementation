package com.example.lessons;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class DeviceList extends ActionBarActivity{

    private ProgressDialog mProgressDlg;
	
	private ArrayList<BluetoothDevice> mDeviceList = new ArrayList<BluetoothDevice>();
	private ArrayList<BluetoothDevice> mpairedList = new ArrayList<BluetoothDevice>();
	 
	private BluetoothAdapter mBluetoothAdapter;
	
	private ListView lv;
	
	private ArrayAdapter<String> mArrayAdapter ;
	private OutputStream outputStream;
	private InputStream inStream;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#33cc5c")));
		setContentView(R.layout.list_devices);
		
        mBluetoothAdapter	= BluetoothAdapter.getDefaultAdapter();
		
		mProgressDlg 		= new ProgressDialog(this);
		
		mProgressDlg.setMessage("Scanning...");
		mProgressDlg.setCancelable(false);
		mProgressDlg.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        dialog.dismiss();
		        
		        mBluetoothAdapter.cancelDiscovery();
		    }
		});
		 
         
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
	    ArrayList<String> list1 = new ArrayList<String>();
   	 
		// If there are paired devices
		if (pairedDevices.size() > 0) {
		    // Loop through paired devices
		    for (BluetoothDevice device : pairedDevices) {
		        // Add the name and address to an array adapter to show in a ListView
		    	Log.d("Main", "paired device: "+device.getName());
		        list1.add(device.getName());
		        if(!mpairedList.contains(device)){
		        	mpairedList.add(device);
	            }
		        
		    }
		    lv =(ListView)findViewById(R.id.devices);
            mArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list1);
            lv.setAdapter(mArrayAdapter);
            lv.setOnItemLongClickListener(new OnItemLongClickListener(){

				@Override
				public boolean onItemLongClick(AdapterView<?> parent,
						View view, int position, long id) {
				    
					final BluetoothDevice device = mpairedList.get(position);
				    ListView t1=showAlert();
					t1.setOnItemClickListener(new OnItemClickListener(){

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							// TODO Auto-generated method stub
							
							
							unpairDevice(device);
							
						}

						 
						
					});
					return false;
				}

				
            	
            });
            lv.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					BluetoothDevice device = mpairedList.get(position);
					ParcelUuid[] uuids = device.getUuids();
					 try {
					BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
	                socket.connect();
	                outputStream = socket.getOutputStream();
	                inStream = socket.getInputStream();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 
	                
					
				}
            	
            });
            
            
        	
		}
		 
	}
	public void write(String s) throws IOException {
	    outputStream.write(s.getBytes());
	}

	public void run() {
	    final int BUFFER_SIZE = 1024;
	    byte[] buffer = new byte[BUFFER_SIZE];
	    int bytes = 0;
	    

	    while (true) {
	        try {
	            bytes = inStream.read(buffer, bytes, BUFFER_SIZE - bytes);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_devices, menu);
		//return true;
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.scan_device) {
			mProgressDlg.show();
			getAvailableDevices();
		}
		
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onPause() {
		if (mBluetoothAdapter != null) {
			if (mBluetoothAdapter.isDiscovering()) {
				mBluetoothAdapter.cancelDiscovery();
			}
		}
		
		super.onPause();
	}
	
	@Override
	public void onDestroy() {
		unregisterReceiver(mReceiver);
		
		super.onDestroy();
	}
	
	protected void onStop() {
	    super.onStop();  // Always call the superclass method first

	    // Save the note's current draft, because the activity is stopping
	    // and we want to be sure the current note progress isn't lost.
	    
	}
	
	
	private void getAvailableDevices() {
		if (mBluetoothAdapter.isDiscovering()){
			 mBluetoothAdapter.cancelDiscovery();
		    }
		
		mBluetoothAdapter.startDiscovery();
        IntentFilter filter = new IntentFilter();
		
		filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		filter.addAction(BluetoothDevice.ACTION_FOUND);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		 
		
		registerReceiver(mReceiver, filter);
		 
		 
		
	}
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
	    public void onReceive(Context context, Intent intent) {	    	
	        String action = intent.getAction();
	        
	        if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
	        	final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
	        	 
	        	if (state == BluetoothAdapter.STATE_ON) {

	        	}
	        } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
	        	mDeviceList = new ArrayList<BluetoothDevice>();
				
				
	        } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
	        	 mProgressDlg.dismiss();
	        	 final ArrayList<String> list = new ArrayList<String>();
	        	 Iterator<BluetoothDevice> itr = mDeviceList.iterator();
	             while (itr.hasNext()) {
	               // Get Services for paired devices
	               BluetoothDevice device = itr.next();
	               Log.d("Main", "devices found: "+device.getName());
	               list.add(device.getName());
 	             } 
	             
  
	             mArrayAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,list);
	             lv.setAdapter(mArrayAdapter);
	             lv.setOnItemClickListener(new OnItemClickListener(){

	     			@Override
	     			public void onItemClick(AdapterView<?> parent, View view,
	     					int position, long id) {
	     				
	     				//Log.d("Main", "devices found: "+deviceList.get(position));
	     	               
	      	            showToast("Pairing device "+list.get(position)+"...");
	      	            BluetoothDevice device = mDeviceList.get(position);
	      	            pairDevice(device);
	     			}

	     	
	     		});
 	        	
	        	
	        } 
	        else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	        	BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	        	if(!mDeviceList.contains(device)){
	        		mDeviceList.add(device);
	            }
	        	
	        	
	        
	        }
	        else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {	        	
	        	 final int state 		= intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
	        	 final int prevState	= intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);
	        	 
	        	 if (state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING) {
	        		 showToast("Paired");
	        	 } else if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED){
	        		 showToast("Unpaired");
	        	 }
	        	  
	        	 mArrayAdapter.notifyDataSetChanged();
	        }
	    }
	};
	
	public void showToast(String msg){
		
		Toast.makeText(getApplicationContext(),msg  ,Toast.LENGTH_LONG).show();
		
	}
	private void pairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	private  ListView showAlert() {
		String names[]={"Unpair"};
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(DeviceList.this);
		LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.list_devices, null);
        alertDialog.setView(convertView);
         
        ListView lv1 = (ListView) convertView.findViewById(R.id.devices1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,names);
        lv1.setAdapter(adapter);
        alertDialog.show();
        return lv1;
		
	}
	
	private void unpairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("removeBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
            showToast("Unpaired");
            mArrayAdapter.notifyDataSetChanged();
            return;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	

	
	
}
