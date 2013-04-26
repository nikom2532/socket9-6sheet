package com.popupsolution.cloudstax.android;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
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

import android.content.Context;
import android.os.Handler;
import android.text.format.Time;

import com.popupsolution.cloudstax.android.log.Log;
import com.popupsolution.cloudstax.android.log.LogFactory;

public class SearchPhotoService {
	private static final Log log = LogFactory.getLog(SearchPhotoService.class);

	public class SearchResult {
		public long id;
		public Date createdTime;
		public int likeCount;
		public String caption;
		public String photoURL;
		public String avatarURL;
		public String username;
	}
	
	Context mContext;
	Handler mHandler = new Handler();
	static String mDebugLog = "";
	
	
	static boolean mRunning = true; // To control main thread loop
	static boolean mSearching = false; // To control search loop
	static boolean mWorkDone = true;
	static long mLimitItem = 10;//300;
	static long mLoopCount = 0;

	static String[] mSearchHashtagArray;
	static String[] m_response_next_urlArray;
	static int mSearchHashtagIndex;
	static Boolean mIsSearchByTag = false;
	
	static String mSearchUsername;
	static String mSearchSpot;
	static String mSearchArea;
	static Date mSearchDate;
	static Runnable mOnSearchComplete;
	
	static Boolean mUseTagFilter = false;
	static Boolean mUseUsernameFilter = false;
	
	static Boolean mIsError = false;
	static public String mError = "";
	static String mResponse = "";
	static JSONArray mJsonDataArray;
	static ArrayList<SearchResult> mSearchResultList = new ArrayList<SearchPhotoService.SearchResult>();
	static ArrayList<Long> mSearchResultIDList = new ArrayList<Long>();

	static String m_response_next_url = "";
	
	// For image sequence control
	static Long mCurrentImageID = (long)0;
	static Long mCurrentMaxImageID = (long)0;
	static Long mJumpImageID = (long)0;
	static Boolean mIsJump = false;
	static Long mJumpReturnID = (long)0;
	
	static Boolean mFoundDuplicateResult = false;
	static Boolean mFoundOlderResult = false;
	
	public SearchPhotoService(Context context) {
		mContext = context;
		mRunning = true;
		mWorkingThread.start();
	}

	public void Stop() {
		mSearching = false;
		mRunning = false;
	}
	
	Thread mWorkingThread = new Thread() {
		public void run() {
			while(mRunning) {
				while(mSearching) {
					mWorkDone = false;
					DebugLog("Search start");
								
					mFoundDuplicateResult = false;
					mLoopCount++;
					if(null != mSearchSpot && mSearchSpot.length() > 0 && null != mSearchArea && mSearchArea.length() > 0) {
						DebugLog("SearchByLocation");
						mUseTagFilter = true;
						mUseUsernameFilter = true;
						mIsSearchByTag = false;
						SearchByLocation();
					}
					else if(null != mSearchUsername && mSearchUsername.length() > 0) {
						DebugLog("SearchByUsername");
						mUseTagFilter = true;
						mUseUsernameFilter = false;
						mIsSearchByTag = false;
						SearchByUsername();
					}
					else if(null != mSearchHashtagArray && mSearchHashtagArray.length > 0) {
						DebugLog("SearchByTag");
						mUseTagFilter = false;
						mUseUsernameFilter = false;
						mIsSearchByTag = true;
						for(int iHashTag = 0;iHashTag < mSearchHashtagArray.length;iHashTag++) {
							mSearchHashtagIndex = iHashTag;
							SearchByTag();	
						}
					} 

					// Sort image by id
					Collections.sort(mSearchResultIDList);
					
					/*Collections.sort(mSearchResultIDList, new Comparator<String>() {
					    public int compare(String a, String b) {
					        return Long.signum(fixString(a) - fixString(b));
					    }
					    private long fixString(String in) {
					        return Long.parseLong(in.substring(0, in.indexOf('_')));
					    }
					});*/
				
					DebugLog("Search result count : " + mSearchResultIDList.size());
					//for (Long val : mSearchResultIDList) PostToast(val.toString());
										
					
					if(
							mIsError
							||mSearchResultIDList.size() > mLimitItem // Break loop if got enough result
							|| true == mFoundDuplicateResult // Break if result is repeated
							|| true == mFoundOlderResult // Break if result is too old
							|| mLoopCount > 5 // Break if operation take long time
						) {
						mSearching = false;
						if(mSearchResultIDList.size() > 0) {
							Long MaxImageID = mSearchResultIDList.get(mSearchResultIDList.size()-1);
							if(0 == mCurrentMaxImageID) mCurrentMaxImageID = MaxImageID;
							
							// If found new item, should jump to the new item 
							if(MaxImageID > mCurrentMaxImageID && 0 != mCurrentImageID) {
								int currentMaxIndex = mSearchResultIDList.indexOf(mCurrentMaxImageID);
								mJumpImageID = mSearchResultIDList.get(currentMaxIndex+1);
								mCurrentMaxImageID = MaxImageID;
								DebugLog("Found new item : " + mJumpImageID);
							}
							else mJumpImageID = (long)0;
						}

						// Call on complete function
						if(mRunning) {
							DebugLog("Search finish");
							mHandler.post(mOnSearchComplete);
						}
					}
				}				
				mWorkDone = true;
			}
			DebugLog("WorkingThread Stopped");
		}
	};
	
