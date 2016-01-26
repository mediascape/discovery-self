/*
** Long Library Name:
**	DataResource Package
**
** Acronym and its version:
**	DataResource v1.0
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
**	The DataResource class return the information for GET/POST/DELETE/OPTIONS queries.
** 
** Development Environment:
** 	The software has been Implemented in Java, and tested in Chrome 
**	browsers and Android 4.3 OS devices.
**
** Dependencies:
** 	As DataResource class depends on other libraries, the user must adhere to and 
** 	keep in place any Licencing terms of those libraries:
**      Android v4.4.2 (http://developer.android.com/)
**		Restlet v2.1 (http://restlet.com/)
**
** Licenses dependencies:
**	License Agreement for Android:
**		Apachev2 license (http://opensource.org/licenses/apache-2.0)
**		GNU LGPLv2.1 license (http://opensource.org/licenses/lgpl-2.1)
**	License Agreement for Restlet:
**		Apachev2 license (http://opensource.org/licenses/apache-2.0)
**		GNU LGPLv3 license (http://opensource.org/licenses/lgpl-3.0)
**		GNU LGPLv2.1 license (http://opensource.org/licenses/lgpl-2.1)
**		CDDLv1 license (http://opensource.org/licenses/cddl1)
**		EPLv1 license (http://opensource.org/licenses/eclipse-1.0)
**
*/
package com.example.discoveryagentrest;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.example.discoveryagentrest.BluetoothService;

public class BluetoothDevicesResource extends ServerResource {
	/*
	 * toString
	 * 
	 * Answer to GET type queries.
	 * 
	 * @return {String} The answer to GET query.
	 */
	@Get
    public Representation doGet() {
		Representation representation = null;
		String answer="";
		Log.d("MyApp", "BluetoothDevices");
		try {
			if(BluetoothService.bluetoothSensor.isEnabled()){
				if(BluetoothService.listBluetooth.size()>0){
					JSONArray ja = new JSONArray();
					for(int i = 0 ; i < BluetoothService.listBluetooth.size() ; i++){
						JSONObject jo = new JSONObject();
						String name = BluetoothService.listBluetooth.get(i).getName();
						Log.d("MyApp", name);
						jo.put("deviceName", name);
						ja.put(jo);
					}
					JSONObject mainObj = new JSONObject();
					mainObj.put("devices", ja);
					if(getReference().getRemainingPart().contains("callback")) answer="devicesBluetooth("+mainObj.toString()+")";
					else answer=mainObj.toString();
					Log.d("MyApp", "devices: "+answer);
					representation = new StringRepresentation(answer, MediaType.APPLICATION_JSON);
				}else {
					if(getReference().getRemainingPart().contains("callback")) representation = new StringRepresentation("devicesBluetooth({\"devices\":\"The number of device is 0.\"})", MediaType.APPLICATION_JSON);
					else representation = new StringRepresentation("{\"devices\":\"The number of device is 0.\"}", MediaType.APPLICATION_JSON);
				}
			}else{
				if(getReference().getRemainingPart().contains("callback")) representation = new StringRepresentation("devicesBluetooth({\"devices\":\"The bluetooth is not enabled.\"})", MediaType.APPLICATION_JSON);
				else representation = new StringRepresentation("{\"devices\":\"The bluetooth is not enabled.\"}", MediaType.APPLICATION_JSON);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return representation;
    }
	
	/*
	 * toString2
	 * 
	 * Answer to POST type queries.
	 * 
	 * @return {String} The answer to POST query.
	 */
	@Post
    public String toString(String value) {
		String answer="";
		Log.d("MyApp", "BluetoothDevices");
		try {
			if(BluetoothService.bluetoothSensor.isEnabled()){
				/*if(!ServiceBoot.bluetoothSensor.isDiscovering()){
					ServiceBoot.listAdapter.clear();
					ServiceBoot.bluetoothSensor.startDiscovery();
				}*/
				if(BluetoothService.listBluetooth.size()>0){
					JSONArray ja = new JSONArray();
					for(int i = 0 ; i < BluetoothService.listBluetooth.size() ; i++){
						JSONObject jo = new JSONObject();
						String name = BluetoothService.listBluetooth.get(i).getName();
						Log.d("MyApp", name);						
						jo.put("deviceName", name);
						ja.put(jo);
					}
					JSONObject mainObj = new JSONObject();
					mainObj.put("devices", ja);
					answer=mainObj.toString();
				}else answer="The number of device is 0.";
			}else answer="The bluetooth is not enabled.";
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return answer;
    }
    /*
     * toString3
     * 
	 * Answer to DELETE type queries.
	 * 
	 * @return {String} The answer to DELETE query.
	 */
	/* @Delete
    public String toString3() {
        return "<html><head><h2>hello, DELETE world</h2></head><body><h2>hello, DELETE world</h2></body></html>";
    }*/
	
    /*
     * toString4
     * 
	 * Answer to OPTIONS type queries.
	 * 
	 * @return {String} The answer to OPTIONS query.
	 */
	/*@Options
    public String toString4() {
        return "<html><head><h2>hello, OPTIONS world</h2></head><body><h2>hello, OPTIONS world</h2></body></html>";
    }*/
}