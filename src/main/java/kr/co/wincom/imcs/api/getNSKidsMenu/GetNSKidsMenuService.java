package kr.co.wincom.imcs.api.getNSKidsMenu;

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
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.handler.ImcsException;

@Service
public class GetNSKidsMenuService
{
	private final static String API_LOG_NAME = "000/getNSKidsMenu";
	
	private Log imcsLogger	= LogFactory.getLog("API_getNSKidsMenu");
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private GetNSKidsMenuDao getNSKidsMenuDao;
	
	public GetNSKidsMenuResultVO getNSKidsMenu(GetNSKidsMenuRequestVO paramVO)
	{
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog("getNSKidsMenu service call");
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		GetNSKidsMenuResultVO resultVO = new GetNSKidsMenuResultVO();
		
		String msg = "";		
		long tp1 = 0, tp2 = 0;
		
		String poster_url = "";
		String still_url = "";
		String caption_url = "";
		String category_url = "";
		
		int iBgnNo = 0;
		int iEndNo = 0;
		int	iCheckCount = 0;
		int	iCheck = 0;
		int iTotalCount = 0;
		int iPageCount = 0;
		int iGuideCount = 0;
		
		HashMap<String, String> userInfoMap = null;
		KidsMenuCategoryInfo_VO catInfoVO = null;
		StringBuilder sbData = new StringBuilder();
		StringBuilder sbGuideData = new StringBuilder();
		StringBuilder sbWatchData = new StringBuilder();
		
		try
		{
			iPageCount = Integer.parseInt(paramVO.getPageCnt());
			if(Integer.parseInt(paramVO.getPageNo()) == 0)
				iBgnNo = 1;
			else
				iBgnNo = (Integer.parseInt(paramVO.getPageNo()) - 1) * iPageCount + 1;
			
			iEndNo = (Integer.parseInt(paramVO.getPageNo())) * iPageCount;
			
			paramVO.setiBgnNo(iBgnNo);
			paramVO.setiEndNo(iEndNo);
			
			// 가입자 정보 설정
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
		    	poster_url	= this.commonService.getImgReplaceUrl2("img_cachensc_server", "getNSKidsMenu");
		    	still_url = this.commonService.getImgReplaceUrl2("img_still_server", "getNSKidsMenu");
		    	caption_url = this.commonService.getImgReplaceUrl2("cap_server", "getNSKidsMenu");
		    	category_url = this.commonService.getImgReplaceUrl2("img_cat_server", "getNSKidsMenu");
		    	
		    	paramVO.setPoster_url(poster_url);
		    	paramVO.setStill_url(still_url);
		    	paramVO.setCaption_url(caption_url);
		    	paramVO.setCategory_url(category_url);
			} catch(Exception e) {
				throw new ImcsException();
			}
		    
		    tp1 = System.currentTimeMillis();
		    
		    // 일반/테스트 사용자 확인
			try {
				userInfoMap = this.getNSKidsMenuDao.getUserInfo(paramVO);
				
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
				resultVO.setResult("1|가입자 정보 가져오기 오류||||||||||\f");
				throw ex;
			}
			
			tp2	= System.currentTimeMillis();
			
			imcsLog.timeLog("가입자 정보 가져오기", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			// request_type check (요청구분에 따라 쿼리가 달라지므로...)
			if(paramVO.getRequestType().equals("A"))
			{
				tp1 = System.currentTimeMillis();
				
				// 아이들나라 대메뉴 진입시 하위 메뉴 정보 제공
				// SQL - 010 : 요청한 카테고리 정보 가져오기
				try {
					catInfoVO = this.getNSKidsMenuDao.getCategoryInfo(paramVO);
					
					paramVO.setCategory_level(catInfoVO.getCategoryLevel());
					paramVO.setCatInfo_catId(catInfoVO.getCategoryId());
				} catch(Exception ex) {
					resultVO.setResult("1|카테고리 정보가 없습니다||||||||||\f");
					throw ex;
				}
				
				if(catInfoVO.getMenuType().equals("TOP") == false)
				{
					imcsLog.serviceLog(" 아이들나라 대메뉴 카테고리가 아닙니다", methodName, methodLine);
					resultVO.setResult("1|아이들나라 대메뉴 카테고리가 아닙니다||||||||||\f");
					
					return resultVO;
				}
				
				// 이 부분은 문제 있을 수 있으니 추후 제거할 수도 있음 (pob77)
				if(catInfoVO.getCategoryType().equals(paramVO.getCategoryType()) == false)
				{
					imcsLog.serviceLog(" 요청 파라메터와 일치하지 않는 서비스 카테고리 입니다", methodName, methodLine);
					resultVO.setResult("1|요청 파라메터와 일치하지 않는 서비스 카테고리 입니다||||||||||\f");
					
					return resultVO;
				}
				
				// 카테고리 편성 정보 가져오기				
				if(paramVO.getCategoryType().equals("B") || paramVO.getTopMenuType().equals("B"))
				{
					iTotalCount = 0;
					iTotalCount = this.getTopMenuBook(imcsLog, sbData, sbWatchData, paramVO); // 책읽어주는TV 진입 메뉴 정보 제공
				}
				else
				{
					iTotalCount = 0;
					iTotalCount = this.getTopMenuList(imcsLog, sbData, paramVO); // 최상위 메뉴 정보 제공
				}
				
				tp2	= System.currentTimeMillis();
				
				imcsLog.timeLog("카테고리 편성정보 가져오기 완료", String.valueOf(tp2 - tp1), methodName, methodLine);
				
				if(iTotalCount < 0)
				{
					resultVO.setResult("1|편성정보를 가져오지 못했습니다||||||||||\f");
					
					return resultVO;
				}
			}
			else if(paramVO.getRequestType().equals("L"))
		    {
				// 아이들나라 영어유치원 레벨별 보기 메뉴 정보 가져오기
				// 요청한 카테고리의 부모카테고리리 ID를 찾아서 부모카테고리 이하 레벨 카테고리 제공
				// 요청 파라메터 체크
				if(StringUtils.isBlank(paramVO.getRequestCode()))
				{
					resultVO.setResult("1|레벨 정보가 요청 파라메터에 없습니다||||||||||\f");
					
					return resultVO;
				}
				
				tp1 = System.currentTimeMillis();
				
				try {
					catInfoVO = this.getNSKidsMenuDao.getCategoryInfo_type_L(paramVO);
					
					paramVO.setCatInfo_catId(catInfoVO.getCategoryId());
				} catch(Exception ex) {
					resultVO.setResult("1|카테고리 정보가 없습니다||||||||||\f");
					throw ex;
				}
				
				iTotalCount = 0;
				iTotalCount = this.getLevelMenu(imcsLog, sbData, paramVO);
				
			    tp2	= System.currentTimeMillis();
				
				imcsLog.timeLog("레벨별 보기 가져오기 완료", String.valueOf(tp2 - tp1), methodName, methodLine);
				
				if(iTotalCount < 0)
				{
					resultVO.setResult("1|레벨별 보기 메뉴 정보를 가져오지 못했습니다||||||||||\f");
					
					return resultVO;
				}
			}
		    else if(paramVO.getRequestType().equals("C"))
		    {
		    	tp1 = System.currentTimeMillis();
		    	
		    	// 아이들나라 서비스별 전체 메뉴 정보 가져오기
		        // 요청한 카테고리의 부모카테고리리 ID를 찾아서 부모카테고리 이하 전체 정보 제공
		    	try {
					catInfoVO = this.getNSKidsMenuDao.getCategoryInfo_type_c(paramVO);
					
					paramVO.setCategory_level(catInfoVO.getCategoryLevel());
					paramVO.setCatInfo_catId(catInfoVO.getCategoryId());
				} catch(Exception ex) {
					resultVO.setResult("1|카테고리 정보가 없습니다||||||||||\f");
					throw ex;
				}
		    	
				iTotalCount = 0;
				iTotalCount = this.getMenuList(imcsLog, sbData, paramVO); // 메뉴 정보 제공
				
				tp2	= System.currentTimeMillis();
				
				imcsLog.timeLog("카테고리 편성정보 가져오기 완료", String.valueOf(tp2 - tp1), methodName, methodLine);
				
				if(iTotalCount < 0)
				{
					resultVO.setResult("1|편성정보를 가져오지 못했습니다||||||||||\f");
					
					return resultVO;
				}
			}
			else
			{
				tp1 = System.currentTimeMillis();
				
				// 아이들나라 기준 2Level 이하 메뉴 정보 가져오기
				// SQL - 020 : 요청한 카테고리 정보 가져오기
				try {
					catInfoVO = this.getNSKidsMenuDao.getCategoryInfo_2(paramVO);
					
					paramVO.setCategory_level(catInfoVO.getCategoryLevel());
					paramVO.setCatInfo_catId(catInfoVO.getCategoryId());
				} catch(Exception ex) {
					resultVO.setResult("1|카테고리 정보가 없습니다||||||||||\f");
					throw ex;
				}
				
				if(catInfoVO.getMenuType().equals("TOP") || catInfoVO.getMenuType().equals("APP")
						|| catInfoVO.getMenuType().equals("MNU"))
				{
					imcsLog.serviceLog(" 메뉴 정보 미제공 카테고리", methodName, methodLine);
					resultVO.setResult("1|메뉴정보를 제공하는 카테고리가 아닙니다||||||||||\f");
					
					return resultVO;
				}
				
				// 에그스쿨의 경우 별도의 메시지 처리를 위해 추가 정보를 획득한다
				if(paramVO.getCategoryType().equals("H"))
				{
					HashMap<String, String> hmMsg = this.getNSKidsMenuDao.getMessageOfTypeH();
					
					if(hmMsg != null)
					{
						paramVO.setCat_msg_ppm1(StringUtil.nullToSpace(hmMsg.get("CAT_MSG_PPM1")));
						paramVO.setCat_msg_ppm2(StringUtil.nullToSpace(hmMsg.get("CAT_MSG_PPM2")));
						paramVO.setCat_msg_free(StringUtil.nullToSpace(hmMsg.get("CAT_MSG_FREE")));
					}
					
					if(catInfoVO.getMenuType().equals("PRV"))
					{
						paramVO.setInfo_text(paramVO.getCat_msg_free());
					}
				}
				
				iTotalCount = 0;
				iTotalCount = this.getMenuList(imcsLog, sbData, paramVO); // 메뉴 정보 제공
				
				tp2	= System.currentTimeMillis();
				
				imcsLog.timeLog("카테고리 편성정보 가져오기 완료", String.valueOf(tp2 - tp1), methodName, methodLine);
				
				if(iTotalCount < 0)
				{
					resultVO.setResult("1|편성정보를 가져오지 못했습니다||||||||||\f");
					
					return resultVO;
				}
			}
			
			// 가이드앨범 가져오기
			if(paramVO.getCategoryType().equals("G") || paramVO.getCategoryType().equals("T") || paramVO.getCategoryType().equals("H"))
			{
				tp1 = System.currentTimeMillis();
				
				iGuideCount = this.getGuideAlbum(imcsLog, sbGuideData, paramVO);
				
				tp2	= System.currentTimeMillis();
				
				imcsLog.timeLog("가이드앨범 가져오기 완료", String.valueOf(tp2 - tp1), methodName, methodLine);
			}
			
			// API 프로세스 처리 완료 후 헤더 정보 생성....
			String strHeader = String.format("%s|%s|%d|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|\f",
					"0", 						"", 						iTotalCount,
					"Y", 						paramVO.getCatId(), 		catInfoVO.getCategoryNm(),
					"", 						paramVO.getCategory_url(), 	catInfoVO.getTopImage(),
					catInfoVO.getGuideText(), 	catInfoVO.getGoodsId(), 	catInfoVO.getGoodsText(),
					"",							paramVO.getCategory_url(),	paramVO.getGuide_image_file(),
					paramVO.getInfo_text());
			
			// 결과 값 합치기
			resultVO.setResult(strHeader + sbData.toString() + sbGuideData.toString());
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
			
			sbData.setLength(0);
			sbGuideData.setLength(0);
			sbWatchData.setLength(0);
			sbData = null;
			sbGuideData = null;
			sbWatchData = null;
		}
		
		return resultVO;
	}
	
