package kr.co.wincom.imcs.api.rmFXAllFavor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.nosql.NoSQLRedisDao;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.handler.ImcsException;
import kr.co.wincom2.imcs.api.rmFXAllFavor.RmFXAllFavorDao2;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import kr.co.wincom.imcs.common.NosqlCacheType;

@Service
public class RmFXAllFavorServiceImpl implements RmFXAllFavorService {
	private Log imcsLogger = LogFactory.getLog("API_rmFXAllFavor");
	
	@Autowired
	private RmFXAllFavorDao rmFXAllFavorDao;

	@Autowired
	private RmFXAllFavorDao2 rmFXAllFavorDao2;

	@Autowired
	private NoSQLRedisDao noSQLRedisDao;
	

//	public void rmFXAllFavor(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	@Override
	public RmFXAllFavorResultVO rmFXAllFavor(RmFXAllFavorRequestVO paramVO){
//		this.rmFXAllFavor(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String isLastProcess = ImcsConstants.RCV_MSG5;

		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		RmFXAllFavorResultVO resultListVO = new RmFXAllFavorResultVO();

		String flag		= "";
		String errMsg	= "";
	    String msg		= "";	// 로그 메세지
		
	    Integer nRetVal		= -99;
	    
	    long tp_start = paramVO.getTp_start();
		long tp1 = 0, tp2 = 0;
	    
		try{
			tp1 = System.currentTimeMillis();
			imcsLog.timeLog("init...", String.valueOf(tp1 - tp_start), methodName, methodLine);
				
			try {
				nRetVal = this.deleteAllFavor(paramVO);
			} catch(Exception e) {}

			nRetVal = paramVO.getResultSet();
			
			if (nRetVal >= 0) {
				this.deleteAllFavorDAO2(paramVO);
			}
			
			tp1	= System.currentTimeMillis();
			imcsLog.timeLog("찜목록 삭제", String.valueOf(tp1 - tp_start), methodName, methodLine); 
			
			if(nRetVal >= 0){
				resultListVO.setFlag("0");
				resultListVO.setErrMsg("찜목록 삭제되었습니다.");
			} else {
				throw new ImcsException();
			}
		} catch(ImcsException ie) {
			resultListVO.setFlag("1");
			resultListVO.setErrMsg("찜목록 삭제가 실패하였습니다.");
			
			ie.setFlag(resultListVO.getFlag());
			ie.setMessage(resultListVO.getErrMsg());
			
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			
			imcsLog.errorLog(methodName + "-E", ie.getClass().getName() + ":" + ie.getMessage());
			throw ie;
		} catch(Exception e) {
			resultListVO.setFlag("1");
			resultListVO.setErrMsg("찜목록 삭제가 실패하였습니다.");
			
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			throw new ImcsException(flag, errMsg, "");
		} finally{
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("COMMIT || ROLLBACK", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			msg = " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID080) + "]" + String.format("%-5s", " sts[" + ImcsConstants.LOG_MSG3 + "]") 
					+ " snd[FLAG=" + resultListVO.getFlag() + "|MESSAGE=" + resultListVO.getErrMsg() + "|]";
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID080) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultListVO;
	}

	
	/**
	 * 찜목록 전체 삭제
	 * @param	RmFXAllFavorRequestVO
	 * @result	Integer
	 */
    public Integer deleteAllFavor(RmFXAllFavorRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
    	
    	String sqlId = "fxvod080_d01_20171214_001";
		String szMsg = "";
		
		int querySize = 0;
		
    	try {
			try{
				querySize = rmFXAllFavorDao.deleteAllFavor(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( querySize < 1){
				paramVO.setResultSet(-1);
			}
			
			try {
				//imcsLog.dbLog(ImcsConstants.FXAPI_PRO_ID080, sqlId, cache, querySize, methodName, methodLine);
				
				if(querySize > 0) {
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID080) + "] delete [PT_VO_FAVORITE] table[" + querySize + "] records Success at";
					imcsLog.serviceLog(szMsg, methodName, methodLine);	
				} else {
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID080) + "] delete [PT_VO_FAVORITE] table Failed at";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
					
					//imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID080, sqlId, cache, "delete fail:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
				}
			} catch (Exception e) {}
			
			paramVO.setResultSet(querySize);
		} catch (Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID080) + "] delete [PT_VO_FAVORITE] table Failed at";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			//imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID080, "", cache, "delete fail:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			paramVO.setResultSet(-1);
		}
		
		return querySize;
    }
    
    public Integer deleteAllFavorDAO2(RmFXAllFavorRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
    	
    	String sqlId = "fxvod080_d01_20171214_001";
		String szMsg = "";
		
		int querySize = 0;
		
    	try {
			try{
				querySize = rmFXAllFavorDao2.deleteAllFavor(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( querySize < 1){
				paramVO.setResultSet(-1);
			}
			
			try {
				//imcsLog.dbLog(ImcsConstants.FXAPI_PRO_ID080, sqlId, cache, querySize, methodName, methodLine);
				
				if(querySize > 0) {
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID080) + "] delete [PT_VO_FAVORITE] table[" + querySize + "] records Success at";
					imcsLog.serviceLog(szMsg, methodName, methodLine);	
				} else {
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID080) + "] delete [PT_VO_FAVORITE] table Failed at";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
					
					//imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID080, sqlId, cache, "delete fail:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
				}
			} catch (Exception e) {}
			
			paramVO.setResultSet(querySize);
		} catch (Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID080) + "] delete [PT_VO_FAVORITE] table Failed at";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			//imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID080, "", cache, "delete fail:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			paramVO.setResultSet(-1);
		}
		
		return querySize;
    }    
}
