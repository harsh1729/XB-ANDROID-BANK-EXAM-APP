package com.xercesblue.onlinebankexampo;

import java.util.ArrayList;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Custom_ArrayAdaptor_Favourites_Questions_List extends ArrayAdapter<Object_Row_Item_Questions_List>  {

	private Activity context;
	private int resourceId;
	private ArrayList<Object_Row_Item_Questions_List> data = null;
	
	
	public Custom_ArrayAdaptor_Favourites_Questions_List(Activity context,int resourceId ,ArrayList<Object_Row_Item_Questions_List> data){
		super(context, resourceId,data);
		
		this.context = context;
		this.resourceId = resourceId;
		this.data = data;
	}
	
	@Override
	public View getView(int position,View convertView,ViewGroup parent){
		
		View row = convertView;
		int textWidth = Globals.getScreenSize(context).x *3/4;
		int btnWidth =  Globals.getScreenSize(context).x/12;
		Object_Row_Item_Questions_List item = data.get(position);
		//if(row == null)
        {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(resourceId, parent, false);
              
            /*
            for(int i=1;i<=item.noOfOptions;i++){
             	ImageView imgOptionBlock = new ImageView(context);
             	imgOptionBlock.setBackgroundResource(0);
    				Options options = new BitmapFactory.Options();
    				options.inScaled = false;
    				
    				if(i == item.correctOption ){
    					imgOptionBlock.setImageResource(R.drawable.question_option_selected);
    				}else{
    					imgOptionBlock.setImageResource(R.drawable.question_option_unselected);
    				}
    				LayoutParams imageParams = new LayoutParams(btnWidth,btnWidth);
    				imageParams.leftMargin =5;
    				imgOptionBlock.setLayoutParams(imageParams);
    				imgOptionBlock.setPadding(0, 0, 0, 0);
    				((ViewGroup) row).addView(imgOptionBlock);

             }*/
        }
		
		
		 
		ImageView imgtype = (ImageView)row.findViewById(R.id.imgType);
		imgtype.getLayoutParams().height = btnWidth;
		imgtype.getLayoutParams().width = btnWidth;
		imgtype.setImageResource(R.drawable.question_favorite_selected);
		
		
		
		TextView txtQues = (TextView)row.findViewById(R.id.txtQuesNo);
		txtQues.setTextSize(Globals.getAppFontSize(context));
        txtQues.setText(item.quesText);
        txtQues.setMaxLines(1);
        txtQues.getLayoutParams().width = textWidth;
        txtQues.getLayoutParams().height = btnWidth;
        return row;
	}
}

