package kr.co.wincom.imcs.api.buyNSConts;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.nosql.NoSQLRedisDao;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.ComDataFreeVO;
import kr.co.wincom.imcs.common.vo.ComDupCHk;
import kr.co.wincom.imcs.common.vo.ComPriceVO;
import kr.co.wincom.imcs.common.vo.ComSbcVO;
import kr.co.wincom.imcs.common.vo.ContTypeVO;
import kr.co.wincom.imcs.common.vo.ComCpnVO;
import kr.co.wincom.imcs.common.vo.GenreInfoVO;
import kr.co.wincom.imcs.common.NosqlCacheType;
import kr.co.wincom.imcs.handler.ImcsException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class BuyNSContsServiceImpl implements BuyNSContsService {
	private Log imcsLogger = LogFactory.getLog("API_buyNSConts");
	
	@Autowired
	private BuyNSContsDao buyNSContsDao;
	
	@Autowired
	private NoSQLRedisDao noSQLRedisDao;
	
	@Autowired
	private CommonService commonService;

//	public void buyNSConts(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	private static int block_flag = 0;

	@Override
	public BuyNSContsResultVO buyNSConts(BuyNSContsRequestVO paramVO){
//		this.buyNSConts(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog(methodName + " service call");
		
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		BuyNSContsResultVO resultVO = new BuyNSContsResultVO();
		ComPriceVO priceVO = new ComPriceVO();
		
		String flag		= "";
		String errCode	= "";
		String errMsg	= "";
		
	    int nProdDupChk	= 0;
	    int nDupChk		= 0;
	    int nEventChk	= 0;
	    
	    Integer resultSet = 0;
	    Integer messageSet = 99;
	    
	    String msg	= "";	    
	    String szDistributor	= "";
	    block_flag = 0;
		
		int nMainCnt = 0;

		long tp1 = 0;
		long tp2 = 0;
		
		ComDataFreeVO datafreeVO = new ComDataFreeVO();
		ComSbcVO sbcVO = new ComSbcVO();
		
		// 2019.04.03 - ????????? ?????? ????????????.
		paramVO.setSzSurtaxRate(commonService.getSurtaxRate());
	    
		try{
			//??????????????? ?????? ????????? ??? ?????? ??????!
			if("A".equals(paramVO.getBuyingType()) ){
				
				if( "1".equals(paramVO.getBuyTypeFlag()) && "".equals(paramVO.getPaymentId()) ){
					msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] sts[" + ImcsConstants.LOG_MSG2 + "] msg[" + String.format("%-27s", ImcsConstants.RCV_MSG1 + "]");
					imcsLog.serviceLog(msg, methodName, methodLine);
					
					resultSet = -1;
					messageSet = 3;
				}
				
				if( "R".equals(paramVO.getBuyingGb()) ){
					msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] ?????? ?????? ?????? ??????[" + paramVO.getBuyingGb() + "]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					
					resultSet = -1;
					messageSet = 3;
				}
			}
			
			/* ????????? ????????? ????????? ?????? ?????? ??????*/
			if("Y".equals(paramVO.getPkgYn()) && !"1".equals(paramVO.getBuyTypeFlag()) ){
				
				msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] ????????? ?????? ????????? ?????? ?????? ??????! PKG_YN[" + paramVO.getPkgYn() + "] DATAFREE_FLAG[" + paramVO.getBuyTypeFlag() + "]";
				imcsLog.serviceLog(msg, methodName, methodLine);
				
				resultSet = -1;
				messageSet = 15;	/* ???????????? ?????? ?????? ???????????????.*/			
			}
			
			/* ???????????? ??? ????????? ?????? ?????? ?????? ??????*/
			if("R".equals(paramVO.getBuyingGb()) && !"1".equals(paramVO.getBuyTypeFlag())){
				
				msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] ???????????? ?????? ????????? ?????? ?????? ??????! PKG_YN[" + paramVO.getPkgYn() + "] DATAFREE_FLAG[" + paramVO.getBuyTypeFlag() + "]";
				imcsLog.serviceLog(msg, methodName, methodLine);
				
				resultSet = -1;
				messageSet = 15;	/* ???????????? ?????? ?????? ???????????????.*/
			}
			
			
			switch (paramVO.getBuyingType().toUpperCase()) {
			case "B":/*?????? ??????			*/
				paramVO.setCpUseYn(ImcsConstants.CP_USE_YN_NORMAL);
				break;
				
			case "C":/* ???????????? 		*/
				paramVO.setCpUseYn("Y");
				break;
				
			case "S":/* PG?????? 			*/
				paramVO.setCpUseYn(ImcsConstants.CP_USE_YN_PREPAY);
				// 20190425 - ????????? AS-IS
//				paramVO.setAlwnceCharge(paramVO.getBuyingPrice());
				// 20190425 - ????????? TO-BE
				paramVO.setUdrCharge(Integer.parseInt(paramVO.getBuyingPrice()) + (Integer.parseInt(paramVO.getBuyingPrice()) / paramVO.getSzSurtaxRate()));
				paramVO.setAlwnceCharge(Integer.toString(paramVO.getUdrCharge()));
				break;
			
			case "A":/*????????????			*/
				paramVO.setCpUseYn(ImcsConstants.CP_USE_YN_INAPP);
				paramVO.setBalace(paramVO.getBuyingPrice());
				break;
				
			case "W":/* PAY NOW ?????? 	*/
				paramVO.setCpUseYn(ImcsConstants.CP_USE_YN_PAYNOW);
				// 20190425 - ????????? AS-IS
//				paramVO.setAlwnceCharge(paramVO.getBuyingPrice());
				// 20190425 - ????????? TO-BE
				paramVO.setUdrCharge(Integer.parseInt(paramVO.getBuyingPrice()) + (Integer.parseInt(paramVO.getBuyingPrice()) / paramVO.getSzSurtaxRate()));
				paramVO.setAlwnceCharge(Integer.toString(paramVO.getUdrCharge()));
				break;
			
			case "T":/* CREATE CARD(????????????) ?????? 	*/
				paramVO.setCpUseYn(ImcsConstants.CP_USE_YN_CREDITCARD);
				// 20190425 - ????????? AS-IS
//				paramVO.setAlwnceCharge(paramVO.getBuyingPrice());
				// 20190425 - ????????? TO-BE
				paramVO.setUdrCharge(Integer.parseInt(paramVO.getBuyingPrice()) + (Integer.parseInt(paramVO.getBuyingPrice()) / paramVO.getSzSurtaxRate()));
				paramVO.setAlwnceCharge(Integer.toString(paramVO.getUdrCharge()));
				break;

			default:
				msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] ???????????? ?????? ?????? ???????????????.[c_buying_type=" + paramVO.getBuyingType() + "]";
				imcsLog.serviceLog(msg, methodName, methodLine);
				
				resultSet	= -1;
    			messageSet = 15; /* ???????????? ?????? ?????? ???????????????.*/	
				break;
			}
			
			switch (paramVO.getBuyTypeFlag()) {
			case "1":
			case "2":
			case "3":
				break;

			default:
				msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] ?????? ?????? DATAFREE_FLAG[" + paramVO.getBuyTypeFlag() + "]";
				imcsLog.serviceLog(msg, methodName, methodLine);
				
				resultSet	= -1;
    			messageSet = 15; /* ???????????? ?????? ?????? ???????????????.*/
				break;
			}
			
			//-----------------------
		    //?????? ????????? ?????? data free ?????? ?????? ??????
		    //-------------------------
			switch (paramVO.getCpUseYn()) {
			case ImcsConstants.CP_USE_YN_INAPP:
				paramVO.setDatafreeCpUseYn(ImcsConstants.CP_USE_YN_INAPP);
				break;
				
			case ImcsConstants.CP_USE_YN_PAYNOW:
				paramVO.setDatafreeCpUseYn(ImcsConstants.CP_USE_YN_PAYNOW);
				break;
				
			case ImcsConstants.CP_USE_YN_CREDITCARD:
				paramVO.setDatafreeCpUseYn(ImcsConstants.CP_USE_YN_CREDITCARD);
				break;

			default:
				paramVO.setDatafreeCpUseYn(ImcsConstants.CP_USE_YN_NORMAL);
				break;
			}			
			
			// ??????, ???????????? ??? ????????? ????????????
			if( resultSet == 0 ){
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
				imcsLog.timeLog("????????? ??????, ???????????? ??????", String.valueOf(tp2 - tp1), methodName, methodLine);

							
				if(resultSet == 0){
					//tp2	= System.currentTimeMillis();
					//imcsLog.timeLog("????????? ??????, ???????????? ??????", String.valueOf(tp2 - tp1), methodName, methodLine); 
					
					tp1 = System.currentTimeMillis();
					
					
					// ?????? ?????? ??????(??????/??????)
					priceVO	= this.getBillType(paramVO);
					paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
					
					if(priceVO != null) {
						paramVO.setProdType(priceVO.getProductType());			// 2019.10.29 - NPT_VO_BUY_META ?????? ??????
						paramVO.setSuggestedPrice(priceVO.getSuggestedPrice());
						paramVO.setReservedPrice(priceVO.getReservedPrice());
						szDistributor	= StringUtil.nullToSpace(priceVO.getDistributor());

						paramVO.setLicensingStart(priceVO.getLicenseStart());
						paramVO.setLicensingEnd(priceVO.getLicenseEnd());
						
						// 2019.10.29 - NPT_VO_BUY_META ?????? ?????? Set
						paramVO.setAssetName(priceVO.getAssetName());
						paramVO.setHdcontent(priceVO.getHdcontent());
						paramVO.setRatingCd(priceVO.getRatingCd());
						
						// 2019.10.29 - NPT_VO_BUY_META PPV ?????? Set
						if(!paramVO.getPkgYn().toUpperCase().equals("Y"))	// ?????? ????????? ??????
						{
							paramVO.setProductId(priceVO.getProductId());
							paramVO.setProductName(priceVO.getProductName());
							paramVO.setProductKind(priceVO.getProductKind());
							paramVO.setCpId(priceVO.getCpId());
							paramVO.setMaximumViewingLength(priceVO.getMaximumViewingLength());
							paramVO.setSeriesNo(priceVO.getSeriesNo());
						}
					}	

				    // 2019.09.04 - ???????????? ?????? ?????? ??? ?????? ??????
				    if(paramVO.getBuyingDate().substring(0, 8).compareTo(paramVO.getLicensingStart().substring(0, 8)) >= 0)
					{
						if(paramVO.getBuyingDate().substring(0, 8).compareTo(paramVO.getLicensingEnd().substring(0, 8)) <= 0)
						{
							paramVO.setLicensingValidYn("Y");
						}else
						{
							paramVO.setLicensingValidYn("N");
						}
					}
					else
					{
						paramVO.setLicensingValidYn("N");
					}
					
					/* FVOD ??? ??????, ?????? ?????????*/
					if("0".equals(paramVO.getProdType())){
						datafreeVO.setPrice("0");
						paramVO.setSuggestedPrice("0");
					}else{
						datafreeVO.setPrice(priceVO.getSuggestedPrice());
					}
					
					//??????????????? ?????? ??????
					// 2020.12.22 - ????????? ??????????????? ???????????? (?????????TV??? ????????? ??????????????? ?????? ??????)
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
					
					paramVO.setApprovalPrice(datafreeVO.getApprovalPrice());
					
					if("N".equals(priceVO.getDatafreeBillYn())){
						datafreeVO.setDatafreePrice("0");
						datafreeVO.setDatafreeApprovalPrice("0.00");
						datafreeVO.setPpvDatafreeApprovalPrice("0.00");
					}
										
					resultSet	= paramVO.getResultSet();
					
					tp2	= System.currentTimeMillis();
					imcsLog.timeLog("?????? ??????(??????/??????) ??????", String.valueOf(tp2 - tp1), methodName, methodLine); 
					
					
					if(resultSet == -1){
						messageSet = 13;
					}
					
		            if((block_flag == 1) && !paramVO.getProdType().equals("0")) {
		            	msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] PPVBLOCK ????????? ";
		    			imcsLog.serviceLog(msg, methodName, methodLine);
		            	messageSet = 19;
		            	resultSet = -1;
		            }
					if ("N".equals(paramVO.getLicensingValidYn()) && !sbcVO.getTestSbc().equals("Y")) {
		            	msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] licensing_expired ";
		    			imcsLog.serviceLog(msg, methodName, methodLine);
						resultSet = -1;
						messageSet = 98;
					}
					
					 /* ???????????? ?????? */
					if(!"Y".equals( sbcVO.getStatusFlag() )){
						resultSet = -1;
						messageSet = 11;
					}
					
					if(resultSet == 0){
						 /* ???????????? ?????? */
						if( "N".equals( sbcVO.getYnVodOpen() ) ){
							/* FVOD??? ???????????? */
							if(!"F".equals(priceVO.getBillType())){
								resultSet = -1;
								messageSet = 12;
							}
						}
					}					
				}
			}
			
			if( resultSet == 0 && "1".equals(paramVO.getProdType()) && "3".equals(paramVO.getBuyTypeFlag()) && "Y".equals(priceVO.getPossessionYn()) ){
				
				msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] ?????? ?????? ?????? ????????? ?????? ?????? ??????! POSSESSION_YN[" + priceVO.getPossessionYn() + "] DATAFREE_FLAG[" + paramVO.getBuyTypeFlag() + "]";
				imcsLog.serviceLog(msg, methodName, methodLine);
				
				resultSet	= -1;
    			messageSet = 15; /* ???????????? ?????? ?????? ???????????????.*/
				
			}
			
			//-------------------------
			//PAYMENT_ID CHECK(?????????)
			//-------------------------
			if(resultSet == 0 && "A".equals(paramVO.getBuyingType().toUpperCase()) ){
				tp1 = System.currentTimeMillis();
				
				try {
					Integer nPaymentIdChk	= 0;
					
					if("2".equals(paramVO.getBuyTypeFlag()) && !"Y".equals(priceVO.getDatafreeBillYn())){
						msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] PAYMENT ID CHECK SKIP IS_PAY_DATAFREE[" + priceVO.getDatafreeBillYn() + "] DATAFREE_BUY_FLAG [" + paramVO.getBuyTypeFlag() + "]";
						imcsLog.serviceLog(msg, methodName, methodLine);
					}else{
						
						paramVO.setIsPayDatafree(priceVO.getDatafreeBillYn());
						
						nPaymentIdChk	= buyNSContsDao.chkPaymentId(paramVO);
						paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
						
						//if(nPaymentIdChk != null && nPaymentIdChk > 0)
						if(nPaymentIdChk != null && nPaymentIdChk == 1){
							msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] chkPaymentId [PT_PAYMENT_INFO] table[" + nPaymentIdChk + "] records Success at";
						}else if(nPaymentIdChk != null && nPaymentIdChk != 1){
							msg	= " [SMARTUX.PT_PAYMENT_INFO FAILURE]";
							resultSet	= -1;
							messageSet	= 3;
						}else {							
							msg	= " [SMARTUX.PT_PAYMENT_INFO no data]";
							resultSet	= -1;
							messageSet	= 3;
						}
						imcsLog.serviceLog(msg, methodName, methodLine);						
					}
					
				} catch (Exception e) {
					resultSet	= -1;
					messageSet	= 3;
					
//					imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID010, "", null, ImcsConstants.RCV_MSG6, methodName, methodLine);
	
				}
				tp2	= System.currentTimeMillis();
				imcsLog.timeLog("???????????? PAYMENT_ID ??????", String.valueOf(tp2 - tp1), methodName, methodLine); 
			}
			
			
			// ????????? ???????????? ?????? ??????
			if(resultSet == 0 && !"F".equals(priceVO.getBillType()) && "N".equals(paramVO.getPkgYn()) ){
				tp1 = System.currentTimeMillis();
				nProdDupChk = this.getCustomerProdChk(paramVO);
				
				tp2	= System.currentTimeMillis();
				imcsLog.timeLog("???????????? ????????? ?????? ??????", String.valueOf(tp2 - tp1), methodName, methodLine); 
				
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
			
			
			
			// ???????????? ??? ???????????? ??????
			if( resultSet == 0 && "1".equals( paramVO.getProdType() )){

				if("2".equals(paramVO.getBuyTypeFlag())){
					
					msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] PPV DATAFREE BUY FLAG ??????! PKG_YN[" + paramVO.getPkgYn() + "] DATAFREE_FLAG["+ paramVO.getBuyTypeFlag() +"]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					
					resultSet	= -1;
	    			messageSet = 15; /* ???????????? ?????? ?????? ???????????????.*/
				}
				
				if("R".equals(paramVO.getBuyingGb())){
					// ??????????????? ?????? ???????????? CHECK
					if( !priceVO.getReservedPrice().equals( paramVO.getBuyingPrice() ) ){
						msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] ??????PPV - incorrect price[imcs_price=" + priceVO.getReservedPrice() + "][stb_price=" + paramVO.getBuyingPrice() + "]";
						imcsLog.serviceLog(msg, methodName, methodLine);
						
						resultSet = -1;
	                    messageSet = 45;
					}
				}else{
					if("A".equals(paramVO.getBuyingType().toUpperCase())){
						if( "1".equals(paramVO.getBuyTypeFlag()) && !paramVO.getApprovalPrice().equals( paramVO.getBuyingPrice() ) ){
							msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] ??????PPV - incorrect price[imcs_price=" + datafreeVO.getApprovalPrice() + "][stb_price=" + paramVO.getBuyingPrice() + "]";
							imcsLog.serviceLog(msg, methodName, methodLine);
							
							resultSet = -1;
	                        messageSet = 45;
						}	
						else if( "3".equals(paramVO.getBuyTypeFlag()) && "Y".equals(priceVO.getDatafreeBillYn()) && !datafreeVO.getPpvDatafreeApprovalPrice().equals( paramVO.getPpvDatafreeBuyPrice() ) ){
							msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] ??????PPV - IS PAY DATAFREE[Y] incorrect price[imcs_price=" + datafreeVO.getPpvDatafreeApprovalPrice() + "][stb_price=" + paramVO.getPpvDatafreeBuyPrice() + "]";
							imcsLog.serviceLog(msg, methodName, methodLine);
							
							resultSet = -1;
	                        messageSet = 45;
						}
						else if( "3".equals(paramVO.getBuyTypeFlag()) && "N".equals(priceVO.getDatafreeBillYn()) && !paramVO.getApprovalPrice().equals( paramVO.getBuyingPrice() ) ){
							msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] ??????PPV - IS PAY DATAFREE[N] incorrect price[imcs_price=" + priceVO.getApprovalPrice() + "][stb_price=" + paramVO.getBuyingPrice() + "]";
							imcsLog.serviceLog(msg, methodName, methodLine);
							
							resultSet = -1;
	                        messageSet = 45;
						}
						
					}else{
						if( ("1".equals(paramVO.getBuyTypeFlag()) || "3".equals(paramVO.getBuyTypeFlag())) && !paramVO.getSuggestedPrice().equals( paramVO.getBuyingPrice() ) ){
							msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] ??????PPV - incorrect price[imcs_price=" + paramVO.getSuggestedPrice() + "][stb_price=" + paramVO.getBuyingPrice() + "]";
							imcsLog.serviceLog(msg, methodName, methodLine);
							
							resultSet = -1;
	                        messageSet = 45;
						}
					}
				}
			} else if( resultSet == 0 && "0".equals( paramVO.getProdType() )){
				// FVOD??????
				
				if("R".equals(paramVO.getBuyingGb())){
					msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "]  FOD ?????? ?????? ??????! BUYING_GB[" + paramVO.getBuyingGb() + "]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					
					resultSet = -1;
                    messageSet = 15; /* ???????????? ?????? ?????? ???????????????.*/
				}
				
				if("3".equals(paramVO.getBuyTypeFlag())){
					msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "]  FOD DATAFREE BUY FLAG ??????! PKG_YN[" + paramVO.getPkgYn() + "] DATAFREE_FLAG[" + paramVO.getBuyTypeFlag() + "]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					
					resultSet = -1;
                    messageSet = 15; /* ???????????? ?????? ?????? ???????????????.*/
				}
				
				if("A".equals(paramVO.getBuyingType()) && "N".equals(priceVO.getDatafreeBillYn())){
					msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "]  FOD DATAFREE BUY  ??????! BUYING_TYPE[" + paramVO.getBuyingType() + "] DATAFREE_FLAG[" + paramVO.getBuyTypeFlag() + "]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					
					resultSet = -1;
                    messageSet = 15; /* ???????????? ?????? ?????? ???????????????.*/
				}else if("A".equals(paramVO.getBuyingType()) && "Y".equals(priceVO.getDatafreeBillYn()) && "2".equals(paramVO.getBuyTypeFlag()) && !paramVO.getDatafreeBuyPrice().equals(datafreeVO.getDatafreeApprovalPrice()) ){
					msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "]  FVOD datafree inapp  - incorrect price[imcs_price=" + datafreeVO.getDatafreeApprovalPrice() + "] [stb_price=" + paramVO.getDatafreeBuyPrice() + "]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					
					resultSet = -1;
                    messageSet = 45;
				}else if("A".equals(paramVO.getBuyingType()) && "1".equals(paramVO.getBuyTypeFlag()) ){
					msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "]  FOD Inapp BUY  ??????! BUYING_TYPE[" + paramVO.getBuyingType() + "] DATAFREE_BILL_YN[" + priceVO.getDatafreeBillYn() + "]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					
					resultSet = -1;
                    messageSet = 15; /* ???????????? ?????? ?????? ???????????????.*/
				}
				
				
				if(!"A".equals(paramVO.getBuyingType()) && ( "1".equals(paramVO.getBuyTypeFlag()) || "2".equals(paramVO.getBuyTypeFlag())) && !"0".equals(paramVO.getBuyingPrice())){
					msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] FVOD - incorrect price[imcs_price=0][stb_price=" + paramVO.getBuyingPrice() + "]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					
					resultSet = -1;
                    messageSet = 45;
				}
			}
			
			/* ????????? ?????? ?????? ??????*/
			if( resultSet == 0 && ( "2".equals(paramVO.getBuyTypeFlag()) || "3".equals(paramVO.getBuyTypeFlag()) ) ){
				
				if("N".equals(priceVO.getDatafreeBillYn())){
					
					if(!"A".equals(paramVO.getBuyingType())){
						
						if( ( "2".equals(paramVO.getBuyTypeFlag()) || "3".equals(paramVO.getBuyTypeFlag()) ) && !"0".equals(paramVO.getDatafreeBuyPrice()) ){
							
							msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] ??????????????? - incorrect price[imcs_price=0][stb_price=" + paramVO.getDatafreeBuyPrice() + "]";
							imcsLog.serviceLog(msg, methodName, methodLine);
							
							resultSet = -1;
		                    messageSet = 45;							
						}						
					}
					
				}else{
					if("A".equals(paramVO.getBuyingType()) && "2".equals(paramVO.getBuyTypeFlag())){
						if(!datafreeVO.getDatafreeApprovalPrice().equals(paramVO.getDatafreeBuyPrice())){
							msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] ??????????????? - incorrect price[imcs_price=" + paramVO.getSuggestedPrice() + "][stb_price=" + paramVO.getDatafreeBuyPrice() + "]";
							imcsLog.serviceLog(msg, methodName, methodLine);
							
							resultSet = -1;
		                    messageSet = 45;
						}
					}else if(!"A".equals(paramVO.getBuyingType())){
						if(!datafreeVO.getDatafreePrice().equals(paramVO.getDatafreeBuyPrice())){
							msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] ??????????????? - incorrect price[imcs_price=" + datafreeVO.getDatafreePrice() + "][stb_price=" + paramVO.getDatafreeBuyPrice() + "]";
							imcsLog.serviceLog(msg, methodName, methodLine);
							
							resultSet = -1;
		                    messageSet = 45;
						}
					}
				}
				
			}
			
			//?????? ????????? ?????? ???????????? ??????
			if( resultSet == 0 ){
				nDupChk = this.chkDatafreeDup(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				resultSet = paramVO.getResultSet();
				
				if(resultSet == -1){
					messageSet = 22;
				}
				
				/* ????????? ???????????? ????????? */
				if(nDupChk > 0){
					resultSet = -1;
					messageSet = 24;
				}
				
				tp2	= System.currentTimeMillis();
				imcsLog.timeLog("?????? ????????? ?????? ???????????? ??????", String.valueOf(tp2 - tp1), methodName, methodLine);
			}
			
			
			// ?????? ???????????? ??????
			if(resultSet == 0 && !"0".equals(paramVO.getProdType())){
				tp1 = System.currentTimeMillis();
				
				List<HashMap<String, Object>> lBuyDupChk = new ArrayList<HashMap<String, Object>>();
				HashMap<String, Object> mBuyDupChk = new HashMap<String, Object>();
				
				// ???????????? ??????
				try {
					if("R".equals(paramVO.getBuyingGb())) {	// ????????????
						
						if("1".equals(paramVO.getProdType())){
							lBuyDupChk	= buyNSContsDao.getBuyDupChkR(paramVO);
							paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
						}
					} else {									// ???????????? ???
						if( "0".equals(paramVO.getProdType()) )
							lBuyDupChk	=	buyNSContsDao.getBuyDupChkType0(paramVO);		// FVOD ???????????? ?????? 
						else if( "1".equals(paramVO.getProdType()) )
							lBuyDupChk	=	buyNSContsDao.getBuyDupChkType1(paramVO);
						else if( "2".equals(paramVO.getProdType()) )
							lBuyDupChk	=	buyNSContsDao.getBuyDupChkType2(paramVO);
						
						paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
					}
				} catch(Exception e) {
					paramVO.setResultCode("40000000");
					resultSet	= -1;
					messageSet	= 22;
				}
				
				
				// ????????? ???????????? ?????????
				if(lBuyDupChk!= null && lBuyDupChk.size() > 0){
					mBuyDupChk	= lBuyDupChk.get(0);
					nDupChk	= (Integer) mBuyDupChk.get("DUP_CHK");
					
					if( nDupChk > 0 ) {
						resultSet	= -1;
						messageSet	= 24;
						
						paramVO.setBuyDate((String) mBuyDupChk.get("BUY_DATE"));
					}
				}
				
				tp2	= System.currentTimeMillis();
				imcsLog.timeLog("?????? ???????????? ??????", String.valueOf(tp2 - tp1), methodName, methodLine); 
			}
			
			
			if(resultSet == 0){
				List<ContTypeVO> lContsList = new ArrayList<ContTypeVO>();
				ContTypeVO contTypeVO = new ContTypeVO();
				nMainCnt = 0;
				
				// ????????? ????????? ????????? ??????
				if("Y".equals(paramVO.getPkgYn())){
					tp1	= System.currentTimeMillis();
					paramVO.setContsGenre("?????????");
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
						
						if(contTypeVO.getCpId().equals("1414"))	// ????????? CP
						{
							ppsCpId[3] = contTypeVO.getCpId();
						}
						else if(contTypeVO.getCpId().equals("1302"))	// ??????????????? CP
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
						
						// ????????? : ?????? Contents??? Fetch?????? insert
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
					
					// 2019.10.29 - VOD ?????? ???????????? ?????? : PPS CP_ID?????? ????????? ?????? ?????? ??????						
					// Case 1. 1414(?????????) / 1302(???????????????) CP ?????? CP_ID??? ????????? ????????? ??? ?????? CP_ID??? NPT_VO_BUY_META ???????????? ??????
				    // Case 2. 1414(?????????) / 1302(???????????????) CP ?????? CP_ID??? ?????? ?????? ????????? ??? "MULTI"?????? ????????? NPT_VO_BUY_META ???????????? ??????
				    // Case 3. 1414(?????????) / 1302(???????????????) CP ?????? CP_ID??? ?????? ??? 1302(???????????????) CP_ID??? ??????????????? NPT_VO_BUY_META ???????????? ??????
				    // Case 4. 1414(?????????) / 1302(???????????????) CP ?????? CP_ID??? ?????? 1302(???????????????) CP_ID??? ????????? 1414(?????????) CP_ID??? ??????????????? NPT_VO_BUY_META ???????????? ??????
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
					imcsLog.timeLog("????????? ????????? ????????? ??????", String.valueOf(tp2 - tp1), methodName, methodLine); 
					
				} else if("N".equals(paramVO.getPkgYn())) {		// ????????????
					tp1	= System.currentTimeMillis();
					
					// ????????? ????????????
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
					imcsLog.timeLog("?????? ????????? ?????? ??????", String.valueOf(tp2 - tp1), methodName, methodLine); 
					
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
					imcsLog.timeLog("?????? ????????? ????????? ??????", String.valueOf(tp1 - tp2), methodName, methodLine); 
				}
								
			} 
			
			if(paramVO.getContsGenre() == null || "".equals(paramVO.getContsGenre())){
				paramVO.setContsGenre("??????");
			}
			
			
			// ???????????? ??????
			if(resultSet == 0){
				tp1	= System.currentTimeMillis();
				try {
					if("N".equals(paramVO.getPkgYn().toUpperCase())) {
						Integer result = 0;
						
						paramVO.setApprovalPrice(datafreeVO.getApprovalPrice());
						
						//???????????? ?????? (??????????????? ?????? ???????????? ?????? ????????? ?????? ?????????)
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
							
							resultVO.setProductId(prodInfoVO.getProductId());		// ??????????????? 
							resultVO.setProductName(prodInfoVO.getProductName());	// ???????????????
							resultVO.setProductPrice(prodInfoVO.getPrice());		// ???????????????
							
							// 2019.10.30 - VOD ?????? ???????????? ?????? : PPS ?????? ?????? NPT_VO_BUY_META??? ???????????? paramVO??? Set
							paramVO.setProductKind(prodInfoVO.getProductKind());
							paramVO.setProductId(prodInfoVO.getProductId());
							paramVO.setProductName(prodInfoVO.getProductName());
							paramVO.setMaximumViewingLength(prodInfoVO.getExpiredDate());
						}
						
						
						/** PPS ?????? ??????*/
						if(!paramVO.getBuyingPrice().equals(paramVO.getProductPrice1())){
							
							msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] PPS ?????? - incorrect price[imcs_price=" + paramVO.getProductPrice1() + "][stb_price=" + paramVO.getBuyingPrice() + "]";
							imcsLog.serviceLog(msg, methodName, methodLine);
							
							resultSet	= -1;
		                    messageSet	= 40;
						}else{
							
							//???????????? ?????? (??????????????? ?????? ???????????? ?????? ????????? ?????? ?????????)
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
				} catch(Exception e) {
					resultSet	= -1;
                    messageSet	= 40;
				}
				tp2	= System.currentTimeMillis();
				imcsLog.timeLog("???????????? ?????? ??????", String.valueOf(tp2 - tp1), methodName, methodLine); 
			}
			
			
			
			ComCpnVO cpnInfoVO = new ComCpnVO();

			if(resultSet == 0){
				// ?????? ?????? ??????
				String szGenreInfo	= "";
				tp1	= System.currentTimeMillis();
				try {
					szGenreInfo =	this.getGenreInfo(paramVO);
					paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
					paramVO.setGenreInfo(szGenreInfo);
				} catch(Exception e) {
					tp2	= System.currentTimeMillis();
					imcsLog.timeLog("?????? ?????? ?????? ??????", String.valueOf(tp2 - tp1), methodName, methodLine);
				}
				resultSet = paramVO.getResultSet();
				msg	 = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] select genre[" + szGenreInfo + "]";
				imcsLog.serviceLog(msg, methodName, methodLine);
				

//                //2019.03.05 - ????????? ????????? ????????? ?????? KBS, SBS, MBC ????????? ??????????????? ????????? ??????
//				if (paramVO.getBuyingDate().compareTo("20190307") >= 0 && (paramVO.getGenreInfo().indexOf("SBS") > 0 && paramVO.getGenreInfo().indexOf("SBS") + 3 == paramVO.getGenreInfo().length()) )
//				{
//					resultSet = -1;
//					messageSet = 13;
//				}
//				else if (paramVO.getBuyingDate().compareTo("20190311") >= 0 && (paramVO.getGenreInfo().indexOf("KBS") > 0 && paramVO.getGenreInfo().indexOf("KBS") + 3 == paramVO.getGenreInfo().length()) )
//				{
//					resultSet = -1;
//					messageSet = 13;
//				}
//				else if (paramVO.getBuyingDate().compareTo("20190315") >= 0 && (paramVO.getGenreInfo().indexOf("MBC") > 0 && paramVO.getGenreInfo().indexOf("MBC") + 3 == paramVO.getGenreInfo().length()) )
//				{
//					resultSet = -1;
//					messageSet = 13;
//				}
				
				//????????? ?????? ?????? ?????? ?????? ?????? ??????
				if(!"A".equals(paramVO.getCpUseYn())){
	                // ????????? ?????? ?????? ??????
					try {
						// ?????? ?????? ??????
						cpnInfoVO	= this.getCpnInfo(paramVO);
						paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
						
						tp2	= System.currentTimeMillis();
						imcsLog.timeLog("?????????/?????? ?????? ?????? ??????", String.valueOf(tp2 - tp1), methodName, methodLine);
					} catch(Exception e) {
						imcsLog.errorLog(e.toString());
						
						resultSet = -1;
						messageSet = 49;
						
						tp2	= System.currentTimeMillis();
						imcsLog.timeLog("?????????/?????? ?????? ?????? ??????", String.valueOf(tp2 - tp1), methodName, methodLine);
					}
				}
			}
			
			String pvsCtnNo = "";
			String pvsAtrtChnlDvCd = "";
			
			if(sbcVO != null ){
				pvsCtnNo = sbcVO.getPvsCtnNo();
				pvsAtrtChnlDvCd = sbcVO.getPvsAtrtChnlDvCd();
			}
			
			msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] D/F Buy Check : result_set["+resultSet+"] c_datafree_buy_flag["+paramVO.getBuyTypeFlag()+"]"
					+ "c_is_pay_datafree["+priceVO.getDatafreeBillYn()+"] c_is_LGU["+paramVO.getIsLGU()+","+pvsCtnNo+","+pvsAtrtChnlDvCd+"]";
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			if( resultSet == 0 && 
					( ( "2".equals(paramVO.getBuyTypeFlag()) || "3".equals(paramVO.getBuyTypeFlag()) 
							|| ( "N".equals(paramVO.getPkgYn()) && "Y".equals(paramVO.getIsLGU()) && "N".equals(priceVO.getDatafreeBillYn()) ) ) ) )
			{
				
				if( "N".equals(paramVO.getPkgYn()) && "Y".equals(paramVO.getIsLGU()) && "N".equals(priceVO.getDatafreeBillYn()) ){
					
					msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] sts[DF_WARN] ?????? ?????????Free DEFAULT ??????";
					imcsLog.serviceLog(msg, methodName, methodLine);
					
				}
				paramVO.setSuggestedDatafreePrice(datafreeVO.getDatafreePrice());
				paramVO.setSuggestedDatafreeApprovalPrice(datafreeVO.getDatafreeApprovalPrice());
				paramVO.setIsPayDatafree(priceVO.getDatafreeBillYn());
				
				Integer result = 0;
				
				result = this.buyDatafree(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
				if(result > 0)		resultSet = 0;
		    	else	    		resultSet = -1;
				
				if(resultSet == -1){
					msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] Data Free ?????? ??????";
					imcsLog.serviceLog(msg, methodName, methodLine);
					
					resultSet = -1;
					messageSet = 40;
				}				
			}
			
			// 2019.10.30 - VOD ?????? ???????????? ?????? : NPT_VO_BUY_META ????????? INSERT
			// ???????????? ?????? ??????????????? ????????? ?????? ?????????, PPV+DataFree ?????? ??? ???????????? NPT_VO_BUY_META???????????? ????????? ???????????? ????????????.
			if(resultSet == 0)
			{
				if(this.insBuyMeta(paramVO) > 0) {
		    		resultSet	= 0;
		    	} else {
		    		resultSet	= -1;
	                messageSet	= 40;
		    	}
			}
						
			/* ???????????? ???????????? ???????????? */
		    if(resultSet == 0){
		    	tp1	= System.currentTimeMillis();
		    	
		    	resultVO.setFlag("0");
		    	resultVO.setErrMsg("insert success");
		    	resultVO.setCpnInfoVO(cpnInfoVO);
		    	resultVO.setBuyingDate(paramVO.getBuyingDate());
		    	
		    	tp2	= System.currentTimeMillis();
		    	imcsLog.timeLog("EXEC SQL COMMIT", String.valueOf(tp2 - tp1), methodName, methodLine); 
		    } else {
		    	paramVO.setMessageSet(messageSet);
		    	resultVO.setBuyingDate(paramVO.getBuyingDate());
		    	
		    	throw new ImcsException();
		    }
		    
		} catch(ImcsException ie) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			tp1	= System.currentTimeMillis();
			
			HashMap<String, String> mResult = new HashMap<String, String>();
			mResult = commonService.getErrorMsg(messageSet);
			
			flag	= "1";
			resultVO.setBuyingDate("");
			
			if(mResult != null) {
				errCode	= mResult.get("ERR_CODE");
				errMsg	= mResult.get("ERR_MSG");
			} else {
				errCode	= "99";
				errMsg	= "incorrect failed !!!";
			}
			
			if(messageSet == 24) {
				flag	= "2";
				errCode	= "";
				resultVO.setBuyingDate(paramVO.getBuyDate());
				resultVO.setResultCode("20000090");
			}
			
			resultVO.setFlag(flag);
			resultVO.setErrMsg(errMsg);
			
			switch (messageSet) {
			case 3:
			case 15:
			case 20:
			case 21:
			case 24:
				break;

			default:
				resultVO.setErrCode(errCode);
				break;
			}			
			
			
			msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] msg[" + errMsg + "]";
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("EXEC SQL ROLLBACK", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			imcsLog.errorLog(methodName + "-E",ie.getClass().getName()+":"+ie.getMessage());
			throw new ImcsException(resultVO.getFlag(), resultVO.getErrMsg(), resultVO.getErrCode(), resultVO);
		} catch(Exception e) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			
			resultVO.setFlag("1");
			resultVO.setErrCode("99");
			resultVO.setErrMsg("incorrect failed !!!");
			
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());

			throw new ImcsException(resultVO.getFlag(), resultVO.getErrMsg(), resultVO.getErrCode());
		} finally{
			resultVO.setResultCode(paramVO.getResultCode());
			
			msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] return[" + resultVO.getFlag() + "|" + resultVO.getErrMsg() + "|" + resultVO.getBuyingDate() + "|" + resultVO.getErrCode() + "|]";
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
			
