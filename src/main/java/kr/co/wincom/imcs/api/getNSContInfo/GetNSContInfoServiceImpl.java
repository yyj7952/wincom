package kr.co.wincom.imcs.api.getNSContInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.ComConcertInfoVO;
import kr.co.wincom.imcs.handler.ImcsException;
import kr.co.wincom.imcs.common.NosqlCacheType;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetNSContInfoServiceImpl implements GetNSContInfoService {
	private Log imcsLogger		= LogFactory.getLog("API_getNSContInfo");
	
	@Autowired
	private GetNSContInfoDao getNSContInfoDao;
	
	@Autowired
	private CommonService commonService;

//	public void getNSContInfo(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
	@Override
	public GetNSContInfoResultVO getNSContInfo(GetNSContInfoRequestVO paramVO){
//		this.getNSContInfo(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		List<GetNSContInfoResponseVO> resultVO	= new ArrayList<GetNSContInfoResponseVO>();
		GetNSContInfoResultVO resultListVO	= new GetNSContInfoResultVO();

		String msg	= "";
		
		int nMainCnt = 0;
        // header계산
        int iCntDtl  = 0;
        int iCntPre  = 0;
        int iCntOn   = 0;
        int iCntPost = 0;
        int iResult  = 0;	// 성공여부 코드값. 0:성공, 1:오류

	    long tp_start = paramVO.getTp_start();
		long tp1, tp2 = 0;

		try{
			tp1	= System.currentTimeMillis();
			imcsLog.timeLog("init...", String.valueOf(tp1 - tp_start), methodName, methodLine); 
			
			//######################################################
			// 로직구현 (시작)
			//######################################################
			//카테고리 구분 설정
			paramVO.setCatGb("NSC");
			//sysdate 가져오기 (예약구매 확인용...)
			String cur_date = commonService.getSysdateYMD();
			String cur_date2 = commonService.getSysdate().substring(0, 12);
			//이미지 캐쉬 서버 정보 가져오기
			String poster_url   = commonService.getImgReplaceUrl2("img_server", "getNSContInfo");
			String still_url    = commonService.getImgReplaceUrl2("img_still_server", "getNSContInfo");
			String conts_url    = commonService.getImgReplaceUrl2("img_still_server", "getNSContInfo");
			String cuesheet_url = commonService.getImgReplaceUrl2("img_cuesheet_server", "getNSContInfo");
			String chnl_url     = commonService.getImgReplaceUrl2("img_chnl_server", "getNSContInfo");
			
			//부가세 요율 정보 가져오기
			paramVO.setVatRate(getNSContInfoDao.getVatRate());

			//가입자 정보 가져오기 => 검수 여부 (nsvod220_001_20180701)
			tp1 = System.currentTimeMillis();
			this.getTestSbc(paramVO);
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("검수 STB여부 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			//공연앨범인 경우 타입체크
			int albumTypeInfo_cnt = this.getAlbumTypeInfo(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			imcsLog.timeLog("공연앨범인 경우 타입체크", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			if (albumTypeInfo_cnt < 0) {
	            //iResult, 성공여부 코드값 (0:성공, 1:실패)
				String resultHeader  = String.format("%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|", 
						"1", "옴니뷰 앨범의 매핑되는 본편 앨범이 없습니다", "", "", "", "", "", "", "", "", "", "");
				resultListVO.setResultHeader(resultHeader);
				
				resultListVO.setList(resultVO);
				return resultListVO;
			}
			
			//카테고리 유효성 체크 및 편성 여부 체크
			tp1 = System.currentTimeMillis();
			// 2021.02.24 - 모바일TV 기능개선 4차수 : 미편성 콘텐츠의 경우 카테고리 정보를 조회하지 않는다. 미편성 콘텐츠도 상세정보 제공할 수 있도록 한다.
			if(paramVO.getCatId().equals("ZZZZZ") && paramVO.getRqsType().equals("V"))
			{
				paramVO.setViewFlag("Z");
				paramVO.setCatChkCnt(1);
			}
			else
			{
				this.getChkCate(paramVO);
			}
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("카테고리 유효성 체크 및 편성 여부 체크", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			//System.out.println("######################################:"+paramVO.getCatChkCnt());
			
			//카테고리 편성된 내용이 없으면 실제 컨텐츠 관련 로직 수행할 필요 없다.
			if (paramVO.getCatChkCnt() == 0) {
	            //iResult, 성공여부 코드값 (0:성공, 1:실패)
				String resultHeader  = String.format("%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|", 
						"1", "카테고리 편성정보가 없습니다", "", "", "", "", "", "", "", "", "", "");
				resultListVO.setResultHeader(resultHeader);
				
				resultListVO.setList(resultVO);
				return resultListVO;
			}
			
			//컨텐츠 정보 가져오기
			tp1	= System.currentTimeMillis();
			List<AlbumInfo> albimInfoList = this.getContInfoList(paramVO);
			
			//유효성 검증
			if (albimInfoList.size()==0) {
				String resultHeader  = String.format("%s|%s|%s|%s|", 
						"1", "존재하지 않는 앨범입니다", paramVO.getVatRate(), paramVO.getCatId());
				resultListVO.setResultHeader(resultHeader);
				resultListVO.setList(resultVO);
				
				paramVO.setResultCode("4000");
//				imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID220, "", null, "album_list:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
				
				return resultListVO;
			}
			AlbumInfo albumInfo = albimInfoList.get(0);
			
			if(albumInfo.getVodType().equals("D"))	albumInfo.setVodType("Y");
			else									albumInfo.setVodType("N");
			
			//예약구매 및 서비스 플래그 설정
		    if ( albumInfo.getServiceFlag().equals("N") ) albumInfo.setServiceFlag("V");

		    //-------------------------------------------------------------------------//
		    // 공연앱인 경우와 비디오포털인 경우 분리하여 항목 설정
		    //-------------------------------------------------------------------------//
		    // 큐시트 정보 가져오기
		    // ------------------------------------------------------------------------//
		    HashMap cueInfo = new HashMap();
		    if ( paramVO.getRqsType().equals("M") )
			{
				cueInfo = this.getCuesheetInfo(albumInfo, paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
				if ( cueInfo.get("VIEW_FLAG").equals("Y") && cueInfo.get("CONFIRM_YN").equals("Y") )
					cueInfo.put("CUESHEET_YN", "Y");
				else
					cueInfo.put("CUESHEET_YN", "N");
				
				//큐시트가 방송중이거나, 방송전일 경우 재생가능한 물리 파일이 없으므로...
				if ( cueInfo.get("STATUS").equals("0") || cueInfo.get("STATUS").equals("1") )
				{
					albumInfo.setServiceFlag("P");
					
					//방송전 또는 방송중일 경우 옴니뷰 서비스 가능 -- 앨범의 옴니뷰여부 체크할 필요 없음
					//단 VOD ONLY 서비스가 아닐 경우...
					if ( cueInfo.get("VOD_ONLY_YN").equals("N")) albumInfo.setOmniviewYn("Y");
					
				} else {
		            //방송후인 경우 카테고리 노출 설정으로 체크함...
		            //카테고리 노출설정 = 검수인 경우 검수사용자만 시청 가능
					if (!paramVO.getViewFlag().equals("V")) {
						albumInfo.setServiceFlag("P");
						
						if (paramVO.getCustFlag().equals("T") && paramVO.getViewFlag().equals("T")) {
							albumInfo.setServiceFlag("V");
						}
					}
				}
				
				albumInfo.setServiceDate("");
				albumInfo.setDownYn("N");
				albumInfo.setSeasonYn("N");
				albumInfo.setPromotionCopy("");
				
			    //큐시트 이미지 체크
				//System.out.println("#################################:"+ cueInfo.get("IMAGE_FOLDER"));
			    cueInfo.put("IMAGE_URL", cuesheet_url + cueInfo.get("IMAGE_FOLDER").toString());
			    if (cueInfo.get("LOGO_IMG_FILE") != null && !cueInfo.get("LOGO_IMG_FILE").toString().equals("")) {
			    	chnl_url = cueInfo.get("IMAGE_URL").toString();
			    	cueInfo.put("CHNL_IMG_FILE", cueInfo.get("LOGO_IMG_FILE").toString());
			    }
			    
			    String resultImage  = String.format("IMG|%s|%s|%s|%s|%s|%s|\f", 
						chnl_url, 
						cueInfo.get("CHNL_IMG_FILE")==null?"":cueInfo.get("CHNL_IMG_FILE"), 
						cueInfo.get("IMAGE_URL")==null?"":cueInfo.get("IMAGE_URL"),
						cueInfo.get("MAIN_IMAGE_URL")==null?"":cueInfo.get("MAIN_IMAGE_URL"), 
						cueInfo.get("IMAGE_URL")==null?"":cueInfo.get("IMAGE_URL"), 
						cueInfo.get("OMNI_IMG_FILE")==null?"":cueInfo.get("OMNI_IMG_FILE")
						);
			    
			    resultListVO.setResultImg(resultImage);
			    
			    if(cueInfo.get("PAY_FLAG").equals("1")) {//아이돌 라이브 유료콘서트
			    	ComConcertInfoVO cstInfo = new ComConcertInfoVO();
			    	String cstBgnTime = "";
			    	String cstEndTime = "0";
			    	cstInfo = this.getConsertInfo(albumInfo, paramVO);
			    	
			    	
					if (cstInfo != null) {
						
						if(cstInfo.getPerformEndDate().length() > 0 && cstInfo.getPerformEndTime().length() > 0 ) {
							cstEndTime = cstInfo.getPerformEndDate() + cstInfo.getPerformEndTime();
						}
						cstBgnTime = cstInfo.getPerformDate() + cstInfo.getPerformTime();
						
						if (Double.parseDouble(cur_date2) < Double.parseDouble(cstEndTime) || cstEndTime.equals("0")) {
							
							if(cstEndTime.equals("0")) {
								cstEndTime = "";
							}
							
							albumInfo.setPayFlag("Y");
							
							String resultConsert = String.format("LIVE|%s|%s|%s|%s|\f",
									cstInfo.getPerformDate(),
									cstBgnTime,
									cstEndTime,
									cstInfo.getGuideText());

							resultListVO.setResultCst(resultConsert);
						} else {
							albumInfo.setPayFlag("N");
						}
			    	 }
			    		 
			    	
			    	
			    }
			}
			else
			{
				cueInfo.put("CUESHEET_YN", "");
				cueInfo.put("STATUS", "");

				cueInfo.put("IMAGE_URL", "");
				cueInfo.put("IMAGE_FOLDER", "");
				cueInfo.put("LOGO_IMG_FILE", "");
				cueInfo.put("CHNL_IMG_FILE", "");
				cueInfo.put("LOGO_IMG_FILE", "");

				// 2021.02.24 - 모바일TV 기능개선 4차수 : 미편성 콘텐츠의 경우 연동규격서상 28번쨰 preview_flag값을 Z로 제공한다.
				if(paramVO.getViewFlag().equals("Z"))
				{
					albumInfo.setServiceFlag("Z");
				}
				else
				{
					// 예약구매인 경우 -- 예약구매시청가능일이 현재일보다 작거나 같을 경우 시청 가능으로 전환한다
					if ( albumInfo.getServiceFlag().equals("R") )
					{
						if ( paramVO.getViewFlag().equals("V") )
						{
							if ( albumInfo.getServiceDate() != "" && (Integer.parseInt(albumInfo.getServiceDate()) <= Integer.parseInt(cur_date))) 
								albumInfo.setServiceFlag("V");
						}
					}
					else
					{
				        albumInfo.setServiceDate("");
					}
	
					// 포스터만 노출 또는 정보만 노출인 경우
					if ( albumInfo.getServiceFlag().equals("P") )
					{
						if ( paramVO.getViewFlag().equals("V") ) albumInfo.setServiceFlag("V");
					}
				}
			}
		    
		    //-------------------------------------------------------------------------//
		    // 노출 설정 체크 (공연앱과 비디오포털 처리가 다르다)
		    //-------------------------------------------------------------------------//
		    tp1 = System.currentTimeMillis();
		    String checkFlag = "Y";
		    
		    // 2021.02.24 - 모바일TV 기능개선 4차수 : 미편성 콘텐츠의 경우 상세 정보를 제공할 수 있도록 한다.
			if(paramVO.getViewFlag().equals("Z"))
			{
				albumInfo.setServiceFlag("Z");
			}
			else
			{
				//노출설정이 "V" 가 아닐 경우 처리...
				if ( !paramVO.getViewFlag().equals("V") && !paramVO.getViewFlag().equals(paramVO.getCustFlag()))
				{
					//공연앱일 때...
					if ( paramVO.getRqsType().equals("M"))
					{
						//큐시트 방송구분이 방송전이나 방송중일 경우에는 카테고리 노출 설정 상관없음 (방송후일 때만 유효함)
						//if ( cueInfo.get("STATUS").equals("2")) checkFlag = "N";
						
						//시청가능 플래그 값이 "P"가 아닌 경우에만 오류...
						if (!albumInfo.getServiceFlag().equals("P")) {
							checkFlag = "N";
						}
					}
					else
					{
						if ( !albumInfo.getServiceFlag().equals("R") &&
							 !albumInfo.getServiceFlag().equals("P") &&
							 !albumInfo.getServiceFlag().equals("X") )
						{
							checkFlag = "N";
						}
					}
				}
			}
			
			if ( !checkFlag.equals("Y") )
			{
				//String resultHeader  = String.format("%s|%s|%s|%s|", 
				//		"1", "카테고리의 노출설정이 비노출입니다", paramVO.getVatRate(), paramVO.getCatId());
				String resultHeader  = String.format("%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|", 
						"1", "카테고리의 노출설정이 비노출입니다", "", "", "", "", "", "", "", "", "", "");
				
				resultListVO.setResultHeader(resultHeader);
				
				resultListVO.setList(resultVO);
				return resultListVO;
			}
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("앨범 유효성 체크 완료", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			//공연앱이 아닐 경우 앨범 관련 정보 가져온다
			String ostInfo = "";
			tp1	= System.currentTimeMillis();
			if ( paramVO.getRqsType().equals("M") )
			{
			    //ONAIR DATE 설정
				if( cueInfo.get("PERFORM_DATE").toString().length() >= 8 )
					albumInfo.setOnairDate(cueInfo.get("PERFORM_DATE").toString() + cueInfo.get("PERFORM_TIME").toString());
				else
					albumInfo.setOnairDate(albumInfo.getOnairDateTemp());
			}
			else
			{
		        //ONAIR DATE 설정
				albumInfo.setOnairDate(albumInfo.getOnairDateTemp());
				
				String genreInfo = this.getGenreInfo(albumInfo, paramVO);	//장르정보
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
				albumInfo.setGenreInfo(genreInfo);
				String downYn = this.getDownCheck(albumInfo, paramVO);	//다운로드 체크
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
				albumInfo.setDownYn(downYn);;
		    	ostInfo = this.getOSTInfo(albumInfo, paramVO);	//OST 정보
		    	paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
		    	
		    	albumInfo.setSeasonYn("N");
		    	
				tp2	= System.currentTimeMillis();
				imcsLog.timeLog("추가 정보 설정 완료 (장르, 다운로드, OST)", String.valueOf(tp2 - tp1), methodName, methodLine);
		    }
			
			//RESULT_TYPE 설정
			albumInfo.setResultType("ALB");
			
			//서비스아이콘 설정
			if (paramVO.getNscGb().equals("UFX")) {
				albumInfo.setServiceIcon(albumInfo.getServiceIconUflix());
			} else {
				albumInfo.setServiceIcon(albumInfo.getServiceIconHdtv());
			}
			
		    //예고편 앨범 정보 설정
		    if ( albumInfo.getPrAlbumId() != null && !albumInfo.getPrAlbumId().equals("") )
		    	albumInfo.setPreviewYn("Y");
		    else
		    	albumInfo.setPreviewYn("N");
			
			//이미지 정보 가져오기
			String imagflag = "N";
			if ( paramVO.getRqsType().equals("M")) imagflag = "M";
			albumInfo.setFlag(imagflag);
			ImageInfo imgInfo = this.getImgInfo(albumInfo, paramVO);
			
			if(resultVO != null){
				nMainCnt = resultVO.size();
			}else{
				paramVO.setResultCode("21000000");
//				imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID220, "", null, "chnl_list:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
			}
			
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("컨텐츠 정보 가져오기", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			// 씨네21 평균 별점, 리뷰 수 가져오기
			HashMap<String, String> hmCinePointCnt = null;
			
			try {
				if(StringUtils.isNotBlank(albumInfo.getCineId()))
				{
					hmCinePointCnt = this.getNSContInfoDao.getCinePointCnt(albumInfo);
					
					albumInfo.setCine_avg_point(hmCinePointCnt.get("cine_avg_point"));
					albumInfo.setCine_count(hmCinePointCnt.get("cine_count"));
				}
			} catch(Exception e) {
				imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			}
			
			tp1	= System.currentTimeMillis();
			imcsLog.timeLog("씨네21 평균 별점, 리뷰 수 가져오기", String.valueOf(tp1 - tp2), methodName, methodLine);
			
			//ResponseVO에서 나머지 결과값 세팅
			GetNSContInfoResponseVO responseVO = new GetNSContInfoResponseVO();

			responseVO.setAlbumId(albumInfo.getAlbumId());
			responseVO.setAlbumName(albumInfo.getAlbumName());
			responseVO.setRuntime(albumInfo.getRuntime());
			responseVO.setPrInfo(albumInfo.getPrInfo());
			responseVO.setSynopsis(albumInfo.getSynopsis());
			responseVO.setIsNew(paramVO.getIsNew());
			responseVO.setIsUpdate(paramVO.getIsUpdate());
			responseVO.setDirector(albumInfo.getProducer());
			responseVO.setProducer(albumInfo.getProducer());
			responseVO.setActor(albumInfo.getActorsDisplay());
			responseVO.setOnairDate(albumInfo.getOnairDate());
			responseVO.setReleaseDate(albumInfo.getReleaseDate());
			responseVO.setSeriesDesc(albumInfo.getSeriesNo());
			responseVO.setTerrCh(albumInfo.getTerrCh());
			responseVO.setTerrEdDate(albumInfo.getTerrEdDate());
			responseVO.setReservedDate(albumInfo.getServiceDate());
			responseVO.setServiceGb(albumInfo.getServiceIcon());
			responseVO.setImgUrl(poster_url);
			responseVO.setImgFileName(albumInfo.getPosterP());
			responseVO.setStillUrl(still_url);
			responseVO.setStillFileName(imgInfo.getStillImage());
			responseVO.setPreviewYn(albumInfo.getPreviewYn());
			responseVO.setPrevAlbumId(albumInfo.getPrAlbumId());
			responseVO.setGenreLarge(albumInfo.getGenreLarge());
			responseVO.setGenreMid(albumInfo.getGenreMid());
			responseVO.setGenreInfo(albumInfo.getGenreInfo());
			responseVO.setSmiLanguage(albumInfo.getSmiLanguage());
			responseVO.setServiceFlag(albumInfo.getServiceFlag());
			responseVO.setPointWatcha(albumInfo.getWatchaPoint());
			responseVO.setPointCntWatcha(albumInfo.getWatchaCount());
			responseVO.setCommentCnt(albumInfo.getCommentCnt());
			responseVO.setPoint("");
			responseVO.setCpProperty(albumInfo.getCpProperty());
			responseVO.setCpPropertyUfx(albumInfo.getCpPropertyUfx());
			responseVO.setRearHd(albumInfo.getRealHd());
			responseVO.setPresentYn(albumInfo.getPresentYn());
			responseVO.setPresentPrice(albumInfo.getPresentPrice());
			responseVO.setDownloadYn(albumInfo.getDownYn());
			responseVO.setMycutYn(albumInfo.getMycutYn());
			responseVO.setDatafreeBillFlag(albumInfo.getDatafreeBillYn());
			responseVO.setSeasonYn(albumInfo.getSeasonYn());			
			responseVO.setAddImgUrl(conts_url);
			responseVO.setAddImgFileName(imgInfo.getMainImage());
			responseVO.setQsheetYn(cueInfo.get("CUESHEET_YN").toString());
			responseVO.setMusicAlbumType(albumInfo.getMusicType());
			responseVO.setMusicOnairStatus(cueInfo.get("STATUS").toString());
			responseVO.setOmniviewYn(albumInfo.getOmniviewYn());
			responseVO.setLinkUrl(albumInfo.getLinkUrl());
			responseVO.setSubTitle(albumInfo.getSubTitle());
			responseVO.setYear(albumInfo.getYear());
			responseVO.setNfcCode(albumInfo.getNfcCode());
			responseVO.setVodType(albumInfo.getVodType());
			responseVO.setPlayer(albumInfo.getPlayer());
			responseVO.setStudio(albumInfo.getStudio());
			responseVO.setVrType(albumInfo.getVrType());
			responseVO.setGenreUxten(albumInfo.getGenreUxten());
			responseVO.setHqAudioYn(albumInfo.getHqAudioYn());
			responseVO.setPromotionCopy(albumInfo.getPromotionCopy());
			responseVO.setKidsGrade(albumInfo.getKidsGrade());
			responseVO.setCineId(albumInfo.getCineId());
			responseVO.setCine_avg_point(albumInfo.getCine_avg_point());
			responseVO.setCine_count(albumInfo.getCine_count());
			responseVO.setMainGrpType(albumInfo.getMainGrpType());
			responseVO.setSubGrpType(albumInfo.getSubGrpType());
			responseVO.setDirectorDisplay(albumInfo.getDirectorDisplay());
			responseVO.setLivePpvYn(albumInfo.getPayFlag());
			resultVO.add(responseVO);
			
			//API 프로세스 처리 완료 후 헤더 정보 생성....
			String resultHeader  = String.format("%s|%s|%s|%s|", 
					"0", "", paramVO.getVatRate(), paramVO.getCatId());
			resultListVO.setResultHeader(resultHeader);
			resultListVO.setResultOst(ostInfo);
			resultListVO.setList(resultVO);
			
			//######################################################
			// 로직구현 (끝)
			//######################################################
			
	    	
	    	tp1	= System.currentTimeMillis();
			imcsLog.timeLog("콘텐츠 인포 리스트 Fetch", String.valueOf(tp1 - tp2), methodName, methodLine); 
			
		}catch(ImcsException ie) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E",ie.getClass().getName()+":"+ie.getMessage());
			throw ie;
		} catch(Exception e) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			throw new ImcsException();
		} finally{
			resultListVO.setResultCode(paramVO.getResultCode());
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID220) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultListVO;
	}
	
	/**
	 * 검수 STB여부 조회
	 * @param paramVO
	 * @return String		TEST_SBC	
	 */
	@SuppressWarnings("rawtypes")
	public void getTestSbc(GetNSContInfoRequestVO paramVO) {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId 		= "nsvod220_001_20180701";
		int querySize		= 0;

		try {			
			List<HashMap> list = getNSContInfoDao.getTestSbc(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			
			if (list != null && !list.isEmpty()) {
				querySize	= list.size();
				paramVO.setTestSbc(list.get(0).get("TEST_SBC").toString());
				paramVO.setCustFlag(list.get(0).get("CUST_FLAG").toString());
				
			}else{
				paramVO.setTestSbc("N");
				paramVO.setCustFlag("N");
			}
			
			try{
//				imcsLog.dbLog(ImcsConstants.NSAPI_PRO_ID220, sqlId, null, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}

	}
	
	/**
	 * 카테고리 유효성 체크 및 편성 여부 체크
	 * @param paramVO
	 * @return paramVO	
	 */
	public void getChkCate(GetNSContInfoRequestVO paramVO) {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId 		= "nsvod220_010_20180701"; 
		int querySize		= 0;
		List<HashMap> list = null;
		
		try {
			if ( !"".equals(paramVO.getCatId()) && !"M".equals(paramVO.getRqsType()) )
			{
				list = getNSContInfoDao.getChkCate(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}
			
			if (list != null && !list.isEmpty()) {
				querySize	= list.size();
				paramVO.setViewFlag(list.get(0).get("VIEW_FLAG").toString());
				paramVO.setIsNew(list.get(0).get("IS_NEW").toString());
				paramVO.setIsUpdate(list.get(0).get("IS_UPDATE").toString());
				paramVO.setNscGb(list.get(0).get("NSC_GB").toString());
				
				paramVO.setCatChkCnt(querySize);
			} else {
				list = getNSContInfoDao.getChkCate2(paramVO); //nsvod220_011_20180701
				
				querySize	= list.size();
				//LCHECKCOUNT
				paramVO.setCatChkCnt(Integer.parseInt(list.get(0).get("LCHECKCOUNT").toString()));
				paramVO.setViewFlag(list.get(0).get("VIEW_FLAG").toString());
				paramVO.setIsNew(list.get(0).get("IS_NEW").toString());
				paramVO.setIsUpdate(list.get(0).get("IS_UPDATE").toString());
				paramVO.setNscGb(list.get(0).get("NSC_GB").toString());
				
		        //시리즈 카테고리 > 상용 카테고리 > 검수카테고리로 우선순위 설정
		        if ( !list.get(0).get("CATEGORY_ID").toString().equals("") && list.get(0).get("CATEGORY_ID").toString().length() > 0 )
		        {
		        	paramVO.setCatId(list.get(0).get("CATEGORY_ID").toString());
		        }
		        else
		        {
		            if ( !list.get(0).get("CATEGORY_ID_CHK").toString().equals("")) {
		            	paramVO.setCatId(list.get(0).get("CATEGORY_ID_CHK").toString());
		            }
		            else {
		            	paramVO.setCatId(list.get(0).get("CATEGORY_ID_ALL").toString());
		            }
		        }
		        
				if (list==null || list.size() == 0) {
//					imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID220, sqlId, null, "카테고리 편성정보가 없습니다" + ImcsConstants.RCV_MSG3, methodName, methodLine);
				}
			}
			
			try{
//				imcsLog.dbLog(ImcsConstants.NSAPI_PRO_ID220, sqlId, null, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}

	}
	
	/**
	 * 컨텐츠 정보 가져오기
	 * @param paramVO
	 * @return GetNSContInfoResponseVO
	 */
	public List<AlbumInfo> getContInfoList(GetNSContInfoRequestVO paramVO) {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId 		= "nsvod220_100_20180725"; 
		int querySize		= 0;
		List<AlbumInfo> list = null;
		
		try {
			list = getNSContInfoDao.getContInfoList(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			
			if (list != null && !list.isEmpty()) {
				querySize	= list.size();
			} else {

			}
			
			try{
//				imcsLog.dbLog(ImcsConstants.NSAPI_PRO_ID220, sqlId, null, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}

		return list;
	}
	
	/**
	 * 장르 정보 가져오기
	 * @param AlbimInfo
	 * @return String
	 */
	@SuppressWarnings("rawtypes")
	public String getGenreInfo(AlbumInfo albumInfo, GetNSContInfoRequestVO paramVO) {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId 		= "nsvod220_300_20180725"; 
		int querySize		= 0;
		List<HashMap> list = null;
		String genreInfo  = albumInfo.getGenreInfo();
		String vod_category_name = "";
		String vod_sort_no = "";
		int count = 0;
		
		try {
			list = getNSContInfoDao.getGenreInfoList(albumInfo);
			
			if (list != null && !list.isEmpty()) {
				querySize	= list.size();
				for (HashMap item : list) {
					count ++;
					vod_category_name = item.get("VOD_CATEGORY_NAME").toString();
					vod_sort_no = item.get("VOD_SORT_NO").toString();
					
					if (count == 1) {
						genreInfo = vod_category_name;
					} else {
						genreInfo = genreInfo + ", " + vod_category_name;
					}
				}
				
			} else {

			}
			
			try{
//				imcsLog.dbLog(ImcsConstants.NSAPI_PRO_ID220, sqlId, null, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}

		return genreInfo;
	}

	/**
	 * 다운로드 체크
	 * @param AlbimInfo
	 * @return String
	 */
	@SuppressWarnings("rawtypes")
	public String getDownCheck(AlbumInfo albumInfo, GetNSContInfoRequestVO paramVO) {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId 		= "nsvod220_400_20180725"; 
		int querySize		= 0;
		List<String> list = null;
		String down_yn = "N";
		
		try {
			list = getNSContInfoDao.getDownCheck(albumInfo);
			
			if (list != null && !list.isEmpty()) {
				querySize	= list.size();
				down_yn = list.get(0);
			}
			
			try{
//				imcsLog.dbLog(ImcsConstants.NSAPI_PRO_ID220, sqlId, null, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}

		return down_yn;
	}
	
	/**
	 * OST 정보 가져오기
	 * @param AlbimInfo
	 * @return List<HashMap>
	 */
	@SuppressWarnings("rawtypes")
	public String getOSTInfo(AlbumInfo albumInfo, GetNSContInfoRequestVO paramVO) {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId 		= "nsvod220_900_20180725"; 
		int querySize		= 0;
		List<HashMap> list = null;
		int count = 0;
		String ostInfo = "";
		
		try {
			list = getNSContInfoDao.getOSTInfo(albumInfo);
			
			if (list != null && !list.isEmpty()) {
				querySize	= list.size();
				for (HashMap item : list) {
					ostInfo = String.format("%s|%s|%s|%s|%s|%s|%s|\f", 
							item.get("OST").toString(),
							albumInfo.getAlbumId(),
							item.get("RESERVED_SEQ").toString(),
							item.get("OST_TYPE").toString(),
							item.get("OST_ID").toString(),
							item.get("OST_SINGER").toString(),
							item.get("OST_TITLE").toString()
							);
				}
				
			} else {

			}
			
			try{
//				imcsLog.dbLog(ImcsConstants.NSAPI_PRO_ID220, sqlId, null, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		//System.out.println("####################################################:" + ostInfo);
		return ostInfo;
	}
	
	/**
	 * OST 정보 가져오기
	 * @param AlbimInfo
	 * @return List<HashMap>
	 */
	@SuppressWarnings("rawtypes")
	public ImageInfo getImgInfo(AlbumInfo albumInfo, GetNSContInfoRequestVO paramVO) {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId 		= "nsvod220_200_20180725"; 
		int querySize		= 0;
		List<HashMap> list = null;
	    int	iStillCount = 0;
	    //int	iLogoCount = 0;
	    int	iBackCount = 0;
		ImageInfo imgInfo = new ImageInfo();
		
		try {
			list = getNSContInfoDao.getImgInfo(albumInfo);
			
			if (list != null && !list.isEmpty()) {
				querySize	= list.size();
				
				for (HashMap item : list) {
					
					if (item.get("IMG_FLAG").equals("M")) {
						if (iBackCount == 1) continue;
						
						imgInfo.setMainImage(item.get("MAIN_IMG_FILE_NAME").toString());
						iBackCount ++;
					} else {
						if ( iStillCount >= 5 ) continue;
						
						iStillCount ++;

						if ( iStillCount == 1 )
						{
							//strcpy((char*)imageInfo.c_still_image.arr, (char*)c_file_name.arr);
							imgInfo.setStillImage(item.get("MAIN_IMG_FILE_NAME").toString());
						}
						else
						{
							//sprintf((char*)imageInfo.c_still_image.arr, "%s\b%s", (char*)imageInfo.c_still_image.arr, (char*)c_file_name.arr);
							String stillImage = String.format("%s\b%s", imgInfo.getStillImage(), item.get("MAIN_IMG_FILE_NAME").toString());
							imgInfo.setStillImage(stillImage);
						}
					}
					
//					imgInfo = String.format("%s\b%s", 
//							list.get(0).get("MAIN_IMG_FILE_NAME").toString(),
//							list.get(0).get("IMG_FLAG").toString()
//							);
				}
				
			} else {

			}
			
			try{
//				imcsLog.dbLog(ImcsConstants.NSAPI_PRO_ID220, sqlId, null, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}

		return imgInfo;
	}
	
	public int getAlbumTypeInfo(GetNSContInfoRequestVO paramVO) {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId 		= "nsvod220_010_20180701"; 
		int querySize		= 0;
		
		try {
			//공연앨범인 경우 타입 체크
			if (paramVO.getRqsType().equals("M")) {
				HashMap ainfo = getNSContInfoDao.getAlbumTypeCheck(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
				String chkVodType = ainfo.get("CHK_VOD_TYPE") == null ? "" : ainfo.get("CHK_VOD_TYPE").toString();
				String chkConType = ainfo.get("CHK_CON_TYPE") == null ? "" : ainfo.get("CHK_CON_TYPE").toString();
				String mainAlbumId = "";
				
				//옴니뷰 앨범일 경우 해당
				if (chkVodType.equals("C") || chkVodType.equals("O")) {
					//숏클립에 대한 옴니뷰인 경우 숏클립 앨범 ID 구해오기
					if (chkConType.equals("D") || chkConType.equals("T")) {
						mainAlbumId = getNSContInfoDao.getOmniviewAlbumId1(paramVO);
					} else {
						mainAlbumId = getNSContInfoDao.getOmniviewAlbumId2(paramVO);
					}					
					
					if (mainAlbumId==null || mainAlbumId.equals("")) {
//						imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID220, sqlId, null, "옴니뷰 앨범의 매핑되는 본편 앨범이 없습니다...:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
						querySize = -1;
					}
					else
					{
						paramVO.setAlbumId(mainAlbumId);
					}
				}									
			}			
			try{
//				imcsLog.dbLog(ImcsConstants.NSAPI_PRO_ID220, sqlId, null, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
		return querySize;

	}
	
	public HashMap<String, String> getCuesheetInfo(AlbumInfo albumInfo, GetNSContInfoRequestVO paramVO) {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId 		= "nsvod220_010_20180701"; 
		int querySize		= 0;

		List<HashMap> cueList;
		HashMap<String, String> cueInfo = new HashMap<String, String>();
		
		try {
			
			if (albumInfo.getMusicType().equals("D") || albumInfo.getMusicType().equals("T")) {
				cueList = getNSContInfoDao.getCueSheetInfo1(albumInfo);
			} else {
				cueList = getNSContInfoDao.getCueSheetInfo2(albumInfo);
			}
			
			if (cueList.size() > 0) {
				cueInfo = cueList.get(0);
			} else {
				
				cueInfo.put("PERFORM_DATE","");
				cueInfo.put("PERFORM_TIME","");
				cueInfo.put("STATUS","");
				cueInfo.put("CONFIRM_YN","");
				cueInfo.put("VOD_ONLY_YN","");
				cueInfo.put("VIEW_FLAG","");
				cueInfo.put("CUESHEET_YN","");
				cueInfo.put("IMAGE_URL","");
				cueInfo.put("IMAGE_FOLDER","");
				cueInfo.put("CHNL_IMG_FILE","");
				cueInfo.put("MAIN_IMG_FILE","");
				cueInfo.put("OMNI_IMG_FILE","");
				cueInfo.put("LOGO_IMG_FILE","");
				cueInfo.put("PAY_FLAG","");
			}
			
			try{
//				imcsLog.dbLog(ImcsConstants.NSAPI_PRO_ID220, sqlId, null, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
		return cueInfo;

	}
	
	public ComConcertInfoVO getConsertInfo(AlbumInfo albumInfo, GetNSContInfoRequestVO paramVO) {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		
		ComConcertInfoVO cstInfo = new ComConcertInfoVO();
		
		try {
			cstInfo = getNSContInfoDao.getConsertInfo(albumInfo);
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
		return cstInfo;

	}

}
