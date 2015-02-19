package com.xercesblue.onlinebankexampo;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;


public class Custom_XMLPullParserHandler 
{

	Activity_Settings_DownloadMore context;
	private List<Object_Question> questionsList;
	private Object_Question question;

	private String text;
	private ArrayList<Object_Options> options;
	private Object_Options objOption;

	DBHandler_Questions  dbh ;
	
	public Custom_XMLPullParserHandler(Activity_Settings_DownloadMore context)
	{
		questionsList = new ArrayList<Object_Question>();
		this.context = context;
	}
	
	/*
	public List<Object_Question> getQuestions()
	{
		return questionsList;
	}
	*/
	public void parse(FileInputStream is)
	{
		XmlPullParserFactory factory = null;
		XmlPullParser parser = null;
		try
		{
			factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			parser = factory.newPullParser();
			
			parser.setInput(is, null);
			
			int eventType = parser.getEventType();
			while(eventType != XmlPullParser.END_DOCUMENT)
			{
				String tagName = parser.getName();
				switch(eventType)
				{
				case XmlPullParser.START_TAG:
					if(tagName.equalsIgnoreCase("Question"))
					{
						dbh = new DBHandler_Questions (context);
						System.out.println("Question Start!");
						question = new Object_Question();
						
						int attrCount = parser.getAttributeCount();
						for(int i=0;i<attrCount;i++)
						{
							if( parser.getAttributeName(i).equalsIgnoreCase("language") )
							{
								question.langCode = parser.getAttributeValue(i);
							}
							else if( parser.getAttributeName(i).equalsIgnoreCase("CategoryId") )
							{
								question.catId = Integer.parseInt(parser.getAttributeValue(i));
							}
							else if( parser.getAttributeName(i).equalsIgnoreCase("BankName") )
							{
								question.bankName =  parser.getAttributeValue(i);
							}
							else if( parser.getAttributeName(i).equalsIgnoreCase("Year") )
							{
								question.examYear =  parser.getAttributeValue(i);
							}
							else if( parser.getAttributeName(i).equalsIgnoreCase("QuesId"))
							{
								question.quesId = Integer.parseInt( parser.getAttributeValue(i));
							}
						}
					}
					else if(tagName.equalsIgnoreCase("Question_Text"))
					{
						int attrCount = parser.getAttributeCount();
						for(int i=0;i<attrCount;i++)
						{
							if( parser.getAttributeName(i).equalsIgnoreCase("Image") )
							{
								question.image = getBitmapFromURL(parser.getAttributeValue(i));
							}
						}
					}
					else if( tagName.equalsIgnoreCase("Options") )
					{
						int attrCount = parser.getAttributeCount();
						options = new ArrayList<Object_Options>();
						for(int i=0;i<attrCount;i++)
						{
							if(parser.getAttributeName(i).equalsIgnoreCase("total_options"))
							{
								//TODO HARSH I think not used
							}
						}
					}
					else if( tagName.equalsIgnoreCase("Option") )
					{
						int attrCount = parser.getAttributeCount();
						objOption = new Object_Options();
						
						for(int i=0;i<attrCount;i++)
						{
							if(parser.getAttributeName(i).equalsIgnoreCase("Image"))
							{	
	
								objOption.image = getBitmapFromURL(parser.getAttributeValue(i));

							}else if(parser.getAttributeName(i).equalsIgnoreCase("Number"))
							{
								objOption.optionNo = Integer.parseInt( parser.getAttributeValue(i) );
								Log.i("HARSH", "Option No is "+objOption.optionNo);
							}
						}
					}
					else if( tagName.equalsIgnoreCase("Answer"))
					{
						int attrCount = parser.getAttributeCount();
						for(int i=0;i<attrCount;i++)
						{
							if(parser.getAttributeName(i).equalsIgnoreCase("Option_number"))
							{
								question.correctOption = Integer.parseInt(parser.getAttributeValue(i));
							}
							else if(parser.getAttributeName(i).equalsIgnoreCase("Image"))
							{
								question.solutionImage = getBitmapFromURL(parser.getAttributeValue(i));
							}
						}
					}
					break;
					
				case XmlPullParser.TEXT:
					text = parser.getText();				
					break;
					
				case XmlPullParser.END_TAG:
					if(tagName.equalsIgnoreCase("Question"))
					{
						questionsList.add(question);
						DBHandler_Language dbH2 = new DBHandler_Language(context);
						dbh.insertNewQuestionWithID(question , dbH2.getLangMap());
					}
					else if(tagName.equalsIgnoreCase("Question_Text"))
					{
						Log.i("HARSH", "Question Text is "+text);
						question.question = text;
					}
					else if(tagName.equalsIgnoreCase("Options"))
					{
						question.arrayOptions = options;
						//question.setOptionsImage(optionsImage);
					}
					else if(tagName.equalsIgnoreCase("Option"))
					{
						//System.out.println("option_"+optionsCounter+" End!");
						//options[optionsCounter-1] = text;
						Log.i("HARSH", "Option Text is "+text);
						objOption.optionText = text;
						if(options!= null){
							options.add(objOption);
						}
					}
					else if(tagName.equalsIgnoreCase("Answer"))
					{
						Log.i("HARSH", "Solution Text is "+text);
						question.solution = text;
					}
					break;
					
				default:
					break;	
				}
				eventType = parser.next();
			}
		}
		catch (XmlPullParserException e) 	
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		 
		
		
		context.runOnUiThread(new Runnable() {
			  public void run() {
				  if(questionsList.size() == 0){
					  context.noNewQuestions();
				  }
			  }
			});
	}
	
	public static byte[] getBitmapFromURL(String link) 
	{
		   /*--- this method downloads an Image from the given URL, 
		    *  then decodes and returns a Bitmap object
		    ---*/
		   try 
		   {
			   DefaultHttpClient mHttpClient = new DefaultHttpClient();
			   HttpGet mHttpGet = new HttpGet(link);
		       System.out.println("Downloading image from : "+link);
			   HttpResponse mHttpResponse = mHttpClient.execute(mHttpGet);
			   if (mHttpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) 
			   {
				   HttpEntity entity = mHttpResponse.getEntity();
			       if ( entity != null) 
			       {
			    	   return EntityUtils.toByteArray(entity);	// returns blob data , pass it to query
			       }
			   }
		   }
		   catch (IOException e) 
		   {
		       e.printStackTrace();
		       return null;
		   }
		return null;
	}
}









