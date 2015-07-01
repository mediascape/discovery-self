/*
** Long Library Name:
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

import java.util.Vector;

import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.routing.Router;
import org.teleal.cling.android.AndroidUpnpService;
import org.teleal.cling.android.AndroidUpnpServiceImpl;
import org.teleal.cling.model.meta.Device;
import org.teleal.cling.model.meta.LocalDevice;
import org.teleal.cling.model.meta.RemoteDevice;
import org.teleal.cling.registry.DefaultRegistryListener;
import org.teleal.cling.registry.Registry;

import com.example.discoveryagentrest.R;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.input.InputManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.InputDevice;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.Toast;

@SuppressLint("InlinedApi") public class ServiceBoot extends Service implements SensorEventListener{
	//variables for the different sensors.
	//private int mInterval = 60000; //1 minute
	//public Handler customHandler;
	
	//variables for the different sensors.
	public static Context context;
	public static int screenWidth = 0;
	public static int screenHeight = 0;	
	public static final int REQUEST_ENABLE_BT = 1;
	public static String touchScreenExtra = "";
	
	public static boolean batterySensor = false;
	public static int level = 0;
	public static boolean cameraSensor = false;
	public static int cameraNumber = 0;
	public static boolean gpsSensor = false;
	public static double latitude = 0 ;
	public static double longitude = 0 ;

	public static boolean vibratorSensor = false;
	public static boolean touchScreenSensor = false;
	public static SensorManager sensorManager = null;
	public static Sensor temperatureSensor = null;
	public static Sensor proximitySensor = null;
	public static Sensor magnetometerSensor = null;
	public static boolean near = false;
	public static Sensor lightSensor = null;
	public static Sensor accelerometerSensor = null;
	public static float ax,ay,az;
	public static Sensor orientationSensor = null;
	public static int orientation = 0;
	
	//Function to get battery information
	private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
	    @Override
		public void onReceive(Context ctxt, Intent intent) {
	    	if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
		    	batterySensor=true;
		    	level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
	    	}
		}
	};
	
	//Function to get camera information
    private void checkCameraHardware(Context context) {
	    if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
	        // this device has a camera
	    	cameraSensor = true;
	    	cameraNumber= Camera.getNumberOfCameras();
	    } else {
	        // no camera on this device
	    	cameraSensor = false;
	    }
	}	
    
    //Function to get device orientation
    public static String getRotation(Context context){
	    @SuppressWarnings("deprecation")
		final int rotation = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getOrientation();
	    switch (rotation) {
	            case Surface.ROTATION_0:
	                return "portrait";
	            case Surface.ROTATION_90:
	                return "landscape";
	            case Surface.ROTATION_180:
	                return "reverse portrait";
	            default:
	                return "reverse landscape";
	     }
	}
    
    //functions to get Screen sizes
    public static int getScreenWidth(Context context){
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		return size.x/2;    
    }
    
    public static int getScreenHeight(Context context){
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		return size.y/2;
    
    }
    public static void getScreenHeightInches(Context context){
    	WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	    DisplayMetrics dm = new DisplayMetrics();
	    wm.getDefaultDisplay().getMetrics(dm);
	    double screenHeightInches = dm.heightPixels/dm.ydpi;
	    Log.d("MyApp","Screen Height inches : " + screenHeightInches);
    }
    public static void getScreenWidthInches(Context context){
    	WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	    DisplayMetrics dm = new DisplayMetrics();
	    wm.getDefaultDisplay().getMetrics(dm);
	    double screenWidthInches = dm.widthPixels/dm.xdpi;
	    Log.d("MyApp","Screen Width inches : " + screenWidthInches);
    }   
    public static void getScreenInches(Context context){
    	WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	    DisplayMetrics dm = new DisplayMetrics();
	    wm.getDefaultDisplay().getMetrics(dm);
	    double x = Math.pow(dm.widthPixels/dm.xdpi,2);
	    double y = Math.pow(dm.heightPixels/dm.ydpi,2);
	    double screenInches = Math.sqrt(x+y);
	    Log.d("MyApp","Screen inches : " + screenInches);
    }
    
    //location sensor listener
    LocationListener locationListener = new LocationListener() {

	    @Override
	    public void onLocationChanged(Location loc)
	    {
	    	gpsSensor=true;
		    latitude = loc.getLatitude();
		    longitude = loc.getLongitude();
		   
		   //Log.d("MyApp", "My current location is: " +  "Latitud = " + loc.getLatitude() + "Longitud = " + loc.getLongitude());
	    }
		
	    @Override
	    public void onProviderDisabled(String provider)
	    {
	    	 Log.d("MyApp", "Location Disabled");
	    }
	
	    @Override
	    public void onProviderEnabled(String provider)
	    {
	    	Log.d("MyApp", "Location Enabled");
	    }

	    @Override
	    public void onStatusChanged(String provider, int status, Bundle extras)
	    {
	    }
    };
	
	private AndroidUpnpServiceImpl upnpServiceImpl;
	
	public static Vector<DeviceDisplay> listAdapter;

	private AndroidUpnpService upnpService;
    
	private BrowseRegistryListener registryListener = new BrowseRegistryListener();

	private ServiceConnection serviceConnection = new ServiceConnection() {
		 
		/**
	     * onServiceConnected
	     * 
	     * Service connection event listener.
	     * 
	     * @param {ComponentName} className
	     * @param {IBinder} service
	     * 
	     */
        @SuppressWarnings("rawtypes")
		public void onServiceConnected(ComponentName className, IBinder service) {
        	// Associate a service to upnpService variable.
        	upnpService = (AndroidUpnpService) service;
            Log.d("MyApp", "Connect");
            
            // Refresh the list with all known devices
            listAdapter.clear();
            for (Device device : upnpService.getRegistry().getDevices()) {
                registryListener.deviceAdded(device);
            }

            // Getting ready for future device advertisements
            upnpService.getRegistry().addListener(registryListener);

            // Search asynchronously for all devices
            upnpService.getControlPoint().search();
            Log.d("MyApp", "Searching for devices");
        }
        
        /**
         * onServiceDisconnected
         * 
         * Service disconnection event listener.
         * 
         * @param {ComponentName} className
         * 
         */
        public void onServiceDisconnected(ComponentName className) {
        	// Remove a service from upnpService variable.
            upnpService = null;
        }
    };
    
    /**
     * onBind
     * 
     * ServiceBoot class binding event listener.
     * 
     * @param {Intent} intent
     * 
     */
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	/**
     * onCreate
     * 
     * ServiceBoot class creation event listener.
     * 
     */
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate() {
		super.onCreate();
		
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
			
		IntentFilter filterBattery = new IntentFilter();
		filterBattery.addAction(Intent.ACTION_BATTERY_CHANGED);		
		
		this.registerReceiver(this.mBatInfoReceiver , filterBattery);
		
		context=this;
		
		this.checkCameraHardware(this);
		
		screenWidth = getScreenWidth(context);
		screenHeight = getScreenHeight(context);
		Log.d("MyApp","Screen screenWidth : " + screenWidth);
		Log.d("MyApp","Screen screenHeight : " + screenHeight);
		getScreenWidthInches(context);
		getScreenHeightInches(context);
		getScreenInches(context);
		
		Vibrator mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibratorSensor = mVibrator.hasVibrator();
		
		Log.d("MyApp",context.getSystemService(INPUT_SERVICE).toString());
		
		InputManager input = (InputManager) context.getSystemService(INPUT_SERVICE);
		int[] x = input.getInputDeviceIds();
		
		Log.d("MyApp",""+input.getInputDeviceIds().length);
		
		for(int i=0;i<input.getInputDeviceIds().length;i++){
			if((input.getInputDevice(x[i]).getSources()==InputDevice.SOURCE_CLASS_BUTTON)||(input.getInputDevice(x[i]).getSources()==InputDevice.SOURCE_KEYBOARD)||(input.getInputDevice(x[i]).getSources()==InputDevice.SOURCE_MOUSE)){
				Log.d("MyApp","KEYBOARD");
				touchScreenSensor=false;
				if(input.getInputDevice(x[i]).getSources()==InputDevice.SOURCE_CLASS_BUTTON) touchScreenExtra="Keyboard";
				if(input.getInputDevice(x[i]).getSources()==InputDevice.SOURCE_CLASS_BUTTON) touchScreenExtra="Keyboard";
				if(input.getInputDevice(x[i]).getSources()==InputDevice.SOURCE_CLASS_BUTTON) touchScreenExtra="Mouse";
			}else if((input.getInputDevice(x[i]).getSources()==InputDevice.SOURCE_TOUCHSCREEN)||(input.getInputDevice(x[i]).getSources()==InputDevice.SOURCE_TOUCHPAD)){
				Log.d("MyApp","TOUCH");
				touchScreenSensor=true;
			}
		}
		
		//Object inputService = context.getSystemService(INPUT_SERVICE);
		
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
	    proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
	    temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
	    lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
	    accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	    orientationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
	    magnetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
	    
	    if(accelerometerSensor != null){
	    	Log.d("MyApp", "Acelerometer Sensor : True");
	        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
	    }
	
	    if(proximitySensor != null){
	    		Log.d("MyApp", "Proximity Sensor : True");
	    		sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
	    }
	
	    if(lightSensor != null){
	    	Log.d("MyApp", "Light Sensor : True");
	    	sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
		}
	
	    if(temperatureSensor != null){
	    	Log.d("MyApp", "Temperature Sensor : True");
	    	sensorManager.registerListener(this, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
	    }
	    
	    if(magnetometerSensor != null){
	    	Log.d("MyApp", "Magnetometer Sensor : True");
	    	sensorManager.registerListener(this, magnetometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
	    }
	
	    if(orientationSensor != null){
	    	Log.d("MyApp", "Orientation Sensor : True");
	    	orientation = getResources().getConfiguration().orientation;
	    	sensorManager.registerListener(this, orientationSensor, SensorManager.SENSOR_DELAY_NORMAL);
		}
		    
		// Create a new Component.
    	final Component component = new Component();
    	 
    	// Add a new HTTP server listening on port 8080.
    	component.getServers().add(Protocol.HTTP, 8182);
    	
    	final Router router = new Router(component.getContext().createChildContext());
    	
    	router.attach("/agent/presence", AgentPresenceResource.class);
    	router.attach("/agent/capabilities", CapabilitiesResource.class);
    	
    	router.attach("/upnp", DataResource.class);
    	router.attach("/upnp/devices", UpnpDevicesResource.class);
    	router.attach("/upnp/services", UpnpServicesResource.class);
    	router.attach("/upnp/actions", UpnpActionsResource.class);
    	router.attach("/upnp/parameters", UpnpParametersResource.class);
    	router.attach("/upnp/presence", UpnpPresenceResource.class);
    	router.attach("/upnp/extra", UpnpExtraResource.class);
    	
    	router.attach("/bluetooth/presence", BluetoothPresenceResource.class);
    	router.attach("/bluetooth/devices", BluetoothDevicesResource.class);
    	
    	router.attach("/battery/presence", BatteryPresenceResource.class);
    	router.attach("/battery/extra", BatteryExtraResource.class);
    	
    	router.attach("/camera/presence", CameraPresenceResource.class);
    	router.attach("/camera/extra", CameraExtraResource.class);
    	
    	/*router.attach("/deviceProximity/presence", DeviceProximityPresenceResource.class);
    	router.attach("/deviceProximity/extra", DeviceProximityExtraResource.class);*/
    	
    	router.attach("/geolocation/presence", GeolocationPresenceResource.class);
    	router.attach("/geolocation/extra", GeolocationExtraResource.class);
    	
    	//router.attach("/language/presence", LanguagePresenceResource.class);
    	//router.attach("/language/extra", LanguageExtraResource.class);
    	
    	router.attach("/orientation/presence", OrientationPresenceResource.class);
    	router.attach("/orientation/extra", OrientationExtraResource.class);
    	
    	router.attach("/screen/presence", ScreenPresenceResource.class);
    	router.attach("/screen/extra", ScreenExtraResource.class);
    	
    	router.attach("/userProximity/presence", UserProximityPresenceResource.class);
    	router.attach("/userProximity/extra", UserProximityExtraResource.class);
    	
    	router.attach("/vibration/presence", VibrationPresenceResource.class);
    	
    	router.attach("/touchscreen/presence", TouchscreenPresenceResource.class);
    	
    	router.attach("/agent/presence", AgentPresenceResource.class);
    	
    	router.attach("/notification", Notification.class);
    	
    	
    	// Attach the sample application.
    	component.getDefaultHost().attach("/discoveryagent", router);
    	
    	// Start the component.
    	try {
			component.start();
			Toast.makeText(this, "Service created", Toast.LENGTH_LONG).show();
			Log.d("MyApp", "Service created");
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	// Create a new Vector<DeviceDisplay> for the devices.
    	listAdapter = new Vector<DeviceDisplay>();
    	
    	//UPnP service binding.
        //getApplicationContext().bindService( new Intent(this, BrowserUpnpService.class), serviceConnection, Context.BIND_AUTO_CREATE );
    	this.bindService( new Intent(this, BrowserUpnpService.class), serviceConnection, Context.BIND_AUTO_CREATE );
        
        // Create a new AndroidUpnpServiceImpl.
        upnpServiceImpl = new AndroidUpnpServiceImpl();
        
        Log.d("MyApp","upnpServiceImpl: "+upnpServiceImpl.toString());
        Log.d("MyApp","listAdapter.size(): "+listAdapter.size());
        
        /*final Handler customHandler = new Handler();
        Runnable mStatusChecker = new Runnable() {
    	    @Override 
    	    public void run() {
    	    	listAdapter.clear();
	    	    if (upnpService != null) {
	    	    	Log.d("MyApp", listAdapter.toString());
	    	    	//upnpService.getRegistry().getDevices().clear();
	    	    	upnpService.getRegistry().removeListener(registryListener);
	    	    	
	                // Refresh the list with all known devices
	                
	                upnpService.getRegistry().removeAllLocalDevices();
	                upnpService.getRegistry().removeAllRemoteDevices();

	                // Getting ready for future device advertisements
	                upnpService.getRegistry().addListener(registryListener);

	                // Search asynchronously for all devices
	                upnpService.getControlPoint().search();
	            }    	    
	    	    Log.d("MyApp", "Restarting!!");
	    	    customHandler.postDelayed(this, mInterval);
    	    }
    	};
    	customHandler.postDelayed(mStatusChecker, mInterval);*/
	}

	
	/**
     * onDestroy
     * 
     * ServiceBoot class destruction event listener.
     * 
     */
	@Override
	public void onDestroy() {
		Log.d("MyApp", "onDestroy");
		super.onDestroy();
		
		// Remove the server listener.
		if (upnpService != null) {
            upnpService.getRegistry().removeListener(registryListener);
        }
		
		getApplicationContext().unbindService(serviceConnection);
		Toast.makeText(this, "Service destroyed", Toast.LENGTH_LONG).show();
		Log.d("MyApp", "Service destroyed");
	}
	
	
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
	}
	
	@SuppressWarnings("unused")
	private static AccelerometerListener listener;
	
	/** Accuracy configuration */
    private static float threshold  = 50.0f;//15.0f; 
    private static int interval     = 2000; //200
	private long now = 0;
    private long timeDiff = 0;
    private long lastUpdate = 0;
    private long lastShake = 0;

    /*private float x = 0;
    private float y = 0;
    private float z = 0;*/
    private float lastX = 0;
    private float lastY = 0;
    private float lastZ = 0;
    private float force = 0;
	@Override
	public void onSensorChanged(SensorEvent arg0) {
		if (arg0.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
			ax=arg0.values[0];
		    ay=arg0.values[1];
		    az=arg0.values[2];
		    
		    // use the event timestamp as reference
	        // so the manager precision won't depends 
	        // on the AccelerometerListener implementation
	        // processing time
	        now = arg0.timestamp;
	        //Log.d("MyApp", "timestamp"+now);
	        // if not interesting in shake events
	        // just remove the whole if then else block
	        if (lastUpdate == 0) {
	        	lastUpdate = now;
	            lastShake = now;
	            lastX = ax;
	            lastY = ay;
	            lastZ = az;
	            //Toast.makeText(aContext,"No Motion detected", Toast.LENGTH_SHORT).show();
	        }else{
	        		timeDiff = now - lastUpdate;
	            if (timeDiff > 0) { 
	                /*force = Math.abs(x + y + z - lastX - lastY - lastZ) 
	                            / timeDiff;*/
	                force = Math.abs(ax + ay + az - lastX - lastY - lastZ);
	                //Log.d("MyApp", "Force: "+force);
	                if (Float.compare(force, threshold) >0 ) {
	                    if (now - lastShake >= interval) { 
	                        // trigger shake event
	                    	Log.d("MyApp", "Shake!!!!!!");
	                    	String url = "http://10.0.20.10:3000/Catcher/";
	                    	Intent i = new Intent(Intent.ACTION_VIEW);
	                    	Uri u = Uri.parse(url);
	                    	i.setData(u);
	                    	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	                    	this.startActivity(i);
	                        //listener.onShake(force);
	                    }
	                    lastShake = now;
	                }
	                lastX = ax;
	                lastY = ay;
	                lastZ = az;
	                lastUpdate = now; 
	            }
	        }
	        // trigger change event
	        //listener.onAccelerationChanged(x, y, z);
		}else{
			if (arg0.values[0] == 0) near=true;
			else if(arg0.values[0] == 1) near=false;
		}
	}
	
	protected void onResume() {
	    // register this class as a listener for the orientation and
	    // accelerometer sensors
		Log.d("MyApp", "AcelerometerResume");
	    sensorManager.registerListener(this,
	        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
	        SensorManager.SENSOR_DELAY_NORMAL);
	}

	protected void onPause() {
	    // unregister listener
	    sensorManager.unregisterListener(this);
	}
	
	/*@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
	    Log.d("MyApp", "OnStartCommandServiceBoot");
        return super.onStartCommand(intent, flags, startId);
	}*/
	
	/**
     * 
     * BrowseRegistryListener class.
     * 
     */
	class BrowseRegistryListener extends DefaultRegistryListener {
		
		/**
         * remoteDeviceDiscoveryStarted
         * 
         * When the remote device discovery starts, add to listAdapter vector.
         * 
         * @param {Registry} registry
         * @param {RemoteDevice} device
         * 
         */
		@Override
        public void remoteDeviceDiscoveryStarted(Registry registry, RemoteDevice device) {
            deviceAdded(device);
        }
		
        /**
         * remoteDeviceDiscoveryFailed
         * 
         * When the remote device discovery failed, remove from listAdapter vector.
         * 
         * @param {Registry} registry
         * @param {RemoteDevice} device
         * 
         */
        @Override
        public void remoteDeviceDiscoveryFailed(Registry registry, final RemoteDevice device, final Exception ex) {
			/*Toast.makeText(
            		ServiceBoot.this,
                    "Discovery failed of '" + device.getDisplayString() + "': " +
                            (ex != null ? ex.toString() : "Couldn't retrieve device/service descriptors"),
                    Toast.LENGTH_LONG
            ).show();*/
        	Log.d("MyApp", "Remote Discovery Error "+device.getDisplayString());
            deviceRemoved(device);
        }
        
        /**
         * remoteDeviceAdded
         * 
         * Add remote device to listAdapter vector.
         * 
         * @param {Registry} registry
         * @param {RemoteDevice} device
         * 
         */
        @Override
        public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
        	Log.d("MyApp", "Add remote device "+device.getDisplayString());
            deviceAdded(device);
        }
        
        /**
         * remoteDeviceRemoved
         * 
         * Remove remote device to listAdapter vector.
         * 
         * @param {Registry} registry
         * @param {RemoteDevice} device
         * 
         */
        @Override
        public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
        	Log.d("MyApp", "Remove remote device "+device.getDisplayString());
            deviceRemoved(device);
        }
        
        /**
         * localDeviceAdded
         * 
         * Add local device to listAdapter vector.
         * 
         * @param {Registry} registry
         * @param {LocalDevice} device
         * 
         */
        @Override
        public void localDeviceAdded(Registry registry, LocalDevice device) {
        	Log.d("MyApp", "Add local device "+device.getDisplayString());
            deviceAdded(device);
        }
        
        /**
         * localDeviceRemoved
         * 
         * Remove local device to listAdapter vector.
         * 
         * @param {Registry} registry
         * @param {LocalDevice} device
         * 
         */
        @Override
        public void localDeviceRemoved(Registry registry, LocalDevice device) {
        	Log.d("MyApp", "Remove local device "+device.getDisplayString());
            deviceRemoved(device);
        }
        
        /**
         * deviceAdded
         * 
         * Add device to listAdapter vector. If the device exists in the vector, the function 
         * re-set new value at same position.
         * 
         * @param {Device} device
         * 
         */
        @SuppressWarnings("rawtypes")
		public void deviceAdded(final Device device) {
            DeviceDisplay d = new DeviceDisplay(device);
            
            int position = listAdapter.indexOf(d);
            if (position >= 0) {
                // Device already in the list, re-set new value at same position
            	listAdapter.remove(d);
            	listAdapter.add(position, d);
            } else {
                listAdapter.add(d);
            }
            Log.d("MyApp", listAdapter.toString());
        }
        /**
         * deviceRemoved
         * 
         * Remove device from listAdapter vector.
         * 
         * @param {Device} device
         * 
         */
        @SuppressWarnings("rawtypes")
		public void deviceRemoved(final Device device) {
            listAdapter.remove(new DeviceDisplay(device));
        }
    }
	
	/**
     * 
     * DeviceDisplay class.
     * 
     */
    class DeviceDisplay {
        @SuppressWarnings("rawtypes")
		Device device;
        /**
         * DeviceDisplay
         * 
         * DeviceDisplay constructor.
         * 
         * @param {Device} device
         * @return {DeviceDisplay} The DeviceDisplay with the device information included inside.
         * 
         */
        @SuppressWarnings("rawtypes")
		public DeviceDisplay(Device device) {
            this.device = device;
        }
        /**
         * getDevice
         * 
         * Get DeviceDisplay's device.
         * 
         * @return {Device} The accounts with identifier boundId or an error message in case there is any problem durin the execution.
         * 
         */
        @SuppressWarnings("rawtypes")
		public Device getDevice() {
            return device;
        }
        /**
         * getDetailsMessage
         * 
         * Get Device details as a string.
         * 
         * @return {String} The accounts with identifier boundId or an error message in case there is any problem durin the execution.
         * 
         */
        @SuppressWarnings("rawtypes")
		public String getDetailsMessage() {
        	StringBuilder sb = new StringBuilder();
        	 if (getDevice().isFullyHydrated()) {
        		 sb.append(getDevice().getDisplayString());
                 sb.append("\n\n");
                 for (org.teleal.cling.model.meta.Service service : getDevice().getServices()) {
                     sb.append(service.getServiceType()).append("\n");
                 }
        	 }else {
                 sb.append(getString(R.string.deviceDetailsNotYetAvailable));
             }
        	 return sb.toString();
        }
        /**
         * equals
         * 
         * Comparison between two DeviceDisplay's devices.
         * 
         * @param {String} Object
         * @return {boolean} Boolean that represents the equality of two DeviceDisplay's devices.
         * 
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DeviceDisplay that = (DeviceDisplay) o;
            return device.equals(that.device);
        }
        /**
         * hashCode
         * 
         * Get devices hashCode.
         * 
         * @return {Integer} The device's hashCode of DeviceDisplay.
         * 
         */
        @Override
        public int hashCode() {
            return device.hashCode();
        }
        /**
         * toString
         * 
         * Get devices information as String.
         * 
         * @return {String} Selected device information.
         * 
         */
        @Override
        public String toString() {
            // Display a little star while the device is being loaded
            return device.isFullyHydrated() ? device.getDisplayString() : device.getDisplayString() + " *";
        }
    }
}