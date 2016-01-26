/*
** Long Library Name:
**	ReceiverBoot Class
**
** Acronym and its version:
**	ReceiverBoot v1.0
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
**	The ReceiverBoot class extends BroadcastReceiver and it is waiting for 
**  launching ServiceBoot service when OS starts.
** 
** Development Environment:
** 	The software has been implemented in Java, and tested in Chrome 
**	browsers and Android 4.3 OS devices.
**
** Dependencies:
** 	As ReceiverBoot class depends on other libraries, the user must adhere to and 
**	keep in place any Licencing terms of those libraries:
**		Android 4.4.2 (http://developer.android.com/)
** 
** Licenses dependencies:
**	License Agreement for Android:
**		Apachev2 license (http://opensource.org/licenses/apache-2.0)
**		GNU LGPLv2.1 license (http://opensource.org/licenses/lgpl-2.1)
**
*/
package com.example.discoveryagentrest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ReceiverBoot extends BroadcastReceiver {
	/**
     * onReceive
     * 
     * ReceiverBoot class's receive event listener. When receive invocation it boots
     * ServiceBoot service. 
     * 
     * @param {Bundle} savedInstanceState
     * 
     */
	@Override
	public void onReceive(Context context, Intent intent) {
		// Launch Service
		Intent serviceIntent = new Intent();
		serviceIntent.setAction("com.example.discoveryagentrest.ServiceBoot.MyService");
		context.startService(serviceIntent);
		
		Intent serviceIntent1 = new Intent();
		serviceIntent1.setAction("com.example.discoveryagentrest.BluetoothService.MyService");
		context.startService(serviceIntent1);
	}
}
