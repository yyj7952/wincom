package kr.co.wincom.imcs.api.buyContsCp;

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
import kr.co.wincom.imcs.handler.ImcsException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class BuyContsCpServiceImpl implements BuyContsCpService {
	private Log imcsLogger = LogFactory.getLog("API_buyContsCp");
	
	@Autowired
	private BuyContsCpDao buyContsCpDao;
	
	@Autowired
	private NoSQLRedisDao noSQLRedisDao;
	
	@Autowired
	private CommonService commonService;

//	public void buyContsCp(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	
	@Override
	public BuyContsCpResultVO buyContsCp(BuyContsCpRequestVO paramVO){
//		this.buyContsCp(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	

		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		imcsLog.debugLog(methodName + " service call");
		
		BuyContsCpResultVO resultVO = new BuyContsCpResultVO();
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		String flag		= "";
		String errCode	= "";
		String errMsg	= "";
		String resultCode	= "";
		
	    int nProdChk	= 0;
	    int nDupChk		= 0;
	    
	    Integer resultSet	= 0;
	    Integer messageSet	= 99;
	    
	    String msg	= "";
		
		int nMainCnt = 0;

		long tp1 = 0;
		long tp2 = 0;
		long tp_start		= paramVO.getTp_start();
	    
		List<ComSbcVO> lstCustomerInfo	= null;
		List<ComPriceVO> lstPriceVO	= null;
		List<ContTypeVO> lstPkgConts = null;
		ComSbcVO customerVO	= new ComSbcVO();
		ComPriceVO priceInfoVO = new ComPriceVO();
		ContTypeVO pkgContsVO = new ContTypeVO();
		ComDataFreeVO datafreeVO = new ComDataFreeVO();
		
		try{
			
			/* ????????? ????????? ????????? ?????? ?????? ??????*/
			if("Y".equals(paramVO.getPkgYn()) && !"1".equals(paramVO.getBuyTypeFlag())){
				
				msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID108) + "] ????????? ?????? ????????? ?????? ?????? ??????! PKG_YN[" + paramVO.getPkgYn() + "] DATAFREE_FLAG[" + paramVO.getBuyTypeFlag() + "]";
				imcsLog.serviceLog(msg, methodName, methodLine);
				
				resultSet	= -1;
    			messageSet = 15; /* ???????????? ?????? ?????? ???????????????.*/
			}
			
			tp1 = System.currentTimeMillis();
			imcsLog.timeLog("init...", String.valueOf(tp1 - tp_start), methodName, methodLine);
			
			switch (paramVO.getBuyingType()) {
			case ImcsConstants.CP_USE_YN_INNERCOUPON:
				switch (paramVO.getOfferType()) {
				case "1":
					paramVO.setCpUseYn(ImcsConstants.CP_USE_YN_INNERCOUPON);
					paramVO.setDatafreeCpUseYn(ImcsConstants.CP_USE_YN_NORMAL);
					break;
					
				case "2":
					paramVO.setCpUseYn(ImcsConstants.CP_USE_YN_NORMAL);
					paramVO.setDatafreeCpUseYn(ImcsConstants.CP_USE_YN_INNERCOUPON);
					break;
					
				case "3":
					paramVO.setCpUseYn(ImcsConstants.CP_USE_YN_INNERCOUPON);
					paramVO.setDatafreeCpUseYn(ImcsConstants.CP_USE_YN_INNERCOUPON);
					break;
					
				default:
					msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID108) + "] ????????? offer type[offer_type=" + paramVO.getOfferType() + "]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					
					resultSet	= -1;
	    			messageSet = 15; /* ???????????? ?????? ?????? ???????????????.*/					
					break;
				}				
				break;
			
			case ImcsConstants.CP_USE_YN_OTHERCOUPON:
				switch (paramVO.getOfferType()) {
				case "1":
					paramVO.setCpUseYn(ImcsConstants.CP_USE_YN_OTHERCOUPON);
					paramVO.setDatafreeCpUseYn(ImcsConstants.CP_USE_YN_NORMAL);
					break;
					
				case "2":
					paramVO.setCpUseYn(ImcsConstants.CP_USE_YN_NORMAL);
					paramVO.setDatafreeCpUseYn(ImcsConstants.CP_USE_YN_OTHERCOUPON);
					break;
					
				case "3":
					paramVO.setCpUseYn(ImcsConstants.CP_USE_YN_OTHERCOUPON);
					paramVO.setDatafreeCpUseYn(ImcsConstants.CP_USE_YN_OTHERCOUPON);
					break;
					
				default:
					msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID108) + "] ????????? offer type[offer_type=" + paramVO.getOfferType() + "]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					
					resultSet	= -1;
	    			messageSet = 15; /* ???????????? ?????? ?????? ???????????????.*/					
					break;
				}				
				break;
				
			case ImcsConstants.CP_USE_YN_MEMBERSHIP:
				paramVO.setOfrSeq(""); /* ????????? ?????? ????????? INPUT?????? ?????? ???????????? ?????????. */
				switch (paramVO.getOfferType()) {
				case "1":
					paramVO.setCpUseYn(ImcsConstants.CP_USE_YN_MEMBERSHIP);
					paramVO.setDatafreeCpUseYn(ImcsConstants.CP_USE_YN_NORMAL);
					switch (paramVO.getAlwnceUnt()) {
					case "3":						
						break;

					default:
						msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID108) + "] ????????? alwnce unt[alwnce_unt=" + paramVO.getAlwnceUnt() + "]";
						imcsLog.serviceLog(msg, methodName, methodLine);
						
						resultSet	= -1;
		    			messageSet = 15; /* ???????????? ?????? ?????? ???????????????.*/	
						break;
					}
					break;
					
				case "2":
				case "3":
				default:
					msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID108) + "] ????????? offer type[offer_type=" + paramVO.getOfferType() + "]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					
					resultSet	= -1;
	    			messageSet = 15; /* ???????????? ?????? ?????? ???????????????.*/					
					break;
				}				
				break;

			default:
				msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID108) + "] ???????????? ?????? ?????? ???????????????.[c_buying_type=" + paramVO.getBuyingType() + "]";
				imcsLog.serviceLog(msg, methodName, methodLine);
				
				resultSet	= -1;
    			messageSet = 15; /* ???????????? ?????? ?????? ???????????????.*/	
				break;
			}
			
			if(resultSet == 0 ){
				switch (paramVO.getBuyTypeFlag()) {
				case "1":
					switch (paramVO.getOfferType()) {
					case "1":
						
						break;

					default:
						msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID108) + "] OFFER TYPE ?????? BUY TYPE FLAG ??????! OFFER TYPE[" + paramVO.getOfferType() + "] DATAFREE_FLAG["+ paramVO.getBuyTypeFlag() +"]";
						imcsLog.serviceLog(msg, methodName, methodLine);
						
						resultSet	= -1;
		    			messageSet = 15; /* ???????????? ?????? ?????? ???????????????.*/	
						break;
					}
					break;
				
				case "2":
					switch (paramVO.getOfferType()) {
					case "2":
						
						break;

					default:
						msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID108) + "] OFFER TYPE ?????? BUY TYPE FLAG ??????! OFFER TYPE[" + paramVO.getOfferType() + "] DATAFREE_FLAG["+ paramVO.getBuyTypeFlag() +"]";
						imcsLog.serviceLog(msg, methodName, methodLine);
						
						resultSet	= -1;
		    			messageSet = 15; /* ???????????? ?????? ?????? ???????????????.*/	
						break;
					}
					break;
					
				case "3":
					switch (paramVO.getOfferType()) {
					case "1":
					case "2":
					case "3":
						break;

					default:
						msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID108) + "] OFFER TYPE ?????? BUY TYPE FLAG ??????! OFFER TYPE[" + paramVO.getOfferType() + "] DATAFREE_FLAG["+ paramVO.getBuyTypeFlag() +"]";
						imcsLog.serviceLog(msg, methodName, methodLine);
						
						resultSet	= -1;
		    			messageSet = 15; /* ???????????? ?????? ?????? ???????????????.*/	
						break;
					}
					break;

				default:
					msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID108) + "] OFFER TYPE ?????? BUY TYPE FLAG ??????! OFFER TYPE[" + paramVO.getOfferType() + "] DATAFREE_FLAG["+ paramVO.getBuyTypeFlag() +"]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					
					resultSet	= -1;
	    			messageSet = 15; /* ???????????? ?????? ?????? ???????????????.*/
					break;
				}
			}
			
			if(resultSet == 0){
				
				// ??????, ???????????? ??? ????????? ????????????
				String szBuyingDate = "";
		    	try {
		    		szBuyingDate = commonService.getSysdate();
		    		paramVO.setBuyingDate(szBuyingDate);
		    		paramVO.setpIdxDay(szBuyingDate.substring(6, 8));
		    	} catch (Exception e) {
		    		imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage() + "," + methodName + "_getSysdate()");
				}
		    	
		    	try {
		    		lstCustomerInfo = this.getCustomerInfo(paramVO);
		    		
		    		if(lstCustomerInfo == null || lstCustomerInfo.isEmpty()) {
		    			resultSet	= -1;
		    			messageSet = 10;
		    		} else {
		    			customerVO	= lstCustomerInfo.get(0);
		    			
		    			if(!"".equals(customerVO.getPvsCtnNo()) && "MOBILE".equals(customerVO.getPvsAtrtChnlDvCd()) ){
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
				
			}
			
			// ?????? ???????????? (??????/??????) ??????
			if(resultSet == 0){
				tp1 = System.currentTimeMillis();
				
				lstPriceVO = this.getBillTypeInfo(paramVO);
				if(lstPriceVO == null || lstPriceVO.size() == 0) {
					resultSet = -1;
					messageSet	= 13;
				} else {	
					priceInfoVO = lstPriceVO.get(0);
					
					paramVO.setProductType(priceInfoVO.getProductType());		// 2019.10.29 - NPT_VO_BUY_META ?????? ??????
					
					if("0".equals(paramVO.getProductType())){
						datafreeVO.setPrice("0");
					}else{
						datafreeVO.setPrice(priceInfoVO.getSuggestedPrice());
					}
					
					// 2019.10.29 - NPT_VO_BUY_META ?????? ?????? Set
					paramVO.setAssetName(priceInfoVO.getAssetName());
					paramVO.setHdcontent(priceInfoVO.getHdcontent());
					paramVO.setRatingCd(priceInfoVO.getRatingCd());
					
					// 2019.10.29 - NPT_VO_BUY_META PPV ?????? Set
					if(!paramVO.getPkgYn().toUpperCase().equals("Y"))	// ?????? ????????? ??????
					{
						paramVO.setProductId(priceInfoVO.getProductId());
						paramVO.setProductName(priceInfoVO.getProductName());
						paramVO.setProductKind(priceInfoVO.getProductKind());
						paramVO.setCpId(priceInfoVO.getCpId());
						paramVO.setMaximumViewingLength(priceInfoVO.getMaximumViewingLength());
						paramVO.setSeriesNo(priceInfoVO.getSeriesNo());
					}
					
					if("N".equals(priceInfoVO.getDatafreeBillYn())){
						datafreeVO.setDatafreePrice("0");
					}else{
						
						//??????????????? ?????? ??????
		    			datafreeVO = this.getDatafreeInfo(datafreeVO, paramVO);
						
					}
					
					paramVO.setSuggestedDatafreeBuyPrice(datafreeVO.getDatafreePrice());
					
					tp2	= System.currentTimeMillis();
					imcsLog.timeLog("??????/?????? ??????", String.valueOf(tp2 - tp1), methodName, methodLine); 
					
					/* ???????????? ?????? */
					if(!"Y".equals(customerVO.getStatusFlag())){
						resultSet = -1;
						messageSet = 11;
					}
					
					if(resultSet == 0){
						/* ???????????? ?????? */
						if( "N".equals(customerVO.getYnVodOpen()) ){
							/* FVOD??? ???????????? */
							if(!"F".equals(priceInfoVO.getBillType())){
								resultSet = -1;
								messageSet = 12;
							}
						}
					}
				}				
				
			}
			
			if(resultSet == 0){
				/* ?????? ?????? ????????? ?????? ?????? ?????? */
				if("1".equals(paramVO.getProductType()) && "3".equals(paramVO.getBuyTypeFlag()) && "Y".equals(priceInfoVO.getPossessionYn()) ){
					
					msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID108) + "] ?????? ?????? ?????? ????????? ?????? ?????? ??????! POSSESSION_YN[" + priceInfoVO.getPossessionYn() + "] DATAFREE_FLAG["+ paramVO.getBuyTypeFlag() +"]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					
					resultSet	= -1;
	    			messageSet = 15; /* ???????????? ?????? ?????? ???????????????.*/
					
				}
				
				if( ( "0".equals(paramVO.getProductType()) && "3".equals(paramVO.getBuyTypeFlag()) ) 
						|| ( "1".equals(paramVO.getProductType()) && "2".equals(paramVO.getBuyTypeFlag()) ) ){
					
					msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID108) + "] DATAFREE BUY FLAG ??????! PKG_YN[" + paramVO.getPkgYn() + "] PRODUCT_TYPE["+ paramVO.getProductType() +"] DATAFREE_FLAG["+ paramVO.getBuyTypeFlag() +"]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					
					resultSet	= -1;
	    			messageSet = 15; /* ???????????? ?????? ?????? ???????????????.*/
					
				}
			}
			
			
			// ???????????? ????????? ?????? ??????
			if(resultSet == 0 && !"F".equals(priceInfoVO.getBillType()) && "N".equals(paramVO.getPkgYn()) ){
				tp1 = System.currentTimeMillis();
				nProdChk = this.CustomProductChk(paramVO);
				
				tp2	= System.currentTimeMillis();
				imcsLog.timeLog("???????????? ????????? ?????? ??????", String.valueOf(tp2 - tp1), methodName, methodLine); 
				
				resultSet = paramVO.getResultSet();
				
				if(resultSet == -1){
					messageSet = 14;
				}
				
				if(nProdChk == 0){
					if( !"F".equals(priceInfoVO.getBillType()) ){
						resultSet = -1;
						messageSet = 9;
					}
				}
				
			}
			
			
			// ???????????? ??????
			if( resultSet == 0 && "1".equals( paramVO.getProductType() ) ){			// PPV ??????
				
				if("2".equals(paramVO.getBuyTypeFlag())){
					
					msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID108) + "] PPV DATAFREE BUY FLAG ??????! PKG_YN[" + paramVO.getPkgYn() + "] DATAFREE_FLAG["+ paramVO.getBuyTypeFlag() +"]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					
					resultSet	= -1;
	    			messageSet = 15; /* ???????????? ?????? ?????? ???????????????.*/
				}
				
				if(Integer.parseInt( priceInfoVO.getEventValue() ) > 0){

					msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID108) + "] ??????????????? - price[" + priceInfoVO.getSuggestedPrice() + " -> " +priceInfoVO.getEventPrice() + "]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					paramVO.setBuyingPrice(priceInfoVO.getEventPrice());
						
				}else{

					if( ( "1".equals(paramVO.getBuyTypeFlag()) || "3".equals(paramVO.getBuyTypeFlag()) ) && !priceInfoVO.getSuggestedPrice().equals( paramVO.getBuyingPrice() ) ){
						msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID108) + "] ??????PPV - incorrect price[imcs_price=" + priceInfoVO.getSuggestedPrice() + "][stb_price=" + paramVO.getBuyingPrice() + "]";
						imcsLog.serviceLog(msg, methodName, methodLine);
						
						resultSet = -1;
                        messageSet = 45;
					}
				}
					
			} else if( resultSet == 0 && "0".equals(paramVO.getProductType()) ){		// FVOD ??????
				
				if("3".equals(paramVO.getBuyTypeFlag())){
					
					msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID108) + "] FOD DATAFREE BUY FLAG ??????! PRODUCT_TYPE[" + paramVO.getProductType() + "] DATAFREE_FLAG["+ paramVO.getBuyTypeFlag() +"]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					
					resultSet	= -1;
	    			messageSet = 15; /* ???????????? ?????? ?????? ???????????????.*/
				}
				
				if( "1".equals(paramVO.getOfferType()) || "3".equals(paramVO.getOfferType()) ){
					
					msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID108) + "] FOD DATAFREE OFFER FLAG ??????! PRODUCT_TYPE[" + paramVO.getProductType() + "] OFFER_TYPE["+ paramVO.getOfferType() +"]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					
					resultSet	= -1;
	    			messageSet = 15; /* ???????????? ?????? ?????? ???????????????.*/
					
				}
				
				if( ( "1".equals(paramVO.getBuyTypeFlag()) || "2".equals(paramVO.getBuyTypeFlag()) ) && !"0".equals(paramVO.getBuyingPrice())){
					msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID108) + "] FVOD - incorrect price[imcs_price=0][stb_price=" + paramVO.getBuyingPrice() + "]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					
					resultSet = -1;
                    messageSet = 45;
				}
			}
			
			//?????? ????????? ?????? ???????????? ??????
			if(resultSet == 0){
				
				nDupChk = this.chkDatafreeDup(paramVO);
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
			
			
			// ???????????? ?????? ??????
			if(resultSet == 0 && !"0".equals(paramVO.getProductType())){
				nDupChk = this.getBuyDupChk(paramVO);
				resultSet = paramVO.getResultSet();
				
				if(resultSet == -1){
					messageSet = 22;
				}
				//}
				/* ????????? ???????????? ????????? */
				if(nDupChk > 0){
					resultSet = -1;
					messageSet = 24;
				}
			}
			
			if( resultSet == 0 ){
				if("N".equals(priceInfoVO.getDatafreeBillYn())){
					if( "2".equals(paramVO.getOfferType()) || "3".equals(paramVO.getOfferType())  ){
						msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID108) + "] ?????? ????????? ?????? - ?????? ??????  [offer_type=" + paramVO.getOfferType() + "]";
						imcsLog.serviceLog(msg, methodName, methodLine);
						
						resultSet	= -1;
		    			messageSet = 15; /* ???????????? ?????? ?????? ???????????????.*/
					}
					if( ( "2".equals(paramVO.getBuyTypeFlag()) || "3".equals(paramVO.getBuyTypeFlag()) ) && !"0".equals(paramVO.getDatafreeBuyPrice()) ){
						msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID108) + "] ????????? ?????? - incorrect price [imcs_price=0][stb_price="+paramVO.getDatafreeBuyPrice()+"]";
						imcsLog.serviceLog(msg, methodName, methodLine);
						
						resultSet	= -1;
		    			messageSet = 45; 
					}
				}else if( ( "2".equals(paramVO.getBuyTypeFlag()) || "3".equals(paramVO.getBuyTypeFlag()) ) && "Y".equals(priceInfoVO.getDatafreeBillYn()) ){
					if(!datafreeVO.getDatafreePrice().equals(paramVO.getDatafreeBuyPrice())){
						
						msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID108) + "] Datafree - incorrect price [imcs_price="+datafreeVO.getDatafreePrice()+"][stb_price="+paramVO.getDatafreeBuyPrice()+"]";
						imcsLog.serviceLog(msg, methodName, methodLine);
						
						resultSet	= -1;
		    			messageSet = 45; 
					}
				}
			}
			
			
			
			if(resultSet == 0){
				if("Y".equals(paramVO.getPkgYn())){			// ????????? ????????? ??????
					tp1	= System.currentTimeMillis();
					paramVO.setContsGenre("?????????");
					String[] ppsCpId = new String[4];
					
					/* NO SQL ?????? <SQL-070> START ----------------------------------------------*/
					/* ???????????? -- NOSQL ?????? ?????? ----------------------------------------------
					????????? ????????? ?????? ???????????? ?????? ??? ???????????? ?????? ???????????? ?????? ??????
					?????? ????????? ID?????? ?????? ?????? ??? ?????? ?????? ???????????? ??????????????? ????????? ??? ??????
					????????? ????????? ?????? ?????? ???????????? ????????? ??????ID??? ????????????
					?????? ?????? ID??? ????????? ????????? ?????? ???
					Client/???????????? ??? ?????????????????? ?????? ?????? ????????????
					PPS ????????? ?????? PT_PD_PACKAGE_DETAIL ????????? ??????ID??? ?????????????????? ????????????
					NOSQL ???????????? ???
					/---------------------------------------------------------------------------*/
					nMainCnt = 0;
					
					try {
						lstPkgConts = this.getPkgContent(paramVO);
						
						if(lstPkgConts != null && !lstPkgConts.isEmpty())
							nMainCnt	= lstPkgConts.size();
						
						if(nMainCnt == 0) {
							resultSet	= -1;
							messageSet	= 31;
						} else {
							for(int i=0; i<nMainCnt; i++){
								pkgContsVO = lstPkgConts.get(i);
								
								Integer result = 0;
								
								paramVO.setProductId2(pkgContsVO.getProductId());
								paramVO.setContsId2(pkgContsVO.getContsId());
								paramVO.setContsName2(pkgContsVO.getContsName());
								paramVO.setContsGenre2(pkgContsVO.getContsGenre());
								
								if(pkgContsVO.getCpId().equals("1414"))	// ????????? CP
								{
									ppsCpId[3] = pkgContsVO.getCpId();
								}
								else if(pkgContsVO.getCpId().equals("1302"))	// ??????????????? CP
								{
									ppsCpId[2] = pkgContsVO.getCpId();
								}
								else
								{
									if(ppsCpId[0] == null || ppsCpId[0].equals(""))
									{
										ppsCpId[0] = pkgContsVO.getCpId();
									}
									else if(!ppsCpId[0].equals(pkgContsVO.getCpId()))
									{
										ppsCpId[1] = pkgContsVO.getCpId();
									}
								}
								
						    	result = this.insertBuyContent3(paramVO);
						    	
						    	if(result > 0)		resultSet = 0;
						    	else		   		resultSet = -1;
						    	
						    	if(resultSet == -1){
						    		resultSet = -1;
				                    messageSet = 32;
				                    
				                    if(paramVO.getSqlCode() == -1)
				                    	messageSet = 24;
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
			    		    
						}
					} catch (Exception e) {
						resultSet	= -1;
						messageSet	= 30;
					}
					
					tp2	= System.currentTimeMillis();
					imcsLog.timeLog("????????? ????????? ????????? ??????", String.valueOf(tp2 - tp1), methodName, methodLine); 
					
				}else if("N".equals(paramVO.getPkgYn())){		// ??????????????? ??????
					tp1	= System.currentTimeMillis();
					
					// ???????????? ??????
					try {
						lstPkgConts	= this.getGenreType(paramVO);
						
						if(lstPkgConts != null && !lstPkgConts.isEmpty())
							nMainCnt	= lstPkgConts.size();
						
						if(nMainCnt > 0) {
							pkgContsVO	= lstPkgConts.get(nMainCnt - 1);
							paramVO.setContsGenre(pkgContsVO.getContsGenre());
							paramVO.setEventType(pkgContsVO.getContsType());
						}
						
						tp2	= System.currentTimeMillis();
						imcsLog.timeLog("?????? ????????? ?????? ??????", String.valueOf(tp2 - tp1), methodName, methodLine); 
						
						
						tp1	= System.currentTimeMillis();
						
						Integer result = 0;
						
						result = this.insertBuyContent4(paramVO);
				    	
				    	if(result == 1)   		resultSet = 0;
				    	else		    		resultSet = -1;
				    	
				    	if(resultSet == -1){
				    		resultSet = -1;
		                    messageSet = 32;
				    	}
						
						tp2	= System.currentTimeMillis();
						imcsLog.timeLog("?????? ????????? ????????? ??????", String.valueOf(tp2 - tp1), methodName, methodLine); 
					} catch(Exception e) {
						resultSet	= -1;
						messageSet	= 90;
						
						if(paramVO.getSqlCode() == -1)
							messageSet	= 24;

					}
				}
			} 
			
			
			
			if(paramVO.getContsGenre() == null || "".equals(paramVO.getContsGenre())){
				paramVO.setContsGenre("??????");
			}
			
			if(resultSet == 0){
				
				// ???????????? ??????
				tp1	= System.currentTimeMillis();
				
				if("N".equals(paramVO.getPkgYn().toUpperCase())){
					Integer result = 0;
					
					result = this.insertBuyContent1(paramVO);
			    	
			    	if(result > 0)		resultSet = 0;
			    	else	    		resultSet = -1;
			    	
			    	if(resultSet == -1){
			    		resultSet = -1;
	                    messageSet = 40;
			    	}
				}else{
					pkgContsVO = this.getProduct(paramVO);
					
					if(pkgContsVO != null) {
						paramVO.setProductId1(pkgContsVO.getProductId());
						paramVO.setProductName1(pkgContsVO.getProductName());
						paramVO.setProductPrice1(pkgContsVO.getPrice());
						paramVO.setExpiredDate1(pkgContsVO.getExpiredDate());
						
						// 2019.10.30 - VOD ?????? ???????????? ?????? : PPS ?????? ?????? NPT_VO_BUY_META??? ???????????? paramVO??? Set
						paramVO.setProductKind(pkgContsVO.getProductKind());
						paramVO.setProductId(pkgContsVO.getProductId());
						paramVO.setProductName(pkgContsVO.getProductName());
						paramVO.setMaximumViewingLength(pkgContsVO.getExpiredDate());
					}
					
					/**PPS ?????? ??????**/
					if(!paramVO.getBuyingPrice().equals(paramVO.getProductPrice1())){
						msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID108) + "] PPS ?????? - incorrect price[imcs_price="+paramVO.getProductPrice1()+"][stb_price="+paramVO.getBuyingPrice()+"]";
						imcsLog.serviceLog(msg, methodName, methodLine);
						
						resultSet = -1;
					}
					
					if(resultSet == 0){
						
						Integer result = 0;
						
						result = this.insertBuyContent2(paramVO);
				    	
				    	if(result > 0)		resultSet = 0;
				    	else	    		resultSet = -1;				    	
					}
					
					if(resultSet == -1){
			    		resultSet = -1;
	                    messageSet = 40;
			    	}
					
					
				}
				
				tp2	= System.currentTimeMillis();
				imcsLog.timeLog("???????????? ?????? ??????", String.valueOf(tp2 - tp1), methodName, methodLine); 
			}
			
			msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID108) + "] D/F Buy Check : result_set["+resultSet+"] c_datafree_buy_flag["+paramVO.getBuyTypeFlag()+"]"
					+ "c_is_pay_datafree["+priceInfoVO.getDatafreeBillYn()+"] c_is_LGU["+paramVO.getIsLGU()+","+customerVO.getPvsCtnNo()+","+customerVO.getPvsAtrtChnlDvCd()+"]";
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			if( resultSet == 0 && 
					( ( "2".equals(paramVO.getBuyTypeFlag()) || "3".equals(paramVO.getBuyTypeFlag()) 
							|| ( "N".equals(paramVO.getPkgYn()) && "Y".equals(paramVO.getIsLGU()) && "N".equals(priceInfoVO.getDatafreeBillYn()) ) ) ) )
			{
				
				if( "N".equals(paramVO.getPkgYn()) && "Y".equals(paramVO.getIsLGU()) && "N".equals(priceInfoVO.getDatafreeBillYn()) ){
					
					msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID108) + "] sts[DF_WARN] ?????? ?????????Free DEFAULT ??????";
					imcsLog.serviceLog(msg, methodName, methodLine);
					
				}
				
				Integer result = 0;
				
				result = this.buyDatafree(paramVO);
				
				if(result > 0)		resultSet = 0;
		    	else	    		resultSet = -1;
				
				if(resultSet == -1){
					msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID108) + "] Data Free ?????? ??????";
					imcsLog.serviceLog(msg, methodName, methodLine);
					
					resultSet = -1;
					messageSet = 40;
				}				
			}
			
			// 2019.10.30 - VOD ?????? ???????????? ?????? : NPT_VO_BUY_META ????????? INSERT
			// ???????????? ?????? ??????????????? ????????? ?????? ?????????, PPV+DataFree ?????? ??? ???????????? NPT_VO_BUY_META???????????? ????????? ???????????? ????????????.
			if(resultSet == 0)
			{
				if(this.insBuyMeta(paramVO, priceInfoVO) > 0) {
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
		    	resultVO.setBuyingDate(paramVO.getBuyingDate());
		    	
		    	tp2	= System.currentTimeMillis();
		    	imcsLog.timeLog("EXEC SQL COMMIT", String.valueOf(tp2 - tp1), methodName, methodLine); 
		    }else{
		       	resultVO.setFlag("1");
		    	resultVO.setBuyingDate("");

			    // paramVO.setGetCouponYn("N");	// ????????????
			    // paramVO.setGetStampYn("N");	// ????????????
		    	
			    throw new ImcsException(resultVO.getFlag(), resultVO.getErrMsg(), resultVO.getErrCode(), resultVO);
		    }
		}catch(ImcsException ie) {
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
				resultVO.setBuyingDate(paramVO.getBuyDate());
			}
			
			resultVO.setFlag(flag);
			resultVO.setErrCode(errCode);
			resultVO.setErrMsg(errMsg);
			paramVO.setResultCode(resultCode);
			
			msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID108) + "] msg[" + errMsg + "]";
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("EXEC SQL ROLLBACK", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			imcsLog.errorLog(methodName + "-E",ie.getClass().getName()+":"+ie.getMessage());
			throw new ImcsException(flag, errMsg, errCode, resultVO);
		} catch(Exception e) {
			isLastProcess = ImcsConstants.RCV_MSG6;

			resultVO.setFlag("1");
			resultVO.setErrCode("99");
			resultVO.setErrMsg("incorrect failed !!!");
			
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			throw new ImcsException();
		} finally{
			resultVO.setResultCode(paramVO.getResultCode());
			
			msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID108) + "] return[imcs_price=" + resultVO.toString() + "]";
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			// NosqlCacheType.HBASE_WR.ordinal(), NosqlCacheType.USERDB.ordinal() ??????????
			