	public void CleanCache() {
		// Clear old result
		mSearchResultList = new ArrayList<SearchPhotoService.SearchResult>();
		mSearchResultIDList = new ArrayList<Long>();

		DebugLog("Clear cache");
		
		mCurrentImageID = (long)0;
		mCurrentMaxImageID = (long)0;
		mJumpImageID = (long)0;
		mIsJump = false;
		mJumpReturnID = (long)0;		
	}
	
	public void BeginSearch(Boolean bUseOldResult,String hashtag,String username,String spot,String area,Date date,int ItemLimit,Runnable onSearchComplete) {
		if(!mWorkDone) {
			//helper.PopError(mContext, "Prev search not finished");
			DebugLog("Prev search not finished");
			return;
		}
		
		if(mIsJump) {
			// Should not search while jumping
			return;
		}
		
		// Reset break condition flag
		mFoundDuplicateResult = false;
		mIsError = false;
		mFoundOlderResult = false;
		
		// Init search parameter
		m_response_next_url = "";
		mLoopCount = 0;
		
		if(null == hashtag || hashtag.length() == 0){
			mSearchHashtagArray = null;
			m_response_next_urlArray = null;
		}
		else {
			mSearchHashtagArray = hashtag.split(",");
			m_response_next_urlArray = new String[mSearchHashtagArray.length];
		}
		
		mSearchHashtagIndex = 0;
		
		mSearchUsername = username;
		mSearchSpot = spot;
		mSearchArea = area;
		mSearchDate = date;
		mLimitItem = ItemLimit;
		mOnSearchComplete = onSearchComplete;

		if(bUseOldResult) {
			DebugLog("Resume activity");
			mHandler.post(mOnSearchComplete);
		}
		else {
			DebugLog("Refresh result");
			m_response_next_url = "";
			mFoundDuplicateResult = false;
			mFoundOlderResult = false;			
			mSearching = true;
		}

		//if(null != mSearchDate) DebugLog("mSearchDate : " + mSearchDate.toLocaleString());
	}
	
	public void EndSearch() {
		mSearching = false;
	}
	
	public Boolean HasResult(){
		if (null == mSearchResultList || mSearchResultList.isEmpty()) return false;
		else return true;
	}
	
	public SearchResult GetPrevResult() {
		Boolean bMoveToPrevID = true;
		if(0 == mCurrentImageID) {
			mCurrentImageID = mSearchResultIDList.get(0);
			bMoveToPrevID = false;
		}
		
		// Lookup index in array
		int currentIndex = mSearchResultIDList.indexOf(mCurrentImageID);
		
		// Move to Prev ID
		if(bMoveToPrevID) currentIndex--;
		if(currentIndex < 0) {
			currentIndex = mSearchResultIDList.size()-1;
		}
		
		mCurrentImageID = mSearchResultIDList.get(currentIndex);

		// Search for this ID in result list
		SearchResult searchResult = null;
		for (SearchResult tmpResult : mSearchResultList) {
			if(tmpResult.id == mCurrentImageID) {
				searchResult = tmpResult;
				break;
			}
		}

		DebugLog("Prev result : " + mCurrentImageID + "," + searchResult.photoURL);
		
		return searchResult;		
	}
	
