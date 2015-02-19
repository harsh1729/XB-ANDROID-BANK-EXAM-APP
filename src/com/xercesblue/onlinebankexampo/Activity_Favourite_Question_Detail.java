package com.xercesblue.onlinebankexampo;

import java.util.ArrayList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Activity_Favourite_Question_Detail extends Activity_Parent {

	static long quesId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favourite_question_detail);
		setFooterAndHeader(-1,getResources().getString(R.string.header_favourite));	
		showQuestion();
	}

	private void showQuestion(){
		DBHandler_Questions dbH = new DBHandler_Questions(this);
		Object_Question objQues = dbH.getQuestionWithId(quesId);
		
		TextView txtQ = (TextView)findViewById(R.id.txtQuestion);
		ImageView imgQ = (ImageView)findViewById(R.id.imgViewQuestion);
		txtQ.setTextSize(Globals.getAppFontSize(this));
		txtQ.setText("Q. "+objQues.question);
		if(objQues.image != null){
			Bitmap bm = BitmapFactory.decodeByteArray(objQues.image, 0,objQues.image.length);
			imgQ.setImageBitmap(bm);
		}else{
			imgQ.setImageDrawable(null);
		}
		
		ListView lv = (ListView)findViewById(R.id.listOptions);
		ArrayList<Object_Row_Item_Options> data = new ArrayList<Object_Row_Item_Options>();

		
		for(Object_Options obj:objQues.arrayOptions)
		{
			Object_Row_Item_Options item=new Object_Row_Item_Options();
			item.text = obj.optionText;
			if(obj.image!=null){
				Bitmap bit = BitmapFactory.decodeByteArray(obj.image, 0,
						obj.image.length);
				item.iconBitmap = bit;
			}else{
				item.iconBitmap = null;
			}
			item.isCorrect = false;
			item.isWrong = false;
			
			if(objQues.correctOption == obj.optionNo){
				item.isCorrect = true;
			}
			data.add(item);

		}

		Custom_ArrayAdaptor_Result_Question_Options adp=new Custom_ArrayAdaptor_Result_Question_Options(this,R.layout.row_listview_options_question,data); 
		lv.setAdapter(adp);
		
	}
	
}
