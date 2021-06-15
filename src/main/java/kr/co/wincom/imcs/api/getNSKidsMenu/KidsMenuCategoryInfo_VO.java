package kr.co.wincom.imcs.api.getNSKidsMenu;

import java.io.Serializable;

public class KidsMenuCategoryInfo_VO implements Serializable
{
	private static final long serialVersionUID = 1481897466944027282L;
	
	private String categoryId = "";
	private String categoryNm = "";
	private String categoryType = "";
	private String menuType = "";
	private String topImage = "";
	private String guideText = "";
	private String goodsId = "";
	private String goodsText = "";
    private int categoryLevel = 0;
    
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryNm() {
		return categoryNm;
	}
	public void setCategoryNm(String categoryNm) {
		this.categoryNm = categoryNm;
	}
	public String getCategoryType() {
		return categoryType;
	}
	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}
	public String getMenuType() {
		return menuType;
	}
	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}
	public String getTopImage() {
		return topImage;
	}
	public void setTopImage(String topImage) {
		this.topImage = topImage;
	}
	public String getGuideText() {
		return guideText;
	}
	public void setGuideText(String guideText) {
		this.guideText = guideText;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getGoodsText() {
		return goodsText;
	}
	public void setGoodsText(String goodsText) {
		this.goodsText = goodsText;
	}
	public int getCategoryLevel() {
		return categoryLevel;
	}
	public void setCategoryLevel(int categoryLevel) {
		this.categoryLevel = categoryLevel;
	}
    
    
}
