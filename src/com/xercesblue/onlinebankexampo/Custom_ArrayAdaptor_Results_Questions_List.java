package com.xercesblue.onlinebankexampo;

import java.util.ArrayList;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class Custom_ArrayAdaptor_Results_Questions_List extends ArrayAdapter<Object_Row_Item_Questions_List>  {

	private Activity context;
	private int resourceId;
	private ArrayList<Object_Row_Item_Questions_List> data = null;
	private int btnWidth = 40;
	
	public Custom_ArrayAdaptor_Results_Questions_List(Activity context,int resourceId ,ArrayList<Object_Row_Item_Questions_List> data,int btnWidth){
		super(context, resourceId,data);
		
		this.context = context;
		this.resourceId = resourceId;
		this.data = data;
		this.btnWidth = btnWidth;
	}
	
	@Override
	public View getView(int position,View convertView,ViewGroup parent){
		
		View row = convertView;
		Object_Row_Item_Questions_List item = data.get(position);
		//if(row == null)
        {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(resourceId, parent, false);
              
            for(int i=1;i<=item.noOfOptions;i++){
             	ImageView imgOptionBlock = new ImageView(context);
             	imgOptionBlock.setBackgroundResource(0);
    				Options options = new BitmapFactory.Options();
    				options.inScaled = false;
    				
    				if(i == item.correctOption ){
    					imgOptionBlock.setImageResource(R.drawable.question_option_selected);
    				}else if(i == item.selectedOptionNo ){
    					imgOptionBlock.setImageResource(R.drawable.question_option_wrong);
    				}else{
    					imgOptionBlock.setImageResource(R.drawable.question_option_unselected);
    				}
    				LayoutParams imageParams = new LayoutParams(btnWidth,btnWidth);
    				imageParams.leftMargin =5;
    				imgOptionBlock.setLayoutParams(imageParams);
    				imgOptionBlock.setPadding(0, 0, 0, 0);
    				((ViewGroup) row).addView(imgOptionBlock);

             }
        }
		
		
		 
		ImageView imgtype = (ImageView)row.findViewById(R.id.imgType);
		imgtype.getLayoutParams().height = btnWidth;
		imgtype.getLayoutParams().width = btnWidth;
		if(item.correctOption == item.selectedOptionNo)
			imgtype.setImageResource(R.drawable.question_type_correct);
		else if(item.selectedOptionNo < 1){
			imgtype.setImageResource(R.drawable.question_type_unattempted);
		}else {
			imgtype.setImageResource(R.drawable.question_type_wrong);
		}
		
		
		TextView txtQues = (TextView)row.findViewById(R.id.txtQuesNo);
		txtQues.setTextSize(Globals.getAppFontSize(context));
        txtQues.setText("Q. "+item.quesNo+" ");
        txtQues.getLayoutParams().width = btnWidth*2;
        return row;
	}
}

