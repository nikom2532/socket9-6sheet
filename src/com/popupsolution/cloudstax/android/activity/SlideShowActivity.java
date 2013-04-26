package com.popupsolution.cloudstax.android.activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.popupsolution.cloudstax.android.Configuration;
import com.popupsolution.cloudstax.android.AnimationFactory;
import com.popupsolution.cloudstax.android.Constants;
import com.popupsolution.cloudstax.android.OnSwipeTouchListener;
import com.popupsolution.cloudstax.android.SearchPhotoService;
import com.popupsolution.cloudstax.android.helper;
import com.popupsolution.cloudstax.android.SearchPhotoService.SearchResult;
import com.popupsolution.cloudstax.android.log.Log;
import com.popupsolution.cloudstax.android.log.LogFactory;
import com.popupsolution.cloudstax.android.task.DownloadImageTask;
import com.popupsolution.cloudstax.android.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SlideShowActivity extends Activity {
	private static final Log log = LogFactory.getLog(SlideShowActivity.class);

	Context mContext;

	// Animation
	Animation mAniFadeIn;
	Animation mAniFadeOut;
	Animation mAniHPushIn;
	Animation mAniHPushOut;
	Animation mAniPushIn;
	Animation mAniPushOut;
	Animation mAniSnapIn;
	Animation mAniSnapOut;

	Boolean mPlaySlide = false;
	Handler mHandler = new Handler();
	long mInterval = 3000;

	SearchResult mSearchResult;

	ImageSwitcher mImageSwitcher;
	ImageView mImageViewSlide1, mImageViewSlide2, mImageBackground;
	static LinearLayout mImageUserLayout,mDebugLogLayout;
	static LinearLayout mLikeCounterLayuot;
	static TextView mUserNameInst, mTextViewCaption, mLikeCounter;
	static ImageView mImageUser;
	Boolean mSlidePhotoLoaded = true;
	
	Boolean mUseOldResult = true;

	private final int ANIMATION_DURATION_MSEC = 2000;
	
	private int mPendingRunnable = 0;

	SearchPhotoService mSearchPhotoService = null;
	Date mLastSearchTime = new Date(System.currentTimeMillis());

	// mImageBackground =

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//log.debug("onCreate");
		mContext = this;
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_slide_show);
		
		Configuration.gDebugLogTextView = (TextView) findViewById(R.id.debugLogTextView);
		mDebugLogLayout = (LinearLayout) findViewById(R.id.mDebugLogLayout);
		
		mImageViewSlide1 = (ImageView) findViewById(R.id.imageViewSlide1);
		mImageViewSlide2 = (ImageView) findViewById(R.id.imageViewSlide2);
		mTextViewCaption = (TextView) findViewById(R.id.textViewCaption);
		mImageUser = (ImageView) findViewById(R.id.mImageUser);
		mImageUserLayout = (LinearLayout) findViewById(R.id.UserImage);
		mLikeCounterLayuot = (LinearLayout) findViewById(R.id.mLikeCounterLayuot);
		mLikeCounter = (TextView) findViewById(R.id.mLikeCounter);
		mUserNameInst = (TextView) findViewById(R.id.mUserNameInst);

		mTextViewCaption.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
				| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		mTextViewCaption.setLineSpacing((float)14.5, (float)1.0);
		
		
		//mTextViewCaption.setTypeface(helper.getTypeface("DINEngschriftStd.otf"));
		mTextViewCaption.setTypeface(helper.getTypeface("sixsheet.ttf"));
		mLikeCounter.setTypeface(helper.getTypeface("DINEngschriftStd.otf"));
		mUserNameInst.setTypeface(helper.getTypeface("DINNeuzeitGroteskStd-BdCond.otf"));

		mImageViewSlide1.setOnTouchListener(new OnSwipeTouchListener(){
			 public void onSwipeRight() {
				 PlayNextPhoto(true);
			    }
			    public void onSwipeLeft() {
			    	PlayNextPhoto(false);
			    }
		});
		
		mImageViewSlide2.setOnTouchListener(new OnSwipeTouchListener(){
			 public void onSwipeRight() {
				 PlayNextPhoto(true);
			    }
			    public void onSwipeLeft() {
			    	PlayNextPhoto(false);
			    }
		});		
		// Init Background
		mImageBackground = (ImageView) findViewById(R.id.ImageBackground);
		Uri u = Uri.parse(Configuration.getPrivateString(Constants.slide_Path));
		mImageBackground.setImageURI(u);
		// mImageBackground.setImageResource(R.drawable.bg_06_06); // Testing bg
		// layout

		// Init animation
		mAniFadeIn = AnimationUtils.loadAnimation(mContext,
				android.R.anim.fade_in);
		mAniFadeOut = AnimationUtils.loadAnimation(mContext,
				android.R.anim.fade_out);
		mAniHPushIn = AnimationFactory.inFromLeft();
		mAniHPushOut = AnimationFactory.outToRight();
		mAniPushIn = AnimationFactory.inFromTop();
		mAniPushOut = AnimationFactory.outToBottom();

		mAniSnapIn = AnimationUtils.loadAnimation(mContext, R.anim.snap_in);
		mAniSnapOut = AnimationUtils.loadAnimation(mContext, R.anim.snap_out);

		mAniFadeIn.setDuration(ANIMATION_DURATION_MSEC);
		mAniFadeOut.setDuration(ANIMATION_DURATION_MSEC);
		mAniHPushIn.setDuration(ANIMATION_DURATION_MSEC);
		mAniHPushOut.setDuration(ANIMATION_DURATION_MSEC);
		mAniPushIn.setDuration(ANIMATION_DURATION_MSEC);
		mAniPushOut.setDuration(ANIMATION_DURATION_MSEC);

		// Init image switcher
		mImageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
		mImageSwitcher.setDisplayedChild(0);

		if (Configuration.getPrivateString(Constants.slide_Fade).equalsIgnoreCase("true"))
			SetAnimation(0);
		else if (Configuration.getPrivateString(Constants.slide_HPush).equalsIgnoreCase("true"))
			SetAnimation(1);
		else if (Configuration.getPrivateString(Constants.slide_Push).equalsIgnoreCase("true"))
			SetAnimation(2);
		else if (Configuration.getPrivateString(Constants.slide_Snap).equalsIgnoreCase("true"))
			SetAnimation(3);
		else
			SetAnimation(0);

		if (Configuration.getPrivateString(Constants.slide_BackgroundWhite).equalsIgnoreCase("true")) {
			if (Configuration.getPrivateString(Constants.slide_BackgroundWhite) == null) {
				if(null != menu2.mCheckBoxBackgroundWhite) menu2.mCheckBoxBackgroundWhite.setChecked(true);
			}
			mImageBackground.setVisibility(View.GONE);
		}

		else {
			mImageBackground.setVisibility(View.VISIBLE);

		}

		if (Configuration.getPrivateString(Constants.slide_AvatarOn).equalsIgnoreCase("true")) {
			mImageUserLayout.setVisibility(View.VISIBLE);
		} else {
			mImageUserLayout.setVisibility(View.INVISIBLE);
		}

		if (Configuration.getPrivateString(Constants.slide_CaptionOn).equalsIgnoreCase("true")) {
			mTextViewCaption.setVisibility(View.VISIBLE);
		} else {
			mTextViewCaption.setVisibility(View.INVISIBLE);
		}

	}

	@Override
	protected void onStart() {
		super.onStart(); // Always call the superclass method first

		String strDelayTime = Configuration.getPrivateString(Constants.slide_DelayTime);
		//log.debug(strDelayTime);
		if (null != strDelayTime) {
			mInterval = 1000 * Integer.parseInt(strDelayTime);
			if(0 == mInterval) mInterval = 100; 
		}
		
		// Hide like count when loading, show again when load finish 
		mLikeCounterLayuot.setVisibility(View.INVISIBLE);

		// Call search service
		//mTextViewCaption.setText("Loading...");
		//log.debug("onStart");
		mSearchPhotoService = new SearchPhotoService(this);
		BeginSearch();
	}

	@Override
	public void onStop() {
		super.onStop();
		mInterval = 0;
		mPlaySlide = false;
		if(null != mSearchPhotoService) mSearchPhotoService.Stop();
		//log.debug("onStop");
	}

	@Override
	public void onResume(){
        super.onResume();
        //log.debug("onResume");
        mUseOldResult = true;
    }	

	@Override
	public void onPause(){
        super.onPause();
        //log.debug("onPause");
    }	

	// @Override
	// public void onRestart() {
	// super.onRestart();
	// mInterval = 0;
	// mPlaySlide = false;
	// mSearchPhotoService.Stop();
	// onStop();
	//
	//
	// }

	private void BeginSearch() {
		mLastSearchTime = new Date(System.currentTimeMillis());
		
		int ItemLimit = 10;
		String EditTextCountSearch = Configuration.getPrivateString(Constants.slide_EditTextCountSearch);
		if(EditTextCountSearch!= null){
			ItemLimit = Integer.parseInt(EditTextCountSearch);
		}
		
		String calendarViewBegining = Configuration.getPrivateString(Constants.account_calendarViewBegining);
		String timePickerBeginingHour = Configuration.getPrivateString(Constants.account_timePickerBeginingHour);
		String timePickerBeginingMinute = Configuration.getPrivateString(Constants.account_timePickerBeginingMinute);
		Date date = null;
		
		// Set date if radio is set
		if (Configuration.getPrivateString(Constants.account_RadioBeginingSetTime).equalsIgnoreCase("true")) {
			if(null != calendarViewBegining && null != timePickerBeginingHour && null != timePickerBeginingMinute) {
				long time = Long.parseLong(calendarViewBegining);

				date = new java.util.Date(time);
				date.setHours(Integer.parseInt(timePickerBeginingHour));
				date.setMinutes(Integer.parseInt(timePickerBeginingMinute));
				date.setSeconds(0);
			}
		} 
		
		if(Configuration.gNewSearch == true)
		{
			mSearchPhotoService.CleanCache();			
			Configuration.gNewSearch = false;
			mUseOldResult = false;
		}
		
		mSearchPhotoService.BeginSearch(
				mUseOldResult,
				Configuration.getPrivateString(Constants.account_Hashtag).replace(" ", ""),
				Configuration.getPrivateString(Constants.account_Username),
				Configuration.getPrivateString(Constants.account_LocationSpot),
				Configuration.getPrivateString(Constants.account_LocationIn),
				date, 
				ItemLimit,
				mOnSearchComplete);
	}

	Runnable mOnSearchComplete = new Runnable() {
		@Override
		public void run() {
			// Check result
			if (!mSearchPhotoService.HasResult()) {
				/*helper.PopError(mContext, mSearchPhotoService.mError,
						new Runnable() {
							@Override
							public void run() {
								((Activity) mContext).finish();
							}
						});*/
				
				if(mSearchPhotoService.mError.equalsIgnoreCase("network error")) {
					mImageViewSlide1.setImageResource(R.drawable.no_connect);
				}
				else {
					mImageViewSlide1.setImageResource(R.drawable.notmatch);
				}
			} else {
				// Do not start play loop if already start
				if (false == mPlaySlide) {
					mPlaySlide = true;
					
					// Show like count here
					if (Configuration.getPrivateString(Constants.slide_LikeOn).equalsIgnoreCase("true")) {
						mLikeCounterLayuot.setVisibility(View.VISIBLE);
					} 
					
					PlayNextPhoto(false);
				}
			}
		}
	};

	int iAni = 0;
	Runnable mOnLoadPhotoComplete = new Runnable() {
		@Override
		public void run() {
			mSlidePhotoLoaded = true;

			// Display photo and caption
			mHandler.post(new Runnable() {
				public void run() {
					mLikeCounter.setText("" + mSearchResult.likeCount);
					mUserNameInst.setText(mSearchResult.username);
					//mTextViewCaption.setText(mSearchResult.createdTime.toLocaleString() +" :" +mSearchResult.caption);
					mTextViewCaption.setText(mSearchResult.caption.toUpperCase());
					
					if (Configuration.getPrivateString(Constants.slide_Random).equalsIgnoreCase("true")) {
						// SetAnimation(iAni++%4);
						SetAnimation((int) System.currentTimeMillis() % 4);
					}

					// SetAnimation(iAni++%4);
					// mImageSwitcher.showNext();
					// mImageSwitcher.showPrevious();
					if (mImageSwitcher.getCurrentView() == mImageViewSlide1)
						mImageSwitcher.showNext();
					else
						mImageSwitcher.showPrevious();
				}
			});

			// Display next photo and caption
			if (0 != mInterval && mPlaySlide) {
				mPendingRunnable++;
				mHandler.postDelayed(new Runnable() {
					public void run() {
						mPendingRunnable--;
						if(0 == mPendingRunnable) PlayNextPhoto(false);
					}
				}, mInterval);
			}
		}
	};

	private void PlayNextPhoto(Boolean bReverse) {
		// This method may be called after activity finished
		if (true != mPlaySlide)
			return;

		if(bReverse) mSearchResult = mSearchPhotoService.GetPrevResult();
		else mSearchResult = mSearchPhotoService.GetNextResult();
		mSlidePhotoLoaded = false;

		DownloadImageTask downloadImageTask = new DownloadImageTask();

		if (mImageSwitcher.getCurrentView() == mImageViewSlide1) {
			downloadImageTask.execute(mSearchResult.photoURL, mImageViewSlide2,
					mOnLoadPhotoComplete);
		} else {
			downloadImageTask.execute(mSearchResult.photoURL, mImageViewSlide1,
					mOnLoadPhotoComplete);
		}

		new DownloadImageTask().execute(mSearchResult.avatarURL, mImageUser);

		// Restart search if reach specific time
		Date currentTime = new Date(System.currentTimeMillis());
		Date nextSearchTime = new Date(mLastSearchTime.getTime()+(60*1000));	// 60 Second after last search 
		
		if(currentTime.after(nextSearchTime)) {
			mUseOldResult = false;
			BeginSearch();
		}
	}

	private void SetAnimation(int iAnimation) {
		if(iAnimation < 0) iAnimation = iAnimation*-1;
		switch (iAnimation) {
		case 0:
			mImageSwitcher.setOutAnimation(mAniFadeOut);
			mImageSwitcher.setInAnimation(mAniFadeIn);
			break;
		case 1:
			mImageSwitcher.setInAnimation(mAniHPushIn);
			mImageSwitcher.setOutAnimation(mAniHPushOut);
			break;
		case 2:
			mImageSwitcher.setInAnimation(mAniPushIn);
			mImageSwitcher.setOutAnimation(mAniPushOut);
			break;
		case 3:
			mImageSwitcher.setInAnimation(mAniSnapIn);
			mImageSwitcher.setOutAnimation(mAniSnapOut);
			break;
		}
	}

	public void onSettingClick(View v) {
		startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
	}

	public void onPrevClick(View v) {
		PlayNextPhoto(true);
	}

	public void onNextClick(View v) {
		PlayNextPhoto(false);
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {    
    	// Load menu for display
    	MenuInflater inflater = getMenuInflater();    
    	inflater.inflate(R.menu.activity_main, menu);    
    	return true;
    }
 

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	// Handle item selection    
    	switch (item.getItemId()) {    
    	case R.id.menu_showlog:
    		mDebugLogLayout.setVisibility(LinearLayout.VISIBLE);
    		break;       
    	case R.id.menu_hidelog:
    		mDebugLogLayout.setVisibility(LinearLayout.GONE);
    		break;       
    	case R.id.menu_clearlog:
    		mSearchPhotoService.clearLog();
    		break;       
    	default:        
    		return super.onOptionsItemSelected(item);    
    	}
		return true;
    }    	
}
