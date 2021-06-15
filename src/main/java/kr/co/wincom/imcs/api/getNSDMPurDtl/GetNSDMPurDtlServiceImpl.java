package kr.co.wincom.imcs.api.getNSDMPurDtl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import kr.co.wincom.imcs.common.NosqlCacheType;
import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.handler.ImcsException;

@Service
public class GetNSDMPurDtlServiceImpl implements GetNSDMPurDtlService {
	private Log imcsLogger		= LogFactory.getLog("API_getNSDMPurDtl");
	
	@Autowired
	private GetNSDMPurDtlDao getNSDMPurDtlDao;
	
	@Autowired
	private CommonService commonService;
	
//	public void getNSContList(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	/**
	 * @author HONG
	 * @since 2016-07-12
	 * 컨텐츠 타입과 구매여부, 구매시간, 구매타입 값을 리턴 (lgvod997.pc)
	 */
	@Override
	public GetNSDMPurDtlResultVO getNSDMPurDtl(GetNSDMPurDtlRequestVO paramVO)	{
//		this.getNSContList(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		String flag		= "";
		String errCode	= "";
		String errMsg	= "";
		String resultCode	= "";
		
		int szSurtaxRate = 10; /* 부가세 요율 */
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		GetNSDMPurDtlResponseVO	resultVO	= new GetNSDMPurDtlResponseVO();
		GetNSDMPurDtlResultVO	resultListVO	= new GetNSDMPurDtlResultVO();
		
		long tp1			= 0;		// timePoint 1
		long tp2			= 0;		// timePoint 2
		
		String msg			= "";
		
		Integer resultSet = -1;
	    Integer messageSet = 99;
	    
		try {
			// 부가세 요율
			szSurtaxRate = commonService.getSurtaxRate();
			paramVO.setSurtaxRate(szSurtaxRate);
				
			tp1	= System.currentTimeMillis();
			
			resultVO = this.getBuyDmDetail(paramVO);
			
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("구매 상세 내역 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			resultSet = paramVO.getResultSet();
						
			/* 리턴값을 지정하여 리턴처리 */
		    if(resultSet != 0){
		    	resultVO	= new GetNSDMPurDtlResponseVO();
		    }else{
		    	resultVO.setBuyingDate(paramVO.getBuyingDate());
		    }
		    
		    resultVO.setContsId(paramVO.getContsId());
		    
		    resultListVO.setResultVO(resultVO);
			
		} catch(ImcsException ie) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			
			imcsLog.errorLog(methodName + "-E",ie.getClass().getName()+":"+ie.getMessage());
			
		} catch(Exception e) {
			isLastProcess	= ImcsConstants.RCV_MSG6;
			
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			
		} finally{
			resultListVO.setResultCode(paramVO.getResultCode());
			
			msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID015) + "] result[" + resultListVO.toString() + "]"; 
			
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID015) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultListVO;
	}
	
	/**
	 * 구매 상세 정보 조회
	 * @param paramVO
	 * @return
	 * @throws Exception
	 */
    public GetNSDMPurDtlResponseVO getBuyDmDetail(GetNSDMPurDtlRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		
		String szMsg = "";
    	
    	String sqlId = "nsvod015_s01_20181029_001";
    	
    	if(paramVO.getnBuyYn().equals("Y")) // 엔스크린
    		sqlId = "nsvod015_s02_20181029_001";

		List<GetNSDMPurDtlResponseVO> list   = new ArrayList<GetNSDMPurDtlResponseVO>();
		GetNSDMPurDtlResponseVO tempVO = null;
		GetNSDMPurDtlResponseVO resultVO = new GetNSDMPurDtlResponseVO();
		
		int delimiter_cnt = 0;
		
		String real_price = "";
		String[] real_price_arr = null;
		
		String real_df_price = "";
		String[] real_df_price_arr = null;
		
		String conts_id = paramVO.getContsId();
		String df_conts_id = paramVO.getContsId()+"_D";
		
		try {
			try{
				if(paramVO.getnBuyYn().equals("N"))
					list = getNSDMPurDtlDao.getBuyDmDetail(paramVO);
				else // 엔스크린
					list = getNSDMPurDtlDao.getBuyDmDetail_iptv(paramVO);
				
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			} catch(DataAccessException e) {
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list == null || list.isEmpty()){
				
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID015) + "] sts[0] "
						+ String.format("%-21s", "msg[BUY_DM_DETAIL INFO: Not Found ]");
				IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
				imcsLog.serviceLog(szMsg, methodName, methodLine);
				
				paramVO.setResultSet(-1);
								
			} else {

				for(int i = 0; i < list.size(); i++){
					
					delimiter_cnt = 0;
					
					tempVO = list.get(i);
					
					if(paramVO.getnBuyYn().equals("N"))
					{
						if( conts_id.equals( tempVO.getProductId() ) ){
							
							switch (tempVO.getDmUseFlag()) {
							case "N":
							case "A":
							case "W":
							case "T":
							// 2021.02.24 - 네이버/카카오페이 결제 수단 추가
							case "B":
							case "F":
								
								resultVO.setBuyingType(tempVO.getDmUseFlag());
								resultVO.setBuyingPrice(tempVO.getDmPrice());
								resultVO.setSuggestedPrice(tempVO.getBuyAmt());
								real_price = tempVO.getRealPrice();
								
								// split 을 그냥 쓰면 1|2|| 이렇게 공백이 있는 경우 공백을 제거하고 2개만 반환함.
								// splitPreserveAllTokens 을 사용하면 1|| => 3개 반환, 1|2|3| => 4개 반환됨.
								real_price_arr = StringUtils.splitPreserveAllTokens(real_price, "|");
								
								if(real_price_arr.length >= 3)
									resultVO.setRealDatafreeSuggestedPrice(real_price_arr[2]);
								
								break;
							case "K":
							case "I":
								resultVO.setDiscountCoupon(tempVO.getDmPrice());
								
								if (tempVO.getIdolBuyYn().equals("Y")) { // 유료콘서트 쿠폰 구매 취소 링크 RUL
									resultVO.setCancelLinkUrl(tempVO.getCancelLinkUrl());
								}
								
								break;
								
							case "H":
								resultVO.setDiscountMembership(tempVO.getDmPrice());
								break;
								
							case "P":
							case "Q":
								resultVO.setDiscountTvpoint(tempVO.getDmPrice());
								break;
								
							case "L":
								resultVO.setDiscountKlupoint(tempVO.getDmPrice());
								break;
	
							default:
								paramVO.setResultSet(-1);
								break;
							}
							
						}else if( df_conts_id.equals( tempVO.getProductId() ) ){
							
							switch (tempVO.getDmUseFlag()) {
							case "N":
							case "A":
							case "W":
							case "T":
							// 2021.02.24 - 네이버/카카오페이 결제 수단 추가
							case "B":
							case "F":
								
								resultVO.setDatafreeBuyingType(tempVO.getDmUseFlag());
								resultVO.setDatafreeBuyingPrice(tempVO.getDmPrice());
								resultVO.setDatafreeSuggestedPrice(tempVO.getBuyAmt());
								real_df_price = tempVO.getRealPrice();
								
								// split 을 그냥 쓰면 1|2|| 이렇게 공백이 있는 경우 공백을 제거하고 2개만 반환함.
								// splitPreserveAllTokens 을 사용하면 1|| => 3개 반환, 1|2|3| => 4개 반환됨.
								real_df_price_arr = StringUtils.splitPreserveAllTokens(real_df_price, "|");
								
								if(real_df_price_arr.length >= 3)
									resultVO.setRealSuggestedPrice(real_df_price_arr[2]);
									//resultVO.setRealDatafreeSuggestedPrice(real_df_price_arr[2]);
								
								break;
							case "K":
							case "I":
								resultVO.setDatafreeDiscountCoupon(tempVO.getDmPrice());
								break;
								
							case "H":
								resultVO.setDatafreeDiscountMembership(tempVO.getDmPrice());
								break;
								
							case "P":
							case "Q":
								resultVO.setDatafreeDiscountTvpoint(tempVO.getDmPrice());
								break;
								
							case "L":
								resultVO.setDatafreeDiscountKlupoint(tempVO.getDmPrice());
								break;
	
							default:
								paramVO.setResultSet(-1);
								break;
							}
							
						}
					}
					else // 엔스크린
					{
						String tempProductId = tempVO.getProductId();
						
						if(conts_id.length() > 15)
							conts_id = conts_id.substring(0, 15);
						
						if(tempProductId.length() > 15)
							tempProductId = tempProductId.substring(0, 15);
						
						if(conts_id.equals(tempProductId))
						{
							switch(tempVO.getDmUseFlag())
							{
								
								case "N":
								case "W":
								case "S":
									resultVO.setBuyingType(tempVO.getDmUseFlag());
									resultVO.setBuyingPrice(tempVO.getDmPrice());
									resultVO.setSuggestedPrice(tempVO.getBuyAmt());
									real_price = tempVO.getRealPrice();
									
									// split 을 그냥 쓰면 1|2|| 이렇게 공백이 있는 경우 공백을 제거하고 2개만 반환함.
									// splitPreserveAllTokens 을 사용하면 1|| => 3개 반환, 1|2|3| => 4개 반환됨.
									real_price_arr = StringUtils.splitPreserveAllTokens(real_price, "|");
									
									if(real_price_arr.length >= 3)
										resultVO.setRealSuggestedPrice(real_df_price_arr[2]);
										//resultVO.setRealSuggestedPrice(real_price_arr[2]);
									
									break;
								case "K":
								case "I":
									resultVO.setDiscountCoupon(tempVO.getDmPrice());
									break;
								case "H":
									resultVO.setDiscountMembership(tempVO.getDmPrice());
									break;
								case "P":
								case "Q":
									resultVO.setDiscountTvpoint(tempVO.getDmPrice());
									break;
								default:
									paramVO.setResultSet(-1);
									break;
							}
						}
					} // 엔스크린 END
				}
				
				// 엔스크린 인 경우
				if(paramVO.getnBuyYn().equals("Y"))
				{
					if(StringUtils.isBlank(resultVO.getRealSuggestedPrice()))
					{
						String strSuggestedPrice = resultVO.getSuggestedPrice();
						String strDiscountCoupon = resultVO.getDiscountCoupon();
						String strDiscountMembership = resultVO.getDiscountMembership();
						String strDiscountTvpoint = resultVO.getDiscountTvpoint();
						
						if(StringUtils.isBlank(strSuggestedPrice))
							strSuggestedPrice = "0";
						
						if(StringUtils.isBlank(strDiscountCoupon))
							strDiscountCoupon = "0";
						
						if(StringUtils.isBlank(strDiscountMembership))
							strDiscountMembership = "0";
						
						if(StringUtils.isBlank(strDiscountTvpoint))
							strDiscountTvpoint = "0";
						
						resultVO.setRealSuggestedPrice(
							String.format("%.0f", 
								(
									Integer.parseInt(strSuggestedPrice)
									- Integer.parseInt(strDiscountCoupon)
									- Integer.parseInt(strDiscountMembership)
									- Integer.parseInt(strDiscountTvpoint)
								)
								+
								(
									(double)(
										Integer.parseInt(strSuggestedPrice)
										- Integer.parseInt(strDiscountCoupon)
										- Integer.parseInt(strDiscountMembership)
									)
									/
									((double)100 / paramVO.getSurtaxRate())
								)
							)
						);
					}
				}
			}
			
		} catch (Exception e) {
//			if(cache.getLastException() != null)	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
//			else									paramVO.setResultCode("41000000");			
			
			paramVO.setResultSet(-1);
			
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID015, "", cache, "BUY_DM_DETAIL INFO:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID015) + "] SQLID[" + sqlId + "] sts[" + "] "
					+ String.format("%-21s", "msg[BUY_DM_DETAIL INFO:" + ImcsConstants.RCV_MSG6 + "]");
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.serviceLog(szMsg, methodName, methodLine);
		}
		
    	return resultVO;
    }
}






















