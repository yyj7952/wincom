package kr.co.wincom.imcs.api.authorizeVView;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;


@SuppressWarnings("serial")
public class AuthorizeVViewResponseVO extends NoSqlLoggingVO implements Serializable {
	private String flag						= "";		// 성공여부 코드값 (0:성공, 1:실패)
	private String ticketId					= "";		// 구매티켓ID
	private String vodServer1				= "";		// VOD서버 1
	private String vodFileNameHL			= "";		// m3u8파일명 HIGH - L
	private String vodServer2				= "";		// VOD서버 2
	private String vodFileNameHN			= "";		// m3u8파일명 HIGH - N
	private String vodServer3				= "";		// VOD서버 3
	private String vodFileNameHC			= "";		// m3u8파일명 HIGH - C
	private String vodFileNameLL			= "";		// m3u8파일명 LOW  - L
	private String vodFileNameLN			= "";		// m3u8파일명 LOW  - N
	private String vodFileNameLC			= "";		// m3u8파일명 LOW  - C
	private String capUrl					= "";		// 자막 파일 URL (WAP서버 IP)
	private String capFileName				= "";		// 자막 파일명
	private String capFileSize				= "";		// 자막 파일 사이즈
	private String errCode					= "";		// 오류 구분 코드 (99:시청불가)
	private String watermarkYn				= "";		// 워터마크 여부
	private String linkTime					= "";		// 시청 정보 - 이어보기시간정보
	private String onetimekey				= "";		// OneTimeKey
	private String vodServer1Type			= "";		// VOD 서버 1 타입 (1:castis, 2:주인넷)
	private String vodServer2Type			= "";		// VOD 서버 2 타입 (1:castis, 2:주인넷)
	private String vodServer3Type			= "";		// VOD 서버 3 타입 (1:castis, 2:주인넷)
	private String thumbnailViewUrl			= "";		// 구간점프 이미지 URL (이미지서버 IP)
	private String thumbnailViewFileName	= "";		// 구간점프 이미지 파일명
	private String thumbnailViewFileName6s	= "";		// 구간점프 이미지 파일명 (6초 썸네일)
	private String timeInfo					= "";		// 구간 초 정보
	private String extMetaGb				= "";		// 외부메타 속성
	private String conts360					= "";		// 360도 컨텐츠 여부
	
	
	private String capFileName2				= "";		// 자막 파일명2
	private String capFileSize2				= "";		// 자막 파일 사이즈2
	private String capFileEncryptYn			= "";		// 다국어 자막 암호화 여부
	
	private String serverIpv6Node1      = "";
	private String serverIpv6Node2      = "";
	private String serverIpv6Node3      = "";
	
	//2019.10.11 - IPv6듀얼스택 지원 변수 추가
	private String vodIpv6Server1		  = "";		// VOD IPv6 서버 1
	private String vodIpv6Server2		  = "";		// VOD IPv6 서버 2
	private String vodIpv6Server3		  = "";		// VOD IPv6 서버 3
	private String vodIpv6Server1Type	  = "";		// VOD IPv6 서버 1 타입 (1:castis, 2:주인넷)
	private String vodIpv6Server2Type	  = "";		// VOD IPv6 서버 2 타입 (1:castis, 2:주인넷)
	private String vodIpv6Server3Type	  = "";		// VOD IPv6 서버 3 타입 (1:castis, 2:주인넷)


	/********************************************************************
	 * 추가 사용되는 속성값  start
	 ********************************************************************/
	private String pid						= "";
	private String resultCode				= "";
	private String viewFlag					= "";		// S:시청, D:다운로드
	private String smiYn					= "";
	private String smiImpYn					= "";
	
	// 2019.02.28 - M9화질일 때 , VR_TYPE, MUSIC_CONT_TYPE값을 보고 CDN 노드를 찾는다.
	// 공연 : O , VR : O   -->   5G노드
	// 공연 : X , VR : O   -->   AWS(아마존) 노드
	// 그 외에는 일반 노드
	// 2019.04.02 - MB,MC,MD 화질일 때 , VR_TYPE, MUSIC_CONT_TYPE값을 보고 CDN 노드를 찾는다.
	// 공연 : O , VR : O   -->   5G노드
	// 공연 : X , VR : O   -->   AWS(아마존) 노드
	// 공연 : O , VR : X   -->   일반
	// 공연 : X , VR : X   -->   5G
	private String musicYn					= "";
	private String vrYn						= "";
	
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
			
