package com.popupsolution.cloudstax.android.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.popupsolution.cloudstax.android.R;

public class AndroidTabLayoutActivity extends TabActivity {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        TabHost tabHost = getTabHost();
        
        // the first Account
        TabSpec accountTab = tabHost.newTabSpec("Account");
        // setting Title and Icon for the Tab
        accountTab.setIndicator("", getResources().getDrawable(R.drawable.menu_account));
        Intent accountIntent = new Intent(this, menu1a3.class);
        accountTab.setContent(accountIntent);
        
        // Tab for Slide
        TabSpec slideTab = tabHost.newTabSpec("Slide");
        slideTab.setIndicator("", getResources().getDrawable(R.drawable.menu_slideshow));
        Intent slideIntent = new Intent(this, menu2.class);
        slideTab.setContent(slideIntent);
        
        // Tab for Device
        TabSpec deviceTab = tabHost.newTabSpec("Device");
        deviceTab.setIndicator("", getResources().getDrawable(R.drawable.menu_device));
        Intent deviceIntent = new Intent(this, menu3.class);
        deviceTab.setContent(deviceIntent);
        
        // Adding all Tab to TabHost
        
        tabHost.addTab(accountTab); 
        tabHost.addTab(slideTab); 
        tabHost.addTab(deviceTab); 
    }

}