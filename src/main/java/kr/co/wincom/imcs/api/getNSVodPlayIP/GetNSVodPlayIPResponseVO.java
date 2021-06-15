package kr.co.wincom.imcs.api.getNSVodPlayIP;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;


@SuppressWarnings("serial")
public class GetNSVodPlayIPResponseVO extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * getNSVodPlayIP API 전문 칼럼(순서 일치)
	********************************************************************/
	private String resultType		  = ""; //결과값 구분
	private String localPlayIp        = "";	//메인 CDN NODE IP
	private String nearPlayIp	      = "";	//인접 CDN NODE IP
	private String centerPlayIp       = "";	//중앙 CDN NODE IP
	private String localServerType    = "";	//메인 서버TYPE
	private String nearServerType	  = "";	//인접 서버TYPE
	private String centerServerType   = "";	//중앙 서버TYPE
	private String ipv6CdnType1		  = "";
	private String ipv6CdnType2		  = "";
	private String ipv6CdnType3		  = "";
	
	/********************************************************************
	 * getNSVodPlayIP API 기타 칼럼(순서 일치)
	********************************************************************/
	private String fileSvcNode;
	private String chkBaseGb1;
	private String chkBaseGb2;
	private String chkBaseGb3;
	
	//2019.10.11 - IPv6듀얼스택 지원 변수 추가
	private String vodIpv6Server1		  = "";		// VOD IPv6 서버 1
	private String vodIpv6Server2		  = "";		// VOD IPv6 서버 2
	private String vodIpv6Server3		  = "";		// VOD IPv6 서버 3
	private String vodIpv6Server1Type	  = "";		// VOD IPv6 서버 1 타입 (1:castis, 2:주인넷)
	private String vodIpv6Server2Type	  = "";		// VOD IPv6 서버 2 타입 (1:castis, 2:주인넷)
	private String vodIpv6Server3Type	  = "";		// VOD IPv6 서버 3 타입 (1:castis, 2:주인넷)
	
    @Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
			sb.append(StringUtils.defaultString(this.getResultType(), "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtils.defaultString(this.getLocalPlayIp(), "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtils.defaultString(this.getNearPlayIp(), "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtils.defaultString(this.getCenterPlayIp(), "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtils.defaultString(this.getLocalServerType(), "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtils.defaultString(this.getNearServerType(), "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtils.defaultString(this.getCenterServerType(), "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtils.defaultString(this.getIpv6CdnType1(), "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtils.defaultString(this.getIpv6CdnType2(), "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtils.defaultString(this.getIpv6CdnType3(), "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtils.defaultString(this.getVodIpv6Server1(), "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtils.defaultString(this.getVodIpv6Server2(), "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtils.defaultString(this.getVodIpv6Server3(), "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtils.defaultString(this.getVodIpv6Server1Type(), "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtils.defaultString(this.getVodIpv6Server2Type(), "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtils.defaultString(this.getVodIpv6Server3Type(), "")).append(ImcsConstants.COLSEP);

		return sb.toString();
	}

	public String getResultType() {
		return resultType;
	}

	public void setResultType(String resultType) {
		this.resultType = resultType;
	}

	public String getLocalPlayIp() {
		return localPlayIp;
	}

	public void setLocalPlayIp(String localPlayIp) {
		this.localPlayIp = localPlayIp;
	}

	public String getNearPlayIp() {
		return nearPlayIp;
	}

	public void setNearPlayIp(String nearPlayIp) {
		this.nearPlayIp = nearPlayIp;
	}

	public String getCenterPlayIp() {
		return centerPlayIp;
	}

	public void setCenterPlayIp(String centerPlayIp) {
		this.centerPlayIp = centerPlayIp;
	}

	public String getLocalServerType() {
		return localServerType;
	}

	public void setLocalServerType(String localServerType) {
		this.localServerType = localServerType;
	}

	public String getNearServerType() {
		return nearServerType;
	}

	public void setNearServerType(String nearServerType) {
		this.nearServerType = nearServerType;
	}

	public String getCenterServerType() {
		return centerServerType;
	}

	public void setCenterServerType(String centerServerType) {
		this.centerServerType = centerServerType;
	}

	public String getFileSvcNode() {
		return fileSvcNode;
	}

	public void setFileSvcNode(String fileSvcNode) {
		this.fileSvcNode = fileSvcNode;
	}

	public String getChkBaseGb1() {
		return chkBaseGb1;
	}

	public void setChkBaseGb1(String chkBaseGb1) {
		this.chkBaseGb1 = chkBaseGb1;
	}

	public String getChkBaseGb2() {
		return chkBaseGb2;
	}

	public void setChkBaseGb2(String chkBaseGb2) {
		this.chkBaseGb2 = chkBaseGb2;
	}

	public String getChkBaseGb3() {
		return chkBaseGb3;
	}

	public void setChkBaseGb3(String chkBaseGb3) {
		this.chkBaseGb3 = chkBaseGb3;
	}

	public String getIpv6CdnType1() {
		return ipv6CdnType1;
	}

	public void setIpv6CdnType1(String ipv6CdnType1) {
		this.ipv6CdnType1 = ipv6CdnType1;
	}

	public String getIpv6CdnType2() {
		return ipv6CdnType2;
	}

	public void setIpv6CdnType2(String ipv6CdnType2) {
		this.ipv6CdnType2 = ipv6CdnType2;
	}

	public String getIpv6CdnType3() {
		return ipv6CdnType3;
	}

	public void setIpv6CdnType3(String ipv6CdnType3) {
		this.ipv6CdnType3 = ipv6CdnType3;
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
