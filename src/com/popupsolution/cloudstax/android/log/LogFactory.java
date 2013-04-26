package com.popupsolution.cloudstax.android.log;

public class LogFactory {
	
	public static Log getLog(Class clazz)
	{
		Log log = new Log();
		log.setClassName(clazz.getName());
		return log;
	}

	public static Log getLog(java.lang.String name)
	{
		Log log = new Log();
		log.setClassName(name);
		return log;
	}
}
