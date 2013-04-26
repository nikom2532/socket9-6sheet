package com.popupsolution.cloudstax.android.task;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.popupsolution.cloudstax.android.ActivitySelector;
import com.popupsolution.cloudstax.android.helper;
import com.popupsolution.cloudstax.android.log.Log;
import com.popupsolution.cloudstax.android.log.LogFactory;

public class DownloadImageTask extends AsyncTask <Object, Void, Void>
{
	private static final Log log = LogFactory.getLog(DownloadImageTask.class);
	
	private ImageView mImageView = null;
	private ImageButton mImageButton = null;
	private ImageGalleryParam mImageGalleryParam = null; 
	public Bitmap mBitmap = null;
	private String mUrl;
	private Runnable mCallback = null;

	public static class ImageGalleryParam
    {
    	public Drawable drawable;	
    	public String photoURL;
    	public String photoThumbURL;
    }

	@Override
	public Void doInBackground(Object... params) 
	{
		try 
		{
			mUrl = (String) params[0];
			if(null == mUrl || mUrl.length() == 0) return null;
			
			if(null != params[1])
			{
				if(ImageView.class == params[1].getClass()) mImageView = (ImageView) params[1];
				else if(ImageButton.class == params[1].getClass()) mImageButton = (ImageButton) params[1];
				else if(ImageGalleryParam.class == params[1].getClass()) mImageGalleryParam = (ImageGalleryParam) params[1];
			}
	
	    	// Extra callback on complete
	    	if(params.length > 2) mCallback = (Runnable) params[2];	
	    	
        	// Load from cache
	    	mBitmap = helper.GetCacheImage(ActivitySelector.mRootActivity,mUrl);
	    	
	    	if(null == mBitmap) {
	        	// If no cache, get from internet
			    URL aURL = new URL((String) mUrl);
			    URLConnection conn = aURL.openConnection();
			    conn.connect();
			    InputStream is = conn.getInputStream();
		    	BufferedInputStream bis = new BufferedInputStream(is, 50);
		    	mBitmap = BitmapFactory.decodeStream(bis);
	
		    	/*Here you can add your image caching like storing fetched image to sd card*/
		    	helper.SetCacheImage(ActivitySelector.mRootActivity,mUrl,mBitmap);
		    	
		    	bis.close();
		    	is.close();		    	
	    	}	    	
	    	return null;
		} 
		catch (IOException e) 
		{
			log.error(e.toString());
			return null;
		} 
		catch(Exception e)
		{
			log.error(e.toString());
			return null;
		}
	}

	protected void onPostExecute(Void unused) 
	{
    	try
    	{
    		//Helper.PopError(m_Context,m_url);
    		if(null != mImageView) mImageView.setImageBitmap(mBitmap);
    		if(null != mImageButton) mImageButton.setImageBitmap(mBitmap);
    		if(null != mImageGalleryParam) mImageGalleryParam.drawable = new BitmapDrawable(mBitmap);
    		
    		// Callback on complete
    		if(null != mCallback) mCallback.run();
    	}
    	catch(Exception e)
    	{
    		log.error(e.toString());
    	} 	    	 
	}
}   