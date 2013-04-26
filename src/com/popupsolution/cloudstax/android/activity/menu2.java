package com.popupsolution.cloudstax.android.activity;

import com.popupsolution.cloudstax.android.ActivitySelector;
import com.popupsolution.cloudstax.android.Configuration;
import com.popupsolution.cloudstax.android.Constants;
import com.popupsolution.cloudstax.android.R;
import com.popupsolution.cloudstax.android.SearchPhotoService;
//import com.popupsolution.cloudstax.android.log.Log;
//import com.popupsolution.cloudstax.android.log.LogFactory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.util.Log;


public class menu2 extends Activity {
	public static SeekBar mSeekBarDelayTime;
	public static RadioButton mCheckBoxFade;
	public static RadioButton mCheckBoxHPush;
	public static RadioButton mCheckBoxPush;
	public static RadioButton mCheckBoxSnap;
	public static RadioButton mCheckBoxRandom;
	public static RadioButton mCheckBoxBackgroundWhite;
	public static RadioButton mCheckBoxBackgroundChoose;
	public static Switch mSwitchCaption;
	public static Switch mSwitchAvatar;
	public static Switch mSwitchLike;
	public static TextView mTextViewPath;
	public static Button mButtonChoosebg;
	public static EditText mEditTextCountSearch;

	Context mContext;

	public static void onTabTwo(){
		
		Log.v("menu2","onTabTwo");
		
		// Display current value
		// for SeekBar below
		String strDelayTime = Configuration
				.getPrivateString(Constants.slide_DelayTime);
		if (null != strDelayTime) {
			mSeekBarDelayTime.setProgress(Integer.parseInt(strDelayTime));
		} else
			mSeekBarDelayTime.setProgress(5);
		
		Boolean hasTransition = false;
		if (null != Configuration.getPrivateString(Constants.slide_Fade) && Configuration.getPrivateString(Constants.slide_Fade).equalsIgnoreCase("true")) {
			mCheckBoxFade.setChecked(true);
			hasTransition = true;
		} else {
			mCheckBoxFade.setChecked(false);
		}
		
		if (null != Configuration.getPrivateString(Constants.slide_HPush) && Configuration.getPrivateString(Constants.slide_HPush).equalsIgnoreCase("true")) {
			mCheckBoxHPush.setChecked(true);
			hasTransition = true;
		} else {
			mCheckBoxHPush.setChecked(false);
		}
		
		if (null != Configuration.getPrivateString(Constants.slide_Push) && Configuration.getPrivateString(Constants.slide_Push).equalsIgnoreCase("true")) {
			mCheckBoxPush.setChecked(true);
			hasTransition = true;
		} else {
			mCheckBoxPush.setChecked(false);
		}
		
		if (null != Configuration.getPrivateString(Constants.slide_Snap) && Configuration.getPrivateString(Constants.slide_Snap).equalsIgnoreCase("true")) {
			mCheckBoxSnap.setChecked(true);
			hasTransition = true;
		} else {
			mCheckBoxSnap.setChecked(false);
		}
		
		if (null != Configuration.getPrivateString(Constants.slide_Random) && Configuration.getPrivateString(Constants.slide_Random).equalsIgnoreCase("true")) {
			mCheckBoxRandom.setChecked(true);
			hasTransition = true;
		} else {
			mCheckBoxRandom.setChecked(false);
		}
		
		if(false == hasTransition) mCheckBoxFade.setChecked(true);
		
		Boolean hasBackground = false;
		
		if (null != Configuration.getPrivateString(Constants.slide_BackgroundWhite) && Configuration.getPrivateString(Constants.slide_BackgroundWhite).equalsIgnoreCase("true")) {
			mCheckBoxBackgroundWhite.setChecked(true);
			//mTextViewPath.setText(null);
			hasBackground = true;
		} else {
			mCheckBoxBackgroundWhite.setChecked(false);
		}
		
		if (null != Configuration.getPrivateString(Constants.slide_BackgroundChoose) && Configuration.getPrivateString(Constants.slide_BackgroundChoose).equalsIgnoreCase("true")) {
			mCheckBoxBackgroundChoose.setChecked(true);
			hasBackground = true;
			if (Configuration.getPrivateString(Constants.slide_Path) != null && Configuration.getPrivateString(Constants.slide_Path).length() > 0) {
				mTextViewPath.setText(Configuration.getPrivateString(Constants.slide_Path));
				// mRadioButtonCaptionOn.setSelected(true);
			} else {
				mTextViewPath.setText(null);
			}
			
		} else {
			mCheckBoxBackgroundChoose.setChecked(false);
		}
		
		if(false == hasBackground) mCheckBoxBackgroundWhite.setChecked(true);
		
		if (null != Configuration.getPrivateString(Constants.slide_AvatarOn) && Configuration.getPrivateString(Constants.slide_AvatarOn).equalsIgnoreCase("true")) {
			mSwitchAvatar.setChecked(true);
			// SlideShowActivity.mImageUser.setVisibility(View.INVISIBLE);
		} else {
			mSwitchAvatar.setChecked(false);
			// SlideShowActivity.mImageUser.setVisibility(View.VISIBLE);
		}
		
		if (null != Configuration.getPrivateString(Constants.slide_CaptionOn) && Configuration.getPrivateString(Constants.slide_CaptionOn).equalsIgnoreCase("true")) {
			mSwitchCaption.setChecked(true);
			// SlideShowActivity.mUserNameInst.setVisibility(View.INVISIBLE);
		} else {
			mSwitchCaption.setChecked(false);
			// SlideShowActivity.mUserNameInst.setVisibility(View.VISIBLE);
		}
		
		if (null != Configuration.getPrivateString(Constants.slide_LikeOn) && Configuration.getPrivateString(Constants.slide_LikeOn).equalsIgnoreCase("true")) {
			mSwitchLike.setChecked(true);
			// SlideShowActivity.mUserNameInst.setVisibility(View.INVISIBLE);
		} else {
			mSwitchLike.setChecked(false);
			// SlideShowActivity.mUserNameInst.setVisibility(View.VISIBLE);
		}
		
		if (null != Configuration.getPrivateString(Constants.slide_EditTextCountSearch) && Configuration.getPrivateString(Constants.slide_EditTextCountSearch) != null){
			mEditTextCountSearch.setText(Configuration.getPrivateString(Constants.slide_EditTextCountSearch));
		}
		else{
			mEditTextCountSearch.setText("10");
		}
		
		// ###### Set to show Background or not #######
		if(mCheckBoxBackgroundWhite.isChecked()==true){
			mButtonChoosebg.setVisibility(LinearLayout.GONE);
			mTextViewPath.setVisibility(LinearLayout.GONE);
		}
		else if(mCheckBoxBackgroundChoose.isChecked()==true){
			mButtonChoosebg.setVisibility(LinearLayout.VISIBLE);
			mTextViewPath.setVisibility(LinearLayout.VISIBLE);
		}
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu2);
		mContext = this;
		
