package kr.co.wincom.imcs.api.buyNSContent;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.wincom.imcs.api.buyNSConts.BuyNSContsRequestVO;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.NosqlCacheType;
import kr.co.wincom.imcs.common.CommonService;
//import kr.co.wincom.imcs.common.nosql.NoSQLRedisDao;
import kr.co.wincom.imcs.common.property.ImcsProperties;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.ComCpnVO;
import kr.co.wincom.imcs.common.vo.GenreInfoVO;
import kr.co.wincom.imcs.handler.ImcsException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.util.SystemOutLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class BuyNSContentServiceImpl implements BuyNSContentService {
	
	@Autowired
	private BuyNSContentDAO buyNSContentDAO;
	
//	@Autowired
//	private NoSQLRedisDao noSQLRedisDao;
	
	@Autowired
	private CommonService commonService;

	private Log imcsLogger = LogFactory.getLog("API_buyNSContent");
	
//	public void buyNSContent(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	int 	block_flag = 0;
	
	@Override
	public BuyNSContentResultVO buyNSContent(BuyNSContentRequestVO paramVO){
//		this.buyNSContent(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		BuyNSContentResultVO resultVO = new BuyNSContentResultVO();
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog(methodName + " service call");
		
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		String flag = ImcsConstants.SUCCESS_CODE;
		String errCode = ImcsProperties.getProperty("result.code.success");
		String errMsg = ImcsProperties.getProperty("result.msg.success");
		
	    
	    int custom_product_chk = 0;
	    int duplic_chk = 0;
	    int evnt_chk = 0;
	    
	    Integer resultSet = 0;
	    Integer messageSet = 99;
	    
	    String msg	= "";
		
		int iMainCount = 0;
		long tp1 = 0;
		long tp2 = 0;
		
		// 2019.04.03 - 부가세 정보 가져오기.
		paramVO.setSzSurtaxRate(commonService.getSurtaxRate());
	    
		try{
			
			/* 일반구매 */
			if( "B".equals(paramVO.getBuyingType().toUpperCase()) ){
				paramVO.setCpUseYn("N");
			}
			/* 쿠폰구매 */
			else if("C".equals(paramVO.getBuyingType().toUpperCase())){
				paramVO.setCpUseYn("Y");
			}	
			/* PG구매 */
			else if("S".equals(paramVO.getBuyingType().toUpperCase())){
				paramVO.setCpUseYn("S");
				// 20190425 - 오과금 AS-IS
//				paramVO.setAlwnceCharge(paramVO.getBuyingPrice());
				// 20190425 - 오과금 TO-BE
				paramVO.setUdrCharge(Integer.parseInt(paramVO.getBuyingPrice()) + (Integer.parseInt(paramVO.getBuyingPrice()) / paramVO.getSzSurtaxRate()));
				paramVO.setAlwnceCharge(Integer.toString(paramVO.getUdrCharge()));
			}
			/* 인앱구매 */
			else if("A".equals(paramVO.getBuyingType().toUpperCase())){
				paramVO.setCpUseYn("A");
				paramVO.setBalace(paramVO.getBuyingPrice());
				if (paramVO.getPaymentId().equals("")) {
					resultSet  = -1;
					messageSet = 3;
				}
			}
			/* PAY NOW 구매 */
			else if("W".equals(paramVO.getBuyingType().toUpperCase())){
				paramVO.setCpUseYn("W");
				// 20190425 - 오과금 AS-IS
//				paramVO.setAlwnceCharge(paramVO.getBuyingPrice());
				// 20190425 - 오과금 TO-BE
				paramVO.setUdrCharge(Integer.parseInt(paramVO.getBuyingPrice()) + (Integer.parseInt(paramVO.getBuyingPrice()) / paramVO.getSzSurtaxRate()));
				paramVO.setAlwnceCharge(Integer.toString(paramVO.getUdrCharge()));
			}else{
				// 20190425 - 오과금 AS-IS
//				paramVO.setCpUseYn(paramVO.getBuyingType().toUpperCase());
				// 20190425 - 오과금 TO-BE
				msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] 정의되지 않은 구매 타입입니다.[c_buying_type=" + paramVO.getBuyingType() + "]";
				imcsLog.serviceLog(msg, methodName, methodLine);
				resultSet  = -1;
				messageSet = 15;
			}
			
			//-------------------------
			//PAYMENT_ID CHECK(인앱용)
			//-------------------------
			if(resultSet == 0 && "A".equals(paramVO.getBuyingType().toUpperCase()) ){
				tp1 = System.currentTimeMillis();
				
				try {
					Integer nPaymentIdChk	= 0;
										
					nPaymentIdChk	= buyNSContentDAO.chkPaymentId(paramVO);
					paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
					
					
					if(nPaymentIdChk != null && nPaymentIdChk > 0)
						msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] chkPaymentId [CHECK PT_PAYMENT_INFO] table[" + nPaymentIdChk + "] records Success at";
					else {
						msg	= " [SMARTUX.PT_PAYMENT_INFO no data]";
						resultSet	= -1;
						messageSet	= 3;
					}
					imcsLog.serviceLog(msg, methodName, methodLine);						
					
				} catch (Exception e) {
					resultSet	= -1;
					messageSet	= 3;
					
					msg	= " [SMARTUX.PT_PAYMENT_INFO FAILURE]";
					
					imcsLog.serviceLog(msg, methodName, methodLine);						
	
				}
				tp2	= System.currentTimeMillis();
				imcsLog.timeLog("인앱구매 PAYMENT_ID 조회", String.valueOf(tp2 - tp1), methodName, methodLine); 
			}
			
			if(resultSet == 0){
				
				tp1 = System.currentTimeMillis();
				/* 상태, 개통여부 및 쿠폰값 가져오기 */
				paramVO = this.buyContent_Cust_Sel(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
				resultSet = paramVO.getResult_set();
				
				if(resultSet == -1){
					
					tp2	= System.currentTimeMillis();
					imcsLog.timeLog("가입자 상태, 개통여부 조회", String.valueOf(tp2 - tp1), methodName, methodLine); 
					
					messageSet = 10;
				}
							
				if(resultSet == 0){
					
					tp2	= System.currentTimeMillis();
					imcsLog.timeLog("가입자 상태, 개통여부 조회", String.valueOf(tp2 - tp1), methodName, methodLine); 
					
					tp1 = System.currentTimeMillis();
					
					/* 상품 정보 가져오기 */
					this.buyContent_Bill_Type(paramVO);
					paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
					
					tp2	= System.currentTimeMillis();
					imcsLog.timeLog("정액/종량 조회", String.valueOf(tp2 - tp1), methodName, methodLine); 
					
					resultSet = paramVO.getResult_set();
					
					if(resultSet == -1){
						messageSet = 13;
					}
					
					if (block_flag == 1 && !paramVO.getProductType().startsWith("0")) {
						resultSet = -1;
						messageSet = 19;
					}
					
					/* 사용여부 체크 */
					if(!"Y".equals(paramVO.getStatusFlag())){
						resultSet = -1;
						messageSet = 11;
					}
					
					/* 개통여부 체크 */
					if(resultSet == 0){
						
						if( "N".equals(paramVO.getYnVodOpen()) ){
							
							if(!"F".equals(paramVO.getBillType())){
								resultSet = -1;
								messageSet = 12;
							}
							
						}
						
					}
					
				}
				
			}
			
			if(resultSet == 0 && !"F".equals(paramVO.getBillType()) && "N".equals(paramVO.getPkgYn()) ){
				
				tp1 = System.currentTimeMillis();
				/* 가입자가 구입한 상품인지 가져오기 */
				custom_product_chk = this.buyContent_Custom_Product(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
				tp2	= System.currentTimeMillis();
				imcsLog.timeLog("가입자가 구입한 상품 조회", String.valueOf(tp2 - tp1), methodName, methodLine); 
				
				resultSet = paramVO.getResult_set();
				
				if(resultSet == -1){
					messageSet = 14;
				}
				
				if(custom_product_chk == 0){
					if( !"F".equals(paramVO.getBillType()) ){
						resultSet = -1;
						messageSet = 9;
					}
				}
				
			}
			
			if( "1".equals( paramVO.getProductType() ) ){
				
				/*PPV상품*/
				if("SONY".equals(paramVO.getDistributor()) || "SHOWBOX".equals(paramVO.getDistributor())  ){
					
					tp1 = System.currentTimeMillis();
					
					/*이벤트 구매내역 조회*/
					evnt_chk = this.ContentStat_Evnt_Chk(paramVO);
					paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
					
					tp2	= System.currentTimeMillis();
					imcsLog.timeLog("이벤트 구매내역 조회", String.valueOf(tp2 - tp1), methodName, methodLine); 
					
					if("R".equals(paramVO.getBuyingGb())){
						
						//====================================
		                //예약구매의 경우 가격정보 CHECK
		                //====================================
						if(evnt_chk != 0 ){
							if( !paramVO.getReservedPrice().equals(paramVO.getBuyingPrice()) ){
								msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] 이벤트구매존재 - incorrect price[imcs_price=" + paramVO.getReservedPrice() + "][stb_price=" + paramVO.getBuyingPrice() + "]";
								imcsLog.serviceLog(msg, methodName, methodLine);
								
								resultSet = -1;
	                            messageSet = 45;
							}
						}else{
							if( !"0".equals( paramVO.getBuyingPrice() ) ){
								msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] 이벤트비구매 - incorrect price[imcs_price=" + paramVO.getReservedPrice() + "][stb_price=" + paramVO.getBuyingPrice() + "]";
								imcsLog.serviceLog(msg, methodName, methodLine);
								
								resultSet = -1;
	                            messageSet = 45;
							}
						}
						
					}else{
						
						if( "A".equals(paramVO.getBuyingType()) ){
							
							//====================================
		                    //인앱구매의 경우 가격정보 CHECK
		                    //====================================
							if(evnt_chk != 0 ){
								if( !paramVO.getApprovalPrice().equals(paramVO.getBuyingPrice()) ){
									msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] 이벤트구매존재 - incorrect price[imcs_price=" + paramVO.getApprovalPrice() + "][stb_price=" + paramVO.getBuyingPrice() + "]";
									imcsLog.serviceLog(msg, methodName, methodLine);
									
									resultSet = -1;
		                            messageSet = 45;
								}
							}else{
								if( !"0".equals( paramVO.getBuyingPrice() ) ){
									msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] 이벤트비구매 - incorrect price[imcs_price=" + paramVO.getApprovalPrice() + "][stb_price=" + paramVO.getBuyingPrice() + "]";
									imcsLog.serviceLog(msg, methodName, methodLine);
									
									resultSet = -1;
		                            messageSet = 45;
								}
							}
							
						}else{
							
							//====================================
		                    //일반구매의 경우 가격정보 CHECK
		                    //====================================
							if(evnt_chk != 0 ){
								if( !paramVO.getSuggestedPrice().equals(paramVO.getBuyingPrice()) ){
									msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] 이벤트구매존재 - incorrect price[imcs_price=" + paramVO.getSuggestedPrice() + "][stb_price=" + paramVO.getBuyingPrice() + "]";
									imcsLog.serviceLog(msg, methodName, methodLine);
									
									resultSet = -1;
		                            messageSet = 45;
								}
							}else{
								if( !"0".equals( paramVO.getBuyingPrice() ) ){
									msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] 이벤트비구매 - incorrect price[imcs_price=" + paramVO.getSuggestedPrice() + "][stb_price=" + paramVO.getBuyingPrice() + "]";
									imcsLog.serviceLog(msg, methodName, methodLine);
									
									resultSet = -1;
		                            messageSet = 45;
								}
							}
							
						}
					}
				}else{/*PPV상품*/
					if(Integer.parseInt( paramVO.getEventValue() ) > 0){

						if("R".equals(paramVO.getBuyingGb())){
							
							msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] 할인율구매 - price[" + paramVO.getReservedPrice() + " -> " +paramVO.getEventPrice() + "]";
							imcsLog.serviceLog(msg, methodName, methodLine);
							paramVO.setBuyingPrice(paramVO.getEventPrice());
							
						}else{
							
							if( "A".equals(paramVO.getBuyingType()) ){
								
								msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] 할인율구매 - price[" + paramVO.getApprovalPrice() + " -> " +paramVO.getEventPrice() + "]";
								imcsLog.serviceLog(msg, methodName, methodLine);
								paramVO.setBuyingPrice(paramVO.getEventPrice());
								
							}else{
								
								msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] 할인율구매 - price[" + paramVO.getSuggestedPrice() + " -> " +paramVO.getEventPrice() + "]";
								imcsLog.serviceLog(msg, methodName, methodLine);
								paramVO.setBuyingPrice(paramVO.getEventPrice());
								
							}
						}
							
					}else{

						if( "R".equals(paramVO.getBuyingGb()) ){
							
							//====================================
		                    //예약구매의 경우 가격정보 CHECK
		                    //====================================
							if( !paramVO.getReservedPrice().equals( paramVO.getBuyingPrice() ) ){
								msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] 일반PPV - incorrect price[imcs_price=" + paramVO.getReservedPrice() + "][stb_price=" + paramVO.getBuyingPrice() + "]";
								imcsLog.serviceLog(msg, methodName, methodLine);
								
								resultSet = -1;
	                            messageSet = 45;
							}
							
						}else{
							
							if( "A".equals(paramVO.getBuyingType()) ){
								
								if( !paramVO.getApprovalPrice().equals( paramVO.getBuyingPrice() ) ){
									msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] 일반PPV - incorrect price[imcs_price=" + paramVO.getApprovalPrice() + "][stb_price=" + paramVO.getBuyingPrice() + "]";
									imcsLog.serviceLog(msg, methodName, methodLine);
									
									resultSet = -1;
		                            messageSet = 45;
								}
								
							}else{
								
								if( !paramVO.getSuggestedPrice().equals( paramVO.getBuyingPrice() ) ){
									msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] 일반PPV - incorrect price[imcs_price=" + paramVO.getSuggestedPrice() + "][stb_price=" + paramVO.getBuyingPrice() + "]";
									imcsLog.serviceLog(msg, methodName, methodLine);
									
									resultSet = -1;
		                            messageSet = 45;
								}
								
							}
							
							
						}
					}
				}
				
			}else if( "0".equals( paramVO.getProductType() ) ){
				/*FVOD상품*/
				if(!"0".equals(paramVO.getBuyingPrice())){
					msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] FVOD - incorrect price[imcs_price=0][stb_price=" + paramVO.getBuyingPrice() + "]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					
					resultSet = -1;
                    messageSet = 45;
				}
			}
			
			if(resultSet == 0 && !"0".equals(paramVO.getProductType())){
				
				tp1	= System.currentTimeMillis();
				if("R".equals(paramVO.getBuyingGb())){
					duplic_chk = this.buyContent_Dup_Chk_R(paramVO);
				}else{
					duplic_chk = this.buyContent_Dup_Chk(paramVO);
				}
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
				resultSet = paramVO.getResult_set();
				
				if(resultSet == -1){
					messageSet = 22;
				}
				//}
				/* 기존에 데이타가 있으면 */
				if(duplic_chk > 0){
					resultSet = -1;
					messageSet = 24;
				}
				
				tp2	= System.currentTimeMillis();
				imcsLog.timeLog("기존 구매내역 조회", String.valueOf(tp2 - tp1), methodName, methodLine); 
			}
			
			if(resultSet == 0){
				
				/* PKG구매 */
				if("Y".equals(paramVO.getPkgYn())){
					
					tp1	= System.currentTimeMillis();
					
					paramVO.setContsGenre("패키지");
										
					List<BuyNSContentResponseVO> getPkgContent = new ArrayList<BuyNSContentResponseVO>();
					BuyNSContentResponseVO lst_pkgContent = new BuyNSContentResponseVO();
					
					iMainCount = 0;
					
					try {
						getPkgContent = this.getPkgContent(paramVO);
						paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
					} catch (Exception e) {
						paramVO.setResult_set(-1);
					}
					
					resultSet = paramVO.getResult_set();
					
					if(resultSet == -1){
						messageSet = 30;
					}
					
					if(getPkgContent != null){
						iMainCount = getPkgContent.size();
					}
					
					if(iMainCount == 0){
						resultSet = -1;
						messageSet = 31;
						
					}
					
					for(int i=0; i<iMainCount; i++){
						
						lst_pkgContent = getPkgContent.get(i);
						
						Integer result = 0;
						
						paramVO.setProductId2(lst_pkgContent.getProductId());
						paramVO.setContsId2(lst_pkgContent.getContsId());
						paramVO.setContsName2(lst_pkgContent.getContsName());
						paramVO.setContsGenre2(lst_pkgContent.getContsGenre());
						
						/* 보관함 : 해당 Contents를 Fetch하여 insert */
				    	result = this.buyContent_Ins3(paramVO); // 저장하고 buy_detail
				    	paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				    	
				    	if(result == 1){
				    		resultSet = 0;
				    	}else{
				    		resultSet = -1;
				    	}
				    	
				    	if(resultSet == -1){
				    		resultSet = -1;
		                    messageSet = 32;
		                    
		                    if(paramVO.getSqlCode() == -1)
								messageSet	= 24;
				    	}
												
					}
					
					tp2	= System.currentTimeMillis();
					imcsLog.timeLog("패키지 컨덴츠 보관함 저장", String.valueOf(tp2 - tp1), methodName, methodLine); 
					
				}else if("N".equals(paramVO.getPkgYn())){/* 단품구매 */
					
					tp1	= System.currentTimeMillis();
					
					/* 장르값 가져오기 */
					this.buyContent_Genre_Sel(paramVO);
					paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
					
					tp2	= System.currentTimeMillis();
					imcsLog.timeLog("단품 컨텐츠 장르 조회", String.valueOf(tp2 - tp1), methodName, methodLine); 
					
					resultSet = paramVO.getResult_set();
					
					if(resultSet == -1){
						messageSet = 90;
					}
					
					tp1	= System.currentTimeMillis();
					
					Integer result = 0;
					
					result = this.buyContent_Ins4(paramVO);
					paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			    	
			    	if(result == 1){
			    		resultSet = 0;
			    	}else{
			    		resultSet = -1;
			    	}
			    	
			    	if(resultSet == -1){
			    		resultSet = -1;
	                    messageSet = 32;
	                    
	                    if(paramVO.getSqlCode() == -1)
							messageSet	= 24;
			    	}
					
					tp2	= System.currentTimeMillis();
					imcsLog.timeLog("단품 컨텐츠 보관함 저장", String.valueOf(tp2 - tp1), methodName, methodLine); 
				}
								
			} 
			
			if(paramVO.getContsGenre() == null || "".equals(paramVO.getContsGenre())){
				paramVO.setContsGenre("기타");
			}
			
			if(resultSet == 0){
				
				tp1	= System.currentTimeMillis();
				
				/* 구매내역 생성 */
				if("N".equals(paramVO.getPkgYn().toUpperCase())){
					
					Integer result = 0;
					
					if("R".equals(paramVO.getBuyingGb())){
						result = this.buyContent_Ins1_R(paramVO);
					}else{
						result = this.buyContent_Ins1(paramVO);
					}
					paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
					
			    	if(result == 1){
			    		resultSet = 0;
			    	}else{
			    		resultSet = -1;
			    	}
			    	
			    	if(resultSet == -1){
			    		resultSet = -1;
	                    messageSet = 40;
			    	}
										
				}else{
					
					Integer result = 0;
					
					if("R".equals(paramVO.getBuyingGb())){
						result = this.buyContent_Ins2_R(paramVO);
					}else{
						result = this.buyContent_Ins2(paramVO);
					}
					paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
					
			    	if(result == 1){
			    		resultSet = 0;
			    	}else{
			    		resultSet = -1;
			    	}
			    	
			    	if(resultSet == -1){
			    		resultSet = -1;
	                    messageSet = 40;
			    	}
					
				}
				
				tp2	= System.currentTimeMillis();
				imcsLog.timeLog("구매내역 생성 완료", String.valueOf(tp2 - tp1), methodName, methodLine); 
				
			}
			
			ComCpnVO cpnInfoVO = new ComCpnVO();
			
			if(resultSet == 0){
				// 장르 정보 조회
				String szGenreInfo	= "";
				tp1	= System.currentTimeMillis();
				try {
					szGenreInfo =	this.getGenreInfo(paramVO);
					paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
					paramVO.setGenreInfo(szGenreInfo);
				} catch(Exception e) {
					tp2	= System.currentTimeMillis();
					imcsLog.timeLog("장르 정보 조회 실패", String.valueOf(tp2 - tp1), methodName, methodLine);
				}
				resultSet = paramVO.getResultSet();
				msg	 = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] select genre[" + szGenreInfo + "]";
				imcsLog.serviceLog(msg, methodName, methodLine);
				
