package kr.co.wincom.imcs.api.getNSKidsHome;

import java.io.Serializable;

public class KidsHomeRecom_VO implements Serializable
{
	private static final long serialVersionUID = 2263457198021153080L;
	
	private String categoryId = "";
	private String contsId = "";
	private String contsNm = "";
	private String runtime = "";
	private String stillUrl = "";
	private String stillFile = "";
	private String posterR = "";
	private String posterH = "";
	private String posterV = "";
	
	private String poster_h_url = "";
	private String poster_h_file = "";
	private String poster_v_url = "";
	private String poster_v_file = "";
	
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getContsId() {
		return contsId;
	}
	public void setContsId(String contsId) {
		this.contsId = contsId;
	}
	public String getContsNm() {
		return contsNm;
	}
	public void setContsNm(String contsNm) {
		this.contsNm = contsNm;
	}
	public String getRuntime() {
		return runtime;
	}
	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}
	public String getStillUrl() {
		return stillUrl;
	}
	public void setStillUrl(String stillUrl) {
		this.stillUrl = stillUrl;
	}
	public String getStillFile() {
		return stillFile;
	}
	public void setStillFile(String stillFile) {
		this.stillFile = stillFile;
	}
	public String getPosterR() {
		return posterR;
	}
	public void setPosterR(String posterR) {
		this.posterR = posterR;
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
	public String getPoster_h_url() {
		return poster_h_url;
	}
	public void setPoster_h_url(String poster_h_url) {
		this.poster_h_url = poster_h_url;
	}
	public String getPoster_h_file() {
		return poster_h_file;
	}
	public void setPoster_h_file(String poster_h_file) {
		this.poster_h_file = poster_h_file;
	}
	public String getPoster_v_url() {
		return poster_v_url;
	}
	public void setPoster_v_url(String poster_v_url) {
		this.poster_v_url = poster_v_url;
	}
	public String getPoster_v_file() {
		return poster_v_file;
	}
	public void setPoster_v_file(String poster_v_file) {
		this.poster_v_file = poster_v_file;
	}
}
