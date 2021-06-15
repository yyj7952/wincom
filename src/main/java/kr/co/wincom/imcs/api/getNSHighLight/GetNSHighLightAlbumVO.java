package kr.co.wincom.imcs.api.getNSHighLight;

import java.io.Serializable;

@SuppressWarnings("serial")
public class GetNSHighLightAlbumVO implements Serializable {

	private String albumNm 	 = "";	
	private String onairDate = "";
	private String sysnopsis = "";
	private String runTime 	 = "";

	public String getAlbumNm() {
		return albumNm;
	}
	public void setAlbumNm(String albumNm) {
		this.albumNm = albumNm;
	}
	public String getOnairDate() {
		return onairDate;
	}
	public void setOnairDate(String onairDate) {
		this.onairDate = onairDate;
	}
	public String getSysnopsis() {
		return sysnopsis;
	}
	public void setSysnopsis(String sysnopsis) {
		this.sysnopsis = sysnopsis;
	}
	public String getRunTime() {
		return runTime;
	}
	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}

}
