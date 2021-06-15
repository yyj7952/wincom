package kr.co.wincom.imcs.api.getNSContList;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.ComDataFreeVO;
import kr.co.wincom.imcs.common.vo.ComTrailerVO;
import kr.co.wincom.imcs.common.vo.ComWatchaVO;
import kr.co.wincom.imcs.common.vo.ContTypeVO;
import kr.co.wincom.imcs.common.vo.OstInfoVO;
import kr.co.wincom.imcs.common.vo.StillImageVO;
import kr.co.wincom.imcs.common.vo.SvodPkgVO;
import kr.co.wincom.imcs.handler.ImcsException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class GetNSContListServiceImpl implements GetNSContListService {
	private Log imcsLogger		= LogFactory.getLog("API_getNSContList");
	
	@Autowired
	private GetNSContListDao getNSContListDao;
	
	@Autowired
	private CommonService commonService;
	
//	public void getNSContList(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	/**
	 * @author HONG
	 * @since 2016-07-12
	 * 컨텐츠 타입과 구매여부, 구매시간, 구매타입 값을 리턴 (lgvod995.pc)
	 */
	@Override
	public GetNSContListResultVO getNSContList(GetNSContListRequestVO paramVO)	{
//		this.getNSContList(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		imcsLog.debugLog(methodName + " service call");
		
		List<GetNSContListResponseVO> resultVO	= new ArrayList<GetNSContListResponseVO>();
		List<GetNSContListResponseVO> returnVO	= new ArrayList<GetNSContListResponseVO>();
		List<OstInfoVO> OstReturnVO				= new ArrayList<OstInfoVO>();
		GetNSContListResponseVO tempVO			= new GetNSContListResponseVO();
		GetNSContListResponseVO contDescVO		= new GetNSContListResponseVO();
		GetNSContListResultVO	resultListVO	= new GetNSContListResultVO();
		ComDataFreeVO datafreeVO = new ComDataFreeVO();
		
		long tp1			= 0;		// timePoint 1
		long tp2			= 0;		// timePoint 2
		long tpOst, tpWatcha	= 0;
		
		String msg			= "";
		
		int nStartNo		= 0;
		int nEndNo			= 0;
		int nPageNo			= 0;
		int nPageCnt		= 0;
		int nPageIdx		= 0;
		
		int MAX_RES_CNT     = 150;
		
		String szSelectAll	= "";
		
		String szImgSvrIp		= "";		// 이미지 서버 IP
		String szImgCasheSvrIp	= "";		// 이미지 캐쉬서버 IP
		String szImgResizeSvrIp	= "";		// 이미지 리사이즈서버 IP
		String szImgSvrUrl		= "";		// 이미지 서버 URL
		String szDate		= "";
		String szTrilerUrl1	= "";
		String szTrilerUrl2	= "";
		String szTrilerUrl3	= "";
		
		int nMainCnt		= 0;		// 메인쿼리 Count
		int nSubCnt			= 0;
		int nStillCnt		= 0;
		int nGenreCnt		= 0;
		
		try {
			tp1 = System.currentTimeMillis();
			
			// 시작, 끝 번호 저장
			nPageNo		= Integer.parseInt(StringUtil.nullToZero(paramVO.getPageNo()));
			nPageCnt	= Integer.parseInt(StringUtil.nullToZero(paramVO.getPageCnt()));
			nPageIdx	= Integer.parseInt(StringUtil.nullToZero(paramVO.getPageIdx()));

			if(nPageNo != 0 && nPageCnt != 0) {
				nStartNo	= (nPageNo * nPageCnt) - (nPageCnt - 1);
				nEndNo		= nPageNo * nPageCnt;
				
				if(nPageIdx > 0) {
					nStartNo	= nPageIdx;
					nEndNo		= nStartNo + (nPageCnt - 1);
				}
				
				szSelectAll	= "N";
			} else {
				szSelectAll	= "Y";
			}
			
			paramVO.setSelectAll(szSelectAll);
			paramVO.setStartNo(String.valueOf(nStartNo));
			paramVO.setEndNo(String.valueOf(nEndNo));
			
			// 서버IP정보 조회
			try {
				szImgCasheSvrIp		= commonService.getIpInfo("img_cachensc_server", ImcsConstants.API_PRO_ID995.split("/")[1]);		// 이미지 캐쉬서버 IP 조회
				szImgSvrUrl			= commonService.getIpInfo("img_resize_server", ImcsConstants.API_PRO_ID995.split("/")[1]);			// 이미지서버 URL 조회
				szImgSvrIp			= commonService.getIpInfo("img_server", ImcsConstants.API_PRO_ID995.split("/")[1]);		
				szImgResizeSvrIp	= commonService.getIpInfo("img_create_server", ImcsConstants.API_PRO_ID995.split("/")[1]);	
				
			} catch(Exception e) {
//				imcsLog.failLog(ImcsConstants.API_PRO_ID995, "", null, "svr_ip:" + ImcsConstants.RCV_MSG1, methodName, methodLine);
				paramVO.setResultCode("31000000");
				
				throw new ImcsException();
			}

			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("init...", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			try {
				szDate	= commonService.getSysdateYMD();
			} catch(Exception e) {
//				imcsLog.failLog(ImcsConstants.API_PRO_ID995, "", null, "sysdate:" + ImcsConstants.RCV_MSG2, methodName, methodLine);
			}
	
	
			// TEST계정 유무 조회
		    String test_sbcYN = this.testSbc(paramVO);
		    paramVO.setTestSbc(test_sbcYN);
			
			
			// SVOD 패키지 정보 조회 
			List<SvodPkgVO> lSvodPkg = new ArrayList<SvodPkgVO>();
			SvodPkgVO svodPkgVO = new SvodPkgVO();
			
			try {
				lSvodPkg = this.getSvodPkg(paramVO);
				if(lSvodPkg != null)	nMainCnt = lSvodPkg.size();
			} catch (Exception e) {
				paramVO.setResultCode("41000000");
			}
			
			for(int i = 0; i < nMainCnt; i++){
				svodPkgVO = lSvodPkg.get(i);
			
				if("PKG".equals(svodPkgVO.getSvodPkg())) {
					paramVO.setPkgYn("Y");
					// paramVO.setPkgProdId(svodPkgVO.getProuctId());		// 이후 소스 내 사용안함
				} else if("SVOD".equals(svodPkgVO.getSvodPkg())){
					paramVO.setSvodYn("Y");
					// paramVO.setSvodProdId(svodPkgVO.getProuctId());		// 이후 소스 내 사용안함
				}
			}

			
			// 트레일러 URL 리스트 조회
			ComTrailerVO trilerVO	= new ComTrailerVO();
			
			trilerVO	= this.getTrilerList(paramVO);
			
			if(trilerVO != null) {
				szTrilerUrl1	= trilerVO.getTrailerUrl1();
				szTrilerUrl2	= trilerVO.getTrailerUrl2();
				szTrilerUrl3	= trilerVO.getTrailerUrl3();
			}

			
			tp1	= System.currentTimeMillis();
			imcsLog.timeLog("가입자/카테고리/NODE 정보 조회", String.valueOf(tp1 - tp2) ,methodName, methodLine);
			
			
			// 컨텐츠 리스트 조회
			resultVO = this.getContList(paramVO);
			
			if(resultVO != null)	nMainCnt = resultVO.size();
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("컨텐츠정보 조회", String.valueOf(tp2 - tp1) ,methodName, methodLine);
			
			for(int i = 0; i < nMainCnt; i++){
		    	if(i == MAX_RES_CNT)
		    	{
		    		break;
		    	}
		    	
				tempVO = resultVO.get(i);
			
				tempVO.setImgUrl(szImgSvrIp);
				tempVO.setVodServer1(szTrilerUrl1);
				tempVO.setVodServer2(szTrilerUrl2);
				tempVO.setVodServer3(szTrilerUrl3);
				
				if(i == nMainCnt -1 && !tempVO.getCatName().equals("")) {
					resultListVO.setScatName(tempVO.getCatName());
				}
				datafreeVO = null;
				datafreeVO = new ComDataFreeVO();
			
				paramVO.setContsId(tempVO.getAlbumId());
				paramVO.setNscGb(tempVO.getNscGb());
												
				// 컨텐츠 상세 정보 조회
				contDescVO = this.getContDesc(paramVO);
				
				if(contDescVO != null) {
					String szTempProdType	= "";
					
					tempVO.setAlbumName(contDescVO.getAlbumName());
					tempVO.setPreviewYn(contDescVO.getPreviewYn());
					tempVO.setOnairDate(contDescVO.getOnairDate());
					tempVO.setSeriesDesc(contDescVO.getSeriesDesc());
					tempVO.setRealHd(contDescVO.getRealHd());
					tempVO.setServiceGb(contDescVO.getServiceGb());
					tempVO.setPoint(contDescVO.getPoint());
					tempVO.setAdiProdId(contDescVO.getAdiProdId());
					tempVO.setAudioType(contDescVO.getAudioType());
					//tempVO.setSmiYn(contDescVO.getSmiYn());
					//tempVO.setSmiIpmYn(contDescVO.getSmiIpmYn());
					tempVO.setIsHd(contDescVO.getIsHd());
					tempVO.setIs3d(contDescVO.getIs3d());
					tempVO.setSuggestedPrice(contDescVO.getSuggestedPrice());
					tempVO.setRunTime(contDescVO.getRunTime());
					tempVO.setPrInfo(contDescVO.getPrInfo());
					tempVO.setSynopsis(contDescVO.getSynopsis());
					tempVO.setOverseerName(contDescVO.getOverseerName());
					tempVO.setActor(contDescVO.getActor());
					//tempVO.setDistributor(contDescVO.getDistributor());
					tempVO.setEventValue(contDescVO.getEventValue());
					tempVO.setReleaseDate(contDescVO.getReleaseDate());
					tempVO.setGenreMid(contDescVO.getGenreMid());
					tempVO.setGenreLarge(contDescVO.getGenreLarge());
					tempVO.setTerrCh(contDescVO.getTerrCh());
					tempVO.setImgFileName(contDescVO.getImgFileName());
					tempVO.setDownCnt(contDescVO.getDownCnt());
//					tempVO.setProductType(contDescVO.getProductType());
					tempVO.setContentsId(contDescVO.getContentsId());		// 통계용으로 넣어야 함
					tempVO.setContentsName(contDescVO.getContentsName());	// 통계용으로 넣어야 함
					tempVO.setVodFileName1(contDescVO.getVodFileName1());
					tempVO.setVodFileName2(contDescVO.getVodFileName1());
					tempVO.setVodFileName3(contDescVO.getVodFileName1());
					//tempVO.setVodFileSize(contDescVO.getVodFilesize());
					tempVO.setFmYn(contDescVO.getFmYn());
					tempVO.setIsCaption(contDescVO.getIsCaption());
					tempVO.setSmiLanguage(contDescVO.getSmiLanguage());
					tempVO.setReservedPrice(contDescVO.getReservedPrice());
					tempVO.setPreviewFlag(contDescVO.getPreviewFlag());
					tempVO.setTerrEdDate(contDescVO.getTerrEdDate());
					tempVO.setMycutYn(contDescVO.getMycutYn());
					tempVO.setReservedDate(contDescVO.getReservedDate());
					tempVO.setGenreUxten(contDescVO.getGenreUxten());
					tempVO.setPromotionCopy(contDescVO.getPromotionCopy());
					tempVO.setCpProperty(contDescVO.getCpProperty());
					tempVO.setCpPropertyUfx(contDescVO.getCpPropertyUfx());
					tempVO.setPresentYn(contDescVO.getPresentYn());
					tempVO.setPresentPrice(contDescVO.getPresentPrice());
					tempVO.setCpPropertyBin(contDescVO.getCpPropertyBin());
					tempVO.setDatafreeBillFlag(contDescVO.getDatafreeBillFlag());			
					tempVO.setMaxViewingLength(contDescVO.getMaxViewingLength());
					
					szTempProdType	= contDescVO.getProductType();
					
					tempVO.setProductType("");
					// strcpy((char*)lst_ContentList.c_is_dolby.arr        , "N");		// 사용안함
		            // strcpy((char*)lst_ContentList.c_is_surround.arr     , "N");		// 사용안함
		            // strcpy((char*)lst_ContentList.c_is_stereo.arr       , "N");		// 사용안함
		            // strcpy((char*)lst_ContentList.c_play.arr            , "Non");	// 사용안함
				 	tempVO.setIs51("N");
				 	tempVO.setIsHot("N");
				 	tempVO.setBroadcastYn("Y");
					
					
		            // 최신회차 정보 [통계로그용] 추출
				 	if(i == nMainCnt -1) {
				 		resultListVO.setContentsId(tempVO.getAlbumId());			// IMCS : 컨텐츠(앨범)ID
				 		resultListVO.setContentsName(tempVO.getAlbumName());		// IMCS : 컨텐츠(앨범)명
				 		resultListVO.setGenreLarge(tempVO.getGenreLarge());			// IMCS : 대장르
				 		resultListVO.setGenreMid(tempVO.getGenreMid());				// IMCS : 중장르
				 		resultListVO.setCreateDate(tempVO.getIsUpdate());			// IMCS : 편성일
				 	}					

		            // 인앱정보 조회
					/*tempVO.setInappPrice("0.00");
					if( "1".equals(tempVO.getCpPropertyBin())) {
						tempVO.setPrice(tempVO.getSuggestedPrice());
						
						try {
							HashMap<String, String> mInappInfo = new HashMap<String, String>();
							mInappInfo	= getNSContListDao.getInappInfo(tempVO.getPrice());
							
							if(mInappInfo != null) {
								tempVO.setInappProdId(mInappInfo.get("INAPP_PROD_ID"));
								tempVO.setInappPrice(mInappInfo.get("INAPP_PRICE"));
							}
							
							paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.HBASE.ordinal()]++;
							
						} catch(Exception e) {
							imcsLog.failLog(ImcsConstants.API_PRO_ID995, "", null, "approval_info1:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
						}
					}*/
					
					if(szTempProdType.equals("NULL")){
						tempVO.setProductType("");
						//tempVO.setContsType("");
					}else{
						tempVO.setProductType(szTempProdType);
						//tempVO.setContsType(szTempProdType);
					}

					if(Integer.parseInt(StringUtil.nullToZero(tempVO.getDownCnt())) > 0)	paramVO.setDownYn("Y");
					else																	paramVO.setDownYn("N");
					
					//Triler 정보 통합/분리했으므로 관련 앨범정보가 있을 때에만 표시한다
					if(tempVO.getContentsId().length() == 0 || "".equals(tempVO.getContentsId())) {
						tempVO.setVodServer1("");
						tempVO.setVodServer2("");
						tempVO.setVodServer3("");
					}
				}
				
				 /* 다국어 자막 관련 로직         */
				if("N".equals(paramVO.getDecPosYn())){
					
					String szSmiLanguage = "";
					
					if( tempVO.getSmiLanguage().indexOf("한국어") >= 0){
						szSmiLanguage = "한국어;";
					}
					
					if( tempVO.getSmiLanguage().indexOf("영어") >= 0){
						szSmiLanguage = szSmiLanguage + "영어;";
					}
					
					if(!"".equals(szSmiLanguage)){
						tempVO.setSmiLanguage(szSmiLanguage);
					}
					
				}
				
				tempVO.setImgUrl(szImgCasheSvrIp + "\b" + szImgSvrIp + "\b" + szImgResizeSvrIp);
				tempVO.setImgUrl1(szImgSvrUrl);
				
				if("DOLBY 5.1".equals(tempVO.getAudioType()))
					tempVO.setIs51("Y");
				else
				
				
				if("Y".equals(tempVO.getIsHd()) || "S".equals(tempVO.getIsHd()))
					tempVO.setIsHd("Y");
				else
					tempVO.setIsHd("N");
				
				
				if("N".equals(tempVO.getPreviewFlag()))		tempVO.setPreviewFlag("V");
				
				
				// OST 정보 조회
				tp2	= System.currentTimeMillis();
				
				if( !"".equals(paramVO.getContsId())){
					List<OstInfoVO> lOstInfo = new ArrayList<OstInfoVO>();
					int OstInfoCnt = 0;
					//String szOstInfo	= "";
					
					lOstInfo = this.getOstInfo(paramVO);
					
					if(paramVO.getResultSet() != -1) {
						if(lOstInfo != null && lOstInfo.size() > 0){
							for(OstInfoCnt = 0 ; OstInfoCnt < lOstInfo.size() ; OstInfoCnt++)
							{
								OstReturnVO.add(lOstInfo.get(OstInfoCnt));
							}
						}						
					} else {
						tpOst	= System.currentTimeMillis();
						imcsLog.timeLog("OST정보 조회 실패", String.valueOf(tpOst - tp2) , methodName, methodLine);
					}
				}
				
				// 평점(왓챠)정보 조회
				tp2	= System.currentTimeMillis();
				
				if( !"".equals(paramVO.getContsId())){
					List<ComWatchaVO> lWatchaInfo = new ArrayList<ComWatchaVO>();
					ComWatchaVO watchaInfo	= new ComWatchaVO();
					
					lWatchaInfo = this.getWatchaInfo(paramVO);
					
					if(paramVO.getResultSet() != -1) {
						if(lWatchaInfo != null && lWatchaInfo.size() > 0)
							nSubCnt	= lWatchaInfo.size();
						
						for(int j = 0; j < lWatchaInfo.size(); j++) {
							watchaInfo	= lWatchaInfo.get(j);
							
							tempVO.setPointWatcha(watchaInfo.getPointWatcha());
							tempVO.setPointCntWatcha(watchaInfo.getTotRatingCount());
							tempVO.setRating01Watcha(watchaInfo.getRating01());
							tempVO.setRating02Watcha(watchaInfo.getRating02());
							tempVO.setRating03Watcha(watchaInfo.getRating03());
							tempVO.setRating04Watcha(watchaInfo.getRating04());
							tempVO.setRating05Watcha(watchaInfo.getRating05());
							tempVO.setRating06Watcha(watchaInfo.getRating06());
							tempVO.setRating07Watcha(watchaInfo.getRating07());
							tempVO.setRating08Watcha(watchaInfo.getRating08());
							tempVO.setRating09Watcha(watchaInfo.getRating09());
							tempVO.setRating10Watcha(watchaInfo.getRating10());
							tempVO.setCommentCnt(watchaInfo.getCommentCnt());
							tempVO.setLinkWatcha(watchaInfo.getWatchaUrl());
						}
					} else {
						tpWatcha	= System.currentTimeMillis();
						imcsLog.timeLog("평점(왓챠)정보 조회 실패", String.valueOf(tpWatcha - tp2) , methodName, methodLine);
					}
				}
				
				
				// 스틸이미지명 조회
				List<StillImageVO> lStillImageVO	= new ArrayList<StillImageVO>();
				String szStillFileName	= "";	// 스틸이미지 파일명
				String szThumnailName	= "";	// 썸네일 파일명	- 사용안함
				
				lStillImageVO	= this.getStillImage(paramVO);

				nSubCnt	= 0;
    			if(lStillImageVO != null)	nSubCnt = lStillImageVO.size();
    			
    			nStillCnt = 0;
    				    			
    			for(int j = 0; j < nSubCnt; j++){
    				
    				if(lStillImageVO.get(j).getImgFlag().equals("N")){
	    				nStillCnt++;
	    				
	 					if(nStillCnt == 1){
							szStillFileName = lStillImageVO.get(j).getImgFileName();
							szThumnailName	= lStillImageVO.get(j).getImgFileName();
							
							tempVO.setThumbnailFileName(szThumnailName);
						}else{
							szStillFileName = szStillFileName +"\b"+ lStillImageVO.get(j).getImgFileName();
						}
    				}
    			}
    			
    			tempVO.setStillFileName(szStillFileName);
    			
				
				
    			// 대장르 카테고리명 조회
    			String szCatName	= "";		// 대장르카테고리명
    			String szGenreName	= "";		// 장르명
    			
    			if(!paramVO.getFxType().equals("X")) {
    				List<HashMap<String, String>> lGenreName	= new ArrayList<HashMap<String, String>>();
    				HashMap<String, String> mGenreName	= new HashMap<String, String>();
    			
    				try {
						lGenreName = this.getGenreName(paramVO);
						nSubCnt = 0;
    				}catch(Exception e){
    					imcsLog.errorLog("" + lGenreName.size());
    				}
        			
        			if(lGenreName != null)		nSubCnt = lGenreName.size();
    				
        			nGenreCnt = 0 ;
        			
    				for(int j = 0; j < nSubCnt; j++){
    					mGenreName	= lGenreName.get(j);
    					szCatName	= mGenreName.get("VOD_CATEGORY_NAME");
    					
    					nGenreCnt++;
    					
    					if(nGenreCnt == 1)			szGenreName = szCatName;			
    					else				szGenreName	= szGenreName + ", " + szCatName;
    				}
				}
    			
    			tempVO.setGenreInfo(szGenreName);
    			
    			// 포인트 유무조회
    			tempVO.setSetPointYn("N");
    			
    			String szPointYN	 = "Y";
//    			szPointYN = this.getPointYN(paramVO);    		
    			
    			if(!"".equals(szPointYN)){
    				tempVO.setSetPointYn(szPointYN);
    			}
    			
    			// 상품타입이 FVOD이면 (무료VOD)
    			String szPriceDesc		= tempVO.getPriceDesc();
    			int nTempEventValue		= 0;
    			int nProdCnt			= 0;
    			int nDupCnt	= 0;
				
    			if( "0".equals(tempVO.getProductType()) ) {
    				tempVO.setPrice("0");
    				tempVO.setPriceDesc("0");
    				tempVO.setBuyYn("1");
    				tempVO.setExpireDate("");
    				tempVO.setBuyDate("");			
    				
    				if(nTempEventValue <= Integer.parseInt(StringUtil.nullToZero(tempVO.getEventValue())))
    						nTempEventValue	= Integer.parseInt(tempVO.getEventValue());
    				
    				nProdCnt ++;
    			} 
    			
    			// 상품타입이 PPV이면
    			else if( "1".equals(tempVO.getProductType()) ) {
    				tempVO.setPrice(tempVO.getSuggestedPrice());
    				tempVO.setPriceDesc(tempVO.getSuggestedPrice());
    				
    				if(nTempEventValue <= Integer.parseInt(StringUtil.nullToZero(tempVO.getEventValue())))
						nTempEventValue	= Integer.parseInt(tempVO.getEventValue());
    			
    				tempVO.setBuyYn("1");
    				tempVO.setExpireDate("");
    				tempVO.setBuyDate("");

    				nProdCnt ++;
    			}    			
    		
    			
    			// 상품타입이 패키지이면
    			String szTempId		= "";
    			tempVO.setContsType(tempVO.getProductType());
				
				nDupCnt			= 0;
				
    			List<ContTypeVO> lcontTypeVo	= new ArrayList<ContTypeVO>();
				ContTypeVO contTypeVo = new ContTypeVO();
				
    			
    			
    			if( "Y".equals(paramVO.getPkgYn()) ) {
    				
    				paramVO.setAdiProdId(tempVO.getAdiProdId());
    				
    				// 패키지 상품 및 가격정보 조회
    				lcontTypeVo = this.getPkgInfo(paramVO);
    				nSubCnt = 0;    				
    				
    				if(paramVO.getResultSet() != -1) 	
    					nSubCnt	= lcontTypeVo.size();
    				
    				for(int j = 0; j < nSubCnt; j++) {
    					contTypeVo = lcontTypeVo.get(j);
    					
    					szTempId	= contTypeVo.getProductId();
    					
    					if(nProdCnt > 0) {
    						tempVO.setContsType(tempVO.getContsType() + "\b" + contTypeVo.getContsType());
    						tempVO.setPriceDesc(tempVO.getPriceDesc() + "\b" + contTypeVo.getPrice());
    					} else {
    						tempVO.setContsType(contTypeVo.getContsType());
    						tempVO.setPriceDesc(contTypeVo.getPrice());
    					}        				
    				}        				

    				if(paramVO.getResultSet() != -1){
    					if(nProdCnt > 0) {
    						tempVO.setBuyYn(tempVO.getBuyYn() + "\b1");
    						tempVO.setExpireDate(tempVO.getExpireDate() + "\b" + "");
    						tempVO.setBuyDate(tempVO.getBuyDate() + "\b" + "");
        				} else {
        					tempVO.setBuyYn("1");
        					tempVO.setExpireDate("");
        					tempVO.setBuyDate("");
        				}
    				}    				
    				
    				nProdCnt++;
    			}
    			
    			// 상품타입이 VOD이면
    			
    			
    			if( "Y".equals(paramVO.getSvodYn()) ) {
    				
    				paramVO.setAdiProdId(tempVO.getAdiProdId());
    				
    				// 컨텐츠 상품 및 가격정보 조회
    				lcontTypeVo = this.getContsInfo(paramVO);
    				nSubCnt = 0;
    				
    				if(paramVO.getResultSet() != -1) 	
    					nSubCnt	= lcontTypeVo.size();
    				
    				for(int j = 0; j < nSubCnt; j++) {
    					contTypeVo = lcontTypeVo.get(j);
    					
    					
    					if(tempVO.getProductType().equals("3")) {		// 소스 내 svod_yn > 0
    						
    						tempVO.setContsType(tempVO.getContsType());
    						tempVO.setBuyYn("0");
    						tempVO.setExpireDate("");
    						tempVO.setBuyDate("");
    					} else {    
    						tempVO.setContsType(tempVO.getContsType() + "\b" + "" + contTypeVo.getContsType().toString());    						
    						tempVO.setBuyYn(tempVO.getBuyYn()	+ "\b1");
    						tempVO.setExpireDate(tempVO.getExpireDate() + "\b" + "");
    						tempVO.setBuyDate(tempVO.getBuyDate() + "\b" + "");
    					}
    				}
    				nProdCnt++;
    			}
    			
    			tempVO.setDownloadYn(paramVO.getDownYn());
    			tempVO.setSetPointYn(tempVO.getSetPointYn());    			
				tempVO.setCatGb("\b\b");
				
    			nProdCnt = 0;
    			
    			
    			// 예약구매의 경우 PreviewFlag 설정
    			if( "R".equals(tempVO.getPreviewFlag()) ) {
    				if( "T".equals(tempVO.getViewingFlag()) )		tempVO.setPreviewFlag("R");		// 검수인 경우
    				if( "V".equals(tempVO.getViewingFlag()) ){										// 노출인 경우
    					if(Integer.parseInt(StringUtil.nullToZero(tempVO.getReservedDate())) > Integer.parseInt(StringUtil.nullToZero(szDate)))
    						tempVO.setPreviewFlag("R");
    					else
    						tempVO.setPreviewFlag("V");
    					
    				}
    			}
    			
    			// 예약컨텐츠의 경우  PreviewFlag 설정
    			if( "P".equals(tempVO.getPreviewFlag()) ) {
    				if( "T".equals(tempVO.getViewingFlag()) )		tempVO.setPreviewFlag("P");		// 검수인 경우
    				if( "V".equals(tempVO.getViewingFlag()) )		tempVO.setPreviewFlag("V");		// 노출인 경우
    			}
    			
    			tp1	= System.currentTimeMillis();
    			/* FVOD의 경우 0원으로 인앱 가격을 조회한다.	*/
    			if("0".equals(tempVO.getProductType())){
    				datafreeVO.setPrice("0");
    			}else{
    				datafreeVO.setPrice(tempVO.getSuggestedPrice());
    			}    		
    			
    			//인앰 가격 조회
    			datafreeVO = this.getDatafreeInfo(datafreeVO, paramVO);
    			
    			/* 예약 구매인 경우 데이터 프리 관련 가격을 주지 않는다.	*/
    			if( "R".equals(tempVO.getPreviewFlag()) && Integer.parseInt(tempVO.getReservedDate()) >= Integer.parseInt(szDate)  ){
    				
    				datafreeVO.setDatafreePrice("0");
    				datafreeVO.setDatafreeApprovalId("");
    				datafreeVO.setDatafreeApprovalPrice("0.00");
    				datafreeVO.setPpvDatafreeApprovalId("");
    				datafreeVO.setPpvDatafreeApprovalPrice("0.00");
    				
    			}else{
    				if("N".equals(tempVO.getDatafreeBillFlag())){
    					
    					/* 데이터 프리 무료	*/
    					if("1".equals(tempVO.getCpPropertyBin())){
    						
    						/* 인앱 가격 제공	*/
    						datafreeVO.setDatafreePrice("0");
    	    				datafreeVO.setDatafreeApprovalId("");
    	    				datafreeVO.setDatafreeApprovalPrice("0.00");
    	    				datafreeVO.setPpvDatafreeApprovalId("");
    	    				datafreeVO.setPpvDatafreeApprovalPrice("0.00");
    						
    	    				if(!"1".equals(tempVO.getProductType())){
    	    					datafreeVO.setApprovalId("");
    	    					datafreeVO.setApprovalPrice("0.00");
    	    				}
    						
    					}else{
    						
    						/* 인앱 가격 미제공	*/
    						datafreeVO.setDatafreePrice("0");
    	    				datafreeVO.setDatafreeApprovalId("");
    	    				datafreeVO.setDatafreeApprovalPrice("0.00");
    	    				datafreeVO.setPpvDatafreeApprovalId("");
    	    				datafreeVO.setPpvDatafreeApprovalPrice("0.00");
    	    				datafreeVO.setApprovalId("");
	    					datafreeVO.setApprovalPrice("0.00");
    						
    					}
    					
    					
    				}else{
    					
    					/* 데이터 프리가 유료인 경우	*/
    					if("1".equals(tempVO.getCpPropertyBin())){
    						
    						/* 인앱 가격 제공	*/	    							    						
    						if("1".equals(tempVO.getProductType())){
    							/* PPV 컨텐츠	*/
    							datafreeVO.setDatafreeApprovalId("");
	    	    				datafreeVO.setDatafreeApprovalPrice("0.00");
	    	    				
    						}   						
    						else if("0".equals(tempVO.getProductType())){
    							/* FVOD 컨텐츠	*/
    							datafreeVO.setApprovalId("");
    	    					datafreeVO.setApprovalPrice("0.00");
    	    					datafreeVO.setPpvDatafreeApprovalId("");
	    	    				datafreeVO.setPpvDatafreeApprovalPrice("0.00");
	    	    				
    						}else{		    						
	    						/* FVOD나 PPV가 아닌 경우		*/
	    						datafreeVO.setDatafreePrice("0");
	    	    				datafreeVO.setDatafreeApprovalId("");
	    	    				datafreeVO.setDatafreeApprovalPrice("0.00");
	    	    				datafreeVO.setPpvDatafreeApprovalId("");
	    	    				datafreeVO.setPpvDatafreeApprovalPrice("0.00");
	    	    				datafreeVO.setApprovalId("");
    	    					datafreeVO.setApprovalPrice("0.00");
    	    					
	    					}
    						
    					}else{
    						/* 인앱 가격 미제공	*/	  
    						/* PPS, SVOD ONLY 인경우 datafree_price(원 가격)를 0으로 제공	*/
    						if( "3".equals(tempVO.getProductType()) || "2".equals(tempVO.getProductType()) ){
    							datafreeVO.setDatafreePrice("0");
    						}
    						
    						datafreeVO.setDatafreeApprovalId("");
    	    				datafreeVO.setDatafreeApprovalPrice("0.00");
    	    				datafreeVO.setPpvDatafreeApprovalId("");
    	    				datafreeVO.setPpvDatafreeApprovalPrice("0.00");
    	    				datafreeVO.setApprovalId("");
	    					datafreeVO.setApprovalPrice("0.00");
    						
    					}
    					
    				}
    				
    			}
    			
    			int maxViewingLength = 0;
		    	
		    	try {
		    		maxViewingLength = Integer.parseInt(tempVO.getMaxViewingLength());
				} catch (Exception e) {
					maxViewingLength = 0;
				}
    			
    			/* 평생 소장 컨텐츠 인앱 가격 미제공, 데이터 프리 구매 불가	*/
    			if(maxViewingLength/24 > 2000){
    				
    				tempVO.setDatafreeBillFlag("X");
    				datafreeVO.setApprovalId("");
    				datafreeVO.setApprovalPrice("0.00");
    				datafreeVO.setDatafreePrice("");
    				datafreeVO.setDatafreeApprovalId("");
    				datafreeVO.setDatafreeApprovalPrice("0.00");
    				datafreeVO.setPpvDatafreeApprovalId("");
    				datafreeVO.setPpvDatafreeApprovalPrice("0.00");
    				
    			}
    			
    			tp2	= System.currentTimeMillis();
		    	//imcsLog.timeLog("인앱 가격 정보 조회(IMCSUSER.PT_LA_APPROVAL_INFO)", String.valueOf(tp2 - tp1), methodName, methodLine);
		    	
		    	 /* 구매기간 상관없이 이어보기 시간을 가져온다  ----> 안써서 제외 */
		    	
    			//2020.04.17 - 구API이므로 시즌 정보는 무조건 N으로 제공한다.
    			tempVO.setSeasonYn("N");
    			
    			tempVO.setImgFileName(tempVO.getImgFileName() + "\b" + tempVO.getImgFileName() + "\b" + tempVO.getImgFileName());
    			tempVO.setPrice(tempVO.getPriceDesc());
    			    			
    			tempVO.setInappProdId(datafreeVO.getApprovalId());
    			tempVO.setInappPrice(datafreeVO.getApprovalPrice());
    			tempVO.setDatafreePrice(datafreeVO.getDatafreePrice());
    			tempVO.setDatafreeInappPrice(datafreeVO.getDatafreeApprovalPrice());
    			tempVO.setDatafreeInappProdId(datafreeVO.getDatafreeApprovalId());
    			tempVO.setPpvDatafreeInappPrice(datafreeVO.getPpvDatafreeApprovalPrice());
    			tempVO.setPpvDatafreeInappProdId(datafreeVO.getPpvDatafreeApprovalId());

    			returnVO.add(tempVO);
			}
			
			tp1	= System.currentTimeMillis();
			imcsLog.timeLog("컨텐츠정보 FETCH", String.valueOf(tp1 - tp2), methodName, methodLine);
			
			resultListVO.setList(returnVO);
			resultListVO.setOstList(OstReturnVO);
		} catch(ImcsException ie) {
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			imcsLog.errorLog(methodName + "-E",ie.getClass().getName()+":"+ie.getMessage());

			// TLO Log 를 Controller Layer 로 전달하기 위해 Setting 한다. (TLO Log 세부값은 finally 문에서 설정된다.)
			throw ie;
		} catch(Exception e) {
			isLastProcess	= ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			
			// TLO Log 를 Controller Layer 로 전달하기 위해 ImcsException 으로 변환한다. (TLO Log 세부값은 finally 문에서 설정된다.)
			throw new ImcsException();
		} finally{
			resultListVO.setResultCode(paramVO.getResultCode());
			
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID995) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"; 
						
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultListVO;
	}
	
	
	
	/**
	 * 컨텐츠 상품 및 가격정보 조회
	 * @param paramVO
	 * @return
	 */
	public List<ContTypeVO> getContsInfo(GetNSContListRequestVO paramVO) throws Exception{
//		long tp1 = System.currentTimeMillis();
		
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId	=  "lgvod995_012_20171214_001";
		
		int nResultSet	= 0;
		paramVO.setResultSet(nResultSet);
		
		List<ContTypeVO> list	= new ArrayList<ContTypeVO>();
		
		try {
			try{
				list  = getNSContListDao.getContsInfo(paramVO);
				
			}catch(DataAccessException e){
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			if(list == null || list.isEmpty()){
				nResultSet	= -1;
			}
			
		} catch (Exception e) {
			//if(cache.getLastException() != null) 	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
			//else									paramVO.setResultCode("41000000");
			nResultSet	= -1;
			
//			imcsLog.failLog(ImcsConstants.API_PRO_ID995, sqlId, null, "conts_type:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
		paramVO.setResultSet(nResultSet);
//		long tp2 = System.currentTimeMillis();
//		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
//		imcsLog.timeLog("DAO쿼리수행####################:", String.valueOf(tp2 - tp1), methodName, methodLine);
		return list;
	}


	/**
	 * 패키지 상품 및 가격정보 조회
	 * @param paramVO
	 * @return
	 */
	public List<ContTypeVO> getPkgInfo(GetNSContListRequestVO paramVO) throws Exception{
//		long tp1 = System.currentTimeMillis();
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId	=  "lgvod995_013_20171214_001";
		
		int nResultSet	= 0;
		paramVO.setResultSet(nResultSet);
		
		List<ContTypeVO> list	= new ArrayList<ContTypeVO>();
		
		try {
			try{
				list  = getNSContListDao.getPkgInfo(paramVO);
			}catch(DataAccessException e){
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if(list == null || list.isEmpty()){
				nResultSet	= -1;
			}
		} catch (Exception e) {
			//if(cache.getLastException() != null) 	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
			//else									paramVO.setResultCode("41000000");
			nResultSet	= -1;
			
//			imcsLog.failLog(ImcsConstants.API_PRO_ID995, sqlId, null, "conts_type:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
		paramVO.setResultSet(nResultSet);
//		long tp2 = System.currentTimeMillis();
//		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
//		imcsLog.timeLog("DAO쿼리수행####################:", String.valueOf(tp2 - tp1), methodName, methodLine);

		return list;
	}



	/**
     * 포인트 유무 조회
     *  @param
     *  @result	INT
     */
//    public String getPointYN(GetNSContListRequestVO paramVO) throws Exception{
//		long tp1 = System.currentTimeMillis();
//    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
//		String methodName = oStackTrace.getMethodName();
//		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
//		
//    	String sqlId =  "lgvod995_008_20171214_001";
//		
//		List<String> list   = null;
//		String szPointYn	= "";
//		
//		try {
//			try{
//				list = getNSContListDao.getPointYN(paramVO);
//			}catch(DataAccessException e){
//				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
//				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
//			}
//			
//			if( list != null && !list.isEmpty()){
//				szPointYn = (String) StringUtil.replaceNull(list.get(0), "N");
//			}
//			
//		} catch (Exception e) {
//			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
//		}
//		long tp2 = System.currentTimeMillis();
//		imcsLog.timeLog("DAO쿼리수행####################:", String.valueOf(tp2 - tp1), methodName, "");
//		
//    	return szPointYn;
//    }
    



	/**
	 * 대장르 카테고리명 조회 
	 * @param paramVO
	 * @return List<HashMap<String, String>>
	 */
	@SuppressWarnings("rawtypes")
	public List<HashMap<String, String>> getGenreName(GetNSContListRequestVO paramVO) throws Exception{
//		long tp1 = System.currentTimeMillis();
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
    	
    	String sqlId =  "lgvod995_009_20171214_001";

		List<HashMap<String, String>> list   = new ArrayList<HashMap<String, String>>();
		
		try {
			try{
				list = getNSContListDao.getGenreName(paramVO);
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
		} catch (Exception e) {
			//if(cache.getLastException() != null) 	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
			//else									paramVO.setResultCode("41000000");
			
//			imcsLog.failLog(ImcsConstants.API_PRO_ID995, sqlId, null, "genre_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
//		long tp2 = System.currentTimeMillis();
//		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
//		imcsLog.timeLog("DAO쿼리수행####################:", String.valueOf(tp2 - tp1), methodName, methodLine);
		
    	return list;
    }
	
    
    
	/**
	 * 스틸 이미지명 조회
	 * @param paramVO
	 * @return
	 */
	public List<StillImageVO> getStillImage(GetNSContListRequestVO paramVO) throws Exception {
//		long tp1 = System.currentTimeMillis();
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId	=  "lgvod995_007_20171214_001";
					
		List<StillImageVO> list	= new ArrayList<StillImageVO>();
		
		try {
			try{
				list  = getNSContListDao.getStillImage(paramVO);
			}catch(DataAccessException e){
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
		} catch (Exception e) {
			//if(cache.getLastException() != null) 	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
			//else									paramVO.setResultCode("41000000");
			
//			imcsLog.failLog(ImcsConstants.API_PRO_ID995, sqlId, null, "still_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
//		long tp2 = System.currentTimeMillis();
//		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
//		imcsLog.timeLog("DAO쿼리수행####################:", String.valueOf(tp2 - tp1), methodName, methodLine);

		return list;
	}


	/**
	 * 평점(왓챠)정보 조회
	 * @param paramVO
	 * @return
	 */
	public List<ComWatchaVO> getWatchaInfo(GetNSContListRequestVO paramVO) throws Exception {
//		long tp1 = System.currentTimeMillis();
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId	=  "lgvod995_s11_20171214_001";
		int nResultSet	= 0;
		
		paramVO.setResultSet(nResultSet);
						
		List<ComWatchaVO> list	= new ArrayList<ComWatchaVO>();
		
		try {
			try{
				list  = getNSContListDao.getWatchaInfo(paramVO);
			}catch(DataAccessException e){
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
		} catch (Exception e) {
//			imcsLog.failLog(ImcsConstants.API_PRO_ID995, sqlId, null, "watcha_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			nResultSet	= -1;
		}
		
		paramVO.setResultSet(nResultSet);
//		long tp2 = System.currentTimeMillis();
//		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
//		imcsLog.timeLog("DAO쿼리수행####################:", String.valueOf(tp2 - tp1), methodName, methodLine);
		return list;
	}



	/**
	 * OST 정보조회
	 * @param paramVO
	 * @return
	 */
	public List<OstInfoVO> getOstInfo(GetNSContListRequestVO paramVO) throws Exception {
//		long tp1 = System.currentTimeMillis();
//	   	IMCSLog imcsLog = new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId	=  "lgvod995_i10_20171214_001";
		int nResultSet	= 0;
		
		paramVO.setResultSet(nResultSet);
						
		List<OstInfoVO> list	= new ArrayList<OstInfoVO>();
		
		try {
			try{
				list  = getNSContListDao.getOstInfo(paramVO);
			}catch(DataAccessException e){
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}	
			
		} catch (Exception e) {
//			imcsLog.failLog(ImcsConstants.API_PRO_ID995, sqlId, null, "ost_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			nResultSet	= -1;
		}
		
		paramVO.setResultSet(nResultSet);
//		long tp2 = System.currentTimeMillis();
//		imcsLog.timeLog("DAO쿼리수행####################:", String.valueOf(tp2 - tp1), methodName, methodLine);
		return list;
	}


	/**
	 * 컨텐츠 상세정보 조회(앨범정보 조회)
	 * @param paramVO
	 * @return
	 */
	public GetNSContListResponseVO getContDesc(GetNSContListRequestVO paramVO) throws Exception {
//		long tp1 = System.currentTimeMillis();
//    	IMCSLog imcsLog = new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId	=  "lgvod995_004_20171214_001";
			
		List<GetNSContListResponseVO> list	= new ArrayList<GetNSContListResponseVO>();
		GetNSContListResponseVO contDescVO	= new GetNSContListResponseVO();
		
		try {
			try{
				list  = getNSContListDao.getContDesc(paramVO);
			}catch(DataAccessException e){
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if (list != null && !list.isEmpty()) {
				contDescVO	= list.get(0);
			}
					
		} catch (Exception e) {
			//if(cache.getLastException() != null) 	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
			//else									paramVO.setResultCode("41000000");
			
//			imcsLog.failLog(ImcsConstants.API_PRO_ID995, sqlId, null, "cont_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
//		long tp2 = System.currentTimeMillis();
//		imcsLog.timeLog("DAO쿼리수행####################:", String.valueOf(tp2 - tp1), methodName, methodLine);
		
		return contDescVO;
	}



	/**
	 * 컨텐츠 리스트 조회 
	 * @param paramVO
	 * @return
	 */
	public List<GetNSContListResponseVO> getContList(GetNSContListRequestVO paramVO) throws Exception {
//		long tp1 = System.currentTimeMillis();
//    	IMCSLog imcsLog = new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId	=  "";
		String szMsg	= "";
		
		int querySize	= 0;
		
		if( "Y".equals(paramVO.getCloseYn())) {
			if("Y".equals(paramVO.getSelectAll()))	sqlId	= "lgvod995_m01_20171214_001";
			else									sqlId	= "lgvod995_m02_20171214_001";
		} else {
			if("Y".equals(paramVO.getSelectAll()))	sqlId	= "lgvod995_m03_20171214_001";
			else									sqlId	= "lgvod995_m04_20171214_001";
		}
				
		List<GetNSContListResponseVO> list	= new ArrayList<GetNSContListResponseVO>();
		
		try {
			
			try{
				list  = getNSContListDao.getContList(paramVO);
			}catch(DataAccessException e){
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if (list != null && !list.isEmpty()) {
				querySize	= list.size();
			}
			
			try{
//				imcsLog.dbLog(ImcsConstants.API_PRO_ID995, sqlId, null, querySize, methodName, methodLine);
			}catch(Exception e){}
					
//			paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
			
		} catch (Exception e) {
//			if(cache.getLastException() != null){
//				paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
//				
//				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID995) + "] SQLID[" + sqlId + "] sts[" + cache.getLastException().getErrorCode() + "] "
//						+ String.format("%-21s", "msg[conts_info:" + cache.getLastException().getErrorMessage() + "]");
//				imcsLog.serviceLog(szMsg, methodName, methodLine);
//			} else {
//				paramVO.setResultCode("41000000");
//				
//				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID995) + "] SQLID[" + sqlId + "] sts[    0] "
//						+ String.format("%-21s", "msg[conts_info:" + ImcsConstants.RCV_MSG6 + "]");
//				imcsLog.serviceLog(szMsg, methodName, methodLine);
//			}
			
		}
//		long tp2 = System.currentTimeMillis();
//		imcsLog.timeLog("DAO쿼리수행####################:", String.valueOf(tp2 - tp1), methodName, methodLine);
		
		return list;
	}


	/**
	 * 트레일러 URL리스트 조회
	 * @param paramVO
	 * @return
	 */
	public ComTrailerVO getTrilerList(GetNSContListRequestVO paramVO) throws Exception {
//		long tp1 = System.currentTimeMillis();
//    	IMCSLog imcsLog = new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId = "lgvod995_003_20171214_001";
		int querySize = 0;
		
		paramVO.setRangeIpCd("1234567890");
		
		List<ComTrailerVO> list	= new ArrayList<ComTrailerVO>();
		ComTrailerVO urlListVO		= new ComTrailerVO();
		
		try {
			
			try{
				list  = getNSContListDao.getTrilerList(paramVO);
			}catch(DataAccessException e){
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list != null && !list.isEmpty()){
				urlListVO	= list.get(0);
				querySize = list.size();
			}
		
			try{
//				imcsLog.dbLog(ImcsConstants.API_PRO_ID995, sqlId, null, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
//			if(cache.getLastException() != null) 	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
//			else									paramVO.setResultCode("41000000");
			
//			imcsLog.failLog(ImcsConstants.API_PRO_ID995, sqlId, null, "trailer_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
//		long tp2 = System.currentTimeMillis();
//		imcsLog.timeLog("DAO쿼리수행####################:", String.valueOf(tp2 - tp1), methodName, methodLine);
		
    	return urlListVO;
	}

	
	/**
	 * SVOD PKG 정보 조회
     * @param	GetNSMultiContsRequestVO
     * @result	List<SvodPkgVO>
     */
	public List<SvodPkgVO> getSvodPkg(GetNSContListRequestVO paramVO) throws Exception{
//		long tp1 = System.currentTimeMillis();
//		IMCSLog imcsLog = new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId = "lgvod995_002_20171214_001";
		
		int querySize = 0;
		
		List<SvodPkgVO> list	= new ArrayList<SvodPkgVO>();
		
		try {
			
			try{
				list  = getNSContListDao.getSvodPkg(paramVO);
			}catch(DataAccessException e){
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			try {
				if( list != null && !list.isEmpty())	querySize	= list.size();
//				imcsLog.dbLog(ImcsConstants.API_PRO_ID995, sqlId, null, querySize, methodName, methodLine);
			} catch (Exception e) {}
			
		} catch (Exception e) {
//			if(cache.getLastException() != null) 	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
//			else									paramVO.setResultCode("41000000");
			
//			imcsLog.failLog(ImcsConstants.API_PRO_ID995, sqlId, null, "svod_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
//		long tp2 = System.currentTimeMillis();
//		imcsLog.timeLog("DAO쿼리수행####################:", String.valueOf(tp2 - tp1), methodName, methodLine);
		
    	return list;
	}

	/**
	 * 검수 STB 여부 조회
     * @param
     * @result 
     */
    public String testSbc(GetNSContListRequestVO paramVO) throws Exception{
//		long tp1 = System.currentTimeMillis();
    	IMCSLog imcsLog = new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		
		String sqlId =  "lgvod995_001_20171214_001";
		String szTestSbc	= "N";
		
		List<String> list   = null;
		
		try {
			
			try{
				list  = getNSContListDao.testSbc(paramVO);
			}catch(DataAccessException e){
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list != null && !list.isEmpty()){
				szTestSbc = StringUtil.replaceNull((String)list.get(0), "N");
				
				if(szTestSbc.equals("Y"))	paramVO.setViewFlag2("T");
				else						paramVO.setViewFlag2("V");
			}
		
		} catch (Exception e) {
			
			szTestSbc = "N";
			paramVO.setViewFlag2("V");
			
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}
//		long tp2 = System.currentTimeMillis();
//		imcsLog.timeLog("DAO쿼리수행####################:", String.valueOf(tp2 - tp1), methodName, "");
		
    	return szTestSbc;
    }
	
    /**
     * 데이터프리 정보 조회
     * @param	ComDataFreeVO, GetNSContDtlRequestVO
     * @result	ComDataFreeVO
    **/
    public ComDataFreeVO getDatafreeInfo(ComDataFreeVO tempVO, GetNSContListRequestVO paramVO) throws Exception{
//		long tp1 = System.currentTimeMillis();
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
    	
    	String sqlId = "lgvod995_015_20171214_001";
		
		List<ComDataFreeVO> list   = new ArrayList<ComDataFreeVO>();
		ComDataFreeVO resultVO = new ComDataFreeVO();
		
		try {
			
			try{
				list  = getNSContListDao.getDatafreeInfo(tempVO);				
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list != null && !list.isEmpty()){
				resultVO = list.get(0);
			}else{
				resultVO.setDatafreePrice("0");
				resultVO.setApprovalPrice("0");
				resultVO.setDatafreeApprovalPrice("0.00");
				resultVO.setPpvDatafreeApprovalPrice("0.00");
			}
			
		} catch (Exception e) {
			 imcsLog.failLog(ImcsConstants.API_PRO_ID995, sqlId, null, "approval_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			 
			 paramVO.setResultCode("41000000");
		}
//		long tp2 = System.currentTimeMillis();
//		imcsLog.timeLog("DAO쿼리수행####################:", String.valueOf(tp2 - tp1), methodName, methodLine);
    	return resultVO;
    }
	
}
