package kr.co.wincom.imcs.api.getNSComBotList;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;


@SuppressWarnings("serial")
public class GetNSComBotListResponseVO implements Serializable {
	/********************************************************************
	 * GetNSProdinfo API 전문 칼럼(순서 일치)
	********************************************************************/
	
	//Body
	private String resultType = "";
	private String categoryId = "";
	private String albumId = "";
	private String appId = "";
	private String expTime = "";
	private String expText = "";
	private String optionTime = "";
	private String audioFile = "";
	private String audioFileIos = "";
	private String type = "";
	private String categoryType = "";
	private String categoryFlag = "";
    
    
    @Override
    public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(StringUtil.nullToSpace(this.resultType)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.categoryId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.albumId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.appId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.expTime)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.expText)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.optionTime)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.audioFile)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.audioFileIos)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.type)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.categoryType)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.categoryFlag)).append(ImcsConstants.COLSEP);
		
		return sb.toString();
    }


	public String getResultType() {
		return resultType;
	}


	public void setResultType(String resultType) {
		this.resultType = resultType;
	}


	public String getCategoryId() {
		return categoryId;
	}


	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}


	public String getAlbumId() {
		return albumId;
	}


	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}


	public String getAppId() {
		return appId;
	}


	public void setAppId(String appId) {
		this.appId = appId;
	}


	public String getExpTime() {
		return expTime;
	}


	public void setExpTime(String expTime) {
		this.expTime = expTime;
	}


	public String getExpText() {
		return expText;
	}


	public void setExpText(String expText) {
		this.expText = expText;
	}


	public String getOptionTime() {
		return optionTime;
	}


	public void setOptionTime(String optionTime) {
		this.optionTime = optionTime;
	}


	public String getAudioFile() {
		return audioFile;
	}


	public void setAudioFile(String audioFile) {
		this.audioFile = audioFile;
	}


	public String getAudioFileIos() {
		return audioFileIos;
	}


	public void setAudioFileIos(String audioFileIos) {
		this.audioFileIos = audioFileIos;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getCategoryType() {
		return categoryType;
	}


	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}


	public String getCategoryFlag() {
		return categoryFlag;
	}


	public void setCategoryFlag(String categoryFlag) {
		this.categoryFlag = categoryFlag;
	}


	
	
}
