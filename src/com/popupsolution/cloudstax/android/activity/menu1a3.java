package com.popupsolution.cloudstax.android.activity;

import java.util.Calendar;
import com.popupsolution.cloudstax.android.Configuration;
import com.popupsolution.cloudstax.android.Constants;
import com.popupsolution.cloudstax.android.R;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.popupsolution.cloudstax.android.*;

//import Configuration.setPrivateString;

public class menu1a3 extends Activity {
	public static EditText mEditTextHashtag;
	public static EditText mEditTextUsername;
	public static EditText mEditTextLocationSpot;
	public static EditText mEditTextLocationIn;
	public static TimePicker mDatePickerBegining;
	public static CalendarView mCalendar;

	//time
	public static TimePicker mTimePickerBegining;
	public static CalendarView mCalendarViewBegining;
	
	public static RadioButton mRadioButtonBeginingAllTime;
	public static RadioButton mRadioButtonBeginingSetTime;
	
	Context mContext;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu1a3);
		mContext = this;
		onCreateMethod();
	}

	public void onCreateMethod() {
		
		
		// ###### Layout ########
		LinearLayout LinearLayoutDateTime = (LinearLayout)this.findViewById(R.id.LinearLayout2);		

		// ###### Hash EditText #######
		mEditTextHashtag = (EditText) findViewById(R.id.EditTextHashtag);
		mEditTextUsername = (EditText) findViewById(R.id.EditTextUsername);
		mEditTextLocationSpot = (EditText) findViewById(R.id.EditTextLocationSpot);
		mDatePickerBegining = (TimePicker) findViewById(R.id.timePickerBegining);
		mEditTextLocationIn = (EditText) findViewById(R.id.EditTextLocationIn);
		mCalendar = (CalendarView) findViewById(R.id.calendarView);

		//time
		mTimePickerBegining = (TimePicker) findViewById(R.id.timePickerBegining);
		mCalendarViewBegining = (CalendarView) findViewById(R.id.calendarView);
		
		mRadioButtonBeginingAllTime = (RadioButton) findViewById(R.id.RadioBeginingAllTime);
		mRadioButtonBeginingSetTime = (RadioButton) findViewById(R.id.RadioBeginingSetTime);
		
		
		// ####### make search keyboard
		//mEditTextHashtag.setInputType(InputType.TYPE_CLASS_TEXT);
		mEditTextUsername.setInputType(InputType.TYPE_CLASS_TEXT);
		mEditTextLocationSpot.setInputType(InputType.TYPE_CLASS_TEXT);
		mEditTextLocationIn.setInputType(InputType.TYPE_CLASS_TEXT);
		
		// ####### make enter key when search
		mEditTextHashtag.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((keyCode == KeyEvent.KEYCODE_ENTER)) {
			    	if(event.getAction() == KeyEvent.ACTION_UP) onStartClickMethod();
			    	return true;
				} else
					return false;
			}
		});
		
		mEditTextUsername.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((keyCode == KeyEvent.KEYCODE_ENTER) && event.getAction() == KeyEvent.ACTION_UP) {
			    	onStartClickMethod();
			    	return true;
				} else
					return false;
			}
		});
		mEditTextLocationSpot.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((keyCode == KeyEvent.KEYCODE_ENTER) && event.getAction() == KeyEvent.ACTION_UP) {
			    	onStartClickMethod();
			    	return true;
				} else
					return false;
			}
		});
		mEditTextLocationIn.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((keyCode == KeyEvent.KEYCODE_ENTER) && event.getAction() == KeyEvent.ACTION_UP) {
			    	onStartClickMethod();
			    	return true;
				} else
					return false;
			}
		});
		
		// Display current value
		mEditTextHashtag.setText(Configuration
				.getPrivateString(Constants.account_Hashtag));
		mEditTextUsername.setText(Configuration
				.getPrivateString(Constants.account_Username));
		mEditTextLocationSpot.setText(Configuration
				.getPrivateString(Constants.account_LocationSpot));
		// mDatePickerBegining.setText(Configuration.getPrivateString(Constants.account_Begining));
		mEditTextLocationIn.setText(Configuration
				.getPrivateString(Constants.account_LocationIn));

		//### Get Begining Time
		if (null != Configuration.getPrivateString(Constants.account_RadioBeginingAllTime) && Configuration.getPrivateString(Constants.account_RadioBeginingAllTime).equalsIgnoreCase("true")) {
			mRadioButtonBeginingAllTime.setChecked(true);
		} else {
			//mRadioButtonBeginingAllTime.setChecked(false);
		}
		
		if (null != Configuration.getPrivateString(Constants.account_RadioBeginingSetTime) && Configuration.getPrivateString(Constants.account_RadioBeginingSetTime).equalsIgnoreCase("true")) {
			mRadioButtonBeginingSetTime.setChecked(true);
//			LinearLayoutDateTime.setVisibility(LinearLayout.VISIBLE);
		} else {
			//mRadioButtonBeginingSetTime.setChecked(false);
		}		
		
		// Set default time check
		if(false == mRadioButtonBeginingSetTime.isChecked() && false == mRadioButtonBeginingAllTime.isChecked()) {
			mRadioButtonBeginingAllTime.setChecked(true);
		}
				
		//#####Get Time 20130318
