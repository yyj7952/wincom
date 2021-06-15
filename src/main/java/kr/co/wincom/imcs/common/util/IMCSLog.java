package kr.co.wincom.imcs.common.util;

import kr.co.wincom.imcs.common.ImcsConstants;
//import lguplus.nosqlcache.NosqlCacheType;
//import lguplus.nosqlcache.NosqlResultCache;
//import lguplus.nosqlcache.exceptions.NosqlCacheException;

import org.apache.commons.logging.Log;
import org.springframework.util.StopWatch;

public class IMCSLog {
	
	public StopWatch mStopWatch = new StopWatch();
	private Log imcsLog;
	
	/**
	 * 가번/가맥 미입력 시 예와 같이 출력 됨 예)[-----:------------:--------------]
	 */
	private String sa_id	= "------------";
	private String stb_mac	= "--------------";
	private String pid		= "-----";
	
	/**
	 * 요청자 정보(IP,호출URL-Request객체)가 없을 시 사용하는 기본 생성자
	 * @param log_logger	사용할 로그객체를 넘겨준다.
	 */
	public IMCSLog(Log log_logger) {
		this.imcsLog = log_logger;
	}

	public IMCSLog(Log log_logger, String sa_id, String stb_mac, String pid) {
		this.imcsLog	= log_logger;
		this.sa_id		= sa_id;
		this.stb_mac	= stb_mac;
		this.pid		= pid;
	}
	

	/**
	 * 일반 로그 기록
	 * @param szMsg
	 * @param szMethodName
	 * @param szMethodLine
	 */
	public void serviceLog(String szMsg, String szMethodName, String szMethodLine) {
		StringBuilder makeStr = new StringBuilder();
		
		makeStr.append("[").append(String.format("%-33s", this.pid + ":" + this.sa_id + ":" + this.stb_mac)).append("]");
		
		if(!szMsg.equals(""))
			makeStr.append(szMsg);
		
		if(!szMethodName.equals("") && !szMethodLine.equals(""))
			makeStr.append(" [").append(szMethodName).append(":").append(szMethodLine).append("]");
		
		this.imcsLog.info(makeStr.toString());
	}
	
	
	/**
	 * 시간 기록을 위한 로그
	 * 로그 중 Time for 가 들어가는 로그에 대한 메소드
	 * ex) [2016/06/28 13:44:45] [28700:M01022338541:vv10.2233.8541] svc[119/getNSPurchased  ] sts[    1] msg[conts_info:no data found      ] [lgvod119.pc:1515]
	 */
	
