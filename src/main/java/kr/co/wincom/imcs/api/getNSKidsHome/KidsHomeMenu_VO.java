package kr.co.wincom.imcs.api.getNSKidsHome;

import java.io.Serializable;

public class KidsHomeMenu_VO implements Serializable
{
	private static final long serialVersionUID = 5564476228439229684L;
	
	private String suggestType = "";
	private String menuId = "";
	private String menuNm = "";
	private String menuDesc = "";
	private String type = "";
	private String catType = "";
	private String recommendId = "";
	private String imageFile = "";
	private String orderNo = "";
	private String catFlag = "";
	private String dispOption = "";
	
	public String getSuggestType() {
		return suggestType;
	}
	public void setSuggestType(String suggestType) {
		this.suggestType = suggestType;
	}
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	public String getMenuNm() {
		return menuNm;
	}
	public void setMenuNm(String menuNm) {
		this.menuNm = menuNm;
	}
	public String getMenuDesc() {
		return menuDesc;
	}
	public void setMenuDesc(String menuDesc) {
		this.menuDesc = menuDesc;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCatType() {
		return catType;
	}
	public void setCatType(String catType) {
		this.catType = catType;
	}
	public String getRecommendId() {
		return recommendId;
	}
	public void setRecommendId(String recommendId) {
		this.recommendId = recommendId;
	}
	public String getImageFile() {
		return imageFile;
	}
	public void setImageFile(String imageFile) {
		this.imageFile = imageFile;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getCatFlag() {
		return catFlag;
	}
	public void setCatFlag(String catFlag) {
		this.catFlag = catFlag;
	}
	public String getDispOption() {
		return dispOption;
	}
	public void setDispOption(String dispOption) {
		this.dispOption = dispOption;
	}
	
}
