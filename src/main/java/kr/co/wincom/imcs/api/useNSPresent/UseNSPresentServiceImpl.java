package kr.co.wincom.imcs.api.useNSPresent;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.nosql.NoSQLRedisDao;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.handler.ImcsException;
//import lguplus.nosqlcache.CacheableExecutor;
//import lguplus.nosqlcache.NosqlCacheType;
//import lguplus.nosqlcache.NosqlResultCache;
//import lguplus.nosqlcache.RowKeyList;
//import lguplus.nosqlcache.VersionUpdateCheckKey;
//import lguplus.nosqlcache.VersionUpdateExcutor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import kr.co.wincom.imcs.common.NosqlCacheType;

@Service
public class UseNSPresentServiceImpl implements UseNSPresentService {
	private Log imcsLogger = LogFactory.getLog("API_useNSPresent");
	
	@Autowired
	private UseNSPresentDao useNSPresentDao;
	
	@Autowired
	private NoSQLRedisDao noSQLRedisDao;
	
	@Autowired
	private CommonService commonService;

//	public void useNSPresent(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	
	@Override
	public UseNSPresentResultVO useNSPresent(UseNSPresentRequestVO paramVO){
//		this.useNSPresent(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		UseNSPresentResultVO resultVO = new UseNSPresentResultVO();
		
		// NoSQL Db별 쿼리 카운터
		//paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		String flag		= "";
		String errMsg	= "";
		
	    Integer nResultSet = -1;
	    
	    String msg	= "";

	    long tp_start = paramVO.getTp_start();
		long tp1 = 0;
	    
		try{
			String szCurrentDate = "";
			
			try {
				szCurrentDate = commonService.getSysdate();
	    	} catch (Exception e) {
	    		imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage() + "," + methodName + "_getSysdate()");
				//throw new ImcsException(ImcsConstants.FAIL_CODE, e);
			}
			paramVO.setCurrentDate(szCurrentDate);
			
			
			// 구매상품과 중복 여부 체크
			Integer nDupChk = 0;
			String sqlId	= "lgvod314_s01_20171214_001";
			
			try {
				nDupChk= useNSPresentDao.getPresentDupCk(paramVO);    
				
				//imcsLog.dbLog(ImcsConstants.API_PRO_ID314, sqlId, null, nDupChk, methodName, methodLine);
			} catch (Exception e) {
				nResultSet	= -1;
				
				//imcsLog.failLog(ImcsConstants.API_PRO_ID314, "", null, "present_Dup_Chk:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			}
			
			
			if(nResultSet == -1){
				// 기구매 체크 실패시 에러 메시지 필요 여부??
			}
			
			// 내가 구매한 컨텐츠중 선물과 동일한 앨범의 유효한 앨범이 존재하는 경우 경고!!
			if(nDupChk > 0){
				resultVO.setFlag("2");
				resultVO.setErrMsg("유효한  컨텐츠가 존재합니다. 사용처리가 실패하였습니다.");
				paramVO.setResultCode("20000002");
				
				throw new ImcsException(resultVO.getFlag(), resultVO.getErrMsg(), "");
			}else{
				// 이벤트타입 정보조회
				String szEventType = this.getEventType(paramVO);
				paramVO.setEventType(szEventType);
				
				nResultSet = paramVO.getResultSet();
				
				if(nResultSet == 0){
					int result = 0;
					
					// VOD 선물 사용
					result = this.usePresent(paramVO);
					
					if(result == 1)    		nResultSet = 0;		// 선물사용 성공
			    	else		    		nResultSet = -1;	// 선물사용 실패
			    	
					tp1	= System.currentTimeMillis();
					imcsLog.timeLog("선물 사용 여부 Update", String.valueOf(tp1 - tp_start), methodName, methodLine); 
					
				}
				
				if(nResultSet == 0){
					resultVO.setFlag("0");
			    	resultVO.setErrMsg("선물 사용처리가 완료되었습니다.");
				}else{
					resultVO.setFlag("1");
			    	resultVO.setErrMsg("선물 사용처리가 실패하였습니다.");
			    	paramVO.setResultCode("20000001");
			    	
			    	throw new ImcsException(resultVO.getFlag(), resultVO.getErrMsg(), "");
				}
			}
			
		}catch(ImcsException ie) {
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			
			imcsLog.errorLog(methodName + "-E",ie.getClass().getName()+":"+ie.getMessage());
			throw ie;
		} catch(Exception e) {			
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			
			flag	= "1";
			errMsg	= "선물 사용처리가 실패하였습니다.";		
			
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			throw new ImcsException(flag, errMsg, "");
		} finally{
			msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID314) + "]" + String.format("%-5s",  " sts[" + ImcsConstants.LOG_MSG3 + "]") 
					+ " snd[FLAG=" + resultVO.getFlag() + "|MESSAGE=" + resultVO.getErrMsg() + "|]";
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID314) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
			
		}
		
		return resultVO;
	}
	
	
	
	
	/**
	 * 이벤트타입 정보조회
	 * @param 	UseNSPresentRequestVO
	 * @return	String szEventType
	 */
	public String getEventType(UseNSPresentRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId	= "lgvod314_001_20171214_001";
		String szEventType	= "";
		
		List<String> list   = new ArrayList<String>();
		
		
		try {
			try{
				list  = useNSPresentDao.getEventType(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list != null && !list.isEmpty()){
				szEventType = StringUtil.nullToSpace(list.get(0));
			}
			
		} catch (Exception e) {
			paramVO.setResultSet(-1);
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			throw new ImcsException();
		}
		
    	return szEventType;
    }
	
	
    /**
     * VOD 선물사용
     * @param 	int
     * @return	UseNSPresentRequestVO
     */
	public int usePresent(UseNSPresentRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
    	
    	String sqlId =  "lgvod314_u01_20171214_001";
		
		int querySize = 0;
		
		String szMsg = "";
		    	
    	try {
			try{
				querySize = useNSPresentDao.useNSPresentUpdate(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			try {
				//imcsLog.dbLog(ImcsConstants.API_PRO_ID314, sqlId, null, querySize, methodName, methodLine);
				
				if( querySize == 0 ){
					//imcsLog.failLog(ImcsConstants.API_PRO_ID314, "", cache, "fail info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
				} else {
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID314) + "] update [PT_VO_PRESENT] table[" + querySize + "] records Success at";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
				}
			} catch (Exception e) {}
			
		} catch (Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID314) + "] update [PT_VO_PRESENT] table Failed at";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			//imcsLog.failLog(ImcsConstants.API_PRO_ID314, "", cache, "fail info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			
		}
		
		return querySize;
	}
}
