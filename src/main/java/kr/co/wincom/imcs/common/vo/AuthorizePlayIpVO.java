package kr.co.wincom.imcs.common.vo;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;

@SuppressWarnings("serial")
public class AuthorizePlayIpVO  implements Serializable {
	private String nodeGroup            = "";
	private String tmpNodeGroup            = "";
	private String serverPlayIp1        = "";
	private String serverPlayIp2        = "";
	private String serverPlayIp3        = "";
	private String serverType1          = "";
	private String serverType2          = "";
	private String serverType3          = "";
	private String serverIpv6Node1      = "";
	private String serverIpv6Node2      = "";
	private String serverIpv6Node3      = "";
	
	private String nodeCd		= "";
    private String baseCondi	= "";
    
    private String saId	= "";
    private String stbMac	= "";
	
    private String m3u8Type             = "";
    private String castisM3u8			= "";		// 캐스트이즈 m3u8 파일명
    private String onnuriM3u8			= "";		// 온누리넷 m3u8 파일명	
    
    private String vodFileName			= "";
    private String contentFileSize		= "";
	
    private String bitRate		= "";
    private String replay4dYn	= "";
    
    // 2019.10.11 - IPv6듀얼스택 지원 변수 추가
    private String serverIpv6PlayIp1        = "";
    private String serverIpv6PlayIp2        = "";
    private String serverIpv6PlayIp3        = "";    
    private String serverIpv6Port	        = "";
    private String serverIpv6Type1      	= "";
    private String serverIpv6Type2     	    = "";
    private String serverIpv6Type3        	= "";
    
    @Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
			
