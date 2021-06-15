package kr.co.wincom.imcs.api.getNSPropertyList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;

@SuppressWarnings("serial")
public class GetNSPropertyListSubPropertyVO extends NoSqlLoggingVO implements Cloneable, Serializable {

	private String propId				= "";
	private String propNm				= "";
	private String propTag				= "";
	private String viewingFlag			= "";
	private String controlFlag			= "";
	private String info1				= "";
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
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


	public String getPropTag() {
		return propTag;
	}


	public void setPropTag(String propTag) {
		this.propTag = propTag;
	}


	public String getInfo1() {
		return info1;
	}


	public void setInfo1(String info1) {
		this.info1 = info1;
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
	
}
