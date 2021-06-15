package kr.co.wincom.imcs.api.rmNSAllAlert;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.NosqlCacheType;
import kr.co.wincom.imcs.common.nosql.NoSQLRedisDao;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.handler.ImcsException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class RmNSAllAlertServiceImpl implements RmNSAllAlertService
{
	private Log imcsLogger		= LogFactory.getLog("API_rmNSAllAlert");
	
	@Autowired
	private RmNSAllAlertDao rmNSAllAlertDao;
	
	@Autowired
	private NoSQLRedisDao	noSQLRedisDao;
	
//	public void rmNSAllAlert(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	/**
	 * @author HONG
	 * @since 2016-06-21
	 * 알림받기 삭제 (lgvod663.pc)
	 */
	@Override
	public RmNSAllAlertResultVO rmNSAllAlert(RmNSAllAlertRequestVO paramVO)	{
//		this.rmNSAllAlert(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		RmNSAllAlertResultVO	resultListVO	= new RmNSAllAlertResultVO();
		
		String isLastProcess = ImcsConstants.RCV_MSG5;

		IMCSLog imcsLog = new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog(methodName + " service call");
		
		// DB쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		String resFlag		= "";
		String resErrMsg	= "";
		
		int nRetVal		= -99;
		
		try {
			long start = System.currentTimeMillis();
			
			try{
				nRetVal = this.rmNSAllAlertInfo(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			} catch (Exception e) {
				throw new ImcsException();
			}
			
			long end	= System.currentTimeMillis();
			
			if(nRetVal > 0) {
				resFlag		= "0";
				resErrMsg	= "알림받기 삭제되었습니다.";
			}
				
			imcsLog.timeLog("알림받기 삭제", String.valueOf(end - start), methodName, methodLine);
		} catch(ImcsException ie) {
			resFlag		= "1";
			resErrMsg	= "알림받기 삭제가 실패하였습니다.";
			
			paramVO.setResultCode("20000001");
			
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			
			imcsLog.errorLog(methodName + "-E",ie.getClass().getName()+":"+ie.getMessage());

			// TLO Log 를 Controller Layer 로 전달하기 위해 Setting 한다. (TLO Log 세부값은 finally 문에서 설정된다.)
			throw ie;
		} catch(Exception e) {
			resFlag		= "1";
			resErrMsg	= "알림받기 삭제가 실패하였습니다.";
			
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			
			paramVO.setResultCode("20000001");
			
			// TLO Log 를 Controller Layer 로 전달하기 위해 ImcsException 으로 변환한다. (TLO Log 세부값은 finally 문에서 설정된다.)
			throw new ImcsException(resFlag, resErrMsg);
		} finally{
			resultListVO.setFlag(resFlag);
			resultListVO.setErrMsg(resErrMsg);
			
			resultListVO.setResultCode(paramVO.getResultCode());
			
			String msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID663) + "] sts[" + ImcsConstants.LOG_MSG3 + "]"
					+ " snd[FLAG=" + resFlag + "|MESSAGE=" + resErrMsg + "]"; 
						
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID663) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			//msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID663) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
			//		+ " [DR:" + nDRedis + "|DH:" + nDHbase + "|R:" + nRedis + "|H:" + nHbase + "]"; 
			//imcsLog.serviceLog(msg, methodName, methodLine);
		}
				
		return resultListVO;
	}
	
	


	/**
	 * 알림받기 인덱스 삭제
	 * @param 	GetNSPurchasedVO paramVO
	 * @return  String
	 **/
	public int rmNSAllAlertInfo(RmNSAllAlertRequestVO paramVO) throws Exception {
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine	= String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName	= oStackTrace.getMethodName();

		String sqlId	= "lgvod663_d01_20171214_001";
		String szMsg	= "";

		int querySize	= 0;

		try {
			try {
				querySize = rmNSAllAlertDao.rmNSAllAlertInfo(paramVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해
				throw new SQLException(e.getClass().getName()+ ":" + e.getMessage());
			}

		} catch (Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID663) + "] delete [PT_VO_ALERT] table Failed at";
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			//imcsLog.failLog(ImcsConstants.API_PRO_ID663, "", cache, "fail info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}

		return querySize;
	}



	
}