		sb.append("FILE").append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.m3u8Type, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.castisM3u8, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.onnuriM3u8, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.serverPlayIp1, "")).append(ImcsConstants.ARRSEP);
		sb.append(StringUtil.replaceNull(this.serverPlayIp2, "")).append(ImcsConstants.ARRSEP);
		sb.append(StringUtil.replaceNull(this.serverPlayIp3, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.serverType1, "")).append(ImcsConstants.ARRSEP);
		sb.append(StringUtil.replaceNull(this.serverType2, "")).append(ImcsConstants.ARRSEP);
		sb.append(StringUtil.replaceNull(this.serverType3, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.serverIpv6Node1, "")).append(ImcsConstants.ARRSEP);
		sb.append(StringUtil.replaceNull(this.serverIpv6Node2, "")).append(ImcsConstants.ARRSEP);
		sb.append(StringUtil.replaceNull(this.serverIpv6Node3, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.serverIpv6PlayIp1, "")).append(ImcsConstants.ARRSEP);
		sb.append(StringUtil.replaceNull(this.serverIpv6PlayIp2, "")).append(ImcsConstants.ARRSEP);
		sb.append(StringUtil.replaceNull(this.serverIpv6PlayIp3, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.serverIpv6Type1, "")).append(ImcsConstants.ARRSEP);
		sb.append(StringUtil.replaceNull(this.serverIpv6Type2, "")).append(ImcsConstants.ARRSEP);
		sb.append(StringUtil.replaceNull(this.serverIpv6Type3, "")).append(ImcsConstants.COLSEP);
		
		return sb.toString();
	}
    
	public String getNodeGroup() {
		return nodeGroup;
	}
	public void setNodeGroup(String nodeGroup) {
		this.nodeGroup = nodeGroup;
	}	
	public String getTmpNodeGroup() {
		return tmpNodeGroup;
	}
	public void setTmpNodeGroup(String tmpNodeGroup) {
		this.tmpNodeGroup = tmpNodeGroup;
	}
	public String getServerPlayIp1() {
		return serverPlayIp1;
	}
	public void setServerPlayIp1(String serverPlayIp1) {
		this.serverPlayIp1 = serverPlayIp1;
	}
	public String getServerPlayIp2() {
		return serverPlayIp2;
	}
	public void setServerPlayIp2(String serverPlayIp2) {
		this.serverPlayIp2 = serverPlayIp2;
	}
	public String getServerPlayIp3() {
		return serverPlayIp3;
	}
	public void setServerPlayIp3(String serverPlayIp3) {
		this.serverPlayIp3 = serverPlayIp3;
	}
	public String getServerType1() {
		return serverType1;
	}
	public void setServerType1(String serverType1) {
		this.serverType1 = serverType1;
	}
	public String getServerType2() {
		return serverType2;
	}
	public void setServerType2(String serverType2) {
		this.serverType2 = serverType2;
	}
	public String getServerType3() {
		return serverType3;
	}
	public void setServerType3(String serverType3) {
		this.serverType3 = serverType3;
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
	public String getNodeCd() {
		return nodeCd;
	}
	public void setNodeCd(String nodeCd) {
		this.nodeCd = nodeCd;
	}
	public String getBaseCondi() {
		return baseCondi;
	}
	public void setBaseCondi(String baseCondi) {
		this.baseCondi = baseCondi;
	}
	public String getSaId() {
		return saId;
	}
	public void setSaId(String saId) {
		this.saId = saId;
	}
	public String getStbMac() {
		return stbMac;
	}
	public void setStbMac(String stbMac) {
		this.stbMac = stbMac;
	}
	
	public String getM3u8Type() {
		return m3u8Type;
	}

	public void setM3u8Type(String m3u8Type) {
		this.m3u8Type = m3u8Type;
	}

	public String getCastisM3u8() {
		return castisM3u8;
	}

	public void setCastisM3u8(String castisM3u8) {
		this.castisM3u8 = castisM3u8;
	}

	public String getOnnuriM3u8() {
		return onnuriM3u8;
	}

	public void setOnnuriM3u8(String onnuriM3u8) {
		this.onnuriM3u8 = onnuriM3u8;
	}

	public String getVodFileName() {
		return vodFileName;
	}

	public void setVodFileName(String vodFileName) {
		this.vodFileName = vodFileName;
	}
	
	public String getContentFileSize() {
		return contentFileSize;
	}

	public void setContentFileSize(String contentFileSize) {
		this.contentFileSize = contentFileSize;
	}

	public String getBitRate() {
		return bitRate;
	}

	public void setBitRate(String bitRate) {
		this.bitRate = bitRate;
	}

	public String getReplay4dYn() {
		return replay4dYn;
	}

	public void setReplay4dYn(String replay4dYn) {
		this.replay4dYn = replay4dYn;
	}

	public String getServerIpv6PlayIp1() {
		return serverIpv6PlayIp1;
	}

	public void setServerIpv6PlayIp1(String serverIpv6PlayIp1) {
		this.serverIpv6PlayIp1 = serverIpv6PlayIp1;
	}

	public String getServerIpv6PlayIp2() {
		return serverIpv6PlayIp2;
	}

	public void setServerIpv6PlayIp2(String serverIpv6PlayIp2) {
		this.serverIpv6PlayIp2 = serverIpv6PlayIp2;
	}

	public String getServerIpv6PlayIp3() {
		return serverIpv6PlayIp3;
	}

	public void setServerIpv6PlayIp3(String serverIpv6PlayIp3) {
		this.serverIpv6PlayIp3 = serverIpv6PlayIp3;
	}

	public String getServerIpv6Port() {
		return serverIpv6Port;
	}

	public void setServerIpv6Port(String serverIpv6Port) {
		this.serverIpv6Port = serverIpv6Port;
	}

	public String getServerIpv6Type1() {
		return serverIpv6Type1;
	}

	public void setServerIpv6Type1(String serverIpv6Type1) {
		this.serverIpv6Type1 = serverIpv6Type1;
	}

	public String getServerIpv6Type2() {
		return serverIpv6Type2;
	}

	public void setServerIpv6Type2(String serverIpv6Type2) {
		this.serverIpv6Type2 = serverIpv6Type2;
	}

	public String getServerIpv6Type3() {
		return serverIpv6Type3;
	}

	public void setServerIpv6Type3(String serverIpv6Type3) {
		this.serverIpv6Type3 = serverIpv6Type3;
	}
	
}
