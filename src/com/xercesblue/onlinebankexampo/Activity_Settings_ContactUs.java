package com.xercesblue.onlinebankexampo;



import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_Settings_ContactUs extends Activity_Parent {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings_contact_us);
		setFooterAndHeader(-1,getResources().getString(R.string.header_contect_us));
		initializeControls();	
	}

	private void initializeControls(){
		TextView tv1=(TextView)findViewById(R.id.txtCompanyName);
		TextView tv2=(TextView)findViewById(R.id.txtPhone);
		TextView tv3=(TextView)findViewById(R.id.txtEmail);
		TextView tv4=(TextView)findViewById(R.id.txtWebsite);
		int fontSize = Globals.getAppFontSize_Large(this);
		tv1.setTextSize( fontSize);
		tv2.setTextSize( fontSize);
		tv3.setTextSize( fontSize);
		tv4.setTextSize( fontSize);
		
		Button btnSendMessage = (Button)findViewById(R.id.btnSendMesage);
		Point btnSize = Globals.getAppButtonSize(this);
		btnSendMessage.getLayoutParams().width = btnSize.x;
		btnSendMessage.getLayoutParams().height = btnSize.y;
		btnSendMessage.setOnTouchListener(new Custom_ButtonOnTouchListener_GreenBG(btnSendMessage, this));
		btnSendMessage.setTextSize(Globals.getAppFontSize(this));
		
		SharedPreferences prefs = this.getSharedPreferences("appregister", 0);
		 
		 if(prefs.getBoolean("register_done", false)){
			 EditText txtEmail = (EditText)findViewById(R.id.edtEmail);
			 txtEmail.setText(prefs.getString("register_email", ""));
			 EditText txt = (EditText)findViewById(R.id.edtMessage);
			 txt.requestFocus();
		 }else{
			 DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// Write your code here to execute after dialog closed
						dialog.cancel();
						Intent i = new Intent(Activity_Settings_ContactUs.this, Activity_Settings_Registration.class);
						startActivity(i);
					}
				};
			 Globals.showAlertDialog("Alert", "Dear user, register with us and you will have access to free study material and Current affairs PDF.",this, "Register Now", listener, "Later", null, false);
		 }
	}
	
	public void onClickSendMessage(View v){
		if(!validateMessage())
			return;
 
		TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		String deviceImei = telephonyManager.getDeviceId();
		EditText txtMsg = (EditText)findViewById(R.id.edtMessage);
		EditText txtEmail = (EditText)findViewById(R.id.edtEmail);
		new PostData().execute(deviceImei,Globals.GCM_REG_ID,txtEmail.getText().toString(), txtMsg.getText().toString());

   	 
		txtEmail.setText("");
		txtMsg.setText("");
   	 	Toast.makeText(this, "Message sent successfully", Toast.LENGTH_SHORT).show();
	}
	
	private Boolean validateMessage(){
		Custom_ConnectionDetector cd = new Custom_ConnectionDetector(getApplicationContext());

		if(!cd.isConnectingToInternet()){
			Globals.showAlertDialogOneButton("Alert", "No Internet connection", this, "OK", null, true);
			return false;
		}
		EditText txtE = (EditText)findViewById(R.id.edtEmail);
		if(txtE.getText().toString().trim().equals("")){
			Globals.showAlertDialogOneButton("Error", "Please enter your email address", this, "OK", null, true);
			return false;
		}
		
		EditText txt = (EditText)findViewById(R.id.edtMessage);
		if(txt.getText().toString().trim().equals("")){
			Globals.showAlertDialogOneButton("Error", "Please write a message", this, "OK", null, true);
			return false;
		}
		return true;
	}
	
	class PostData extends AsyncTask<String, Void, String> {
		@Override
		    protected String doInBackground(String... params) {
		         try{
		        	//http://xercesblue.in/OnlineXamServer/liquid_data/userreview/myreview.php?deviceId=222222222&msg=jaspalsingh
		             String address = ServerURL.getContactUs_link(Activity_Settings_ContactUs.this);
		 			
		 			HttpClient client = new DefaultHttpClient();
		 			HttpPost post = new HttpPost(address);
		 			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		 			pairs.add(new BasicNameValuePair("deviceId", params[0]));
		 			pairs.add(new BasicNameValuePair("gcmId", params[1]));
		 			pairs.add(new BasicNameValuePair("email", params[2]));
		 			pairs.add(new BasicNameValuePair("msg", params[3]));
		 			post.setEntity(new UrlEncodedFormEntity(pairs));
		 			//HttpResponse response = client.execute(post);
		 			client.execute(post);
		 			
		        	 
		         }
		         catch (UnsupportedEncodingException e) {
		 			System.out.println("UnsupportedEncodingException in sending message " + e.getMessage());
		 			e.printStackTrace();
		 		}
		 		catch(Exception ex)
		         {
		              System.out.println("Exception in sending message " + ex.getMessage());
		         }
		        return null;
		    }

		
		}
}