		sb.append(StringUtil.replaceNull(this.flag, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.ticketId, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodServer1, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodFileNameHL, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodServer2, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodFileNameHN, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodServer3, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodFileNameHC, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodServer1, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodFileNameLL, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodServer2, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodFileNameLN, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodServer3, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodFileNameLC, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.capUrl, "")).append(ImcsConstants.COLSEP);			// cap_url + cap_server
		sb.append(StringUtil.replaceNull(this.capFileName, "")).append(ImcsConstants.COLSEP);		// cap_file_name + cap_file_name
		sb.append(StringUtil.replaceNull(this.capFileSize, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.errCode, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.watermarkYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.linkTime, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.onetimekey, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodServer1Type, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodServer2Type, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodServer3Type, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.thumbnailViewUrl, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.thumbnailViewFileName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.timeInfo, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.extMetaGb, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.conts360, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.capFileEncryptYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.thumbnailViewFileName6s, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.serverIpv6Node1, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.serverIpv6Node2, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.serverIpv6Node3, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodIpv6Server1, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodIpv6Server2, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodIpv6Server3, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodIpv6Server1Type, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodIpv6Server2Type, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodIpv6Server3Type, "")).append(ImcsConstants.COLSEP);
		return sb.toString();
	}


	public String getPid() {
		return pid;
	}


	public void setPid(String pid) {
		this.pid = pid;
	}


	public String getResultCode() {
		return resultCode;
	}


	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}


	public String getFlag() {
		return flag;
	}


	public void setFlag(String flag) {
		this.flag = flag;
	}


	public String getTicketId() {
		return ticketId;
	}


	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}


	public String getVodServer1() {
		return vodServer1;
	}


	public void setVodServer1(String vodServer1) {
		this.vodServer1 = vodServer1;
	}


	public String getVodFileNameHL() {
		return vodFileNameHL;
	}


	public void setVodFileNameHL(String vodFileNameHL) {
		this.vodFileNameHL = vodFileNameHL;
	}


	public String getVodServer2() {
		return vodServer2;
	}


	public void setVodServer2(String vodServer2) {
		this.vodServer2 = vodServer2;
	}


	public String getVodFileNameHN() {
		return vodFileNameHN;
	}


	public void setVodFileNameHN(String vodFileNameHN) {
		this.vodFileNameHN = vodFileNameHN;
	}


	public String getVodServer3() {
		return vodServer3;
	}


	public void setVodServer3(String vodServer3) {
		this.vodServer3 = vodServer3;
	}


	public String getVodFileNameHC() {
		return vodFileNameHC;
	}


	public void setVodFileNameHC(String vodFileNameHC) {
		this.vodFileNameHC = vodFileNameHC;
	}


	public String getVodFileNameLL() {
		return vodFileNameLL;
	}


	public void setVodFileNameLL(String vodFileNameLL) {
		this.vodFileNameLL = vodFileNameLL;
	}


	public String getVodFileNameLN() {
		return vodFileNameLN;
	}


	public void setVodFileNameLN(String vodFileNameLN) {
		this.vodFileNameLN = vodFileNameLN;
	}


	public String getVodFileNameLC() {
		return vodFileNameLC;
	}


	public void setVodFileNameLC(String vodFileNameLC) {
		this.vodFileNameLC = vodFileNameLC;
	}


	public String getCapUrl() {
		return capUrl;
	}


	public void setCapUrl(String capUrl) {
		this.capUrl = capUrl;
	}


	public String getCapFileName() {
		return capFileName;
	}


	public void setCapFileName(String capFileName) {
		this.capFileName = capFileName;
	}


	public String getCapFileSize() {
		return capFileSize;
	}


	public void setCapFileSize(String capFileSize) {
		this.capFileSize = capFileSize;
	}


	public String getErrCode() {
		return errCode;
	}


	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}


	public String getWatermarkYn() {
		return watermarkYn;
	}


	public void setWatermarkYn(String watermarkYn) {
		this.watermarkYn = watermarkYn;
	}


	public String getLinkTime() {
		return linkTime;
	}


	public void setLinkTime(String linkTime) {
		this.linkTime = linkTime;
	}


	public String getOnetimekey() {
		return onetimekey;
	}


	public void setOnetimekey(String onetimekey) {
		this.onetimekey = onetimekey;
	}


	public String getVodServer1Type() {
		return vodServer1Type;
	}


	public void setVodServer1Type(String vodServer1Type) {
		this.vodServer1Type = vodServer1Type;
	}


	public String getVodServer2Type() {
		return vodServer2Type;
	}


	public void setVodServer2Type(String vodServer2Type) {
		this.vodServer2Type = vodServer2Type;
	}


	public String getVodServer3Type() {
		return vodServer3Type;
	}


	public void setVodServer3Type(String vodServer3Type) {
		this.vodServer3Type = vodServer3Type;
	}


	public String getThumbnailViewUrl() {
		return thumbnailViewUrl;
	}


	public void setThumbnailViewUrl(String thumbnailViewUrl) {
		this.thumbnailViewUrl = thumbnailViewUrl;
	}


	public String getThumbnailViewFileName() {
		return thumbnailViewFileName;
	}


	public void setThumbnailViewFileName(String thumbnailViewFileName) {
		this.thumbnailViewFileName = thumbnailViewFileName;
	}


	public String getTimeInfo() {
		return timeInfo;
	}


	public void setTimeInfo(String timeInfo) {
		this.timeInfo = timeInfo;
	}


	public String getExtMetaGb() {
		return extMetaGb;
	}


	public void setExtMetaGb(String extMetaGb) {
		this.extMetaGb = extMetaGb;
	}


	public String getConts360() {
		return conts360;
	}


	public void setConts360(String conts360) {
		this.conts360 = conts360;
	}


	public String getViewFlag() {
		return viewFlag;
	}


	public void setViewFlag(String viewFlag) {
		this.viewFlag = viewFlag;
	}


	public String getSmiYn() {
		return smiYn;
	}


	public void setSmiYn(String smiYn) {
		this.smiYn = smiYn;
	}


	public String getSmiImpYn() {
		return smiImpYn;
	}


	public void setSmiImpYn(String smiImpYn) {
		this.smiImpYn = smiImpYn;
	}


	public String getCapFileName2() {
		return capFileName2;
	}


	public void setCapFileName2(String capFileName2) {
		this.capFileName2 = capFileName2;
	}


	public String getCapFileSize2() {
		return capFileSize2;
	}


	public void setCapFileSize2(String capFileSize2) {
		this.capFileSize2 = capFileSize2;
	}


	public String getCapFileEncryptYn() {
		return capFileEncryptYn;
	}


	public void setCapFileEncryptYn(String capFileEncryptYn) {
		this.capFileEncryptYn = capFileEncryptYn;
	}


	public String getThumbnailViewFileName6s() {
		return thumbnailViewFileName6s;
	}


	public void setThumbnailViewFileName6s(String thumbnailViewFileName6s) {
		this.thumbnailViewFileName6s = thumbnailViewFileName6s;
	}


	public String getMusicYn() {
		return musicYn;
	}


	public void setMusicYn(String musicYn) {
		this.musicYn = musicYn;
	}


	public String getVrYn() {
		return vrYn;
	}


	public void setVrYn(String vrYn) {
		this.vrYn = vrYn;
	}


	public String getServerIpv6Node1() {
		return serverIpv6Node1;
	}


	public void setServerIpv6Node1(String serverIpv6Node1) {
		this.serverIpv6Node1 = serverIpv6Node1;
	}


	public String getServerIpv6Node2() {
		return serverIpv6Node2;
	}


	public void setServerIpv6Node2(String serverIpv6Node2) {
		this.serverIpv6Node2 = serverIpv6Node2;
	}


	public String getServerIpv6Node3() {
		return serverIpv6Node3;
	}


	public void setServerIpv6Node3(String serverIpv6Node3) {
		this.serverIpv6Node3 = serverIpv6Node3;
	}


	public String getVodIpv6Server1() {
		return vodIpv6Server1;
	}


	public void setVodIpv6Server1(String vodIpv6Server1) {
		this.vodIpv6Server1 = vodIpv6Server1;
	}


	public String getVodIpv6Server2() {
		return vodIpv6Server2;
	}


	public void setVodIpv6Server2(String vodIpv6Server2) {
		this.vodIpv6Server2 = vodIpv6Server2;
	}


	public String getVodIpv6Server3() {
		return vodIpv6Server3;
	}


	public void setVodIpv6Server3(String vodIpv6Server3) {
		this.vodIpv6Server3 = vodIpv6Server3;
	}


	public String getVodIpv6Server1Type() {
		return vodIpv6Server1Type;
	}


	public void setVodIpv6Server1Type(String vodIpv6Server1Type) {
		this.vodIpv6Server1Type = vodIpv6Server1Type;
	}


	public String getVodIpv6Server2Type() {
		return vodIpv6Server2Type;
	}


	public void setVodIpv6Server2Type(String vodIpv6Server2Type) {
		this.vodIpv6Server2Type = vodIpv6Server2Type;
	}


	public String getVodIpv6Server3Type() {
		return vodIpv6Server3Type;
	}


	public void setVodIpv6Server3Type(String vodIpv6Server3Type) {
		this.vodIpv6Server3Type = vodIpv6Server3Type;
	}

}
