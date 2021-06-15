package kr.co.wincom.imcs.api.getNSCHRank;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;

import org.apache.commons.lang.StringUtils;

@SuppressWarnings("serial")
public class GetNSCHRankResponseVO extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * getNSCHRank API 전문 칼럼(순서 일치)
	********************************************************************/
	private String serviceId			= "";	// 서비스 ID
	private String liveServer1			= "";	// LIVE 서버 1
	private String liveIp1				= "";	// LIVE 서버 IP1
	private String liveFileName1		= "";	// LIVE 서버 파일명1
	private String liveServer2			= "";
	private String liveIp2				= "";
	private String liveFileName2		= "";
	private String liveServer3			= "";
	private String liveIp3				= "";
	private String liveFileName3		= "";
	private String liveServer4			= "";
	private String liveIp4				= "";
	private String liveFileName4		= "";
	private String liveServer5			= "";
	private String liveIp5				= "";
	private String liveFileName5		= "";
	private String liveServer6			= "";
	private String liveIp6				= "";
	private String liveFileName6		= "";
	private String imgUrl				= "";		// 아이콘 URL
	private String imgFileName			= "";		// 아이콘 이미지 파일명
	private String channelNo			= "";		// 채널번호
	private String vodServer1Type		= "";
	private String vodServer2Type		= "";
	private String vodServer3Type		= "";
    
    private String livePort				= "";
    
    
    @Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append(StringUtils.defaultString(this.getServiceId(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getLiveServer1(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getLiveFileName1(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getLiveServer2(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getLiveFileName2(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getLiveServer3(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getLiveFileName3(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getLiveServer4(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getLiveFileName4(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getLiveServer5(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getLiveFileName5(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getLiveServer6(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getLiveFileName6(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getImgUrl(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getImgFileName(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getChannelNo(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getVodServer1Type(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getVodServer2Type(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getVodServer3Type(), "")).append(ImcsConstants.COLSEP);
		
		return sb.toString();
	}
    
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getLiveServer1() {
		return liveServer1;
	}
	public void setLiveServer1(String liveServer1) {
		this.liveServer1 = liveServer1;
	}
	public String getLiveIp1() {
		return liveIp1;
	}
	public void setLiveIp1(String liveIp1) {
		this.liveIp1 = liveIp1;
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
	public String getLiveIp2() {
		return liveIp2;
	}
	public void setLiveIp2(String liveIp2) {
		this.liveIp2 = liveIp2;
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
	public String getLiveIp3() {
		return liveIp3;
	}
	public void setLiveIp3(String liveIp3) {
		this.liveIp3 = liveIp3;
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
	public String getLiveIp4() {
		return liveIp4;
	}
	public void setLiveIp4(String liveIp4) {
		this.liveIp4 = liveIp4;
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
	public String getLiveIp5() {
		return liveIp5;
	}
	public void setLiveIp5(String liveIp5) {
		this.liveIp5 = liveIp5;
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
	public String getLiveIp6() {
		return liveIp6;
	}
	public void setLiveIp6(String liveIp6) {
		this.liveIp6 = liveIp6;
	}
	public String getLiveFileName6() {
		return liveFileName6;
	}
	public void setLiveFileName6(String liveFileName6) {
		this.liveFileName6 = liveFileName6;
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
	public String getChannelNo() {
		return channelNo;
	}
	public void setChannelNo(String channelNo) {
		this.channelNo = channelNo;
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
	public String getLivePort() {
		return livePort;
	}
	public void setLivePort(String livePort) {
		this.livePort = livePort;
	}
    
}
