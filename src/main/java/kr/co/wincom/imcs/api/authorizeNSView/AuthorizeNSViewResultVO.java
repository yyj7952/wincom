package kr.co.wincom.imcs.api.authorizeNSView;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.StatVO;

@SuppressWarnings("serial")
public class AuthorizeNSViewResultVO  extends StatVO implements Serializable {
	
	class authorizeSView {
		
		String vodServer1					= "";
		String vodServer2					= "";
		String vodServer3					= "";
		String vodServer1Type				= "";
		String vodServer2Type				= "";
		String vodServer3Type				= "";
		String vodNodeGroup					= "";
		String vodIpv6Node1					= "";
		String vodIpv6Node2					= "";
		String vodIpv6Node3					= "";
		//2019.10.11 - IPv6듀얼스택 지원 변수 추가
		String vodIpv6Server1		  = "";		// VOD IPv6 서버 1
		String vodIpv6Server2		  = "";		// VOD IPv6 서버 2
		String vodIpv6Server3		  = "";		// VOD IPv6 서버 3
		String vodIpv6Port1			  = "";
		String vodIpv6Server1Type	  = "";		// VOD IPv6 서버 1 타입 (1:castis, 2:주인넷)
		String vodIpv6Server2Type	  = "";		// VOD IPv6 서버 2 타입 (1:castis, 2:주인넷)
		String vodIpv6Server3Type	  = "";		// VOD IPv6 서버 3 타입 (1:castis, 2:주인넷)
	}
	authorizeSView[] listAuthorizeSView		= new authorizeSView[20]; 
	

	private int resultSet					= 0;
	private int dataChk						= 0;

	private String tmpSndBuf				= "";
	private String tmpSndBufStemp			= "";
	private String tmpSndBufCpn				= "";
	private String tmpSndBufCpnUse			= "";
	private String tmpSndBufProfile			= "";
		

//	private String saId						= "";	// 가입자정보
//	private String albumId					= "";	// 앨범 ID
	private String vodServer1				= "";
	private String vodFileNamel				= "";
	private String vodServer2				= "";
	private String vodFileNamen				= "";
	private String vodServer3				= "";
	private String vodFileNamec				= "";
	private String vodFileNamel1			= "";
	private String vodFileNamen1			= "";
	private String vodFileNamec1			= "";
	private String capUrl					= "";
	private String capServer				= "";
	private String capFileName				= "";
	private String capFileSize				= "";
	private String watermarkYn				= "";
	private String linkTime					= "";
	private String vodServer1Type			= "";
	private String vodServer2Type			= "";
	private String vodServer3Type			= "";
	private String imgUrl					= "";
	private String imgFileName				= "";
	private String timeInfo					= "";
	private String liveHevcServer1			= "";
	private String liveHevcServer2			= "";
	private String liveHevcServer3			= "";
	private String mycutYn					= "";
	private String smiLanguage				= "";
	private String fmYn						= "";
	private String fmAssetId				= "";
	private String cpProperty				= "";
	private String cpPropertyUfx			= "";
	private String extMetaGb				= "";
	private String conts360					= "";
	private String hevcYn					= "";
	private String capFileEncryptYn			= "";
	private String capFileLanguageYn		= "";
	private String seasonYn					= "";
	private String openingSkipTime			= "";
	private String endingSkipTime			= "";
	private String imgFileName6S			= "";
	
	private String fourdReplayYn			= "";
	private String fourdReplayContValue		= "";
	private String vodFileName				= "";
	private String liveHevcFileName			= "";
	private int capYn					    = 0; 
	private String smiYn					= "";
	private String smiImpYn					= "";
	private String contWatermark			= "";
	private String capFile2Name				= "";
	private String capFile2Size				= "";
	private String cpId						= "";
	private String musicYn					= "";
	private String vrYn						= "";
	private String vodIpv6Node1				= "";
	private String vodIpv6Node2				= "";
	private String vodIpv6Node3				= "";
	
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
	
		
	// Default Constructor
	AuthorizeNSViewResultVO() {
		assignAuthorizeSView(listAuthorizeSView.length);
	}
	
