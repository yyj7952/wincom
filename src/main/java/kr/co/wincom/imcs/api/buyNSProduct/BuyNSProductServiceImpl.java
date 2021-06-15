package kr.co.wincom.imcs.api.buyNSProduct;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kr.co.wincom.imcs.api.buyNSDMConts.BuyNSDMContsRequestVO;
import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.nosql.NoSQLRedisDao;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.ComDupCHk;
import kr.co.wincom.imcs.common.vo.ComProdInfoVO;
import kr.co.wincom.imcs.common.vo.ComSbcVO;
import kr.co.wincom.imcs.handler.ImcsException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class BuyNSProductServiceImpl implements BuyNSProductService {
	private Log imcsLogger = LogFactory.getLog("API_buyNSProduct");
	
	@Autowired
	private BuyNSProductDao buyNSProductDao;
	
	@Autowired
	private NoSQLRedisDao noSQLRedisDao;
	
	@Autowired
	private CommonService commonService;

//	public void buyNSProduct(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	@Override
	public BuyNSProductResultVO buyNSProduct(BuyNSProductRequestVO paramVO){
//		this.buyNSProduct(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	

		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		imcsLog.debugLog(methodName + " service call");
		
		BuyNSProductResultVO resultVO = new BuyNSProductResultVO();
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		String flag		= "";
		String errCode	= "";
		String errMsg	= "";
		
	    int nDupChk = 0;
	    int couponCnt = 0;

	    Integer resultSet = 0;
	    Integer messageSet = 99;
	    
	    String msg	= "";
		
		long tp1 = 0;
		long tp2 = 0;
	    
		try{
			String szNscnId	= "";
			szNscnId	= this.getNscnId(paramVO);
			paramVO.setNscnId(szNscnId);
			
			List<String> freeCouponList   = new ArrayList<String>();
			// 2020.06.26 - 영화월정액 1개월 체험권
			freeCouponList = this.getFreeCouponId(paramVO);
			int errflag = 1;
			if(!paramVO.getOfferCd().startsWith("TEST"))
			{
				if(freeCouponList.size() > 0) {
					for(int i = 0; i < freeCouponList.size(); i++) {
						if (paramVO.getProdId().equals(freeCouponList.get(i))) {
							
							if(paramVO.getCouponFlag().equals("0")) {
								errflag = 1;
							} else {
								couponCnt = this.getFreeCouponCnt(paramVO);
								if(couponCnt > 0) {
									paramVO.setCouponCnt(couponCnt);
									errflag = 0;
								} else {
									errflag = 1;
								}
								break;
							}
							
						} else {
							if(paramVO.getCouponFlag().equals("1")) {
								errflag = 1;
							} else {
								paramVO.setCouponCnt(0);
								errflag = 0;
							}
						}
					}
					
					if(errflag != 0) {
						if(paramVO.getCouponFlag().equals("1")) {
							msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID005) + 
									"] 체험권 상품 구매 불가능, getCouponFlag : " + paramVO.getCouponFlag() + " getFreeCouponCnt : " + couponCnt;
							imcsLog.serviceLog(msg, methodName, methodLine);
						} else {
							msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID005) + 
									"] 체험권 상품이라 구매 불가능, getCouponFlag : " + paramVO.getCouponFlag() + " getFreeCouponCnt : " + couponCnt;
							imcsLog.serviceLog(msg, methodName, methodLine);
						}
						throw new ImcsException(resultVO.getFlag(), resultVO.getErrMsg(), resultVO.getErrCode(), resultVO);
					} else {
						if(paramVO.getCouponFlag().equals("1")) {
							msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID005) + 
									"] 체험권 상품 구매 가능, getCouponFlag : " + paramVO.getCouponFlag() + " getFreeCouponCnt : " + couponCnt;
							imcsLog.serviceLog(msg, methodName, methodLine);
						}
					}
				} else {
					if(paramVO.getCouponFlag().equals("1")) {
						msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID005) + 
								"] 체험권 상품 구매 불가능, getCouponFlag : " + paramVO.getCouponFlag() + " getFreeCouponCnt : " + couponCnt;
						imcsLog.serviceLog(msg, methodName, methodLine);
						throw new ImcsException(resultVO.getFlag(), resultVO.getErrMsg(), resultVO.getErrCode(), resultVO);
					}
				}
			} else {
				if(freeCouponList.size() > 0) {
					for(int i = 0; i < freeCouponList.size(); i++) {
						if (paramVO.getProdId().equals(freeCouponList.get(i))) {
							
							if(paramVO.getCouponFlag().equals("0")) {
								errflag = 1;
							} else {
								couponCnt = 1;
								paramVO.setCouponCnt(couponCnt);
								errflag = 0;
								break;
							}
							
						} else {
							if(paramVO.getCouponFlag().equals("1")) {
								errflag = 1;
							} else {
								paramVO.setCouponCnt(0);
								errflag = 0;
							}
						}
					}
					if(errflag != 0) {
						if(paramVO.getCouponFlag().equals("1")) {
							msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID005) + 
									"] 체험권 상품 구매 불가능, getCouponFlag : " + paramVO.getCouponFlag() + " getFreeCouponCnt : " + couponCnt;
							imcsLog.serviceLog(msg, methodName, methodLine);
						} else {
							msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID005) + 
									"] 체험권 상품이라 구매 불가능, getCouponFlag : " + paramVO.getCouponFlag() + " getFreeCouponCnt : " + couponCnt;
							imcsLog.serviceLog(msg, methodName, methodLine);
						}
						throw new ImcsException(resultVO.getFlag(), resultVO.getErrMsg(), resultVO.getErrCode(), resultVO);
					} else {
						if(paramVO.getCouponFlag().equals("1")) {
							msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID005) + 
									"] 체험권 상품 구매 가능, getCouponFlag : " + paramVO.getCouponFlag() + " getFreeCouponCnt : " + couponCnt;
							imcsLog.serviceLog(msg, methodName, methodLine);
						}
					}
				} else {
					if(paramVO.getCouponFlag().equals("1")) {
						msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID005) + 
								"] 체험권 상품 구매 불가능, getCouponFlag : " + paramVO.getCouponFlag() + " getFreeCouponCnt : " + couponCnt;
						imcsLog.serviceLog(msg, methodName, methodLine);
						throw new ImcsException(resultVO.getFlag(), resultVO.getErrMsg(), resultVO.getErrCode(), resultVO);
					}
				}
			}
			
			ComProdInfoVO prodInfoVO	= null;	
			prodInfoVO	= this.getProdInfo(paramVO);			
			
			if(prodInfoVO != null) {
				paramVO.setProdGb(prodInfoVO.getProdGb());
				paramVO.setExpiCd(prodInfoVO.getExpiCd());
				paramVO.setProdType(prodInfoVO.getProdType());
				paramVO.setExpiredTime(prodInfoVO.getExpiredTime());
				
				// 통계용 정보 Set
				resultVO.setProductName(prodInfoVO.getProdName());
				resultVO.setProductPrice(prodInfoVO.getPrice());
				
				// 2019.11.01 - VOD 정산 프로세스 개선 : 메타에 저장할 변수 Set
				paramVO.setProductName(prodInfoVO.getProdName());
				paramVO.setProductKind(prodInfoVO.getProdKind());
			}
			
			tp1 = System.currentTimeMillis();
			
			// 상태, 개통여부 및 쿠폰값 가져오기
			String szBuyingDate = "";
	    	try {
	    		szBuyingDate = commonService.getSysdate();
	    		paramVO.setBuyingDate(szBuyingDate);
	    	} catch (Exception e) {
	    		imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage() + "," + methodName + "_getSysdate()");
			}
	    	
	    	List<ComSbcVO> lCustomerInfo = new ArrayList<ComSbcVO>();
	    	ComSbcVO customerVO = new ComSbcVO();
	    	try {
	    		lCustomerInfo = this.getCustomerInfo(paramVO);
	    		
	    		if(lCustomerInfo == null || lCustomerInfo.size() == 0) {
	    			resultSet	= -1;
	    			messageSet = 10;
	    		} else {
	    			customerVO	= lCustomerInfo.get(0);
	    			
	    			if( !"Y".equals(customerVO.getStatusFlag()) ){
						resultSet = -1;
						messageSet = 11;
					}
					// 개통여부 체크
					if(resultSet == 0){
						if( "N".equals(customerVO.getYnVodOpen()) ){
							resultSet = -1;
							messageSet = 12;
						}
					}
	    		}
			} catch (Exception e) {
				resultSet	= -1;
				messageSet = 10;
			}
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("가입자 상태, 개통여부 조회", String.valueOf(tp2 - tp1), methodName, methodLine); 
			
			
			// 기존 구매내역 체크
			tp1 = System.currentTimeMillis();
			if(resultSet == 0) {
				if (paramVO.getCouponFlag().equals("0")) {
					// 현재는  31060(1일), 31050(7일), 31040(30일) 상품들만 들어온다
					nDupChk = this.getBuyDupChk(paramVO);
				} else {
					nDupChk = this.getBuyFreeCouponChk(paramVO);
				}
				
				resultSet	= paramVO.getResultSet();
				if(resultSet == -1){
					messageSet = 22;
				}
	
				if(nDupChk > 0){	// 기존 구매내역 존재 시
					resultSet = -1;
					messageSet = 24;
				}
			}
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("기존 구매내역 조회", String.valueOf(tp2 - tp1), methodName, methodLine); 


			// 구매내역 저장
			if(resultSet == 0){
				tp1 = System.currentTimeMillis();
				
				Integer result = 0;
				
				result = this.insertBuyProduct(paramVO);
		    	
		    	if(result > 0)    		resultSet = 0;
		    	else		    		resultSet = -1;
		    	
		    	if(resultSet == -1){
		    		resultSet = -1;
                    messageSet = 40;
		    	}
		    	
		    	tp2	= System.currentTimeMillis();
				imcsLog.timeLog("구매내역 생성 완료", String.valueOf(tp2 - tp1), methodName, methodLine); 
			}
			
			// 2019.10.30 - VOD 정산 프로세스 개선 : NPT_VO_BUY_META 테이블 INSERT
			// PT_VO_NSC_PRODUCT 테이블 기준으로 데이터를 저장한다.
			if(resultSet == 0)
			{
				if(this.insBuyMeta(paramVO) > 0) {
		    		resultSet	= 0;
		    	} else {
		    		resultSet	= -1;
	                messageSet	= 40;
		    	}
			}
						
			// 리턴값을 지정하여 리턴처리
		    if(resultSet == 0){
		    	tp1	= System.currentTimeMillis();
		    	
		    	resultVO.setFlag("0");
		    	resultVO.setErrMsg("insert success");
		    	resultVO.setBuyingDate(paramVO.getBuyingDate());
		    	
		    	tp2	= System.currentTimeMillis();
		    	imcsLog.timeLog("EXEC SQL COMMIT", String.valueOf(tp2 - tp1), methodName, methodLine); 
		    	
		    }else{
		    	paramVO.setMessageSet(messageSet);

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
			} else {
				errCode	= "99";
				errMsg	= "incorrect failed !!!";
			}
			
			if(messageSet == 24) {
				flag	= "2";
				errMsg	= "이미 가입 또는 구매하셨습니다.";
				resultVO.setBuyingDate(paramVO.getBuyDate());
				errCode	= "";
			}
			
			resultVO.setFlag(flag);
			resultVO.setErrCode(errCode);
			resultVO.setErrMsg(errMsg);
			
			msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID005) + "] msg[" + errMsg + "]";
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
			
			msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID005) + "] return[" + resultVO.getFlag() +"|" + resultVO.getErrMsg() + "|" + paramVO.getBuyingDate() + "|" + resultVO.getErrCode() + "|]";
			imcsLog.serviceLog(msg, methodName, methodLine);
			
