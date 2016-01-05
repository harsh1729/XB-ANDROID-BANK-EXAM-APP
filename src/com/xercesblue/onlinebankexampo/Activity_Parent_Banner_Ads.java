package com.xercesblue.onlinebankexampo;

import java.util.Map;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.inmobi.ads.InMobiAdRequestStatus;
import com.inmobi.ads.InMobiBanner;
import com.inmobi.ads.InMobiBanner.BannerAdListener;
import com.inmobi.sdk.InMobiSdk;
import com.startapp.android.publish.StartAppSDK;
import com.startapp.android.publish.banner.Banner;

public class Activity_Parent_Banner_Ads extends Activity_Parent_IntertialAds {

	private InMobiBanner banner;
	
	
	
	@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			try{
			if(Globals.getAppConfig(this).showAdds ==Globals.APP_TRUE){
				
				if(Globals.getAppConfig(this).adTypeId == Globals.ADD_TYPE_INMOBI && Globals.getAppConfig(this).adTypeInterId != Globals.ADD_TYPE_INMOBI )
					//InMobi.initialize(this, Globals.AD_INMOBI_PROPERTY_ID);
					InMobiSdk.init(this, Globals.AD_INMOBI_ACCOUNT_ID);
				else if(Globals.getAppConfig(this).adTypeId == Globals.ADD_TYPE_STARTAPP && Globals.getAppConfig(this).adTypeInterId != Globals.ADD_TYPE_STARTAPP)
					StartAppSDK.init(this, Globals.STARTAPP_DEVELOPER_ID,  Globals.STARTAPP_APP_ID, true);
			}
			}catch(Exception ex){
				Log.i("HARSH", "Exception in Banner Init On create");
			}
			
		}
	
	@Override
	protected void onResume() {
		super.onResume();
		try{
			setAddsVisibility();
		}catch(Exception ex){
			Log.i("HARSH", "Exception in Banner Init On create");
		}
	}


	/** Called before the activity is destroyed. */
	@Override
	public void onDestroy() {
		//if(banner!= null){
			//banner.destroy();
			
		//}
		super.onDestroy();
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
		
		case Globals.ADD_TYPE_STARTAPP:
			Log.i("HARSH", "Show Banner ADD_TYPE_STARTAPP");
			showStartAppAdd();
		default:
			break;
		}
		
		
	}
	
	private void  showInMobiAdd(){

		LinearLayout layout = (LinearLayout) findViewById(R.id.llytAdd);
		
		//banner = new IMBanner(this,Globals.AD_INMOBI_PROPERTY_ID,Globals.getOptimalSlotSize(this)); //IMBanner.INMOBI_AD_UNIT_320X50);
		banner = new InMobiBanner(this, Globals.AD_INMOBI_BANNER_PLACEMENT_ID);
		final float scale = getResources().getDisplayMetrics().density;
		
		int width =Globals.getScreenSize(this).x; //(int) (layout.getLayoutParams().width );//320* scale + 0.5f);
		int height = (int) (50 * scale + 0.5f);	
		Log.i("HARSH", "layout.getWidth() "+ width);
		banner.setLayoutParams(new LinearLayout.LayoutParams(width, height));
		banner.setRefreshInterval(30);
		banner.setListener(new BannerAdListener() {
			@Override
			public void onAdLoadSucceeded(InMobiBanner ad){ 
			}
			@Override
			public void onAdLoadFailed(InMobiBanner ad, InMobiAdRequestStatus statusCode){
			}
			@Override
			public void onAdDisplayed(InMobiBanner ad) {
			}
			@Override
			public void onAdDismissed(InMobiBanner ad) {
			}
			@Override
			public void onAdInteraction(InMobiBanner ad, Map<Object, Object> params) {
			}
			@Override
			public void onUserLeftApplication(InMobiBanner ad) {
			}
			@Override
			public void onAdRewardActionCompleted(InMobiBanner ad, Map<Object, Object> rewards){
			}
			});
		
		banner.setAnimationType(InMobiBanner.AnimationType.ROTATE_HORIZONTAL_AXIS);
		banner.load();
		layout.addView(banner);
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
		int width = Globals.getScreenSize(this).x;// (int) (320 * scale + 0.5f);
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
