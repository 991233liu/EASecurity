package com.easecurity.admin.utils

import java.text.SimpleDateFormat
import java.text.ParseException

import groovy.transform.CompileStatic

/**
 * 时间（日期）相关的工具。
 */
class DateUtils {
	
	/* 标准的日期、时间格式：注意 SimpleDateFormat 是线程不安全的 */
	/** 时间格式:yyyy-MM-dd */
	static final public String dateFmt = "yyyy-MM-dd"
	/** 时间格式:yyyy-MM */
	static final public String monthFmt = "yyyy-MM"
	/** 时间格式:yyyy-MM-dd HH:mm */
	static final public String datetimeFmt = "yyyy-MM-dd HH:mm"
	/** 时间格式:yyyy-MM-dd'T'HH:mm:ss */
	static final public String msOfficeDatetimeFmt = "yyyy-MM-dd'T'HH:mm:ss"
	/** 时间格式:HH:mm */
	static final public String timeFmt = "HH:mm"
	
	/*
	 * 几种不大标准的时间格式，但常用。
	 * 在做自动Web binding的时候，同时修改grails-web-XXX.jar中的GrailsDataBinder.java、StructuredDateEditor.java；
	 * 在包“org.codehaus.groovy.grails.web.binding”下。
	 */
	/** 时间格式:yyyy-M-d */
	static final private SimpleDateFormat df11 = new SimpleDateFormat("yyyy-M-d");
	/** 时间格式:yyyy.M.d */
	static final private SimpleDateFormat df12 = new SimpleDateFormat("yyyy.M.d");
	/** 时间格式:yyyy/M/d */
	static final private SimpleDateFormat df13 = new SimpleDateFormat("yyyy/M/d");
	/** 时间格式:yyyyMMdd */
	static final private SimpleDateFormat df14 = new SimpleDateFormat("yyyyMMdd");
	/** 时间格式:yyyyMd */
	static final private SimpleDateFormat df15 = new SimpleDateFormat("yyyyMd");	// 解析结果可能不正确！
	/** 时间格式:yyyy-M-d H:m */
	static final private SimpleDateFormat df21 = new SimpleDateFormat("yyyy-M-d H:m");
	/** 时间格式:yyyy.M.d H:m */
	static final private SimpleDateFormat df22 = new SimpleDateFormat("yyyy.M.d H:m");
	/** 时间格式:yyyy/M/d H:m */
	static final private SimpleDateFormat df23 = new SimpleDateFormat("yyyy/M/d H:m");	// 12~16位
	/** 时间格式:yyyy-MM-dd'T'HH:mm:ss */
	static final private SimpleDateFormat df24 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");	// 14~19位
	/** 时间格式:yyyy-MM-dd HH:mm:ss */
	static final private SimpleDateFormat df25 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/** 时间格式:yyyy-M */
	static final private SimpleDateFormat df31 = new SimpleDateFormat("yyyy-M");
	/** 时间格式:yyyyM */
	static final private SimpleDateFormat df32 = new SimpleDateFormat("yyyyM");
	// TODO 添加 月/日/年 的格式

	/**
	 * 按照自定义个格式格式化日期。
	 * 
	 * <p>如果日期或格式为空，则返回defaultVal，默认为空字符串。</p>
	 * 
	 * @param date		需要格式化的日期
	 * @param format	格式化的形式，如"yyyy-MM-dd"
	 * @param defaultVal
	 */
	@CompileStatic
	static String format(Date date, String format, String defaultVal="") {
		if ( !date || !format ) return defaultVal;
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}
	
	/**
	 * 按照自定义格式解析日期字符串。
	 * 
	 * <p>如果字符串或格式为空，或者解析失败，则返回defaultVal，默认为null。</p>
	 * 
	 * @param date		日期字符串
	 * @param format	日期格式，如"yyyy-MM-dd"
	 * @param defaultVal
	 */
	@CompileStatic
	static Date parse(String date, String format, Date defaultVal=null) {
		if ( !date || !format ) return defaultVal;
		SimpleDateFormat df = new SimpleDateFormat(format);
		try {
			return df.parse(date);
		} catch ( ParseException e ) {
			return defaultVal;
		}
	}
	
	/**
	 * 今天
	 */
	@CompileStatic
	static Date today() {
		return toDay( new Date() );
	}
	
