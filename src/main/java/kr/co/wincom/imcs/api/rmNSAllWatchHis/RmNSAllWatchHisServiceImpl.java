package kr.co.wincom.imcs.api.rmNSAllWatchHis;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kr.co.wincom.imcs.api.rmNSWatchHis.RmNSWatchHisRequestVO;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.nosql.NoSQLRedisDao;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.handler.ImcsException;
import kr.co.wincom.imcs.common.NosqlCacheType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class RmNSAllWatchHisServiceImpl implements RmNSAllWatchHisService {
	private Log imcsLogger		= LogFactory.getLog("API_rmNSAllWatchHis");
	
	@Autowired
	private RmNSAllWatchHisDao rmNSAllWatchHisDao;
	
	@Autowired
	private NoSQLRedisDao noSQLRedisDao;
	
//	public void rmNSAllWatchHis(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	
	@Override
	public RmNSAllWatchHisResultVO rmNSAllWatchHis(RmNSAllWatchHisRequestVO paramVO){
//		this.rmNSAllWatchHis(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String isLastProcess = ImcsConstants.RCV_MSG5;

		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		RmNSAllWatchHisResultVO resultListVO = new RmNSAllWatchHisResultVO();

		String flag		= "";
		String errMsg	= "";
	    String msg		= "";	// 로그 메세지
		
	    Integer nRetVal		= -99;
		
	    long tp_start = paramVO.getTp_start();
		long tp1 = 0;
	    
		try{
			try {
				nRetVal = this.rmNSAllWatchHisUpdate(paramVO);
			} catch(Exception e){
				throw new ImcsException();
			}	
			
			try {
				nRetVal = this.updateSetTime(paramVO);
			} catch(Exception e){
				throw new ImcsException();
			}	
		
//			if( nRetVal == null) {
//				nRetVal = 0;
//    		} 
			
			tp1	= System.currentTimeMillis();
			imcsLog.timeLog("시청목록 삭제", String.valueOf(tp1 - tp_start), methodName, methodLine); 
			
			if(nRetVal > 0){
				flag = "0";
				errMsg = "시청목록 삭제되었습니다.";
			} else {
				flag = "1";
				errMsg = "시청목록 삭제가 실패하였습니다.";
				
				throw new ImcsException(flag, errMsg, "");
			}
		}catch(ImcsException ie) {
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			
			imcsLog.errorLog(methodName + "-E", ie.getClass().getName() + ":" + ie.getMessage());
			ie.setFlag(flag);
			ie.setMessage(errMsg);
			throw ie;
		} catch(Exception e) {
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			resultListVO.setFlag("1");
			resultListVO.setErrMsg("시청목록 삭제가 실패하였습니다.");
			
			new ImcsException(resultListVO.getFlag(), resultListVO.getErrMsg());
		} finally {
			resultListVO.setFlag(flag);
			resultListVO.setErrMsg(errMsg);
			resultListVO.setResultCode(paramVO.getResultCode());
			
			/*msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID358) + "] sts[" + ImcsConstants.LOG_MSG3 + "]" 
					+ " snd[FLAG=" + resultListVO.getFlag() + "|MESSAGE=" + resultListVO.getErrMsg() + "|]";
			imcsLog.serviceLog(msg, methodName, methodLine);*/
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID358) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultListVO;
	}

	
	/**
	 * 가입자 전체 시청목록 삭제 (DEL_YN 업데이트)
	 * @param	RmNSAllWatchHisRequestVO
	 * @result	Integer
	 */
    public Integer rmNSAllWatchHisUpdate(RmNSAllWatchHisRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
    	
    	String sqlId =  "lgvod358_u01_20171214_001";
		
		int querySize = 0;
		
		String szMsg = "";
		    	
    	try {
			try{
				rmNSAllWatchHisDao.rmNSAllWatchHisUpdate(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			try {
				//imcsLog.dbLog(ImcsConstants.API_PRO_ID358, sqlId, null, querySize, methodName, methodLine);

				if(querySize > 0) {
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID358) + "] update [PT_VO_WATCH_HISTORY_NSC] table[" + querySize + "] records Success at";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
				} else {
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID358) + "] update [PT_VO_WATCH_HISTORY_NSC] table Failed at";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
					
					imcsLog.failLog(ImcsConstants.API_PRO_ID358, "", null, "fail info: no data found", methodName, methodLine);
				}
			} catch (Exception e) {}
			
		} catch (Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID358) + "] update [PT_VO_WATCH_HISTORY_NSC] table Failed at";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			//imcsLog.failLog(ImcsConstants.API_PRO_ID358, "", null, "fail info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
		return querySize;
    	
    }
    
	/**
	 *  이어보기 시간 갱신
	 *  @param	RmNSAllWatchHisRequestVO
	 *  @result	int
	 */
	public int updateSetTime(RmNSAllWatchHisRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
    	
    	String sqlId =  "lgvod348_u02_20190208_001";
		
		int querySize = 0;
		
		String szMsg = "";
		    	
    	try {
    		try{
    			querySize =rmNSAllWatchHisDao.updateSetTime(paramVO);
    			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			try{
				//imcsLog.dbLog(ImcsConstants.API_PRO_ID358, sqlId, null, querySize, methodName, methodLine);

				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID358) + "] update [PT_VO_SET_TIME_PTT_NSC] table[" + querySize + "] records Success at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
		return querySize;
	}
}
