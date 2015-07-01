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
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Delete;
import org.restlet.resource.Options;
import org.restlet.resource.ServerResource;
import org.teleal.cling.model.meta.Action;
import org.teleal.cling.model.meta.ActionArgument;
import org.teleal.cling.model.meta.Device;

import com.example.discoveryagentrest.ServiceBoot;

public class UpnpParametersResource extends ServerResource {
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
		if(getReference().getRemainingPart().contains("service=")&&getReference().getRemainingPart().contains("device=")&&getReference().getRemainingPart().contains("action=")){
			Log.d("MyApp", "UPnPParameters");
			try{
				if(ServiceBoot.listAdapter.size()>0){
					int deviceNumber = Integer.parseInt(getValue("device="));
					String serviceName = getValue("service=");
					String actionName = getValue("action=");
					if((deviceNumber-1) <= ServiceBoot.listAdapter.size()){
						Device device = ServiceBoot.listAdapter.get(deviceNumber-1).getDevice();
						for(org.teleal.cling.model.meta.Service service : device.getServices()){
							String serviceType=service.getServiceType().getType();
							Log.d("MyApp", "serviceList "+serviceType);
							if(serviceType.equals(serviceName)){
								if(service.getActions().length>0){
									JSONArray ja = new JSONArray();
									for(Action action : service.getActions()){
										String actionListName=action.getName();
										if((action.hasArguments())&&(actionListName.equals(actionName))){
											for(ActionArgument argument : action.getArguments()){
												JSONObject jo = new JSONObject();
												jo.put("argument", argument.getName());
												jo.put("type", argument.getDatatype());
												ja.put(jo);
											}
										}											
									}
									JSONObject mainObj = new JSONObject();
									mainObj.put("action", actionName);
									mainObj.put("parameters", ja);
									//answer="parameters("+mainObj.toString()+")";
									if(getReference().getRemainingPart().contains("callback")) answer = "parametersUpnp("+mainObj.toString()+")";
									else answer = mainObj.toString();
									Log.d("MyApp", "parameters: "+answer);
									representation = new StringRepresentation(answer, MediaType.APPLICATION_JSON);
									//return answer;
								}else {
									if(getReference().getRemainingPart().contains("callback")) representation = new StringRepresentation("parametersUpnp({\"parameters\":\"The service has no actions.\"})", MediaType.APPLICATION_JSON);
									else representation = new StringRepresentation("{\"parameters\":\"The service has no actions.\"}", MediaType.APPLICATION_JSON);
								}
							}
						}
					}else {
						if(getReference().getRemainingPart().contains("callback")) representation = new StringRepresentation("parametersUpnp({\"parameters\":\"Selected device is not in the list.\"})", MediaType.APPLICATION_JSON);
						else representation = new StringRepresentation("{\"parameters\":\"Selected device is not in the list.\"}", MediaType.APPLICATION_JSON);
					}
				}else {
					if(getReference().getRemainingPart().contains("callback")) representation = new StringRepresentation("parametersUpnp({\"parameters\":\"The number of device is 0.\"})", MediaType.APPLICATION_JSON);
					else representation = new StringRepresentation("{\"parameters\":\"The number of device is 0.\"}", MediaType.APPLICATION_JSON);
				}//representation = new StringRepresentation("parameters({\"parameters\": \"The number of device is 0.\"})", MediaType.APPLICATION_JSON);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			if(getReference().getRemainingPart().contains("callback")) representation = new StringRepresentation("parametersUpnp({\"parameters\":\"There is no function asociated to the request.\"})", MediaType.APPLICATION_JSON);
			else representation = new StringRepresentation("{\"parameters\":\"There is no function asociated to the request.\"}", MediaType.APPLICATION_JSON);
			//representation = new StringRepresentation("parameters({\"parameters\": \"There is no function asociated to the request.\"})", MediaType.APPLICATION_JSON);
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
	@SuppressWarnings("rawtypes")
	@Post
    public String toString2(String value) {
		String answer="";
		Log.d("MyApp", "UPnPParameters");
		try{
			if(ServiceBoot.listAdapter.size()>0){
				Form form = new Form(value);
				int deviceNumber = Integer.parseInt(form.getFirstValue("device"));
				String serviceName = form.getFirstValue("service").trim();
				String actionName = form.getFirstValue("action").trim();
				Log.d("MyApp", "deviceNumber "+deviceNumber);
				Log.d("MyApp", "serviceName "+serviceName);
				Log.d("MyApp", "actionName "+actionName);
				if(ServiceBoot.listAdapter.size()>= (deviceNumber-1)){
					Device device = ServiceBoot.listAdapter.get(deviceNumber-1).getDevice();
					for(org.teleal.cling.model.meta.Service service : device.getServices()){
						String serviceType=service.getServiceType().getType();
						Log.d("MyApp", "service "+serviceType);
						Log.d("MyApp", serviceType+".equals(serviceName) "+serviceType.equals(serviceName));
						Log.d("MyApp", serviceType+".compareTo(serviceName) "+serviceType.compareTo(serviceName));
						if(serviceType.equals(serviceName)){
							Log.d("MyApp", "Comparison ");
							if(service.getActions().length>0){
								for(Action action : service.getActions()){
									String actionListName=action.getName();
									Log.d("MyApp", "action.hasArguments() "+action.hasArguments());
									Log.d("MyApp", actionListName+".equals("+actionName+") "+actionListName.equals(actionName));
									if((action.hasArguments())&&(actionListName.equals(actionName))){
										JSONArray ja = new JSONArray();
										JSONObject mainObj = new JSONObject();
										Log.d("MyApp", "actionName "+actionName);
										Log.d("MyApp", "action "+action.getName());
										Log.d("MyApp", "action length: "+action.getArguments().length);
										for(ActionArgument argument : action.getArguments()){
											Log.d("MyApp", "argument "+ argument.getName());
											JSONObject jo = new JSONObject();
											jo.put("argument", argument.getName());
											jo.put("type", argument.getDatatype());
											ja.put(jo);
										}
										mainObj.put("action", serviceName);
										mainObj.put("parameters", ja);
										answer=mainObj.toString();
										break;
									}													
								}													
							}else answer = "The service has no actions.";
						}
					}
					if(answer=="") answer="There is no service with that name.";
				}else answer="The number of devices is smaller.";
			}else answer="The number of devices is 0.";
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