package kr.co.wincom.imcs.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import kr.co.wincom.imcs.common.property.ImcsProperties;
import kr.co.wincom.imcs.common.util.FileUtil;
import kr.co.wincom.imcs.common.util.GlobalCom;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.vo.ImageServerVO;
//import lguplus.nosqlcache.NosqlCacheType;
//import lguplus.nosqlcache.NosqlResultCache;
//import lguplus.nosqlcache.exceptions.NosqlCacheException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommonService {
	
	@Autowired
	private CommonDao commonDao;
	
	String SERVER_CONF = ImcsProperties.getProperty("filepath.imgcache");
	String SERVER_CONF2 = ImcsProperties.getProperty("filepath.vc_ch");
	String SERVER_CONF3 = ImcsProperties.getProperty("filepath.surtaxrate");
	String SERVER_CONF4 = ImcsProperties.getProperty("filepath.cachepath");
	String SERVER_CONF5 = ImcsProperties.getProperty("filepath.img_replace");
	//String SERVER_CONF6 = ImcsProperties.getProperty("filepath.img_replace.cache"); //2018.09.17 신규 설정 파일
	String SERVER_CONF6 = ImcsProperties.getProperty("filepath.img_replace"); //2018.09.17 신규 설정 파일
	String SERVER_CONF7 = ImcsProperties.getProperty("filepath.server"); //2020.02.10 신규 설정 파일
	
    Properties serverManager = GlobalCom.getPropertyFile(SERVER_CONF);
    Properties serverManager2 = GlobalCom.getPropertyFile(SERVER_CONF2);    
    Properties serverManager3 = GlobalCom.getPropertyFile(SERVER_CONF3);
    Properties serverManager4 = GlobalCom.getPropertyFile(SERVER_CONF4);
    Properties serverManager5 = GlobalCom.getPropertyFile(SERVER_CONF5);
    Properties serverManager7 = GlobalCom.getPropertyFile(SERVER_CONF7);
    
    private String comm = serverManager.getProperty("COMMON");
    private String comm2 = serverManager2.getProperty("COMMON");
    private String comm3 = serverManager3.getProperty("surtaxRate");
    private String comm4 = serverManager4.getProperty("LOCAL");
    private String comm5 = serverManager4.getProperty("NAS");
    private String comm6 = serverManager4.getProperty("CVTSNAS");
    private String comm7 = serverManager4.getProperty("COPY_LOCAL");
    private String comm8 = serverManager4.getProperty("COPY_NAS");
    
    /**
     * WAS 기동시 실행
     */
    @PostConstruct
    private void init()
    {    	
    	kr.co.wincom.imcs.common.util.GlobalCom.dbMcustUser = this.getServerProperties("db.account.mcustuser");
    	kr.co.wincom.imcs.common.util.GlobalCom.dbMconUser = this.getServerProperties("db.account.mconuser");
    	
    	// 혹시 몰라서.......
    	if(StringUtils.isBlank(kr.co.wincom.imcs.common.util.GlobalCom.dbMcustUser))
    		kr.co.wincom.imcs.common.util.GlobalCom.dbMcustUser = "MCUSTUSER";
    	
    	if(StringUtils.isBlank(kr.co.wincom.imcs.common.util.GlobalCom.dbMconUser))
    		kr.co.wincom.imcs.common.util.GlobalCom.dbMconUser = "MCONUSER";
    }
    
    
    //private String img_server = serverManager5.getProperty("img_server");	
    //private String img_resize_server = serverManager5.getProperty("img_resize_server");	
    //private String img_cat_server = serverManager5.getProperty("img_cat_server");	
    
    
    public String getImgReplaceUrl(String key){
    	
    	String result = "";
    	try{
	    	String img_server = serverManager5.getProperty(key);
	    	
	    	
	    	String[] img_server_arr =  img_server.split("[|]");
	    	
	    	if("0".equals(comm)){
	    		result = img_server_arr[0];
	    	}else{
	    		result = img_server_arr[1];
	    	}
    	} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return result;
    }
    
    public String getImgReplaceUrl2(String key, String apiName){
    	
    	String result = "";
    	try{
	    	String img_server = serverManager5.getProperty(key);
	    	
	    	
	    	String[] img_server_arr =  img_server.split("[|]");
	    	
	    	if("0".equals(comm)){
	    		String api_value = serverManager.getProperty(apiName);
	    		if (api_value==null || api_value.equals("")) {
	    			result = img_server_arr[0];
	    		} else {
	    			if("0".equals(api_value)){
	    				result = img_server_arr[0];
	    			} else {
	    				result = img_server_arr[1];
	    			}
	    		}	    		
	    	}else{
	    		result = img_server_arr[1];
	    	}
    	} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return result;
    }
    
    public String getImgReplaceCacheUrl(String key){
    	
    	String result = "";
    	try{
    		System.out.println("##########key :" + key);
			BufferedReader in = new BufferedReader(new FileReader(SERVER_CONF6));
			String img_server;
			String[] img_server_arr = new String[3];
			
			while ((img_server = in.readLine()) != null) {
				System.out.println("##########img_server :" + img_server);
			  if (img_server.indexOf(key) > -1) {
				  img_server_arr =  img_server.split("[|]");
			  }
			}
			in.close();
	    	
			System.out.println("##########comm: " + comm);
	    	if("0".equals(comm)){
	    		result = img_server_arr[1].split("[=]")[1];
	    	}else{
	    		result = img_server_arr[2].split("[=]")[1];
	    	}
	    	System.out.println("##########comm: " + comm);
	    	
    	} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return result;
    }

	/**
	 * 공용 이미지 URL
	 * @return 	ImageServerVO 		// 노드 IP1, 노드 IP2 노드 IP3
	 */	
	public String getIpInfo(String szServerName, String apiName) {
		
		String img = "http://";
		
		//System.out.println("apiName : "+apiName);
		
		ImageServerVO vo = new ImageServerVO();
		
		try {
			vo = this.commonDao.getIpInfo(szServerName);
			
			if(vo == null){
				vo = new ImageServerVO();
			}
			
			if("0".equals(comm)){
				img = vo.gettImgsvrip1();
			}else{
				
				String api_cfg = serverManager.getProperty(apiName);
				
				if("1".equals(api_cfg)){					
					img = vo.getDrImgsvrip();				
				}else{					
					img = vo.gettImgsvrip1();	
				}				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return img;
	}
	
	/**
	 * 오늘 날짜 시간 조회(YYYYMMDDHH24MISS)
	 * @return
	 */
	public String getSysdate(){
		return commonDao.getSysdate();
	}
	
	/**
	 * 오늘 날짜 시간 조회 (YYYYMMDD)
	 * @return
	 */
	public String getSysdateYMD(){
		return commonDao.getSysdateYMD();
	}
	
	/**
	 * 노드 IP 조회
	 * @return 	ImageServerVO 		// 노드 IP1, 노드 IP2 노드 IP3
	 */	
	public ImageServerVO getImgNodeIp(String saId, String stbMac){
		
		ImageServerVO vo = new ImageServerVO();		
		
		try {
			vo = this.commonDao.getImgNodeIp(vo);
			
			if(vo == null){
				vo = new ImageServerVO();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return vo;
	}
	
	
	/**
	 * cache validation
	 * @return 	ImageServerVO 		// 노드 IP1, 노드 IP2 노드 IP3
	 */	
	public boolean  chkCacheFile(String fileName, IMCSLog imcsLog){
		
		SimpleDateFormat fm = new SimpleDateFormat("yyyyMMddHHmmss");
		
		boolean reuslt = true;
		
		String msg = "";
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();


		String current_date =  getSysdate();
		
		if(!fileName.equals("1") && !fileName.equals("2")){
			String[] arr_file_date = fileName.split("_");
			
			Date file_date = null;
			Date cur_date = null;
			
			long diff_sec = 0;
			try {
				file_date = fm.parse(arr_file_date[1]);
				cur_date = fm.parse(current_date);

				
				diff_sec = (cur_date.getTime() / 1000) - (file_date.getTime()/ 1000);

				
				if(diff_sec >= Long.parseLong(serverManager4.getProperty(arr_file_date[0]))){
					reuslt = true;
				}else{
					reuslt = false;
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				msg = "ParseException : " + e.toString();
				imcsLog.serviceLog(msg, methodName, methodLine);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				msg = "Exception : " + e.toString();
				imcsLog.serviceLog(msg, methodName, methodLine);
			}
		}
//		}else if(fileName.equals("1") ){
//			reuslt = true;
//		}else if(fileName.equals("2")){
//			reuslt = false;
//		}
		
		return reuslt;
	}
	
	

	/**
	 * 에러메세지 조회 메소드
	 * @param errorCode
	 * @return
	 */
    public HashMap<String, String> getErrorMsg(int messageSet) {
    	HashMap<String, String> mResult = new HashMap<String, String>();
    	
    	String szErrCode	= "";
    	String szErrMsg		= "";
    	String szResultCode	= "";
    			
    	if( messageSet == 3 ) {
    		szErrCode	= "98";
    		szErrMsg	= "올바른 인앱구매가 아닙니다.";
    	} else if( messageSet == 9 ) {
    		szErrCode	= "14";
    		szErrMsg	= "가입자가 구입한 상품이 아닙니다.";
    		szResultCode= "20000014";
    	} else if( messageSet == 10 ) {
    		szErrCode	= "10";
    		szErrMsg	= "가입자 정보가 없습니다.";
    		szResultCode= "20000010";
    	} else if( messageSet == 11 ) {
    		szErrCode	= "11";
    		szErrMsg	= "가입자가 사용상태가 아닙니다.";
    		szResultCode= "20000011";
    	} else if( messageSet == 12 ) {
    		szErrCode	= "12";
    		szErrMsg	= "해피콜이 미완료 되었습니다.";
    		szResultCode= "20000012";
    	} else if( messageSet == 13 ) {
    		szErrCode	= "20";
    		szErrMsg	= "컨텐츠 타입 조회를 실패했습니다.";
    		szResultCode= "20000020";
    	} else if( messageSet == 14 ) {
    		szErrCode	= "13";
    		szErrMsg	= "가입자 상품 가입여부 조회를 실패했습니다.";
    		szResultCode= "20000013";
    	} else if( messageSet == 15 ) {
    		szErrCode	= "15";
    		szErrMsg	= "정의 되지 않은 구매 타입입니다.";
    	} else if( messageSet == 19 ) {
    		szErrCode	= "15";
    		szErrMsg	= "결제 차단된 사용자입니다.";
    	} else if( messageSet == 20 ) {
    		szErrCode	= "20";
    		szErrMsg	= "크레디트 한도금액을 초과하였습니다.";
    		szResultCode= "20000005";
    	} else if( messageSet == 21 ) {
    		szErrCode	= "21";
    		szErrMsg	= "쿠폰잔여갯수가 없습니다.";
    		szResultCode= "20000006";
    	} else if( messageSet == 22 ) {
    		szErrCode	= "30";
    		szErrMsg	= "기존 구매내역 조회를 실패했습니다.";
    		szResultCode= "20000030";
    	} else if( messageSet == 23 ) {
    		szErrCode	= "31";
    		szErrMsg	= "기존 보관함내역 조회를 실패했습니다.";
    	} else if( messageSet == 24 ) {	// 기구매의 경우 flag, buyingDate 별도 세팅
    		szErrCode	= "90";
    		szErrMsg	= "이미 구매하셨습니다.";
    		szResultCode= "20000002";
    	} else if( messageSet == 30 ) {
    		szErrCode	= "40";
    		szErrMsg	= "패키지상품 조회를 실패했습니다.";
    		szResultCode= "20000040";
    	} else if( messageSet == 31 ) {
    		szErrCode	= "41";
    		szErrMsg	= "패키지상품이 존재하지 않습니다.";
    		szResultCode= "20000041";
    	} else if( messageSet == 32 ) {
    		szErrCode	= "50";
    		szErrMsg	= "보관함에 등록되지 않았습니다.";
    		szResultCode= "20000050";
    	} else if( messageSet == 40 ) {
    		szErrCode	= "51";
    		szErrMsg	= "구매내역에 등록되지 않았습니다.";
    		szResultCode= "20000051";
    	} else if( messageSet == 44 ) {
    		szErrCode	= "04";
    		szErrMsg	= "쿠폰이 존재하지 않습니다.";
    		szResultCode= "20000004";
    	} else if( messageSet == 43 ) {
    		szErrCode	= "03";
    		szErrMsg	= "쿠폰 적용에 실패하였습니다.";
    		szResultCode= "20000003";
    	} else if( messageSet == 42 ) {
    		szErrCode	= "02";
    		szErrMsg	= "쿠폰 적용이 불가능합니다.";
    		szResultCode= "20000002";
    	} else if( messageSet == 41 ) {
    		szErrCode	= "01";
    		szErrMsg	= "쿠폰 적용일이 종료되었습니다.";
    		szResultCode= "20000001";
    	} else if( messageSet == 60 ) {
    		szErrCode	= "60";
    		szErrMsg	= "선물하기 불가능한 컨텐츠 입니다.";
    	} else if( messageSet == 61 ) {
    		szErrCode	= "61";
    		szErrMsg	= "선물하기 불가능한 결제수단 입니다.";
    	} else if( messageSet == 45 ) {
    		szErrCode	= "20";
    		szErrMsg	= "incorrect failed !!!";
    		szResultCode= "20000020";
    	} else if( messageSet == 90 ) {
    		szErrCode	= "21";
    		szErrMsg	= "장르구분 정보가 없습니다.";
    		szResultCode= "20000021";
    	} else if( messageSet == 98 ){
    		szErrCode	= "98";
    		szErrMsg	= "판매가 종료된 콘텐츠 입니다.";
    		szResultCode= "20000098";
    	} else {
    		szErrCode	= "99";
    		szErrMsg	= "incorrect failed !!!";
    		szResultCode= "20000090";
    	}
    	
    	mResult.put("ERR_CODE", szErrCode);
    	mResult.put("ERR_MSG", szErrMsg);
    	mResult.put("RESULT_CODE", szResultCode);
    	
		return mResult;
	}

	
    /**
	 * DataFree 에 사용하는 OneTimeKey 를 생성한다.
	 * @param saId - SA_ID
	 * @param albumId - 앨범 아이디
	 * @param pwd - DB에 있는 패스워드(개발DB는 lguplus 로 되어 있음.)
	 * @param encryptFlag - V : 가입번호 7~10번째 자리(4byte) + Asset ID 5~10번째자리(6byte) + 현재시각
	 *                      S : 가입번호 7~10번째 자리 + 서비스ID 1~3자리 + 현재시각
	 * @param serviceId - 서비스 아이디
	 * @return
	 * @throws Exception
	 */
	public String createOneTimeKey(String saId, String albumId, String pwd, String encryptFlag, String serviceId, IMCSLog imcsLog)
			throws Exception
	{
		String strOneTimeKey = "";
		String strTempSaId = "";
		String strTempMsg = "";
		
		String msg = "";
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		// APP, CDN 요청으로 인하여 가입번호가 10자리인 경우 가입번호 앞에 00 이라는 숫자 append
		if(saId.trim().length() == 10)
			strTempSaId = ("00" + saId);
		else
			strTempSaId = saId;
		
		msg = " PWD [" + pwd + "]";
		imcsLog.serviceLog(msg, methodName, methodLine);
		
		msg = " strTempSaId [" + strTempSaId + "]";
		imcsLog.serviceLog(msg, methodName, methodLine);
		
		msg = " albumId [" +albumId + "]";
		imcsLog.serviceLog(msg, methodName, methodLine);
		
		msg = " systemTime [" +String.valueOf(CommonService.getSystemTime(0)) + "]";
		imcsLog.serviceLog(msg, methodName, methodLine);
		
		msg = " encryptFlag [" +encryptFlag + "]";
		imcsLog.serviceLog(msg, methodName, methodLine);
		
		
		if(encryptFlag.equals("V"))
		{
			/* VOD인 경우 가입번호 7~10번째 자리(4byte) + Asset ID 5~10번째자리(6byte) + 현재시각으로 원본 데이터 생성	*/
			strTempMsg = strTempSaId.substring(6, 10) + albumId.substring(4, 10) + 
					String.valueOf(CommonService.getSystemTime(0));
		}
		else
		{
			/* 실시간 채널인 경우 가입번호 6~10번째 자리 + 서비스ID 1~3자리 + 현재시각으로 원본 데이터 생성	*/
			strTempMsg = strTempSaId.substring(6, 10) + serviceId.substring(0, 3) + 
					String.valueOf(CommonService.getSystemTime(0));
		}
		
		/*msg = " strTempMsg [" +strTempMsg + "]";
		imcsLog.serviceLog(msg, methodName, methodLine);*/
		
		// AES 256 으로 암호화
		strOneTimeKey = AESUtil.encrypt(pwd, strTempMsg);
		
		msg = " origin [" + strTempMsg + "] AES256 Enc and Base64Encoding result [" + strOneTimeKey + "] key[" + pwd + "]";
		imcsLog.serviceLog(msg, methodName, methodLine);
	    
	    return strOneTimeKey;
	}
	
	/**
	 * 시스템의 현재시간 - YYYYMMDDHHMMSS 형식
	 * @param hour - 마이너스는 이전 시간 플러스는 이후 시간
	 * @return
	 * @throws Exception
	 */
	public static long getSystemTime(int hour) throws Exception
	{
		Calendar cal = new GregorianCalendar(Locale.KOREA);
		cal.setTime(new Date());
		cal.add(Calendar.HOUR, hour); // 시간을 더한다
		
		SimpleDateFormat fm = new SimpleDateFormat("yyyyMMddHHmmss");
		String strDate = fm.format(cal.getTime());
		
		return Long.parseLong(strDate);
	}
	
	
	/**
	 * 가상채널 노출 여부
	 * @return 	String 		// 가상채널 노출 여부
	 */	
	public String getVCFlag(String apiName) {
		
		String VirtualChFlag = "0";
		
		try {

			
			if("1".equals(comm2)){
				VirtualChFlag = comm2;
			}else{
				
				String api_cfg = serverManager2.getProperty(apiName);
				
				VirtualChFlag = api_cfg;
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return VirtualChFlag;
	}
	
	/**
	 * 부가세 요율 조회
	 * @return 	String 	
	 */	
	public int getSurtaxRate() {
		
		int surtaxRate = 10;
		
		try {

			surtaxRate = Integer.parseInt(comm3);		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return surtaxRate;
	}
	
	/**
	 * 캐시 파일 경로
	 * @return 	String 		// 캐시 파일 경로
	 */	
	public String getCachePath(String pathName, String apiName, IMCSLog imcsLog) {
		
		String msg = "";
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		String cachePath = "/home/jeus/cache/";
		
		try {

			if("NAS".equals(pathName)){
				cachePath = comm5;
			}else if("CVTSNAS".equals(pathName)){
				cachePath = comm6;
			}else if("COPY_LOCAL".equals(pathName)){
				cachePath = comm7;
			}
			else if("COPY_NAS".equals(pathName)){
				cachePath = comm8;
			}
			else{
				cachePath = comm4;
			}			
			
			
			cachePath = cachePath +apiName;
			
			/*File f = new File(cachePath);
			
			if(!f.exists()){
				if(!f.mkdirs()){
					msg = " getCachePath Create Dir Fail !!  cachePath [" + cachePath + "] pathName [" + pathName + "] apiName[" + apiName + "]";
					imcsLog.serviceLog(msg, methodName, methodLine);
				}
			}
			*/
			
		} catch (Exception e) {
			msg = " getCachePath Fail pathName [" + pathName + "] apiName[" + apiName + "]";
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			//cachePath = "/home/jeus/cache/"+apiName;
		}
		
		return cachePath;
	}
	
	
//	public int getNosqlException(NosqlResultCache cacheInfo){
//		
//		int szRetCode = 0;
//		
//		if(cacheInfo == null)  cacheInfo = new NosqlResultCache();
//		
//		NosqlCacheType cacheType = cacheInfo.getLastExecutionCacheType();
//		NosqlCacheException cacheException = cacheInfo.getLastException();
//		
//		if(null != cacheException){
//			try{
//				NosqlCacheException NosqlEx =  (NosqlCacheException) cacheException;
//			
//				if(NosqlEx.getErrorCode() == NosqlCacheException.ECODE_COMMON_QUERY_EXECUTION_ERROR)				szRetCode = -2;
//				else if(NosqlEx.getErrorCode() == NosqlCacheException.ECODE_REDIS_EXECUTION_ERROR_IN_LONG_QUERY)	szRetCode = -3;
//				else																								szRetCode = -1;
//			}catch(Exception e){
//				szRetCode = -1;
//			}
//		} else if("HBASE_WR".equals(cacheType.toString()) || "USERDB".equals(cacheType.toString())) {
//			szRetCode	= 1;
//		}
//				
//		return szRetCode;
//	}
	
	/**
	 * IPv6_Prefix_IP 정보 조회
	 * @return 	String 		
	 */	
	public List<String> getIPv6PrefixIP(String apiName, String SearchKey, IMCSLog imcsLog) {
		
		String msg = "";
		String LOCALPATH      = "";
		String LOCAL_RESFILE		  = ""; //LOCAL res
		List<String> PrefixIP = new ArrayList<>();
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		String cachePath = "/home/jeus/cache/";
		
		try {
			cachePath = comm4;	
			
			cachePath = cachePath + apiName;
			
			LOCALPATH = this.getCachePath("LOCAL", apiName, imcsLog);
			
			LOCAL_RESFILE = String.format("%s/" + apiName + ".res", LOCALPATH); //Local res 파일
			
			File fLOCALRESFILE	   = new File(LOCAL_RESFILE);
			
			if(fLOCALRESFILE.exists()) {
				String result = FileUtil.fileRead(LOCAL_RESFILE, "UTF-8");

				if(!"".equals(result)) {
					String[] arrResult	= result.split(ImcsConstants.ROWSEP);			
					String[] SearchResult = null;
					result = "";
					
					for(int i = 0; i < arrResult.length; i++) {
						
						if(arrResult[i].startsWith(SearchKey + "|"))
						{
							result = arrResult[i].toString();
							SearchResult	= result.split(ImcsConstants.COLSEP_SPLIT);
							break;
						}
					}					
					try
					{
						result = SearchResult[1].toString();
						arrResult = result.split(ImcsConstants.KEY_VALUE_SPLIT);
						
						PrefixIP.add(arrResult[1].toString());
						
						result = SearchResult[2].toString();
						arrResult = result.split(ImcsConstants.KEY_VALUE_SPLIT);
						
						PrefixIP.add(arrResult[1].toString());
					}
					catch(Exception e)
					{
						throw new Exception(e);
					}
					
				}else{					
					PrefixIP.add("64:ff9b::");
					PrefixIP.add("2001:4430:E000::");
					msg = " File [" + LOCAL_RESFILE + "] is empty ";
					imcsLog.serviceLog(msg, methodName, methodLine);
				}	
			}else{
				PrefixIP.add("64:ff9b::");
				PrefixIP.add("2001:4430:E000::");
				msg = " File [" + LOCAL_RESFILE + "] not exist ";
				imcsLog.serviceLog(msg, methodName, methodLine);
			}			
		} catch (Exception e) {
			PrefixIP.add("64:ff9b::");
			PrefixIP.add("2001:4430:E000::");
			msg = " File [" + LOCAL_RESFILE + "] Exception [" + e.getMessage() + "]";
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return PrefixIP;
	}
	
	// 2019.10.16 - INPUT PARAM Valid Function
	// IN - 1 : 체크할 변수
	//      2 : 최소자리
	//      3 : 최대자리
	//      4 : 옵션 (0 - 체크하지 않음 
	//				1 - 알파벳/숫자/특수기호(.,) 인지 확인
	//				2 - 알파벳 인지 확인
	//				3 - 숫자 인지 확인
	// return - True (이상없음) / False (이상있음) 
	public boolean getValidParam(String param, int minLength, int maxLength, int option_flag)
	{		
	        String pattern = "^[ㄱ-ㅎ가-힣a-zA-Z0-9]*$";
	        boolean result = true;
	        
	        if(param.length() > maxLength || param.length() < minLength) return false;
	        
	        if(option_flag == 0)
	        {
	        	// 값을 체크하지 않는다.
	        }
	        else if(option_flag == 1)
	        {
	        	pattern = "^[a-zA-Z0-9.,]*$";
	        	result = Pattern.matches(pattern, param);
	        }
	        else if(option_flag == 2)
	        {
	        	pattern = "^[a-zA-Z]*$";
	        	result = Pattern.matches(pattern, param);
	        }
	        else if(option_flag == 3)
	        {
	        	pattern = "^[0-9]*$";
	        	result = Pattern.matches(pattern, param);
	        }
	        else
	        {
	        	result = false;
	        }
	        
	        return result;
	}
	
	/**
	 * server.properties 에 있는 설정값 반환
	 * 주로 getNSKidsList 관련 된 설정값만 사용할 것 같음.
	 * 
	 * COMMON.COMFLAG=0
	 * COMMON.SAFLAG=0
	 * 
	 * getNSKidsList.CFLAG=0
	 * getNSKidsList.SFLAG=0
	 * getNSKidsList.BOOKCATID=12000
	 * getNSKidsList.PARENTCATID=16001
	 * 
	 * @param key
	 * @return
	 */
    public String getServerProperties(String key){
    	
    	String result = "";
    	try{
    		result = serverManager7.getProperty(key);
    	} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return result;
    }
    
    /**
     * 개인정보 제어 Flag (0:개인정보 조회 / 1:개인정보 미조회<DEFAULT:>)
     * server.protertis 파일에서 읽어온다.
     * 
     * @return
     */
    public String getPersonalInfoConfig()
    {
    	String retVal = "1";
    	String SAFLAG = "";
    	String SFLAG = "";
    	
    	try {
    		SAFLAG = this.getServerProperties("COMMON.SAFLAG");
    		SFLAG = this.getServerProperties("getNSKidsList.SFLAG");
    		
    		if(SAFLAG.equals("0"))
    			retVal = SFLAG;
    		else
    			retVal = SAFLAG;
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    	return retVal;
    }
    
	/**
	 * 인자로 받은 문자열(str)의 idx 번째 위치값을 반환한다.
	 * indexOf 는 첫번째 위치만 반환해서 걍 만들었음.
	 * 
	 * @param str
	 * @param idx
	 * @return
	 * @throws Exception
	 */
	public int searchStringIndex(String strVal, String str, int idx) throws Exception
	{
		int cnt = 0;
		
		for(int i = 0; i < strVal.length(); i++)
		{
			String str2 = String.valueOf(strVal.charAt(i));
			
			if(str.equals(str2))
				cnt++;
			
			if(cnt == idx)
				return i;
		}
		
		return -1;
	}
	
	/**
	 * 인자로 받은 문자열(str)의 idx 번째 데이터를 반환한다.
	 * 
	 * @param strVal
	 * @param str
	 * @param idx - 1부터 시작임.
	 * @return
	 * @throws Exception
	 */
	public String getSearchStrData(String strVal, String str, int idx) throws Exception
	{
		String strData = "";
		int firIdx = -1;
		int secIdx = -1;
		
		firIdx = searchStringIndex(strVal, str, idx -1);
		secIdx = searchStringIndex(strVal, str, idx);
		
		if(firIdx == 0)
			strData = strVal.substring(firIdx, secIdx);
		else
			strData = strVal.substring(firIdx + 1, secIdx);
		
		return strData;
	}
}