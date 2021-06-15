package kr.co.wincom.imcs.api.getNSNodeList;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;


@SuppressWarnings("serial")
public class GetNSNodeListResponseVO extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * getNSNodeList API 전문 칼럼(순서 일치)
	********************************************************************/
	private String resultType		  = ""; //결과값 구분f(CHNL / VOD)
	private String svcNode            = ""; //서비스 노드 구분
	private String baseGb             = ""; //기지국 구분
	private String baseCd             = ""; //기지국 코드
	private String loadbalancingYn    = ""; //로드밸런싱 여부
	private String loadbalancingSort  = ""; //로드밸런싱 순번
	private String localPlayIp        = ""; //메인 CDN NODE IP
	private String nearPlayIp         = ""; //인접 CDN NODE IP
	private String centerPlayIp       = ""; //중앙 CDN NODE IP
	private String localServerType    = ""; //메인 서버TYPE
	private String nearServerType     = ""; //인접 서버TYPE
	private String centerServerType   = ""; //중앙 서버TYPE
	private String ipv6CdnType1   = ""; //메인 CDN Prefix Type
	private String ipv6CdnType2   = ""; //인접 CDN Prefix Type
	private String ipv6CdnType3   = ""; //중앙 CDN Prefix Type
	private String ipv6LocalPlayIp        = ""; //메인 Ipv6 CDN NODE IP
	private String ipv6NearPlayIp         = ""; //인접 Ipv6 CDN NODE IP
	private String ipv6CenterPlayIp       = ""; //중앙 Ipv6 CDN NODE IP
	private String ipv6LocalServerType    = ""; //메인 Ipv6 서버TYPE
	private String ipv6NearServerType     = ""; //인접 Ipv6 서버TYPE
	private String ipv6CenterServerType   = ""; //중앙 Ipv6 서버TYPE

    @Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(StringUtils.defaultString(this.getResultType(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getSvcNode(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getBaseGb(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getBaseCd(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getLoadbalancingYn(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getLoadbalancingSort(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getLocalPlayIp(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getNearPlayIp(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getCenterPlayIp(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getLocalServerType(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getNearServerType(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getCenterServerType(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString("", "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getIpv6CdnType1(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getIpv6CdnType2(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getIpv6CdnType3(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getIpv6LocalPlayIp(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getIpv6NearPlayIp(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getIpv6CenterPlayIp(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getIpv6LocalServerType(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getIpv6NearServerType(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getIpv6CenterServerType(), "")).append(ImcsConstants.COLSEP);
		
		return sb.toString();
	}
    
    public void dataParcing(String[] arrRowResult)
    {
    	for(int j = 0; j < arrRowResult.length; j++)
		{
			switch(j)
			{
				case 0:
					this.setResultType(arrRowResult[j]);
					break;
				case 1:
					this.setSvcNode(arrRowResult[j]);
					break;
				case 2:
					this.setBaseGb(arrRowResult[j]);
					break;
				case 3:
					this.setBaseCd(arrRowResult[j]);
					break;
				case 4:
					this.setLoadbalancingYn(arrRowResult[j]);
					break;
				case 5:
					this.setLoadbalancingSort(arrRowResult[j]);
					break;
				case 6:
					this.setLocalPlayIp(arrRowResult[j]);
					break;
				case 7:
					this.setNearPlayIp(arrRowResult[j]);
					break;
				case 8:
					this.setCenterPlayIp(arrRowResult[j]);
					break;
				case 9:
					this.setLocalServerType(arrRowResult[j]);
					break;
				case 10:
					this.setNearServerType(arrRowResult[j]);
					break;
				case 11:
					this.setCenterServerType(arrRowResult[j]);
					break;
				case 13:
					this.setIpv6CdnType1(arrRowResult[j]);
					break;
				case 14:
					this.setIpv6CdnType2(arrRowResult[j]);
					break;
				case 15:
					this.setIpv6CdnType3(arrRowResult[j]);
					break;
				case 16:
					this.setIpv6LocalPlayIp(arrRowResult[j]);
					break;
				case 17:
					this.setIpv6NearPlayIp(arrRowResult[j]);
					break;
				case 18:
					this.setIpv6CenterPlayIp(arrRowResult[j]);
					break;
				case 19:
					this.setIpv6LocalServerType(arrRowResult[j]);
					break;
				case 20:
					this.setIpv6NearServerType(arrRowResult[j]);
					break;
				case 21:
					this.setIpv6CenterServerType(arrRowResult[j]);
					break;
				default:
					break;
			}
		}
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

	public String getIpv6LocalPlayIp() {
		return ipv6LocalPlayIp;
	}

	public void setIpv6LocalPlayIp(String ipv6LocalPlayIp) {
		this.ipv6LocalPlayIp = ipv6LocalPlayIp;
	}

	public String getIpv6NearPlayIp() {
		return ipv6NearPlayIp;
	}

	public void setIpv6NearPlayIp(String ipv6NearPlayIp) {
		this.ipv6NearPlayIp = ipv6NearPlayIp;
	}

	public String getIpv6CenterPlayIp() {
		return ipv6CenterPlayIp;
	}

	public void setIpv6CenterPlayIp(String ipv6CenterPlayIp) {
		this.ipv6CenterPlayIp = ipv6CenterPlayIp;
	}

	public String getIpv6LocalServerType() {
		return ipv6LocalServerType;
	}

	public void setIpv6LocalServerType(String ipv6LocalServerType) {
		this.ipv6LocalServerType = ipv6LocalServerType;
	}

	public String getIpv6NearServerType() {
		return ipv6NearServerType;
	}

	public void setIpv6NearServerType(String ipv6NearServerType) {
		this.ipv6NearServerType = ipv6NearServerType;
	}

	public String getIpv6CenterServerType() {
		return ipv6CenterServerType;
	}

	public void setIpv6CenterServerType(String ipv6CenterServerType) {
		this.ipv6CenterServerType = ipv6CenterServerType;
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
