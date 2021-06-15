package kr.co.wincom.imcs.common.vo;

import java.io.Serializable;


@SuppressWarnings("serial")
public class SvodPkgVO  implements Serializable {

    private String productId = "";
    private String svodPkg = "";
    private String categoryLevel = "";
    
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getSvodPkg() {
		return svodPkg;
	}
	public void setSvodPkg(String svodPkg) {
		this.svodPkg = svodPkg;
	}
	public String getCategoryLevel() {
		return categoryLevel;
	}
	public void setCategoryLevel(String categoryLevel) {
		this.categoryLevel = categoryLevel;
	}
    
}