	/**
	 * 최상위 메뉴 정보 제공
	 */
	private int getTopMenuList(IMCSLog imcsLog, StringBuilder sbData, GetNSKidsMenuRequestVO paramVO)
	{
		int iCount = 0;
		
		List<KidsMenuMenuList_VO> listCatTopMenu = null;
		
		try {
			if(paramVO.getCategoryType().equals("C") || paramVO.getTopMenuType().equals("C"))
			{
				listCatTopMenu = this.getNSKidsMenuDao.listCategoryTopMenu_CatType_C(paramVO);
			}
			else
			{
				// SQL - 100 : 카테고리 편성 정보 가져오기 (최상위 메뉴)
				listCatTopMenu = this.getNSKidsMenuDao.listCategoryTopMenu(paramVO);
			}
		} catch(Exception ex) {
			StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
			String methodName = stackTraceElement.getMethodName();
			
			imcsLog.errorLog(methodName + "-E", ex.getClass().getName() + ":" + ex.getMessage());
			
			iCount = -1;
			return iCount;
		}
		
		for(KidsMenuMenuList_VO vo : listCatTopMenu)
		{
			iCount++;
			
			if(iCount < paramVO.getiBgnNo() || iCount > paramVO.getiEndNo())
				continue;
			
			if(StringUtils.isNotBlank(vo.getPpmProdId()) || StringUtils.isNotBlank(vo.getPpsProdId()))
			{
				// 가입여부 또는 구매여부 제공하는 로직 추가해서 개발해야 함...
	        	// 하지만 상위에는 업체상품을 편성할 이유가 없으므로 여기서는 무시해도 됨
			}
			
			if(vo.getType().equals("DIR"))
			{
				paramVO.setTemp_category_id(vo.getContsId());
				
				String temp_ser_cat_id = this.getNSKidsMenuDao.getTempSerCatId(paramVO);
				
				// 예외처리 하위에 시리즈 카테고리가 없을 때는 type 값을 제공하지 않음
				if(StringUtils.isBlank(temp_ser_cat_id))
					vo.setType("");
				else
					vo.setRecommendId(temp_ser_cat_id);
			}
			
			vo.setImage_type("C");
			
			sbData.append(String.format(""
					+ "%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|"
					+ "%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|"
					+ "%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|"
					+ "%s|%s|%s|%s|%s|%s|%s|\f",
					vo.getResultType(), 		vo.getContsId(), 			vo.getContsNm(),
					vo.getType(), 				vo.getCategoryType(), 		vo.getParentId(),
					vo.getCategorySubName(), 	vo.getSubCatYn(), 			vo.getDisplayOption(),
					vo.getCategoryFlag(),		vo.getCatEngLevel(), 		paramVO.getCategory_url(),
					vo.getImageFile(),			paramVO.getCategory_url(),	vo.getAnimationFile(),
					"", 						vo.getStillFile(), 			vo.getRecommendId(),
					vo.getServiceIcon(),		vo.getPpmProdId(), 			vo.getPpmSubscribeYn(),
					vo.getPpmProdType(),		vo.getPpsProdId(), 			vo.getPrInfo(),
					vo.getKidsGrade(),			vo.getRuntime(), 			vo.getIs51Ch(),
					vo.getIsCaption(),			vo.getIsHd(), 				vo.getWatchaPoint(),
					vo.getCinePoint(),			vo.getOrderGb(), 			vo.getWatchDate(),
					vo.getScreenType(), 		vo.getSynopsis(),			vo.getAddInfo(),
					vo.getImage_type()));
		}
		
		return iCount;
	}
	
