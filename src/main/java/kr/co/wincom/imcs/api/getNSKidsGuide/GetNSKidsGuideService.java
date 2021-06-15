package kr.co.wincom.imcs.api.getNSKidsGuide;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.handler.ImcsException;

@Service
public class GetNSKidsGuideService
{
	private final static String API_LOG_NAME = "000/getNSKidsGuide";
	
	private Log imcsLogger	= LogFactory.getLog("API_getNSKidsGuide");
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private GetNSKidsGuideDao getNSKidsGuideDao;
	
	public GetNSKidsGuideResultVO getNSKidsGuide(GetNSKidsGuideRequestVO paramVO)
	{
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog("getNSKidsGuide service call");
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		GetNSKidsGuideResultVO resultVO = new GetNSKidsGuideResultVO();
		
		String msg = "";		
		long tp1 = 0, tp2 = 0;
		
		long l_count = 0;
		String still_url = "";
		HashMap<String, String> userInfoMap = null;
		HashMap<String, String> kidsGuideCatIfno = null;
		List<GetNSKidsGuide_VO> listKidsGuide = null;
		
		StringBuilder sbBody = new StringBuilder();
		String strHeader = "";
		
		try
		{
			// 이미지 캐쉬 서버 정보 가져오기
		    try {
		    	still_url = this.commonService.getImgReplaceUrl2("img_still_server", "getNSKidsMenu");
		    	
		    	paramVO.setStill_url(still_url);
			} catch(Exception e) {
				throw new ImcsException();
			}
		    
		    tp1 = System.currentTimeMillis();
		    
		    // 일반/테스트 사용자 확인
			try {
				userInfoMap = this.getNSKidsGuideDao.getUserInfo(paramVO);
				
				if(userInfoMap != null && userInfoMap.size() > 0)
				{
					paramVO.setTestSbc(userInfoMap.get("test_sbc"));
					paramVO.setViewFlag(userInfoMap.get("view_flag"));
				}
			} catch(Exception ex) {
				resultVO.setResult("1|가입자 정보 가져오기 오류||0||\f");
				throw ex;
			}
			
			tp2	= System.currentTimeMillis();
			
			imcsLog.timeLog("가입자 정보 가져오기", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			// 현재 흘려듣기 카테고리 정보 가져오기
			try {
				kidsGuideCatIfno = this.getNSKidsGuideDao.getKidsGuideCatInfo(paramVO);
				
				if(kidsGuideCatIfno != null && kidsGuideCatIfno.size() > 0)
				{					
					paramVO.setCurrent_level(kidsGuideCatIfno.get("current_level"));
					paramVO.setParent_cat_id(StringUtil.nullToSpace(kidsGuideCatIfno.get("parent_cat_id")));
					paramVO.setSub_title(kidsGuideCatIfno.get("sub_title"));
				}
			} catch(Exception ex) {
				resultVO.setResult("1|흘려듣기 카테고리 정보 가져오기 오류||0||\f");
				throw ex;
			}
			
			tp1	= System.currentTimeMillis();
			
			imcsLog.timeLog("흘려듣기 카테고리 정보 가져오기", String.valueOf(tp1 - tp2), methodName, methodLine);
			
			// 현재 흘려듣기 전체 목록 가져오기
			try {
				listKidsGuide = this.getNSKidsGuideDao.listKidsGuide(paramVO);
			} catch(Exception ex) {
				resultVO.setResult("1|흘려듣기 전체 목록 가져오기 오류||0||\f");
				throw ex;
			}
			
			tp2	= System.currentTimeMillis();
			
			imcsLog.timeLog("흘려듣기 전체 목록 가져오기", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			for(GetNSKidsGuide_VO vo : listKidsGuide)
			{
				l_count++;
				
				// ※이미지 URL 관련 수정 주의.....
				sbBody.append(String.format("%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|\f",
						"GUD", 					vo.getGuideLevel(), 	vo.getCategoryId(),
						vo.getCategoryNm(), 	StringUtil.nullToSpace(vo.getSubTitle()), vo.getContsId(),
						vo.getContsNm(), 		vo.getAssetId(), 		vo.getAssetFileNm(),
						StringUtil.nullToSpace(vo.getGuideText()), 	still_url, 	vo.getImageFileName()
					));
			}
			
			// API 프로세스 처리 완료 후 헤더 정보 생성....
			strHeader = String.format("%s|%s|%s|%s|%s|\f",
					"0", 							"", 					l_count,
					paramVO.getCurrent_level(), 	paramVO.getSub_title()
				);
			
			resultVO.setResult(strHeader + sbBody.toString());
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



















































