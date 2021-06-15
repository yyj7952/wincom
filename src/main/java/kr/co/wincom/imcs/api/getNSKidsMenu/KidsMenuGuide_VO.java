package kr.co.wincom.imcs.api.getNSKidsMenu;

import java.io.Serializable;

public class KidsMenuGuide_VO implements Serializable
{
	private static final long serialVersionUID = 1059945535211035748L;
	
	private String contsId = "";
	private String contsNm = "";
	private String guideText = "";
	private String m3u8File1 = "";
	private String m3u8File2 = "";
	private String imageFileName = "";
	
	private String tempLevelId = "";
	private String tempMaxLevelId = "";
	private String tempParentId = "";
	private String guideImageFile = "";
	
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
	public String getImageFileName() {
		return imageFileName;
	}
	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}
	public String getM3u8File1() {
		return m3u8File1;
	}
	public void setM3u8File1(String m3u8File1) {
		this.m3u8File1 = m3u8File1;
	}
	public String getM3u8File2() {
		return m3u8File2;
	}
	public void setM3u8File2(String m3u8File2) {
		this.m3u8File2 = m3u8File2;
	}
	public String getTempLevelId() {
		return tempLevelId;
	}
	public void setTempLevelId(String tempLevelId) {
		this.tempLevelId = tempLevelId;
	}
	public String getTempMaxLevelId() {
		return tempMaxLevelId;
	}
	public void setTempMaxLevelId(String tempMaxLevelId) {
		this.tempMaxLevelId = tempMaxLevelId;
	}
	public String getTempParentId() {
		return tempParentId;
	}
	public void setTempParentId(String tempParentId) {
		this.tempParentId = tempParentId;
	}
	public String getGuideImageFile() {
		return guideImageFile;
	}
	public void setGuideImageFile(String guideImageFile) {
		this.guideImageFile = guideImageFile;
	}
	
}
