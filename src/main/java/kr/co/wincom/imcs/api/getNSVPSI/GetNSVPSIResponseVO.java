
package kr.co.wincom.imcs.api.getNSVPSI;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;


@SuppressWarnings("serial")
public class GetNSVPSIResponseVO implements Serializable {
	/********************************************************************
	 * GetNSVPSI API 전문 칼럼(순서 일치)
	********************************************************************/
    private String serviceId	= "";	// 서비스 ID
    private String programId	= "";	// 프로그램 ID
    private String eventId		= "";	// 이벤트 ID
    private String programTitle	= "";	// 프로그램 타이틀
    private String programSynopsis	= "";	// 프로그램 시놉시스
    private String url			= "";	// 이미지URL
    private String dlb			= "";	// Dolby 여부
    private String ste			= "";	// 스테레오 여부
    private String rsl			= "";	// HD 여부
    private String mlg			= "";	// Multilingual audio 여부
    private String dir			= "";	// 감독
    private String act			= "";	// 주연
    private String pvr			= "";	// 녹화가능프로그램 여부
    private String sid			= "";	// 시리즈 ID
    private String nsc			= "";	// 필터링코드
    private String genre1		= "";	// 장르1
    private String genre2		= "";	// 장르2
    private String startTime	= "";	// 시작시간
    private String endTime		= "";	// 종료시간
    private String imgUrl		= "";	// 썸네일 URL
    private String imgFileName	= "";	// 썸네일 파일명
    private String virtualUrl	= "";	// 가상채널 URL
    private String virtualId	= "";	// 가상채널 앨범 ID
    private String runtime		= "";	// 가상채널 상영시간
    private String prInfo		= "";	// 가상채널 나이제한
    private String telephonYn	= "";	// 전화후원 여부
    private String telephonNum	= "";	// 전화후원 전화번호
    private String smsYn		= "";	// 문자후원 여부
    private String smsNum		= "";	// 문자후원 수신번호
    private String linkYn		= "";	// 웹사이트링크 여부
    private String linkUrl		= "";	// 웹사이트링크 URL
    
    /********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
    private String imageYn		= "";
    
    @Override
    public String toString(){
		StringBuffer sb = new StringBuffer();
			
		sb.append(StringUtil.nullToSpace(this.serviceId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.programId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.eventId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.programTitle)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.programSynopsis)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.url)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.dlb)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.ste)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.rsl)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.mlg)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.dir)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.act)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.pvr)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.sid)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.nsc)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.genre1)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.genre2)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.startTime)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.endTime)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.imgUrl)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.imgFileName)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.virtualUrl)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.virtualId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.runtime)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.prInfo)).append(ImcsConstants.COLSEP);
			
		return sb.toString();
	}

    
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getProgramId() {
		return programId;
	}
	public void setProgramId(String programId) {
		this.programId = programId;
	}
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	public String getProgramTitle() {
		return programTitle;
	}
	public void setProgramTitle(String programTitle) {
		this.programTitle = programTitle;
	}
	public String getProgramSynopsis() {
		return programSynopsis;
	}
	public void setProgramSynopsis(String programSynopsis) {
		this.programSynopsis = programSynopsis;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDlb() {
		return dlb;
	}
	public void setDlb(String dlb) {
		this.dlb = dlb;
	}
	public String getSte() {
		return ste;
	}
	public void setSte(String ste) {
		this.ste = ste;
	}
	public String getRsl() {
		return rsl;
	}
	public void setRsl(String rsl) {
		this.rsl = rsl;
	}
	public String getMlg() {
		return mlg;
	}
	public void setMlg(String mlg) {
		this.mlg = mlg;
	}
	public String getDir() {
		return dir;
	}
	public void setDir(String dir) {
		this.dir = dir;
	}
	public String getAct() {
		return act;
	}
	public void setAct(String act) {
		this.act = act;
	}
	public String getPvr() {
		return pvr;
	}
	public void setPvr(String pvr) {
		this.pvr = pvr;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getNsc() {
		return nsc;
	}
	public void setNsc(String nsc) {
		this.nsc = nsc;
	}
	public String getGenre1() {
		return genre1;
	}
	public void setGenre1(String genre1) {
		this.genre1 = genre1;
	}
	public String getGenre2() {
		return genre2;
	}
	public void setGenre2(String genre2) {
		this.genre2 = genre2;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getImgFileName() {
		return imgFileName;
	}
	public void setImgFileName(String imgFileName) {
		this.imgFileName = imgFileName;
	}
	public String getImageYn() {
		return imageYn;
	}
	public void setImageYn(String imageYn) {
		this.imageYn = imageYn;
	}
	public String getVirtualUrl() {
		return virtualUrl;
	}
	public void setVirtualUrl(String virtualUrl) {
		this.virtualUrl = virtualUrl;
	}
	public String getVirtualId() {
		return virtualId;
	}
	public void setVirtualId(String virtualId) {
		this.virtualId = virtualId;
	}
	public String getRuntime() {
		return runtime;
	}
	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}
	public String getPrInfo() {
		return prInfo;
	}
	public void setPrInfo(String prInfo) {
		this.prInfo = prInfo;
	}
	public String getTelephonYn() {
		return telephonYn;
	}
	public void setTelephonYn(String telephonYn) {
		this.telephonYn = telephonYn;
	}
	public String getTelephonNum() {
		return telephonNum;
	}
	public void setTelephonNum(String telephonNum) {
		this.telephonNum = telephonNum;
	}
	public String getSmsYn() {
		return smsYn;
	}
	public void setSmsYn(String smsYn) {
		this.smsYn = smsYn;
	}
	public String getSmsNum() {
		return smsNum;
	}
	public void setSmsNum(String smsNum) {
		this.smsNum = smsNum;
	}
	public String getLinkYn() {
		return linkYn;
	}
	public void setLinkYn(String linkYn) {
		this.linkYn = linkYn;
	}
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
		
}
