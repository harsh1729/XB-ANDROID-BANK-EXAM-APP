package com.xercesblue.onlinebankexampo;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class Custom_ViewOnTouchListener_WhiteBG implements OnTouchListener {

	final View view;
	Context context;
	  public Custom_ViewOnTouchListener_WhiteBG(final View view , Context context ) {
	    super();
	    this.view = view;
	    this.context = context;
	  }

	  public boolean onTouch(final View view, final MotionEvent motionEvent) {
		  Log.i("HARSH", "Event -- > "+motionEvent.getAction());
		  switch (motionEvent.getAction()) {
          case MotionEvent.ACTION_DOWN: 
        	   //view.getBackground().setColorFilter(R.color.app_green,PorterDuff.Mode.SRC_ATOP);
        	  //view.setBackgroundResource(R.drawable.homescreen_tutorial);
        	 // view.setBackgroundColor(context.getResources().getColor(R.color.app_green));
        	  view.setBackgroundResource(R.drawable.bg_white_rounded);
        	  break;
          
          case MotionEvent.ACTION_UP: 
        	  view.setBackgroundResource(0);
        	  //view.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
              break;
          case MotionEvent.ACTION_CANCEL: 
        	  view.setBackgroundResource(0);
        	  //view.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
              break;
          default   :
        	  //view.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
        	  //view.setBackgroundResource(0);
        	  break;
          
      }
	    return false;
	  }

}
