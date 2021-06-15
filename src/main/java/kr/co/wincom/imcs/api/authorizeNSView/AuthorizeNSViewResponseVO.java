package kr.co.wincom.imcs.api.authorizeNSView;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;

@SuppressWarnings("serial")
public class AuthorizeNSViewResponseVO implements Serializable {
	
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
	private String flag						= "";		// 성공여부 코드값 (0:성공, 1:실패)
	private String errCode					= "";		// 오류 구분 코드 (99:시청불가)
	private String watermarkYn				= "";		// 워터마크 여부
	private String linkTime					= "";		// 시청 정보 - 이어보기시간정보
	private String onetimekey				= "";		// OneTimeKey
	private String vodServer1Type			= "";		// VOD 서버 1 타입 (1:castis, 2:주인넷)
	private String vodServer2Type			= "";		// VOD 서버 2 타입 (1:castis, 2:주인넷)
	private String vodServer3Type			= "";		// VOD 서버 3 타입 (1:castis, 2:주인넷)
	private String imgUrl					= "";		// 구간점프 이미지 URL (이미지서버 IP)
	private String imgFileName				= "";		// 구간점프 이미지 파일명
	private String timeInfo					= "";		// 구간 초 정보
	private String liveHevcServer1			= "";		// Live(REAL) HD 서버 1
	private String liveHevcServer2			= "";		// Live(REAL) HD 서버 2
	private String liveHevcServer3			= "";		// Live(REAL) HD 서버 3
	private String liveHevcFileName			= "";		// Live(REAL) HD 파일명
	private String mycutYn					= "";		// MY CUT 가능여부 (Y:가능, N:불가능, C:CP_PROPERTY참조)
	private String smiLanguage				= "";		// 자막제공언어
	private String fmYn						= "";		// Face-Match 준비여부
	private String assetId					= "";		// ASSET_ID 
	private String cpProperty				= "";		// CP별 속성값
	private String cpPropertyUfx			= "";		// UFLIX CP별 속성값
	private String extMetaGb				= "";		// 외부메타 속성
	private String conts360					= "";		// 360도 컨텐츠 여부
	private String presentYn				= "";		// 선물 컨텐츠 여부
	private String hevcYn					= "";		// HEVC 여부
	private String datafreeWatchYn			= "";		// 데이터프리 시청 여부
	private String datafreeOnetimeKey		= "";		// 데이터프리 One_Time_Key
	private String capFileEncryptYn			= "";		// 다국어 자막 암호화 여부
	private String capFileLanguageYn		= "";		// 다국어 자막 어학 여부 (Y:신규자막, N:기존자막)
	private String seasonYn					= "";		// 시즌 여부
	
	private String capFile2Name				= "";		// 자막 파일명2
	private String capFile2Size				= "";		// 자막 파일 사이즈2
	
	private String cpId						= "";

	/********************************************************************
	 * 추가 사용되는 속성값  start
	 ********************************************************************/
	private String pid						= "";
	private String resultCode				= "";
	private String viewFlag					= "";		// S:시청, D:다운로드
	
	private String capYn					= "";		// 자막파일유무
	private String smiYn					= "";
	private String smiImpYn					= "";
	private String smiValue					= "";
	private String openingSkipTime			= "";
	private String endingSkipTime			= "";
	private String fourdReplayYn			= "";
	private String fourdReplayContValue		= "";
	private String musicYn					= "";
	private String vrYn						= "";
	private String contWatermark			= "";
	private String imgType					= "";
	private String imgFileName6s			= "";
	private String liveHevcGb				= "";
	
