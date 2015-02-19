package com.xercesblue.onlinebankexampo;

public class ServerURL {

	public static String getDownload_question_link(long qusNumber ,String deviceImei, String langCode ) {
		return "http://www.xercesblue.in/onlinexamserver/liquid_data/xport_xml.php?lastQuestionNumber="+qusNumber+"&appId="+Globals.APP_ID+"&deviceIMEI="+deviceImei+"&LangCode="+langCode+"&gcmId="+Globals.GCM_REG_ID+"&version_code="+Globals.getversion_code();
	}
	
	public final static String getExam_Alert_link()
	{
		return "http://www.xercesblue.in/onlinexamserver/liquid_data/xamalertdatabase/xamupdate.php?version_code="+Globals.getversion_code();
	}
	
	public final static String getCurrent_GK_Read_link(int pageNo, String langCode, int AppId, String date, String month, String year)
	{
		return "http://xercesblue.in/onlinexamserver/liquid_data/CurrentGKCenter/test.php?pageno="+ pageNo+ "&langCode="+ langCode+ "&AppId="+AppId+ "&date="+ date+ "&month="+ month+ "&year=" + year+ "&version_code="+Globals.getversion_code();
	}
	
	public final static String getCurrent_GK_Test_link(int qNo, String langCode, String date, String month, String year)
	{
		return "http://xercesblue.in/onlinexamserver/liquid_data/CurrentGKCenter/getGKQuestions.php?QuestionNo="+qNo+"&langCode="+langCode+"&date="+date+"&month="+month+"&year="+year +"&version_code="+Globals.getversion_code();
	}
	
	public final static String getBug_Report_Wrong_Question_link()
	{
		return "http://xercesblue.in/onlinexamserver/liquid_data/bugReport/reportQuestion.php?version_code="+Globals.getversion_code();
	}
	
	public static String getContactUs_link() {
		return "http://xercesblue.in/OnlineXamServer/liquid_data/userreview/myreview.php?version_code="+Globals.getversion_code();
	}
	
	public static String getRegistration_link() {
		return "http://xercesblue.in/onlinexamserver/liquid_data/AppRegisteredUsers/registerUser.php?version_code="+Globals.getversion_code();
	}
	
	public static String getPushnotificationRegisteruser_link(String regId,int AppId ) {
		return "http://www.xercesblue.in/onlinexamserver/liquid_data/PushNotificationDatabase/registerUser.php?regId="+regId+"&AppId="+AppId+ "&version_code="+Globals.getversion_code();
	}
	
	public static String getAdvertisement_link(int AppId) {
		return "http://xercesblue.in/onlinexamserver/liquid_data/AdvtControlCenter/getAdvtjsonNew.php?appId="+AppId+ "&version_code="+Globals.getversion_code();
	}
	
	public static String getShareLinkGeneric() {
		return "http://xercesblue.in/download/androidApp.php?id=5";
	}
	
	public static String getShareLinkWhatsapp() {
		return "http://xercesblue.in/download/androidApp.php?id=4";
	}
	
	public static String getShareLinkFacebook() {
		return "http://xercesblue.in/download/androidApp.php?id=3";
	}
	
	public static String getShareAppMsg() {
		return "Friends, check out this awesome app for Bank PO / Clerical exams preparation. ";
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
