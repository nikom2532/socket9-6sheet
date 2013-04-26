package com.popupsolution.cloudstax.android.activity;

import com.popupsolution.cloudstax.android.ActivitySelector;
import com.popupsolution.cloudstax.android.Configuration;
import com.popupsolution.cloudstax.android.R;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Settings.System;
//import android.widget.ProgressBar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
//import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class menu3 extends Activity {

	// private RadioButton mRadioButtonWifiOn;
	// private RadioButton mRadioButtonWifiOff;
	private Button mButtonWifiSetting;
	private Button mDisplayBrightness;
	private int mBrightness;
	private ContentResolver mResolver;
	private Window mWindow;

	Context mContext;

	private TextView contentTxt;
	private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context arg0, Intent intent) {
			// TODO Auto-generated method stub
			int level = intent.getIntExtra("level", 0);
			contentTxt.setText(String.valueOf(level) + "%");
		}
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu3);

		// mRadioButtonWifiOn = (RadioButton)findViewById(R.id.radioWifiOn);
		// mRadioButtonWifiOff = (RadioButton)findViewById(R.id.radioWifiOff);
		mButtonWifiSetting = (Button) findViewById(R.id.buttonOnWifiSetting);
		mDisplayBrightness = (Button) findViewById(R.id.buttonBrightness);

//		mSeekBarDisplayBrightness.setMax(255);
//		mSeekBarDisplayBrightness.setProgress((int) (android.provider.Settings.System.getInt(
//	            getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, 255)));
//
//		mSeekBarDisplayBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {  
//	        public void onStopTrackingTouch(SeekBar seekBar) {}
//	        public void onStartTrackingTouch(SeekBar seekBar) {}  
//
//	        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//	            if (fromUser) {
//	                android.provider.Settings.System.putInt(getContentResolver(),
//	                        android.provider.Settings.System.SCREEN_BRIGHTNESS, progress);
//
//	                WindowManager.LayoutParams layoutParams = getWindow().getAttributes();  
//	                layoutParams.screenBrightness = (float)progress / 255;
//
//	                getWindow().setAttributes(layoutParams);
//	            }
//	        }
//	    });

		// if(Configuration.getPrivateString("WifiOn") == "true"){
		// mRadioButtonWifiOn.setChecked(true);
		// }
		// else{
		// mRadioButtonWifiOff.setChecked(true);
		// }

		// mSeekBarDisplayBrightness.setOnSeekBarChangeListener(new
		// SeekBar.OnSeekBarChangeListener(){
		//
		// @Override
		// public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
		// // TODO Auto-generated method stub
		// float BackLightValue = (float)arg1/100;
		// mBackLightSetting.setText(String.valueOf(BackLightValue));
		//
		// WindowManager.LayoutParams layoutParams =
		// getWindow().getAttributes();
		// layoutParams.screenBrightness = BackLightValue;
		// getWindow().setAttributes(layoutParams);
		//
		//
		// }
		//
		// @Override
		// public void onStartTrackingTouch(SeekBar arg0) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void onStopTrackingTouch(SeekBar arg0) {
		// // TODO Auto-generated method stub
		//
		// }});

		// String strDisplayBrightness =
		// Configuration.getPrivateString("DisplayBrightness");
		// if(null != strDisplayBrightness) {
		// mSeekBarDisplayBrightness.setProgress(Integer.parseInt(strDisplayBrightness));
		// }

		// mRadioButtonWifiOn.setText(String.valueOf(Configuration.getPrivateString("DisplayBrightness")));

		// ProgressBar bar = (ProgressBar)findViewById(R.id.progressBar1);
		// bar.getLayoutParams().height = 500;
		// bar.invalidate();

		contentTxt = (TextView) this.findViewById(R.id.monospaceTxt);
		this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED));
	}

	public void onWifiSettingClick(View v) {
		startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
	}
	
	public void onDisplaySettingClick(View v) {
		startActivity(new Intent(android.provider.Settings.ACTION_DISPLAY_SETTINGS));
	}

	
	public void onStartClick(View v) {
		// Configuration.setPrivateString("WifiOn",
		// String.valueOf(mRadioButtonWifiOn.isChecked()));
		// Configuration.setPrivateString("WifiOff",
		// String.valueOf(mRadioButtonWifiOff.isChecked()));
		// Configuration.setPrivateString("WifiSetting",
		// String.valueOf(mButtonWifiSetting.isChecked()));
		// Configuration.setPrivateString("DisplayBrightness",
		// mSeekBarDisplayBrightness.getText().toString());
		// Configuration.setPrivateString("DisplayBrightness",
		// String.valueOf(mSeekBarDisplayBrightness.);

//		Configuration.setPrivateString("DisplayBrightness",
//				String.valueOf(mSeekBarDisplayBrightness.getProgress()));
		ActivitySelector.StartSlideShowActivity();

	}
}