	private void assignAuthorizeSView(int nSize) {
		
		for(int i = 0; i < nSize; i++ ) {
			listAuthorizeSView[i] = new authorizeSView();
		}
	}
	
	public authorizeSView[] getListAuthorizeSView() 						{ return listAuthorizeSView; }
	public void setListAuthorizeSView(authorizeSView[] listAuthorizeSView)	{
		this.listAuthorizeSView = listAuthorizeSView;
		assignAuthorizeSView(listAuthorizeSView.length);
	}
	
	/********************************************************************
	 * Getter/Setter
	 ********************************************************************/
	
	
	public int getResultSet() 												{ return resultSet; }
	public void setResultSet(int resultSet) 								{ this.resultSet = resultSet; }
	
	public int getDataChk() 												{ return dataChk; }
	public void setDataChk(int dataChk) 									{ this.dataChk = dataChk; }
	
	public String getTmpSndBuf()											{ return tmpSndBuf; }
	public void setTmpSndBuf(String tmpSndBuf)								{ this.tmpSndBuf = tmpSndBuf; }
	
	public String getTmpSndBufStemp()										{ return tmpSndBufStemp; }
	public void setTmpSndBufStemp(String tmpSndBufStemp )					{ this.tmpSndBufStemp = tmpSndBufStemp; }
	
	public String getTmpSndBufCpn()											{ return tmpSndBufCpn; }
	public void setTmpSndBufCpn(String tmpSndBufCpn )						{ this.tmpSndBufCpn = tmpSndBufCpn; }
	
	public String getTmpSndBufCpnUse()										{ return tmpSndBufCpnUse; }
	public void setTmpSndBufCpnUse(String tmpSndBufCpnUse )					{ this.tmpSndBufCpnUse = tmpSndBufCpnUse; }
	
	public String getTmpSndBufProfile() 									{	return tmpSndBufProfile; }
	public void setTmpSndBufProfile(String tmpSndBufProfile) 				{ this.tmpSndBufProfile = tmpSndBufProfile; }
	
//	public String getSaId()													{ return saId; }
//	public void setSaId(String saId)										{ this.saId = saId; }
//	
//	public String getAlbumId() 												{ return albumId; }
//	public void setAlbumId(String albumId) 									{ this.albumId = albumId; }
	
	public String getVodServer1() 											{ return vodServer1; }
	public void setVodServer1(String vodServer1) 							{ this.vodServer1 = vodServer1; }
	
	public String getVodFileNamel()											{ return vodFileNamel;}
	public void setVodFileNamel(String vodFileNamel ) 						{ this.vodFileNamel = vodFileNamel; }
	
	public String getVodServer2()											{ return vodServer2;}
	public void setVodServer2(String vodServer2 ) 							{ this.vodServer2 = vodServer2; }
	
	public String getVodFileNamen()											{ return vodFileNamen;}
	public void setVodFileNamen(String vodFileNamen ) 						{ this.vodFileNamen = vodFileNamen; }
	
	public String getVodServer3()											{ return vodServer3;}
	public void setVodServer3(String vodServer3 ) 							{ this.vodServer3 = vodServer3; }
	
	public String getVodFileNamec()											{ return vodFileNamec;}
	public void setVodFileNamec(String vodFileNamec ) 						{ this.vodFileNamec = vodFileNamec; }
	
	public String getVodFileNamel1()										{ return vodFileNamel1;}
	public void setVodFileNamel1(String vodFileNamel1 ) 					{ this.vodFileNamel1 = vodFileNamel1; }
	
	public String getVodFileNamen1()										{ return vodFileNamen1;}
	public void setVodFileNamen1(String vodFileNamen1 ) 					{ this.vodFileNamen1 = vodFileNamen1; }
	
	public String getVodFileNamec1()										{ return vodFileNamec1;}
	public void setVodFileNamec1(String vodFileNamec1 ) 					{ this.vodFileNamec1 = vodFileNamec1; }
	
