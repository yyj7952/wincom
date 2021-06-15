package kr.co.wincom.imcs.api.getNSKidsHome;

import java.io.Serializable;

public class KidsHomeWatchInfo_1_VO implements Serializable
{
	private static final long serialVersionUID = 3257101236199415415L;
	
	private String albumId = "";
	private int linkTime = 0;
	private String categoryId = "";
	private String seriesYn = "";
	private String seriesNo = "";
	
	private String link_yn = "";
	private String poster_v_file = "";
	private String poster_v_url = "";
	private String still_url = "";
	private String poster_h_file = "";
	private String poster_h_url = "";
	
	public String getAlbumId() {
		return albumId;
	}
	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}
	public int getLinkTime() {
		return linkTime;
	}
	public void setLinkTime(int linkTime) {
		this.linkTime = linkTime;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getSeriesYn() {
		return seriesYn;
	}
	public void setSeriesYn(String seriesYn) {
		this.seriesYn = seriesYn;
	}
	public String getSeriesNo() {
		return seriesNo;
	}
	public void setSeriesNo(String seriesNo) {
		this.seriesNo = seriesNo;
	}
	public String getLink_yn() {
		return link_yn;
	}
	public void setLink_yn(String link_yn) {
		this.link_yn = link_yn;
	}
	public String getPoster_v_file() {
		return poster_v_file;
	}
	public void setPoster_v_file(String poster_v_file) {
		this.poster_v_file = poster_v_file;
	}
	public String getPoster_v_url() {
		return poster_v_url;
	}
	public void setPoster_v_url(String poster_v_url) {
		this.poster_v_url = poster_v_url;
	}
	public String getStill_url() {
		return still_url;
	}
	public void setStill_url(String still_url) {
		this.still_url = still_url;
	}
	public String getPoster_h_file() {
		return poster_h_file;
	}
	public void setPoster_h_file(String poster_h_file) {
		this.poster_h_file = poster_h_file;
	}
	public String getPoster_h_url() {
		return poster_h_url;
	}
	public void setPoster_h_url(String poster_h_url) {
		this.poster_h_url = poster_h_url;
	}
}
