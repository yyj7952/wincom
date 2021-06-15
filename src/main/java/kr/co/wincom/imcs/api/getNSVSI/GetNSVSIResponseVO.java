package kr.co.wincom.imcs.api.getNSVSI;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;


@SuppressWarnings("serial")
public class GetNSVSIResponseVO implements Serializable {
	/********************************************************************
	 * GetNSVSI API 전문 칼럼(순서 일치)
	********************************************************************/
    private String serviceId 			= "";
    private String serviceName 			= "";
    private String serviceEngName 		= "";
    
    private String liveServer1 			= "";
    private String liveFileName1		= "";
    private String liveServer2 			= "";
    private String liveFileName2 		= "";
    private String liveServer3 			= "";
    private String liveFileName3 		= "";
    private String liveServer4 			= "";
    private String liveFileName4 		= "";
    private String liveServer5 			= "";
    private String liveFileName5 		= "";
    private String liveServer6 			= "";
    private String liveFileName6 		= "";
    
    private String liveIp1 				= "";
    private String liveIp2 				= "";
    private String liveIp3 				= "";
    private String liveIp4 				= "";
    private String liveIp5 				= "";
    private String liveIp6 				= "";
    private String livePort 			= "";
    
    private String imgUrl 				= "";
    private String imgFileName 			= "";
    private String serviceType 			= "";
    private String pvrYn 				= "";
    private String localAreaCode 		= "";
    private String runningStatus 		= "";
    private String channelNo 			= "";
    private String barkerChannel 		= "";
    private String description 			= "";
    private String sortNo 				= "";
    private String filteringCode 		= "";
    private String maxBitrate 			= "";
    private String vodServer1Type 		= "";
    private String vodServer2Type 		= "";
    private String vodServer3Type 		= "";
    private String favorYn 				= "";    
    private String genre1 				= "";
    private String arsNo1 				= "";
    private String arsNo2 				= "";
    private String prodDescUrl 			= "";
    private String virtualType 			= "";
    private String timeAppYn 			= "";
    private String liveTimeServer1 		= "";
    private String liveTimeServer2 		= "";
    private String liveTimeServer3 		= "";
    private String pooqYn 				= "";
    private String pooqGenreName 		= "";
    private String productId 			= "";
    private String productName 			= "";
    private String saveTime 			= "";
    private String startChunk 			= "";
    private String isFhd 				= "";
    private String adultYn	 			= "";
    private String ratings	 			= "";
    private String chnlGrp	 			= "";
    private String isUhd 				= "";
    private String comName 				= "";	// GenreName
    private String chatYn 				= "";
    private String chatId 				= "";
    private String chatName 			= "";
    
    private String tsFileName1 			= "";
    private String tsFileName2 			= "";
    private String tsFileName3 			= "";
    private String tsFileName4 			= "";
    private String tsFileName5 			= "";
    private String tsFileName6 			= "";
    private String tsLowFileName1 		= "";
    private String tsLowFileName2 		= "";
    private String tsLowFileName3 		= "";
    
    private String hdtvViewGb 			= "";
    private String serviceRefId 		= "";
    
    
    /********************************************************************
	 * 추가 사용되는 속성값  start
	 ********************************************************************/
    private String liveTimeGb 			= "";
    private String liveTimeServer 		= "";
    private String contentsId 			= "";
    