	public SearchResult GetNextResult() {
		Boolean bMoveToNextID = true;
		if(0 == mCurrentImageID) {
			mCurrentImageID = mSearchResultIDList.get(0);
			bMoveToNextID = false;
		}
		else{
			if(0 != mJumpImageID) {
				// Jump to this id
				DebugLog("Jump to : " + mJumpImageID);
				DebugLog("Jump from : " + mCurrentImageID);

				mJumpReturnID = mCurrentImageID;
				mIsJump = true;
				mCurrentImageID = mJumpImageID;
				mJumpImageID = (long)0;
				bMoveToNextID = false;
			}
		}
		
		// Lookup index in array
		int currentIndex = mSearchResultIDList.indexOf(mCurrentImageID);
		
		// Move to next ID
		if(bMoveToNextID) currentIndex++;
		if(currentIndex >= mSearchResultIDList.size()) {
			if(mIsJump) {
				// Jump back
				DebugLog("Jump back : " + mJumpReturnID);
				mJumpImageID = (long)0;
				mIsJump = false;
				currentIndex = mSearchResultIDList.indexOf(mJumpReturnID);
			}
			else currentIndex = 0;
		}
		
		mCurrentImageID = mSearchResultIDList.get(currentIndex);

		
		// Search for this ID in result list
		SearchResult searchResult = null;
		for (SearchResult tmpResult : mSearchResultList) {
			if(tmpResult.id == mCurrentImageID) {
				searchResult = tmpResult;
				break;
			}
		}

		DebugLog("Next result : " + mCurrentImageID + "," + searchResult.photoURL);
		
		return searchResult;
	}
	