//		Configuration.setPrivateString(Constants.account_timePickerBeginingHour, "0");
		if(null != Configuration.getPrivateString(Constants.account_timePickerBeginingHour)){
			int aHour = 0;
			aHour = Integer.parseInt(Configuration.getPrivateString(Constants.account_timePickerBeginingHour));
			mTimePickerBegining.setCurrentHour(aHour);
		}
		
//		Configuration.setPrivateString(Constants.account_timePickerBeginingMinute, "0");
		if(null != Configuration.getPrivateString(Constants.account_timePickerBeginingMinute)){
			int aMinute = 0;
			aMinute = Integer.parseInt(Configuration.getPrivateString(Constants.account_timePickerBeginingMinute));
			mTimePickerBegining.setCurrentMinute(aMinute);
		}
		
		//###### Get Date
		if(null != Configuration.getPrivateString(Constants.account_calendarViewBegining)){
			long aDate = 0;
			aDate = Long.parseLong(Configuration.getPrivateString(Constants.account_calendarViewBegining));
			mCalendarViewBegining.setDate(aDate);
		}
		
		// ###### Set to show DateTime or not #######
		if(mRadioButtonBeginingAllTime.isChecked()==true){
			LinearLayoutDateTime.setVisibility(LinearLayout.GONE);
		}
		else if(mRadioButtonBeginingSetTime.isChecked()==true){
			LinearLayoutDateTime.setVisibility(LinearLayout.VISIBLE);
		}
		
		//##### Log Date
