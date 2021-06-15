package kr.co.wincom.imcs.api.buyNSProduct;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

@SuppressWarnings("serial")
public class BuyNSProductRequestVO  extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * BuyNSProduct API 전문 칼럼(순서 일치)
	********************************************************************/
	private String saId = "";
    private String stbMac = "";
    private String prodId = "";		// 2019.10.29 - NPT_VO_BUY_META 에도 사용
    private String appType = "";
    
    /********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
    private String pid		= "";
    private String resultCode = "";
    
    private Integer resultSet = 0;
    private Integer messageSet = 0;
    
    private long tp_start = 0;

    private String nscnId = "";
    private String expiCd = "";
    private String prodGb = "";		// 2019.10.29 - NPT_VO_BUY_META 에도 사용, 메타저장시에는 (1 : 2400 / 2 : 16800 / 3 : 72000) 으로 저장 
    private String prodType = "";		// 2019.10.29 - NPT_VO_BUY_META 에도 사용
    private String expiredTime = "";
    private String buyingDate = "";		// 2019.10.29 - NPT_VO_BUY_META 에도 사용
    private String buyDate = "";
    
    // 2019.10.29 - VOD 정산 프로세스 개선 : NPT_VO_BUY_META 테이블 넣는 변수 추가
    private String productName 		= "";
    private String productKind 		= "";  
    
    // 2020.06.26 - 영화월정액 1개월 체험권
    private String couponFlag		= "";
    private String ofrSeq			= "";
    private String offerCd			= "";
    private int couponCnt		= 0;
    
    public BuyNSProductRequestVO(){}
    
    public BuyNSProductRequestVO(String szParam){
    	
    	CommonService commonService = new CommonService();
    	
    	String[] arrStat = szParam.split("\\|");
		
		String key		= "";
		String value	= "";
		int    nStr		= 0;
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		
		for(int i = 0; i < arrStat.length; i++){
			nStr	= arrStat[i].indexOf("=");
			
			if(nStr > 0) {
				key = arrStat[i].substring(0, nStr).toLowerCase().trim();
				value = arrStat[i].substring(nStr + 1, arrStat[i].length()).trim();
				
				paramMap.put(key, value);
		
				
				if(key.toLowerCase().equals("sa_id"))			this.saId = value;
				if(key.toLowerCase().equals("stb_mac"))		this.stbMac = value;
				if(key.toLowerCase().equals("prod_id"))		this.prodId =value;
				if(key.toLowerCase().equals("app_type"))		this.appType = value;
				if(key.toLowerCase().equals("coupon_flag"))		this.couponFlag = value;
				if(key.toLowerCase().equals("ofr_seq"))		this.ofrSeq =value;
				if(key.toLowerCase().equals("offer_cd"))		this.offerCd = value;
			}
		}
		
		if(!commonService.getValidParam(this.saId, 7, 12, 1))
		{
			throw new ImcsException();
		}	
		
		if(!commonService.getValidParam(this.stbMac, 14, 14, 1))
		{
			throw new ImcsException();
		}	
		
		if(!commonService.getValidParam(this.prodId, 5, 5, 1))
		{
			throw new ImcsException();
		}	
		
		this.couponFlag = StringUtil.replaceNull(this.couponFlag, "0");
		
		if(!commonService.getValidParam(this.couponFlag, 1, 1, 3))
		{
			throw new ImcsException();
		}	
		
		if(this.couponFlag.equals("1")) {
			
			// 2020.07.14 - 쿠폰번호는 14자리 fix이므로 14자리로만 체크한다.
			if(!commonService.getValidParam(this.ofrSeq, 14, 14, 1))
			{
				throw new ImcsException();
			}	
			
			// 2020.07.14 - 쿠폰 이벤트ID의 경우 MIMS도 모르므로 일단 3자리부터 10자리까지 허용할 수 있도록 한다.
			if(!commonService.getValidParam(this.offerCd, 3, 10, 1))
			{
				throw new ImcsException();
			}	
		} else if (this.couponFlag.equals("0")) {
			if (this.ofrSeq.length() > 0 || this.offerCd.length() > 0) {
				throw new ImcsException();
			}
		}
	}
    

	public String getSaId() {
		return saId;
	}
	public void setSaId(String saId) {
		this.saId = saId;
	}
	public String getStbMac() {
		return stbMac;
	}
	public void setStbMac(String stbMac) {
		this.stbMac = stbMac;
	}

	public String getProdId() {
		return prodId;
	}

	public void setProdId(String prodId) {
		this.prodId = prodId;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public Integer getResultSet() {
		return resultSet;
	}

	public void setResultSet(Integer resultSet) {
		this.resultSet = resultSet;
	}

	public Integer getMessageSet() {
		return messageSet;
	}

	public void setMessageSet(Integer messageSet) {
		this.messageSet = messageSet;
	}

	public long getTp_start() {
		return tp_start;
	}

	public void setTp_start(long tp_start) {
		this.tp_start = tp_start;
	}

	public String getNscnId() {
		return nscnId;
	}

	public void setNscnId(String nscnId) {
		this.nscnId = nscnId;
	}

	public String getExpiCd() {
		return expiCd;
	}

	public void setExpiCd(String expiCd) {
		this.expiCd = expiCd;
	}

	public String getProdGb() {
		return prodGb;
	}

	public void setProdGb(String prodGb) {
		this.prodGb = prodGb;
	}

	public String getProdType() {
		return prodType;
	}

	public void setProdType(String prodType) {
		this.prodType = prodType;
	}

	public String getExpiredTime() {
		return expiredTime;
	}

	public void setExpiredTime(String expiredTime) {
		this.expiredTime = expiredTime;
	}

	public String getBuyingDate() {
		return buyingDate;
	}

	public void setBuyingDate(String buyingDate) {
		this.buyingDate = buyingDate;
	}

	public String getBuyDate() {
		return buyDate;
	}

	public void setBuyDate(String buyDate) {
		this.buyDate = buyDate;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductKind() {
		return productKind;
	}

	public void setProductKind(String productKind) {
		this.productKind = productKind;
	}

	public String getCouponFlag() {
		return couponFlag;
	}

	public void setCouponFlag(String couponFlag) {
		this.couponFlag = couponFlag;
	}

	public String getOfrSeq() {
		return ofrSeq;
	}

	public void setOfrSeq(String ofrSeq) {
		this.ofrSeq = ofrSeq;
	}

	public String getOfferCd() {
		return offerCd;
	}

	public void setOfferCd(String offerCd) {
		this.offerCd = offerCd;
	}

	public int getCouponCnt() {
		return couponCnt;
	}

	public void setCouponCnt(int couponCnt) {
		this.couponCnt = couponCnt;
	}

}
