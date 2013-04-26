package com.popupsolution.cloudstax.android;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.popupsolution.cloudstax.android.log.Log;
import com.popupsolution.cloudstax.android.log.LogFactory;
import com.popupsolution.cloudstax.android.task.DownloadImageTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class helper {

	private static final Log log = LogFactory.getLog(helper.class);
	public static ProgressDialog mProgressDialogDialog = null;
	static Toast mToast = null;
	
    public static void Popup(Context context,String message,String title)
    {
    	try {
	        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
	        dialog.setTitle(title);
	        dialog.setMessage(message);
	        dialog.setPositiveButton("OK", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
	        dialog.show();
    	} 
	    catch (Exception e) {}	        
    }
    
	static public void toast(Context context,String txt) {
		CharSequence text = txt;
		int duration = Toast.LENGTH_LONG;
		if(null != mToast) mToast.cancel();
		mToast = Toast.makeText(context, text, duration);
		mToast.show();
	}

	public static void PopError(Context context,String message,final Runnable onClick) {
    	try {
	        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
	        dialog.setTitle("Error");
	        dialog.setMessage(message);
	        dialog.setPositiveButton("OK", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					if(null != onClick) {
						onClick.run();
					}
				}
			});
	        dialog.show();
    	} 
	    catch (Exception e) {
	    	log.error("PopError : "+e.getMessage());
	    }	        		
	}
	
    public static void PopError(Context context,String message)
    {
    	PopError(context,message,null);
    }

	static public void showWaitDlg(Context context,String txt,final Runnable onCancel) {
		try {
			if(null != mProgressDialogDialog) mProgressDialogDialog.dismiss();
			mProgressDialogDialog = null;
			mProgressDialogDialog = new ProgressDialog(context);
			mProgressDialogDialog.setMessage(txt); 
			mProgressDialogDialog.setIndeterminate(true); 
			if(null != onCancel){
				mProgressDialogDialog.setCancelable(true);
				mProgressDialogDialog.setButton("Cancel", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						onCancel.run();
					}
				});
				mProgressDialogDialog.setOnCancelListener(new OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						onCancel.run();
					}
				});
			}
			else {
				mProgressDialogDialog.setCancelable(false);
			}
			mProgressDialogDialog.show(); 
	    } 
	    catch (Exception e) {}
	}
	
	static public void hideWaitDlg() {		
	    try {
			if(null != mProgressDialogDialog) mProgressDialogDialog.dismiss();
			mProgressDialogDialog = null;
	    } 
	    catch (Exception e) {}
	}

    public static void ConfirmDialog(String message,Context context, final Runnable onConfirm)
    {
    	try {
	        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
	        dialog.setTitle("Confrim");
	        dialog.setMessage(message);
	        dialog.setPositiveButton("Yes", new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					onConfirm.run();					
				}
			});
	        dialog.setNegativeButton("No", new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
	        dialog.show();
    	} 
	    catch (Exception e) {}	        
    }
    
	static public String getFileName(String url) {
		String filename = "";
		if(url.lastIndexOf('/') > 0 && url.lastIndexOf('.') > 0) { 
			filename = url.substring(url.lastIndexOf('/')+1, url.lastIndexOf('.'));
		}
		// Or see http://stackoverflow.com/questions/605696/get-file-name-from-url
		return filename;
	}
	
	static public void SetCacheImage(Context context,String url,Bitmap bitmap) {
		File cacheDir = context.getCacheDir(); 
		File imageFile;
		FileOutputStream fos;
		imageFile = new File(cacheDir, getFileName(url));
		//Boolean bJPG = true;
		
		//if(url.toLowerCase().contains(".png")) bJPG = false;
		
		try {
			fos = new FileOutputStream(imageFile);
			//if(bJPG) bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);			
			//else bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}   
	}
	
	static public Bitmap GetCacheImage(Context context,String url) {
		Bitmap bitmap = null;
		File cacheDir = context.getCacheDir(); 
		File imageFile;
		FileInputStream fis;
		imageFile = new File(cacheDir, getFileName(url));
		
		try {
			fis = new FileInputStream(imageFile);
			bitmap = BitmapFactory.decodeStream(fis);
			fis.close();
		} catch (FileNotFoundException e) {
			bitmap = null;
			//e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}	
	/*
	public static View createTabView(final Context context, final String text) {
		View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg, null);
		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		tv.setText(text);
		return view;
	}
	
	public static View createTabViewWithIcon(final Context context, final String text, final int resid) {
		View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg, null);
		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		ImageView iv = (ImageView) view.findViewById(R.id.tabsImage);
		iv.setVisibility(View.VISIBLE);
		iv.setBackgroundResource(resid);
		tv.setText(text);
		return view;
	}*/

    public static void ExitProgram(final Activity activity)
    {
    	try {
	        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
	        dialog.setTitle("Confrim");
	        dialog.setMessage("Are you sure you want to exit ?");
	        dialog.setPositiveButton("Yes", new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					activity.setResult(-1);
					activity.finish();					
				}
			});
	        dialog.setNegativeButton("No", new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
	        dialog.show();
    	} 
	    catch (Exception e) {}	        
    }

    //see http://androidsnippets.com/how-to-invert-bitmap-color
    public static Bitmap invert(Bitmap src) {
		Bitmap output = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
		int A, R, G, B;
		int pixelColor;
		int height = src.getHeight();
		int width = src.getWidth();

	    for (int y = 0; y < height; y++) {
	        for (int x = 0; x < width; x++) {
	            pixelColor = src.getPixel(x, y);
	            A = Color.alpha(pixelColor);
	            R = 255 - Color.red(pixelColor);
	            G = 255 - Color.green(pixelColor);
	            B = 255 - Color.blue(pixelColor);
	            
	            output.setPixel(x, y, Color.argb(A, R, G, B));
	        }
	    }

	    return output;
	}      

	static public void loadImage(String url,ImageView imageView) {
		Bitmap bitmap = null;
		
		if(null == url || url.length() == 0) return;
		bitmap = GetCacheImage(ActivitySelector.mRootActivity,url);
		if(null == bitmap) {
			new DownloadImageTask().execute(url,imageView);
		}
		else {
			imageView.setImageBitmap(bitmap);
		}
			
	}
	
	static public Typeface getTypeface(String fontName) {
		Typeface gtypface = Typeface.MONOSPACE;
		try {
			Context context = Configuration.mContext;
			AssetManager assetManager = context.getAssets();
			gtypface = Typeface.createFromAsset(assetManager, "fonts/"+fontName);
		}
		catch(Exception e) {
			
		}
		return gtypface;
	}
	
}

