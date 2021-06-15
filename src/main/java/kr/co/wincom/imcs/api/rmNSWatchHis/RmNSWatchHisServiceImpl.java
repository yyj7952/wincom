package kr.co.wincom.imcs.api.rmNSWatchHis;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kr.co.wincom.imcs.api.setNSPassedTime.SetNSPassedTimeRequestVO;
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
public class RmNSWatchHisServiceImpl implements RmNSWatchHisService {
	private Log imcsLogger = LogFactory.getLog("API_rmNSWatchHis");
	
	@Autowired
	private RmNSWatchHisDao rmNSWatchHisDao;
	
	@Autowired
	private NoSQLRedisDao noSQLRedisDao;
	

//	public void rmNSWatchHis(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	@Override
	public RmNSWatchHisResultVO rmNSWatchHis(RmNSWatchHisRequestVO paramVO){
//		this.rmNSWatchHis(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String isLastProcess = ImcsConstants.RCV_MSG5;

		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		RmNSWatchHisResultVO resultListVO = new RmNSWatchHisResultVO();

		String flag		= "";
		String errMsg	= "";
	    String msg		= "";	// 로그 메세지
		
	    Integer nRetVal		= -99;
	    
	    long tp_start = paramVO.getTp_start();
		long tp1 = 0;
	    
		try{
			for(int i = 0; i < paramVO.getContentsList().size(); i++) {
				paramVO.setContentId(paramVO.getContentsList().get(i));
				
				// 시청목록 삭제 (DEL_YN 업데이트)
				try {
					nRetVal = this.rmNSWatchHisUpdate(paramVO);
				} catch (Exception e) {
					throw new ImcsException();
				}
				
				nRetVal = this.updateSetTime(paramVO);
				
			}
			
			tp1	= System.currentTimeMillis();
			imcsLog.timeLog("시청목록 삭제", String.valueOf(tp1 - tp_start), methodName, methodLine); 
			
			if(nRetVal > 0){
				flag 	= "0";
				errMsg	= "시청목록 삭제되었습니다.";
			} else {
				flag	= "1";
				errMsg	= "시청목록 삭제가 실패하였습니다.";
				paramVO.setResultCode("20000001");
				//throw new ImcsException(flag, errMsg);
			}
		} catch(ImcsException ie) {
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			
			resultListVO.setFlag(ie.getFlag());
			resultListVO.setErrMsg(ie.getMessage());
			imcsLog.errorLog(methodName + "-E", ie.getClass().getName() + ":" + ie.getMessage());
			throw ie;
		} catch(Exception e) {
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			
			resultListVO.setFlag("1");
			resultListVO.setErrMsg("시청목록 삭제가 실패하였습니다.");
			
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			new ImcsException(resultListVO.getFlag(), resultListVO.getErrMsg());
		} finally{
			resultListVO.setFlag(flag);
			resultListVO.setErrMsg(errMsg);
			resultListVO.setResultCode(paramVO.getResultCode());
			
			msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID348) + "]" + String.format("%-5s", " sts[" + ImcsConstants.LOG_MSG3 + "]") 
					+ " snd[FLAG=" + flag + "|MESSAGE=" + errMsg + "|]";
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			//msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID348) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
			//		+ " [DR:" + nDRedis + "|DH:" + nDHbase + "|R:" + nRedis + "|H:" + nHbase + "]";
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID348) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultListVO;
	}

	
	/**
	 * 시청목록 삭제 (DEL_YN 업데이트)
	 * @param	RmNSWatchHisRequestVO
	 * @result	Integer
	 */
    public Integer rmNSWatchHisUpdate(RmNSWatchHisRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
    	
    	String sqlId = "lgvod348_u01_20190208_001";
		String szMsg = "";
		
		int querySize = 0;
		
//		NosqlResultCache cache = new NosqlResultCache();
//		List<Object> binds = new ArrayList<Object>();
//		VersionUpdateCheckKey checkKey = new VersionUpdateCheckKey();
		
		    	
    	try {
//			checkKey.addVersionTuple("PT_VO_WATCH_HISTORY", paramVO.getSaId());
//			binds.add(paramVO);
//			
//			querySize = cache.updateWithCacheVersion(new VersionUpdateExcutor() {
//				@Override
//				public int execute(List<Object> parameters) throws SQLException {
//					try{
//						RmNSWatchHisRequestVO newInput = (RmNSWatchHisRequestVO)parameters.get(0);
//						return rmNSWatchHisDao.rmNSWatchHisUpdate(newInput);
//					}catch(DataAccessException e){
//						//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
//						throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
//					}
//				}
//			}, binds, checkKey);
//			
//			// HABSE 버전 업 쿼리이므로 ORA_HBS COUNT + 1
//			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.HBASE_DB.ordinal()]++;
    		
			try{
				querySize = rmNSWatchHisDao.rmNSWatchHisUpdate(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			try {
				//imcsLog.dbLog(ImcsConstants.API_PRO_ID348, sqlId, null, querySize, methodName, methodLine);
	
				if(querySize == 0) {
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID348) + "] update [PT_VO_WATCH_HISTORY_NSC] table Failed at";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
					
					//imcsLog.failLog(ImcsConstants.API_PRO_ID348, "", null, "fail info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);	
				} else {
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID348) + "] update [PT_VO_WATCH_HISTORY_NSC] table[" + querySize + "] records Success at";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
				}
			} catch (Exception e) {}
			
		} catch (Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID348) + "] update [PT_VO_WATCH_HISTORY_NSC] table Failed at";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			//imcsLog.failLog(ImcsConstants.API_PRO_ID348, "", null, "fail info:" + ImcsConstants.RCV_MSG6 , methodName, methodLine);
		}
		
		return querySize;
    }
    
	/**
	 *  이어보기 시간 갱신
	 *  @param	RmNSWatchHisRequestVO
	 *  @result	int
	 */
	public int updateSetTime(RmNSWatchHisRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
    	
    	String sqlId =  "lgvod348_u02_20190208_001";
		
		int querySize = 0;
		
		String szMsg = "";
		    	
    	try {
    		try{
    			querySize =rmNSWatchHisDao.updateSetTime(paramVO);
    			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			try{
				//imcsLog.dbLog(ImcsConstants.API_PRO_ID348, sqlId, null, querySize, methodName, methodLine);

				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID348) + "] update [PT_VO_SET_TIME_PTT_NSC] table[" + querySize + "] records Success at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
		return querySize;
	}
}
