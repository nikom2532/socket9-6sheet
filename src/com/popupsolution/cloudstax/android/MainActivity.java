package com.popupsolution.cloudstax.android;

import com.popupsolution.cloudstax.android.Configuration;
import com.popupsolution.cloudstax.android.R;
import com.popupsolution.cloudstax.android.activity.AndroidTabLayoutActivity;
import com.popupsolution.cloudstax.android.activity.SlideShowActivityTest2;
import com.popupsolution.cloudstax.android.log.Log;
import com.popupsolution.cloudstax.android.log.LogFactory;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class MainActivity extends Activity {
	private static final Log log = LogFactory.getLog(MainActivity.class);
	
	Context mContext;	
	Boolean mLoading = true;
	Boolean mStart = false;
	Handler mHandler = new Handler(); 
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		log.debug("onCreate");
		
		setContentView(R.layout.splash);
        mContext = this;
        Configuration.Init(this);
        int splashItem = (int)System.currentTimeMillis() % 3;
        if(splashItem < 0) splashItem*=-1;
	   switch(splashItem)
	    {
	        case 0:
	        	((ImageView) findViewById(R.id.imageViewSplash)).setImageResource(R.drawable.splash01);
	        	break;
	        case 1:
	        	((ImageView) findViewById(R.id.imageViewSplash)).setImageResource(R.drawable.splash02);
	        	break;
	        case 2:
	        	((ImageView) findViewById(R.id.imageViewSplash)).setImageResource(R.drawable.splash03);
	        	break;
	    }
	}
	
    @Override
    protected void onStart() {
        super.onStart();  // Always call the superclass method first
        log.debug("onStart");
        mStart = true;
        Thread splashTread = new Thread()
        {
        	public void run()
        	{
        		try
				{
        			sleep(3000);
				} 
				catch (InterruptedException e) 
				{} 
				finally		
				{
					mHandler.post(new Runnable() {
						public void run() {
							if(mStart) {
								log.info("ActivitySelector.Init");
								ActivitySelector.Init((Activity) mContext);
							}
						}
					});
				}        		
        	}
        };
        
        splashTread.start();
		mLoading = false;
    }
    
    @Override  
    public void onStop() {    
    	super.onStop();
    	log.debug("onStop");
    	mStart = false;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	log.debug("onActivityResult");
		finish();
		ActivitySelector.Clean();
    }
    
    @Override      
    protected void onDestroy() 
    {
    	super.onDestroy();
    	log.debug("onDestroy");
    }    
    
    
    
/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
*/
}
