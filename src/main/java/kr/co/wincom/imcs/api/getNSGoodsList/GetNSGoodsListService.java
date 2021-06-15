package kr.co.wincom.imcs.api.getNSGoodsList;

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
public class GetNSGoodsListService
{
	private final static String API_LOG_NAME = "000/getNSGoodsList";
	
	private Log imcsLogger	= LogFactory.getLog("API_getNSGoodsList");
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private GetNSGoodsListDao GetNSGoodsListDao;
	
	public GetNSGoodsListResultVO getNSGoodsList(GetNSGoodsListRequestVO paramVO)
	{
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog("getNSGoodsList service call");
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		GetNSGoodsListResultVO resultVO = new GetNSGoodsListResultVO();
		
		String msg = "";		
		long tp1 = 0, tp2 = 0;
		
		String poster_url = "";
		String still_url = "";
		String caption_url = "";
		String category_url = "";
		String product_url = "";
		
		int	iCheck = 0;
		int iTotalCount = 0;
		int iAlbumCount = 0;
		String strMsg = "";
		
		HashMap<String, String> userInfoMap = null;
		StringBuilder sbGoodsList = new StringBuilder();
		StringBuilder sbAlbums = new StringBuilder();
		StringBuilder sbImages = new StringBuilder();
		
		try
		{
			//가입자 정보 설정
			String c_idx_sa = paramVO.getSaId().substring(paramVO.getSaId().length() - 2, paramVO.getSaId().length());
			int p_idx_sa = 0;
			
			try {
				p_idx_sa = Integer.parseInt(c_idx_sa) % 33;
			} catch (NumberFormatException e) {
				p_idx_sa = 0;
			}
			
			paramVO.setP_idx_sa(p_idx_sa);
			
			// 이미지 캐쉬 서버 정보 가져오기(poster_new -> poster_url 로 설정)
		    try {
		    	poster_url	= this.commonService.getImgReplaceUrl2("img_resize_server", "getNSGoodsList");
		    	still_url = this.commonService.getImgReplaceUrl2("img_still_server", "getNSGoodsList");
		    	caption_url = this.commonService.getImgReplaceUrl2("cap_server", "getNSGoodsList");
		    	category_url = this.commonService.getImgReplaceUrl2("img_cat_server", "getNSGoodsList");
		    	product_url = this.commonService.getImgReplaceUrl2("img_ppm_server", "getNSGoodsList");
		    	
		    	paramVO.setPoster_url(poster_url);
		    	paramVO.setStill_url(still_url);
		    	paramVO.setCaption_url(caption_url);
		    	paramVO.setCategory_url(category_url);
		    	paramVO.setProduct_url(product_url);
			} catch(Exception e) {
				throw new ImcsException();
			}
		    
		    tp1 = System.currentTimeMillis();
		    
		    // 일반/테스트 사용자 확인
			try {
				userInfoMap = this.GetNSGoodsListDao.getUserInfo(paramVO);
				
				if(userInfoMap != null && userInfoMap.size() > 0)
				{
					paramVO.setTestSbc(userInfoMap.get("test_sbc"));
					paramVO.setViewFlag(userInfoMap.get("view_flag"));
				}
			} catch(Exception ex) {
				resultVO.setResult("1|가입자 정보 가져오기 오류||\f");
				throw ex;
			}
			
			tp2	= System.currentTimeMillis();
			
			imcsLog.timeLog("가입자 정보 가져오기", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			tp1 = System.currentTimeMillis();
			
			// 카테고리에 편성된 체험상품 리스트 정보 제공
			iTotalCount = this.getGoodsList(imcsLog, sbGoodsList, paramVO);
			
			tp2	= System.currentTimeMillis();
			
			imcsLog.timeLog("메뉴 정보 가져오기 완료", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			if(iTotalCount < 0)
			{
				resultVO.setResult("1|편성된 상품 정보를 가져오지 못했습니다||\f");
				
				return resultVO;
			}
			else
			{
				tp1 = System.currentTimeMillis();
				
				if(iTotalCount >= 0)
					iAlbumCount = this.getRelaAlbum(imcsLog, sbAlbums, paramVO);
				
				tp2	= System.currentTimeMillis();
				
				imcsLog.timeLog("연관 앨범 정보 가져오기 완료", String.valueOf(tp2 - tp1), methodName, methodLine);
			}
			
			// 상품정보가 존재할 경우 이미지 정보 가져오기
			if(iTotalCount > 0)
			{
				this.getImageList(imcsLog, sbImages, paramVO);
			}
			else
			{
				strMsg = "편성된 상품 정보가 없습니다";
			}
			
			String strHeader = String.format("0|%s|%s|\f", strMsg, iTotalCount);
			
			// 결과값 합치기...
			resultVO.setResult(String.format("%s%s%s%s",
					strHeader, sbGoodsList.toString(), sbImages.toString(), sbAlbums.toString()));
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
			
			sbGoodsList.setLength(0);
			sbAlbums.setLength(0);
			sbImages.setLength(0);
		}
		
		return resultVO;
	}
	
	/**
	 * - request_type check (요청구분에 따라 쿼리가 달라지므로...)
	 * - 카테고리 ID를 받았을 경우 카테고리에 편성된 목록을 제공 (상품 + 앨범)
	 * - 상품 ID를 받았을 경우 단일 상품 정보 제공
	 * @param imcsLog
	 * @param sbGoodsList
	 * @param paramVO
	 * @return
	 */
	private int getGoodsList(IMCSLog imcsLog, StringBuilder sbGoodsList, GetNSGoodsListRequestVO paramVO)
	{
		int iCount = 0;
		
		List<GoodsListReqTypeCat_VO> listReqTypeCat = null;
		
		if(paramVO.getRequestType().equals("0"))
	    {
		    // 체험상품 편성 정보 가져오기
			try {
				listReqTypeCat = this.GetNSGoodsListDao.listReqTypeCategory(paramVO);		
			} catch(Exception ex) {
				StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
				String methodName = stackTraceElement.getMethodName();
				
				imcsLog.errorLog(methodName + "-E", ex.getClass().getName() + ":" + ex.getMessage());
				
				iCount = -1;
				return iCount;
			}
	    }
		else
		{
			// 요청 상품 정보 가져오기
			try {
				listReqTypeCat = this.GetNSGoodsListDao.listReqTypeGoodsId(paramVO);		
			} catch(Exception ex) {
				StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
				String methodName = stackTraceElement.getMethodName();
				
				imcsLog.errorLog(methodName + "-E", ex.getClass().getName() + ":" + ex.getMessage());
				
				iCount = -1;
				return iCount;
			}
		}
		
		for(GoodsListReqTypeCat_VO vo : listReqTypeCat)
		{
			iCount ++;
			
			if(paramVO.getRequestType().equals("0"))
			{
	        	vo.setStillImageUrl(paramVO.getProduct_url());
	        	vo.setTopImageUrl(paramVO.getProduct_url());
			}
			else
			{
	        	vo.setStillImageUrl(paramVO.getStill_url());
	        	vo.setTopImageUrl(paramVO.getStill_url());
			}
			
			sbGoodsList.append(String.format(""
					+ "%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|"
					+ "%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|"
					+ "%s|%s|%s|%s|%s|\f",
					vo.getResultType(), 	vo.getContsId(), 			vo.getContsNm(),
					vo.getRuntime(), 		vo.getEventInfo(), 			vo.getPrInfo(),
					vo.getKidsGrade(), 		vo.getServiceIcon(), 		vo.getOnairDate(),
					vo.getReleaseDate(), 	vo.getCountry(), 			vo.getDirector(),
					vo.getProducer(), 		vo.getStarringActor(), 		vo.getActor(),
					vo.getVoiceActor(), 	vo.getIsCaption(), 			vo.getSmiLanguage(),
					vo.getIs51Ch(), 		vo.getContsInfo(), 			vo.getSynopsis(),
					vo.getRelaAlbumYn(), 	vo.getRefUrl(),				vo.getSynopsis2(),
					vo.getSynopsis3()));
		}
		
		return iCount;
	}
	
	/**
	 * 메뉴 정보 제공
	 * @param imcsLog
	 * @param sbAlbums
	 * @param paramVO
	 * @return
	 */
	private int getRelaAlbum(IMCSLog imcsLog, StringBuilder sbAlbums, GetNSGoodsListRequestVO paramVO)
	{
		int iCount = 0;
		
		List<GoodsListReqTypeCat_VO> listRelaAlbums = null;
		
		if(paramVO.getRequestType().equals("0"))
	    {
		    // 체험상품 편성 정보 가져오기
			try {
				listRelaAlbums = this.GetNSGoodsListDao.listRelaAlbums(paramVO);		
			} catch(Exception ex) {
				StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
				String methodName = stackTraceElement.getMethodName();
				
				imcsLog.errorLog(methodName + "-E", ex.getClass().getName() + ":" + ex.getMessage());
				
				iCount = -1;
				return iCount;
			}
	    }
		else
		{
			// SQL - 210 : 요청 상품의 연관 앨범 가져오기 (단일상품) -- 단 개발만 하고 실제 수행되는 케이스는 없음
			//---------------------------------
	        // 향후 제공하게 될 때 아래 쿼리 사용
	        // 만들어 놓고 버리기 아까워서...
	        //---------------------------------
	        /*
			EXEC SQL
	        SELECT G.goods_id conts_id,
	               G.album_id rela_album_id,
	               A.album_name rela_album_nm,
	               SUBSTR(T.run_time, 1, 6) run_time
	          FROM IMCSUSER.PT_KD_GOODS_MST G,
	               IMCSUSER.PT_LA_ALBUM_INFO A,
	               IMCSUSER.PT_LA_ALBUM_SUB T
	         WHERE G.album_id = A.album_id
	           AND A.album_id = T.album_id
	           AND G.goods_id = 'A202001-0000003'
	           AND NVL(G.viewing_flag, 'V') IN ('V', 'T')
	        */
		}
		
		if(listRelaAlbums != null)
		{
			for(GoodsListReqTypeCat_VO vo : listRelaAlbums)
			{
				iCount ++;
				
				sbAlbums.append(String.format("%s|%s|%s|%s|%s|\f",
						"R", 					vo.getContsId(), 	vo.getRelaAlbumId(),
						vo.getRelaAlbumNm(), 	vo.getRuntime()));
			}
		}
		
		return iCount;
	}
	
	/**
	 * 이미지 리스트 제공
	 * request_type check (요청구분에 따라 쿼리가 달라지므로...)
	 * 카테고리 ID를 받았을 경우 카테고리에 편성된 목록을 제공 (상품 + 앨범)
	 * 상품 ID를 받았을 경우 단일 상품 정보 제공
	 * 
	 * @param imcsLog
	 * @param sbImages
	 * @param paramVO
	 */
	private int getImageList(IMCSLog imcsLog, StringBuilder sbImages, GetNSGoodsListRequestVO paramVO)
	{
		int iCount = 0;
		String imageUrl = "";
		
		List<GoodsListReqTypeCat_VO> listImages = null;
		
		if(paramVO.getRequestType().equals("0"))
	    {
			// 체험상품 편성된 상품의 이미지 가져오기 (목록)
			try {
				listImages = this.GetNSGoodsListDao.listImagesRequestType_0(paramVO);		
			} catch(Exception ex) {
				StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
				String methodName = stackTraceElement.getMethodName();
				
				imcsLog.errorLog(methodName + "-E", ex.getClass().getName() + ":" + ex.getMessage());
				
				iCount = -1;
				return iCount;
			}
	    }
		else
		{
			// 단독 상품의 이미지 가져오기 (목록)
			try {
				listImages = this.GetNSGoodsListDao.listImagesRequestType_1(paramVO);		
			} catch(Exception ex) {
				StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
				String methodName = stackTraceElement.getMethodName();
				
				imcsLog.errorLog(methodName + "-E", ex.getClass().getName() + ":" + ex.getMessage());
				
				iCount = -1;
				return iCount;
			}
		}
		
		if(listImages != null)
		{
			for(GoodsListReqTypeCat_VO vo : listImages)
			{
				iCount ++;
				
				if(paramVO.getRequestType().equals("0") && vo.getImageType().equals("S"))
				{
					imageUrl = paramVO.getStill_url();
					vo.setImageType("A");
				}
				else
				{
					imageUrl = paramVO.getCategory_url();
				}
				
				sbImages.append(String.format("%s|%s|%s|%s|%s|\f",
						"IMG", 		vo.getContsId(), 		vo.getImageType(),
						imageUrl, 	vo.getImageFileName()));
			}
		}
		
		return iCount;
	}
}





