	/**
	 * 将时间的精度转换为日期（小时、分钟、秒等都设为0）<p>
	 * 
	 * @param datetime		需要转换的时间
	 * @return 转换后的日期
	 */
	@CompileStatic
	static Date toDay( Date datetime ) {
		if ( !datetime ) return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(datetime);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	/**
	 * 将时间转换为精度为日期的周一<p>
	 * 
	 * @param datetime		需要转换的时间
	 * @return 转换后的日期
	 */
	@CompileStatic
	static Date toMonday( Date datetime ) {
		if ( !datetime ) return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(datetime);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * 将时间转换为年份（精度）<p>
	 * 
	 * @param datetime	需要转换的时间
	 * @return 转换后的年份的1月1日0时
	 */
	@CompileStatic
	static Date toYear( Date datetime ) {
		if ( !datetime ) return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(datetime);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	/**
	 * 将时间转换为月份（精度）<p>
	 * 
	 * @param datetime	需要转换的时间
	 * @return 转换后的月份
	 */
	@CompileStatic
	static Date toMonth( Date datetime ) {
		if ( !datetime ) return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(datetime);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * 格式化时间<p>
	 *
	 * @param datetime	需要格式化的时间
	 * @return 格式化后的时间字符串（yyyy-MM-dd HH:mm）
	 */
	@CompileStatic
	static String formatDatetime( Date datetime ) {
		return datetime ? datetime.format(datetimeFmt) : "";
	}

	/**
	 * 格式化时间
	 * @param datetime	需要格式化的时间
	 * @return 格式化后的时间字符串（HH:mm）
	 */
	@CompileStatic
	static String formatTime( Date datetime ) {
		return datetime ? datetime.format(timeFmt) : "";
	}

	/**
	 * 格式化时间（微软Office）<p>
	 *
	 * @param datetime		需要格式化的时间
	 * @return 格式化后的时间字符串（yyyy-MM-dd'T'HH:mm:ss）
	 */
	@CompileStatic
	static String formatMSOfficeDatetime( Date datetime ) {
		return datetime ? datetime.format(msOfficeDatetimeFmt) : "";
	}

	/**
	 * 格式化日期<p>
	 *
	 * @param day	需要格式化的日期
	 * @return 格式化后的日期字符串（yyyy-MM-dd）
	 */
	@CompileStatic
	static String formatDate( Date day ) {
		return day ? day.format(dateFmt) : "";
	}

	/**
	 * 格式化月份<p>
	 *
	 * @param month	需要格式化的月份
	 * @return 格式化后的月份字符串（yyyy-MM）
	 */
	@CompileStatic
	static String formatMonth( Date month ) {
		return month ? month.format(monthFmt) : "";
	}

	/**
	 * 解析字符串为日期<p>
	 * 
	 * @param date	需要解析的字符串
	 * @return 解析后的日期
	 */
	@CompileStatic
	static Date parse(String date) throws ParseException {
		if ( null==date || 0==date.length() ) return null;
		SimpleDateFormat df = null;
		Date result = null;
		ParseException pe = null;
		try {
			// 仅包含月份
			if ( date.length() <= 7 ) {
				if ( date.indexOf("-") > 0 ) df = df31;
				else if ( date.length() < 7 ) df = df32;
				else df = df15;		// 仅包含日期
				synchronized(df) { result = df.parse(date); }
			}
			// 包含日期、时间（小时、分钟、秒）、[时区]：如 2007-06-30T16:00:00Z
			else if ( org.apache.commons.lang.StringUtils.countMatches(date,":") == 2 ) {
				df = (date.indexOf("T") != -1) ? df24 : df25;
				synchronized(df) {	// SimpleDateFormat是线程不安全的，可以考虑用 JODA Time 代替
					String tzs = date.replaceAll("^[\\d-: T]+", "");
					df.setTimeZone((tzs.length()==0 ? java.util.TimeZone.getDefault() : java.util.TimeZone.getTimeZone(tzs)));
					result = df.parse(date);
				}
			}
			// 仅包含日期
			else if ( date.indexOf(" ") < 0 ) {
				if ( date.indexOf("-") > 0 ) df = df11;
				else if ( date.indexOf(".") > 0 ) df = df12;
				else if ( date.indexOf("/") > 0 ) df = df13;
				else if ( date.length() == 8 ) df = df14;
				else df = df15;
				synchronized(df) { result = df.parse(date); }
			}
			// 包含日期、小时、分钟
			else {
				if ( date.indexOf("-") > 0 ) df = df21;
				else if ( date.indexOf(".") > 0 ) df = df22;
				else if ( date.indexOf("/") > 0 ) df = df23;
				synchronized(df) { result = df.parse(date); }
			}
		} catch ( ParseException e ) {
			pe = e;
		}
		if ( result != null ) {
			return result;
		} else {
			throw new ParseException( "Unparseable date: ${date}", 0);
		}
	}

	/**
     * 同 parse(String Date)
	 */
	@Deprecated static Date parseDate( String date ) throws ParseException {
		return parse(date);
	}

	/**
	 * 计算两个日期间的月数<p>
	 * 
	 * @param startdate		开始日期
	 * @param enddate		结束日期
	 * @return 相差的月数
	 */
	@CompileStatic
	static int monthesBetween(Date startDate, Date endDate) {
		if ( !startDate || !endDate ) return 0;
		def cal1 = Calendar.getInstance()
		cal1.setTime( startDate )
		def cal2 = Calendar.getInstance()
		cal2.setTime( endDate )
		def m1 = cal1.get(Calendar.YEAR)*12 + cal1.get(Calendar.MONTH)
		def m2 = cal2.get(Calendar.YEAR)*12 + cal2.get(Calendar.MONTH)
		return m2 - m1 + 1
	}

	/**
	 * 计算两个日期之间的天数<p>
	 * 
	 * @param startdate	开始日期（含）
	 * @param enddate	结束日期（不含）
	 * @return 相差的天数
	 */
	@CompileStatic
	static int daysBetween(Date startDate, Date endDate) {
		if ( startDate && endDate ) {
			SimpleDateFormat df = new SimpleDateFormat(dateFmt);
			Date d1 = df.parse(df.format(startDate))
			Date d2 = df.parse(df.format(endDate));
			return (Long.valueOf((d2.getTime() - d1.getTime())) / 86400000).intValue();
		}
		return 0;
	}

	/**
	 * 判断日期是否在某个月份之中<p>
	 * 
	 * @param day	需要判断的日期
	 * @param month	月份
	 * @return	true/false
	 */
	@CompileStatic
	static boolean inMonth(Date day, Date month) {
		if ( day && month ) {
			SimpleDateFormat df = new SimpleDateFormat(monthFmt);
			return (df.format(day) == df.format(month));
		}
		return false;
	}

	/**
	 * 根据月份取上个月<p>
	 * 
	 * @param month		月份
	 * @return	上个月
	 */
	@CompileStatic
	static Date preMonth( Date month ) {
		Calendar cal = Calendar.getInstance()
		cal.setTime( month )
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)-1)
		return cal.getTime()
	}

	/**
	 * 根据月份取下个月<p>
	 * 
	 * @param month		月份
	 * @return 	下个月的一日
	 */
	@CompileStatic
	static Date nextMonth( Date month ) {
		Calendar cal = Calendar.getInstance()
		cal.setTime( month )
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)+1)
		return cal.getTime()
	}
	
