package com.xercesblue.onlinebankexampo;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class Activity_ExamAlert extends Activity_Parent 
{
	ListView listView ;
	public static InputStream xmlStream;
	DownloadXamAlertXml downloadThread = null;
	TextView txtMessage;
	static List<Object_ExamAlert> listExams = null;
	
	
	//public static final int progress_bar_type = 0; 
	
	/*
	public Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			System.out.println("Hanlder is called!");
			txtMessage.setVisibility(View.GONE);
			
			//loadData();
		}
	};*/
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exam_alert);
		setFooterAndHeader(-1,getResources().getString(R.string.header_exam_alert));	

		listView = (ListView) findViewById(R.id.listExamAlert);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int pos,
					long id) {
				
				moveToExamAlertDetail(pos);
			}
		});


		Custom_ConnectionDetector cd = new Custom_ConnectionDetector(getApplicationContext());
        
		txtMessage = (TextView)findViewById(R.id.txtWaitMessage);
		txtMessage.setTextSize(Globals.getAppFontSize_Large(this));
        // Check if Internet present
        if (cd.isConnectingToInternet())
        {
        	downloadThread = new DownloadXamAlertXml();
        	downloadThread.start();
        	txtMessage.setVisibility(View.VISIBLE);
        }else{
        	Globals.showAlertDialogError(this, "No Internet Connectivity");
        	txtMessage.setVisibility(View.GONE);
        }
        
	}

	public void moveToExamAlertDetail(int rowNo){
		
		Intent it=new Intent(this,Activity_ExamAlertDetail.class);
		Activity_ExamAlertDetail.ExamDetails = listExams.get(rowNo);
		System.out.print("detail"+listExams);
		startActivity(it);
	}



	@Override
	protected void onResume() {
		super.onResume();
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(downloadThread!= null){
			if(downloadThread.isAlive())
				downloadThread.interrupt();
		}
	}
	public void loadData()
	{
		System.out.println("load data called!");
		
		ListView lv1=(ListView)findViewById(R.id.listExamAlert);
		//Custom_ExamAlertXmlParser parser = new Custom_ExamAlertXmlParser();
		
		//listExams = parser.parse(xmlStream); //getAssets().open("jaspalCheck.xml") );
		

		System.out.println("list of alerts: "+listExams.size());
		
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		for( Object_ExamAlert  ob : listExams)
		{
			HashMap<String,String> map=new HashMap<String,String>(2);
			System.out.print("Activity :"+ob.examname);
			System.out.print("Date"+ob.examDate);
			map.put("examname", ob.examname);
			map.put("examdate",ob.examDate);
			
			list.add(map);
		}
		SimpleAdapter ad=new SimpleAdapter(this,list,R.layout.row_listview_examalert, new String[]{"examname","examdate"},new int[]{R.id.txtExamAlert1,R.id.txtExamAlert2});
		lv1.setAdapter(ad);

	}
	
	class DownloadXamAlertXml extends Thread
	{
		public void run()
		{
			try 
			{
				
				HttpClient httpClient = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(Custom_ServerURL_Params.getExam_Alert_link(Activity_ExamAlert.this));
				HttpResponse response = httpClient.execute(httpGet);
				
				Activity_ExamAlert.xmlStream = response.getEntity().getContent();
				
				//Message handlerMsg = new Message();
				//handler.sendMessage(handlerMsg);
				
				//loadData();
				
				Custom_ExamAlertXmlParser parser = new Custom_ExamAlertXmlParser();				
				listExams = parser.parse(xmlStream); 
				
				runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                    	txtMessage.setVisibility(View.GONE);
            			loadData();
                    }
                });
				
				System.out.println("downloaded!");
				
			
		}
			catch (ClientProtocolException e) 
			{
				e.printStackTrace();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}

		}

	}
}

