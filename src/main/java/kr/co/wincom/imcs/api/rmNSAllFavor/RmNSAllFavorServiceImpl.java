package kr.co.wincom.imcs.api.rmNSAllFavor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.nosql.NoSQLRedisDao;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.handler.ImcsException;
import kr.co.wincom2.imcs.api.rmNSAllFavor.RmNSAllFavorDao2;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import kr.co.wincom.imcs.common.NosqlCacheType;

@Service
public class RmNSAllFavorServiceImpl implements RmNSAllFavorService {
	private Log imcsLogger		= LogFactory.getLog("API_rmNSAllFavor");
	
	@Autowired
	private RmNSAllFavorDao rmNSAllFavorDao;

	@Autowired
	private RmNSAllFavorDao2 rmNSAllFavorDao2;
	
	@Autowired
	private NoSQLRedisDao	noSQLRedisDao;
	
//	public void rmNSAllFavor(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	/**
	 * @author HONG
	 * @since 2016-06-21
	 * 찜목록 전체삭제 (lgvod359.pc)
	 */
	@Override
	public RmNSAllFavorResultVO rmNSAllFavor(RmNSAllFavorRequestVO paramVO)	{
//		this.rmNSAllFavor(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		String resFlag		= "1";
		String resErrMsg	= "찜목록 삭제가 실패하였습니다.";
		String szMsg		= "";
		String resultCode	= "20000001";
		
		RmNSAllFavorResultVO	resultListVO	= new RmNSAllFavorResultVO();
		
		
		int nRetVal		= -99;
		
		long tp1			= 0;		// timePoint 1
		long tp2			= 0;		// timePoint 2
		
		try {
			tp1 = System.currentTimeMillis();
			
			try {
				nRetVal = this.rmAllFavorMIMS(paramVO);
				imcsLog.timeLog("찜목록 삭제(MIMS)", String.valueOf(tp2 - tp1), methodName, methodLine);
				
				nRetVal	= this.rmAllFavor(paramVO);
				imcsLog.timeLog("찜목록 삭제(DAO)", String.valueOf(tp2 - tp1), methodName, methodLine);
				
				if(nRetVal > 0) {
					resFlag		= "0";
					resErrMsg	= "찜목록 삭제되었습니다.";
					resultCode	= "20000000";
				}				
				
				nRetVal	= this.rmAllFavorDAO2(paramVO);
				imcsLog.timeLog("찜목록 삭제(DAO2)", String.valueOf(tp2 - tp1), methodName, methodLine);

				
			} catch(Exception e) {}
						
			tp2	= System.currentTimeMillis();			
			imcsLog.timeLog("찜목록 삭제", String.valueOf(tp2 - tp1), methodName, methodLine);
			
		} catch(ImcsException ie) {
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			
			imcsLog.errorLog(methodName + "-E",ie.getClass().getName()+":"+ie.getMessage());
			ie.setFlag(resFlag);
			ie.setMessage(resErrMsg);
			ie.setList(resultListVO);
			throw ie;
		} catch(Exception e) {
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			resultListVO.setFlag(resFlag);
			resultListVO.setErrMsg(resErrMsg);
			throw new ImcsException(resFlag, resErrMsg);
		} finally{
			resultListVO.setFlag(resFlag);
			resultListVO.setErrMsg(resErrMsg);					
			resultListVO.setResultCode(resultCode);
			
			szMsg	 = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID359) + "]" 
					 + " sts[" + ImcsConstants.LOG_MSG3 + "]" + String.format("%-21s", " snd[FLAG=" + resultListVO.getFlag() + ":MESSAGE=" + resultListVO.getErrMsg() + "|]");
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID359) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(szMsg, methodName, methodLine);			
			
		}
				
		return resultListVO;
	}
	
	


	/**
	 * 찜목록 전체 삭제
	 * @param 	GetNSPurchasedVO paramVO
	 * @return  Integer
	 **/
	public Integer rmAllFavor(RmNSAllFavorRequestVO paramVO) throws Exception {
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine	= String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName	= oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	

		String sqlId	= "lgvod359_d01_20171214_001";
		String szMsg	= "";

		int querySize	= 0;

		try {
			try {
				querySize = rmNSAllFavorDao.rmAllFavor(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해
				throw new SQLException(e.getClass().getName()+ ":" + e.getMessage());
			}

			try {
				//imcsLog.dbLog(ImcsConstants.API_PRO_ID359, sqlId, cache, querySize, methodName, methodLine);
				
				if(querySize > 0) {
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID359) + "] delete [PT_VO_FAVORITE] table[" + querySize + "] records Success at";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
				} else {
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID359) + "] delete [PT_VO_FAVORITE] table Failed at";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
					
					//imcsLog.failLog(ImcsConstants.API_PRO_ID359, "", cache, "fail info:no data found", methodName, methodLine);
				}
			} catch (Exception e) {}

		} catch (Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID359) + "] delete [PT_VO_FAVORITE] table Failed at";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			//imcsLog.failLog(ImcsConstants.API_PRO_ID359, "", cache, "fail info:" + ImcsConstants.RCV_MSG6 , methodName, methodLine);
		}

		return querySize;
	}
	
	public Integer rmAllFavorMIMS(RmNSAllFavorRequestVO paramVO) throws Exception {
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine	= String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName	= oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	

		String sqlId	= "lgvod359_d01_20171214_001";
		String szMsg	= "";

		int querySize	= 0;

		try {
			try {
				querySize = rmNSAllFavorDao.rmAllFavorMIMS(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해
				throw new SQLException(e.getClass().getName()+ ":" + e.getMessage());
			}

			try {
				//imcsLog.dbLog(ImcsConstants.API_PRO_ID359, sqlId, cache, querySize, methodName, methodLine);
				
				if(querySize > 0) {
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID359) + "] delete [SMARTUX.PT_CM_FAVORITE] table[" + querySize + "] records Success at";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
				} else {
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID359) + "] delete [SMARTUX.PT_CM_FAVORITE] table Failed at";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
					
					//imcsLog.failLog(ImcsConstants.API_PRO_ID359, "", cache, "fail info:no data found", methodName, methodLine);
				}
			} catch (Exception e) {}

		} catch (Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID359) + "] delete [SMARTUX.PT_CM_FAVORITE] table Failed at";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			//imcsLog.failLog(ImcsConstants.API_PRO_ID359, "", cache, "fail info:" + ImcsConstants.RCV_MSG6 , methodName, methodLine);
		}

		return querySize;
	}	

	public Integer rmAllFavorDAO2(RmNSAllFavorRequestVO paramVO) throws Exception {
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine	= String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName	= oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	

		String sqlId	= "lgvod359_d01_20171214_001";
		String szMsg	= "";

		int querySize	= 0;

		try {
			try {
				querySize = rmNSAllFavorDao2.rmNSAllFavor(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해
				throw new SQLException(e.getClass().getName()+ ":" + e.getMessage());
			}

			try {
				//imcsLog.dbLog(ImcsConstants.API_PRO_ID359, sqlId, cache, querySize, methodName, methodLine);
				
				if(querySize > 0) {
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID359) + "] delete [PT_VO_FAVORITE] table[" + querySize + "] records Success at";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
				} else {
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID359) + "] delete [PT_VO_FAVORITE] table Failed at";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
					
					//imcsLog.failLog(ImcsConstants.API_PRO_ID359, "", cache, "fail info:no data found", methodName, methodLine);
				}
			} catch (Exception e) {}

		} catch (Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID359) + "] delete [PT_VO_FAVORITE] table Failed at";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			//imcsLog.failLog(ImcsConstants.API_PRO_ID359, "", cache, "fail info:" + ImcsConstants.RCV_MSG6 , methodName, methodLine);
		}

		return querySize;
	}
	
}