	public void timeLog(String szMsg, String szTime, String szMethodName, String szMethodLine) {
		double nTime	= StringUtil.convertStrToInt(szTime) / 1000.0;
		
		// 한글의 경우 2byte를 차지하여 영문으로 들어왔을 떄와 공간 차지하는 것이 달라 넣은 로직
		int nLength		= 35;
		nLength			= nLength - (szMsg.getBytes().length - szMsg.length()) / 2;
		
		StringBuilder makeStr = new StringBuilder()
			.append("[").append(String.format("%-33s", this.pid + ":" + this.sa_id + ":" + this.stb_mac)).append("]")
			.append(String.format("%-" + nLength + "s", (" Time for " + szMsg)))
			.append(" [").append(nTime).append("] seconds")
			.append(" [").append(szMethodName).append(":").append(szMethodLine).append("]");

		this.imcsLog.info(makeStr.toString());
	}
	
	
	/**
	 * 시간 기록을 위한 로그
	 * 로그 중 Time for 가 들어가는 로그에 대한 메소드
	 * ex) [2016/06/28 13:44:45] [28700:M01022338541:vv10.2233.8541] svc[119/getNSPurchased  ] sts[    1] msg[conts_info:no data found      ] [lgvod119.pc:1515]
	 */
	public void failLog(String szApiId, String szSqlId, Object obj, String szMsg, String szMethodName, String szMethodLine) {
	String szStsInfo	= "    1";
	int nDotIdx			= StringUtil.nullToSpace(szMsg).indexOf(":");
	
	String szFrontMsg	= "";
	String szBackMsg	= "";
	
	if(nDotIdx < 0) {
		szMsg		= StringUtil.replaceNull(szBackMsg, ImcsConstants.RCV_MSG6); 
	} else {
		szFrontMsg	= szMsg.substring(0, szMsg.indexOf(":"));
		szMsg		= szMsg.substring(szMsg.indexOf(":") + 1, szMsg.length());
		szBackMsg	= StringUtil.replaceNull(szBackMsg, szMsg);
		
		szMsg		= szFrontMsg + ":" + szBackMsg;
	}
	
	
	StringBuilder makeStr = new StringBuilder();
	makeStr.append("[").append(String.format("%-33s", this.pid + ":" + this.sa_id + ":" + this.stb_mac)).append("] ");
	
	if(!"".equals(szApiId))
		makeStr.append("svc[" + String.format("%-20s", szApiId) + "] ");
	
	if(!"".equals(szSqlId))
		makeStr.append("SQLID[" + szSqlId + "] ");
	
	makeStr.append("sts[" + szStsInfo + "] ");
	makeStr.append("msg[" + szMsg + "] ");
	
	if(!szMethodName.equals("") && !szMethodLine.equals(""))
		makeStr.append(" [").append(szMethodName).append(":").append(szMethodLine).append("]");
	
	this.imcsLog.info(makeStr.toString());
}
	
//	public void failLog(String szApiId, String szSqlId, NosqlResultCache cacheInfo, String szMsg, String szMethodName, String szMethodLine) {
//		String szStsInfo	= "    1";
//		int nDotIdx			= StringUtil.nullToSpace(szMsg).indexOf(":");
//		
//		String szFrontMsg	= "";
//		String szBackMsg	= "";
//		
//		if(cacheInfo == null)	cacheInfo = new NosqlResultCache();
//		
//		if(cacheInfo.getLastException() != null) {
//			szStsInfo	= String.valueOf(cacheInfo.getLastException().getErrorCode());
//			szStsInfo	= String.format("%5s", szStsInfo);								// 5자리 맞춤
//			szBackMsg	= cacheInfo.getLastException().getErrorMessage();
//		}
//		
//		if(nDotIdx < 0) {
//			szMsg		= StringUtil.replaceNull(szBackMsg, ImcsConstants.RCV_MSG6); 
//		} else {
//			szFrontMsg	= szMsg.substring(0, szMsg.indexOf(":"));
//			szMsg		= szMsg.substring(szMsg.indexOf(":") + 1, szMsg.length());
//			szBackMsg	= StringUtil.replaceNull(szBackMsg, szMsg);
//			
//			szMsg		= szFrontMsg + ":" + szBackMsg;
//		}
//		
//		
//		StringBuilder makeStr = new StringBuilder();
//		makeStr.append("[").append(String.format("%-33s", this.pid + ":" + this.sa_id + ":" + this.stb_mac)).append("] ");
//		
//		if(!"".equals(szApiId))
//			makeStr.append("svc[" + String.format("%-20s", szApiId) + "] ");
//		
//		if(!"".equals(szSqlId))
//			makeStr.append("SQLID[" + szSqlId + "] ");
//		
//		makeStr.append("sts[" + szStsInfo + "] ");
//		makeStr.append("msg[" + szMsg + "] ");
//		
//		if(!szMethodName.equals("") && !szMethodLine.equals(""))
//			makeStr.append(" [").append(szMethodName).append(":").append(szMethodLine).append("]");
//		
//		this.imcsLog.info(makeStr.toString());
//	}
	
	
	