	/**
	 * 책읽어주는TV 진입 메뉴 정보 제공
	 * @param sbGetTopMenuBook
	 * @return
	 */
	private int getTopMenuBook(IMCSLog imcsLog, StringBuilder sbData, StringBuilder sbWatchData, GetNSKidsMenuRequestVO paramVO)
	{
		int iCount = 0;
		int iWatchCount = 0;
		
		String pre_result_type = "";
		String check_id = "";
		
		List<KidsMenuMenuList_VO> listCatBookWatch = null;
		
		// 최근 시청한 책 가져오기
		try {
			listCatBookWatch = this.getNSKidsMenuDao.listCategoryTopMenuBookWatch(paramVO);		
		} catch(Exception ex) {
			StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
			String methodName = stackTraceElement.getMethodName();
			
			imcsLog.errorLog(methodName + "-E", ex.getClass().getName() + ":" + ex.getMessage());
			
			iWatchCount = 0;
		}
		
		for(KidsMenuMenuList_VO vo : listCatBookWatch)
		{
			// 동일한 앨범인 경우 SKIP
			if(vo.getContsId().equals(check_id))
				continue;
			
			iWatchCount++;
			
			check_id = vo.getContsId();
			
			vo.setImageUrl(paramVO.getPoster_url());
			vo.setOrderGb("W");
			
			vo.setImage_type("P");
			
			// ※이미지 URL 관련 수정 주의.....
			this.createMenuData(sbWatchData, vo, paramVO);
			
			if(iWatchCount >= 3)
				break;
		}
		
		/** 여기까지 최근 시청한 책 가져오기 */
		
		// 추천 카테고리
		pre_result_type = "CAT";
		int iLevel = paramVO.getCategory_level() + 1;
		
		String sugg_cat_id = this.commonService.getServerProperties("getNSKidsMenu.sugg_cat_id");
		String sugg_cat_id_2 = this.commonService.getServerProperties("getNSKidsMenu.sugg_cat_id_2");
		String sugg_cat_id_3 = this.commonService.getServerProperties("getNSKidsMenu.sugg_cat_id_3");
		
		if(paramVO.getCategoryType().equals("B")) 		// BOOK
			paramVO.setSugg_cat_id(sugg_cat_id);
		else if(paramVO.getCategoryType().equals("P")) 	// PARENT
			paramVO.setSugg_cat_id(sugg_cat_id_2);
		else if(paramVO.getCategoryType().equals("H")) 	// EGG SCHOOL
			paramVO.setSugg_cat_id(sugg_cat_id_3);
		else
			paramVO.setSugg_cat_id(sugg_cat_id);
		
		paramVO.setiLevel(iLevel);
		
		List<KidsMenuMenuList_VO> listCatTopMenuBook = null;
		
		try {
			// 카테고리 편성 정보 가져오기 (아이들나라 기준 2레벨 이하)
			listCatTopMenuBook = this.getNSKidsMenuDao.listCategoryTopMenuBook(paramVO);		
		} catch(Exception ex) {
			StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
			String methodName = stackTraceElement.getMethodName();
			
			imcsLog.errorLog(methodName + "-E", ex.getClass().getName() + ":" + ex.getMessage());
			
			iCount = -1;
			return iCount;
		}
		
		// 추천도서가 없거나 데이터를 못 가져온 경우 최근 시청 컨텐츠는 제공해야 함
		if(listCatTopMenuBook == null || listCatTopMenuBook.size() == 0)
		{
			if(pre_result_type.equals("CAT"))
			{
				sbData.append(sbWatchData.toString());
			}
		}
		
		for(KidsMenuMenuList_VO vo : listCatTopMenuBook)
		{
			// 카테고리 마치고, 컨텐츠 설정하기 전에 기존에 가져왔던, 시청정보 먼저 설정해준다
			if(pre_result_type.equals("CAT") && vo.getResultType().equals("ALB"))
			{
				sbData.append(sbWatchData.toString());
				iCount = (iCount + iWatchCount);
			}
			
			pre_result_type = vo.getResultType();
	        
			if(vo.getResultType().equals("ALB")) iCount++;
			
//			if(iCount < paramVO.getiBgnNo() || iCount > paramVO.getiEndNo())
//				continue;
			
			if(vo.getResultType().equals("CAT"))
			{
				vo.setImage_type("C");
				vo.setImageUrl(paramVO.getCategory_url());
			}
			else
			{
				vo.setImage_type("P");
				vo.setImageUrl(paramVO.getPoster_url());
			}
			
			if(iCount > 10 && vo.getResultType().equals("ALB"))
				continue;
			
			// ※이미지 URL 관련 수정 주의.....
			this.createMenuData(sbData, vo, paramVO);
		}
		
		return iCount;
	}
	
