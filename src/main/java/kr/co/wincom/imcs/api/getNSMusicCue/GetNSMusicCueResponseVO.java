package kr.co.wincom.imcs.api.getNSMusicCue;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;


@SuppressWarnings("serial")
public class GetNSMusicCueResponseVO extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * getNSMusicCue API 전문 칼럼(순서 일치)
	********************************************************************/
	private String resultFlag			= ""; //결과값
	private String resultMsg				= ""; //결과메시지
	private String itemCount			= ""; //아이템 카운트
	private String musicCount			= ""; //뮤직 카운트
	private String albumGb	 			= ""; //앨범구분
	private String serviceId			= ""; //서비스ID
	private String angleFlag			= ""; //앵글플레그
	private String memberFlag			= ""; //멤버플래그
	private String channelUrl			= ""; //채널URL
	private String channelImgFile		= ""; //채널이미지파일
	private String mainImgFile			= ""; //방송사 로고 파일명 (본방용)
	private String logoImgFile          = "";
	private String omniImgFile			= ""; //방송사 로고 파일명 (카메라용)
	private String imageFolder			= "";
	private String imageUrl				= ""; //방송사 로고 URL(본방용,카메라용)
	
    @Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(StringUtils.defaultString(this.getResultFlag(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getResultMsg(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getItemCount(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getMusicCount(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getAlbumGb(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getServiceId(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getAngleFlag(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getMemberFlag(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getChannelUrl(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getChannelImgFile(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getImageUrl(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getMainImgFile(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getImageUrl(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getOmniImgFile(), "")).append(ImcsConstants.COLSEP);


		return sb.toString();
	}

	public String getResultFlag() {
		return resultFlag;
	}

	public void setResultFlag(String resultFlag) {
		this.resultFlag = resultFlag;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
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

	public String getAlbumGb() {
		return albumGb;
	}

	public void setAlbumGb(String albumGb) {
		this.albumGb = albumGb;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getAngleFlag() {
		return angleFlag;
	}

	public void setAngleFlag(String angleFlag) {
		this.angleFlag = angleFlag;
	}

	public String getMemberFlag() {
		return memberFlag;
	}

	public void setMemberFlag(String memberFlag) {
		this.memberFlag = memberFlag;
	}

	public String getChannelUrl() {
		return channelUrl;
	}

	public void setChannelUrl(String channelUrl) {
		this.channelUrl = channelUrl;
	}

	public String getChannelImgFile() {
		return channelImgFile;
	}

	public void setChannelImgFile(String channelImgFile) {
		this.channelImgFile = channelImgFile;
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

	public String getLogoImgFile() {
		return logoImgFile;
	}

	public void setLogoImgFile(String logoImgFile) {
		this.logoImgFile = logoImgFile;
	}


    
    

}
