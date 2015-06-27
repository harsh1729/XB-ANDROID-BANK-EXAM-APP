package com.xercesblue.onlinebankexampo;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Request;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;

public class Activity_Settings_DownloadMore extends Activity_Parent_IntertialAds {
	//private Point p;
	
	private UiLifecycleHelper uiHelper;
	DownloadXmlThread downloadThread ;

	private String deviceImei;
	private long qusNumber;
	private ProgressDialog pDialog;
	// Progress dialog type (0 - for Horizontal progress bar)
	public static final int progress_bar_type = 0; 
	Boolean canKillDownloadThread = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(this, null);
		uiHelper.onCreate(savedInstanceState);

		setContentView(R.layout.activity_settings_download_more);
		setFooterAndHeader(-1,
				getResources().getString(R.string.header_download_more));
		createDynamicControls();
		//printHashKey();
	}

	/*
	public void printHashKey() {

        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.xercesblue.onlinebankexam",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
               System.out.println("HASH KEY :" + Base64.encodeToString(md.digest(), Base64.DEFAULT));
               Log.i("HARSH", "HASH KEY :" + Base64.encodeToString(md.digest(), Base64.DEFAULT));
                       
            }
        } catch (NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

    }
	*/
	private void createDynamicControls() {

		LinearLayout l1 = (LinearLayout) findViewById(R.id.llytMainBodyDownload);
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		Point btnSize = Globals.getAppButtonSize(this);
		LayoutParams lpbtn = new LayoutParams(LayoutParams.WRAP_CONTENT, btnSize.y);// (btnWidth,
		// btnWidth)

		Point sceenSize = Globals.getScreenSize(this);
		TextView tv = new TextView(this);
		tv.setText("Need new questions for exam ?\nClick below to share on facebook first and then download more questions.");
		tv.setTextColor(this.getResources().getColor(R.color.app_black));
		tv.setTextSize(Globals.getAppFontSize(this));

		tv.setLayoutParams(lp);
		l1.addView(tv);
		
		lpbtn.gravity = Gravity.CENTER;
		lpbtn.topMargin = sceenSize.y / 10; // only for first button
		
		Button btnShare = new Button(this);
		btnShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				downloadClick();
			}
		});
		btnShare.setText("Share & Download");
		btnShare.setTypeface(null, Typeface.BOLD);
		btnShare.setTextColor(this.getResources().getColor(R.color.app_white));
		btnShare.setLayoutParams(lpbtn);
		btnShare.setTextSize(Globals.getAppFontSize_Large(this));
		btnShare.setBackgroundResource(R.drawable.bg_white_black_border);
		btnShare.setOnTouchListener(new Custom_ButtonOnTouchListener_GreenBG(btnShare,
				this));
		l1.addView(btnShare);
		
	}

	private void downloadClick(){

		Custom_ConnectionDetector cd = new Custom_ConnectionDetector(getApplicationContext());
		if(cd.isConnectingToInternet()){
			//shareOnFaceBook();
			downloadQuestions();
		}
		else{
			Log.i("HARSH", "NO INTERNET");
			Globals.showAlertDialogOneButton("Alert", "No Internet connection", this, "OK", null, true);
		}
	}

	private void downloadQuestions(){

		TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		deviceImei = telephonyManager.getDeviceId();
		DBHandler_Questions dbH = new DBHandler_Questions(this);
		qusNumber = dbH.getMaxQuestionId();

		DBHandler_Language dbH2 = new DBHandler_Language(this);		
		String langCode = dbH2.getLangCode(Globals.getAppConfig(this).selectedLangId);
		
		System.out.println(qusNumber);
		System.out.println("LAngCode is "+langCode);
		downloadThread = new DownloadXmlThread(deviceImei,qusNumber,langCode);
		try {
		downloadThread.start();
		}catch (RuntimeException e) {
		      System.out.println("** RuntimeException from main");
	    }
	}
	/*
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		int[] location = new int[2];

		btn.getLocationOnScreen(location);

		// Initialize the Point with x, and y positions
		//p = new Point();
		//p.x = location[0];
		//p.y = location[1];

	}
	*/
	private void shareOnFaceBook() {
		if (FacebookDialog.canPresentShareDialog(getApplicationContext(),
				FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
			// Publish the post using the Share Dialog
			Log.i("HARSH", "FacebookDialog");
			FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(
					this).setLink(ServerURL.getShareLinkFacebook()).setDescription("Globals.SHARE_APP_MSG").setName("Online Bank Exam Practice")
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

					session.openForRead(new Session.OpenRequest(this)
					//                .setPermissions(permissions)
					.setCallback(mFacebookCallback));
				} else {
					Session.openActiveSession(this, true, mFacebookCallback);
				}
			}
			//publishFeedDialog();
			//Log.i("HARSH", "publishFeedDialog");
			// Fallback. For example, publish the post using the Feed Dialog
		}

	}

	private Session.StatusCallback mFacebookCallback = new Session.StatusCallback() {
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
		Bundle params = new Bundle();
		params.putString("name", "Bank Online Exam Practice");
		params.putString("caption", "Android app");
		params.putString("description", ServerURL.getShareAppMsg());
		params.putString("link",ServerURL.getShareLinkFacebook());
		//params.putString("picture", "https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png");

		WebDialog feedDialog = (
				new WebDialog.FeedDialogBuilder(this,
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
										Toast.makeText(Activity_Settings_DownloadMore.this,
												"Posted successfully ",
												Toast.LENGTH_SHORT).show();
										downloadQuestions();
									} else {
										// User clicked the Cancel button
										Toast.makeText(Activity_Settings_DownloadMore.this.getApplicationContext(), 
												"Publish cancelled", 
												Toast.LENGTH_SHORT).show();
									}
								} else if (error instanceof FacebookOperationCanceledException) {
									// User clicked the "x" button
									Toast.makeText(Activity_Settings_DownloadMore.this.getApplicationContext(), 
											"Publish cancelled", 
											Toast.LENGTH_SHORT).show();
								} else {
									// Generic, ex: network error
									Toast.makeText(Activity_Settings_DownloadMore.this.getApplicationContext(), 
											"Error posting , try again", 
											Toast.LENGTH_SHORT).show();
								}
							}

						})
						.build();
		feedDialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		uiHelper.onActivityResult(requestCode, resultCode, data,
				new FacebookDialog.Callback() {
			@Override
			public void onError(FacebookDialog.PendingCall pendingCall,
					Exception error, Bundle data) {
				Log.e("Activity",
						String.format("Error: %s", error.toString()));
			}

			@Override
			public void onComplete(
					FacebookDialog.PendingCall pendingCall, Bundle data) {
				Log.i("Activity", "Success!");
				downloadQuestions();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		uiHelper.onResume();
	}

	public void killDownloadThread() {
		canKillDownloadThread = true;
	       if(downloadThread != null && downloadThread.isAlive() && !downloadThread.isInterrupted()){
	    	   downloadThread.interrupt();
	       }
	   }
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
		killDownloadThread();
	}

	public void insertQuestionsToDatabase()
	{
		//Environment.getExternalStorageDirectory().toString();
		Custom_XMLPullParserHandler parser = new Custom_XMLPullParserHandler(this);
		File file = new File( Environment.getExternalStorageDirectory().toString()+"/qusDatabase.xml" );
		try 
		{
			parser.parse(new FileInputStream(file)  );
			this.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					Toast.makeText(Activity_Settings_DownloadMore.this, "Questions downloaded successfully", Toast.LENGTH_SHORT).show();
					
				}
			});
			
		}
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		finally
		{
			if(file.delete())
			{
				System.out.println("temporary file is deleted!");
			}
		}

	}

	public Handler handler = new Handler()
	{
		@SuppressWarnings("deprecation")
		public void handleMessage(Message msg)
		{
			Bundle bundle = msg.getData();
			if(bundle.getString("percentage") != null)
			{

				String returnVal = bundle.getString("percentage");

				// setting progress percentage
				pDialog.setProgress(Integer.parseInt(returnVal));

			}
			else if(bundle.getString("showDialog") != null)
			{
				if(bundle.getString("showDialog").equals("true"))
				{
					showDialog(progress_bar_type);
					pDialog.setProgress(0);					
				}
				else if(bundle.getString("showDialog").equals("false"))
				{
					//System.out.println("Thread state:"+downloadThread.getState());
					dismissDialog(progress_bar_type);
				}
			}
			else if(bundle.getString("insertingQuestions") != null)
			{
				pDialog.setMessage("Adding downloaded questions. Please wait...");
			}

		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case progress_bar_type:
			pDialog = new ProgressDialog(this);
			pDialog.setMessage("Downloading questions. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setMax(100);
			pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pDialog.setCancelable(false);
			pDialog.show();
			return pDialog;
		default:
			return null;
		}
	}

	@Override
	public void onBackPressed() {
		canKillDownloadThread = true;
		super.onBackPressed();
	}
	
	class DownloadXmlThread extends Thread
	{
		String deviceImei,langCode;
		long qusNumber;
		
		public DownloadXmlThread(String imei,long qn,String lcode) 
		{
			deviceImei = imei;
			qusNumber = qn;
			langCode = lcode;
		}

		//TODO HARSH
		//private final ReentrantLock lock = new ReentrantLock();

		public void run()
		{

			//sendmessage as data to create progress bar;
			Message msg = new Message();
			Bundle bundle = new Bundle();
			bundle.putString("showDialog","true");

			msg.setData(bundle);
			handler.sendMessage(msg);

			int count;
			try 
			{
				HttpClient httpClient = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(ServerURL.getDownload_question_link(qusNumber,deviceImei,langCode,Activity_Settings_DownloadMore.this));

				HttpResponse response = httpClient.execute(httpGet);

				int lengthOfFile = (int) response.getEntity().getContentLength();


				
				/*
				  Header []valPair = response.getAllHeaders();
				for(Header key : valPair)
				{
					//System.out.println("key is: "+key+" value is :"+valPair.get(key).get(0));
					System.out.println("key is: "+key.getName()+" value is :"+key.getValue());

				}
				//int lengthOfFile = 0;
				*/

				System.out.println("length of file:"+lengthOfFile);
				
				Log.i("HARSH", "File Input is "+response.toString());
				InputStream inputStream = new BufferedInputStream(response.getEntity().getContent(),32);
				OutputStream output = new FileOutputStream( Environment.getExternalStorageDirectory().toString()+"/qusDatabase.xml" );
				byte packet[] = new byte[2];

				long total = 0;
				while ((count = inputStream.read(packet)) != -1 && !canKillDownloadThread) 
				{
					//System.out.println("downloaded! Count is"+count);
					total += count;
					// writing data to file
					output.write(packet, 0, count);

					//sendmessage as data for update progress bar;
					Message msg1 = new Message();
					Bundle bundle1 = new Bundle();
					bundle1.putString("percentage", ""+(int)((total*100)/lengthOfFile));

					msg1.setData(bundle1);
					handler.sendMessage(msg1);
				}

				// flushing output
				output.flush();

				// closing streams
				output.close();
				inputStream.close();

				if(!canKillDownloadThread){
					Message msg2 = new Message();
					Bundle bundle2 = new Bundle();
					msg2 = new Message();
					bundle2 = new Bundle();
					bundle2.putString("insertingQuestions","true");

					msg2.setData(bundle2);
					handler.sendMessage(msg2);
				
					insertQuestionsToDatabase();
				}
				//sendmessage as data for dismiss progress bar;
				Message msg3 = new Message();
				Bundle bundle3 = new Bundle();
				msg3 = new Message();
				bundle3 = new Bundle();
				bundle3.putString("showDialog","false");

				msg3.setData(bundle3);
				handler.sendMessage(msg3);


			} 
			catch (ClientProtocolException e) 
			{
				Log.i("HARSH", "Exception in downloading 1");
				e.printStackTrace();
			} 
			catch (IOException e) 
			{
				Log.i("HARSH", "Exception in downloading 2");
				e.printStackTrace();
			}
			catch (Exception e) 
			{
				Log.i("HARSH", "Exception in downloading 3");
				e.printStackTrace();
			}

		}

	}

	public void noNewQuestions(){
		Globals.showAlertDialogOneButton("Alert", "No new questions on server , please try after some days",this,"OK", null, true);
		dismissDialog(progress_bar_type);
	}

}
