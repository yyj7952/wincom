package kr.co.wincom.curation.common.validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.util.StringUtils;

public class ValidationParameter {
	

	/**
	 * 단순히 값의 존재여부만 체크할 경우 해당 메소드 사용
	 * @param param		체크할 대상인 입력받은 값을 넘겨준다.
	 * @return true:이상없음, false:제약사항에 위배됨 
	 */
	public static boolean CheckEmpty(String param){
		
		if(StringUtils.hasText(param)){
			return true;
		}

		return false;
	}
	
	/**
	 * Y와 N만을 허용하는 항목일 경우 해당 메소드 사용
	 * @param param		체크할 대상인 입력받은 값을 넘겨준다.
	 * @param isEmpty	빈값 허용여부 - 해당 값이 true일 경우 빈값도 허용한다. false일 경우 빈값을 허용하지 않는다.
	 * @return true:이상없음, false:제약사항에 위배됨 
	 */
	public static boolean CheckYn(String param, boolean isEmpty){
		
		if(isEmpty && !StringUtils.hasText(param)){
			return true;
		}else{
			if("Y".equals(param) || "N".equals(param)){
				return true;
			}
		}

		return false;
	}
	
	/**
	 * 최소 사이즈가 정해져 있는 항목일 경우 체크
	 * @param param		체크할 대상인 입력받은 값을 넘겨준다.
	 * @param length	최소사이즈를 입력한다.
	 * @return
	 */
	public static boolean CheckMinLength(String param, int length){
		
		if(param.length() < length) return false;

		return true;
	}
	
	/**
	 * API별로 같은 속성의 파라메터가 존재하더라도 체크할 값이 달라질 수 있는 경우 해당 메소드 사용(체크할 문자를 입력받아 체크한다.)
	 * @param param		체크할 대상인 입력받은 값을 넘겨준다.
	 * @param validList	허용할 값 리스트 - 허용할 문자 리스트를 넘겨받아 체크한다.
	 * @param isEmpty	빈값 허용여부 - 해당 값이 true일 경우 빈값도 허용한다. false일 경우 빈값을 허용하지 않는다.
	 * @return true:이상없음, false:제약사항에 위배됨 
	 */
	public static boolean CheckValid(String param, String[] validList, boolean isEmpty){
		
		if(isEmpty && !StringUtils.hasText(param)){
			return true;
		}else{
			for(String str : validList){
				if(str.equals(param)){
					return true;
				}
			}
		}

		return false;
	}
	
	/**
	 * app_type[어플타입(RABC)]에 대한 체크
	 * 넘어오는 값의 맨 앞자리가 지정 문자일 경우에만 허용
	 * @param param		체크할 대상인 입력받은 값을 넘겨준다.
	 * @param isEmpty	빈값 허용여부 - 해당 값이 true일 경우 빈값도 허용한다. false일 경우 빈값을 허용하지 않는다.
	 * @return true:이상없음, false:제약사항에 위배됨 
	 */
	public static boolean CheckApp_type(String param, boolean isEmpty){
		
		//허용문자
		String[] checkChar = {"C", "D"};
		
		if(isEmpty && !StringUtils.hasText(param)){
			return true;
		}else{
			if(StringUtils.hasText(param)){
				for(String str : checkChar){
					if(param.startsWith(str)){
						return true;
					}
				}
			}
		}

		return false;
	}
	
	/**
	 * defin_flag[사용자 화질 설정 FLAG]에 대한 체크
	 * 해당 번호일 경우나 값이 없을 경우만 허용
	 * @param param		체크할 대상인 입력받은 값을 넘겨준다.
	 * @param isEmpty	빈값 허용여부 - 해당 값이 true일 경우 빈값도 허용한다. false일 경우 빈값을 허용하지 않는다.
	 * @return true:이상없음, false:제약사항에 위배됨 
	 */
	public static boolean CheckDefin_flag(String param, boolean isEmpty){
		
		//허용문자 1:HD, 3:SD
		String[] checkChar = {"1", "3"};
		
		if(isEmpty && !StringUtils.hasText(param)){
			return true;
		}else{
			for(String str : checkChar){
				if(str.equals(param)){
					return true;
				}
			}
		}

		return false;
	}
	