		mSeekBarDelayTime = (SeekBar) findViewById(R.id.seekBarDelayTime);
		
		mCheckBoxFade = (RadioButton) findViewById(R.id.radioButtonFade);
		mCheckBoxHPush = (RadioButton) findViewById(R.id.radioButtonH_Push);
		mCheckBoxPush = (RadioButton) findViewById(R.id.radioButtonPush);
		mCheckBoxSnap = (RadioButton) findViewById(R.id.radioButtonSnap);
		mCheckBoxRandom = (RadioButton) findViewById(R.id.radioButtonRandom);
		mCheckBoxBackgroundWhite = (RadioButton) findViewById(R.id.checkBoxBackgroundWhite);
		mCheckBoxBackgroundChoose = (RadioButton) findViewById(R.id.checkBoxBackgroundChoose);
		mSwitchAvatar = (Switch) findViewById(R.id.switchShowAvatar);
		mSwitchCaption = (Switch) findViewById(R.id.switchShowCaption);
		mSwitchLike = (Switch) findViewById(R.id.switchShowLikeCounter);
		
		mButtonChoosebg = (Button) findViewById(R.id.chooseBackground);
		mTextViewPath = (TextView) findViewById(R.id.pathFile);
		
		mEditTextCountSearch = (EditText) findViewById(R.id.EditTextCountSearch);
		
		onTabTwo();