//			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID005) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
//					+ " [DR:" + nDRedis + "|DH:" + nDHbase + "|R:" + nRedis + "|H:" + nHbase + "]"; 
						
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultVO;
	}
	
	
	/**
	 * NSCN ID 조회
	 * @param paramVO
	 * @return
	 */
	public String getNscnId(BuyNSProductRequestVO paramVO){
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();    	
    	
    	String sqlId = "lgvod005_001_20171214_001";
    	String szNscnId	= "";
		List<String> list   = new ArrayList<String>();
		
		try {
			
			try{
				list = buyNSProductDao.getNscnId(paramVO);
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list == null || list.isEmpty()){
				szNscnId	= "";
			} else {
				szNscnId	= StringUtil.nullToSpace((list.get(0)));
			}
						
		} catch (Exception e) {
//			imcsLog.failLog(ImcsConstants.API_PRO_ID005, sqlId, cache, "product_info:" + ImcsConstants.RCV_MSG2, methodName, methodLine);

		}
		return szNscnId;
    }
	
	
	
	/**
	 * 상품 정보 조회
	 * @param paramVO
	 * @return
	 */
	public ComProdInfoVO getProdInfo(BuyNSProductRequestVO paramVO){
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();    	
    	
    	String sqlId = "lgvod005_002_20171214_001";
		List<ComProdInfoVO> list   = new ArrayList<ComProdInfoVO>();
		ComProdInfoVO prodInfoVO = new ComProdInfoVO();
		
		try {
			
			try{
				list = buyNSProductDao.getProdInfo(paramVO);
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list != null && !list.isEmpty()){
				prodInfoVO = list.get(0);
			}
									
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			throw new ImcsException();
		}
		
		return prodInfoVO;
    }
	
  
    
    
    /**
     * 가입자 정보 조회
     * @param	BuyNSProductRequestVO
     * @result	List<CommonSbcVO>
     */
    public List<ComSbcVO> getCustomerInfo(BuyNSProductRequestVO paramVO){
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();    	
    	
    	String sqlId =  "lgvod005_003_20171214_001";		
		List<ComSbcVO> list   = new ArrayList<ComSbcVO>();
		
		try {
			
			try{
				list = buyNSProductDao.getCustomerInfo(paramVO);
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
						
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
    	return list;
    }
    
    
    
    /**
     *	구매 중복 체크
     *	@param	BuyNSProductRequestVO
     *	@result	Integer
     */
    public Integer getBuyDupChk(BuyNSProductRequestVO paramVO){
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();    	
    	
    	String sqlId = "lgvod005_004_20171214_001";
		int querySize = 0;
		List<ComDupCHk> list   = new ArrayList<ComDupCHk>();
		ComDupCHk dupChkVO = new ComDupCHk();
		
		Integer nDupChk = 0;
		try {
			
			
			try {
				list = buyNSProductDao.getBuyDupChk(paramVO);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if( list == null || list.isEmpty()){
				querySize = 0;
				list = null;
				
				paramVO.setResultSet(-1);
			} else {
				querySize = list.size();
				dupChkVO = list.get(0);
				
				paramVO.setBuyDate(dupChkVO.getBuyDate());
    			nDupChk = Integer.parseInt(StringUtil.nullToZero(dupChkVO.getDataChk()));
			}
			
			try {
//				imcsLog.dbLog(ImcsConstants.API_PRO_ID005, sqlId, cache, querySize, methodName, methodLine);
			} catch (Exception e) {}
			
			paramVO.setResultSet(0);
						
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			//throw new ImcsException();
			
			paramVO.setResultSet(-1);
		}
		
    	return nDupChk;
    }
    
    
    /**
     *	체험권 상품 구매 중복 체크
     *	@param	BuyNSProductRequestVO
     *	@result	Integer
     */
    public Integer getBuyFreeCouponChk(BuyNSProductRequestVO paramVO){
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();    	
    	
		int querySize = 0;
		List<ComDupCHk> list   = new ArrayList<ComDupCHk>();
		ComDupCHk dupChkVO = new ComDupCHk();
		
		Integer nDupChk = 0;
		try {
			
			
			try {
				list = buyNSProductDao.getBuyFreeCouponChk(paramVO);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if( list == null || list.isEmpty()){
				querySize = 0;
				list = null;
				
				paramVO.setResultSet(-1);
			} else {
				querySize = list.size();
				dupChkVO = list.get(0);
				
				paramVO.setBuyDate(dupChkVO.getBuyDate());
    			nDupChk = Integer.parseInt(StringUtil.nullToZero(dupChkVO.getDataChk()));
			}
			
			try {
//				imcsLog.dbLog(ImcsConstants.API_PRO_ID005, sqlId, cache, querySize, methodName, methodLine);
			} catch (Exception e) {}
			
			paramVO.setResultSet(0);
						
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			//throw new ImcsException();
			
			paramVO.setResultSet(-1);
		}
		
    	return nDupChk;
    }
    
    
    /**
     *	구매내역 등록
     * 	@param	BuyNSProductRequestVO
     * 	@result	Integer
     */
    public Integer insertBuyProduct(BuyNSProductRequestVO paramVO){
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
    	
    	String sqlId =  "lgvod005_i01_20171214_001";
    	Integer querySize = 0;
		String szMsg = "";
		    	
    	try {
			
			try{
				querySize = buyNSProductDao.insertBuyProduct(paramVO);
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			try {				
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID005) + "] insert [PT_VO_NSC_PRODUCT] table[" + querySize + "] records Success at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
					
			} catch (Exception e) {}
			
		} catch (Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID005) + "] insert [PT_VO_NSC_PRODUCT] table Failed at";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
//			imcsLog.failLog(ImcsConstants.API_PRO_ID005, sqlId, cache, "fail info:" + ImcsConstants.RCV_MSG2, methodName, methodLine);
		}
		
		return querySize;
	}


    /**
     * PT_VO_NSC_PRODUCT 테이블 기준으로 데이터를 저장한다.    
     * @param vo
     * @return
     * @throws Exception
     */
    public Integer insBuyMeta(BuyNSProductRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
    	Integer querySize = 0;		
		String szMsg = "";
		    	
    	try {    		
    		try{    			
    				querySize = buyNSProductDao.insBuyMeta(paramVO);
    			    						
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID005) + "] insert [NPT_VO_BUY_META] table[" + querySize + "] records Success at";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}		
		} catch (Exception e) {			
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID005) + "] insert [NPT_VO_BUY_META] table Failed at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);			
		}
		
		return querySize;
	}
    
    
    /**
	 * 체험권 상품 조회
	 * @param paramVO
	 * @return
	 */
	public List<String> getFreeCouponId(BuyNSProductRequestVO paramVO){
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodName = oStackTrace.getMethodName();
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
    	
		String msg				= "";
		List<String> list   = new ArrayList<String>();

		try {
			
			try{
				try
	        	{
					list = buyNSProductDao.getFreeCouponId(paramVO);
	        	}catch(Exception e)
	        	{
	        		msg	= "[getFreeCouponId Error : " + e.getMessage() + "]"; 								
					imcsLog.serviceLog(msg, methodName, methodLine);
	        	}
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
						
		} catch (Exception e) {	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}
		
		return list;
    }
	
	
	/**
	 * 가입자 체험권 상품 쿠폰 조회
	 * @param paramVO
	 * @return
	 */
	public int getFreeCouponCnt(BuyNSProductRequestVO paramVO){
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodName = oStackTrace.getMethodName();
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
    	
		String msg = "";
		int result = 0;
		try {
			
			try{
				try
	        	{
					result = buyNSProductDao.getFreeCouponCnt(paramVO);
	        	}catch(Exception e)
	        	{
	        		msg	= "[getFreeCouponCnt Error : " + e.getMessage() + "]"; 								
					imcsLog.serviceLog(msg, methodName, methodLine);
	        	}
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
						
		} catch (Exception e) {	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}
		
		return result;
    }
	      
}
