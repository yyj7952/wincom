
package kr.co.wincom.imcs.api.getNSChPGM;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;


@SuppressWarnings("serial")
public class GetNSChPGMResponseVO implements Serializable {
	/********************************************************************
	 * GetNSChPGM API 전문 칼럼(순서 일치)
	********************************************************************/
	private String resultType   = "EPG";//EPG
	private String epgType      = "";   //EPG 타입
    private String serviceId	= "";	// 서비스 ID
    private String chnlNm       = "";   // 채널명
    private String programId	= "";	// 프로그램 ID
    private String eventId		= "";	// 이벤트 ID
    private String programTitle	= "";	// 프로그램 타이틀
    private String duplGb       = "";   // 본재방 여부
    private String categoryId	= "";	// 시리즈 ID
    private String genre1		= "";	// 장르1
    private String genre2		= "";	// 장르2
    private String startTime	= "";	// 시작시간
    private String endTime		= "";	// 종료시간
    private String imgUrl		= "";	// 썸네일 URL
    private String imgFileName	= "";	// 썸네일 파일명
    private String virtualId	= "";	// 가상채널 앨범 ID
    private String runtime		= "";	// 가상채널 상영시간
    private String prInfo		= "";	// 가상채널 나이제한
    private String subChnlYn    = "";   // 옴니뷰 서비스 여부
    private String liveYn       = "";   // 라이브방송 여부
    private String omniviewCnt  = "";
    private String filteringCode= "";
    
    // 2020.05.06 - 아이돌 라이브 2020-1Q
 	private String mainProperty			= "";
 	private String subProperty			= "";
 	
//    private String programSynopsis	= "";	// 프로그램 시놉시스
//    private String url			= "";	// 이미지URL
//    private String dlb			= "";	// Dolby 여부
//    private String ste			= "";	// 스테레오 여부
//    private String rsl			= "";	// HD 여부
//    private String mlg			= "";	// Multilingual audio 여부
//    private String dir			= "";	// 감독
//    private String act			= "";	// 주연
//    private String pvr			= "";	// 녹화가능프로그램 여부
//    private String nsc			= "";	// 필터링코드
//    private String virtualUrl	= "";	// 가상채널 URL
//    private String telephonYn	= "";	// 전화후원 여부
//    private String telephonNum	= "";	// 전화후원 전화번호
//    private String smsYn		= "";	// 문자후원 여부
//    private String smsNum		= "";	// 문자후원 수신번호
//    private String linkYn		= "";	// 웹사이트링크 여부
//    private String linkUrl		= "";	// 웹사이트링크 URL
    
    /********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
    private String imageYn		= "";
    
    @Override
    public String toString(){
		StringBuffer sb = new StringBuffer();
			
		sb.append(StringUtil.nullToSpace(this.resultType)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.epgType)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.serviceId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.chnlNm)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.programId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.eventId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.programTitle)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.duplGb)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.categoryId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.genre1)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.genre2)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.startTime)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.endTime)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.imgUrl)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.imgFileName)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.virtualId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.runtime)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.prInfo)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.subChnlYn)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.liveYn)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.filteringCode)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getMainProperty())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getSubProperty().replaceAll("\\\\b", ImcsConstants.ARRSEP))).append(ImcsConstants.COLSEP);
		
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
	public String getResultType() {
		return resultType;
	}


	public void setResultType(String resultType) {
		this.resultType = resultType;
	}


	public String getEpgType() {
		return epgType;
	}


	public void setEpgType(String epgType) {
		this.epgType = epgType;
	}


	public String getChnlNm() {
		return chnlNm;
	}


	public void setChnlNm(String chnlNm) {
		this.chnlNm = chnlNm;
	}


	public String getDuplGb() {
		return duplGb;
	}


	public void setDuplGb(String duplGb) {
		this.duplGb = duplGb;
	}


	public String getSubChnlYn() {
		return subChnlYn;
	}


	public void setSubChnlYn(String subChnlYn) {
		this.subChnlYn = subChnlYn;
	}


	public String getLiveYn() {
		return liveYn;
	}


	public void setLiveYn(String liveYn) {
		this.liveYn = liveYn;
	}


	public String getOmniviewCnt() {
		return omniviewCnt;
	}


	public void setOmniviewCnt(String omniviewCnt) {
		this.omniviewCnt = omniviewCnt;
	}


	public String getFilteringCode() {
		return filteringCode;
	}


	public void setFilteringCode(String filteringCode) {
		this.filteringCode = filteringCode;
	}


	public String getCategoryId() {
		return categoryId;
	}


	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}


	public String getMainProperty() {
		return mainProperty;
	}


	public void setMainProperty(String mainProperty) {
		this.mainProperty = mainProperty;
	}


	public String getSubProperty() {
		return subProperty;
	}


	public void setSubProperty(String subProperty) {
		this.subProperty = subProperty;
	}
	
	
}