	//2019.10.11 - IPv6듀얼스택 지원 변수 추가
	private String vodIpv6Server1		  = "";		// VOD IPv6 서버 1
	private String vodIpv6Server2		  = "";		// VOD IPv6 서버 2
	private String vodIpv6Server3		  = "";		// VOD IPv6 서버 3
	private String vodIpv6Server1Type	  = "";		// VOD IPv6 서버 1 타입 (1:castis, 2:주인넷)
	private String vodIpv6Server2Type	  = "";		// VOD IPv6 서버 2 타입 (1:castis, 2:주인넷)
	private String vodIpv6Server3Type	  = "";		// VOD IPv6 서버 3 타입 (1:castis, 2:주인넷)
	private String realHdIpv6Server1      = "";
	private String realHdIpv6Server2      = "";
	private String realHdIpv6Server3      = "";
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		if(	"S".equals(viewFlag) ) {				// 시청
			if("0".equals(flag))		this.errCode	= "";
			this.onetimekey	= "";
			
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
			sb.append(StringUtil.replaceNull(this.imgUrl, "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.replaceNull(this.imgFileName, "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.replaceNull(this.timeInfo, "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.replaceNull(this.liveHevcServer1, "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.replaceNull(this.liveHevcServer2, "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.replaceNull(this.liveHevcServer3, "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.replaceNull(this.mycutYn, "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.replaceNull(this.smiLanguage, "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.replaceNull(this.fmYn, "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.replaceNull(this.assetId, "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.replaceNull(this.cpProperty, "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.replaceNull(this.cpPropertyUfx, "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.replaceNull(this.extMetaGb, "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.replaceNull(this.conts360, "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.replaceNull(this.hevcYn, "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.replaceNull(this.capFileEncryptYn, "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.replaceNull(this.capFileLanguageYn, "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.replaceNull(this.seasonYn, "")).append(ImcsConstants.COLSEP);
		} else if( "D".equals(viewFlag)) {			// 다운로드
			if("0".equals(flag))		this.errCode	= "";
			
			sb.append(StringUtil.replaceNull(this.flag, "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.replaceNull(this.ticketId, "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.replaceNull(this.vodServer1, "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.replaceNull(this.vodFileNameHL, "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.replaceNull(this.vodServer2, "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.replaceNull(this.vodFileNameHL, "")).append(ImcsConstants.COLSEP);		// D는 NEAR가 아닌 LOCAL
			sb.append(StringUtil.replaceNull(this.vodServer3, "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.replaceNull(this.vodFileNameHL, "")).append(ImcsConstants.COLSEP);		// D는 CENTER가 아닌 LOCAL
			sb.append(StringUtil.replaceNull(this.capUrl, "")).append(ImcsConstants.COLSEP);			// cap_url + cap_server
			sb.append(StringUtil.replaceNull(this.capFileName, "")).append(ImcsConstants.COLSEP);		// cap_file_name + cap_file_name
			sb.append(StringUtil.replaceNull(this.capFileSize, "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.replaceNull(this.errCode, "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.replaceNull(this.watermarkYn, "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.replaceNull(this.linkTime, "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.replaceNull(this.vodServer1Type, "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.replaceNull(this.vodServer2Type, "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.replaceNull(this.vodServer3Type, "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.replaceNull(this.imgUrl, "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.replaceNull(this.imgFileName, "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.replaceNull(this.timeInfo, "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.replaceNull(this.liveHevcServer1, "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.replaceNull(this.liveHevcServer2, "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.replaceNull(this.liveHevcServer3, "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.replaceNull(this.liveHevcFileName, "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.replaceNull(this.mycutYn, "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.replaceNull(this.smiLanguage, "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.replaceNull(this.fmYn, "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.replaceNull(this.assetId, "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.replaceNull(this.cpProperty, "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.replaceNull(this.cpPropertyUfx, "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.replaceNull(this.extMetaGb, "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.replaceNull(this.conts360, "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.replaceNull(this.hevcYn, "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.replaceNull(this.capFileEncryptYn, "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.replaceNull(this.capFileLanguageYn, "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.replaceNull(this.seasonYn, "")).append(ImcsConstants.COLSEP);
		}
		
		
		
		return sb.toString();
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
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
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
	public String getTimeInfo() {
		return timeInfo;
	}
	public void setTimeInfo(String timeInfo) {
		this.timeInfo = timeInfo;
	}
	public String getLiveHevcServer1() {
		return liveHevcServer1;
	}
	public void setLiveHevcServer1(String liveHevcServer1) {
		this.liveHevcServer1 = liveHevcServer1;
	}
	public String getLiveHevcServer2() {
		return liveHevcServer2;
	}
	public void setLiveHevcServer2(String liveHevcServer2) {
		this.liveHevcServer2 = liveHevcServer2;
	}
	public String getLiveHevcServer3() {
		return liveHevcServer3;
	}
	public void setLiveHevcServer3(String liveHevcServer3) {
		this.liveHevcServer3 = liveHevcServer3;
	}
	public String getMycutYn() {
		return mycutYn;
	}
	public void setMycutYn(String mycutYn) {
		this.mycutYn = mycutYn;
	}
	public String getSmiLanguage() {
		return smiLanguage;
	}
	public void setSmiLanguage(String smiLanguage) {
		this.smiLanguage = smiLanguage;
	}
	public String getFmYn() {
		return fmYn;
	}
	public void setFmYn(String fmYn) {
		this.fmYn = fmYn;
	}
	public String getAssetId() {
		return assetId;
	}
	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}
	public String getCpProperty() {
		return cpProperty;
	}
	public void setCpProperty(String cpProperty) {
		this.cpProperty = cpProperty;
	}
	public String getCpPropertyUfx() {
		return cpPropertyUfx;
	}
	public void setCpPropertyUfx(String cpPropertyUfx) {
		this.cpPropertyUfx = cpPropertyUfx;
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
	public String getPresentYn() {
		return presentYn;
	}
	public void setPresentYn(String presentYn) {
		this.presentYn = presentYn;
	}
	public String getHevcYn() {
		return hevcYn;
	}
	public void setHevcYn(String hevcYn) {
		this.hevcYn = hevcYn;
	}
	public String getDatafreeWatchYn() {
		return datafreeWatchYn;
	}
	public void setDatafreeWatchYn(String datafreeWatchYn) {
		this.datafreeWatchYn = datafreeWatchYn;
	}
	public String getDatafreeOnetimeKey() {
		return datafreeOnetimeKey;
	}
	public void setDatafreeOnetimeKey(String datafreeOnetimeKey) {
		this.datafreeOnetimeKey = datafreeOnetimeKey;
	}
	public String getCapFileEncryptYn() {
		return capFileEncryptYn;
	}
	public void setCapFileEncryptYn(String capFileEncryptYn) {
		this.capFileEncryptYn = capFileEncryptYn;
	}
	public String getCapFileLanguageYn() {
		return capFileLanguageYn;
	}
	public void setCapFileLanguageYn(String capFileLanguageYn) {
		this.capFileLanguageYn = capFileLanguageYn;
	}
	public String getSeasonYn() {
		return seasonYn;
	}
	public void setSeasonYn(String seasonYn) {
		this.seasonYn = seasonYn;
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
	public String getViewFlag() {
		return viewFlag;
	}
	public void setViewFlag(String viewFlag) {
		this.viewFlag = viewFlag;
	}
	public String getLiveHevcFileName() {
		return liveHevcFileName;
	}
	public void setLiveHevcFileName(String liveHevcFileName) {
		this.liveHevcFileName = liveHevcFileName;
	}
	public String getCapYn() {
		return capYn;
	}
	public void setCapYn(String capYn) {
		this.capYn = capYn;
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


	public String getCapFile2Name() {
		return capFile2Name;
	}


	public void setCapFile2Name(String capFile2Name) {
		this.capFile2Name = capFile2Name;
	}


	public String getCapFile2Size() {
		return capFile2Size;
	}


	public void setCapFile2Size(String capFile2Size) {
		this.capFile2Size = capFile2Size;
	}


	public String getCpId() {
		return cpId;
	}


	public void setCpId(String cpId) {
		this.cpId = cpId;
	}
	
	
	public String getSmiValue()								{ return smiValue; }
	public void setSmiValue(String smiValue)				{ this.smiValue = smiValue; }
	
	public String getOpeningSkipTime()						{ return openingSkipTime; }
	public void setOpeningSkipTime(String openingSkipTime)	{ this.openingSkipTime = openingSkipTime; }
	
	public String getEndingSkipTime()						{ return endingSkipTime; }
	public void setEndingSkipTime(String endingSkipTime)	{ this.endingSkipTime = endingSkipTime; }
	
	public String getFourdReplayYn()						{ return fourdReplayYn; }
	public void setFourdReplayYn(String fourdReplayYn)		{ this.fourdReplayYn = fourdReplayYn; }
	
	public String getFourdReplayContValue()								{ return fourdReplayContValue; }
	public void setFourdReplayContValue(String fourdReplayContValue)	{ this.fourdReplayContValue = fourdReplayContValue; }
	
	public String getMusicYn()								{ return musicYn; }
	public void setMusicYn(String musicYn)					{ this.musicYn = musicYn; }
	
	public String getVrYn()									{ return vrYn; }
	public void setVrYn(String vrYn)						{ this.vrYn = vrYn; }
	
	public String getContWatermark()						{ return contWatermark; }
	public void setContWatermark(String contWatermark)		{ this.contWatermark = contWatermark; }
	
	public String getImgType()								{ return imgType; }
	public void setImgType(String imgType)					{ this.imgType = imgType; }
	
	public String getImgFileName6s()						{ return imgFileName6s; }
	public void setImgFileName6s(String imgFileName6s)		{ this.imgFileName6s = imgFileName6s; }
	
	public String getLiveHevcGb()							{ return liveHevcGb; }
	public void setLiveHevcGb(String liveHevcGb)			{ this.liveHevcGb = liveHevcGb; }


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


	public String getRealHdIpv6Server1() {
		return realHdIpv6Server1;
	}


	public void setRealHdIpv6Server1(String realHdIpv6Server1) {
		this.realHdIpv6Server1 = realHdIpv6Server1;
	}


	public String getRealHdIpv6Server2() {
		return realHdIpv6Server2;
	}


	public void setRealHdIpv6Server2(String realHdIpv6Server2) {
		this.realHdIpv6Server2 = realHdIpv6Server2;
	}


	public String getRealHdIpv6Server3() {
		return realHdIpv6Server3;
	}


	public void setRealHdIpv6Server3(String realHdIpv6Server3) {
		this.realHdIpv6Server3 = realHdIpv6Server3;
	}
	
	
	//liveHevcGb
	
}
