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
import org.teleal.cling.model.meta.Device;
import org.teleal.cling.model.meta.RemoteService;
import org.teleal.cling.model.meta.Service;
import org.teleal.cling.model.meta.Action;
import org.teleal.cling.model.meta.ActionArgument;

import com.example.discoveryagentrest.ServiceBoot;

public class UpnpExtraResource extends ServerResource {
	/*
	 * toString
	 * 
	 * Answer to GET type queries.
	 * 
	 * @return {String} The answer to GET query.
	 */
	@SuppressWarnings("rawtypes")
	@Get
    public Representation doGet() {
		Representation representation = null;
		String answer="";
		Log.d("MyApp", "UPnPExtra");
		try {
			if(ServiceBoot.listAdapter.size()>0){
				JSONArray ja = new JSONArray();
				for(int i = 0 ; i < ServiceBoot.listAdapter.size() ; i++){
					JSONObject jo = new JSONObject();
					Device device = ServiceBoot.listAdapter.get(i).getDevice();
					String name = device.getDisplayString();
					Log.d("MyApp", name);						
					jo.put("deviceName", name);
					
					JSONArray ja1 = new JSONArray();
					if(device.getServices().length>0){
						for(Service service : device.getServices()){
							JSONObject jo1 = new JSONObject();							
							jo1.put("serviceId", service.getServiceId().toString());
							jo1.put("name", service.toString()); 
							jo1.put("type", service.getServiceType().getType());
							if (service instanceof RemoteService){
								RemoteService remoteService = (RemoteService) service;
								Log.d("MyApp","URL:"+remoteService.getControlURI().toString());
								jo1.put("URL", remoteService.getControlURI());
							}
							if(service.getActions().length>0){
								JSONArray ja2 = new JSONArray();
								for(Action action : service.getActions()){
									JSONObject jo2 = new JSONObject();
									jo2.put("actionName", action.getName());
									JSONArray ja3 = new JSONArray();
									if(action.hasArguments()){
										for(ActionArgument argument : action.getArguments()){
											JSONObject jo3 = new JSONObject();
											jo3.put("argument", argument.getName());
											jo3.put("type", argument.getDatatype());
											ja3.put(jo3);
										}
										jo2.put("arguments",ja3);
									}
									ja2.put(jo2);
								}
								jo1.put("actions",ja2);
							}
							ja1.put(jo1);
						}
						jo.put("Services",ja1);
					}
					ja.put(jo);
				}
				JSONObject mainObj = new JSONObject();
				mainObj.put("extra", ja);
				if(getReference().getRemainingPart().contains("callback")) answer="extraUpnp("+mainObj.toString()+")";
				else answer=mainObj.toString();
				Log.d("MyApp", "extra: "+answer);
				representation = new StringRepresentation(answer, MediaType.APPLICATION_JSON);
			}else {
				if(getReference().getRemainingPart().contains("callback")) representation = new StringRepresentation("extraUpnp({\"extra\":\"The number of device is 0.\"})", MediaType.APPLICATION_JSON);
				else representation = new StringRepresentation("{\"extra\":\"The number of device is 0.\"}", MediaType.APPLICATION_JSON);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return representation;
		//return answer;
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
	@SuppressWarnings("rawtypes")
	@Post
    public String toString(String value) {
		String answer="";
		Log.d("MyApp", "UPnPExtra");
		try {
			if(ServiceBoot.listAdapter.size()>0){
				JSONArray ja = new JSONArray();
				for(int i = 0 ; i < ServiceBoot.listAdapter.size() ; i++){
					JSONObject jo = new JSONObject();
					Device device = ServiceBoot.listAdapter.get(i).getDevice();
					String name = device.getDisplayString();
					Log.d("MyApp", name);						
					jo.put("deviceName", name);
					
					JSONArray ja1 = new JSONArray();
					if(device.getServices().length>0){
						for(Service service : device.getServices()){
							JSONObject jo1 = new JSONObject();						
							jo1.put("serviceId", service.getServiceId().toString());
							jo1.put("name", service.toString());
							jo1.put("type", service.getServiceType().getType());
							if (service instanceof RemoteService){
								RemoteService remoteService = (RemoteService) service;
								Log.d("MyApp","URL:"+remoteService.getControlURI().toString());
								jo1.put("URL", remoteService.getControlURI());
							}
							if(service.getActions().length>0){
								JSONArray ja2 = new JSONArray();
								for(Action action : service.getActions()){
									JSONObject jo2 = new JSONObject();
									jo2.put("actionName", action.getName());
									JSONArray ja3 = new JSONArray();
									if(action.hasArguments()){
										for(ActionArgument argument : action.getArguments()){
											JSONObject jo3 = new JSONObject();
											jo3.put("argument", argument.getName());
											jo3.put("type", argument.getDatatype());
											ja3.put(jo3);
										}
										jo2.put("arguments",ja3);
									}
									ja2.put(jo2);
								}
								jo1.put("actions",ja2);
							}
							ja1.put(jo1);
						}
						jo.put("Services",ja1);
					}
					ja.put(jo);
				}
				JSONObject mainObj = new JSONObject();
				mainObj.put("devices", ja);
				answer=mainObj.toString();
			}else answer="The number of device is 0.";
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