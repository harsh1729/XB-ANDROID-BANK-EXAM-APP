package com.xercesblue.onlinebankexampo;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;

public class Custom_ButtonOnTouchListener_GreenBG implements OnTouchListener {

	final View view;
	Context context;

	public Custom_ButtonOnTouchListener_GreenBG(final View view , Context context ) {
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
				//btn.setBackgroundColor(this.context.getResources().getColor(R.color.app_green_dark));
				btn.setBackgroundResource(R.drawable.bg_green_black_border);
				break;

			case MotionEvent.ACTION_UP: 
				//view.setBackgroundResource(0);
				btn.setTextColor(this.context.getResources().getColor(R.color.app_white));
				//btn.setBackgroundResource(R.drawable.bg_white_square);
				//btn.setBackgroundColor(this.context.getResources().getColor(R.color.app_green));
				btn.setBackgroundResource(R.drawable.bg_white_black_border);
				break;

			case MotionEvent.ACTION_CANCEL: 
				btn.setTextColor(this.context.getResources().getColor(R.color.app_white));
				//btn.setBackgroundResource(R.drawable.bg_white_square);
				//btn.setBackgroundColor(this.context.getResources().getColor(R.color.app_green));
				btn.setBackgroundResource(R.drawable.bg_white_black_border);
				break;

			default   :
				break;

			}
		return false;
	}

}
