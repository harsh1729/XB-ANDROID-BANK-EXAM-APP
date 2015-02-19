package com.xercesblue.onlinebankexampo;

import java.util.Map;

import mobi.vserv.android.ads.ViewNotEmptyException;
import mobi.vserv.android.ads.VservController;
import mobi.vserv.android.ads.VservManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.inmobi.commons.InMobi;
import com.inmobi.monetization.IMBanner;
import com.inmobi.monetization.IMBannerListener;
import com.inmobi.monetization.IMErrorCode;
import com.startapp.android.publish.banner.Banner;

public class Activity_Parent_Banner_Ads extends Activity_Parent_IntertialAds {

	private IMBanner banner;
	
	private VservController controller;
	
	@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			
			if(Globals.getAppConfig(this).showAdds ==Globals.APP_TRUE){
				
				if(Globals.getAppConfig(this).adTypeId == Globals.ADD_TYPE_INMOBI )
					InMobi.initialize(this, Globals.AD_INMOBI_PROPERTY_ID);
			}
			
		}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setAddsVisibility();
	}


	/** Called before the activity is destroyed. */
	@Override
	public void onDestroy() {
		if(banner!= null){
			banner.destroy();
			
		}
		super.onDestroy();
	}
	
	@Override
	protected void onStart() {
		if (null != controller) {
			controller.resumeRefresh();
		}
		super.onStart();
	}

	@Override
	protected void onStop() {

		if (null != controller) {
			controller.stopRefresh();
		}
		super.onStop();
	}
	
	private void createAdds(){
		// Create an ad.
		

		// Add the AdView to the view hierarchy. The view will have no size
		// until the ad is loaded.
		
		switch (Globals.getAppConfig(this).adTypeId) {
		case Globals.ADD_TYPE_INMOBI:
			Log.i("HARSH", "Show Banner ADD_TYPE_INMOBI");
			showInMobiAdd();
			break;
		case Globals.ADD_TYPE_VSERV:
			Log.i("HARSH", "Show Banner ADD_TYPE_VSERV");
			showVservAdd();
			break;
		case Globals.ADD_TYPE_STARTAPP:
			Log.i("HARSH", "Show Banner ADD_TYPE_STARTAPP");
			showStartAppAdd();
		default:
			break;
		}
		
		
	}
	
	private void  showInMobiAdd(){

		LinearLayout layout = (LinearLayout) findViewById(R.id.llytAdd);
		
		banner = new IMBanner(this,Globals.AD_INMOBI_PROPERTY_ID,Globals.getOptimalSlotSize(this)); //IMBanner.INMOBI_AD_UNIT_320X50);
		final float scale = getResources().getDisplayMetrics().density;
		int width = (int) (320 * scale + 0.5f);
		int height = (int) (50 * scale + 0.5f);		
		banner.setLayoutParams(new LinearLayout.LayoutParams(width, height));
		banner.setRefreshInterval(30);
		banner.setIMBannerListener(new IMBannerListener() {
	        @Override
	        public void onShowBannerScreen(IMBanner arg0) {
	        	Log.i("HARSH", "onShowBannerScreen");
	                }
	            @Override
	        public void onLeaveApplication(IMBanner arg0) {
	            	Log.i("HARSH", "onLeaveApplication");
	        }
	        @Override
	        public void onDismissBannerScreen(IMBanner arg0) {
	        	Log.i("HARSH", "onDismissBannerScreen");
	        }
	                @Override
	        public void onBannerRequestFailed(IMBanner banner, IMErrorCode errorCode) {
	                	Log.i("HARSH", "onBannerRequestFailed");
	                	banner.loadBanner();
	        }
	        @Override
	        public void onBannerRequestSucceeded(IMBanner arg0) {
	        	Log.i("HARSH", "onBannerRequestSucceeded");
	                }
	                @Override
	        public void onBannerInteraction(IMBanner arg0, Map<String, String> arg1) {
	                	Log.i("HARSH", "onBannerInteraction");
	        }
	    });
		
		banner.loadBanner();
		layout.addView(banner);
	}
	
	private void showVservAdd(){
		LinearLayout adView = (LinearLayout) findViewById(R.id.llytAdd);
		if (adView != null) {
			adView.removeAllViews();
		}
		VservManager renderAdManager = VservManager.getInstance(this);
		try {
			controller = renderAdManager.renderAd(Globals.VSERV_BANNER_ZONE_ID, adView);
			if(controller!=null){
				controller.setRefresh(30);
			}
		} catch (ViewNotEmptyException e) {
			Log.e("HARSH ERROR", e.getMessage());
		}
	}
	
	public void setAddsVisibility(){
		Boolean hide = true;
		if(Globals.getAppConfig(this).showAdds == 1){
			hide = false;
		}
		LinearLayout llAdd = (LinearLayout)findViewById(R.id.llytAdd);
		if(hide){
			llAdd.setVisibility(View.GONE);
			Log.i("HARSH", "Hide Banner");
		}else{
			llAdd.setVisibility(View.VISIBLE);
			Log.i("HARSH", "Show Banner");
			createAdds();
		}
	}

	private void showStartAppAdd(){
		LinearLayout layout = (LinearLayout) findViewById(R.id.llytAdd);
		
		// Create new StartApp banner
		Banner startAppBanner = new Banner(this);
		final float scale = getResources().getDisplayMetrics().density;
		int width = (int) (320 * scale + 0.5f);
		int height = (int) (50 * scale + 0.5f);
		LinearLayout.LayoutParams bannerParameters = 
				new LinearLayout.LayoutParams(
						width,
						height);
		
		// Add the banner to the main layout
		layout.addView(startAppBanner, bannerParameters);
		System.out.println("Banner Added");
	}
	
}
