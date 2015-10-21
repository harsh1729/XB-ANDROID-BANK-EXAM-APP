package com.xercesblue.onlinebankexampo;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ImageView;

//import com.chartboost.sdk.Chartboost;
import com.google.android.gcm.GCMRegistrar;
//import android.widget.Toast;

public class Activity_Splash extends Activity {

	private final int SPLASH_TIME_OUT = 2000;
	
	Thread_RegisterOnPhpServer thread_RegisterOnPhpServer ;
	Thread_GetAppConfigFromServer thread_GetConfig;
	String Imei;
	// Internet detector
    Custom_ConnectionDetector cd;
    boolean canfinish = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		Imei = telephonyManager.getDeviceId();
		if(Imei==null){
			Imei = "";
		}
		resizeImages();
		DBHandler_Main dbHandler = new DBHandler_Main(this);
		try {
			dbHandler.deleteOldDB(this);
			dbHandler.createDataBase();

    		Log.i("HARSH","Recieved ID!" + GCMRegistrar.getRegistrationId(getApplicationContext()) );
		} catch (IOException e) {

			Globals.showAlertDialogError(this, "Failed to load questions .");
			return;
		}
		
		registerDeviceOnServer();
		getAppConfigFromServer();
		hideScreenAfterTimeOut();
		