	public String getCapUrl()												{ return capUrl;}
	public void setCapUrl(String capUrl ) 									{ this.capUrl = capUrl; }
	
	public String getCapServer()											{ return capServer;}
	public void setCapServer(String capServer ) 							{ this.capServer = capServer; }
	
	public String getCapFileName()											{ return capFileName;}
	public void setCapFileName(String capFileName ) 						{ this.capFileName = capFileName; }
	
	public String getCapFileSize()											{ return capFileSize;}
	public void setCapFileSize(String capFileSize ) 						{ this.capFileSize = capFileSize; }
	
	public String getWatermarkYn()											{ return watermarkYn;}
	public void setWatermarkYn(String watermarkYn ) 						{ this.watermarkYn = watermarkYn; }
	
	public String getLinkTime()												{ return linkTime;}
	public void setLinkTime(String linkTime ) 								{ this.linkTime = linkTime; }
	
	public String getVodServer1Type()										{ return vodServer1Type;}
	public void setVodServer1Type(String vodServer1Type ) 					{ this.vodServer1Type = vodServer1Type; }
	
	public String getVodServer2Type()										{ return vodServer2Type;}
	public void setVodServer2Type(String vodServer2Type ) 					{ this.vodServer2Type = vodServer2Type; }
	
	public String getVodServer3Type()										{ return vodServer3Type;}
	public void setVodServer3Type(String vodServer3Type ) 					{ this.vodServer3Type = vodServer3Type; }
	
	public String getImgUrl()												{ return imgUrl;}		
	public void setImgUrl(String imgUrl ) 									{ this.imgUrl = imgUrl; }
	
	public String getImgFileName()											{ return imgFileName;}
	public void setImgFileName(String imgFileName) 							{ this.imgFileName = imgFileName; }
	
	public String getTimeInfo()												{ return timeInfo;}
	public void setTimeInfo(String timeInfo ) 								{ this.timeInfo = timeInfo; }
	
	public String getLiveHevcServer1()										{ return liveHevcServer1;}
	public void setLiveHevcServer1(String liveHevcServer1 ) 				{ this.liveHevcServer1 = liveHevcServer1; }
	
	public String getLiveHevcServer2()										{ return liveHevcServer2;}
	public void setLiveHevcServer2(String liveHevcServer2 ) 				{ this.liveHevcServer2 = liveHevcServer2; }
	
	public String getLiveHevcServer3()										{ return liveHevcServer3;}
	public void setLiveHevcServer3(String liveHevcServer3) 					{ this.liveHevcServer3 = liveHevcServer3; }
	
	public String getMycutYn()												{ return mycutYn;}
	public void setMycutYn(String mycutYn ) 								{ this.mycutYn = mycutYn; }
	
	public String getSmiLanguage()											{ return smiLanguage;}
	public void setSmiLanguage(String smiLanguage ) 						{ this.smiLanguage = smiLanguage; }
	
	public String getFmYn()													{ return fmYn;}
	public void setFmYn(String fmYn) 										{ this.fmYn = fmYn; }
	
	public String getFmAssetId() 											{ return fmAssetId; }
	public void setFmAssetId(String fmAssetId) 								{ this.fmAssetId = fmAssetId; }
	
	public String getCpProperty()											{ return cpProperty;}
	public void setCpProperty(String cpProperty ) 							{ this.cpProperty = cpProperty; }
	
	public String getCpPropertyUfx()										{ return cpPropertyUfx;}
	public void setCpPropertyUfx(String cpPropertyUfx ) 					{ this.cpPropertyUfx = cpPropertyUfx; }
	
	public String getExtMetaGb()											{ return extMetaGb;}
	public void setExtMetaGb(String extMetaGb ) 							{ this.extMetaGb = extMetaGb; }
	
	public String getConts360()												{ return conts360;}
	public void setConts360(String conts360 ) 								{ this.conts360 = conts360; }
	
	public String getHevcYn()												{ return hevcYn;}
	public void setHevcYn(String hevcYn ) 									{ this.hevcYn = hevcYn; }
	