	/**
	 * 디비 결과 로그
	 */
	public void dbLog(String szApiId, String szSqlId, String szLastHostName, int querySize, String szMethodName, String szMethodLine) {
	String szDbInfo		= "";
	String szCacheType	= "";
	
	String szHostName	= szLastHostName;
	
	String szHostNum	= "00";
	String szRetCode	= "1";
	    	
	if(szHostName != null && szHostName.length() > 1)
		szHostNum	= 	szHostName.substring(szHostName.length() - 2, szHostName.length());
	
	// TB 일경우 그 앞에 2문자 재조회 (개발에서만 적용될듯)
	if("TB".equals(szHostNum))		
		szHostNum	= 	szHostName.substring(szHostName.length() - 4, szHostName.length() - 2);
			
	szRetCode	= "1";
	
	// rcv[HBASE01:0:3] 형식으로 만들기 위한 로직
	szCacheType	= "ORACLE";
	
	szDbInfo	= szCacheType + ":" + szRetCode + ":" + querySize;
    		
	StringBuilder makeStr = new StringBuilder();
	makeStr.append("[").append(String.format("%-33s", this.pid + ":" + this.sa_id + ":" + this.stb_mac)).append("] ");
	
	if(!"".equals(szApiId))
		makeStr.append("svc[" + String.format("%-20s", szApiId) + "] ");
	
	if(!"".equals(szSqlId))
		makeStr.append("SQLID[" + szSqlId + "] ");
	
	makeStr.append("rcv[" + szDbInfo + "] ");
	
	if(!szMethodName.equals("") && !szMethodLine.equals(""))
		makeStr.append(" [").append(szMethodName).append(":").append(szMethodLine).append("]");
	
	this.imcsLog.info(makeStr.toString());
}
	
//	public void dbLog(String szApiId, String szSqlId, NosqlResultCache cacheInfo, int querySize, String szMethodName, String szMethodLine) {
//		String szDbInfo		= "";
//		String szCacheType	= "";
//		
//		if(cacheInfo == null)  cacheInfo = new NosqlResultCache();
//		
//		String szHostName	= cacheInfo.getLastHostName();
//		NosqlCacheType cacheType = cacheInfo.getLastExecutionCacheType();
//		NosqlCacheException cacheException = cacheInfo.getLastException();
//		
//		String szHostNum	= "00";
//		String szRetCode	= "1";
//		    	
//    	if(szHostName != null && szHostName.length() > 1)
//    		szHostNum	= 	szHostName.substring(szHostName.length() - 2, szHostName.length());
//    	
//    	// TB 일경우 그 앞에 2문자 재조회 (개발에서만 적용될듯)
//    	if("TB".equals(szHostNum))		
//    		szHostNum	= 	szHostName.substring(szHostName.length() - 4, szHostName.length() - 2);
//    			
//    			
//    	if(null != cacheException){
//			try{
//				NosqlCacheException NosqlEx =  (NosqlCacheException) cacheException;
//			
//				if(NosqlEx.getErrorCode() == NosqlCacheException.ECODE_COMMON_QUERY_EXECUTION_ERROR)				szRetCode = "-2";
//				else if(NosqlEx.getErrorCode() == NosqlCacheException.ECODE_REDIS_EXECUTION_ERROR_IN_LONG_QUERY)	szRetCode = "-3";
//				else																								szRetCode = "-1";
//			}catch(Exception e){
//				szRetCode = "-1";
//			}
//		} else if("HBASE_WR".equals(cacheType.toString()) || "USERDB".equals(cacheType.toString())) {
//			szRetCode	= "1";
//		}
//    	
//    	// rcv[HBASE01:0:3] 형식으로 만들기 위한 로직
//    	if("USERDB".equals(cacheType.toString()))			szCacheType	= "ORACLE";
//    	else if("HBASE_WR".equals(cacheType.toString()))	szCacheType	= "ORACLE";
//    	else if("HBASE_DB".equals(cacheType.toString()))	szCacheType	= "ORA_HBS" + szHostNum;
//    	else if("REDIS_DB".equals(cacheType.toString()))	szCacheType	= "ORA_RDS" + szHostNum;
//    	else												szCacheType = cacheType.toString() + szHostNum;
//    	
//    	szDbInfo	= szCacheType + ":" + szRetCode + ":" + querySize;
//	    		
//		StringBuilder makeStr = new StringBuilder();
//		makeStr.append("[").append(String.format("%-33s", this.pid + ":" + this.sa_id + ":" + this.stb_mac)).append("] ");
//		
//		if(!"".equals(szApiId))
//			makeStr.append("svc[" + String.format("%-20s", szApiId) + "] ");
//		
//		if(!"".equals(szSqlId))
//			makeStr.append("SQLID[" + szSqlId + "] ");
//		
//		makeStr.append("rcv[" + szDbInfo + "] ");
//		
//		if(!szMethodName.equals("") && !szMethodLine.equals(""))
//			makeStr.append(" [").append(szMethodName).append(":").append(szMethodLine).append("]");
//		
//		this.imcsLog.info(makeStr.toString());
//	}
	
	
	
