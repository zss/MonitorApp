package com.oumiao.monitor.utils;

import android.text.TextUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class DateUtil {
	public static final long m_second=1000;
	public static final long m_minute=m_second*60;
	public static final long m_hour=m_minute*60;
	public static final long m_day=m_hour*24;
	public static final String PATTERN_DATE = "yyyy-MM-dd";
	public static final String PATTERN_TIME = "HH:mm:ss";
	public static final String PATTERN_TIMESTAMPE = "yyyy-MM-dd HH:mm:ss";
	public static final String PATTERN_DATE_TIME_SHORT = "yyyy-MM-dd HH:mm";
	
	/**
	* <p>DateUtil instances should NOT be constructed in standard programming.</p>
	* <p>This constructor is public to permit tools that require a JavaBean instance
	* to operate.</p>
	 */
	public DateUtil(){}
	
	public static final long timeMillis(){
		return System.currentTimeMillis();
	}
	
	public static final Date currentTime(){
		return new Date();
	}
	public static final Timestamp currentTimestamp(){
		return new Timestamp(System.currentTimeMillis());
	}
	public static final int nextMonth(){
		String next = format(new Date(), "M");
		int nextMonth = Integer.parseInt(next) + 1;
		if(nextMonth==13) return 1;
		return nextMonth;
	}
	/**
	 * parse date using default pattern yyyy-MM-dd
	 * @param strDate
	 * @return
	 */
	public static final Date parseDate(String strDate){
		Date date = null;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			date = dateFormat.parse(strDate);
			return date;
		} catch (Exception pe) {
			return null;
		}
	}

	public static final Timestamp parseTimestamp(String strDate){
		try {
			// Timestamp format must be yyyy-mm-dd hh:mm:ss[.fffffffff]
			Timestamp result = Timestamp.valueOf(strDate);
			return result;
		} catch (Exception pe) {
			return null;
		}
	}
	/**
	 * @param strDate
	 * @param pattern
	 * @return
	 */
	public static final Date parseDate(String strDate, String pattern){
		SimpleDateFormat df = null;
		Date date = null;
		df = new SimpleDateFormat(pattern);
		try {
			date = df.parse(strDate);
			return date;
		} catch (Exception pe) {
			return null;
		}
	}

	/**
	 * @param date
	 * @return formated date by yyyy-MM-dd
	 */
	public static final <T extends Date> String formatDate(T date) {
		if(date==null) return null;
		SimpleDateFormat dateFormat = new SimpleDateFormat(PATTERN_DATE);
		return dateFormat.format(date);
	}

	/**
	 *
	 * @param mills
	 * @return formated date by yyyy-MM-dd
	 */
	public static final String formatDate(long mills) {
		return formatDate(new Date(mills));
	}

	/**
	 * @param date
	 * @return formated time by HH:mm:ss
	 */
	public static final <T extends Date> String formatTime(T date) {
		if(date==null) return null;
		SimpleDateFormat timeFormat = new SimpleDateFormat(PATTERN_TIME);
		return timeFormat.format(date);
	}
	/**
	 * @param date
	 * @return formated time by yyyy-MM-dd HH:mm:ss
	 */
	public static final <T extends Date> String formatTimestamp(T date) {
		if(date==null) return null;
		SimpleDateFormat timestampFormat = new SimpleDateFormat(PATTERN_TIMESTAMPE);
		return timestampFormat.format(date);
	}
	public static final String formatTimestamp(Long mills){
		return formatTimestamp(new Date(mills));
	}

	/**
	 * @param date
	 * @param pattern: Date format pattern
	 * @return
	 */
	public static final <T extends Date> String format(T date, String pattern) {
		if(date==null) return null;
		try{
			SimpleDateFormat df = new SimpleDateFormat(pattern);
			String result = df.format(date);
			return result;
		}catch(Exception e){
			return null;
		}
	}

	/**
	 * @param original
	 * @param days
	 * @param hours
	 * @param minutes
	 * @param seconds
	 * @return original+day+hour+minutes+seconds+millseconds
	 */
	public static final <T extends Date> T addTime(T original, int days, int hours, int minutes, int seconds){
		if(original==null) return null;
		long newTime=original.getTime()+m_day*days+m_hour*hours+m_minute*minutes+m_second*seconds;
		T another = (T) original.clone();
		another.setTime(newTime);
		return another;
	}
	public static final <T extends Date> T addDay(T original, int days){
		if(original==null) return null;
		long newTime=original.getTime() + m_day * days;
		T another = (T) original.clone();
		another.setTime(newTime);
		return another;
	}
	public static final <T extends Date> T addHour(T original, int hours){
		if(original==null) return null;
		long newTime=original.getTime()+m_hour*hours;
		T another = (T) original.clone();
		another.setTime(newTime);
		return another;
	}
	public static final <T extends Date> T addMinute(T original, int minutes){
		if(original==null) return null;
		long newTime=original.getTime() + m_minute*minutes;
		T another = (T) original.clone();
		another.setTime(newTime);
		return another;
	}
	public static final <T extends Date> T addSecond(T original, int second){
		if(original==null) return null;
		long newTime=original.getTime() + m_second*second;
		T another = (T) original.clone();
		another.setTime(newTime);
		return another;
	}
	
	/**
	 * @param day
	 * @return for example ,1997/01/02 22:03:00,return 1997/01/02 00:00:00.0
	 */
	public static final <T extends Date> T getBeginningTimeOfDay(T day){
		if(day==null) return null;
		//new Date(0)=Thu Jan 01 08:00:00 CST 1970
		String strDate = formatDate(day);
		Long mill = parseDate(strDate).getTime();
		T another = (T) day.clone();
		another.setTime(mill);
		return another;
	}
	/**
	 * @param day
	 * @return for example ,1997/01/02 22:03:00,return 1997/01/02 23:59:59.999
	 */
	public static final <T extends Date> T getLastTimeOfDay(T day){
		if(day==null) return null;
		Long mill = getBeginningTimeOfDay(day).getTime() + m_day - 1;
		T another = (T) day.clone();
		another.setTime(mill);
		return another;
	}
	/**
	 * 09:00:00,09:07:00 ---> 9:00,9:7:00
	 * @param time
	 * @return
	 */
	public static final String formatTime(String time){
		if(time==null) return null;
		time = time.trim();
		if(TextUtils.isEmpty(time)) throw new IllegalArgumentException("time format is error!");
		time = time.replace('：', ':');
		String[] times = time.split(":");
		String result="";
		if(times[0].length()<2) result += "0" + times[0]+":";
		else result += times[0]+":";
		if(times.length > 1){
			if(times[1].length()<2) result += "0" + times[1];
			else result += times[1];
		}else{
			result += "00";
		}
		Timestamp.valueOf("2001-01-01 " + result + ":00");
		return result;
	}
	public static boolean isTomorrow(Date date) {
		if(date==null) return false;
		if(formatDate(addTime(new Date(), 1, 0, 0, 0)).equals(formatDate(date))) return true;
		return false;
	}
	/***
	 * @param date
	 * @return 1,2,3,4,5,6,7
	 */
	private static int[] chweek = new int[]{0,7,1,2,3,4,5,6};
	/**
	 * @param date
	 * @return 1,2,3,4,5,6,7
	 */
	public static Integer getWeek(Date date) {
		if(date==null) return null;
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return chweek[c.get(Calendar.DAY_OF_WEEK)];
	}
	public static String[] cnweek = new String[]{"", "周日", "周一", "周二", "周三", "周四", "周五", "周六"};
	public static String[] cnSimpleweek = new String[]{"", "日", "一", "二", "三", "四", "五", "六"};
	/**
	 * @param date
	 * @return "周日", "周一", "周二", "周三", "周四", "周五", "周六"
	 */
	public static String getCnWeek(Date date){
		if(date==null) return null;
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return cnweek[c.get(Calendar.DAY_OF_WEEK)];
	}
	public static String getCnSimpleWeek(Date date){
		if(date==null) return null;
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return cnSimpleweek[c.get(Calendar.DAY_OF_WEEK)];
	}
	public static Integer getMonth(Date date) {
		if(date==null) return null;
		String month = format(date, "M");
		return Integer.parseInt(month);
	}
	public static Integer getCurrentDay(){
		return getDay(new Date());
	}
	public static Integer getCurrentMonth(){
		return getMonth(new Date());
	}
	public static Integer getCurrentYear() {
		return getYear(new Date());
	}
	public static Integer getYear(Date date) {
		if(date==null) return null;
		String year = DateUtil.format(date, "yyyy");
		return Integer.parseInt(year);
	}
	public static Integer getDay(Date date) {
		if(date==null) return null;
		String year = DateUtil.format(date, "d");
		return Integer.parseInt(year);
	}
	public static Integer getCurrentHour(Date date) {
		if(date==null) return null;
		String hour = DateUtil.format(date, "H");
		return Integer.parseInt(hour);
	}

	/**
	 *
	 * @return yyyy-MM-dd
	 */
	public static String getCurDateStr(){
		return DateUtil.formatDate(new Date());
	}
	/**
	 * 获取当前日期 时间
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String getCurTimeStr(){
		return DateUtil.formatTimestamp(new Date());
	}
	
	public static boolean isAfter(Date date){
		if(date.after(new Date())){
			return true;
		}
		return false;
	}

	/**
	 * date 是否在distdate后面
	 *
	 * @param date
	 * @param distDate
	 * @return
	 */
	public static boolean isAfter(Date date, Date distDate) {
		if (date.after(distDate)) {
			return true;
		}
		return false;
	}

	/**
	 * 获取date所在月份的星期为weektype且日期在date之后（或等于）的所有日期
	 * @param weektype
	 * @return
	 */
	public static List<Date> getWeekDateList(Date date, String weektype) {
		int curMonth = getMonth(date);
		int week = Integer.parseInt(weektype);
		int curWeek = getWeek(date);
		int sub = (7 + week - curWeek) % 7;
		Date next = addDay(date, sub);
		List<Date> result = new ArrayList<Date>();
		while(getMonth(next) == curMonth){
			result.add(next);
			next = addDay(next, 7);
		}
		return result;
	}
	/**
	 * 获取date之后(包括date)的num个星期为weektype日期（不限制月份）
	 * @param weektype
	 * @return
	 */
	public static List<Date> getWeekDateList(Date date, String weektype, int num) {
		int week = Integer.parseInt(weektype);
		int curWeek = getWeek(date);
		List<Date> result = new ArrayList<Date>();
		int sub = (7 + week - curWeek) % 7;
		Date next = addDay(date, sub);
		for(int i=0; i<num; i++){
			result.add(next);
			next = addDay(next, 7);
		}
		return result;
	}
	/**
	 * 获取date所在星期的周一至周日的日期
	 * @param date
	 * @return
	 */
	public static List<Date> getCurWeekDateList(Date date){
		int curWeek = getWeek(date);
		List<Date> dateList = new ArrayList<Date>();
		for(int i=1;i<=7;i++) dateList.add(DateUtil.addDay(date, -curWeek + i ));
		return dateList;
	}
	public static Date getCurDate(){
		return getBeginningTimeOfDay(new Date());
	}
	/**
	 * 获取日期所在月份的第一天
	 * @param date
	 * @return
	 */
	public static <T extends Date> T getMonthFirstDay(T date) {
		if(date == null) return null;
		String dateStr = format(date, "yyyy-MM") + "-01";
		Long mill = parseDate(dateStr).getTime();
		T another = (T) date.clone();
		another.setTime(mill);
		return another;
	}
	
	public static <T extends Date> T getNextMonthFirstDay(T day) {
		if(day==null) return null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(day);
		int month = calendar.get(Calendar.MONTH);
		calendar.set(Calendar.MONTH, month +1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		String datefor = format(calendar.getTime(), "yyyy-MM-dd");
		Long mill = parseDate(datefor).getTime();
		T another = (T) day.clone();
		another.setTime(mill);
		return  another;
	}
	/**
	 * 获取日期所在月份的最后一天
	 * @param date
	 * @return
	 */
	public static <T extends Date> T  getMonthLastDay(T date){
		if(date == null) return null;
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		String dateStr = format(date, "yyyy-MM") + "-" + c.getActualMaximum(Calendar.DAY_OF_MONTH);
		Long mill = parseDate(dateStr).getTime();
		T another = (T) date.clone();
		another.setTime(mill);
		return another;
	}

	/**
	 * @param days
	 * @return yyyy-MM-dd
	 */
	public static String formatDateAddDay(int days) {
		return formatDate(addDay(new Date(), days));
	}

	public static Timestamp getCurTimestamp() {
		return getBeginningTimeOfDay(new Timestamp(System.currentTimeMillis()));
	}
	public static Integer getHour(Date date) {
		if(date==null) return null;
		String hour = format(date, "H");
		return Integer.parseInt(hour);
	}
	
	public static Timestamp formatLocalTime(String paramString){
		if(paramString == null) return null;
		Timestamp date = parseTimestamp(paramString);
		return addMinute(date, getTimeZone());
	}

	/**
	 * 返回当前时区与UTC相差多少分钟
	 *
	 * @return
	 */
	public static int getTimeZone(){
		Calendar cal = Calendar.getInstance(Locale.getDefault());
		int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
		int zone = zoneOffset/60/1000;
		return zone;
	}

	public static String getTimeDesc(Timestamp time) {
		if(time==null) return "";
		String timeContent;
		Long ss = System.currentTimeMillis()-time.getTime();
		Long minute = ss/60000;
		if (minute<1) minute=1L;
		if(minute>=60){
			Long hour = minute/60;
			if(hour>=24){
				if(hour>720)timeContent= "1月前";
				else if(hour>168 && hour<=720) timeContent= (hour/168)+"周前";
				else timeContent = (hour/24)+"天前";
			}else{
				timeContent =  hour+"小时前";
			}
		}else{
			timeContent = minute+"分钟前";
		}
		return timeContent;
	}
	
	public static String getDateDesc(Date time) {
		if(time==null) return "";
		String timeContent;
		Long ss = System.currentTimeMillis()-time.getTime();
		Long minute = ss/60000;
		if (minute<1) minute=1L;
		if(minute>=60){
			Long hour = minute/60;
			if(hour>=24){
				if(hour>720)timeContent= "1月前";
				else if(hour>168 && hour<=720) timeContent= (hour/168)+"周前";
				else timeContent = (hour/24)+"天前";
			}else{
				timeContent =  hour+"小时前";
			}
		}else{
			timeContent = minute+"分钟前";
		}
		return timeContent;
	}
	
	/**
	 *  author: bob
	 *  date:	20100729
	 *  截取日期, 去掉年份
	 *  param: 	date1
	 *  eg. 传入"1986-07-28", 返回 07-28 
	 */
	public static String getMonthAndDay(Date date){
		return formatDate(date).substring(5);
	}
	public static Date getMillDate(){
		return new Date();
	}
	public static Timestamp getMillTimestamp(){
		return new Timestamp(System.currentTimeMillis());
	}
	/**
	 * 时间差：day1-day2
	 * @param day1
	 * @param day2
	 * @return
	 */
	public static final <T extends Date> String getDiffStr(T day1, T day2){
		if(day1==null || day2==null) return "---";
		long diff = day1.getTime() - day2.getTime();
		long sign = diff/ Math.abs(diff);
		diff = Math.abs(diff)/1000;
		long hour = diff/3600;
		long minu = diff%3600/60;
		long second = diff%60;
		return (sign<0?"-":"+") + (hour==0?"":hour+"小时") + (minu==0?"":minu+"分") + (second==0?"":second+"秒");
	}
	/**
	 * 时间差（秒）：day1-day2
	 * @param day1
	 * @param day2
	 * @return
	 */
	public static final <T extends Date> long getDiffSecond(T day1, T day2){
		if(day1==null || day2==null) return 0;
		long diff = day1.getTime() - day2.getTime();
		long sign = diff/ Math.abs(diff);
		diff = Math.abs(diff)/1000;
		return sign * diff;
	}
	/**
	 * 时间差（分钟）：day1-day2
	 * @param day1
	 * @param day2
	 * @return
	 */
	public static final <T extends Date> double getDiffMinu(T day1, T day2){
		if(day1==null || day2==null) return 0;
		try {
			long diff = day1.getTime() - day2.getTime();
			long sign = diff / Math.abs(diff);
			diff = Math.abs(diff) / 1000;
			return Math.round(diff * 1.0d * 10 / 6.0) / 100.0 * sign;//两位小数
		}catch(Exception e){
			return 0;
		}
	}
	/**
	 * 时间差（小时）：day1-day2
	 * @param day1
	 * @param day2
	 * @return
	 */
	public static final <T extends Date> double getDiffHour(T day1, T day2){
		if(day1==null || day2==null) return 0;
		try {
			long diff = day1.getTime() - day2.getTime();
			long sign = diff / Math.abs(diff);
			diff = Math.abs(diff) / 1000;
			return Math.round(diff * 1.0d / 3.6) / 1000.0 * sign;//三位小数
		}catch (Exception e){
			return 0;
		}
	}
	public static final <T extends Date> int getDiffDay(T day1, T day2){
		if(day1==null || day2==null) return 0;
		long diff = day1.getTime() - day2.getTime();
		diff = Math.abs(diff)/1000;
		return Math.round(diff/(3600*24));
	}
	public static boolean isAfterOneHour(Date date, String time){
		String datetime = formatDate(date)+" "+time+":00";
		if(addHour(parseTimestamp(datetime),-1).after(getMillTimestamp())){
			return true;
		}
		return false;
	}
	public static boolean isValidDate(String fyrq) {
		return DateUtil.parseDate(fyrq)!=null;
	}
	
	public static final <T extends Date> String getAgendaDate(Date t , String pattern){
		if(t == null) return "";
		try{
			Long paramsTimes = getBeginningTimeOfDay(t).getTime();
			if(getCurDate().getTime() == paramsTimes) return "今天";
			if(addDay(getCurDate(), 1).getTime() == paramsTimes) return "明天";
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			return sdf.format(t);
		}catch(Exception e){
			return "";
		}
	}
	
	public static <T extends Date> long getCurDateMills(T date){
		if(date == null) return 0;
		return date.getTime();
	}
	
	/**
	 *  eg.  1997/01/02 22:03:00,return 1997/01/02 00:00:00.0
	 **/
	public static Timestamp getBeginTimestamp(Date date){
		return new Timestamp(getBeginningTimeOfDay(date).getTime());
	}
	public static Timestamp getEndTimestamp(Date date){
		return new Timestamp(getLastTimeOfDay(date).getTime());
	}
	public static Date getDateFromTimestamp(Timestamp timestamp){
		return new Date(getCurDateMills(timestamp));
	}
	
	public static int after(Date date1, Date date2){
		date1 = getBeginningTimeOfDay(date1);
		date2 = getBeginningTimeOfDay(date2);
		return date1.compareTo(date2);
	}
	public static Timestamp mill2Timestamp(Long mill){
		if(mill==null) return null;
		return new Timestamp(mill);
	}
	
	public static int subCurTimeSend(){
		Timestamp curtime = DateUtil.currentTimestamp();
		Timestamp endtime = DateUtil.getLastTimeOfDay(curtime);
		Long scopeSecond = DateUtil.getDiffSecond(endtime, curtime);
		return scopeSecond.intValue();
	}
	
	
	public static int getDiffMonth(Date date1, Date date2) {
		int iMonth = 0;
		int flag = 0;
		try {
			Calendar objCalendarDate1 = Calendar.getInstance();
			objCalendarDate1.setTime(date1);

			Calendar objCalendarDate2 = Calendar.getInstance();
			objCalendarDate2.setTime(date2);

			if (objCalendarDate2.equals(objCalendarDate1))
				return 0;
			if (objCalendarDate1.after(objCalendarDate2)) {
				Calendar temp = objCalendarDate1;
				objCalendarDate1 = objCalendarDate2;
				objCalendarDate2 = temp;
			}
			if (objCalendarDate2.get(Calendar.DAY_OF_MONTH) < objCalendarDate1
					.get(Calendar.DAY_OF_MONTH))
				flag = 1;

			if (objCalendarDate2.get(Calendar.YEAR) > objCalendarDate1
					.get(Calendar.YEAR))
				iMonth = ((objCalendarDate2.get(Calendar.YEAR) - objCalendarDate1
						.get(Calendar.YEAR))
						* 12 + objCalendarDate2.get(Calendar.MONTH) - flag)
						- objCalendarDate1.get(Calendar.MONTH);
			else
				iMonth = objCalendarDate2.get(Calendar.MONTH)
						- objCalendarDate1.get(Calendar.MONTH) - flag;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return iMonth;
	}
	
	/**
	 * 
	 * @return 
	 * @pattern yyyy-MM-dd HH:mm:ss
	 */
	public static String getUTCTime(){
		Timestamp curTimestamp = new Timestamp(System.currentTimeMillis());
		int minutes = getTimeZone();
		return formatTimestamp(addMinute(curTimestamp, minutes));
	}

	/**
	 *
	 * @param date
	 * @param month
	 * @return yyyy-MM-dd
	 */
	public static Date addMonth(Date date, int month) {
        String dateresult = null; // 返回的日期字符串
        // 创建格式化格式  
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        // 加减日期所用  
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date); // 得到gc格式的时间  
        gc.add(2,month); // 2表示月的加减，年代表1依次类推(周,天。。)  
        // 把运算完的时间从新赋进对象  
        gc.set(gc.get(gc.YEAR), gc.get(gc.MONTH), gc.get(gc.DATE));  
        // 在格式化回字符串时间  
        dateresult = df.format(gc.getTime());  
        return parseDate(dateresult);
	}

	/**
	 * @param date
	 * @param month
	 * @param pattern
	 * @return pattern 格式日期
	 */
	public static Date addMonthReturnAll(Date date, int month, String pattern) {
		String dateresult = null; // 返回的日期字符串
		// 创建格式化格式
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		// 加减日期所用
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date); // 得到gc格式的时间
		gc.add(2, month); // 2表示月的加减，年代表1依次类推(周,天。。)
		// 把运算完的时间从新赋进对象
		gc.set(gc.get(gc.YEAR), gc.get(gc.MONTH), gc.get(gc.DATE));
		// 在格式化回字符串时间
		dateresult = df.format(gc.getTime());
		return parseDate(dateresult, pattern);
	}

	public static String formatPromoDateStr(String dateStr){
		if(StringUtils.isEmpty(dateStr)) return "";
		Date date = parseDate(dateStr, PATTERN_TIMESTAMPE);
		String time1 = format(date, "M月d日");
		String week = getCnWeek(date);
		String time2 = format(date, "HH:mm");
		return time1+" "+week+" "+time2;
	}
	
	public static String formatEndTime(String dateStr){
		if(StringUtils.isEmpty(dateStr)) return "";
		Date endDate = parseDate(dateStr, PATTERN_TIMESTAMPE);
		Calendar calendar = Calendar.getInstance();
		Date curTime = calendar.getTime();
		if(!isAfter(endDate)) return "活动已结束";
		int day = getDiffDay(endDate, curTime);
		if(day == 0){
			double hour = getDiffHour(endDate, curTime);
			if(hour >=1) return Double.valueOf(hour).intValue()+"小时后结束";
			else{
				double minute = getDiffMinu(endDate, curTime);
				if(minute > 0) return Double.valueOf(hour).intValue()+"分钟后结束";
				return "活动已结束";
			}
		}else{
			return day+"天后结束";
		}
		
		
	}
	
	public static final <T extends Date> String getDateStr(Date t , String pattern){
		if(t == null) return "";
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			return sdf.format(t);
		}catch(Exception e){
			return "";
		}
	}
	
	private static int[] monthDays = {31,28,31,30,31,30,31,31,30,31,30,31};
	
	/**
	 * 获取当前月的天数
	 * @return
	 */
	public static int getDayOfMonth(int year,int month){
		int day = monthDays[month-1];
		if(month == 2){
			if((year%4 == 0 && year%100 != 0) || (year%100 == 0 && year%400 == 0)){
				day = 29;
			}
		}
		return day;
	}

	/**
	 *
	 * @param year
	 * @param month
	 * @param day
	 * @return yyyy-MM-dd
	 */
	public static String formatDate(int year, int month, int day){
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, day);
		return DateUtil.formatDate(calendar.getTime());
	}

	public static Date parseDate(int year, int month, int day){
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, day);
		return calendar.getTime();
	}

	/**
	 *
	 * @param dateStr
	 * @param parttern
	 * @return
	 */
	public static String formatDateStr(String dateStr, String parttern){
		return formatDateStr(parseTimestamp(dateStr),parttern);
	}

	public static String formatDateStr(Date date, String parttern){
		return DateUtil.format(date,parttern);
	}

}
