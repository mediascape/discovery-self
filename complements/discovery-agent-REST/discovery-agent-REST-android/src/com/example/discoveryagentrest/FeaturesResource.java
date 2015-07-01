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
import org.restlet.resource.Delete;
import org.restlet.resource.Options;
import org.restlet.resource.ServerResource;

import com.example.discoveryagentrest.ServiceBoot;

public class FeaturesResource extends ServerResource {
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
		Log.d("MyApp", "Battery");
		Log.d("MyApp",getReference().getRemainingPart().toString());
		try {
			JSONArray ja = new JSONArray();
			
			JSONObject jo = new JSONObject();
			jo.put("battery", String.valueOf(ServiceBoot.level) + "%");
			ja.put(jo);
			
			JSONObject jo1 = new JSONObject();
			jo1.put("camera", String.valueOf(ServiceBoot.cameraSensor));
			ja.put(jo1);
			
			JSONObject jo2 = new JSONObject();
			jo2.put("screen", ServiceBoot.screenWidth+", "+ServiceBoot.screenHeight);
			ja.put(jo2);
			
			JSONObject jo3 = new JSONObject();
			if(ServiceBoot.accelerometerSensor == null)	jo3.put("acelerometerSernsor", "false");
			else jo3.put("acelerometerSernsor", "true");
			ja.put(jo3);
			
			JSONObject jo4 = new JSONObject();
			if(ServiceBoot.proximitySensor == null)	jo4.put("proximitySensor", "false");
			else jo4.put("proximitySensor", "true");
			ja.put(jo4);
			
			JSONObject jo5 = new JSONObject();
			if(ServiceBoot.lightSensor == null)	jo5.put("lightSensor", "false");
			else jo5.put("lightSensor", "true");
			ja.put(jo5);
			
			JSONObject jo6 = new JSONObject();
			if(ServiceBoot.orientationSensor == null)	jo6.put("orientationSernsor", "false");
			else jo6.put("orientationSernsor", "true");
			ja.put(jo6);
			
			JSONObject jo7 = new JSONObject();
			if(ServiceBoot.temperatureSensor == null) jo7.put("temperatureSensor", "false");
			else jo7.put("temperatureSensor", "true");
			ja.put(jo7);
			
			JSONObject jo8 = new JSONObject();
			if(ServiceBoot.vibratorSensor) jo8.put("vibratorSensor", "true");
			else jo8.put("vibratorSensor", "false");
			ja.put(jo8);
			
			JSONObject jo9 = new JSONObject();
			if(BluetoothService.bluetoothSensor == null) jo9.put("bluetoothSensor", "false");
			else jo9.put("bluetoothSensor", "true");
			ja.put(jo9);
			
			JSONObject mainObj = new JSONObject();
			mainObj.put("features", ja);
			if(getReference().getRemainingPart().contains("callback")) answer="features("+mainObj.toString()+")";
			else answer=mainObj.toString();
			Log.d("MyApp", "features: "+answer);
			representation = new StringRepresentation(answer, MediaType.APPLICATION_JSON);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return representation;
    }
	public String getValue(String variable){
		Log.d("MyApp", "getValue");	
		String remaining=getReference().getRemainingPart();
		if(remaining.indexOf("&", remaining.indexOf(variable))!=-1) return remaining.substring(remaining.indexOf(variable)+variable.length(), remaining.indexOf("&", remaining.indexOf(variable)));
		else return remaining.substring(remaining.indexOf(variable)+variable.length());
	}
	
	/*
	 * toString2
	 * 
	 * Answer to POST type queries.
	 * 
	 * @return {String} The answer to POST query.
	 */
	@Post
    public String toString() {
		String answer="";
		Log.d("MyApp", "Battery");
		
		try {
			JSONArray ja = new JSONArray();
			
			JSONObject jo = new JSONObject();
			jo.put("battery", String.valueOf(ServiceBoot.level) + "%");
			ja.put(jo);
			
			JSONObject jo1 = new JSONObject();
			jo1.put("camera", String.valueOf(ServiceBoot.cameraSensor));
			ja.put(jo1);
			
			JSONObject jo2 = new JSONObject();
			jo2.put("screen", ServiceBoot.screenWidth+", "+ServiceBoot.screenHeight);
			ja.put(jo2);
			
			JSONObject jo3 = new JSONObject();
			if(ServiceBoot.accelerometerSensor == null)	jo3.put("acelerometerSernsor", "false");
			else jo3.put("acelerometerSernsor", "true");
			ja.put(jo3);
			
			JSONObject jo4 = new JSONObject();
			if(ServiceBoot.proximitySensor == null)	jo4.put("proximitySensor", "false");
			else jo4.put("proximitySensor", "true");
			ja.put(jo4);
			
			JSONObject jo5 = new JSONObject();
			if(ServiceBoot.lightSensor == null)	jo5.put("lightSensor", "false");
			else jo5.put("lightSensor", "true");
			ja.put(jo5);
			
			JSONObject jo6 = new JSONObject();
			if(ServiceBoot.orientationSensor == null)	jo6.put("orientationSernsor", "false");
			else jo6.put("orientationSernsor", "true");
			ja.put(jo6);
			
			JSONObject jo7 = new JSONObject();
			if(ServiceBoot.temperatureSensor == null) jo7.put("temperatureSensor", "false");
			else jo7.put("temperatureSensor", "true");
			ja.put(jo7);
			
			JSONObject jo8 = new JSONObject();
			if(ServiceBoot.vibratorSensor) jo8.put("vibratorSensor", "true");
			else jo8.put("vibratorSensor", "false");
			ja.put(jo8);
			
			JSONObject jo9 = new JSONObject();
			if(BluetoothService.bluetoothSensor == null) jo9.put("bluetoothSensor", "false");
			else jo9.put("bluetoothSensor", "true");
			ja.put(jo9);
			
			JSONObject mainObj = new JSONObject();
			mainObj.put("features", ja);
			answer=mainObj.toString();
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
    @Delete
    public String toString3() {
        return "<html><head><h2>hello, DELETE world</h2></head><body><h2>hello, DELETE world</h2></body></html>";
    }
    /*
     * toString4
     * 
	 * Answer to OPTIONS type queries.
	 * 
	 * @return {String} The answer to OPTIONS query.
	 */
    @Options
    public String toString4() {
        return "<html><head><h2>hello, OPTIONS world</h2></head><body><h2>hello, OPTIONS world</h2></body></html>";
    }
}