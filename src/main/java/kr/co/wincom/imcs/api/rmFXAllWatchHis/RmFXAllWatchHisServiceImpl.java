package kr.co.wincom.imcs.api.rmFXAllWatchHis;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.nosql.NoSQLRedisDao;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.handler.ImcsException;
import kr.co.wincom2.imcs.api.rmFXAllWatchHis.RmFXAllWatchHisDao2;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import kr.co.wincom.imcs.common.NosqlCacheType;

@Service
public class RmFXAllWatchHisServiceImpl implements RmFXAllWatchHisService {
	private Log imcsLogger		= LogFactory.getLog("API_rmFXAllWatchHis");
	
	@Autowired
	private RmFXAllWatchHisDao rmFXAllWatchHisDao;
	
	@Autowired
	private RmFXAllWatchHisDao2 rmFXAllWatchHisDao2;
	
	@Autowired
	private NoSQLRedisDao noSQLRedisDao;
	
//	public void rmFXAllWatchHis(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	
	@Override
	public RmFXAllWatchHisResultVO rmFXAllWatchHis(RmFXAllWatchHisRequestVO paramVO){
//		this.rmFXAllWatchHis(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String isLastProcess = ImcsConstants.RCV_MSG5;

		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		RmFXAllWatchHisResultVO resultListVO = new RmFXAllWatchHisResultVO();

	    String msg		= "";	// 로그 메세지
		
	    Integer nRetVal		= -99;
		
	    long tp_start = paramVO.getTp_start();
		long tp1 = 0;
	    
		try{
			nRetVal = this.rmFXAllWatchHisUpdate(paramVO);
			
			if( nRetVal == null) {
				nRetVal = 0;
    		} 
			
			tp1	= System.currentTimeMillis();
			imcsLog.timeLog("시청목록 삭제", String.valueOf(tp1 - tp_start), methodName, methodLine); 
			
			if(nRetVal >= 0){
				resultListVO.setFlag("0");
				resultListVO.setErrMsg("시청목록 삭제되었습니다.");
				
				//2019.03.08 권형도 수정 (tpacall rmFXWatchHis 로직)
				try {
					int ret = this.rmFXAllWatchHisUpdate2(paramVO);
					
					imcsLog.serviceLog(msg, methodName, methodLine);
				} catch(Exception e) {
					msg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID120) + "] rmFXWatchHisDao2 [PT_VO_WATCH_HISTORY] table Failed at";
					imcsLog.serviceLog(msg, methodName, methodLine);

					nRetVal = -1;
				}
				
			} else {
				resultListVO.setFlag("1");
				resultListVO.setErrMsg("시청목록 삭제가 실패하였습니다.");
				
				throw new ImcsException(resultListVO.getFlag(), resultListVO.getErrMsg());
			}
			
			if(nRetVal <= 0)
			{
				resultListVO.setFlag("1");
				resultListVO.setErrMsg("시청목록 삭제가 실패하였습니다.");
				
				throw new ImcsException(resultListVO.getFlag(), resultListVO.getErrMsg());
			}
		}catch(ImcsException ie) {
			ie.setFlag(resultListVO.getFlag());
			ie.setMessage(resultListVO.getErrMsg());
			
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			
			imcsLog.errorLog(methodName + "-E", ie.getClass().getName() + ":" + ie.getMessage());
			throw ie;
		} catch(Exception e) {
			resultListVO.setFlag("1");
			resultListVO.setErrMsg("시청목록 삭제가 실패하였습니다.");
			
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			throw new ImcsException(resultListVO.getFlag(), resultListVO.getErrMsg());
		} finally {
			msg = " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID120) + "] sts[" + ImcsConstants.LOG_MSG3 + "]" 
					+ " snd[FLAG=" + resultListVO.getFlag() + "|MESSAGE=" + resultListVO.getErrMsg() + "|]";
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID120) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);			
		}
		
		return resultListVO;
	}

	
	/**
	 * 가입자 전체 시청목록 삭제 (DEL_YN 업데이트)
	 * @param	RmNSWatchHisRequestVO
	 * @result	Integer
	 */
    public Integer rmFXAllWatchHisUpdate(RmFXAllWatchHisRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
    	
    	String sqlId =  "fxvod120_u01_20171214_001";
		
		int querySize = 0;
		
		String szMsg = "";
		    	
    	try {

    		try{
				querySize = rmFXAllWatchHisDao.rmFXAllWatchHisUpdate(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			try {
				//imcsLog.dbLog(ImcsConstants.FXAPI_PRO_ID120, sqlId, null, querySize, methodName, methodLine);

				if(querySize > 0) {
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID120) + "] update [PT_VO_WATCH_HISTORY_NSC] table[" + querySize + "] records Success at";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
				} else {
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID120) + "] update [PT_VO_WATCH_HISTORY_NSC] table Failed at";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
					
					//imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID120, sqlId, null, "fail info:" + ImcsConstants.RCV_MSG3 , methodName, methodLine);
				}
			} catch (Exception e) {}
			
		} catch (Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID120) + "] update [PT_VO_WATCH_HISTORY_NSC] table Failed at";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			//imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID120, sqlId, null, "fail info:" + ImcsConstants.RCV_MSG6 , methodName, methodLine);
		}
		
		return querySize;
    	
    }
    
	/**
	 * 가입자 전체 시청목록 삭제 (DEL_YN 업데이트) / DB분리  rmFXAllWatchHis 구현
	 * @param	RmNSWatchHisRequestVO
	 * @result	Integer
	 */
    public Integer rmFXAllWatchHisUpdate2(RmFXAllWatchHisRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
    	
    	String sqlId =  "fxvod120_u01_20171214_001";
		
		int querySize = 0;
		
//		NosqlResultCache cache = new NosqlResultCache();
//		List<Object> binds = new ArrayList<Object>();
//		VersionUpdateCheckKey checkKey = new VersionUpdateCheckKey();
		
		String szMsg = "";
		    	
    	try {
			try{
				querySize = rmFXAllWatchHisDao2.rmFXAllWatchHisUpdate(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			try {
				//imcsLog.dbLog(ImcsConstants.FXAPI_PRO_ID120, sqlId, null, querySize, methodName, methodLine);

				if(querySize > 0) {
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID120) + "] update [PT_VO_WATCH_HISTORY] table[" + querySize + "] records Success at";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
				} else {
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID120) + "] update [PT_VO_WATCH_HISTORY] table Failed at";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
					
					//imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID120, sqlId, null, "fail info:" + ImcsConstants.RCV_MSG3 , methodName, methodLine);
				}
			} catch (Exception e) {}
			
		} catch (Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID120) + "] update [PT_VO_WATCH_HISTORY] table Failed at";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			//imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID120, sqlId, null, "fail info:" + ImcsConstants.RCV_MSG6 , methodName, methodLine);
		}
		
		return querySize;
    	
    }
}
