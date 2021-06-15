package kr.co.wincom.imcs.api.getNSKidsHome;

import java.util.HashMap;
import java.util.List;

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
public class GetNSKidsHomeService
{
	private final static String API_LOG_NAME = "000/getNSKidsHome";
	
	private Log imcsLogger	= LogFactory.getLog("API_getNSKidsHome");
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private GetNSKidsHomeDao getNSKidsHomeDao;
	
	public GetNSKidsHomeResultVO getNSKidsHome(GetNSKidsHomeRequestVO paramVO)
	{
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog("getNSKidsHome service call");
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		GetNSKidsHomeResultVO resultVO = new GetNSKidsHomeResultVO();
		
		String msg = "";		
		long tp1 = 0, tp2 = 0;
		
		String poster_url = "";
		String still_url = "";
		String caption_url = "";
		String category_url = "";
		
		String watchRespData = "";
		StringBuilder sbRecomRespData = new StringBuilder();
		StringBuilder sbMenuRespData = new StringBuilder();
		
		HashMap<String, String> userInfoMap = null;
		KidsHomeWatchInfo_1_VO watchInfo_1_vo = null;
		KidsHomeWatchInfo_album_VO watchInfo_album_vo = null;
		List<KidsHomeMenu_VO> listMenus = null;
		
		try
		{
			//가입자 정보 설정
			String c_idx_sa = paramVO.getSaId().substring(paramVO.getSaId().length() - 2, paramVO.getSaId().length());
			int p_idx_sa = 0;
			int p_stb_idx_sa = 0;
			
			try {
				p_idx_sa = Integer.parseInt(c_idx_sa) % 33;
			} catch (NumberFormatException e) {
				p_idx_sa = 0;
			}
			
			paramVO.setP_idx_sa(p_idx_sa);
			
		    // 이미지 캐쉬 서버 정보 가져오기(poster_new -> poster_url 로 설정)
		    try {
		    	poster_url	= this.commonService.getImgReplaceUrl2("img_resize_server", "getNSKidsHome");
		    	still_url = this.commonService.getImgReplaceUrl2("img_still_server", "getNSKidsHome");
		    	caption_url = this.commonService.getImgReplaceUrl2("cap_server", "getNSKidsHome");
		    	category_url = this.commonService.getImgReplaceUrl2("img_cat_server", "getNSKidsHome");
			} catch(Exception e) {
				throw new ImcsException();
			}
		    
		    tp1 = System.currentTimeMillis();
		    
		    // 일반/테스트 사용자 확인
			try {
				userInfoMap = this.getNSKidsHomeDao.getUserInfo(paramVO);
				
				if(userInfoMap != null && userInfoMap.size() > 0)
				{
					paramVO.setTestSbc(userInfoMap.get("test_sbc"));
					paramVO.setViewFlag(userInfoMap.get("view_flag"));
					paramVO.setStbSaId(userInfoMap.get("stb_sa_id"));
					paramVO.setStbMacAddr(userInfoMap.get("stb_mac_addr"));
					paramVO.setStbPairing(userInfoMap.get("stb_pairing"));
				}
				
				// 가입자 정보 설정 (페어링된 NSC 가입자)
				if(paramVO.getStbPairing().equals("Y"))
				{
					String stb_idx_sa = paramVO.getStbSaId().substring(paramVO.getStbSaId().length() - 2, paramVO.getStbSaId().length());
					try {
						p_stb_idx_sa = Integer.parseInt(stb_idx_sa) % 33;
					} catch (NumberFormatException e) {
						p_stb_idx_sa = 0;
					}
				}
				else
				{
					p_stb_idx_sa = 0;
					paramVO.setStbSaId("X");
				}
				
				paramVO.setP_stb_idx_sa(p_stb_idx_sa);
			} catch(Exception ex) {
				resultVO.setResult("1|가입자 정보 가져오기 오류|\f");
				throw ex;
			}
			
			tp2	= System.currentTimeMillis();
			
			imcsLog.timeLog("가입자 정보 가져오기", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			// 최근 시청 정보 가져오기
			if(paramVO.getRequestFlag().equals("00") || paramVO.getRequestFlag().equals("10"))
			{
				tp1 = System.currentTimeMillis();
				
				if("Y".equals(paramVO.getStbPairing()))
				{
					paramVO.setiCheckFlag(1);
					paramVO.setTemp_stb_sa_id(paramVO.getStbSaId());
				}
				else
				{
					paramVO.setiCheckFlag(2);
					paramVO.setTemp_stb_sa_id("X");
				}
				
				try {
					watchInfo_1_vo = this.getNSKidsHomeDao.getWatchInfo_1(paramVO);
				} catch(Exception ex) {
					resultVO.setResult("1|최근 시청 정보 가져오기 오류|\f");
					throw ex;
				}
				
				if(watchInfo_1_vo == null)
				{
					imcsLog.serviceLog(" watch info no data found", methodName, methodLine);
				}
				else
				{
					// SQL - 011 : 앨범 정보 가져오기
					paramVO.setAlbumId(watchInfo_1_vo.getAlbumId());
					
					try {
						watchInfo_album_vo = this.getNSKidsHomeDao.getWatchInfoAlbum(paramVO);
					} catch(Exception ex) {
						resultVO.setResult("1|최근 시청 앨범 정보 가져오기 오류|\f");
						throw ex;
					}
					
					if(watchInfo_album_vo == null)
					{
						imcsLog.serviceLog(" watch albumm not found", methodName, methodLine);
					}
					else
					{
						if(watchInfo_1_vo.getLinkTime() <= 5)
						{
							watchInfo_1_vo.setLink_yn("N");
						}
						else
						{
							double iCheck;
							double iContTime;
							
							iContTime = watchInfo_album_vo.getiRuntime();
							iCheck = (watchInfo_1_vo.getLinkTime() / iContTime) * 100;
							
							if(iCheck >= 98)
								watchInfo_1_vo.setLink_yn("N");
							else
								watchInfo_1_vo.setLink_yn("Y");
						}
						
						watchInfo_1_vo.setPoster_v_file(watchInfo_album_vo.getPosterV());
						watchInfo_1_vo.setPoster_v_url(poster_url);
						watchInfo_1_vo.setStill_url(still_url);
						watchInfo_1_vo.setPoster_h_url(poster_url);
						watchInfo_1_vo.setPoster_h_file(watchInfo_album_vo.getPosterH());
						
						watchRespData = String.format("%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|\f",
		                		"WATCH", 							watchInfo_1_vo.getAlbumId(), 		watchInfo_album_vo.getAlbumNm(),
		                		watchInfo_1_vo.getCategoryId(), 	watchInfo_1_vo.getSeriesYn(), 		watchInfo_1_vo.getSeriesNo(),
		                		watchInfo_album_vo.getSeriesDesc(), watchInfo_album_vo.getRuntime(), 	watchInfo_1_vo.getLink_yn(),
		                		watchInfo_1_vo.getPoster_h_url(), 	watchInfo_1_vo.getPoster_h_file(), 	watchInfo_1_vo.getPoster_v_url(),
		                		watchInfo_1_vo.getPoster_v_file(), 	watchInfo_1_vo.getStill_url(), 		watchInfo_album_vo.getStillFile(),
		                		watchInfo_1_vo.getLinkTime());
					}
				}
				
				tp2 = System.currentTimeMillis();
				
				imcsLog.timeLog("최근 시청 정보 가져오기", String.valueOf(tp2 - tp1), methodName, methodLine);
			}
			
		    // 사업차 추천 컨텐츠 가져오기
			if(paramVO.getRequestFlag().equals("00") || paramVO.getRequestFlag().equals("20"))
		    {
				tp1 = System.currentTimeMillis();
				
				String sugg_cat_id = this.commonService.getServerProperties("getNSKidsHome.sugg_cat_id");
				paramVO.setSugg_cat_id(sugg_cat_id);
				
				List<KidsHomeRecom_VO> list = null;
				try {
					list = this.getNSKidsHomeDao.listKidsHomeRecom(paramVO);
				} catch(Exception ex) {
					resultVO.setResult("1|사업차 추천 컨텐츠 가져오기|\f");
					throw ex;
				}
				
				for(KidsHomeRecom_VO vo : list)
				{
					vo.setPoster_v_file(vo.getPosterV());
					vo.setPoster_v_url(poster_url);
					vo.setStillUrl(still_url);
					vo.setPoster_h_url(poster_url);
					vo.setPoster_h_file(vo.getPosterH());
					vo.setCategoryId(paramVO.getSugg_cat_id());
					
					sbRecomRespData.append(String.format("%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|\f",
							"RECOM", 				vo.getCategoryId(), 	vo.getContsId(),
							vo.getContsNm(), 		vo.getRuntime(), 		vo.getPoster_h_url(),
							vo.getPoster_h_file(), 	vo.getPoster_v_url(), 	vo.getPoster_v_file(),
							vo.getStillUrl(), 		vo.getStillFile()));
				}
				
				tp2 = System.currentTimeMillis();
				
		        imcsLog.timeLog("사업차 추천 컨텐츠 가져오기", String.valueOf(tp2 - tp1), methodName, methodLine);
		    }
			
			// 최근 사용한 메뉴 가져오기
			if(paramVO.getRequestFlag().equals("00") || paramVO.getRequestFlag().equals("30"))
			{
				tp1 = System.currentTimeMillis();
				
				int iMenuCount = 0;
				
				try {
					listMenus = this.getNSKidsHomeDao.listKidsHomeMenu(paramVO);
				} catch(Exception ex) {
					resultVO.setResult("1|최근 사용한 메뉴 가져오기 오류|\f");
					throw ex;
				}
				
				if(listMenus == null || listMenus.size() == 0)
				{
					imcsLog.serviceLog(" menu info not found", methodName, methodLine);
				}
				else
				{
					for(KidsHomeMenu_VO vo : listMenus)
					{
						 iMenuCount ++;
						 
						 if(iMenuCount > 20 )
							 break;
						 
						 // 상세 즉시 진입 설정인 경우 첫번째 시리즈 카테고리 정보를 제공해야 함
						 if(vo.getType().equals("DIR"))
						 {
							 paramVO.setTemp_category_id(vo.getMenuId());
							 
							 String tempSerCatId = this.getNSKidsHomeDao.getSeriesTypeDirCategoryId(paramVO);
							 
							 // 예외처리 하위에 시리즈 카테고리가 없을 때는 type 값을 제공하지 않음
							 if(StringUtils.isBlank(tempSerCatId))
								 vo.setType("");
							 else
								 vo.setRecommendId(tempSerCatId);
						 }
				            
						sbMenuRespData.append(String.format("%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|\f",
								"MENU", 			vo.getSuggestType(), 	vo.getMenuId(),
								vo.getMenuNm(), 	vo.getMenuDesc(), 		vo.getType(),
								vo.getCatType(), 	vo.getRecommendId(), 	category_url,
								vo.getImageFile(), 	vo.getCatFlag(), 		vo.getDispOption()));
					}
				}
				
				tp2 = System.currentTimeMillis();
				
				imcsLog.timeLog("최근 사용한 메뉴 가져오기", String.valueOf(tp2 - tp1), methodName, methodLine);
			}
			
			// 결과 저장
			resultVO.setResult(String.format("0||\f%s%s%s", watchRespData, sbRecomRespData.toString(), sbMenuRespData.toString()));
			
			sbMenuRespData.setLength(0);
			sbRecomRespData.setLength(0);
			sbMenuRespData = null;
			sbRecomRespData = null;
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



























