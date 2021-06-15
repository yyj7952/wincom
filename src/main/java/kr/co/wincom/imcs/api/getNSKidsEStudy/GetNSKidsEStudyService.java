package kr.co.wincom.imcs.api.getNSKidsEStudy;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.handler.ImcsException;

@Service
public class GetNSKidsEStudyService
{
	private final static String API_LOG_NAME = "000/getNSKidsEStudy";
	
	private Log imcsLogger	= LogFactory.getLog("API_getNSKidsEStudy");
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private GetNSKidsEStudyDao getNSKidsEStudyDao;
	
	public GetNSKidsEStudyResultVO getNSKidsEStudy(GetNSKidsEStudyRequestVO paramVO)
	{
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog("getNSKidsEStudy service call");
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		GetNSKidsEStudyResultVO resultVO = new GetNSKidsEStudyResultVO();
		
		String msg = "";		
		long tp1 = 0, tp2 = 0;
		
		String szChnlImgSvrip = "";
		String tmpsndbuf_header = "";
		String tmpsndbuf_body = "";
		String tmpsndbuf_footer = "";
		
		try {
			szChnlImgSvrip	= this.commonService.getImgReplaceUrl2("img_resize_server", "getNSKidsEStudy");
		} catch(Exception e) {
			throw new ImcsException();
		}
		
		try
		{
			tp1 = System.currentTimeMillis();
			
			// 고객의 레벨별 진행상황 퍼센트율 조회
			try {
				tmpsndbuf_header = this.get_level_percent(paramVO);
			} catch(Exception ex) {
				resultVO.setResult("1||||||||\f");
				throw ex;
			}
			
			tp2	= System.currentTimeMillis();
			
			imcsLog.timeLog("레벨별 진행상황 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			tp1 = System.currentTimeMillis();
			
			// 고객의 3Depth 카테고리별 진행상황 퍼센트율 조회
			try {
				tmpsndbuf_body = this.get_cat_percent(paramVO);
			} catch(Exception ex) {
				resultVO.setResult("1||||||||\f");
				throw ex;
			}
			
			imcsLog.timeLog("3Depth 카테고리별 진행상황 조회", String.valueOf(tp1 - tp2), methodName, methodLine);
			
			tp2	= System.currentTimeMillis();
			
			// 고객 level에 가장 많이 시청한 콘텐츠 정보를 제공한다 (6개 제공하며, 전일 기준으로 집계한다.)
			try {
				tmpsndbuf_footer = this.get_level_watch_suggest(paramVO, szChnlImgSvrip);
			} catch(Exception ex) {
				resultVO.setResult("1||||||||\f");
				throw ex;
			}
			
			imcsLog.timeLog("가장 많이 시청한 콘텐츠 정보 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			resultVO.setResult(tmpsndbuf_header + tmpsndbuf_body + tmpsndbuf_footer);
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
	
	/**
	 * 고객의 레벨별 진행상황 퍼센트율 조회
	 * @param paramVO
	 * @return
	 * @throws Exception
	 */
	private String get_level_percent(GetNSKidsEStudyRequestVO paramVO) throws Exception
	{
		String retVal = "";
		
		// 헤더에 있는 레벨 진도율 6개를 일단 공백으로 선언
		String[] arrPercent = {"", "", "", "", "", ""};
		
		List<HashMap<String, String>> listLevel = this.getNSKidsEStudyDao.listLevelReadPersent(paramVO);
		
		if(listLevel == null || listLevel.size() == 0)
			retVal = "0||||||||\f";
		else
		{
			for(int i = 0; i < listLevel.size(); i++)
			{
				HashMap<String, String> hm = listLevel.get(i);
				
				int point = Integer.parseInt(hm.get("POINT"));
				String percent = hm.get("READ_PERSENT");
				
				if(point >= 1 && point <= 6)
				{
					arrPercent[point - 1] = percent;
				}
			}
			
			retVal = String.format("0||%s|%s|%s|%s|%s|%s|\f",
					arrPercent[0], arrPercent[1], arrPercent[2],
					arrPercent[3], arrPercent[4], arrPercent[5]);
		}
		
		if(listLevel != null)
			listLevel.clear();
		
		arrPercent = null;
		
		return retVal;
	}
	
	/**
	 * 고객의 3Depth 카테고리별 진행상황 퍼센트율 조회
	 * @param paramVO
	 * @return
	 * @throws Exception
	 */
	public String get_cat_percent(GetNSKidsEStudyRequestVO paramVO) throws Exception
	{
		String retVal = "";
		
		StringBuilder sb = new StringBuilder();
		
		List<HashMap<String, String>> listCat = this.getNSKidsEStudyDao.listCategoryReadPersent(paramVO);
		
		if(listCat == null || listCat.size() == 0)
			retVal = "CAT||||\f";
		else
		{
			for(HashMap<String, String> hm : listCat)
			{
				sb.append(String.format("CAT|%s|%s|%s|\f",
						hm.get("TWO_DEPTH_CAT_ID"),
						hm.get("TWO_DEPTH_CAT_NAME"),
						hm.get("READ_PERSENT")));
			}
			
			retVal = sb.toString();
		}
		
		if(listCat != null)
			listCat.clear();
		
		sb = null;
		
		return retVal;
	}
	
	/**
	 * 고객 level에 가장 많이 시청한 콘텐츠 정보를 제공한다 (6개 제공하며, 전일 기준으로 집계한다.)
	 * @param paramVO
	 * @return
	 * @throws Exception
	 */
	public String get_level_watch_suggest(GetNSKidsEStudyRequestVO paramVO, String szChnlImgSvrip) throws Exception
	{
		String retVal = "";
		
		StringBuilder sb = new StringBuilder();
		
		List<HashMap<String, String>> list = this.getNSKidsEStudyDao.listSuggestLevel(paramVO);
		
		if(list != null && list.size() > 0)
		{
			for(HashMap<String, String> hm : list)
			{
				sb.append(String.format("ALB|%s|%s|%s|%s|%s|%s|%s|%s|\f",
						hm.get("CAT_ID"), 		hm.get("ALBUM_ID"), hm.get("SER_CAT_YN"),
						hm.get("SER_CAT_ID"), 	hm.get("SER_NO"), 	hm.get("ALBUM_NAME"),
						szChnlImgSvrip, 		hm.get("IMG_FILE_NAME_V")));
			}
			
			retVal = sb.toString();
		}
		
		if(list != null)
			list.clear();
		
		sb = null;
		
		return retVal;
	}
}































