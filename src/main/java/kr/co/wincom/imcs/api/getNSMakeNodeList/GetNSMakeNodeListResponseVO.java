package kr.co.wincom.imcs.api.getNSMakeNodeList;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;


@SuppressWarnings("serial")
public class GetNSMakeNodeListResponseVO extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * getNSNodeList API 전문 칼럼(순서 일치)
	********************************************************************/
	private String resultType		  = ""; //결과값 구분f(CHNL / VOD)
	private String svcNode            = ""; //서비스 노드 구분
	private String baseGb             = ""; //기지국 구분
	private String baseCd             = ""; //기지국 코드
	private String loadbalancingYn    = ""; //로드밸런싱 여부
	private String loadbalancingSort  = ""; //로드밸런싱 순번
//	private String localPlayIp        = ""; //메인 CDN NODE IP
//	private String nearPlayIp         = ""; //인접 CDN NODE IP
//	private String centerPlayIp       = ""; //중앙 CDN NODE IP
//	private String localServerType    = ""; //메인 서버TYPE
//	private String nearServerType     = ""; //인접 서버TYPE
//	private String centerServerType   = ""; //중앙 서버TYPE
	
	private String stbPlayIp1;  //메인 CDN NODE IP
	private String stbPlayIp2;  //인접 CDN NODE IP
	private String stbPlayIp3; //중앙 CDN NODE IP
	private String cdnLocalTyp; //메인 서버TYPE
	private String cdnNearTyp; //인접 서버TYPE
	private String cdnCenterTyp; //중앙 서버TYPE
	
	private String fourDReplayYn = "";
	private String ipv6Flag1  = "";
	private String ipv6Flag2  = "";
	private String ipv6Flag3  = "";
	
	// 2019.09.09 - IPv6듀얼스택 지원 : 변수 선언
	private String ipv6PlayIp1 = "";
	private String ipv6PlayIp2 = "";
	private String ipv6PlayIp3 = "";
	private String ipv6CdnType1 = "";
	private String ipv6CdnType2 = "";
	private String ipv6CdnType3 = "";

    @Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(StringUtils.defaultString(this.getResultType(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getSvcNode(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getBaseGb(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getBaseCd(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getLoadbalancingYn(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getLoadbalancingSort(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getStbPlayIp1(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getStbPlayIp2(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getStbPlayIp3(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getCdnLocalTyp(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getCdnNearTyp(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getCdnCenterTyp(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getFourDReplayYn(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getIpv6Flag1(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getIpv6Flag2(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getIpv6Flag3(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getIpv6PlayIp1(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getIpv6PlayIp2(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getIpv6PlayIp3(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getIpv6CdnType1(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getIpv6CdnType2(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getIpv6CdnType3(), "")).append(ImcsConstants.COLSEP);
		
		return sb.toString();
	}

	public String getResultType() {
		return resultType;
	}

	public void setResultType(String resultType) {
		this.resultType = resultType;
	}

	public String getSvcNode() {
		return svcNode;
	}

	public void setSvcNode(String svcNode) {
		this.svcNode = svcNode;
	}

	public String getBaseGb() {
		return baseGb;
	}

	public void setBaseGb(String baseGb) {
		this.baseGb = baseGb;
	}

	public String getBaseCd() {
		return baseCd;
	}

	public void setBaseCd(String baseCd) {
		this.baseCd = baseCd;
	}

	public String getLoadbalancingYn() {
		return loadbalancingYn;
	}

	public void setLoadbalancingYn(String loadbalancingYn) {
		this.loadbalancingYn = loadbalancingYn;
	}

	public String getLoadbalancingSort() {
		return loadbalancingSort;
	}

	public void setLoadbalancingSort(String loadbalancingSort) {
		this.loadbalancingSort = loadbalancingSort;
	}

	public String getStbPlayIp1() {
		return stbPlayIp1;
	}

	public void setStbPlayIp1(String stbPlayIp1) {
		this.stbPlayIp1 = stbPlayIp1;
	}

	public String getStbPlayIp2() {
		return stbPlayIp2;
	}

	public void setStbPlayIp2(String stbPlayIp2) {
		this.stbPlayIp2 = stbPlayIp2;
	}

	public String getStbPlayIp3() {
		return stbPlayIp3;
	}

	public void setStbPlayIp3(String stbPlayIp3) {
		this.stbPlayIp3 = stbPlayIp3;
	}

	public String getCdnLocalTyp() {
		return cdnLocalTyp;
	}

	public void setCdnLocalTyp(String cdnLocalTyp) {
		this.cdnLocalTyp = cdnLocalTyp;
	}

	public String getCdnNearTyp() {
		return cdnNearTyp;
	}

	public void setCdnNearTyp(String cdnNearTyp) {
		this.cdnNearTyp = cdnNearTyp;
	}

	public String getCdnCenterTyp() {
		return cdnCenterTyp;
	}

	public void setCdnCenterTyp(String cdnCenterTyp) {
		this.cdnCenterTyp = cdnCenterTyp;
	}

	public String getFourDReplayYn() {
		return fourDReplayYn;
	}

	public void setFourDReplayYn(String fourDReplayYn) {
		this.fourDReplayYn = fourDReplayYn;
	}

	public String getIpv6Flag1() {
		return ipv6Flag1;
	}

	public void setIpv6Flag1(String ipv6Flag1) {
		this.ipv6Flag1 = ipv6Flag1;
	}

	public String getIpv6Flag2() {
		return ipv6Flag2;
	}

	public void setIpv6Flag2(String ipv6Flag2) {
		this.ipv6Flag2 = ipv6Flag2;
	}

	public String getIpv6Flag3() {
		return ipv6Flag3;
	}

	public void setIpv6Flag3(String ipv6Flag3) {
		this.ipv6Flag3 = ipv6Flag3;
	}

	public String getIpv6PlayIp1() {
		return ipv6PlayIp1;
	}

	public void setIpv6PlayIp1(String ipv6PlayIp1) {
		this.ipv6PlayIp1 = ipv6PlayIp1;
	}

	public String getIpv6PlayIp2() {
		return ipv6PlayIp2;
	}

	public void setIpv6PlayIp2(String ipv6PlayIp2) {
		this.ipv6PlayIp2 = ipv6PlayIp2;
	}

	public String getIpv6PlayIp3() {
		return ipv6PlayIp3;
	}

	public void setIpv6PlayIp3(String ipv6PlayIp3) {
		this.ipv6PlayIp3 = ipv6PlayIp3;
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

}
