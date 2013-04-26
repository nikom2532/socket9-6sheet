package com.popupsolution.cloudstax.android.log;

import java.net.SocketException;

import javax.xml.parsers.ParserConfigurationException;

import com.popupsolution.cloudstax.android.Constants;

import android.widget.TextView;

public class Log {
	private static TextView m_tv = null;
	private static String m_log;
	
	private String m_classname = "";
	

	public void Init(TextView tv)
	{
		m_tv = tv;
		m_log= "";
	}
	
	public void setClassName(String className){
		m_classname = className + " : ";
	}
	
	public void log(String strLog)
	{
		android.util.Log.i(Constants.TAGWDBG,m_classname +strLog+"\r\n");
		//m_log += strLog+"\r\n";
		//m_tv.setText(m_log);
	}
	
	public void logcat(String strLog)
	{
		log(strLog);		
	}
	
	public void error(String strLog,Throwable throwable )
	{
		log(strLog);
	}
	
	public void trace(String strLog) 
	{
		log(strLog);
	}

	public void fatal(String strLog, ParserConfigurationException ex) 
	{
		log(strLog);
	}

	public void error(String strLog) {
		log(strLog);
	} 
	
	public void info(String strLog,Throwable throwable )
	{
		log(strLog);
	}
	
	public void info(String strLog) 
	{
		log(strLog);
	}

	public void debug(String strLog) {
		log(strLog);
	}

	public void debug(String strLog, SocketException ex) {
		log(strLog);
	}

	public void warn(String strLog) {
		log(strLog);		
	}

	public void debug(String strLog, InterruptedException ex) {
		log(strLog);
	}

	public void warn(String strLog, Exception ex) {
		log(strLog);
	}


}
