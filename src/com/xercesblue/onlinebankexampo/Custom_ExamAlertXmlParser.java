package com.xercesblue.onlinebankexampo;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class Custom_ExamAlertXmlParser {
	
	
	private Object_ExamAlert examAlert;
	
	private String text;
	public List<Object_ExamAlert> parse(InputStream is) 
	{
		List<Object_ExamAlert> listExams = new ArrayList<Object_ExamAlert>();
		XmlPullParserFactory factory = null;
		XmlPullParser parser = null;
		try {
			factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			parser = factory.newPullParser();

			parser.setInput(is, null);

			int eventType = parser.getEventType();
			System.out.println("Event type is :"+eventType);
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String tagName = parser.getName();
				
				if(tagName==null)
				{
					tagName = "";
				}

				System.out.println("TAg Name is :"+tagName);
				switch (eventType) {
				case XmlPullParser.START_TAG:
					if(tagName.equalsIgnoreCase("ExamName"))
					{
						System.out.println("ExamName Start!");
						examAlert = new Object_ExamAlert();	
						

						int attrCount = parser.getAttributeCount();
						for (int i = 0; i < attrCount; i++) 
						{
							if (parser.getAttributeName(i).equalsIgnoreCase("Exam_Name")) 
							{
								examAlert.examname = parser.getAttributeValue(i);
							}else if (parser.getAttributeName(i).equalsIgnoreCase("Opening_Date")) 
							{
								System.out.println("Exam Dates declared");
								examAlert.examDate = parser.getAttributeValue(i);
								
							}
						}
					}
					break;

				case XmlPullParser.TEXT:
					text = parser.getText();
					System.out.println(text);
					break;

				case XmlPullParser.END_TAG:
					if (tagName.equalsIgnoreCase("ExamName")) 
					{
						System.out.println("examName end");
						listExams.add(examAlert);
							
					} 
					else if (tagName.equalsIgnoreCase("Details")) 
					{
						System.out.println("Details end!");
						examAlert.setDetails(text);
						
					}
					break;
				default:
					break;
				}
				eventType = parser.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return listExams;
	}
}
