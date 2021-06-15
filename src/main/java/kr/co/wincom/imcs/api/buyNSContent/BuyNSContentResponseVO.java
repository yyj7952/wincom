package kr.co.wincom.imcs.api.buyNSContent;

import java.io.Serializable;

import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;


@SuppressWarnings("serial")
public class BuyNSContentResponseVO extends NoSqlLoggingVO implements Serializable {

	private String productId = "";
	private String productName = "";
    private String productPrice = "";
    private String expiredDate = "";
    private String contsId = "";
    private String contsName = "";
    private String contsGenre = "";
    
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}
	public String getExpiredDate() {
		return expiredDate;
	}
	public void setExpiredDate(String expiredDate) {
		this.expiredDate = expiredDate;
	}
	public String getContsId() {
		return contsId;
	}
	public void setContsId(String contsId) {
		this.contsId = contsId;
	}
	public String getContsName() {
		return contsName;
	}
	public void setContsName(String contsName) {
		this.contsName = contsName;
	}
	public String getContsGenre() {
		return contsGenre;
	}
	public void setContsGenre(String contsGenre) {
		this.contsGenre = contsGenre;
	}
    
}
