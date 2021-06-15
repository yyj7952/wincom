package kr.co.wincom.imcs.api.getNSMapInfo;

import java.util.ArrayList;
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
public class GetNSMapInfoService
{
	private final static String API_LOG_NAME = "000/getNSMapInfo";
	
	private Log imcsLogger	= LogFactory.getLog("API_getNSMapInfo");
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private GetNSMapInfoDao getNSMapInfoDao;
	
	public GetNSMapInfoResultVO getNSMapInfo(GetNSMapInfoRequestVO paramVO)
	{
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog("getNSMapInfo service call");
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		String msg = "";
		String[] arrAlbumIds = null;
		String szChnlImgSvrip = "";
		long tp1 = 0, tp2 = 0;
		
		ArrayList<HashMap<String, String>> listAlbumInfo = null;
		
		StringBuilder sb = new StringBuilder();
		
		GetNSMapInfoResultVO resultVO = new GetNSMapInfoResultVO();
		
		if(StringUtils.isBlank(paramVO.getSaId()) || !this.commonService.getValidParam(paramVO.getSaId(), 7, 12, 1))
		{
			msg	= " svc[" + String.format("%-20s", API_LOG_NAME) + "] 정상적인 INPUT PARAM이 아닙니다.:"+paramVO.getSaId()+","+paramVO.getStbMac()+")"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			return resultVO;
		}
		
		if(StringUtils.isBlank(paramVO.getStbMac()) || !this.commonService.getValidParam(paramVO.getStbMac(), 14, 14, 1))
		{
			msg	= " svc[" + String.format("%-20s", API_LOG_NAME) + "] 정상적인 INPUT PARAM이 아닙니다.:"+paramVO.getSaId()+","+paramVO.getStbMac()+")"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			return resultVO;
		}
		
		if(paramVO.getMultiAlbumId().length() < 15)
		{
			msg	= " svc[" + String.format("%-20s", API_LOG_NAME) + "] 정상적인 INPUT PARAM이 아닙니다.:"+paramVO.getSaId()+","+paramVO.getStbMac()+")"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			return resultVO;
		}
		
		arrAlbumIds = StringUtils.split(paramVO.getMultiAlbumId(), ",");
		paramVO.setArrAlbumIds(arrAlbumIds); // VO 객체에 배열 저장
		
		if(arrAlbumIds.length > 30)
		{
			msg	= " svc[" + String.format("%-20s", API_LOG_NAME) + "] 앨범 개수 30개 초과:"+paramVO.getSaId()+","+paramVO.getStbMac()+")"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			return resultVO;
		}
		
		for(String aId : arrAlbumIds)
		{
			if(aId.length() != 15)
			{
				msg	= " svc[" + String.format("%-20s", API_LOG_NAME) + "] 정상적인 앨범ID가 아닙니다.:"+paramVO.getSaId()+","+paramVO.getStbMac()+")"; 
				imcsLog.serviceLog(msg, methodName, methodLine);
				
				return resultVO;
			}
		}
		
		try {
			szChnlImgSvrip	= this.commonService.getImgReplaceUrl2("img_resize_server", "getNSMapInfo");
		} catch(Exception e) {
			throw new ImcsException();
		}
		
		try
		{
			tp1 = System.currentTimeMillis();
			
			// 멀티 앨범 정보 가져오기
			try {
				listAlbumInfo = this.getNSMapInfoDao.listMultiAlbumInfo(paramVO);
			} catch(Exception ex) {
				throw ex;
			}
			
			tp2	= System.currentTimeMillis();
			
			imcsLog.timeLog("multi_album_info 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			// 3. 조회되지 않은 앨범ID 결과 문자열 만들기
			for(HashMap<String, String> hm : listAlbumInfo)
			{
				for(String strId : arrAlbumIds)
				{
					if(hm.get("CONTENTS_ID").equals(strId))
					{
						// 1. RESULT_CODE = Y 결과 문자열 만들기
						if(hm.get("RESULT_CODE").equals("Y"))
						{
							sb.append(String.format("%s|%s|%s|%s|%s|%s|%s|%s|%s|\f",
									hm.get("RESULT_CODE"), 	hm.get("CAT_ID"), 		hm.get("CONTENTS_ID"),
									hm.get("SERIES_YN"), 	hm.get("SER_CAT_ID"), 	hm.get("SERIES_NO"),
									hm.get("ALBUM_NAME"), 	szChnlImgSvrip, 		hm.get("POSTER_V")));
						}		
						// 2. RESULT_CODE = N 결과 문자열 만들기(이런 경우는 없을 텐데......)
						else if(hm.get("RESULT_CODE").equals("N"))
						{
							sb.append(String.format("%s|%s|%s|%s|%s|%s|%s|%s|%s|\f",
									hm.get("RESULT_CODE"), 		"", 				"",
									"", 						"", 				"",
									"", 						szChnlImgSvrip, 	""));
						}
					}
				}
			}
			
			for(String strId : arrAlbumIds)
			{
				boolean isYn = false;
				
				for(HashMap<String, String> hm : listAlbumInfo)
				{
					if(hm.get("CONTENTS_ID").equals(strId))
					{
						isYn = true;
					}
				}
				
				// 3. 조회되지 않은 앨범ID 결과 문자열 만들기
				if(isYn == false)
				{
					sb.append(String.format("%s|%s|%s|%s|%s|%s|%s|%s|%s|\f",
							"N", 		"", 				strId,
							"", 		"", 				"",
							"", 		szChnlImgSvrip, 	""));
				}
			}
			
			
			
			resultVO.setResult(sb.toString());
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






















