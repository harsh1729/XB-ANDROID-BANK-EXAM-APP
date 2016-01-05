package com.xercesblue.onlinebankexampo;

import java.util.Map;

import android.os.Bundle;
import android.util.Log;

import com.inmobi.ads.InMobiAdRequestStatus;
import com.inmobi.ads.InMobiInterstitial;
import com.inmobi.sdk.InMobiSdk;
//import com.chartboost.sdk.CBLocation;
//import com.chartboost.sdk.Chartboost;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;
//import mobi.vserv.android.ads.AdPosition;
//import mobi.vserv.android.ads.AdType;
//import mobi.vserv.android.ads.VservManager;

public class Activity_Parent_IntertialAds extends Activity_Parent {

	//private VservManager manager;
	protected StartAppAd startAppAd = null;
	protected int questionCount = 0;
	//private boolean showingChartboost = false;

	InMobiInterstitial interstitial;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(Globals.getAppConfig(this).showAdds ==Globals.APP_TRUE){
						
			if(Globals.getAppConfig(this).adTypeInterId == Globals.ADD_TYPE_INMOBI )
				//InMobi.initialize(this, Globals.AD_INMOBI_PROPERTY_ID);
				InMobiSdk.init(this, Globals.AD_INMOBI_ACCOUNT_ID);
			else if(Globals.getAppConfig(this).adTypeInterId == Globals.ADD_TYPE_STARTAPP){
				startAppAd = new StartAppAd(this);
				StartAppSDK.init(this, Globals.STARTAPP_DEVELOPER_ID,  Globals.STARTAPP_APP_ID, true);
			}else if(Globals.getAppConfig(this).adTypeInterId == Globals.ADD_TYPE_CHARTBOOST){
				//Chartboost.startWithAppId(this, Globals.CHARTBOOST_APP_ID, Globals.CHARTBOOST_SIGNATURE_ID);
			    //Chartboost.onCreate(this);
			   // showingChartboost = true;
			}
		}
	}
	
	
	
	private void showAds(){
		if(Globals.getAppConfig(this).showAdds ==Globals.APP_TRUE && questionCount > 20)//
		{
			switch (Globals.getAppConfig(this).adTypeInterId) {
			case Globals.ADD_TYPE_INMOBI:
				loadInMobiIntertial();
				break;
			case Globals.ADD_TYPE_CHARTBOOST:
				showChartBoostIntertial();
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
	
	
	
	
	private void loadInMobiIntertial(){
		Log.i("HARSH", "INIT AD");
		// Create an ad.
		if(interstitial == null){
			System.out.println("interstitial is null");
		
			InMobiInterstitial.InterstitialAdListener interstitialAdListener = new InMobiInterstitial.InterstitialAdListener() {
				@Override
				public void onAdLoadSucceeded(InMobiInterstitial ad) {
				if(ad.isReady()){
				ad.show();
				}
				}
				@Override
				public void onAdDisplayed(InMobiInterstitial ad) {}
				@Override
				public void onAdDismissed(InMobiInterstitial ad) {}
				@Override
				public void onAdInteraction(InMobiInterstitial ad, Map<Object, Object> params) {}
				@Override
				public void onAdRewardActionCompleted(InMobiInterstitial ad, Map<Object, Object> rewards) {}
				@Override
				public void onUserLeftApplication(InMobiInterstitial ad) {}
				@Override
				public void onAdLoadFailed(InMobiInterstitial arg0,
						InMobiAdRequestStatus arg1) {
					// TODO Auto-generated method stub
					
				}
				};
				
		interstitial = new InMobiInterstitial(this, Globals.AD_INMOBI_INTERTIAL_PLACEMENT_ID, interstitialAdListener);

		}
	   
		interstitial.load();
	}
	
	
	private void showChartBoostIntertial(){
		
		Log.i("HARSH", "showChartBoostIntertial called");
		//Chartboost.cacheInterstitial(CBLocation.LOCATION_DEFAULT);
		//Chartboost.showInterstitial(CBLocation.LOCATION_DEFAULT);

		
	}
	
	

	@Override
	protected void onResume() {
	    super.onResume();
	    if(startAppAd != null)
	    	startAppAd.onResume();
	    //if(showingChartboost)
	    	//Chartboost.onResume(this);
	        
	}
	
	@Override
	protected void onPause() {
	    super.onPause();
	    if(startAppAd != null)
	    	startAppAd.onPause();
	    //if(showingChartboost)
	    	//Chartboost.onPause(this);
	}
	
	@Override
	protected void onDestroy() {		
		showAds();
		super.onDestroy();
		//if(showingChartboost)
			//Chartboost.onDestroy(this);
	}
	
	/*
	@Override
	public void onStart() {
	    super.onStart();
	    //if(showingChartboost)
	    	//Chartboost.onStart(this);
	}
	
	
        
	@Override
	public void onStop() {
	    super.onStop();
	    //if(showingChartboost)
	    	//Chartboost.onStop(this);
	}


	@Override
	public void onBackPressed() {
	   // if (Chartboost.onBackPressed())
	        //return;
	    //else
	        super.onBackPressed();
	}
	*/

}