	/**
	 * 根据日期，获得周的第一天（默认为周一）的日期<p>
	 * 
	 * @param day			要计算的日期
	 * @param weekOffset	周的偏差，比如 -1 为上周一，2为下下周一
	 * @param firstDay		周的第一天，默认为 Calendar.MONDAY
	 * @return 周一0点
	 */
	@CompileStatic
	static Date firstDayOfWeek( Date day, Integer weekOffset = 0, int firstDay = Calendar.MONDAY ) {
		Calendar cal = Calendar.getInstance()
		cal.setTime( day )
		cal.setFirstDayOfWeek( firstDay ?: Calendar.MONDAY )
		cal.set( Calendar.DAY_OF_WEEK, firstDay ?: Calendar.MONDAY )
		return toDay( cal.getTime() + weekOffset * 7 )
	}
	
	/**
	 * 根据日期，获得周的最后一天（默认为周日）的日期<p>
	 *
	 * @param day			要计算的日期
	 * @param weekOffset	周的偏差，比如 -1 为上周日，2为下下周日
	 * @param firstDay		周的第一天，默认为 Calendar.MONDAY
	 * @return 周日0点
	 */
	@CompileStatic
	static Date lastDayOfWeek( Date day, Integer weekOffset = 0, int firstDay = Calendar.MONDAY ) {
		return firstDayOfWeek( day, weekOffset, firstDay ) + 6
	}

