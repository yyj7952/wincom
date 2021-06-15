package kr.co.wincom.imcs.common.vo;

public class StillImageVO {
	private String imgUrl		= "";
	private String imgFileName	= "";
	private String imgFlag		= "";
	private String timeInfo		= "";
	private String posterType	= "";
	
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
	public String getImgFlag() {
		return imgFlag;
	}
	public void setImgFlag(String imgFlag) {
		this.imgFlag = imgFlag;
	}
	public String getTimeInfo() {
		return timeInfo;
	}
	public void setTimeInfo(String timeInfo) {
		this.timeInfo = timeInfo;
	}
	public String getPosterType() {
		return posterType;
	}
	public void setPosterType(String posterType) {
		this.posterType = posterType;
	}
}
