package kr.co.wincom.imcs.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author medialog
 * @date 2015. 5. 27.
 * kr.co.wincom.imcs.common.util.DateUtils
 */
public class DateUtil {
	private static final Locale currentLocale = new Locale("KOREAN", "KOREA");
	
	/**
	 * <pre>
	 * 현재시간 가져오기 
	 * </pre>
	 * @author medialog
	 * @date 2015. 5. 27.
	 * @method getCustomTodayFormat
	 * @param format(yyyyMMddHHmmssSSS)
	 * @return String
	 */
	public String getCustomTodayFormat(String fmt) {
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(fmt, currentLocale);
		return sdf.format(dt);
	}
	
	/**
	 * <pre>
	 * Date 객체를 특정 Format 으로 변환하기 
	 * </pre>
	 * @author Dreambug
	 * @date 2015. 11. 09.
	 * @method getCustomDateFormat
	 * @param format(yyyyMMddHHmmssSSS)
	 * @return String
	 */
	public String getCustomDateFormat(Date dt, String fmt) {
		SimpleDateFormat sdf = new SimpleDateFormat(fmt, currentLocale);
		return sdf.format(dt);
	}
	
	/**
	 * <pre>
	 * 현재날짜 기준 시간을 조작한다. 
	 * </pre>
	 * @author medialog
	 * @date 2015. 7. 3.
	 * @method setHourOperationCustomFormat
	 * @return String
	 */
	public String setHourOperationCustomFormat(String fmt, int hour) {
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(new Date().getTime());
		calendar.add(Calendar.HOUR_OF_DAY, hour);

		SimpleDateFormat sdf = new SimpleDateFormat(fmt, currentLocale);
		return sdf.format(calendar.getTime());
	}
	
	/**
	 * <pre>
	 * 현재날짜 기준 시간을 조작한다. 
	 * </pre>
	 * @author medialog
	 * @date 2015. 7. 3.
	 * @method setHourOperationCustomFormat
	 * @return String
	 */
	public String setHourOperationCustomFormat(Date date, String fmt, int hour) {
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date.getTime());
		calendar.add(Calendar.HOUR_OF_DAY, hour);

		SimpleDateFormat sdf = new SimpleDateFormat(fmt, currentLocale);
		return sdf.format(calendar.getTime());
	}
	
	/**
	 * <pre>
	 * 날짜 문자열을 입력받아 시간을 가감한 후, 입력받은 포맷형태로 반환
	 * </pre>
	 * @author Dreambug
	 * @date 2015. 7. 24.
	 * @method setHourOperationCustomFormat
	 * @return String
	 * @throws ParseException 입력받은 날짜와 format 이 서로 다를 경우 예외처리
	 */
	public String setHourOperationCustomFormat(String strDate, String fmt, int hour) throws ParseException {
		
		Calendar calendar = Calendar.getInstance();
		
		SimpleDateFormat sdf = new SimpleDateFormat(fmt, currentLocale);
		Date date = sdf.parse(strDate);
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, hour);
		
		return sdf.format(calendar.getTime());
	}
	
	/**
	 * <pre>
	 * String형의 날짜를 Date형의 날짜로 변환 
	 * </pre>
	 * @author medialog
	 * @date 2015. 7. 17.
	 * @method getStringToDate
	 * @return Date
	 */
	public Date getStringToDate(String date, String fmt){
		Date dt;
		try {
			DateFormat formatter = new SimpleDateFormat(fmt);
			dt = (Date)formatter.parse(date);
		} catch (ParseException e) {
			return new Date();
		}
		
		return dt;
	}
	
	/**
	 * 문자열 형태의 날짜를 원하는 형태로 변환합니다.
	 * 
	 * 예시)
	 * "yyyy.MM.dd G 'at' HH:mm:ss z"	2001.07.04 AD at 12:08:56 PDT
	 * "EEE, MMM d, ''yy"	Wed, Jul 4, '01
	 * "h:mm a"	12:08 PM
	 * "hh 'o''clock' a, zzzz"	12 o'clock PM, Pacific Daylight Time
	 * "K:mm a, z"	0:08 PM, PDT
	 * "yyyyy.MMMMM.dd GGG hh:mm aaa"	02001.July.04 AD 12:08 PM
	 * "EEE, d MMM yyyy HH:mm:ss Z"	Wed, 4 Jul 2001 12:08:56 -0700
	 * "yyMMddHHmmssZ"	010704120856-0700
	 * "yyyy-MM-dd'T'HH:mm:ss.SSSZ"	2001-07-04T12:08:56.235-0700
	 * 
	 * @param date 변환할 날짜
	 * @param fromFormatString 변환될 포맷
	 * @param toFormatString 변환할 포맷
	 * @return 변환된 날짜 문자열
	 */
	public String formattedDate(String date, String fromFormatString, String toFormatString)
	{
		SimpleDateFormat fromFormat =
			new SimpleDateFormat(fromFormatString, currentLocale);
		SimpleDateFormat toFormat =
			new SimpleDateFormat(toFormatString, currentLocale);
		Date fromDate = null;
		
		try
		{
			fromDate = fromFormat.parse(date);
		}
		catch(ParseException e)
		{
			fromDate = new Date();
		}
		
		return toFormat.format(fromDate);
	}
	
}