//			msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
//					+ " [DR:" + nDRedis + "|DH:" + nDHbase + "|R:" + nRedis + "|H:" + nHbase + "]"; 
						
//			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultVO;
	}


    
    
    /**
     * ????????? ??????, ???????????? ??????
     * @param vo
     * @return
     */
    public ComSbcVO getSbcInfo(BuyNSContsRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();    	
    	
    	String sqlId = "nsvod010_001_20171214_001";
		List<ComSbcVO> list   = new ArrayList<ComSbcVO>();
		ComSbcVO resultVO = null;
		int SbcInfoCnt = 0;
		
		try {
			
			try{
				list = buyNSContsDao.getSbcInfo(paramVO);
			} catch(DataAccessException e) {
				//DB?????? Exception?????? ??? getLastException??? ???????????? SQLException?????? ??????
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
			//throw new ImcsException();
		}
		
    	return resultVO;
    }
    
    
    
    
    /**
     * ???????????? ?????? (??????/??????)
     * @param	BuyNSContsRequestVO
     * @return
     */
    public ComPriceVO getBillType(BuyNSContsRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();    	
    	
    	String sqlId = "nsvod010_002_20171214_001";		
		List<ComPriceVO> list   = new ArrayList<ComPriceVO>();
		ComPriceVO resultVO = null;
		
		try {
			
			
			try{
				list = buyNSContsDao.getBillType(paramVO);
			}catch(DataAccessException e){
				//DB?????? Exception?????? ??? getLastException??? ???????????? SQLException?????? ??????
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list == null || list.isEmpty()){
				paramVO.setResultSet(-1);
				
				String szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] no data found album_id[" + paramVO.getAlbumId() + "] [" + paramVO.getBuyTypeFlag() +"]";
				IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
				imcsLog.serviceLog(szMsg, methodName, methodLine);
				
			} else {
				resultVO	= list.get(0);
			}
			
			
		} catch (Exception e) {
			paramVO.setResultSet(-1);
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID010, sqlId, null, "bill_type:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
    	return resultVO;
    }
    
    
    
    /**
     * ????????? ???????????? ?????? ??????
     * @param vo
     * @return
     * @throws Exception
     */
    public int getCustomerProdChk(BuyNSContsRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId = "nsvod010_003_20171214_001";
    	
		int querySize = 0;
		List<Integer> list   = new ArrayList<Integer>();
		Integer resultVO = 0;
		
		try {
			
			try{
				list = buyNSContsDao.getCustomerProdChk(paramVO);
			}catch(DataAccessException e){
				//DB?????? Exception?????? ??? getLastException??? ???????????? SQLException?????? ??????
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list == null || list.isEmpty()){
				querySize	= 0;
				resultVO	= 0;
			} else {
				querySize	= list.size();
				resultVO	= (Integer)list.get(0);
			}
			
			
			try{
//				imcsLog.dbLog(ImcsConstants.NSAPI_PRO_ID010, sqlId, null, querySize, methodName, methodLine);
			}catch(Exception e){}
		} catch (Exception e) {
			paramVO.setResultSet(-1);
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			throw new ImcsException();
		}
		
    	return resultVO;    	
    }
    
    
    
    
    
    
    /**
     * ????????? ????????? ????????? ??????
     * @param vo
     * @return
     */
    public List<ContTypeVO> getPkgContent(BuyNSContsRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();    	
    	
    	String sqlId = "nsvod010_009_20171214_001";		
		List<ContTypeVO> list   = new ArrayList<ContTypeVO>();
		
		try {

			try{
				list = buyNSContsDao.getPkgContent(paramVO);
			}catch(DataAccessException e){
				//DB?????? Exception?????? ??? getLastException??? ???????????? SQLException?????? ??????
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
     * ????????? ????????? ???????????? ??????
     * @param paramVO
     * @return
     * @throws Exception
     */
    public Integer insBuyConts3(BuyNSContsRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId = "nsvod010_i01_20171214_001";
    	String szMsg = "";
    	Integer querySize = 0;
		    	
    	try {
    		
			try{
				querySize = buyNSContsDao.insBuyConts3(paramVO);
			}catch(DataAccessException e){
				//DB?????? Exception?????? ??? getLastException??? ???????????? SQLException?????? ??????
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			
		} catch (Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] insert [PT_VO_BUY_DETAIL_NSC] table Failed at";
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID010, "", null, "fail info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			
//			paramVO.setSqlCode(cache.getLastException().getErrorCode());
		}
    	
		return querySize;
	}
    
    
    
    
    
    /**
     * ?????? ?????? ?????? ??????
     * @param paramVO
     * @return
     */
    public ContTypeVO getGenreType(BuyNSContsRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();    	
    	
    	String sqlId = "nsvod010_010_20171214_001";
    	
		int querySize = 0;
		List<ContTypeVO> list   = new ArrayList<ContTypeVO>();
		ContTypeVO resultVO	= null;
		
		try {
			
			try{
				list = buyNSContsDao.getGenreType(paramVO);
			}catch(DataAccessException e){
				//DB?????? Exception?????? ??? getLastException??? ???????????? SQLException?????? ??????
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list == null || list.isEmpty()) {
				querySize	= 0;
			} else {
				querySize	= list.size();
				resultVO	= list.get(0);
			}

			
			try {
//				imcsLog.dbLog(ImcsConstants.NSAPI_PRO_ID010, sqlId, null, querySize, methodName, methodLine);
			} catch (Exception e) {}
						
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			throw new ImcsException();
		}
		
    	return resultVO;
    }
    
    
    
    /**
     * ?????? ????????? ??????
     * @param paramVO
     * @return
     * @throws Exception
     */
    public Integer insBuyConts4(BuyNSContsRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
    	
    	String sqlId = "";
    	String szMsg = "";
    	
    	if( "0".equals(paramVO.getProdType()) )  	sqlId =  "nsvod010_i02_20171214_001";
    	else							    		sqlId =  "nsvod010_i03_20171214_001";
		
    	Integer querySize = 0;
		    	
    	try {
    		
			try{
				querySize = buyNSContsDao.insBuyConts4(paramVO);
			}catch(DataAccessException e){
				//DB?????? Exception?????? ??? getLastException??? ???????????? SQLException?????? ??????
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
						
			
			try {
//				imcsLog.dbLog(ImcsConstants.NSAPI_PRO_ID010, sqlId, null, querySize, methodName, methodLine);
				
				if( "0".equals(paramVO.getProdType()) ){
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] insert [PT_VO_BUY_DETAIL_FVOD_NSC] table[" + querySize + "] records Success at";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
				}else{
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] insert [PT_VO_BUY_DETAIL_NSC] table[" + querySize + "] records Success at";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
				}	
			} catch (Exception e) {}
			
		} catch (Exception e) {
			
			if( "0".equals(paramVO.getProdType()) ){
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] insert [PT_VO_BUY_DETAIL_FVOD_NSC] table Failed at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			}else{
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] insert [PT_VO_BUY_DETAIL_NSC] table Failed at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			}			
			
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID010, "", null, "fail info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
//			paramVO.setSqlCode(cache.getLastException().getErrorCode());
		}
		
		return querySize;
	}
    
  
    
    
    /**
     * ???????????? ??????    
     * @param vo
     * @return
     * @throws Exception
     */
    public Integer insBuyConts1(BuyNSContsRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
    	
    	String sqlId =  "";
    	
    	if( "0".equals(paramVO.getProdType()) ){
    		sqlId =  "nsvod010_i04_20171214_001";
    	}else{
    		if("R".equals(paramVO.getBuyingGb())){
    			sqlId =  "nsvod010_j05_20171214_001";
    		}else{
    			sqlId =  "nsvod010_i05_20171214_001";
    		}
    	}
		
    	Integer querySize = 0;
		String szMsg = "";
		    	
    	try {
			
    		
			try{
				querySize = buyNSContsDao.insBuyConts1(paramVO);
			}catch(DataAccessException e){
				//DB?????? Exception?????? ??? getLastException??? ???????????? SQLException?????? ??????
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			try {
//				imcsLog.dbLog(ImcsConstants.NSAPI_PRO_ID010, sqlId, null, querySize, methodName, methodLine);
				
				if( "0".equals(paramVO.getProdType()) ){
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] insert [PT_VO_BUY_FVOD_NSC] table[" + querySize + "] records Success at";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
				}else{
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] insert [PT_VO_BUY_NSC] table[" + querySize + "] records Success at";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
				}	
			} catch (Exception e) {}
			
		} catch (Exception e) {
			
			if( "0".equals(paramVO.getProdType()) ){
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] insert [PT_VO_BUY_FVOD_NSC] table Failed at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			}else{
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] insert [PT_VO_BUY_NSC] table Failed at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			}			
			
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID010, "", null, "fail info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
		return querySize;
	}
    
    
    
    
    /**
     * ????????? ???????????? ??????
     * @param vo
     * @return
     */
    public ContTypeVO getProduct(BuyNSContsRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();    	
    	
    	String sqlId = "";
    	String szMsg = "";
    	
    	if("R".equals(paramVO.getBuyingGb()))		sqlId =  "nsvod010_021_20171214_001";
    	else						    			sqlId =  "nsvod010_011_20171214_001";
		
		int querySize = 0;		
		List<ContTypeVO> list   = new ArrayList<ContTypeVO>();
		ContTypeVO resultVO = null;
		
		try {
			
			try{
				list = buyNSContsDao.getProduct(paramVO);
			}catch(DataAccessException e){
				//DB?????? Exception?????? ??? getLastException??? ???????????? SQLException?????? ??????
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
//				imcsLog.dbLog(ImcsConstants.NSAPI_PRO_ID010, sqlId, null, querySize, methodName, methodLine);
			} catch (Exception e) {}
									
		} catch (Exception e) {
			//szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] sts [" + cache.getLastException().getErrorCode() + "]"
			//		+ " msg[????????? ?????? ?????? ??????]";	
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.serviceLog(szMsg, methodName, methodLine);
		}
		
    	return resultVO;
    }
    
    
    
    
    /**
     * ???????????? ??????
     * @param paramVO
     * @return
     * @throws Exception
     */
    public Integer insBuyConts2(BuyNSContsRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
    	    	
    	String sqlId = "";
    	String szMsg = "";
    	
    	if("R".equals(paramVO.getBuyingGb()))  	sqlId =  "nsvod010_j06_20171214_001";
    	else					    				sqlId =  "nsvod010_i06_20171214_001";
    	    			
    	Integer querySize = 0;
    	try {
    		
			try{
				querySize = buyNSContsDao.insBuyConts2(paramVO);
			}catch(DataAccessException e){
				//DB?????? Exception?????? ??? getLastException??? ???????????? SQLException?????? ??????
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
					
			
			try {
//				imcsLog.dbLog(ImcsConstants.NSAPI_PRO_ID010, sqlId, null, querySize, methodName, methodLine);
				
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] insert [PT_VO_BUY_NSC] table[" + querySize + "] records Success at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
				
			} catch (Exception e) {}
			
		} catch (Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] insert [PT_VO_BUY_NSC] table Failed at";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
		
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID010, "", null, "fail info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
		return querySize;
	}
	
    
    
	/**
	 * ?????? ?????? ??????
	 * @param paramVO
	 * @return
	 */
	public String getGenreInfo(BuyNSContsRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		
		String sqlId		= "nsvod010_p01_20171214_001";
		String szGenreInfo	= "";

		List<GenreInfoVO> list	= new ArrayList<GenreInfoVO>();
		GenreInfoVO genreVO		= null;
		
		try {
			
			try {
				list = buyNSContsDao.getGenreInfo(paramVO);
			} catch (DataAccessException e) {
				// DB?????? Exception?????? ??? getLastException??? ???????????? SQLException?????? ??????
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
	 * ?????? ?????? ?????? (SMARTUX ?????? ??????)
	 * @param paramVO
	 * @return
	 */
	public ComCpnVO getCpnInfo(BuyNSContsRequestVO paramVO) throws Exception {
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName	= oStackTrace.getMethodName();
		String methodLine	= String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		long tp1, tp2	= 0;
		
		ComCpnVO rtnCpnInfoVO = new ComCpnVO();
		ComCpnVO cpnInfoVO = new ComCpnVO();
		
		String szMsg = " svc[" + ImcsConstants.NSAPI_PRO_ID010 + "] START smartux info : p_idx_sa["+paramVO.getpIdxSa()+"] album_id["+paramVO.getAlbumId()+"] product["+paramVO.getProdType()+"] "
    			+ "price["+paramVO.getSuggestedPrice()+"] genre["+paramVO.getGenreInfo()+"] pkg["+paramVO.getPkgYn()+"]";
		imcsLog.serviceLog(szMsg, methodName, methodLine);
		
		// ?????????????????? ?????? ??????
		tp1	= System.currentTimeMillis();

		try{
			cpnInfoVO	= buyNSContsDao.getCpnPossibleList(paramVO);
			
			if(cpnInfoVO != null && cpnInfoVO.getCpnInfo() != null && !"".equals(cpnInfoVO.getCpnInfo())) {
				//2018.12.22 - VR??? : VR????????? ?????? ?????? ?????? ?????????/?????? ????????? ????????? ?????? ?????????.
				if(!(paramVO.getAppType().equals("") || paramVO.getAppType() == null) && !paramVO.getAppType().substring(0, 1).equals("E")) {
					rtnCpnInfoVO.setCpnInfo("CPN01" + ImcsConstants.COLSEP + cpnInfoVO.getCpnInfo() + ImcsConstants.COLSEP);
				}
				
				// ?????? ?????? ?????? ?????? ??? ?????? ?????? ??????
				this.insCpnInfo(paramVO, cpnInfoVO.getCpnInsInfo());
			} else {
				cpnInfoVO = new ComCpnVO();
			}

			szMsg	= " SELECT smartux.F_GET_CPN_COND_POSSIBLE_LIST =[" + StringUtil.nullToSpace(cpnInfoVO.getCpnInfo()) + "]";
		    imcsLog.serviceLog(szMsg, methodName, methodLine);
		    
		} catch(Exception e) {
//				imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID010, "", null, "coupon(mims)_info:" + ImcsConstants.RCV_MSG2, methodName, methodLine);
		}
		
		tp2	= System.currentTimeMillis();
		imcsLog.timeLog("??????????????????(F_GET_CPN_COND_POSSIBLE_LIST)", String.valueOf(tp2 - tp1), methodName, methodLine);
		
		
		// ????????? ?????? ??????
		try{
			cpnInfoVO	= buyNSContsDao.getStmPossibleList(paramVO);
			
			if(cpnInfoVO != null && cpnInfoVO.getStmInfo() != null && !"".equals(cpnInfoVO.getStmInfo())) {
				//2018.12.22 - VR??? : VR????????? ?????? ?????? ?????? ?????????/?????? ????????? ????????? ?????? ?????????.
				if(!(paramVO.getAppType().equals("") || paramVO.getAppType() == null) && !paramVO.getAppType().substring(0, 1).equals("E")) {
					rtnCpnInfoVO.setStmInfo("STP00" + ImcsConstants.COLSEP + cpnInfoVO.getStmInfo() + ImcsConstants.COLSEP);
				}
			    
				// ?????? ?????? ?????? ?????? ??? ?????? ?????? ??????
				this.insStmInfo(paramVO, cpnInfoVO.getStmInsInfo());
			} else {
				cpnInfoVO = new ComCpnVO();
			}
			
			szMsg	= " SELECT smartux.F_GET_STM_COND_POSSIBLE =[" + StringUtil.nullToSpace(cpnInfoVO.getStmInfo()) + "]";
		    imcsLog.serviceLog(szMsg, methodName, methodLine);
		    
		} catch(Exception e) {
//				imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID010, "", null, "coupon(mims)_info:" + ImcsConstants.RCV_MSG2, methodName, methodLine);
		}
		
		tp1	= System.currentTimeMillis();
		imcsLog.timeLog("?????????????????????(F_GET_STM_COND_POSSIBLE)", String.valueOf(tp1 - tp2), methodName, methodLine);
		
		
		//2018.12.22 - VR??? : VR????????? ?????? ?????? ?????? ?????????/?????? ????????? ????????? ?????? ?????????.
		if(!(paramVO.getAppType().equals("") || paramVO.getAppType() == null) && !paramVO.getAppType().substring(0, 1).equals("E")) {
			// ?????? ?????? ?????? ?????? ??????
			try{
				cpnInfoVO	= buyNSContsDao.getUseCpnPossibleList(paramVO);
							
				if(cpnInfoVO != null && cpnInfoVO.getUseCpnInfo() != null && !"".equals(cpnInfoVO.getUseCpnInfo())) {
					rtnCpnInfoVO.setUseCpnInfo("CPN02" + ImcsConstants.COLSEP + cpnInfoVO.getUseCpnInfo() + ImcsConstants.COLSEP);
				} else {
					cpnInfoVO = new ComCpnVO();
				}
				
				szMsg	= " SELECT smartux.F_GET_CPN_USE_POSSIBLE_LIST =[" + StringUtil.nullToSpace(cpnInfoVO.getUseCpnInfo()) + "]";
			    imcsLog.serviceLog(szMsg, methodName, methodLine);
			    
			    
			} catch(Exception e) {
	//				imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID010, "", null, "coupon(mims)_info:" + ImcsConstants.RCV_MSG2, methodName, methodLine);
			}
			
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("?????????????????????(F_GET_CPN_USE_POSSIBLE_LIST)", String.valueOf(tp2 - tp1), methodName, methodLine);
		}
		
		return rtnCpnInfoVO;
	}

	
	
	
	
	
	
	/**
	 * ????????? ?????? ??? ????????? ????????? INSERT
	 * @param paramVO
	 * @return
	 */
	public int insStmInfo(BuyNSContsRequestVO paramVO, String szData) throws Exception {
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
			paramVO.setStrmpId(szCpnId);
			
			// ????????? ?????? ??????
			try {
				nResult = buyNSContsDao.insStmInfo(paramVO);
				
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] insert [PT_CPM_STAMP_BOX_ACTION] [" + nResult + "] records Success at ";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			} catch(Exception e) { 	nResult	= -1;	}
			
			if(nResult < 1) {
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] insert [PT_CPM_STAMP_BOX_ACTION] COUPON_BOX_ACTION table Failed at ";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
				break;
			}
			
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] INSERT COUPON_BOX_ACTION table records Success : STAMP_ID[" + paramVO.getStrmpId() + "] ";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
		}
		
		return nResult;
	}
	



	/**
	 * ???????????? ?????? ??? ?????? ????????? INSERT
	 * @param paramVO
	 * @return
	 */
	public int insCpnInfo(BuyNSContsRequestVO paramVO, String szData) throws Exception {
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
			
			// ?????? ???????????? ??????
			try {
				nResult = buyNSContsDao.insCpnInfo(paramVO);
				
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] insert [SEQ_PT_CPM_COUPON_BOX_ACTION] [" + nResult + "] records Success at ";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			} catch(Exception e) { 	nResult	= -1;	}
			
			if(nResult < 1) {
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] insert [SEQ_PT_CPM_COUPON_BOX_ACTION] COUPON_BOX_ACTION table Failed at ";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
				break;
			}
			
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] INSERT PT_CPM_COUPON_BOX_ACTION table records Success : CPEVT_ID[" + paramVO.getCpevtId() + "] ";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
		}
		
		return nResult;
	}

	/**
     * ??????????????? ?????? ??????
     * @param	ComDataFreeVO, ChkBuyNSPGRequestVO
     * @result	ComDataFreeVO
    **/
    public ComDataFreeVO getDatafreeInfo(ComDataFreeVO tempVO, BuyNSContsRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
    	
    	String sqlId = "nsvod010_005_20171214_001";
    	String szMsg = "";
		
//		NosqlResultCache cache = new NosqlResultCache();
//		List<Object> binds = new ArrayList<Object>();
//		RowKeyList rowKeys = new RowKeyList();
//		VersionUpdateCheckKey checkKey = new VersionUpdateCheckKey();
		
		List<ComDataFreeVO> list   = new ArrayList<ComDataFreeVO>();
		ComDataFreeVO resultVO = null;
		
		try {
//			rowKeys.setSqlId(sqlId);
//			rowKeys.addRowKeys(tempVO.getPrice());
//			checkKey.addVersionTuple("PT_LA_APPROVAL_INFO");
//			binds.add(tempVO);
//			
//			list = cache.getCachedResult(new CacheableExecutor<ComDataFreeVO>() {
//				@Override
//				public List<ComDataFreeVO> execute(List<Object> param) throws SQLException {
//					try{
//						ComDataFreeVO requestVO = (ComDataFreeVO)param.get(0);
//						List<ComDataFreeVO> rtnList  = buyNSContsDao.getDatafreeInfo(requestVO);
//							
//						return rtnList;
//					}catch(DataAccessException e){
//						//DB?????? Exception?????? ??? getLastException??? ???????????? SQLException?????? ??????
//						throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
//					}
//				}
//				
//				@Override
//				public Class<ComDataFreeVO> getReturnType() {
//					return ComDataFreeVO.class;
//				}
//			}, binds, rowKeys, checkKey);
			
			try{
				list  = buyNSContsDao.getDatafreeInfo(tempVO);
			}catch(DataAccessException e){
				//DB?????? Exception?????? ??? getLastException??? ???????????? SQLException?????? ??????
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
				resultVO.setApprovalPrice("0");
				resultVO.setDatafreeApprovalPrice("0.00");
				resultVO.setPpvDatafreeApprovalPrice("0.00");
				
			}
			
		} catch (Exception e) {
//			 imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID010, sqlId, null, "approval_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			 
			 paramVO.setResultCode("41000000");
		}
    	return resultVO;
    }
	
    /**
     * ??????????????? ???????????? ?????? ??????
     * @param 	BuyContsCpRequestVO
     * @return	Integer
     */
    public Integer chkDatafreeDup(BuyNSContsRequestVO paramVO) throws Exception{
		List<ComDupCHk> lstDupChk	= new ArrayList<ComDupCHk>();
		ComDupCHk dupChkVO = new ComDupCHk();
		
		Integer nDupChk = 0;
		
    	try {
    		
    		lstDupChk = buyNSContsDao.chkDatafreeDup(paramVO);
    		
    		if(lstDupChk != null && !lstDupChk.isEmpty()){
    			dupChkVO	= lstDupChk.get(0);
    			
    			paramVO.setBuyDate(dupChkVO.getBuyDate());
    			nDupChk = Integer.parseInt(StringUtil.nullToZero(dupChkVO.getDataChk()));
    		} else 
    			nDupChk	= 0;
    		
		} catch (Exception e) {			
			paramVO.setResultSet(-1);
			
			 paramVO.setResultCode("40000000");
			
		}
    	
    	return nDupChk;
    }
  
    public Integer buyDatafree(BuyNSContsRequestVO paramVO) throws Exception{
    	
    	Integer result = 0;
    	
    	result = this.insertDatafreeContent(paramVO);
    	
    	if(result > 0){
    		result = this.insertDatafreeDetail(paramVO);
    	}
    	
    	return result;
    	
    }
    
    /**
     *  ??????????????? ???????????? ??????
     *  @param	BuyContsCpRequestVO
     *  @result	Integer
     */
    public Integer insertDatafreeContent(BuyNSContsRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
    	
    	String sqlId = "nsvod010_i07_20171214_001";
    	String szMsg = "";
    	
    	Integer querySize = 0;
    	try {
    		
    		if("0".equals(paramVO.getProdType())){
    			
				sqlId = "nsvod010_i04_20171214_001";				
				try{
					querySize = buyNSContsDao.insertDatafreeContent1(paramVO);
				}catch(DataAccessException e){
					//DB?????? Exception?????? ??? getLastException??? ???????????? SQLException?????? ??????
					throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
				}
    			
    		}else if("1".equals(paramVO.getProdType())){
    			
				sqlId = "nsvod010_i08_20171214_001";				
				try{
					querySize= buyNSContsDao.insertDatafreeContent2(paramVO);
				}catch(DataAccessException e){
					//DB?????? Exception?????? ??? getLastException??? ???????????? SQLException?????? ??????
					throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
				}
    			
    		}
    		
			try {
//				imcsLog.dbLog(ImcsConstants.NSAPI_PRO_ID010, sqlId, null, querySize, methodName, methodLine);
				
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] insert [PT_VO_BUY_NSC] table[" + querySize + "] records Success at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
				
			} catch (Exception e) {}
			
		} catch (Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] insert [PT_VO_BUY_NSC] table Failed at";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID010, sqlId, null, "fail info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
		return querySize;
	}
    
    /**
     *  ??????????????? ???????????? ??????
     *  @param	BuyContsCpRequestVO
     *  @result	Integer
     */
    public Integer insertDatafreeDetail(BuyNSContsRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId = "nsvod010_i09_20171214_001";
    	String szMsg = "";
    	
    	Integer querySize = 0;	
    	try {
    		
    		
			try{
				querySize = buyNSContsDao.insertDatafreeDetail(paramVO);
			}catch(DataAccessException e){
				//DB?????? Exception?????? ??? getLastException??? ???????????? SQLException?????? ??????
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
		} catch (Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] insert [PT_VO_BUY_DETAIL_NSC] table Failed at";
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID010, sqlId, null, "fail info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
		return querySize;
	}
    
    /**
     * FVOD??? ????????? PPV or PPS ????????? ?????? ????????? NPT_VO_BUY_META???????????? ????????? ????????????.    
     * @param vo
     * @return
     * @throws Exception
     */
    public Integer insBuyMeta(BuyNSContsRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
    	Integer querySize = 0;		
		String szMsg = "";
		    	
    	try {    		
    		try{
    			// 2019.10.30 - VOD ?????? ???????????? ?????? : FVOD??? ???????????? ?????????. (???, FVOD+??????????????? ????????? ????????? ????????????.)
    			if( !( "0".equals(paramVO.getProdType()) && !"2".equals(paramVO.getBuyTypeFlag()) ) || ( "N".equals(paramVO.getPkgYn()) && "Y".equals(paramVO.getIsLGU()) && "N".equals(paramVO.getIsPayDatafree()) ) ){    			
    				querySize = buyNSContsDao.insBuyMeta(paramVO);
    			    						
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] insert [NPT_VO_BUY_META] table[" + querySize + "] records Success at";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
				}
    			else
    			{
    				querySize = 1;
    				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] insert [NPT_VO_BUY_META] table pass(FVOD)";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
    			}
			}catch(DataAccessException e){
				//DB?????? Exception?????? ??? getLastException??? ???????????? SQLException?????? ??????
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}		
		} catch (Exception e) {			
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID010) + "] insert [NPT_VO_BUY_META] table Failed at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);			
		}
		
		return querySize;
	}
}
