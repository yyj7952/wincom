package kr.co.wincom.imcs.api.chkBuyNSPG;

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
import kr.co.wincom.imcs.common.vo.ComPriceVO;
import kr.co.wincom.imcs.common.vo.ComSbcVO;
import kr.co.wincom.imcs.common.vo.ContTypeVO;
import kr.co.wincom.imcs.handler.ImcsException;
import kr.co.wincom.imcs.common.NosqlCacheType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

//import com.sun.xml.internal.rngom.digested.DZeroOrMorePattern;

@Service
public class ChkBuyNSPGServiceImpl implements ChkBuyNSPGService {
	private Log imcsLogger = LogFactory.getLog("API_chkBuyNSPG");
	
	@Autowired
	private ChkBuyNSPGDao chkBuyNSPGDao;
	
	@Autowired
	private NoSQLRedisDao noSQLRedisDao;
	
	@Autowired
	private CommonService commonService;

//	public void chkBuyNSPG(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	HashMap<String, String> mResult = new HashMap<String, String>();
	
	@Override
	public ChkBuyNSPGResultVO chkBuyNSPG(ChkBuyNSPGRequestVO paramVO){
//		this.chkBuyNSPG(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		ChkBuyNSPGResultVO resultVO = new ChkBuyNSPGResultVO();
		ComPriceVO priceVO = new ComPriceVO();
		ComDataFreeVO datafreeVO = new ComDataFreeVO();
		
		String flag		= "";
		String errCode	= "";
		String errMsg	= "";
		
	    int nDupChk		= 0;
	    int nEventChk	= 0;
	    
	    Integer	block_flag	= 0;
	    Integer resultSet	= 0;
	    Integer messageSet	= 99;
	    
	    String msg	= "";
		String szBuyingDate	= "";
		String szBuyDate	= "";
	    

	    long tp_start = paramVO.getTp_start();
		long tp1 = 0;
		long tp2 = 0;
		long tp3 = 0;
		long tp4 = 0;
	    
		try{
			tp1	= System.currentTimeMillis();
			imcsLog.timeLog("init...", String.valueOf(tp1 - tp_start), methodName, methodLine); 
			
			
			switch(paramVO.getPayGb())
		    {
				case "N":
				case "S":
					break;
				case "R":
					if(paramVO.getBuyingType().equals("Y"))
					{
						resultSet = -1;
						messageSet = 13;
					}
					if(paramVO.getPkgYn().equals("Y"))
					{
						resultSet = -1;
						messageSet = 13;
					}
					break;
				default:
					resultSet = -1;
					messageSet = 13;
					break;
		    }
			
			// 상태, 개통여부 및 쿠폰값 가져오기
	    	/*try {
	    		szBuyingDate = commonService.getSysdate();
	    		paramVO.setBuyingDate(szBuyingDate);
	    	} catch (Exception e) {
	    		imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage() + "," + methodName + "_getSysdate()");
				throw new ImcsException(ImcsConstants.FAIL_CODE, e);
			}*/
	    	
	    	
			if(resultSet == 0){

				List<ComSbcVO> sbcInfo = new ArrayList<ComSbcVO>();
				String szStatusFlag	= "";
				String szYnVodOpen	= "";
				String szTestSbc	= "";
				//int nCpCnt	= 0;
				
	    		try {
	    			/* 상태, 개통여부 및 쿠폰값 가져오기 */
	    			sbcInfo = this.buyContent_Cust_Sel(paramVO); // C -> buyContent_Cust_Sel()
	    			if(sbcInfo != null && sbcInfo.size() > 0){
	    				szStatusFlag	= StringUtil.nullToSpace(sbcInfo.get(0).getStatusFlag());
	    				szYnVodOpen		= StringUtil.nullToSpace(sbcInfo.get(0).getYnVodOpen());
	    				szTestSbc		= StringUtil.nullToSpace(sbcInfo.get(0).getTestSbc());
	    				//nCpCnt			= sbcInfo.get(0).getCpCnt();
	    			} else {
	    				resultSet	= -1;
	    				messageSet = 10;
	    			}
	    		} catch (Exception e) {
	    			resultSet	= -1;
	    			messageSet = 10;
	    			
	    			//throw new ImcsException(ImcsConstants.FAIL_CODE, e);
	    		}
	    		
	    		tp2	= System.currentTimeMillis();
	    		imcsLog.timeLog("가입자 상태, 개통여부 조회", String.valueOf(tp2 - tp1), methodName, methodLine); 
				tp1 = System.currentTimeMillis();
				
				String cur_date = commonService.getSysdateYMD();
				
				// 정액/종량 조회
				try {
					priceVO	= this.buyContent_Bill_Type(paramVO);
					
					if(priceVO != null) {
						paramVO.setProdType(priceVO.getProductType());
						
						paramVO.setLicensingStart(priceVO.getLicenseStart());
						paramVO.setLicensingEnd(priceVO.getLicenseEnd());
						
					    // 2019.09.04 - 라이센스 유효 기간 외 구매 제한
					    if(cur_date.compareTo(paramVO.getLicensingStart().substring(0, 8)) >= 0)
						{
							if(cur_date.compareTo(paramVO.getLicensingEnd().substring(0, 8)) <= 0)
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
						
					} else {
						priceVO	= new ComPriceVO();
						
						resultSet = -1;
						messageSet	= 13;
					}
					
					if(resultSet == 0){
					
						if(priceVO.getPreviewFlag().equals("R")){
							
							//String cur_date = commonService.getSysdateYMD();
							
							
							if(paramVO.getPayGb().equals("R")){
								if(paramVO.getBuyingType().equals("A")){
									resultSet = -1;
									messageSet = 13;
									String szMsg = "";
									szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID258) + "] 인앱 예약 구매 불가[pay_gb:" + paramVO.getPayGb() + "]";
									imcsLog.serviceLog(szMsg, methodName, methodLine);
								}else if(cur_date.compareTo(priceVO.getReservedDate().substring(0, 8)) >= 0){
									resultSet = -1;
									messageSet = 13;
									String szMsg = "";
									szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID258) + "] 예약구매 기간 종료[시청가능일:" + priceVO.getReservedDate() + "]";
									imcsLog.serviceLog(szMsg, methodName, methodLine);
									
								}else{
									String szMsg = "";
									szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID258) + "] 예약구매 가능";
									imcsLog.serviceLog(szMsg, methodName, methodLine);
								}
							}else{
								if(cur_date.compareTo(priceVO.getReservedDate().substring(0, 8)) < 0){
									resultSet = -1;
									messageSet = 13;
									String szMsg = "";
									szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID258) + "] 예약구매 기간 [시청가능일:" + priceVO.getPreviewFlag() + "]";
									imcsLog.serviceLog(szMsg, methodName, methodLine);
								}
							}
						}else if(priceVO.getPreviewFlag().equals("N")){
							if(paramVO.getPayGb().equals("R")){
								resultSet = -1;
								messageSet = 13;
								String szMsg = "";
								szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID258) + "] 예약구매 불가 컨텐츠[previce_flag:" + priceVO.getPreviewFlag() + "]";
								imcsLog.serviceLog(szMsg, methodName, methodLine);
							}
							
						}else{
							resultSet = -1;
							String szMsg = "";
							szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID258) + "] 구매 불가 컨텐츠[previce_flag:" + priceVO.getPreviewFlag() + "]";
							imcsLog.serviceLog(szMsg, methodName, methodLine);
						}
					}
					
					tp2	= System.currentTimeMillis();
					imcsLog.timeLog("정액/종량/인앱가격 조회", String.valueOf(tp2 - tp1), methodName, methodLine); 					
					
					
					if("A".equals(paramVO.getBuyingType()) && resultSet == 0){
						
						/* FVOD의 경우 0원으로 인앱 가격을 조회한다.	*/
		    			if("0".equals(paramVO.getProdType())){
		    				datafreeVO.setPrice("0");
		    			}else{
		    				datafreeVO.setPrice(priceVO.getSuggestedPrice());
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
		    			
		    			if(datafreeVO != null){
		    				paramVO.setApprovalPrice(datafreeVO.getApprovalPrice());
		    			}
					}
					
					//결제 차단
					if(paramVO.getBlock_flag() == 1 && !"0".equals(paramVO.getProdType())) {
						String szMsg = "";
						szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID258) + "] PPVBLOCK 사용자 ";
						imcsLog.serviceLog(szMsg, methodName, methodLine);
						resultSet = -1;
						messageSet = 19;
					}
					
					
					if ("N".equals(paramVO.getLicensingValidYn()) && !szTestSbc.equals("Y")) {
		            	msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID258) + "] licensing_expired ";
		    			imcsLog.serviceLog(msg, methodName, methodLine);
						resultSet = -1;
						messageSet = 98;
					}
					
					/* 사용여부 체크 */
					if(!"Y".equals(szStatusFlag)){
						resultSet = -1;
						messageSet = 11;
					}
					
					if(resultSet == 0){
						if( "N".equals(szYnVodOpen) ){		// 개통여부 조회
							if(!"F".equals(priceVO.getBillType())){	// FVOD만 사용가능
								resultSet = -1;
								messageSet = 12;
							}
						}
					}
					
					if( resultSet == 0 && !"F".equals(priceVO.getBillType()) && !"Y".equals(paramVO.getPkgYn())){
						tp1 = System.currentTimeMillis();
						nDupChk = this.getBuyDupChk(paramVO);
						
						tp2	= System.currentTimeMillis();
						imcsLog.timeLog("가입자가 구입한 상품 조회", String.valueOf(tp2 - tp1), methodName, methodLine); 
						
						
						if (nDupChk == 0)
				        {
				            if (!priceVO.getBillType().equals("F"))
				            {
				            	resultSet = -1;
				            	messageSet = 9;
				            }
				        }
					}
					
					
					
					if( resultSet == 0 && !"Y".equals(paramVO.getPkgYn()) ){
						if(paramVO.getPayGb().equals("R")){
							if(paramVO.getProdType().equals("1")){
								if(!paramVO.getBuyingPrice().equals(priceVO.getReservedPrice())){
									String szMsg = "";
									szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID258) + "] 예약PPV - incorrect price[imcs_price=" + priceVO.getReservedPrice() + "][stb_price=" + paramVO.getBuyingPrice() + "]";
									imcsLog.serviceLog(szMsg, methodName, methodLine);
									resultSet = -1;
									messageSet = 45;
								}else{
									tp1 = System.currentTimeMillis();
									nDupChk = this.getBuyContentDupChk(paramVO);
									
									tp2	= System.currentTimeMillis();
									imcsLog.timeLog("가입자가 구입한 상품 조회", String.valueOf(tp2 - tp1), methodName, methodLine); 
									
								//대기	
								/*	if(nDupChk == -1){
										resultSet 	= -1;
										messageSet	= 22;
									}*/
									

									if(nDupChk > 0){
										resultSet	= -1;
										messageSet	= 24;
									}	
								}								
							}else{
								String szMsg = "";
								szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID258) + "] 예약구매 불가(FVOD상품)";
								imcsLog.serviceLog(szMsg, methodName, methodLine);
								
								resultSet = -1;
								messageSet = 13;
							}
						}else{
							/* 데이터 프리 구매 여부 확인	*/
							List<String> mDataFreeInfo	= new ArrayList<String>();
							String szMsg = "";
							Integer buyDataFreeYn	= 0;
							try {
								String AlbumId = paramVO.getAlbumId();
								paramVO.setAlbumId(AlbumId+"_D");
								mDataFreeInfo = chkBuyNSPGDao.getBuyDataFreeInfo(paramVO);
								
								szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID258) + "] chkPaymentId [SELECT DATAFREE PT_VO_BUY_NSC] table [" + mDataFreeInfo.size() + "] records Success at";
								imcsLog.serviceLog(szMsg, methodName, methodLine);
								paramVO.setAlbumId(AlbumId);
								buyDataFreeYn = 0;
								
								if(mDataFreeInfo == null || mDataFreeInfo.isEmpty()){
									buyDataFreeYn = -1;
								}
								
								
								
							} catch (Exception e) {				
//								imcsLog.failLog(ImcsConstants.API_PRO_ID258, "", null, ImcsConstants.RCV_MSG6, methodName, methodLine);
								
								buyDataFreeYn = -1;
							}
							
													
							if(buyDataFreeYn != 0){
								
								// 기존 구매 내역 조회
								if( "1".equals( paramVO.getProdType() ) ){
									// PPV상품의 경우 구매 내역 조회
									if( "A".equals( paramVO.getBuyingType().toUpperCase() ) ){
										if( !paramVO.getApprovalPrice().equals( paramVO.getBuyingPrice() ) ){
											
											msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID258) + "] 일반PPV - incorrect price[imcs_price=" + paramVO.getApprovalPrice() + "][stb_price=" + paramVO.getBuyingPrice() + "]";
											imcsLog.serviceLog(msg, methodName, methodLine);
											
											resultSet = -1;
				                            messageSet = 45;
										}
									}else{
										if( !priceVO.getSuggestedPrice().equals( paramVO.getBuyingPrice() ) ){
											
											msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID258) + "] 일반PPV - incorrect price[imcs_price=" + priceVO.getSuggestedPrice() + "][stb_price=" + paramVO.getBuyingPrice() + "]";
											imcsLog.serviceLog(msg, methodName, methodLine);
											
											resultSet = -1;
				                            messageSet = 45;
										}
									}
									
								}else if("0".equals(paramVO.getProdType())){
									if(!"0".equals(paramVO.getBuyingPrice())){
										msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID258) + "] FVOD - incorrect price[imcs_price=0][stb_price=" + paramVO.getBuyingPrice() + "]";
										imcsLog.serviceLog(msg, methodName, methodLine);
										
										resultSet = -1;
					                    messageSet = 45;
									}
									
								}
								
								if(resultSet == 0 && !"0".equals(paramVO.getProdType())){
									
									tp1 = System.currentTimeMillis();
									nDupChk = this.getBuyContentDupChk(paramVO);
									
									tp2	= System.currentTimeMillis();
									imcsLog.timeLog("가입자가 구입한 상품 조회", String.valueOf(tp2 - tp1), methodName, methodLine); 
							
									//대기
									/*if(nDupChk == -1){
										resultSet 	= -1;
										messageSet	= 22;
									}*/
									
									if(nDupChk > 0){
										resultSet	= -1;
										messageSet	= 24;
									}	
								}
							}
						}
					} else {
						tp1 = System.currentTimeMillis();
						nDupChk = this.getBuyContentDupChk(paramVO);
						
						tp2	= System.currentTimeMillis();
						imcsLog.timeLog("가입자가 구입한 패키지 상품 조회", String.valueOf(tp2 - tp1), methodName, methodLine); 
						
						if(nDupChk > 0){
							resultSet	= -1;
							messageSet	= 24;
						}	
					}
					
					
					if(resultSet == 0){
						ContTypeVO contInfoVO = new ContTypeVO();
						
						if( "Y".equals( paramVO.getPkgYn().toUpperCase() ) ){			// 패키지 컨텐츠 조회
							tp1 = System.currentTimeMillis();
							paramVO.setContsGenre("패키지");
							
							try {
								contInfoVO = this.getPackageContent(paramVO);
							} catch (Exception e) {
								resultSet = -1;
								messageSet = 30;
							}
							
							if(contInfoVO == null){
								resultSet = -1;
								messageSet = 31;
							}
							
							tp2	= System.currentTimeMillis();
							imcsLog.timeLog("패키지 컨텐츠 보관함 저장", String.valueOf(tp2 - tp1), methodName, methodLine); 
						} else if("N".equals( paramVO.getPkgYn().toUpperCase() )) {		// 단품 컨텐츠 조회
							tp1	= System.currentTimeMillis();
							
							try {
								contInfoVO = this.getContGenre(paramVO);
								
								if(contInfoVO != null) {
									paramVO.setContsGenre(contInfoVO.getContsGenre());
									// EventType 사용하지 않음 (쿼리에서는 contType으로 우선 해두었음)
								}
							} catch (Exception e) {
								resultSet = -1;
								messageSet = 90;
								throw new ImcsException(ImcsConstants.FAIL_CODE, e);
							}
							
							tp2	= System.currentTimeMillis();
							imcsLog.timeLog("단품 컨텐츠 장르 조회", String.valueOf(tp2 - tp1), methodName, methodLine); 
						}
					}
					
					if(paramVO.getContsGenre() == null){
						paramVO.setContsGenre("기타");
					}
					
					if(resultSet == 0)
					{
						if(priceVO.getPayFlag().equals("1")) {//아이돌라이브 유로콘서트 
							int cstFlag = 1;
							
							if(resultSet == 0) {
								cstFlag = this.chkConsert(paramVO);
								
								if(cstFlag == 1) {
									msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID258) + "] 아이돌라이브 유료콘서트 구매 가능.";
									imcsLog.serviceLog(msg, methodName, methodLine);
								} else {
									resultSet  = -1;
									messageSet = 40;
									msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID258) + "] 아이돌라이브 유료콘서트 구매 불가능.";
									imcsLog.serviceLog(msg, methodName, methodLine);
								}
							}
						}
					}
				    
				    if(resultSet == 0){
				    	resultVO.setFlag("0");
				    	resultVO.setErrMsg("insert success");
				    	resultVO.setBuyingDate("");
				    } else {
				    	paramVO.setMessageSet(messageSet);
				    	throw new ImcsException();
				    }
				} catch (Exception e) {
					
					if(resultSet != -1){
						resultSet	= -1;
						messageSet	= 13;
					}
					
					//imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage() + "," + methodName + "_buyContent_Bill_Type()");
					msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID258) + "] return[" + resultVO.getFlag() +"|" + resultVO.getErrMsg() + "|" + resultVO.getBuyingDate() + "|" + resultVO.getErrCode() + "|]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					
					//throw new ImcsException(ImcsConstants.FAIL_CODE, e);
				}
			}
		

		} catch(ImcsException ie) {
			isLastProcess = ImcsConstants.RCV_MSG5;
			tp1	= System.currentTimeMillis();
			
			
			mResult = commonService.getErrorMsg(messageSet);
			
			flag	= "1";
			
			if(mResult != null) {
				errCode	= mResult.get("ERR_CODE");
				errMsg	= mResult.get("ERR_MSG");
			} else {
				errCode	= "99";
				errMsg	= "incorrect failed !!!";
			}
			
			if(messageSet == 24) {
				flag	= "2";
				errCode = "";
				szBuyDate = paramVO.getBuyDate();
				resultVO.setBuyingDate(szBuyDate);
			}
			
			resultVO.setFlag(flag);
			resultVO.setErrCode(errCode);
			resultVO.setErrMsg(errMsg);
			
			tp2	= System.currentTimeMillis();
			
			imcsLog.errorLog(methodName + "-E",ie.getClass().getName()+":"+ie.getMessage());
			throw new ImcsException(flag, errMsg, errCode, resultVO);
		} catch(Exception e) {
			isLastProcess = ImcsConstants.RCV_MSG6;

			resultVO.setFlag("1");
			resultVO.setErrCode("99");
			resultVO.setErrMsg("incorrect failed !!!");
			
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			throw new ImcsException(resultVO.getFlag(), resultVO.getErrCode(), resultVO.getErrMsg());
		} finally{
			
			if(resultSet == -1){
				mResult = commonService.getErrorMsg(messageSet);
				
				flag	= "1";
				
				if(mResult != null) {
					errCode	= mResult.get("ERR_CODE");
					errMsg	= mResult.get("ERR_MSG");
				} else {
					errCode	= "99";
					errMsg	= "incorrect failed !!!";
				}
				
				if(messageSet == 24) {
					flag	= "2";
					errCode = "";
					szBuyDate = paramVO.getBuyDate();
					resultVO.setBuyingDate(szBuyDate);
				}
				
				resultVO.setFlag(flag);
				resultVO.setErrCode(errCode);
				resultVO.setErrMsg(errMsg);
				
				String szMsg = "";
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID258) + "] msg[" + resultVO.getErrMsg() + "]";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			}
			
			msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID258) + "] return[" + resultVO.getFlag() +"|" + resultVO.getErrMsg() + "|" + resultVO.getBuyingDate() + "|" + resultVO.getErrCode() + "|]";
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID258) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
			

		}
		
		return resultVO;
	}
	
	
	
    
    
    
    
    /**
     * 가입자 구매상품 여부 조회
     * @param paramVO
     * @return
     */
    public int getBuyDupChk(ChkBuyNSPGRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
    	
    	String sqlId = "lgvod258_030_20190102_002";
		
		List<Integer> list = null;
		Integer resultVO = 0;
		
		int querySize = 0;
		
		try {
			try{
				list = chkBuyNSPGDao.getBuyDupChk(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list == null || list.isEmpty()){
				resultVO = 0;
			} else {
				querySize = list.size();
				resultVO = (Integer)list.get(0);
				if(resultVO == null)	resultVO = 0;
			}
			
			try{
//				imcsLog.dbLog2(ImcsConstants.API_PRO_ID258, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			paramVO.setResultSet(-1);
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			//throw new ImcsException();
		}
		
    	return resultVO;    	
    }

    
    
    
    /**
     * 상태, 개통여부 및 쿠폰값 가져오기
     * @param paramVO
     * @return
     */
    public List<ComSbcVO> buyContent_Cust_Sel(ChkBuyNSPGRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();   
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
    	
    	String sqlId = "lgvod258_010_20171214_001";

		List<ComSbcVO> list = new ArrayList<ComSbcVO>();
		
		int querySize = 0;
		try {
			try{
				list = chkBuyNSPGDao.getSbcInfo(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
				int iCount = 1;
				for (ComSbcVO item : list) {
					iCount++;
					if(!"N".equals(item.getComName())) {
						paramVO.setBlock_flag(1);
					}
				}
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			if( list != null && !list.isEmpty()){
				querySize = list.size();
			}

			try{
//				imcsLog.dbLog2(ImcsConstants.API_PRO_ID258, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			throw new ImcsException();
		}
		
    	return list;
    }
    
    
    
    /**
     * 가격정보 조회
     * @param paramVO
     * @return
     */
    public ComPriceVO buyContent_Bill_Type(ChkBuyNSPGRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();    	
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		
    	String sqlId = "";
    	
    	if("Y".equals(paramVO.getPkgYn()))	sqlId = "lgvod258_021_20171214_001";
    	else					   			sqlId = "lgvod258_022_20171214_001";
    	
		List<ComPriceVO> list   = null;
		ComPriceVO resultVO = null;
		
		int querySize = 0;
		
		try {
			try{
				list = chkBuyNSPGDao.getBillType(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list != null && !list.isEmpty()){
				resultVO = list.get(0);
				querySize = list.size();
			}
			
			try{
//				imcsLog.dbLog2(ImcsConstants.API_PRO_ID258, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			throw new ImcsException();
		}
		
    	return resultVO;
    }

    
    

  
    /**
     * 패키지 컨텐츠 조회
     * @param vo
     * @return
     */
    public ContTypeVO getPackageContent(ChkBuyNSPGRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();    	
    	
    	String sqlId = "lgvod258_070_20171214_001";
		
		int querySize = 0;
		
		List<ContTypeVO> list   = null;
		ContTypeVO resultVO = null;
				
		try {
			try{
				if("N".equals(paramVO.getPpsId())){
					list = chkBuyNSPGDao.getPackageContent(paramVO);
				} else {// ppsID 로 패키지 컨텐츠 조회
					list = chkBuyNSPGDao.getPackageContent2(paramVO);
				}
				
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list == null || list.isEmpty()){
				querySize = 0;
				list = null;
			} else {
				querySize = list.size();
				resultVO = list.get(querySize - 1);
			}
			
			try{
//				imcsLog.dbLog2(ImcsConstants.API_PRO_ID258, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			throw new ImcsException();
		}
		
    	return resultVO;
    }
    

    
    
    /**
     * 단품 상품정보 조회
     * @param vo
     * @return
     */
    public ContTypeVO getContGenre(ChkBuyNSPGRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();    	
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
    	String sqlId = "lgvod258_080_20171214_001";
		
		List<ContTypeVO> list   = null;
		ContTypeVO resultVO = null;
		
		int querySize  = 0;
		
		try {
			try{
				list = chkBuyNSPGDao.getContGenre(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			if( list != null && !list.isEmpty()){
				resultVO = list.get(0);
				querySize = list.size();
			}
			
			try{
//				imcsLog.dbLog2(ImcsConstants.API_PRO_ID258, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":"  +e.getMessage());
			throw new ImcsException();
		}
		
    	return resultVO;
    }
    
    /**
     * 데이터프리 정보 조회
     * @param	ComDataFreeVO, ChkBuyNSPGRequestVO
     * @result	ComDataFreeVO
    **/
    public ComDataFreeVO getDatafreeInfo(ComDataFreeVO tempVO, ChkBuyNSPGRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
    	
    	String sqlId = "lgvod258_090_20171214_001";
		
		List<ComDataFreeVO> list   = new ArrayList<ComDataFreeVO>();
		ComDataFreeVO resultVO = null;
		
		int querySize  = 0;
		
		try {
			try{
				list  = chkBuyNSPGDao.getDatafreeInfo(tempVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list != null && !list.isEmpty()){
				resultVO = (ComDataFreeVO)list.get(0);
				
				querySize = list.size();
			}else{
				resultVO = new ComDataFreeVO();
				resultVO.setDatafreePrice("0");
				resultVO.setApprovalPrice("0");
				resultVO.setDatafreeApprovalPrice("0.00");
				resultVO.setPpvDatafreeApprovalPrice("0.00");
			}
			
			try{
//				imcsLog.dbLog2(ImcsConstants.API_PRO_ID258, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
//			 imcsLog.failLog(ImcsConstants.API_PRO_ID258, sqlId, cache, "approval_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
    	return resultVO;
    }

    
    public int getBuyContentDupChk(ChkBuyNSPGRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
    	
    	String sqlId = "";
    	
    	int resultVO = 0;
    	
    	int querySize = 0;
    	
//    	if(paramVO.getProdType().equals("0")){
//    		if(paramVO.getAppType().substring(0,1).equals("R")){
//    			sqlId = "lgvod258_061_20171214_001";
//    		}else{
//    			sqlId = "lgvod258_062_20171214_001";
//    		}
//    	}else if(paramVO.getProdType().equals("1")){
//    		if(paramVO.getAppType().substring(0,1).equals("R")){
//    			sqlId = "lgvod258_063_20171214_001";
//    		}else{
//    			sqlId = "lgvod258_064_20171214_001";
//    		}
//    	}else if(paramVO.getProdType().equals("2")){
//    		if(paramVO.getAppType().substring(0,1).equals("R")){
//    			sqlId = "lgvod258_065_20171214_001";
//    		}else{
//    			sqlId = "lgvod258_066_20171214_001";
//    		}
//    	}
    	
		
		try {

			List<HashMap> rtnlist = null;
			
			if(paramVO.getProdType().equals("0")){
				rtnlist = chkBuyNSPGDao.buyPresentDupChk1(paramVO);
			}else if(paramVO.getProdType().equals("1")){
				rtnlist = chkBuyNSPGDao.buyPresentDupChk2(paramVO);
			}else if(paramVO.getProdType().equals("2")){
				rtnlist = chkBuyNSPGDao.buyPresentDupChk3(paramVO);
			}
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			
			
			if( rtnlist != null && !rtnlist.isEmpty()){
				resultVO = Integer.parseInt(rtnlist.get(0).get("DUP_CHK").toString());
				paramVO.setBuyDate(rtnlist.get(0).get("BUY_DATE").toString());
				
				querySize = rtnlist.size();
			}
			
			
			try{
//				imcsLog.dbLog2(ImcsConstants.API_PRO_ID258, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
//			 imcsLog.failLog(ImcsConstants.API_PRO_ID258, sqlId, cache, "approval_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
    	return resultVO;
    }
    
    /**
     * 아이돌라이브 유료콘서트 컨텐츠를 구매 가능한지 조회
     * @param vo
     * @return
     * @throws Exception
     */
    public Integer chkConsert(ChkBuyNSPGRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
    	Integer result = 0;		
		String szMsg = "";
		    	
    	try {    		
    		try{
    			// 아이돌라이브 유료콘서트 구매가능 조회
    			result = chkBuyNSPGDao.chkConsert(paramVO);
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
}