	public String getCapFileEncryptYn()										{ return capFileEncryptYn;}
	public void setCapFileEncryptYn(String capFileEncryptYn ) 				{ this.capFileEncryptYn = capFileEncryptYn; }
	
	public String getCapFileLanguageYn()									{ return capFileLanguageYn;}
	public void setCapFileLanguageYn(String capFileLanguageYn ) 			{ this.capFileLanguageYn = capFileLanguageYn; }
	
	public String getSeasonYn()												{ return seasonYn;}
	public void setSeasonYn(String seasonYn) 								{ this.seasonYn = seasonYn; }
	
	public String getOpeningSkipTime()										{ return openingSkipTime;}
	public void setOpeningSkipTime(String openingSkipTime ) 				{ this.openingSkipTime = openingSkipTime; }
	
	public String getEndingSkipTime()										{ return endingSkipTime;}
	public void setEndingSkipTime(String endingSkipTime ) 					{ this.endingSkipTime = endingSkipTime; }
	
	public String getImgFileName6S()										{ return imgFileName6S;}
	public void setImgFileName6S(String imgFileName6S ) 					{ this.imgFileName6S = imgFileName6S; }
			
	public String getFourdReplayYn() 										{ return fourdReplayYn; }
	public void setFourdReplayYn(String fourdReplayYn) 						{ this.fourdReplayYn = fourdReplayYn; }
	
	public String getFourdReplayContValue()									{ return fourdReplayContValue; }
	public void setFourdReplayContValue(String fourdReplayContValue)		{ this.fourdReplayContValue = fourdReplayContValue; }
	
	public String getVodFileName() 											{ return vodFileName; }
	public void setVodFileName(String vodFileName) 							{ this.vodFileName = vodFileName; }
	
	public String getLiveHevcFileName() 									{ return liveHevcFileName; }
	public void setLiveHevcFileName(String liveHevcFileName) 				{ this.liveHevcFileName = liveHevcFileName; }
	
	public int getCapYn() 													{ return capYn; }
	public void setCapYn(int capYn) 										{ this.capYn = capYn; }
	
	public String getSmiYn() 												{ return smiYn; }
	public void setSmiYn(String smiYn) 										{ this.smiYn = smiYn; }
	
	public String getSmiImpYn() 											{ return smiImpYn; }
	public void setSmiImpYn(String smiImpYn) 								{ this.smiImpYn = smiImpYn; }
	
	public String getContWatermark() 										{ return contWatermark; }
	public void setContWatermark(String contWatermark) 						{ this.contWatermark = contWatermark; }
	
	public String getCapFile2Name() 										{ return capFile2Name; }
	public void setCapFile2Name(String capFile2Name) 						{ this.capFile2Name = capFile2Name; }
	
	public String getCapFile2Size() 										{ return capFile2Size; }
	public void setCapFile2Size(String capFile2Size) 						{ this.capFile2Size = capFile2Size; }
	
	public String getCpId() 												{ return cpId; }
	public void setCpId(String cpId) 										{ this.cpId = cpId; }
	
	public String getMusicYn() 												{ return musicYn; }
	public void setMusicYn(String musicYn) 									{ this.musicYn = musicYn; }
	
	public String getVrYn() 												{ return vrYn; }
	public void setVrYn(String vrYn) 										{ this.vrYn = vrYn; }
	
	public String getVodIpv6Node1()											{ return vodIpv6Node1;}
	public void setVodIpv6Node1(String vodIpv6Node1 ) 						{ this.vodIpv6Node1 = vodIpv6Node1; }
	
	public String getVodIpv6Node2()											{ return vodIpv6Node2;}
	public void setVodIpv6Node2(String vodIpv6Node2 ) 						{ this.vodIpv6Node2 = vodIpv6Node2; }
	
	public String getVodIpv6Node3()											{ return vodIpv6Node3;}
	public void setVodIpv6Node3(String vodIpv6Node3) 						{ this.vodIpv6Node3 = vodIpv6Node3; }
	
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

	/********************************************************************
	 * Other Functions
	 ********************************************************************/
	
