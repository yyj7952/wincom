package kr.co.wincom.imcs.api.getNSLastWatch;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.handler.ImcsException;

@Service
public class GetNSLastWatchService
{
	private final static String API_LOG_NAME = "000/getNSLastWatch";
	
	private Log imcsLogger	= LogFactory.getLog("API_getNSLastWatch");
	
	@Autowired
	private GetNSLastWatchDao getNSLastWatchDao;
	
	@Autowired
	private CommonService commonService;
	
	public GetNSLastWatchResultVO getNSLastWatch(GetNSLastWatchRequestVO paramVO)
	{
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog("GetNSLastWatch service call");
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		GetNSLastWatchResultVO resultVO = new GetNSLastWatchResultVO();
		
		String msg = "";
		String test_sbc = "N";
		
		long tp1 = 0, tp2 = 0;
		
		HashMap<String, String> hm = null;
		
		try
		{
			// Validation 체크
			this.myValidationCheck(paramVO, resultVO);
			
			if(StringUtils.isNotBlank(resultVO.getResult()))
			{
				msg	= " svc[" + String.format("%-20s", API_LOG_NAME) + "] 정상적인 INPUT PARAM이 아닙니다.:"+paramVO.getSaId()+","+paramVO.getStbMac()+")"; 
				imcsLog.serviceLog(msg, methodName, methodLine);
				
				return resultVO;
			}
			
			tp1 = System.currentTimeMillis();
			
			// 검수 STB여부 조회
			try {
				test_sbc = this.getTestSbc(paramVO);
			} catch(Exception ex) {
				resultVO.setResult(String.format("1|%s|\f", "가입자 정보 가져오기 오류"));
				throw ex;
			}
			
			tp2	= System.currentTimeMillis();
			
			imcsLog.timeLog("가입자 정보 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			// 가입자정보 세팅
			paramVO.setTestSbc(test_sbc);
			
			// 마지막 시청기록 가져오기
			try {
				hm = this.getLastWathcInfo(paramVO);
			} catch(Exception ex) {
				resultVO.setResult(String.format("1|%s|\f", "마지막 시청기록 가져오기 오류"));
				throw ex;
			}
			
			tp1	= System.currentTimeMillis();			
			imcsLog.timeLog("마지막 시청기록 조회", String.valueOf(tp1 - tp2), methodName, methodLine);
			
			// 최종 결과물
			if(hm == null || hm.size() == 0)
			{
				resultVO.setResult(String.format("1|%s|\f", "해당 카테고리 내 시청한 컨텐츠가 존재하지 않습니다."));
			}
			else
			{
				resultVO.setResult(
			    		String.format("0||\f%s|%s|\f", hm.get("ADI_ALBUM_ID"), hm.get("WATCH_DATE"))
			    	);
			}
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
			if(hm != null)
			{
				hm.clear();
				hm = null;
			}
			
			resultVO.setResultCode(paramVO.getResultCode());
			
			msg	= " svc[" + String.format("%-20s", API_LOG_NAME) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"; 
			
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultVO;
	}
	
	/**
	 * 검수 STB여부 조회
	 * @param paramVO
	 * @return String		TEST_SBC	
	 */
	@SuppressWarnings("rawtypes")
	private String getTestSbc(GetNSLastWatchRequestVO paramVO) throws Exception
	{
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String testSbc = "N";

		try
		{
			HashMap<String, String> testSbcMap = this.getNSLastWatchDao.getTestSbc(paramVO);
			
			if(testSbcMap != null && testSbcMap.size() > 0)
			{
				testSbc = testSbcMap.get("TEST_SBC1");
			}
		}
		catch(Exception e)
		{
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			
			throw e;
		}
		
		return testSbc;
	}
	
	/**
	 * 마지막 시청 기록 조회
	 * @param paramVO
	 * @return
	 * @throws Exception
	 */
	private HashMap<String, String> getLastWathcInfo(GetNSLastWatchRequestVO paramVO) throws Exception
	{
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		HashMap<String, String> map = null;

		try
		{
			map = this.getNSLastWatchDao.getLastWathcInfo(paramVO);
		}
		catch(Exception e)
		{
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			
			throw e;
		}
		
		return map;
	}
	
	/**
	 * 파라미터 Validation 체크
	 * @param resultVO
	 * @return
	 */
	private void myValidationCheck(GetNSLastWatchRequestVO paramVO, GetNSLastWatchResultVO resultVO)
	{
		String errorMsg = String.format("1|%s|\f", "REQUEST PARAMETER ERROR");
		
		if(StringUtils.isBlank(paramVO.getSaId()) || !this.commonService.getValidParam(paramVO.getSaId(), 7, 12, 1))
		{
			resultVO.setResult(errorMsg);
			return;
		}
		
		if(StringUtils.isBlank(paramVO.getStbMac()) || !this.commonService.getValidParam(paramVO.getStbMac(), 14, 14, 1))
		{
			resultVO.setResult(errorMsg);
			return;
		}
		
		if(StringUtils.isBlank(paramVO.getCatId()))
		{
			resultVO.setResult(errorMsg);
			return;
		}
		
		if(paramVO.getCatId().length() > 5 || paramVO.getCatId().length() < 4)
		{
			resultVO.setResult(errorMsg);
			return;
		}
	}
}






















