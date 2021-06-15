package kr.co.wincom.imcs.api.getNSHighLight;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;

@SuppressWarnings("serial")
public class GetNSHighLightResponseVO extends NoSqlLoggingVO implements Serializable {
	
	private String albumId				= "";
	private String albumNm				= "";
	private String onairDate			= "";
	private String sceneType			= "";
	private String sysnopsis			= "";
	private String watchCount			= "";
	private String stillUrl				= "";
	private String stillFileName		= "";
	private String runTime				= "";
	
	public String getAlbumId() {
		return albumId;
	}
	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}
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
	public String getSceneType() {
		return sceneType;
	}
	public void setSceneType(String sceneType) {
		this.sceneType = sceneType;
	}
	public String getSysnopsis() {
		return sysnopsis;
	}
	public void setSysnopsis(String sysnopsis) {
		this.sysnopsis = sysnopsis;
	}
	public String getWatchCount() {
		return watchCount;
	}
	public void setWatchCount(String watchCount) {
		this.watchCount = watchCount;
	}
	public String getStillUrl() {
		return stillUrl;
	}
	public void setStillUrl(String stillUrl) {
		this.stillUrl = stillUrl;
	}
	public String getStillFileName() {
		return stillFileName;
	}
	public void setStillFileName(String stillFileName) {
		this.stillFileName = stillFileName;
	}
	public String getRunTime() {
		return runTime;
	}
	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}
	
}
