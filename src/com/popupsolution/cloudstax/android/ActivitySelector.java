package com.popupsolution.cloudstax.android;

import com.popupsolution.cloudstax.android.activity.AndroidTabLayoutActivity;
import com.popupsolution.cloudstax.android.activity.SlideShowActivity;
import com.popupsolution.cloudstax.android.activity.SlideShowActivityTest;
import com.popupsolution.cloudstax.android.activity.SlideShowActivityTest2;
import com.popupsolution.cloudstax.android.activity.menu1a3;
import com.popupsolution.cloudstax.android.activity.menu2;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;

public class ActivitySelector {
	static public Activity mRootActivity;
	static public Activity mMenuCallerActivity;
	static public int mCurrentMenuID = -1;
	static public Handler mHandler;

	static public void Init(Activity rootActivity) {
		mCurrentMenuID = -1;
		mRootActivity = rootActivity;
		mHandler = new Handler();
		
		//mRootActivity.startActivityForResult(new Intent(mRootActivity, SlideShowActivity.class),999);
		//mRootActivity.startActivityForResult(new Intent(mRootActivity, SlideShowActivityTest.class),999);
		//mRootActivity.startActivityForResult(new Intent(mRootActivity, SlideShowActivityTest2.class),999);
		mRootActivity.startActivityForResult(new Intent(mRootActivity, AndroidTabLayoutActivity.class),999);
	}
	
	static public void Clean()
	{
		mCurrentMenuID = -1;
	}
	
	static public void StartSlideShowActivity()
	{
		Configuration.gNewSearch = true;
		//mRootActivity.startActivityForResult(new Intent(mRootActivity, SlideShowActivity.class),999);
		mRootActivity.startActivity(new Intent(mRootActivity, SlideShowActivity.class));
	}
}
