package com.xercesblue.onlinebankexampo;

import java.util.Map;

import mobi.vserv.android.ads.AdPosition;
import mobi.vserv.android.ads.AdType;
import mobi.vserv.android.ads.VservManager;

import android.os.Bundle;
import android.util.Log;

import com.inmobi.commons.InMobi;
import com.inmobi.monetization.IMErrorCode;
import com.inmobi.monetization.IMInterstitial;
import com.inmobi.monetization.IMInterstitialListener;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;

public class Activity_Parent_IntertialAds extends Activity_Parent {

	private VservManager manager;
	protected StartAppAd startAppAd = null;
	

	IMInterstitial interstitial;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(Globals.getAppConfig(this).showAdds ==Globals.APP_TRUE){
						
			if(Globals.getAppConfig(this).adTypeInterId == Globals.ADD_TYPE_INMOBI )
				InMobi.initialize(this, Globals.AD_INMOBI_PROPERTY_ID);
			else if(Globals.getAppConfig(this).adTypeInterId == Globals.ADD_TYPE_STARTAPP){
				startAppAd = new StartAppAd(this);
				StartAppSDK.init(this, Globals.STARTAPP_DEVELOPER_ID,  Globals.STARTAPP_APP_ID, true);
			}
		}
	}
	
	@Override
	protected void onDestroy() {		
		showAds();
		super.onDestroy();
	}
	
	private void showAds(){
		if(Globals.getAppConfig(this).showAdds ==Globals.APP_TRUE)
		{
			switch (Globals.getAppConfig(this).adTypeInterId) {
			case Globals.ADD_TYPE_INMOBI:
				loadInMobiIntertial();
				break;
			case Globals.ADD_TYPE_VSERV:
				showVservIntertial();
				break;
			case Globals.ADD_TYPE_STARTAPP:
				showStartAppIntertial();
				break;

			default:
				break;
			}
		}
	}
	private void showStartAppIntertial(){
		if(startAppAd != null){
			startAppAd.showAd();
			startAppAd.loadAd();
		}
	}
	
	@Override
	protected void onResume() {
	    super.onResume();
	    if(startAppAd != null)
	    	startAppAd.onResume();
	    
	    
	}
	
	@Override
	protected void onPause() {
	    super.onPause();
	    if(startAppAd != null)
	    	startAppAd.onPause();
	}
	
	private void loadInMobiIntertial(){
		Log.i("HARSH", "INIT AD");
		// Create an ad.
		if(interstitial == null){
			System.out.println("interstitial is null");
		
		interstitial = new IMInterstitial(this, Globals.AD_INMOBI_PROPERTY_ID);
		interstitial.setIMInterstitialListener(new IMInterstitialListener() {
	        public void onShowInterstitialScreen(IMInterstitial arg0) {
	        	System.out.println("onShowInterstitialScreen");
	        }
	        @Override
	        public void onLeaveApplication(IMInterstitial arg0) {
	        	System.out.println("onLeaveApplication");
	        }
	        @Override
	        public void onDismissInterstitialScreen(IMInterstitial arg0) {
	        	System.out.println("onDismissInterstitialScreen");
	        }
	        @Override
	        public void onInterstitialLoaded(IMInterstitial arg0) {
	        	System.out.println("onInterstitialLoaded");
	        	showInMobiInterstitial();
	        }
	        @Override
	        public void onInterstitialInteraction(IMInterstitial interstitial, Map<String, String> params) {                
	        	System.out.println("onInterstitialInteraction");
	        }
	        @Override
	        public void onInterstitialFailed(IMInterstitial arg0, IMErrorCode arg1) {
	        	System.out.println("onInterstitialFailed");
	        	interstitial.loadInterstitial();
	        }
	    }); 
		
		}
	   
		interstitial.loadInterstitial();
	}
	private void showInMobiInterstitial() {
		Log.i("HARSH", "showInterstitial");
		if (interstitial.getState() ==IMInterstitial.State.READY)
    		interstitial.show();
    	else {
	      Log.i("HARSH", "Interstitial ad was not ready to be shown.");
	    }
	    
	  }
	
	private void showVservIntertial(){
		
		Log.i("HARSH", "showVservIntertial called");
		manager = VservManager.getInstance(this);
		manager.setShowAt(AdPosition.START);
		//manager.setGender(Gender.FEMALE);
		manager.displayAd(Globals.VSERV_BILLBOARD_ZONE_ID, AdType.INTERSTITIAL);
		
		
		
		/*
		if (null != controller) {
			controller.stopRefresh();
			controller = null;
		}

		manager = VservManager.getInstance(this);

		manager.getAd(Globals.VSERV_BILLBOARD_ZONE_ID, AdOrientation.PORTRAIT,
				new AdLoadCallback() {

					@Override
					public void onLoadSuccess(VservAd adObj) {
						Toast.makeText(Activity_Results_Exam.this,
								"Success in getting Ad", Toast.LENGTH_SHORT)
								.show();
						adObject = adObj;
						Toast.makeText(
								Activity_Results_Exam.this,
								"Showing Ad for ZoneId "
										+ adObject.getZoneId(),
								Toast.LENGTH_SHORT).show();

						if (null != controller) {
							controller.stopRefresh();
							controller = null;
						}

						if (null != adObject) {
							adObject.setZoneId(Globals.VSERV_BILLBOARD_ZONE_ID);
							adObject.overlay(Activity_Results_Exam.this);
						}
					}

					@Override
					public void onLoadFailure() {
						Toast.makeText(Activity_Results_Exam.this,
								"Failed in getting AD", Toast.LENGTH_SHORT)
								.show();
					}

					@Override
					public void onNoFill() {
						Toast.makeText(Activity_Results_Exam.this, "No Ad Found",
								Toast.LENGTH_SHORT).show();
					}
				});
				
				*/
	}

}
