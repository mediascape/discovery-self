/* Long Library Name:
**	ServiceBoot Class
**
** Acronym and its version:
**	ServiceBoot v1.0
**
** Copyright claim:
**	Copyright ( C ) 2013-2014 Vicomtech-IK4 ( http://www.vicomtech.org/ ),
**	all rights reserved.
**
** Authors (in alphabetical order):
**	Angel Martin <amartin@vicomtech.org>,
**	Iñigo Tamayo <itamayo@vicomteh.org>,
**	Ion Alberdi <ialberdi@vicomtech.org>
**
** Description:		
**	The ServiceBoot class extends Service and boots the different services used for the publication of 
**  UPnP information. The class boots RestFul and UPnP services and implements necessary
**  subclasses like BrowseRegistryListener and DeviceDisplay.
** 
** Development Environment:
** 	The software has been implemented in Java, and tested in Chrome 
**	browsers and Android 4.3 OS devices.
**
** Dependencies:
** 	As ServiceBoot class depends on other libraries, the user must adhere to and 
**	keep in place any Licencing terms of those libraries:
**      Android v4.4.2 (http://developer.android.com/)
**		Restlet v2.1 (http://restlet.com/)
**		Cling v2.0-alpha3  (http://4thline.org/projects/cling/)
** 
** Licenses dependencies:
**	License Agreement for Restlet:
**		Apachev2 license (http://opensource.org/licenses/apache-2.0)
**		GNU LGPLv3 license (http://opensource.org/licenses/lgpl-3.0)
**		GNU LGPLv2.1 license (http://opensource.org/licenses/lgpl-2.1)
**		CDDLv1 license (http://opensource.org/licenses/cddl1)
**		EPLv1 license (http://opensource.org/licenses/eclipse-1.0)
**  License Agreement for Cling:
**		GNU LGPLv3 (http://www.gnu.org/licenses/lgpl.html)
**
*/
		
package com.example.discoveryagentrest;

import java.util.Set;
import java.util.Vector;

import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Log;

public class BluetoothService extends Service{
		//variables for the different sensors.
		public static Context context;
		public static final int REQUEST_ENABLE_BT = 1;
		public static BluetoothAdapter bluetoothSensor = null;
		public static Vector<BluetoothDevice> listBluetooth;
		public static Vector<Parcelable> listBluetoothServices;
			
