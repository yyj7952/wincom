package kr.co.wincom.imcs.api.getNSComBotList;

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
public class GetNSComBotListServiceImpl implements GetNSComBotListService {
	private Log imcsLogger = LogFactory.getLog("API_getNSComBotList");
	
	@Autowired
	private GetNSComBotListDao getNSComBotListDao;
	
	@Autowired
	private CommonService commonService;

//	public void getNSComBotList(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	@SuppressWarnings("unused")
	@Override
	public GetNSComBotListResultVO getNSComBotList(GetNSComBotListRequestVO paramVO){
//		this.getNSComBotList(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	

		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog(methodName + " service call");
		
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		List<GetNSComBotListResponseVO> resultVO	= new ArrayList<GetNSComBotListResponseVO>();
		GetNSComBotListResultVO resultListVO	= new GetNSComBotListResultVO();
	
		String msg	= "";
		
		int nMainCnt = 0;
		int nSubCnt = 0;
		int i_loop_cnt = 0;			/* i_loop_cnt는 조회된 상품 중 처음에 한번만 부가세요율정보를 가져오기 위해 사용*/		
		int mem_loop_flag = 0;					/* 멤버쉽 정기차감 조회 했는지 여부 (0 : 조회안함 / 1 : 조회함) - 가입상품이 있는 가입자만 조회하기 위해 */

	    long tp_start = paramVO.getTp_start();
		long tp1 = 0;
		long tp2 = 0;
		long tp3 = 0;
		long tp4 = 0;
		
		String szPpmImgSvrip = "";
		String nSurtaxRate			= "";
		String resultHeader = "";
		Integer messageSet	= 99;
		HashMap<String, String> mResult = new HashMap<String, String>();
		
		
		try {
			tp1 = System.currentTimeMillis();
			HashMap<String, String> testInfo = this.getTestSbc(paramVO);
			tp2 = System.currentTimeMillis();
			imcsLog.timeLog("가입자 정보 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			if(testInfo == null) {
				messageSet = 10;
				mResult = commonService.getErrorMsg(messageSet);
				resultHeader  = String.format("%s|%s|%s|%s|%s|%s|", 
						"1", mResult.get("ERR_MSG"), "", "", "", ""); 
				resultListVO.setResultHeader(resultHeader);
				return resultListVO;
			}
			
			tp1 = System.currentTimeMillis();
			HashMap<String, String> EXPInfo = this.getEXPInfo(paramVO);
			tp2 = System.currentTimeMillis();
			imcsLog.timeLog("컴페니언 봇 정보 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			try {
				String audioUrl = commonService.getIpInfo("audio_kids_server", ImcsConstants.NSAPI_GETNSCOMBOTLIST);
				paramVO.setAudioUrl(audioUrl);
			} catch(Exception e) {
				
				imcsLog.failLog(ImcsConstants.NSAPI_GETNSCOMBOTLIST, "", null, "audio_kids_svr_ip:" + ImcsConstants.NSAPI_GETNSCOMBOTLIST, methodName, methodLine);
				paramVO.setResultCode("31000000");
				throw new ImcsException();
			}
			
			tp1 = System.currentTimeMillis();
			resultVO = this.getNSComBotListInfo(paramVO);
			imcsLog.timeLog("컴패니언 봇 전체 리스트 정보 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			resultHeader  = String.format("%s|%s|%s|%s|%s|%s|", 
					"0", "", paramVO.getExpInterval(), paramVO.getExpType(), paramVO.getExpMaxCount(), paramVO.getAudioUrl()); 
			resultListVO.setResultHeader(resultHeader);
		
			resultListVO.setList(resultVO);
		} catch(Exception e) {
			//imcsLog.failLog(ImcsConstants.NSAPI_GETNSCOMBOTLIST, "", null, "svr_ip:" + ImcsConstants.RCV_MSG1, methodName, methodLine);
			throw new ImcsException();
		}
		
	    
		try{} catch(ImcsException ie) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E", ie.getClass().getName() + ":" + ie.getMessage());
		} catch(Exception e) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		} finally{
			
			msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_GETNSCOMBOTLIST) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);			
		}
		
		return resultListVO;
	}
	
	/**
	 * TEST_SBC 정보 조회
	 * @param 	GetNSComBotListRequestVO paramVO
	 * @return  HashMap<String, String>
	 **/
	public HashMap<String, String> getTestSbc(GetNSComBotListRequestVO paramVO) throws Exception {
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		
		HashMap<String, String> testSbcInfo = new HashMap<String, String>();

		try {
			testSbcInfo = getNSComBotListDao.getTestSbc(paramVO);
			
			if(testSbcInfo != null) {
				String testSbc = testSbcInfo.get("TEST_SBC") == null ? "" : testSbcInfo.get("TEST_SBC").toString();
				String viewingFlag = testSbcInfo.get("VIEWING_FLAG") == null ? "" : testSbcInfo.get("VIEWING_FLAG").toString();
				
				if(testSbc.length() > 0) {
					paramVO.setTestSbc(testSbc);
				}
				if(viewingFlag.length() > 0) {
					paramVO.setViewingFlag(viewingFlag);
				}
			}
			
		} catch (Exception e) {
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}

		return testSbcInfo;
	}

	/**
	 * TEST_SBC 정보 조회
	 * @param 	GetNSComBotListRequestVO paramVO
	 * @return  HashMap<String, String>
	 **/
	public HashMap<String, String> getEXPInfo(GetNSComBotListRequestVO paramVO) throws Exception {
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		HashMap<String, String> EXPInfo = new HashMap<String, String>();

		paramVO.setExpInterval(EXPInfo.get("EXP_INTERVAL"));
		paramVO.setExpType(EXPInfo.get("EXP_TYPE"));
		paramVO.setExpMaxCount(EXPInfo.get("EXP_MAX_COUNT"));

		try {
			EXPInfo = getNSComBotListDao.getEXPInfo(paramVO);
			if(EXPInfo != null) {
				String expInterval = EXPInfo.get("EXP_INTERVAL") == null ? "" : EXPInfo.get("EXP_INTERVAL").toString();
				String expType = EXPInfo.get("EXP_TYPE") == null ? "" : EXPInfo.get("EXP_TYPE").toString();
				String expMaxCount = EXPInfo.get("EXP_MAX_COUNT") == null ? "" : EXPInfo.get("EXP_MAX_COUNT").toString();
			
				if(expInterval.length() > 0) {
					paramVO.setExpInterval(expInterval);
				} else {
					paramVO.setExpInterval("");
				}
				if(expType.length() > 0) {
					paramVO.setExpType(expType);
				} else {
					paramVO.setExpType("");
				}
				if(expMaxCount.length() > 0) {
					paramVO.setExpMaxCount(expMaxCount);
				} else {
					paramVO.setExpMaxCount("");
				}
			}
			
		} catch (Exception e) {
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}

		return EXPInfo;
	}
	
	/**
	 * 컴패니언 봇 노출 리스트 조회
	 * @param 	GetNSComBotListRequestVO paramVO
	 * @return  HashMap<String, String>
	 **/
	public List<GetNSComBotListResponseVO> getNSComBotListInfo (GetNSComBotListRequestVO paramVO) throws Exception {
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		List<GetNSComBotListResponseVO> comBotListInfo = new ArrayList<GetNSComBotListResponseVO>();

		try {
			
			comBotListInfo = getNSComBotListDao.getNSComBotList(paramVO);
			
		} catch (Exception e) {	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}

		return comBotListInfo;
	}
	
	
}