    //2018.02.23 - 프로야구2.0/골프APP 변수 추가
    private String msvcId				= ""; 	// 2018.01.16 - 프로야구2.0 에서 메인/서브채널 개념이 생기면서 시보딜레이ID 변수를 메인서비스ID로 변경
    private String chnlType				= ""; 	// 프로야구에서 서브채널에서 대한 포지션 정보
    private String m3u8Info				= ""; 	// 채널 hevc화질 서비스 여부
    private String liveServerL 			= "";	// 화질에 대한 CDN 서버 타입 제공
    private String liveFileNameL		= "";	// 화질 추가에 따른 배열로 m3u8파일명 전달 (local)
    private String liveServerN 			= "";	// 화질에 대한 CDN 서버 타입 제공
    private String liveFileNameN		= "";	// 화질 추가에 따른 배열로 m3u8파일명 전달 (near)
    private String liveServerC 			= "";	// 화질에 대한 CDN 서버 타입 제공
    private String liveFileNameC		= "";	// 화질 추가에 따른 배열로 m3u8파일명 전달 (center)
    private String tsFileNameL			= "";	// 화질 추가에 따른 배열로 시보딜레이m3u8파일명 전달 (local)
    private String tsFileNameN			= "";	// 화질 추가에 따른 배열로 시보딜레이m3u8파일명 전달 (near)
    private String tsFileNameC			= "";	// 화질 추가에 따른 배열로 시보딜레이m3u8파일명 전달 (center)
    
    
    @Override
    public String toString() {
		StringBuffer sb = new StringBuffer();
		// 몇몇 row의 adult_yn이 " "로 나타나는 경우가 있는데, 모르겠음
			
		sb.append(StringUtil.nullToSpace(this.getServiceId())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getServiceName())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getServiceEngName())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getLiveServer1())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getLiveFileName1())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getLiveServer2())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getLiveFileName2())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getLiveServer3())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getLiveFileName3())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getLiveServer4())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getLiveFileName4())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getLiveServer5())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getLiveFileName5())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getLiveServer6())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getLiveFileName6())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getImgUrl())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getImgFileName())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getServiceType())).append(ImcsConstants.COLSEP);
		sb.append(this.getPvrYn()).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getLocalAreaCode())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getRunningStatus())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getChannelNo())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getBarkerChannel())).append(ImcsConstants.COLSEP);
		sb.append(this.getDescription()).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getSortNo())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getFilteringCode())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getMaxBitrate())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getVodServer1Type())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getVodServer2Type())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getVodServer3Type())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getFavorYn())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getGenre1())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getArsNo1())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getArsNo2())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getProdDescUrl())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getVirtualType())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getTimeAppYn())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getLiveTimeServer1())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getLiveTimeServer2())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getLiveTimeServer3())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getPooqYn())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getPooqGenreName())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getProductId())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getProductName())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getSaveTime())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getStartChunk())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getIsUhd())).append(ImcsConstants.COLSEP);
		sb.append(this.getAdultYn()).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getRatings())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getChnlGrp())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getIsFhd())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getComName())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getChatYn())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getChatId())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getChatName())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getTsFileName1())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getTsFileName2())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getTsFileName3())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getTsLowFileName1())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getTsLowFileName2())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getTsLowFileName3())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getTsFileNameL())).append(ImcsConstants.COLSEP);	
		sb.append(StringUtil.nullToSpace(this.getTsFileNameN())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getTsFileNameC())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getmsvcId())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getChnlType())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getM3u8Info())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getLiveServerL())).append(ImcsConstants.COLSEP);
		sb.append(this.getLiveFileNameL()).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getLiveServerN())).append(ImcsConstants.COLSEP);
		sb.append(this.getLiveFileNameN()).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getLiveServerC())).append(ImcsConstants.COLSEP);
		sb.append(this.getLiveFileNameC()).append(ImcsConstants.COLSEP);

		return sb.toString();
	}
    
    
    
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getServiceEngName() {
		return serviceEngName;
	}
	public void setServiceEngName(String serviceEngName) {
		this.serviceEngName = serviceEngName;
	}
	public String getLiveServer1() {
		return liveServer1;
	}
	public void setLiveServer1(String liveServer1) {
		this.liveServer1 = liveServer1;
	}
	public String getLiveFileName1() {
		return liveFileName1;
	}
	public void setLiveFileName1(String liveFileName1) {
		this.liveFileName1 = liveFileName1;
	}
	public String getLiveServer2() {
		return liveServer2;
	}
	public void setLiveServer2(String liveServer2) {
		this.liveServer2 = liveServer2;
	}
	public String getLiveFileName2() {
		return liveFileName2;
	}
	public void setLiveFileName2(String liveFileName2) {
		this.liveFileName2 = liveFileName2;
	}
	public String getLiveServer3() {
		return liveServer3;
	}
	public void setLiveServer3(String liveServer3) {
		this.liveServer3 = liveServer3;
	}
	public String getLiveFileName3() {
		return liveFileName3;
	}
	public void setLiveFileName3(String liveFileName3) {
		this.liveFileName3 = liveFileName3;
	}
	public String getLiveServer4() {
		return liveServer4;
	}
	public void setLiveServer4(String liveServer4) {
		this.liveServer4 = liveServer4;
	}
	public String getLiveFileName4() {
		return liveFileName4;
	}
	public void setLiveFileName4(String liveFileName4) {
		this.liveFileName4 = liveFileName4;
	}
	public String getLiveServer5() {
		return liveServer5;
	}
	public void setLiveServer5(String liveServer5) {
		this.liveServer5 = liveServer5;
	}
	public String getLiveFileName5() {
		return liveFileName5;
	}
	public void setLiveFileName5(String liveFileName5) {
		this.liveFileName5 = liveFileName5;
	}
	public String getLiveServer6() {
		return liveServer6;
	}
	public void setLiveServer6(String liveServer6) {
		this.liveServer6 = liveServer6;
	}
	public String getLiveFileName6() {
		return liveFileName6;
	}
	public void setLiveFileName6(String liveFileName6) {
		this.liveFileName6 = liveFileName6;
	}
	public String getLiveIp1() {
		return liveIp1;
	}
	public void setLiveIp1(String liveIp1) {
		this.liveIp1 = liveIp1;
	}
	public String getLiveIp2() {
		return liveIp2;
	}
	public void setLiveIp2(String liveIp2) {
		this.liveIp2 = liveIp2;
	}
	public String getLiveIp3() {
		return liveIp3;
	}
	public void setLiveIp3(String liveIp3) {
		this.liveIp3 = liveIp3;
	}
	public String getLiveIp4() {
		return liveIp4;
	}
	public void setLiveIp4(String liveIp4) {
		this.liveIp4 = liveIp4;
	}
	public String getLiveIp5() {
		return liveIp5;
	}
	public void setLiveIp5(String liveIp5) {
		this.liveIp5 = liveIp5;
	}
	public String getLiveIp6() {
		return liveIp6;
	}
	public void setLiveIp6(String liveIp6) {
		this.liveIp6 = liveIp6;
	}
	public String getLivePort() {
		return livePort;
	}
	public void setLivePort(String livePort) {
		this.livePort = livePort;
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
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getPvrYn() {
		return pvrYn;
	}
	public void setPvrYn(String pvrYn) {
		this.pvrYn = pvrYn;
	}
	public String getLocalAreaCode() {
		return localAreaCode;
	}
	public void setLocalAreaCode(String localAreaCode) {
		this.localAreaCode = localAreaCode;
	}
	public String getRunningStatus() {
		return runningStatus;
	}
	public void setRunningStatus(String runningStatus) {
		this.runningStatus = runningStatus;
	}
	public String getChannelNo() {
		return channelNo;
	}
	public void setChannelNo(String channelNo) {
		this.channelNo = channelNo;
	}
	public String getBarkerChannel() {
		return barkerChannel;
	}
	public void setBarkerChannel(String barkerChannel) {
		this.barkerChannel = barkerChannel;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSortNo() {
		return sortNo;
	}
	public void setSortNo(String sortNo) {
		this.sortNo = sortNo;
	}
	public String getFilteringCode() {
		return filteringCode;
	}
	public void setFilteringCode(String filteringCode) {
		this.filteringCode = filteringCode;
	}
	public String getMaxBitrate() {
		return maxBitrate;
	}
	public void setMaxBitrate(String maxBitrate) {
		this.maxBitrate = maxBitrate;
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
	public String getFavorYn() {
		return favorYn;
	}
	public void setFavorYn(String favorYn) {
		this.favorYn = favorYn;
	}
	public String getGenre1() {
		return genre1;
	}
	public void setGenre1(String genre1) {
		this.genre1 = genre1;
	}
	public String getArsNo1() {
		return arsNo1;
	}
	public void setArsNo1(String arsNo1) {
		this.arsNo1 = arsNo1;
	}
	public String getArsNo2() {
		return arsNo2;
	}
	public void setArsNo2(String arsNo2) {
		this.arsNo2 = arsNo2;
	}
	public String getProdDescUrl() {
		return prodDescUrl;
	}
	public void setProdDescUrl(String prodDescUrl) {
		this.prodDescUrl = prodDescUrl;
	}
	public String getVirtualType() {
		return virtualType;
	}
	public void setVirtualType(String virtualType) {
		this.virtualType = virtualType;
	}
	public String getTimeAppYn() {
		return timeAppYn;
	}
	public void setTimeAppYn(String timeAppYn) {
		this.timeAppYn = timeAppYn;
	}
	public String getLiveTimeServer1() {
		return liveTimeServer1;
	}
	public void setLiveTimeServer1(String liveTimeServer1) {
		this.liveTimeServer1 = liveTimeServer1;
	}
	public String getLiveTimeServer2() {
		return liveTimeServer2;
	}
	public void setLiveTimeServer2(String liveTimeServer2) {
		this.liveTimeServer2 = liveTimeServer2;
	}
	public String getLiveTimeServer3() {
		return liveTimeServer3;
	}
	public void setLiveTimeServer3(String liveTimeServer3) {
		this.liveTimeServer3 = liveTimeServer3;
	}
	public String getPooqYn() {
		return pooqYn;
	}
	public void setPooqYn(String pooqYn) {
		this.pooqYn = pooqYn;
	}
	public String getPooqGenreName() {
		return pooqGenreName;
	}
	public void setPooqGenreName(String pooqGenreName) {
		this.pooqGenreName = pooqGenreName;
	}
	public String getContentsId() {
		return contentsId;
	}
	public void setContentsId(String contentsId) {
		this.contentsId = contentsId;
	}
	public String getSaveTime() {
		return saveTime;
	}
	public void setSaveTime(String saveTime) {
		this.saveTime = saveTime;
	}
	public String getStartChunk() {
		return startChunk;
	}
	public void setStartChunk(String startChunk) {
		this.startChunk = startChunk;
	}
	public String getLiveTimeGb() {
		return liveTimeGb;
	}
	public void setLiveTimeGb(String liveTimeGb) {
		this.liveTimeGb = liveTimeGb;
	}
	public String getLiveTimeServer() {
		return liveTimeServer;
	}
	public void setLiveTimeServer(String liveTimeServer) {
		this.liveTimeServer = liveTimeServer;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getIsUhd() {
		return isUhd;
	}
	public void setIsUhd(String isUhd) {
		this.isUhd = isUhd;
	}
	public String getIsFhd() {
		return isFhd;
	}
	public void setIsFhd(String isFhd) {
		this.isFhd = isFhd;
	}
	public String getAdultYn() {
		return adultYn;
	}
	public void setAdultYn(String adultYn) {
		this.adultYn = adultYn;
	}
	public String getRatings() {
		return ratings;
	}
	public void setRatings(String ratings) {
		this.ratings = ratings;
	}
	public String getChnlGrp() {
		return chnlGrp;
	}
	public void setChnlGrp(String chnlGrp) {
		this.chnlGrp = chnlGrp;
	}
	public String getComName() {
		return comName;
	}
	public void setComName(String comName) {
		this.comName = comName;
	}
	public String getChatYn() {
		return chatYn;
	}
	public void setChatYn(String chatYn) {
		this.chatYn = chatYn;
	}
	public String getChatId() {
		return chatId;
	}
	public void setChatId(String chatId) {
		this.chatId = chatId;
	}
	public String getChatName() {
		return chatName;
	}
	public void setChatName(String chatName) {
		this.chatName = chatName;
	}
	public String getHdtvViewGb() {
		return hdtvViewGb;
	}
	public void setHdtvViewGb(String hdtvViewGb) {
		this.hdtvViewGb = hdtvViewGb;
	}
	public String getServiceRefId() {
		return serviceRefId;
	}
	public void setServiceRefId(String serviceRefId) {
		this.serviceRefId = serviceRefId;
	}
	public String getTsFileName1() {
		return tsFileName1;
	}
	public void setTsFileName1(String tsFileName1) {
		this.tsFileName1 = tsFileName1;
	}
	public String getTsFileName2() {
		return tsFileName2;
	}
	public void setTsFileName2(String tsFileName2) {
		this.tsFileName2 = tsFileName2;
	}
	public String getTsFileName3() {
		return tsFileName3;
	}
	public void setTsFileName3(String tsFileName3) {
		this.tsFileName3 = tsFileName3;
	}
	public String getTsLowFileName1() {
		return tsLowFileName1;
	}
	public void setTsLowFileName1(String tsLowFileName1) {
		this.tsLowFileName1 = tsLowFileName1;
	}
	public String getTsLowFileName2() {
		return tsLowFileName2;
	}
	public void setTsLowFileName2(String tsLowFileName2) {
		this.tsLowFileName2 = tsLowFileName2;
	}
	public String getTsLowFileName3() {
		return tsLowFileName3;
	}
	public void setTsLowFileName3(String tsLowFileName3) {
		this.tsLowFileName3 = tsLowFileName3;
	}
	
	public String getmsvcId() {
		return msvcId;
	}
	public void setmSvcId(String msvcId) {
		this.msvcId = msvcId;
	}
	public String getChnlType() {
		return chnlType;
	}
	public void setChnlType(String chnlType) {
		this.chnlType = chnlType;
	}
	public String getM3u8Info() {
		return m3u8Info;
	}
	public void setM3u8Info(String m3u8Info) {
		this.m3u8Info = m3u8Info;
	}
	public String getLiveFileNameL() {
		return liveFileNameL;
	}
	
	public String getLiveServerL() {
		return liveServerL;
	}
	public void setLiveServerL(String liveServerL) {
		this.liveServerL = liveServerL;
	}
	
	public void setLiveFileNameL(String liveFileNameL) {
			this.liveFileNameL = liveFileNameL;
//		if(this.liveFileNameL.isEmpty())
//		{
//			this.liveFileNameL = liveFileNameL;
//		}
//		else
//		{
//			this.liveFileNameL = this.liveFileNameL + ImcsConstants.ARRSEP + liveFileNameL;
//		}
	}
	
	public String getLiveServerN() {
		return liveServerN;
	}
	public void setLiveServerN(String liveServerN) {
		this.liveServerN = liveServerN;
	}
	
	public String getLiveFileNameN() {
		return liveFileNameN;
	}
	public void setLiveFileNameN(String liveFileNameN) {
		this.liveFileNameN = liveFileNameN;
//		if(this.liveFileNameN.isEmpty())
//		{
//			this.liveFileNameN = liveFileNameN;
//		}
//		else
//		{
//			this.liveFileNameN = this.liveFileNameN + ImcsConstants.ARRSEP + liveFileNameN;
//		}
	}
	
	public String getLiveServerC() {
		return liveServerC;
	}
	public void setLiveServerC(String liveServerC) {
		this.liveServerC = liveServerC;
	}
	
	public String getLiveFileNameC() {
		return liveFileNameC;
	}
	public void setLiveFileNameC(String liveFileNameC) {
		this.liveFileNameC = liveFileNameC;
//		if(this.liveFileNameC.isEmpty())
//		{
//			this.liveFileNameC = liveFileNameC;
//		}
//		else
//		{
//			this.liveFileNameC = this.liveFileNameC + ImcsConstants.ARRSEP + liveFileNameC;
//		}
	}
	public String getTsFileNameL() {
		return tsFileNameL;
	}
	public void setTsFileNameL(String tsFileNameL) {
		this.tsFileNameL = tsFileNameL;
	}
	public String getTsFileNameN() {
		return tsFileNameN;
	}
	public void setTsFileNameN(String tsFileNameN) {
		this.tsFileNameN = tsFileNameN;
	}
	public String getTsFileNameC() {
		return tsFileNameC;
	}
	public void setTsFileNameC(String tsFileNameC) {
		this.tsFileNameC = tsFileNameC;
	}



	public String getTsFileName4() {
		return tsFileName4;
	}



	public void setTsFileName4(String tsFileName4) {
		this.tsFileName4 = tsFileName4;
	}



	public String getTsFileName5() {
		return tsFileName5;
	}



	public void setTsFileName5(String tsFileName5) {
		this.tsFileName5 = tsFileName5;
	}



	public String getTsFileName6() {
		return tsFileName6;
	}



	public void setTsFileName6(String tsFileName6) {
		this.tsFileName6 = tsFileName6;
	}
	
	
}
