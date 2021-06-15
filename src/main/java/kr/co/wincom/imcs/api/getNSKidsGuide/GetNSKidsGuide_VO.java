package kr.co.wincom.imcs.api.getNSKidsGuide;

import java.io.Serializable;

public class GetNSKidsGuide_VO implements Serializable
{
	private static final long serialVersionUID = 7096784442289056415L;
	
	private String guideLevel = null;
	private String categoryId = null;
	private String categoryNm = null;
	private String subTitle = null;
	private String contsId = null;
	private String contsNm = null;
	private String guideText = null;
	private String assetId = null;
	private String assetFileNm = null;
	private String imageFileName = null;
	
	public String getGuideLevel() {
		return guideLevel;
	}
	public void setGuideLevel(String guideLevel) {
		this.guideLevel = guideLevel;
	}
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
	public String getSubTitle() {
		return subTitle;
	}
	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}
	public String getContsId() {
		return contsId;
	}
	public void setContsId(String contsId) {
		this.contsId = contsId;
	}
	public String getContsNm() {
		return contsNm;
	}
	public void setContsNm(String contsNm) {
		this.contsNm = contsNm;
	}
	public String getGuideText() {
		return guideText;
	}
	public void setGuideText(String guideText) {
		this.guideText = guideText;
	}
	public String getAssetId() {
		return assetId;
	}
	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}
	public String getImageFileName() {
		return imageFileName;
	}
	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}
	public String getAssetFileNm() {
		return assetFileNm;
	}
	public void setAssetFileNm(String assetFileNm) {
		this.assetFileNm = assetFileNm;
	}
	

}
