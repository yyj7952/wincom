package kr.co.wincom.imcs.api.rmNSCHFavor;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.nosql.NoSQLRedisDao;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.handler.ImcsException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.wincom.imcs.common.NosqlCacheType;

@Service
public class RmNSCHFavorServiceImpl implements RmNSCHFavorService {
	private Log imcsLogger = LogFactory.getLog("API_rmNSCHFavor");
	
	@Autowired
	private RmNSCHFavorDao rmNSCHFavorDao;
	
	@Autowired
	private NoSQLRedisDao noSQLRedisDao;
	
//	public void rmNSCHFavor(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	@Override
	public RmNSCHFavorResultVO rmNSCHFavor(RmNSCHFavorRequestVO paramVO){
//		this.rmNSCHFavor(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);

		String flag		= "";
		String errMsg	= "";
		String msg		= "";
		
		RmNSCHFavorResultVO resultVO = new RmNSCHFavorResultVO();
		
	    Integer resultSet = -1;
	    
	    long tp_start = paramVO.getTp_start();
		long tp1, tp2 = 0;
	    
		try{

			for(int i = 0; i < paramVO.getContentsList().size(); i++) {
				paramVO.setContentId(paramVO.getContentsList().get(i));
				
				Integer result = 0;
				
				try {
					result = rmNSCHFavorDao.updateFavIdx(paramVO);
					paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
					
					if(result == null)		result = 0;
					
					// 성공한 것이 있을때만 로그 기록
					if(result > 0) {
						msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID347) + "] update [PT_VO_FAVORITE_CH] table[" + result + "] records Success at";
						imcsLog.serviceLog(msg, methodName, methodLine);
					}
					
					resultSet = 0;
				} catch (Exception e) {
					resultSet = -1;
					
					msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID347) + "] update [PT_VO_FAVORITE_CH] table Failed at";
					imcsLog.serviceLog(msg, methodName, methodLine);
					
					throw new ImcsException(ImcsConstants.FAIL_CODE, e);
				}
				
				tp1	= System.currentTimeMillis();
				imcsLog.timeLog("선호채널 순번 Update", String.valueOf(tp1 - tp_start), methodName, methodLine);
				
				
				try {
					result = rmNSCHFavorDao.deleteCHFavor(paramVO);
					paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
					if(result == null)		result	= 0;
					
					if(result > 0) {
						resultSet	= 0;
						
						msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID347) + "] delete [PT_VO_FAVORITE_CH] table[" + result + "] records Success at";
						imcsLog.serviceLog(msg, methodName, methodLine);
					} else {					// 삭제한 선호채널이 없어도 실패이다.
						resultSet	= -1;
						
						msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID347) + "] delete [PT_VO_FAVORITE_CH] table Failed at";
						imcsLog.serviceLog(msg, methodName, methodLine);
					}
				} catch (Exception e) {
					resultSet = -1;
					
					msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID347) + "] delete [PT_VO_FAVORITE_CH] table Failed at";
					imcsLog.serviceLog(msg, methodName, methodLine);
					
					throw new ImcsException();
				}
				
				tp2	= System.currentTimeMillis();
				imcsLog.timeLog("선호채널 삭제", String.valueOf(tp2 - tp1), methodName, methodLine);
				
				if(resultSet==-1) {
		         	break;
		        }
			}
			
			if(resultSet == 0){
				resultVO.setFlag("0");
				resultVO.setErrMsg("선호채널 삭제되었습니다.");
			}else{
				flag	= "1";
				errMsg	= "선호채널 삭제가 실패하였습니다.";
				
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
			resultVO.setFlag("1");
			resultVO.setErrMsg("선호채널 삭제가 실패하였습니다.");
			
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			
			imcsLog.errorLog(methodName + "-E",e.getClass().getName() + ":" + e.getMessage());
			throw new ImcsException(resultVO.getFlag(), resultVO.getErrMsg());
			
		} finally{
			msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID347) + "]" + String.format("%-5s", " sts[" + ImcsConstants.LOG_MSG3 + "]") 
					+ "snd[FLAG=" + resultVO.getFlag() + "|MESSAGE=" + resultVO.getErrMsg() + "|]";
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID347) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
			
		}
		
		return resultVO;
	}
	
}
