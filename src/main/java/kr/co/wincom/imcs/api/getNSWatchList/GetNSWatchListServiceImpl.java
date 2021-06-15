package kr.co.wincom.imcs.api.getNSWatchList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.wincom.imcs.api.getNSViewInfo.GetNSViewInfoRequestVO;
import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.NosqlCacheType;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.handler.ImcsException;

@Service
public class GetNSWatchListServiceImpl implements GetNSWatchListService {
	private Log imcsLogger		= LogFactory.getLog("API_getNSWatchList");
	
	@Autowired
	private GetNSWatchListDao getNSWatchListDao;
	
	@Autowired
	private CommonService commonService;

//	public void getNSWatchList(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	@SuppressWarnings("unused")
	@Override
	public GetNSWatchListResultVO getNSWatchList(GetNSWatchListRequestVO paramVO){
//		this.getNSWatchList(paramVO.getSaId(), paramVO.getMacAddr(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getMacAddr(), paramVO.getPid());	
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		List<GetNSWatchListResponseVO> resultVO	= new ArrayList<GetNSWatchListResponseVO>();
		GetNSWatchListResultVO resultListVO	= new GetNSWatchListResultVO();

		String msg	= "";
		
		int nMainCnt = 0;
        int iResult  = 0;	// 성공여부 코드값. 0:성공, 1:오류

	    long tp_start = paramVO.getTp_start();
		long tp1, tp2 = 0;
		
		try{
			tp1	= System.currentTimeMillis();
			imcsLog.timeLog("init...", String.valueOf(tp1 - tp_start), methodName, methodLine); 
			
			//######################################################
			// 로직구현 (시작)
			//######################################################
			int nPageNo			= Integer.parseInt(StringUtil.nullToZero(paramVO.getPageNo()));
			int nPageCnt		= Integer.parseInt(StringUtil.nullToZero(paramVO.getPageCnt()));
			int nStartNo		= 0;
			int nEndNo			= 0;
			
			int nRqs_Type       = 0;
			int iTypeEnd        = 0;
			
			// 홈화면 이어보기 지면에서 노출일 경우 12개 까지만 정보 제공
			if (paramVO.getRqsFlag().equals("H") || paramVO.getRqsFlag().equals("U")) {
				nStartNo = 1;
				nEndNo = 12;
			}
			
			if (nPageNo == 0) {
				nStartNo = 1;
				nEndNo = nPageCnt;
			} else {
				nStartNo = (nPageNo - 1) * nPageCnt + 1;
				nEndNo = nStartNo + nPageCnt - 1;
			}
			
			paramVO.setStartNum(String.valueOf(nStartNo));
			paramVO.setEndNum(String.valueOf(nEndNo));
			
			//이미지 캐쉬 서버 정보 가져오기
			String img_poster_server = commonService.getImgReplaceUrl2("img_server", "getNSWatchList");
			String img_still_server = commonService.getImgReplaceUrl2("img_still_server", "getNSWatchList");
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("image url ...", String.valueOf(tp2 - tp1), methodName, methodLine);

		    // ------------------------------------------------------------------------//
		    // SQL - 001 : 가입자 정보 가져오기 검수 여부, 엔스크린 가입여부 가져오기
		    //-------------------------------------------------------------------------//

			String szTestSbc	= "";
			tp1	= System.currentTimeMillis();
			szTestSbc	= this.getTestSbc(paramVO);
			
			if (szTestSbc.equals("1")) {
				String resultHeader  = String.format("%s|%s|%d|%s|%s|%s|", 
						"0", "페어링된 가입자가 아닙니다", 0, 
						"", "", paramVO.getStbPairing());
				resultListVO.setResultHeader(resultHeader);
				resultListVO.setList(resultVO);
				return resultListVO;
			}
			else if (szTestSbc.equals("2")) {
				String resultHeader  = String.format("%s|%s|%d|%s|%s|%s|", 
						"1", "가입자 정보 가져오기 오류", 0, 
						"", "", paramVO.getStbPairing());
				resultListVO.setResultHeader(resultHeader);
				resultListVO.setList(resultVO);
				return resultListVO;
			}
			
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("검수 STB여부 조회", String.valueOf(tp2 - tp1), methodName, methodLine);					   
			
			//--------------------------------------------------
		    // 월정액 가입 목록 조회
		    // 엔스크린 IPTV 목록 조회일 경우 IPTV 가번의 가입 목록도 가져온다...
		    //--------------------------------------------------
						
			this.custsubscribeList(paramVO);
			
			tp1 = System.currentTimeMillis();
			imcsLog.timeLog("가입자 가입 상품 정보 조회", String.valueOf(tp1 - tp2), methodName, methodLine);
			
			
		    //--------------------------------------------------
		    // 월정액 가입 목록 조회
		    // 엔스크린 IPTV 목록 조회일 경우 IPTV 가번의 목록도 가져온다...
		    //--------------------------------------------------
			resultListVO.setiTotalCount(0);
			resultListVO = this.getSubscribeList(paramVO);
			
			//결과 헤더 구성
            //iResult, 성공여부 코드값 (0:성공, 1:실패)
			String resultHeader  = String.format("%s|%s|%d|%s|%s|%s|%s|%s|", 
					"0", "", resultListVO.getiTotalCount(), 
					img_poster_server, img_still_server, paramVO.getStbPairing(),
					paramVO.getStbSaId(), paramVO.getStbMac());
			resultListVO.setResultHeader(resultHeader);
			
//			resultListVO.setList(resultVO);
			
			//######################################################
			// 로직구현 (끝)
			//######################################################
			
	    	
	    	tp2	= System.currentTimeMillis();
			imcsLog.timeLog("시청목록 리스트 Fetch", String.valueOf(tp2 - tp1), methodName, methodLine); 
			
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
			msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID710) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
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
	public String getTestSbc(GetNSWatchListRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId 		= "nsvod210_001_20180601";
		String result_flag	= "0";
		
		int querySize		= 0;
		
		try {			
			HashMap<String, String> mTestInfo = getNSWatchListDao.getTestSbc(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;					
			
			paramVO.setConSaId(paramVO.getSaId());
			paramVO.setConMac(paramVO.getMacAddr());
			
			if (mTestInfo != null && !mTestInfo.isEmpty()) {
				querySize = 1;
				paramVO.setTestSbc(mTestInfo.get("TEST_SBC") == null ? "" : mTestInfo.get("TEST_SBC").toString());
				paramVO.setStbSaId(mTestInfo.get("STB_SA_ID") == null ? "" : mTestInfo.get("STB_SA_ID").toString());
				paramVO.setStbMac(mTestInfo.get("STB_MAC") == null ? "" : mTestInfo.get("STB_MAC").toString());
				paramVO.setStbPairing(mTestInfo.get("STB_PAIRING") == null ? "N" : mTestInfo.get("STB_PAIRING").toString());
				paramVO.setSysdate(mTestInfo.get("SYS_DATE") == null ? "" : mTestInfo.get("SYS_DATE").toString());
				
				if (paramVO.getNscList().equals("Y")) {
					if (!paramVO.getStbPairing().equals("Y") ) {
						result_flag = "1"; //페어링된 가입자가 아닙니다.
					}
					else
					{
						paramVO.setConSaId(paramVO.getStbSaId());
						paramVO.setConMac(paramVO.getStbMac());
					}
				} else if ( paramVO.getNscList().equals("A") ) {
					
					if (paramVO.getStbPairing().equals("Y") ) {
						paramVO.setConSaId2(paramVO.getStbSaId());
						paramVO.setConMac2(paramVO.getStbMac());
					} else {
						paramVO.setNscList("N");
						paramVO.setConSaId(paramVO.getSaId());
						paramVO.setConMac(paramVO.getMacAddr());
					}
					
				} else {
					paramVO.setConSaId(paramVO.getSaId());
					paramVO.setConMac(paramVO.getMacAddr());
				}
			}else{
				result_flag = "2"; //가입자 정보 가져오기 오류
			}
			
			try{
				//imcsLog.dbLog(ImcsConstants.NSAPI_PRO_ID710, sqlId, null, querySize, methodName, methodLine);
			}catch(Exception e){}
			
			//가입자 정보 설정
			String c_idx_sa = paramVO.getConSaId().substring(paramVO.getConSaId().length()-2, paramVO.getConSaId().length());
			int p_idx_sa = 0; 
			
			try {
				p_idx_sa = Integer.parseInt(c_idx_sa) % 33;
			} catch (NumberFormatException e) {
				p_idx_sa = 0;
			}
			
			paramVO.setIdxSa(p_idx_sa);
			
			if ( paramVO.getNscList().equals("A") && paramVO.getConSaId2().length() > 0) {
				String c_idx_sa2 = paramVO.getConSaId2().substring(paramVO.getConSaId2().length()-2, paramVO.getConSaId2().length());
				int p_idx_sa2 = 0; 
				
				try {
					p_idx_sa2 = Integer.parseInt(c_idx_sa2) % 33;
				} catch (NumberFormatException e) {
					p_idx_sa2 = 0;
				}
				
				paramVO.setIdxSa2(p_idx_sa2);
			}
			
			
			
		} catch (Exception e) {
			result_flag = "2"; //가입자 정보 가져오기 오류
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getMacAddr(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
		return result_flag;
	}
	/**
	 * 가입자 월정액 가입정보 조회
	 * @param paramVO
	 * @return String		TEST_SBC	
	 */
	public void custsubscribeList(GetNSWatchListRequestVO paramVO) throws Exception {
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		GetNSWatchListRequestVO custInfo = new GetNSWatchListRequestVO();
		List<String> nscCustProd = new ArrayList<String>();
		List<String> iptvCustProd = new ArrayList<String>();
		String msg = "";

		try
		{
			nscCustProd = getNSWatchListDao.getNscCustProd(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			
			paramVO.setHca_prod(nscCustProd);
			paramVO.setInscCustProdCount(nscCustProd.size());
			
			if( paramVO.getStbPairing().equals("Y"))
			{
				iptvCustProd = getNSWatchListDao.getIptvCustProd(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
				paramVO.setIptv_prod(iptvCustProd);
				paramVO.setIiptvCustProdCount(iptvCustProd.size());
			}
		}
		catch(Exception e)
		{
			msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID710) + "] msg[가입자 정보 가져오기 오류]" + " errCode[" + String.format("%-20s", e.getMessage() + "]");
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getMacAddr(), paramVO.getPid());	
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
	}	
	
	public GetNSWatchListResultVO getSubscribeList (GetNSWatchListRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		int querySize = 0;
		int iTotalCount = 0;
		//seriesCheck(int)
		
		List<GetNSWatchListResponseVO> listWatchInfo = new ArrayList<GetNSWatchListResponseVO>();
		List<GetNSWatchListResponseVO> returnVO = new ArrayList<GetNSWatchListResponseVO>();
		GetNSWatchListResultVO resultVO = new GetNSWatchListResultVO();
		GetNSWatchListResponseVO NextVO;
		String[] productArray = null;
		String[] nscArray = null;

		try {
			
			if (paramVO.getNscList().equals("N")) {
				listWatchInfo = getNSWatchListDao.getSubscribeList1(paramVO);
			} else if (paramVO.getNscList().equals("Y")) {
				listWatchInfo = getNSWatchListDao.getSubscribeList2(paramVO);
			} else if (paramVO.getNscList().equals("A")) {
				listWatchInfo = getNSWatchListDao.getSubscribeList3(paramVO);
			}
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			
			int iCount = 0;
			for (GetNSWatchListResponseVO item : listWatchInfo) {
				iCount++;
				int nscflag = 0;
				item.setResultType("LIST");
				item.setViewType("S");
				item.setDatafreeBuyYn("N");
				
				nscArray = item.getNscInfo().split(";");
				String nscYn = nscArray[0];
				item.setNscreenYn(nscArray[0]);
				
				String productType = "";
				if (nscYn.equals("Y")) {
					productArray = nscArray[1].split("/");
					
					//2021.05.11 Seamless 단방향서비스 2차
					if(item.getScreenType().equals("I")) {
						if(nscArray[2].equals("M")) {
							continue;
						}
					}
				}
				
				
		        if("D".equals(item.getVodType()))	
		        	item.setVodType("Y");
				else
					item.setVodType("N");
				
//				//2019.03.05 - 지상파 서비스 종료로 인해 KBS, SBS, MBC 컨텐츠 순차적으로 비노출 처리
//				if (paramVO.getSysdate().compareTo("20190307") >= 0 && item.getGenreSmall().equals("SBS")) continue;
//				if (paramVO.getSysdate().compareTo("20190311") >= 0 && item.getGenreSmall().equals("KBS")) continue;
//				if (paramVO.getSysdate().compareTo("20190315") >= 0 && item.getGenreSmall().equals("MBC")) continue;
				
		        //--------------------
		        // 홈화면 이어보기 호출시 19세 로맨스 제외 (성인물)
		        //--------------------
		        if ( paramVO.getRqsFlag().equals("H") || paramVO.getRqsFlag().equals("U"))
		        {
		            if (item.getPrInfo().equals("05") && item.getGenreMid().equals("성인")) continue;
		            if (item.getPrInfo().equals("06")) continue;
		            
		            if (item.getGenreLarge().equals("키즈") || item.getGenreLarge().equals("라이프")) continue;
		            
		            if (!"Y".equals(item.getSeriesYn())) {
		            	//시청완료 컨텐츠 continue
		            	if (item.getLinkTime().equals("0")) continue;
		            	if ( check_runtime(item) == 1 ) continue;
		            }
		        }
		        
		        //--------------------
		        // R등급 체크
		        //--------------------
		        if ( paramVO.getrGrade().equals("Y") )
		        {
		            if ( !item.getPrInfo().equals("06")) continue;
		        }
		        else if ( paramVO.getrGrade().equals("N") )
		        {
		            if ( item.getPrInfo().equals("06") ) continue;
		        }

		        //--------------------
		        // 동일 시리즈 여부 체크
		        //--------------------
		        if ( paramVO.getSeriesFlag().equals("Y") && item.getSeriesYn().equals("Y"))
		        {
		            if ( seriesCheck(item, paramVO) == 1) continue;
		        }
		        
		        //--------------------
		        // 엔스크린 체크
		        //--------------------
		        
		        /*this.checkNscreenInfo(item, paramVO);
		        
		        if ( item.getNscreenYn().equals("C") ) item.setNscreenYn(item.getCpNscreenYn());
		        if ( item.getProductType().equals("0") ) item.setNscreenYn("N");
		        if ( item.getNscreenYn().equals("Y") )
		        {
		            this.checkNscreen(item, paramVO);
		            
		        }*/
		        
		        //if ( paramVO.getNscList().equals("Y") && !item.getNscreenYn().equals("Y") ) continue;
		        
		        
		        
				item.setSubscribeYn("N");
				int buyflag = 0;
				if (item.getProductType().equals("0")) {
					item.setBuyYn("Y");
				} else {
					if (paramVO.getNscList().equals("Y") || (paramVO.getNscList().equals("A") && item.getScreenType().equals("I"))) {  //iptv 시청목록
							
						// -------------------------------------------
						// IPTV 가입/구매 체크 (엔스크린 컨텐츠)
						// -------------------------------------------
						if (!item.getBuyYn().equals("Y") && !item.getSubscribeYn().equals("Y") && paramVO.getStbPairing().equals("Y")) {
							iptvBuyCheckVO nscreen = this.iptvBuyCheck(item, paramVO);
							item.setNscreen(nscreen);
							if(nscreen.getBuyYn().equals("Y")) {
								item.setExpiredYn("N");
								buyflag++;  // 하는 경우 바로 넘어 간다
							}
						}
						
						
						// -------------------------------------------
						// IPTV 미가입/미구매시 비디오포털 가입/구매 체크 (엔스크린 컨텐츠)
						// -------------------------------------------
						if(buyflag != 1){
							for (int i = productArray.length - 1; i >= 0; i--) {
								productType = productArray[i];
								if (productType.equals("3") && nscflag == 0) {
									this.svodCheck(item, paramVO);
									// SVOD 가입해서 시청 권한이 있는 경우 앞에서 체크한 구매 관련 정보를 새로 설정한다
									if (item.getSubscribeYn().equals("Y")) {
										item.setProductType("3");
										item.setExpiredYn("N");
										item.setExpiredDate("");
										nscflag++;
									}
								}

								if ((productType.equals("1") || productType.equals("2")) && nscflag == 0) {
									this.buyCheck(item, paramVO);
									nscflag++;
								}
							}
						}
							
					} else if (paramVO.getNscList().equals("N") || (paramVO.getNscList().equals("A") && item.getScreenType().equals("N"))){ // 모바일 시청목록
						
						// -------------------------------------------
						// 비디오포털 가입/구매 체크 (엔스크린 컨텐츠)
						// -------------------------------------------
						if (item.getNscreenYn().equals("Y")) {
							if ( item.getSvodYn().equals("Y")  ) {
								this.svodCheck(item, paramVO);
								// SVOD 가입해서 시청 권한이 있는 경우 앞에서 체크한 구매 관련 정보를 새로 설정한다
								if (item.getSubscribeYn().equals("Y")) {
									item.setProductType("3");
									item.setExpiredYn("N");
									item.setExpiredDate("");
								}
							}

							if ((item.getProductType().equals("1") || item.getProductType().equals("2")) && !item.getSubscribeYn().equals("Y")) {
								this.buyCheck(item, paramVO);
							}
							
							// -------------------------------------------
							// 비디오포털 미구매시 IPTV 구매 체크 (엔스크린 컨텐츠)
							// -------------------------------------------
							if (!item.getBuyYn().equals("Y") && !item.getSubscribeYn().equals("Y") && paramVO.getStbPairing().equals("Y")) {
								iptvBuyCheckVO nscreen = this.iptvBuyCheck(item, paramVO);
								item.setNscreen(nscreen);
							}

						} else {
							
							if (item.getSvodYn().equals("Y") && !item.getBuyYn().equals("Y"))
								this.svodCheck(item, paramVO);
							// SVOD 가입해서 시청 권한이 있는 경우 앞에서 체크한 구매 관련 정보를 새로 설정한다
							if (item.getSubscribeYn().equals("Y")) {
								item.setProductType("3");
								item.setExpiredYn("N");
								item.setExpiredDate("");
							}
							if (item.getProductType().equals("1") || item.getProductType().equals("2") && !item.getSubscribeYn().equals("Y"))
								this.buyCheck(item, paramVO);

						}
					}
				}

				
				
				
				
		      /*  //----------------------
		        // 구매체크 (FVOD 아닌 경우)
		        //----------------------
		        item.setSubscribeYn("N");

		        if ( item.getProductType().equals("0") )
		        {
		        	item.setBuyYn("Y"); //여기가 문제인데..
		        }
		        else
		        {
		            if ( item.getProductType().equals("1") || item.getProductType().equals("2") ) this.buyCheck(item, paramVO);

		            if ( item.getSvodYn().equals("Y") && !item.getBuyYn().equals("Y") ) this.svodCheck(item, paramVO);

		            //SVOD 가입해서 시청 권한이 있는 경우 앞에서 체크한 구매 관련 정보를 새로 설정한다
		            if ( item.getSubscribeYn().equals("Y") )
		            {
		                item.setProductType("3");
		                item.setExpiredYn("N");
		                item.setExpiredDate("");
		            }
		        }*/
		        
		        
		        //-----------------------------------------------
		        // 구매만료 컨텐츠 제외 옵션
		        //-----------------------------------------------
		        // 구매만료된 컨텐츠이고, SVOD 가입 정보도 없을 경우 제외한다...
		        //-----------------------------------------------
		        if ( paramVO.getExpiredFlag().equals("Y") && item.getExpiredYn().equals("Y") && !item.getSubscribeYn().equals("Y") ) continue;
		        
		        //--------------------
		        // 다음 회차 정보 설정
		        //--------------------
		        if ((paramVO.getRqsFlag().equals("H") || paramVO.getRqsFlag().equals("U")) && paramVO.getSeriesFlag().equals("Y") 
		        		&& paramVO.getNextFlag().equals("Y") && item.getSeriesYn().equals("Y") && item.getScreenType().equals("N")) {
		        	
		        	NextVO = null;
		        	NextVO = new GetNSWatchListResponseVO();
		        	
		        	addNextSeriesResponseVO an = addNextSeries(item, paramVO);
		        	
		        	
		        	if (an!=null) {
		        		NextVO.setResultType("NEXT");
		        		NextVO.setProductType(an.getProductType());
		        		NextVO.setAlbumId(an.getAlbumId());
		        		NextVO.setAlbumName(an.getAlbumName());
		        		NextVO.setCategoryId(item.getCategoryId());
		        		NextVO.setSeriesYn(item.getSeriesYn());
		        		NextVO.setSeriesNo(an.getSeriesNo());
		        		NextVO.setSeriesDesc(an.getSeriesDesc());
		        		NextVO.setPrInfo(an.getPrInfo());
		        		NextVO.setViewType("S");
		        		NextVO.setRuntime(an.getRuntime());
		        		NextVO.setPosterP(an.getPosterP());
		        		NextVO.setStillFileName(an.getStillFileName());
		        		NextVO.setDatafreeBuyYn("N");
		        		
		        		String [] NextNscArray = NextVO.getNscInfo().split(";");
						String NextNscYn = NextNscArray[0];
						NextVO.setNscreenYn(NextNscYn);
		        		
		        		
		        		returnVO.add(NextVO);
		        	}
		        	else
		        	{
		        		if ( item.getLinkTime().equals("0") ) continue;
		            	if ( check_runtime(item) == 1 ) continue;
		        	}
		        	
		        }
		        
		        // CATEGORY_TYPE 을 구하기 위해서 kids_type 을 조회함
		        if(paramVO.getRqsFlag().equals("K") || paramVO.getRqsFlag().equals("B"))
		        {
		        	paramVO.setAlbumId(item.getAlbumId());
		        	
		        	String strKidsType = ""; 
		        			
		        	if(item.getScreenType().equals("N"))
		        	{
		        		strKidsType = this.getNSWatchListDao.getKidsType_1(paramVO);
		        	}
		        	else
		        	{
		        		strKidsType = this.getNSWatchListDao.getKidsType_2(paramVO);
		        	}
		        	
		        	// 키즈 카테고리 구분 정보 설정
		        	if("K".equals(strKidsType) || "B".equals(strKidsType))
		        	{
		        		item.setCategoryType(strKidsType);
		        	}
		        	
		        	// 키즈홈 호출인 경우 KIDS 시청목록이 아니면 제외
		        	// 키즈홈 책읽어주는 TV 호출인 경우 책읽어주기 컨텐츠 시청목록이 아니면 제외
		        	// 2020.08.05 - 아이들나라 시청 목록 제공시, 시청 여부까지 확인하지 않고, 아이들나라에 편성이 되어 있으면 정보 제공하는 것으로 기능 수정
		        	// 2020.08.05 - 참고로 RqsFlag는 현재 B는 호출하지 않음... 연동규격서에서 삭제하고 로직도 삭제해야 할 듯
//		        	if(paramVO.getRqsFlag().equals("K"))
//		        	{
//		        		if("K".equals(item.getCategoryType()) == false && "B".equals(item.getCategoryType()) == false)
//		        			continue;
//		        	}
//		        	else if(paramVO.getRqsFlag().equals("B"))
//		        	{
//		        		if("B".equals(item.getCategoryType()) == false)
//		        			continue;
//		        	}
		        }
		        
		        //2020.09.10 - C : 키즈 플레이어(단편재생) 이어보기 목록 (아이들나라 4.0)
		        //Case1. 시리즈에 걸려있는 콘텐츠 제외
		        //Case2. FVOD 콘텐츠는 노출
		        //Case3. FVOD 콘텐츠가 아닐 경우에는 구매/가입 했으면 노출 안 했으면 미노출
		        if(paramVO.getRqsFlagTmp().equals("C"))
		        {
		        	if(item.getSeriesContsYn().equals("N"))
		        	{
		        		if(!item.getProductType().equals("0"))
		        		{
		        			if (!item.getBuyYn().equals("Y") && !item.getSubscribeYn().equals("Y"))
							{
								// IPTV 권한 체크는 BuyYn으로만 체크하면 구매 or 가입했는지 알 수 있다..
//								if(!item.getNscreen().getBuyYn().equals("Y") && !item.getNscreen().getSubscYn().equals("Y"))
								if(item.getNscreen() == null || !item.getNscreen().getBuyYn().equals("Y"))
								{
									continue;
								}
							}
		        		}
		        	}
		        	else
		        	{
		        		continue;
		        	}
		        }
		        
		        //여기까지 수행했으면 전체 카운트 증가시킨다
		        iTotalCount ++;
		        
		        if ( iTotalCount < Integer.parseInt(paramVO.getStartNum()) || iTotalCount > Integer.parseInt(paramVO.getEndNum()) ) continue;
		        
		       /* //-------------------------------------------
		        // 비디오포털 미구매시 IPTV 구매 체크 (엔스크린 컨텐츠)
		        //-------------------------------------------
		        if ( item.getNscreenYn().equals("Y") && !item.getBuyYn().equals("Y") && !item.getSubscribeYn().equals("Y") )
		        {
		        	iptvBuyCheckVO nscreen = this.iptvBuyCheck(item, paramVO);
		        	item.setNscreen(nscreen);
		        }*/
		        
		        returnVO.add(item);
				
		        if ((paramVO.getRqsFlag().equals("H") || paramVO.getRqsFlag().equals("U")) && iTotalCount == 12) {
		        	break;
		        }
			}
			
			if (returnVO != null && !returnVO.isEmpty()) {
				querySize	= returnVO.size();
			}
			
			try{
				//imcsLog.dbLog2(ImcsConstants.NSAPI_PRO_ID710, "", null, querySize, methodName, methodLine);
			}catch(Exception e){}
			
			resultVO.setiTotalCount(iTotalCount);
			resultVO.setList(returnVO);
			
		} catch (Exception e) {
			//imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID710, "", null, "series_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			String msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID710) + "] msg[시청 목록 조회 실패]" + " errCode[" + String.format("%-20s", e.getMessage() + "]");
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getMacAddr(), paramVO.getPid());	
			imcsLog.serviceLog(msg, methodName, methodLine);			
		}

		return resultVO;
	}
	
	/*-----------------------------------------------------------------
    seriesCheck(int)
	------------------------------------------------------------------*/
	private int seriesCheck(GetNSWatchListResponseVO resVO, GetNSWatchListRequestVO paramVO)
	{
	    int i = 0;
	    int iCheck = 0;
	
	    for(i = 0; i < paramVO.getHca_ser().size() ; i++)
	    {
	        if ( paramVO.getHca_ser().get(i).equals(resVO.getCategoryId())) iCheck = 1;
	    }
	
	    if ( iCheck == 0 )
	    {
	        paramVO.setHca_ser(resVO.getCategoryId());
	    }
	
	    return iCheck;
	}
	
	/**
	 * 구매체크
	 * @param paramVO
	 * @return buy_yn, buy_date, expired_date
	 */
	public void buyCheck(GetNSWatchListResponseVO tempVO, GetNSWatchListRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		int querySize		= 0;

		try {			
			tempVO.setChkSaId(paramVO.getSaId());
			tempVO.setChkMacAddr(paramVO.getMacAddr());
			HashMap<String, String> mBuyCheckInfo = getNSWatchListDao.getBuyCheck(tempVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			
			if (mBuyCheckInfo != null && !mBuyCheckInfo.isEmpty()) {
				String buy_yn = mBuyCheckInfo.get("BUY_YN").toString();
				String buy_date = mBuyCheckInfo.get("BUY_DATE") == null ? "" : mBuyCheckInfo.get("BUY_DATE").toString();
				String expired_date = mBuyCheckInfo.get("EXPIRED_DATE") == null ? "" : mBuyCheckInfo.get("EXPIRED_DATE").toString();
				
				tempVO.setBuyYn(buy_yn);
				tempVO.setBuyDate(buy_date);
				tempVO.setExpiredDate(expired_date);
				
			    //구매내역이 존재할 경우 만료여부 설정
			    if ( tempVO.getExpiredDate().compareTo("0") > 0 )
			    {
			        if ( tempVO.getBuyYn().equals("Y") )
			            tempVO.setExpiredYn("N");
			        else
			            tempVO.setExpiredYn("Y");
			    }
			    
			    //데이터프리 구매체크
			    tempVO.setTempId(tempVO.getAlbumId() + "_D");
			    String resultDataFree = getNSWatchListDao.getDatafreeCheck(tempVO);
			    if (resultDataFree==null) {
			    	tempVO.setDatafreeBuyYn("N");
			    } else {
			    	tempVO.setDatafreeBuyYn("Y");
			    }
			}
			
			try{
				//imcsLog.dbLog(ImcsConstants.NSAPI_PRO_ID710, "", null, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getMacAddr(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
	}
	
	/**
	 * SVOD 가입 체크
	 * @param paramVO
	 * @return buy_yn, buy_date, expired_date
	 */
	public void svodCheck(GetNSWatchListResponseVO tempVO, GetNSWatchListRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		int querySize		= 0;
	    int iCheck = 0;
	    int ii = 0;
	    int jj = 0;

		try {			
			List<String> svodCheck = getNSWatchListDao.getSvodCheck(tempVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;

			if (svodCheck != null && !svodCheck.isEmpty()) {
		        ii = 0;
		        jj = 0;
		        for(ii = 0 ; ii < svodCheck.size() ; ii++ )
		        {
			        for ( jj = 0; jj < paramVO.getInscCustProdCount(); jj++ )
			        {
			            if ( paramVO.getHca_prod().get(jj).equals(svodCheck.get(ii)) == true)
			            {
			                iCheck = 1;
			                break;
			            }
			        }
		        }
			}
			

		    if ( iCheck > 0 )
		        tempVO.setSubscribeYn("Y");
		    else
		        tempVO.setSubscribeYn("N");
			
			try{
				//imcsLog.dbLog(ImcsConstants.NSAPI_PRO_ID710, "", null, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getMacAddr(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
	}
	
	/**
	 * 비디오포털 미구매시 IPTV 구매 체크 (엔스크린 컨텐츠)
	 * @param paramVO
	 * @return buy_date, expired_date, chk_prod_id
	 */
	public iptvBuyCheckVO iptvBuyCheck(GetNSWatchListResponseVO tempVO, GetNSWatchListRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		int querySize = 0;
	    int iCheck    = 0;
	    int ii        = 0;
	    int jj        = 0;
	    
	    iptvBuyCheckVO retVO = new iptvBuyCheckVO();

		try {
		    //----------------------------------------------------------------------
		    // 구매한 컨텐츠가 아닌 경우 가입 체크를 위해 미리 상품 정보를 가져온다
		    //----------------------------------------------------------------------
		    // 1. 비디오 포털 가번으로 가입한 모든 상품 조회 (iptvSubscList)
		    // 2. 해당 컨텐츠가 포함된 모든 상품 조회 (번들 포함)
		    // 3. 1-2번 비교 (매칭되는 게 하나라도 있으면 OK)
		    //----------------------------------------------------------------------

		    // ---------------------------------------------------------------------
		    // SQL - 950 : IPTV 가입체크 (해당앨범의 SVOD 상품 가져오기)
		    //----------------------------------------------------------------------
			
			if(paramVO.getNscList().equals("A")) {
				if(tempVO.getScreenType().equals("I")) {
					tempVO.setChkStbSaId(paramVO.getConSaId2());
				} else {
					tempVO.setChkStbSaId(paramVO.getSaId());
				}
				
			} else {
				tempVO.setChkStbSaId(paramVO.getStbSaId());
			}
			
			if(tempVO.getBookYn().equals("Y")) {
				int kidProd =  this.kidProductCd(tempVO, paramVO);
				if (kidProd == 1) {
					iCheck = 1;
				}
			} else {
				List<String> chk_prod_id = getNSWatchListDao.getIptvSvodCheck(tempVO);
				
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			    if (chk_prod_id!=null) {
			        ii = 0;
			        jj = 0;
			        for(ii = 0 ; ii < chk_prod_id.size() ; ii++ )
			        {
				        for ( jj = 0; jj < paramVO.getIiptvCustProdCount(); jj++ )
				        {
				            if ( paramVO.getIptv_prod().get(jj).equals(chk_prod_id.get(ii)) == true)
				            {
				                iCheck = 1;
				                break;
				            }
				        }
			        }
			    }
			}
			
		    
		    if(iCheck == 1)
		    {
		    	retVO.setBuyYn("Y");
		    	retVO.setSubscYn("Y");
		    	
		    	tempVO.setProductType("3");
		    	return retVO;
		    }
		    else
		    {
		    	retVO.setSubscYn("N");
		    }
		    
		    
		    
		    /////////////
		    
		    if(iCheck != 1)
		    {
			    HashMap<String, String> mIptvBuyCheck = getNSWatchListDao.getIptvBuyCheck(tempVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
				if (mIptvBuyCheck != null && !mIptvBuyCheck.isEmpty()) {
					String icount = mIptvBuyCheck.get("ICOUNT").toString();
					String buy_date = mIptvBuyCheck.get("BUY_DATE") == null ? "" : mIptvBuyCheck.get("BUY_DATE").toString();
					String expired_date = mIptvBuyCheck.get("EXPIRED_DATE") == null ? "" : mIptvBuyCheck.get("EXPIRED_DATE").toString();
					
					if (Integer.parseInt(icount)>0) {
						retVO.setBuyYn("Y");
						retVO.setBuyDate(buy_date);
						retVO.setExpiredDate(expired_date);
						
						//IPTV 시청 권한이라도 볼 수 있는 상품타입을 설정해 준다...
						tempVO.setProductType("1");
						
						return retVO;
					} else {
						retVO.setBuyYn("N");
						retVO.setBuyDate("");
						retVO.setExpiredDate("");
					}
				}
		    }
			
		    
		    
		    
		    
		    
			try{
				//imcsLog.dbLog(ImcsConstants.NSAPI_PRO_ID710, "", null, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getMacAddr(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
		return retVO;
	}
	
	public addNextSeriesResponseVO addNextSeries(GetNSWatchListResponseVO tempVO, GetNSWatchListRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
	    addNextSeriesResponseVO ansr = null;

		try {			
			tempVO.setTestSbc(paramVO.getTestSbc());
			
			ansr = getNSWatchListDao.addNextSeries(tempVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			
			try{
				//imcsLog.dbLog(ImcsConstants.NSAPI_PRO_ID710, "", null, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getMacAddr(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
		return ansr;
	}

	/*-----------------------------------------------------------------
    check_runtime
------------------------------------------------------------------*/
	public int check_runtime(GetNSWatchListResponseVO tempVO)
	{
	    String runtime = "";
	    String hh = "";
	    String mm = "";
	    String ss = "";
	
	    int iRet = 0;
	    int iLinkTime = 0;
	    double iContTime;
	    double icheck;
	
	    /* 원래 DB에서 가져오던 것을 계산 로직으로 변경함 --------------------/
	    EXEC SQL
	    SELECT case when (:iruntime / (substr(:c_temp_runtime,1,2)*60*60 + substr(:c_temp_runtime,3,2)*60 + substr(:c_temp_runtime,5,2))*100) < 98 then 'Y' else 'N' end
	      INTO :c_chk_runtime
	      FROM dual
	    ;
	
	    printf("\nDB -- c_chk_runtime=[%s] : iruntime[%d]/runtime[%s]\n",c_chk_runtime,iruntime,c_temp_runtime);
	    /--------------------------------------------------------------------*/
	    runtime = tempVO.getRuntime();
	    hh = tempVO.getRuntime().substring(0, 2);
	    mm = tempVO.getRuntime().substring(2, 4);
	    ss = tempVO.getRuntime().substring(4, 6);
	
	    iLinkTime = Integer.parseInt(tempVO.getLinkTime());
	    iContTime = Integer.parseInt(hh)*60*60 + Integer.parseInt(mm)*60 + Integer.parseInt(ss);
	    icheck = (iLinkTime/iContTime)*100;
	
	    if (icheck >= 98) iRet = 1;
	
	    return iRet;
	
	    //printf("\nC+ -- c_chk_runtime=[%s] icheck[%.2f] : iruntime[%d]/iContTime[%.2f|%s]\n", c_chk_runtime, icheck, iruntime, iContTime, c_temp_runtime);
	}
	

	/**
	 * 엔스크린(NSCREEN) 키즈 가입 여부 체크
	 * @param paramVO
	 * @throws Exception
	 */
	private int kidProductCd(GetNSWatchListResponseVO tempVO, GetNSWatchListRequestVO paramVO) throws Exception
	{
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		int kidProduct = 0;
		String szMsg = "";
		
		try
		{
			kidProduct = getNSWatchListDao.kidProductCd(tempVO);
			
		}
		catch(Exception e)
		{
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
		return kidProduct;
	}
}
