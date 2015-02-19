package com.xercesblue.onlinebankexampo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class Activity_Parent extends Activity {


	public static Boolean comingFromPushMessage = false;


	public void footerBtnClick(View v){

		Class<?> nextClass = null;

		switch (v.getId()) {
		case R.id.imgBtnFooterHome:
			nextClass = Activity_Home.class;
			break;
		case R.id.imgBtnFooterResult:
			nextClass = Activity_Results_List.class;
			break;
		case R.id.imgBtnFooterSettings:
			nextClass = Activity_Settings.class;
			break;
		case R.id.imgBtnFooterTutorial:
			nextClass = Activity_Tutorial.class;
			break;

		default:
			break;
		}

		if(nextClass != null){
			if(this.getClass() != nextClass){
				Intent i = new Intent(this,nextClass);
				startActivity(i);
			}
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


	}

	@Override
	protected void onResume() {
		super.onResume();
		if(comingFromPushMessage){
			comingFromPushMessage = false;
			
			Globals.showAlertDialogOneButton("Message", GCMIntentService.pushMessage, this, "OK", null, false);
		}
	}
	protected void setFooterAndHeader(int footerbtnId,String headerText){
		if(footerbtnId != -1)
		{
			View v = this.findViewById(footerbtnId);
			if(v!= null){
				v.setSelected(true);
				Log.i("FOOTER", "Set Selected");
			}else{
				Log.i("FOOTER", "Null Button");
			}
		}

		TextView txtH = (TextView)this.findViewById(R.id.txtHeader);
		if(txtH!=null)
			txtH.setText(headerText);

	}

}
