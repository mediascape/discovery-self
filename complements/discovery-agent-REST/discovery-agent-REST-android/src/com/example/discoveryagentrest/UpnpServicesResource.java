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
import org.teleal.cling.model.meta.Device;
import org.teleal.cling.model.meta.RemoteService;

import com.example.discoveryagentrest.ServiceBoot;

public class UpnpServicesResource extends ServerResource {
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
		if(getReference().getRemainingPart().contains("device=")){
			Log.d("MyApp", "UPnPServices");
			try {
				if(ServiceBoot.listAdapter.size()>0){
					int deviceNumber = Integer.parseInt(getValue("device="));
					//int deviceNumber = Integer.parseInt(getReference().getRemainingPart().substring(getReference().getRemainingPart().indexOf("=")+1));
					if(getReference().getRemainingPart().indexOf("=")!=-1){
						if((deviceNumber-1) <= ServiceBoot.listAdapter.size()){
							JSONArray ja = new JSONArray();
							Device device = ServiceBoot.listAdapter.get(deviceNumber-1).getDevice();
							for(org.teleal.cling.model.meta.Service service : device.getServices()){
								JSONObject jo = new JSONObject();								
								jo.put("serviceId", service.getServiceId().toString());
								jo.put("name", service.toString()); 
								jo.put("type", service.getServiceType().getType());
								if (service instanceof RemoteService){
									RemoteService remoteService = (RemoteService) service;
									Log.d("MyApp","URL:"+remoteService.getControlURI().toString());
									jo.put("URL", remoteService.getControlURI());
								}
								ja.put(jo);
							}
							JSONObject mainObj = new JSONObject();
							mainObj.put("services", ja);
							//answer="services("+mainObj.toString()+")";
							if(getReference().getRemainingPart().contains("callback")) answer = "servicesUpnp("+mainObj.toString()+")";
							else answer = mainObj.toString();
							Log.d("MyApp", "services: "+answer);
							representation = new StringRepresentation(answer, MediaType.APPLICATION_JSON);
						}else{
							if(getReference().getRemainingPart().contains("callback")) representation = new StringRepresentation("servicesUpnp({\"parameters\":\"Selected device is not in the list.\"})", MediaType.APPLICATION_JSON);
							else representation = new StringRepresentation("{\"services\":\"Selected device is not in the list.\"}", MediaType.APPLICATION_JSON);
						}
					}else {
						if(getReference().getRemainingPart().contains("callback")) representation = new StringRepresentation("servicesUpnp({\"services\":\"The device id has not been defined.\"})", MediaType.APPLICATION_JSON);
						else representation = new StringRepresentation("{\"services\":\"The device id has not been defined.\"}", MediaType.APPLICATION_JSON);
					}//representation = new StringRepresentation("services({\"services\":\"The device id has not been defined.\"})", MediaType.APPLICATION_JSON);
				}else {
					if(getReference().getRemainingPart().contains("callback")) representation = new StringRepresentation("servicesUpnp({\"services\":\"The number of device is 0.\"})", MediaType.APPLICATION_JSON);
					else representation = new StringRepresentation("{\"services\":\"The number of device is 0.\"}", MediaType.APPLICATION_JSON);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
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

		Log.d("MyApp", "UPnPServices");
		try {
			if(ServiceBoot.listAdapter.size()>0){
				Form form = new Form(value); 
				int deviceNumber = Integer.parseInt(form.getFirstValue("device"));
				if(deviceNumber > 0){
					JSONArray ja = new JSONArray();
					Device device = ServiceBoot.listAdapter.get(deviceNumber-1).getDevice();
					for(org.teleal.cling.model.meta.Service service : device.getServices()){
						JSONObject jo = new JSONObject();
						Log.d("MyApp", service.toString());
						jo.put("serviceId", service.getServiceId().toString());
						jo.put("name", service.toString()); 
						jo.put("type", service.getServiceType().getType());
						if (service instanceof RemoteService){
							RemoteService remoteService = (RemoteService) service;
							Log.d("MyApp","URL:"+remoteService.getControlURI().toString());
							jo.put("URL", remoteService.getControlURI());
						}
						ja.put(jo);
					}
					JSONObject mainObj = new JSONObject();
					mainObj.put("device", device.getDisplayString());
					mainObj.put("services", ja);
					answer = mainObj.toString();
				}else answer = "The device id has not been defined.";
			}else answer = "The number of device is 0.";
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