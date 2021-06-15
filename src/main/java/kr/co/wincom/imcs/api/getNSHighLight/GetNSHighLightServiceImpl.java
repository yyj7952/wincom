package kr.co.wincom.imcs.api.getNSHighLight;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import kr.co.wincom.imcs.api.getNSHighLight.GetNSHighLightRequestVO;
import kr.co.wincom.imcs.api.getNSHighLight.GetNSHighLightResponseVO;
import kr.co.wincom.imcs.api.getNSHighLight.GetNSHighLightResultVO;
import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.property.ImcsProperties;
import kr.co.wincom.imcs.common.util.GlobalCom;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.StillImageVO;
import kr.co.wincom.imcs.handler.ImcsException;

@Service
public class GetNSHighLightServiceImpl implements GetNSHighLightService {
	
	String SERVER_CONF = ImcsProperties.getProperty("filepath.server");
	Properties serverManager = GlobalCom.getPropertyFile(SERVER_CONF);
	private String BaseBallServerUrl = StringUtil.replaceNull(serverManager.getProperty("BaseBall_server_url"),"http://localhost:8070/highlightapi/"); //getNSHitSum 서버 IP
	String BaseBallTimeOut = StringUtil.replaceNull(serverManager.getProperty("BaseBall_HighLight_timeout"),"3000");
	String BaseBallServerUseYn = StringUtil.replaceNull(serverManager.getProperty("BaseBall_server_use_yn"),"N");
	
	private Log imcsLogger = LogFactory.getLog("API_getNSHighLight");
	
	@Autowired
	private GetNSHighLightDao getNSHighLightDao;
	
