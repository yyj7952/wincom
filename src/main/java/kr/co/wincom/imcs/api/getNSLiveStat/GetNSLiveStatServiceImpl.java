package kr.co.wincom.imcs.api.getNSLiveStat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import kr.co.wincom.imcs.api.getNSLiveStat.GetNSLiveStatResponseVO;
import kr.co.wincom.imcs.api.getNSLiveStat.GetNSLiveStatResultVO;
import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsCacheService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.DateUtil;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.ComDataFreeVO;
import kr.co.wincom.imcs.handler.ImcsException;
import kr.co.wincom.imcs.common.NosqlCacheType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class GetNSLiveStatServiceImpl implements GetNSLiveStatService {
	private Log imcsLogger		= LogFactory.getLog("API_getNSLiveStat");
	
	@Autowired
	private GetNSLiveStatDao getNSLiveStatDao;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private ImcsCacheService imcsCacheService;
	
	/**
	 * @author HONG
	 * @since 2016-07-12
	 * 컨텐츠 타입과 구매여부, 구매시간, 구매타입 값을 리턴 (lgvod997.pc)
	 */
	@Override
	public GetNSLiveStatResultVO getNSLiveStat(GetNSLiveStatRequestVO paramVO) {
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);

		GetNSLiveStatResponseVO tempVO = new GetNSLiveStatResponseVO();
		GetNSLiveStatResultVO resultVO = new GetNSLiveStatResultVO();

		long tp1			= 0;		// timePoint 1
		long tp2			= 0;		// timePoint 2
		
		String msg			= "";
		int nDataChk		= 0; 		//2017.08.16 엔스크린(NSCREEN) 구매여부 체크		
		
		try {
			// 서버IP정보 조회
			tp1 = System.currentTimeMillis();
			
			String szCurrentDate = "";
			
			try {
				szCurrentDate = commonService.getSysdate();
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			} catch(Exception e) {
				Date today = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMDD");
				szCurrentDate = sdf.format(today);
			}
			
			paramVO.setCurrentDate(szCurrentDate);
			
			// Guest 계정의 경우 처리
			if("M20110725000".equals(paramVO.getSaId())) {
				paramVO.setmProdId("31200");
				
				String cuesheetId = this.getCuesheetInfo(paramVO); // 유료 콘서트 채널 여부 조회
				if(cuesheetId != null && !cuesheetId.equals("")) {
					paramVO.setCuesheetId(cuesheetId);
				}
				
				try {
					resultVO = this.getPPMInfo(paramVO);
					if(resultVO.getList() != null) {
						tempVO = resultVO.getList().get(0);
						
						tempVO.setContsId(paramVO.getAlbumId());
						tempVO.setInappPrice("");
						tempVO.setInappProdId("");
						
						// 2017.08.16 엔스크린(NSCREEN) 페어링 가입자 구매/가입 여부 전달
						tempVO.setnScreenYn("N");
						tempVO.setnBuyYn("N");
						tempVO.setSurtaxRate(Integer.toString(commonService.getSurtaxRate()));
						
						resultVO.getList().set(0, tempVO);
					}
				} catch(Exception e) {}
				
				imcsLog.serviceLog("[Guest 계정입니다.]", methodName, methodLine);
			}			
			else
			{
				String cuesheetId = this.getCuesheetInfo(paramVO); // 유료 콘서트 채널 여부 조회
				if(cuesheetId != null && !cuesheetId.equals("")) {
					paramVO.setCuesheetId(cuesheetId);
				}
				
				paramVO.setCustomUflix("0");
				
				if("".equals(cuesheetId)) { // 주상품 미가입에 대한 Default 응답 설정
					tempVO.setResultType("CON");
					tempVO.setContsType("1");
					tempVO.setBuyYn("1");
					tempVO.setMaxViewingLen("48");
					tempVO.setPrice("0");
				} else {
					paramVO.setUflixBuyYn("1");			// 유플릭스 가입 여부 (default : 미가입)
					paramVO.setSvodProdBuyYn("1");		// 일반상품 구매 여부
					paramVO.setPremiumYn("N");			// 프리미엄 편성 여부
					
//					paramVO.setmProdId(szMProdId);
					
					// 진정한 망 로직의 시작 먀ㅏ너에ㅑ저ㅜ럊버ㅜ레ㅑㅓ제어레머야ㅔ먼에
					// 2018.05.28 - 엔스크린2차 : 엔스크린 Default는 N에서 CP속성(C)으로 수정
					resultVO = this.getPPMInfo(paramVO);		// (0건 또는 실패 시 모두 FAILURE)
					if(resultVO.getList() != null) {
						tempVO = resultVO.getList().get(0);
						
						//통계 로그용
						resultVO.setContentsName(tempVO.getContsNameSt());
						resultVO.setProductId(tempVO.getSubsProdId());
						resultVO.setProductName(tempVO.getSubsProdName());
						
						if("0".equals(tempVO.getContsType()))
							resultVO.setBuyingType("FVOD");
						else if("1".equals(tempVO.getContsType()))
							resultVO.setBuyingType("PPV");
						else if("2".equals(tempVO.getContsType()))
							resultVO.setBuyingType("PVOD");
						else if("7".equals(tempVO.getContsType()))
							resultVO.setBuyingType("PPM");
						
						tp2 = System.currentTimeMillis();
						imcsLog.timeLog("컨텐츠타입, 금액, 지상파여부 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
						
						// 2017.08.16 엔스크린(NSCREEN) - 엔스크린 기능 제공 단말 및 엔스크린 컨텐츠일 경우 페어링된 가입자의 구매/가입 여부를 확인한다.
						tempVO.setnBuyYn("N");
						if(tempVO.getnScreenYn().equals("Y"))
						{
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
									}
								}
								else
								{
									if(tempVO.getContsType().indexOf("0") > -1)
										msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID989) + "] msg[FVOD is not auth(pairing)]";
									else if(tempVO.getBuyYn().indexOf("0") > -1)
										msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID989) + "] msg[BUY is not auth(pairing)]";
									else
										msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID989) + "] msg[SUBSCRIPTION is mobile auth]";
									
									imcsLog.serviceLog(msg, methodName, methodLine);
								}
							}
							else
							{
								msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID989) + "] msg[non pairing (pairing cnt:" + pairList.size() + ")]"; 
								
								imcsLog.serviceLog(msg, methodName, methodLine);
							}
						}
					}
				}
				
				tempVO.setContsId(paramVO.getAlbumId());
				tempVO.setContsId(paramVO.getAlbumId());
				
				if(tempVO.getContsType().equals("0"))
				{
					tempVO.setBuyingDate(szCurrentDate);
				}
				
				// 2020.11.09 - Seamless 단방향
				if(tempVO.getnScreenYn().equals("Y") && tempVO.getnBuyYn().equals("N")) {
					if(tempVO.getGenreSmall().equals("SBS") || tempVO.getGenreSmall().equals("KBS") || tempVO.getGenreSmall().equals("MBC")) {
						tempVO.setnScreenYn("N");
					}
				}
				// 2020.03.18 - 모바일 아이들나라 - 부가세 정보 가져오기.
				// (상세 진입하지 않고, 구매하는 Scene이 있다고 하여, 기존 getNSContInfo / getNSSeriesList 에서 주던 부가세 요율 정보를 getNSLiveStat에서도 제공한다.)
				tempVO.setSurtaxRate(Integer.toString(commonService.getSurtaxRate()));
				
				if(resultVO.getList() != null && !resultVO.getList().isEmpty()){
					resultVO.getList().set(0, tempVO);
				}else{
					resultVO.getList().add(tempVO);
				}
				
			}			
			
			resultVO.setList(resultVO.getList());
			
			Date currentTime = new Date(System.currentTimeMillis());
			SimpleDateFormat fm = new SimpleDateFormat("yyyyMMddHHmmss");
			
			this.imcsCacheService.putEhcacheValue(ImcsCacheService.LIVE_STAT_DATA_CACHE, paramVO.getAlbumId(), resultVO.toString() + "&" + fm.format(currentTime)); // 공통 데이터 캐시에 저장
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
		} finally {
			resultVO.setResultCode(paramVO.getResultCode());
			
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID989) + "] result[" + resultVO.toString() + "]"; 
			
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID989) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
					+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
		}

		return resultVO;
	}

	/**
	 * 월정액 상품정보 조회
	 * @param GetNSChListRequestVO	paramVO
	 * @return String
	 * @throws Exception 
	 **/
	// 2019.04.01 - PPMSTOP으로 인해 가입여부를 받아서 노출 시키지 말아야할 상품을 걸려낸다.
	public GetNSLiveStatResultVO getPPMInfo(GetNSLiveStatRequestVO paramVO) throws Exception{
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		List<GetNSLiveStatResponseVO> responseVOList = new ArrayList<GetNSLiveStatResponseVO>();
		GetNSLiveStatResponseVO responseVO = new GetNSLiveStatResponseVO();
		GetNSLiveStatResponseVO tempVO     = new GetNSLiveStatResponseVO();
		GetNSLiveStatResultVO resultVO	   = new GetNSLiveStatResultVO();
		
		ComDataFreeVO datafreeVO = new ComDataFreeVO();
		
		responseVOList = this.getPPMProdInfo(paramVO);
		
		int nMainCnt		= 0;
		String tmpProdId	= "";
		
		if(responseVOList != null) 
			nMainCnt = responseVOList.size();

		StringBuffer resultHeader = null;
		
		String nScreenYn = "N";
		String nScreenProd = "";
		String[] nScreenTmp;
		String[] genReSmall;
		
		paramVO.setBuyPossibleYn("N");
		responseVO.setTempUflixProdYn("N");
		
		for(int i = 0; i < nMainCnt; i++) {
			tempVO = responseVOList.get(i);
			
			if(i == 0) {												
				// 2017.09.28 엔스크린(NSCREEN) - FVOD 컨텐츠는 nscreen 서비스를 하지 않는다.
				// 2020.01.14 엔스크린 여부 로직 변경 (FVOD 엔스크린 지원 / 월정액의 경우 IPTV와 맵핑된 월정액이 존재해야 지원)
				nScreenTmp = tempVO.getnScreenYnTmp().split(";");
				if(nScreenTmp != null) {
					tempVO.setnScreenYn(nScreenTmp[0]);
					nScreenYn = tempVO.getnScreenYn();
					
					if(tempVO.getnScreenYn().equals("Y")) {						
						tempVO.setnScreenProd(nScreenTmp[1]);
						nScreenProd = tempVO.getnScreenProd();
						
						genReSmall = tempVO.getGenreInfo().split("[|]");
						if(genReSmall.length > 2) {
							responseVO.setGenreSmall(genReSmall[2]);
						}
					}
				}
				
				if(tempVO.getnScreenProd().indexOf("0") >= 0) {
					if(!(tempVO.getnScreenProd().indexOf("1") >= 0 || tempVO.getnScreenProd().indexOf("2") >= 0 || tempVO.getnScreenProd().indexOf("3") >= 0)) {
						nScreenYn = "N";
					}
				}
				
				resultHeader = new StringBuffer("");
				
				String[] payInfo = tempVO.getPayInfo().split("\\^");
				for(int count = 0; count < payInfo.length; count++) {
					if(count == 0) {
						resultHeader.append(tempVO.getSurtaxRate() + "|");
					}
					resultHeader.append(payInfo[count]);
					resultHeader.append("|");
				}
			}
			
			// 2019.12.06 - IPTV 영화월정액 가입자일 경우에는 구매 가능한 콘텐츠가 있을 때에는 영화월정액 상품을 주지 않고, 구매 가능한 콘텐츠가 없을 때에는 영화월정액 상품 정보를 주어 상세 페이지 진입시 오류가 없도록 한다.
			if(tempVO.getContsType().equals("0") || tempVO.getContsType().equals("1") || tempVO.getContsType().equals("2")) {
				paramVO.setBuyPossibleYn("Y");
			}
			
			// 쿼리에서 DISTINCT 를 제거 했기 때문에 추가된 로직
			if(tmpProdId.equals(tempVO.getSubsProdId()))
				continue;
			else
				tmpProdId = StringUtil.nullToSpace(tempVO.getSubsProdId());
			
			// 구매일, 현재일에 대한 날짜 계산 로직 적용 예정 (변수 등 정리필요)
//			DateUtil dateUtil = new DateUtil();
//			Date dExpiredDate = dateUtil.getStringToDate(tempVO.getExpiredDate(), "yyyyMMdd");
//			Date dCurrentTime = dateUtil.getStringToDate(paramVO.getCurrentDate(), "yyyyMMddHHmmss");
			
			// 상품종료일이 지난경우 조회 할 상품에서 제외 한다
//			int nExpiredYn = dExpiredDate.compareTo(dCurrentTime);
//			if(nExpiredYn < 0)	continue;		// 현재 시간이 만료일보다 크면 Y
		
			paramVO.setSuggestedPrice(tempVO.getPrice());
			paramVO.setGenreInfo(tempVO.getGenreInfo());
			paramVO.setProdType(tempVO.getContsType());
			
			//통계 로그용
			responseVO.setContsNameSt(tempVO.getContsNameSt());
			
			// N_SCREEN_YN 값 세팅
			responseVO.setnScreenYn(nScreenYn);
			responseVO.setnScreenProd(nScreenProd);			
			
			if("Y".equals(tempVO.getUflixProdYn())) {
				responseVO.setTempUflixProdYn("Y");
			}
			
			int maxViewingLen = 0;
			
			try {
				maxViewingLen = Integer.parseInt(tempVO.getMaxViewingLenCon());
			} catch (Exception e) {
				maxViewingLen = 0;
			}
			
			if(i == 0) {
				/* 인앱 가격 정보	*/
				/* 컨텐츠 및 데이터 프리의 인앱 가격 가져오기	*/
				
				/* FVOD의 경우 0원으로 인앱 가격을 조회한다.	*/
				if("0".equals(tempVO.getContsType())) {
					datafreeVO.setPrice("0");
				} else {
					datafreeVO.setPrice(tempVO.getPrice());
				}
				
				tempVO.setInappPrice("");
				
				//데이터프리 정보 조회
    			try {
					// 2020.12.22 - 모바일 아이들나라 인앱결제 (모바일TV와 모바일 아이들나라 인앱 분리)
					if(paramVO.getAppType().substring(0,1).equals("A")) {
						datafreeVO.setApprovalGb("A");
					} else if(paramVO.getAppType().substring(0,1).equals("E")) {
						datafreeVO.setApprovalGb("E");
					} else if(paramVO.getAppType().substring(0,1).equals("L")) {
						datafreeVO.setApprovalGb("L");
					} else {
						datafreeVO.setApprovalGb("N");
					}
					datafreeVO = this.getDatafreeInfo(datafreeVO, paramVO);
				} catch(Exception e) {
					
				}

				/* 평생 소장 컨텐츠 인앱 가격 미제공	*/
				if(maxViewingLen/24 > 2000) {
					datafreeVO.setApprovalId("");
					datafreeVO.setApprovalPrice("");
				}
			}
			// 2020.01.13 - PPS도 이제 인앱결제 지원 - 사업팀 결정
			else if(i != 0 && "2".equals(tempVO.getContsType()))
			{
				
				datafreeVO.setPrice(tempVO.getPrice());
				try {
					// 2020.12.22 - 모바일 아이들나라 인앱결제 (모바일TV와 모바일 아이들나라 인앱 분리)
					if(paramVO.getAppType().substring(0,1).equals("A")) {
						datafreeVO.setApprovalGb("A");
					} else if(paramVO.getAppType().substring(0,1).equals("E")) {
						datafreeVO.setApprovalGb("E");
					} else if(paramVO.getAppType().substring(0,1).equals("L")) {
						datafreeVO.setApprovalGb("L");
					} else {
						datafreeVO.setApprovalGb("N");
					}
					datafreeVO = this.getDatafreeInfo(datafreeVO, paramVO);
				} catch(Exception e) {
					
				}
				
				/* 평생 소장 컨텐츠 인앱 가격 미제공	*/
				if(maxViewingLen/24 > 2000) {
					datafreeVO.setApprovalId("");
					datafreeVO.setApprovalPrice("");
				}
			}

			if(i == 0) {
				responseVO.setContsType(tempVO.getContsType());
				responseVO.setMaxViewingLen(tempVO.getMaxViewingLen());
				responseVO.setPrice(tempVO.getPrice());
				
				// PPV일때만 인앱정보 설정
				// 2020.01.13 - PPS도 인앱결제 가능 - 사업팀 결정
				if("1".equals(tempVO.getContsType()) || "2".equals(tempVO.getContsType())) {
					responseVO.setInappPrice(datafreeVO.getApprovalPrice());
					responseVO.setInappProdId(datafreeVO.getApprovalId());
					if("2".equals(tempVO.getContsType())) {
						responseVO.setPvodProdId(tempVO.getSubsProdId());
						responseVO.setPvodProdName(tempVO.getSubsProdName());
					}
				}
			} else {
				responseVO.setDatafreeBuyYn("N");
				
				paramVO.setContsType(tempVO.getContsType());
				
				// 가격 저장
				if("0".equals(tempVO.getContsType())) {
					responseVO.setPrice(responseVO.getPrice() + "\b0");
				} else {
					responseVO.setPrice(responseVO.getPrice() + "\b" + tempVO.getPrice());
					if("2".equals(tempVO.getContsType())) {							
						if(!datafreeVO.getApprovalPrice().equals("")) {
							responseVO.setInappPrice(responseVO.getInappPrice() + "," + datafreeVO.getApprovalPrice());
							responseVO.setInappProdId(responseVO.getInappProdId() + "," + datafreeVO.getApprovalId());
						}
					}
				}
			}

			if(i == 0) {
				if("0".equals(tempVO.getContsType()))
					responseVO.setPrice("0");
				
				paramVO.setContsType(tempVO.getContsType());
			} else {
				responseVO.setContsType(responseVO.getContsType() + "\b" + tempVO.getContsType());
				responseVO.setMaxViewingLen(responseVO.getMaxViewingLen() + "\b" + tempVO.getMaxViewingLen());
				
				if("2".equals(tempVO.getContsType())) {
					responseVO.setPvodProdId(responseVO.getPvodProdId() + "\b" + tempVO.getSubsProdId());
					responseVO.setPvodProdName(responseVO.getPvodProdName() + "\b" + tempVO.getSubsProdName());					
				}
			}
			
//			if(i != 0) {
//				HashMap<String, Object> mDupCk	= new HashMap<String, Object>();
//				
//				try {
//					mDupCk = this.getBuyDupChk(paramVO);
//				} catch(Exception e) {
//					return null;
//				}
//				
//				responseVO.setBuyYn(responseVO.getBuyYn() + "\b" + (String) mDupCk.get("BUY_YN"));
//				responseVO.setBuyingDate(responseVO.getBuyingDate() + "\b" + (String) mDupCk.get("BUY_DATE"));								
//				responseVO.setExpireDate(responseVO.getExpireDate() + "\b" + (String) mDupCk.get("EXP_DATE"));
//				
//				responseVO.setContsType(responseVO.getContsType() + "\b" + tempVO.getContsType());
//				responseVO.setMaxViewingLen(responseVO.getMaxViewingLen() + "\b" + tempVO.getMaxViewingLen());
//				
//				if("2".equals(tempVO.getContsType())) {
//					responseVO.setPvodProdId(responseVO.getPvodProdId() + "\b" + tempVO.getSubsProdId());
//					responseVO.setPvodProdName(responseVO.getPvodProdName() + "\b" + tempVO.getSubsProdName());					
//				}
//			} else {
//				// 상품타입이 FVOD(무료 VOD)의 경우 가격 0원 저장
//				if( "0".equals( tempVO.getContsType() ))
//					responseVO.setPrice("0");
//				
//				paramVO.setContsType(tempVO.getContsType());
//				
//				HashMap<String, Object> mDupCk	= new HashMap<String, Object>();
//				
//				try {
//					mDupCk = this.getBuyDupChk(paramVO);
//				} catch (Exception e) {
//					return null;
//				}
//				
//				responseVO.setBuyYn((String) mDupCk.get("BUY_YN")); //권형도
//				responseVO.setBuyingDate((String) mDupCk.get("BUY_DATE"));
//				responseVO.setExpireDate((String) mDupCk.get("EXP_DATE"));
//			}

			// 상품타입이 PPV(단편구매)의 경우
			if("1".equals(tempVO.getContsType()))
				paramVO.setShortYn("1");
		}

		responseVOList = new ArrayList<GetNSLiveStatResponseVO>(); 
		responseVOList.add(responseVO);
		
		resultVO.setList(responseVOList);
		resultVO.setResultHeader(resultHeader.toString());

		return resultVO;
	}

	/**
	 * 유료 콘서트 채널 여부 조회
	 * @param GetNSChListRequestVO	paramVO
	 * @return GetNSLiveStatResponseVO
	 **/
	public String getCuesheetInfo(GetNSLiveStatRequestVO paramVO) {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String sqlId = "lgvod997_001_20181018_001";
		
		GetNSLiveStatResponseVO cuesheetInfo = null;
		long tp1 = System.currentTimeMillis();

		String cuesheetId = "";
		try {			
			try {
				cuesheetId = getNSLiveStatDao.getCuesheetId(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			long tp2 = System.currentTimeMillis();
	        imcsLog.timeLog("getCuesheetInfo 정보 조회", String.valueOf(tp2 - tp1), methodName, "");
		} catch (Exception e) {
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}
		
		return cuesheetId;
	}
	
	
	/**
	 * 	구매중복 체크
	 * 	1row와 1row 아닌 경우에 따라 해당 로직을 이용하는 경우가 달라 별로 생성함
	 * 	@param 	GetNSChListRequestVO
	 * 	@return HashMap<String, Object>
	 */
	public HashMap<String, Object> getBuyDupChk(GetNSLiveStatRequestVO paramVO) {
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
			if("0".equals(paramVO.getContsType())) 		mDupChk = getNSLiveStatDao.getBuyDupChk1(paramVO);
			// PPV 구매 중복 체크
			else if("1".equals(paramVO.getContsType()))	mDupChk = getNSLiveStatDao.getBuyDupChk2(paramVO);
			// PVOD 구매 중복 체크
			else if("2".equals(paramVO.getContsType()))	mDupChk = getNSLiveStatDao.getBuyDupChk3(paramVO);
			
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			
			nDupChk = (Integer) mDupChk.get("DATA_CHK");
			szBuyDate	= StringUtil.nullToSpace((String) mDupChk.get("BUY_DATE"));
			szExpDate	= StringUtil.nullToSpace((String) mDupChk.get("EXP_DATE"));

			if(mDupChk != null && !mDupChk.isEmpty()) {
				// 만료일 시작이 0으로 시작하면 공백 세팅
				if(!"".equals(szExpDate)) {
					if("0".equals(szExpDate.substring(0)))		szExpDate	= "";
				}
			}
			
			if(nDupChk > 0) { //구매
				szBuyYn		= "0";
			} else { //미구매
				szBuyYn 	= "1";
				szBuyDate	= "";
				szExpDate	= "";
			}
			
			mDupChk.clear();
			mDupChk.put("BUY_YN", szBuyYn);
			mDupChk.put("BUY_DATE", szBuyDate);
			mDupChk.put("EXP_DATE", szExpDate);

		} catch(Exception e) {
			throw new ImcsException();
		}
		
		return mDupChk;
	}

	/**
	 * PPM 월정액 정보 조회
	 * @param GetNSChListRequestVO	paramVO
	 * @return List<GetNSSvodInfoVO>
	 **/
	public List<GetNSLiveStatResponseVO> getPPMProdInfo(GetNSLiveStatRequestVO paramVO) {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		
		String szMsg = "";
		String sqlId = "lgvod997_f77_20180830_001";
		
		List<GetNSLiveStatResponseVO> list = new ArrayList<GetNSLiveStatResponseVO>();
		long tp1 = System.currentTimeMillis();
		
		try {
			try {
				list = getNSLiveStatDao.getPPMProdInfo(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			long tp2 = System.currentTimeMillis();
	        imcsLog.timeLog("getPPMProdInfo 정보 조회", String.valueOf(tp2 - tp1), methodName, "");
			
			if(list == null || list.isEmpty()) {
				throw new ImcsException();
			}
		} catch(Exception e) {
			throw new ImcsException();
		}
					
		return list;
	}
	
	/**
     * 데이터프리 정보 조회
     * @param	ComDataFreeVO, GetNSLiveStatRequestVO
     * @result	ComDataFreeVO
    **/
    public ComDataFreeVO getDatafreeInfo(ComDataFreeVO tempVO, GetNSLiveStatRequestVO paramVO) throws Exception{
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
			try {
				list = getNSLiveStatDao.getDatafreeInfo(tempVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			} catch(DataAccessException e) {
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			long tp2 = System.currentTimeMillis();
	        imcsLog.timeLog("getDatafreeInfo 정보 조회", String.valueOf(tp2 - tp1), methodName, "");
			
			if(list != null && !list.isEmpty()) {
				resultVO = (ComDataFreeVO)list.get(0);
				
				// 2020.01.02 - 인앱결제는 데이터프리 구매를 지원하지 않는다.
				resultVO.setDatafreeApprovalId("");
				resultVO.setDatafreeApprovalPrice("");
				resultVO.setPpvDatafreeApprovalId("");
				resultVO.setPpvDatafreeApprovalPrice("");
				
				querySize = list.size();
			} else {
				resultVO = new ComDataFreeVO();
				resultVO.setDatafreePrice("0");
				resultVO.setApprovalPrice("");
				resultVO.setDatafreeApprovalPrice("");
				resultVO.setPpvDatafreeApprovalPrice("");
			}
		} catch(Exception e) {

		}
    	return resultVO;
    }
	
	/**
	 * 엔스크린 기능 제공 단말 및 엔스크린 컨텐츠일 경우 페어링된 가입자의 구매/가입 여부를 확인
	 * @param GetNSChListRequestVO	paramVO
	 * @return boolean
	 **/
	@SuppressWarnings("rawtypes")
	private List<HashMap> getNScreenPairingInfo(GetNSLiveStatRequestVO paramVO)
	{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId 		= "lgvod997_005_20180830_001";
		
		List<HashMap> list = null;

		try {
			try {
				list = getNSLiveStatDao.getNScreenPairingInfo(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			if(list == null)
				list = new ArrayList<>();
			
		} catch (Exception e) {
//			imcsLog.failLog(ImcsConstants.API_PRO_ID989, sqlId, null, "getNScreenPairingInfo:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
		return list;
	}
	
	/**
	 * 20170817 엔스크린(NSCREEN) 구매 여부 체크
	 * 2017.08.16 엔스크린(NSCREEN) - 예약구매는 구매했다고 줄 필요 없을 것으로 봄
	 * @param	GetNSChListRequestVO
	 * @result	Integer
	 */
	public HashMap<String, String> nScreenBuyChk(GetNSLiveStatRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		
		HashMap<String, String> hm = null;
		String sqlId	= "lgvod178_025_20171214_001";		
		
		try {
			hm = getNSLiveStatDao.getNScreenBuyChk(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			
//			imcsLog.dbLog(ImcsConstants.API_PRO_ID989, sqlId, null, hm.size(), methodName, methodLine);
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			throw new ImcsException(ImcsConstants.FAIL_CODE, e);
		}
		
		return hm;
	}

}