		private BroadcastReceiver mBluetoothInfoReceiver = new BroadcastReceiver(){
		    @SuppressWarnings("unused")
			@SuppressLint("InlinedApi") 
		    @Override
			public void onReceive(Context ctxt, Intent intent) {
		    	if(intent.getAction().equals(BluetoothDevice.ACTION_FOUND))
		    	{
		    		Log.d("MyApp", "ACTION_FOUND");
		    		BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
		    		Log.d("MyApp", "contains: "+listBluetooth.contains(device));
		    		if (!listBluetooth.contains(device)) {
			    		Log.d("MyApp", "add "+device.getName()+" "+device.getType()+" "+device.getAddress()+" "+device.getBondState()+" "+device.getBluetoothClass());
			    		listBluetooth.add(device);
		    		}
		    		
		    		Set<BluetoothDevice> pairedDevices = bluetoothSensor.getBondedDevices();
		    		// If there are paired devices
		    		Log.d("MyApp", "PairedDevices: "+pairedDevices.size());
		    		if (pairedDevices.size() > 0) {
		    		    // Loop through paired devices
		    		    for (BluetoothDevice device1 : pairedDevices) {
		    		        // Add the name and address to an array adapter to show in a ListView
		    		    	Log.d("MyApp", "Paired Devices:");
		    		    	Log.d("MyApp", device.getName()+" "+device.getType());
		    		    }
		    		}
		    		Log.d("MyApp", "BluetoothList: "+listBluetooth.toString());
		    	}else if(intent.getAction().equals(BluetoothDevice.ACTION_UUID))
		    		{
			    		Log.d("MyApp", "ACTION_UUID");
			    		BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			    		Parcelable[] uuidExtra = intent.getParcelableArrayExtra(BluetoothDevice.EXTRA_UUID);
			    		Log.d("MyApp", device.getName());
			    		for (int i=0; i<uuidExtra.length; i++) {
			    			Log.d("MyApp", uuidExtra[i].toString());
			    			listBluetoothServices.add(uuidExtra[i]);
			    		}
			    	}else if(intent.getAction().equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)){
			    			Log.d("MyApp", "Discovery Start");
			    			listBluetooth.clear();
			    			listBluetoothServices.clear();
			    		}else if(intent.getAction().equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)){
				    			if(!bluetoothSensor.isDiscovering()){
				    				Log.d("MyApp", "Discovery Finished");
				    				final Handler handler = new Handler();
				    				handler.postDelayed(new Runnable() {
				    				  @Override
				    				  public void run() {
				    					  if(bluetoothSensor.isEnabled()) bluetoothSensor.startDiscovery();
				    				  }
				    				}, 30000);
				    			}
				    		}else if(intent.getAction().equals(BluetoothAdapter.ACTION_STATE_CHANGED)){
				    				final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
				    				listBluetooth.clear();
				    				listBluetoothServices.clear();
				    				switch (state) {
					    	            case BluetoothAdapter.STATE_OFF:
					    	            	Log.d("MyApp", "Bluetooth Off");
					    	            	if(bluetoothSensor.isDiscovering()) bluetoothSensor.cancelDiscovery();
					    	                break;
					    	            case BluetoothAdapter.STATE_ON:
					    	            	Log.d("MyApp", "Bluetooth On");
					    	            	if(bluetoothSensor.isEnabled()) bluetoothSensor.startDiscovery();
					    	                break;
				    	            }
				    			}
			}
		};
		
		@SuppressLint("InlinedApi") @Override
		public void onCreate() {
			super.onCreate();
			
			listBluetooth= new Vector<BluetoothDevice>();
	    	listBluetoothServices= new Vector<Parcelable>();
			
			context = this;
			
			IntentFilter filterBluetooth = new IntentFilter();
			filterBluetooth.addAction(BluetoothDevice.ACTION_FOUND);		
			filterBluetooth.addAction(BluetoothDevice.ACTION_UUID);	
			filterBluetooth.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
			filterBluetooth.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
			filterBluetooth.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
						
			this.registerReceiver(this.mBluetoothInfoReceiver , filterBluetooth);		
			
			bluetoothSensor = BluetoothAdapter.getDefaultAdapter();
		    
		    if (bluetoothSensor == null) {
			    // Device does not support Bluetooth
				Log.d("MyApp", "Bluetooth Sensor : False");
			}else{
				if (!bluetoothSensor.isEnabled()) Log.d("MyApp", "Bluetooth Enabled : False");
				else if(bluetoothSensor.isEnabled()){
					if(bluetoothSensor.isDiscovering()) bluetoothSensor.cancelDiscovery();
									
					bluetoothSensor.startDiscovery();
					Log.d("MyApp", "Bluetooth Enabled : True");
				}
			}    	
	    	Log.d("MyApp", "Bluetooth Service created");
		}
		
		@Override
		public void onDestroy() {
			Log.d("MyApp", "onDestroy");
			super.onDestroy();
			//Unbind UPnP service
			if (bluetoothSensor.isDiscovering()) {
				bluetoothSensor.cancelDiscovery();
			}
			Log.d("MyApp", "Bluetooth Service destroyed");
		}

		@Override
		public IBinder onBind(Intent intent) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
	    public int onStartCommand(Intent intent, int flags, int startId) {
		    Log.d("MyApp", "onStartCommandBluetooth");
		    // TODO Auto-generated method stub
	        return super.onStartCommand(intent, flags, startId);
		}
}
