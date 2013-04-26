package com.popupsolution.cloudstax.android;

import com.popupsolution.cloudstax.android.log.Log;
import com.popupsolution.cloudstax.android.log.LogFactory;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootUpReceiver extends BroadcastReceiver {
	private static final Log log = LogFactory.getLog(BootUpReceiver.class);

	@Override
	public void onReceive(Context context, Intent intent) {
		//helper.toast(context, "BootUpReceiver");
		log.debug("BootUpReceive");
	    Intent i = new Intent(context, MainActivity.class);
	    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    context.startActivity(i);  
	    /*
		long interval = 5000;
	    AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	    PendingIntent pi = PendingIntent.getService(context, 0, new Intent(context, CloudStaxAndroidTVActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
		am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + interval , interval, pi);
	    */
	}

}
