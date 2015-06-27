package com.xercesblue.onlinebankexampo;

import java.util.ArrayList;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class Custom_ArrayAdaptor_Settings_Home extends ArrayAdapter<Object_Row_Item_Options>  {

	Activity context;
	int resourceId;
	ArrayList<Object_Row_Item_Options> data = null;
	
	public Custom_ArrayAdaptor_Settings_Home(Activity context,int resourceId ,ArrayList<Object_Row_Item_Options> data){
		super(context, resourceId,data);
		
		this.context = context;
		this.resourceId = resourceId;
		this.data = data;
	}
	
	static class ViewHolder{
		ImageView imgIcon;
		TextView txtTitle;
	}
	@Override
	public View getView(int position,View convertView,ViewGroup parent){
		
		View row = convertView;
		ViewHolder holder = null;
		if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(resourceId, parent, false);
            
            holder = new ViewHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.imgRowIcon);
            holder.txtTitle = (TextView)row.findViewById(R.id.txtRowText);
            
            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }
        
		Object_Row_Item_Options item = data.get(position);
        holder.txtTitle.setText(item.text);
        holder.txtTitle.setTextSize(Globals.getAppFontSize(context));
        holder.imgIcon.setImageBitmap(item.iconBitmap);
        
		int rowHeight =parent.getHeight()/10;
		
		row.getLayoutParams().height = rowHeight;
		
        return row;
	}
}
