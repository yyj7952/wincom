package kr.co.wincom.imcs.api.getNSContDtl;

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
import kr.co.wincom.imcs.common.vo.ComDupCHk;
import kr.co.wincom.imcs.common.vo.ComTrailerVO;
import kr.co.wincom.imcs.common.vo.ComWatchaVO;
import kr.co.wincom.imcs.common.vo.ContTypeVO;
import kr.co.wincom.imcs.common.vo.FmInfoVO;
import kr.co.wincom.imcs.common.vo.OstInfoVO;
import kr.co.wincom.imcs.common.vo.StillImageVO;
import kr.co.wincom.imcs.common.vo.SvodPkgVO;
import kr.co.wincom.imcs.handler.ImcsException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import kr.co.wincom.imcs.common.NosqlCacheType;

@Service
public class GetNSContDtlServiceImpl implements GetNSContDtlService {
	private Log imcsLogger		= LogFactory.getLog("API_getNSContDtl");
	
	@Autowired
	private GetNSContDtlDao getNSContDtlDao;
	
	@Autowired
	private CommonService commonService;
	
//	public void getNSContDtl(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	private int nDataChk	= 0;
	
	@Override
	public GetNSContDtlResultVO getNSContDtl(GetNSContDtlRequestVO paramVO){
//		this.getNSContDtl(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	

		// 프로세스 정보
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		String isLastProcess = ImcsConstants.RCV_MSG5;

		List<GetNSContDtlResponseVO> resultVO	= new ArrayList<GetNSContDtlResponseVO>();
		GetNSContDtlResponseVO tempVO = new GetNSContDtlResponseVO();		// 임시용 컨텐츠 상세 정보
		GetNSContDtlResponseVO returnVO = null;		// 컨텐츠 상세 정보
		GetNSContDtlResultVO resultListVO = new GetNSContDtlResultVO();
		
		String szImgSvrIp		= "";	// 이미지서버 IP
	    String szImgCasheSvrIp	= "";	// 이미지 캐쉬서버 IP
	    String szImgResizeSvrIp	= "";	// 이미지 리사이즈서버 IP
	    String szImgSvrUrl		= "";	// 이미지 서버 URL
	    String msg				= "";
	    
		String szCatName	= "";
		String szGenreName	= "";
		
		int nResultSet		= 1;		// 조회 결과 ?
		int nMainCnt		= 0;		// 메인쿼리 Count 수
		int nSubCnt			= 0;		// 메인쿼리 내 서브쿼리 Count 수
	    int nProdCnt = 0;
	    nDataChk = 0;
	     
	    
	    long tp1, tp2;
	    
		try{
			// 서버IP정보 조회
			tp1 = System.currentTimeMillis();
			
			try {
				szImgSvrIp			= commonService.getIpInfo("img_server", ImcsConstants.API_PRO_ID996.split("/")[1]);					// 이미지 서버 IP 조회
				szImgCasheSvrIp		= commonService.getIpInfo("img_cachensc_server", ImcsConstants.API_PRO_ID996.split("/")[1]);		// 이미지 캐쉬서버 IP 조회
				szImgResizeSvrIp	= commonService.getIpInfo("img_create_server", ImcsConstants.API_PRO_ID996.split("/")[1]);			// 이미지ReSize서버 IP 조회
				szImgSvrUrl			= commonService.getIpInfo("img_resize_server", ImcsConstants.API_PRO_ID996.split("/")[1]);			// 이미지서버 URL 조회
			} catch(Exception e) {
//				imcsLog.failLog(ImcsConstants.API_PRO_ID996, "", null, "svr_ip:" + ImcsConstants.RCV_MSG1, methodName, methodLine);
				paramVO.setResultCode("31000000");

				throw new ImcsException();
			}

			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("서버IP값 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			// 날짜 검색
			String szSysDate	= "";
			
			try {
				szSysDate	= commonService.getSysdate();
			} catch (Exception e) {
//				imcsLog.failLog(ImcsConstants.API_PRO_ID996, "", null, "sysdate:" + ImcsConstants.RCV_MSG2, methodName, methodLine);
			}
		    
		    // TEST계정 유무 조회
		    String testSbc = this.testSbc(paramVO);
		    paramVO.setTestSbc(testSbc);
		    

					    
		    String szThumbFileName	= "";	// 썸네일 파일명
		    String szStillFileName	= "";	// 스틸이미지 명

		    ComWatchaVO watchaVO = new ComWatchaVO();
		    ComDataFreeVO datafreeVO = new ComDataFreeVO();
    		List<OstInfoVO> lstOstInfo = new ArrayList<OstInfoVO>();
		    
		    //if(paramVO.getCatId().length() != 0){
		    	// 컨텐츠 상세 정보 조회
		    	resultVO =  this.getNSContDtlList(paramVO);
		    	
		    	tp1	= System.currentTimeMillis();
		    	imcsLog.timeLog("컨텐츠정보 조회", String.valueOf(tp1 - tp2), methodName, methodLine);
		        
		    	
		    	if(resultVO != null && resultVO.size() > 0){
		    		nMainCnt = resultVO.size();
		    	} else {
		    		// 컨텐츠 ID로 조회시 미존재일 경우 앨범ID로 재 조회
		    		paramVO.setCatId("");
		    		resultVO = this.getNSContDtlList(paramVO);
		    		
		    		if(resultVO != null && resultVO.size() > 0)
			    		nMainCnt = resultVO.size();
		    	}
		    	
		    	for(int i = 0; i < nMainCnt; i++){
		    		returnVO = resultVO.get(i);
		    		// returnVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		    		/*returnVO.setApprovalPrice("0.00");		// 인앱 기본가격 저장
		    		
		    		// CP_PROPERTY_BIN이 1이면 인앱 정보 조회
		    		if( "1".equals( returnVO.getCpPropertyBin() ) ){
		    			HashMap<String, String> mApproval = new HashMap<String, String>();
		    			mApproval = getNSContDtlDao.getApproval(returnVO.getSuggestedPrice());
		    			
		    			if(mApproval != null && !mApproval.isEmpty()){
		    				returnVO.setApprovalId(StringUtil.nullToSpace(mApproval.get("APPROVAL_ID")));
			    			returnVO.setApprovalPrice(StringUtil.nullToSpace(mApproval.get("APPROVAL_PRICE")));
		    			}
		    		}*/
		    		
		    		 /* 다국어 자막 관련 로직         */
					if("N".equals(paramVO.getDecPosYn())){
						
						String szSmiLanguage = "";
						
						if( returnVO.getSmiLanguage().indexOf("한국어") >= 0){
							szSmiLanguage = "한국어;";
						}
						
						if( returnVO.getSmiLanguage().indexOf("영어") >= 0){
							szSmiLanguage = szSmiLanguage + "영어;";
						}
						
						if(!"".equals(szSmiLanguage)){
							returnVO.setSmiLanguage(szSmiLanguage);
						}
						
					}
		    		
		    		// 이미지 URL 정보 저장
		    		returnVO.setImgUrl(szImgSvrIp);
		    		returnVO.setImgUrl1(szImgCasheSvrIp);
		    		returnVO.setImgUrl2(szImgResizeSvrIp);
		    		returnVO.setImgUrl3(szImgSvrUrl);
		    		
		    		// 5.1채널 유무 저장
		    		if("DOLBY 5.1".equals(returnVO.getAudioType())){
		    			returnVO.setIs51Ch("Y");
		    		}
		    		
		    		// HD 여부 저장
		    		if("Y".equals(returnVO.getHdcontent()) || "S".equals(returnVO.getHdcontent())  ){
		    			returnVO.setIsHd("Y");
		    		}else{
		    			returnVO.setIsHd("N");
		    		}
		    		
		    		if("N".equals(returnVO.getPreviewFlag())){
		    			returnVO.setPreviewFlag("V");
		    		}
		    		
		    		if(!"".equals(returnVO.getContsId())){
		    			//OST정보 조회
		    			paramVO.setContsId(returnVO.getContsId());
		    			lstOstInfo = this.getOstInfo(paramVO);
		    			
		    			tp2	= System.currentTimeMillis();
				    	imcsLog.timeLog("OST정보 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
				    	
				    	
		    			//별점정보조회
		    			watchaVO = this.getWatchaInfo(paramVO);
		    			
		    			tp1	= System.currentTimeMillis();
				    	imcsLog.timeLog("별점(왓챠)정보 조회", String.valueOf(tp1 - tp2), methodName, methodLine);
		    		}
		    		
		    		
		    		// 상품타입조회
	    			String szProdType = this.getProductType(paramVO);
	    			returnVO.setProductType(szProdType);
	    			
	    			
		    		
	    			/* 컨텐츠 및 데이터 프리의 인앱 가격 가져오기
	    			 * PPV/FVOD 구분을 위하여 product_type 조회 후 가격 정보 조회*/
	    			/* FVOD의 경우 0원으로 인앱 가격을 조회한다.	*/
	    			if("0".equals(returnVO.getProductType())){
	    				datafreeVO.setPrice("0");
	    			}else{
	    				datafreeVO.setPrice(returnVO.getSuggestedPrice());
	    			}
	    			
	    			tp1	= System.currentTimeMillis();	    			
	    			
	    			//인앰 가격 조회
	    			datafreeVO = this.getDatafreeInfo(datafreeVO, paramVO);
	    			
	    			if( "R".equals(returnVO.getPreviewFlag()) && Integer.parseInt(returnVO.getReservedDate()) >= Integer.parseInt(szSysDate)  ){
	    				
	    				datafreeVO.setDatafreePrice("0");
	    				datafreeVO.setDatafreeApprovalId("");
	    				datafreeVO.setDatafreeApprovalPrice("0.00");
	    				datafreeVO.setPpvDatafreeApprovalId("");
	    				datafreeVO.setPpvDatafreeApprovalPrice("0.00");
	    				
	    			}else{
	    				if("N".equals(returnVO.getDatafreeBillYn())){
	    					
	    					/* 데이터 프리 무료	*/
	    					if("1".equals(returnVO.getCpPropertyBin())){
	    						
	    						/* 인앱 가격 제공	*/
	    						datafreeVO.setDatafreePrice("0");
	    	    				datafreeVO.setDatafreeApprovalId("");
	    	    				datafreeVO.setDatafreeApprovalPrice("0.00");
	    	    				datafreeVO.setPpvDatafreeApprovalId("");
	    	    				datafreeVO.setPpvDatafreeApprovalPrice("0.00");
	    						
	    	    				if("1".equals(returnVO.getProductType())){
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
	    					if("1".equals(returnVO.getCpPropertyBin())){
	    						
	    						/* 인앱 가격 제공	*/	    							    						
	    						if("1".equals(returnVO.getProductType())){
	    							/* PPV 컨텐츠	*/
	    							datafreeVO.setDatafreeApprovalId("");
		    	    				datafreeVO.setDatafreeApprovalPrice("0.00");
		    	    				
	    						}   						
	    						else if("0".equals(returnVO.getProductType())){
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
	    						/* SVOD ONLY 인경우 datafree_price(원 가격)를 0으로 제공	*/
	    						if( "3".equals(returnVO.getProductType()) || "2".equals(returnVO.getProductType()) ){
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
	    			
	    			tp2	= System.currentTimeMillis();
			    	//imcsLog.timeLog("인앱 가격 정보 조회(IMCSUSER.PT_LA_APPROVAL_INFO)", String.valueOf(tp2 - tp1), methodName, methodLine);
	    			
			    	int maxViewingLength = 0;
			    	
			    	try {
			    		maxViewingLength = Integer.parseInt(returnVO.getMaxViewingLength());
					} catch (Exception e) {
						maxViewingLength = 0;
					}
			    	
	    			/* 평생 소장 컨텐츠 인앱 가격 미제공, 데이터 프리 구매 불가	*/
	    			if(maxViewingLength/24 > 2000){
	    				
	    				returnVO.setDatafreeBillYn("X");
	    				datafreeVO.setApprovalId("");
	    				datafreeVO.setApprovalPrice("0.00");
	    				datafreeVO.setDatafreePrice("");
	    				datafreeVO.setDatafreeApprovalId("");
	    				datafreeVO.setDatafreeApprovalPrice("0.00");
	    				datafreeVO.setPpvDatafreeApprovalId("");
	    				datafreeVO.setPpvDatafreeApprovalPrice("0.00");
	    				
	    			}

	    			
	    			// 유무료 설정
	    			if( "0".equals(returnVO.getProductType())) {
	    				returnVO.setPrice("0");
	    				returnVO.setBuyYn("1");
	    				returnVO.setExpiredDate("");
	    				
	    				nProdCnt++;
	    			} else if ("1".equals(returnVO.getProductType())) {
	    				returnVO.setPrice(returnVO.getSuggestedPrice());
	    				returnVO.setPriceDesc(returnVO.getSuggestedPrice());
	    				
	    				// 구매 중복 체크
	    				try {
	    					ComDupCHk dupChkVO = new ComDupCHk();
	    					
	    					tp1	= System.currentTimeMillis();
	    					
	    					try {
	    						dupChkVO	= getNSContDtlDao.getBuyDupChk(paramVO);
							} catch (Exception e) {
								paramVO.setResultCode("40000000");
//								imcsLog.failLog(ImcsConstants.API_PRO_ID996, "", null, "buy_info1:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
							}
	    					
	    					
	    					
	    					tp2	= System.currentTimeMillis();
	    			    	imcsLog.timeLog("구매내역,, 구매일자 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
	    					
	    					int nDupChk	= 0;
	    					
	    					if(dupChkVO != null && dupChkVO.getnDataChk() > 0) {
	    						//
	    						nDupChk	= dupChkVO.getnDataChk();
	    					}
	    					
		    				if(nDupChk > 0){
		    					returnVO.setBuyYn("0");
		    				}
		    				else{
		    					returnVO.setBuyYn("1");
		    					dupChkVO.setExpDate("");
		    				}
		    					
		    				
		    				returnVO.setExpiredDate(dupChkVO.getExpDate());
		    				returnVO.setBuyDate(dupChkVO.getBuyDate());
	    				} catch (Exception e) {
	    					nResultSet	= 1;
	    					paramVO.setResultCode("40000000");
	    					
//	    					imcsLog.failLog(ImcsConstants.API_PRO_ID996, "", null, "buy_info1:" + ImcsConstants.RCV_MSG2, methodName, methodLine);
	    				}
	    				
	    				nProdCnt++;
	    			}
	    			
	    			//imcsLog.timeLog("예고편 정보 조회 ", String.valueOf(tp2 - tp1), methodName, methodLine);
	    			/* 2017.05.25 예고편 정보 조회 (PT_VO_CATEGORY_MAP에 있는 예고편 컬럼 정보는 사용 안함) */
	    			getTasteAlbum(paramVO, returnVO);
	    			
	    			// 예약구매의 경우
	    			if("R".equals(returnVO.getPreviewFlag())){
	    				if("T".equals(returnVO.getViewingFlag()))		// 검수인 경우
	    					returnVO.setPreviewFlag("R");
	    				if("V".equals(returnVO.getViewingFlag())){	// 노출인 경우
	    					int nReservedDate = Integer.parseInt(StringUtil.nullToZero(returnVO.getReservedDate()));
	    					
	    					if( nReservedDate >= Integer.parseInt(szSysDate) )
	    						returnVO.setPreviewFlag("R");
	    					else
	    						returnVO.setPreviewFlag("V");
	    				}
	    			}
	    			
	    			// 예약컨텐츠의 경우
	    			if("P".equals(returnVO.getPreviewFlag())){
	    				if("T".equals(returnVO.getViewingFlag()))		returnVO.setPreviewFlag("P");		// 검수인 경우
	    				if("V".equals(returnVO.getViewingFlag()))		returnVO.setPreviewFlag("V");		// 노출인 경우
	    			}
	    			
	    			// 2020.04.17 - 구API이므로 시즌 정보는 무조건 N으로 제공한다.
    				returnVO.setSeasonYn("N");
	    			
	    			//imcsLog.timeLog("트레일러 정보 조회 ", String.valueOf(tp2 - tp1), methodName, methodLine);
	    			// 트레일러 정보 조회
	    			ComTrailerVO trailerVO = new ComTrailerVO();
	    			trailerVO = this.getTrailerInfo(paramVO);
					
	    			if(trailerVO != null) {
	    				returnVO.setTrailerUrl1(trailerVO.getTrailerUrl1());
	    				returnVO.setTrailerUrl2(trailerVO.getTrailerUrl2());
	    				returnVO.setTrailerUrl3(trailerVO.getTrailerUrl3());
	    				returnVO.setTrailerFileName1(trailerVO.getTrailerFileName1());
	    				returnVO.setTrailerFileSize1(trailerVO.getTrailerFileSize1());
	    			}
	    			
	    			//imcsLog.timeLog("스틸이미지 정보 조회 ", String.valueOf(tp2 - tp1), methodName, methodLine);
	    			// 스틸이미지 정보 조회
	    			List<StillImageVO> lstImageInfo = new ArrayList<StillImageVO>();
	    			StillImageVO imageVO = new StillImageVO();
	    			lstImageInfo = this.getStillImage(paramVO);
	    			
	    			nSubCnt	= 0;
	    			
	    			if(lstImageInfo != null){
	    				nSubCnt = lstImageInfo.size();
	    			}
	    				    			
	    			for(int j = 0; j < nSubCnt; j++){
	    				imageVO = lstImageInfo.get(j);
	    					
    					//if(nSubCnt == 1){
	    				if(j == 0){
    						szStillFileName = imageVO.getImgFileName();
    						szThumbFileName	= imageVO.getImgFileName();
    					}else{
    						szStillFileName = szStillFileName + ImcsConstants.ARRSEP + imageVO.getImgFileName();
    					}
	    			}
	    			returnVO.setStillImgUrl(szStillFileName);
	    			returnVO.setThumbImgUrl(szThumbFileName);
	    			
	    			
	    			// 인입된 FX_TYPE이 NULL이 아니면 대장르 카테고리명 조회
	    			if(!"X".equals(paramVO.getFxType())){
	    				List<HashMap<String, String>> lstGenreName	= new ArrayList<HashMap<String, String>>();
	    				HashMap<String, String> mGenreName	= new HashMap<String, String>();
	    				
	    				
	    				//imcsLog.timeLog("장르 이름 조회 ", String.valueOf(tp2 - tp1), methodName, methodLine);
	    				try {
	    					lstGenreName = this.getGenreName(paramVO);
	    					
	    					if(lstGenreName != null && !lstGenreName.isEmpty())		nSubCnt = lstGenreName.size();
		        			else													nSubCnt	= 0;
		    				for(int j = 0; j < nSubCnt; j++){
		    					mGenreName	= lstGenreName.get(j);
		    					szCatName	= mGenreName.get("VOD_CATEGORY_NAME");
		    					
		    					if(j == 0)			szGenreName = szCatName;			// szCatName은 통계쪽에 넣어야 함
		    					else				szGenreName	= szGenreName + ", " + szCatName;
		    				}
		    				
		    				returnVO.setGenreName(szGenreName);
						} catch (Exception e) {
							paramVO.setResultCode("41000000");
						}
	    			}
	    			
	    			
	    			// 장르가 V(VOD)이면 썸네일 이미지명 조회
	    			if("V".equals(returnVO.getGenreGb())){
	    				//imcsLog.timeLog("썸네일 이미지명 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
	    				szThumbFileName	= this.getThumbnail(paramVO);
	    				returnVO.setThumbImgUrl(szThumbFileName);
	    			}
	    			
	    			
	    			
	    			// SVOD 패키지 정보 조회 
	    			List<SvodPkgVO> lstSvodPkg = new ArrayList<SvodPkgVO>();
	    			SvodPkgVO svodPkgVO = new SvodPkgVO();
	    			nSubCnt = 0;
	    			
	    			//imcsLog.timeLog("SVOD 패키지 정보 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
	    			try {
	    				
	    				lstSvodPkg = this.getSvodPkg(paramVO);
	    			
	    				if(lstSvodPkg != null && !lstSvodPkg.isEmpty())		nSubCnt = lstSvodPkg.size();
					} catch (Exception e) {
						paramVO.setResultCode("41000000");
					}
	    			
	    			
	    			for(int j = 0; j < nSubCnt; j++){
	    				svodPkgVO = lstSvodPkg.get(j);

	    				// 패키지 일 경우
    					if( "PKG".equals(svodPkgVO.getSvodPkg()) ){
    						//imcsLog.timeLog("패키지 상품정보 및 가격정보 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
	    					// 패키지 상품정보 및 가격정보 조회
	    					tempVO	= this.getPkgDtlInfo(nProdCnt, paramVO, returnVO);
	    					nResultSet = paramVO.getResultSet();
	    					
	    					if(tempVO != null && nResultSet == 0)
	    						returnVO = tempVO;
	    					
	    					
	    					nProdCnt++;
	    				}
    					
    					// 패키지가 아닐 경우
    					else if( "SVOD".equals(svodPkgVO.getSvodPkg()) ){
    						//imcsLog.timeLog("SVO 상품정보 및 가격정보 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
	    					// 상품정보 및 가격정보 조회 (컨텐츠 타입 : SVOD)
	    					int nIsSvod	= 0;
	    					
	    					if("3".equals(returnVO.getProductType())) 	nIsSvod	= 0;
	    					else										nIsSvod	= 1;
	    					
	    					try{
	    					
	    					tempVO	= this.getContDtlType(nIsSvod, paramVO, returnVO);
	    					nResultSet = paramVO.getResultSet();
	    					
	    					if(tempVO != null && nResultSet == 0)
	    						returnVO = tempVO;
	    					
	    					nProdCnt++;
	    					} catch (Exception e) {
	    						//paramVO.setResultSet(-1);
	    						imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage() + "," + methodName + "getContDtlType()");
	    						//return null;
	    					}
	    				}
	    			}
	    			
	    			//imcsLog.timeLog("포인트 유무 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
	    			// 포인트 유무 조회
//	    			String szPointYn = this.getPointYN(paramVO);
	    			returnVO.setSetPointYn("Y");
	    			
	    			
	    			//  Face-Match 준비여부 조회
	    			returnVO.setDownYn("N");
	    			//imcsLog.timeLog("Face-Match 준비여부 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
	    			FmInfoVO getFminfo = this.getFminfo(paramVO);
	    			
	    			int nDownChk = 0;
	    			
	    			if(getFminfo != null){
	    				returnVO.setFmYn(getFminfo.getFmYN());
	    				returnVO.setAssetId(getFminfo.getAdiProdId());
	    				nDownChk = Integer.parseInt(StringUtil.nullToZero(getFminfo.getDownCnt()));
	    			}
	    			
	    			if(nDownChk > 0) 		returnVO.setDownYn("Y");
	    			
	    			returnVO.setCatGb("");
	    			returnVO.setCatGb(returnVO.getCatGb() + ImcsConstants.ARRSEP + returnVO.getCatGb() + ImcsConstants.ARRSEP + returnVO.getCatGb());
		    	}
		    //}
		    	if(returnVO != null){
		    		 // 통계용 데이터 입력
				    resultListVO.setContentsId(returnVO.getContsId());
				    resultListVO.setContentsName(returnVO.getContsName());
				    resultListVO.setCatName(returnVO.getCatName());
				    resultListVO.setGenreLarge(returnVO.getGenreLarge());
				    resultListVO.setGenreMid(returnVO.getGenreMid());
				    resultListVO.setCreateDate(returnVO.getIsUpdate());
				    
				    resultListVO.setDatafreeInfo(datafreeVO);
				    resultListVO.setWatchaInfo(watchaVO);
					resultListVO.setOstInfo(lstOstInfo);
				    resultListVO.setList(returnVO);
		    	}
		   
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
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID996) + "] result[" + resultListVO.toString() + "]"; 
						
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			resultListVO.setResultCode(paramVO.getResultCode());
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID996) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
			
		}
		
		return resultListVO;
	}
	////////////////////////////////////////////////////////////////////////////
	
    /**
     * TEST계정 유무 조회
     * @param
     * @result 
     */
    public String testSbc(GetNSContDtlRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		
		String sqlId =  "lgvod996_001_20171214_001";
		String szTestSbc	= "N";
		
		List<String> list   = new ArrayList<String>();
		
		try {
			
			try{
				list  = getNSContDtlDao.testSbc(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			}catch(DataAccessException e){
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if(list != null && !list.isEmpty()){
				szTestSbc = StringUtil.replaceNull((String)list.get(0), "N");
				
				if(szTestSbc.equals("Y"))	paramVO.setViewFlag2("T");
				else						paramVO.setViewFlag2("V");
			}else{
				szTestSbc = "N";
				paramVO.setViewFlag2("V");
			}
		
		} catch (Exception e) {
			//imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":"+e.getMessage());
			szTestSbc = "N";
			paramVO.setViewFlag2("V");
		}
		
    	return szTestSbc;
    }
    
    
    
    
    /**
     * 컨텐츠 상세 정보 조회
     * @param GetNSContDtlRequestVO
     * @return List<GetNSContDtlResponseVO>
     */
    public List<GetNSContDtlResponseVO> getNSContDtlList(GetNSContDtlRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
    	
    	String sqlId  =  "lgvod996_002_20171215_001";
    	
    	if(paramVO.getCatId().length() != 0){
    		sqlId  =  "lgvod996_002_20171215_001";
    	}else{
    		sqlId  =  "lgvod996_010_20171215_001";
    	}
		int querySize = 0;
		
		List<GetNSContDtlResponseVO> list   = null;

		try {
			try{
				list = getNSContDtlDao.getNSContDtl(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list == null || list.isEmpty()){
				querySize = 0;
//				imcsLog.failLog(ImcsConstants.API_PRO_ID996, "", null, "conts_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
			} else {
				querySize = list.size();
			}
			
			try{
//				imcsLog.dbLog(ImcsConstants.API_PRO_ID996, sqlId, null, querySize, methodName, methodLine);
			}catch(Exception e){}
			
//			paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
			
		} catch (Exception e) {
//			if(cache.getLastException() != null)	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
//			else									paramVO.setResultCode("41000000");
			
//			imcsLog.failLog(ImcsConstants.API_PRO_ID996, sqlId, null, "conts_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
    	return list;
    }
    
    
    /**
     * 컨텐츠 상세 정보 조회2
     * @param GetNSContDtlRequestVO
     * @return List<GetNSContDtlResponseVO>
     */
    public List<GetNSContDtlResponseVO> getNSContDtlList2(GetNSContDtlRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
    	
    	String sqlId	=  "lgvod996_010_20171214_001";
		int querySize = 0;
		
		List<GetNSContDtlResponseVO> list   = null;

		try {
			try{
				list = getNSContDtlDao.getNSContDtl(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list == null || list.isEmpty()){
				querySize = 0;
//				imcsLog.failLog(ImcsConstants.API_PRO_ID996, "", null, "conts_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
			} else {
				querySize = list.size();
			}
			
			try{
//				imcsLog.dbLog(ImcsConstants.API_PRO_ID996, sqlId, null, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
//			imcsLog.failLog(ImcsConstants.API_PRO_ID996, sqlId, null, "conts_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
    	return list;
    }
    
    
    
    /**
     * OST 정보 조회
     * @param GetNSContDtlRequestVO
     * @return List<GetNSContDtlOstVO>
     */
    public List<OstInfoVO> getOstInfo(GetNSContDtlRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
    	
    	String sqlId	= "lgvod995_i10_20171214_001";
    	
		List<OstInfoVO> list   = null;
		
		try {
			try{
				list = getNSContDtlDao.getOstInfo(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

		} catch (Exception e) {
//			 imcsLog.failLog(ImcsConstants.API_PRO_ID996, sqlId, null, "cate_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
    	return list;
    }
    
    
    /**
     * 왓챠(별점) 정보 조회
     * @param	GetNSContDtlRequestVO
     * @result	GetNSContDtlWatchaVO
    **/
    public ComWatchaVO getWatchaInfo(GetNSContDtlRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
    	
    	String sqlId = "lgvod996_s11_20171214_001";
		
		List<ComWatchaVO> list   = new ArrayList<ComWatchaVO>();
		ComWatchaVO resultVO = null;
		
		try {
			
			try{
				list  = getNSContDtlDao.getWatchaInfo(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list != null && !list.isEmpty()){
				resultVO = (ComWatchaVO)list.get(0);
			}
			
		} catch (Exception e) {
//			 imcsLog.failLog(ImcsConstants.API_PRO_ID996, sqlId, null, "cate_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
    	return resultVO;
    }
    
    
    
    /**
     * 상품타입 정보조회
     * @param	GetNSContDtlRequestVO
     * @result	GetNSContDtlWatchaVO
    **/
    public String getProductType(GetNSContDtlRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
				
    	String sqlId =  "lgvod996_003_20171214_001";
		String szProductType	= "";
		
		List<String> list   = null;
		
		try {
			
			try{
				list = getNSContDtlDao.getProductType(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list != null && !list.isEmpty()){
				szProductType = StringUtil.nullToSpace((String)list.get(0));
				if("NULL".equals( szProductType )) szProductType	= "";
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());			
		}
		
    	return szProductType;
    }
    
    
    
    /**
     * 트레일러 정보 조회
     * @param	GetNSContDtlRequestVO
     * @result	List<TrilerVO>
     */
    public ComTrailerVO getTrailerInfo(GetNSContDtlRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
    	
    	String sqlId =  "lgvod996_004_20171214_001";
		
		List<ComTrailerVO> list = new ArrayList<ComTrailerVO>();
		ComTrailerVO resultVO = null;
		
		try {
			try{
				list = getNSContDtlDao.getTrailerInfo(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list != null && !list.isEmpty()){
				resultVO	= list.get(0);
			}
			
		} catch (Exception e) {
			//if(cache.getLastException() != null)	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
			//else									paramVO.setResultCode("41000000");
//			imcsLog.failLog(ImcsConstants.API_PRO_ID996, sqlId, null, "triler_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
    	return resultVO;
    }
    
    
    
   
    
	/**
	 * 이미지파일명 조회 
	 * @param paramVO
	 * @return String 	이미지파일명
	 */
	public List<StillImageVO> getStillImage(GetNSContDtlRequestVO paramVO) {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId = "lgvod996_005_20171214_001";

		List<StillImageVO> list = new ArrayList<StillImageVO>();
		
		try {
			try {
				list = getNSContDtlDao.getStillImage(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

//			paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
		} catch (Exception e) {
//			imcsLog.failLog(ImcsConstants.API_PRO_ID996, sqlId, null, "still_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
		return list;
	}
	
	
	
	/**
	 * 장르명 조회 
	 * @param paramVO
	 * @return List<HashMap<String, String>>
	 */
    @SuppressWarnings("rawtypes")
	public List<HashMap<String, String>> getGenreName(GetNSContDtlRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
    	
    	String sqlId =  "lgvod996_011_20171214_001";

		List<HashMap<String, String>> list   = null;
		
		try {
			
			try{
				list = getNSContDtlDao.getGenreName(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
		} catch (Exception e) {
//			imcsLog.failLog(ImcsConstants.API_PRO_ID996, sqlId, null, "genre_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
    	return list;
    }
    
    
	/**
	 * 썸네일 이미지명 조회 
	 * @param paramVO
	 * @return String
	 */
    public String getThumbnail(GetNSContDtlRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		
    	String sqlId		=  "lgvod996_006_20171214_001";
		String szThumbnail	= "";
		
		List<String> list   = null;
		
		try {
			try{
				list = getNSContDtlDao.getThumbnail(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list != null && !list.isEmpty()){
				szThumbnail = (String)list.get(0);
				
				if(szThumbnail.equals("NULL"))	szThumbnail	= "";
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}
		
    	return szThumbnail;    	
    }
    
    
    
	/**
	 * SVOD 패키지 정보 조회 
	 * @param paramVO
	 * @return String
	 */
    public List<SvodPkgVO> getSvodPkg(GetNSContDtlRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
    	
    	String sqlId =  "lgvod996_007_20171214_001";
		
		List<SvodPkgVO> list   = null;
		
		try {
			try{
				list = getNSContDtlDao.getSvodPkg(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
		} catch (Exception e) {
//			imcsLog.failLog(ImcsConstants.API_PRO_ID996, sqlId, null, "cont_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
    	return list;
    }
    
    
    /**
     * 패키지 타입 및 가격정보 조회
     * @param paramVO
     * @return
     * @throws Exception
     */
    public List<ContTypeVO> getPkgType(GetNSContDtlRequestVO paramVO) throws Exception{
    	IMCSLog imcsLog = new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
    	
    	String sqlId =  "lgvod996_f06_20171214_001";
		
		List<ContTypeVO> list   = null;
		
		try {
			try{
				list = getNSContDtlDao.getPkgType(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
		} catch (Exception e) {
//			imcsLog.failLog(ImcsConstants.API_PRO_ID996, sqlId, null, "conts_type:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
    	return list;       	
    }
    
    
   
    /**
     * 상품타입 및 가격정보 조회
     * @param paramVO
     * @return
     * @throws Exception
     */
    public List<ContTypeVO> getContsType(GetNSContDtlRequestVO paramVO) throws Exception{
    	IMCSLog imcsLog = new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
    	
    	String sqlId =  "lgvod996_f02_20171214_001";
		
		List<ContTypeVO> list   = null;
		
		try {
			
			try{
				list = getNSContDtlDao.getContsType(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list == null ||list.isEmpty()){
//				imcsLog.failLog(ImcsConstants.API_PRO_ID996, sqlId, null, "conts_type...:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
			}
			
		} catch (Exception e) {
			//if(cache.getLastException() != null)	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
			//else									paramVO.setResultCode("41000000");
			
//			imcsLog.failLog(ImcsConstants.API_PRO_ID996, sqlId, null, "conts_type..:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
    	return list;
    }
    
    
    
    /**
     * SVOD 상품 유무 조회
     *  @param
     *  @result	INT
     */
    public Integer ContsTypeChk(GetNSContDtlRequestVO paramVO){
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		
    	String sqlId =  "lgvod996_f03_20171214_001";
		
		List<Integer> list   = null;
		Integer resultVO = 0;
		
		try {
			try{
				list = getNSContDtlDao.ContsTypeChk(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			if(list != null && !list.isEmpty()){
				resultVO = (Integer) list.get(0);
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}
		
    	return resultVO;    	
    }
    
    
    /**
     * SVOD 상품 유무 조회 2
     *  @param
     *  @result	INT
     */
    public Integer ContsTypeChk2(GetNSContDtlRequestVO paramVO){
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();

		String sqlId =  "lgvod996_f04_20171214_001";
		
		List<Integer> list   = null;
		Integer resultVO = 0;
		
		try {
			try{
				list = getNSContDtlDao.ContsTypeChk2(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if(list != null && !list.isEmpty())	resultVO = (Integer) list.get(0);
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}
		
    	return resultVO;
    }
    
    
    /**
     * 포인트 유무 조회
     *  @param
     *  @result	INT
     */
//    public String getPointYN(GetNSContDtlRequestVO paramVO) throws Exception{
//    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
//		String methodName = oStackTrace.getMethodName();
//		
//    	String sqlId =  "lgvod996_008_20171214_001";
//		
//		List<String> list   = null;
//		String resultVO		= "N";
//		
//		try {
//			try{
//				list = getNSContDtlDao.getPointYN(paramVO);
//				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
//				
//			}catch(DataAccessException e){
//				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
//				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
//			}
//			
//			if( list != null && !list.isEmpty()){
//				resultVO = (String) StringUtil.replaceNull(list.get(0), "N");
//			}
//			
//		} catch (Exception e) {
//			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
//			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
//		}
//		
//    	return resultVO;
//    }
    
    
    /**
     * Face-Match 준비여부 조회
     * @param
     * @result
     */
    public FmInfoVO getFminfo(GetNSContDtlRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
    	String sqlId =  "lgvod996_009_20171214_001";
    	int querySize = 0;
		
		List<FmInfoVO> list = null;
		FmInfoVO resultVO = null;
		
		try {

			try{
				list = getNSContDtlDao.getFminfo(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
				if( list != null && !list.isEmpty()){
					resultVO = (FmInfoVO)list.get(0);
					querySize = list.size();
				}
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
//			imcsLog.dbLog(ImcsConstants.API_PRO_ID996, sqlId, null, querySize, methodName, methodLine);
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
    	return resultVO;
    }
    
    
    
	/**
	 * 패키지 상품정보 및 가격정보 조회 
	 * @param paramVO
	 * @return String
	 */
	public GetNSContDtlResponseVO getPkgDtlInfo(int nPkg, GetNSContDtlRequestVO paramVO, GetNSContDtlResponseVO tempVO){
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);

		List<ContTypeVO> lstContTypeInfo = new ArrayList<ContTypeVO>();
		ContTypeVO pkgTypeVO = new ContTypeVO();
		
		paramVO.setResultSet(0);
		
		int nCnt = 0;		
		String szPkgProdId	= "";
		
		ComDupCHk dupChkVO = new ComDupCHk();
		
		// 패키지 타입 및 가격정보 조회 
		try {
			//paramVO.setContsId(tempVO.getContsId());
			paramVO.setAdiProdId(tempVO.getAdiProductId());
			lstContTypeInfo = this.getPkgType(paramVO);
			
			if(lstContTypeInfo != null && !lstContTypeInfo.isEmpty()){
				nCnt = lstContTypeInfo.size();
			} else {
				paramVO.setResultSet(-1);
				return null;
			}
		} catch (Exception e) {
			paramVO.setResultSet(-1);
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage() + "," + methodName + "_getPkgType()");
			
			return null;
		}
			
		for(int i = 0; i < nCnt; i++){
			pkgTypeVO = lstContTypeInfo.get(i);
			szPkgProdId	= pkgTypeVO.getProductId();
			
			if(nPkg > 0) {
				tempVO.setProductType(tempVO.getProductType() + ImcsConstants.ARRSEP + pkgTypeVO.getContsType());
				tempVO.setPriceDesc(tempVO.getPriceDesc() + ImcsConstants.ARRSEP + pkgTypeVO.getPrice());
			} else {
				tempVO.setProductType(pkgTypeVO.getContsType());
				tempVO.setPriceDesc(pkgTypeVO.getPrice());
			}
		}
		
		paramVO.setProdId(szPkgProdId);
		try {
			dupChkVO	= getNSContDtlDao.getPkgDupChk(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			
			if(dupChkVO != null)
				nDataChk	= Integer.parseInt(StringUtil.nullToZero(dupChkVO.getDataChk()));
		} catch(Exception e) {
//	        imcsLog.failLog(ImcsConstants.API_PRO_ID996, "", null, "buy_info1:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
	        paramVO.setResultCode("40000000");
	        paramVO.setResultSet(-1);
		}
		
		if(nPkg > 0) {
			if(nDataChk > 0) {
				tempVO.setBuyYn(tempVO.getBuyYn() + ImcsConstants.ARRSEP + "0");
				tempVO.setExpiredDate(tempVO.getExpiredDate() + ImcsConstants.ARRSEP + dupChkVO.getExpDate());
				tempVO.setBuyDate(tempVO.getBuyDate() + ImcsConstants.ARRSEP + dupChkVO.getBuyDate());
			} else {
				tempVO.setBuyYn(tempVO.getBuyYn() + ImcsConstants.ARRSEP+ "1");
				tempVO.setExpiredDate(tempVO.getExpiredDate() + ImcsConstants.ARRSEP + "");
				tempVO.setBuyDate(tempVO.getBuyDate() + ImcsConstants.ARRSEP + "");
			}
		} else {
			if(nDataChk > 0) {
				tempVO.setBuyYn("0");
				tempVO.setExpiredDate(dupChkVO.getExpDate());
				tempVO.setBuyDate(dupChkVO.getBuyDate());
			} else {
				tempVO.setBuyYn("1");
				tempVO.setExpiredDate("");
				tempVO.setBuyDate("");
			}
		}
		
		return tempVO;
	}
	
	
	
	/**
	 * 상품정보 및 가격정보 조회
	 * @param paramVO
	 * @return String
	 */
	public GetNSContDtlResponseVO getContDtlType(int nSvod, GetNSContDtlRequestVO paramVO, GetNSContDtlResponseVO tempVO){
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		List<ContTypeVO> lstContTypeInfo = new ArrayList<ContTypeVO>();
		ContTypeVO contTypeVO = new ContTypeVO();
		
		int nCnt		= 0;
		int dataChk	= 0;
		
		paramVO.setResultSet(0);
		
		// 상품타입 및 가격정보 조회
		try {
			paramVO.setAdiProdId(tempVO.getAdiProductId());
			lstContTypeInfo = this.getContsType(paramVO);
			
			if(lstContTypeInfo != null && !lstContTypeInfo.isEmpty()){
				nCnt = lstContTypeInfo.size();
			} else {
				paramVO.setResultSet(-1);
				//return null;
			}
		} catch (Exception e) {
			paramVO.setResultSet(-1);
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage() + "," + methodName + "getContsType()");
			//return null;
		}
		
		// SVOD 상품 존재 유무 조회1
		ComDupCHk dupChkVO = new ComDupCHk();
		
		for(int i = 0; i < nCnt; i++){
			contTypeVO = lstContTypeInfo.get(i);
			
			paramVO.setProdId(contTypeVO.getProductId());
			
			
			try {
			
			// PT_PD_PACKAGE 테이블 조회
				dataChk = this.ContsTypeChk(paramVO);
				if(dataChk != 0)	nDataChk = dataChk;
			} catch (Exception e) {
				paramVO.setResultSet(-1);
				imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage() + "," + methodName + "ContsTypeChk()");
				//return null;
			}
			
			// PT_PD_PACKAGE_RELATION 테이블 포함 재조회
			if(nDataChk == 0) {
				try {
					dataChk = this.ContsTypeChk2(paramVO);
					if(dataChk != 0)	nDataChk = dataChk;
				} catch (Exception e) {
					paramVO.setResultSet(-1);
					imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage() + "," + methodName + "ContsTypeChk2()");
					//return null;
				}
			}
			
			// PT_VO_NSC_PRODUCT 조회
			if(nDataChk == 0) {
				try {
				dupChkVO	= getNSContDtlDao.ContsTypeChk3(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
				} catch (Exception e) {
					paramVO.setResultSet(-1);
					imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage() + "," + methodName + "ContsTypeChk3()");
					//return null;
				}
				
				if(dupChkVO != null) nDataChk	= Integer.parseInt(StringUtil.nullToZero(dupChkVO.getDataChk()));
			}
			
			
			
			try {
			if(nSvod > 0) {
				tempVO.setProductType(tempVO.getProductType() + ImcsConstants.ARRSEP + contTypeVO.getContsType());
				
				if(nDataChk > 0){		// 패키지 구매내역이 존재하면
					tempVO.setBuyYn(tempVO.getBuyYn() + ImcsConstants.ARRSEP + "0");
					if(dupChkVO != null)
					{
						tempVO.setBuyDate(tempVO.getBuyDate() + ImcsConstants.ARRSEP + dupChkVO.getBuyDate());
						tempVO.setExpiredDate(tempVO.getExpiredDate() + ImcsConstants.ARRSEP + dupChkVO.getExpDate());
					}
					else
					{
						tempVO.setBuyDate(tempVO.getBuyDate() + ImcsConstants.ARRSEP + "");
						tempVO.setExpiredDate(tempVO.getExpiredDate() + ImcsConstants.ARRSEP + "");
					}
				}else{
					tempVO.setBuyYn(tempVO.getBuyYn() + ImcsConstants.ARRSEP + "1");
					tempVO.setBuyDate(tempVO.getBuyDate() + ImcsConstants.ARRSEP + "");
					tempVO.setExpiredDate(tempVO.getExpiredDate() + ImcsConstants.ARRSEP + "");
				}
				
				tempVO.setExpiredDate(tempVO.getExpiredDate() + ImcsConstants.ARRSEP + "");
			} else {
				tempVO.setProductType(contTypeVO.getContsType());
				
				// 패키지 구매내역이 존재하지 않으면
				if(nDataChk > 0)		tempVO.setBuyYn("0");
				else					tempVO.setBuyYn("1");
				
				tempVO.setExpiredDate("");
			}
			} catch (Exception e) {
				paramVO.setResultSet(-1);
				imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage() + "," + methodName + "nSvod : " + nSvod);
				//return null;
			}
		}
		
		return tempVO;
	}
	
	/**
     * 데이터프리 정보 조회
     * @param	ComDataFreeVO, GetNSContDtlRequestVO
     * @result	ComDataFreeVO
    **/
    public ComDataFreeVO getDatafreeInfo(ComDataFreeVO tempVO, GetNSContDtlRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
    	
    	String sqlId = "lgvod996_016_20171214_001";
		
		List<ComDataFreeVO> list   = new ArrayList<ComDataFreeVO>();
		ComDataFreeVO resultVO = null;
		
		try {
			
			try{
				list  = getNSContDtlDao.getDatafreeInfo(tempVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list != null && !list.isEmpty()){
				resultVO = (ComDataFreeVO)list.get(0);
			}else{
				resultVO = new ComDataFreeVO();
				resultVO.setDatafreePrice("0");
				resultVO.setApprovalPrice("0");
				resultVO.setDatafreeApprovalPrice("0.00");
				resultVO.setPpvDatafreeApprovalPrice("0.00");
			}
			
		} catch (Exception e) {
//			 imcsLog.failLog(ImcsConstants.API_PRO_ID996, sqlId, null, "approval_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			 
			 paramVO.setResultCode("41000000");
		}
    	return resultVO;
    }
    
    /**
     * 예고편 앨범 ID 조회
     * @param	GetNSContDtlRequestVO
     * @result	String
    **/
    public GetNSContDtlResponseVO getTasteAlbum(GetNSContDtlRequestVO paramVO, GetNSContDtlResponseVO tempVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId = "";  	
		
		List<String> list   = new ArrayList<String>();
		String result = null;
		
		try {		
			
			sqlId = "lgvod996_019_20180412_001";
			
			paramVO.setContsId(tempVO.getContsId());
			
			try{
				list  = getNSContDtlDao.getTasteAlbum(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list != null && !list.isEmpty()){
				result = (String)list.get(0);
				tempVO.setTasteAlbumId(result);
				tempVO.setTasteCatId("");
			}else{
				//imcsLog.failLog(ImcsConstants.API_PRO_ID996, sqlId, cache, "TASTE_ALBUM_INFO:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
				
				tempVO.setTasteAlbumId("");
				tempVO.setTasteCatId("");
			}
			
//			paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
			
		} catch (Exception e) {
//			 imcsLog.failLog(ImcsConstants.API_PRO_ID996, sqlId, null, "TASTE_ALBUM_INFO:" + ImcsConstants.RCV_MSG6, methodName, methodLine);	
			 
			 tempVO.setTasteAlbumId("");
			 tempVO.setTasteCatId("");
		}
    	return tempVO;
    }

}