	/**
	 * 통합통계로그
	 * @param szStat	통합통계데이터
	 */
	public void statLog(String szStat) {
		StringBuilder makeStr = new StringBuilder();

		makeStr.append(szStat);
		this.imcsLog.info(makeStr.toString());
	}


	
	/**	
	 * 처리도중 진행중인 요청을 중지시키지는 않으나 문제소지가 될 수 있는 에러발생 시 찍는 로그 메소드
	 * @param info[0]		기능별 ID	API명과 같은 각 기능별 구분ID
	 * @param info[1]		보여주고자 하는 정보메시지
	 */
	public void errorLog(String... info) {
		StringBuilder makeStr = new StringBuilder().append("[").append(String.format("%-9s", "ERROR")).append("][");
		makeStr.append(String.format("%-35s", this.sa_id+":"+this.stb_mac)).append("]");
		
		for(String subStr : info){
			makeStr.append("[").append(subStr).append("]");
		}
		
		this.imcsLog.error(makeStr.toString());
	}

	/**
	 * Debug로그를 찍는 로그 메소드
	 * @param info[0]		기능별 ID	API명과 같은 각 기능별 구분ID
	 * @param info[1]		보여주고자 하는 정보메시지
	 */
	public void debugLog(String... info) {
		StringBuilder makeStr = new StringBuilder();
		
		makeStr.append("[").append(String.format("%-9s", "DEBUG")).append("]")
		       .append("[").append(String.format("%-35s", this.sa_id + ":"+this.stb_mac)).append("]");
		
		for(String subStr : info){
			makeStr.append("[").append(subStr).append("]");
		}
		
		this.imcsLog.trace(makeStr.toString());
	}
	
