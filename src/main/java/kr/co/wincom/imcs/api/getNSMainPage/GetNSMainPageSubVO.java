package kr.co.wincom.imcs.api.getNSMainPage;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;


@SuppressWarnings("serial")
public class GetNSMainPageSubVO extends NoSqlLoggingVO implements Serializable {

	private String genreGb			= "";
	private String contsId			= "";
	private String contsName		= "";
	private String imgUrl			= "";
	private String imgFileName		= "";
	private String catId			= "";
	private String catLevel			= "";		// contsLevel
	private String suggestedPrice	= "";
	private String prInfo			= "";
	private String runTime			= "";
	private String is51ch			= "";
	private String isCaption		= "";
	private String isHd				= "";
	private String point			= "";
	private String is3d				= "";
	private String serviceIcon		= "";
	private String filterGb			= "";
	private String terrCh			= "";
	private String overseerName		= "";
	private String actor			= "";
	private String releaseDate		= "";
	private String stillFileName	= "";
	private String seriesDesc		= "";
	private String realHd			= "";
	private String serCatId			= "";
	
	private String hdcontent		= "";
	private String nscType			= "";
	
    @Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("ALB").append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.genreGb, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.contsId, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.contsName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.imgUrl, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.imgFileName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.catId, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.catLevel, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.suggestedPrice, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.prInfo, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.runTime, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.is51ch, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.isCaption, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.isHd, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.point, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.is3d, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.serviceIcon, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.filterGb, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.terrCh, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.overseerName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.actor, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.releaseDate, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.stillFileName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.seriesDesc, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.serCatId, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.realHd, "")).append(ImcsConstants.COLSEP);
		sb.append(ImcsConstants.ROWSEP);
		
		return sb.toString();
	}

    
	public String getGenreGb() {
		return genreGb;
	}

	public void setGenreGb(String genreGb) {
		this.genreGb = genreGb;
	}

	public String getContsId() {
		return contsId;
	}

	public void setContsId(String contsId) {
		this.contsId = contsId;
	}

	public String getContsName() {
		return contsName;
	}

	public void setContsName(String contsName) {
		this.contsName = contsName;
	}

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

	public String getCatId() {
		return catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
	}

	public String getCatLevel() {
		return catLevel;
	}

	public void setCatLevel(String catLevel) {
		this.catLevel = catLevel;
	}

	public String getSuggestedPrice() {
		return suggestedPrice;
	}

	public void setSuggestedPrice(String suggestedPrice) {
		this.suggestedPrice = suggestedPrice;
	}

	public String getPrInfo() {
		return prInfo;
	}

	public void setPrInfo(String prInfo) {
		this.prInfo = prInfo;
	}

	public String getRunTime() {
		return runTime;
	}

	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}

	public String getIs51ch() {
		return is51ch;
	}

	public void setIs51ch(String is51ch) {
		this.is51ch = is51ch;
	}

	public String getIsCaption() {
		return isCaption;
	}

	public void setIsCaption(String isCaption) {
		this.isCaption = isCaption;
	}

	public String getIsHd() {
		return isHd;
	}

	public void setIsHd(String isHd) {
		this.isHd = isHd;
	}

	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

	public String getIs3d() {
		return is3d;
	}

	public void setIs3d(String is3d) {
		this.is3d = is3d;
	}

	public String getServiceIcon() {
		return serviceIcon;
	}

	public void setServiceIcon(String serviceIcon) {
		this.serviceIcon = serviceIcon;
	}

	public String getFilterGb() {
		return filterGb;
	}

	public void setFilterGb(String filterGb) {
		this.filterGb = filterGb;
	}

	public String getTerrCh() {
		return terrCh;
	}

	public void setTerrCh(String terrCh) {
		this.terrCh = terrCh;
	}

	public String getOverseerName() {
		return overseerName;
	}

	public void setOverseerName(String overseerName) {
		this.overseerName = overseerName;
	}

	public String getActor() {
		return actor;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getStillFileName() {
		return stillFileName;
	}

	public void setStillFileName(String stillFileName) {
		this.stillFileName = stillFileName;
	}

	public String getSeriesDesc() {
		return seriesDesc;
	}

	public void setSeriesDesc(String seriesDesc) {
		this.seriesDesc = seriesDesc;
	}

	public String getRealHd() {
		return realHd;
	}

	public void setRealHd(String realHd) {
		this.realHd = realHd;
	}

	public String getSerCatId() {
		return serCatId;
	}

	public void setSerCatId(String serCatId) {
		this.serCatId = serCatId;
	}


	public String getHdcontent() {
		return hdcontent;
	}


	public void setHdcontent(String hdcontent) {
		this.hdcontent = hdcontent;
	}


	public String getNscType() {
		return nscType;
	}


	public void setNscType(String nscType) {
		this.nscType = nscType;
	}

	
}
