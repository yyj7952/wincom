package kr.co.wincom.imcs.common.vo;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;


@SuppressWarnings("serial")
public class ComCueSheetMstVO implements Serializable {

	private String cuesheetId;
	private String itemNo;
	private String omniviewYn;
	private String vodOnlyYn;
	private String angleSvcFlag;
	private String memberSvcFlag;
	private String status;
	private String itemCount;
	private String musicCount;
	private String serviceId;
	private String channelId;
	private String chnlImgFile;
	private String mainImgFile;
	private String logoImgFile;
	private String omniImgFile;
	private String imageFolder;
	private String imageUrl;

	public String getCuesheetId() {
		return cuesheetId;
	}


	public void setCuesheetId(String cuesheetId) {
		this.cuesheetId = cuesheetId;
	}


	public String getItemNo() {
		return itemNo;
	}


	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}


	public String getOmniviewYn() {
		return omniviewYn;
	}


	public void setOmniviewYn(String omniviewYn) {
		this.omniviewYn = omniviewYn;
	}


	public String getVodOnlyYn() {
		return vodOnlyYn;
	}


	public void setVodOnlyYn(String vodOnlyYn) {
		this.vodOnlyYn = vodOnlyYn;
	}


	public String getAngleSvcFlag() {
		return angleSvcFlag;
	}


	public void setAngleSvcFlag(String angleSvcFlag) {
		this.angleSvcFlag = angleSvcFlag;
	}


	public String getMemberSvcFlag() {
		return memberSvcFlag;
	}


	public void setMemberSvcFlag(String memberSvcFlag) {
		this.memberSvcFlag = memberSvcFlag;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getItemCount() {
		return itemCount;
	}


	public void setItemCount(String itemCount) {
		this.itemCount = itemCount;
	}


	public String getMusicCount() {
		return musicCount;
	}


	public void setMusicCount(String musicCount) {
		this.musicCount = musicCount;
	}


	public String getServiceId() {
		return serviceId;
	}


	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}


	public String getChannelId() {
		return channelId;
	}


	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}


	public String getChnlImgFile() {
		return chnlImgFile;
	}


	public void setChnlImgFile(String chnlImgFile) {
		this.chnlImgFile = chnlImgFile;
	}


	public String toCueSheetMstString(){
		StringBuffer sb = new StringBuffer();
		
		sb.append(StringUtil.nullToSpace(this.cuesheetId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.itemNo)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.omniviewYn)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.vodOnlyYn)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.angleSvcFlag)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.memberSvcFlag)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.status)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.itemCount)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.musicCount)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.serviceId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.channelId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.chnlImgFile)).append(ImcsConstants.COLSEP);
			
		return sb.toString();
	}


	public String getImageUrl() {
		return imageUrl;
	}


	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}


	public String getMainImgFile() {
		return mainImgFile;
	}


	public void setMainImgFile(String mainImgFile) {
		this.mainImgFile = mainImgFile;
	}


	public String getLogoImgFile() {
		return logoImgFile;
	}


	public void setLogoImgFile(String logoImgFile) {
		this.logoImgFile = logoImgFile;
	}


	public String getOmniImgFile() {
		return omniImgFile;
	}


	public void setOmniImgFile(String omniImgFile) {
		this.omniImgFile = omniImgFile;
	}


	public String getImageFolder() {
		return imageFolder;
	}


	public void setImageFolder(String imageFolder) {
		this.imageFolder = imageFolder;
	}
	    
}
