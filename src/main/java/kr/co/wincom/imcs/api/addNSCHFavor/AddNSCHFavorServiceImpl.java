package kr.co.wincom.imcs.api.addNSCHFavor;

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
public class AddNSCHFavorServiceImpl implements AddNSCHFavorService {
	private Log imcsLogger	= LogFactory.getLog("API_addNSCHFavor");
	
	@Autowired
	private AddNSCHFavorDao AddNSCHFavorDao;
	
	@Autowired
	private NoSQLRedisDao noSQLRedisDao;
	
//	public void addNSCHFavor(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	@Override
	public AddNSCHFavorResultVO addNSCHFavor(AddNSCHFavorRequestVO paramVO){
//		this.addNSCHFavor(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String isLastProcess = ImcsConstants.RCV_MSG5;

		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);		
		
		AddNSCHFavorResultVO resultVO = new AddNSCHFavorResultVO();
		
		String flag		= "";
		String errCode	= "";
		String errMsg	= "";
		String resultCode	= "";
		String favIdx	= "";
		String msg		= "";
		
		Integer dataCnt		= 0;
		Integer favCnt		= 0;
	    Integer resultSet	= -1;
	    
	    long tp_start = paramVO.getTp_start();
		long tp1 = 0;
		long tp2 = 0;
		long tp3 = 0;
	    
		try{
			try {
				// 선호채널 중복 체크
				dataCnt = AddNSCHFavorDao.getCHFavorDupCk(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				if( dataCnt == null){
    				dataCnt = 0;
    			}
				
				resultSet = 0;
			} catch (Exception e) {
				resultSet = -1;
				
				//imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage() + "," + methodName + "_getCHFavorDupCk()");
				//throw new ImcsException(ImcsConstants.FAIL_CODE, e);
			}
			
			tp1	= System.currentTimeMillis();
			imcsLog.timeLog("선호채널 중복 Check", String.valueOf(tp1 - tp_start), methodName, methodLine); 
			
			if(dataCnt > 0){
				resultSet = -1;
			}
			
			
			
			// 선호채널 인덱스 조회
			if(resultSet == 0){
				try {
					favIdx = AddNSCHFavorDao.getCHFavorIndex(paramVO);
					paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
					if(favIdx == null || "".equals(favIdx)){
						favIdx = "1";
					}
					
					paramVO.setFavIdx(favIdx);
					
					resultSet = 0;
				} catch (Exception e) {
					resultSet = -1;
//					imcsLog.failLog(ImcsConstants.API_PRO_ID377, "", null, "favor_idx:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
				}
				
				tp2	= System.currentTimeMillis();
				imcsLog.timeLog("선호채널 순번 조회", String.valueOf(tp2 - tp1), methodName, methodLine); 
			}
			
			tp2	= System.currentTimeMillis();
			
			if(resultSet == 0){
				int result = 0;
				
				try {
					result = AddNSCHFavorDao.insertCHFavor(paramVO);
					msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID377) + "] insert [PT_VO_FAVORITE_CH] table[" + result + "] records Success at";
					imcsLog.serviceLog(msg, methodName, methodLine);
					
				} catch (Exception e) {
					resultSet = -1;
					
					msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID377) + "] insert [PT_VO_FAVORITE_CH] table Failed at";
					imcsLog.serviceLog(msg, methodName, methodLine);
				}
				
				
				if(result == 1)		resultSet = 0;
				else				resultSet = -1;
				
				tp3	= System.currentTimeMillis();
				imcsLog.timeLog("선호채널 등록", String.valueOf(tp3 - tp2), methodName, methodLine); 
				
			}
			
			if(resultSet == 0){
				resultVO.setFlag("0");
				resultVO.setErrMsg("선호채널에 등록되었습니다.");
			}else if(resultSet == -1 && dataCnt > 0){		
				flag = "1";
				errMsg = "이미 선호채널에 등록된 상품입니다.";
				errCode = "01";
				resultCode = "20000001";
				
				throw new ImcsException(flag, errMsg, errCode);
			}else if(resultSet == -1 && favCnt > 59){
				flag = "1";
				errMsg = "더 이상 선호채널에 추가하실 수 없습니다.";
				errCode = "02";
				resultCode = "20000002";
				
				throw new ImcsException(flag, errMsg, errCode);
			}else{
				flag = "1";
				errMsg = "선호채널 등록이 실패하였습니다.";
				errCode = "03";
				resultCode = "20000003";
				
				throw new ImcsException(flag, errMsg, errCode);
			}
		}catch(ImcsException ie) {
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			
			resultVO.setFlag(flag);
			resultVO.setErrCode(errCode);
			resultVO.setErrMsg(errMsg);
			resultVO.setResultCode(resultCode);
			
			imcsLog.errorLog(methodName + "-E", ie.getClass().getName() + ":" + ie.getMessage());
			throw ie;
			
		} catch(Exception e) {
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			
			resultVO.setFlag(flag);
			resultVO.setErrCode(errCode);
			resultVO.setErrMsg(errMsg);
			resultVO.setResultCode(resultCode);
			
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			throw new ImcsException(flag, errMsg, errCode);
			
		} finally{
			msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID377) + "]" + String.format("%-5s",  " sts[" + ImcsConstants.LOG_MSG3 + "]") 
					+ "snd[FLAG=" + resultVO.getFlag() + "|MESSAGE=" + resultVO.getErrMsg() + "|]";
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID377) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);			
						
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultVO;
	}
}