	public void setAlbumInfo(HashMap<String, String> mapAlbumInfo) {
		
		this.capYn = Integer.parseInt(StringUtil.nullToZero(mapAlbumInfo.get("CAP_YN")) );
		this.capFileName = StringUtil.nullToSpace(mapAlbumInfo.get("CAP_FILE_NAME") );
		this.capFileSize = StringUtil.nullToSpace(mapAlbumInfo.get("CAP_FILE_SIZE") );
		this.smiYn = StringUtil.nullToSpace(mapAlbumInfo.get("SMI_YN") );
		this.smiImpYn = StringUtil.nullToSpace(mapAlbumInfo.get("SMI_IMP_YN") );
		this.contWatermark = StringUtil.nullToSpace(mapAlbumInfo.get("CONT_WATERMARK") );
		this.smiLanguage = StringUtil.nullToSpace(mapAlbumInfo.get("SMI_LANGUAGE") );
		this.mycutYn = StringUtil.nullToSpace(mapAlbumInfo.get("MYCUT_YN") );
		this.cpProperty = StringUtil.nullToSpace(mapAlbumInfo.get("CP_PROPERTY") );
		this.cpPropertyUfx = StringUtil.nullToSpace(mapAlbumInfo.get("CP_PROPERTY_UFX") );
		this.conts360 = StringUtil.nullToSpace(mapAlbumInfo.get("CONTS_360") );
		this.extMetaGb = StringUtil.nullToSpace(mapAlbumInfo.get("EXT_META_GB") );
		this.capFile2Name = StringUtil.nullToSpace(mapAlbumInfo.get("CAP_FILE2_NAME") );
		this.capFile2Size = StringUtil.nullToSpace(mapAlbumInfo.get("CAP_FILE2_SIZE") );
		this.capFileEncryptYn = StringUtil.nullToSpace(mapAlbumInfo.get("CAP_FILE_ENCRYPT_YN") );
		this.capFileLanguageYn = StringUtil.nullToSpace(mapAlbumInfo.get("CAP_FILE_LANGUAGE_YN") );
		this.cpId = StringUtil.nullToSpace(mapAlbumInfo.get("CP_ID") );
		this.openingSkipTime = StringUtil.nullToSpace(mapAlbumInfo.get("OPENING_SKIP_TIME") );
		this.endingSkipTime = StringUtil.nullToSpace(mapAlbumInfo.get("ENDING_SKIP_TIME") );
		this.fourdReplayYn = StringUtil.nullToSpace(mapAlbumInfo.get("FOURD_REPLAY_YN") );
		this.fourdReplayContValue = StringUtil.nullToSpace(mapAlbumInfo.get("FOURD_REPLAY_CONT_VALUE") );
		this.musicYn = StringUtil.nullToSpace(mapAlbumInfo.get("MUSIC_YN") );
		this.vrYn = StringUtil.nullToSpace(mapAlbumInfo.get("VR_YN") );		
		// 2020.05.08 - 프로야구 4D Replay 확장자를 물리 파일의 확장자 그대로 주기로 수정
		this.fourdReplayContValue = String.format("%s", this.fourdReplayContValue);
	}
	
	
	
	
	@Override
	public String toString() {
		
		String strResult = makeTmpSndBuf();
		if("S".equals(this.getViewType()) ) {
			strResult += tmpSndBufProfile;
		}
		
		return strResult;
	}
	
	public String makeTmpSndBuf() {
		
		StringBuffer sb		=  null;
		String tmpSndBuf	= "";
		
		if(1 == this.dataChk ) {
			
			if("S".equals(this.getViewType()) && "N".equals(this.fourdReplayYn) ) {
				
				sb = new StringBuffer();
				tmpSndBuf = makeResultCase01(sb);
			} else if("S".equals(this.getViewType()) && "Y".equals(this.fourdReplayYn) ) {
				
				sb = new StringBuffer();
				tmpSndBuf = makeResultCase02(sb);
			} else {
				
				sb = new StringBuffer();
				tmpSndBuf = makeResultCase03(sb);
			}
		} else if( 0 == this.resultSet && 0 == this.dataChk ){
			
			if("S".equals(this.getViewType()) ) {
				tmpSndBuf = makeResultCase04_1();
			} else {
				tmpSndBuf = makeResultCase04_2();
			}
			
			this.setResultCode("21000000");
		}else {
			
			if("S".equals(this.getViewType()) ) {
				tmpSndBuf = makeResultCase04_3();
			} else {
				tmpSndBuf = makeResultCase04_4();
			}
			
			this.setResultCode("21000000"); 
		}
		
		return tmpSndBuf;
	}
		
