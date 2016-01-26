/*
** Long Library Name:
**	BrowserUpnpService Package
**
** Acronym and its version:
**	BrowserUpnpService v1.0
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
**	The BrowserUpnpService class extends AndroidUpnpServiceImpl and create
**  a new AndroidUpnpServiceConfiguration. 
** 
** Development Environment:
** 	The software has been implemented in Java, and tested in Chrome 
**	browsers and Android 4.3 OS devices.
**
** Dependencies:
** 	As BrowserUpnpService class depends on other libraries, the user must adhere to and 
**	keep in place any Licencing terms of those libraries:
**		Android v4.4.2 (http://developer.android.com/)
**		Cling v2.0-alpha3  (http://4thline.org/projects/cling/)	
** 
** Licenses dependencies:
**  License Agreement for Android:
**		Apachev2 license (http://opensource.org/licenses/apache-2.0)
**		GNU LGPLv2.1 license (http://opensource.org/licenses/lgpl-2.1)
**  License Agreement for Cling:
**		GNU LGPLv3 (http://www.gnu.org/licenses/lgpl.html)
**
*/
package com.example.discoveryagentrest;

import android.net.wifi.WifiManager;
import org.teleal.cling.android.AndroidUpnpServiceConfiguration;
import org.teleal.cling.android.AndroidUpnpServiceImpl;

/**
 * @author Christian Bauer
 */

public class BrowserUpnpService extends AndroidUpnpServiceImpl {
	/**
     * createConfiguration
     * 
     * Creates the configuration for Android UPnP service.
     * 
     * @param {WifiManager} wifiManager
     * @return {AndroidUpnpServiceConfiguration} Android UPnP service configuration.
     * 
     */
    protected AndroidUpnpServiceConfiguration createConfiguration(WifiManager wifiManager) {
    	//Creates a new android UPnP service configuration
        return new AndroidUpnpServiceConfiguration(wifiManager) {
        	/*
            // DOC:REGISTRY
            @Override
            public int getRegistryMaintenanceIntervalMillis() {
                return 7000;
            }
            // DOC:REGISTRY

            // DOC:SERVICE_TYPE
            @Override
            public ServiceType[] getExclusiveServiceTypes() {
                return new ServiceType[]{
                    new UDAServiceType("SwitchPower")
                };
            }
            // DOC:SERVICE_TYPE
          
            */
        };
    }
}
