package kr.co.wincom.imcs.api.rmFXFavorite;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.nosql.NoSQLRedisDao;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.handler.ImcsException;
import kr.co.wincom2.imcs.api.rmFXFavorite.RmFXFavoriteDao2;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import kr.co.wincom.imcs.common.NosqlCacheType;

@Service
public class RmFXFavoriteServiceImpl implements RmFXFavoriteService {
	private Log imcsLogger = LogFactory.getLog("API_rmFXFavorite");
	
	@Autowired
	private RmFXFavoriteDao rmFXFavoriteDao;

	@Autowired
	private RmFXFavoriteDao2 rmFXFavoriteDao2;

	@Autowired
	private NoSQLRedisDao noSQLRedisDao;
	

//	public void rmFXFavorite(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	@Override
	public RmFXFavoriteResultVO rmFXFavorite(RmFXFavoriteRequestVO paramVO){
//		this.rmFXFavorite(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	

		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		RmFXFavoriteResultVO resultListVO = new RmFXFavoriteResultVO();

	    String msg		= "";	// 로그 메세지
	    Integer nRetVal		= -99;
	    
	    long tp_start = paramVO.getTp_start();
		long tp1 = 0, tp2 = 0;
	    
		// 2019.03.19 - 유플릭스는 모든 스크린 찜 다 보여준다고 하여 동일 컨텐츠가 있으면 다 지움.
		try{
			tp1 = System.currentTimeMillis();
			imcsLog.timeLog("init...", String.valueOf(tp1 - tp_start), methodName, methodLine);
			
			for(int i = 0; i < paramVO.getAlbumList().size(); i++) {
				paramVO.setAlbumListId(paramVO.getAlbumList().get(i));
				
				// 인덱스 수정
//				try {
//					nRetVal = rmFXFavoriteDao.uptFavorIdx(paramVO);
//					paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
//					
//					tp2 = System.currentTimeMillis();
//					imcsLog.timeLog("찜목록 순번 Update", String.valueOf(tp2 - tp1), methodName, methodLine);
//					
//					if(nRetVal > 0) {
//						msg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID070) + "] update [PT_VO_FAVORITE] table [" + nRetVal + "] records Success at";
//						imcsLog.serviceLog(msg, methodName, methodLine);
//					}
//				} catch (Exception e) {
//					tp2 = System.currentTimeMillis();
//					imcsLog.timeLog("찜목록 순번 Update", String.valueOf(tp2 - tp1), methodName, methodLine);
//					
//					msg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID070) + "] update [PT_VO_FAVORITE] table Failed at";
//					imcsLog.serviceLog(msg, methodName, methodLine);
//					
//					nRetVal	= -1;
//				}
				
				// 찜목록 삭제
				try {
					this.delFavorite(paramVO);
					
					if (paramVO.getResultSet() > 0) {
						this.delFavoriteDAO2(paramVO);
					}
				} catch(Exception e) {}
				
				nRetVal = paramVO.getResultSet();
				
				if(nRetVal <= 0){
					break;
				}
			}
			
			tp1	= System.currentTimeMillis();
			imcsLog.timeLog("찜목록 삭제", String.valueOf(tp1 - tp_start), methodName, methodLine); 
			
			if(nRetVal > 0){
				resultListVO.setFlag("0");
				resultListVO.setErrMsg("찜목록 삭제되었습니다.");
			} else {
				resultListVO.setFlag("1");
				resultListVO.setErrMsg("찜목록 삭제가 실패하였습니다.");
				
				throw new ImcsException(resultListVO.getFlag(), resultListVO.getErrMsg(), "");
			}
		} catch(ImcsException ie) {
			ie.setFlag(ie.getFlag());
			ie.setMessage(ie.getMessage());
			
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			
			imcsLog.errorLog(methodName + "-E", ie.getClass().getName() + ":" + ie.getMessage());
			throw ie;
		} catch(Exception e) {
			resultListVO.setFlag("1");
			resultListVO.setErrMsg("찜목록 삭제가 실패하였습니다.");
			
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			throw new ImcsException(resultListVO.getFlag(), resultListVO.getErrMsg());
		} finally{
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("COMMIT / ROLLBACK", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			msg = " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID070) + "]" + String.format("%-5s", " sts[" + ImcsConstants.LOG_MSG3 + "]") 
					+ " snd[FLAG=" + resultListVO.getFlag() + "|MESSAGE=" + resultListVO.getErrMsg() + "|]";
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID070) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultListVO;
	}

	
	/**
	 * 시청목록 삭제 (DEL_YN 업데이트)
	 * @param	RmFXFavoriteRequestVO
	 * @result	Integer
	 */
    public Integer delFavorite(RmFXFavoriteRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
    	
    	String sqlId = "fxvod070_d01_20171214_001";
		String szMsg = "";
		
		int querySize = 0;
		
    	try {
			try{
				querySize = rmFXFavoriteDao.delFavorite(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID070, "", null, "delete fail:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			try {
				//imcsLog.dbLog(ImcsConstants.FXAPI_PRO_ID070, sqlId, cache, querySize, methodName, methodLine);
				
				if( querySize == 0){
					paramVO.setResultSet(querySize);
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID070) + "] delete [PT_VO_FAVORITE] table Failed at";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
					
					//imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID070, sqlId, cache, "delete fail:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
				} else {
					paramVO.setResultSet(querySize);
					
					/*szMsg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID070) + "] delete [PT_VO_FAVORITE] table[" + querySize + "] records Success at";
					imcsLog.serviceLog(szMsg, methodName, methodLine);*/
				}
			} catch (Exception e) {}
			
		} catch (Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID070) + "] delete [SMARTUX.PT_CM_FAVORITE] table Failed at";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			//imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID070, "", cache, "delete fail:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			
			paramVO.setResultSet(-1);
		}
		
		return querySize;
    }
    
    public Integer delFavoriteDAO2(RmFXFavoriteRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
    	
    	String sqlId = "fxvod070_d01_20171214_001";
		String szMsg = "";
		
		int querySize = 0;
		
    	try {
			try{
				querySize = rmFXFavoriteDao2.delFavorite(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID070, "", null, "delete fail:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			try {
				//imcsLog.dbLog(ImcsConstants.FXAPI_PRO_ID070, sqlId, cache, querySize, methodName, methodLine);
				
				if( querySize == 0){
					paramVO.setResultSet(-1);
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID070) + "] delete [SMARTUX.PT_UX_FAVORITE] table Failed at";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
					
					//imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID070, sqlId, cache, "delete fail:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
				} else {
					paramVO.setResultSet(querySize);
					
					/*szMsg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID070) + "] delete [PT_VO_FAVORITE] table[" + querySize + "] records Success at";
					imcsLog.serviceLog(szMsg, methodName, methodLine);*/
				}
			} catch (Exception e) {}
			
		} catch (Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID070) + "] delete [SMARTUX.PT_UX_FAVORITE] table Failed at";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			//imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID070, "", cache, "delete fail:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			
			paramVO.setResultSet(-1);
		}
		
		return querySize;
    }

}
