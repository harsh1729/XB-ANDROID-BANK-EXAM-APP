package com.xercesblue.onlinebankexampo;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class Activity_Current_GK_Read extends Activity_Parent_Banner_Ads {

	public static InputStream xmlStream;
	DownloadCurrentGKXml downloadThread = null;
	Boolean firstTime = true;
	int currentPageNo;
	Boolean nextDisabled = false;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_current_gk_read);
		setFooterAndHeader(-1,
				getResources().getString(R.string.header_current_gk_read));
		
		currentPageNo = 1;
		downloadGkForPage(currentPageNo);
		resizeImageButtons(true);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (firstTime) {
			resizeImageButtons(false);
			firstTime = false;
		}

	}
	
	void downloadGkForPage(int pageNo) {
		Custom_ConnectionDetector cd = new Custom_ConnectionDetector(
				this);

		LinearLayout parentLayout = (LinearLayout) findViewById(R.id.llytMainBodyExamAlertDetail);
		parentLayout.removeAllViews();
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);

		TextView txtWaitMsg = new TextView(this);
		txtWaitMsg.setText("Downloading current GK page " + pageNo
				+ ". Please wait .....");
		txtWaitMsg.setTextSize(Globals.getAppFontSize_Large(this));
		txtWaitMsg
				.setTextColor(this.getResources().getColor(R.color.app_black));
		txtWaitMsg.setPadding(10, 0, 10, 10);
		txtWaitMsg.setLayoutParams(lp);
		parentLayout.addView(txtWaitMsg);

		// Check if Internet present
		if (cd.isConnectingToInternet()) {
			DBHandler_Language dbH = new DBHandler_Language(this);
			Object_AppConfig appConfig = Globals.getAppConfig(this);

			String langCode = dbH.getLangCode(appConfig.selectedLangId);

			if (downloadThread != null) { // && downloadThread.isAlive()
				// downloadThread.interrupt();
				downloadThread = null;
			}

			downloadThread = new DownloadCurrentGKXml(Globals.APP_ID, langCode,
					pageNo, this);
			downloadThread.start();
		} else {
			Globals.showAlertDialogError(this, "No Internet Connectivity");
			parentLayout.removeAllViews();
		}
	}

