package kr.co.wincom.imcs.api.rmNSAllCHFavor;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.NosqlCacheType;
import kr.co.wincom.imcs.common.nosql.NoSQLRedisDao;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.handler.ImcsException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RmNSAllCHFavorServiceImpl implements RmNSAllCHFavorService {
	private Log imcsLogger = LogFactory.getLog("API_rmNSAllCHFavor");
	
	@Autowired
	private RmNSAllCHFavorDao rmNSAllCHFavorDao;
	
	@Autowired
	private NoSQLRedisDao noSQLRedisDao;
	
//	public void rmNSAllCHFavor(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	@Override
	public RmNSAllCHFavorResultVO rmNSAllCHFavor(RmNSAllCHFavorRequestVO paramVO){
//		this.rmNSAllCHFavor(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		imcsLog.debugLog(methodName + " service call");
		
		// DB쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		RmNSAllCHFavorResultVO resultVO = new RmNSAllCHFavorResultVO();
		
		String flag		= "";
		String errMsg	= "";
		String msg		= "";
			    
	    Integer resultSet = -1;
	    

	    long tp_start = paramVO.getTp_start();
		long tp1 = 0;
	    
		try{
						
			Integer result = 0;
			
			try {
				result = rmNSAllCHFavorDao.deleteNSAllCHFavor(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
				if(result == null)		result = 0;
				
				if(result > 0) {
					resultSet = 0;
					
					msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID357) + "] delete [PT_VO_FAVORITE_CH] table[" + result + "] records Success at";
					imcsLog.serviceLog(msg, methodName, methodLine);
				} else {
					resultSet = -1;
					
					msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID357) + "] delete [PT_VO_FAVORITE_CH] table Failed at";
					imcsLog.serviceLog(msg, methodName, methodLine);
				}
				
			} catch (Exception e) {
				resultSet = -1;
				
				msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID357) + "] delete [PT_VO_FAVORITE_CH] table Failed at";
				imcsLog.serviceLog(msg, methodName, methodLine);
				
				throw new ImcsException(ImcsConstants.FAIL_CODE, e);
			}
			
			tp1	= System.currentTimeMillis();
			imcsLog.timeLog("선호채널 삭제", String.valueOf(tp1 - tp_start), methodName, methodLine);
			
			
			if(resultSet == 0){
				resultVO.setFlag("0");
				resultVO.setErrMsg("선호채널 삭제되었습니다.");
			}else{
				flag = "1";
				errMsg = "선호채널 삭제가 실패하였습니다.";
				
				resultVO.setResultCode("20000001");
				throw new ImcsException(flag, errMsg);
			}
			
		}catch(ImcsException ie) {
			resultVO.setFlag(flag);
			resultVO.setErrMsg(errMsg);
			
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			
			imcsLog.errorLog(methodName + "-E", ie.getClass().getName() + ":" + ie.getMessage());
			throw ie;
		} catch(Exception e) {
			resultVO.setFlag(flag);
			resultVO.setErrMsg(errMsg);
			
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			
			// TLO Log 를 Controller Layer 로 전달하기 위해 ImcsException 으로 변환한다. (TLO Log 세부값은 finally 문에서 설정된다.)
			throw new ImcsException(resultVO.getFlag(), resultVO.getErrMsg());
		} finally{
			msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID357) + "]" + String.format("%-5s",  " sts[" + ImcsConstants.LOG_MSG3 + "]") 
					+ "snd[FLAG=" + resultVO.getFlag() + "|MESSAGE=" + resultVO.getErrMsg() + "|]";
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID357) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			//msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID357) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
			//		+ " [DR:" + nDRedis + "|DH:" + nDHbase + "|R:" + nRedis + "|H:" + nHbase + "]"; 
			//imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultVO;
	}
}
