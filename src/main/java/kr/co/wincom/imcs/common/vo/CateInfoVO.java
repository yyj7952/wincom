package kr.co.wincom.imcs.common.vo;

import java.io.Serializable;

public class CateInfoVO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String count			= "";
	private String parentCatId		= "";		// 기존 C 소스 내 vc_category_id
	private String isUpdate			= "";
	private String contsLevel		= "";
	private String chaNum			= "";
	private String serInfo			= "";
	private String belongingName	= "";		// getNSReposited에서 categoryName
	
	private String cateList			= "";
	private String categoryId		= "";
	private String categoryName		= "";
	private String isNew			= "";
	private String authYn			= "";
	private String cateInfo			= "";
	private String seriesYn			= "";
	private String cateGb1			= "";
	private String cateGb2			= "";
	private String cateGb3			= "";
	//2018.07.25 권형도
	private String cateGb4			= "";
//	private String iptvProdChk        = "";
//	private String nscProdChk        = "";
	private String iptvTestSbc        = "";
	private String nscTestSbc        = "";
	
	private String iptvProductType = "";
	private String nscProductType = "";
	
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getIsUpdate() {
		return isUpdate;
	}
	public void setIsUpdate(String isUpdate) {
		this.isUpdate = isUpdate;
	}
	public String getContsLevel() {
		return contsLevel;
	}
	public void setContsLevel(String contsLevel) {
		this.contsLevel = contsLevel;
	}
	public String getChaNum() {
		return chaNum;
	}
	public void setChaNum(String chaNum) {
		this.chaNum = chaNum;
	}
	public String getSerInfo() {
		return serInfo;
	}
	public void setSerInfo(String serInfo) {
		this.serInfo = serInfo;
	}
	public String getBelongingName() {
		return belongingName;
	}
	public void setBelongingName(String belongingName) {
		this.belongingName = belongingName;
	}
	public String getParentCatId() {
		return parentCatId;
	}
	public void setParentCatId(String parentCatId) {
		this.parentCatId = parentCatId;
	}
	public String getCateList() {
		return cateList;
	}
	public void setCateList(String cateList) {
		this.cateList = cateList;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getIsNew() {
		return isNew;
	}
	public void setIsNew(String isNew) {
		this.isNew = isNew;
	}
	public String getAuthYn() {
		return authYn;
	}
	public void setAuthYn(String authYn) {
		this.authYn = authYn;
	}
	public String getCateInfo() {
		return cateInfo;
	}
	public void setCateInfo(String cateInfo) {
		this.cateInfo = cateInfo;
	}
	public String getSeriesYn() {
		return seriesYn;
	}
	public void setSeriesYn(String seriesYn) {
		this.seriesYn = seriesYn;
	}
	public String getCateGb1() {
		return cateGb1;
	}
	public void setCateGb1(String cateGb1) {
		this.cateGb1 = cateGb1;
	}
	public String getCateGb2() {
		return cateGb2;
	}
	public void setCateGb2(String cateGb2) {
		this.cateGb2 = cateGb2;
	}
	public String getCateGb3() {
		return cateGb3;
	}
	public void setCateGb3(String cateGb3) {
		this.cateGb3 = cateGb3;
	}
	public String getCateGb4() {
		return cateGb4;
	}
	public void setCateGb4(String cateGb4) {
		this.cateGb4 = cateGb4;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
//	public String getIptvProdChk() {
//		return iptvProdChk;
//	}
//	public void setIptvProdChk(String iptvProdChk) {
//		this.iptvProdChk = iptvProdChk;
//	}
//	public String getNscProdChk() {
//		return nscProdChk;
//	}
//	public void setNscProdChk(String nscProdChk) {
//		this.nscProdChk = nscProdChk;
//	}
	
	
	
	public String getIptvTestSbc() {
		return iptvTestSbc;
	}
	public String getIptvProductType() {
		return iptvProductType;
	}
	public void setIptvProductType(String iptvProductType) {
		this.iptvProductType = iptvProductType;
	}
	public String getNscProductType() {
		return nscProductType;
	}
	public void setNscProductType(String nscProductType) {
		this.nscProductType = nscProductType;
	}
	public void setIptvTestSbc(String iptvTestSbc) {
		this.iptvTestSbc = iptvTestSbc;
	}
	public String getNscTestSbc() {
		return nscTestSbc;
	}
	public void setNscTestSbc(String nscTestSbc) {
		this.nscTestSbc = nscTestSbc;
	}

}
