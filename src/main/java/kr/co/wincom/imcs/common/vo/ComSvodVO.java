package kr.co.wincom.imcs.common.vo;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ComSvodVO extends NoSqlLoggingVO implements Serializable {
	private String svodProdId		= "";
	private String svodProdName		= "";
	private String svodProdPrice	= "";
	private String svodProdDesc		= "";
	private String svodProdIsuYn	= ""; 
	private String svodProdIsuType	= "";
	private String svodProdType		= "";
	private String premiumYn		= "";
	private String prodSubYn		= "";
	private String maxValue			= "";
	private String uflixProdYn		= "";
	private String currentCount	= "";
	private String imcsViewCtrl	= "";
	
	//2020.11.24 - 인앱 결제 지원
	private String appCtrl	= "";
	
	public String getCurrentCount() {
		return currentCount;
	}
	public void setCurrentCount(String currentCount) {
		this.currentCount = currentCount;
	}
	public String getSvodProdId() {
		return svodProdId;
	}
	public void setSvodProdId(String svodProdId) {
		this.svodProdId = svodProdId;
	}
	public String getSvodProdName() {
		return svodProdName;
	}
	public void setSvodProdName(String svodProdName) {
		this.svodProdName = svodProdName;
	}
	public String getSvodProdPrice() {
		return svodProdPrice;
	}
	public void setSvodProdPrice(String svodProdPrice) {
		this.svodProdPrice = svodProdPrice;
	}
	public String getSvodProdDesc() {
		return svodProdDesc;
	}
	public void setSvodProdDesc(String svodProdDesc) {
		this.svodProdDesc = svodProdDesc;
	}
	public String getSvodProdIsuYn() {
		return svodProdIsuYn;
	}
	public void setSvodProdIsuYn(String svodProdIsuYn) {
		this.svodProdIsuYn = svodProdIsuYn;
	}
	public String getSvodProdIsuType() {
		return svodProdIsuType;
	}
	public void setSvodProdIsuType(String svodProdIsuType) {
		this.svodProdIsuType = svodProdIsuType;
	}
	public String getSvodProdType() {
		return svodProdType;
	}
	public void setSvodProdType(String svodProdType) {
		this.svodProdType = svodProdType;
	}
	public String getPremiumYn() {
		return premiumYn;
	}
	public void setPremiumYn(String premiumYn) {
		this.premiumYn = premiumYn;
	}
	public String getProdSubYn() {
		return prodSubYn;
	}
	public void setProdSubYn(String prodSubYn) {
		this.prodSubYn = prodSubYn;
	}
	public String getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}
	public String getUflixProdYn() {
		return uflixProdYn;
	}
	public void setUflixProdYn(String uflixProdYn) {
		this.uflixProdYn = uflixProdYn;
	}
	public String getImcsViewCtrl() {
		return imcsViewCtrl;
	}
	public void setImcsViewCtrl(String imcsViewCtrl) {
		this.imcsViewCtrl = imcsViewCtrl;
	}
	public String getAppCtrl() {
		return appCtrl;
	}
	public void setAppCtrl(String appCtrl) {
		this.appCtrl = appCtrl;
	}
	
}
