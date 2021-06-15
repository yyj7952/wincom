package kr.co.wincom.imcs.api.setNSRating;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.nosql.NoSQLRedisDao;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.handler.ImcsException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import kr.co.wincom.imcs.common.NosqlCacheType;


@Service
public class SetNSRatingServiceImpl implements SetNSRatingService {
	private Log imcsLogger = LogFactory.getLog("API_setNSRating");
	
	@Autowired
	private SetNSRatingDao setNSRatingDao;
	
	@Autowired
	private NoSQLRedisDao noSQLRedisDao;
	
//	public void setNSRating(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	@Override
	public SetNSRatingResultVO setNSRating(SetNSRatingRequestVO paramVO){
//		this.setNSRating(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		SetNSRatingResultVO resultVO = new SetNSRatingResultVO();
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		String msg		= "";
		
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		Integer nResultSet	= 0;
		
	    int nDataChk = 0;
	    long tp1, tp2, tp3 = 0;
		
		try{
			// 연령 정보 체크
			tp1 = System.currentTimeMillis();
			nDataChk = this.getRatingChk(paramVO);
			
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("select", String.valueOf(tp2 - tp1), methodName, methodLine); 
						
			int result = 0;
			
			
			if(nDataChk == 1){						// 연령정보가 있으면 UPDATE
				tp1 = System.currentTimeMillis();
				try {
					result = this.updateRating(paramVO);
					
					if(result > 0) 		nResultSet = 0;
			    	else	    		nResultSet = -1;
				} catch (Exception e) {
					nResultSet = -1;
				}
				
				tp2	= System.currentTimeMillis();
				imcsLog.timeLog("update", String.valueOf(tp2 - tp1), methodName, methodLine); 
			}else{									// 연령정보가 있으면 INSERT
				tp1 = System.currentTimeMillis();
				try {
					result = this.insertRating(paramVO);
					
					if(result > 0) 		nResultSet = 0;
			    	else	    		nResultSet = -1;
				} catch (Exception e) {
					nResultSet = -1;
				}
				
				tp2	= System.currentTimeMillis();
				imcsLog.timeLog("insert", String.valueOf(tp2 - tp1), methodName, methodLine); 
			}
			
			tp3 = System.currentTimeMillis();
			
			if(nResultSet == 0){
				resultVO.setFlag("0");
				resultVO.setErrMsg("연령설정이 적용되었습니다.");
				
				imcsLog.timeLog("EXEC SQL COMMIT", String.valueOf(tp3 - tp2), methodName, methodLine); 
			}else{
				resultVO.setFlag("1");
				resultVO.setErrMsg("연령설정 적용이 실패하였습니다.");
				
				imcsLog.timeLog("EXEC SQL ROLLBACK", String.valueOf(tp3 - tp2), methodName, methodLine); 
				
				throw new ImcsException(resultVO.getFlag(), resultVO.getErrMsg(), "");
			}
		}catch(ImcsException ie) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			
			resultVO.setFlag(ie.getFlag());
			resultVO.setErrMsg(ie.getMessage());
			
			throw ie;
		} catch(Exception e) {
			isLastProcess = ImcsConstants.RCV_MSG6;

			resultVO.setFlag("1");
			resultVO.setErrMsg("연령설정 적용이 실패하였습니다.");

			throw new ImcsException(resultVO.getFlag(), resultVO.getErrMsg(), "");
		} finally{
			msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID999) + "] sts[" + ImcsConstants.LOG_MSG3 + "]"
					+ " snd[FLAG=" + resultVO.getFlag() + "|MESSAGE=" + resultVO.getErrMsg() + "]";
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID999) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultVO;
	}
    
	
	
	
    /**
     * 연령 정보 체크
     * @param paramVO
     * @return
     * @throws Exception
     */
    public Integer getRatingChk(SetNSRatingRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
    	
    	String sqlId =  "lgvod999_001_20171214_001";
    	int querySize = 0;
		
		List<Integer> list   = null;
		Integer nRatingChk = 0;
		
		try {
			try{
				list = setNSRatingDao.getRatingChk(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list == null ||list.isEmpty()){
				nRatingChk = 0;
			} else {
				nRatingChk = list.get(0);
				querySize = list.size();
			}
			
			//C에서 주석 처리된 로그
			try {
				//imcsLog.dbLog2(ImcsConstants.API_PRO_ID358, sqlId, cache, querySize, methodName, methodLine);
			} catch (Exception e) {}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
    	return nRatingChk;    	
    }
    
    
    
    /**
     * 연령 정보 수정
     * @param paramVO
     * @return
     * @throws Exception
     */
    public Integer updateRating(SetNSRatingRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
    	
    	String sqlId = "lgvod999_u01_20171214_001";
		String szMsg = "";
		
		int querySize = 0;
		
    	try {

    		try{
				querySize = setNSRatingDao.updateRating(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}			
			
			try {
				//imcsLog.dbLog(ImcsConstants.API_PRO_ID999, sqlId, cache, querySize, methodName, methodLine);

				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID999) + "] update [PT_VO_CUSTOM_RATING] table[" + querySize + "] records Success at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			} catch (Exception e) {}
			
		} catch (Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID999) + "] update [PT_VO_CUSTOM_RATING] table Failed at";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			//imcsLog.failLog(ImcsConstants.API_PRO_ID999, "", cache, "fail info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
		return querySize;
	}
    
    
    
    /**
     * 연령 정보 등록
     * @param paramVO
     * @return
     * @throws Exception
     */
    public Integer insertRating(SetNSRatingRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
    	
    	String sqlId =  "lgvod999_i01_20171214_001";
		
		int querySize = 0;
		
		String szMsg = "";
		    	
    	try {
			try{
				querySize = setNSRatingDao.insertRating(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			try {
				//imcsLog.dbLog(ImcsConstants.API_PRO_ID999, sqlId, cache, querySize, methodName, methodLine);

				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID999) + "] insert [PT_VO_CUSTOM_RATING] table[" + querySize + "] records Success at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			} catch (Exception e) {}
			
		} catch (Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID999) + "] insert [PT_VO_CUSTOM_RATING] table Failed at";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			//imcsLog.failLog(ImcsConstants.API_PRO_ID999, "", cache, "fail info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
		return querySize;
	}
}
