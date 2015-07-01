/*
** Long Library Name:
**	AndroidUpnpService Interface
**
** Acronym and its version:
**	AndroidUpnpService v1.0
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
**	The AndroidUpnpService is an interface for interact with UPnP service. 
** 
** Development Environment:
** 	The software has been implemented in Java, and tested in Chrome 
**	browsers and Android 4.3 OS devices.
**
** Dependencies:
** 	As AndroidUpnpService class depends on other libraries, the user must adhere to and 
**	keep in place any Licencing terms of those libraries:
**		cling v2.0-alpha3  (http://4thline.org/projects/cling/)
** 
** Licenses dependencies:
**	License Agreement for Cling:
**		GNU LGPLv3 (http://www.gnu.org/licenses/lgpl.html)
**
*/
package com.example.discoveryagentrest;

import org.teleal.cling.UpnpService;
import org.teleal.cling.UpnpServiceConfiguration;
import org.teleal.cling.controlpoint.ControlPoint;
import org.teleal.cling.registry.Registry;

public interface AndroidUpnpService {
    public UpnpService get();
    public UpnpServiceConfiguration getConfiguration();
    public Registry getRegistry();
    public ControlPoint getControlPoint();
}
