package kr.co.wincom.imcs.api.authorizeNSView;

import java.io.Serializable;

import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;

public class AuthorizeNSCateVO extends NoSqlLoggingVO implements Serializable {
	private static final long serialVersionUID = 820027358203692775L;

	private String prodId		= "";
	private String catType		= "";	// 카테고리타입	(SVOD, PPM)
	private String catLevel		= "";
	private String catId		= "";
	private String catName		= "";
	private String catIdSvod	= "";
	private String catNameSvod	= "";
	
	
	public String getProdId() {
		return prodId;
	}
	public void setProdId(String prodId) {
		this.prodId = prodId;
	}
	public String getCatType() {
		return catType;
	}
	public void setCatType(String catType) {
		this.catType = catType;
	}
	public String getCatLevel() {
		return catLevel;
	}
	public void setCatLevel(String catLevel) {
		this.catLevel = catLevel;
	}
	public String getCatId() {
		return catId;
	}
	public void setCatId(String catId) {
		this.catId = catId;
	}
	public String getCatName() {
		return catName;
	}
	public void setCatName(String catName) {
		this.catName = catName;
	}
	public String getCatIdSvod() {
		return catIdSvod;
	}
	public void setCatIdSvod(String catIdSvod) {
		this.catIdSvod = catIdSvod;
	}
	public String getCatNameSvod() {
		return catNameSvod;
	}
	public void setCatNameSvod(String catNameSvod) {
		this.catNameSvod = catNameSvod;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

	
}
