package kr.co.wincom.imcs.api.buyNSPresent;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.nosql.NoSQLRedisDao;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.ComPriceVO;
import kr.co.wincom.imcs.common.vo.ComSbcVO;
import kr.co.wincom.imcs.common.NosqlCacheType;
import kr.co.wincom.imcs.handler.ImcsException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class BuyNSPresentServiceImpl implements BuyNSPresentService {
	private Log imcsLogger = LogFactory.getLog("API_buyNSPresent");
	
	@Autowired
	private BuyNSPresentDao buyNSPresentDao;
	
	@Autowired
	private NoSQLRedisDao noSQLRedisDao;
	
	@Autowired
	private CommonService commonService;

//	public void buyNSPresent(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	@Override
	public BuyNSPresentResultVO buyNSPresent(BuyNSPresentRequestVO paramVO){
//		this.buyNSPresent(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	

		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		imcsLog.debugLog(methodName + " service call");

		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		BuyNSPresentResultVO resultVO = new BuyNSPresentResultVO();
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		String flag		= "";
		String errCode	= "";
		String errMsg	= "";
		
	    Integer resultSet	= 0;
	    Integer messageSet	= 0;
	    
	    String msg	= "";
		String szBuyDate	= "";
		String szBuyingDate	= "";

		long tp1 = 0;
		long tp2 = 0;
	    
		try{
			tp1 = System.currentTimeMillis();
			// ??????, ???????????? ??? ????????? ??????
			
	    	try {
	    		szBuyingDate = commonService.getSysdate();
	    		paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
	    	} catch (Exception e) {
	    		imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage() + "," + methodName + "_getSysdate()");
				throw new ImcsException();
			}
	    	
	    	paramVO.setBuyingDate(szBuyingDate);
	    	
	    	List<ComSbcVO> lCustomerInfo = new ArrayList<ComSbcVO>();
	    	ComSbcVO customerVO = new ComSbcVO();
	    	
	    	try {
	    		lCustomerInfo = this.getCustomerInfo(paramVO);
	    		paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
	    		
	    		if(lCustomerInfo == null || lCustomerInfo.size() == 0)
	    			resultSet	= -1;
	    		else
	    			customerVO	= lCustomerInfo.get(0);
	    		
			} catch (Exception e) {
				resultSet	= -1;
			}
	    				
			if(resultSet == -1){
				tp2	= System.currentTimeMillis();
				imcsLog.timeLog("????????? ??????, ???????????? ??????", String.valueOf(tp2 - tp1), methodName, methodLine); 
				
				messageSet = 10;
			}

			
			List<ComPriceVO> lPriceVO = new ArrayList<ComPriceVO>();
			ComPriceVO priceInfoVO = new ComPriceVO();

			if(resultSet == 0){
				if( "Y".endsWith(paramVO.getCpUseYn()) ){		// ?????????????????? ???????????? ?????????
					messageSet = 62;
				}
				
				tp2	= System.currentTimeMillis();
				imcsLog.timeLog("????????? ??????, ???????????? ??????", String.valueOf(tp2 - tp1), methodName, methodLine); 
				
				tp1 = System.currentTimeMillis();
				
				
				// ?????? ???????????? (??????/??????) ??????
				lPriceVO	= this.getBillTypeInfo(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				resultSet = paramVO.getResultSet();
				
				if(lPriceVO == null || lPriceVO.size() == 0) {
					resultSet = -1;
					messageSet	= 13;
				} else {	
					priceInfoVO = lPriceVO.get(0);
					
					paramVO.setContsGenre(priceInfoVO.getContsGenre());
										
					// 2019.10.29 - NPT_VO_BUY_META ?????? ?????? Set
					// ??????????????? PPV ???????????? ??????.
					paramVO.setProdType(priceInfoVO.getProductType());		// 2019.10.29 - ????????? PRODUCT_TYPE??? ???????????? ????????? ??????
					paramVO.setAssetName(priceInfoVO.getAssetName());
					paramVO.setHdcontent(priceInfoVO.getHdcontent());
					paramVO.setRatingCd(priceInfoVO.getRatingCd());				
					paramVO.setProductId(priceInfoVO.getProductId());
					paramVO.setProductName(priceInfoVO.getProductName());
					paramVO.setProductKind(priceInfoVO.getProductKind());
					paramVO.setCpId(priceInfoVO.getCpId());
					paramVO.setMaximumViewingLength(priceInfoVO.getMaximumViewingLength());
					paramVO.setSeriesNo(priceInfoVO.getSeriesNo());
				}
				tp2	= System.currentTimeMillis();
				imcsLog.timeLog("?????? ??????(??????/??????) ??????", String.valueOf(tp2 - tp1), methodName, methodLine); 
				
				
				if(!"Y".equals(customerVO.getStatusFlag())){
					resultSet = -1;
					messageSet = 11;
				}
				
				if(resultSet == 0){
					// ???????????? ??????
					if( "N".equals(customerVO.getYnVodOpen()) ){
						// FVOD??? ?????? ?????? (F??? ?????? ?????? ??????)
						if(!"F".equals(priceInfoVO.getBillType())){
							resultSet = -1;
							messageSet = 12;
						}
					}
				}
			}
			
			
			// ????????? ?????? ?????? ?????? ?????? ??????
			if(resultSet == 0 && !"F".equals(priceInfoVO.getBillType()) ){
				tp1 = System.currentTimeMillis();
				
				int nCustomerProdYn = this.CustomProductChk(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
				tp2	= System.currentTimeMillis();
				imcsLog.timeLog("???????????? ????????? ?????? ??????", String.valueOf(tp2 - tp1), methodName, methodLine); 
				
				resultSet = paramVO.getResultSet();
				
				if(resultSet == -1){
					messageSet = 14;
				}
				
				// ???????????? ????????? ????????? ????????? FVOD??? ?????? ??????
				if(nCustomerProdYn == 0){
					if( !"F".equals(priceInfoVO.getBillType()) ){
						resultSet = -1;
						messageSet = 9;
					}
				}
			}
			
			
			// ?????? ????????? 1??? ????????? ??????
			//if( resultSet == 0 && !"1".equals( priceInfoVO.getProductType() ) ){
			if(!"1".equals( priceInfoVO.getProductType() ) ){
				resultSet = -1;
				messageSet = 62;
			}
			
			//----------------------------------------------------------------------------
			// ?????? ?????? ?????? ?????? ??????
		    // - ?????? ?????? ????????? ???????????? ??????, 
		    // 1. ??????????????? ????????????(Prezent_price) ?????? ?????? ????????? ??????
		    // 2. ????????????(Prezent_price)??? ???????????? ?????? ?????? 
		    //    ????????????(c_suggested_price) ?????? ??????????????? ?????? 
		    // - ?????? ?????? ????????? ???????????? ?????? ??????
		    // 1. ????????????(c_suggested_price) ?????? ??????????????? ?????? 
		    //----------------------------------------------------------------------------
			if(resultSet == 0){
				tp1	= System.currentTimeMillis();
				
				HashMap<String, Object> mPresentDupCk = new HashMap<String, Object>();
				Integer nDupCk	= 0;
				// ???????????? ?????? ?????? (????????????)
				try {
					
					mPresentDupCk = buyNSPresentDao.getPresentDupCk(paramVO);
					paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
					if(mPresentDupCk != null) {
						nDupCk = (Integer) mPresentDupCk.get("DUPLIC_CHK");
						
						if(nDupCk == null)	nDupCk = 0;
						szBuyDate = StringUtil.nullToSpace((String) mPresentDupCk.get("BUY_DATE"));
						
						/*if(mPresentDupCk.get("BUY_DATE") != null)
							szBuyDate = (String) mPresentDupCk.get("BUY_DATE");*/
					}
				} catch (Exception e) {
					resultSet = -1;
					messageSet = 22;
					
					paramVO.setResultCode("40000000");
					
					imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage() + "," + methodName + "_insertBuyPresent2()");
				}
				
				
				// ????????? ???????????? ?????????
				if(nDupCk > 0){
					//----------------------------------------------------------------------------
		            // ???????????? ?????? (????????????)
		            //----------------------------------------------------------------------------
		            //????????? ????????? ???????????? ???????????? ?????? ???????????? ?????? ?????? ??????
					if(!priceInfoVO.getPresentPrice().equals(paramVO.getPresentPrice())){
						resultSet	= -1;
						messageSet	= 45;
					}else{
						msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID311) + "] ?????? ??????????????? - price[" + paramVO.getPresentPrice() + " -> " +priceInfoVO.getPresentPrice() + "]";
						imcsLog.serviceLog(msg, methodName, methodLine);
					}
					
				}else{
					// ?????? ?????? ????????? ?????? ?????? ??????????????? suggested_price??? ??????
					priceInfoVO.setPresentPrice(priceInfoVO.getSuggestedPrice());
					
					//----------------------------------------------------------------------------
		            // ???????????? ?????? (???????????????, ????????????)
		            //----------------------------------------------------------------------------
		            // PPV??????					
					if(Integer.parseInt( priceInfoVO.getEventValue() ) > 0){
						msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID311) + "] ??????????????? - price[" + priceInfoVO.getSuggestedPrice() + " -> " +priceInfoVO.getEventPrice() + "]";
						imcsLog.serviceLog(msg, methodName, methodLine);
						priceInfoVO.setPresentPrice(priceInfoVO.getEventPrice());
					}else{
						if( !priceInfoVO.getSuggestedPrice().equals( paramVO.getBuyingPrice() ) ){
							msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID311) + "] ??????PPV - incorrect price[imcs_price=" + priceInfoVO.getSuggestedPrice() + "][stb_price=" + paramVO.getBuyingPrice() + "]";
							imcsLog.serviceLog(msg, methodName, methodLine);
							
							resultSet = -1;
                            messageSet = 45;
						}
					}
					
				}
			}
			
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("?????? ???????????? ??????", String.valueOf(tp2 - tp1), methodName, methodLine); 
			
			
			
			//----------------------------------------------------------------------------
		    // ?????? ????????? ????????? ?????? : PT_VO_BUY_DETAIL
		    //----------------------------------------------------------------------------
		    if(resultSet == 0){
		    	paramVO.setContsGenre(priceInfoVO.getContsGenre());
		    	
		    	tp1	= System.currentTimeMillis();
		    	
		    	try {
		    		// ????????? ?????? ???????????? ??????
		    		this.insertBuyPresent2(paramVO);
		    		paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				} catch (Exception e) {
					resultSet	= -1;
					messageSet	= 32;
					
					if(paramVO.getSqlCode() == -1)		messageSet = 24;
					
					imcsLog.errorLog(methodName + "-E",e.getClass().getName() + ":" + e.getMessage() + "," + methodName + "_insertBuyPresent2()");
				}
		    	tp2	= System.currentTimeMillis();
		    	imcsLog.timeLog("?????? ????????? ????????? ??????", String.valueOf(tp2 - tp1), methodName, methodLine); 
		    }
		    
		    if(priceInfoVO.getContsGenre() == ""){
		    	priceInfoVO.setContsGenre("??????");
		    }
		    
		    //----------------------------------------------------------------------------
		    // ?????? ???????????? ?????? ?????? : PT_VO_BUY
		    //----------------------------------------------------------------------------
		    if(resultSet == 0){
		    	tp1	= System.currentTimeMillis();
		    	paramVO.setPresentPrice(priceInfoVO.getPresentPrice());
		    	try {
		    		this.insertBuyPresent1(paramVO);
		    		paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
		    	} catch(Exception e) {
		    		resultSet	= -1;
					messageSet	= 40;
					
					imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage() + "," + methodName + "_insertBuyPresent1()");
		    	}
		    	tp2	= System.currentTimeMillis();
		    	imcsLog.timeLog("?????? ???????????? ?????? ??????", String.valueOf(tp2 - tp1), methodName, methodLine); 
		    }
		    
		    //----------------------------------------------------------------------------
		    // ?????? ???????????? ?????? ??????
		    //----------------------------------------------------------------------------
		    if(resultSet == 0){
		    	tp1	= System.currentTimeMillis();
		    	paramVO.setPresentPrice(priceInfoVO.getPresentPrice());
		    	
		    	try {
		    		this.insertBuyPresentP(paramVO);
		    		paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				} catch (Exception e) {
					resultSet = -1;
					messageSet = 40;
					
					imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage() + "," + methodName + "_insertBuyPresentP()");
				}
		    	tp2	= System.currentTimeMillis();
		    	imcsLog.timeLog("?????? ???????????? ?????? ??????", String.valueOf(tp2 - tp1), methodName, methodLine); 
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
		 			
		    // ????????? INSERT (???????????? default??? autocommit??? ???????????? rollback ?????? ????????? ???????????????)
		    if(resultSet == 0){
		    	tp1	= System.currentTimeMillis();
		    	
		    	resultVO.setFlag("0");
		    	resultVO.setErrMsg("insert success");
		    	resultVO.setBuyingdate(paramVO.getBuyingDate());
		    	
		    	tp2	= System.currentTimeMillis();
		    	imcsLog.timeLog("EXEC SQL COMMIT", String.valueOf(tp2 - tp1), methodName, methodLine); 
		    } else {
		    	paramVO.setMessageSet(messageSet);
		    	
		    	throw new ImcsException();
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
			} else {
				errCode	= "99";
				errMsg	= "incorrect failed !!!";
			}
			
			if(messageSet == 24) {
				flag	= "2";
				paramVO.setBuyingDate(szBuyDate);
			}
			
			msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID311) + "] msg[" + errMsg + "]";
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("EXEC SQL ROLLBACK", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			imcsLog.errorLog(methodName + "-E",ie.getClass().getName()+":"+ie.getMessage());
			throw new ImcsException(flag, errMsg, errCode);
		} catch(Exception e) {
			isLastProcess = ImcsConstants.RCV_MSG6;

			resultVO.setFlag("1");
			resultVO.setErrCode("99");
			resultVO.setErrMsg("incorrect failed !!!");
			
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			throw new ImcsException();
		} finally{
			
			resultVO.setResultCode(paramVO.getResultCode());
			
			msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID311) + "] return[" + flag +"|" + errMsg + "|" + paramVO.getBuyingDate() + "|" + errCode + "|]";
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID030) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
			
