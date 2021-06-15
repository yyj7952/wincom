package kr.co.wincom.imcs.api.getNSProdinfo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.ComCdVO;
import kr.co.wincom.imcs.common.vo.ComSbcVO;
import kr.co.wincom.imcs.common.vo.StillImageVO;
import kr.co.wincom.imcs.handler.ImcsException;
import kr.co.wincom.imcs.common.NosqlCacheType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class GetNSProdinfoServiceImpl implements GetNSProdinfoService {
	private Log imcsLogger = LogFactory.getLog("API_getNSProdInfo");
	
	@Autowired
	private GetNSProdinfoDao getNSProdinfoDao;
	
	@Autowired
	private CommonService commonService;

//	public void getNSProdinfo(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	@Override
	public GetNSProdinfoResultVO getNSProdinfo(GetNSProdinfoRequestVO paramVO){
//		this.getNSProdinfo(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	

		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		GetNSProdinfoResultVO resultListVO	= new GetNSProdinfoResultVO();
	
		String msg	= "";
		
		int nMainCnt = 0;
		int nSubCnt = 0;
		int i_loop_cnt = 0;			/* i_loop_cnt는 조회된 상품 중 처음에 한번만 부가세요율정보를 가져오기 위해 사용*/		
		int mem_loop_flag = 0;					/* 멤버쉽 정기차감 조회 했는지 여부 (0 : 조회안함 / 1 : 조회함) - 가입상품이 있는 가입자만 조회하기 위해 */

	    long tp_start = paramVO.getTp_start();
		long tp1 = 0;
		long tp2 = 0;
		long tp3 = 0;
		long tp4 = 0;
		
		String szPpmImgSvrip = "";
		String nSurtaxRate			= "";
		
		try {
			szPpmImgSvrip	= commonService.getIpInfo("img_ppm_server", ImcsConstants.API_PRO_ID004.split("/")[1]);
		} catch(Exception e) {
			//imcsLog.failLog(ImcsConstants.API_PRO_ID004, "", null, "svr_ip:" + ImcsConstants.RCV_MSG1, methodName, methodLine);
			throw new ImcsException();
		}
		
	    
		try{
			// 상품정보 조회	- sysdate 사용으로 NOSQL 적용 안함
						GetNSProdinfoResponseVO tempVO = new GetNSProdinfoResponseVO();
						GetNSProdinfoResponseVO memberVO = new GetNSProdinfoResponseVO();
						List<GetNSProdinfoResponseVO> resultVO = new ArrayList<GetNSProdinfoResponseVO>();
						List<GetNSProdinfoResponseVO> returnVO = new ArrayList<GetNSProdinfoResponseVO>();		// 리턴용 0, 1만 리턴
			
			// FX_TYPE이 M, P, T, H 일 경우 가상채널장치 코드 조회
			if( "M".equals(paramVO.getFxType()) || "P".equals(paramVO.getFxType()) || "T".equals(paramVO.getFxType()) || "H".equals(paramVO.getFxType()) ){
				ComSbcVO sbcVO = new ComSbcVO();
				sbcVO = this.getAtrctChnlDvCd(paramVO);
				
				if(sbcVO != null) {
					paramVO.setAtrctChnlDvCd(sbcVO.getChnlDvCd());
					paramVO.setPvsNscnCustNo(sbcVO.getCustNo());
				}
			}else{
				paramVO.setAtrctChnlDvCd("N");
				paramVO.setPvsNscnCustNo("");
			}
			tp1	= System.currentTimeMillis();	    	
			imcsLog.timeLog("HDTV PROD SBC 조회", String.valueOf((tp1 - tp_start)), methodName, methodLine);
	
			//파일 일기는 읽어오는 값을 따로 쓰기 않아 구현X
			
			// 테스트 가입자 여부 조회
			this.getTestSbc(paramVO);
			
			// 가입상품 메뉴명 및 코드
			String joinMenuCd = paramVO.getJoinMenu().split(",")[0];
			String joinMenuNm = paramVO.getJoinMenu().split(",")[1];
						
			tp3	= System.currentTimeMillis();
			imcsLog.timeLog("공통코드 조회, 가입자 정보 조회", String.valueOf((tp3 - tp_start)), methodName, methodLine);
	
			
			
			// 가입자의 구매상품정보 조회
			List<HashMap<String, String>> lstCustomerProd = new ArrayList<HashMap<String, String>>();
			HashMap<String, String> mCustomerProd = new HashMap<String, String>();
			
			String szNscProd	= "";
			String szCustomProd	= "";
			String szComcd		= "";
			String szCustomExp	= "";
			
			// 가입자의 구매 상품정보 조회
			try {
				nSubCnt	= 0;
				lstCustomerProd = getNSProdinfoDao.getCutsomProdList(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
				if(lstCustomerProd != null && lstCustomerProd.size() > 0) {
					nSubCnt	= lstCustomerProd.size();
				} else {
					//imcsLog.failLog(ImcsConstants.API_PRO_ID004, "", null, "nscprod_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
				}
				
				for(int i = 0; i < nSubCnt; i++) {
					mCustomerProd	= lstCustomerProd.get(i);
					szNscProd	= szNscProd + "PROD_ID=" + mCustomerProd.get("PROD_CD") + "|ENTRYDATE=" +  mCustomerProd.get("ENTRYDATE") + "|\f";
				}
				
			} catch (Exception e) {
				//imcsLog.failLog(ImcsConstants.API_PRO_ID004, "", null, "nscprod_info:" + ImcsConstants.RCV_MSG2, methodName, methodLine);
				paramVO.setResultCode("40000000");
				
				throw new ImcsException();
			}
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("가입자의 구매 상품정보 조회", String.valueOf((tp2 - tp3)), methodName, methodLine);
			paramVO.setCustomUflix(0);
			
			// 가입자의 가입 상품정보 조회
			try {
				nSubCnt	= 0;
				lstCustomerProd = getNSProdinfoDao.getCutsomProdList2(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
				if(lstCustomerProd != null && lstCustomerProd.size() > 0) {
					nSubCnt	= lstCustomerProd.size();
				} else {
					//imcsLog.failLog(ImcsConstants.API_PRO_ID004, "", null, "custprod_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
				}
				
				for(int i = 0; i < nSubCnt; i++) {
					mCustomerProd	= lstCustomerProd.get(i);
					if(!"D".equals(mCustomerProd.get("PROD_DEL_FLAG"))) {
						szCustomProd	= szCustomProd + "PROD_ID=" + mCustomerProd.get("PROD_CD") + "|XCION_PROD_CD=" +  mCustomerProd.get("UCUBE_PROD_CD")
								+ "|ENTRYDATE=" +  mCustomerProd.get("ENTRYDATE") + "|\f";
						
						szCustomExp	= szCustomExp + "PROD_ID=" + mCustomerProd.get("PROD_CD") + "|XCION_PROD_CD=" +  mCustomerProd.get("UCUBE_PROD_CD")
								+ "|EXPIRE_YN=Y|\f";
	
						if("Y".equals(mCustomerProd.get("UFLIXYN"))) {
							if(paramVO.getCustomUflix() == 0){
								if(!"0".equals(mCustomerProd.get("CONCURRENT_COUNT"))){
									paramVO.setCustomUflix(2);
								}
								else {
									paramVO.setCustomUflix(1);
								}
							}
						}
					}
				}
			} catch(Exception e) {
				//imcsLog.failLog(ImcsConstants.API_PRO_ID004, "", null, "custprod_info:" + ImcsConstants.RCV_MSG2, methodName, methodLine);
				paramVO.setResultCode("40000000");
				
				throw new ImcsException();
			}
			tp3	= System.currentTimeMillis();
			imcsLog.timeLog("가입자의 가입 상품정보 조회", String.valueOf((tp3 - tp2)), methodName, methodLine);
			

			List<HashMap<String, String>> lstEventrProd = new ArrayList<HashMap<String, String>>();
			int event_prod_cnt = 0;
			
			// 가입자의 가입가능 상품정보 조회
			try {
				nSubCnt	= 0;
				lstCustomerProd = getNSProdinfoDao.getCutsomProdList3(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
				if(lstCustomerProd != null && lstCustomerProd.size() > 0) {
					nSubCnt	= lstCustomerProd.size();
				} else {
					//imcsLog.failLog(ImcsConstants.API_PRO_ID004, "", null, "hdtvprod_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
				}
				
				for(int i = 0; i < nSubCnt; i++) {
					mCustomerProd	= lstCustomerProd.get(i);
					
					if( "N".equals(mCustomerProd.get("EVENT_YN")) ){
						szComcd	= szComcd + "XCION_PROD_CD=" + mCustomerProd.get("UCUBE_PROD_CD") + "|\f";
					}else{
						
						lstEventrProd.add(mCustomerProd);
						
						event_prod_cnt++;
					}
					
				}
			} catch(Exception e) {
				//imcsLog.failLog(ImcsConstants.API_PRO_ID004, "", null, "hdtvprod_info:" + ImcsConstants.RCV_MSG2, methodName, methodLine);
				paramVO.setResultCode("40000000");
				
				throw new ImcsException();
			}
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("가입자의 가입가능 상품정보 조회", String.valueOf((tp2 - tp3)), methodName, methodLine);

			
/*			// 상품정보 조회	- sysdate 사용으로 NOSQL 적용 안함
			GetNSProdinfoResponseVO tempVO = new GetNSProdinfoResponseVO();
			GetNSProdinfoResponseVO memberVO = new GetNSProdinfoResponseVO();
			List<GetNSProdinfoResponseVO> resultVO = new ArrayList<GetNSProdinfoResponseVO>();
			List<GetNSProdinfoResponseVO> returnVO = new ArrayList<GetNSProdinfoResponseVO>();		// 리턴용 0, 1만 리턴
			*/
			try {
				if("A".equals(paramVO.getProductFlag())) {
					resultVO = getNSProdinfoDao.getNSProdinfoList(paramVO);					
				} else if("P".equals(paramVO.getProductFlag()) || "G".equals(paramVO.getProductFlag()) || "K".equals(paramVO.getProductFlag())) {
					resultVO = getNSProdinfoDao.getNSProdinfoList2(paramVO);
				}
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
				if(resultVO != null)	nMainCnt = resultVO.size();
				else{
					nMainCnt	= 0;
					//imcsLog.failLog(ImcsConstants.API_PRO_ID004, "", null, "prod_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
				}
			} catch (Exception e) {
				//imcsLog.failLog(ImcsConstants.API_PRO_ID004, "", null, "prod_info:" + ImcsConstants.RCV_MSG2, methodName, methodLine);
				paramVO.setResultCode("40000000");
				
				throw new ImcsException();
			}
			tp3	= System.currentTimeMillis();	    	
			imcsLog.timeLog("상품정보 조회", String.valueOf((tp3 - tp2)), methodName, methodLine);
			
			
			// 가입 상품정보 Fetch
			for(int i = 0; i < nMainCnt; i++){
				tempVO = resultVO.get(i);
				tempVO.setEntrydate("");
				tempVO.setSubYn("N");
				
				int nUseScreenMobile	= Integer.parseInt(StringUtil.nullToZero(tempVO.getUseScreenMobile()));
				
				if( nUseScreenMobile >= 1 && nUseScreenMobile <= 8 ){
					tempVO.setScreen("M");
				}
				
				if( nUseScreenMobile % 7 >= 2 && nUseScreenMobile % 7 <= 5 ){
					if(!"".equals(tempVO.getScreen()))		tempVO.setScreen(tempVO.getScreen()+"\bP");
					else									tempVO.setScreen("P");
				}
				
				if( nUseScreenMobile % 7 == 3 || nUseScreenMobile % 7 == 4 || nUseScreenMobile % 7 == 6 || nUseScreenMobile == 8 || nUseScreenMobile == 14){
					if(!"".equals(tempVO.getScreen()))		tempVO.setScreen(tempVO.getScreen()+"\bT");
					else									tempVO.setScreen("T");
				}
				
				if( nUseScreenMobile % 7 == 4 || nUseScreenMobile % 7 == 5 || nUseScreenMobile % 8 == 6 ){
					if(!"".equals(tempVO.getScreen()))		tempVO.setScreen(tempVO.getScreen()+"\bH");
					else									tempVO.setScreen("H");
				}
				
				//if(i == 0 ){
				if (i_loop_cnt == 0) {					
					tp1	= System.currentTimeMillis();	    	
					nSurtaxRate = this.getSurtaxRateInfo(paramVO);
					tp4	= System.currentTimeMillis();	    	
					
					imcsLog.timeLog("가입자의 가입가능 상품정보 조회", String.valueOf((tp4 - tp1)), methodName, methodLine);
				}

				tempVO.setSurtaxRate(nSurtaxRate);

				// 구매상품 처리 시작
				Boolean bExist	= false;
				String szCompareVal	= "";
				String entryDate = "";
				
				// 2021.01.05 - 모바일 아이들나라4.0_phase2 : IPTV 상품은 가입상품만 보여 주므로 가입여부를 체크할 필요가 없다.
				if(tempVO.getScreenInfo().equals("N"))
				{
					if("1".equals(tempVO.getProdType())){
						// 조회된 구매상품 리스트에 동일한 상품이 존재하면 '가입'으로 리턴
						szCompareVal	= "PROD_ID=" + tempVO.getSubProdId()+ "|ENTRYDATE=";
						
						entryDate = this.gf_getProdInfo(szNscProd, 1, szCompareVal);
						
						if(!"FAIL".equals(entryDate)){
							bExist	= true;
							
							tempVO.setEntrydate(entryDate);
						}
						
						
						/*if(szNscProd.contains(szCompareVal)){
							bExist	= true;
						}*/
							
						
						if(bExist && ("0".equals(tempVO.getViewCtrl()) || "2".equals(tempVO.getViewCtrl()))){
							tempVO.setSubYn("0");	// 가입
						}
						
						if(!bExist && ("Y".equals(tempVO.getExpiredYn()) && ( "0".equals(tempVO.getViewCtrl()) || "3".equals(tempVO.getViewCtrl()) ) ) ){
							tempVO.setSubYn("1");	// 미가입
						}
					}
					
					
					// 가입상품 처리 시작
					else {		
						// 조회된 가입상품 리스트에 동일한 상품이 존재하면 '가입'으로 리턴
						// 서브 상품 ID와 유큐브상품 ID 같이 비교
						szCompareVal	= "PROD_ID=" + tempVO.getSubProdId() + "|XCION_PROD_CD=" + tempVO.getUcubeProdCd() + "|ENTRYDATE=";
						
						entryDate = this.gf_getProdInfo(szCustomProd, 1, szCompareVal);
						
						if(!"FAIL".equals(entryDate)){
							bExist	= true;
							
							tempVO.setEntrydate(entryDate);
						}
						
						/*if(szCustomProd.contains(szCompareVal)){
							bExist	= true;
						}*/						
						
						
						if(bExist && "N".equals(tempVO.getProdSubYn()) && ("0".equals(tempVO.getViewCtrl()) || "2".equals(tempVO.getViewCtrl()))) {
							tempVO.setSubYn("0");	// 가입
						} else {
							bExist	= false;
							
							szCompareVal	= "PROD_ID=" + tempVO.getSubProdId();
							
							if(szCustomProd.contains(szCompareVal))
								bExist	= true;
							
								
						
							if(!bExist && "Y".equals(tempVO.getExpiredYn()) && ( "0".equals(tempVO.getViewCtrl()) || "3".equals(tempVO.getViewCtrl()) )){
								String szTempChnlDvCd	= tempVO.getAtrctChnlDvCd();
								if(szTempChnlDvCd.length() != 0 && paramVO.getAtrctChnlDvCd().length() != 0 && szTempChnlDvCd.length() > paramVO.getAtrctChnlDvCd().length())
									szTempChnlDvCd	= szTempChnlDvCd.substring(0, paramVO.getAtrctChnlDvCd().length());
								
								if("Y".equals(tempVO.getUflixYn()) && "N".equals(tempVO.getProdSubYn()) && paramVO.getAtrctChnlDvCd().equals(szTempChnlDvCd)) {
									tempVO.setSubYn("1");
								} else {
									
									bExist	= false;
									
									szCompareVal	= "XCION_PROD_CD=" + tempVO.getUcubeProdCd();
									if(szComcd.contains(szCompareVal))
										bExist	= true;
									
									if(bExist && "1".equals(tempVO.getNscProdKind()))
										tempVO.setSubYn("1");	// 미가입
								}
							}
						}
					}
					
					//미가입 상품 중 이벤트 상품 가입 여부 확인
					if("1".equals(tempVO.getSubYn())){
						
						bExist	= false;
						
						for(int j = 0; j < event_prod_cnt; j++){
							
							if(tempVO.getSubProdId().equals(lstEventrProd.get(j).get("PRDO_CD"))){
								szCompareVal	= "PROD_ID=" + tempVO.getUcubeProdCd(); // 이벤트 상품 정보는 유큐드 상품 코드 부분에도 방송센터 상품코드가 들어감
								
								if(szComcd.contains(szCompareVal))
									bExist	= true;
								
								if(bExist) break;
							}
						}
						
						//이벤트 상품 가입 확인 시 원 상품 데이터 출력 X
						if(bExist) {
							i_loop_cnt++;
							continue;
						}
					}
				}
				else
				{
					// 2021.01.05 - 모바일 아이들나라4.0_phase2 : IPTV 상품은 가입상품만 보여 주므로 무조건 가입으로 전달
					tempVO.setSubYn("0");	// 가입
					
					// 가입날짜 제공을 위해 추가
					szCompareVal	= "PROD_ID=" + tempVO.getSubProdId() + "|XCION_PROD_CD=" + tempVO.getUcubeProdCd() + "|ENTRYDATE=";
					
					entryDate = this.gf_getProdInfo(szCustomProd, 1, szCompareVal);
					
					if(!"FAIL".equals(entryDate)){
						tempVO.setEntrydate(entryDate);
					}
				}
				
				//현재 로직상 무조건 Y
				/*String expiredYn = "";
				
				szCompareVal = "PROD_ID=" + tempVO.getSubProdId() + "|XCION_PROD_CD=" + tempVO.getUcubeProdCd() + "|EXPIRE_YN=";
				
				expiredYn = gf_getProdInfo(szCustomExp, 1, szCompareVal);
				
				if(!"FAIL".equals(expiredYn)){					
					tempVO.setExpiredYn(expiredYn);
				}		*/	
				
				tempVO.setExpiredYn("Y");
				
				if(!"N".equals(tempVO.getSubYn())) {
					//2018.12.06 - 신규 유플릭스 상품 추가
					if("Y".equals(tempVO.getUflixYn()) && "2".equals(tempVO.getProdType()) && "1".equals(tempVO.getSubYn())) {
						if("0".equals(tempVO.getConcurrentCount())){
							switch (paramVO.getCustomUflix()) {
							case 0:
//								tempVO.setUflixPop("2");
//								tempVO.setUflixPopMsg("구 요금제는 더이상 가입이 불가합니다.");
								tempVO.setUflixPop("9");
								tempVO.setUflixPopMsg("");
								break;
							case 1:
								tempVO.setUflixPop("0");
								tempVO.setUflixPopMsg("");
								break;
							case 2:
								tempVO.setUflixPop("9");
								tempVO.setUflixPopMsg("");
								break;
							default:
								break;
							}
						} else {
							switch (paramVO.getCustomUflix()) {
							case 0:
								tempVO.setUflixPop("0");
								tempVO.setUflixPopMsg("");
								break;
							case 1:
								tempVO.setUflixPop("1");
								tempVO.setUflixPopMsg("기존 상품 해지후 가입이 가능합니다.");
								break;
							case 2:
								tempVO.setUflixPop("1");
								tempVO.setUflixPopMsg("기존 상품 해지후 가입이 가능합니다.");
								break;
							default:
								break;
							}
						}
						
						if("9".equals(tempVO.getUflixPop())) {
							i_loop_cnt++;
							continue;
						}
					}
					if(!"".equals(tempVO.getProdType())){
						List<StillImageVO> lImageVO = new ArrayList<StillImageVO>();
						StillImageVO imageVO = new StillImageVO();
						
						paramVO.setSubProdId(tempVO.getSubProdId());
						
						// 요금제 이미지 조회
						
						try {
							lImageVO = getNSProdinfoDao.getImage(paramVO);
							paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
							
							if(lImageVO != null && lImageVO.size() > 0){
								imageVO = lImageVO.get(0);
								
								if(!"".equals(imageVO.getImgUrl())){
									tempVO.setImgUrl(szPpmImgSvrip);
								}
								//tempVO.setImgUrl(imageVO.getImgUrl());
								tempVO.setImgFileName(imageVO.getImgFileName());
							}
						} catch (Exception e) {
							//imcsLog.failLog(ImcsConstants.API_PRO_ID004, "", null, "img_info:" + ImcsConstants.RCV_MSG2, methodName, methodLine);
							paramVO.setResultCode("40000000");
							
							throw new ImcsException();
						}
					}
					
					 /* 신규 유플릭스 타입이고 SVOD이면 부가세제외하여 계산 (DB 마이그레이션 안하고 신규 타입의 경우에 원가격 정보로 주기 위하여)  */
					if( !"1".equals(tempVO.getProdType()) && "V".equals(paramVO.getUflixYn()) ){
						/*상품가격 (부가세제외) 계산*/
						tempVO.setSubProdPrice( String.format("%.0f", Float.parseFloat(tempVO.getSubProdPrice()) / (float)1.1 ) );
					}
					
					tempVO.setMemDeductionYn("N");
					
					// 2021.01.05 - 모바일 아이들나라4.0_phase2 : IPTV 상품은 멤버십 정기차감이 없으므로 멤버십 정보를 조회할 필요가 없다.
					if("0".equals(tempVO.getSubYn()) && tempVO.getScreenInfo().equals("N")){
						
						if(mem_loop_flag == 0){
							
							try {
								/* 멤버십 정기차감 정보 조회*/
								memberVO = getNSProdinfoDao.getMemberDeductionInfo(paramVO);
								paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
								
							} catch (Exception e) {
								//imcsLog.failLog(ImcsConstants.API_PRO_ID004, "", null, "mem_info:" + ImcsConstants.RCV_MSG2, methodName, methodLine);
								throw new ImcsException();
							}
														
							mem_loop_flag = 1;
							
						}
						
						if(memberVO != null){
							//2017.02.17 멤버쉽 월정액 가입자인지 판단
							if(tempVO.getUcubeProdCd().equals(memberVO.getMemProdCd())){
								tempVO.setMemDeductionYn("Y");
								tempVO.setMemDeductionPrice(memberVO.getMemDeductionPrice());
							}
						}
						
						
						msg = " 가입자 가입상품 [custom_product] [" + tempVO.getSubProdId() + "|" + tempVO.getSubProdName() + "]";
						imcsLog.serviceLog(msg, methodName, methodLine);
						
						tempVO.setGroupCd(joinMenuCd);
						tempVO.setGroupNm(joinMenuNm);
						
						returnVO.add(tempVO);
						
					}else if("1".equals(tempVO.getSubYn())) {
						
						returnVO.add(tempVO);
						
					}
					// 2021.01.05 - 모바일 아이들나라4.0_phase2 : IPTV 상품은 응답값 set
					else if(tempVO.getScreenInfo().equals("I"))
					{
						returnVO.add(tempVO);
					}
					
				}
				
				i_loop_cnt++;
			}			
			
			tp2	= System.currentTimeMillis();	    	
			imcsLog.timeLog("가입 상품정보 Fetch", String.valueOf((tp2 - tp3)), methodName, methodLine);
			
			// 상품 카테고리 정렬
			if("A".equals(paramVO.getProductFlag())) {
				Collections.sort(returnVO, new Comparator<GetNSProdinfoResponseVO>() {
					public int compare(GetNSProdinfoResponseVO o1, GetNSProdinfoResponseVO o2) {
						return o1.getSubYn().compareTo(o2.getSubYn());
					}
				});				
			}
			resultListVO.setList(returnVO);
		} catch(ImcsException ie) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E", ie.getClass().getName() + ":" + ie.getMessage());
		} catch(Exception e) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		} finally{
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID004) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);			
		}
		
		return resultListVO;
	}
	
	
	
	/**
	 * 가상채널 장치 코드 조회
	 * @param paramVO
	 * @return
	 */
    public ComSbcVO getAtrctChnlDvCd(GetNSProdinfoRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId =  "lgvod004_001_20171214_001";

		
		List<ComSbcVO> list   = new ArrayList<ComSbcVO>();
		ComSbcVO resultVO = null;
		
		try {
			try{
				list = getNSProdinfoDao.getAtrctChnlDvCd(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list != null && !list.isEmpty()){
				resultVO = list.get(0);
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
		return resultVO;
    }
    
    
    
    /**
	 * prodsbc 조회
	 * @param paramVO
	 * @return
	 */
    public List<ComCdVO> getProdSbc(GetNSProdinfoRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId = "lgvod004_002_20171214_001";
		
		List<ComCdVO> list   = new ArrayList<ComCdVO>();
				
		try {
			try{
				list = getNSProdinfoDao.getProdSbc();
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list == null || list.isEmpty()){
				//imcsLog.failLog(ImcsConstants.API_PRO_ID004, "", cache, "periode_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
			}
			
		} catch (Exception e) {
			//imcsLog.failLog(ImcsConstants.API_PRO_ID004, sqlId, cache, "periode_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			throw new ImcsException();
		}
		
		return list;
    }
    
    
    
    
    /**
   	 * 검수 STB 여부 조회
   	 * @param paramVO
   	 * @return
   	 */
    public void getTestSbc(GetNSProdinfoRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId =  "lgvod004_003_20171214_001";
		
		List<HashMap<String, String>> listTestSbc = new ArrayList<HashMap<String, String>>();
		
		try {
			try{
				listTestSbc = getNSProdinfoDao.getTestSbc(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( listTestSbc == null || listTestSbc.isEmpty()){
				paramVO.setTestSbc("N");
				paramVO.setParingSaId("X");
				paramVO.setParingStbMac("X");
				paramVO.setJoinMenu("");
			} else {
				paramVO.setTestSbc(StringUtil.replaceNull(listTestSbc.get(0).get("TEST_SBC"), "N"));
				paramVO.setParingSaId(listTestSbc.get(0).get("PAIRING_SAID"));
				paramVO.setParingStbMac(listTestSbc.get(0).get("PAIRING_MAC"));
				paramVO.setJoinMenu(listTestSbc.get(0).get("JOIN_MENU"));
			}
		} catch (Exception e) {
			paramVO.setTestSbc("N");
			paramVO.setParingSaId("X");
			paramVO.setParingStbMac("X");
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
    }
    
    /**
	 * 부가세 요율 조회 (NoSql)
	 * @param 	
	 * @return  String
	 **/
	public String getSurtaxRateInfo(GetNSProdinfoRequestVO paramVO) throws Exception {
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
		
		String sqlId = "lgvod004_004_20171214_001";
				
		List<String> list = new ArrayList<String>();
		String nSurtaxRate	= "";
		
		try {
			try {
				list = getNSProdinfoDao.getSurtaxRateInfo();
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			

			if (list == null || list.isEmpty()) {
				//imcsLog.failLog(ImcsConstants.API_PRO_ID004, "", cache, "SURTAXRATE INFO:Not Found", methodName, methodLine);				
			} else {
				nSurtaxRate	= list.get(0);
			}
		} catch (Exception e) {
			//if(cache.getLastException() != null) 	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
			//else									paramVO.setResultCode("41000000");
			//imcsLog.failLog(ImcsConstants.API_PRO_ID004, sqlId, cache, "SURTAXRATE INFO::" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			
			throw new ImcsException();
		}

		return nSurtaxRate;
	}
	
	/**
   	 * 요금제 정보 자르는 함수
   	 * @param strInput, nPos, strCatID
   	 * @return
   	 */   
	public String gf_getProdInfo(String strInput, int nPos, String strCatID){
		
		//int nReturn = -1;
		
		String strReturn="FAIL";
		
		int nRoop = 0;
		int eq_idx = 0;
		int eq_idx2 = 0;
		
		String ptr1 = strInput;
		
		if(ptr1.length() == 0){
			return strReturn;
		}
		
		String ptr2="";
		
		for(nRoop =0; nRoop<nPos; nRoop++){
			eq_idx = ptr1.indexOf(strCatID);
			
			if(eq_idx == -1){
				break;
			}
			
			ptr1 = ptr1.substring(eq_idx+strCatID.length(), ptr1.length());
			if(ptr1 == null) break;
			ptr2 = ptr1;
			
			if((nRoop+1) == nPos){
								
				eq_idx2 = ptr1.indexOf("|");
				
				if(eq_idx2 != -1){
					
					strReturn = ptr2.substring(0, eq_idx2);	
					
					//nReturn = 1;
				}
				
				break;
				
			}
			
		}
		
		return strReturn;
	}
   
}
