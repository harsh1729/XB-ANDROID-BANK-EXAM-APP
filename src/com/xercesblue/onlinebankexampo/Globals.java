package com.xercesblue.onlinebankexampo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Globals {
	public static final int VOLLEY_TIMEOUT_MILLISECS = 10000;
	private static Object_AppConfig objAppConfig ;
	private static RequestQueue mRequestQueue;
	
	public static final int APP_TRUE = 1;
	public static final int APP_FALSE = 0;
	public static final int APP_ID= 1;

	//public static final String AD_UNIT_ID_BANNER = "ca-app-pub-4880866047187616/3018903684";//"ca-app-pub-1764326035133434/9755228502";//"ca-app-pub-1764326035133434/3454466504";
	//public static final String AD_UNIT_ID_INTERSTITIAL = "ca-app-pub-4880866047187616/4495636880";//"ca-app-pub-1764326035133434/3708694908";//"ca-app-pub-1764326035133434/6407932906";
	
	public static final long AD_INMOBI_BANNER_PLACEMENT_ID =Long.parseLong("1431974061373053") ;
	public static final long AD_INMOBI_INTERTIAL_PLACEMENT_ID = Long.parseLong("1431974061374244");
	public static final String AD_INMOBI_ACCOUNT_ID ="e38831527b0b4e8f9cf1411dbc59304d";
	public static final String AD_INMOBI_PROPERTY_ID = "0441626db6ee4cb2954475a95e7ad2ef";//"53d0ef05be81426ea33d9e7005a32a94";//
	public static  String GCM_REG_ID = "";
	
	//public static final String VSERV_BILLBOARD_ZONE_ID = "db51ffbf";
	//public static final String VSERV_BANNER_ZONE_ID = "4beee22e";
	
	public static final String STARTAPP_APP_ID = "209772601";
	public static final String STARTAPP_DEVELOPER_ID = "109766035";
	
	public static final String CHARTBOOST_APP_ID = "5597787143150f6033624d71";
	public static final String CHARTBOOST_SIGNATURE_ID = "b8be56e0882cb8351a308da45d44f0906f81a8ab";
	
	public static final int ADD_TYPE_INMOBI = 1;
	public static final int ADD_TYPE_CHARTBOOST= 2;
	public static final int ADD_TYPE_STARTAPP= 3;
	
	private static EditText etAlerrtMessage;
	//private static int version_code;
	public static int current_version_code=9;
	@SuppressLint("NewApi")

	static public Point getScreenSize(Activity currentActivity){
		Display display = currentActivity.getWindowManager().getDefaultDisplay();
		Point size = new Point();


		if (android.os.Build.VERSION.SDK_INT >= 13) 
		{
			display.getSize(size);
		} 
		else 
		{
			 size.x = display.getWidth();
			 size.y = display.getHeight();
		}
		
		return size;
	}
	
	static public Object_AppConfig getAppConfig(Context con){
		
		if(objAppConfig == null){
			DBHandler_AppConfig dbH = new DBHandler_AppConfig(con);
			objAppConfig = dbH.getAppConfiguration();
		}
		
		return objAppConfig;
	}

	static public Point getAppButtonSize(Activity context ){
		
		int screenWidth = Globals.getScreenSize(context).x;
		
		Point size = new Point();
		
		size.x = 4*screenWidth/10;
		size.y = size.x/3;
		
		return size;
	}
	static public Boolean getBoolFromInt(int val){
		if(val > 0)
			return true;
		return false;
	}
	static public int getIntFromBool(Boolean val){
		if(val)
			return 1;
		
		return 0;
	}
	
	static public Bitmap scaleToWidth(Bitmap bitmap,int scaledWidth) {
		if (bitmap != null) {

			int bitmapHeight = bitmap.getHeight(); 
			int bitmapWidth = bitmap.getWidth(); 

			// scale According to WIDTH
			int scaledHeight = (scaledWidth * bitmapHeight) / bitmapWidth;

			try {

				bitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth,
						scaledHeight, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return bitmap;
	}
	
	static public int getButtonsPadding(int btnSize){
		
		return btnSize/10;
	}
	
	static public int getAppFontSize(Activity context){
		
		
		return (getScreenSize(context).x/120 + 12);
	}
	
static public int getAppFontSize_Small(Activity context){
		
		return (getScreenSize(context).x/120 + 10);
	}
	static public int getAppFontSize_Large(Activity context){
		
		return (getScreenSize(context).x/120 + 14);
	}

	static public void showAlertDialogError(final Activity activity){
		
		showAlertDialogError(activity,"Please try again.");
		
	}
	
	static public void showAlertDialogError(final Activity activity , String msg){
	
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog closed
				dialog.cancel();
				activity.finish();
			}
		};
		
		Globals.showAlertDialog("Error",msg ,activity,"OK", listener,null,null,false);
	}
	
static public void showAlertDialogOneButton(String title,String msg,Context context,String positiveButtonText,DialogInterface.OnClickListener listnerPositive,Boolean isCancelable){
		
		Globals.showAlertDialog(title,msg ,context,positiveButtonText, listnerPositive,null,null,isCancelable);
	}

	@SuppressLint("NewApi")
	static public void showAlertDialog(String title,String msg,Context context,String positiveButtonText,DialogInterface.OnClickListener listnerPositive,String negativeButtonText ,DialogInterface.OnClickListener listnerNegative,Boolean isCancelable){
		
		AlertDialog alertDialog ;
		
		if (android.os.Build.VERSION.SDK_INT >= 11) 
		{
		 alertDialog = new AlertDialog.Builder(
				context ,AlertDialog.THEME_HOLO_LIGHT).create();
		}else{
			alertDialog = new AlertDialog.Builder(
					context).create();
		}

		alertDialog.setTitle(title);
		alertDialog.setMessage(msg);
		alertDialog.setCancelable(isCancelable);
		alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,positiveButtonText,listnerPositive);
		
		if(negativeButtonText!= null && !negativeButtonText.equals("")){
			alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,negativeButtonText,listnerNegative);
		}
		alertDialog.show();
		
	}
	
