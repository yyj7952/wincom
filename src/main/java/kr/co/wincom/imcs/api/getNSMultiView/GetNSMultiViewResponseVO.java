package kr.co.wincom.imcs.api.getNSMultiView;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;


@SuppressWarnings("serial")
public class GetNSMultiViewResponseVO extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * getNSMultiView API 전문 칼럼(순서 일치)
	********************************************************************/
    private String multiviewId = "";
    private String multiviewName = "";
    private String serviceId = "";
    private String serviceName = "";
    private String serviceEngName = "";
    private String imgUrl = "";
    private String imgFileName = "";
    private String channelNo = "";
    private String description= "";
    private String thmUrl = "";
    private String thmFile = "";
    private String ratings = "";
    
    /********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
    private String pooqYn = "";
    
    @Override
	public String toString() {
    	StringBuffer sb = new StringBuffer();
		
		sb.append(StringUtil.replaceNull(this.multiviewId, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.multiviewName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.serviceId, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.serviceName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.serviceEngName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.imgUrl, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.imgFileName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.channelNo, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.description, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.thmUrl, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.thmFile, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.ratings, "")).append(ImcsConstants.COLSEP);
		
		return sb.toString();
	}
		
	public String getMultiviewId() {
		return multiviewId;
	}
	public void setMultiviewId(String multiviewId) {
		this.multiviewId = multiviewId;
	}
	public String getMultiviewName() {
		return multiviewName;
	}
	public void setMultiviewName(String multiviewName) {
		this.multiviewName = multiviewName;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPooqYn() {
		return pooqYn;
	}
	public void setPooqYn(String pooqYn) {
		this.pooqYn = pooqYn;
	}
	public String getThmUrl() {
		return thmUrl;
	}
	public void setThmUrl(String thmUrl) {
		this.thmUrl = thmUrl;
	}
	public String getRatings() {
		return ratings;
	}
	public void setRatings(String ratings) {
		this.ratings = ratings;
	}
	public String getThmFile() {
		return thmFile;
	}
	public void setThmFile(String thmFile) {
		this.thmFile = thmFile;
	}
    
}
