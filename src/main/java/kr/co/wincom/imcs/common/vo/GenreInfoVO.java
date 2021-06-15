package kr.co.wincom.imcs.common.vo;

import java.io.Serializable;

@SuppressWarnings("serial")
public class GenreInfoVO implements Serializable {
	
	private String genreLarge	  = "";
	private String genreMid		  = "";
	private String genreSmall	  = "";
	private String suggestedPrice = "";
	private String szGenreInfo    = "";
	private String terrYn        = "";
	private String terrPeriod    = "";
	private String terrEdDate   = "";
	private String onairDate     = "";
	private String previewPeriod = "";
	
	// 2019.10.31 - VOD 정산 프로세스 개선 : NPT_VO_WATCH_META 테이블 넣는 변수 추가
    private String assetName 			= "";
    private String ratingCd 			= "";    
    private String runTime 				= "";
    private String cpIdUflix 			= "";
    private String contentFilesize 		= "";
    private String seriesNo		 		= "";
    
	
	public String getGenreLarge() {
		return genreLarge;
	}
	public void setGenreLarge(String genreLarge) {
		this.genreLarge = genreLarge;
	}
	public String getGenreMid() {
		return genreMid;
	}
	public void setGenreMid(String genreMid) {
		this.genreMid = genreMid;
	}
	public String getGenreSmall() {
		return genreSmall;
	}
	public void setGenreSmall(String genreSmall) {
		this.genreSmall = genreSmall;
	}
	public String getSuggestedPrice() {
		return suggestedPrice;
	}
	public void setSuggestedPrice(String suggestedPrice) {
		this.suggestedPrice = suggestedPrice;
	}
	public String getSzGenreInfo() {
		return szGenreInfo;
	}
	public void setSzGenreInfo(String szGenreInfo) {
		this.szGenreInfo = szGenreInfo;
	}
	public String getTerrYn() {
		return terrYn;
	}
	public void setTerrYn(String terrYn) {
		this.terrYn = terrYn;
	}
	public String getTerrPeriod() {
		return terrPeriod;
	}
	public void setTerrPeriod(String terrPeriod) {
		this.terrPeriod = terrPeriod;
	}
	public String getTerrEdDate() {
		return terrEdDate;
	}
	public void setTerrEdDate(String terrEdDate) {
		this.terrEdDate = terrEdDate;
	}
	public String getOnairDate() {
		return onairDate;
	}
	public void setOnairDate(String onairDate) {
		this.onairDate = onairDate;
	}
	public String getPreviewPeriod() {
		return previewPeriod;
	}
	public void setPreviewPeriod(String previewPeriod) {
		this.previewPeriod = previewPeriod;
	}
	public String getAssetName() {
		return assetName;
	}
	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}
	public String getRatingCd() {
		return ratingCd;
	}
	public void setRatingCd(String ratingCd) {
		this.ratingCd = ratingCd;
	}
	public String getRunTime() {
		return runTime;
	}
	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}
	public String getCpIdUflix() {
		return cpIdUflix;
	}
	public void setCpIdUflix(String cpIdUflix) {
		this.cpIdUflix = cpIdUflix;
	}
	public String getContentFilesize() {
		return contentFilesize;
	}
	public void setContentFilesize(String contentFilesize) {
		this.contentFilesize = contentFilesize;
	}
	public String getSeriesNo() {
		return seriesNo;
	}
	public void setSeriesNo(String seriesNo) {
		this.seriesNo = seriesNo;
	}

}
