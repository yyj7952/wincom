package kr.co.wincom.imcs.api.rmFXWatchHis;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kr.co.wincom.imcs.api.setFXFavorGenre.SetFXFavorGenreRequestVO;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.nosql.NoSQLRedisDao;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.handler.ImcsException;
import kr.co.wincom2.imcs.api.rmFXWatchHis.RmFXWatchHisDao2;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import kr.co.wincom.imcs.common.NosqlCacheType;

@Service
public class RmFXWatchHisServiceImpl implements RmFXWatchHisService {
	private Log imcsLogger = LogFactory.getLog("API_rmFXWatchHis");
	
	@Autowired
	private RmFXWatchHisDao rmFXWatchHisDao;
	
	@Autowired
	private RmFXWatchHisDao2 rmFXWatchHisDao2;
	@Autowired
	private NoSQLRedisDao noSQLRedisDao;
	

//	public void rmFXWatchHis(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	@Override
	public RmFXWatchHisResultVO rmFXWatchHis(RmFXWatchHisRequestVO paramVO){
//		this.rmFXWatchHis(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String isLastProcess = ImcsConstants.RCV_MSG5;

		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		RmFXWatchHisResultVO resultListVO = new RmFXWatchHisResultVO();

	    String msg		= "";	// 로그 메세지
		
	    Integer nRetVal		= -99;
	    
	    long tp_start = paramVO.getTp_start();
		long tp1 = 0;
	    
		try{
			
			for(int i = 0; i < paramVO.getContsList().size(); i++) {
				paramVO.setContsListId(paramVO.getContsList().get(i));
				
				// 시청목록 삭제 (DEL_YN 업데이트)
				try {
					nRetVal = this.rmFXWatchHisUpdate(paramVO);
				} catch (Exception e) {
					resultListVO.setFlag("1");
					resultListVO.setErrMsg("시청목록 삭제가 실패하였습니다.");
					throw new ImcsException();
				}
				if(nRetVal == -1)		break;
				if(nRetVal == null) 	nRetVal = 0;
			}
			
			tp1	= System.currentTimeMillis();
			imcsLog.timeLog("시청목록 삭제", String.valueOf(tp1 - tp_start), methodName, methodLine); 
			
			if(nRetVal >= 0){
				resultListVO.setFlag("0");
				resultListVO.setErrMsg("시청목록 삭제되었습니다.");
				
				//2019.03.08 권형도 수정 (tpacall rmFXWatchHis 로직)
				try {
					nRetVal = this.rmFXWatchHisUpdate2(paramVO);
					
				} catch(Exception e) {
					msg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID110) + "] rmFXWatchHisDao2 [PT_VO_WATCH_HISTORY] table Failed at";
					imcsLog.serviceLog(msg, methodName, methodLine);
				}
			} else {
				resultListVO.setFlag("1");
				resultListVO.setErrMsg("시청목록 삭제가 실패하였습니다.");
				
				throw new ImcsException();
			}
			
			if(nRetVal < 0){
				resultListVO.setFlag("1");
				resultListVO.setErrMsg("시청목록 삭제가 실패하였습니다.");
				
				throw new ImcsException();
			}
		} catch(ImcsException ie) {
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
			new ImcsException(resultListVO.getFlag(), resultListVO.getErrMsg());
		} finally{
			/*msg = " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID110) + "]" + String.format("%-5s", " sts[" + ImcsConstants.LOG_MSG3 + "]") 
					+ " snd[FLAG=" + resultListVO.getFlag() + "|MESSAGE=" + resultListVO.getErrMsg() + "|]";
			imcsLog.serviceLog(msg, methodName, methodLine);*/
			
			msg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID110) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]";
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID110) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
						
		}
		
		return resultListVO;
	}

	
	/**
	 * 시청목록 삭제 (DEL_YN 업데이트)
	 * @param	RmFXWatchHisRequestVO
	 * @result	Integer
	 */
    public Integer rmFXWatchHisUpdate(RmFXWatchHisRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
    	
    	String sqlId = "fxvod110_u01_20171214_001";
		String szMsg = "";
		
		int querySize = -1;
		
    	try {
    		
			try{
				querySize = rmFXWatchHisDao.rmFXWatchHisUpdate(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			try {
				//imcsLog.dbLog(ImcsConstants.FXAPI_PRO_ID110, sqlId, null, querySize, methodName, methodLine);
				
				if(querySize < 0) {
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID110) + "] update [PT_VO_WATCH_HISTORY_NSC] table Failed at";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
					querySize = -1;
					//imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID110, sqlId, null, "update fail:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
				} else {
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID110) + "] update [PT_VO_WATCH_HISTORY_NSC] table[" + querySize + "] records Success at";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
				}
			} catch (Exception e) {}
			
			// HABSE 버전 업 쿼리이므로 ORA_HBS COUNT + 1
			//paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.HBASE_DB.ordinal()]++;
			
		} catch (Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID110) + "] update [PT_VO_WATCH_HISTORY_NSC] table Failed at";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			querySize = -1;
			//imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID110, sqlId, null, "update fail:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
		return querySize;
    }
    
    /**
     * DB분리에 따른 tpacall rmFXWatchHis() 구현
     * @param paramVO
     * @return
     * @throws Exception
     */
    public Integer rmFXWatchHisUpdate2(RmFXWatchHisRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String sqlId =  "nsvod999_016_20190208_001";
    	
		int querySize = -1;
		
		String szMsg = "";
		
		try {
			
			if (paramVO.getContsList().size() > 0) {
				
				for (String cont_id : paramVO.getContsList()) {
					paramVO.setContsListId(cont_id);
					querySize = rmFXWatchHisDao2.rmFXWatchHisUpdate(paramVO);
					paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
					
					//imcsLog.dbLog(ImcsConstants.FXAPI_PRO_ID010, sqlId, null, querySize, methodName, methodLine);
					if (querySize >= 0) {	
						szMsg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID110) + "] rmFXWatchHisDao2 update [PT_VO_WATCH_HISTORY] table[" + querySize + "] records Success at";
					} else {
						szMsg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID110) + "] rmFXWatchHisDao2 update [PT_VO_WATCH_HISTORY] table Failed at";
						querySize = -1;
						break;
					}
					imcsLog.serviceLog(szMsg, methodName, methodLine);
					
				}
			}
			
		} catch (Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID110) + "] rmFXWatchHisDao2 [PT_VO_WATCH_HISTORY] table Failed at";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			//imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID110, sqlId, null, "update fail:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			querySize = -1;
		}
		
		return querySize;
	}
}
