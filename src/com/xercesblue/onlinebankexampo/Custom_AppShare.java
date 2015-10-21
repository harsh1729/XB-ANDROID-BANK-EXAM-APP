package com.xercesblue.onlinebankexampo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Custom_AppShare {

	
    //private final static String APP_PNAME = "com.xercesblue.onlinebankexampo";
    
    private final static int DAYS_UNTIL_PROMPT = 0;
    public final static int LAUNCHES_UNTIL_PROMPT = 7;
    private UiLifecycleHelper uiHelper;
    private Activity activity;
  
    
    public boolean app_launched(Activity mContext , Bundle savedInstanceState) {
    	Boolean returnStatus = false;
    	this.activity = mContext; 
    	uiHelper = new UiLifecycleHelper(activity, null);
		uiHelper.onCreate(savedInstanceState);
		
        SharedPreferences prefs = mContext.getSharedPreferences("appsharer", 0);
        if (prefs.getBoolean("dontshowagain_share", false)) { return returnStatus; }
        
        SharedPreferences.Editor editor = prefs.edit();
        
        // Increment launch counter
        long launch_count = prefs.getLong("launch_count_share", 0) + 1;
        editor.putLong("launch_count_share", launch_count);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }
        
        // Wait at least n days before opening
        if (launch_count%LAUNCHES_UNTIL_PROMPT == 0) {
            if (System.currentTimeMillis() >= date_firstLaunch + 
                    (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                showShareDialog(editor);
                returnStatus = true;
            }
        }
        
        editor.commit();
        return returnStatus;
    }   
    
    @SuppressLint("NewApi")
	public  void showShareDialog( final SharedPreferences.Editor editor) {
    	if(activity == null){
			return;
		}
    	
    	AlertDialog.Builder dialogBuilder;
		if (android.os.Build.VERSION.SDK_INT >= 11) 
		{
			dialogBuilder = new AlertDialog.Builder(
				 activity ,AlertDialog.THEME_HOLO_LIGHT);
		}else{
			dialogBuilder = new AlertDialog.Builder(
					activity);
		}
		
		dialogBuilder.setTitle("Share Online Bank Exam App" );
		
		final Dialog dialog = dialogBuilder.create();
		LinearLayout ll = new LinearLayout(activity);
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setBackgroundResource(R.color.app_darkoffwhite);
		ll.setPadding(30, 20, 30, 20);

        
        TextView tv = new TextView(activity);
        tv.setText("If you liked this app and our efforts, please take a moment to share this app with your friends.");
        tv.setWidth(240);
        tv.setPadding(4, 0, 4, 10);
        tv.setTextColor(activity.getResources().getColor(R.color.app_darkblue));
        ll.addView(tv);
        
        Button b1 = new Button(activity);
        b1.setText("Share on Facebook" );
        b1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	shareOnFacebook();
                dialog.dismiss();
                
            }
        });        
        ll.addView(b1);

        Button b2 = new Button(activity);
        b2.setText("Share on WhatsApp");
        b2.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	shareOnWhatsApp();
                dialog.dismiss();
            }
        });
        ll.addView(b2);
        
      
        
        Button b3 = new Button(activity);
        b3.setText("Remind me later");
        b3.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ll.addView(b3);

        
        Button b4 = new Button(activity);
        b4.setText("Dont show again");
        b4.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (editor != null) {
                    editor.putBoolean("dontshowagain_share", true);
                    editor.commit();
                }
                dialog.dismiss();
            }
        });
        ll.addView(b4);
         
                
        dialog.show();   
        dialog.setContentView(ll);
    }
    
    private  void shareOnFacebook(){
    	if(activity == null){
			return;
		}
		
    	Custom_ConnectionDetector cd = new Custom_ConnectionDetector(activity);
		if(!cd.isConnectingToInternet()){
			Toast.makeText(activity, "No Internet connection", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (FacebookDialog.canPresentShareDialog(activity,
				FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
			// Publish the post using the Share Dialog
			Log.i("HARSH", "FacebookDialog");
			FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(
					activity).setLink(ServerURL.getShareLinkFacebook()).setDescription(ServerURL.getShareAppMsg()).setName("Online Bank Exam")
					.build();
			//.setApplicationName("Bank PO Online Exam")
			uiHelper.trackPendingDialogCall(shareDialog.present());

		} else {
			if (Session.getActiveSession() != null && Session.getActiveSession().isOpened()) {
				publishFeedDialog();
			}
			else {

				Session session = Session.getActiveSession();
				if (!session.isOpened() && !session.isClosed()) {

					//          List<String> permissions = new ArrayList<String>();
					//            permissions.add("email");

					session.openForRead(new Session.OpenRequest(activity)
					//                .setPermissions(permissions)
					.setCallback(mFacebookCallback));
				} else {
					Session.openActiveSession(activity, true, mFacebookCallback);
				}
			}
			//publishFeedDialog();
			//Log.i("HARSH", "publishFeedDialog");
			// Fallback. For example, publish the post using the Feed Dialog
		}

	}

	private  Session.StatusCallback mFacebookCallback = new Session.StatusCallback() {
	    @Override
	    public void
	    call(final Session session, final SessionState state, final Exception exception) {

	        if (state.isOpened()) {
	            String facebookToken = session.getAccessToken();
	            Log.i("MainActivityFaceBook", facebookToken);
	            Request.newMeRequest(session, new Request.GraphUserCallback() {

	                @Override
	                public void onCompleted(GraphUser user,
	                        com.facebook.Response response) {
	                    publishFeedDialog();
	                }
	            }).executeAsync();
	        }
	    }
	};
	private void publishFeedDialog() {
		
		if(activity == null){
			return;
		}
			
			
		Bundle params = new Bundle();
		params.putString("name", "Bank Online Exam Practice");
		params.putString("caption", "Android app");
		params.putString("description", ServerURL.getShareAppMsg());
		params.putString("link", ServerURL.getShareLinkFacebook());
		//params.putString("picture", "https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png");

		WebDialog feedDialog = (
				new WebDialog.FeedDialogBuilder(activity,
						Session.getActiveSession(),
						params))
						.setOnCompleteListener(new OnCompleteListener() {

							@Override
							public void onComplete(Bundle values,
									FacebookException error) {
								if (error == null) {
									// When the story is posted, echo the success
									// and the post Id.
									final String postId = values.getString("post_id");
									if (postId != null) {
										Toast.makeText(activity,
												"Posted successfully ",
												Toast.LENGTH_SHORT).show();
									} else {
										// User clicked the Cancel button
									}
								} else if (error instanceof FacebookOperationCanceledException) {
									// User clicked the "x" button
								} else {
									// Generic, ex: network error
								}
							}

						})
						.build();
		feedDialog.show();
	}

    
    private  void shareOnWhatsApp(){
    	
    	Custom_ConnectionDetector cd = new Custom_ConnectionDetector(activity);
		if(!cd.isConnectingToInternet()){
			Toast.makeText(activity, "No Internet connection", Toast.LENGTH_SHORT).show();
			return;
		}
		
    	Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT,Custom_ServerURL_Params.getShareAppMsg()+ "\n "+Custom_ServerURL_Params.getShareLinkWhatsapp());
		sendIntent.setPackage("com.whatsapp");
		sendIntent.setType("text/plain");
		if(activity != null)
			activity.startActivity(sendIntent);
    }
}
