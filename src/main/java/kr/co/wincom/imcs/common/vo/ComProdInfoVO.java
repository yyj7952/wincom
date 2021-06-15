package kr.co.wincom.imcs.common.vo;

public class ComProdInfoVO {
    
    public String prodGb	= "";
	public String expiCd	= "";
	public String prodType	= "";
	public String expiredTime	= "";
	public String prodName	= "";
	public String prodId	= "";
	public String price		= "";
	
	// 2019.10.29 - VOD 정산 프로세스 개선 : NPT_VO_BUY_META 테이블 넣는 변수 추가
	public String prodKind		= "";
	
	public String getProdGb() {
		return prodGb;
	}
	public void setProdGb(String prodGb) {
		this.prodGb = prodGb;
	}
	public String getExpiCd() {
		return expiCd;
	}
	public void setExpiCd(String expiCd) {
		this.expiCd = expiCd;
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
	public String getProdName() {
		return prodName;
	}
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getProdId() {
		return prodId;
	}
	public void setProdId(String prodId) {
		this.prodId = prodId;
	}
	public String getProdKind() {
		return prodKind;
	}
	public void setProdKind(String prodKind) {
		this.prodKind = prodKind;
	}
	
}
