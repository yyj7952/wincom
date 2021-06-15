package kr.co.wincom.imcs.common.vo;

import java.io.Serializable;


@SuppressWarnings("serial")
public class ContTypeVO implements Serializable {

    private String productId		= "";
    private String productName		= "";
    private String contsId			= "";
    private String contsName		= "";
    private String contsType		= "";
    private String contsGenre		= "";
    private String price			= "";
    private String dataChk			= "";
    private String expiredDate		= "";
    private String uflixFlag		= "";
    private String imcsProductType	= "";
    private String productCd		= "";
    private String pProductId		= "";
    
    // 2019.10.29 - VOD 정산 프로세스 개선 : PPS CP_ID정보 조회를 위한 변수 추가    
    private String cpId				= "";
    private String productKind 		= "";
    
    // 2020.03.03 - seamless
    private String nscreenYn		= "";
    
    private String metaRunTime		= "";
    
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getContsType() {
		return contsType;
	}
	public void setContsType(String contsType) {
		this.contsType = contsType;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getDataChk() {
		return dataChk;
	}
	public void setDataChk(String dataChk) {
		this.dataChk = dataChk;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getContsName() {
		return contsName;
	}
	public void setContsName(String contsName) {
		this.contsName = contsName;
	}
	public String getContsId() {
		return contsId;
	}
	public void setContsId(String contsId) {
		this.contsId = contsId;
	}
	public String getContsGenre() {
		return contsGenre;
	}
	public void setContsGenre(String contsGenre) {
		this.contsGenre = contsGenre;
	}
	public String getExpiredDate() {
		return expiredDate;
	}
	public void setExpiredDate(String expiredDate) {
		this.expiredDate = expiredDate;
	}
	public String getUflixFlag() {
		return uflixFlag;
	}
	public void setUflixFlag(String uflixFlag) {
		this.uflixFlag = uflixFlag;
	}
	public String getImcsProductType() {
		return imcsProductType;
	}
	public void setImcsProductType(String imcsProductType) {
		this.imcsProductType = imcsProductType;
	}
	
	public String getProductCd()					{ return productCd; }
	public void setProductCd(String productCd)		{ this.productCd = productCd; }
	public String getpProductId() 					{ return pProductId; }
	public void setpProductId(String pProductId) 	{ this.pProductId = pProductId; }
	
	public String getCpId() {
		return cpId;
	}
	public void setCpId(String cpId) {
		this.cpId = cpId;
	}
	public String getProductKind() {
		return productKind;
	}
	public void setProductKind(String productKind) {
		this.productKind = productKind;
	}
	public String getNscreenYn() {
		return nscreenYn;
	}
	public void setNscreenYn(String nscreenYn) {
		this.nscreenYn = nscreenYn;
	}
	public String getMetaRunTime() {
		return metaRunTime;
	}
	public void setMetaRunTime(String metaRunTime) {
		this.metaRunTime = metaRunTime;
	}	
	
}