	@Autowired
	private CommonService commonService;

	
	/**
	 * 
	 * @author 
	 * @since 
	 */
	@Override
	public GetNSHighLightResultVO getNSHighLight(GetNSHighLightRequestVO paramVO) {
		
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog("GetNSHighLight service call");
		
		String isLastProcess = ImcsConstants.RCV_MSG5;

		List<GetNSHighLightResponseVO> resultVO	= new ArrayList<GetNSHighLightResponseVO>();
		GetNSHighLightResultVO	resultListVO	= new GetNSHighLightResultVO();
		
		String msg			= "";
		
		long tp1 = 0, tp2 = 0;
		
		try {
			resultVO = this.getHighLightInfo(paramVO);
			
			tp1	= System.currentTimeMillis();			
			imcsLog.timeLog("하이라이트 정보 조회", String.valueOf(tp1 - tp2), methodLine, methodLine);
			
			tp2	= System.currentTimeMillis();			
			imcsLog.timeLog("하이라이트 정보 Fetch", String.valueOf(tp2 - tp1), methodLine, methodLine);
			
			resultListVO.setFlag("0");
			resultListVO.setTotalCount(String.valueOf(resultVO.size()));
			resultListVO.setMessage("SUCCESS");
			resultListVO.setList(resultVO);
		} catch(ImcsException ce) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			resultListVO.setFlag("1");
			resultListVO.setTotalCount("0");
			resultListVO.setMessage("FAIL");
		} catch(Exception e) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			resultListVO.setFlag("1");
			resultListVO.setTotalCount("0");
			resultListVO.setMessage("FAIL");
		} finally{
			resultListVO.setResultCode(paramVO.getResultCode());
			
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID990) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"; 
						
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
				
		return resultListVO;
	}
	
	/**
	 * 
	 * @param 
	 * @return 
	 **/
	public List<GetNSHighLightResponseVO> getHighLightInfo(GetNSHighLightRequestVO paramVO) throws Exception {
		
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodName = oStackTrace.getMethodName();
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
    	
		String sqlId = "lgvod569_004_20171214_001";
		String msg				= "";
		
		List<GetNSHighLightResponseVO> list = new ArrayList<GetNSHighLightResponseVO>();
		
		int querySize = 0;

		try {
			try {
				// list = getNSHighLightDao.getHighLightInfo(paramVO);
				long tp1 = System.currentTimeMillis();
				String still_url    = commonService.getImgReplaceUrl2("img_still_server", "getNSHighLight");
				
				if(BaseBallServerUseYn.equals("Y"))
				{
		        	try
		        	{
		        		//URL호출
		        		
				        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
						factory.setConnectTimeout(Integer.parseInt(BaseBallTimeOut));
						factory.setReadTimeout(Integer.parseInt(BaseBallTimeOut));
						
						RestTemplate restTemplate = new RestTemplate(factory);
						
						Gson gson = new Gson();
						
						//(예시) http://localhost:8080/getNSHitSum
						String getNSHitSumServerFullUrl = String.format("%s%s?SA_ID=%s&MAC_ADDR=%s&FLAG=%s&SCENE_TYPE=%s",
								BaseBallServerUrl, 
								"getNSHitSum",
								paramVO.getSaId(), 
								paramVO.getStbMac(),
								paramVO.getRequestFlag(),
								"H");
						String retJsonStr = restTemplate.getForObject(getNSHitSumServerFullUrl, String.class); //getNSHitSum 찔러서 결과가져오기 (최근 30건)
						Type type = new TypeToken<List<GetNSHitSumResultVO>>(){}.getType();
						List<GetNSHitSumResultVO> listHitSumResult = gson.fromJson(retJsonStr, type);
						
						long tp2 = System.currentTimeMillis();
				        imcsLog.timeLog("getNSHitSumServerFullUrl 조회 개수:" + String.valueOf(listHitSumResult.size()), String.valueOf(tp2 - tp1), methodName, "");
						
						GetNSHighLightResponseVO responseVO = null;
						GetNSHighLightAlbumVO highlight_albumVO = null;
						for (GetNSHitSumResultVO item : listHitSumResult) {
							System.out.println(item.getAlbum_id());
							
							highlight_albumVO = getNSHighLightDao.getHighlightAlbumInfo(item);
							
							if (highlight_albumVO != null) {
								List<StillImageVO> getStillImage = getNSHighLightDao.getStillImage(item.getAlbum_id());
								String still_file_name = "";
								if (getStillImage.size() > 0) {
									still_file_name = getStillImage.get(0).getImgFileName();
								}
								
								responseVO = new GetNSHighLightResponseVO();
								responseVO.setAlbumId(item.getAlbum_id());
								responseVO.setAlbumNm(highlight_albumVO.getAlbumNm());
								responseVO.setOnairDate(highlight_albumVO.getOnairDate());
								responseVO.setSceneType(item.getScene_type());
								responseVO.setSysnopsis(highlight_albumVO.getSysnopsis());
								responseVO.setWatchCount(item.getTot_cnt());
								responseVO.setStillUrl(still_url);
								responseVO.setStillFileName(still_file_name);
								responseVO.setRunTime(highlight_albumVO.getRunTime());
								list.add(responseVO);
								
							}
							
						}
						
						long tp3 = System.currentTimeMillis();
				        imcsLog.timeLog("하이라이트 목록 개수:" + String.valueOf(list.size()), String.valueOf(tp3 - tp2), methodName, "");
	
						
		        	}catch(Exception e)
		        	{
		        		msg	= "[BaseBall HighLight VOD Call Error : " + e.getMessage().substring(0, e.getMessage().length() > 20 ? 20 : e.getMessage().length()) + "]"; 								
						imcsLog.serviceLog(msg, methodName, methodLine);
		        	}
				}
				else
				{
					msg	= "[BaseBall HighLight VOD Call Pass]"; 								
					imcsLog.serviceLog(msg, methodName, methodLine);
				}
				
				RestTemplate restTemplate = new RestTemplate();
				
/*				GetNSHighLightResponseVO responseVO = new GetNSHighLightResponseVO();
				responseVO.setAlbumId("M0119BR012PPV00");
				responseVO.setAlbumNm("[MZ] 프로야구 검색구분");
				responseVO.setOnairDate("20191127");
				responseVO.setSceneType("1");
				responseVO.setSysnopsis("[MZ] 프로야구 검색구분");
				responseVO.setWatchCount("10");
				responseVO.setStillUrl("\\\\123.140.16.76\\still\\");
				responseVO.setStillFileName("ST_M011827068PPV00_105308.jpg");
				list.add(responseVO);
				
				responseVO = new GetNSHighLightResponseVO();
				responseVO.setAlbumId("M01199Q034PPV00");
				responseVO.setAlbumNm("9/26 프로야구");
				responseVO.setOnairDate("20200926");
				responseVO.setSceneType("2");
				responseVO.setSysnopsis("9/26 프로야구");
				responseVO.setWatchCount("1");
				responseVO.setStillUrl("\\\\123.140.16.76\\still\\");
				responseVO.setStillFileName("ST_M011827068PPV00_105309.jpg");
				list.add(responseVO);
				
				responseVO = new GetNSHighLightResponseVO();
				responseVO.setAlbumId("M011952038PPV00");
				responseVO.setAlbumNm("0502_본편_프로야구");
				responseVO.setOnairDate("20190502");
				responseVO.setSceneType("2");
				responseVO.setSysnopsis("0502_본편_프로야구");
				responseVO.setWatchCount("2");
				responseVO.setStillUrl("\\\\123.140.16.76\\still\\");
				responseVO.setStillFileName("ST_M011319B38PPV00_161315.jpg");
				list.add(responseVO);
				
				responseVO = new GetNSHighLightResponseVO();
				responseVO.setAlbumId("M01196I040PPV00");
				responseVO.setAlbumNm("4D 엑셀 프로야구");
				responseVO.setOnairDate("20190105");
				responseVO.setSceneType("2");
				responseVO.setSysnopsis("4D 엑셀 프로야구");
				responseVO.setWatchCount("5");
				responseVO.setStillUrl("\\\\123.140.16.76\\still\\");
				responseVO.setStillFileName("ST_M011343A01PPV00_152651.bmp");
				list.add(responseVO);
				
				responseVO = new GetNSHighLightResponseVO();
				responseVO.setAlbumId("M01192M135PPV00");
				responseVO.setAlbumNm("프로야구_단편_MG_0222_003");
				responseVO.setOnairDate("20200222");
				responseVO.setSceneType("1");
				responseVO.setSysnopsis("프로야구_단편_MG_0222_003");
				responseVO.setWatchCount("1");
				responseVO.setStillUrl("\\\\123.140.16.76\\still\\");
				responseVO.setStillFileName("ST_M011319B38PPV00_161315.jpg");
				list.add(responseVO);
*/
				
			} catch(DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if(list != null && !list.isEmpty()) {
				querySize = list.size();
			}
			
		} catch(Exception e) {

		}

		return list;
	}
}