	private void ParseResult() {
		try
		{
			JSONObject jsonResult = new JSONObject(mResponse);
			mJsonDataArray = jsonResult.getJSONArray("data");

			for (int i=0;i<mJsonDataArray.length();i++) {
				JSONObject jsonDataItem = mJsonDataArray.getJSONObject(i);
				
				JSONObject jsonCaption = null;
				JSONObject jsonLikes = jsonDataItem.getJSONObject("likes");
				JSONObject jsonImages = jsonDataItem.getJSONObject("images");
				JSONObject jsonPhoto = jsonImages.getJSONObject("standard_resolution");
				JSONObject jsonUser = jsonDataItem.getJSONObject("user");

				SearchResult searchResult = new SearchResult();
				String id = jsonDataItem.getString("id");
				searchResult.id = Long.parseLong(id.substring(0, id.indexOf('_')));
				
				// Return if item already in list
				if(-1 != mSearchResultIDList.indexOf(searchResult.id)) {
					mFoundDuplicateResult = true;
					DebugLog("Found Duplicate Result");
					
					/*searchResult.id = searchResult.id*2;
					
					if(-1 != mSearchResultIDList.indexOf(searchResult.id)) return;
					
					mSearchResultIDList.add(searchResult.id);
					
					long created_time = Long.parseLong(jsonDataItem.getString("created_time"));
					searchResult.createdTime = new java.util.Date((long)created_time*1000);
					
					searchResult.likeCount = jsonLikes.getInt("count");
					if(!jsonDataItem.isNull("caption")) 
					{
						jsonCaption = jsonDataItem.getJSONObject("caption");
						searchResult.caption = jsonCaption.getString("text");
					}
					else searchResult.caption = "null";
					searchResult.photoURL = jsonPhoto.getString("url");
					
					searchResult.username = jsonUser.getString("username");
					searchResult.avatarURL = jsonUser.getString("profile_picture");
					
										
					mSearchResultList.add(searchResult);*/
					
					return;
				}
				
				long created_time = Long.parseLong(jsonDataItem.getString("created_time"));
				searchResult.createdTime = new java.util.Date((long)created_time*1000);

				searchResult.likeCount = jsonLikes.getInt("count");
				if(!jsonDataItem.isNull("caption")) 
				{
					jsonCaption = jsonDataItem.getJSONObject("caption");
					searchResult.caption = jsonCaption.getString("text");
				}
				//else searchResult.caption = "null";
				else searchResult.caption = "";
				
				searchResult.photoURL = jsonPhoto.getString("url");
				
				searchResult.username = jsonUser.getString("username");
				searchResult.avatarURL = jsonUser.getString("profile_picture");
												
				// Return if result is too old
				if(null != mSearchDate) {
					if(searchResult.createdTime.before(mSearchDate)) {
						mFoundOlderResult = true;
						DebugLog("Found older result : " + searchResult.createdTime.toLocaleString());
						return;
					}
				}

				// Skip item if not in tag list
				if(mUseTagFilter && null != mSearchHashtagArray && mSearchHashtagArray.length > 0) {
					Boolean bTagFound = false;
					JSONArray jsonTags = jsonDataItem.getJSONArray("tags");
					for(int iTag = 0;iTag < jsonTags.length();iTag++) {
						//DebugLog("Compare tag : " + jsonTags.getString(iTag));
						for(int iHash=0;iHash < mSearchHashtagArray.length;iHash++) {
							String SearchHashtag = mSearchHashtagArray[iHash]; 
							if(SearchHashtag.contains(jsonTags.getString(iTag))) {
								bTagFound = true;
								break;
							}
						}
					}
					
					if(false == bTagFound) {
						//DebugLog("Not in tag list");
						continue;
					}
				}
				
				// Skip item if user not match
				if(mUseUsernameFilter && null != mSearchUsername && mSearchUsername.length() > 0) {
					if(true != searchResult.username.equalsIgnoreCase(mSearchUsername)) {
						//DebugLog("User not match");
						continue;
					}
				}				
				
				// Add item to list
				mSearchResultIDList.add(searchResult.id);
				mSearchResultList.add(searchResult);
			}
			
			// Parse next url, if null it will throw error and end search 
			JSONObject jsonPagination = jsonResult.getJSONObject("pagination");
			
			if(mIsSearchByTag == false){
				m_response_next_url = jsonPagination.getString("next_url");
			}
			else {
				m_response_next_urlArray[mSearchHashtagIndex] = jsonPagination.getString("next_url");
			}
			//PostToast(m_response_next_url);
		} 
		catch (final JSONException e) {
			mIsError = true;
			e.printStackTrace();
			DebugLog(e.getMessage());
			//mError = e.getMessage();
			mError = "No image found";
		} 		
		finally {
		}		
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Main instagram API
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private void SearchByTag() {
    	if("" != m_response_next_urlArray[mSearchHashtagIndex] && null != m_response_next_urlArray[mSearchHashtagIndex]) InstagramAPI(m_response_next_urlArray[mSearchHashtagIndex]);
    	else {
        	StringBuilder urlString = new StringBuilder(); 
    		String tag;
    		try {
    			tag = URLEncoder.encode(mSearchHashtagArray[mSearchHashtagIndex], "UTF-8");
    		} catch (UnsupportedEncodingException e) {
    			e.printStackTrace();
    			tag = mSearchHashtagArray[mSearchHashtagIndex];
    		}

        	urlString.append("https://api.instagram.com/v1/tags/"+tag+"/media/recent?");
        	//urlString.append("client_id="+Constants.InstagramClientID);
        	urlString.append("access_token="+Constants.InstagramToken);
    		
    		InstagramAPI(urlString.toString());
    	}
    	
    	// Save result
		if(mResponse.length() == 0) return;
		ParseResult();
	}
	
	private void SearchByUsername() {
		if("" != m_response_next_url) InstagramAPI(m_response_next_url);
    	else {
        	StringBuilder urlString = new StringBuilder(); 
    		String userid = GetUserID(mSearchUsername);
    		
    		if(null == userid) {
    			mIsError = true;
    			mError = "User not found";
    			return;
    		}
        	urlString.append("https://api.instagram.com/v1/users/"+userid+"/media/recent?");
        	//urlString.append("client_id="+Constants.InstagramClientID);
        	urlString.append("access_token="+Constants.InstagramToken);
        	
    		InstagramAPI(urlString.toString());
    	}
    	
    	// Save result
		if(mResponse.length() == 0) return;
		ParseResult();
	}
	
	private void SearchByLocation() {

    	if("" != m_response_next_url) InstagramAPI(m_response_next_url);
    	else {
    		StringBuilder urlString = new StringBuilder(); 
    		String locationID = GetLocationID(mSearchSpot,mSearchArea);
    		String locationIDV1 = null;
    		
    		if(null != locationID) locationIDV1 = GetLocationIDV1(locationID); 
    		//DebugLog(locationIDV1);
    		if(null == locationIDV1) {
    			mIsError = true;
    			mError = "Location not found";
    			return;
    		}
    		
        	urlString.append("https://api.instagram.com/v1/locations/"+locationIDV1+"/media/recent?");
        	urlString.append("access_token="+Constants.InstagramToken);
        	    		
    		InstagramAPI(urlString.toString());
    	}
    	
    	// Save result
		if(mResponse.length() == 0) return;
		ParseResult();
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Sub instagram API
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private String GetUserID(String username) {

    	StringBuilder urlString = new StringBuilder(); 
		String user;
		String userid = null;
		
		try {
			user = URLEncoder.encode(username, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			user = username;
		}

    	urlString.append("https://api.instagram.com/v1/users/search?q="+user);
    	urlString.append("&client_id="+Constants.InstagramClientID);
    	InstagramAPI(urlString.toString());
    	
    	// Save result
		if(mResponse.length() == 0) return userid;
		try
		{
			JSONObject jsonResult = new JSONObject(mResponse);
			mJsonDataArray = jsonResult.getJSONArray("data");

			if(mJsonDataArray.length() > 0) {
				JSONObject jsonDataItem = mJsonDataArray.getJSONObject(0);
				userid = jsonDataItem.getString("id");
			}
		} 
		catch (final JSONException e) {
			e.printStackTrace();
			mHandler.post(new Runnable() {
				public void run() {
					helper.PopError(mContext, e.getMessage());
				}
			});					
		} 
		finally {
		}
		return userid;
	}
	
	private String GetLocationID(String spot, String area) {
    	StringBuilder urlString = new StringBuilder(); 
		String near,query;
		String locationID = null;
		
		try {
			near = URLEncoder.encode(area, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			near = area;
		}
		try {
			query = URLEncoder.encode(spot, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			query = spot;
		}
		
    	urlString.append("https://api.foursquare.com/v2/venues/search?");
    	urlString.append("near="+near+"&query="+query);
    	urlString.append("&oauth_token="+Constants.FourSquareToken);
    	urlString.append("&v=20130217");
    	InstagramAPI(urlString.toString());
    	
    	// Save result
		if(mResponse.length() == 0) return locationID;
		try
		{
			JSONObject jsonResult = new JSONObject(mResponse);
			JSONObject jsonResponse = jsonResult.getJSONObject("response");
			
			mJsonDataArray = jsonResponse.getJSONArray("venues");

			if(mJsonDataArray.length() > 0) {
				JSONObject jsonDataItem = mJsonDataArray.getJSONObject(0);
				locationID = jsonDataItem.getString("id");
			}
		} 
		catch (final JSONException e) {
			e.printStackTrace();
			mHandler.post(new Runnable() {
				public void run() {
					helper.PopError(mContext, e.getMessage());
				}
			});					
		} 
		finally {
		}
		return locationID;
	}
	
	private String GetLocationIDV1(String locationID) {
		StringBuilder urlString = new StringBuilder(); 
		String locationIDV1 = null;
		
    	urlString.append("https://api.instagram.com/v1/locations/search?");
    	urlString.append("foursquare_v2_id="+locationID);
    	urlString.append("&client_id="+Constants.InstagramClientID);
    	InstagramAPI(urlString.toString());
    	

    	// Save result
		if(mResponse.length() == 0) return locationIDV1;
		try
		{
			JSONObject jsonResult = new JSONObject(mResponse);
			mJsonDataArray = jsonResult.getJSONArray("data");

			if(mJsonDataArray.length() > 0) {
				JSONObject jsonDataItem = mJsonDataArray.getJSONObject(0);
				locationIDV1 = jsonDataItem.getString("id");
			}
		} 
		catch (final JSONException e) {
			e.printStackTrace();
			mHandler.post(new Runnable() {
				public void run() {
					helper.PopError(mContext, e.getMessage());
				}
			});					
		} 
		finally {
		}
		return locationIDV1;
	}
	
	private void InstagramAPI(String request) {
    	mError = "Not found";
    	mResponse = "";
		try
		{
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();

            DebugLog("InstagramAPI : " + "Request: " + request );
            
            HttpGet httpGet = new HttpGet(request);
            HttpResponse response = httpClient.execute(httpGet, localContext);
            HttpEntity myEntity = response.getEntity();
            
            mResponse = EntityUtils.toString(myEntity);
            
			//log.debug("InstagramAPI : " + "Response: " + mResponse );
			mError = "success";
		} 
		catch(UndeclaredThrowableException e)
		{
			mIsError = true;
			DebugLog(e.getUndeclaredThrowable().getMessage());
			mError = "network error";
		}
		catch (Exception e) 
		{
			mIsError = true;
			DebugLog(e.getCause() + " : " + e.getMessage());
			mError = "network error";
		}
	}
	
	private void DebugLog(final String text) {
		log.debug(text);
		if(mDebugLog.length() < 9999) {
	        Time now = new Time();
	        now.setToNow();
			mDebugLog = now.format("%d %b %y, %T")+" : "+text+"\r\n"+mDebugLog;
		}
		if(null != Configuration.gDebugLogTextView)
		{
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					Configuration.gDebugLogTextView.setText(mDebugLog);
				}
			});
		}
		
		/*mHandler.post(new Runnable() {
			@Override
			public void run() {
				helper.toast(mContext, text);
			}
		});*/		
	}

	public void clearLog() {
		mDebugLog = "";
		if(null != Configuration.gDebugLogTextView)
		{
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					Configuration.gDebugLogTextView.setText(mDebugLog);
				}
			});
		}
	}
}
