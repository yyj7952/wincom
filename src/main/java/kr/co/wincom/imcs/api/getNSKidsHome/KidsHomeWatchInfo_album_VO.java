package kr.co.wincom.imcs.api.getNSKidsHome;

import java.io.Serializable;

public class KidsHomeWatchInfo_album_VO implements Serializable
{
	private static final long serialVersionUID = 7634322029602439647L;
	
	private String albumNm = "";
	private String seriesDesc = "";
	private String runtime = "";
	private int iRuntime = 0;
	private String posterH = "";
	private String posterV = "";
	private String posterR = "";
	private String stillFile = "";
	
	public String getAlbumNm() {
		return albumNm;
	}
	public void setAlbumNm(String albumNm) {
		this.albumNm = albumNm;
	}
	public String getSeriesDesc() {
		return seriesDesc;
	}
	public void setSeriesDesc(String seriesDesc) {
		this.seriesDesc = seriesDesc;
	}
	public String getRuntime() {
		return runtime;
	}
	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}
	public int getiRuntime() {
		return iRuntime;
	}
	public void setiRuntime(int iRuntime) {
		this.iRuntime = iRuntime;
	}
	public String getPosterH() {
		return posterH;
	}
	public void setPosterH(String posterH) {
		this.posterH = posterH;
	}
	public String getPosterV() {
		return posterV;
	}
	public void setPosterV(String posterV) {
		this.posterV = posterV;
	}
	public String getPosterR() {
		return posterR;
	}
	public void setPosterR(String posterR) {
		this.posterR = posterR;
	}
	public String getStillFile() {
		return stillFile;
	}
	public void setStillFile(String stillFile) {
		this.stillFile = stillFile;
	}
	
}