	/**
	 * 메뉴 정보 제공
	 * 카테고리 편성 정보 가져오기 (아이들나라 기준 2레벨 이하)
	 */
	private int getMenuList(IMCSLog imcsLog, StringBuilder sbData, GetNSKidsMenuRequestVO paramVO)
	{
		int	iLastLevel = 0;
		int iCount = 0;
		
		//-------------------------------------------------------------
		// LEVEL_GB에 따라 조회해야 하는 Depth를 조정한다
		// A = 요청 카테고리 포함 하위 전체 Depth
		// B = 요청 카테고리 + 하위 1레벨 카테고리 & 컨텐츠 + 하위 1레벨에 편성된 컨텐츠
		// 컨텐츠 정보 제공시 시리즈 카테고리에 편성된 컨텐츠는 제외
		//-------------------------------------------------------------
		if(paramVO.getLevelGb().equals("A"))
			iLastLevel = paramVO.getCategory_level() + 2;
		else
			iLastLevel = paramVO.getCategory_level() + 1;
		
		paramVO.setiLastLevel(iLastLevel);
		
		List<KidsMenuMenuList_VO> listCatTopMenuList = null;
		
		try {
			// 카테고리 편성 정보 가져오기 (아이들나라 기준 2레벨 이하)
			listCatTopMenuList = this.getNSKidsMenuDao.listCategoryMenuList(paramVO);
		} catch(Exception ex) {
			StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
			String methodName = stackTraceElement.getMethodName();
			
			imcsLog.errorLog(methodName + "-E", ex.getClass().getName() + ":" + ex.getMessage());
			
			iCount = -1;
			return iCount;
		}
		
		for(KidsMenuMenuList_VO vo : listCatTopMenuList)
		{
			iCount ++;
			
			if(iCount < paramVO.getiBgnNo() || iCount > paramVO.getiEndNo())
				continue;
			
			// 아이들나라 업체 상품 가입여부 체크(페어링된 IPTV 권한으로 체크함)
			if(StringUtils.isNotBlank(vo.getPpmProdId()))
			{
				if("S".equals(vo.getPpmProdType()) || "M".equals(vo.getPpmProdType()))
				{
					if(paramVO.getStbPairing().equals("Y"))
					{
						paramVO.setPpm_prod_id(vo.getPpmProdId());
						
						String strPPMSubscribe = this.getNSKidsMenuDao.getPPMSubscribe(paramVO);
						
						if(StringUtils.isNotBlank(strPPMSubscribe) && strPPMSubscribe.equals("Y"))
						{
							vo.setPpmSubscribeYn("Y");
							
							if(paramVO.getCategoryType().equals("H"))
								paramVO.setInfo_text(paramVO.getCat_msg_ppm1());
						}
						else
						{
							vo.setPpmSubscribeYn("N");
							
							if(paramVO.getCategoryType().equals("H"))
								paramVO.setInfo_text(paramVO.getCat_msg_ppm2());
						}
					}
					else
					{
						vo.setPpmSubscribeYn("N");
						
						if(paramVO.getCategoryType().equals("H"))
							paramVO.setInfo_text(paramVO.getCat_msg_ppm2());
					}
				}
				else
				{
					paramVO.setPpm_prod_id(vo.getPpmProdId());
					
					String strPPMSubscribe = this.getNSKidsMenuDao.getPPMSubscribeMobile(paramVO);
					
					if(StringUtils.isNotBlank(strPPMSubscribe) && strPPMSubscribe.equals("Y"))
					{
						vo.setPpmSubscribeYn("Y");
					}
					else
					{
						vo.setPpmSubscribeYn("N");
					}
				}
			}
			
			// 카테고리의 경우 이미지 처리...
			if(vo.getResultType().equals("CAT"))
			{
				if(vo.getType().equals("SER") && StringUtils.isBlank(vo.getImageFile()))
				{
					vo.setImage_type("P");
					vo.setImageUrl(paramVO.getPoster_url());
					vo.setImageFile(vo.getPosterV());
				}
				else
				{
					 vo.setImage_type("C");
					if(StringUtils.isNotBlank(vo.getImageFile()))
						vo.setImageUrl(paramVO.getCategory_url());
				}
			}
			else
			{
				vo.setImage_type("P");
				vo.setImageUrl(paramVO.getPoster_url());
			}
			
			// ※이미지 URL 관련 수정 주의.....
			this.createMenuData(sbData, vo, paramVO);
		}
		
		return iCount;
	}
	
