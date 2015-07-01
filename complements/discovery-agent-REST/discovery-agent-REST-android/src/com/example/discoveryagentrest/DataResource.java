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
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Delete;
import org.restlet.resource.Options;
import org.restlet.resource.ServerResource;
import org.teleal.cling.model.meta.Action;
import org.teleal.cling.model.meta.ActionArgument;
import org.teleal.cling.model.meta.Device;
import org.teleal.cling.model.meta.DeviceDetails;

import com.example.discoveryagentrest.ServiceBoot;

public class DataResource extends ServerResource {
	/*
	 * toString
	 * 
	 * Answer to GET type queries.
	 * 
	 * @return {String} The answer to GET query.
	 */
	@SuppressWarnings("rawtypes")
	@Get
    public String toString() {
		/*if(getReference().getRemainingPart().contains("getNearDevices")){
			Log.d("MyApp", "getNearDevices");
			return "callToUPNP";
		}else if(getReference().getRemainingPart().contains("probatzen")){
			Log.d("MyApp", "Probatzen");
			return "probando1";
		}else {
			Log.d("MyApp", "Answer");
			Log.d("MyApp",ServiceBoot.listAdapter.toString());
			
			String answer = "Resource URI  : " + getReference() + '\n' + "Root URI      : "
		    + getRootRef() + '\n' + "Routed part   : "
		    + getReference().getBaseRef() + '\n' + "Remaining part: "
		    + getReference().getRemainingPart() + '\n' + '\n' + "UPnP Devices List: "
		    + ServiceBoot.listAdapter.toString()+ '\n' + '\n' + "UPnP Devices List Information: \n";
			for(int i=0;i<ServiceBoot.listAdapter.size(); i++){
				DeviceDisplay dd=ServiceBoot.listAdapter.get(i);
				answer=answer+dd.getDevice().getDisplayString()+" :\n";
				for(org.teleal.cling.model.meta.Service service :ServiceBoot.listAdapter.get(i).getDevice().getServices()){
					answer=answer+service.toString()+"\n";
				}
				answer=answer+" \n";
			}
		    return answer;
		//}*/
		String answer="";
		if(getReference().getRemainingPart().contains("devices")){
				Log.d("MyApp", "devices");
				try {
					if(ServiceBoot.listAdapter.size()>0){
						JSONArray ja = new JSONArray();
						for(int i = 0 ; i < ServiceBoot.listAdapter.size() ; i++){
							JSONObject jo = new JSONObject();
							String name = ServiceBoot.listAdapter.get(i).getDevice().getDisplayString();
							Log.d("MyApp", name);						
							jo.put("deviceName", name);
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
		}else if(getReference().getRemainingPart().contains("services")){
				Log.d("MyApp", "services");
				try {
					if(ServiceBoot.listAdapter.size()>0){
						int deviceNumber = Integer.parseInt(getReference().getRemainingPart().substring(getReference().getRemainingPart().indexOf("=")+1));
						if(getReference().getRemainingPart().indexOf("=")!=-1){
							JSONArray ja = new JSONArray();
							Device device = ServiceBoot.listAdapter.get(deviceNumber-1).getDevice();
							for(org.teleal.cling.model.meta.Service service : device.getServices()){
								JSONObject jo = new JSONObject();
								Log.d("MyApp", service.toString());
								jo.put("serviceId", service.getServiceId().toString());
								jo.put("name", service.toString()); 
								jo.put("type", service.getServiceType().getType()); 
								ja.put(jo);
							}
							JSONObject mainObj = new JSONObject();
							mainObj.put("services", ja);
							answer = mainObj.toString();
						}else answer = "The device id has not been defined.";
					}else answer = "The number of device is 0.";
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		
			}
			else if(getReference().getRemainingPart().contains("devicedetails")){
					Log.d("MyApp", "devicedetails");
					try {
						if(ServiceBoot.listAdapter.size()>0){
							int deviceNumber = Integer.parseInt(getReference().getRemainingPart().substring(getReference().getRemainingPart().indexOf("=")+1));
							if(getReference().getRemainingPart().indexOf("=")!=-1){
								JSONArray ja = new JSONArray();
								JSONArray ja2 = new JSONArray();
								Device device = ServiceBoot.listAdapter.get(deviceNumber-1).getDevice();
								DeviceDetails detail = device.getDetails();
								
								JSONObject jo = new JSONObject();
								Log.d("MyApp", detail.toString());
								jo.put("detailSerialNumber", detail.getSerialNumber());
								if(!detail.getFriendlyName().isEmpty()) jo.put("name", detail.getFriendlyName());
								if(!detail.getManufacturerDetails().getManufacturer().isEmpty()) jo.put("manufacturer", detail.getFriendlyName());
								if(!detail.getModelDetails().getModelName().isEmpty()) jo.put("FriendlyName", detail.getFriendlyName());
								if(!detail.getModelDetails().getModelDescription().isEmpty()) jo.put("description", detail.getModelDetails().getModelDescription());
								if(!detail.getModelDetails().getModelNumber().isEmpty()) jo.put("number", detail.getModelDetails().getModelNumber());
								if(detail.getModelDetails().getModelURI().getFragment() != null) jo.put("URI", detail.getModelDetails().getModelURI().getFragment());
								if(detail.getDlnaCaps() != null){
									if(detail.getDlnaCaps().getCaps().length>0){
										Log.d("MyApp", detail.getDlnaCaps().getCaps().toString());
										for(String cap : detail.getDlnaCaps().getCaps()){
											JSONObject jo2 = new JSONObject();
											jo2.put("DLNA", cap.toString()); 
											ja2.put(jo2);
										}
										jo.put("DLNA", ja2); 
									}
									Log.d("MyApp", detail.getDlnaCaps().toString());
								}
								if(detail.getUpc() != null) jo.put("UPC", detail.getUpc());
								ja.put(jo);
								JSONObject mainObj = new JSONObject();
								mainObj.put("device", device.getDisplayString());
								mainObj.put("details", ja);
								answer= mainObj.toString();
							}else answer="The device id has not been defined.";
						}else answer="The number of device is 0.";
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if(getReference().getRemainingPart().contains("actions")&&getReference().getRemainingPart().contains("service=")&&getReference().getRemainingPart().contains("device=")){
						Log.d("MyApp", "serviceactions");	
						try{
							if(ServiceBoot.listAdapter.size()>0){
								int deviceNumber = Integer.parseInt(getValue("device="));
								String serviceName = getValue("service=");
								Log.d("MyApp", "device "+deviceNumber);
								Log.d("MyApp", "service "+serviceName);
								Device device = ServiceBoot.listAdapter.get(deviceNumber-1).getDevice();
								for(org.teleal.cling.model.meta.Service service : device.getServices()){
									String serviceType=service.getServiceType().getType();
									Log.d("MyApp", "serviceList "+serviceType);
									if(serviceType.equals(serviceName)){
										if(service.getActions().length>0){
											JSONArray ja = new JSONArray();
											for(Action action : service.getActions()){
												JSONObject jo = new JSONObject();
												jo.put("actionName", action.getName());
												ja.put(jo);
											}
											JSONObject mainObj = new JSONObject();
											mainObj.put("service", serviceName);
											mainObj.put("actions", ja);
											answer=mainObj.toString();
										}else answer = "The service has no actions.";
									}
								}
							}else answer="The number of device is 0.";
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else if(getReference().getRemainingPart().contains("parameters")&&getReference().getRemainingPart().contains("service=")&&getReference().getRemainingPart().contains("device=")&&getReference().getRemainingPart().contains("action=")){
						Log.d("MyApp", "actionparameters");
						try{
							if(ServiceBoot.listAdapter.size()>0){
								int deviceNumber = Integer.parseInt(getValue("device="));
								String serviceName = getValue("service=");
								String actionName = getValue("action=");
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
											answer=mainObj.toString();
										}else answer = "The service has no actions.";
									}
								}
							}else answer="The number of device is 0.";
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else{
						answer="There is no function asociated to the request.";
					}
		return answer;
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
		if(getReference().getRemainingPart().contains("devices")){
				Log.d("MyApp", "devices");
				try {
					if(ServiceBoot.listAdapter.size()>0){
						JSONArray ja = new JSONArray();
						for(int i = 0 ; i < ServiceBoot.listAdapter.size() ; i++){
							JSONObject jo = new JSONObject();
							String name = ServiceBoot.listAdapter.get(i).getDevice().getDisplayString();
							Log.d("MyApp", name);						
							jo.put("deviceName", name);
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
		}else if(getReference().getRemainingPart().contains("services")){
				Log.d("MyApp", "services");
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
			}
			else if(getReference().getRemainingPart().contains("devicedetails")){
					Log.d("MyApp", "devicedetails");
					try {
						if(ServiceBoot.listAdapter.size()>0){
							Form form = new Form(value); 
							int deviceNumber = Integer.parseInt(form.getFirstValue("device"));
							if(deviceNumber > 0){
								JSONArray ja = new JSONArray();
								JSONArray ja2 = new JSONArray();
								Device device = ServiceBoot.listAdapter.get(deviceNumber-1).getDevice();
								DeviceDetails detail = device.getDetails();
								
								JSONObject jo = new JSONObject();
								Log.d("MyApp", detail.toString());
								jo.put("detailSerialNumber", detail.getSerialNumber());
								if(!detail.getFriendlyName().isEmpty()) jo.put("name", detail.getFriendlyName());
								if(!detail.getManufacturerDetails().getManufacturer().isEmpty()) jo.put("manufacturer", detail.getFriendlyName());
								if(!detail.getModelDetails().getModelName().isEmpty()) jo.put("FriendlyName", detail.getFriendlyName());
								if(!detail.getModelDetails().getModelDescription().isEmpty()) jo.put("description", detail.getModelDetails().getModelDescription());
								if(!detail.getModelDetails().getModelNumber().isEmpty()) jo.put("number", detail.getModelDetails().getModelNumber());
								if(detail.getModelDetails().getModelURI().getFragment() != null) jo.put("URI", detail.getModelDetails().getModelURI().getFragment());
								if(detail.getDlnaCaps() != null){
									if(detail.getDlnaCaps().getCaps().length>0){
										Log.d("MyApp", detail.getDlnaCaps().getCaps().toString());
										for(String cap : detail.getDlnaCaps().getCaps()){
											JSONObject jo2 = new JSONObject();
											jo2.put("DLNA", cap.toString()); 
											ja2.put(jo2);
										}
										jo.put("DLNA", ja2); 
									}
									Log.d("MyApp", detail.getDlnaCaps().toString());
								}
								if(detail.getUpc() != null) jo.put("UPC", detail.getUpc());
								ja.put(jo);
								JSONObject mainObj = new JSONObject();
								mainObj.put("device", device.getDisplayString());
								mainObj.put("details", ja);
								answer= mainObj.toString();
							}else answer="The device id has not been defined.";
						}else answer="The number of device is 0.";
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if(getReference().getRemainingPart().contains("actions")){
						Log.d("MyApp", "actions");	
						try{
							if(ServiceBoot.listAdapter.size()>0){
								Form form = new Form(value); 
								int deviceNumber = Integer.parseInt(form.getFirstValue("device"));
								String serviceName = form.getFirstValue("service");
								//int deviceNumber = Integer.parseInt(getValue("device="));
								//String serviceName = getValue("service=");
								Log.d("MyApp", "device "+deviceNumber);
								Log.d("MyApp", "service "+serviceName);
								Device device = ServiceBoot.listAdapter.get(deviceNumber-1).getDevice();
								for(org.teleal.cling.model.meta.Service service : device.getServices()){
									String serviceType=service.getServiceType().getType();
									Log.d("MyApp", "serviceList "+serviceType);
									if(serviceType.equals(serviceName)){
										if(service.getActions().length>0){
											JSONArray ja = new JSONArray();
											for(Action action : service.getActions()){
												JSONObject jo = new JSONObject();
												jo.put("actionName", action.getName());
												ja.put(jo);
											}
											JSONObject mainObj = new JSONObject();
											mainObj.put("service", serviceName);
											mainObj.put("actions", ja);
											answer=mainObj.toString();
										}else answer = "The service has no actions.";
									}
								}
							}else answer="The number of device is 0.";
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else if(getReference().getRemainingPart().contains("parameters")){
						Log.d("MyApp", "parameters");
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
					}else if(getReference().getRemainingPart().contains("proba")){
						Log.d("MyApp", "actionparameters");
						answer="There is no function asociated to the request.";
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