@SuppressLint("NewApi")
static public void showAlertDialogEditText(String title,String msg,Context context,String positiveButtonText,DialogInterface.OnClickListener listnerPositive,String negativeButtonText ,DialogInterface.OnClickListener listnerNegative,Boolean isCancelable){
		
	AlertDialog alertDialog ;
	
	if (android.os.Build.VERSION.SDK_INT >= 11) 
	{
	 alertDialog = new AlertDialog.Builder(
			context ,AlertDialog.THEME_HOLO_LIGHT).create();
	}else{
		alertDialog = new AlertDialog.Builder(
				context).create();
	}
		
		etAlerrtMessage = new EditText(context);
		etAlerrtMessage.setHint("Type Your Message");

		alertDialog.setTitle(title);
		alertDialog.setMessage(msg);
		alertDialog.setView(etAlerrtMessage);
		alertDialog.setCancelable(isCancelable);
		alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,positiveButtonText,listnerPositive);
		
		if(negativeButtonText!= null && !negativeButtonText.equals("")){
			alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,negativeButtonText,listnerNegative);
		}
		alertDialog.show();
		
	}

public static String getAlertMessage(){
	
	String msg ="";
	
	if(etAlerrtMessage != null)
		if(etAlerrtMessage.getText() != null){
			
			msg = etAlerrtMessage.getText().toString();
			etAlerrtMessage = null;
		}
	
	
	
	return msg;
}