	/**
	 * 根据年度取下一年<p>
	 * 
	 * @param year	年度
	 * @return	下一年的一月一日
	 */
	@CompileStatic
	static Date nextYear( Date year ) {
		Calendar cal = Calendar.getInstance()
		cal.setTime( year )
		cal.set(Calendar.YEAR, cal.get(Calendar.YEAR)+1);
		return cal.getTime()
	}

	/**
	 *  获取今天时第几周
	 **/
	// TODO 为什么原来的类不行了？CompileStatic中增加了什么？还是降低了动态匹配？
	static int getBtWeek(Date date = new Date()) {
		Calendar calendar = Calendar.getInstance()
		calendar.setFirstDayOfWeek(Calendar.MONDAY)
		calendar.setTime(new Date())
		System.out.println(calendar.get(Calendar.WEEK_OF_YEAR))
		return calendar.get(Calendar.WEEK_OF_YEAR)
	}

	static Date getStartDate(Date date = new Date()) {
		Calendar calendar = Calendar.getInstance()
		calendar.setFirstDayOfWeek(Calendar.MONDAY)
		calendar.setTime(new Date())
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		System.out.println(calendar.getTime())
		return calendar.getTime()
	}

	static Date getEndDate(Date date = new Date()) {
		Calendar calendar = Calendar.getInstance()
		calendar.setFirstDayOfWeek(Calendar.MONDAY)
		calendar.setTime(new Date())
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		System.out.println(calendar.getTime())
		return calendar.getTime()
	}
	
	/**
	 * 获得日期所对应该年的周信息<p>
	 * 
	 * @param date	日期，默认取当前日期
	 * @param firstDay		周的第一天，默认为 Calendar.MONDAY
	 * @return [year:年份，week：第几周，startDate：本周的开始日期，endDate：本周的结束日期]
	 */
	@CompileStatic
	static Map getWeekInfo(Date date = null, int firstDay = Calendar.MONDAY) {
		Calendar cal = Calendar.getInstance();
		if ( date ) cal.setTime(date);
		cal.setFirstDayOfWeek(firstDay);	// 周一为开始
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		// 第一天
		cal.set(Calendar.DAY_OF_WEEK, firstDay);
		Date startDate = cal.getTime();
		// 最后一天
		Date endDate = startDate + 6;
		// 其他信息
		cal.setTime(startDate);
		def week = cal.get(Calendar.WEEK_OF_YEAR);
		def year = cal.get(Calendar.YEAR);
		return [year:year, week:week, startDate:startDate, endDate:endDate]
	}
	
	/**
	 * 将对象转换成日期类型，主要用于转换sql返回的日期类型<p>
	 * 
	 * @param date	
	 * @return
	 */
	static Date convertSqlDate( Object date ) {
		if ( !date ) return null;
		String className = date.getClass().getName();
		if ( className.startsWith("oracle.sql.") ) return date.dateValue();
		if ( className.startsWith("dm.sql.Dmdb") ) return date.getDate();
		return date;
	}
	
	/**
	 * 转换成 XMLGregorianCalendar 类型。常用于 WebService Client。<p>
	 * 
	 * @throws Exception
	 */
	@CompileStatic
	static javax.xml.datatype.XMLGregorianCalendar toXMLGregorianCalendar(java.util.Date date) throws Exception {
		if ( date == null ) return null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		javax.xml.datatype.DatatypeFactory dtf = javax.xml.datatype.DatatypeFactory.newInstance();
		return dtf.newXMLGregorianCalendar(
			calendar.get(calendar.YEAR) as int,
			calendar.get(calendar.MONTH)+1 as int,
			calendar.get(calendar.DAY_OF_MONTH) as int,
			calendar.get(calendar.HOUR) as int,
			calendar.get(calendar.MINUTE) as int,
			calendar.get(calendar.SECOND) as int,
			calendar.get(calendar.MILLISECOND) as int,
			calendar.get(calendar.ZONE_OFFSET)/(1000*60) as int
		);
	}
	
}
