/*
** Long Library Name:
**	MainActivity Class
**
** Acronym and its version:
**	MainActivity v1.0
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
**	The MainActivity class is the class that initiates the activity.
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

import com.example.discoveryagentrest.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity{
	/**
     * onCreate
     * 
     * ServiceBoot class creation event listener.
     * 
     * @param {Bundle} savedInstanceState
     * 
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	    
	    //create the service when app starts
	    Intent serviceIntent = new Intent(this,ServiceBoot.class);
        startService(serviceIntent);
        
        Intent intent1 = new Intent(this, BluetoothService.class);
        //intent1.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
		startService(intent1);
	}
}