	public void dbLog2(String szApiId, String szSqlId, String szLastHostName, int querySize, String szMethodName, String szMethodLine) {
	String szDbInfo		= "";
	String szCacheType	= "";
	
	String szHostName	= szLastHostName;
	
	String szHostNum	= "00";
	String szRetCode	= "1";
	    	
	if(szHostName != null && szHostName.length() > 1)
		szHostNum	= 	szHostName.substring(szHostName.length() - 2, szHostName.length());
	
	// TB 일경우 그 앞에 2문자 재조회 (개발에서만 적용될듯)
	if("TB".equals(szHostNum))		
		szHostNum	= 	szHostName.substring(szHostName.length() - 4, szHostName.length() - 2);
			
	szRetCode	= "1";
	
	// rcv[HBASE01:0:3] 형식으로 만들기 위한 로직
	szCacheType	= "ORACLE";
	
	szDbInfo	= szCacheType + ":" + szRetCode + ":" + querySize;
    		
	StringBuilder makeStr = new StringBuilder();
	makeStr.append("[").append(String.format("%-33s", this.pid + ":" + this.sa_id + ":" + this.stb_mac)).append("] ");
	
	if(!"".equals(szApiId))
		makeStr.append("svc[" + String.format("%-20s", szApiId) + "] ");
	
	if(!"".equals(szSqlId))
		makeStr.append("SQLID[" + szSqlId + "] ");
	
	makeStr.append("rcv[" + szDbInfo + "] ");
	
	if(!szMethodName.equals("") && !szMethodLine.equals(""))
		makeStr.append(" [").append(szMethodName).append(":").append(szMethodLine).append("]");
	
	this.imcsLog.trace(makeStr.toString());
}
	
//	public void dbLog2(String szApiId, String szSqlId, NosqlResultCache cacheInfo, int querySize, String szMethodName, String szMethodLine) {
//		String szDbInfo		= "";
//		String szCacheType	= "";
//		
//		if(cacheInfo == null)  cacheInfo = new NosqlResultCache();
//		
//		String szHostName	= cacheInfo.getLastHostName();
//		NosqlCacheType cacheType = cacheInfo.getLastExecutionCacheType();
//		NosqlCacheException cacheException = cacheInfo.getLastException();
//		
//		String szHostNum	= "00";
//		String szRetCode	= "1";
//		    	
//    	if(szHostName != null && szHostName.length() > 1)
//    		szHostNum	= 	szHostName.substring(szHostName.length() - 2, szHostName.length());
//    	
//    	// TB 일경우 그 앞에 2문자 재조회 (개발에서만 적용될듯)
//    	if("TB".equals(szHostNum))		
//    		szHostNum	= 	szHostName.substring(szHostName.length() - 4, szHostName.length() - 2);
//    			
//    			
//    	if(null != cacheException){
//			try{
//				NosqlCacheException NosqlEx =  (NosqlCacheException) cacheException;
//			
//				if(NosqlEx.getErrorCode() == NosqlCacheException.ECODE_COMMON_QUERY_EXECUTION_ERROR)				szRetCode = "-2";
//				else if(NosqlEx.getErrorCode() == NosqlCacheException.ECODE_REDIS_EXECUTION_ERROR_IN_LONG_QUERY)	szRetCode = "-3";
//				else																								szRetCode = "-1";
//			}catch(Exception e){
//				szRetCode = "-1";
//			}
//		} else if("HBASE_WR".equals(cacheType.toString()) || "USERDB".equals(cacheType.toString())) {
//			szRetCode	= "1";
//		}
//    	
//    	// rcv[HBASE01:0:3] 형식으로 만들기 위한 로직
//    	if("USERDB".equals(cacheType.toString()))			szCacheType	= "ORACLE";
//    	else if("HBASE_WR".equals(cacheType.toString()))	szCacheType	= "ORACLE";
//    	else if("HBASE_DB".equals(cacheType.toString()))	szCacheType	= "ORA_HBS" + szHostNum;
//    	else if("REDIS_DB".equals(cacheType.toString()))	szCacheType	= "ORA_RDS" + szHostNum;
//    	else												szCacheType = cacheType.toString() + szHostNum;
//    	
//    	szDbInfo	= szCacheType + ":" + szRetCode + ":" + querySize;
//	    		
//		StringBuilder makeStr = new StringBuilder();
//		makeStr.append("[").append(String.format("%-33s", this.pid + ":" + this.sa_id + ":" + this.stb_mac)).append("] ");
//		
//		if(!"".equals(szApiId))
//			makeStr.append("svc[" + String.format("%-20s", szApiId) + "] ");
//		
//		if(!"".equals(szSqlId))
//			makeStr.append("SQLID[" + szSqlId + "] ");
//		
//		makeStr.append("rcv[" + szDbInfo + "] ");
//		
//		if(!szMethodName.equals("") && !szMethodLine.equals(""))
//			makeStr.append(" [").append(szMethodName).append(":").append(szMethodLine).append("]");
//		
//		this.imcsLog.trace(makeStr.toString());
//	}

}
