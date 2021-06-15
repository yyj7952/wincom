package kr.co.wincom.imcs.common.vo;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;


@SuppressWarnings("serial")
public class ComCueSheetItemVO implements Serializable {

	private String itemNo                = "";
	private String timeTag               = "";
	private String duration              = "";
	private String itemType              = "";
	private String albumId               = "";
	private String albumName             = "";
	private String actorId               = "";
	private String actorName             = "";
	private String actorImgUrl           = "";
	private String actorImgFile          = "";
	private String placeAudioYn          = "";
	private String addMessage            = "";
	private String status                = "";
	private String memberName            = "";
	private String memberImgFile         = "";
	private String memberImgUrl          = "";
	private String omnivCount            = "";
	private String memberCount           = "";
	private String vrType                = "";
	private String vrAlbumId             = "";
	////////////////////////////////////////
	private String orderNo               = "";
	private String cuesheetId            = "";
	private String viewFlag              = "";
	private String omnivType             = "";
	private String prAlbumId             = "";
	private String camNo                 = "";
	private String ServiceId             = "";
	private String m3u8Castis            = "";
	private String m3u8Onnet             = "";
	
	private String chnlId             = "";
	
    //2019.09.19 아이들나라 3차 고도화
	private String  voteId;
	private String  voteFlagBgn;
	private String  voteFlagEnd;
	private String  voteFlag;	//투표 시작/종료 정보 - 1:시작숏클립, 2:종료숏클립, 3:시작/종료 숏클립
	private String  voteStatus;	//투표 서비스 여부 - 0:미사용, 1:검수, 2:상용	
	
	public String getM3u8Castis() {
		return m3u8Castis;
	}



	public void setM3u8Castis(String m3u8Castis) {
		this.m3u8Castis = m3u8Castis;
	}



	public String getM3u8Onnet() {
		return m3u8Onnet;
	}



	public void setM3u8Onnet(String m3u8Onnet) {
		this.m3u8Onnet = m3u8Onnet;
	}



	public String getCamNo() {
		return camNo;
	}



	public void setCamNo(String camNo) {
		this.camNo = camNo;
	}



	public String getServiceId() {
		return ServiceId;
	}



	public void setServiceId(String serviceId) {
		ServiceId = serviceId;
	}



	public String getPrAlbumId() {
		return prAlbumId;
	}



	public void setPrAlbumId(String prAlbumId) {
		this.prAlbumId = prAlbumId;
	}



	public String getOmnivType() {
		return omnivType;
	}



	public void setOmnivType(String omnivType) {
		this.omnivType = omnivType;
	}



	public String getViewFlag() {
		return viewFlag;
	}



	public void setViewFlag(String viewFlag) {
		this.viewFlag = viewFlag;
	}



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



	public String getTimeTag() {
		return timeTag;
	}



	public void setTimeTag(String timeTag) {
		this.timeTag = timeTag;
	}



	public String getDuration() {
		return duration;
	}



	public void setDuration(String duration) {
		this.duration = duration;
	}



	public String getItemType() {
		return itemType;
	}



	public void setItemType(String itemType) {
		this.itemType = itemType;
	}