//		Log.v("date", String.valueOf(mCalendarViewBegining.getDate()));
//		Log.v("date_account_calendarViewBegining", Configuration.getPrivateString(Constants.account_calendarViewBegining));
		Log.v("mCalendarViewBegining", String.valueOf(mCalendarViewBegining.getDate()));
		Log.v("currentTimeMillis", String.valueOf(System.currentTimeMillis()));
		
		//## initial Menu 2 fields
		initMenu2();
	}
	public void onChangeTimeNow(View v){
		mCalendarViewBegining = (CalendarView) findViewById(R.id.calendarView);
		mCalendarViewBegining.setDate(Long.parseLong(String.valueOf(System.currentTimeMillis())));
	}
	
	public void onChangeShowDateTime(View v){
		// ###### Layout ########
		LinearLayout LinearLayoutDateTime = (LinearLayout)this.findViewById(R.id.LinearLayout2);		

		// Why this function need to call ?
		//onCreateMethod();
		
		mRadioButtonBeginingAllTime = (RadioButton) findViewById(R.id.RadioBeginingAllTime);
		mRadioButtonBeginingSetTime = (RadioButton) findViewById(R.id.RadioBeginingSetTime);
				
		// ###### Set to show DateTime or not #######
		if(mRadioButtonBeginingAllTime.isChecked()==true){
			LinearLayoutDateTime.setVisibility(LinearLayout.GONE);
		}
		else if(mRadioButtonBeginingSetTime.isChecked()==true){
			LinearLayoutDateTime.setVisibility(LinearLayout.VISIBLE);
		}
	}

	public void onStartClick(View v) {
		onStartClickMethod();
	}
		
	public void onStartClickMethod() {
		// Save value
		// helper.toast(mContext, mEditTextHashtag.getText().toString());
		Configuration.setPrivateString(Constants.account_Hashtag,
				mEditTextHashtag.getText().toString());
		Configuration.setPrivateString(Constants.account_Username,
				mEditTextUsername.getText().toString());
		Configuration.setPrivateString(Constants.account_LocationSpot,
				mEditTextLocationSpot.getText().toString());
		Configuration.setPrivateString(Constants.account_LocationIn,
				mEditTextLocationIn.getText().toString());

		//### set time
		Configuration.setPrivateString(Constants.account_timePickerBeginingHour,
				String.valueOf(mTimePickerBegining.getCurrentHour()));
		Configuration.setPrivateString(Constants.account_timePickerBeginingMinute,
				String.valueOf(mTimePickerBegining.getCurrentMinute()));
		
		//### set Date
		Configuration.setPrivateString(Constants.account_calendarViewBegining,
				String.valueOf(mCalendarViewBegining.getDate()));

		//### set Begining Time
		Configuration.setPrivateString(Constants.account_RadioBeginingAllTime, 
				String.valueOf(mRadioButtonBeginingAllTime.isChecked()));	
		Configuration.setPrivateString(Constants.account_RadioBeginingSetTime, 
				String.valueOf(mRadioButtonBeginingSetTime.isChecked()));	
		
		//################### Tab 2 ######################
		
//		menu2.onTabTwo();
		
		if (null != menu2.mSeekBarDelayTime) {
			Configuration.setPrivateString(Constants.slide_DelayTime,
					String.valueOf(menu2.mSeekBarDelayTime.getProgress()));
			Log.v("slide_DelayTime", Configuration.getPrivateString(Constants.slide_DelayTime));
		}
		else{
			Configuration.setPrivateString(Constants.slide_DelayTime, "5");
			Log.v("slide_DelayTime", Configuration.getPrivateString(Constants.slide_DelayTime));
		}
		
		if (null != menu2.mCheckBoxFade) {
			Configuration.setPrivateString(Constants.slide_Fade,
					String.valueOf(menu2.mCheckBoxFade.isChecked()));
		}
		else{
			Configuration.setPrivateString(Constants.slide_Fade, "true");
		}
		Log.v("slide_Fade", Configuration.getPrivateString(Constants.slide_Fade));
		
		if (null != menu2.mCheckBoxHPush) {
			Configuration.setPrivateString(Constants.slide_HPush,
					String.valueOf(menu2.mCheckBoxHPush.isChecked()));
		}
		else{
			Configuration.setPrivateString(Constants.slide_HPush, "false");
		}
		Log.v("slide_HPush", Configuration.getPrivateString(Constants.slide_HPush));

		if (null != menu2.mCheckBoxPush) {
			Configuration.setPrivateString(Constants.slide_Push,
					String.valueOf(menu2.mCheckBoxPush.isChecked()));
		}
		else{
			Configuration.setPrivateString(Constants.slide_Push, "false");
		}
		Log.v("slide_Push", Configuration.getPrivateString(Constants.slide_Push));

		if (null != menu2.mCheckBoxSnap) {
			Configuration.setPrivateString(Constants.slide_Snap,
					String.valueOf(menu2.mCheckBoxSnap.isChecked()));
		}
		else{
			Configuration.setPrivateString(Constants.slide_Snap, "false");
		}
		Log.v("slide_Snap", Configuration.getPrivateString(Constants.slide_Snap));

		if (null != menu2.mCheckBoxRandom) {
			Configuration.setPrivateString(Constants.slide_Random,
					String.valueOf(menu2.mCheckBoxRandom.isChecked()));
		}
		else{
			Configuration.setPrivateString(Constants.slide_Random, "false");
		}
		Log.v("slide_Random", Configuration.getPrivateString(Constants.slide_Random));

		if (null != menu2.mCheckBoxBackgroundWhite) {
			Configuration.setPrivateString(Constants.slide_BackgroundWhite,
					String.valueOf(menu2.mCheckBoxBackgroundWhite.isChecked()));
		}
		else{
			Configuration.setPrivateString(Constants.slide_BackgroundWhite, "true");
		}
		Log.v("slide_BackgroundWhite", Configuration.getPrivateString(Constants.slide_BackgroundWhite));

		if (null != menu2.mCheckBoxBackgroundChoose) {
			Configuration.setPrivateString(Constants.slide_BackgroundChoose, 
					String.valueOf(menu2.mCheckBoxBackgroundChoose.isChecked()));
		}
		else{
			Configuration.setPrivateString(Constants.slide_BackgroundChoose, "false");
		}
		Log.v("slide_BackgroundChoose", Configuration.getPrivateString(Constants.slide_BackgroundChoose));

		if (null != menu2.mSwitchAvatar) {
			Configuration.setPrivateString(Constants.slide_AvatarOn,
					String.valueOf(menu2.mSwitchAvatar.isChecked()));
		}
		else{
			Configuration.setPrivateString(Constants.slide_AvatarOn, "false");
		}
		Log.v("slide_AvatarOn", Configuration.getPrivateString(Constants.slide_AvatarOn));

		if (null != menu2.mSwitchCaption) {
			Configuration.setPrivateString(Constants.slide_CaptionOn,
					String.valueOf(menu2.mSwitchCaption.isChecked()));
		}
		else{
			Configuration.setPrivateString(Constants.slide_CaptionOn, "false");
		}
		Log.v("slide_CaptionOn", Configuration.getPrivateString(Constants.slide_CaptionOn));
		
		if (null != menu2.mSwitchLike) {
			Configuration.setPrivateString(Constants.slide_LikeOn,
					String.valueOf(menu2.mSwitchLike.isChecked()));
		}
		else{
			Configuration.setPrivateString(Constants.slide_LikeOn, "false");
		}
		Log.v("slide_LikeOn", Configuration.getPrivateString(Constants.slide_LikeOn));

		if (null != menu2.mTextViewPath) {
			Configuration.setPrivateString(Constants.slide_Path,
					String.valueOf(menu2.mTextViewPath.getText()));
		}
		Log.v("mTextViewPath", Configuration.getPrivateString(Constants.slide_Path));
		
		if(null != menu2.mEditTextCountSearch){
			Configuration.setPrivateString(Constants.slide_EditTextCountSearch, 
					String.valueOf(menu2.mEditTextCountSearch.getText()));			
		}
		


		/* #### this command is start to show the slide ###### */
		ActivitySelector.StartSlideShowActivity();
		// mSearchPhotoService.BeginSearch(mTag, null, null, null, null,
		// mOnSearchComplete);
		// mTextViewDebug.setText("Loading...");
	}

	public static void initMenu2() {
		if(Configuration.getPrivateString(Constants.slide_DelayTime) == null){
			Configuration.setPrivateString(Constants.slide_DelayTime, "5");
			Configuration.setPrivateString(Constants.slide_Fade, "true");
			Configuration.setPrivateString(Constants.slide_HPush, "false");
			Configuration.setPrivateString(Constants.slide_Push, "false");
			Configuration.setPrivateString(Constants.slide_Snap, "false");
			Configuration.setPrivateString(Constants.slide_Random, "false");
			Configuration.setPrivateString(Constants.slide_BackgroundWhite, "true");
			Configuration.setPrivateString(Constants.slide_BackgroundChoose, "false");
			Configuration.setPrivateString(Constants.slide_CaptionOn, "false");
			Configuration.setPrivateString(Constants.slide_AvatarOn, "false");
			Configuration.setPrivateString(Constants.slide_Path, "");
			Configuration.setPrivateString(Constants.slide_EditTextCountSearch, "10");
			Configuration.setPrivateString(Constants.slide_LikeOn, "false");
			Configuration.setPrivateString(Constants.slide_LikeOff, "true");
		}
	}

}
