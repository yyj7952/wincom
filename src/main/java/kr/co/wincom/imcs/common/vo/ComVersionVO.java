package kr.co.wincom.imcs.common.vo;

import java.io.Serializable;


@SuppressWarnings("serial")
public class ComVersionVO implements Serializable {
	private int downloadCnt		= 0;
    private String versionType	= "";
    private String version		= "";
    private String catId		= "";
    private String catName		= "";
    
	public int getDownloadCnt() {
		return downloadCnt;
	}
	public void setDownloadCnt(int downloadCnt) {
		this.downloadCnt = downloadCnt;
	}
	public String getVersionType() {
		return versionType;
	}
	public void setVersionType(String versionType) {
		this.versionType = versionType;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getCatId() {
		return catId;
	}
	public void setCatId(String catId) {
		this.catId = catId;
	}
	public String getCatName() {
		return catName;
	}
	public void setCatName(String catName) {
		this.catName = catName;
	}

}
