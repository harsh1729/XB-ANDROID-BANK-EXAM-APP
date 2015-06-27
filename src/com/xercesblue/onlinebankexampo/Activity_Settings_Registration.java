package com.xercesblue.onlinebankexampo;

import java.util.HashMap;
import java.util.Map;

import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class Activity_Settings_Registration extends Activity_Parent implements TextWatcher {

	EditText edtName ;
	EditText edtCity ;
	EditText edtMobile ;
	EditText edtEmail ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings_registration);
		setFooterAndHeader(-1,
				getResources().getString(R.string.header_share_register));
		
		Button btn=(Button)findViewById(R.id.btnRegister);
		Point btnSize = Globals.getAppButtonSize(this);
		btn.getLayoutParams().width = btnSize.x;
		btn.getLayoutParams().height = btnSize.y;
		btn.setTextSize( Globals.getAppFontSize_Large(this));
		btn.setBackgroundResource(R.drawable.bg_white_black_border);
		btn.setTextColor(this.getResources().getColor(R.color.app_white));
		btn.setOnTouchListener(new Custom_ButtonOnTouchListener_GreenBG(btn, this));
		
		 edtName = (EditText)findViewById(R.id.edtName);
		 edtCity = (EditText)findViewById(R.id.edtCity);
		 edtMobile = (EditText)findViewById(R.id.edtPhone);
		 edtEmail = (EditText)findViewById(R.id.edtEmail);
		 
		 SharedPreferences prefs = this.getSharedPreferences("appregister", 0);
		 
		 if(prefs.getBoolean("register_done", false)){
			edtName.setText(prefs.getString("register_name", ""));
			edtMobile.setText(prefs.getString("register_mobile", ""));
			edtEmail.setText(prefs.getString("register_email", ""));
			edtCity.setText(prefs.getString("register_city", ""));
			
			Globals.showAlertDialogOneButton("Already Registered", "Change your mobile number or email address to register again.", this, "OK", null, false);

			btn.setEnabled(false);
			//edtName.addTextChangedListener(this);
			//edtCity.addTextChangedListener(this);
			edtMobile.addTextChangedListener(this);
			edtEmail.addTextChangedListener(this);
		 }
	}
	
	
	public void btnRegisterClick(View v){
		
		if(validate()){
			Button btn = (Button)findViewById(R.id.btnRegister);
			btn.setEnabled(false);
			String url = ServerURL.getRegistration_link(Activity_Settings_Registration.this);
			StringRequest postRequest = new StringRequest(Request.Method.POST, url, 
			    new Response.Listener<String>() 
			    {
			        @Override
			        public void onResponse(String response) {
			            // response
			        	 Log.i("HARSH","Response "+ response.toString());
			        	 Toast.makeText(Activity_Settings_Registration.this, "Successfully Registered!", Toast.LENGTH_SHORT).show();
			        	 edtName.setText("");
			        	 edtMobile.setText("");
			        	 edtCity.setText("");
			        	 edtEmail.setText("");
			        }
			    }, 
			    new Response.ErrorListener() 
			    {
			         @Override
			         public void onErrorResponse(VolleyError error) {
			             // error
			             //Log.d("HARSH","Error Response "+ error.networkResponse.toString());
			             Toast.makeText(Activity_Settings_Registration.this, "Error while submission , please try again.", Toast.LENGTH_SHORT).show();
			       }
			    }
			) {     
			    @Override
			    protected Map<String, String> getParams() 
			    {  
			            Map<String, String>  params = new HashMap<String, String>();  
			            params.put("name", edtName.getText().toString());  
			            params.put("mobile", edtMobile.getText().toString());
			            params.put("email", edtEmail.getText().toString());
			            params.put("city", edtCity.getText().toString());
			             
			            return params;  
			    }
			};
			Globals.getRequestQueue(this).add(postRequest);
			
			SharedPreferences prefs = this.getSharedPreferences("appregister", 0);
			
			SharedPreferences.Editor editor = prefs.edit();
			if (editor != null) {
				editor.putBoolean("register_dontshowagain", true);
				editor.putBoolean("register_done", true);
				editor.putString("register_name", edtName.getText().toString());
				editor.putString("register_mobile", edtMobile.getText().toString());
				editor.putString("register_email", edtEmail.getText().toString());
				editor.putString("register_city", edtCity.getText().toString());
				editor.commit();
			}
			
		}
	}
	
	Boolean  validate(){	
		Custom_ConnectionDetector cd = new Custom_ConnectionDetector(getApplicationContext());
        
		String errorMsg = "";
        // Check if Internet present
        if (!cd.isConnectingToInternet())
        {
        	errorMsg = "No Internet Connectivity";
        }
        else if(edtName.getText().toString().trim().equals("")){
			
			errorMsg = "Please enter your name. ";
		}else if(edtCity.getText().toString().trim().equals("")){
			
			errorMsg = "Please enter the name of city you live in. ";
		}else if(edtMobile.getText().toString().trim().equals("")){
			errorMsg = "Please enter your mobile number.";
		}else if(edtMobile.getText().length()<10){
			errorMsg = "Please enter a valid mobile number.";
		}else if(edtEmail.getText().toString().trim().equals("")){
			errorMsg = "Please enter your email address.";
		}else if(!edtEmail.getText().toString().trim().contains("@")){
			errorMsg = "Please enter a valid email address.";
		}
		
		if(!errorMsg.equals("")){
			Globals.showAlertDialogOneButton("Error", errorMsg, this, "OK", null, false);
			return false;
		}
		
		return true;
	}
	
	
	public void afterTextChanged(Editable s) {
		Button btn=(Button)findViewById(R.id.btnRegister);
		btn.setEnabled(true);
	}
	
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		
	}  
	
}
