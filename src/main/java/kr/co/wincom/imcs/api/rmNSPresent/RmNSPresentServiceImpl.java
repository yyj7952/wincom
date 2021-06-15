package kr.co.wincom.imcs.api.rmNSPresent;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kr.co.wincom.imcs.common.CommonService;
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
public class RmNSPresentServiceImpl implements RmNSPresentService {
	private Log imcsLogger = LogFactory.getLog("API_rmNSPresent");
	
	@Autowired
	private RmNSPresentDao rmNSPresentDao;
	
	@Autowired
	private NoSQLRedisDao noSQLRedisDao;
	
	@Autowired
	private CommonService commonService;

//	public void rmNSPresent(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	@Override
	public RmNSPresentResultVO rmNSPresent(RmNSPresentRequestVO paramVO){
//		this.rmNSPresent(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		RmNSPresentResultVO resultVO = new RmNSPresentResultVO();
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		String flag		= "";
		String errMsg	= "";
		
	    Integer nResultSet = 0;
	    Integer result = 0;		// 삭제 쿼리 처리 결과
	    
	    String msg	= "";

	    long tp_start = paramVO.getTp_start();
		long tp1 = 0;
	    
		try{
		    // 입수된 앨범 ID 가 'A'의 경우 전체삭제 처리를 한다
			if("A".equals(paramVO.getAlbumId())){
				result = this.rmNSPresentUpdate(paramVO);
				
				tp1	= System.currentTimeMillis();
				imcsLog.timeLog("선물 전체 삭제 처리 update", String.valueOf(tp1 - tp_start), methodName, methodLine); 
				
				if(result != null && result > 0)	nResultSet = 0;
		    	else					    		nResultSet = -1;
			}
			// 전달받은 앨범 삭제
			else{
				String szAlbumId;
				String szPresentDate;
				
				for(int i = 0; i < paramVO.getAlbumList().size(); i++) {
					szAlbumId		= paramVO.getAlbumList().get(i);
					szPresentDate	= paramVO.getPresentList().get(i);
					
					paramVO.setAlbumId(szAlbumId);
					paramVO.setPresentDate(szPresentDate);					
					
					result = this.rmNSPresentUpdate(paramVO);
					
					if(result != null && result > 0) {
			    		nResultSet = 0;
			    	} else {
			    		nResultSet = -1;
			    		
			    		break;
			    	}
				}
				
				tp1	= System.currentTimeMillis();
				imcsLog.timeLog("선물 삭제 처리 update", String.valueOf(tp1 - tp_start), methodName, methodLine); 
			}
			
			if(nResultSet == 0){
				resultVO.setFlag("0");
		    	resultVO.setErrMsg("선물 삭제가 완료되었습니다.");
			}else{
				resultVO.setFlag("1");
		    	resultVO.setErrMsg("선물 삭제가 실패하였습니다.");
		    	paramVO.setResultCode("20000001");
		    	
		    	throw new ImcsException(resultVO.getFlag(), resultVO.getErrMsg());
			}
			
		}catch(ImcsException ie) {
			resultVO.setFlag(ie.getFlag());
	    	resultVO.setErrMsg(ie.getMessage());
	    	
	    	isLastProcess	= ImcsConstants.RCV_MSG6; 
	    	
			imcsLog.errorLog(methodName + "-E", ie.getClass().getName() + ":" + ie.getMessage());
			throw ie;
		} catch(Exception e) {
			flag	= "1";
			errMsg	= "선물 삭제가 실패하였습니다.";			
			
			resultVO.setFlag(flag);
			resultVO.setErrMsg(errMsg);
			
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			throw new ImcsException(flag, errMsg);
		} finally{
			resultVO.setResultCode(paramVO.getResultCode());
			
			msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID313) + "]" + String.format("%-5s",  " sts[" + ImcsConstants.LOG_MSG3 + "]") 
					+ "snd[FLAG=" + resultVO.getFlag() + "|MESSAGE=" + resultVO.getErrMsg() + "|]";
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID313) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
			
		}
		
		return resultVO;
	}
	
	
	
	/**
	 * VOD 선물 삭제
	 * @param	RmNSPresentRequestVO
	 * @return	Integer
	 */
	public Integer rmNSPresentUpdate(RmNSPresentRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
    	
    	String sqlId = "lgvod313_d03_20171214_001";
    	String szMsg = "";
    	
    	if("A".equals(paramVO.getAlbumId())){				// 전체 삭제
    		if("G".equals(paramVO.getPresentGb()))	sqlId =  "lgvod313_d03_20171214_001";	// 받은선물함 전체 삭제
        	else					        		sqlId =  "lgvod313_d04_20171214_001";	// 보낸선물함 적체 삭제
    	}else{
    		if("G".equals(paramVO.getPresentGb()))	sqlId =  "lgvod313_d01_20171214_001";	// 받은선물함 개별 삭제
    		else									sqlId =  "lgvod313_d02_20171214_001";	// 보낸선물함 개별 삭제
    	}
		
		int querySize = 0;
		
    	try {
			try{
				querySize = rmNSPresentDao.rmNSPresentUpdate(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			try {
				//imcsLog.dbLog(ImcsConstants.API_PRO_ID313, sqlId, cache, querySize, methodName, methodLine);
				
				if(querySize == 0){
					//imcsLog.failLog(ImcsConstants.API_PRO_ID313, "", cache, "fail info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
				}else{
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID313) + "] update [PT_VO_PRESENT] table[" + querySize + "] records Success at";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
				}
			} catch (Exception e) {}
		} catch (Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID313) + "] update [PT_VO_PRESENT] table Failed at";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			//imcsLog.failLog(ImcsConstants.API_PRO_ID313, "", cache, "fail info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
		return querySize;
    }
}
