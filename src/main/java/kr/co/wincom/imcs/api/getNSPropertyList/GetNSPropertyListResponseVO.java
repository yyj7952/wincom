package kr.co.wincom.imcs.api.getNSPropertyList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;

@SuppressWarnings("serial")
public class GetNSPropertyListResponseVO extends NoSqlLoggingVO implements Serializable {
	
	
	private String propGrpId			= "";
	private String propGrpNm			= "";
	private String propGrpType			= "";
	private String logoFileName1		= "";
	private String logoFileName2		= "";
	private String tag					= "";
	
	private String propId				= "";
	private String propNm				= "";
	private String logoUrl				= "";
	private String propTag				= "";
	private String viewingFlag			= "";
	private String controlFlag			= "";
	private String info1				= "";
	
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
	public String getPropId() {
		return propId;
	}
	public void setPropId(String propId) {
		this.propId = propId;
	}
	public String getPropNm() {
		return propNm;
	}
	public void setPropNm(String propNm) {
		this.propNm = propNm;
	}
	public String getLogoUrl() {
		return logoUrl;
	}
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}
	public String getPropTag() {
		return propTag;
	}
	public void setPropTag(String propTag) {
		this.propTag = propTag;
	}
	public String getViewingFlag() {
		return viewingFlag;
	}
	public void setViewingFlag(String viewingFlag) {
		this.viewingFlag = viewingFlag;
	}
	public String getControlFlag() {
		return controlFlag;
	}
	public void setControlFlag(String controlFlag) {
		this.controlFlag = controlFlag;
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
	public String getInfo1() {
		return info1;
	}
	public void setInfo1(String info1) {
		this.info1 = info1;
	}
}
