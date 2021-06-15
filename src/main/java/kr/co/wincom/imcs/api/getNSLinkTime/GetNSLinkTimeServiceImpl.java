package kr.co.wincom.imcs.api.getNSLinkTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.handler.ImcsException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetNSLinkTimeServiceImpl implements GetNSLinkTimeService {
	private Log imcsLogger = LogFactory.getLog("API_getNSLinkTime");
	
	@Autowired
	private GetNSLinkTimeDao getNSLinkTimeDao;
	
	@Autowired
	private CommonService commonService;

//	public void getNSLinkTime(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	@SuppressWarnings("unused")
	@Override
	public GetNSLinkTimeResultVO getNSLinkTime(GetNSLinkTimeRequestVO paramVO){
//		this.getNSLinkTime(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	

		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog(methodName + " service call");
		
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		GetNSLinkTimeResponseVO resultVO	= new GetNSLinkTimeResponseVO();
		GetNSLinkTimeResultVO resultListVO	= new GetNSLinkTimeResultVO();
	
		String msg	= "";
		
		int nMainCnt = 0;
		int nSubCnt = 0;
		int i_loop_cnt = 0;			/* i_loop_cnt는 조회된 상품 중 처음에 한번만 부가세요율정보를 가져오기 위해 사용*/		
		int mem_loop_flag = 0;					/* 멤버쉽 정기차감 조회 했는지 여부 (0 : 조회안함 / 1 : 조회함) - 가입상품이 있는 가입자만 조회하기 위해 */

	    long tp_start = paramVO.getTp_start();
		long tp1 = 0;
		long tp2 = 0;
		                          
		try {
			
			tp1 = System.currentTimeMillis();
			String linkTime = this.getNSLinkTimeInfo(paramVO);
			
			if(linkTime == null) {
				resultVO.setLinkTime("1");
			} else {
				resultVO.setLinkTime(linkTime);
			}
			
			tp2 = System.currentTimeMillis();
			imcsLog.timeLog("이어보기 정보 조회", String.valueOf(tp2 - tp1), methodName, methodLine);

		
			resultListVO.setResult(resultVO);
		
		} catch(Exception e) {
			throw new ImcsException();
		}
		
	    
		try{} catch(ImcsException ie) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E", ie.getClass().getName() + ":" + ie.getMessage());
		} catch(Exception e) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		} finally{
			msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_GETNSLINKTIME) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);			
		}
		
		return resultListVO;
	}
	
	
	/**
	 * 이어보기 정보 조회
	 * @param 	GetNSLinkTimeRequestVO paramVO
	 * @return  String
	 **/
	public String getNSLinkTimeInfo (GetNSLinkTimeRequestVO paramVO) throws Exception {
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sLinkTime = "";

		try {
			
			sLinkTime = getNSLinkTimeDao.getNSLinkTime(paramVO);
			
		} catch (Exception e) {	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}

		return sLinkTime;
	}
	
	
}
