package com.xercesblue.onlinebankexampo;

import android.content.Context;
import android.widget.Button;
import android.widget.LinearLayout;

public class Custom_Button_ListView_Container extends Button {


	public LinearLayout  llytCatContainer;
	public Boolean isHidden;
	public Custom_Button_ListView_Container(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public Custom_Button_ListView_Container(Context context, Boolean isHidden) {
		super(context);
		this.isHidden = isHidden;
	}

}