	/**
	 * net_type[망 타입]에 대한 체크
	 * 해당 번호일 경우나 값이 없을 경우만 허용
	 * @param param		체크할 대상인 입력받은 값을 넘겨준다.
	 * @param isEmpty	빈값 허용여부 - 해당 값이 true일 경우 빈값도 허용한다. false일 경우 빈값을 허용하지 않는다.
	 * @return true:이상없음, false:제약사항에 위배됨 
	 */
	public static boolean CheckNet_type(String param, boolean isEmpty){
		
		//허용문자 02:광랜, 01:HFC-100M, 31:HFC-10M
		String[] checkChar = {"01", "02", "31"};
		
		if(isEmpty && !StringUtils.hasText(param)){
			return true;
		}else{
			for(String str : checkChar){
				if(str.equals(param)){
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * svc_type[Service Type]에 대한 체크
	 * 해당 번호일 경우에만 허용
	 * @param param		체크할 대상인 입력받은 값을 넘겨준다.
	 * @param isEmpty	빈값 허용여부 - 해당 값이 true일 경우 빈값도 허용한다. false일 경우 빈값을 허용하지 않는다.	 * 
	 * @return true:이상없음, false:제약사항에 위배됨 
	 */
	public static boolean CheckSvc_type(String param, boolean isEmpty){
		//허용문자 1:채널진입, 2:다음회차, 3:시청이력 진입, 4:구매이력 진입, 5:통합검색 진입
		String[] checkChar = {"1", "2", "3", "4", "5"};
		
		if(isEmpty && !StringUtils.hasText(param)){
			return true;
		}else{
			for(String str : checkChar){
				if(str.equals(param)){
					return true;
				}
			}
		}

		return false;
	}
	
	/**
	 * request_type[재생경로]에 대한 체크
	 * 해당 번호일 경우에만 허용
	 * @param param		체크할 대상인 입력받은 값을 넘겨준다.
	 * @param isEmpty	빈값 허용여부 - 해당 값이 true일 경우 빈값도 허용한다. false일 경우 빈값을 허용하지 않는다.	 * 
	 * @return true:이상없음, false:제약사항에 위배됨 
	 */
	public static boolean CheckRequest_type(String param, boolean isEmpty){
		
		//1 : VOD 채널, 2 : 시청목록, 3 : 구매목록, 4: 통합검색
		String[] checkChar = {"1", "2", "3", "4"};
		
		if(isEmpty && !StringUtils.hasText(param)){
			return true;
		}else{
			for(String str : checkChar){
				if(str.equals(param)){
					return true;
				}
			}
		}

		return false;
	}
	
	/**
	 * buying_type[구매타입]에 대한 체크
	 * 해당 문자일 경우에만 허용
	 * @param param		체크할 대상인 입력받은 값을 넘겨준다.
	 * @param isEmpty	빈값 허용여부 - 해당 값이 true일 경우 빈값도 허용한다. false일 경우 빈값을 허용하지 않는다.	 * 
	 * @return true:이상없음, false:제약사항에 위배됨 
	 */
	public static boolean CheckBuying_type(String param, boolean isEmpty){
		
		//B : 일반구매, S : 소액결제, P : 카드포인트
		String[] checkChar = {"B", "S", "P"};
		
		if(isEmpty && !StringUtils.hasText(param)){
			return true;
		}else{
			for(String str : checkChar){
				if(str.equals(param)){
					return true;
				}
			}
		}

		return false;
	}
	
	/**
	 * 데이터형식이 맞는지 체크
	 * @param param 체크할 대상인 입력받은 값을 넘겨준다.
	 * @param formatString 체크할 format값
	 * @param isEmpty	빈값 허용여부 - 해당 값이 true일 경우 빈값도 허용한다. false일 경우 빈값을 허용하지 않는다.	 * 
	 * @return true:이상없음, false:제약사항에 위배됨
	 */
	public static boolean CheckDateFormat(String param, String formatString, boolean isEmpty){
		if(isEmpty && !StringUtils.hasText(param)){
			return true;
		}else{
			if( StringUtils.hasText(param) && StringUtils.hasText(formatString) ){
				if( param.length() != formatString.length() ){
					return false;
				}else{
					SimpleDateFormat format = new SimpleDateFormat(formatString);
					try {
						format.parse(param);
					} catch (ParseException e) {
						return false;
					}
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * 숫자형만 허용하는 항목일 경우 해당 메소드 사용
	 * @param param		체크할 대상인 입력받은 값을 넘겨준다.
	 * @param isEmpty	빈값 허용여부 - 해당 값이 true일 경우 빈값도 허용한다. false일 경우 빈값을 허용하지 않는다.
	 * @return true:이상없음, false:제약사항에 위배됨 
	 */
	public static boolean CheckNumber(String param, boolean isEmpty){

		if(isEmpty && !StringUtils.hasText(param)){
			return true;
		}else{
			try{
				Double.parseDouble(param);
				return true;
			}catch(NumberFormatException e){
				return false;
			}
		}
		
	}
	
	/**
	 * r_grade[R등급보기여부]에 대한 체크
	 * 해당 번호일 경우나 값이 없을 경우만 허용
	 * @param param		체크할 대상인 입력받은 값을 넘겨준다.
	 * @param isEmpty	빈값 허용여부 - 해당 값이 true일 경우 빈값도 허용한다. false일 경우 빈값을 허용하지 않는다.
	 * @return true:이상없음, false:제약사항에 위배됨 
	 */
	public static boolean CheckR_grade(String param, boolean isEmpty){
		
		//허용문자 Y:등급만 보기, N:R등급제외, A:전체보기
		String[] checkChar = {"Y", "N", "A"};
		
		if(isEmpty && !StringUtils.hasText(param)){
			return true;
		}else{
			for(String str : checkChar){
				if(str.equals(param)){
					return true;
				}
			}
		}

		return false;
	}
}