		//Chartboost.startWithAppId(this, Globals.CHARTBOOST_APP_ID, Globals.CHARTBOOST_SIGNATURE_ID);
	    //Chartboost.onCreate(this);	
	}

	private void registerDeviceOnServer(){
		Log.i("HARSH","registerDeviceOnServer");
		try
        {
        	GCMRegistrar.checkDevice(this);
        	GCMRegistrar.checkManifest(this);
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
        

    	cd = new Custom_ConnectionDetector(getApplicationContext());
        
        // Check if Internet present
        if (cd.isConnectingToInternet())
        {
        	
        	String regId = GCMRegistrar.getRegistrationId(this);
        	//Log.i("HARSH","At Line 70");
        	if (regId.equals("")) 
        	{
        		//Log.i("HARSH","At Line 73");
        		//Toast.makeText(getApplicationContext(), "GCM request send!", Toast.LENGTH_SHORT).show();
        		GCMRegistrar.register(getApplicationContext(), GCMIntentService.SENDER_ID);
        		regId = GCMRegistrar.getRegistrationId(this);
            } 
        	Globals.GCM_REG_ID = regId;
        	if(!GCMRegistrar.isRegisteredOnServer(this))
        	{
        		//Log.i("HARSH","At Line 80");
        		int MAX_LOOP = 2;
        		int counter =1;
        		//  device is not registered on php server. First register it on php server and then call GCMRegistrar.setRegisteredOnServer(context, true)
        		do{
        			Log.i("HARSH","Do "+counter);
        			if(!regId.trim().equals("")){
        				Log.i("HARSH"," REGISTERING WITH ID "+regId);
        				thread_RegisterOnPhpServer = new Thread_RegisterOnPhpServer(Globals.APP_ID,regId,Imei,this);
        				thread_RegisterOnPhpServer.start();
        				break;
        			}
        			try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
        			counter++;
        		}while(  counter < MAX_LOOP);
        		}
        } 
        else
        { 
        	//Toast.makeText(getApplicationContext(), "No INTERNET Connection", Toast.LENGTH_SHORT).show();
        	Log.i("HARSH","No INTERNET Connection");
        }
	}
	private void getAppConfigFromServer(){
		thread_GetConfig = new Thread_GetAppConfigFromServer(Globals.APP_ID,this);
		thread_GetConfig.start();
	}
	private void resizeImages(){
		ImageView imgViewLogo = (ImageView)findViewById(R.id.imgLogoXB);
		//ImageView imgViewName = (ImageView)findViewById(R.id.imgLogoName);

		int screenWidth = Globals.getScreenSize(this).x;
		int logoWidth = screenWidth/100 * 60 ;// 17%
		//int nameWidth = screenWidth/100 * 78 ;// 64%

		Options options = new BitmapFactory.Options();
		options.inScaled = false;
		Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.xb, options);
		logo = Globals.scaleToWidth(logo,logoWidth);
		//Bitmap name = BitmapFactory.decodeResource(getResources(), R.drawable.logo_name, options);
		//name = Globals.scaleToWidth(name,nameWidth);

		imgViewLogo.setImageBitmap(logo);
		//imgViewName.setImageBitmap(name);
	}
	private void hideScreenAfterTimeOut(){
		new Handler().postDelayed(new Runnable() {

			/*
			 * Showing splash screen with a timer. This will be useful when you
			 * want to show case your app logo / company
			 */

			@Override
			public void run() {
				// This method will be executed once the timer is over
				
					Intent i = null;
					int selectLangId = 0;
					
					try{
						selectLangId = Globals.getAppConfig(Activity_Splash.this).selectedLangId;
					}catch(Exception ex){
						Log.e("HARSH","Error in hideScreenAfterTimeOut , Activity Splash");
					}
					
					if( selectLangId > 0){ // already selected
						i = new Intent(Activity_Splash.this, Activity_Home.class);
					}else{
						i = new Intent(Activity_Splash.this, Activity_SelectLanguage.class);
					}

					startActivity(i);
					finish();
					
					
				}
		}, SPLASH_TIME_OUT);
	}
	
	
	class Thread_RegisterOnPhpServer extends Thread
    {
    	Context context;
    	private String regId ;
    	private int AppId;
    	private String Imei;
    	public Thread_RegisterOnPhpServer(int AppId,String regId,String Imei,Context context)
    	{
    		this.regId = regId;
    		this.context = context;
    		this.AppId = AppId;
    		this.Imei = Imei;
    		Log.i("SUSHIL","In RegisterOnPhpServer constructor"+Imei);
    	}
    	

		HttpClient httpClient;
		@SuppressLint("NewApi")
		public void run()
    	{
    		try 
    		{
    			httpClient =  AndroidHttpClient.newInstance("Android");
    			HttpGet httpGet = new HttpGet(Custom_ServerURL_Params.getPushnotificationRegisteruser_link(regId,AppId,Imei,Activity_Splash.this));
    			Log.i("HARSH","ADDress is : "+Custom_ServerURL_Params.getPushnotificationRegisteruser_link(regId,AppId,Imei,Activity_Splash.this));			
    			HttpResponse response = httpClient.execute(httpGet);
    			String data = Globals.convertInputStreamToString(response.getEntity().getContent());
    			if(data.equalsIgnoreCase("registered"))
    			{
    				GCMRegistrar.setRegisteredOnServer(context, true);
    				Log.i("HARSH","Recieved registered!");
    				
    			}
    			else if(data.equalsIgnoreCase("false"))
    			{
    				GCMRegistrar.setRegisteredOnServer(context, false);
    				Log.i("HARSH","Recieved false!");					
    			}
    			
    			
    			
    			}
    		catch (IOException e) 
    		{
    			e.printStackTrace();
    		}
    		catch (Exception e)
    		{
    			e.printStackTrace();
    		}
    		finally{
    			
    			if(httpClient != null ){
    				httpClient = null;
    			}
    		}
    		
    	}
		
    }
		
		class Thread_GetAppConfigFromServer extends Thread
	    {
	    	Context context;
	    	private int AppId;
	    	public Thread_GetAppConfigFromServer(int AppId,Context context)
	    	{
	    		this.context = context;
	    		this.AppId = AppId;
	    		Log.i("HARSH","In RegisterOnPhpServer constructor");
	    	}

			HttpClient httpClient;
			@SuppressLint("NewApi")
			public void run()
	    	{
	    		try 
	    		{
	    			//Fetch AppConfig
	    			HttpClient httpClient = new DefaultHttpClient();
					HttpGet httpGetAppConfig = new HttpGet(Custom_ServerURL_Params.getAdvertisement_link(AppId,Activity_Splash.this));
					HttpResponse responseAppConfig = httpClient.execute(httpGetAppConfig);
					
					String jsonResponce = Globals.convertInputStreamToString(responseAppConfig.getEntity().getContent());
					
					Log.i("HARSH", "Responce is : "+jsonResponce);
					
					try {

					    JSONObject obj = new JSONObject(jsonResponce);
					    int showAds = 1;
					    int adTypeId = 3;
					    int adTypeIntertialId = 3;
					    
					    
					    if(obj.has("ShowAdvt")){
					     showAds = obj.getInt("ShowAdvt");
					    }
					    
					    if(obj.has("AdvtId")){
					    	adTypeId = obj.getInt("AdvtId");
						    }
					    
					    if(obj.has("AdvtIntertialId")){
					    	adTypeIntertialId = obj.getInt("AdvtIntertialId");
						    }
					    //Log.i("HARSH", "showAds, adTypeId , adTypeIntertialId ::"+ showAds +","+ adTypeId +","+ adTypeIntertialId);
					    
					    if(obj.has("promotional_text"))
					    	Activity_Home.promotionalText = obj.getString("promotional_text");
					    DBHandler_AppConfig dbH = new DBHandler_AppConfig(context);
					    dbH.updateAppConfigurationAds(showAds,adTypeId,adTypeIntertialId);
					} catch (Exception ex) {
					    Log.e("HARSH", "Could not parse malformed JSON: \"" + jsonResponce + "\"");
					}
					//Toast.makeText(Activity_Splash.this, "Responce is : "+responce, Toast.LENGTH_SHORT).show();
	    		}
	    		catch (IOException e) 
	    		{
	    			e.printStackTrace();
	    		}
	    		catch (Exception e)
	    		{
	    			e.printStackTrace();
	    		}
	    		finally{
	    			
	    			if(httpClient != null ){
	    				httpClient = null;
	    			}
	    		}
	    		
	    	}
    	
    	
	    }

		
}
