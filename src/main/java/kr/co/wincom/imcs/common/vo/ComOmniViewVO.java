package kr.co.wincom.imcs.common.vo;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;


@SuppressWarnings("serial")
public class ComOmniViewVO implements Serializable {

	private String type;
	private String camNo;
	private String albumId;
	private String viewFlag;
	private String chnlId;
	private String serviceId;
	private String m3u8Castis;
	private String m3u8Onnet;
	
	

	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public String getCamNo() {
		return camNo;
	}



	public void setCamNo(String camNo) {
		this.camNo = camNo;
	}



	public String getAlbumId() {
		return albumId;
	}



	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}



	public String getViewFlag() {
		return viewFlag;
	}



	public void setViewFlag(String viewFlag) {
		this.viewFlag = viewFlag;
	}



	public String getChnlId() {
		return chnlId;
	}



	public void setChnlId(String chnlId) {
		this.chnlId = chnlId;
	}



	public String getServiceId() {
		return serviceId;
	}



	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}



	public String getM3u8Castis() {
		return m3u8Castis;
	}



	public void setM3u8Castis(String m3u8Castis) {
		this.m3u8Castis = m3u8Castis;
	}



	public String getM3u8Onnet() {
		return m3u8Onnet;
	}



	public void setM3u8Onnet(String m3u8Onnet) {
		this.m3u8Onnet = m3u8Onnet;
	}



	public String toOmniViewString(){
		StringBuffer sb = new StringBuffer();
		
		sb.append(StringUtil.nullToSpace(this.type)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.camNo)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.albumId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.viewFlag)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.chnlId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.serviceId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.m3u8Castis)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.m3u8Onnet)).append(ImcsConstants.COLSEP);
			
		return sb.toString();
	}
	    
}
