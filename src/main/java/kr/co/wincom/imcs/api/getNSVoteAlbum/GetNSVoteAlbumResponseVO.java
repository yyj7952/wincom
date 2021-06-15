package kr.co.wincom.imcs.api.getNSVoteAlbum;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;


@SuppressWarnings("serial")
public class GetNSVoteAlbumResponseVO extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * getNSVoteAlbum API 전문 칼럼(순서 일치)
	********************************************************************/
	private String resultType			= "ALB"; //결과값 구분
	
	private String posterUrl			= "";
	private String stillUrl				= "";
	
	private String albumId				= "";
	private String albumName			= "";
	private String runTime				= "";
	private String categoryId			= "";
	private String seriesYn				= "";
	private String seriesNo				= "";
	private String seriesDesc			= "";
	private String onairDate			= "";
	private String posterImage			= "";
	private String omnivYn				= "";
	private String hqAudioYn			= "";
	private String musicType			= "";
	private String orderNo				= "";
	
	private String mainImgFileName		= "";
	
	
	private String performDate			= "";
	private String performTime			= "";
	
	
	
    @Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(StringUtils.defaultString(this.getResultType(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getAlbumId(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getAlbumName(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getRunTime(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getCategoryId(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getSeriesYn(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getSeriesNo(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getSeriesDesc(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getOnairDate(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getPosterUrl(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getPosterImage(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getStillUrl(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getMainImgFileName(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getOmnivYn(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getHqAudioYn(), "")).append(ImcsConstants.COLSEP);
		
		return sb.toString();
	}

	public String getResultType() {
		return resultType;
	}

	public void setResultType(String resultType) {
		this.resultType = resultType;
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

	public String getRunTime() {
		return runTime;
	}

	public void setRunTime(String runTime) {
		this.runTime = runTime;
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

	public String getPosterImage() {
		return posterImage;
	}

	public void setPosterImage(String posterImage) {
		this.posterImage = posterImage;
	}

	public String getOmnivYn() {
		return omnivYn;
	}

	public void setOmnivYn(String omnivYn) {
		this.omnivYn = omnivYn;
	}

	public String getHqAudioYn() {
		return hqAudioYn;
	}

	public void setHqAudioYn(String hqAudioYn) {
		this.hqAudioYn = hqAudioYn;
	}

	public String getMusicType() {
		return musicType;
	}

	public void setMusicType(String musicType) {
		this.musicType = musicType;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getMainImgFileName() {
		return mainImgFileName;
	}

	public void setMainImgFileName(String mainImgFileName) {
		this.mainImgFileName = mainImgFileName;
	}

	public String getPerformDate() {
		return performDate;
	}

	public void setPerformDate(String performDate) {
		this.performDate = performDate;
	}

	public String getPerformTime() {
		return performTime;
	}

	public void setPerformTime(String performTime) {
		this.performTime = performTime;
	}

	public String getPosterUrl() {
		return posterUrl;
	}

	public void setPosterUrl(String posterUrl) {
		this.posterUrl = posterUrl;
	}

	public String getStillUrl() {
		return stillUrl;
	}

	public void setStillUrl(String stillUrl) {
		this.stillUrl = stillUrl;
	}
    
}
