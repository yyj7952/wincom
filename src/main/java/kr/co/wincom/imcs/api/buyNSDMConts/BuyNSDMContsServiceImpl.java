package kr.co.wincom.imcs.api.buyNSDMConts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsCacheService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.FileUtil;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.ComCpnVO;
import kr.co.wincom.imcs.common.vo.ComDataFreeVO;
import kr.co.wincom.imcs.common.vo.ComDupCHk;
import kr.co.wincom.imcs.common.vo.ComPriceVO;
import kr.co.wincom.imcs.common.vo.ComSbcVO;
import kr.co.wincom.imcs.common.vo.ContTypeVO;
import kr.co.wincom.imcs.common.vo.GenreInfoVO;
import kr.co.wincom.imcs.common.NosqlCacheType;
import kr.co.wincom.imcs.handler.ImcsException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class BuyNSDMContsServiceImpl implements BuyNSDMContsService {
	private Log imcsLogger		= LogFactory.getLog("API_buyNSDMConts");
	
	@Autowired
	private BuyNSDMContsDao buyNSDMContsDao;
	
	@Autowired
	private CommonService commonService;
	@Autowired
	private ImcsCacheService imcsCacheService;
//	public void getNSContList(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	private static int block_flag = 0;
	
	/**
	 * @author HONG
	 * @since 2016-07-12
	 * 컨텐츠 타입과 구매여부, 구매시간, 구매타입 값을 리턴 (lgvod997.pc)
	 */
	@Override
	public BuyNSDMContsResultVO buyNSDMConts(BuyNSDMContsRequestVO paramVO)	{
//		this.getNSContList(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		String flag		= "";
		String errCode	= "";
		String errMsg	= "";
		String resultCode	= "";
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		imcsLog.debugLog(methodName + " service call");
		
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);

		BuyNSDMContsResultVO	resultVO	= new BuyNSDMContsResultVO();
		
		long tp1			= 0;		// timePoint 1
		long tp2			= 0;		// timePoint 2
		
		String msg			= "";
		String szDistributor	= "";
		
		Integer resultSet = -1;
	    Integer messageSet = 99;
	    
	    int nProdDupChk	= 0;
	    int nEventChk	= 0;
	    int nDupChk	= 0;
	    int nMainCnt = 0;
	    block_flag = 0;
	    
	    ComSbcVO sbcVO = new ComSbcVO();
	    ComPriceVO priceVO = new ComPriceVO();
	    ComDataFreeVO datafreeVO = new ComDataFreeVO();
	    ComCpnVO cpnInfoVO = new ComCpnVO();
 
	
		try {

			resultSet = Request_Param_Valid(paramVO);
			
			if(resultSet != 0){
				
				msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] 정확한 구매 정보가 아닙니다. "; 
				imcsLog.serviceLog(msg, methodName, methodLine);
				resultSet = -1;
				messageSet = 15;
				
			}
			
			if(resultSet == 0){
				
				/*********************************************
				 * MIMS를 통한 정상 구매인지 여부 확인
				 ********************************************/
				// TEST는 IMCS테스트하기 위해서.....임시로
				if( !("TEST".equals(paramVO.getValidPayKey()) || (ImcsConstants.CP_USE_YN_OCULUS.equals(paramVO.getBuyingType()) && "OCULUS".equals(paramVO.getValidPayKey()) )) ){
					
					tp1 = System.currentTimeMillis();
					
					resultSet = this.getBuyChkMIMS(paramVO);
					paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
					
					tp2	= System.currentTimeMillis();
					imcsLog.timeLog("MIMS를 통한 정상 구매인지 확인", String.valueOf(tp2 - tp1), methodName, methodLine);
				}
				
				//20200326 아이들나라 3.0
				if(resultSet == 0 && paramVO.getAppType().substring(0, 1).equals("A")){
					String function = "getKidsChk";
					HashMap<String, String> mKidChk = new HashMap<String, String>();
					mKidChk	= buyNSDMContsDao.getKidsChk(paramVO);
					
					if(mKidChk != null)
					{
						String nscGb = mKidChk.get("NSC_GB").toString();
						String categoryType = mKidChk.get("CATEGORY_TYPE").toString();
						
						msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] "+ function + " function Success at";
						imcsLog.serviceLog(msg, methodName, methodLine);
						
						if(nscGb.equals("KID")) {
							paramVO.setNscGb("K");
						} else {
							paramVO.setNscGb("X");
						}
						
						switch (categoryType) {
						case "B":
							paramVO.setCategoryType("B");
							break;
						case "C":
							paramVO.setCategoryType("C");
							break;
						default:
							paramVO.setCategoryType("X");
							break;
						}
						
					} else {
						msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] "+ function + " function Data Empty";
						imcsLog.serviceLog(msg, methodName, methodLine);
						paramVO.setNscGb("X");
						paramVO.setCategoryType("X");
					}
					
				}
				
				
				/* 상태, 개통여부 및 쿠폰값 가져오기 */
				if(resultSet == 0){
					
					tp1 = System.currentTimeMillis();
					paramVO.setBuyingDate(commonService.getSysdate());
					paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
					sbcVO = new ComSbcVO();
					
					try {
						
						sbcVO	= this.getSbcInfo(paramVO);
						paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
						
						if(sbcVO == null) {
							resultSet	= -1;
			    			messageSet = 10;
						}else{
							
							if(!"".equals(sbcVO.getPvsCtnNo()) && "MOBILE".equals(sbcVO.getPvsAtrtChnlDvCd()) ){
			    				paramVO.setIsLGU("Y");
			    			}else{
			    				paramVO.setIsLGU("N");
			    			}
						}
						
					} catch (Exception e) {
						resultSet	= -1;
						messageSet = 10;
					}
					
					tp2	= System.currentTimeMillis();
					imcsLog.timeLog("가입자 상태, 개통여부 조회", String.valueOf(tp2 - tp1), methodName, methodLine);	
					
					/* 상품 정보 가져오기 */
					if(resultSet == 0){
						
						tp1 = System.currentTimeMillis();
						
						// 상품 정보 조회(정액/종량)
						priceVO	= this.getBillType(paramVO);
						paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
						
						resultSet	= paramVO.getResultSet();
						
						if(resultSet == 0){
							
							if(priceVO != null) {
								paramVO.setProdType(priceVO.getProductType());		// 2019.10.29 - NPT_VO_BUY_META 에도 사용							
								paramVO.setBillSuggestedPrice(priceVO.getSuggestedPrice());
								paramVO.setReservedPrice(priceVO.getReservedPrice());
								szDistributor	= StringUtil.nullToSpace(priceVO.getDistributor());								
								// 2019.10.29 - NPT_VO_BUY_META 공통 변수 Set
								paramVO.setAssetName(priceVO.getAssetName());
								paramVO.setHdcontent(priceVO.getHdcontent());
								paramVO.setRatingCd(priceVO.getRatingCd());
								
								// 2019.10.29 - NPT_VO_BUY_META PPV 변수 Set
								if(!paramVO.getPkgYn().toUpperCase().equals("Y"))	// 단편 구매일 경우
								{
									paramVO.setProductId(priceVO.getProductId());
									paramVO.setProductName(priceVO.getProductName());
									paramVO.setProductKind(priceVO.getProductKind());
									paramVO.setCpId(priceVO.getCpId());
									paramVO.setMaximumViewingLength(priceVO.getMaximumViewingLength());
									paramVO.setSeriesNo(priceVO.getSeriesNo());
								}
							}
							
							if(priceVO.getPreviewFlag().equals("R")){
								if(paramVO.getBuyingGb().equals("R")){
									if(Integer.parseInt(paramVO.getBuyingDate().substring(0, 8)) >= Integer.parseInt(priceVO.getReservedDate().substring(0, 8))){
										String szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] 예약구매 기간 종료[시청가능일:" + priceVO.getReservedDate() + "]";
										imcsLog.serviceLog(szMsg, methodName, methodLine);
										resultSet = -1;
									}else{
										String szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] 예약구매 가능";
										imcsLog.serviceLog(szMsg, methodName, methodLine);
									}
								}else{
									if(Integer.parseInt(paramVO.getBuyingDate().substring(0, 8)) < Integer.parseInt(priceVO.getReservedDate().substring(0, 8))){
										String szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] 예약구매 기간 [시청가능일:" + priceVO.getReservedDate() + "]";
										imcsLog.serviceLog(szMsg, methodName, methodLine);
										resultSet = -1;
									}
								}
							}else if(priceVO.getPreviewFlag().equals("N")){
								if(paramVO.getBuyingGb().equals("R")){
									String szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] 예약구매 불가 컨텐츠[previce_flag:" + priceVO.getPreviewFlag() + "]";
									imcsLog.serviceLog(szMsg, methodName, methodLine);
									resultSet = -1;
								}
							}else{
								String szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] 구매 불가 컨텐츠[previce_flag:" + priceVO.getPreviewFlag() + "]";
								imcsLog.serviceLog(szMsg, methodName, methodLine);
								resultSet = -1;
							}
							
							if(resultSet == 0){
							
								/* FVOD 인 경우, 가격 초기화*/
								if("0".equals(paramVO.getProdType())){
									datafreeVO.setPrice("0");
									paramVO.setBillSuggestedPrice("0");
								}else{
									if(paramVO.getPkgYn().equals("N"))
									{
										datafreeVO.setPrice(priceVO.getSuggestedPrice());
									}
									else
									{
										// 2020.01.03 - 실제 패키지 가격은 나중에 구해오기 때문에 여기선, 요청온 가격을 넣어서 인앱가격을 구해오고... 밑에서 패키지 가격 validation을 한다.
										datafreeVO.setPrice(paramVO.getSuggestedPrice());
									}
								}
								
								//데이터프리 정보 조회
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
				    			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
														
								if("N".equals(priceVO.getDatafreeBillYn())){
									datafreeVO.setDatafreePrice("0");
									datafreeVO.setDatafreeApprovalPrice("");
									datafreeVO.setPpvDatafreeApprovalPrice("");
								}
								
								if(datafreeVO != null){
									paramVO.setSuggestedDatafreePrice(datafreeVO.getDatafreePrice());
								}
													
								resultSet	= paramVO.getResultSet();
							}
							
						}
						
						if(resultSet == -1){
							messageSet = 13;
						}
						
						if(priceVO == null){
							priceVO = new ComPriceVO();
						}
						
						tp2	= System.currentTimeMillis();
						imcsLog.timeLog("상품 정보(정액/종량) 조회", String.valueOf(tp2 - tp1), methodName, methodLine); 
						
						if((block_flag == 1) && !"0".equals(priceVO.getProductType())) {
			            	msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] PPVBLOCK 사용자 ";
			    			imcsLog.serviceLog(msg, methodName, methodLine);
			            	messageSet = 19;
			            	resultSet = -1;
			            }
						
			            if ("N".equals(priceVO.getLicensingValidYn()) && !sbcVO.getTestSbc().equals("Y"))
			            {
			            	msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] msg[licensing_expired : " + paramVO.getAlbumId() + "] ";
			            	messageSet = 98;
			            	resultSet = -1;
			            }						
						
						/* 컨텐츠가 FVOD일 때, 컨텐츠 할인이 없어야 한다. 있으면 에러처리 */
						if( "0".equals(paramVO.getProdType()) && !"0000".equals(paramVO.getDiscountDivOrigin()) ){
							msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[컨텐츠 무료 > 컨텐츠 할인정보 존재 Validation Fail]";
							imcsLog.serviceLog(msg, methodName, methodLine);
							resultSet = -1;
							messageSet = 15;
						}
						/* 데이터프리가 무료일 때, 데이터프리 할인이 없어야 한다. 있으면 에러처리 */
						if( "N".equals(priceVO.getDatafreeBillYn()) && !"0000".equals(paramVO.getDatafreeDiscountDivOrigin()) ){
							msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[데이터프리 무료 > 데이터프리 할인정보 존재 Validation Fail]";
							imcsLog.serviceLog(msg, methodName, methodLine);
							resultSet = -1;
							messageSet = 15;							
						}
						
						 /* 사용여부 체크 */
						if(!"Y".equals( sbcVO.getStatusFlag() )){
							resultSet = -1;
							messageSet = 11;
						}
						
						if(resultSet == 0){
							 /* 개통여부 체크 */
							if( "N".equals( sbcVO.getYnVodOpen() ) ){
								/* FVOD만 사용가능 */
								if(!"F".equals(priceVO.getBillType())){
									resultSet = -1;
									messageSet = 12;
								}
							}
						}							
						
					}
					
				}
				
				if( resultSet == 0 && "1".equals(paramVO.getProdType()) && "3".equals(paramVO.getBuyTypeFlag()) && "Y".equals(priceVO.getPossessionYn()) ){
					
					msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] 평생 소장 상품 데이터 프리 구매 시도! POSSESSION_YN[" + priceVO.getPossessionYn() + "] DATAFREE_FLAG[" + paramVO.getBuyTypeFlag() + "]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					
					resultSet	= -1;
	    			messageSet = 15; /* 정의되지 않은 구매 타입입니다.*/
					
				}
				
				// 가입자 구매상품 여부 조회
				if(resultSet == 0 && !"F".equals(priceVO.getBillType()) && "N".equals(paramVO.getPkgYn()) ){
					tp1 = System.currentTimeMillis();
					nProdDupChk = this.getCustomerProdChk(paramVO);
					paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
					
					tp2	= System.currentTimeMillis();
					imcsLog.timeLog("가입자가 구입한 상품 조회", String.valueOf(tp2 - tp1), methodName, methodLine); 
					
					resultSet = paramVO.getResultSet();
					
					if(resultSet == -1) {
						messageSet = 14;
					}
					
					if(nProdDupChk == 0){
						if( !"F".equals(priceVO.getBillType()) ){
							resultSet = -1;
							messageSet = 9;
						}
					}
				}
				
				// 상품타입 별 구매내역 조회
				if( resultSet == 0 && "1".equals( paramVO.getProdType() )){

					if("2".equals(paramVO.getBuyTypeFlag())){
						
						msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] PPV DATAFREE BUY FLAG 오류! PKG_YN[" + paramVO.getPkgYn() + "] DATAFREE_FLAG["+ paramVO.getBuyTypeFlag() +"]";
						imcsLog.serviceLog(msg, methodName, methodLine);
						
						resultSet	= -1;
		    			messageSet = 15; /* 정의되지 않은 구매 타입입니다.*/
					}
					
					
					if(Integer.parseInt( priceVO.getEventValue() ) > 0){
						
						if("R".equals(paramVO.getBuyingGb())){
							msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] 할인율구매 - price[" + priceVO.getReservedPrice() + " -> " + priceVO.getEventPrice() + "]";
							imcsLog.serviceLog(msg, methodName, methodLine);
							paramVO.setBuyingPrice(priceVO.getEventPrice());
						}else{
							if( "1".equals(paramVO.getBuyTypeFlag()) || "3".equals(paramVO.getBuyTypeFlag()) ){
								msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] 할인율구매 - price[" + paramVO.getSuggestedPrice() + " -> " + priceVO.getEventPrice() + "]";
								imcsLog.serviceLog(msg, methodName, methodLine);
								paramVO.setBuyingPrice(priceVO.getEventPrice());
							}
						}
						
					}else{
						if("R".equals(paramVO.getBuyingGb())){
							// 예약구매의 경우 가격정보 CHECK
							if( !priceVO.getReservedPrice().equals(paramVO.getSuggestedPrice()) ){
								msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] 예약PPV - incorrect price[imcs_price=" + priceVO.getReservedPrice() + "][stb_price=" + paramVO.getSuggestedPrice() + "]";
								imcsLog.serviceLog(msg, methodName, methodLine);
								
								resultSet = -1;
	                            messageSet = 45;
							}
						}else{
							
							if(paramVO.getBuyingType().equals(ImcsConstants.CP_USE_YN_INAPP))
							{
								if(priceVO.getInappBuyYn().equals("Y"))
								{
									if( "1".equals(paramVO.getBuyTypeFlag()) && !(priceVO.getSuggestedPrice().equals(paramVO.getSuggestedPrice()) && datafreeVO.getApprovalPrice().equals(paramVO.getRealBuyingPrice())) ){
										msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] 인앱PPV - incorrect price[imcs_price=" + priceVO.getSuggestedPrice() + "][stb_price=" + paramVO.getSuggestedPrice() + "]"
																															+ "real price[imcs_price=" + datafreeVO.getApprovalPrice() + "][stb_price=" + paramVO.getRealBuyingPrice() + "]";
										imcsLog.serviceLog(msg, methodName, methodLine);
										
										resultSet = -1;
			                            messageSet = 45;
									}
								}
								else
								{
									msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] 인앱PPV - 인앱결제를 지원하지 않는 콘텐츠입니다.";
									imcsLog.serviceLog(msg, methodName, methodLine);
									
									resultSet = -1;
									messageSet = 3;
								}
							}
							else
							{
								if( ("1".equals(paramVO.getBuyTypeFlag()) || "3".equals(paramVO.getBuyTypeFlag())) && !priceVO.getSuggestedPrice().equals( paramVO.getSuggestedPrice() ) ){
									msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] 일반PPV - incorrect price[imcs_price=" + priceVO.getSuggestedPrice() + "][stb_price=" + paramVO.getSuggestedPrice() + "]";
									imcsLog.serviceLog(msg, methodName, methodLine);
									
									resultSet = -1;
		                            messageSet = 45;
								}
							}
							
						}
					}
					
				} else if( resultSet == 0 && "0".equals( priceVO.getProductType() )){
					// FVOD상품
					
					if("R".equals(paramVO.getBuyingGb())){
						msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "]  FOD 예약 구매 오류! BUYING_GB[" + paramVO.getBuyingGb() + "]";
						imcsLog.serviceLog(msg, methodName, methodLine);
						
						resultSet = -1;
	                    messageSet = 15; /* 정의되지 않은 구매 타입입니다.*/
					}
					
					if("3".equals(paramVO.getBuyTypeFlag())){
						msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "]  FOD DATAFREE BUY FLAG 오류! PKG_YN[" + paramVO.getPkgYn() + "] DATAFREE_FLAG[" + paramVO.getBuyTypeFlag() + "]";
						imcsLog.serviceLog(msg, methodName, methodLine);
						
						resultSet = -1;
	                    messageSet = 15; /* 정의되지 않은 구매 타입입니다.*/
					}
					
					if( "1".equals(paramVO.getOfferType()) || "3".equals(paramVO.getOfferType()) ){
						msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "]  FOD DATAFREE OFFER FLAG 오류! PRODUCT_TYPE[" + priceVO.getProductType() + "] OFFER_TYPE[" + paramVO.getOfferType() + "]";
						imcsLog.serviceLog(msg, methodName, methodLine);
						
						resultSet = -1;
	                    messageSet = 15; /* 정의되지 않은 구매 타입입니다.*/
					}
					
					
					if( ( "1".equals(paramVO.getBuyTypeFlag()) || "2".equals(paramVO.getBuyTypeFlag()) ) && !"0".equals(paramVO.getBuyingPrice()) ){
						msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] FVOD - incorrect price[imcs_price=0][stb_price=" + priceVO.getSuggestedPrice() + "]";
						imcsLog.serviceLog(msg, methodName, methodLine);
						
						resultSet = -1;
	                    messageSet = 45;
					}
				}
				
				/* 데이터 프리 가격 체크*/
				if( resultSet == 0 && ( "2".equals(paramVO.getBuyTypeFlag()) || "3".equals(paramVO.getBuyTypeFlag()) ) ){
					
					if("N".equals(priceVO.getDatafreeBillYn())){
						
						if( ( "2".equals(paramVO.getBuyTypeFlag()) || "3".equals(paramVO.getBuyTypeFlag()) ) && !"0".equals(paramVO.getDatafreeBuyPrice()) ){
							
							msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] 데이터프리 - incorrect price[imcs_price=0][stb_price=" + paramVO.getDatafreeBuyPrice() + "]";
							imcsLog.serviceLog(msg, methodName, methodLine);
							
							resultSet = -1;
		                    messageSet = 45;							
						}	
						
					}else{
						if(!datafreeVO.getDatafreePrice().equals(paramVO.getDatafreePrice())){
							msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] 데이터프리 - incorrect price[imcs_price=" + datafreeVO.getDatafreePrice() + "][stb_price=" + paramVO.getDatafreePrice() + "]";
							imcsLog.serviceLog(msg, methodName, methodLine);
							
							resultSet = -1;
		                    messageSet = 45;
						}
						
					}
					
				}
				
				//기존 데이터 프리 구매내역 조회
				if( resultSet == 0 ){
					nDupChk = this.chkDatafreeDup(paramVO);
					paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
					resultSet = paramVO.getResultSet();
					
					if(resultSet == -1){
						messageSet = 22;
					}
					
					/* 기존에 데이타가 있으면 */
					if(nDupChk > 0){
						resultSet = -1;
						messageSet = 24;
					}
					
					tp2	= System.currentTimeMillis();
					imcsLog.timeLog("기존 데이터 프리 구매내역 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
				}
				
				// 기존 구매내역 조회
				if( resultSet == 0 && nDupChk <= 0 && !"0".equals(priceVO.getProductType()) ){
					tp1 = System.currentTimeMillis();
					
					List<HashMap<String, Object>> lBuyDupChk = new ArrayList<HashMap<String, Object>>();
					HashMap<String, Object> mBuyDupChk = new HashMap<String, Object>();
					
					// 중복구매 체크
					try {
						if("R".equals(paramVO.getBuyingGb())) {	// 예약구매
							lBuyDupChk	= buyNSDMContsDao.getBuyDupChkR(paramVO);
							
//							paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()]++;
						} else {									// 예약구매 외
							if( "0".equals(paramVO.getProdType()) )
								lBuyDupChk	=	buyNSDMContsDao.getBuyDupChkType0(paramVO);		// FVOD 구매내역 조회 
							else if( "1".equals(paramVO.getProdType()) )
								lBuyDupChk	=	buyNSDMContsDao.getBuyDupChkType1(paramVO);
							else if( "2".equals(paramVO.getProdType()) )
								lBuyDupChk	=	buyNSDMContsDao.getBuyDupChkType2(paramVO);
							
//							paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()]++;
						}
						paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
					} catch(Exception e) {
						paramVO.setResultCode("40000000");
						resultSet	= -1;
						messageSet	= 22;
					}
					
					
					// 기존에 데이타가 있으면
					if(lBuyDupChk!= null && lBuyDupChk.size() > 0){
						mBuyDupChk	= lBuyDupChk.get(0);
						nDupChk	= (Integer) mBuyDupChk.get("DUP_CHK");
						paramVO.setResultBuyDate((String) mBuyDupChk.get("BUY_DATE"));
						
						if( nDupChk > 0 ) {
							resultSet	= -1;
							messageSet	= 24;							
						}
					}
					
					tp2	= System.currentTimeMillis();
					imcsLog.timeLog("기존 구매내역 조회", String.valueOf(tp2 - tp1), methodName, methodLine); 
				}
				
				if( resultSet == 0 ){
					
					if( "N".equals(priceVO.getDatafreeBillYn()) ){
						
						if( "2".equals(paramVO.getOfferType()) || "3".equals(paramVO.getOfferType()) ){
							
							msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] 무료 데이터 프리 - 공제 오류 [offer_type=" + paramVO.getOfferType() + "]";
							imcsLog.serviceLog(msg, methodName, methodLine);
							
							resultSet	= -1;
							messageSet	= 15;
						}
						if( ( "2".equals(paramVO.getBuyTypeFlag()) || "3".equals(paramVO.getBuyTypeFlag()) ) && !"0".equals(paramVO.getDatafreeBuyPrice()) ){
							
							msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] 데이터 프리 - incorrect price [imcs_price=0][stb_price=" + paramVO.getDatafreeBuyPrice() + "]";
							imcsLog.serviceLog(msg, methodName, methodLine);
							
							resultSet	= -1;
							messageSet	= 45;
						}
						
					}else if( ( "2".equals(paramVO.getBuyTypeFlag()) || "3".equals(paramVO.getBuyTypeFlag()) ) && "Y".equals(priceVO.getDatafreeBillYn()) ){
						
						if( !paramVO.getDatafreePrice().equals( datafreeVO.getDatafreePrice() ) ){
							
							msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] Datafree - incorrect price [imcs_price=" + datafreeVO.getDatafreePrice() + "][stb_price=" + paramVO.getDatafreePrice() + "]";
							imcsLog.serviceLog(msg, methodName, methodLine);
							
							resultSet	= -1;
							messageSet	= 45;
							
						}
						
					}
					
				}
				
				if(resultSet == 0){
					List<ContTypeVO> lContsList = new ArrayList<ContTypeVO>();
					ContTypeVO contTypeVO = new ContTypeVO();
					nMainCnt = 0;
					
					// 패키지 컨텐츠 보관함 조회
					if("Y".equals(paramVO.getPkgYn())){
						tp1	= System.currentTimeMillis();
						paramVO.setContsGenre("패키지");
						String[] ppsCpId = new String[4];
						
						try {
							lContsList = this.getPkgContent(paramVO);
							paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
							
							if(lContsList != null && lContsList.size() > 0){
								nMainCnt = lContsList.size();
							} else {
								resultSet	= -1;
								messageSet	= 31;
							}
						} catch (Exception e) {
							resultSet	= -1;
							messageSet	= 30;
						}
						
						for(int i = 0; i < nMainCnt; i++){
							contTypeVO = lContsList.get(i);
							
							Integer result = 0;
							
							paramVO.setProductId2(contTypeVO.getProductId());
							paramVO.setContsId2(contTypeVO.getContsId());
							paramVO.setContsName2(contTypeVO.getContsName());
							paramVO.setContsGenre2(contTypeVO.getContsGenre());
							
							if(contTypeVO.getCpId().equals("1414"))	// 예고편 CP
							{
								ppsCpId[3] = contTypeVO.getCpId();
							}
							else if(contTypeVO.getCpId().equals("1302"))	// 미디어로그 CP
							{
								ppsCpId[2] = contTypeVO.getCpId();
							}
							else
							{
								if(ppsCpId[0] == null || ppsCpId[0].equals(""))
								{
									ppsCpId[0] = contTypeVO.getCpId();
								}
								else if(!ppsCpId[0].equals(contTypeVO.getCpId()))
								{
									ppsCpId[1] = contTypeVO.getCpId();
								}
							}										
							
							// 보관함 : 해당 Contents를 Fetch하여 insert
					    	result = this.insBuyConts3(paramVO);
					    	paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
					    	
					    	if(result > 0) {
					    		resultSet	= 0;
					    	} else {
					    		resultSet	= -1;
			                    messageSet	= 32;
			                    
			                    if(paramVO.getSqlCode() == -1)
									messageSet	= 24;
					    	}
						}
						
						// 2019.10.29 - VOD 정산 프로세스 개선 : PPS CP_ID정보 조회를 위한 변수 추가						
						// Case 1. 1414(예고편) / 1302(미디어로그) CP 외의 CP_ID가 하나만 존재할 시 해당 CP_ID를 NPT_VO_BUY_META 테이블에 저장
					    // Case 2. 1414(예고편) / 1302(미디어로그) CP 외의 CP_ID가 두개 이상 존재할 시 "MULTI"라는 문자를 NPT_VO_BUY_META 테이블에 저장
					    // Case 3. 1414(예고편) / 1302(미디어로그) CP 외의 CP_ID가 없을 시 1302(미디어로그) CP_ID를 우선순위로 NPT_VO_BUY_META 테이블에 저장
					    // Case 4. 1414(예고편) / 1302(미디어로그) CP 외의 CP_ID가 없고 1302(미디어로그) CP_ID도 없을시 1414(예고편) CP_ID를 우선순위로 NPT_VO_BUY_META 테이블에 저장
		    		    for(int i = 0 ; i < ppsCpId.length ; i++)
		    		    {
		    		    	if(!(ppsCpId[i] == null || ppsCpId[i].equals("")))
		    		    	{
		    		    		if(!paramVO.getCpId().equals(""))
		    		    		{
		    		    			if(i == 1)
		    		    			{
		    		    				paramVO.setCpId("MULTI");
		    		    				break;
		    		    			}
		    		    			if(i >= 2) break;
		    		    		}
		    		    		else
		    		    		{
		    		    			paramVO.setCpId(ppsCpId[i]);
		    		    		}
		    		    	}		    		    			    		    
		    		    }
						
						tp2	= System.currentTimeMillis();
						imcsLog.timeLog("패키지 컨텐츠 보관함 저장", String.valueOf(tp2 - tp1), methodName, methodLine); 
						
					} else if("N".equals(paramVO.getPkgYn())) {		// 단품구매
						tp1	= System.currentTimeMillis();
						
						// 장르값 가져오기
						try {
							contTypeVO	= this.getGenreType(paramVO);
							paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
							
							if(contTypeVO != null) {
								paramVO.setContsGenre(contTypeVO.getContsGenre());
								paramVO.setEventType(contTypeVO.getContsType());
							}
						} catch(Exception e) {
							resultSet	= -1;
							messageSet	= 90;
						}
						tp2	= System.currentTimeMillis();
						imcsLog.timeLog("단품 컨텐츠 장르 조회", String.valueOf(tp2 - tp1), methodName, methodLine); 
						
						Integer result	= 0;
						
						result = this.insBuyConts4(paramVO);
						paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				    	
				    	if(result > 0) {
				    		resultSet	= 0;
				    	} else {
				    		resultSet	= -1;
		                    messageSet	= 32;
		                    
		                    if(paramVO.getSqlCode() == -1)
								messageSet	= 24;
				    	}
						tp1	= System.currentTimeMillis();
						imcsLog.timeLog("단품 컨텐츠 보관함 저장", String.valueOf(tp1 - tp2), methodName, methodLine); 
					}
									
				}
				
				if(paramVO.getContsGenre() == null || "".equals(paramVO.getContsGenre())){
					paramVO.setContsGenre("기타");
				}
				
				if( resultSet == 0 ){
					
					/* 컨텐츠 할인정보 등록 */
					if( ( "1".equals(paramVO.getBuyTypeFlag()) || "3".equals(paramVO.getBuyTypeFlag()) ) && !"0".equals(priceVO.getProductType()) ){
						
						Integer result	= 0;
						
						result = this.buyInsDiscount(paramVO);
						paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
						
						if(result > 0) {
				    		resultSet	= 0;
				    	} else {
				    		resultSet	= -1;
		                    messageSet	= 40;
				    	}
					}
					
				}
				
				if( resultSet == 0 ){
					
					/* 데이터프리 할인정보 등록 */
					if( "2".equals(paramVO.getBuyTypeFlag()) || "3".equals(paramVO.getBuyTypeFlag()) || ( "N".equals(paramVO.getPkgYn()) && "Y".equals(paramVO.getIsLGU()) && "N".equals(priceVO.getDatafreeBillYn()) ) ){
						
						paramVO.setIsPayDatafree(priceVO.getDatafreeBillYn());
						
						if( "N".equals(paramVO.getPkgYn()) && "Y".equals(paramVO.getIsLGU()) && "N".equals(priceVO.getDatafreeBillYn()) ){
							paramVO.setDatafreeBuyPrice("0");
							paramVO.setUdrBuyPrice(0);
							paramVO.setDatafreeDiscountCnt(0);
						}
						
						Integer result	= 0;
						
						result = this.buyInsDfDiscount(paramVO);
						paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
						
						if(result > 0) {
				    		resultSet	= 0;
				    	} else {
				    		resultSet	= -1;
		                    messageSet	= 40;
				    	}
					}
					
				}
				
				// 구매내역 생성
				if(resultSet == 0){
					tp1	= System.currentTimeMillis();
					try {
						if("N".equals(paramVO.getPkgYn().toUpperCase())) {
							Integer result = 0;
			
							
							//예약구매 포함 (예약구매의 경우 유효기간 관련 처리를 하지 않는다)
							result = this.insBuyConts1(paramVO);
							paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
							
					    	if(result > 0) {
					    		resultSet	= 0;
					    	} else {
					    		resultSet	= -1;
			                    messageSet	= 40;
					    	}
					    						    	
						} else {
							Integer result = 0;
							ContTypeVO prodInfoVO = new ContTypeVO();
							prodInfoVO	= this.getProduct(paramVO);
							paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
							
							if(prodInfoVO != null) {
								paramVO.setProductId1(prodInfoVO.getProductId());
								paramVO.setProductName1(prodInfoVO.getProductName());
								paramVO.setProductPrice1(prodInfoVO.getPrice());
								paramVO.setExpiredDate1(prodInfoVO.getExpiredDate());
								
								resultVO.setProductId(prodInfoVO.getProductId());		// 통합통계용 
								resultVO.setProductName(prodInfoVO.getProductName());	// 통합통계용
								resultVO.setProductPrice(prodInfoVO.getPrice());		// 통합통계용
								
								// 2019.10.30 - VOD 정산 프로세스 개선 : PPS 상품 정보 NPT_VO_BUY_META에 넣기위해 paramVO에 Set
								paramVO.setProductKind(prodInfoVO.getProductKind());
								paramVO.setProductId(prodInfoVO.getProductId());
								paramVO.setProductName(prodInfoVO.getProductName());
								paramVO.setMaximumViewingLength(prodInfoVO.getExpiredDate());
							}
							
							/** PPS 가격 체크*/
							if(!paramVO.getSuggestedPrice().equals(paramVO.getProductPrice1())){
								
								msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] PPS 구매 - incorrect price[imcs_price=" + paramVO.getProductPrice1() + "][stb_price=" + paramVO.getSuggestedPrice() + "]";
								imcsLog.serviceLog(msg, methodName, methodLine);
								
								resultSet	= -1;
			                    messageSet	= 40;
							}else{
								
								if(paramVO.getBuyingType().equals(ImcsConstants.CP_USE_YN_INAPP))
								{
									if(!paramVO.getRealBuyingPrice().equals(datafreeVO.getApprovalPrice())){
										msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] inApp PPS 구매 - incorrect price[imcs_price=" + datafreeVO.getApprovalPrice() + "][stb_price=" + paramVO.getRealBuyingPrice() + "]";
										imcsLog.serviceLog(msg, methodName, methodLine);
										
										resultSet	= -1;
					                    messageSet	= 40;
									}
								}
								
								if(resultSet == 0)
								{
									//예약구매 포함 (예약구매의 경우 유효기간 관련 처리를 하지 않는다)
									result = this.insBuyConts2(paramVO);
									paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
									
							    	if(result > 0) {
							    		resultSet	= 0;
							    	} else {
							    		resultSet	= -1;
					                    messageSet	= 40;
							    	}
								}
								
							}
							
						}
					} catch(Exception e) {
						resultSet	= -1;
	                    messageSet	= 40;
					}
					tp2	= System.currentTimeMillis();
					imcsLog.timeLog("구매내역 생성 완료", String.valueOf(tp2 - tp1), methodName, methodLine); 
				}
				
				if(resultSet == 0 && ( ( "2".equals(paramVO.getBuyTypeFlag()) || "3".equals(paramVO.getBuyTypeFlag()) ) || ( "N".equals(paramVO.getPkgYn()) && "Y".equals(paramVO.getIsLGU()) && "N".equals(paramVO.getIsPayDatafree()) ) ) )
				{
					
					if( "N".equals(paramVO.getPkgYn()) && "Y".equals(paramVO.getIsLGU()) && "N".equals(paramVO.getIsPayDatafree()) ){
						msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] sts[DF_WARN] 무료 데이터Free DEFAULT 구매";
						imcsLog.serviceLog(msg, methodName, methodLine);
						paramVO.setDatafreeBuyPrice("0");
						paramVO.setRealBuyingPrice("0");
					}
					
					Integer result = 0;
					
					result = this.buyDatafree(paramVO);
					paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
					
					if(result > 0)		resultSet = 0;
			    	else	    		resultSet = -1;
					
					if(resultSet == -1){
						msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] Data Free 구매 실패";
						imcsLog.serviceLog(msg, methodName, methodLine);
						
						resultSet = -1;
						messageSet = 40;
					}
					
				}				

				if(resultSet == 0){
					tp1	= System.currentTimeMillis();
					
	                // 장르 정보 조회
					String szGenreInfo	= "";
					tp1	= System.currentTimeMillis();
					try {
						szGenreInfo =	this.getGenreInfo(paramVO);
						paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
						paramVO.setGenreInfo(szGenreInfo);
						resultSet = paramVO.getResultSet();
						
//						//2019.03.05 - 지상파 서비스 종료로 인해 KBS, SBS, MBC 컨텐츠 순차적으로 비노출 처리
//						if (paramVO.getBuyingDate().compareTo("20190307") >= 0 && (paramVO.getGenreInfo().indexOf("SBS") > 0 && paramVO.getGenreInfo().indexOf("SBS") + 3 == paramVO.getGenreInfo().length()) )
//						{
//							resultSet = -1;
//							messageSet = 13;
//						}
//						else if (paramVO.getBuyingDate().compareTo("20190311") >= 0 && (paramVO.getGenreInfo().indexOf("KBS") > 0 && paramVO.getGenreInfo().indexOf("KBS") + 3 == paramVO.getGenreInfo().length()) )
//						{
//							resultSet = -1;
//							messageSet = 13;
//						}
//						else if (paramVO.getBuyingDate().compareTo("20190315") >= 0 && (paramVO.getGenreInfo().indexOf("MBC") > 0 && paramVO.getGenreInfo().indexOf("MBC") + 3 == paramVO.getGenreInfo().length()) )
//						{
//							resultSet = -1;
//							messageSet = 13;
//						}
						
					} catch(Exception e) {
						tp2	= System.currentTimeMillis();
						imcsLog.timeLog("장르 정보 조회 실패", String.valueOf(tp2 - tp1), methodName, methodLine);
					}
					
					msg	 = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] select genre[" + szGenreInfo + "]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					
					//쿠폰을 사용하였을 경우 쿠폰 정보를 조회 하지 않음
					if( !("1".equals(paramVO.getDiscountDiv()[ImcsConstants.DISCOUNT_DIV_COUPON]) || "1".equals(paramVO.getDatafreeDiscountDiv()[ImcsConstants.DISCOUNT_DIV_COUPON])
							|| "1".equals(paramVO.getDiscountDiv()[ImcsConstants.DISCOUNT_DIV_MEMBERSHIP]) ) ){									
		                // 스탬프 쿠폰 정보 조회
						try {
							// 쿠폰 정보 조회
							cpnInfoVO	= this.getCpnInfo(paramVO);
							paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
							
							tp2	= System.currentTimeMillis();
							imcsLog.timeLog("스탬프/쿠폰 정보 조회 완료", String.valueOf(tp2 - tp1), methodName, methodLine);
						} catch(Exception e) {
							imcsLog.errorLog(e.toString());
							
							resultSet = -1;
							messageSet = 49;
							
							tp2	= System.currentTimeMillis();
							imcsLog.timeLog("스탬프/쿠폰 정보 조회 실패", String.valueOf(tp2 - tp1), methodName, methodLine);
						}
					}
				}
				
				try
				{
					msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] D/F Buy Check : result_set["+resultSet+"] c_datafree_buy_flag["+paramVO.getBuyTypeFlag()+"]"
							+ "c_is_pay_datafree["+priceVO.getDatafreeBillYn()+"] c_is_LGU["+paramVO.getIsLGU()+","+sbcVO.getPvsCtnNo()+","+sbcVO.getPvsAtrtChnlDvCd()+"]";
					imcsLog.serviceLog(msg, methodName, methodLine);
				}
				catch(Exception e)
				{
					msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] 가입자 정보 없음.";
					imcsLog.serviceLog(msg, methodName, methodLine);
				}
			    
			}
			
			// 2019.10.30 - VOD 정산 프로세스 개선 : NPT_VO_BUY_META 테이블 INSERT
			// 모바일의 경우 데이터프리 구매도 있기 떄문에, PPV+DataFree 구매 일 경우에는 NPT_VO_BUY_META테이블에 하나의 데이터만 저장한다.
			if(resultSet == 0)
			{
				if(this.insBuyMeta(paramVO) > 0) {
		    		resultSet	= 0;
		    	} else {
		    		resultSet	= -1;
	                messageSet	= 40;
		    	}
			}
			
			
			if(resultSet == 0 || (resultSet == -1 && messageSet == 24)) // 이미 구매한 사용자도 캐시 생성
			{
				if(priceVO.getPayFlag().equals("1")) { //아이돌라이브 유로콘서트 키값 캐시파일 생성
		    		
					int cstFlag = 1;
					
					if(resultSet == 0) {
						cstFlag = this.chkConsert(paramVO);
						
						if(cstFlag == 1) {
							msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] 아이돌라이브 유료콘서트 구매 가능.";
							imcsLog.serviceLog(msg, methodName, methodLine);
						} else {
							resultSet  = -1;
							messageSet = 40;
							msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] 아이돌라이브 유료콘서트 구매 불가능.";
							imcsLog.serviceLog(msg, methodName, methodLine);
						}
					}
					
					if(cstFlag == 1) {
						int resultFlag = this.makeCacheFile(paramVO);
						
						if(resultFlag == 0) {
							msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] 메모리캐시 성공.";
							imcsLog.serviceLog(msg, methodName, methodLine);
						} else {
							msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] 메모리캐시 실패.";
							imcsLog.serviceLog(msg, methodName, methodLine);
						}
					}
		    	}
			}
			
			
			/* 리턴값을 지정하여 리턴처리 */
		    if(resultSet == 0){
		    	tp1	= System.currentTimeMillis();
		    	
		    	resultVO.setFlag("0");
		    	resultVO.setErrMsg("insert success");
		    	resultVO.setCpnInfoVO(cpnInfoVO);
		    	resultVO.setBuyingDate(paramVO.getBuyingDate());
		    	
		    	tp2	= System.currentTimeMillis();
		    	imcsLog.timeLog("EXEC SQL COMMIT", String.valueOf(tp2 - tp1), methodName, methodLine); 
		    } else {
		    	resultVO.setFlag("1");
		    	resultVO.setBuyingDate(paramVO.getResultBuyDate());
		    	
		    	throw new ImcsException(resultVO.getFlag(), resultVO.getErrMsg(), resultVO.getErrCode(), resultVO);
		    }
			
			
		} catch(ImcsException ie) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			tp1	= System.currentTimeMillis();
			
			HashMap<String, String> mResult = new HashMap<String, String>();
			mResult = commonService.getErrorMsg(messageSet);
			
			flag	= "1";
			
			if(mResult != null) {
				errCode	= mResult.get("ERR_CODE");
				errMsg	= mResult.get("ERR_MSG");
				resultCode	= mResult.get("RESULT_CODE");
			} else {
				errCode	= "99";
				errMsg	= "incorrect failed !!!";
				resultCode	= "20000090";
			}
			
			if(messageSet == 24) {
				errCode	= "";
				flag	= "2";
				resultVO.setBuyingDate(paramVO.getResultBuyDate());
			}
			
			resultVO.setFlag(flag);
			resultVO.setErrCode(errCode);
			resultVO.setErrMsg(errMsg);
			paramVO.setResultCode(resultCode);
			
			
			msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] msg[" + flag + "|" + errMsg + "|" + resultVO.getBuyingDate() + "||]";
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("EXEC SQL ROLLBACK", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			imcsLog.errorLog(methodName + "-E",ie.getClass().getName()+":"+ie.getMessage());
			throw new ImcsException(flag, errMsg, errCode, resultVO);
		} catch(Exception e) {
			isLastProcess	= ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			
			// TLO Log 를 Controller Layer 로 전달하기 위해 ImcsException 으로 변환한다. (TLO Log 세부값은 finally 문에서 설정된다.)
			throw new ImcsException();
		} finally{
			resultVO.setResultCode(paramVO.getResultCode());
//			msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
//					+ " [DR:" + nDRedis + "|DH:" + nDHbase + "|R:" + nRedis + "|H:" + nHbase + "]"; 
						
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID030) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultVO;
	}
		
	public Integer Request_Param_Valid(BuyNSDMContsRequestVO paramVO){
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String msg = "";
		
		Integer result_set = 0;
		
		int conts_valid = 0;
		int df_valid = 0; 
		int real_conts_valid = 0;
		int real_df_valid = 0;
		paramVO.setDiscountCnt(0); /* 컨텐츠 할인 정보 개수 */
		paramVO.setDatafreeDiscountCnt(0); /* 데이터프리 할인 정보 개수 */
		int alwnce_charge_st_tmp = 0; /* 통합통계log에서 할인정보 저장 용도 */
		
		int szSurtaxRate = 10; /* 부가세 요율 */
		
		try {
			
			// 2021.02.04 - 특정 APP별 결제/할인 수단 제어
			if(getPaymentBlock(paramVO) != 0)
			{
				msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[앱별 결제/할인 수단 Validation Fail]";
				imcsLog.serviceLog(msg, methodName, methodLine);
				result_set = -1;
				return result_set;
			}
			
			
			conts_valid = conts_valid + Integer.parseInt(paramVO.getSuggestedPrice());
			real_conts_valid = real_conts_valid + Integer.parseInt(paramVO.getSuggestedPrice());
			df_valid = df_valid + Integer.parseInt(paramVO.getDatafreePrice());
			real_df_valid = real_df_valid + Integer.parseInt(paramVO.getDatafreePrice());
			
			// 20190425 - 오과금 TO-BE
			paramVO.setUdrBuyPrice(0);
			paramVO.setUdrDfBuyPrice(0);
			
			
			szSurtaxRate = commonService.getSurtaxRate();
			
			paramVO.setSurtaxRate(szSurtaxRate);
			
			/* 가입자 번호 12자리 / 10자리가 아닐 경우 FAIL */
			if( paramVO.getSaId().length() != 12 && paramVO.getSaId().length() != 10 ){
				msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[가입자 번호 Validation Fail]";
				imcsLog.serviceLog(msg, methodName, methodLine);
				result_set = -1;
				return result_set;
			}
			
			/* 맥주소가 14자리가 아닐 경우 FAIL */
			if(paramVO.getStbMac().length() != 14){
				msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[맥주소 Validation Fail]";
				imcsLog.serviceLog(msg, methodName, methodLine);
				result_set = -1;
				return result_set;
			}
			
			/* MIMS 통해서 정상적으로 구매한 것인지 확인하는 Key값이 있는지 확인 */
			if( "".equals(paramVO.getValidPayKey()) || paramVO.getValidPayKey() == null ){
				msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[Pay_Key Validation Fail]";
				imcsLog.serviceLog(msg, methodName, methodLine);
				result_set = -1;
				return result_set;
			}
			else if(!paramVO.getBuyingType().equals(ImcsConstants.CP_USE_YN_OCULUS))
			{
				// 2019.08.05 - VR1.5 오큘러스 결제가 아니면 PASS
				if(paramVO.getValidPayKey().equals("OCULUS"))
				{
					msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[Pay_Key(BUY_TYPE not OCULUS) Validation Fail]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					result_set = -1;
					return result_set;
				}				
			}
			else if( !(paramVO.getBuyingType().equals(ImcsConstants.CP_USE_YN_OCULUS) && (paramVO.getValidPayKey().equals("OCULUS") || paramVO.getValidPayKey().equals("TEST"))) )
			{
				msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[Pay_Key(OCULUS) Validation Fail]";
				imcsLog.serviceLog(msg, methodName, methodLine);
				result_set = -1;
				return result_set;
			}
			
			/* 패키지여부가 Y/N이 아닐 경우 FAIL */
			switch (paramVO.getPkgYn()) {
			case "Y":
				if(paramVO.getBuyingGb().equals("R"))
				{
					msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[패키지->예약구매불가 Validation Fail]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					result_set = -1;
					return result_set;
				}
				// 2019.07.31 - VR1.5(OCULUS 결제)에서는 패키지구매는 없다.
				if(paramVO.getBuyingType().equals(ImcsConstants.CP_USE_YN_OCULUS))
				{
					msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[패키지->OCULUS 결제 불가 Validation Fail]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					result_set = -1;
					return result_set;
				}
				
				// 2020.01.03 - 패키지 구매는 데이터프리 구매 없음
				if(paramVO.getBuyTypeFlag().equals("2") || paramVO.getBuyTypeFlag().equals("3"))
				{
					msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[패키지->데이터프리 불가 Validation Fail]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					result_set = -1;
					return result_set;
				}
				break;
			case "N":
				break;

			default:
				msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[패키지여부 Validation Fail]";
				imcsLog.serviceLog(msg, methodName, methodLine);
				result_set = -1;
				return result_set;
			}
			
			
			/* 구매 타입Flag 및 기본 결제 타입에 따른 Validation */
			switch (paramVO.getBuyTypeFlag()) {
			case "1":
				break;
				
			case "2":
			case "3":				
				// 2019.07.31 - VR1.5(OCULUS 결제)에서는 데이터프리 구매는 없다.
				if(paramVO.getBuyingType().equals(ImcsConstants.CP_USE_YN_OCULUS))
				{
					msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[데이터프리->OCULUS 결제 불가 Validation Fail]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					result_set = -1;
					return result_set;
				}
				
				/* 데이터프리 구매시 멤버십 포인트는 사용 불가 */
				if("1".equals(paramVO.getDatafreeDiscountDiv()[ImcsConstants.DISCOUNT_DIV_MEMBERSHIP])){
					msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[데이터프리 구매시 멤버십 포인트는 사용 불가 Validation Fail]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					result_set = -1;
					return result_set;
				}
				
				/* 데이터프리 가격이 숫자가 아닐 경우 FAIL */
				try {
					Integer.parseInt(paramVO.getDatafreePrice());
				} catch (Exception e) {
					msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[데이터프리 제공 가격 Validation Fail]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					result_set = -1;
					return result_set;
				}
				
				/* 데이터프리 구매 가격이 숫자가 아닐 경우 또는 음수나 NULL일 경우 FAIL */
				try {
					if( Integer.parseInt(paramVO.getDatafreeBuyPrice()) < 0
						|| paramVO.getDatafreeBuyPrice().length() == 0
						|| Integer.parseInt(paramVO.getRealDatafreeBuyPrice()) < 0
						|| paramVO.getRealDatafreeBuyPrice().length() == 0)
					{
						msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[데이터프리 구매 가격 Validation Fail]";
						imcsLog.serviceLog(msg, methodName, methodLine);
						result_set = -1;	
						return result_set;
					}
					
				} catch (Exception e) {
					msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[데이터프리 구매 가격 Validation Fail]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					result_set = -1;
					return result_set;
				}
				
				break;

			default:
				msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[데이터프리 구매 가격 Validation Fail]";
				imcsLog.serviceLog(msg, methodName, methodLine);
				result_set = -1;
				return result_set;
			}
			
			
			switch (paramVO.getBuyingType()) {
			case ImcsConstants.CP_USE_YN_NORMAL:
				/* 컨텐츠 가격이 숫자가 아닐 경우 FAIL */
				try {
					Integer.parseInt(paramVO.getSuggestedPrice());
				} catch (Exception e) {
					msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[컨텐츠 제공 가격 Validation Fail]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					result_set = -1;
					return result_set;
				}
				
				/* 컨텐츠 구매 가격이 숫자가 아닐 경우 또는 음수나 NULL일 경우 FAIL */
				try {
					if( Integer.parseInt(paramVO.getBuyingPrice()) < 0
						|| paramVO.getBuyingPrice().length() == 0
						|| Integer.parseInt(paramVO.getRealBuyingPrice()) < 0
						|| paramVO.getRealBuyingPrice().length() == 0)
					{
						msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[컨텐츠 구매 가격 Validation Fail]";
						imcsLog.serviceLog(msg, methodName, methodLine);
						result_set = -1;
						return result_set;
					}
					
				} catch (Exception e) {
					msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[컨텐츠 구매 가격 Validation Fail]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					result_set = -1;
					return result_set;
				}
				
				// 20190425 - 오과금 TO-BE
				paramVO.setUdrBuyPrice( Integer.parseInt(paramVO.getRealBuyingPrice()));
				paramVO.setUdrDfBuyPrice( Integer.parseInt(paramVO.getRealDatafreeBuyPrice()));				
				break;
			
			// 2019.07.31 - VR1.5(OCULUS 결제)는 청구서 결제와 금액 validation 체크는 동일 (대신 BUY_DM_DETAIL 에 남기는 DM_UDR_PRICE는 페이나우와 동일하게 남긴다.)
			case ImcsConstants.CP_USE_YN_OCULUS:
				/* 컨텐츠 가격이 숫자가 아닐 경우 FAIL */
				try {
					Integer.parseInt(paramVO.getSuggestedPrice());
				} catch (Exception e) {
					msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[컨텐츠 제공 가격 Validation Fail]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					result_set = -1;
					return result_set;
				}
				
				/* 컨텐츠 구매 가격이 숫자가 아닐 경우 또는 음수나 NULL일 경우 FAIL */
				try {
					if( Integer.parseInt(paramVO.getBuyingPrice()) < 0
						|| paramVO.getBuyingPrice().length() == 0
						|| Integer.parseInt(paramVO.getRealBuyingPrice()) < 0
						|| paramVO.getRealBuyingPrice().length() == 0)
					{
						msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[컨텐츠 구매 가격 Validation Fail]";
						imcsLog.serviceLog(msg, methodName, methodLine);
						result_set = -1;
						return result_set;
					}
					
				} catch (Exception e) {
					msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[컨텐츠 구매 가격 Validation Fail]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					result_set = -1;
					return result_set;
				}
				break;
				
			case ImcsConstants.CP_USE_YN_CREDITCARD:
				/* 컨텐츠 가격이 숫자가 아닐 경우 FAIL */
				try {
					Integer.parseInt(paramVO.getSuggestedPrice());
				} catch (Exception e) {
					msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[컨텐츠 제공 가격 Validation Fail]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					result_set = -1;
					return result_set;
				}
				
				/* 컨텐츠 구매 가격이 숫자가 아닐 경우 또는 음수나 NULL일 경우 FAIL */
				try {
					if( Integer.parseInt(paramVO.getBuyingPrice()) < 0
						|| paramVO.getBuyingPrice().length() == 0
						|| Integer.parseInt(paramVO.getRealBuyingPrice()) < 0
						|| paramVO.getRealBuyingPrice().length() == 0)
					{
						msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[컨텐츠 구매 가격 Validation Fail]";
						imcsLog.serviceLog(msg, methodName, methodLine);
						result_set = -1;
						return result_set;
					}
					
				} catch (Exception e) {
					msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[컨텐츠 구매 가격 Validation Fail]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					result_set = -1;
					return result_set;
				}
				
				/* 신용카드 결제인데, 컨텐츠and데이터프리 구매 가격이 둘 다 0일 수는 없다. */
				if( Integer.parseInt(paramVO.getRealBuyingPrice()) == 0 && Integer.parseInt(paramVO.getRealDatafreeBuyPrice()) == 0 ){
					msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[신용카드 > 컨텐츠+데이터프리 구매 가격 0원 Validation Fail]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					result_set = -1;
					return result_set;
				}
				
				// 20190425 - 오과금 AS-IS
//				paramVO.setUdrBuyPrice( paramVO.getUdrBuyPrice() - Integer.parseInt(paramVO.getBuyingPrice()));
//				paramVO.setUdrDfBuyPrice( paramVO.getUdrDfBuyPrice() - Integer.parseInt(paramVO.getDatafreeBuyPrice()));
				break;
				
			case ImcsConstants.CP_USE_YN_PAYNOW:
				/* 페이나우 구매시 KLU포인트 사용 불가 */
//				if("1".equals(paramVO.getDiscountDiv()[ImcsConstants.DISCOUNT_DIV_KLUPOINT])
//					|| "1".equals(paramVO.getDatafreeDiscountDiv()[ImcsConstants.DISCOUNT_DIV_KLUPOINT]) ){
//					System.out.println("[ERROR] 페이나우 구매 > KLU포인트 사용 불가 Validation Fail");
//					result_set = -1;
//				}
				
				/* 컨텐츠 가격이 숫자가 아닐 경우 FAIL */
				try {
					Integer.parseInt(paramVO.getSuggestedPrice());
				} catch (Exception e) {
					msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[컨텐츠 제공 가격 Validation Fail]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					result_set = -1;
					return result_set;
				}
				
				/*
				 * 컨텐츠 구매 가격이 숫자가 아닐 경우 또는 음수나 NULL일 경우 FAIL
				 * 페이나우 결제시 100원 미만은 구매 불가
				 * */
				try {
					if( Integer.parseInt(paramVO.getBuyingPrice()) < 0
						|| paramVO.getBuyingPrice().length() == 0
						|| Integer.parseInt(paramVO.getRealBuyingPrice()) < 0
						|| paramVO.getRealBuyingPrice().length() == 0)
					{
						msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[컨텐츠 구매 가격 Validation Fail [페이나우]]";
						imcsLog.serviceLog(msg, methodName, methodLine);
						result_set = -1;
						return result_set;
					}
					
				} catch (Exception e) {
					msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[컨텐츠 구매 가격 Validation Fail [페이나우]]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					result_set = -1;
					return result_set;
				}
				
				switch (paramVO.getBuyTypeFlag()) {
				case "1":
					if( Integer.parseInt(paramVO.getRealBuyingPrice()) < 100 ){
						msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[페이나우 구매(컨텐츠) 100원보다 작을 경우 Validation Fail]";
						imcsLog.serviceLog(msg, methodName, methodLine);
						result_set = -1;
						return result_set;
					}
					break;
					
				case "2":
					if( Integer.parseInt(paramVO.getRealDatafreeBuyPrice()) < 100 ){
						msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[페이나우 구매(데이터프리) 100원보다 작을 경우 Validation Fail]";
						imcsLog.serviceLog(msg, methodName, methodLine);
						result_set = -1;
						return result_set;
					}
					break;
					
				case "3":
					if( Integer.parseInt(paramVO.getRealBuyingPrice()) + Integer.parseInt(paramVO.getRealDatafreeBuyPrice()) < 100 ){
						msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[페이나우 구매(컨텐츠+데이터프리) 100원보다 작을 경우 Validation Fail]";
						imcsLog.serviceLog(msg, methodName, methodLine);
						result_set = -1;
						return result_set;
					}
					break;

				default:
					result_set = -1;
					return result_set;
				}
				
				/* 페이나우 구매인데, 컨텐츠and데이터프리 구매 가격이 둘 다 0일 수는 없다. */
				if( Integer.parseInt(paramVO.getRealBuyingPrice()) == 0 && Integer.parseInt(paramVO.getRealDatafreeBuyPrice()) == 0 ){
					msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[페이나우 > 컨텐츠+데이터프리 구매 가격 0원 Validation Fail]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					result_set = -1;
					return result_set;
				}								
				
				break;
				
			case ImcsConstants.CP_USE_YN_INAPP:
				
				/* 컨텐츠 가격이 숫자가 아닐 경우 FAIL */
				try {
					Integer.parseInt(paramVO.getSuggestedPrice());
				} catch (Exception e) {
					msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[컨텐츠 제공 가격 Validation Fail]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					result_set = -1;
					return result_set;
				}
				
				/*
				 * 컨텐츠 구매 가격이 숫자가 아닐 경우 또는 음수나 NULL일 경우 FAIL
				 * */
				try {
					if( Integer.parseInt(paramVO.getBuyingPrice()) < 0
						|| paramVO.getBuyingPrice().length() == 0
						|| Integer.parseInt(paramVO.getRealBuyingPrice()) < 0
						|| paramVO.getRealBuyingPrice().length() == 0)
					{
						msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[컨텐츠 구매 가격 Validation Fail [인앱결제]]";
						imcsLog.serviceLog(msg, methodName, methodLine);
						result_set = -1;
						return result_set;
					}
					
				} catch (Exception e) {
					msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[컨텐츠 구매 가격 Validation Fail [인앱결제]]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					result_set = -1;
					return result_set;
				}
				
				switch (paramVO.getBuyTypeFlag()) {
				case "1":
					// 2019.12.27 - 인앱결제는 콘텐츠 구매만 가능
					break;
					
				case "2":
				case "3":
					msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[인앱결제는 데이터프리 구매 불가 Validation Fail]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					result_set = -1;
					return result_set;

				default:
					result_set = -1;
					return result_set;
				}
				
				/* 인앱결제는 부가세포함 가격이 0원이면 안되고, 부가세 미포함 콘텐츠가격과 구매가격이 동일해야 한다. */
				if( Integer.parseInt(paramVO.getRealBuyingPrice()) == 0 && !(Integer.parseInt(paramVO.getSuggestedPrice()) == Integer.parseInt(paramVO.getBuyingPrice())) ){
					msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[인앱결제 > 부가세 포함 콘텐츠 가격 0원  or 부가세 미포함 콘텐츠가격과 구매가격이 다름 Validation Fail]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					result_set = -1;
					return result_set;
				}
				
				// 2020.01.02 - 인앱의 경우 부가세 포함 가격을 그대로 PT_VO_BUY_DM_DETAIL_NSC의 DM_UDR_PRICE에 넣는다. 
				paramVO.setUdrBuyPrice( Integer.parseInt(paramVO.getRealBuyingPrice()));
				
				break;
			case ImcsConstants.CP_USE_YN_NAVER:
				/* 컨텐츠 가격이 숫자가 아닐 경우 FAIL */
				try {
					Integer.parseInt(paramVO.getSuggestedPrice());
				} catch (Exception e) {
					msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[컨텐츠 제공 가격 Validation Fail]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					result_set = -1;
					return result_set;
				}
				
				/* 컨텐츠 구매 가격이 숫자가 아닐 경우 또는 음수나 NULL일 경우 FAIL */
				try {
					if( Integer.parseInt(paramVO.getBuyingPrice()) < 0
						|| paramVO.getBuyingPrice().length() == 0
						|| Integer.parseInt(paramVO.getRealBuyingPrice()) < 0
						|| paramVO.getRealBuyingPrice().length() == 0)
					{
						msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[컨텐츠 구매 가격 Validation Fail]";
						imcsLog.serviceLog(msg, methodName, methodLine);
						result_set = -1;
						return result_set;
					}
					
				} catch (Exception e) {
					msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[컨텐츠 구매 가격 Validation Fail]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					result_set = -1;
					return result_set;
				}
				
				/* 신용카드 결제인데, 컨텐츠and데이터프리 구매 가격이 둘 다 0일 수는 없다. */
				if( Integer.parseInt(paramVO.getRealBuyingPrice()) == 0 && Integer.parseInt(paramVO.getRealDatafreeBuyPrice()) == 0 ){
					msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[네이버pay > 컨텐츠+데이터프리 구매 가격 0원 Validation Fail]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					result_set = -1;
					return result_set;
				}
				break;
			case ImcsConstants.CP_USE_YN_KAKAO:
				/* 컨텐츠 가격이 숫자가 아닐 경우 FAIL */
				try {
					Integer.parseInt(paramVO.getSuggestedPrice());
				} catch (Exception e) {
					msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[컨텐츠 제공 가격 Validation Fail]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					result_set = -1;
					return result_set;
				}
				
				/* 컨텐츠 구매 가격이 숫자가 아닐 경우 또는 음수나 NULL일 경우 FAIL */
				try {
					if( Integer.parseInt(paramVO.getBuyingPrice()) < 0
						|| paramVO.getBuyingPrice().length() == 0
						|| Integer.parseInt(paramVO.getRealBuyingPrice()) < 0
						|| paramVO.getRealBuyingPrice().length() == 0)
					{
						msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[컨텐츠 구매 가격 Validation Fail]";
						imcsLog.serviceLog(msg, methodName, methodLine);
						result_set = -1;
						return result_set;
					}
					
				} catch (Exception e) {
					msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[컨텐츠 구매 가격 Validation Fail]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					result_set = -1;
					return result_set;
				}
				
				/* 신용카드 결제인데, 컨텐츠and데이터프리 구매 가격이 둘 다 0일 수는 없다. */
				if( Integer.parseInt(paramVO.getRealBuyingPrice()) == 0 && Integer.parseInt(paramVO.getRealDatafreeBuyPrice()) == 0 ){
					msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[카카오pay > 컨텐츠+데이터프리 구매 가격 0원 Validation Fail]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					result_set = -1;
					return result_set;
				}
				break;
				
			default:
				msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[buying_type Validation Fail]";
				imcsLog.serviceLog(msg, methodName, methodLine);
				result_set = -1;
				return result_set;
			}
			
			/* 쿠폰+멤버쉽 포인트 중복 사용 불가 */
			if( ( "1".equals(paramVO.getDiscountDiv()[ImcsConstants.DISCOUNT_DIV_COUPON])
				|| "1".equals(paramVO.getDatafreeDiscountDiv()[ImcsConstants.DISCOUNT_DIV_COUPON]) )
				&& "1".equals(paramVO.getDiscountDiv()[ImcsConstants.DISCOUNT_DIV_MEMBERSHIP]) )
			{
				msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[쿠폰+멤버쉽 포인트 중복 사용 불가 Validation Fail]";
				imcsLog.serviceLog(msg, methodName, methodLine);
				result_set = -1;
				return result_set;
			}
			
			// 2019.07.31 - VR1.5(OCULUS 결제)는 할인혜택을 받을 수 없다.
			// 2019.12.27 - 인앱결제는 할인혜택을 받을 수 없다.
			if( (paramVO.getBuyingType().equals(ImcsConstants.CP_USE_YN_OCULUS) || paramVO.getBuyingType().equals(ImcsConstants.CP_USE_YN_INAPP)) 
					&& ( "1".equals(paramVO.getDiscountDiv()[ImcsConstants.DISCOUNT_DIV_COUPON]) || "1".equals(paramVO.getDiscountDiv()[ImcsConstants.DISCOUNT_DIV_MEMBERSHIP]) 
						|| "1".equals(paramVO.getDiscountDiv()[ImcsConstants.DISCOUNT_DIV_TVPOINT]) || "1".equals(paramVO.getDiscountDiv()[ImcsConstants.DISCOUNT_DIV_KLUPOINT]) 
						) 
				)			  
			{
				msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[OCULUS/INAPP 할인혜택 사용 불가 Validation Fail]";
				imcsLog.serviceLog(msg, methodName, methodLine);
				result_set = -1;
				return result_set;
			}
			
			/* 컨텐츠 할인 정보 Validation */
			for(int i=0; i<ImcsConstants.DISCOUNT_CNT; i++){
				
				if( i == 2 ){
					
					switch (paramVO.getBuyTypeFlag()) {
					case "1":
						// 쿠폰+멤버쉽 할인 후 부가세 계산
						// 컨텐츠 구매일 때에는 내림
						real_conts_valid = real_conts_valid + (real_conts_valid / szSurtaxRate);					
						break;
					case "2":
						// 쿠폰+멤버쉽 할인 후 부가세 계산
						// 데이터프리 구매일 때에는 내림
						real_df_valid = real_df_valid + (real_df_valid / szSurtaxRate);					
						break;
					case "3":
						// 쿠폰+멤버쉽 할인 후 부가세 계산
						// 컨텐츠+데이터프리 구매일 때에는 컨텐츠 올림, 데이터프리 내림
						if( paramVO.getDatafreeDiscountCnt() > 0){
							real_conts_valid = real_conts_valid + (int) Math.ceil(Math.floor(real_conts_valid) / szSurtaxRate);
							real_df_valid = real_df_valid + (real_df_valid / szSurtaxRate);
						}else{
							real_conts_valid = real_conts_valid + (real_conts_valid / szSurtaxRate);
							real_df_valid = real_df_valid + (real_df_valid / szSurtaxRate);
						}
						break;
						
					default:
						result_set = -1;
						return result_set;
					}
					
				}
				
				switch (i) {
				case ImcsConstants.DISCOUNT_DIV_COUPON:
					// 컨텐츠 할인 정보 Validation
					switch (paramVO.getDiscountDiv()[i]) {
					case "0":
						try {
							if( Integer.parseInt(paramVO.getDiscountPrice()[i]) < 0
								|| Integer.parseInt(paramVO.getDiscountPrice()[i]) > 0
								|| paramVO.getDiscountPrice()[i].length() == 0 )
							{
								msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[컨텐츠 쿠폰할인 가격(할인X -> 0원 아닐때) Validation Fail]";
								imcsLog.serviceLog(msg, methodName, methodLine);
								result_set = -1;	
								return result_set;
							}
							
						} catch (Exception e) {
							msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[컨텐츠 쿠폰할인 가격(할인X -> 0원 아닐때) Validation Fail]";
							imcsLog.serviceLog(msg, methodName, methodLine);
							result_set = -1;
							return result_set;
						}
						break;
						
					case "1":
						conts_valid = conts_valid - Integer.parseInt(paramVO.getDiscountPrice()[i]);
						real_conts_valid = real_conts_valid - Integer.parseInt(paramVO.getDiscountPrice()[i]);
						alwnce_charge_st_tmp += Integer.parseInt(paramVO.getDiscountPrice()[i]);
						paramVO.setDiscountCnt(paramVO.getDiscountCnt() + 1);
						
						try {
							if( Integer.parseInt(paramVO.getDiscountPrice()[i]) <= 0
								|| paramVO.getDiscountPrice()[i].length() == 0 )
							{
								msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[컨텐츠 쿠폰할인 가격 Validation Fail]";
								imcsLog.serviceLog(msg, methodName, methodLine);
								result_set = -1;
								return result_set;
							}
							
						} catch (Exception e) {
							msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[컨텐츠 쿠폰할인 가격 Validation Fail]";
							imcsLog.serviceLog(msg, methodName, methodLine);
							result_set = -1;
							return result_set;
						}
						
						/* 신규 쿠폰을 사용하겠다고 했는데, 자/타사 구분이 없으면 FAIL */
						switch (paramVO.getOfferBuyingType()) {
						// 쿠폰(자사)만 공제유형이고, 쿠폰(타사)는 공제유형으로 보지 않는다.
						case ImcsConstants.CP_USE_YN_INNERCOUPON:
							// 20190425 - 오과금 AS-IS
//							paramVO.setUdrBuyPrice( paramVO.getUdrBuyPrice() - Integer.parseInt(paramVO.getDiscountPrice()[i]) );
							break;
						case ImcsConstants.CP_USE_YN_OTHERCOUPON:
							// 20190425 - 오과금 TO-BE
							paramVO.setUdrBuyPrice( paramVO.getUdrBuyPrice() + Integer.parseInt(paramVO.getDiscountPrice()[i]) );
							break;
						default:
							msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[컨텐츠 쿠폰 자/사타 구분 Validation Fail]";
							imcsLog.serviceLog(msg, methodName, methodLine);
							result_set = -1;
							return result_set;
						}
						/* 신규쿠폰인데, 건수/비율/가격 할인 쿠폰인지 정보가 없으면 FAIL */
						switch (paramVO.getAlwnceUnt()) {
						case "1":
							// 신규 쿠폰은 건수 할인이 없는 것으로 알고 있음.
							msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[신규 쿠폰 > 건수 할인 Validation Fail]";
							imcsLog.serviceLog(msg, methodName, methodLine);
							result_set = -1;
							return result_set;
						case "2":
						case "3":
							break;

						default:
							msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[신규 쿠폰 Alwnce_Unt Validation Fail]";
							imcsLog.serviceLog(msg, methodName, methodLine);
							result_set = -1;
							return result_set;
						}
						/* 신규쿠폰 정보 없으면 FAIL */
						if(paramVO.getOfrSeq() == null || "".equals(paramVO.getOfrSeq())){
							msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[신규 쿠폰 ofr_seq Validation Fail]";
							imcsLog.serviceLog(msg, methodName, methodLine);
							result_set = -1;
							return result_set;
						}
						/* 신규쿠폰 정보 없으면 FAIL */
						if(paramVO.getOfferCd() == null || "".equals(paramVO.getOfferCd())){
							msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[신규 쿠폰 offer_cd Validation Fail]";
							imcsLog.serviceLog(msg, methodName, methodLine);
							result_set = -1;
							return result_set;
						}
						/* 신규쿠폰 정보 없으면 FAIL */
						if(paramVO.getOfferNm() == null || "".equals(paramVO.getOfferNm())){
							msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[신규 쿠폰 offer_nm Validation Fail]";
							imcsLog.serviceLog(msg, methodName, methodLine);
							result_set = -1;
							return result_set;
						}
						/* 신규쿠폰인데, 컨텐츠/데이터프리/컨텐츠+데이터프리 할인 쿠폰인지 정보가 없으면 FAIL */
						switch (paramVO.getOfferType()) {
						case "1":
						case "3":						
							break;
						case "2":
							//컨텐츠 쿠폰할인인데, 데이터프리 쿠폰할인하겠다고 온다면 FAIL
							msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[컨텐츠 > 데이터프리 쿠폰할인일때 Validation Fail]";
							imcsLog.serviceLog(msg, methodName, methodLine);
							result_set = -1;
							return result_set;
						default:
							msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[컨텐츠 offer_type Validation Fail]";
							imcsLog.serviceLog(msg, methodName, methodLine);
							result_set = -1;
							return result_set;
						}
						
						break;					
						
					default:
						result_set = -1;
						return result_set;
					}
					
					// 데이터프리 할인 정보 Validation
					switch (paramVO.getDatafreeDiscountDiv()[i]) {
					case "0":
						try {
							if( Integer.parseInt(paramVO.getDatafreeDiscountPrice()[i]) <0
								|| Integer.parseInt(paramVO.getDatafreeDiscountPrice()[i]) > 0
								|| paramVO.getDatafreeDiscountPrice()[i].length() == 0 )
							{
								msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[데이터프리 쿠폰할인 가격 (할인X -> 0원 아닐때) Validation Fail]";
								imcsLog.serviceLog(msg, methodName, methodLine);
								result_set = -1;
								return result_set;
							}
							
						} catch (Exception e) {
							msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[데이터프리 쿠폰할인 가격 (할인X -> 0원 아닐때) Validation Fail]";
							imcsLog.serviceLog(msg, methodName, methodLine);
							result_set = -1;
							return result_set;
						}
						break;
					case "1":
						df_valid = df_valid - Integer.parseInt(paramVO.getDatafreeDiscountPrice()[i]);
						real_df_valid = real_df_valid - Integer.parseInt(paramVO.getDatafreeDiscountPrice()[i]);
						alwnce_charge_st_tmp += Integer.parseInt(paramVO.getDatafreeDiscountPrice()[i]);
						paramVO.setDatafreeDiscountCnt(paramVO.getDatafreeDiscountCnt() + 1);
						
						try {
							if( Integer.parseInt(paramVO.getDatafreeDiscountPrice()[i]) <= 0
								|| paramVO.getDatafreeDiscountPrice()[i].length() == 0 )
							{
								msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[데이터프리 쿠폰할인 가격 Validation Fail]";
								imcsLog.serviceLog(msg, methodName, methodLine);
								result_set = -1;
								return result_set;
							}
							
						} catch (Exception e) {
							msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[데이터프리 쿠폰할인 가격 Validation Fail]";
							imcsLog.serviceLog(msg, methodName, methodLine);
							result_set = -1;
							return result_set;
						}
						/* 신규 쿠폰을 사용하겠다고 했는데, 자/타사 구분이 없으면 FAIL */
						switch (paramVO.getOfferBuyingType()) {
						case ImcsConstants.CP_USE_YN_INNERCOUPON:
							// 20190425 - 오과금 AS-IS
//							paramVO.setUdrDfBuyPrice(paramVO.getUdrDfBuyPrice() - Integer.parseInt(paramVO.getDatafreeDiscountPrice()[i]) );
							break;							
						case ImcsConstants.CP_USE_YN_OTHERCOUPON:
							// 20190425 - 오과금 TO-BE
							paramVO.setUdrDfBuyPrice(paramVO.getUdrDfBuyPrice() + Integer.parseInt(paramVO.getDatafreeDiscountPrice()[i]) );
							break;

						default:
							msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[데이터프리 쿠폰 자/사타 구분 Validation Fail]";
							imcsLog.serviceLog(msg, methodName, methodLine);
							result_set = -1;
							return result_set;
						}
						/* 신규쿠폰인데, 건수/비율/가격 할인 쿠폰인지 정보가 없으면 FAIL */
						switch (paramVO.getAlwnceUnt()) {
						case "1":
							// 신규 쿠폰은 건수 할인이 없는 것으로 알고 있음.
							msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[신규 쿠폰 > 건수 할인 Validation Fail]";
							imcsLog.serviceLog(msg, methodName, methodLine);
							result_set = -1;
							return result_set;
						case "2":
						case "3":
							break;

						default:
							msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[신규 쿠폰 Alwnce_Unt Validation Fail]";
							imcsLog.serviceLog(msg, methodName, methodLine);
							result_set = -1;
							return result_set;
						}
						/* 신규쿠폰 정보 없으면 FAIL */
						if(paramVO.getOfrSeq() == null || "".equals(paramVO.getOfrSeq())){
							msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[신규 쿠폰 ofr_seq Validation Fail]";
							imcsLog.serviceLog(msg, methodName, methodLine);
							result_set = -1;
							return result_set;
						}
						/* 신규쿠폰 정보 없으면 FAIL */
						if(paramVO.getOfferCd() == null || "".equals(paramVO.getOfferCd())){
							msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[신규 쿠폰 offer_cd Validation Fail]";
							imcsLog.serviceLog(msg, methodName, methodLine);
							result_set = -1;
							return result_set;
						}
						/* 신규쿠폰 정보 없으면 FAIL */
						if(paramVO.getOfferNm() == null || "".equals(paramVO.getOfferNm())){
							msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[신규 쿠폰 offer_nm Validation Fail]";
							imcsLog.serviceLog(msg, methodName, methodLine);
							result_set = -1;
							return result_set;
						}
						/* 신규쿠폰인데, 컨텐츠/데이터프리/컨텐츠+데이터프리 할인 쿠폰인지 정보가 없으면 FAIL */
						switch (paramVO.getOfferType()) {
						case "1":
							//데이터 쿠폰할인인데, 컨텐츠 쿠폰할인하겠다고 온다면 FAIL
							msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[컨텐츠 > 데이터프리 쿠폰할인일때 Validation Fail]";
							imcsLog.serviceLog(msg, methodName, methodLine);
							result_set = -1;
							return result_set;
						case "3":	
						case "2":
							break;
						default:
							msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[데이터프리 offer_type Validation Fail]";
							imcsLog.serviceLog(msg, methodName, methodLine);
							result_set = -1;
							return result_set;
						}
						break;

					default:
						result_set = -1;
						return result_set;
					}
					break;
				
				case ImcsConstants.DISCOUNT_DIV_MEMBERSHIP:
					/* 멤버쉽 할인 Validation 제거 - MIMS도 멤버쉽 서버에서 한도율 받는다고 함 */
					
					// 컨텐츠 할인 정보 Validation
					switch (paramVO.getDiscountDiv()[i]) {
					case "0":
						try {
							if( Integer.parseInt(paramVO.getDiscountPrice()[i]) < 0
								|| Integer.parseInt(paramVO.getDiscountPrice()[i]) > 0
								|| paramVO.getDiscountPrice()[i].length() == 0 )
							{
								msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[컨텐츠 쿠폰할인 가격(할인X -> 0원 아닐때) Validation Fail]";
								imcsLog.serviceLog(msg, methodName, methodLine);
								result_set = -1;	
								return result_set;
							}
							
						} catch (Exception e) {
							msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[컨텐츠 쿠폰할인 가격(할인X -> 0원 아닐때) Validation Fail]";
							imcsLog.serviceLog(msg, methodName, methodLine);
							result_set = -1;
							return result_set;
						}
						break;
						
					case "1":
						conts_valid = conts_valid - Integer.parseInt(paramVO.getDiscountPrice()[i]);
						real_conts_valid = real_conts_valid - Integer.parseInt(paramVO.getDiscountPrice()[i]);
						alwnce_charge_st_tmp += Integer.parseInt(paramVO.getDiscountPrice()[i]);
						paramVO.setDiscountCnt(paramVO.getDiscountCnt() + 1);
						// 20190425 - 오과금 AS-IS
//						paramVO.setUdrBuyPrice(paramVO.getUdrBuyPrice() - Integer.parseInt(paramVO.getDiscountPrice()[i]));
						
						try {
							/* 할인가격이 숫자가 아닌지, 0원 또는 음수값이 들어왔는지 확인 */
							if( Integer.parseInt(paramVO.getDiscountPrice()[i]) <= 0
								|| paramVO.getDiscountPrice()[i].length() == 0 )
							{
								msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[컨텐츠 할인 가격 Validation Fail]";
								imcsLog.serviceLog(msg, methodName, methodLine);
								result_set = -1;
								return result_set;
							}
							
						} catch (Exception e) {
							msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[컨텐츠 할인 정보 Validation Fail]";
							imcsLog.serviceLog(msg, methodName, methodLine);
							result_set = -1;
							return result_set;
						}
											
						break;					
						
					default:
						result_set = -1;
						return result_set;
					}
					
					// 데이터프리 할인 정보 Validation
					switch (paramVO.getDatafreeDiscountDiv()[i]) {
					case "0":
						try {
							if( Integer.parseInt(paramVO.getDatafreeDiscountPrice()[i]) <0
								|| Integer.parseInt(paramVO.getDatafreeDiscountPrice()[i]) > 0
								|| paramVO.getDatafreeDiscountPrice()[i].length() == 0 )
							{
								msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[데이터프리 쿠폰할인 가격 (할인X -> 0원 아닐때) Validation Fail]";
								imcsLog.serviceLog(msg, methodName, methodLine);
								result_set = -1;
								return result_set;
							}
							
						} catch (Exception e) {
							msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[데이터프리 쿠폰할인 가격 (할인X -> 0원 아닐때) Validation Fail]";
							imcsLog.serviceLog(msg, methodName, methodLine);
							result_set = -1;
							return result_set;
						}
						break;
					case "1":
						msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[멤버쉽 할인인데, 데이터프리 할인 Validation Fail]";
						imcsLog.serviceLog(msg, methodName, methodLine);
						result_set = -1;
						return result_set;

					default:
						msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[데이터프리 할인 정보 Validation Fail]";
						imcsLog.serviceLog(msg, methodName, methodLine);
						result_set = -1;
						return result_set;
					}
					
					break;
				case ImcsConstants.DISCOUNT_DIV_TVPOINT:
				case ImcsConstants.DISCOUNT_DIV_KLUPOINT:
					
					// 컨텐츠 할인 정보 Validation
					switch (paramVO.getDiscountDiv()[i]) {
					case "0":
						try {
							if( Integer.parseInt(paramVO.getDiscountPrice()[i]) < 0
								|| Integer.parseInt(paramVO.getDiscountPrice()[i]) > 0
								|| paramVO.getDiscountPrice()[i].length() == 0 )
							{
								msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[컨텐츠 쿠폰할인 가격(할인X -> 0원 아닐때) Validation Fail]";
								imcsLog.serviceLog(msg, methodName, methodLine);
								result_set = -1;	
								return result_set;
							}
							
						} catch (Exception e) {
							msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[컨텐츠 쿠폰할인 가격(할인X -> 0원 아닐때) Validation Fail]";
							imcsLog.serviceLog(msg, methodName, methodLine);
							result_set = -1;
							return result_set;
						}
						break;
						
					case "1":
						conts_valid = conts_valid - Integer.parseInt(paramVO.getDiscountPrice()[i]);
						real_conts_valid = real_conts_valid - Integer.parseInt(paramVO.getDiscountPrice()[i]);
						alwnce_charge_st_tmp += Integer.parseInt(paramVO.getDiscountPrice()[i]);
						paramVO.setDiscountCnt(paramVO.getDiscountCnt() + 1);						
						try {
							/* 할인가격이 숫자가 아닌지, 0원 또는 음수값이 들어왔는지 확인 */
							if( Integer.parseInt(paramVO.getDiscountPrice()[i]) <= 0
								|| paramVO.getDiscountPrice()[i].length() == 0 )
							{
								msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[컨텐츠 할인 가격 Validation Fail]";
								imcsLog.serviceLog(msg, methodName, methodLine);
								result_set = -1;
								return result_set;
							}
							
						} catch (Exception e) {
							msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[컨텐츠 할인 정보 Validation Fail]";
							imcsLog.serviceLog(msg, methodName, methodLine);
							result_set = -1;
							return result_set;
						}
						// 20190425 - 오과금 TO-BE
						paramVO.setUdrBuyPrice( paramVO.getUdrBuyPrice() + Integer.parseInt(paramVO.getDiscountPrice()[i]) );
						break;					
						
					default:
						result_set = -1;
						return result_set;
					}
					
					// 데이터프리 할인 정보 Validation
					switch (paramVO.getDatafreeDiscountDiv()[i]) {
					case "0":
						try {
							if( Integer.parseInt(paramVO.getDatafreeDiscountPrice()[i]) < 0
								|| Integer.parseInt(paramVO.getDatafreeDiscountPrice()[i]) > 0
								|| paramVO.getDatafreeDiscountPrice()[i].length() == 0 )
							{
								msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[데이터프리 쿠폰할인 가격 (할인X -> 0원 아닐때) Validation Fail]";
								imcsLog.serviceLog(msg, methodName, methodLine);
								result_set = -1;
								return result_set;
							}
							
						} catch (Exception e) {
							msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[데이터프리 쿠폰할인 가격 (할인X -> 0원 아닐때) Validation Fail]";
							imcsLog.serviceLog(msg, methodName, methodLine);
							result_set = -1;
							return result_set;
						}
						break;
					case "1":
						df_valid = df_valid - Integer.parseInt(paramVO.getDatafreeDiscountPrice()[i]);
						real_df_valid = real_df_valid - Integer.parseInt(paramVO.getDatafreeDiscountPrice()[i]);
						alwnce_charge_st_tmp += Integer.parseInt(paramVO.getDatafreeDiscountPrice()[i]);
						paramVO.setDatafreeDiscountCnt(paramVO.getDatafreeDiscountCnt() + 1);
						
						try {
							if( Integer.parseInt(paramVO.getDatafreeDiscountPrice()[i]) <= 0
								|| paramVO.getDatafreeDiscountPrice()[i].length() == 0 )
							{
								msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[데이터프리 쿠폰할인 가격 Validation Fail]";
								imcsLog.serviceLog(msg, methodName, methodLine);
								result_set = -1;
								return result_set;
							}
							
						} catch (Exception e) {
							msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[데이터프리 쿠폰할인 가격 Validation Fail]";
							imcsLog.serviceLog(msg, methodName, methodLine);
							result_set = -1;
							return result_set;
						}
						// 20190425 - 오과금 TO-BE
						paramVO.setUdrDfBuyPrice( paramVO.getUdrDfBuyPrice() + Integer.parseInt(paramVO.getDatafreeDiscountPrice()[i]) );
						break;
					default:
						msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[데이터프리 할인 정보 Validation Fail]";
						imcsLog.serviceLog(msg, methodName, methodLine);
						result_set = -1;
						return result_set;
					}
					
					break;
					
				default:
					msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[할인 정보 Validation Fail]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					result_set = -1;
					return result_set;
				}					
			}
			
			/* 일반,예약 구매에 따른 Validation */
			switch (paramVO.getBuyingGb()) {
			case "N":			
				break;
			
			case "R":			
				/* 예약구매의 경우 청구서 결제만 가능하며, 할인혜택을 받을 수 없다. */
				/* 2017.06.26 예약구매도 할인혜택 적용 */
				/* 20170717 예약구매도 신용카드/페이나우 결제가 가능하다. (오형석K) */
//				if( paramVO.getDiscountCnt() > 0 || !"N".equals(paramVO.getBuyingType()) || !"1".equals(paramVO.getBuyTypeFlag()) ){
//					msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[buying_gb 예약구매 Validation Fail]";
//					imcsLog.serviceLog(msg, methodName, methodLine);
//					result_set = -1;
//					return result_set;				
//				}
				// 2019.07.31 - VR1.5(OCULUS 결제)에서는 예약 구매를 할 수 없다.
				// 2019.12.27 - 인앱결제는 예약구매를 할 수 없다.
				if(paramVO.getBuyingType().equals(ImcsConstants.CP_USE_YN_OCULUS) || paramVO.getBuyingType().equals(ImcsConstants.CP_USE_YN_INAPP))
				{
					msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[예약구매->OCULUS/INAPP 결제 불가 Validation Fail]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					result_set = -1;
					return result_set;
				}
				break;

			default:
				msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[buying_gb Validation Fail]";
				imcsLog.serviceLog(msg, methodName, methodLine);
				result_set = -1;
				return result_set;	
			}
			
			// 음수가 나올 경우 0원 처리를 한다.
			if(conts_valid < 0)
			{
				conts_valid = 0;
			}
			if(df_valid < 0)
			{
				df_valid = 0;
			}
			
			/* 부가세 포함 금액이 소수점이 나올 때에는 컨텐츠 가격 올림, 데이터프리 가격은 내림으로 계산하여 나온 값으로 가격 검증한다. */
			switch (paramVO.getBuyTypeFlag()) {
			case "3":
				/* 컨텐츠 구매 가격이 서버에서 계산한 가격과 동일한지 비교.  */
				if( conts_valid != Integer.parseInt(paramVO.getBuyingPrice()) ){
					msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[컨텐츠 구매 가격 FAILURE [0 <> "+conts_valid+"]]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					result_set = -1;
					return result_set;
				}
				
				/* 컨텐츠 실구매 가격(부가세포함)이 서버에서 계산한 가격과 동일한지 비교. */
				if( Integer.parseInt(paramVO.getRealBuyingPrice()) != real_conts_valid ){
					msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[컨텐츠 구매 가격(부가세포함) != (컨텐츠가격-할인가격<TV포인트/KLU포인트는 부가세포함계산>) ["+Integer.parseInt(paramVO.getRealBuyingPrice())+" <> "+real_conts_valid+"]]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					result_set = -1;
					return result_set;
				}
				
				/* 데이터프리 구매 가격이 서버에서 계산한 가격과 동일한지 비교.  */
				if(df_valid != Integer.parseInt(paramVO.getDatafreeBuyPrice()) ){
					msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[데이터프리 구매 가격 FAILURE [0 <> "+df_valid+"]]";
					imcsLog.serviceLog(msg, methodName, methodLine);					
					result_set = -1;
				}
				
				/* 데이터프리 실구매 가격(부가세포함)이 서버에서 계산한 가격과 동일한지 비교. */
				if( Integer.parseInt(paramVO.getRealDatafreeBuyPrice()) != real_df_valid ){
					msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[데이터프리 구매 가격(부가세포함) != (데이터프리 가격-할인가격<TV포인트/KLU포인트는 부가세포함계산>) ["+Integer.parseInt(paramVO.getRealDatafreeBuyPrice())+" <> "+real_df_valid+"]]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					result_set = -1;
					return result_set;
				}
				
				break;
			case "1":
				/* 컨텐츠 구매 가격이 서버에서 계산한 가격과 동일한지 비교.  */
				if( conts_valid != Integer.parseInt(paramVO.getBuyingPrice()) ){
					msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[컨텐츠 구매 가격 FAILURE [0 <> "+conts_valid+"]]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					result_set = -1;
					return result_set;
				}
				
				// 2019.12.27 - 인앱결제의 경우 부가세 포함 금액을 요율로 계산하지 않고, 인앱테이블에서 정해진 가격으로 확인 한다. (즉, valid함수에서는 체크하지 않고, 후에 인앱가격 가져오고 체크)
				if( !paramVO.getBuyingType().equals(ImcsConstants.CP_USE_YN_INAPP) )
				{
					/* 컨텐츠 실구매 가격(부가세포함)이 서버에서 계산한 가격과 동일한지 비교. */
					if( Integer.parseInt(paramVO.getRealBuyingPrice()) != real_conts_valid ){
						msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[컨텐츠 구매 가격(부가세포함) != (컨텐츠가격-할인가격<TV포인트/KLU포인트는 부가세포함계산>) ["+Integer.parseInt(paramVO.getRealBuyingPrice())+" <> "+real_conts_valid+"]]";
						imcsLog.serviceLog(msg, methodName, methodLine);					
						result_set = -1;
						return result_set;
					}
				}
				
				break;
			case "2":
				/* 데이터프리 구매 가격이 서버에서 계산한 가격과 동일한지 비교.  */
				if(df_valid != Integer.parseInt(paramVO.getDatafreeBuyPrice()) ){
					msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[데이터프리 구매 가격 FAILURE [0 <> "+df_valid+"]]";
					imcsLog.serviceLog(msg, methodName, methodLine);	
					result_set = -1;
				}
				
				/* 데이터프리 실구매 가격(부가세포함)이 서버에서 계산한 가격과 동일한지 비교. */
				if( Integer.parseInt(paramVO.getRealDatafreeBuyPrice()) != real_df_valid ){
					msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg[데이터프리 구매 가격(부가세포함) != (데이터프리 가격-할인가격<TV포인트/KLU포인트는 부가세포함계산>) ["+Integer.parseInt(paramVO.getRealDatafreeBuyPrice())+" <> "+real_df_valid+"]]";
					imcsLog.serviceLog(msg, methodName, methodLine);	
					
					result_set = -1;
					return result_set;
				}
				
				break;
			default:
				break;
			}
			
			paramVO.setAlwnceCharge( Integer.toString(alwnce_charge_st_tmp) );
			
		} catch (Exception e) {
			result_set = -1;
		}
		
		return result_set;
	}
	
	/**
	 * MIMS를 통한 정상구매인지 여부 체크
	 * @param paramVO
	 * @return
	 */
	public Integer getBuyChkMIMS(BuyNSDMContsRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		BuyNSDMContsRequestVO requestVO = new BuyNSDMContsRequestVO();
		List<BuyNSDMContsRequestVO> list = new ArrayList<BuyNSDMContsRequestVO>();
		
		Integer result_set = -1;
		String msg = "";
		

		try {
			
			list = buyNSDMContsDao.getBuyChkMIMS(paramVO);
			if( list != null && list.get(0) != null ){
				
				result_set = 0;
				
				requestVO = list.get(0);
				
				if( !requestVO.getSaId().equals(paramVO.getSaId())
					|| !requestVO.getStbMac().equals(paramVO.getStbMac())
					|| !requestVO.getAlbumId().equals(paramVO.getAlbumId())
				){
					
					msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] MIMS구매 정보와 일치하지 않습니다.(MIMS INFO:"+paramVO.getSaId()+","+paramVO.getStbMac()+","+paramVO.getAlbumId()+")";
					IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
					imcsLog.serviceLog(msg, methodName, methodLine);
					
					result_set = -1;
				}
				
			}
			
			
			
		} catch (Exception e) {
			paramVO.setResultCode("31000000");
			
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID014, "", null, ImcsConstants.RCV_MSG6, methodName, methodLine);
		}

		return result_set;
	}
	
	
	/**
     * 가입자 상태, 개통여부 조회
     * @param vo
     * @return
     */
    public ComSbcVO getSbcInfo(BuyNSDMContsRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();    	
		
		List<ComSbcVO> list   = new ArrayList<ComSbcVO>();
		ComSbcVO resultVO = null;
		int SbcInfoCnt = 0;
		
		try {
			try{
				list = buyNSDMContsDao.getSbcInfo(paramVO);
			} catch(DataAccessException e) {
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list == null || list.isEmpty()){
				paramVO.setResultSet(-1);
			} else {
				resultVO	= list.get(0);
			}
			
			for(SbcInfoCnt = 0; SbcInfoCnt < list.size() ; SbcInfoCnt++)
			{
				if(!list.get(SbcInfoCnt).getBlockFlag().equals("N"))
				{
					block_flag = 1;
				}
			}
					
		} catch (Exception e) {
			paramVO.setResultSet(-1);
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			throw new ImcsException();
		}
		
    	return resultVO;
    }
    
    /**
     * 상품정보 조회 (정액/종량)
     * @param	GetNSDMContsRequestVO
     * @return
     */
    public ComPriceVO getBillType(BuyNSDMContsRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();    	
    	
    	String sqlId = "nsvod014_002_20171214_001";		
		List<ComPriceVO> list   = new ArrayList<ComPriceVO>();
		ComPriceVO resultVO = null;
		
		try {
			
			try{
				list = buyNSDMContsDao.getBillType(paramVO);
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list == null || list.isEmpty()){
				paramVO.setResultSet(-1);
				
				String szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] no data found album_id[" + paramVO.getAlbumId() + "] [" + paramVO.getBuyTypeFlag() +"]";
				IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
				imcsLog.serviceLog(szMsg, methodName, methodLine);
				
			} else {
				resultVO	= list.get(0);
				
			    // 2019.09.04 - 라이센스 유효 기간 외 구매 제한
				if (paramVO.getBuyingDate().substring(0, 8).compareTo(resultVO.getLicenseStart()) >= 0)
				{
			    	if (paramVO.getBuyingDate().substring(0, 8).compareTo(resultVO.getLicenseEnd()) <= 0)
					{
						resultVO.setLicensingValidYn("Y");
					}else
					{
						resultVO.setLicensingValidYn("N");
					}
				}
				else
				{
					resultVO.setLicensingValidYn("N");
				}
				
				if(resultVO.getPayFlag().equals("1")) {//아이돌라이브 유료콘서트
					paramVO.setNscGb("I");
				}
			}
			
		} catch (Exception e) {
			paramVO.setResultSet(-1);
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID014, sqlId, cache, "bill_type:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
    	return resultVO;
    }
    
    /**
     * 데이터프리 정보 조회
     * @param	ComDataFreeVO, BuyNSDMContsRequestVO
     * @result	ComDataFreeVO
    **/
    public ComDataFreeVO getDatafreeInfo(ComDataFreeVO tempVO, BuyNSDMContsRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
    	
    	String sqlId = "nsvod014_s01_20171214_001";
    	String szMsg = "";
		List<ComDataFreeVO> list   = new ArrayList<ComDataFreeVO>();
		ComDataFreeVO resultVO = null;
		
		if(!"".equals(tempVO.getPrice())){
			try {
				try{
					list = buyNSDMContsDao.getDatafreeInfo(tempVO);
				}catch(DataAccessException e){
					//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
					throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
				}
				
				if( list != null && !list.isEmpty()){
					resultVO = (ComDataFreeVO)list.get(0);
				}else{
					szMsg	= "Not Found Approval Info["+ tempVO.getPrice() +"]";
					IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
					imcsLog.serviceLog(szMsg, methodName, methodLine);
					
					resultVO = new ComDataFreeVO();
					resultVO.setDatafreePrice("0");
					resultVO.setApprovalPrice("");
					resultVO.setDatafreeApprovalPrice("");
					resultVO.setPpvDatafreeApprovalPrice("");
					
				}
				
			} catch (Exception e) {
				
				resultVO = new ComDataFreeVO();
				resultVO.setDatafreePrice("0");
				resultVO.setApprovalPrice("");
				resultVO.setDatafreeApprovalPrice("");
				resultVO.setPpvDatafreeApprovalPrice("");
				
//				 imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID014, sqlId, cache, "approval_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
				 
				 paramVO.setResultCode("41000000");
				 
				 paramVO.setResultSet(-1);
			}
		}else{
			
			resultVO = new ComDataFreeVO();
			resultVO.setDatafreePrice("0");
			resultVO.setApprovalPrice("");
			resultVO.setDatafreeApprovalPrice("");
			resultVO.setPpvDatafreeApprovalPrice("");
			
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID014, sqlId, cache, "approval_info: nosql error row_key_arr or row_key size", methodName, methodLine);
			 
			 paramVO.setResultCode("41000000");
			 
			 paramVO.setResultSet(-1);
		}
		
		
    	return resultVO;
    }
    
    /**
     * 가입자 구매상품 여부 조회
     * @param vo
     * @return
     * @throws Exception
     */
    public int getCustomerProdChk(BuyNSDMContsRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		
		List<Integer> list   = new ArrayList<Integer>();
		Integer resultVO = 0;
		
		try {
			
			try{
				list = buyNSDMContsDao.getCustomerProdChk(paramVO);
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list == null || list.isEmpty()){
//				querySize	= 0;
				resultVO	= 0;
			} else {
//				querySize	= list.size();
				resultVO	= (Integer)list.get(0);
			}

			
//			try{
//				imcsLog.dbLog(ImcsConstants.NSAPI_PRO_ID014, sqlId, cache, querySize, methodName, methodLine);
//			}catch(Exception e){}
		} catch (Exception e) {
			paramVO.setResultSet(-1);
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			throw new ImcsException();
		}
		
    	return resultVO;    	
    }
    
    /**
     * 데이터프리 구매내역 중복 확인
     * @param 	GetNSDMContsRequestVO
     * @return	Integer
     */
    public Integer chkDatafreeDup(BuyNSDMContsRequestVO paramVO) throws Exception{
		List<ComDupCHk> lstDupChk	= new ArrayList<ComDupCHk>();
		ComDupCHk dupChkVO = new ComDupCHk();
		
		Integer nDupChk = 0;
		
    	try {
    		
    		lstDupChk = buyNSDMContsDao.chkDatafreeDup(paramVO);
    		
    		if(lstDupChk != null && !lstDupChk.isEmpty()){
    			dupChkVO	= lstDupChk.get(0);
    			
    			paramVO.setResultBuyDate(dupChkVO.getBuyDate());
    			nDupChk = Integer.parseInt(StringUtil.nullToZero(dupChkVO.getDataChk()));
    		} else 
    			nDupChk	= 0;
    		
		} catch (Exception e) {			
			paramVO.setResultSet(-1);
			
			 paramVO.setResultCode("40000000");
			
		}
    	
    	return nDupChk;
    }
    
    /**
     * 패키지 컨텐츠 보관함 조회
     * @param vo
     * @return
     */
    public List<ContTypeVO> getPkgContent(BuyNSDMContsRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		List<ContTypeVO> list   = new ArrayList<ContTypeVO>();
		
		try {
			
			try{
				if("N".equals(paramVO.getPpsId())){
					list = buyNSDMContsDao.getPkgContent(paramVO);
				} else {
					list = buyNSDMContsDao.getPkgContent2(paramVO);
				}
				
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
				
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			throw new ImcsException();
		}
		
    	return list;
    }
    
    /**
     * 패키지 컨텐츠 구매내역 저장
     * @param paramVO
     * @return
     * @throws Exception
     */
    public Integer insBuyConts3(BuyNSDMContsRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
    	
//    	String sqlId = "nsvod010_i01_20171214_001";
    	String szMsg = "";
    	Integer querySize = 0;
		    	
    	try {
    		
    		try{
    			querySize = buyNSDMContsDao.insBuyConts3(paramVO);
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
		} catch (Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] insert [PT_VO_BUY_DETAIL_NSC] table Failed at";
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID014, null, null, "fail info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID014, sqlId, cache, "fail info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
//			paramVO.setSqlCode(cache.getLastException().getErrorCode());
		}
    	
		return querySize;
	}
    
    /**
     * 단품 장르 정보 조회
     * @param paramVO
     * @return
     */
    public ContTypeVO getGenreType(BuyNSDMContsRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();    	
    	
    	String sqlId = "nsvod014_014_20171214_001";
		int querySize = 0;
		List<ContTypeVO> list   = new ArrayList<ContTypeVO>();
		ContTypeVO resultVO	= null;
		
		try {
			
			try{
				list = buyNSDMContsDao.getGenreType(paramVO);
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list == null || list.isEmpty()) {
				querySize	= 0;
			} else {
				querySize	= list.size();
				resultVO	= list.get(0);
			}
			
			
			try {
//				imcsLog.dbLog(ImcsConstants.NSAPI_PRO_ID014, sqlId, cache, querySize, methodName, methodLine);
			} catch (Exception e) {}
						
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			throw new ImcsException();
		}
		
    	return resultVO;
    }
    
    /**
     * 단품 보관함 저장
     * @param paramVO
     * @return
     * @throws Exception
     */
    public Integer insBuyConts4(BuyNSDMContsRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
    	
    	String sqlId = "";
    	String szMsg = "";
    	
//    	if( "0".equals(paramVO.getProdType()) )  	sqlId =  "nsvod014_i02_20171214_001";
//    	else							    		sqlId =  "nsvod014_i03_20171214_001";
		
    	Integer querySize = 0;    	
    	try {
    		
    		try{
    			querySize = buyNSDMContsDao.insBuyConts4(paramVO);
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			try {
//				imcsLog.dbLog(ImcsConstants.NSAPI_PRO_ID014, sqlId, cache, querySize, methodName, methodLine);
//				imcsLog.dbLog(ImcsConstants.NSAPI_PRO_ID014, sqlId, null, querySize, methodName, methodLine);
				
				if( "0".equals(paramVO.getProdType()) ){
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] insert [PT_VO_BUY_DETAIL_FVOD_NSC] table[" + querySize + "] records Success at";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
				}else{
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] insert [PT_VO_BUY_DETAIL_NSC] table[" + querySize + "] records Success at";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
				}	
			} catch (Exception e) {}
			
		} catch (Exception e) {
			
			if( "0".equals(paramVO.getProdType()) ){
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] insert [PT_VO_BUY_DETAIL_FVOD_NSC] table Failed at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			}else{
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] insert [PT_VO_BUY_DETAIL_NSC] table Failed at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			}			
			
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID014, sqlId, null, "fail info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID014, sqlId, cache, "fail info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
//			paramVO.setSqlCode(cache.getLastException().getErrorCode());
		}
		
		return querySize;
	}
    
    /**
     * 컨텐츠 할인정보 등록
     * @param paramVO
     * @return
     * @throws Exception
     */
    public Integer buyInsDiscount(BuyNSDMContsRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
    	String szMsg = "";
    	Integer querySize = 0;
    	
    	try {
    		
			int i = 0;
			if(paramVO.getDiscountCnt() > 0){
				while( i < ImcsConstants.DISCOUNT_CNT){
					switch (i) {
					
					case ImcsConstants.DISCOUNT_DIV_COUPON:
						switch (paramVO.getDiscountDiv()[i]) {
						case "0":			
							paramVO.setDisCpYn("N");							
							break;
						case "1":		
							paramVO.setDisCpYn("Y");
							paramVO.setDisCpPrice(paramVO.getDiscountPrice()[i]);
							break;
						default:
							paramVO.setDisCpYn("N");	
							break;
						}						
						break;
						
					case ImcsConstants.DISCOUNT_DIV_MEMBERSHIP:
						switch (paramVO.getDiscountDiv()[i]) {
						case "0":			
							paramVO.setDisMbYn("N");							
							break;
						case "1":		
							paramVO.setDisMbYn("Y");
							paramVO.setDisMbPrice(paramVO.getDiscountPrice()[i]);
							break;
						default:
							paramVO.setDisMbYn("N");	
							break;
						}						
						break;
					
					case ImcsConstants.DISCOUNT_DIV_TVPOINT:
						switch (paramVO.getDiscountDiv()[i]) {
						case "0":			
							paramVO.setDisTvYn("N");							
							break;
						case "1":		
							paramVO.setDisTvYn("Y");
							paramVO.setDisTvPrice(paramVO.getDiscountPrice()[i]);
							break;
						default:
							paramVO.setDisTvYn("N");	
							break;
						}						
						break;
						
					case ImcsConstants.DISCOUNT_DIV_KLUPOINT:
						switch (paramVO.getDiscountDiv()[i]) {
						case "0":			
							paramVO.setDisKuYn("N");							
							break;
						case "1":		
							paramVO.setDisKuYn("Y");
							paramVO.setDisKuPrice(paramVO.getDiscountPrice()[i]);
							break;
						default:
							paramVO.setDisKuYn("N");	
							break;
						}						
						break;
						
					default:
						break;
					}
					i++;
				}
			}
			
			try{
				querySize = buyNSDMContsDao.buyInsDiscount(paramVO);
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			try {
//				imcsLog.dbLog(ImcsConstants.NSAPI_PRO_ID014, sqlId, cache, querySize, methodName, methodLine);
//				imcsLog.dbLog(ImcsConstants.NSAPI_PRO_ID014, "", null, querySize, methodName, methodLine);
				
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] insert [PT_VO_BUY_DM_DETAIL] table[" + querySize + "] records Success at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
				
			} catch (Exception e) {}
			
		} catch (Exception e) {
			
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] insert [PT_VO_BUY_DM_DETAIL] table Failed at";
			imcsLog.serviceLog(szMsg, methodName, methodLine);		
			
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID014, "", cache, "fail info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID014, "", null, "fail info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
		return querySize;
	}
    
    /**
     * 데이터프리 할인정보 등록
     * @param paramVO
     * @return
     * @throws Exception
     */
    public Integer buyInsDfDiscount(BuyNSDMContsRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
    	
    	String szMsg = "";
    	Integer querySize = 0;
		    	
    	try {
			
			int i = 0;
			paramVO.setDisCpYn("N");
			paramVO.setDisMbYn("N");
			paramVO.setDisTvYn("N");
			paramVO.setDisKuYn("N");
			
			if(paramVO.getDatafreeDiscountCnt() > 0){
				while( i < ImcsConstants.DISCOUNT_CNT){
					switch (i) {
					
					case ImcsConstants.DISCOUNT_DIV_COUPON:
						switch (paramVO.getDatafreeDiscountDiv()[i]) {
						case "0":			
							paramVO.setDisCpYn("N");							
							break;
						case "1":		
							paramVO.setDisCpYn("Y");
							paramVO.setDisDfCpPrice(paramVO.getDatafreeDiscountPrice()[i]);
							break;
						default:
							paramVO.setDisCpYn("N");	
							break;
						}						
						break;
						
					case ImcsConstants.DISCOUNT_DIV_MEMBERSHIP:
						switch (paramVO.getDatafreeDiscountDiv()[i]) {
						case "0":			
							paramVO.setDisMbYn("N");							
							break;
						case "1":		
							paramVO.setDisMbYn("Y");
							paramVO.setDisDfMbPrice(paramVO.getDatafreeDiscountPrice()[i]);
							break;
						default:
							paramVO.setDisMbYn("N");	
							break;
						}						
						break;
					
					case ImcsConstants.DISCOUNT_DIV_TVPOINT:
						switch (paramVO.getDatafreeDiscountDiv()[i]) {
						case "0":			
							paramVO.setDisTvYn("N");							
							break;
						case "1":		
							paramVO.setDisTvYn("Y");
							paramVO.setDisDfTvPrice(paramVO.getDatafreeDiscountPrice()[i]);
							break;
						default:
							paramVO.setDisTvYn("N");	
							break;
						}						
						break;
						
					case ImcsConstants.DISCOUNT_DIV_KLUPOINT:
						switch (paramVO.getDatafreeDiscountDiv()[i]) {
						case "0":			
							paramVO.setDisKuYn("N");							
							break;
						case "1":		
							paramVO.setDisKuYn("Y");
							paramVO.setDisDfKuPrice(paramVO.getDatafreeDiscountPrice()[i]);
							break;
						default:
							paramVO.setDisKuYn("N");	
							break;
						}						
						break;
						
					default:
						break;
					}
					
					i++;
				}
			}
			
			try{
				// 2020.02.19 - Fortify 보안 툴에서 SQL에서 $를 사용하면 안된다고 하여 $datafreecnt + 1을 #datafreecnt로 변경하기 위해 로직에서 +1을 한다.
				paramVO.setDatafreeDiscountCnt(paramVO.getDatafreeDiscountCnt() + 1);
				querySize = buyNSDMContsDao.buyInsDfDiscount(paramVO);
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			try {
//				imcsLog.dbLog(ImcsConstants.NSAPI_PRO_ID014, sqlId, cache, querySize, methodName, methodLine);
//				imcsLog.dbLog(ImcsConstants.NSAPI_PRO_ID014, "", null, querySize, methodName, methodLine);
				
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] insert [PT_VO_BUY_DM_DETAIL_NSC] table[" + querySize + "] records Success at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
				
			} catch (Exception e) {}
			
		} catch (Exception e) {
			
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] insert [PT_VO_BUY_DM_DETAIL_NSC] table Failed at";
			imcsLog.serviceLog(szMsg, methodName, methodLine);		
			
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID014, sqlId, cache, "fail info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID014, "", null, "fail info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
		return querySize;
	}

    /**
     * 구매내역 저장    
     * @param vo
     * @return
     * @throws Exception
     */
    public Integer insBuyConts1(BuyNSDMContsRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
    	Integer querySize = 0;		
		String szMsg = "";
		    	
    	try {
			
    		
    		try{
				
    			querySize = buyNSDMContsDao.insBuyConts1(paramVO);
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			try {
				
//				imcsLog.dbLog(ImcsConstants.NSAPI_PRO_ID014, sqlId, cache, querySize, methodName, methodLine);
//				imcsLog.dbLog(ImcsConstants.NSAPI_PRO_ID014, "", null, querySize, methodName, methodLine);
				
				if( "0".equals(paramVO.getProdType()) ){
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] insert [PT_VO_BUY_FVOD_NSC] table[" + querySize + "] records Success at";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
				}else{
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] insert [PT_VO_BUY_NSC] table[" + querySize + "] records Success at";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
				}	
			} catch (Exception e) {}
			
		} catch (Exception e) {
			
			if( "0".equals(paramVO.getProdType()) ){
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] insert [PT_VO_BUY_FVOD_NSC] table Failed at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			}else{
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] insert [PT_VO_BUY_NSC] table Failed at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			}			
			
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID014, "", cache, "fail info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID014, "", null, "fail info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
		return querySize;
	}
    
    /**
     * 패키지 상품정보 조회
     * @param vo
     * @return
     */
    public ContTypeVO getProduct(BuyNSDMContsRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();    	
    	
    	String sqlId = "";
    	String szMsg = "";
    	
    	if("R".equals(paramVO.getBuyingGb()))		sqlId =  "nsvod014_021_20171214_001";
    	else						    			sqlId =  "nsvod014_011_20171214_001";
		
		int querySize = 0;
		List<ContTypeVO> list   = new ArrayList<ContTypeVO>();
		ContTypeVO resultVO = null;
		
		try {
			try{
				list = buyNSDMContsDao.getProduct(paramVO);
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list == null || list.isEmpty()){
				querySize	= 0;
				list		= null;
			} else {
				querySize	= list.size();
				resultVO	= list.get(0);
			}
			
			
			try {
//				imcsLog.dbLog(ImcsConstants.NSAPI_PRO_ID014, sqlId, cache, querySize, methodName, methodLine);
			} catch (Exception e) {}
									
		} catch (Exception e) {
//			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] sts [" + cache.getLastException().getErrorCode() + "]"
//					+ " msg[패키지 정보 조회 실패]";	
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.serviceLog(szMsg, methodName, methodLine);
		}
		
    	return resultVO;
    }
    
    /**
     * 구매내역 저장
     * @param paramVO
     * @return
     * @throws Exception
     */
    public Integer insBuyConts2(BuyNSDMContsRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
    	
    	String szMsg = "";
    	Integer querySize = 0;
		    	
    	try {

    	
	    	try{				
	    		querySize = buyNSDMContsDao.insBuyConts2(paramVO);
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			try {
//				imcsLog.dbLog(ImcsConstants.NSAPI_PRO_ID014, sqlId, cache, querySize, methodName, methodLine);
//				imcsLog.dbLog(ImcsConstants.NSAPI_PRO_ID014, "", null, querySize, methodName, methodLine);
				
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] insert [PT_VO_BUY_NSC] table[" + querySize + "] records Success at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
				
			} catch (Exception e) {}
			
		} catch (Exception e) {
			
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] insert [PT_VO_BUY_NSC] table Failed at";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
		
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID014, "", cache, "fail info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID014, "", null, "fail info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
		return querySize;
	}
    
    public Integer buyDatafree(BuyNSDMContsRequestVO paramVO) throws Exception{
    	
    	Integer result = 0;
    	
    	result = this.insertDatafreeContent(paramVO);
    	
    	if(result > 0){
    		result = this.insertDatafreeDetail(paramVO);
    	}
    	
    	return result;
    	
    }
    
    /**
     *  데이터프리 구매내역 저장
     *  @param	BuyContsCpRequestVO
     *  @result	Integer
     */
    public Integer insertDatafreeContent(BuyNSDMContsRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
    	    	
    	String szMsg = "";
    	Integer querySize = 0;
	
    	try {
    		
    		if("0".equals(paramVO.getProdType())){
    			
    			try{					
    				querySize = buyNSDMContsDao.insertDatafreeContent1(paramVO);
				}catch(DataAccessException e){
					//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
					throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
				}
    			
    		}else if("1".equals(paramVO.getProdType())){
    			
    			try{
					querySize = buyNSDMContsDao.insertDatafreeContent2(paramVO);
				}catch(DataAccessException e){
					//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
					throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
				}
    		}
			
			try {
//				imcsLog.dbLog(ImcsConstants.NSAPI_PRO_ID014, sqlId, cache, querySize, methodName, methodLine);
//				imcsLog.dbLog(ImcsConstants.NSAPI_PRO_ID014, "", null, querySize, methodName, methodLine);
				
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] insert [PT_VO_BUY] table[" + querySize + "] records Success at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
				
			} catch (Exception e) {}
			
		} catch (Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] insert [PT_VO_BUY] table Failed at";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID014, sqlId, cache, "fail info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID014, "", null, "fail info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
		return querySize;
	}
    
    /**
     *  데이터프리 구매내역 저장
     *  @param	BuyContsCpRequestVO
     *  @result	Integer
     */
    public Integer insertDatafreeDetail(BuyNSDMContsRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
    	
    	String szMsg = "";
    	Integer querySize = 0;

    	try {
    		
			try{
				querySize = buyNSDMContsDao.insertDatafreeDetail(paramVO);
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			
		} catch (Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] insert [PT_VO_BUY_DETAIL] table Failed at";
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID014, "", null, "fail info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
		return querySize;
	}
    
    /**
	 * 장르 정보 조회
	 * @param paramVO
	 * @return
	 */
	public String getGenreInfo(BuyNSDMContsRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		
		String sqlId		= "nsvod014_p01_20171214_001";
		String szGenreInfo	= "";
		List<GenreInfoVO> list	= new ArrayList<GenreInfoVO>();
		GenreInfoVO genreVO		= null;
		
		try {
			
			try {
				list = buyNSDMContsDao.getGenreInfo(paramVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			if (list != null && !list.isEmpty()) {
				genreVO		= list.get(0);
				
				if(genreVO != null)
					szGenreInfo	= StringUtil.nullToSpace(genreVO.getGenreLarge()) + "|" + StringUtil.nullToSpace(genreVO.getGenreMid())
						+ "|" + StringUtil.nullToSpace(genreVO.getGenreSmall());
			}
			
			
		} catch (Exception e) {
			paramVO.setResultSet(-1);
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}

		return szGenreInfo;
	}
	
	
    
	
	/**
	 * 쿠폰 정보 조회 (SMARTUX 함수 이용)
	 * @param paramVO
	 * @return
	 */
	public ComCpnVO getCpnInfo(BuyNSDMContsRequestVO paramVO) throws Exception {
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName	= oStackTrace.getMethodName();
		String methodLine	= String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		long tp1, tp2	= 0;
		
		ComCpnVO rtnCpnInfoVO = new ComCpnVO();
		ComCpnVO cpnInfoVO = new ComCpnVO();
		
		String szMsg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] START smartux info : p_idx_sa["+paramVO.getpIdxSa()+"] album_id["+paramVO.getAlbumId()+"] product["+paramVO.getProdType()+"] "
    			+ "price["+paramVO.getBillSuggestedPrice()+"] genre["+paramVO.getGenreInfo()+"] pkg["+paramVO.getPkgYn()+"]";
		imcsLog.serviceLog(szMsg, methodName, methodLine);
		
		// 발급가능쿠폰 정보 조회
		tp1	= System.currentTimeMillis();
		try{
			cpnInfoVO	= buyNSDMContsDao.getCpnPossibleList(paramVO);
			
			if(cpnInfoVO != null && cpnInfoVO.getCpnInfo() != null && !"".equals(cpnInfoVO.getCpnInfo())) {
				//2018.12.22 - VR앱 : VR앱으로 조회 하는 경우 스탬프/쿠폰 정보를 단말에 주지 않는다.
				if(!(paramVO.getAppType().equals("") || paramVO.getAppType() == null) && !paramVO.getAppType().substring(0, 1).equals("E")) {
					rtnCpnInfoVO.setCpnInfo("CPN01" + ImcsConstants.COLSEP + cpnInfoVO.getCpnInfo() + ImcsConstants.COLSEP);
				}
				
				// 쿠폰 존재 여부 조회 및 쿠폰 정보 입력
				this.insCpnInfo(paramVO, cpnInfoVO.getCpnInsInfo());
			} else {
				cpnInfoVO = new ComCpnVO();
			}

			szMsg	= " SELECT smartux.F_GET_CPN_COND_POSSIBLE_LIST =[" + StringUtil.nullToSpace(cpnInfoVO.getCpnInfo()) + "]";
		    imcsLog.serviceLog(szMsg, methodName, methodLine);
		    
		} catch(Exception e) {
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID014, "", null, "coupon(mims)_info:" + ImcsConstants.RCV_MSG2, methodName, methodLine);
		}
		
		tp2	= System.currentTimeMillis();
		imcsLog.timeLog("발급가능쿠폰(F_GET_CPN_COND_POSSIBLE_LIST)", String.valueOf(tp2 - tp1), methodName, methodLine);
		
		
		// 스탬프 정보 조회
		try{
			cpnInfoVO	= buyNSDMContsDao.getStmPossibleList(paramVO);
			
			if(cpnInfoVO != null && cpnInfoVO.getStmInfo() != null && !"".equals(cpnInfoVO.getStmInfo())) {
				//2018.12.22 - VR앱 : VR앱으로 조회 하는 경우 스탬프/쿠폰 정보를 단말에 주지 않는다.
				if(!(paramVO.getAppType().equals("") || paramVO.getAppType() == null) && !paramVO.getAppType().substring(0, 1).equals("E")) {
					rtnCpnInfoVO.setStmInfo("STP00" + ImcsConstants.COLSEP + cpnInfoVO.getStmInfo() + ImcsConstants.COLSEP);
				}
			    
				// 쿠폰 존재 여부 조회 및 쿠폰 정보 입력
				this.insStmInfo(paramVO, cpnInfoVO.getStmInsInfo());
			} else {
				cpnInfoVO = new ComCpnVO();
			}
			
			szMsg	= " SELECT smartux.F_GET_STM_COND_POSSIBLE =[" + StringUtil.nullToSpace(cpnInfoVO.getStmInfo()) + "]";
		    imcsLog.serviceLog(szMsg, methodName, methodLine);
		    
		} catch(Exception e) {
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID014, "", null, "coupon(mims)_info:" + ImcsConstants.RCV_MSG2, methodName, methodLine);
		}
		
		tp1	= System.currentTimeMillis();
		imcsLog.timeLog("발급가능스탬프(F_GET_STM_COND_POSSIBLE)", String.valueOf(tp1 - tp2), methodName, methodLine);
		
		
		//2018.12.22 - VR앱 : VR앱으로 조회 하는 경우 스탬프/쿠폰 정보를 단말에 주지 않는다.
		if(!(paramVO.getAppType().equals("") || paramVO.getAppType() == null) && !paramVO.getAppType().substring(0, 1).equals("E")) {
			// 사용 가능 쿠폰 정보 조회
			try{
				cpnInfoVO	= buyNSDMContsDao.getUseCpnPossibleList(paramVO);
				
				if(cpnInfoVO != null && cpnInfoVO.getUseCpnInfo() != null && !"".equals(cpnInfoVO.getUseCpnInfo())) {
					rtnCpnInfoVO.setUseCpnInfo("CPN02" + ImcsConstants.COLSEP + cpnInfoVO.getUseCpnInfo() + ImcsConstants.COLSEP);
				} else {
					cpnInfoVO = new ComCpnVO();
				}
				
				szMsg	= " SELECT smartux.F_GET_CPN_USE_POSSIBLE_LIST =[" + StringUtil.nullToSpace(cpnInfoVO.getUseCpnInfo()) + "]";
			    imcsLog.serviceLog(szMsg, methodName, methodLine);
			    
			    
			} catch(Exception e) {
	//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID014, "", null, "coupon(mims)_info:" + ImcsConstants.RCV_MSG2, methodName, methodLine);
			}
		
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("사용가능스쿠폰(F_GET_CPN_USE_POSSIBLE_LIST)", String.valueOf(tp2 - tp1), methodName, methodLine);
		}

		return rtnCpnInfoVO;
	}
	
	/**
	 * 스탬프 조회 후 스탬프 존재시 INSERT
	 * @param paramVO
	 * @return
	 */
	public int insStmInfo(BuyNSDMContsRequestVO paramVO, String szData) throws Exception {
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		szData = StringUtil.nullToSpace(szData);
		String[] arrData	= null;
		String[] arrInfo	= null;
		
		int nDataCnt	= 1;
		int nResult		= 0;

		String szMsg	= "";
		String szCpnId	= "";
		
		if("".equals(szData)) return -1;
		
		arrData	= szData.split(ImcsConstants.ARRSEP);
		nDataCnt = arrData.length;
		
		for(int i = 0; i < nDataCnt; i++) {
			if("".equals(arrData) || arrData[i].indexOf("|") < 0)	
				return -1;
			
			arrInfo = arrData[i].split("\\|");
			szCpnId	= arrInfo[1];
			paramVO.setStrmpId(szCpnId);
			
			// 스탬프 정보 저장
			try {
				nResult = buyNSDMContsDao.insStmInfo(paramVO);
				
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] insert [PT_CPM_STAMP_BOX_ACTION] STEMP_BOX_ACTION table [" + nResult + "] records Success at ";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			} catch(Exception e) { 	nResult	= -1;	}
			
			if(nResult < 1) {
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] insert [PT_CPM_STAMP_BOX_ACTION] STEMP_BOX_ACTION table Failed at ";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
				break;
			}
			
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] INSERT COUPON_BOX_ACTION table records Success : STAMP_ID[" + paramVO.getStrmpId() + "] ";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
		}
		
		return nResult;
	}
	



	/**
	 * 쿠폰정보 조회 후 쿠폰 존재시 INSERT
	 * @param paramVO
	 * @return
	 */
	public int insCpnInfo(BuyNSDMContsRequestVO paramVO, String szData) throws Exception {
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		szData = StringUtil.nullToSpace(szData);
		String[] arrData	= null;
		String[] arrInfo	= null;
		
		int nDataCnt	= 1;
		int nResult		= 0;

		String szMsg	= "";
		String szCpnId	= "";
		
		if("".equals(szData)) return -1;
		
		arrData	= szData.split("\\^");
		nDataCnt = arrData.length;
		
		for(int i = 0; i < nDataCnt; i++) {
			if("".equals(arrData) || arrData[i].indexOf("|") < 0)	
				return -1;
			
			arrInfo = arrData[i].split("\\|");
			szCpnId	= arrInfo[1];
			paramVO.setCpevtId(szCpnId);
			
			// 쿠폰 존재여부 체크
			try {
				nResult = buyNSDMContsDao.insCpnInfo(paramVO);
				
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] insert [SEQ_PT_CPM_COUPON_BOX_ACTION] COUPON_BOX_ACTION table [" + nResult + "] records Success at ";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			} catch(Exception e) { 	nResult	= -1;	}
			
			if(nResult < 1) {
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] insert [SEQ_PT_CPM_COUPON_BOX_ACTION] COUPON_BOX_ACTION table Failed at ";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
				break;
			}
			
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] INSERT PT_CPM_COUPON_BOX_ACTION table records Success : CPEVT_ID[" + paramVO.getCpevtId() + "] ";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
		}
		
		return nResult;
	}
    
	/**
     * FVOD를 제외한 PPV or PPS 구매시 메타 정보를 NPT_VO_BUY_META테이블에 별도로 저장한다.    
     * @param vo
     * @return
     * @throws Exception
     */
    public Integer insBuyMeta(BuyNSDMContsRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
    	Integer querySize = 0;		
		String szMsg = "";
		    	
    	try {    		
    		try{
    			// 2019.10.30 - VOD 정산 프로세스 개선 : FVOD는 저장하지 않는다. (단, FVOD+데이터프리 구매일 때에는 저장한다.)
    			if( !( "0".equals(paramVO.getProdType()) && !"2".equals(paramVO.getBuyTypeFlag()) ) || ( "N".equals(paramVO.getPkgYn()) && "Y".equals(paramVO.getIsLGU()) && "N".equals(paramVO.getIsPayDatafree()) ) ){    			
    				querySize = buyNSDMContsDao.insBuyMeta(paramVO);
    			    						
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] insert [NPT_VO_BUY_META] table[" + querySize + "] records Success at";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
				}
    			else
    			{
    				querySize = 1;
    				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] insert [NPT_VO_BUY_META] table pass(FVOD)";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
    			}
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}		
		} catch (Exception e) {			
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] insert [NPT_VO_BUY_META] table Failed at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);			
		}
		
		return querySize;
	}
    
    /**
	 * 특정 APP별 결제/할인 수단 제어
	 * @param paramVO
	 * @return
	 */
	public Integer getPaymentBlock(BuyNSDMContsRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		List<BlockVO> list = new ArrayList<BlockVO>();
		
		// result_set이 0이면, 구매 가능 / -1이면 구매 불가능
		Integer result_set = 0;
		String msg = "";
		

		try {
			
			list = buyNSDMContsDao.getPaymentBlock(paramVO);
			if( list != null && list.get(0) != null ){
				
				for(BlockVO Block : list)
				{
					// 데이터 조회된 row 단위로 구매 가능/불가능 여부 체크 (-1 : 구매 불가능 / 0 : 구매 가능)
					int block_flag = -1;
					
					if(!(Block.getAppType().equals(paramVO.getAppType().substring(0, 1)) || Block.getAppType().equals("0")))
					{												
						block_flag = 0;
					}
					
					if(block_flag == -1)
					{
						if(!(Block.getUserType().equals(paramVO.getAppType().substring(1, 2)) || Block.getUserType().equals("0")))
						{
							block_flag = 0;
						}
					}
					
					if(block_flag == -1)
					{
						if(!(Block.getPhoneType().equals(paramVO.getAppType().substring(2, 3)) || Block.getPhoneType().equals("0")))
						{
							block_flag = 0;
						}
					}
					
					if(block_flag == -1)
					{
						if(!(Block.getOsType().equals(paramVO.getAppType().substring(3, 4)) || Block.getOsType().equals("0")))
						{
							block_flag = 0;
						}
					}
					
					// 결제수단 차단
					if(block_flag == -1 && Block.getBlockType().equals("P"))
					{
						if(!(Block.getPaymentType().equals(paramVO.getBuyingType())))
						{
							block_flag = 0;
						}
						else
						{
							msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] 요청한 APP_TYPE은 ( " + paramVO.getBuyingType() + " ) 타입 결제가 불가능 합니다. ";
						}
					}
					// 할인수단 차단
					else if(block_flag == -1 && Block.getBlockType().equals("S"))
					{
						if(Block.getPaymentType().equals(ImcsConstants.CP_USE_YN_INNERCOUPON) || Block.getPaymentType().equals(ImcsConstants.CP_USE_YN_OTHERCOUPON))
						{
							if(paramVO.getDiscountDiv()[0].equals("0") && paramVO.getDatafreeDiscountDiv()[0].equals("0"))
							{
								block_flag = 0;
							}
							else
							{
								msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] 요청한 APP_TYPE은 쿠폰 할인이 불가능 합니다. ";
							}
						}
						else if(Block.getPaymentType().equals(ImcsConstants.CP_USE_YN_MEMBERSHIP))
						{
							if(paramVO.getDiscountDiv()[1].equals("0") && paramVO.getDatafreeDiscountDiv()[1].equals("0"))
							{
								block_flag = 0;
							}
							else
							{
								msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] 요청한 APP_TYPE은 멤버십 할인이 불가능 합니다. ";
							}
						}
						else if(Block.getPaymentType().equals(ImcsConstants.CP_USE_YN_TVPOINT))
						{
							if(paramVO.getDiscountDiv()[2].equals("0") && paramVO.getDatafreeDiscountDiv()[2].equals("0"))
							{
								block_flag = 0;
							}
							else
							{
								msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] 요청한 APP_TYPE은 TV포인트 할인이 불가능 합니다. ";
							}
						}
						else if(Block.getPaymentType().equals(ImcsConstants.CP_USE_YN_KLUPOINT))
						{
							if(paramVO.getDiscountDiv()[3].equals("0") && paramVO.getDatafreeDiscountDiv()[3].equals("0"))
							{
								block_flag = 0;
							}
							else
							{
								msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] 요청한 APP_TYPE은 KLU포인트 할인이 불가능 합니다. ";
							}
						}
					}
					
					if(block_flag == -1)
					{
						result_set = -1;
						break;
					}
				}				
				
				IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
				imcsLog.serviceLog(msg, methodName, methodLine);
			}
		} catch (Exception e) {			
			// 2021.02.09 - 쿼리나 로직상에 문제라고 한다면 요청온대로 구매할 수 있도록 한다. (구매 못 하게 하는 것이 더 문제인 것으로 판단)
			result_set = 0;
		}

		return result_set;
	}
	
	/**
     * 아이돌라이브 유료콘서트 컨텐츠를 구매 가능한지 조회
     * @param vo
     * @return
     * @throws Exception
     */
    public Integer chkConsert(BuyNSDMContsRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
    	Integer result = 0;		
		String szMsg = "";
		    	
    	try {    		
    		try{
    			// 아이돌라이브 유료콘서트 구매가능 조회
    			result = buyNSDMContsDao.chkConsert(paramVO);
				if(result == null) {
					result = 0;
				}
    			
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}		
		} catch (Exception e) {		
				result = 0;
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] chkConsert Failed";
				imcsLog.serviceLog(szMsg, methodName, methodLine);			
		}
		
		return result;
	}
	
	public Integer makeCacheFile(BuyNSDMContsRequestVO paramVO) throws Exception {
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		String LOCALPATH = "";
		String NASPATH = ""; 
		String DATFILE = ""; // 파일생성 결로
		String idolKeyFileName = ""; // 키파일명
		Integer result_set = 0;
		String msg = "";
		
		try {
			

			
    		
			LOCALPATH = commonService.getCachePath("LOCAL", "idollive-cache", imcsLog);
			NASPATH = commonService.getCachePath("NAS", "idollive-cache", imcsLog);
			
			String pkgFlag = "";
			
			if(paramVO.getPkgYn().equals("Y")) {
				pkgFlag = "2";
			} else {
				pkgFlag = "1";
			}
			
			this.imcsCacheService.putEhcacheValue(ImcsCacheService.IDOL_LIVE_BUY_CACHE,
					paramVO.getSaId() + "-" + paramVO.getStbMac() + "-" + paramVO.getAlbumId(), pkgFlag + "|" + paramVO.getBuyingDate()); // 구매 정보 캐시에 저장

			// 하위 폴더 경로 생성
			String localDirName = String.format("%s/%s/%s", LOCALPATH, paramVO.getAlbumId(),
					paramVO.getSaId().substring(paramVO.getSaId().length() - 4, paramVO.getSaId().length()));
			String nasDirName = String.format("%s/%s/%s", NASPATH, paramVO.getAlbumId(),
					paramVO.getSaId().substring(paramVO.getSaId().length() - 4, paramVO.getSaId().length()));
			
			LOCALPATH = localDirName;
			NASPATH = nasDirName;
			
			File LOCAL_DIR = new File(LOCALPATH);
			if(!LOCAL_DIR.exists()){
				LOCAL_DIR.mkdirs();
			}
			
			File NAS_DIR = new File(NASPATH);
			if(!NAS_DIR.exists()){
				NAS_DIR.mkdirs();
			}
			
			idolKeyFileName = paramVO.getSaId() + "-" + paramVO.getStbMac() + "-" + paramVO.getAlbumId() + ".dat";
			DATFILE = LOCALPATH + "/" + idolKeyFileName;
			// 파일 쓰기
			int nRetVal = FileUtil.fileWrite(DATFILE, pkgFlag + "|" + paramVO.getBuyingDate(), false);
			
			if (nRetVal == 1) {
				msg = " File [" + DATFILE + "] WRITE Finished";
				imcsLog.serviceLog(msg, methodName, methodLine);
			} else {
				msg = " File [" + DATFILE + "] WRITE Failed";
				imcsLog.serviceLog(msg, methodName, methodLine);
			}
			
			copyFile(LOCALPATH + "/" + idolKeyFileName , NASPATH + "/" + idolKeyFileName); // local 에서 nas 로 복사
			
			
			
			
		} catch (Exception e) {
			result_set = 0;
		}
		
		return result_set;
	}
	
	
	public void copyFile(String localFileName, String nasFileName) throws Exception{
		try {
			FileInputStream local = new FileInputStream(localFileName);
			FileOutputStream nas = new FileOutputStream(nasFileName);

			int data = 0;
			while ((data = local.read()) != -1) {
				nas.write(data);
			}
			nas.close();
			local.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
