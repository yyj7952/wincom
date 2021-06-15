package kr.co.wincom.imcs.api.getNSContStat;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.DateUtil;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.ComCpnVO;
import kr.co.wincom.imcs.common.vo.ComDataFreeVO;
import kr.co.wincom.imcs.common.vo.ComSvodVO;
import kr.co.wincom.imcs.handler.ImcsException;
import kr.co.wincom.imcs.common.NosqlCacheType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class GetNSContStatServiceImpl implements GetNSContStatService {
	private Log imcsLogger		= LogFactory.getLog("API_getNSContStat");
	
	@Autowired
	private GetNSContStatDao getNSContStatDao;
	
	@Autowired
	private CommonService commonService;
	
//	public void getNSContStat(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	/**
	 * @author HONG
	 * @since 2016-07-12
	 * 컨텐츠 타입과 구매여부, 구매시간, 구매타입 값을 리턴 (lgvod997.pc)
	 */
	@Override
	public GetNSContStatResultVO getNSContStat(GetNSContStatRequestVO paramVO)	{
//		this.getNSContStat(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		List<GetNSContStatResponseVO> resultVO	= new ArrayList<GetNSContStatResponseVO>();
		GetNSContStatResponseVO tempVO			= new GetNSContStatResponseVO();
		GetNSContStatResultVO	resultListVO	= new GetNSContStatResultVO();
		
		
		long tp1			= 0;		// timePoint 1
		long tp2			= 0;		// timePoint 2
		
		String msg			= "";
		int nDataChk		= 0; 		//2017.08.16 엔스크린(NSCREEN) 구매여부 체크
		
		ComCpnVO cpnInfoVO = new ComCpnVO();			
				
		try {
			// 서버IP정보 조회
			tp1 = System.currentTimeMillis();
			
			String szCurrentDate = "";
			
			try {
				szCurrentDate = commonService.getSysdate();
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			} catch (Exception e) {
				Date today = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMDD");
				szCurrentDate = sdf.format(today);
			}
			
			paramVO.setCurrentDate(szCurrentDate);
			
			
			// Guest 계정의 경우 처리
			if("M20110725000".equals(paramVO.getSaId())) {
				paramVO.setmProdId("31200");
				try {
					resultVO	= this.getPPMInfo(paramVO, false);
					if(resultVO != null){
						tempVO		= resultVO.get(0);
						
						tp2 = System.currentTimeMillis();
						imcsLog.timeLog("컨텐츠타입, 금액(인앱), 지상파여부 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
						
						String szBuyMessage	= "";
						
						if("1".equals(paramVO.getShortYn()) && "1".equals(paramVO.getPkgYn()) && "1".equals(paramVO.getSvodYn()))
							szBuyMessage	= "해당 프로그램은 단편/시리즈 구매 및\\n월정액 가입이 가능한 상품입니다.\\n구매 또는 가입하실 상품을 선택하세요";
						else if("1".equals(paramVO.getShortYn()) && "1".equals(paramVO.getPkgYn()) && !"1".equals(paramVO.getSvodYn()))
							szBuyMessage	= "해당 프로그램은 단편/시리즈\\n구매가 가능한 상품입니다.\\n구매하실 상품을 선택하세요";
						else if("1".equals(paramVO.getShortYn()) && !"1".equals(paramVO.getPkgYn()) && "1".equals(paramVO.getSvodYn()))
							szBuyMessage	= "해당 프로그램은 단편구매 및\\n월정액 가입이 가능한 상품입니다.\\n구매 또는 가입하실 상품을 선택하세요.";
						else if(!"1".equals(paramVO.getShortYn()) && "1".equals(paramVO.getPkgYn()) && "1".equals(paramVO.getSvodYn()))
							szBuyMessage	= "해당 프로그램은 시리즈 구매 및\\n월정액 가입이 가능한 상품입니다.\\n구매 또는 가입하실 상품을 선택하세요.";
						
						tempVO.setBuyText(szBuyMessage);
				
				        tp1 = System.currentTimeMillis();
				        imcsLog.timeLog("가입자 SVOD 정보 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
	
				
				        //프리미엄 여부, 선물여부 는 'N'으로 Default처리
				        tempVO.setPremiumYn("N");
				        tempVO.setPresentYn("N");
				        tempVO.setContsId(paramVO.getAlbumId());
				        tempVO.setDatafreeBuyYn("N");
				        tempVO.setDatafreePrice("0");
				        tempVO.setInappPrice("");
				        tempVO.setInappProdId("");
				        tempVO.setDatafreeInappPrice("");
				        tempVO.setDatafreeInappProdId("");
				        tempVO.setPpvDatafreeInappPrice("");
				        tempVO.setPpvDatafreeInappProdId("");
				        
				        tempVO.setFavorYn("N");
				        
				        // 2017.08.16 엔스크린(NSCREEN) 페어링 가입자 구매/가입 여부 전달
				        tempVO.setnScreenYn("N");
				        tempVO.setnBuyYn("N");
				        tempVO.setnSubscriptionYn("N");
				        tempVO.setSurtaxRate(Integer.toString(commonService.getSurtaxRate()));

			        	resultVO.set(0, tempVO);
			        }
			        
			        // sprintf(tmpsndbuf, "%s\f%s\f%s\f%s\f%s\f", tmpsndbuf, tmpsndbuf_stp, tmpsndbuf_cpn, tmpsndbuf_cpn_use, tmpsndbuf_cpn_g);
				} catch(Exception e) {
					//result_set = FAILURE;
				}
				
				imcsLog.serviceLog("[Guest 계정입니다.]", methodName, methodLine);
			}			
			else
			{
				String szMProdId   ="";
				GetNSContStatResponseVO mProdIdresultVO	=  new GetNSContStatResponseVO();
				mProdIdresultVO = this.getMProdId(paramVO); //주상품가입여부조회
				if (mProdIdresultVO != null ) {
					szMProdId = mProdIdresultVO.getmProdId();
					paramVO.setmProdId(mProdIdresultVO.getmProdId());
					paramVO.setTestSbc(mProdIdresultVO.getTestSbc());
				}
				
				//tempVO = new GetNSContStatResponseVO();
				//resultVO = new ArrayList<GetNSContStatResponseVO>();
				paramVO.setCustomUflix("0");
				
				if("".equals(szMProdId)){ // 주상품 미가입에 대한 Default 응답 설정
					//sprintf(tmpsndbuf, "CON|%s|1||1||1|48|0|0|0|||0|1||||||||||||||||N|\f\f\f\f\f", rd1.c_album_id  );
					tempVO.setResultType("CON");
					tempVO.setContsType("1");
					tempVO.setBuyYn("1");
					tempVO.setBillType("1");
					tempVO.setMaxViewingLen("48");
					tempVO.setSalePrice("0");
					tempVO.setEventValue("0");
					tempVO.setTerrYn("0");
					tempVO.setPrice("0");
					tempVO.setSubsYn("1");
					tempVO.setPremiumYn("N");
				} else {
					paramVO.setUflixBuyYn("1");			// 유플릭스 가입 여부 (default : 미가입)
					paramVO.setSvodProdBuyYn("1");		// 일반상품 구매 여부
					paramVO.setPremiumYn("N");			// 프리미엄 편성 여부
					
					paramVO.setmProdId(szMProdId);
					
					// 유플릭스 가입 여부 조회
					paramVO.setUflixBuyYn(this.getUflixBuyChk(paramVO)); // 유플릭스 가입여부 조회 시 <가입상태(0), 미가입상태(1), 모바일이 아닌 TV쪽 유플릭스 가입시(2)
					
					// 프리미엄 편성 여부 조회	(0건 또는 실패 시 모두 FAILURE)
					boolean bPremiumYn		= false;
					bPremiumYn		= this.getPremiumYn(paramVO);
					
					if(paramVO.getFreeFlag().equals("N") && !bPremiumYn) { 
						// result_set = FAILURE;
					}
					
					// 상품(프리미엄/일반 ) 가입여부
					boolean bPPMYn		= false;
					bPPMYn = this.getPPMYn(paramVO);
					
					if(paramVO.getFreeFlag().equals("N") && !bPPMYn) { 
						// result_set = FAILURE;
					}
					
	               String szPresentYn      = "N";
	               String szPresentDate   = "";
	               String szExpiredDate   = "";
	               
	               //2018.05.16 - TV앱 사용자의 경우 선물 내역을 체크하지 않는다(응답값은 받은 선물내역이 없는 경우와 같이 준다.). rd1.c_free_flag == 'Y' 이면 TV 앱 사용자
	               if(paramVO.getFreeFlag().equals("N") ) 
	               {                   
	            	   // 받은 선물 존재 여부 체크      (SQL EXCEPTION일경우만 FAILURE)
	            	   HashMap<String, Object>   presentInfoVO = new HashMap<String, Object>();
	            	   Integer nPresentCnt   = 0;

	            	   try {
	            		   presentInfoVO   = getNSContStatDao.getPresentInfo(paramVO);

	            		   nPresentCnt = (Integer) presentInfoVO.get("PRESENT_CNT");

	            		   if(nPresentCnt > 0)
	            		   {
	            			   szPresentYn   = "Y";
	            			   szPresentDate   = StringUtil.nullToSpace((String) presentInfoVO.get("PRESENT_DATE"));
	            			   szExpiredDate   = StringUtil.nullToSpace((String) presentInfoVO.get("EXPIRED_DATE"));
	            		   }

	            	   } catch (Exception e) {
	            		   //		                     imcsLog.failLog(ImcsConstants.API_PRO_ID997, "", null, "buy_info1:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
	            	   }
	               }
					
					
					// 진정한 망 로직의 시작 먀ㅏ너에ㅑ저ㅜ럊버ㅜ레ㅑㅓ제어레머야ㅔ먼에
					// 2018.05.28 - 엔스크린2차 : 엔스크린 Default는 N에서 CP속성(C)으로 수정
					resultVO	= this.getPPMInfo(paramVO, bPPMYn);		// (0건 또는 실패 시 모두 FAILURE)
					
					if(resultVO != null){
						tempVO		= resultVO.get(0);
						
						//통계 로그용
						resultListVO.setContentsName(tempVO.getContsNameSt());
						resultListVO.setProductId(tempVO.getSubsProdId());
						resultListVO.setProductName(tempVO.getSubsProdName());
						resultListVO.setProductPrice(tempVO.getSubsProdPrice());
						
						if("0".equals(tempVO.getContsType()))
							resultListVO.setBuyingType("FVOD");
						else if("1".equals(tempVO.getContsType()))
							resultListVO.setBuyingType("PPV");
						else if("2".equals(tempVO.getContsType()))
							resultListVO.setBuyingType("PVOD");
						else if("3".equals(tempVO.getContsType()))
							resultListVO.setBuyingType("SVOD");
						else if("7".equals(tempVO.getContsType()))
							resultListVO.setBuyingType("PPM");
												
						tempVO.setPresentYn(szPresentYn);
						tempVO.setPresentDate(szPresentDate);
						tempVO.setpExpiredDate(szExpiredDate);
						
						tempVO.setPremiumYn(paramVO.getPremiumYn());
						//tempVO.setUflixProdYn(paramVO.getUflixBuyYn()); 왜 넣어주는지 모르겠음
						
						tp2 = System.currentTimeMillis();
						imcsLog.timeLog("컨텐츠타입, 금액, 지상파여부 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
						
						//2018.12.07 - VR앱 : VR앱으로 조회 하는 경우 스탬프/쿠폰 정보를 조회하지 않는다.
						if(!(paramVO.getAppType().equals("") || paramVO.getAppType() == null) && !paramVO.getAppType().substring(0, 1).equals("E"))
						{
							cpnInfoVO = this.getCpnInfo(paramVO);
						}
						
						tempVO.setFavorYn("N");
						if("Y".equals(paramVO.getFxTypeTemp())){
							
							tp1 = System.currentTimeMillis();
							/* 찜한 컨텐츠 여부 확인	*/
							if(this.chkFavorInfo(paramVO)){
								tempVO.setFavorYn("Y");
							}
							tp2 = System.currentTimeMillis();
							imcsLog.timeLog("찜목록 여부 조회 완료", String.valueOf(tp2 - tp1), methodName, methodLine);
						}
						
						// 2017.08.16 엔스크린(NSCREEN) - 엔스크린 기능 제공 단말 및 엔스크린 컨텐츠일 경우 페어링된 가입자의 구매/가입 여부를 확인한다.
						tempVO.setnBuyYn("N");
						tempVO.setnSubscriptionYn("N");						
						if(paramVO.getFirmFlag().equals("P") && tempVO.getnScreenYn().equals("Y"))
						{
							// Seamless 단방향
							if(tempVO.getnScreenType().equals("T") || tempVO.getnScreenType().equals("A")) {
								List<HashMap> pairList = this.getNScreenPairingInfo(paramVO);
								
								if(pairList.size() == 1)
								{
									tempVO.setnSaId((String)pairList.get(0).get("STB_SA_ID"));
									tempVO.setnStbMac((String)pairList.get(0).get("STB_MAC"));
									paramVO.setnSaId(tempVO.getnSaId());
									paramVO.setnStbMac(tempVO.getnStbMac());
									
									// FVOD컨텐츠가 아니고 모바일 가입자로 구매
									// or 가입이 없는 경우에만 페어링된 가입자의 구매/가입 여부를 확인한다. (+ 페어링된 가입자도 같이 확인)
									if(tempVO.getContsType().indexOf("0") == -1
											&& tempVO.getBuyYn().indexOf("0") == -1
											&& paramVO.getSvodProdBuyYn().equals("1"))
									{
										// 20170817 엔스크린(NSCREEN) 구매 여부 체크
										HashMap<String, String> hm = new HashMap<String, String>();
										
										// 2020.01.14 - IPTV 상품이 1 / 2이면 구매 여부 확인을 한다.
										if(tempVO.getnScreenProd().indexOf("1") >= 0 || tempVO.getnScreenProd().indexOf("2") >= 0)
										{
											hm = this.nScreenBuyChk(paramVO);
											
											nDataChk = Integer.parseInt(hm.get("DATA_CHK"));
										}
										
										if(nDataChk > 0) // 엔스크린 구매가 있으면
										{
											tempVO.setnBuyYn("Y");
											tempVO.setnBuyDate(hm.get("N_BUY_DATE"));
											tempVO.setnExpiredDate(hm.get("N_EXPIRED_DATE"));
										}
										else
										{
											tempVO.setnBuyYn("N");
											tempVO.setnBuyDate("");
											tempVO.setnExpiredDate("");
											
											////////////////////
											// 20170817 엔스크린(NSCREEN) 가입 여부 체크
											// 2020.01.14 - IPTV 상품이 3이면 가입 여부를 확인한다.
											if(tempVO.getnScreenProd().indexOf("3") >= 0)
											{
												
												String nSubscriptionYn = "N";
												int chkCate = this.chkCategory(paramVO);
												if(chkCate == 1) {
													nSubscriptionYn = this.kidProductCd(paramVO);
												} else {
													List<String> productCdList = this.nscreenSubscriptionChkProductCdList(paramVO);
													nSubscriptionYn = this.nscreenSubscriptionChk(productCdList, paramVO);
												}
												
												// nScreen(STB) 가입자 컨텐츠 가입 여부
												tempVO.setnSubscriptionYn(nSubscriptionYn);											
											}
										}
									}
									else
									{
										if(tempVO.getContsType().indexOf("0") > -1)
											msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID997) + "] msg[FVOD is not auth(pairing)]";
										else if(tempVO.getBuyYn().indexOf("0") > -1)
											msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID997) + "] msg[BUY is not auth(pairing)]";
										else
											msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID997) + "] msg[SUBSCRIPTION is mobile auth]";
										
										imcsLog.serviceLog(msg, methodName, methodLine);
									}
								}
								else
								{
									msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID997) + "] msg[non pairing (pairing cnt:" + pairList.size() + ")]"; 
									
									imcsLog.serviceLog(msg, methodName, methodLine);
								}
							}
						}
						
		                String szBuyMessage	= "";
						
						if("1".equals(paramVO.getShortYn()) && "1".equals(paramVO.getPkgYn()) && "1".equals(paramVO.getSvodYn()))
							szBuyMessage	= "해당 프로그램은 단편/시리즈 구매 및\\n월정액 가입이 가능한 상품입니다.\\n구매 또는 가입하실 상품을 선택하세요";
						else if("1".equals(paramVO.getShortYn()) && "1".equals(paramVO.getPkgYn()) && !"1".equals(paramVO.getSvodYn()))
							szBuyMessage	= "해당 프로그램은 단편/시리즈\\n구매가 가능한 상품입니다.\\n구매하실 상품을 선택하세요";
						else if("1".equals(paramVO.getShortYn()) && !"1".equals(paramVO.getPkgYn()) && "1".equals(paramVO.getSvodYn()))
							szBuyMessage	= "해당 프로그램은 단편구매 및\\n월정액 가입이 가능한 상품입니다.\\n구매 또는 가입하실 상품을 선택하세요.";
						else if(!"1".equals(paramVO.getShortYn()) && "1".equals(paramVO.getPkgYn()) && "1".equals(paramVO.getSvodYn()))
							szBuyMessage	= "해당 프로그램은 시리즈 구매 및\\n월정액 가입이 가능한 상품입니다.\\n구매 또는 가입하실 상품을 선택하세요.";

						
						tempVO.setBuyText(szBuyMessage);
					}
					
					
				}
				
				tempVO.setContsId(paramVO.getAlbumId());
				tempVO.setSubsYn(paramVO.getSvodProdBuyYn());				// 일반상품 구매 여부
				tempVO.setContsId(paramVO.getAlbumId());
				
				//임시				
				/*if("M".equals(paramVO.getFxType())){
					tempVO.setDatafreePrice("0");
				}else{
					tempVO.setDatafreeBuyYn("N");
					tempVO.setDatafreePrice("1000");
					tempVO.setInappPrice("");
				}		        		        
		        tempVO.setDatafreeInappPrice("");
		        tempVO.setPpvDatafreeInappPrice("");
		        tempVO.setFavorYn("N");*/
		        //여기까지 임시
				if(paramVO.getFreeFlag().equals("Y") && tempVO.getContsType().equals("0"))
				{
					tempVO.setBuyingDate(szCurrentDate);
				}
				
				if(paramVO.getFreeFlag().equals("Y") && !tempVO.getContsType().equals("0"))
				{
					msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID997) + "] msg[FreeFlag = Y 일 때에는 FVOD만 시청 가능합니다.]"; 
					
					imcsLog.serviceLog(msg, methodName, methodLine);
					throw new ImcsException();
				}
			
				// 2020.11.09 - Seamless 단방향
				if(tempVO.getnScreenYn().equals("Y") && tempVO.getnSubscriptionYn().equals("N") && tempVO.getnBuyYn().equals("N")) {
					if(tempVO.getnScreenType().equals("T")) {
						tempVO.setnScreenYn("N");
					}
				}
				// 2020.03.18 - 모바일 아이들나라 - 부가세 정보 가져오기.
				// (상세 진입하지 않고, 구매하는 Scene이 있다고 하여, 기존 getNSContInfo / getNSSeriesList 에서 주던 부가세 요율 정보를 getNSContStat에서도 제공한다.)
				tempVO.setSurtaxRate(Integer.toString(commonService.getSurtaxRate()));
				
				if(resultVO != null && !resultVO.isEmpty()){
					resultVO.set(0, tempVO);
				}else{
					resultVO.add(tempVO);
				}
					
			}			
			
			//if(resultVO != null){
				resultListVO.setList(resultVO);
			//}
			
			resultListVO.setCpnInfo(cpnInfoVO);
			
			
		} catch(ImcsException ie) {
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			imcsLog.errorLog(methodName + "-E",ie.getClass().getName()+":"+ie.getMessage());

			// TLO Log 를 Controller Layer 로 전달하기 위해 Setting 한다. (TLO Log 세부값은 finally 문에서 설정된다.)
			throw new ImcsException();
		} catch(Exception e) {
			isLastProcess	= ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			
			// TLO Log 를 Controller Layer 로 전달하기 위해 ImcsException 으로 변환한다. (TLO Log 세부값은 finally 문에서 설정된다.)
			throw new ImcsException();
		} finally{
			resultListVO.setResultCode(paramVO.getResultCode());
			
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID997) + "] result[" + resultListVO.toString() + "]"; 
			
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID997) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultListVO;
	}
	
	
	
	/**
	 * 
	 */
	public ComCpnVO getCpnInfo(GetNSContStatRequestVO paramVO) {
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		//String sqlId = "lgvod997_f01_20160421_002";
		String szMsg = "";
		
		
		ComCpnVO cpnInfoVO = new ComCpnVO();
		ComCpnVO cpnInfoVO2 = new ComCpnVO();
		long tp1, tp2	= 0;
		
		tp1 = System.currentTimeMillis();
		try {
			szMsg	= " START smartux info : category_id=[" + paramVO.getCatId() + "], album_id=[" + paramVO.getAlbumId() + "], screen_type[" + paramVO.getScreenStr() + "]"
					+ ", product[" + paramVO.getProdType() + "], price[" + paramVO.getSuggestedPrice() + "], genre[" + paramVO.getGenreInfo() + "], pkg[" + paramVO.getPkgYn() + "]";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			// 발급 가능 쿠폰 정보 조회 
			tp1 = System.currentTimeMillis();
			try {
				cpnInfoVO = getNSContStatDao.getCpnInfo(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;							
				
				if(cpnInfoVO == null || cpnInfoVO.getCpnInfo() == null) {
					//imcsLog.failLog(ImcsConstants.API_PRO_ID997, "", null, "coupon(mims)_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
				} else {
					cpnInfoVO2.setCpnInfo("CPN01" + ImcsConstants.COLSEP + cpnInfoVO.getCpnInfo() + ImcsConstants.COLSEP);
				}
			} catch(Exception e) {
				//imcsLog.failLog(ImcsConstants.API_PRO_ID997, "", null, "coupon(mims)_info:" + ImcsConstants.RCV_MSG2, methodName, methodLine);
			}
			
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("발급가능쿠폰(F_GET_CPN_COND_POSSIBLE_LIST)", String.valueOf(tp2 - tp1), methodName, methodLine);

			tp1 = System.currentTimeMillis();
			try {
				cpnInfoVO = getNSContStatDao.getStmInfo(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
				if(cpnInfoVO == null || cpnInfoVO.getStmInfo() == null) {
					//imcsLog.failLog(ImcsConstants.API_PRO_ID997, "", null, "coupon(mims)_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
				} else {
					cpnInfoVO2.setStmInfo("STP00" + ImcsConstants.COLSEP + cpnInfoVO.getStmInfo() + ImcsConstants.COLSEP);
				}
				
			} catch(Exception e) {
				//imcsLog.failLog(ImcsConstants.API_PRO_ID997, "", null, "coupon(mims)_info:" + ImcsConstants.RCV_MSG2, methodName, methodLine);
			}
			
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("발급가능스탬프(F_GET_STM_COND_POSSIBLE)", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			tp1 = System.currentTimeMillis();
			try {
				
				cpnInfoVO = getNSContStatDao.getUseCpnInfo(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
				if(cpnInfoVO == null || cpnInfoVO.getUseCpnInfo() == null) {
					//imcsLog.failLog(ImcsConstants.API_PRO_ID997, "", null, "coupon(mims)_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
				} else {
					cpnInfoVO2.setUseCpnInfo("CPN02" + ImcsConstants.COLSEP + cpnInfoVO.getUseCpnInfo() + ImcsConstants.COLSEP);
				}
				
			} catch(Exception e) {
				//imcsLog.failLog(ImcsConstants.API_PRO_ID997, "", null, "coupon(mims)_info:" + ImcsConstants.RCV_MSG2, methodName, methodLine);
			}
			
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("사용가능쿠폰(F_GET_CPN_USE_POSSIBLE_LIST)", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			tp1 = System.currentTimeMillis();
			try {
						
				cpnInfoVO = getNSContStatDao.getGblCpnInfo(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
				if(cpnInfoVO == null || cpnInfoVO.getGblCpnInfo() == null) {
					//imcsLog.failLog(ImcsConstants.API_PRO_ID997, "", null, "coupon(mims)_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
				} else {
					cpnInfoVO2.setGblCpnInfo("CPN03" + ImcsConstants.COLSEP + cpnInfoVO.getGblCpnInfo() + ImcsConstants.COLSEP);
				}
				
			} catch(Exception e) {
				//imcsLog.failLog(ImcsConstants.API_PRO_ID997, "", null, "coupon(mims)_info:" + ImcsConstants.RCV_MSG2, methodName, methodLine);
			}
			
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("사용가능글로벌쿠폰(F_GET_CPN_USE_GLOBAL_LIST)", String.valueOf(tp2 - tp1), methodName, methodLine);
			
		} catch (Exception e) {
			tp2 = System.currentTimeMillis();
			imcsLog.timeLog("스탬프/쿠폰 정보 조회 실패", String.valueOf(tp2 - tp1), methodName, methodLine);
		}
		
		return cpnInfoVO2;
	}
	



	/**
	 * 월정액 상품정보 조회
	 * @param GetNSChListRequestVO	paramVO
	 * @return String
	 * @throws Exception 
	 **/
	// 2019.04.01 - PPMSTOP으로 인해 가입여부를 받아서 노출 시키지 말아야할 상품을 걸려낸다.
	public List<GetNSContStatResponseVO> getPPMInfo(GetNSContStatRequestVO paramVO, boolean ppmYn) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		GetNSContStatResponseVO resultVO	= new GetNSContStatResponseVO();
		GetNSContStatResponseVO tempVO		= new GetNSContStatResponseVO();
		List<GetNSContStatResponseVO> resultListVO	= new ArrayList<GetNSContStatResponseVO>();
		ArrayList<String> svodProdList = new ArrayList<>();
		int svodRowCnt		= 0;
		int svodByBundleRowCnt		= 0;
		
		ComDataFreeVO datafreeVO = new ComDataFreeVO();
		
		resultListVO = this.getPPMProdInfo(paramVO);
		
		int nMainCnt		= 0;
		String tmpProdId	= "";
		
		if(resultListVO != null) 
			nMainCnt = resultListVO.size();
		
		String szMsg		= "";
		String szDataSubCk	= "0";
		String nScreenYn = "N";
		String nScreenProd = "";
		String nScreenType = "";
		String ImpossibleSubscriptionSvod = "N";
		String[] nScreenTmp;
		String[] genReSmall;
		String SvodMaxValue = "Y";
		
		paramVO.setBuyPossibleYn("N");
		resultVO.setTempUflixProdYn("N");
		
		for(int i = 0; i < nMainCnt; i++) {
			tempVO	= resultListVO.get(i);
			
			if (i == 0) {												
				// 2017.09.28 엔스크린(NSCREEN) - FVOD 컨텐츠는 nscreen 서비스를 하지 않는다.
				// 2020.01.14 엔스크린 여부 로직 변경 (FVOD 엔스크린 지원 / 월정액의 경우 IPTV와 맵핑된 월정액이 존재해야 지원)
				nScreenTmp = tempVO.getnScreenYnTmp().split(";");
				if(nScreenTmp != null)
				{
					tempVO.setnScreenYn(nScreenTmp[0]);
					nScreenYn = tempVO.getnScreenYn();
					
					if(tempVO.getnScreenYn().equals("Y"))
					{						
						tempVO.setnScreenProd(nScreenTmp[1]);
						nScreenProd = tempVO.getnScreenProd();
						
						tempVO.setnScreenType(nScreenTmp[2]);
						nScreenType = tempVO.getnScreenType();
												
						genReSmall = tempVO.getGenreInfo().split("[|]");
						if(genReSmall.length > 2) {
							resultVO.setGenreSmall(genReSmall[2]);
						}
					}
				}
				
				if(paramVO.getLicensingEndYn().equals("Y") && tempVO.getnScreenProd().indexOf("0") >= 0)
				{
					if(!(tempVO.getnScreenProd().indexOf("1") >= 0 || tempVO.getnScreenProd().indexOf("2") >= 0 || tempVO.getnScreenProd().indexOf("3") >= 0))
					{
						nScreenYn = "N";
					}
				}
			}
			
			// 2019.12.06 - IPTV 영화월정액 가입자일 경우에는 구매 가능한 콘텐츠가 있을 때에는 영화월정액 상품을 주지 않고, 구매 가능한 콘텐츠가 없을 때에는 영화월정액 상품 정보를 주어 상세 페이지 진입시 오류가 없도록 한다.
			if(tempVO.getContsType().equals("0") || tempVO.getContsType().equals("1") || tempVO.getContsType().equals("2"))
			{
				paramVO.setBuyPossibleYn("Y");
			}
			
			// 쿼리에서 DISTINCT 를 제거 했기 때문에 추가된 로직
			if(tmpProdId.equals(tempVO.getSubsProdId()))
				continue;
			else
				tmpProdId = StringUtil.nullToSpace(tempVO.getSubsProdId());
			
			// 구매일, 현재일에 대한 날짜 계산 로직 적용 예정 (변수 등 정리필요)
			DateUtil dateUtil = new DateUtil();
			Date dExpiredDate = dateUtil.getStringToDate(tempVO.getExpiredDate(), "yyyyMMdd");
			Date dCurrentTime = dateUtil.getStringToDate(paramVO.getCurrentDate(), "yyyyMMddHHmmss");
			Date dReservedDate = dateUtil.getStringToDate(tempVO.getReservedDate(), "yyyyMMdd");
			
			// 상품종료일이 지난경우 조회 할 상품에서 제외 한다
			int nExpiredYn = dExpiredDate.compareTo(dCurrentTime);
			if(nExpiredYn < 0)	continue;		// 현재 시간이 만료일보다 크면 Y
		
			paramVO.setSuggestedPrice(tempVO.getPrice());
			paramVO.setGenreInfo(tempVO.getGenreInfo());
			paramVO.setProdType(tempVO.getContsType());
			
			//통계 로그용
			resultVO.setContsNameSt(tempVO.getContsNameSt());
			
			// N_SCREEN_YN 값 세팅
			resultVO.setnScreenYn(nScreenYn);
			resultVO.setnScreenProd(nScreenProd);
			resultVO.setnScreenType(nScreenType);
			
			// SVOD인 경우 UFlix 상품 여부를 취득
			if("3".equals( tempVO.getContsType() ))
				resultVO.setUflixProdYn(tempVO.getUflixProdYn().substring(0));
			
			if("Y".equals(tempVO.getUflixProdYn())) {
				resultVO.setTempUflixProdYn("Y");
			}
			
			int maxViewingLen = 0;
			
			try {
				maxViewingLen = Integer.parseInt(tempVO.getMaxViewingLenCon());
			} catch (Exception e) {
				maxViewingLen = 0;
			}
			
			if(i == 0){
				
				/* 인앱 가격 정보	*/
				/* 컨텐츠 및 데이터 프리의 인앱 가격 가져오기	*/
				
				/* FVOD의 경우 0원으로 인앱 가격을 조회한다.	*/
				if( "0".equals(tempVO.getContsType()) ){
					datafreeVO.setPrice("0");
				}else{
					datafreeVO.setPrice(tempVO.getPrice());
				}
				
				tempVO.setInappPrice("");
				
				//데이터프리 정보 조회
    			try {
    				if (paramVO.getFreeFlag().equals("N"))
    					// 2020.12.22 - 모바일 아이들나라 인앱결제 (모바일TV와 모바일 아이들나라 인앱 분리)
    					if(paramVO.getAppType().substring(0,1).equals("A"))
    					{
    						datafreeVO.setApprovalGb("A");
    					}
    					else if(paramVO.getAppType().substring(0,1).equals("E"))
    					{
    						datafreeVO.setApprovalGb("E");
    					}
    					else if(paramVO.getAppType().substring(0,1).equals("L"))
    					{
    						datafreeVO.setApprovalGb("L");
    					}
    					else
    					{
    						datafreeVO.setApprovalGb("N");
    					}
    					datafreeVO = this.getDatafreeInfo(datafreeVO, paramVO);
				} catch (Exception e) {
					
				}
    			
    			/* 예약구매 컨텐츠 */
    			if( "R".equals(tempVO.getPreviewFlag()) &&  dReservedDate.compareTo(dCurrentTime) >= 0){
    				
    				datafreeVO.setDatafreePrice("0");    				
    				datafreeVO.setDatafreeApprovalId("");
    				datafreeVO.setDatafreeApprovalPrice("");
    				datafreeVO.setPpvDatafreeApprovalId("");
    				datafreeVO.setPpvDatafreeApprovalPrice("");
    				// 2020.01.02 - 예약구매는 인앱결제를 지원하지 않는다.
    				datafreeVO.setApprovalId("");
    				datafreeVO.setApprovalPrice("");
    				
    			}else{
    				
    				/* 데이터 프리 무료	*/
    				if("N".equals(tempVO.getDatafreeBuyYn())){
    					    					
    					/* 인앱 가격 제공	*/    					
    					if("1".equals(tempVO.getCpPropertyBin())){    						
    						datafreeVO.setDatafreePrice("0");
    	    				datafreeVO.setDatafreeApprovalId("");
    	    				datafreeVO.setDatafreeApprovalPrice("");
    	    				datafreeVO.setPpvDatafreeApprovalId("");
    	    				datafreeVO.setPpvDatafreeApprovalPrice("");    						
    					}
    					/* 인앱 가격 미제공	*/
    					else{    						
    						datafreeVO.setApprovalId("");
    						datafreeVO.setApprovalPrice("");
    						datafreeVO.setDatafreePrice("0");
    	    				datafreeVO.setDatafreeApprovalId("");
    	    				datafreeVO.setDatafreeApprovalPrice("");
    	    				datafreeVO.setPpvDatafreeApprovalId("");
    	    				datafreeVO.setPpvDatafreeApprovalPrice("");    						
    					}
    				}
    				/* 데이터 프리 유료	*/
    				else{
    					
    					/* 인앱 가격 제공	*/
    					if( "1".equals(tempVO.getCpPropertyBin()) ){ 
    						
    						/* PPV	*/
    						if( "1".equals(tempVO.getContsType()) ){
    							
    							/* 인앱 가격을 제공해주는 경우 PPV+데이터프리 인앱 가격만 제공한다.	*/
    							datafreeVO.setDatafreeApprovalId("");
    							datafreeVO.setDatafreeApprovalPrice("");    							
    						}
    						/* FVOD	*/
    						else if( "0".equals(tempVO.getContsType()) ){
    							
    							/* 데이터프리 가격만 제공한다.	*/
    							datafreeVO.setPpvDatafreeApprovalId("");
        	    				datafreeVO.setPpvDatafreeApprovalPrice("");    
    						}else if( "2".equals(tempVO.getContsType()) ){
    							
    							// 2020.01.13 - PPS도 이제 인앱결제 지원 - 사업팀 결정 
        						datafreeVO.setDatafreePrice("0");
        	    				datafreeVO.setDatafreeApprovalId("");
        	    				datafreeVO.setDatafreeApprovalPrice("");
        	    				datafreeVO.setPpvDatafreeApprovalId("");
        	    				datafreeVO.setPpvDatafreeApprovalPrice("");      							
    						}
    						else
    						{
    							/* FVOD나 PPV가 아닌 경우에는 SVOD ONLY로 간주 */
    							datafreeVO.setApprovalId("");
        						datafreeVO.setApprovalPrice("");
        						datafreeVO.setDatafreePrice("0");
        	    				datafreeVO.setDatafreeApprovalId("");
        	    				datafreeVO.setDatafreeApprovalPrice("");
        	    				datafreeVO.setPpvDatafreeApprovalId("");
        	    				datafreeVO.setPpvDatafreeApprovalPrice("");
    						}
    						
    					}
    					/* 인앱 가격 미제공	*/
    					else{
    						
    						/* SVOD ONLY 인경우 datafree_price(원 가격)를 0으로 제공	*/
    						if( "3".equals(tempVO.getContsType()) || "2".equals(tempVO.getContsType()) ){
    							datafreeVO.setDatafreePrice("0");
    						}
    						
    						datafreeVO.setApprovalId("");
    						datafreeVO.setApprovalPrice("");
    	    				datafreeVO.setDatafreeApprovalId("");
    	    				datafreeVO.setDatafreeApprovalPrice("");
    	    				datafreeVO.setPpvDatafreeApprovalId("");
    	    				datafreeVO.setPpvDatafreeApprovalPrice("");
    					}
    					if(paramVO.getFreeFlag().equals("Y")) datafreeVO.setDatafreePrice("0");
    				}        				    			
					
					/* 평생 소장 컨텐츠 인앱 가격 미제공	*/
					if( maxViewingLen/24 > 2000){
						datafreeVO.setApprovalId("");
						datafreeVO.setApprovalPrice("");
						datafreeVO.setDatafreePrice("");
	    				datafreeVO.setDatafreeApprovalId("");
	    				datafreeVO.setDatafreeApprovalPrice("");
	    				datafreeVO.setPpvDatafreeApprovalId("");
	    				datafreeVO.setPpvDatafreeApprovalPrice("");
					}
    			}
    			
//    			resultVO.setInappProdId(datafreeVO.getApprovalId());		
    			resultVO.setDatafreePrice(datafreeVO.getDatafreePrice());
    			resultVO.setDatafreeInappProdId(datafreeVO.getDatafreeApprovalId());
    			resultVO.setDatafreeInappPrice(datafreeVO.getDatafreeApprovalPrice());
    			resultVO.setPpvDatafreeInappProdId(datafreeVO.getPpvDatafreeApprovalId());
    			resultVO.setPpvDatafreeInappPrice(datafreeVO.getPpvDatafreeApprovalPrice());
			}
			// 2020.01.13 - PPS도 이제 인앱결제 지원 - 사업팀 결정
			else if(i != 0 && "2".equals(tempVO.getContsType()))
			{
				datafreeVO.setPrice(tempVO.getPrice());
				
				try {
    				if (paramVO.getFreeFlag().equals("N"))
    					// 2020.12.22 - 모바일 아이들나라 인앱결제 (모바일TV와 모바일 아이들나라 인앱 분리)
    					if(paramVO.getAppType().substring(0,1).equals("A"))
    					{
    						datafreeVO.setApprovalGb("A");
    					}
    					else if(paramVO.getAppType().substring(0,1).equals("E"))
    					{
    						datafreeVO.setApprovalGb("E");
    					}
    					else if(paramVO.getAppType().substring(0,1).equals("L"))
    					{
    						datafreeVO.setApprovalGb("L");
    					}
    					else
    					{
    						datafreeVO.setApprovalGb("N");
    					}
    					datafreeVO = this.getDatafreeInfo(datafreeVO, paramVO);
				} catch (Exception e) {
					
				}
				
				if(!"1".equals(tempVO.getCpPropertyBin())){  
					datafreeVO.setApprovalId("");
					datafreeVO.setApprovalPrice("");					    						
				}
				
				/* 평생 소장 컨텐츠 인앱 가격 미제공	*/
				if( maxViewingLen/24 > 2000){
					datafreeVO.setApprovalId("");
					datafreeVO.setApprovalPrice("");
				}
				
				datafreeVO.setDatafreeApprovalId("");
				datafreeVO.setDatafreeApprovalPrice("");
				datafreeVO.setPpvDatafreeApprovalId("");
				datafreeVO.setPpvDatafreeApprovalPrice("");
			}
			
			// 상품타입이 SVOD 인 경우에 대한 SVOD 정보 조회
			if(paramVO.getFreeFlag().equals("N") && "3".equals( tempVO.getContsType() )) {
				paramVO.setSvodProdId(tempVO.getSubsProdId());
				List<ComSvodVO> svodListInfo	= new ArrayList<ComSvodVO>();
				ComSvodVO svodInfo	= new ComSvodVO();		// SVOD 상품정보
				ComSvodVO subSvodInfo	= new ComSvodVO();		// SVOD의 SUB SVOD가 존재할 경우 SUB SVOD 정보
				SvodMaxValue = "Y";
				
				
				try {
					svodListInfo	= this.getSvodInfo(paramVO);	
					svodByBundleRowCnt = 0;
				} catch (Exception e) {
					return null;
				}
				
				
				int iCount = 0;				
				if(svodListInfo != null)	iCount = svodListInfo.size(); 
				
				if(iCount == 0){
					ImpossibleSubscriptionSvod = "Y"; 
					continue;
				}
				if(i == 0)		paramVO.setSvodOnly("1"); 
//				else			paramVO.setSvodYn("1");
				
				for(int j = 0; j < iCount; j++) {
					svodInfo = svodListInfo.get(j);
					
					//2019.04.01 - 이전에 JAVA에서 잘 못 개발되어 있었음.
//					if(i == 0)	svodInfo.setMaxValue(""); // 원래 i==0 일때는 쿼리상 MAX_VALUE를 안구해온다					
					
					// 해당 비표시 상품 가입여부	> 이게 무슨 의미인지 도저히 모르겠음  
					// 현재 탈 수 없는 로직(Y갑이 나올 수 없음)
					//2019.04.01 - 이전에 JAVA에서 잘 못 개발되어 있었음.
//					if("Y".equals(svodInfo.getMaxValue())) {
//						// SVOD 인 경우에는 컨텐츠가 편성된 상품만 체크 (번들포함)
//						String szProductId = "";
//						try {
//							szProductId = this.getProductId(paramVO);
//						} catch (Exception e) {
//							return null;
//						}
//						
//						if(szProductId == null || "".equals(szProductId)){
//							continue;
//						}
//						
//						if(!"".equals(szProductId))	
//							tempVO.setContsType(tempVO.getContsType() + "\b" + tempVO.getContsType()); 
//					}
					
					if(paramVO.getKidFlag().equals("A")) {
						if(!svodListInfo.get(j).getAppCtrl().substring(1,2).equals("1") && ppmYn == false) {
							continue;
						}
					} else {
						if(!svodListInfo.get(j).getAppCtrl().substring(0,1).equals("1") && ppmYn == false) {
							continue;
						}
					}
					
					if(ppmYn == false && "Y".equals(svodInfo.getMaxValue()))
					{
						continue;
					}
					else
					{
						SvodMaxValue = "N";
					}							
					
					paramVO.setSvodYn("1");
					// 서브 상품의 경우 메인 상품의 정보를 제공
					if("Y".equals( svodInfo.getProdSubYn() )) {
						paramVO.setSvodProdId(svodInfo.getSvodProdId());
						
						try {
							subSvodInfo = getNSContStatDao.getSubSvodInfo(paramVO);
							
							if(subSvodInfo != null) {
								svodInfo.setSvodProdId(subSvodInfo.getSvodProdId());
								
								svodInfo.setSvodProdName(subSvodInfo.getSvodProdName());
								svodInfo.setSvodProdPrice(subSvodInfo.getSvodProdPrice());
								svodInfo.setSvodProdDesc(subSvodInfo.getSvodProdDesc());
								svodInfo.setSvodProdIsuYn(subSvodInfo.getSvodProdIsuYn());
								svodInfo.setSvodProdIsuType(subSvodInfo.getSvodProdIsuType());
							}
							else
							{
								continue;
							}
							
						} catch(Exception e) {
							szMsg	 = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID997) + "] sts[    0]" + String.format("%-21s", "msg[buy_info1_fvod:" + ImcsConstants.RCV_MSG6 + "]");
							imcsLog.serviceLog(szMsg, methodName, methodLine);
						}
					}
					
					if("0".equals( paramVO.getSvodProdBuyYn() ))
						szDataSubCk = "1";
					
					if("Y".equals(svodInfo.getUflixProdYn())) {
						//유플릭스 가입자의 경우 가입한 상품 정보만 노출 시킨다.
						if(!"1".equals(paramVO.getUflixBuyYn())) {
							if(paramVO.getTempAlbumId().equals(svodInfo.getSvodProdId())) {
								
							} else {
								// 2021.02.24 - 구 영화월정액 프리미엄 가입자의 경우 상품 정보를 안 주던 문제를 줄 수 있도록 수정 (신규 영화월정액 가입자한테 구상품이 노출되서 일단 주석-구상품 가입자한테만 구상품 노출할려고 하는게 목적인데 안되서..)
//								if("0".equals(paramVO.getUflixBuyYn()) && "0".equals(svodInfo.getCurrentCount()) && ppmYn == true)
//								{
//									
//								}
								//유플릭스 베이직 상품 가입 고객은 프리미엄 상품은 노출 시킨다.
//								else if(!("2".equals(paramVO.getCustomUflix()) && "3".equals(svodInfo.getCurrentCount()) ))
								if(!("2".equals(paramVO.getCustomUflix()) && "3".equals(svodInfo.getCurrentCount()) ))
                    			{
                    				// 2019.12.06 - TV 영화월정액 가입자인데, 구매 가능한 콘텐츠가 있으면, 영화월정액 상품을 빼고 주고.. 구매 가능한 콘텐츠가 없으면 영화월정액 상품을 제공하여 준다.
                    				if( !("2".equals(paramVO.getUflixBuyYn()) && "N".equals(paramVO.getBuyPossibleYn())) )
                    				{
                    					continue;
                    				}
                    				else if("2".equals(paramVO.getUflixBuyYn()) && "0".equals(svodInfo.getCurrentCount()))
									{
										continue;
									}
                    			}
							}
							
							// 2020.06.29 - 영화월정액 노출정책 적용
							if(svodInfo.getImcsViewCtrl().equals("1") || svodInfo.getImcsViewCtrl().equals("3")){
								continue;
							}
							
						} else {
							if("0".equals(paramVO.getCustomUflix()) && "0".equals(svodInfo.getCurrentCount())) {
								continue;
							}
							
							// 2020.06.29 - 영화월정액 노출정책 적용
							if(svodInfo.getImcsViewCtrl().equals("1") || svodInfo.getImcsViewCtrl().equals("2")){
								continue;
							}
						}
					}
					
					int overlapChk = 0; 
					
					if(svodRowCnt == 0) {
						if(!"N".equals( paramVO.getFxType() )) {
							resultVO.setSubsProdId(svodInfo.getSvodProdId());
							resultVO.setSubsProdName(svodInfo.getSvodProdName());
							resultVO.setSubsProdPrice(svodInfo.getSvodProdPrice());
							resultVO.setSubsProdSub(svodInfo.getSvodProdDesc());
							resultVO.setSubsProdIsu(svodInfo.getSvodProdIsuYn());
							resultVO.setSubsProdType(svodInfo.getSvodProdIsuType());
							svodProdList.add(svodInfo.getSvodProdId());
						}
						
						resultVO.setSubsYn(paramVO.getSvodProdBuyYn());
					} else {
						
						for(int k = 0; k < svodProdList.size(); k++) {
							if(svodInfo.getSvodProdId().equals(svodProdList.get(k))) {
								overlapChk = 1;
							}
						}
						
						if(!"N".equals( paramVO.getFxType() ) && overlapChk != 1) {
							resultVO.setSubsProdId(resultVO.getSubsProdId() + "\b" + svodInfo.getSvodProdId());
							resultVO.setSubsProdName(resultVO.getSubsProdName() + "\b" + svodInfo.getSvodProdName());
							resultVO.setSubsProdPrice(resultVO.getSubsProdPrice() + "\b" + svodInfo.getSvodProdPrice());
							resultVO.setSubsProdSub(resultVO.getSubsProdSub() + "\b" + svodInfo.getSvodProdDesc());
							resultVO.setSubsProdIsu(resultVO.getSubsProdIsu() + "\b" + svodInfo.getSvodProdIsuYn());
							resultVO.setSubsProdType(resultVO.getSubsProdType() + "\b" + svodInfo.getSvodProdIsuType());
							svodProdList.add(svodInfo.getSvodProdId());
						}
						
						resultVO.setSubsYn(paramVO.getSvodProdBuyYn());						
					}
					
					svodRowCnt++;
					if(overlapChk != 1) svodByBundleRowCnt++;
				}
				
				// 2020.09.10 - SVOD의 번들상품이 가입시 노출 월정액만 있을 경우에는  상품정보등을 세팅하지 않는다.
				if(ppmYn == false && "Y".equals(SvodMaxValue))
				{				
					continue;
				}				
				
				if(i == 0 || ImpossibleSubscriptionSvod == "Y") {									
					if( ImpossibleSubscriptionSvod == "Y" || ("N".equals( paramVO.getFxType() ) && "0".equals( szDataSubCk ) && "Y".equals( tempVO.getUflixProdYn() )) ) {
					} else {
						paramVO.setSvodYn("1");
						resultVO.setBillType(tempVO.getBillType());
						resultVO.setContsType(tempVO.getContsType());
						resultVO.setMaxViewingLen(tempVO.getMaxViewingLen());
					}
					
					resultVO.setSalePrice(tempVO.getPrice());
					resultVO.setEventValue(tempVO.getEventValue());
					resultVO.setEventYn(tempVO.getEventYn());
					resultVO.setTerrYn(tempVO.getTerrYn());
					resultVO.setTerrEdDate(tempVO.getTerrEdDate());
					resultVO.setLicensingWindowEnd(tempVO.getLicensingWindowEnd());
					resultVO.setPrice(tempVO.getPrice());			// SUGGEST_PRICE 확인필요
					resultVO.setCpNouseYn(tempVO.getCpNouseYn());
					
					ImpossibleSubscriptionSvod = "N";	// 첫번째 월정액이 가입 불가능 상품일 때, 아래 값을 세팅해주지 않으므로, 두번째 월정액에서 값을 세팅해주도록 한다.
				}							
			}
			// 상품타입이 SVOD가 아닌 경우 
			else {
				if(i == 0) {
					paramVO.setSvodOnly("0");
					
					resultVO.setBillType(tempVO.getBillType());
					resultVO.setContsType(tempVO.getContsType());
					resultVO.setSalePrice(tempVO.getPrice());
					resultVO.setEventValue(tempVO.getEventValue());
					resultVO.setEventYn(tempVO.getEventYn());
					resultVO.setMaxViewingLen(tempVO.getMaxViewingLen());
					resultVO.setTerrYn(tempVO.getTerrYn());
					resultVO.setTerrEdDate(tempVO.getTerrEdDate());
					resultVO.setLicensingWindowEnd(tempVO.getLicensingWindowEnd());
					resultVO.setPrice(tempVO.getPrice());
					resultVO.setCpNouseYn(tempVO.getCpNouseYn());
					
					
					// PPV일때만 인앱정보 설정
					// 2020.01.13 - PPS도 인앱결제 가능 - 사업팀 결정
					if("1".equals( tempVO.getContsType() ) || "2".equals( tempVO.getContsType() )) {
						resultVO.setInappPrice(datafreeVO.getApprovalPrice());
						resultVO.setInappProdId(datafreeVO.getApprovalId());
					}
					
//					resultVO.setDatafreePrice(datafreeVO.getDatafreePrice());
//					resultVO.setDatafreeInappProdId(datafreeVO.getDatafreeApprovalId());
//					resultVO.setDatafreeInappPrice(datafreeVO.getDatafreeApprovalPrice());
//					resultVO.setPpvDatafreeInappProdId(datafreeVO.getPpvDatafreeApprovalId());
//					resultVO.setPpvDatafreeInappPrice(datafreeVO.getPpvDatafreeApprovalPrice());
					
				} else {
					// 첫 Row가 아닐 경우 기존 구매내역 조회
					if(i != 0 && "0".equals( paramVO.getSvodOnly()) ) {
						
						HashMap<String, String> datafreeDateList = new HashMap<String, String>();
						
						 /* 데이터 프리 구매 여부 조회	*/
						try {
							datafreeDateList = getNSContStatDao.getBuyDataFreeInfo(paramVO);
							
						} catch (Exception e) {
//							imcsLog.failLog(ImcsConstants.API_PRO_ID997, "", null, ImcsConstants.RCV_MSG6, methodName, methodLine);
						}
						
						//if( datafreeDateList != null && datafreeDateList.size() > 0 && datafreeDateList.get(0) != null ){
						//if( datafreeDateList != null && datafreeDateList.size() > 0){
						if( datafreeDateList != null){
							int size = datafreeDateList.size();
							
							szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID997) + "] chkbuyDatafree [SELECT DATAFREE PT_VO_BUY_NSC] table[" + size + "] records Success at";
							imcsLog.serviceLog(szMsg, methodName, methodLine);
							
							resultVO.setBuyingDate((String) datafreeDateList.get("BUY_DATE"));
							resultVO.setExpiredDate((String) datafreeDateList.get("EXPIRED_DATE"));
							
							resultVO.setDatafreeBuyYn("Y");
							resultVO.setBuyYn("0");
							
						}else{
							//imcsLog.serviceLog("getNSContStatDao.getBuyDataFreeInfo() : NULL", methodName, methodLine);
							resultVO.setDatafreeBuyYn("N");
							
							paramVO.setContsType(tempVO.getContsType());
							
							HashMap<String, Object> mDupCk	= new HashMap<String, Object>();
							
							try {
								mDupCk = this.getBuyDupChk(paramVO);
							} catch (Exception e) {
								return null;
							}
							
							String buy_chk = (String) mDupCk.get("BUY_YN");
							if(buy_chk.equals("0"))
							{
								resultVO.setBuyYn(resultVO.getBuyYn() + "\b" + (String) mDupCk.get("BUY_YN"));
								resultVO.setBuyingDate(resultVO.getBuyingDate() + "\b" + (String) mDupCk.get("BUY_DATE"));								
								resultVO.setExpiredDate(resultVO.getExpiredDate() + "\b" + (String) mDupCk.get("EXP_DATE"));
							}
							else
							{
								resultVO.setBuyYn(resultVO.getBuyYn() + "\b" + (String) mDupCk.get("BUY_YN"));
								resultVO.setBuyingDate(resultVO.getBuyingDate() + "\b" + (String) mDupCk.get("BUY_DATE"));
							}
							
							resultVO.setSalePrice("0");
						}
						
						// 가격 저장
						if("0".equals( tempVO.getContsType() )) {
							resultVO.setPrice(resultVO.getPrice() + "\b0");
						} else {
							resultVO.setPrice(resultVO.getPrice() + "\b" + tempVO.getPrice());
							if("2".equals( tempVO.getContsType()) )
							{							
								if(!datafreeVO.getApprovalPrice().equals(""))
								{
									resultVO.setInappPrice(resultVO.getInappPrice() + "," + datafreeVO.getApprovalPrice());
									resultVO.setInappProdId(resultVO.getInappProdId() + "," + datafreeVO.getApprovalId());
								}
							}
						}
					}
				}
				
				
			}
			
			// 2020.12.22 - 모바일 아이들나라 상품 분리 (ex. 모바일TV APP에서 콘텐츠 요청시 아이들나라 상품만 걸려 있다면 SVOD 상품 타입을 주지 않도록 한다.)
			if(paramVO.getFreeFlag().equals("N") && "3".equals( tempVO.getContsType() )) {
				if(svodByBundleRowCnt == 0)
				{
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID997) + "] 노출할 SVOD 상품이 없습니다. (SVOD empty)";
					imcsLog.serviceLog(szMsg, methodName, methodLine);	
//					continue;
				}
			}
			
			// 첫 Row일 경우 기존 구매내역 체크
			if(i == 0 && "0".equals( paramVO.getSvodOnly() )) {
				
				HashMap<String, String> datafreeDateList = new HashMap<String, String>();
				
				 /* 데이터 프리 구매 여부 조회	*/
				try {
					datafreeDateList = getNSContStatDao.getBuyDataFreeInfo(paramVO);
					paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
					
				} catch (Exception e) {
//					imcsLog.failLog(ImcsConstants.API_PRO_ID997, "", null, ImcsConstants.RCV_MSG6, methodName, methodLine);
				}
				
				
				//if( datafreeDateList != null && datafreeDateList.size() > 0 && datafreeDateList.get(0) != null ){
				//if( datafreeDateList != null && datafreeDateList.size() > 0){
				if( paramVO.getFreeFlag().equals("N") && datafreeDateList != null){	
					int size = datafreeDateList.size();
					
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID997) + "] chkbuyDatafree [SELECT DATAFREE PT_VO_BUY_NSC] table[" + size + "] records Success at";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
					
					resultVO.setBuyingDate((String) datafreeDateList.get("BUY_DATE"));
					resultVO.setExpiredDate((String) datafreeDateList.get("EXPIRED_DATE"));
					resultVO.setDatafreeBuyYn("Y");
					resultVO.setBuyYn("0");
					
				}else{
					//imcsLog.serviceLog("getNSContStatDao.getBuyDataFreeInfo() : NULL", methodName, methodLine);
					resultVO.setDatafreeBuyYn("N");
					
					paramVO.setContsType(tempVO.getContsType());
					
					HashMap<String, Object> mDupCk	= new HashMap<String, Object>();
					
					try {
						mDupCk = this.getBuyDupChk(paramVO);
					} catch (Exception e) {
						return null;
					}
					
					resultVO.setBuyYn((String) mDupCk.get("BUY_YN")); //권형도
					resultVO.setBuyingDate((String) mDupCk.get("BUY_DATE"));
					resultVO.setExpiredDate((String) mDupCk.get("EXP_DATE"));
					resultVO.setSalePrice("0");
				}
			}			
			
			
			if(i != 0) {
				if( "N".equals( paramVO.getFxType() ) && "3".equals( tempVO.getContsType() ) && "0".equals( szDataSubCk )&& "Y".equals( tempVO.getUflixProdYn() )) {
					paramVO.setSvodYn("0");
				} else {
					resultVO.setBillType(resultVO.getBillType()+ "\b" + tempVO.getBillType());
					resultVO.setContsType(resultVO.getContsType() + "\b" + tempVO.getContsType());
					resultVO.setMaxViewingLen(resultVO.getMaxViewingLen() + "\b" + tempVO.getMaxViewingLen());
	            }
			} else {
				// 상품타입이 FVOD(무료 VOD)의 경우 가격 0원 저장
				if( "0".equals( tempVO.getContsType() ))
					resultVO.setPrice("0");
			}
			
			// 상품타입이 PVOD(시리즈/패키즈)의 경우 
			if(paramVO.getFreeFlag().equals("N") && "2".equals( tempVO.getContsType() )) {
				paramVO.setPkgYn("1");
				
				resultVO.setPvodProdName(tempVO.getSubsProdName());
				resultVO.setPvodProdDesc(tempVO.getPvodProdDesc());
				resultVO.setPvodProdDisRate(tempVO.getPvodProdDisRate());
			}
			
			// 상품타입이 PPV(단편구매)의 경우
			if(paramVO.getFreeFlag().equals("N") && "1".equals( tempVO.getContsType() ))
				paramVO.setShortYn("1");
			
			if (paramVO.getFreeFlag().equals("Y"))
				break;
		}
		
		resultListVO	= new ArrayList<GetNSContStatResponseVO>(); 
		
		//if(nMainCnt > 0){			
		//	resultListVO.add(resultVO);
		//}else{
		//	resultListVO = null;
		//}
		resultListVO.add(resultVO);
		
		
		return resultListVO;
	}



	/**
	 * 프리미엄 편성 여부 조회
	 * @param GetNSChListRequestVO	paramVO
	 * @return boolean
	 **/
	public boolean getPremiumYn(GetNSContStatRequestVO paramVO) {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String sqlId 		= "lgvod997_f07_20180830_001";
		Boolean bPremiumYn	= false;
		
		List<String> list = new ArrayList<String>();
		
		int querySize		= 0;
		long tp1 = System.currentTimeMillis();

		try {
			try {
				list = getNSContStatDao.getPremiumYn(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			long tp2 = System.currentTimeMillis();
	        imcsLog.timeLog("getPremiumYn 정보 조회", String.valueOf(tp2 - tp1), methodName, "");
	        

			if (list != null && !list.isEmpty()) {
				
				querySize = list.size();
				
				if(!"".equals(StringUtil.nullToSpace(list.get(0)))){
					bPremiumYn = true;
					paramVO.setPremiumYn("Y");
				}					
			}else{
//				imcsLog.failLog(ImcsConstants.API_PRO_ID997, sqlId, null, "IsPremium:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
			}
			
	        try{
//				imcsLog.dbLog(ImcsConstants.API_PRO_ID997, sqlId, null, querySize, methodName, methodLine);
			}catch(Exception e){}
			
			//paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
			
		} catch (Exception e) {
//			imcsLog.failLog(ImcsConstants.API_PRO_ID997, sqlId, null, "IsPremium:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			
			bPremiumYn	= false;
		}
		
		return bPremiumYn;
	}


	/**
	 * 유플릭스 상품 구매여부 조회
	 * @param GetNSChListRequestVO	paramVO
	 * @return boolean
	 **/
	public String getUflixBuyChk(GetNSContStatRequestVO paramVO) {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String sqlId 		= "lgvod997_002_20180830_001";
		String bUflixBuyCk	= "1";
		int UflixBuyCnt		= 0;
				
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		long tp1 = System.currentTimeMillis();
				
		try {
			
			try {
				list = getNSContStatDao.getUflixBuyChk(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			long tp2 = System.currentTimeMillis();
	        imcsLog.timeLog("getUflixBuyChk 정보 조회", String.valueOf(tp2 - tp1), methodName, "");
			
			if (list != null && !list.isEmpty()) {
				for(UflixBuyCnt = 0 ; UflixBuyCnt < list.size() ; UflixBuyCnt++)
				{
					paramVO.setConcurrentCnt(list.get(UflixBuyCnt).get("CONCURRENT_COUNT"));
					paramVO.setSubMobileUflixYn(list.get(UflixBuyCnt).get("SUB_MOBILE_UFLIX_YN"));
					
					if("0".equals(paramVO.getCustomUflix())) {
						if(Integer.parseInt(paramVO.getConcurrentCnt()) > 0) {
							if(Integer.parseInt(paramVO.getConcurrentCnt()) == 2) {
								paramVO.setCustomUflix("2");
							}
							else if(Integer.parseInt(paramVO.getConcurrentCnt()) >= 3) {
								paramVO.setCustomUflix("3");	
							}
							else {
								paramVO.setCustomUflix("1");	
							}							
						} else {
							paramVO.setCustomUflix("1");
						}
					}
					
					if(paramVO.getSubMobileUflixYn().equals("Y"))
					{
						if(paramVO.getFreeFlag().equals("N") && list.get(UflixBuyCnt).get("IMCS_PRODUCT_PROPERTY").equals("N"))
						{
							paramVO.setTempAlbumId(list.get(UflixBuyCnt).get("IMCS_PRODUCT_ID"));
							bUflixBuyCk = "0";
							break;
						}
						else if(paramVO.getFreeFlag().equals("N") && list.get(UflixBuyCnt).get("IMCS_PRODUCT_PROPERTY").equals("01") && Integer.parseInt(list.get(UflixBuyCnt).get("CONCURRENT_COUNT")) > 0)
						{
							paramVO.setTempAlbumId(list.get(UflixBuyCnt).get("IMCS_PRODUCT_ID"));
							// 2019.03.08 신규 유플릭스의 경우 일반 유플릭스 상품에 가입하지 않고 프리미엄만 가입할 수 있기 때문에, 신규 프리미엄 유플릭스만 가입한 경우에도 정상 처리 될 수록 처리해 준다.
							bUflixBuyCk = "0";
							break;
						}
					}
					else
					{
						// 2019.12.04 - TV 구 유플릭스 가입자의 경우에도 신규 상품은 전달해 주지 말라는 요구사항으로 인해 2Flag 추가.
						bUflixBuyCk = "2";
					}
				}
			}
			
		} catch (Exception e) {
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			
			bUflixBuyCk	= "1";
		}
		
		return bUflixBuyCk;
	}



	/**
	 * 주상품 가입여부 조회
	 * @param GetNSChListRequestVO	paramVO
	 * @return String
	 **/
	public GetNSContStatResponseVO getMProdId(GetNSContStatRequestVO paramVO) {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String sqlId 		= "lgvod997_001_20181018_001";
		GetNSContStatResponseVO szMProdId	= null;
		
		List<GetNSContStatResponseVO> list = null;
		long tp1 = System.currentTimeMillis();

		try {			
			try {
				list = getNSContStatDao.getMProdId(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
	
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			long tp2 = System.currentTimeMillis();
	        imcsLog.timeLog("getMProdId 정보 조회", String.valueOf(tp2 - tp1), methodName, "");

			if (list != null && !list.isEmpty()) {
				//szMProdId	= StringUtil.nullToSpace(list.get(0));
				szMProdId	= list.get(0);
			}
			
		} catch (Exception e) {
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}
		
		return szMProdId;
	}

	
	
	
	/**
	 * 	구매중복 체크
	 * 	1row와 1row 아닌 경우에 따라 해당 로직을 이용하는 경우가 달라 별로 생성함
	 * 	@param 	GetNSChListRequestVO
	 * 	@return HashMap<String, Object>
	 */
	public HashMap<String, Object> getBuyDupChk(GetNSContStatRequestVO paramVO) {
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		HashMap<String, Object> mDupChk = new HashMap<String, Object>();
		String szBuyYn		= "";
		String szBuyDate	= "";
		String szExpDate	= "";
		Integer nDupChk		= 0;
		String szMsg = "";
		
		try {
			// FVOD 구매 중복 체크
			if("0".equals(paramVO.getContsType())) 		mDupChk = getNSContStatDao.getBuyDupChk1(paramVO);
			// PPV 구매 중복 체크
			else if("1".equals(paramVO.getContsType()))	mDupChk = getNSContStatDao.getBuyDupChk2(paramVO);
			// PVOD 구매 중복 체크
			else if("2".equals(paramVO.getContsType()))	mDupChk = getNSContStatDao.getBuyDupChk3(paramVO);
			
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			
			nDupChk = (Integer) mDupChk.get("DATA_CHK");
			szBuyDate	= StringUtil.nullToSpace((String) mDupChk.get("BUY_DATE"));
			szExpDate	= StringUtil.nullToSpace((String) mDupChk.get("EXP_DATE"));

			if(paramVO.getFreeFlag().equals("N") && mDupChk != null && !mDupChk.isEmpty()) {
				
				// 만료일 시작이 0으로 시작하면 공백 세팅
				if(!"".equals(szExpDate)) {
					if("0".equals(szExpDate.substring(0)))		szExpDate	= "";
				}
				
			}
			
			if(nDupChk > 0) { //구매
				szBuyYn		= "0";
			} else { //미구매
				szBuyYn 	= "1";
				if (paramVO.getFreeFlag().equals("Y")) {
					szBuyYn = "0";
					szBuyDate = commonService.getSysdate();
					paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				}
				szBuyDate	= "";
				szExpDate	= "";
			}
			
			mDupChk.clear();
			mDupChk.put("BUY_YN", szBuyYn);
			mDupChk.put("BUY_DATE", szBuyDate);
			mDupChk.put("EXP_DATE", szExpDate);

		} catch(Exception e) {
//			imcsLog.failLog(ImcsConstants.API_PRO_ID997, "", null, "buy_info1:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			throw new ImcsException();
		}
		
		return mDupChk;
	}
	
	
	
	
	/**
	 * 
	 * @param GetNSChListRequestVO	paramVO
	 * @return String
	 **/
	public String getProductId(GetNSContStatRequestVO paramVO) {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId 		= "lgvod997_f05_20180830_001";
		String szProductId	= "";
		
		int querySize		= 0;
		
		List<String> list = new ArrayList<String>();

		try {			
			try {
				list = getNSContStatDao.getProductId(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
	
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if (list != null && !list.isEmpty()) {
				querySize	= list.size();
				szProductId	= StringUtil.nullToSpace(list.get(0));
			}
			
			try{
//				imcsLog.dbLog(ImcsConstants.API_PRO_ID997, sqlId, null, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
//			imcsLog.failLog(ImcsConstants.API_PRO_ID997, sqlId, null, "ppm_yn:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
		return szProductId;
	}




	/**
	 * SVOD 상품정보 조회
	 * @param GetNSChListRequestVO	paramVO
	 * @return List<GetNSSvodInfoVO>
	 **/
	public List<ComSvodVO> getSvodInfo(GetNSContStatRequestVO paramVO) {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId = "lgvod997_f01_20180830_001";
		if("N".equals(paramVO.getFxType()))			sqlId	= "lgvod997_f02_20180830_001";
		else if("H".equals(paramVO.getFxType()))	sqlId	= "lgvod997_f03_20180830_001";
		else										sqlId	= "lgvod997_f04_20180830_001";
		
		List<ComSvodVO> list = new ArrayList<ComSvodVO>();

		try {
			
			try {
				if("N".equals(paramVO.getFxType()))
					list = getNSContStatDao.getSvodInfoN(paramVO);
				else if("H".equals(paramVO.getFxType()))
					list = getNSContStatDao.getSvodInfoH(paramVO);
				else 
					list = getNSContStatDao.getSvodInfoE(paramVO);
				
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
	
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			if (list == null || list.isEmpty()) {
//				imcsLog.failLog(ImcsConstants.API_PRO_ID997, sqlId, null, "svod_prod:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
			}
			
		} catch (Exception e) {
//			imcsLog.failLog(ImcsConstants.API_PRO_ID997, sqlId, null, "svod_prod:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			throw new ImcsException();
		}
					
		return list;
	}
	
	

	/**
	 * PPM 월정액 정보 조회
	 * @param GetNSChListRequestVO	paramVO
	 * @return List<GetNSSvodInfoVO>
	 **/
	public List<GetNSContStatResponseVO> getPPMProdInfo(GetNSContStatRequestVO paramVO) {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		String szMsg = "";
		
		String sqlId = "lgvod997_f77_20180830_001";
		
		List<GetNSContStatResponseVO> list = new ArrayList<GetNSContStatResponseVO>();
		long tp1 = System.currentTimeMillis();
		
		try {			
			try {
				list = getNSContStatDao.getPPMProdInfo(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
	
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			long tp2 = System.currentTimeMillis();
	        imcsLog.timeLog("getPPMProdInfo 정보 조회", String.valueOf(tp2 - tp1), methodName, "");
			
			if (list == null || list.isEmpty()) {
//				imcsLog.failLog(ImcsConstants.API_PRO_ID997, sqlId, null, "conts_type:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
				throw new ImcsException();
			}
			else
			{
				for(int i = 0 ; i < list.size() ; i++)
				{
					if(list.get(i).getContsType().equals("0") && list.get(i).getLicensingWindowEnd().compareTo(paramVO.getCurrentDate().substring(0, 8)) <= 0)
					{
						if(i == 0) paramVO.setLicensingEndYn("Y");						
						list.remove(i);
						i--;						
					}
				}
				
				if(paramVO.getLicensingEndYn().equals("Y"))
				{
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID997) + "] msg[licensing expired - FVOD exclude]";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
				}
			}
			
			//paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
		} catch (Exception e) {
//			imcsLog.failLog(ImcsConstants.API_PRO_ID997, sqlId, null, "conts_type:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			throw new ImcsException();
		}
					
		return list;
	}
	
	/**
     * 데이터프리 정보 조회
     * @param	ComDataFreeVO, GetNSContStatRequestVO
     * @result	ComDataFreeVO
    **/
    public ComDataFreeVO getDatafreeInfo(ComDataFreeVO tempVO, GetNSContStatRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
    	
    	String sqlId = "lgvod997_003_20180830_001";
    	int querySize = 0;
		
		List<ComDataFreeVO> list   = new ArrayList<ComDataFreeVO>();
		ComDataFreeVO resultVO = null;
		long tp1 = System.currentTimeMillis();
		
		try {
			
			try{
				list = getNSContStatDao.getDatafreeInfo(tempVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			long tp2 = System.currentTimeMillis();
	        imcsLog.timeLog("getDatafreeInfo 정보 조회", String.valueOf(tp2 - tp1), methodName, "");
			
			if( list != null && !list.isEmpty()){
				resultVO = (ComDataFreeVO)list.get(0);
				
				// 2020.01.02 - 인앱결제는 데이터프리 구매를 지원하지 않는다.
				resultVO.setDatafreeApprovalId("");
				resultVO.setDatafreeApprovalPrice("");
				resultVO.setPpvDatafreeApprovalId("");
				resultVO.setPpvDatafreeApprovalPrice("");
				
				querySize = list.size();
			}else{
				resultVO = new ComDataFreeVO();
				resultVO.setDatafreePrice("0");
				resultVO.setApprovalPrice("");
				resultVO.setDatafreeApprovalPrice("");
				resultVO.setPpvDatafreeApprovalPrice("");
			}
			
//			imcsLog.dbLog(ImcsConstants.API_PRO_ID997, sqlId, null, querySize, methodName, methodLine);
			
		} catch (Exception e) {
//			if(cache.getLastException() != null)	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
//			else									paramVO.setResultCode("41000000");
			
//			 imcsLog.failLog(ImcsConstants.API_PRO_ID997, sqlId, null, "approval_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
    	return resultVO;
    }
    
    /**
	 * 상품(프리미엄/일반 ) 가입여부
	 * @param GetNSChListRequestVO	paramVO
	 * @return boolean
	 **/
    @SuppressWarnings("rawtypes")
	public boolean getPPMYn(GetNSContStatRequestVO paramVO) {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String sqlId 		= "lgvod997_061_20180830_001";
		Boolean bPPMYn	= false;
		
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		long tp1 = System.currentTimeMillis();
		
		try {
			
			try {
				list = getNSContStatDao.getUflixProdYnInfo(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			long tp2 = System.currentTimeMillis();
	        imcsLog.timeLog("가입자 SVOD 정보 조회", String.valueOf(tp2 - tp1), methodName, "");

			if (list != null && !list.isEmpty()) {
				
				String uflix_prod_yn = "";
				
				for(int i=0; i<list.size(); i++){
					
					uflix_prod_yn = (String) list.get(i).get("UFLIX_PROD_YN");
					
					if( "Y".equals(uflix_prod_yn) && !"0".equals(paramVO.getUflixBuyYn()) ){
						continue; // 미가입
					}else{
						paramVO.setSvodProdBuyYn("0"); // 가입
					}
				}
				
				bPPMYn	= true;
				
			}else{
				
				//구매형상품(하루권) 유효여부 체크
				List<String> list2 = new ArrayList<String>();
				
				
				try {
					list2 = getNSContStatDao.getHaruYnInfo(paramVO);
					paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
					
					if (list2 != null && !list2.isEmpty()) {
						paramVO.setSvodProdBuyYn("0");
						bPPMYn	= true;
					}
										
				} catch (Exception e) {
					
//					imcsLog.failLog(ImcsConstants.API_PRO_ID997, sqlId, null, "PPM_YN:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
					
					bPPMYn	= false;
				}
				
			}
			
		} catch (Exception e) {
//			imcsLog.failLog(ImcsConstants.API_PRO_ID997, sqlId, null, "ppm_yn:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			
			bPPMYn	= false;
		}
		
		return bPPMYn;
	}
	
	
	/**
	 * 찜한 컨텐츠 여부 확인
	 * @param GetNSChListRequestVO	paramVO
	 * @return boolean
	 **/
	public boolean chkFavorInfo(GetNSContStatRequestVO paramVO) {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId 		= "lgvod997_004_20180830_001";
		Boolean bFavorYn	= false;
		
		List<String> list = new ArrayList<String>();

		try {
			
			try {
				list = getNSContStatDao.chkFavorInfo(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			if (list != null && !list.isEmpty()) {
				bFavorYn	= true;
			}else{				
				bFavorYn	= false;
			}
			
		} catch (Exception e) {
//			imcsLog.failLog(ImcsConstants.API_PRO_ID997, sqlId, null, "getFavorInfo:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			
			bFavorYn	= false;
		}
		
		return bFavorYn;
	}
	
	/**
	 * 엔스크린 기능 제공 단말 및 엔스크린 컨텐츠일 경우 페어링된 가입자의 구매/가입 여부를 확인
	 * @param GetNSChListRequestVO	paramVO
	 * @return boolean
	 **/
	@SuppressWarnings("rawtypes")
	private List<HashMap> getNScreenPairingInfo(GetNSContStatRequestVO paramVO)
	{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId 		= "lgvod997_005_20180830_001";
		
		List<HashMap> list = null;

		try {
			try {
				list = getNSContStatDao.getNScreenPairingInfo(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			if(list == null)
				list = new ArrayList<>();
			
		} catch (Exception e) {
//			imcsLog.failLog(ImcsConstants.API_PRO_ID997, sqlId, null, "getNScreenPairingInfo:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
		return list;
	}
	
	/**
	 * 20170817 엔스크린(NSCREEN) 구매 여부 체크
	 * 2017.08.16 엔스크린(NSCREEN) - 예약구매는 구매했다고 줄 필요 없을 것으로 봄
	 * @param	GetNSChListRequestVO
	 * @result	Integer
	 */
	public HashMap<String, String> nScreenBuyChk(GetNSContStatRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		
		HashMap<String, String> hm = null;
		String sqlId	= "lgvod178_025_20171214_001";		
		
		try {
			hm = getNSContStatDao.getNScreenBuyChk(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			
//			imcsLog.dbLog(ImcsConstants.API_PRO_ID997, sqlId, null, hm.size(), methodName, methodLine);
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			throw new ImcsException(ImcsConstants.FAIL_CODE, e);
		}
		
		return hm;
	}
	
	/**
	 * 엔스크린(NSCREEN) 가입 여부 체크 - 상품코드 리스트
	 * @param paramVO
	 * @throws Exception
	 */
	private List<String> nscreenSubscriptionChkProductCdList(GetNSContStatRequestVO paramVO) throws Exception
	{
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String sqlId = "lgvod997_006_20180830_001";
		
		List<String> productCdList = new ArrayList<>();
		
		String szMsg = "";
		
		try
		{
			try {
				productCdList = getNSContStatDao.getNScreenProductCdList(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅						
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if(productCdList == null)
				productCdList = new ArrayList<>();
			
			szMsg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID997) + "]  SQLID[" + sqlId + "] rcv[" + productCdList.size() + "]";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
		}
		catch(Exception e)
		{
			//szMsg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID997) + "] SQLID[" + sqlId + "] sts[" + cache.getLastException().getErrorCode() + "] " + 
			//		"msg[" + String.format("%-21s", "nscreenSubscriptionChkProductCdList:" + cache.getLastException().getErrorMessage() + "]");
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			paramVO.setResultCode("21000000");
			
			throw new ImcsException();
		}
		
		return productCdList;
	}
	
	/**
	 * 엔스크린(NSCREEN) 가입 여부 체크
	 * @param paramVO
	 * @throws Exception
	 */
	private String nscreenSubscriptionChk(List<String> productCdList, GetNSContStatRequestVO paramVO) throws Exception
	{
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String sqlId = "lgvod997_007_20180830_001";
		
		List<HashMap<String, String>> albumProducInfotList = new ArrayList<>();
		
		String szMsg = "";
		String retNSubscriptionYn = "N"; // 엔스크린(NSCREEN) 가입 여부
		
		try
		{
			try {
				albumProducInfotList = getNSContStatDao.getNScreenAlbumProducInfotList(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅						
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			for(HashMap<String, String> hm : albumProducInfotList)
			{
				String tempChckProdId_1 = hm.get("P_PRODUCT_ID");
				String tempChckProdId_2 = hm.get("PRODUCT_ID");
				
				for(String proCd : productCdList)
				{
					if(proCd.equals(tempChckProdId_1) || proCd.equals(tempChckProdId_2))
					{
						retNSubscriptionYn = "Y";
						
						break;
					}
				}
				
				if(retNSubscriptionYn.equals("Y"))
					break;
			}
			
			szMsg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID997) + "] SQLID[" + sqlId + "] rcv[" + albumProducInfotList.size() + "]";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
		}
		catch(Exception e)
		{
			//szMsg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID997) + "] SQLID[" + sqlId + "] sts[" + cache.getLastException().getErrorCode() + "] " + 
			//		"msg[" + String.format("%-21s", "nscreen-Subscription-Chk:" + cache.getLastException().getErrorMessage() + "]");
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			paramVO.setResultCode("21000000");
			
			throw new ImcsException();
		}
		
		return retNSubscriptionYn;
	}	
	
	
	/**
	 * 엔스크린(NSCREEN) 가입 여부 체크 - 상품코드 리스트
	 * @param paramVO
	 * @throws Exception
	 */
	private int chkCategory(GetNSContStatRequestVO paramVO) throws Exception
	{
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		int chkCate = 0;
		String szMsg = "";
		
		try
		{
			chkCate = getNSContStatDao.chkCategory(paramVO);
		}
		catch(Exception e)
		{
			chkCate = 0;
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
		return chkCate;
	}
	
	
	/**
	 * 엔스크린(NSCREEN) 가입 여부 체크 - 상품코드 리스트
	 * @param paramVO
	 * @throws Exception
	 */
	private String kidProductCd(GetNSContStatRequestVO paramVO) throws Exception
	{
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		int kidId = 0;
		String result = "";
		String szMsg = "";
		
		try
		{
			kidId = getNSContStatDao.kidProductCd(paramVO);
			
			if(kidId == 1) {
				result="Y";
			} else {
				result="N";
			}
			
		}
		catch(Exception e)
		{
			result="N";
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
		return result;
	}
}