//			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID311) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
//					+ " [DR:" + nDRedis + "|DH:" + nDHbase + "|R:" + nRedis + "|H:" + nHbase + "]"; 
						
//			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultVO;
	}
	
    
    
    /**
     *  ????????? ??????, ???????????? ??????
     *  @param	BuyNSPresentRequestVO
     *  @result	BuyNSPresentRequestVO
     */
    public List<ComSbcVO> getCustomerInfo(BuyNSPresentRequestVO paramVO){
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();    	
    	
    	String sqlId = "lgvod311_001_20171214_001";
		List<ComSbcVO> list   = new ArrayList<ComSbcVO>();
		
		try {
			
			try{
				list = buyNSPresentDao.getCustomerInfo(paramVO);
			}catch(DataAccessException e){
				//DB?????? Exception?????? ??? getLastException??? ???????????? SQLException?????? ??????
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
		} catch (Exception e) {
			//imcsLog.failLog(ImcsConstants.API_PRO_ID311, sqlId, cache, "cust_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
    	return list;
    }
    
    
    
    
    
    /**
     *  ?????????????????? ??????
     *  @param	BuyNSPresentRequestVO
     *  @return	List<BuyNSPresentPriceVO>
     */
    public List<ComPriceVO> getBillTypeInfo(BuyNSPresentRequestVO paramVO){
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();    	
    	
    	String sqlId = "lgvod311_002_20171214_001";
		List<ComPriceVO> list = new ArrayList<ComPriceVO>();
		
		try {
			
			try{
				list = buyNSPresentDao.getBillType(paramVO);
			}catch(DataAccessException e){
				//DB?????? Exception?????? ??? getLastException??? ???????????? SQLException?????? ??????
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
		} catch (Exception e) {
			paramVO.setResultSet(-1);
//			imcsLog.failLog(ImcsConstants.API_PRO_ID311, sqlId, cache, "bill_type:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
    	return list;
    }
    
    
    /** 
     * ????????? ???????????? ?????? ??????
     * @param 	BuyNSPresentRequestVO
     * @return	int
     */
    public int CustomProductChk(BuyNSPresentRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId = "lgvod311_003_20171214_001";
		List<Integer> list   = new ArrayList<Integer>();
		Integer nCustomerProdYn = 0;
		
		try {
			
			try{
				list = buyNSPresentDao.CustomProductChk(paramVO);
			}catch(DataAccessException e){
				//DB?????? Exception?????? ??? getLastException??? ???????????? SQLException?????? ??????
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if(list == null || list.isEmpty()){
				nCustomerProdYn = 0;
			} else {
				nCustomerProdYn = (Integer) list.get(0);
			}
			
		} catch (Exception e) {
			paramVO.setResultSet(-1);
			nCustomerProdYn = 0;
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			//throw new ImcsException();
		}
		
    	return nCustomerProdYn;
    }
    
    
    
    
    /**
     * ?????? ????????? ????????? ?????? (PT_VO_BUY_DETAIL)
     * @param 	BuyNSPresentRequestVO
     * @return	void
     */
    public void insertBuyPresent2(BuyNSPresentRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
    	
    	String sqlId = "lgvod311_i02_20171214_001";
		String szMsg = "";
		int querySize = 0;
				    	
    	try {
    		
			try{
				querySize = buyNSPresentDao.insertBuyPresent2(paramVO);
			}catch(DataAccessException e){
				//DB?????? Exception?????? ??? getLastException??? ???????????? SQLException?????? ??????
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			try {
//				imcsLog.dbLog(ImcsConstants.API_PRO_ID311, sqlId, cache, querySize, methodName, methodLine);

				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID311) + "] insert [PT_VO_BUY_DETAIL] table[" + querySize + "] records Success at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			} catch (Exception e) {}
			
		} catch (Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID311) + "] insert [PT_VO_BUY_DETAIL] table Failed at";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
//			paramVO.setSqlCode(cache.getLastException().getErrorCode());
//			imcsLog.failLog(ImcsConstants.API_PRO_ID311, sqlId, cache, "fail info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
	}
    
    
    
    /**
     * ?????????????????? ?????? (PT_VO_BUY)
     * @param 	BuyNSPresentRequestVO
     * @return	void
     */
    public void insertBuyPresent1(BuyNSPresentRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
    	
    	String sqlId = "lgvod311_i01_20171214_001";
		String szMsg = "";
		int querySize = 0;
				    	
    	try {
			
			try{
				querySize = buyNSPresentDao.insertBuyPresent1(paramVO);
			}catch(DataAccessException e){
				//DB?????? Exception?????? ??? getLastException??? ???????????? SQLException?????? ??????
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			try {
//				imcsLog.dbLog(ImcsConstants.API_PRO_ID311, sqlId, cache, querySize, methodName, methodLine);

				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID311) + "] insert [PT_VO_BUY] table[" + querySize + "] records Success at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			} catch (Exception e) {}
			
		} catch (Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID311) + "] insert [PT_VO_BUY] table Failed at";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
//			imcsLog.failLog(ImcsConstants.API_PRO_ID311, sqlId, cache, "fail info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
	}
    
    
    
    /**
     *  ?????? ???????????? ?????? (PT_VO_PRESENT)
     *	@param 	BuyNSPresentRequestVO
     *	@return void
     */
    public void insertBuyPresentP(BuyNSPresentRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
    	
    	String sqlId = "lgvod311_i03_20171214_001";
    	String szMsg = "";
		int querySize = 0;
		
    	try {
			
			try{
				querySize = buyNSPresentDao.insertBuyPresentP(paramVO);
			}catch(DataAccessException e){
				//DB?????? Exception?????? ??? getLastException??? ???????????? SQLException?????? ??????
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			try {
//				imcsLog.dbLog(ImcsConstants.API_PRO_ID311, sqlId, cache, querySize, methodName, methodLine);

				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID311) + "] insert [PT_VO_PRESENT] table[" + querySize + "] records Success at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			} catch (Exception e) {}
			
		} catch (Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID311) + "] insert [PT_VO_PRESENT] table Failed at";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			
//			imcsLog.failLog(ImcsConstants.API_PRO_ID311, sqlId, cache, "fail info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
	}
    
    /**
     * FVOD??? ????????? PPV or PPS ????????? ?????? ????????? NPT_VO_BUY_META???????????? ????????? ????????????.    
     * @param vo
     * @return
     * @throws Exception
     */
    public Integer insBuyMeta(BuyNSPresentRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
    	Integer querySize = 0;		
		String szMsg = "";
		    	
    	try {    		
    		try{
    			// 2019.10.30 - VOD ?????? ???????????? ?????? : PPV ?????? ????????? ??????    			    		
				querySize = buyNSPresentDao.insBuyMeta(paramVO);
			    						
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID311) + "] insert [NPT_VO_BUY_META] table[" + querySize + "] records Success at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
					
			}catch(DataAccessException e){
				//DB?????? Exception?????? ??? getLastException??? ???????????? SQLException?????? ??????
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}		
		} catch (Exception e) {			
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID311) + "] insert [NPT_VO_BUY_META] table Failed at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);			
		}
		
		return querySize;
	}
}