public static int getversion_code(Context con)
{
	PackageInfo pInfo=null;
	try 
	{
		pInfo= con.getPackageManager().getPackageInfo(con.getPackageName(), 0);
	}
	catch (NameNotFoundException e) 
	{
	   e.printStackTrace();
	}
	if(pInfo != null){
		return pInfo.versionCode;
	}else{
		return 0;
	}
	
}
	
	static public Object_QuesCategory getQuesCatObj(int catId , ArrayList<Object_QuesCategory> arrayOfQuesCatObj){
		Object_QuesCategory objCat=null;
		for(Object_QuesCategory obj : arrayOfQuesCatObj){
			if(obj.id==catId)
			{
				objCat=obj;
				break;
			}
		}
		return objCat;
	}
	
	static public String  getChildCatName(int childCatId , Object_QuesCategory quesCatObj){

		String name = "";
		if(quesCatObj.isParentCategory == 1){
			if(quesCatObj.arrayChildrenCat!= null){
				name = getChildCatName(childCatId, quesCatObj.arrayChildrenCat);
			}

		}else{
			name = quesCatObj.name;
		}
		return name;

	}
	
	static public String  getChildCatName(int childCatId ,ArrayList<Object_QuesCategory> arrayChildrenCat){

		String name = "";
				Object_QuesCategory objCat = getQuesCatObj(childCatId,arrayChildrenCat);
				if(objCat != null){
					name = objCat.name;
				}
			
		return name;

	}
	
	public static int getHeightofListView(ListView listView) {

	    ListAdapter mAdapter = listView.getAdapter();

	    int listviewElementsheight = 0;
	    int items = mAdapter.getCount(); 


	    for (int i = 0; i < items; i++) {

	        View childView = mAdapter.getView(i, null, listView);
	        childView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
	        listviewElementsheight+= childView.getMeasuredHeight();
	        }


	        return listviewElementsheight;

	    }
	
	public static String convertInputStreamToString(InputStream is)
	{
		String line = "";
		StringBuilder total = new StringBuilder();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));

		try 
		{
			while((line = rd.readLine()) != null)
			{
				total.append(line);
			}
		} 
		catch (IOException e) 
		{
			//Toast.makeText(this,"Stream Exception", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		return total.toString();
	}
	
	public static RequestQueue getRequestQueue(Context context) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }
  
        return mRequestQueue;
    }
	/*
	public Bitmap writeTextOnDrawable(Bitmap bm, String text , int color) {

	    Typeface tf = Typeface.create("Helvetica", Typeface.NORMAL);

	    Paint paint = new Paint();
	    paint.setStyle(Style.FILL);
	    paint.setColor(color);
	    paint.setTypeface(tf);
	    paint.setTextAlign(Align.CENTER);
	   // paint.setTextSize(convertToPixels(currentActivity, 11));
	    paint.setTextSize(currentActivity.getResources().getDimension(R.dimen.txt_app_buttons_container_fontsize_big));
	    Rect textRect = new Rect();
	    paint.getTextBounds(text, 0, text.length(), textRect);

	    Canvas canvas = new Canvas(bm);

	    //If the text is bigger than the canvas , reduce the font size
	    if(textRect.width() >= (canvas.getWidth() - 4))     //the padding on either sides is considered as 4, so as to appropriately fit in the text
	        paint.setTextSize(currentActivity.getResources().getDimension(R.dimen.txt_app_buttons_container_fontsize_small));        //Scaling needs to be used for different dpi's

	    //Calculate the positions
	    int xPos = (canvas.getWidth() / 2) - 2;     //-2 is for regulating the x position offset

	    //"- ((paint.descent() + paint.ascent()) / 2)" is the distance from the baseline to the center.
	    int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2)) ;  

	    canvas.drawText(text, xPos, yPos, paint);

	    return new BitmapDrawable(currentActivity.getResources(), bm).getBitmap();
	}



	public  int convertToPixels(Context context, int nDP)
	{
	    final float conversionScale = context.getResources().getDisplayMetrics().density;

	    return (int) ((nDP * conversionScale) + 0.5f) ;

	}
	*/
	
	
}