private void resizeImageButtons(Boolean hide) {

		ImageButton imgBtnNext = (ImageButton) findViewById(R.id.imgBtnNext);
		ImageButton imgBtnPrev = (ImageButton) findViewById(R.id.imgBtnPrev);
		TextView txtPageNo = (TextView) findViewById(R.id.txtPageNo);
		if (hide) {
			imgBtnNext.setVisibility(View.INVISIBLE);
			imgBtnPrev.setVisibility(View.INVISIBLE);
			txtPageNo.setVisibility(View.INVISIBLE);
			imgBtnPrev.setEnabled(false);
			txtPageNo.setTextSize(Globals.getAppFontSize_Large(this));
		} else {
			imgBtnNext.setVisibility(View.VISIBLE);
			imgBtnPrev.setVisibility(View.VISIBLE);
			txtPageNo.setVisibility(View.VISIBLE);

			RelativeLayout rlytNextPrev = (RelativeLayout) findViewById(R.id.rlytNextPrev);

			int imageBtnSizeNextPrev = rlytNextPrev.getHeight() * 7 / 10;

			imgBtnNext.getLayoutParams().height = imageBtnSizeNextPrev;
			imgBtnNext.getLayoutParams().width = imageBtnSizeNextPrev;
			imgBtnPrev.getLayoutParams().height = imageBtnSizeNextPrev;
			imgBtnPrev.getLayoutParams().width = imageBtnSizeNextPrev;

		}

	}
	
	ArrayList<String> sortedKeys(Iterator<?> keys) {

		ArrayList<Integer> listInt = new ArrayList<Integer>();
		ArrayList<String> listString = new ArrayList<String>();

		while (keys.hasNext()) {
			String key = (String) keys.next();
			try {
				listInt.add(Integer.parseInt(key));
			} catch (NumberFormatException e) {
				listString.add(key);
			}
		}

		Collections.sort(listInt);

		for(Integer keyInt : listInt){
			listString.add(keyInt.intValue()+"");
		}
		
		return listString;
	}

	private void showJSonGKData(JSONObject jsonObj, int pageNo) {

		LinearLayout parentLayout = (LinearLayout) findViewById(R.id.llytMainBodyExamAlertDetail);
		ImageButton imgBtnNext = (ImageButton) findViewById(R.id.imgBtnNext);

		parentLayout.removeAllViews();

		// Iterator<?> keys = jsonObj.keys();
		ArrayList<String> keys = sortedKeys(jsonObj.keys());

		if (jsonObj.has("errorNoMorePages")) {
			imgBtnNext.setEnabled(false);
		} else {
			imgBtnNext.setEnabled(true);
		}

		for (String key : keys) {
			try {
				Log.i("HARSH","KEY IS "+key);
				String text = jsonObj.getString(key);

				
				LinearLayout llH = new LinearLayout(this);
				LayoutParams lpLL = new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT);
				llH.setOrientation(LinearLayout.HORIZONTAL);
				llH.setPadding(5, 15, 10, 15);
				
				llH.setLayoutParams(lpLL);
				
				Point screenSize = Globals.getScreenSize(this);
				LayoutParams lpImage = new LayoutParams(screenSize.x/12,
						screenSize.x/12);
				ImageView img = new  ImageView(this);
				if(key.trim().equalsIgnoreCase("errorNoMorePages")){
					img.setImageResource(R.drawable.circle_red);
				}else{
					img.setImageResource(R.drawable.circle_green);
				}
				img.setLayoutParams(lpImage);
				llH.addView(img);
				

				LayoutParams lpText = new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT);
				lpText.setMargins(10, 0, 0, 0);
				TextView txtMsg = new TextView(this);
				txtMsg.setText(text.trim());
				txtMsg.setTextSize(Globals.getAppFontSize_Large(this));
				txtMsg.setTextColor(this.getResources().getColor(
						R.color.app_white));
				
				txtMsg.setLayoutParams(lpText);
				llH.addView(txtMsg);
				
				parentLayout.addView(llH);

				TextView txtPageNo = (TextView) findViewById(R.id.txtPageNo);
				// txtPageNo.setTextSize(Globals.getAppFontSize_Large(this));
				txtPageNo.setText("Page " + pageNo);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public void onClickNext(View v) {
		if (currentPageNo == 1) {
			ImageButton imgBtnPrev = (ImageButton) findViewById(R.id.imgBtnPrev);
			imgBtnPrev.setEnabled(true);
		}
		
		TextView txtPageNo = (TextView) findViewById(R.id.txtPageNo);
		txtPageNo.setText("Loading...");
		
		downloadGkForPage(++currentPageNo);
	}

	public void onClickPrev(View v) {
		if (currentPageNo > 1) {

			if (currentPageNo == 2) {
				ImageButton imgBtnPrev = (ImageButton) findViewById(R.id.imgBtnPrev);
				imgBtnPrev.setEnabled(false);
			}

			TextView txtPageNo = (TextView) findViewById(R.id.txtPageNo);
			txtPageNo.setText("Loading...");
			
			downloadGkForPage(--currentPageNo);
		}
	}

	class DownloadCurrentGKXml extends Thread {
		Context context;
		private int AppId;
		private int pageNo;
		private String langCode;
		private JSONObject objJson;

		public DownloadCurrentGKXml(int AppId, String langCode, int pageNo,
				Context context) {
			this.context = context;
			this.AppId = AppId;
			this.pageNo = pageNo;
			this.langCode = langCode;
			Log.i("HARSH", "In DownloadCurrentGKXml constructor");
		}

		public void run() {
			try {
				HttpClient httpClient = new DefaultHttpClient();
				// http://xercesblue.in/onlinexamserver/liquid_data/CurrentGKCenter/getLatestGkUpdates.php?pageno=1&langCode=en&AppId=1
				String date = Activity_Current_GK_Type_Select.getSelectedDate();
				String month = Activity_Current_GK_Type_Select.getSelectedMonth();
				String year = Activity_Current_GK_Type_Select.getSelectedYear();
				Log.i("HARSH",
						"URL is : "+ ServerURL.getCurrent_GK_Read_link(pageNo,langCode,AppId,date,month,year,Activity_Current_GK_Read.this));
				
				HttpGet httpGet = new HttpGet(ServerURL.getCurrent_GK_Read_link(pageNo,langCode,AppId,date,month,year,Activity_Current_GK_Read.this));
				HttpResponse response = httpClient.execute(httpGet);

				String jsonResponce = Globals
						.convertInputStreamToString(response.getEntity()
								.getContent());

				Log.i("HARSH", "Responce is : " + jsonResponce);

				try {

					objJson = new JSONObject(jsonResponce);
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Log.i("HARSH", "showJSonGKData called ");
							showJSonGKData(objJson, pageNo);
						}
					});

				} catch (Exception ex) {
					Log.e("HARSH", "Could not parse malformed JSON: \""
							+ jsonResponce + "\"");
				}

				System.out.println("downloaded!");

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

}