//			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID108) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
//					+ " [DR:" + nDRedis + "|DH:" + nDHbase + "|R:" + nRedis + "|H:" + nHbase + "]"; 
						
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultVO;
	}
	
    
   
    
    /**
     *	????????? ??????, ???????????? ??????
     * 	@param paramVO
     *	@return
     */
    public List<ComSbcVO> getCustomerInfo(BuyContsCpRequestVO paramVO){
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();    	
    	
    	String sqlId =  "lgvod108_010_20171214_001";
		
//		NosqlResultCache cache = new NosqlResultCache();
//		List<Object> binds = new ArrayList<Object>();
//		RowKeyList rowKeys = new RowKeyList();
//		VersionUpdateCheckKey checkKey = new VersionUpdateCheckKey();
		
		List<ComSbcVO> list   = new ArrayList<ComSbcVO>();
		
		try {
			
//			rowKeys.setSaId(paramVO.getSaId());
//			rowKeys.setStbMac(paramVO.getStbMac());
//			rowKeys.setSqlId(sqlId);
//			checkKey.addVersionTuple("PT_VO_CUSTOM_ID", paramVO.getSaId());
//			checkKey.addVersionTuple("NSCN_SBC_TBL", paramVO.getSaId());
			
//			binds.add(paramVO);
//			
//			list = cache.getCachedResult(new CacheableExecutor<ComSbcVO>() {
//				@Override
//				public List<ComSbcVO> execute(List<Object> param) throws SQLException {
//					try{
//						BuyContsCpRequestVO requestVO = (BuyContsCpRequestVO)param.get(0);
//						List<ComSbcVO> rtnList = buyContsCpDao.getSbcInfo(requestVO);
//												
//						return rtnList;
//					}catch(DataAccessException e){
//						//DB?????? Exception?????? ??? getLastException??? ???????????? SQLException?????? ??????
//						throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
//					}
//				}
//				
//				@Override
//				public Class<ComSbcVO> getReturnType() {
//					return ComSbcVO.class;
//				}
//			}, binds, rowKeys, checkKey);
//			
//			paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
			
			try{
				list = buyContsCpDao.getSbcInfo(paramVO);
			}catch(DataAccessException e){
				//DB?????? Exception?????? ??? getLastException??? ???????????? SQLException?????? ??????
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
						
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}
		
    	return list;
    }
    
    
    
    /**
     * 	?????? ??????(??????/??????) ?????? ??????
     * 	@param	BuyContsCpRequestVO
     * 	@result	List<CommonPriceVO>
     */
    public List<ComPriceVO> getBillTypeInfo(BuyContsCpRequestVO paramVO){
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();    	
    	
    	String sqlId =  "";
    	
    	if("Y".equals(paramVO.getPkgYn().toUpperCase()))	sqlId =  "lgvod108_021_20171214_001";
    	else										   		sqlId =  "lgvod108_022_20171214_001";
		
//		NosqlResultCache cache = new NosqlResultCache();
//		List<Object> binds = new ArrayList<Object>();
//		RowKeyList rowKeys = new RowKeyList();
//		VersionUpdateCheckKey checkKey = new VersionUpdateCheckKey();
		
		List<ComPriceVO> list   = new ArrayList<ComPriceVO>();
		
		try {
//			rowKeys.setSqlId(sqlId);
//			rowKeys.addRowKeys(paramVO.getAlbumId());
//			
//			checkKey.addVersionTuple("PT_PD_PACKAGE");
//			checkKey.addVersionTuple("PT_PD_PACKAGE_DETAIL", paramVO.getAlbumId());
//			checkKey.addVersionTuple("PT_LA_ALBUM_MST", paramVO.getAlbumId());
//			
//			binds.add(paramVO);
//			
//			list = cache.getCachedResult(new CacheableExecutor<ComPriceVO>() {
//				@Override
//				public List<ComPriceVO> execute(List<Object> param) throws SQLException {
//					try{
//						BuyContsCpRequestVO requestVO = (BuyContsCpRequestVO)param.get(0);
//						List<ComPriceVO> rtnList = buyContsCpDao.getBillTypeInfo(requestVO);
//												
//						return rtnList;
//					}catch(DataAccessException e){
//						//DB?????? Exception?????? ??? getLastException??? ???????????? SQLException?????? ??????
//						throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
//					}
//				}
//				
//				@Override
//				public Class<ComPriceVO> getReturnType() {
//					return ComPriceVO.class;
//				}
//			}, binds, rowKeys, checkKey);
//			
//			paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
			
			try{
				list = buyContsCpDao.getBillTypeInfo(paramVO);
			}catch(DataAccessException e){
				//DB?????? Exception?????? ??? getLastException??? ???????????? SQLException?????? ??????
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
						
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}
		
    	return list;
    }

    
    
    /**
     * 	????????? ???????????? ?????? ??????
     * 	@param	BuyContsCpRequestVO
     * 	@result	int
     */
    public int CustomProductChk(BuyContsCpRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId =  "lgvod108_030_20171214_001";
		
//		NosqlResultCache cache = new NosqlResultCache();
//		List<Object> binds = new ArrayList<Object>();
//		RowKeyList rowKeys = new RowKeyList();
//		VersionUpdateCheckKey checkKey = new VersionUpdateCheckKey();
		
		List<Integer> list   = new ArrayList<Integer>();
		Integer nProdChk = 0;
		
		
		try {
//			rowKeys.setSaId(paramVO.getSaId());
//			rowKeys.setStbMac(paramVO.getStbMac());
//			rowKeys.setSqlId(sqlId);
//			rowKeys.addRowKeys(paramVO.getAlbumId());
//			
//			checkKey.addVersionTuple("PT_VO_CUSTOM_PRODUCT", paramVO.getSaId());
//			checkKey.addVersionTuple("PT_PD_PACKAGE");
//			checkKey.addVersionTuple("PT_PD_PACKAGE_DETAIL", paramVO.getAlbumId());
//			checkKey.addVersionTuple("PT_PD_PACKAGE_RELATION");
//			checkKey.addVersionTuple("PT_LA_ALBUM_MST", paramVO.getAlbumId());
//			
//			binds.add(paramVO);
//			
//			list = cache.getCachedResult(new CacheableExecutor<Integer>() {
//				@Override
//				public List<Integer> execute(List<Object> param) throws SQLException {
//					try{
//						BuyContsCpRequestVO requestVO = (BuyContsCpRequestVO)param.get(0);
//						List<Integer> rtnList = buyContsCpDao.CustomProductChk(requestVO);
//						
//						return rtnList;
//					}catch(DataAccessException e){
//						//DB?????? Exception?????? ??? getLastException??? ???????????? SQLException?????? ??????
//						throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
//					}
//				}
//				
//				@Override
//				public Class<Integer> getReturnType() {
//					return Integer.class;
//				}
//			}, binds, rowKeys, checkKey);
//			
			
			try{
				list = buyContsCpDao.CustomProductChk(paramVO);
			}catch(DataAccessException e){
				//DB?????? Exception?????? ??? getLastException??? ???????????? SQLException?????? ??????
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list == null || list.isEmpty()){
				nProdChk = 0;
			}else {
				nProdChk = (Integer)list.get(0);
			}
			
			//paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
			
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			paramVO.setResultSet(-1);
		}
		
    	return nProdChk;    	
    }
    
    
    
    /**
     * ???????????? ?????? ?????? (????????? ?????? ??????)
     * @param 	BuyContsCpRequestVO
     * @return	Integer
     */
    public Integer getBuyDupChk(BuyContsCpRequestVO paramVO) throws Exception{
		List<ComDupCHk> lstDupChk	= new ArrayList<ComDupCHk>();
		ComDupCHk dupChkVO = new ComDupCHk();
		
		Integer nDupChk = 0;
		
    	try {
    		if( "0".equals(paramVO.getProductType()) ){			// ???????????? ?????? ?????? (FVOD)
    			if( "R".equals(paramVO.getAppType()) ){
    				lstDupChk = buyContsCpDao.buyContentDupChk1(paramVO);
    			}else{
    				lstDupChk = buyContsCpDao.buyContentDupChk1(paramVO);
    			}
    			
    		}else if( "1".equals(paramVO.getProductType()) ){	// ???????????? ?????? ?????? (PPV)
    			if( "R".equals(paramVO.getAppType()) ){
    				lstDupChk = buyContsCpDao.buyContentDupChk2(paramVO);
    			}else{
    				lstDupChk = buyContsCpDao.buyContentDupChk2(paramVO);
    			}
    			
    		}else if( "2".equals(paramVO.getProductType()) ){	// ???????????? ?????? ?????? (PVOD)
    			if( "R".equals(paramVO.getAppType()) ){
    				lstDupChk = buyContsCpDao.buyContentDupChk3(paramVO);
    			}else{
    				lstDupChk = buyContsCpDao.buyContentDupChk3(paramVO);
    			}
    		}
    		
    		if(lstDupChk != null && !lstDupChk.isEmpty()){
    			dupChkVO	= lstDupChk.get(0);
    			
    			paramVO.setBuyDate(dupChkVO.getBuyDate());
    			nDupChk = Integer.parseInt(StringUtil.nullToZero(dupChkVO.getDataChk()));
    		} else 
    			nDupChk	= 0;
    		
		} catch (Exception e) {			
			paramVO.setResultSet(-1);
			e.printStackTrace();
		}
    	
    	return nDupChk;
    }
    
    
    /**
     * 	????????? ???????????? ?????? (????????? ????????? ????????? )
     * 	@param	BuyContsCpRequestVO
     * 	@result	Integer
     */
    public Integer getEventChk(BuyContsCpRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		
		Integer nEventChk = 0;
		
		String szMsg = "";
		
    	try {
    		if("R".equals( paramVO.getAppType() )){
    			nEventChk= buyContsCpDao.getEventChk(paramVO);
    		}else{
    			nEventChk= buyContsCpDao.getEventChk(paramVO);
    		}
    		
//    		imcsLog.dbLog(ImcsConstants.API_PRO_ID108, "lgvod108_040", null, nEventChk, methodName, methodLine);
		} catch (Exception e) {
			paramVO.setResultSet(-1);
			
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID108) + "] sts [    0]" + String.format("%-27s", " msg[" + ImcsConstants.RCV_MSG6 + "]");
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.serviceLog(szMsg, methodName, methodLine);
		}
    	
    	if(nEventChk == null){
    		nEventChk = 0;
    	}
    	
    	return nEventChk;
    }
    
    
    
    /**
     *  ????????? ????????? ?????? ??????
     *  @param	BuyContsCpRequestVO
     *  @result	List<ContTypeVO>
     */
    public List<ContTypeVO> getPkgContent(BuyContsCpRequestVO paramVO){
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();    	
    	
    	String sqlId = "lgvod108_070_20171214_001";
		int querySize = 0;		
		List<ContTypeVO> list   = new ArrayList<ContTypeVO>();
		
		try {
			
			
			try{
				list = buyContsCpDao.getPkgContent(paramVO);						
			}catch(DataAccessException e){
				//DB?????? Exception?????? ??? getLastException??? ???????????? SQLException?????? ??????
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list == null ||list.isEmpty()){
				querySize = 0;
				list = null;
			} else {
				querySize = list.size();
			}
			
			try{
//				imcsLog.dbLog(ImcsConstants.API_PRO_ID108, sqlId, null, querySize, methodName, methodLine);
			}catch(Exception e){}
						
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}
		
    	return list;
    }
    
    
    
    
    /**
     *  ???????????? ????????? ?????? ??????
     *  @param	BuyContsCpRequestVO
     *  @result Integer
     */
    public Integer insertBuyContent3( BuyContsCpRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId = "lgvod108_i01_20171214_001";
    	Integer querySize = 0;
		String szMsg = "";
		    	
    	try {
			
    		
			try{
				querySize = buyContsCpDao.insertBuyContent3(paramVO);
			}catch(DataAccessException e){
				//DB?????? Exception?????? ??? getLastException??? ???????????? SQLException?????? ??????
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
//			imcsLog.dbLog(ImcsConstants.API_PRO_ID108, sqlId, null, querySize, methodName, methodLine);
			
		} catch (Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID108) + "] insert [PT_VO_BUY_DETAIL_NSC] table Failed at";
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			//paramVO.setSqlCode(cache.getLastException().getErrorCode());
//			imcsLog.failLog(ImcsConstants.API_PRO_ID108, "", null, "fail info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
		return querySize;
	}
    
    
    
    /**
     * 	?????? ?????? ?????? (??????)
     * 	BuyContsCpRequestVO
     * 	CommonPriceVO
     */
    public List<ContTypeVO> getGenreType(BuyContsCpRequestVO paramVO){
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();    	
    	
    	String sqlId = "lgvod108_080_20171214_001";
		List<ContTypeVO> list   = new ArrayList<ContTypeVO>();
		
		try {
			
			try{
				list = buyContsCpDao.getGenreType(paramVO);
			}catch(DataAccessException e){
				//DB?????? Exception?????? ??? getLastException??? ???????????? SQLException?????? ??????
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
						
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
    	return list;
    }
    
    
    
    
    /**
     *  ?????? ????????? ????????? ??????
     *  @param	BuyContsCpRequestVO
     *  @result Integer
     */
    public Integer insertBuyContent4(BuyContsCpRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId		=  "";
    	String tableName	= "";
    	
    	if( "0".equals(paramVO.getProductType()) ) {
    		sqlId =  "lgvod108_i02_20171214_001";
    		tableName	= "PT_VO_BUY_DETAIL_FVOD_NSC";
    	} else {
    		sqlId =  "lgvod108_i03_20171214_001";
    		tableName	= "PT_VO_BUY_DETAIL_NSC";
    	}
		
    	Integer querySize = 0;		
		String szMsg = "";
		    	
    	try {
			
    		
			try{
				querySize = buyContsCpDao.insertBuyContent4(paramVO);
			}catch(DataAccessException e){
				//DB?????? Exception?????? ??? getLastException??? ???????????? SQLException?????? ??????
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}    		

//			imcsLog.dbLog(ImcsConstants.API_PRO_ID108, sqlId, null, querySize, methodName, methodLine);
		} catch (Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID108) + "] insert [" + tableName + "] table Failed at";
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			//paramVO.setSqlCode(cache.getLastException().getErrorCode());
//			imcsLog.failLog(ImcsConstants.API_PRO_ID108, sqlId, null, "fail info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
		return querySize;
	}
    
    
    /**
     * 	???????????? ??????
     * 	@param	BuyContsCpRequestVO
     * 	@result	Integer
     */
    public Integer insertBuyContent1(BuyContsCpRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
    	
    	String sqlId 		=  "";
    	String tableName	= "";
    	
    	if( "0".equals(paramVO.getProductType()) ){
    		sqlId =  "lgvod108_i04_20171214_001";
    		tableName	= "PT_VO_BUY_FVOD_NSC";
    	}else{
    		sqlId =  "lgvod108_i05_20171214_001";
    		tableName	= "PT_VO_BUY_NSC";
    	}
		
    	Integer querySize = 0;		
		String szMsg = "";
		    	
    	try {
			

			try{
				querySize = buyContsCpDao.insertBuyContent1(paramVO);
			}catch(DataAccessException e){
				//DB?????? Exception?????? ??? getLastException??? ???????????? SQLException?????? ??????
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
    		
			try {
//				imcsLog.dbLog(ImcsConstants.API_PRO_ID108, sqlId, null, querySize, methodName, methodLine);
				
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID108) + "] insert [" + tableName + "] table[" + querySize + "] records Success at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			} catch (Exception e) {}
			
		} catch (Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID108) + "] insert [" + tableName + "] table Failed at";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
//			imcsLog.failLog(ImcsConstants.API_PRO_ID108, sqlId, null, "fail info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
		return querySize;
	}
    
    
    
    /**
     *  ????????? ?????? ??????
     * 	@param paramVO
     *	@return
     */
    public ContTypeVO getProduct(BuyContsCpRequestVO paramVO){
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();    	
    	
    	String sqlId = "lgvod108_090_20171214_001";		
		List<ContTypeVO> list   = new ArrayList<ContTypeVO>();
		ContTypeVO resultVO = null;
		
		try {
			
			
			try{
				list = buyContsCpDao.getProduct(paramVO);
			}catch(DataAccessException e){
				//DB?????? Exception?????? ??? getLastException??? ???????????? SQLException?????? ??????
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list == null ||list.isEmpty()){
				list = null;
			} else {
				resultVO	= list.get(0);
			}
									
		} catch (Exception e) {
//			imcsLog.failLog(ImcsConstants.API_PRO_ID108, sqlId, null, "pkg_prod_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
    	return resultVO;
    }
    
    
    
    /**
     *  ???????????? ??????
     *  @param	BuyContsCpRequestVO
     *  @result	Integer
     */
    public Integer insertBuyContent2(BuyContsCpRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
    	
    	String sqlId = "lgvod108_i06_20171214_001";
    	String szMsg = "";
    	Integer querySize = 0;
	
    	try {
    		
			try{
				querySize = buyContsCpDao.insertBuyContent2(paramVO);
			}catch(DataAccessException e){
				//DB?????? Exception?????? ??? getLastException??? ???????????? SQLException?????? ??????
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}    		
			
			try {
//				imcsLog.dbLog(ImcsConstants.API_PRO_ID108, sqlId, null, querySize, methodName, methodLine);
				
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID108) + "] insert [PT_VO_BUY_NSC] table[" + querySize + "] records Success at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
				
			} catch (Exception e) {}
			
		} catch (Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID108) + "] insert [PT_VO_BUY_NSC] table Failed at";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
//			imcsLog.failLog(ImcsConstants.API_PRO_ID108, sqlId, null, "fail info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
		return querySize;
	}
        
    /**
     * ??????????????? ?????? ??????
     * @param	ComDataFreeVO, ChkBuyNSPGRequestVO
     * @result	ComDataFreeVO
    **/
    public ComDataFreeVO getDatafreeInfo(ComDataFreeVO tempVO, BuyContsCpRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
    	
    	String sqlId = "lgvod108_005_20171214_001";
    	String szMsg = "";		
		List<ComDataFreeVO> list   = new ArrayList<ComDataFreeVO>();
		ComDataFreeVO resultVO = null;
		
		try {
			
			try{
				list  = buyContsCpDao.getDatafreeInfo(tempVO);
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
//			 imcsLog.failLog(ImcsConstants.API_PRO_ID108, sqlId, null, "approval_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			 paramVO.setResultCode("41000000");
		}
    	return resultVO;
    }
    
    /**
     * ??????????????? ???????????? ?????? ??????
     * @param 	BuyContsCpRequestVO
     * @return	Integer
     */
    public Integer chkDatafreeDup(BuyContsCpRequestVO paramVO) throws Exception{
		List<ComDupCHk> lstDupChk	= new ArrayList<ComDupCHk>();
		ComDupCHk dupChkVO = new ComDupCHk();
		
		Integer nDupChk = 0;
		
    	try {
    		
    		lstDupChk = buyContsCpDao.chkDatafreeDup(paramVO);
    		
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
    
    public Integer buyDatafree(BuyContsCpRequestVO paramVO) throws Exception{
    	
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
    public Integer insertDatafreeContent(BuyContsCpRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
    	
    	String sqlId = "lgvod108_i07_20171214_001";
    	String szMsg = "";
    	Integer querySize = 0;

    	try {
    		
    		
    		if("0".equals(paramVO.getProductType())){
			
    			if("2".equals(paramVO.getOfferType())){
	    		
					try{
						querySize = buyContsCpDao.insertDatafreeContent1(paramVO);
					}catch(DataAccessException e){
						//DB?????? Exception?????? ??? getLastException??? ???????????? SQLException?????? ??????
						throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
					}
    			}
    			
    		}else if("1".equals(paramVO.getProductType())){
    			
    			if( "2".equals(paramVO.getOfferType()) || "3".equals(paramVO.getOfferType()) ){
    				sqlId = "lgvod108_i07_20171214_001";
    				
					try{
						querySize = buyContsCpDao.insertDatafreeContent2(paramVO);
					}catch(DataAccessException e){
						//DB?????? Exception?????? ??? getLastException??? ???????????? SQLException?????? ??????
						throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
					}
    				
    			}else{
    				sqlId = "lgvod108_i11_20171214_001";
					try{
						querySize = buyContsCpDao.insertDatafreeContent3(paramVO);
					}catch(DataAccessException e){
						//DB?????? Exception?????? ??? getLastException??? ???????????? SQLException?????? ??????
						throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
					}
					
    			}
    			
    		}

			try {
//				imcsLog.dbLog(ImcsConstants.API_PRO_ID108, sqlId, null, querySize, methodName, methodLine);
				
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID108) + "] insert [PT_VO_BUY_NSC] table[" + querySize + "] records Success at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
				
			} catch (Exception e) {}
			
		} catch (Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID108) + "] insert [PT_VO_BUY_NSC] table Failed at";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
//			imcsLog.failLog(ImcsConstants.API_PRO_ID108, sqlId, null, "fail info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
		return querySize;
	}
    
    /**
     *  ??????????????? ???????????? ??????
     *  @param	BuyContsCpRequestVO
     *  @result	Integer
     */
    public Integer insertDatafreeDetail(BuyContsCpRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId = "lgvod108_i09_20171214_001";
    	String szMsg = "";
    	Integer querySize = 0;
    	
    	try {
			
			try{
				querySize = buyContsCpDao.insertDatafreeDetail(paramVO);
			}catch(DataAccessException e){
				//DB?????? Exception?????? ??? getLastException??? ???????????? SQLException?????? ??????
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
		} catch (Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID108) + "] insert [PT_VO_BUY_DETAIL] table Failed at";
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
//			imcsLog.failLog(ImcsConstants.API_PRO_ID108, sqlId, cache, "fail info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
		return querySize;
	}
    
    /**
     * FVOD??? ????????? PPV or PPS ????????? ?????? ????????? NPT_VO_BUY_META???????????? ????????? ????????????.    
     * @param vo
     * @return
     * @throws Exception
     */
    public Integer insBuyMeta(BuyContsCpRequestVO paramVO, ComPriceVO priceInfoVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
    	Integer querySize = 0;		
		String szMsg = "";
		    	
    	try {    		
    		try{
    			// 2019.10.30 - VOD ?????? ???????????? ?????? : FVOD??? ???????????? ?????????. (???, FVOD+??????????????? ????????? ????????? ????????????.)
    			if( !( "0".equals(paramVO.getProductType()) && !"2".equals(paramVO.getBuyTypeFlag()) ) || ( "N".equals(paramVO.getPkgYn()) && "Y".equals(paramVO.getIsLGU()) && "N".equals(priceInfoVO.getDatafreeBillYn()) ) ){    			
    				querySize = buyContsCpDao.insBuyMeta(paramVO);
    			    						
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID108) + "] insert [NPT_VO_BUY_META] table[" + querySize + "] records Success at";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
				}
    			else
    			{
    				querySize = 1;
    				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID108) + "] insert [NPT_VO_BUY_META] table pass(FVOD)";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
    			}
			}catch(DataAccessException e){
				//DB?????? Exception?????? ??? getLastException??? ???????????? SQLException?????? ??????
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}		
		} catch (Exception e) {			
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID108) + "] insert [NPT_VO_BUY_META] table Failed at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);			
		}
		
		return querySize;
	}
}