	private String makeResultCase01(StringBuffer sb) {
		
		sb.append(StringUtil.replaceNull(this.tmpSndBuf, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodServer1, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodFileNamel, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodServer2, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodFileNamen, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodServer3, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodFileNamec, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodServer1, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodFileNamel1, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodServer2, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodFileNamen1, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodServer3, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodFileNamec1, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.capUrl, "")).append(ImcsConstants.ARRSEP);
		sb.append(StringUtil.replaceNull(this.capServer, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.capFileName, "")).append(ImcsConstants.ARRSEP);
		sb.append(StringUtil.replaceNull(this.capFileName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.capFileSize, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(null, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.watermarkYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.linkTime, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(null, "")).append(ImcsConstants.COLSEP);
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
		sb.append(StringUtil.replaceNull(this.fmAssetId, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.cpProperty, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.cpPropertyUfx, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.extMetaGb, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.conts360, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.hevcYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.capFileEncryptYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.capFileLanguageYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.seasonYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.openingSkipTime, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.endingSkipTime, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.imgFileName6S, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodIpv6Node1, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodIpv6Node2, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodIpv6Node3, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodIpv6Server1, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodIpv6Server2, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodIpv6Server3, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodIpv6Server1Type, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodIpv6Server2Type, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodIpv6Server3Type, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.realHdIpv6Server1, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.realHdIpv6Server2, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.realHdIpv6Server3, "")).append(ImcsConstants.COLSEP);
		sb.append(ImcsConstants.ROWSEP);
		sb.append(StringUtil.replaceNull(this.tmpSndBufStemp, ""));
		sb.append(ImcsConstants.ROWSEP);
		sb.append(StringUtil.replaceNull(this.tmpSndBufCpn, ""));
		sb.append(ImcsConstants.ROWSEP);
		sb.append(StringUtil.replaceNull(this.tmpSndBufCpnUse, ""));
		sb.append(ImcsConstants.ROWSEP);
		
		return sb.toString();
	}
	
	private String makeResultCase02(StringBuffer sb) {
		
		sb.append(StringUtil.replaceNull(this.tmpSndBuf, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.listAuthorizeSView[19].vodServer1, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.fourdReplayContValue, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.listAuthorizeSView[19].vodServer2, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.fourdReplayContValue, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.listAuthorizeSView[19].vodServer3, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.fourdReplayContValue, "")).append("|||||||" );
		sb.append(StringUtil.replaceNull(this.capUrl, "")).append(ImcsConstants.ARRSEP);
		sb.append(StringUtil.replaceNull(this.capServer, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.capFileName, "")).append(ImcsConstants.ARRSEP);
		sb.append(StringUtil.replaceNull(this.capFileName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.capFileSize, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(null, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.watermarkYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.linkTime, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(null, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.listAuthorizeSView[19].vodServer1Type, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.listAuthorizeSView[19].vodServer2Type, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.listAuthorizeSView[19].vodServer3Type, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.imgUrl, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.imgFileName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.timeInfo, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.liveHevcServer1, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.liveHevcServer2, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.liveHevcServer3, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.mycutYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.smiLanguage, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.fmYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.fmAssetId, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.cpProperty, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.cpPropertyUfx, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.extMetaGb, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.conts360, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.hevcYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.capFileEncryptYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.capFileLanguageYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.seasonYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.openingSkipTime, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.endingSkipTime, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.imgFileName6S, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.listAuthorizeSView[19].vodIpv6Node1, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.listAuthorizeSView[19].vodIpv6Node2, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.listAuthorizeSView[19].vodIpv6Node3, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.listAuthorizeSView[19].vodIpv6Server1, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.listAuthorizeSView[19].vodIpv6Server2, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.listAuthorizeSView[19].vodIpv6Server3, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.listAuthorizeSView[19].vodIpv6Server1Type, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.listAuthorizeSView[19].vodIpv6Server2Type, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.listAuthorizeSView[19].vodIpv6Server3Type, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull("", "")).append(ImcsConstants.COLSEP);//realHdIpv6Server1,
		sb.append(StringUtil.replaceNull("", "")).append(ImcsConstants.COLSEP);//realHdIpv6Server1,
		sb.append(StringUtil.replaceNull("", "")).append(ImcsConstants.COLSEP);//realHdIpv6Server1,
		sb.append(ImcsConstants.ROWSEP);
		sb.append(StringUtil.replaceNull(this.tmpSndBufStemp, ""));
		sb.append(ImcsConstants.ROWSEP);
		sb.append(StringUtil.replaceNull(this.tmpSndBufCpn, ""));
		sb.append(ImcsConstants.ROWSEP);
		sb.append(StringUtil.replaceNull(this.tmpSndBufCpnUse, ""));
		sb.append(ImcsConstants.ROWSEP);
		
		return sb.toString();
	}
	
private String makeResultCase03(StringBuffer sb) {
		
		sb.append(StringUtil.replaceNull(this.tmpSndBuf, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodServer1, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodFileName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodServer2, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodFileName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodServer3, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodFileName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.capUrl, "")).append(ImcsConstants.ARRSEP);
		sb.append(StringUtil.replaceNull(this.capServer, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.capFileName, "")).append(ImcsConstants.ARRSEP);
		sb.append(StringUtil.replaceNull(this.capFileName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.capFileSize, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(null, "")).append(ImcsConstants.COLSEP);
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
		sb.append(StringUtil.replaceNull(this.fmAssetId, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.cpProperty, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.cpPropertyUfx, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.extMetaGb, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.conts360, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.hevcYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.capFileEncryptYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.capFileLanguageYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.seasonYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.imgFileName6S, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodIpv6Node1, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodIpv6Node2, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodIpv6Node3, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodIpv6Server1, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodIpv6Server2, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodIpv6Server3, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodIpv6Server1Type, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodIpv6Server2Type, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodIpv6Server3Type, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.realHdIpv6Server1, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.realHdIpv6Server2, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.realHdIpv6Server3, "")).append(ImcsConstants.COLSEP);
		sb.append(ImcsConstants.ROWSEP);
		sb.append(StringUtil.replaceNull(this.tmpSndBufStemp, ""));
		sb.append(ImcsConstants.ROWSEP);
		sb.append(StringUtil.replaceNull(this.tmpSndBufCpn, ""));
		sb.append(ImcsConstants.ROWSEP);
		sb.append(StringUtil.replaceNull(this.tmpSndBufCpnUse, ""));
		sb.append(ImcsConstants.ROWSEP);
		
		return sb.toString();
	}
		
	private String makeResultCase04_1() {
		
		String retVal = String.format("%s||||||||||||||\b|\b||%s|%s|||||||||||||||||||||||||||||||||\f\f\f\f", "2", "60", this.watermarkYn );
		return retVal;
	}
	
	private String makeResultCase04_2() {
		
		String retVal = String.format("%s||||||||\b|\b||%s|%s|||||||||||||||||||||||||||||||||\f\f\f\f", "2", "60", this.watermarkYn );
		return retVal;
	}
	
	private String makeResultCase04_3() {
		
		String retVal = String.format("%s||||||||||||||\b|\b||%s|%s|||||||||||||||||||||||||||||||||\f\f\f\f", "1", "60", this.watermarkYn );
		return retVal;
	}
	
	private String makeResultCase04_4() {
		
		String retVal = String.format("%s||||||||\b|\b||%s|%s|||||||||||||||||||||||||||||||||\f\f\f\f", "1", "60", this.watermarkYn );
		return retVal;
	}
	
}