		mButtonChoosebg.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, 0);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			Uri targetUri = data.getData();
			mTextViewPath.setText(targetUri.toString());
		}

		// for SeekBar below
		String strDelayTime = Configuration
				.getPrivateString(Constants.slide_DelayTime);
		if (null != strDelayTime) {
			mSeekBarDelayTime.setProgress(Integer.parseInt(strDelayTime));
		}
	}
	
	public void onClickBackground(View v){
		mButtonChoosebg = (Button) findViewById(R.id.chooseBackground);
		mCheckBoxBackgroundWhite = (RadioButton) findViewById(R.id.checkBoxBackgroundWhite);
		mCheckBoxBackgroundChoose = (RadioButton) findViewById(R.id.checkBoxBackgroundChoose);
		mTextViewPath = (TextView) findViewById(R.id.pathFile);

		// ###### Set to show Background or not #######
		if(mCheckBoxBackgroundWhite.isChecked()==true){
			mButtonChoosebg.setVisibility(LinearLayout.GONE);
			mTextViewPath.setVisibility(LinearLayout.GONE);
		}
		else if(mCheckBoxBackgroundChoose.isChecked()==true){
			mButtonChoosebg.setVisibility(LinearLayout.VISIBLE);
			mTextViewPath.setVisibility(LinearLayout.VISIBLE);
		}
	}

	public void onStartClick(View v) {
		// Save value
		Configuration.setPrivateString(Constants.slide_DelayTime,
				String.valueOf(mSeekBarDelayTime.getProgress()));
		Configuration.setPrivateString(Constants.slide_Fade,
				String.valueOf(mCheckBoxFade.isChecked()));
		Configuration.setPrivateString(Constants.slide_HPush,
				String.valueOf(mCheckBoxHPush.isChecked()));
		Configuration.setPrivateString(Constants.slide_Push,
				String.valueOf(mCheckBoxPush.isChecked()));
		Configuration.setPrivateString(Constants.slide_Snap,
				String.valueOf(mCheckBoxSnap.isChecked()));
		Configuration.setPrivateString(Constants.slide_Random,
				String.valueOf(mCheckBoxRandom.isChecked()));
		Configuration.setPrivateString(Constants.slide_BackgroundWhite,
				String.valueOf(mCheckBoxBackgroundWhite.isChecked()));
		Configuration.setPrivateString(Constants.slide_BackgroundChoose,
				String.valueOf(mCheckBoxBackgroundChoose.isChecked()));
		Configuration.setPrivateString(Constants.slide_AvatarOn,
				String.valueOf(mSwitchAvatar.isChecked()));
		Configuration.setPrivateString(Constants.slide_CaptionOn,
				String.valueOf(mSwitchCaption.isChecked()));
		Configuration.setPrivateString(Constants.slide_LikeOn,
				String.valueOf(mSwitchLike.isChecked()));
		Configuration.setPrivateString(Constants.slide_Path,
				String.valueOf(mTextViewPath.getText()));
		Configuration.setPrivateString(Constants.slide_EditTextCountSearch, 
				String.valueOf(mEditTextCountSearch.getText()));
		
		//### from Tab1
//		if(null != menu1a3.mEditTextHashtag){
//		}
		Configuration.setPrivateString(Constants.account_Hashtag,
				menu1a3.mEditTextHashtag.getText().toString());
		Configuration.setPrivateString(Constants.account_Hashtag,
				menu1a3.mEditTextHashtag.getText().toString());
		Configuration.setPrivateString(Constants.account_Username,
				menu1a3.mEditTextUsername.getText().toString());
		Configuration.setPrivateString(Constants.account_LocationSpot,
				menu1a3.mEditTextLocationSpot.getText().toString());
		Configuration.setPrivateString(Constants.account_LocationIn,
				menu1a3.mEditTextLocationIn.getText().toString());

		//### set time
		Configuration.setPrivateString(Constants.account_timePickerBeginingHour,
				String.valueOf(menu1a3.mTimePickerBegining.getCurrentHour()));
		Configuration.setPrivateString(Constants.account_timePickerBeginingMinute,
				String.valueOf(menu1a3.mTimePickerBegining.getCurrentMinute()));
		
		//### set Date
		Configuration.setPrivateString(Constants.account_calendarViewBegining,
				String.valueOf(menu1a3.mCalendarViewBegining.getDate()));
		
		//### set Begining Time
		Configuration.setPrivateString(Constants.account_RadioBeginingAllTime, 
				String.valueOf(menu1a3.mRadioButtonBeginingAllTime.isChecked()));	
		Configuration.setPrivateString(Constants.account_RadioBeginingSetTime, 
				String.valueOf(menu1a3.mRadioButtonBeginingSetTime.isChecked()));
		

		/* #### this command is start to show the slide ###### */
		ActivitySelector.StartSlideShowActivity();
	}
}