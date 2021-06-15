package kr.co.wincom.imcs.api.getNSPropertyList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;

@SuppressWarnings("serial")
public class GetNSPropertyListSubListVO extends NoSqlLoggingVO implements Cloneable, Serializable {
	
	
	private String propGrpId			= "";
	private String propGrpNm			= "";
	private String propGrpType			= "";
	private String logoUrl				= "";
	private String logoFileName1		= "";
	private String logoFileName2		= "";
	private String tag					= "";
	private List<GetNSPropertyListSubPropertyVO> property;

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	public String getPropGrpId() {
		return propGrpId;
	}
	public void setPropGrpId(String propGrpId) {
		this.propGrpId = propGrpId;
	}
	public String getPropGrpNm() {
		return propGrpNm;
	}
	public void setPropGrpNm(String propGrpNm) {
		this.propGrpNm = propGrpNm;
	}
	public String getPropGrpType() {
		return propGrpType;
	}
	public void setPropGrpType(String propGrpType) {
		this.propGrpType = propGrpType;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public List<GetNSPropertyListSubPropertyVO> getProperty() {
		return property;
	}
	public void setProperty(List<GetNSPropertyListSubPropertyVO> property) {
		this.property = property;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getLogoFileName1() {
		return logoFileName1;
	}

	public void setLogoFileName1(String logoFileName1) {
		this.logoFileName1 = logoFileName1;
	}

	public String getLogoFileName2() {
		return logoFileName2;
	}

	public void setLogoFileName2(String logoFileName2) {
		this.logoFileName2 = logoFileName2;
	}
	
}
