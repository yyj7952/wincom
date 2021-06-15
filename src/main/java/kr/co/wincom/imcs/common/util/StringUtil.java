package kr.co.wincom.imcs.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtil {

	public static String getCurDttm(String i_sFormat) {
		SimpleDateFormat sdf = null;
		sdf = new SimpleDateFormat(i_sFormat);
		return sdf.format(new java.util.Date());
	}

	
	
	
	/**
	 * null 값을 공백으로 변환
	 * @param str 처리 할 문자열
	 * @return String
	 */
	public static String nullToSpace(String str){ 
		if (str == null || str.trim().equals("") || str.equals("null")) {
			return "";
		}
		
		return str.trim();
	}
	
	/**
     * null을 0으로 변환
     * @param str 처리 할 문자열 
     * @return String
     */
	public static String nullToZero(String str){
		try{
			if (str == null || str.trim().equals("") || str.equals("null")){
				return "0";
			}
			
			Integer.parseInt(str);
		} catch(Exception e) {
			return "0";
		}
		
		return str.trim();
	}
	
	
	
	/**
	 * strDate2가 strDate1보다 이전이거나 같을 경우에만 true
	 *
	 * @param pattern
	 * @param strDate1
	 * @param strDate2
	 * @return
	 */
	public boolean getDateComparison(String pattern, String strDate1, String strDate2) {
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);

		Date sDate1;
		Date sDate2;

		boolean returnValue = false;

		try {
			//formatter.set2DigitYearStart(new Date());
			sDate1 = formatter.parse(strDate1);
			sDate2 = formatter.parse(strDate2);

			if (sDate1.before(sDate2)) {
				returnValue = false;
			} else {
				returnValue = true;
			}
		} catch (ParseException e) {
			returnValue = false;
		}

		return returnValue;
	}
	
	/**
	 * String객체가 Null이면 지정값으로 반환한다.
	 *
	 * @param		String형 객체
	 * @return		변환 객체
	 */
	public static String replaceNull( String value, String convValue ) {
		String rslt = value;
		if( rslt == null || rslt.length() == 0 ) rslt = convValue;

		return rslt;
	}
	
	/**
     * 문자열의 첫글자로 변환하여 반환
     * @param		String
     * @return		String
     */
    public static String toFirstUpperCase(String str) {
    	if( isEmpty( str ) ){
    		return str;
    	}
    	String upperChar = subString(str, 0, 1).toUpperCase();
        return new String( upperChar+ subString(str, 1) );
    }
    
	/**
     * null 또는 빈값 체크
     * @param		String
     * @return		String
     */
    public static boolean isEmpty (String data) {
    	return( data == null || data.length() == 0 );
    }
    
    
    
    public static String subString( String value, int startIdx, int endIdx ) {
		String result = "";
		if( isEmpty( value ) )	return "";
		if( value.length() < startIdx )		return "";
		
		if( value.length() < endIdx )		result = value.substring( startIdx );
		else								result = value.substring( startIdx, endIdx );
		
		return result;
	}
    
    public static String subString( String value, int startIdx ) {
		String result = "";
		if( isEmpty( value ) )	return "";
		if( value.length() < startIdx )		return "";

		result = value.substring( startIdx, value.length() );

		return result;
	}

	/**
     * 문자열을 숫자로 변경
     * @param		String
     * @return		int
     */
    public static int convertStrToInt(String str) {
    	if (str == null) {
			return 0;
		}
    	try {
    		return Integer.parseInt(str);
    	} catch(NumberFormatException nfe) {
    		return 0;
    	}
    }
}
