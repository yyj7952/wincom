package kr.co.wincom.imcs.common.vo;


public class ComDupCHk {
	public Integer nDataChk	= 0;
	public String dataChk	= "";
	public String productId	= "";
	public String buyYn		= "";
	public String buyDate	= "";
	public String expDate	= "";
	
	public String getDataChk() {
		return dataChk;
	}
	public void setDataChk(String dataChk) {
		this.dataChk = dataChk;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getBuyDate() {
		return buyDate;
	}
	public void setBuyDate(String buyDate) {
		this.buyDate = buyDate;
	}
	public String getExpDate() {
		return expDate;
	}
	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}
	public String getBuyYn() {
		return buyYn;
	}
	public void setBuyYn(String buyYn) {
		this.buyYn = buyYn;
	}
	public Integer getnDataChk() {
		return nDataChk;
	}
	public void setnDataChk(Integer nDataChk) {
		this.nDataChk = nDataChk;
	}
		
}
