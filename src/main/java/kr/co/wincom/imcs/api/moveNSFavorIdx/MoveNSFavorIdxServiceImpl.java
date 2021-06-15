package kr.co.wincom.imcs.api.moveNSFavorIdx;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.nosql.NoSQLRedisDao;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.handler.ImcsException;
import kr.co.wincom.imcs.common.NosqlCacheType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MoveNSFavorIdxServiceImpl implements MoveNSFavorIdxService {
	private Log imcsLogger		= LogFactory.getLog("API_moveNSFavorIdx");
	
	@Autowired
	private MoveNSFavorIdxDao moveNSFavorIdxDao;
	
	@Autowired
	private NoSQLRedisDao noSQLRedisDao;
	
//	public void moveNSFavorIdx(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	/**
	 * @author HONG
	 * @since 2016-06-21
	 * 찜목록 등록 (lgvod379.pc)
	 */
	@Override
	public MoveNSFavorIdxResultVO moveNSFavorIdx(MoveNSFavorIdxRequestVO paramVO)	{
//		this.moveNSFavorIdx(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		String resFlag		= "";
		String resErrCode	= "";
		String resErrMsg	= "";
		String szMsg		= "";
		
		MoveNSFavorIdxResultVO	resultListVO	= new MoveNSFavorIdxResultVO();
		
		int nRetVal			= 0;
		long tp1			= 0;		// timePoint 1
		long tp2			= 0;		// timePoint 2
		
		try {
			tp1 = System.currentTimeMillis();
			
			Integer pIdx = Integer.parseInt(paramVO.getPrevIndex());
			Integer nIdx = Integer.parseInt(paramVO.getNextIndex());
			
			try {
				if(pIdx > nIdx){
					nRetVal = moveNSFavorIdxDao.updatePNIndex(paramVO);
				}else{
					nRetVal = moveNSFavorIdxDao.updateNPIndex(paramVO);
				}
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			} catch (Exception e) {
				nRetVal = -1;
			}
			
			
			if(nRetVal > 0){				
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID369) + "] update [PT_VO_FAVORITE] table[" + nRetVal + "] records Success at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);				
			}else{
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID369) + "] update [PT_VO_FAVORITE] table Failed at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			}
						
			tp2 = System.currentTimeMillis();
			imcsLog.timeLog("찜목록 순번 Update", String.valueOf(tp2 - tp1), methodName, methodLine);

			try {
				nRetVal = moveNSFavorIdxDao.updateIndex(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			} catch (Exception e) {
				nRetVal = -1;
			}			
			
			if(nRetVal > 0){				
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID369) + "] update [PT_VO_FAVORITE] table[" + nRetVal + "] records Success at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);				
			}else{
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID369) + "] update [PT_VO_FAVORITE] table Failed at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			}
			
			tp1 = System.currentTimeMillis();
			imcsLog.timeLog("선택한 컨텐츠 찜목록 순번 Update", String.valueOf(tp1 - tp2), methodName, methodLine);
			
			if(nRetVal < 0){
				resFlag		= "1";
				resErrCode	= "";
				resErrMsg	= "찜목록 순서 변경 실패하였습니다.";
				paramVO.setResultCode("20000001");
			}else{
				resFlag		= "0";
				resErrCode	= "";
				resErrMsg	= "찜목록 순서 변경되었습니다.";
				paramVO.setResultCode("20000000");
			}
						
		} catch(ImcsException ie) {
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			
			resFlag		= "1";
			resErrCode	= "";
			resErrMsg	= "찜목록 순서 변경 실패하였습니다.";
			paramVO.setResultCode("20000001");
			
			imcsLog.errorLog(methodName + "-E",ie.getClass().getName()+":"+ie.getMessage());
			throw ie;
		} catch(Exception e) {
			isLastProcess	= ImcsConstants.RCV_MSG6;
			
			resFlag		= "1";
			resErrCode	= "";
			resErrMsg	= "찜목록 순서 변경 실패하였습니다.";
			paramVO.setResultCode("20000001");
			
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			throw new ImcsException();
		} finally{
			resultListVO.setFlag(resFlag);
			resultListVO.setErrCode(resErrCode);
			resultListVO.setErrMsg(resErrMsg);
			resultListVO.setResultCode(paramVO.getResultCode());
			
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID369) + "] sts[" + ImcsConstants.LOG_MSG3 + "] "
					+ " snd[FLAG=" + resFlag + "|MESSAGE="+resErrMsg+"|]"; 
						
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID369) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(szMsg, methodName, methodLine);
		}
		
		return resultListVO;
	}
	
}