	public String getAlbumId() {
		return albumId;
	}



	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}



	public String getAlbumName() {
		return albumName;
	}



	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}



	public String getActorId() {
		return actorId;
	}



	public void setActorId(String actorId) {
		this.actorId = actorId;
	}



	public String getActorName() {
		return actorName;
	}



	public void setActorName(String actorName) {
		this.actorName = actorName;
	}



	public String getActorImgUrl() {
		return actorImgUrl;
	}



	public void setActorImgUrl(String actorImgUrl) {
		this.actorImgUrl = actorImgUrl;
	}



	public String getActorImgFile() {
		return actorImgFile;
	}



	public void setActorImgFile(String actorImgFile) {
		this.actorImgFile = actorImgFile;
	}



	public String getPlaceAudioYn() {
		return placeAudioYn;
	}



	public void setPlaceAudioYn(String placeAudioYn) {
		this.placeAudioYn = placeAudioYn;
	}



	public String getAddMessage() {
		return addMessage;
	}



	public void setAddMessage(String addMessage) {
		this.addMessage = addMessage;
	}



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}



	public String getMemberName() {
		return memberName;
	}



	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}



	public String getMemberImgFile() {
		return memberImgFile;
	}



	public void setMemberImgFile(String memberImgFile) {
		this.memberImgFile = memberImgFile;
	}



	public String getMemberImgUrl() {
		return memberImgUrl;
	}



	public void setMemberImgUrl(String memberImgUrl) {
		this.memberImgUrl = memberImgUrl;
	}



	public String getOmnivCount() {
		return omnivCount;
	}



	public void setOmnivCount(String omnivCount) {
		this.omnivCount = omnivCount;
	}



	public String getMemberCount() {
		return memberCount;
	}



	public void setMemberCount(String memberCount) {
		this.memberCount = memberCount;
	}



	public String getVrType() {
		return vrType;
	}



	public void setVrType(String vrType) {
		this.vrType = vrType;
	}



	public String getVrAlbumId() {
		return vrAlbumId;
	}



	public void setVrAlbumId(String vrAlbumId) {
		this.vrAlbumId = vrAlbumId;
	}



	public String getOrderNo() {
		return orderNo;
	}



	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}



	//public String toCueSheetItemString(){
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("MAIN").append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.orderNo)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.timeTag)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.duration)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.itemType)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.albumId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.albumName)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.actorId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.actorName)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.actorImgUrl)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.actorImgFile)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.placeAudioYn)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.addMessage)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.omnivCount)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.memberCount)).append(ImcsConstants.COLSEP);
		sb.append((this.memberName)).append(ImcsConstants.COLSEP);  //\b이 사라지는 것 방지
		sb.append(StringUtil.nullToSpace(this.memberImgUrl)).append(ImcsConstants.COLSEP);
		sb.append(this.memberImgFile).append(ImcsConstants.COLSEP); //\b이 사라지는 것 방지
		sb.append(StringUtil.nullToSpace(this.status)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.vrType)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.vrAlbumId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.voteId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.voteFlagBgn)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.voteFlagEnd)).append(ImcsConstants.COLSEP);
			
		return sb.toString();
	}
	
	public String omniInfoToString(){
		StringBuffer sb = new StringBuffer();
		sb.append("OMNI").append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.omnivType)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.camNo)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.ServiceId)).append(ImcsConstants.COLSEP);
		sb.append(this.m3u8Castis).append(ImcsConstants.COLSEP);
		sb.append(this.m3u8Onnet).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.viewFlag)).append(ImcsConstants.COLSEP);
			
		return sb.toString();
	}
	
	public String addInfoToString(){
		StringBuffer sb = new StringBuffer();
		sb.append("SUB").append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.orderNo)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace("000000")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.duration)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace("")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.prAlbumId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.albumName)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace("")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace("")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.actorImgUrl)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.actorImgFile)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace("")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace("")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace("0")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace("0")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace("")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace("")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace("")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace("")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace("")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace("")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace("")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace("")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace("")).append(ImcsConstants.COLSEP);
			
		return sb.toString();
	}



	public String getChnlId() {
		return chnlId;
	}



	public void setChnlId(String chnlId) {
		this.chnlId = chnlId;
	}



	public String getVoteId() {
		return voteId;
	}



	public void setVoteId(String voteId) {
		this.voteId = voteId;
	}



	public String getVoteFlagBgn() {
		return voteFlagBgn;
	}



	public void setVoteFlagBgn(String voteFlagBgn) {
		this.voteFlagBgn = voteFlagBgn;
	}



	public String getVoteFlagEnd() {
		return voteFlagEnd;
	}



	public void setVoteFlagEnd(String voteFlagEnd) {
		this.voteFlagEnd = voteFlagEnd;
	}



	public String getVoteFlag() {
		return voteFlag;
	}



	public void setVoteFlag(String voteFlag) {
		this.voteFlag = voteFlag;
	}



	public String getVoteStatus() {
		return voteStatus;
	}



	public void setVoteStatus(String voteStatus) {
		this.voteStatus = voteStatus;
	}
	
	
	    
}