//				//2019.03.05 - 지상파 서비스 종료로 인해 KBS, SBS, MBC 컨텐츠 순차적으로 비노출 처리
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
				
				//인앱의 경우 쿠폰 관련 조회 하지 않음
				if(!"A".equals(paramVO.getCpUseYn())){							
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
			
			
			/* 리턴값을 지정하여 리턴처리 */
		    if(resultSet == 0){
		    	tp1	= System.currentTimeMillis();
		    	
		    	resultVO.setFlag("0");
		    	resultVO.setBuyingDate(paramVO.getBuyingDate());
		    	resultVO.setCpnInfoVO(cpnInfoVO);
		    	resultVO.setErrMsg("insert success");
		    	
		    	tp2	= System.currentTimeMillis();
		    	imcsLog.timeLog("EXEC SQL COMMIT", String.valueOf(tp2 - tp1), methodName, methodLine); 
		    }else{
		    			    	
		    	resultVO.setFlag("1");
		    	resultVO.setBuyingDate(paramVO.getBuyingDate());
		    	
		    	throw new ImcsException();
			    
		    }	    
		    
		}catch(ImcsException ie) {
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
			
			
			msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] msg[" + errMsg + "]";
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
			
			msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] return[" + resultVO.getFlag() + "|" + resultVO.getErrMsg() + "|" + resultVO.getBuyingDate() + "|" + resultVO.getErrCode() + "|]";
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
						
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultVO;
	}
	
	public String getSysdate() throws Exception{
		
		String result = buyNSContentDAO.getSysdate();		 
		
		return result;
	}
    
    public BuyNSContentRequestVO buyContent_Cust_Sel( BuyNSContentRequestVO paramVO){
    	
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
    	
    	String buying_date = "";
    	
    	try {
    		buying_date = this.getSysdate();
    	} catch (Exception e) {
    		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
    		imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage() + "," + methodName + "_getSysdate()");
			throw new ImcsException(ImcsConstants.FAIL_CODE, e);
		}
    	
    	paramVO.setBuyingDate(buying_date);
    	
    	List<BuyNSContentRequestVO> sbcInfo = new ArrayList<BuyNSContentRequestVO>();
    	
    	int iMainCount = 0;
    	
    	try {
    		sbcInfo = this.getSbcInfo(paramVO);
		} catch (Exception e) {
			paramVO.setResult_set(-1);
		}
    	
    	
    	if(sbcInfo != null){
    		iMainCount = sbcInfo.size(); 
    	}
    	
    	if(iMainCount == 0){
    		paramVO.setResult_set(-1);
    	}
    	
    	for(int i =0; i<iMainCount; i++){
    		
    		paramVO.setStatusFlag(sbcInfo.get(i).getStatusFlag());
    		paramVO.setYnVodOpen(sbcInfo.get(i).getYnVodOpen());
    		paramVO.setCpCnt(sbcInfo.get(i).getCpCnt());
    		
    		if (!sbcInfo.get(i).getBlockflag().equals("N")) {
    			block_flag = 1;
    		}
   			//if (!sbcInfo.get(i).getBlockflag().equals("N"))
    		//	paramVO.setBlockflag(sbcInfo.get(i).getBlockflag());
    	}
    	    	
    	return paramVO;
    }
    
    public void buyContent_Bill_Type( BuyNSContentRequestVO paramVO){
    	    	    	
    	List<BuyNSContentRequestVO> billtype = new ArrayList<BuyNSContentRequestVO>();
    	
    	BuyNSContentRequestVO tempVO = new BuyNSContentRequestVO();
    	
    	int iMainCount = 0;
    	
    	try {
    		billtype = this.getBillType(paramVO);
		} catch (Exception e) {
			paramVO.setResult_set(-1);
		}
    	
    	
    	if(billtype != null){
    		iMainCount = billtype.size(); 
    	}
    	
    	if(iMainCount == 0){
    		paramVO.setResult_set(-1);
    	}
    	
    	if(iMainCount > 0){
    		
    		tempVO = billtype.get(0);
    		
    		paramVO.setResult_set(0);
    		
    		paramVO.setBillType(tempVO.getBillType());
    		paramVO.setSuggestedPrice(tempVO.getSuggestedPrice());
    		paramVO.setApprovalPrice(tempVO.getApprovalPrice());
    		paramVO.setDistributor(tempVO.getDistributor());
    		paramVO.setProductType(tempVO.getProductType());
    		paramVO.setEventValue(tempVO.getEventValue());
    		paramVO.setEventPrice(tempVO.getEventPrice());  
    		paramVO.setReservedDate(tempVO.getReservedDate());
    		paramVO.setReservedPrice(tempVO.getReservedPrice());
    		
    	}
    }
    
    public int buyContent_Custom_Product( BuyNSContentRequestVO paramVO){
    	
    	Integer custom_product_chk = null;
    	    	
    	try {
    		custom_product_chk = this.CustomProductChk(paramVO);
		} catch (Exception e) {
			paramVO.setResult_set(-1);
		}
    	
    	if(custom_product_chk == null){
    		custom_product_chk = 0;
    	}
    	
    	
    	return custom_product_chk;
    }
    
    public List<BuyNSContentRequestVO> getSbcInfo(BuyNSContentRequestVO vo){

    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();    	
    	
    	String sqlId =  "lgvod109_001_20171214_001";
		
		int querySize = 0;
		
		List<BuyNSContentRequestVO> list   = null;
		BuyNSContentRequestVO resultVO = null;
		
		String szMsg = "";
		
		try {
			
			try {
				list = buyNSContentDAO.getSbcInfo(vo);
				vo.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = vo.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;

			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if( list == null ||list.isEmpty()){
				querySize = 0;
				list = null;
			} else 
			{
				querySize = list.size();
			}
			
						
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, vo.getSaId(), vo.getStbMac(), vo.getPid());		
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			throw new ImcsException(ImcsConstants.FAIL_CODE, e);
		}
		
    	return list;
    }
    
    public List<BuyNSContentRequestVO> getBillType(BuyNSContentRequestVO vo){

    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();    	
    	
    	String sqlId =  "lgvod109_002_20171214_001";
		
		int querySize = 0;
		
//		NosqlResultCache cache = new NosqlResultCache();
//		List<Object> binds = new ArrayList<Object>();
//		RowKeyList rowKeys = new RowKeyList();
//		VersionUpdateCheckKey checkKey = new VersionUpdateCheckKey();
		
		List<BuyNSContentRequestVO> list   = null;
		BuyNSContentRequestVO resultVO = null;
		
		String szMsg = "";
		
		try {
//			rowKeys.setSqlId(sqlId);
//			rowKeys.addRowKeys(vo.getPkgYn());
//			rowKeys.addRowKeys(vo.getAlbumId());
//			
//			checkKey.addVersionTuple("PT_PD_PACKAGE");
//			checkKey.addVersionTuple("PT_PD_PACKAGE_DETAIL", vo.getAlbumId());
//			checkKey.addVersionTuple("PT_LA_ALBUM_MST", vo.getAlbumId());
//			
//			binds.add(vo);
//			
//			list = cache.getCachedResult(new CacheableExecutor<BuyNSContentRequestVO>() {
//				@Override
//				public List<BuyNSContentRequestVO> execute(List<Object> param) throws SQLException {
//					try{
//						BuyNSContentRequestVO requestVO = (BuyNSContentRequestVO)param.get(0);
//						List<BuyNSContentRequestVO> rtnList = null;
//						try {
//							rtnList = buyNSContentDAO.getBillType(requestVO);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//												
//						return rtnList;
//						
//					}catch(DataAccessException e){
//						//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
//						throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
//					}
//				}
//				
//				@Override
//				public Class<BuyNSContentRequestVO> getReturnType() {
//					return BuyNSContentRequestVO.class;
//				}
//			}, binds, rowKeys, checkKey);
			
			try{
				try {
					list = buyNSContentDAO.getBillType(vo);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list == null ||list.isEmpty()){
				querySize = 0;
				list = null;
			} else 
			{
				querySize = list.size();
			}
			
			//vo.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
						
		} catch (Exception e) {
			//szMsg	 = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] SQLID[" + sqlId + "] sts["+cache.getLastException().getErrorCode()+"]" 
			//		+ String.format("%-21s", "msg[bill_type" + cache.getLastException().getErrorMessage() + "]");
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, vo.getSaId(), vo.getStbMac(), vo.getPid());	
			imcsLog.serviceLog(szMsg, methodName, methodLine);
		}
		
    	return list;
    }
    
    public int CustomProductChk(BuyNSContentRequestVO vo){

    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId =  "lgvod109_003_20171214_001";
		
		int querySize = 0;
		
//		NosqlResultCache cache = new NosqlResultCache();
//		List<Object> binds = new ArrayList<Object>();
//		RowKeyList rowKeys = new RowKeyList();
//		VersionUpdateCheckKey checkKey = new VersionUpdateCheckKey();
		
		List<Integer> list   = null;
		Integer resultVO = 0;
		
		String szMsg = "";
		
		try {
//			rowKeys.setSaId(vo.getSaId());
//			rowKeys.setStbMac(vo.getStbMac());
//			rowKeys.setSqlId(sqlId);
//			rowKeys.addRowKeys(vo.getAlbumId());
//			
//			checkKey.addVersionTuple("PT_VO_CUSTOM_PRODUCT", vo.getSaId());
//			checkKey.addVersionTuple("PT_PD_PACKAGE");
//			checkKey.addVersionTuple("PT_PD_PACKAGE_DETAIL", vo.getAlbumId());
//			checkKey.addVersionTuple("PT_PD_PACKAGE_RELATION");
//			
//			binds.add(vo);
//			
//			list = cache.getCachedResult(new CacheableExecutor<Integer>() {
//				@Override
//				public List<Integer> execute(List<Object> param) throws SQLException {
//					try{
//						BuyNSContentRequestVO requestVO = (BuyNSContentRequestVO)param.get(0);
//						List<Integer> rtnList  = null;
//						try {
//							rtnList = buyNSContentDAO.CustomProductChk(requestVO);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//						
//						//List<Integer> rtnList  = null;
//						
//						if( rtnList == null ){
//							rtnList  = new ArrayList<Integer>();
//							rtnList.add(0);
//						}		
//						
//						return rtnList;
//						
//					}catch(DataAccessException e){
//						//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
//						throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
//					}
//				}
//				
//				@Override
//				public Class<Integer> getReturnType() {
//					return Integer.class;
//				}
//			}, binds, rowKeys, checkKey);
			
			try{
				try {
					list = buyNSContentDAO.CustomProductChk(vo);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list == null ||list.isEmpty()){
				querySize = 0;
				resultVO = 0;
			} else 
			{
				querySize = list.size();
				resultVO = (Integer)list.get(0);
			}
			
			//vo.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
			
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, vo.getSaId(), vo.getStbMac(), vo.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			throw new ImcsException(ImcsConstants.FAIL_CODE, e);
		}
		
    	return resultVO;    	
    }
    
    public Integer buyContent_Dup_Chk_R(BuyNSContentRequestVO vo){

    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
    	
		List<HashMap<String, Object>> lBuyDupChk = new ArrayList<HashMap<String, Object>>();
		
		HashMap<String, Object> mBuyDupChk = new HashMap<String, Object>();
		
		
		Integer duplic_chk = 0;
		
    	try {
    		
    		lBuyDupChk = buyNSContentDAO.buyContentDupChkR(vo);
    		
    		if(lBuyDupChk != null && lBuyDupChk.size() > 0){
    			mBuyDupChk = lBuyDupChk.get(0);
    			
    			vo.setBuyDate((String)mBuyDupChk.get("buy_date"));
    			duplic_chk = (Integer)mBuyDupChk.get("duplic_chk");
    		}
    		
		} catch (Exception e) {			
			vo.setResult_set(-1);
			
			vo.setResultCode("40000000");
		}
    	
    	if(duplic_chk == null){
    		duplic_chk = 0;
    	}
    	
    	return duplic_chk;
    }
    
    public Integer buyContent_Dup_Chk(BuyNSContentRequestVO vo){

    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
    			
		List<HashMap<String, Object>> lBuyDupChk = new ArrayList<HashMap<String, Object>>();
		
		HashMap<String, Object> mBuyDupChk = new HashMap<String, Object>();
		
		Integer duplic_chk = 0;
		
    	try {
    		
    		if( "0".equals(vo.getProductType()) ){
    			
    			// 이 쿼리도 동일한데 왜 분리했는지??? 우선은 그냥 놔둠
    			if( "R".equals(vo.getApplType()) ){
    				lBuyDupChk= buyNSContentDAO.buyContentDupChk1(vo);
    			}else{
    				lBuyDupChk= buyNSContentDAO.buyContentDupChk1(vo);
    			}
    			
    		}else if( "1".equals(vo.getProductType()) ){
    			
    			// 이 쿼리도 동일한데 왜 분리했는지??? 우선은 그냥 놔둠
    			if( "R".equals(vo.getApplType()) ){
    				lBuyDupChk= buyNSContentDAO.buyContentDupChk2(vo);
    			}else{
    				lBuyDupChk= buyNSContentDAO.buyContentDupChk2(vo);
    			}
    			
    		}else if( "2".equals(vo.getProductType()) ){
    			
    			// 이 쿼리도 동일한데 왜 분리했는지??? 우선은 그냥 놔둠
    			if( "R".equals(vo.getApplType()) ){
    				lBuyDupChk= buyNSContentDAO.buyContentDupChk3(vo);
    			}else{
    				lBuyDupChk= buyNSContentDAO.buyContentDupChk3(vo);
    			}
    			
    		}
    		
    		if(lBuyDupChk != null && lBuyDupChk.size() > 0){
    			mBuyDupChk = lBuyDupChk.get(0);
    			
    			vo.setBuyDate((String)mBuyDupChk.get("buy_date"));
    			duplic_chk = (Integer)mBuyDupChk.get("duplic_chk");
    		}
    		
    		
		} catch (Exception e) {			
			vo.setResult_set(-1);
			
			vo.setResultCode("40000000");
		}
    	
    	if(duplic_chk == null){
    		duplic_chk = 0;
    	}
    	
    	return duplic_chk;
    }
    
    public Integer ContentStat_Evnt_Chk(BuyNSContentRequestVO vo){
    	
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		
		Integer evnt_chk = 0;
		
		String szMsg = "";
		
    	try {
    		
    		if("R".equals( vo.getApplType() )){
    			evnt_chk= buyNSContentDAO.ContentStatEvntChk(vo);
    		}else{
    			evnt_chk= buyNSContentDAO.ContentStatEvntChk(vo);
    		}
    		
    		//imcsLog.dbLog(ImcsConstants.API_PRO_ID108, "lgvod108_040", null, evnt_chk, methodName, methodLine);
		} catch (Exception e) {
			vo.setResult_set(-1);
			
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] sts [SQL_CODE]"
					+ String.format("%-27s", " msg[" + ImcsConstants.RCV_MSG6 + "]");	
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, vo.getSaId(), vo.getStbMac(), vo.getPid());	
			imcsLog.serviceLog(szMsg, methodName, methodLine);
		}
    	
    	if(evnt_chk == null){
    		evnt_chk = 0;
    	}
    	
    	return evnt_chk;
    }
    
    
    public List<BuyNSContentResponseVO> getPkgContent(BuyNSContentRequestVO vo){
    	
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();    	
    	
    	String sqlId =  "lgvod109_009_20171214_001";
		int querySize = 0;		
		List<BuyNSContentResponseVO> list   = null;
		BuyNSContentResponseVO resultVO = null;
		String szMsg = "";
		
		try {
			
			
			try{
				try {
					list = buyNSContentDAO.getPkgContent(vo);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list == null ||list.isEmpty()){
				querySize = 0;
				list = null;
			} else 
			{
				querySize = list.size();
			}
									
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, vo.getSaId(), vo.getStbMac(), vo.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			throw new ImcsException(ImcsConstants.FAIL_CODE, e);
		}
		
    	return list;
    }
    
    public Integer buyContent_Ins3( BuyNSContentRequestVO vo){
    	
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId =  "lgvod109_i01_20171214_001";
    	Integer querySize = 0;		
		String szMsg = "";
		    	
    	try {
			
    		
			try{
				querySize = buyNSContentDAO.buyContentIns3(vo);
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			/*try {
				imcsLog.dbLog(ImcsConstants.API_PRO_ID109, sqlId, cache, querySize, methodName, methodLine);
				
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] insert [PT_VO_BUY_DETAIL] table[" + querySize + "] records Success at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			} catch (Exception e) {}*/
			
		} catch (Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] insert [PT_VO_BUY_DETAIL] table Failed at";
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, vo.getSaId(), vo.getStbMac(), vo.getPid());	
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			//szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] sts [" + cache.getLastException().getErrorCode() + "]"
			//		+ String.format("%-27s", " msg[fail info:" + cache.getLastException().getErrorMessage() + "]");	
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			//vo.setSqlCode(cache.getLastException().getErrorCode());
		}
    	
    	/*try {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] sts[SQL_CODE] msg[패키지별로 검색한 컨덴츠 내역 저장]";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
		} catch (Exception e) {}
		*/
		return querySize;
	}
    
    public void buyContent_Genre_Sel( BuyNSContentRequestVO paramVO){
    	
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
    	    	
    	List<BuyNSContentRequestVO> genretype = new ArrayList<BuyNSContentRequestVO>();
    	
    	BuyNSContentRequestVO tempVO = new BuyNSContentRequestVO();
    	
    	int iMainCount = 0;
    	
    	try {
    		genretype = this.getGenreType(paramVO);
		} catch (Exception e) {
			paramVO.setResult_set(-1);
		}
    	
    	
    	if(genretype != null){
    		iMainCount = genretype.size(); 
    	}
    	
    	/*if(iMainCount == 0){
    		rd1.setResult_set(-1);
    	}*/
    	
    	if(iMainCount > 0){
    		
    		tempVO = genretype.get(0);
    		
    		paramVO.setResult_set(0);
    		
    		paramVO.setContsGenre(tempVO.getContsGenre());
    		paramVO.setEventType(tempVO.getEventType());	
    		
    	}
    }
    
    public List<BuyNSContentRequestVO> getGenreType(BuyNSContentRequestVO vo){

    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();    	
    	
    	String sqlId =  "lgvod109_010_20171214_001";
		int querySize = 0;		
		List<BuyNSContentRequestVO> list   = null;
		BuyNSContentRequestVO resultVO = null;
		String szMsg = "";
		
		try {
			
			try {
				list = buyNSContentDAO.getGenreType(vo);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if( list == null ||list.isEmpty()){
				querySize = 0;
				list = null;
			} else 
			{
				querySize = list.size();
			}
			
						
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, vo.getSaId(), vo.getStbMac(), vo.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			throw new ImcsException(ImcsConstants.FAIL_CODE, e);
		}
		
    	return list;
    }
    
    public Integer buyContent_Ins4(BuyNSContentRequestVO vo){
    	
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, vo.getSaId(), vo.getStbMac(), vo.getPid());	
    	
    	String sqlId =  "";
    	
    	if( "0".equals(vo.getProductType()) ){
    		sqlId =  "lgvod109_i02_20171214_001";
    	}else{
    		sqlId =  "lgvod109_i03_20171214_001";
    	}
		
    	Integer querySize = 0;
		String szMsg = "";
		    	
    	try {
			
			
			try{
				querySize = buyNSContentDAO.buyContentIns4(vo);
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
    		
			try {
//				imcsLog.dbLog(ImcsConstants.API_PRO_ID109, sqlId, null, querySize, methodName, methodLine);
				
				if( "0".equals(vo.getProductType()) ){
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] insert [PT_VO_BUY_DETAIL_FVOD_NSC] table[" + querySize + "] records Success at";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
				}else{
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] insert [PT_VO_BUY_DETAIL_NSC] table[" + querySize + "] records Success at";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
				}	
				
			} catch (Exception e) {}
			
		} catch (Exception e) {
			
			if( "0".equals(vo.getProductType()) ){
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] insert [PT_VO_BUY_DETAIL_FVOD_NSC] table Failed at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			}else{
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] insert [PT_VO_BUY_DETAIL_NSC] table Failed at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			}			
			
			//szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] sts [" + cache.getLastException().getErrorCode() + "]"
			//		+ String.format("%-27s", " msg[fail info:" + cache.getLastException().getErrorMessage() + "]");	
			imcsLog.serviceLog(szMsg, methodName, methodLine);
		}
		
		return querySize;
	}
    
    public Integer buyContent_Ins1_R(BuyNSContentRequestVO paramVO){
    	
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());		
    	
    	String sqlId = "lgvod109_j05_20171214_001";
    	Integer querySize = 0;
		String szMsg = "";
		
		if("1".equals(paramVO.getProductType())){
			
			try {
				
				
				try{
					querySize = buyNSContentDAO.buyContentIns1R(paramVO);
				}catch(DataAccessException e){
					//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
					throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
				}
				
				try {
//					imcsLog.dbLog(ImcsConstants.API_PRO_ID109, sqlId, null, querySize, methodName, methodLine);
					
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] insert [PT_VO_BUY_NSC] table[" + querySize + "] records Success at";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
					
				} catch (Exception e) {}
				
			} catch (Exception e) {
				
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] insert [PT_VO_BUY_NSC] table Failed at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);	
				
				//szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] sts [" + cache.getLastException().getErrorCode() + "]"
				//		+ String.format("%-27s", " msg[fail info:" + cache.getLastException().getErrorMessage() + "]");	
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			}
			
		}
		
		return querySize;
	}
    
    public Integer buyContent_Ins1(BuyNSContentRequestVO vo){
    	
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, vo.getSaId(), vo.getStbMac(), vo.getPid());	
    	
    	String sqlId =  "";
    	
    	if( "0".equals(vo.getProductType()) ){
    		sqlId =  "lgvod109_i04_20171214_001";
    	}else{
    		sqlId =  "lgvod109_i05_20171214_001";
    	}
		
    	Integer querySize = 0;		
		String szMsg = "";
		    	
    	try {
			

			try{
				querySize = buyNSContentDAO.buyContentIns1(vo);
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
    		
			try {
//				imcsLog.dbLog(ImcsConstants.API_PRO_ID109, sqlId, null, querySize, methodName, methodLine);
				
				if( "0".equals(vo.getProductType()) ){
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] insert [PT_VO_BUY_FVOD_NSC] table[" + querySize + "] records Success at";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
				}else{
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] insert [PT_VO_BUY_NSC] table[" + querySize + "] records Success at";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
				}	
			} catch (Exception e) {}
			
		} catch (Exception e) {
			
			if( "0".equals(vo.getProductType()) ){
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] insert [PT_VO_BUY_FVOD_NSC] table Failed at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			}else{
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] insert [PT_VO_BUY_NSC] table Failed at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			}			
			
			//szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] sts [" + cache.getLastException().getErrorCode() + "]"
			//		+ String.format("%-27s", " msg[fail info:" + cache.getLastException().getErrorMessage() + "]");	
			imcsLog.serviceLog(szMsg, methodName, methodLine);
		}
		
		return querySize;
	}
    
    public Integer buyContent_Ins2_R( BuyNSContentRequestVO rd1){
    	
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
    	    	
    	List<BuyNSContentResponseVO> getProduct = new ArrayList<BuyNSContentResponseVO>();
    	
    	BuyNSContentResponseVO lst_package = new BuyNSContentResponseVO();
    	
    	Integer result = -1;
    	
    	int iMainCount = 0;
    	int iUpdCount = 0;
    	
    	try {
    		getProduct = this.getProductR(rd1);
		} catch (Exception e) {						
			return -1;
		}
    	
    	
    	if(getProduct != null){
    		iMainCount = getProduct.size(); 
    		
    	}
    	if(iMainCount > 0){    		
    		
    		lst_package = getProduct.get(0);	    
    		
    		rd1.setProductId1(lst_package.getProductId());
    		rd1.setProductName1(lst_package.getProductName());
    		rd1.setProductPrice1(lst_package.getProductPrice());
    		rd1.setExpiredDate1(lst_package.getExpiredDate());
    		
    	}
    	
    	try {
			result = this.buyContentIns2R(rd1);
		} catch (Exception e) {
			result = -1;
		}
    	
    	return result;
    }
    
    public Integer buyContent_Ins2( BuyNSContentRequestVO rd1){
    	
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
    	    	
    	List<BuyNSContentResponseVO> getProduct = new ArrayList<BuyNSContentResponseVO>();
    	
    	BuyNSContentResponseVO lst_package = new BuyNSContentResponseVO();
    	
    	Integer result = -1;
    	
    	int iMainCount = 0;
    	int iUpdCount = 0;
    	
    	try {
    		getProduct = this.getProduct(rd1);
		} catch (Exception e) {						
			return -1;
		}
    	
    	
    	if(getProduct != null){
    		iMainCount = getProduct.size(); 
    	}
    	
    	if(iMainCount > 0){    		
    		
    		lst_package = getProduct.get(0);	    
    		
    		rd1.setProductId1(lst_package.getProductId());
    		rd1.setProductName1(lst_package.getProductName());
    		rd1.setProductPrice1(lst_package.getProductPrice());
    		rd1.setExpiredDate1(lst_package.getExpiredDate());
    		
    	}
    	
    	try {
			result = this.buyContentIns2(rd1);
		} catch (Exception e) {
			result = -1;
		}
    	
    	return result;
    }
    
    public List<BuyNSContentResponseVO> getProductR(BuyNSContentRequestVO vo){

    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();    	
    	
    	String sqlId =  "lgvod109_021_20171214_001";
		int querySize = 0;		
		List<BuyNSContentResponseVO> list   = null;
		BuyNSContentResponseVO resultVO = null;
		
		String szMsg = "";
		
		try {
			
			
			try{
				try {
					list = buyNSContentDAO.getProductR(vo);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list == null ||list.isEmpty()){
				querySize = 0;
				list = null;
			} else 
			{
				querySize = list.size();
			}
			
			
			try{
//				imcsLog.dbLog(ImcsConstants.API_PRO_ID109, sqlId, null, querySize, methodName, methodLine);
			}catch(Exception e){}
									
		} catch (Exception e) {
			//szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] sts [" + cache.getLastException().getErrorCode() + "]"
			//		+ String.format("%-27s", " msg[pkg_prod_info info:" + cache.getLastException().getErrorMessage() + "]");	
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, vo.getSaId(), vo.getStbMac(), vo.getPid());	
			imcsLog.serviceLog(szMsg, methodName, methodLine);
		}
		
    	return list;
    }
    
    public List<BuyNSContentResponseVO> getProduct(BuyNSContentRequestVO vo){

    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();    	
    	
    	String sqlId =  "lgvod109_011_20171214_001";
		int querySize = 0;		
		List<BuyNSContentResponseVO> list   = null;
		BuyNSContentResponseVO resultVO = null;
		
		String szMsg = "";
		
		try {
			
			
			try{
				try {
					list = buyNSContentDAO.getProduct(vo);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list == null ||list.isEmpty()){
				querySize = 0;
				list = null;
			} else 
			{
				querySize = list.size();
			}
			
			try{
//				imcsLog.dbLog(ImcsConstants.API_PRO_ID109, sqlId, null, querySize, methodName, methodLine);
			}catch(Exception e){}
									
		} catch (Exception e) {
			//szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] sts [" + cache.getLastException().getErrorCode() + "]"
			//		+ String.format("%-27s", " msg[pkg_prod_info info:" + cache.getLastException().getErrorMessage() + "]");	
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, vo.getSaId(), vo.getStbMac(), vo.getPid());	
			imcsLog.serviceLog(szMsg, methodName, methodLine);
		}
		
    	return list;
    }
    
    public Integer buyContentIns2R(BuyNSContentRequestVO vo){

    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, vo.getSaId(), vo.getStbMac(), vo.getPid());	
    	
    	String sqlId =  "lgvod109_j06_20171214_001";
    	Integer querySize = 0;		
		String szMsg = "";
		    	
    	try {
			
    		
			try{
				querySize = buyNSContentDAO.buyContentIns2R(vo);
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			try {
//				imcsLog.dbLog(ImcsConstants.API_PRO_ID109, sqlId, null, querySize, methodName, methodLine);
				
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] insert [PT_VO_BUY_NSC] table[" + querySize + "] records Success at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
				
			} catch (Exception e) {}
			
		} catch (Exception e) {
			
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] insert [PT_VO_BUY_NSC] table Failed at";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
		
			//szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] sts [" + cache.getLastException().getErrorCode() + "]"
			//		+ String.format("%-27s", " msg[fail info:" + cache.getLastException().getErrorMessage() + "]");	
			imcsLog.serviceLog(szMsg, methodName, methodLine);
		}
		
		return querySize;
	}
    
    public Integer buyContentIns2(BuyNSContentRequestVO vo){
    	
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, vo.getSaId(), vo.getStbMac(), vo.getPid());	
    	
    	String sqlId =  "lgvod109_i06_20171214_001";
    	Integer querySize = 0;
		String szMsg = "";
		    	
    	try {
			
    		
			try{
				querySize = buyNSContentDAO.buyContentIns2(vo);
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			try {
//				imcsLog.dbLog(ImcsConstants.API_PRO_ID109, sqlId, null, querySize, methodName, methodLine);
				
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] insert [PT_VO_BUY_NSC] table[" + querySize + "] records Success at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
				
			} catch (Exception e) {}
			
		} catch (Exception e) {
			
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] insert [PT_VO_BUY_NSC] table Failed at";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
		
			//szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] sts [" + cache.getLastException().getErrorCode() + "]"
			//		+ String.format("%-27s", " msg[fail info:" + cache.getLastException().getErrorMessage() + "]");	
			imcsLog.serviceLog(szMsg, methodName, methodLine);
		}
		
		return querySize;
	}
    
    public String getGenreInfo(BuyNSContentRequestVO paramVO) throws Exception {
    	
 
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		
		//String sqlId		= "nsvod010_p01_20170816_001";
		String sqlId		= "lgvod109_p01_20171214_001";
		String szGenreInfo	= "";
		List<GenreInfoVO> list	= new ArrayList<GenreInfoVO>();
		GenreInfoVO genreVO		= null;
		
		try {
			
			try {
				list = buyNSContentDAO.getGenreInfo(paramVO);
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
	public ComCpnVO getCpnInfo(BuyNSContentRequestVO paramVO) throws Exception {

		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName	= oStackTrace.getMethodName();
		String methodLine	= String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		long tp1, tp2	= 0;
		
		ComCpnVO rtnCpnInfoVO = new ComCpnVO();
		ComCpnVO cpnInfoVO = new ComCpnVO();
		
		//2018.12.07 - VR앱 : VR앱으로 조회 하는 경우 스탬프/쿠폰 정보를 조회하지 않는다.
		if(!(paramVO.getApplType().equals("") || paramVO.getApplType() == null) && paramVO.getApplType().substring(0, 1).equals("E"))
		{
			paramVO.setSystemGb("4");
			paramVO.setScreenType("VR");
		}
		else
		{
			paramVO.setSystemGb("2");
			paramVO.setScreenType("NSC");
		}
		
		String szMsg = " svc[" + ImcsConstants.API_PRO_ID109 + "] START smartux info : p_idx_sa["+paramVO.getpIdxSa()+"] album_id["+paramVO.getAlbumId()+"] product["+paramVO.getProductType()+"] "
    			+ "price["+paramVO.getSuggestedPrice()+"] genre["+paramVO.getGenreInfo()+"] pkg["+paramVO.getPkgYn()+"]";
		imcsLog.serviceLog(szMsg, methodName, methodLine);
		
		// 발급가능쿠폰 정보 조회
		tp1	= System.currentTimeMillis();
		try{
			cpnInfoVO	= buyNSContentDAO.getCpnPossibleList(paramVO);
			
			if(cpnInfoVO != null && cpnInfoVO.getCpnInfo() != null && !"".equals(cpnInfoVO.getCpnInfo())) {
				//2018.12.22 - VR앱 : VR앱으로 조회 하는 경우 스탬프/쿠폰 정보를 단말에 주지 않는다.
				if(!(paramVO.getApplType().equals("") || paramVO.getApplType() == null) && !paramVO.getApplType().substring(0, 1).equals("E")) {
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
//			imcsLog.failLog(ImcsConstants.API_PRO_ID109, "", null, "coupon(mims)_info:" + ImcsConstants.RCV_MSG2, methodName, methodLine);
		}
		
		tp2	= System.currentTimeMillis();
		imcsLog.timeLog("발급가능쿠폰(F_GET_CPN_COND_POSSIBLE_LIST)", String.valueOf(tp2 - tp1), methodName, methodLine);			    
		
		// 스탬프 정보 조회
		try{
			cpnInfoVO	= buyNSContentDAO.getStmPossibleList(paramVO);
			
			if(cpnInfoVO != null && cpnInfoVO.getStmInfo() != null && !"".equals(cpnInfoVO.getStmInfo())) {
				//2018.12.22 - VR앱 : VR앱으로 조회 하는 경우 스탬프/쿠폰 정보를 단말에 주지 않는다.
				if(!(paramVO.getApplType().equals("") || paramVO.getApplType() == null) && !paramVO.getApplType().substring(0, 1).equals("E")) {
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
//			imcsLog.failLog(ImcsConstants.API_PRO_ID109, "", null, "coupon(mims)_info:" + ImcsConstants.RCV_MSG2, methodName, methodLine);
		}
		
		tp1	= System.currentTimeMillis();
		imcsLog.timeLog("발급가능스탬프(F_GET_STM_COND_POSSIBLE)", String.valueOf(tp1 - tp2), methodName, methodLine);
		
	    //2018.12.07 - VR앱 : VR앱으로 조회 하는 경우 스탬프/쿠폰 정보를 조회하지 않는다.
		if(!(paramVO.getApplType().equals("") || paramVO.getApplType() == null) && !paramVO.getApplType().substring(0, 1).equals("E"))
		{
			// 사용 가능 쿠폰 정보 조회
			try{
				cpnInfoVO	= buyNSContentDAO.getUseCpnPossibleList(paramVO);
				
				if(cpnInfoVO != null && cpnInfoVO.getUseCpnInfo() != null && !"".equals(cpnInfoVO.getUseCpnInfo())) {
					rtnCpnInfoVO.setUseCpnInfo("CPN02" + ImcsConstants.COLSEP + cpnInfoVO.getUseCpnInfo() + ImcsConstants.COLSEP);
				} else {
					cpnInfoVO = new ComCpnVO();
				}
				
				szMsg	= " SELECT smartux.F_GET_CPN_USE_POSSIBLE_LIST =[" + StringUtil.nullToSpace(cpnInfoVO.getUseCpnInfo()) + "]";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
				
				
			} catch(Exception e) {
//			imcsLog.failLog(ImcsConstants.API_PRO_ID109, "", null, "coupon(mims)_info:" + ImcsConstants.RCV_MSG2, methodName, methodLine);
			}
		}
		
		
		tp2	= System.currentTimeMillis();
		imcsLog.timeLog("사용가능스쿠폰(F_GET_CPN_USE_POSSIBLE_LIST)", String.valueOf(tp2 - tp1), methodName, methodLine);

		return rtnCpnInfoVO;
	}

	
	
	
	
	
	
	/**
	 * 스탬프 조회 후 스탬프 존재시 INSERT
	 * @param paramVO
	 * @return
	 */
	public int insStmInfo(BuyNSContentRequestVO paramVO, String szData) throws Exception {

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
		
		if("".equals(szData))
			return -1;
		
		arrData	= szData.split("\\^");
		nDataCnt = arrData.length;
		
		for(int i = 0; i < nDataCnt; i++) {
			if("".equals(arrData) || arrData[i].indexOf("|") < 0)	
				return -1;
			
			arrInfo = arrData[i].split("\\|");
			szCpnId	= arrInfo[1];
			paramVO.setStrmpId(szCpnId);
			
			// 스탬프 정보 저장
			try {
				nResult = buyNSContentDAO.insStmInfo(paramVO);
				
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] insert [PT_CPM_STAMP_BOX_ACTION] [" + nResult + "] records Success at ";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			} catch(Exception e) { 	nResult	= -1;	}
			
			if(nResult < 1) {
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] insert [PT_CPM_STAMP_BOX_ACTION] COUPON_BOX_ACTION table Failed at ";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
				break;
			}
			
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] INSERT COUPON_BOX_ACTION table records Success : STAMP_ID[" + paramVO.getStrmpId() + "] ";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
		}
		
		return nResult;
	}
	



	/**
	 * 쿠폰정보 조회 후 쿠폰 존재시 INSERT
	 * @param paramVO
	 * @return
	 */
	public int insCpnInfo(BuyNSContentRequestVO paramVO, String szData) throws Exception {
		
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
				nResult = buyNSContentDAO.insCpnInfo(paramVO);
				
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] insert [SEQ_PT_CPM_COUPON_BOX_ACTION] [" + nResult + "] records Success at ";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			} catch(Exception e) { 	nResult	= -1;	}
			
			if(nResult < 1) {
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] insert [SEQ_PT_CPM_COUPON_BOX_ACTION] COUPON_BOX_ACTION table Failed at ";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
				break;
			}
			
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID109) + "] INSERT PT_CPM_COUPON_BOX_ACTION table records Success : CPEVT_ID[" + paramVO.getCpevtId() + "] ";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
		}
		
		return nResult;
	}

        
}