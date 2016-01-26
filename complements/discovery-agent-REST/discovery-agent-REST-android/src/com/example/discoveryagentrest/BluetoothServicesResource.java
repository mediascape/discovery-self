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

import java.util.UUID;

import android.os.ParcelUuid;
import android.os.Parcelable;
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
import org.restlet.resource.ServerResource;

import com.example.discoveryagentrest.BluetoothService;

public class BluetoothServicesResource extends ServerResource {
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
		if(getReference().getRemainingPart().contains("device=")){
			Log.d("MyApp", "BluetoothServices");
			try {
				int deviceNumber = Integer.parseInt(getValue("device="));
				if(getReference().getRemainingPart().indexOf("=")!=-1){
					if((deviceNumber-1) <= BluetoothService.listBluetooth.size()){
						JSONArray ja = new JSONArray();
						if(BluetoothService.listBluetooth.get(deviceNumber-1).getUuids()!=null){
							if(BluetoothService.listBluetooth.get(deviceNumber-1).getUuids().length>0){
								for (Parcelable parcelable : BluetoothService.listBluetooth.get(deviceNumber-1).getUuids()){
									JSONObject jo = new JSONObject();
									ParcelUuid parcelUuid = (ParcelUuid) parcelable;
									UUID uuid = parcelUuid.getUuid();
									Log.d("MyApp", uuid.toString());					
									jo.put("serviceUuid", uuid.toString());
									jo.put("name", getUuidName(uuid.toString().toUpperCase()));
									ja.put(jo);
								}
								JSONObject mainObj = new JSONObject();
								mainObj.put("services", ja);
								if(getReference().getRemainingPart().contains("callback")) answer="servicesBluetooth("+mainObj.toString()+")";
								else answer=mainObj.toString();
								Log.d("MyApp", "services: "+answer);
								representation = new StringRepresentation(answer, MediaType.APPLICATION_JSON);
							} else {
								if(getReference().getRemainingPart().contains("callback")) representation = new StringRepresentation("servicesBluetooth({\"services\":\"The number of services is 0.\"})", MediaType.APPLICATION_JSON);
								else representation = new StringRepresentation("{\"services\":\"The number of services is 0.\"}", MediaType.APPLICATION_JSON);
							}
						} else {
							if(getReference().getRemainingPart().contains("callback")) representation = new StringRepresentation("servicesBluetooth({\"services\":\"The number of services is 0.\"})", MediaType.APPLICATION_JSON);
							else representation = new StringRepresentation("{\"services\":\"The number of services is 0.\"}", MediaType.APPLICATION_JSON);
						}
					} else {
						if(getReference().getRemainingPart().contains("callback")) representation = new StringRepresentation("servicesBluetooth({\"services\":\"The number of device is 0.\"})", MediaType.APPLICATION_JSON);
						else representation = new StringRepresentation("{\"services\":\"The number of device is 0.\"}", MediaType.APPLICATION_JSON);
					}
				} else {
					if(getReference().getRemainingPart().contains("callback")) representation = new StringRepresentation("servicesUpnp({\"services\":\"The device id has not been defined.\"})", MediaType.APPLICATION_JSON);
					else representation = new StringRepresentation("{\"services\":\"The device id has not been defined.\"}", MediaType.APPLICATION_JSON);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
		Log.d("MyApp", "BluetoothServices");
		try {
			if(BluetoothService.bluetoothSensor.isEnabled()){
				/*if(!ServiceBoot.bluetoothSensor.isDiscovering()){
					ServiceBoot.listAdapter.clear();
					ServiceBoot.bluetoothSensor.startDiscovery();
				}*/
				Form form = new Form(value); 
				int deviceNumber = Integer.parseInt(form.getFirstValue("device"));
				if(deviceNumber > 0){
					if(BluetoothService.listBluetooth.size()>0){
						JSONArray ja = new JSONArray();
						if(BluetoothService.listBluetooth.get(deviceNumber-1).getUuids().length>0){
							for (Parcelable parcelable : BluetoothService.listBluetooth.get(deviceNumber-1).getUuids()){
								JSONObject jo = new JSONObject();
								ParcelUuid parcelUuid = (ParcelUuid) parcelable;
								UUID uuid = parcelUuid.getUuid();
								jo.put("serviceUuid", uuid.toString());
								jo.put("name", getUuidName(uuid.toString().toUpperCase()));
								ja.put(jo);
							}
							JSONObject mainObj = new JSONObject();
							mainObj.put("services", ja);
							answer=mainObj.toString();
						}else answer="The number of services is 0.";
					}else answer="The number of device is 0.";
				}else answer="The device id has not been defined.";
			}else answer="The bluetooth is not enabled.";
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return answer;
    }
	
	public String getValue(String variable){
		Log.d("MyApp", "getValue");	
		String remaining=getReference().getRemainingPart();
		if(remaining.indexOf("&", remaining.indexOf(variable))!=-1) return remaining.substring(remaining.indexOf(variable)+variable.length(), remaining.indexOf("&", remaining.indexOf(variable)));
		else return remaining.substring(remaining.indexOf(variable)+variable.length());
	}
	
	public String getUuidName(String value){
		Log.d("MyApp", "getUuidName:"+value);
		if( "00000000-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "BASE";
		} else if( "00000001-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "SDP";
		} else if( "00000002-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "UDP";
		} else if( "00000003-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "RFCOMM";
		} else if( "00000004-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "TCP";
		} else if( "00000005-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "TCSBIN";
		} else if( "00000006-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "TCSAT";
		} else if( "00000008-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "OBEX";
		} else if( "00000009-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "IP";
		} else if( "0000000A-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "FTP";
		} else if( "0000000C-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "HTTP";
		} else if( "0000000E-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "WSP";
		} else if( "0000000F-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "BNEP";
		} else if( "00000010-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "UPNP";
		} else if( "00000011-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "HID";
		} else if( "00000012-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "HCCC";
		} else if( "00000014-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "HCDC";
		} else if( "00000016-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "HN";
		} else if( "00000017-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "AVCTP";
		} else if( "00000019-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "AVDTP";
		} else if( "0000001B-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "CMPT";
		} else if( "0000001D-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "UDI_C_PLANE";
		} else if( "0000001D-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "MCAPControlChannel";
		} else if( "0000001D-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "MCAPDataChannel";
		} else if( "00000100-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "L2CAP";
		} else if( "00001000-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "ServiceDiscoveryServerService";
		} else if( "00001001-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "BrowseGroupDescriptorService";
		} else if( "00001002-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "PublicBrowseGroupService";
		} else if( "00001101-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "SerialPortService";
		} else if( "00001102-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "LANAccessUsingPPPService";
		} else if( "00001103-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "DialupNetworkingService";
		} else if( "00001104-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "IrMCSyncService";
		} else if( "00001105-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "OBEXObjectPushService";
		} else if( "00001106-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "OBEXFileTransferService";
		} else if( "00001107-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "IrMCSyncCommandService";
		} else if( "00001108-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "HeadsetService";
		} else if( "00001109-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "CordlessTelephonyService";
		} else if( "0000110A-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "AudioSourceService";
		} else if( "0000110B-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "AudioSinkService";
		} else if( "0000110C-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "AVRemoteControlTargetService";
		} else if( "0000110D-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "AdvancedAudioDistributionService";
		} else if( "0000110E-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "AVRemoteControlService";
		} else if( "0000110F-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "VideoConferencingService";
		} else if( "00001110-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "IntercomService";
		} else if( "00001111-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "FaxService";
		} else if( "00001112-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "HeadsetAudioGatewayService";
		} else if( "00001113-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "WAPService";
		} else if( "00001114-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "WAPClientService";
		} else if( "00001115-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "PANUService";
		} else if( "00001116-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "NAPService";
		} else if( "00001117-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "GNService";
		} else if( "00001118-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "DirectPrintingService";
		} else if( "00001119-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "ReferencePrintingService";
		} else if( "0000111A-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "ImagingService";
		} else if( "0000111B-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "ImagingResponderService";
		} else if( "0000111C-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "ImagingAutomaticArchiveService";
		} else if( "0000111D-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "ImagingReferenceObjectsService";
		} else if( "0000111E-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "HandsfreeService";
		} else if( "0000111F-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "HandsfreeAudioGatewayService";
		} else if( "00001120-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "DirectPrintingReferenceObjectsService";
		} else if( "00001121-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "ReflectedUIService";
		} else if( "00001122-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "BasicPrintingService";
		} else if( "00001123-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "PrintingStatusService";
		} else if( "00001124-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "HumanInterfaceDeviceService";
		} else if( "00001125-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "HardcopyCableReplacementService";
		} else if( "00001126-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "HCRPrintService";
		} else if( "00001127-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "HCRScanService";
		} else if( "00001128-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "CommonISDNAccessService";
		} else if( "00001129-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "VideoConferencingGWService";
		} else if( "0000112A-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "UDIMTService";
		} else if( "0000112B-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "UDITAService";
		} else if( "0000112C-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "AudioVideoService";
		} else if( "0000112D-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "SIMAccessService";
		} else if( "0000112E-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "PCEService";
		} else if( "0000112F-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "PSEService";
		} else if( "00001130-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "PhonebookAccessService";
		} else if( "00001131-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "Headset2Service";
		} else if( "00001132-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "MessageAccessServerService";
		} else if( "00001133-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "MessageNotificationService";
		} else if( "00001134-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "MessageAccessService";
		} else if( "00001200-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "PnPInformationService";
		} else if( "00001201-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "GenericNetworkingService";
		} else if( "00001202-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "GenericFileTransferService";
		} else if( "00001203-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "GenericAudioService";
		} else if( "00001204-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "GenericTelephonyService";
		} else if( "00001205-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "UPnPService";
		} else if( "00001206-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "UPnPIpService";
		} else if( "00001300-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "ESdpUPnPIpPanService";
		} else if( "00001301-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "ESdpUPnPIpLapService";
		} else if( "00001302-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "EdpUPnpIpL2CAPService";
		} else if( "00001303-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "VideoSourceService";
		} else if( "00001304-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "VideoSinkService";
		} else if( "00001305-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "VideoDistributionService";
		} else if( "00001400-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "HealthDeviceService";
		} else if( "00001401-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "HealthDeviceSourceService";
		} else if( "00001402-0000-1000-8000-00805F9B34FB".equals(value)) {
			return "HealthDeviceSinkService";
		} else if( "426c6163-6b42-6572-7279-427970617373".equals(value)) {
			return "BlackBerryBypassService";
		} else if( "426c6163-6b42-6572-7279-44736b746f70".equals(value)) {
			return "BlackBerryDesktopService";
		} else if("00000000-DECA-FADE-DECA-DEAFDECACAFE".equals(value)) {
			return "VendorSpecificService";
		}else return "UnknownService";
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