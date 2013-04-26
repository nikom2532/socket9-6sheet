package com.popupsolution.cloudstax.android.activity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
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

import com.popupsolution.cloudstax.android.AnimationFactory;
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
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;

public class SlideShowActivityTest extends Activity {
	private static final Log log = LogFactory.getLog(SlideShowActivityTest.class);
	
	
	Context mContext;
	Boolean mPlaySlide = true;
	Handler mHandler = new Handler();
	long mInterval = 5000;
	
	List<SearchResult> mSearchResultList;
	int mDataIndex = 0;
	
	ImageView mImageViewSlide1,mImageViewSlide2;
	TextView mTextViewDebug;
	String mTag="";
	Boolean mSlidePhotoLoaded=true;

	private ImageSwitcher mImageSwitcher;
	private final int ANIMATION_DURATION_MSEC = 2000;
	
	SearchPhotoService mSearchPhotoService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mContext = this;
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		
		setContentView(R.layout.activity_slide_show_test);
        mImageViewSlide1 = (ImageView)findViewById(R.id.imageViewSlide1);
        mImageViewSlide2 = (ImageView)findViewById(R.id.imageViewSlide2);
        mTextViewDebug = (TextView)findViewById(R.id.textViewDebug);
        
        mTextViewDebug.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        
        // Init image switcher
        mImageSwitcher = (ImageSwitcher)findViewById(R.id.imageSwitcher);
        //Animation fadeIn = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in);
        
        Animation fadeIn =  AnimationFactory.inFromRight();
        
        fadeIn.setDuration(ANIMATION_DURATION_MSEC);
        //Animation fadeOut = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_out);
        Animation fadeOut = AnimationFactory.outToLeft();
        
        
        fadeOut.setDuration(ANIMATION_DURATION_MSEC);
        mImageSwitcher.setInAnimation(fadeIn);
        mImageSwitcher.setOutAnimation(fadeOut);
        mImageSwitcher.setDisplayedChild(0);
        
        mSearchPhotoService = new SearchPhotoService(this); 
	}


    @Override  
    public void onStop() {    
    	super.onStop();
    	mInterval = 0;
    	mPlaySlide = false;
    	mSearchPhotoService.Stop();
    }

    public void onSearchClick(View v) {
    	mTag = ((EditText)findViewById(R.id.date)).getText().toString();
    	
    	mSearchPhotoService.BeginSearch(false,mTag, null, null, null, null,10, mOnSearchComplete);
        mTextViewDebug.setText("Loading...");
    }
    
    Runnable mOnSearchComplete = new Runnable() {
		@Override
		public void run() {
		    // Check result
			/*mSearchResultList = mSearchPhotoService.GetSearchResult();
		    if(null == mSearchResultList || mSearchResultList.isEmpty()) helper.PopError(mContext, "Not found");
		    else 
		    {
		    	mDataIndex = 0;
		    	mPlaySlide = true;
		    	PlayNextPhoto();
		    }*/
		}
	};
    
	private void PlayNextPhoto() {
		if(mDataIndex >= mSearchResultList.size())
		{
			mPlaySlide = false;
			mTextViewDebug.setText("End of data");
		}
		else
		{
			final SearchResult searchResult = mSearchResultList.get(mDataIndex);
			
			mSlidePhotoLoaded = false;
			DownloadImageTask downloadImageTask = new DownloadImageTask();
			Runnable onDownloadImageDone = new Runnable() {							
				@Override
				public void run() {
					mSlidePhotoLoaded = true;
					mHandler.post(new Runnable() {
						public void run() {
							// Display next photo and caption
							mTextViewDebug.setText(searchResult.caption);
							if(mImageSwitcher.getCurrentView() == mImageViewSlide1) mImageSwitcher.showNext();
							else mImageSwitcher.showPrevious();
						}
					});									
				}
			};
			
			if(mImageSwitcher.getCurrentView() == mImageViewSlide1)
			{
				downloadImageTask.execute(searchResult.photoURL,mImageViewSlide2,onDownloadImageDone);
			}
			else
			{
				downloadImageTask.execute(searchResult.photoURL,mImageViewSlide1,onDownloadImageDone);
			}
			
			mDataIndex++;
		}
		
		if(0 != mInterval && mPlaySlide)
		{
			mHandler.postDelayed(new Runnable() {
				public void run() {
					// Display next photo and caption
					PlayNextPhoto();
				}
			},mInterval);	
		}
	}
	
    public void onSettingClick(View v) {
    	startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
    }

}
