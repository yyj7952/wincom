package kr.co.wincom.imcs.api.getNSPageList;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;

public class GetNSPageListResponseVO implements Serializable
{
	private static final long serialVersionUID = 170116096565178079L;
	
	private String albumId				= "";
	private String albumName			= "";
	private String seriesNo				= "";
	private String seriesDesc			= "";
	private String onairDate			= "";
	private String contsType			= "";			// 컨텐츠타입 (0:FVOD, 1:PPV, 2:PVOD, 3:SVOD, 7:PPM)
	private String prInfo				= "";
	private String posterImgUrl			= "";
	private String imgFileName			= "";
	private String terrCh					= "";
	private String thumbnailImgUrl			= "";
	private String thumbnailImgFileName		= "";
	
	// 2018.07.13 - 골프앱 스윙 설명? 및 상영시간 노출을 위해 추가
	private String runtime              = "";
	private String synopsis             = "";       /* 영화해설 */
	
	/********************************************************************
	 * 추가 사용되는 속성값  start
	 ********************************************************************/
	private String resultCode		= "";
	
	private String catName			= "";
	private int totCnt				= 0;
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(StringUtil.nullToSpace(this.albumId)).append(ImcsConstants.COLSEP);
		sb.append(this.albumName).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.seriesNo)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.seriesDesc)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.onairDate)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.contsType)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.prInfo)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.posterImgUrl)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.imgFileName)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.terrCh)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.thumbnailImgUrl)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.thumbnailImgFileName)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.runtime)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.synopsis)).append(ImcsConstants.COLSEP);
		
		return sb.toString();
	}


	public String getAlbumId() {
		return albumId;
	}


	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}


	public String getAlbumName() {
		return albumName;
	}


	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}


	public String getSeriesNo() {
		return seriesNo;
	}


	public void setSeriesNo(String seriesNo) {
		this.seriesNo = seriesNo;
	}


	public String getSeriesDesc() {
		return seriesDesc;
	}


	public void setSeriesDesc(String seriesDesc) {
		this.seriesDesc = seriesDesc;
	}


	public String getOnairDate() {
		return onairDate;
	}


	public void setOnairDate(String onairDate) {
		this.onairDate = onairDate;
	}


	public String getContsType() {
		return contsType;
	}


	public void setContsType(String contsType) {
		this.contsType = contsType;
	}


	public String getPrInfo() {
		return prInfo;
	}


	public void setPrInfo(String prInfo) {
		this.prInfo = prInfo;
	}


	public String getPosterImgUrl() {
		return posterImgUrl;
	}


	public void setPosterImgUrl(String posterImgUrl) {
		this.posterImgUrl = posterImgUrl;
	}


	public String getImgFileName() {
		return imgFileName;
	}


	public void setImgFileName(String imgFileName) {
		this.imgFileName = imgFileName;
	}


	public String getTerrCh() {
		return terrCh;
	}


	public void setTerrCh(String terrCh) {
		this.terrCh = terrCh;
	}


	public String getThumbnailImgUrl() {
		return thumbnailImgUrl;
	}


	public void setThumbnailImgUrl(String thumbnailImgUrl) {
		this.thumbnailImgUrl = thumbnailImgUrl;
	}


	public String getThumbnailImgFileName() {
		return thumbnailImgFileName;
	}


	public void setThumbnailImgFileName(String thumbnailImgFileName) {
		this.thumbnailImgFileName = thumbnailImgFileName;
	}


	public String getResultCode() {
		return resultCode;
	}


	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}


	public String getCatName() {
		return catName;
	}


	public void setCatName(String catName) {
		this.catName = catName;
	}


	public int getTotCnt() {
		return totCnt;
	}


	public void setTotCnt(int totCnt) {
		this.totCnt = totCnt;
	}


	public String getRuntime() {
		return runtime;
	}


	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}


	public String getSynopsis() {
		return synopsis;
	}


	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}
	

}