	/**
	 * 영어유치원 레벨별 보기 메뉴 정보 제공
	 * @param imcsLog
	 * @param sbData
	 * @param paramVO
	 * @return
	 */
	private int getLevelMenu(IMCSLog imcsLog, StringBuilder sbData, GetNSKidsMenuRequestVO paramVO)
	{
		int iCount = 0;
		
		// LEVEL_GB 값 무시하고 제공
		List<KidsMenuMenuList_VO> listCatLevelMenu = null;
		
		try {
			// 영어유치원 레벨별 보기 메뉴 정보 제공
			listCatLevelMenu = this.getNSKidsMenuDao.listCategoryLevelMenu(paramVO);
		} catch(Exception ex) {
			StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
			String methodName = stackTraceElement.getMethodName();
			
			imcsLog.errorLog(methodName + "-E", ex.getClass().getName() + ":" + ex.getMessage());
			
			iCount = -1;
			return iCount;
		}
		
		for(KidsMenuMenuList_VO vo : listCatLevelMenu)
		{
			iCount ++;
			
			if(iCount < paramVO.getiBgnNo() || iCount > paramVO.getiEndNo())
				continue;
			
			if(StringUtils.isNotBlank(vo.getPpmProdId()) || StringUtils.isNotBlank(vo.getPpsProdId()))
			{
				// 가입여부 또는 구매여부 제공하는 로직 추가해서 개발해야 함...
			}
			
			// 카테고리의 경우 이미지 처리...
			if(vo.getResultType().equals("CAT"))
			{
				if(vo.getType().equals("SER") && StringUtils.isBlank(vo.getImageFile()))
				{
					vo.setImage_type("P");
					vo.setImageUrl(paramVO.getPoster_url());
					vo.setImageFile(vo.getPosterV());
				}
				else
				{
					vo.setImage_type("C");
					if(StringUtils.isNotBlank(vo.getImageFile()))
						vo.setImageUrl(paramVO.getCategory_url());
				}
			}
			else
			{
				vo.setImage_type("C");
				vo.setImageUrl(paramVO.getPoster_url());
			}
			
			// ※이미지 URL 관련 수정 주의.....
			this.createMenuData(sbData, vo, paramVO);
		}
		
		return iCount;
	}
	
