package kr.co.wincom.imcs.api.getNSSubCount;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.handler.ImcsException;

@Service
public class GetNSSubCountService
{
	private final static String API_LOG_NAME = "000/getNSSubCount";
	
	private Log imcsLogger	= LogFactory.getLog("API_getNSSubCount");
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private GetNSSubCountDao getNSSubCountDao;
	
	public GetNSSubCountResultVO getNSSubCount(GetNSSubCountRequestVO paramVO)
	{
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog("getNSSubCount service call");
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		GetNSSubCountResultVO resultVO = new GetNSSubCountResultVO();
		
		String msg = "";		
		long tp1 = 0, tp2 = 0;
		
		int subCnt = 0;
		
		HashMap<String, String> userInfoMap = null;
		
		try
		{
			tp1 = System.currentTimeMillis();
			
			// 일반/테스트 사용자 확인
			try {
				userInfoMap = this.getNSSubCountDao.getUserInfo(paramVO);
				
				if(userInfoMap != null && userInfoMap.size() > 0)
				{
					paramVO.setTestSbc(userInfoMap.get("test_sbc"));
					paramVO.setViewFlag(userInfoMap.get("view_flag"));
				}
			} catch(Exception ex) {
				resultVO.setResult("1|가입자 정보 가져오기 오류|\f");
				throw ex;
			}
			
			tp2	= System.currentTimeMillis();
			
			imcsLog.timeLog("가입자 정보 가져오기", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			tp1 = System.currentTimeMillis();
			
			// 요청한 카테고리의 하위 카테고리 컨텐츠 수 가져오기
			try {
				subCnt = this.getNSSubCountDao.getCategorySubContsCnt(paramVO);
			} catch(Exception ex) {
				resultVO.setResult("1|컨텐츠 가져오기 DB Error|\f");
				throw ex;
			}
			
			imcsLog.timeLog("전체 편성 건수 가져오기", String.valueOf(tp1 - tp2), methodName, methodLine);
			
			resultVO.setResult(String.format("0||\f%s|\f", subCnt));
		}
		catch(ImcsException ce)
		{
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E", ce.getClass().getName() + ":" + ce.getMessage());
			
			paramVO.setResultCode("31000000");
		}
		catch(Exception e)
		{
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			
			paramVO.setResultCode("31000000");
		}
		finally
		{			
			resultVO.setResultCode(paramVO.getResultCode());
			
			msg	= " svc[" + String.format("%-20s", API_LOG_NAME) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"; 
			
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultVO;
	}
}


























