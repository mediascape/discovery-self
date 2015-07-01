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

import java.util.Random;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.restlet.data.Form;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.example.discoveryagentrest.R;

public class Notification extends ServerResource {
	/*
	 * toString
	 * 
	 * Answer to GET type queries.
	 * 
	 * @return {String} The answer to GET query.
	 */
	@Get
    public void doGet() {
		try {
			Log.d("MyApp", "Get Notification");
			if(getReference().getRemainingPart().contains("url=")){
				String url = getValue("url=");
				int min = 0;
				int max = 1000000000;

				Random r = new Random();
				int value = r.nextInt(max - min + 1) + min;
				
				if (!url.startsWith("http://") && !url.startsWith("https://")) url = "http://" + url;
				
				
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				
				PendingIntent intent = 
			             PendingIntent.getActivity(com.example.discoveryagentrest.ServiceBoot.context, 0, browserIntent, PendingIntent.FLAG_UPDATE_CURRENT);
				
				NotificationCompat.Builder mBuilder =
				        new NotificationCompat.Builder(com.example.discoveryagentrest.ServiceBoot.context)
				        .setSmallIcon(R.drawable.mediascape)
				        .setContentIntent(intent)
				        .setContentTitle("Mediascape")
				        .setContentText(url)
				        .setAutoCancel(true);
				
				NotificationManager mNotificationManager =
					    (NotificationManager) com.example.discoveryagentrest.ServiceBoot.context.getSystemService(Context.NOTIFICATION_SERVICE);
				mNotificationManager.notify(value, mBuilder.build());
			}
		} catch (ActivityNotFoundException e) {
			Log.d("MyApp", "No application can handle this request."
		        + " Please install a webbrowser");
		    e.printStackTrace();
		}
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
	@Post
    public void doPost(String value) {
		Form form = new Form(value);
		String url = form.getFirstValue("url").trim();
		int min = 0;
		int max = 1000000000;

		Random r = new Random();
		int notifictionId = r.nextInt(max - min + 1) + min;
		
		if (!url.startsWith("http://") && !url.startsWith("https://")) url = "http://" + url;
		
		
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		PendingIntent intent = 
	             PendingIntent.getActivity(com.example.discoveryagentrest.ServiceBoot.context, 0, browserIntent, 0);
		
		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(com.example.discoveryagentrest.ServiceBoot.context)
		        .setSmallIcon(R.drawable.mediascape)
		        .setContentIntent(intent)
		        .setContentTitle("Mediascape")
		        .setContentText(url)
		        .setPriority(1);
		NotificationManager mNotificationManager =
			    (NotificationManager) com.example.discoveryagentrest.ServiceBoot.context.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(notifictionId, mBuilder.build());
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