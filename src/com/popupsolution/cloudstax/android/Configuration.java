package com.popupsolution.cloudstax.android;

import java.util.List;

import com.popupsolution.cloudstax.android.activity.SlideShowActivity;
import com.popupsolution.cloudstax.android.log.Log;
import com.popupsolution.cloudstax.android.log.LogFactory;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.provider.Settings.Secure;
import android.widget.TextView;

public class Configuration {
	private static final Log log = LogFactory.getLog(Configuration.class);
	static Context mContext = null;
	
	static public int gVersionCode = 0;
	static public String gDeviceID = "";
	
	static public Boolean gNewSearch = false;

	static public TextView gDebugLogTextView = null;
	
	static public void Init(Context context)
	{
		mContext = context;
		
		gDeviceID = Secure.getString(mContext.getContentResolver(), Secure.ANDROID_ID); 
        
        try {
			PackageInfo pinfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
			gVersionCode = pinfo.versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	static public void Clean()
	{		
	}
	
	static public void setPrivateString(String name,String val)
	{
		SharedPreferences settings = ((Activity)mContext).getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(name, val);
        editor.commit();
        
        //if(Constants.slide_CaptionOn.equals(name)) log.debug("setPrivateString : "+val);
	}
	
	static public String getPrivateString(String name)
	{
		SharedPreferences settings = ((Activity)mContext).getPreferences(Context.MODE_PRIVATE);
		String access_token = settings.getString(name, null);
		//if(Constants.slide_CaptionOn.equals(name)) log.debug("getPrivateString : "+access_token);		
		return access_token;		
	}

	
	
}