	/**
	 * 흘려듣기 영상 가져오기
	 * @param imcsLog
	 * @param sbGuideData
	 * @param paramVO
	 * @return
	 */
	private int getGuideAlbum(IMCSLog imcsLog, StringBuilder sbGuideData, GetNSKidsMenuRequestVO paramVO)
	{
	    //-------------------------------------------------------------------------//
	    // SQL - 500 : 흘려듣기 편성된 카테고리 가져오기
	    //-------------------------------------------------------------------------//
	    // 1. 가입자 영어테스트 레벨에 해당하는 카테고리 가져오기
	    // 2. 최고레벨 편성이 달라질 수 없으므로 1에 해당하는 레벨이 없는 경우 최고레벨 카테고리 가져오기
	    // 3. 하위호환성 유지를 위해 레벨 흘려듣기 카테고리가 없는 경우 기존 카테고리 가져오기
	    //-------------------------------------------------------------------------//
		if(StringUtils.isBlank(paramVO.getRequestCode()))
			paramVO.setCust_eng_level("2");
		else
			paramVO.setCust_eng_level(paramVO.getRequestCode());
		
		KidsMenuGuide_VO tempLevelInfo = null;
		
		try {
			tempLevelInfo = this.getNSKidsMenuDao.getGuideTempLevelInfo(paramVO);
			
			if(tempLevelInfo == null)
				tempLevelInfo = new KidsMenuGuide_VO();
			
			// 헤더에서 사용
			paramVO.setGuide_image_file(StringUtil.nullToSpace(tempLevelInfo.getGuideImageFile()));
			
			if(StringUtils.isNotBlank(tempLevelInfo.getTempLevelId()))
				paramVO.setGuide_cat_id(StringUtil.nullToSpace(tempLevelInfo.getTempLevelId()));
			else
			{
				if(StringUtils.isNotBlank(tempLevelInfo.getTempMaxLevelId()))
					paramVO.setGuide_cat_id(StringUtil.nullToSpace(tempLevelInfo.getTempMaxLevelId()));
				else
					paramVO.setGuide_cat_id(StringUtil.nullToSpace(tempLevelInfo.getTempParentId()));
			}
		} catch(Exception ex) {
			StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
			String methodName = stackTraceElement.getMethodName();
			
			imcsLog.errorLog(methodName + "-E", ex.getClass().getName() + ":" + ex.getMessage());
		}
		
		int iGuideCount = 0;
		String guideListCategory_id = "";
		
		List<KidsMenuGuide_VO> listGuide = null;
		
		try {
			listGuide = this.getNSKidsMenuDao.listGuideAlbums(paramVO);
		} catch(Exception ex) {
			StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
			String methodName = stackTraceElement.getMethodName();
			
			imcsLog.errorLog(methodName + "-E", ex.getClass().getName() + ":" + ex.getMessage());
			
			iGuideCount = -1;
			return iGuideCount;
		}
		
		for(KidsMenuGuide_VO vo : listGuide)
		{
			iGuideCount++;
			guideListCategory_id = "";
			
			//하위호환성 유지를 위해 흘려듣기 메인 카테고리를 가져온 경우에는 전체화면용 카테고리ID를 제공하지 않음
	        //정상적으로 레벨 카테고리를 가져온 경우에만 제공함
			if(!paramVO.getGuide_cat_id().equals(StringUtil.nullToSpace(tempLevelInfo.getTempParentId())))
			{
				guideListCategory_id = paramVO.getGuide_cat_id();
			}
			
			//※이미지 URL 관련 수정 주의.....
			sbGuideData.append(String.format("%s|%s|%s|%s|%s|%s|%s|%s|%s|\f",
					"GUD", 						vo.getContsId(), 			vo.getContsNm(),
					vo.getM3u8File1(), 			vo.getM3u8File2(), 			vo.getGuideText(),
					paramVO.getStill_url(),		vo.getImageFileName(),		guideListCategory_id));
		}
		
		return iGuideCount;
	}
	
