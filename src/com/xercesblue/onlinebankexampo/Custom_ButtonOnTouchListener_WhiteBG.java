package com.xercesblue.onlinebankexampo;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;

public class Custom_ButtonOnTouchListener_WhiteBG implements OnTouchListener {

	final View view;
	Context context;

	public Custom_ButtonOnTouchListener_WhiteBG(final View view , Context context ) {
		super();
		this.view = view;
		this.context = context;
	}

	public boolean onTouch(final View view, final MotionEvent motionEvent) {
		Button btn =  (Button)view;
		if(btn!= null)
			switch (motionEvent.getAction()) {

			case MotionEvent.ACTION_DOWN:      	  
				btn.setBackgroundResource(R.drawable.bg_white_black_border);
				break;

			case MotionEvent.ACTION_UP: 				
				btn.setBackgroundResource(R.drawable.bg_green_black_border);
				break;

			case MotionEvent.ACTION_CANCEL: 
				btn.setBackgroundResource(R.drawable.bg_green_black_border);
				break;

			default   :
				break;

			}
		return false;
	}

}
