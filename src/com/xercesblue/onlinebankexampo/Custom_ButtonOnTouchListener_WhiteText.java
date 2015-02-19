package com.xercesblue.onlinebankexampo;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;

public class Custom_ButtonOnTouchListener_WhiteText implements OnTouchListener {

	final View view;
	Context context;

	public Custom_ButtonOnTouchListener_WhiteText(final View view , Context context ) {
		super();
		this.view = view;
		this.context = context;
	}

	public boolean onTouch(final View view, final MotionEvent motionEvent) {
		Button btn =  (Button)view;
		if(btn!= null)
			switch (motionEvent.getAction()) {

			case MotionEvent.ACTION_DOWN:      	  
				btn.setTextColor(this.context.getResources().getColor(R.color.app_white));
				break;

			case MotionEvent.ACTION_UP: 				
				btn.setTextColor(this.context.getResources().getColor(R.color.app_black));
				break;

			case MotionEvent.ACTION_CANCEL: 
				btn.setTextColor(this.context.getResources().getColor(R.color.app_black));
				break;

			default   :
				break;

			}
		return false;
	}

}