	/**
	 * Response 데이터를 생성. 똑같은 소스가 계속 반복되어서 메소드에서 만드는 걸로 처리.
	 * 
	 * @param sbData
	 * @param vo
	 */
	private void createMenuData(StringBuilder sbData, KidsMenuMenuList_VO vo, GetNSKidsMenuRequestVO paramVO)
	{
		// ※이미지 URL 관련 수정 주의.....
		sbData.append(String.format(""
				+ "%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|"
				+ "%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|"
				+ "%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|"
				+ "%s|%s|%s|%s|%s|%s|%s|\f",
				vo.getResultType(), 			vo.getContsId(), 			vo.getContsNm(),
				vo.getType(), 					vo.getCategoryType(), 		vo.getParentId(),
				vo.getCategorySubName(), 		vo.getSubCatYn(), 			vo.getDisplayOption(),
				vo.getCategoryFlag(),			vo.getCatEngLevel(), 		vo.getImageUrl(),
				vo.getImageFile(),				paramVO.getCategory_url(),	vo.getAnimationFile(),
				paramVO.getStill_url(),			vo.getStillFile(),			vo.getRecommendId(),
				vo.getServiceIcon(),			vo.getPpmProdId(),			vo.getPpmSubscribeYn(),
				vo.getPpmProdType(),			vo.getPpsProdId(), 			vo.getPrInfo(),
				vo.getKidsGrade(),				vo.getRuntime(), 			vo.getIs51Ch(),
				vo.getIsCaption(),				vo.getIsHd(), 				vo.getWatchaPoint(),
				vo.getCinePoint(),				vo.getOrderGb(), 			vo.getWatchDate(),
				vo.getScreenType(), 			vo.getSynopsis(),			vo.getAddInfo(),
				vo.getImage_type()));
	}
}



























