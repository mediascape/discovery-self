/*
** Long Library Name:
**	AccelerometerListener Class
**
** Acronym and its version:
**	AccelerometerListener v1.0
**
** Copyright claim:
**	Copyright ( C ) 2013-2014 Vicomtech-IK4 ( http://www.vicomtech.org/ ),
**	all rights reserved.
**
** Authors (in alphabetical order):
**	Angel Martin <amartin@vicomtech.org>,
**	Ion Alberdi <ialberdi@vicomtech.org>
**
** Description:		
**	The AccelerometerListener class is the class that works as a interface creating the method onShake.
** 
** Development Environment:
** 	The software has been implemented in Java, and tested in Chrome 
**	browsers and Android 4.3 OS devices.
**
** Dependencies:
** 	As MainActivity class depends on other libraries, the user must adhere to and 
**	keep in place any Licencing terms of those libraries:
**      Android v4.4.2 (http://developer.android.com/)
** 
** Licenses dependencies:
**	License Agreement for Android:
**		Apachev2 license (http://opensource.org/licenses/apache-2.0)
**		GNU LGPLv2.1 license (http://opensource.org/licenses/lgpl-2.1)
**
*/
package com.example.discoveryagentrest;

public interface AccelerometerListener {
    
    //public void onAccelerationChanged(float x, float y, float z);
  
    public void onShake(float force);
  
}