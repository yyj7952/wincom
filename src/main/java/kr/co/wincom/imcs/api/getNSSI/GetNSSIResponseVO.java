package kr.co.wincom.imcs.api.getNSSI;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;


@SuppressWarnings("serial")
public class GetNSSIResponseVO implements Serializable {
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
        
    /********************************************************************
	 * 추가 사용되는 속성값  start
	 ********************************************************************/
    private String liveTimeGb 			= "";
    private String liveTimeServer 		= "";
    private String contentsId 			= "";
    
    
    @Override
    public String toString() {
		StringBuffer sb = new StringBuffer();
		// 몇몇 row의 adult_yn이 " "로 나타나는 경우가 있는데, 모르겠음
			
		sb.append(StringUtil.nullToSpace(this.getServiceId())).append(ImcsConstants.COLSEP);
		sb.append(this.getServiceName()).append(ImcsConstants.COLSEP);
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
	public String getContentsId() {
		return contentsId;
	}
	public void setContentsId(String contentsId) {
		this.contentsId = contentsId;
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
}
