package com.xercesblue.onlinebankexampo;

public class Object_ExamAlert {

	public String examname;
	public String examDate;
	private String details;
	
	public String getDetails() 
	{
		System.out.println("details string is : "+details.replace('$', '\n'));
		String Temp= details.replace('$', '\n');
		return Temp.replace('@', '\t');
	}
	public void setDetails(String details) 
	{
		this.details = details;
	}

}
