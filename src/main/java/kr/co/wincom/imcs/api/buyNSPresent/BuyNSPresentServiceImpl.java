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
			// 상태, 개통여부 및 쿠폰값 조회
			
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
				imcsLog.timeLog("가입자 상태, 개통여부 조회", String.valueOf(tp2 - tp1), methodName, methodLine); 
				
				messageSet = 10;
			}

			
			List<ComPriceVO> lPriceVO = new ArrayList<ComPriceVO>();
			ComPriceVO priceInfoVO = new ComPriceVO();

			if(resultSet == 0){
				if( "Y".endsWith(paramVO.getCpUseYn()) ){		// 신규쿠폰으로 선물하기 불가능
					messageSet = 62;
				}
				
				tp2	= System.currentTimeMillis();
				imcsLog.timeLog("가입자 상태, 개통여부 조회", String.valueOf(tp2 - tp1), methodName, methodLine); 
				
				tp1 = System.currentTimeMillis();
				
				
				// 상품 가격정보 (정액/종량) 조회
				lPriceVO	= this.getBillTypeInfo(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				resultSet = paramVO.getResultSet();
				
				if(lPriceVO == null || lPriceVO.size() == 0) {
					resultSet = -1;
					messageSet	= 13;
				} else {	
					priceInfoVO = lPriceVO.get(0);
					
					paramVO.setContsGenre(priceInfoVO.getContsGenre());
										
					// 2019.10.29 - NPT_VO_BUY_META 공통 변수 Set
					// 선물하기는 PPV 선물밖에 없다.
					paramVO.setProdType(priceInfoVO.getProductType());		// 2019.10.29 - 기존에 PRODUCT_TYPE을 가져오지 않아서 추가
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
				imcsLog.timeLog("상품 정보(정액/종량) 조회", String.valueOf(tp2 - tp1), methodName, methodLine); 
				
				
				if(!"Y".equals(customerVO.getStatusFlag())){
					resultSet = -1;
					messageSet = 11;
				}
				
				if(resultSet == 0){
					// 개통여부 조회
					if( "N".equals(customerVO.getYnVodOpen()) ){
						// FVOD만 사용 가능 (F가 아닐 경우 실패)
						if(!"F".equals(priceInfoVO.getBillType())){
							resultSet = -1;
							messageSet = 12;
						}
					}
				}
			}
			
			
			// 가입자 구매 가능 상품 여부 조회
			if(resultSet == 0 && !"F".equals(priceInfoVO.getBillType()) ){
				tp1 = System.currentTimeMillis();
				
				int nCustomerProdYn = this.CustomProductChk(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
				tp2	= System.currentTimeMillis();
				imcsLog.timeLog("가입자가 구입한 상품 조회", String.valueOf(tp2 - tp1), methodName, methodLine); 
				
				resultSet = paramVO.getResultSet();
				
				if(resultSet == -1){
					messageSet = 14;
				}
				
				// 가입자가 구매한 상품이 아니면 FVOD만 사용 가능
				if(nCustomerProdYn == 0){
					if( !"F".equals(priceInfoVO.getBillType()) ){
						resultSet = -1;
						messageSet = 9;
					}
				}
			}
			
			
			// 상품 타입이 1이 아니면 실패
			//if( resultSet == 0 && !"1".equals( priceInfoVO.getProductType() ) ){
			if(!"1".equals( priceInfoVO.getProductType() ) ){
				resultSet = -1;
				messageSet = 62;
			}
			
			//----------------------------------------------------------------------------
			// 기존 구매 내역 조회 로직
		    // - 기존 구매 내역이 존재하는 경우, 
		    // 1. 구매금액은 선물금액(Prezent_price) 으로 선물 구매를 한다
		    // 2. 선물금액(Prezent_price)이 존재하지 않는 경우 
		    //    일반금액(c_suggested_price) 으로 선물구매를 한다 
		    // - 기존 구매 내역이 존재하지 않는 경우
		    // 1. 일반금액(c_suggested_price) 으로 선물구매를 한다 
		    //----------------------------------------------------------------------------
			if(resultSet == 0){
				tp1	= System.currentTimeMillis();
				
				HashMap<String, Object> mPresentDupCk = new HashMap<String, Object>();
				Integer nDupCk	= 0;
				// 기존구매 여부 체크 (중복체크)
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
				
				
				// 기존에 데이타가 있으면
				if(nDupCk > 0){
					//----------------------------------------------------------------------------
		            // 가격정보 확인 (선물금액)
		            //----------------------------------------------------------------------------
		            //기존에 구매한 컨텐츠를 선물하는 경우 할인금액 으로 구입 한다
					if(!priceInfoVO.getPresentPrice().equals(paramVO.getPresentPrice())){
						resultSet	= -1;
						messageSet	= 45;
					}else{
						msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID311) + "] 선물 할인율구매 - price[" + paramVO.getPresentPrice() + " -> " +priceInfoVO.getPresentPrice() + "]";
						imcsLog.serviceLog(msg, methodName, methodLine);
					}
					
				}else{
					// 기존 구매 내역이 없는 경우 선물가격은 suggested_price로 한다
					priceInfoVO.setPresentPrice(priceInfoVO.getSuggestedPrice());
					
					//----------------------------------------------------------------------------
		            // 가격정보 확인 (이벤트금액, 일반금액)
		            //----------------------------------------------------------------------------
		            // PPV상품					
					if(Integer.parseInt( priceInfoVO.getEventValue() ) > 0){
						msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID311) + "] 할인율구매 - price[" + priceInfoVO.getSuggestedPrice() + " -> " +priceInfoVO.getEventPrice() + "]";
						imcsLog.serviceLog(msg, methodName, methodLine);
						priceInfoVO.setPresentPrice(priceInfoVO.getEventPrice());
					}else{
						if( !priceInfoVO.getSuggestedPrice().equals( paramVO.getBuyingPrice() ) ){
							msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID311) + "] 일반PPV - incorrect price[imcs_price=" + priceInfoVO.getSuggestedPrice() + "][stb_price=" + paramVO.getBuyingPrice() + "]";
							imcsLog.serviceLog(msg, methodName, methodLine);
							
							resultSet = -1;
                            messageSet = 45;
						}
					}
					
				}
			}
			
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("기존 구매내역 조회", String.valueOf(tp2 - tp1), methodName, methodLine); 
			
			
			
			//----------------------------------------------------------------------------
		    // 선물 컨텐츠 보관함 저장 : PT_VO_BUY_DETAIL
		    //----------------------------------------------------------------------------
		    if(resultSet == 0){
		    	paramVO.setContsGenre(priceInfoVO.getContsGenre());
		    	
		    	tp1	= System.currentTimeMillis();
		    	
		    	try {
		    		// 단품의 경우 구매내역 저장
		    		this.insertBuyPresent2(paramVO);
		    		paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				} catch (Exception e) {
					resultSet	= -1;
					messageSet	= 32;
					
					if(paramVO.getSqlCode() == -1)		messageSet = 24;
					
					imcsLog.errorLog(methodName + "-E",e.getClass().getName() + ":" + e.getMessage() + "," + methodName + "_insertBuyPresent2()");
				}
		    	tp2	= System.currentTimeMillis();
		    	imcsLog.timeLog("선물 컨텐츠 보관함 저장", String.valueOf(tp2 - tp1), methodName, methodLine); 
		    }
		    
		    if(priceInfoVO.getContsGenre() == ""){
		    	priceInfoVO.setContsGenre("기타");
		    }
		    
		    //----------------------------------------------------------------------------
		    // 선물 구매내역 생성 완료 : PT_VO_BUY
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
		    	imcsLog.timeLog("선물 구매내역 생성 완료", String.valueOf(tp2 - tp1), methodName, methodLine); 
		    }
		    
		    //----------------------------------------------------------------------------
		    // 선물 구매내역 생성 완료
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
		    	imcsLog.timeLog("선물 구매내역 생성 완료", String.valueOf(tp2 - tp1), methodName, methodLine); 
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
		 			
		    // 데이터 INSERT (스프링은 default가 autocommit이 아니므로 rollback 구현 부분이 무의미하다)
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
     *  가입자 상태, 개통여부 조회
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
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
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
     *  상품가격정보 조회
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
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
		} catch (Exception e) {
			paramVO.setResultSet(-1);
//			imcsLog.failLog(ImcsConstants.API_PRO_ID311, sqlId, cache, "bill_type:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
    	return list;
    }
    
    
    /** 
     * 가입자 구매상품 여부 조회
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
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
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
     * 선물 컨텐츠 보관함 저장 (PT_VO_BUY_DETAIL)
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
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
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
     * 선물구매내역 저장 (PT_VO_BUY)
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
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
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
     *  보낸 선물내역 저장 (PT_VO_PRESENT)
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
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
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
     * FVOD를 제외한 PPV or PPS 구매시 메타 정보를 NPT_VO_BUY_META테이블에 별도로 저장한다.    
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
    			// 2019.10.30 - VOD 정산 프로세스 개선 : PPV 메타 데이터 저장    			    		
				querySize = buyNSPresentDao.insBuyMeta(paramVO);
			    						
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID311) + "] insert [NPT_VO_BUY_META] table[" + querySize + "] records Success at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
					
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}		
		} catch (Exception e) {			
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID311) + "] insert [NPT_VO_BUY_META] table Failed at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);			
		}
		
		return querySize;
	}
}
