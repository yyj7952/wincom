package kr.co.wincom.imcs.api.getNSAlbumList;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;

public class GetNSAlbumListResponseVO implements Serializable
{
	private static final long serialVersionUID = 170116096565178079L;
	//= 무중단 일경우 : SUBSCRIPTION_YN == ""
	
	private String contsId            = "";			// 연동정의서 id
	private String contsName          = "";			// 연동정의서 name
	private String imgUrl             = "";
	private String imgFileName        = "";
	private String catId    		    = "";
	private String chaNum             = "";
	private String catLevel           = "";
	private String suggestedPrice		= "";
	private String prInfo             = "";
	private String runtime            = "";
	private String is51Ch             = "";
	private String isNew              = "";
	private String isUpdate           = "";
	private String isHot              = "";
	private String isCaption          = "";
	private String isHd               = "";
	private String point              = "";
	private String subCnt             = "";
	private String is3d               = "";
	private String serviceGb          = "";
	private String filterGb           = "";
	private String terrCh             = "";
	private String overseerName       = "";
	private String actor              = "";
	private String releaseDate        = "";
	private String catGb              = "";
	private String sampleYn           = "";
	private String sampleCatId        = "";
	private String vodServer1         = "";
	private String vodFileName1       = "";
	private String vodServer2         = "";
	private String vodFileName2       = "";
	private String vodServer3         = "";
	private String vodFileName3       = "";
	private String thumbnailFileName	= "";   
	private String imgSvcUrl			= "";
	public String getImgSvcUrl() {
		return imgSvcUrl;
	}

	public void setImgSvcUrl(String imgSvcUrl) {
		this.imgSvcUrl = imgSvcUrl;
	}

	private String sampleAlbumId      = "";
	private String seriesDesc         = "";
	private String serCatId           = "";
	private String realHd             = "";


	/********************************************************************
	 * 추가 사용되는 속성값  start
	 ********************************************************************/
	private String pid					= "";
	private String resultCode			= "";
	private String resultType			= "";
	private String catType				= "";
	private String sortNo				= "";		// 앨범조회 결과 RANK_NO이나 별도 사용안함
	private String hdContent			= "";
	private String productType			= "";
	private String genreGb				= "";

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		this.catGb	= "" + ImcsConstants.ARRSEP + "" + ImcsConstants.ARRSEP + "";
	
		sb.append(StringUtil.replaceNull(this.contsId, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.contsName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.imgUrl, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.imgFileName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.catId, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.chaNum, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.catLevel, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.suggestedPrice, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.prInfo, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.runtime, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.is51Ch, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.isNew, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.isUpdate, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.isHot, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.isCaption, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.isHd, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.point, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.subCnt, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.is3d, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.serviceGb, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.filterGb, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.terrCh, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.overseerName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.actor, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.releaseDate, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.catGb, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.sampleYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.sampleCatId, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodServer1, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodFileName1, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodServer2, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodFileName2, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodServer3, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.vodFileName3, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.thumbnailFileName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.sampleAlbumId, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.imgSvcUrl, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.seriesDesc, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.serCatId, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.realHd, "")).append(ImcsConstants.COLSEP);
	
		return sb.toString();
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

	public String getChaNum() {
		return chaNum;
	}

	public void setChaNum(String chaNum) {
		this.chaNum = chaNum;
	}

	public String getCatLevel() {
		return catLevel;
	}

	public void setCatLevel(String catLevel) {
		this.catLevel = catLevel;
	}

	public String getPrInfo() {
		return prInfo;
	}

	public void setPrInfo(String prInfo) {
		this.prInfo = prInfo;
	}

	public String getRuntime() {
		return runtime;
	}
	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}

	public String getIs51Ch() {
		return is51Ch;
	}

	public void setIs51Ch(String is51Ch) {
		this.is51Ch = is51Ch;
	}

	public String getIsNew() {
		return isNew;
	}

	public void setIsNew(String isNew) {
		this.isNew = isNew;
	}

	public String getIsUpdate() {
		return isUpdate;
	}

	public void setIsUpdate(String isUpdate) {
		this.isUpdate = isUpdate;
	}

	public String getIsHot() {
		return isHot;
	}

	public void setIsHot(String isHot) {
		this.isHot = isHot;
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

	public String getSubCnt() {
		return subCnt;
	}

	public void setSubCnt(String subCnt) {
		this.subCnt = subCnt;
	}

	public String getIs3d() {
		return is3d;
	}

	public void setIs3d(String is3d) {
		this.is3d = is3d;
	}

	public String getServiceGb() {
		return serviceGb;
	}

	public void setServiceGb(String serviceGb) {
		this.serviceGb = serviceGb;
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

	public String getCatGb() {
		return catGb;
	}

	public void setCatGb(String catGb) {
		this.catGb = catGb;
	}

	public String getSampleYn() {
		return sampleYn;
	}

	public void setSampleYn(String sampleYn) {
		this.sampleYn = sampleYn;
	}

	public String getSampleCatId() {
		return sampleCatId;
	}

	public void setSampleCatId(String sampleCatId) {
		this.sampleCatId = sampleCatId;
	}

	public String getVodServer1() {
		return vodServer1;
	}

	public void setVodServer1(String vodServer1) {
		this.vodServer1 = vodServer1;
	}

	public String getVodFileName1() {
		return vodFileName1;
	}

	public void setVodFileName1(String vodFileName1) {
		this.vodFileName1 = vodFileName1;
	}

	public String getVodServer2() {
		return vodServer2;
	}

	public void setVodServer2(String vodServer2) {
		this.vodServer2 = vodServer2;
	}

	public String getVodFileName2() {
		return vodFileName2;
	}

	public void setVodFileName2(String vodFileName2) {
		this.vodFileName2 = vodFileName2;
	}

	public String getVodServer3() {
		return vodServer3;
	}

	public void setVodServer3(String vodServer3) {
		this.vodServer3 = vodServer3;
	}

	public String getVodFileName3() {
		return vodFileName3;
	}

	public void setVodFileName3(String vodFileName3) {
		this.vodFileName3 = vodFileName3;
	}

	public String getThumbnailFileName() {
		return thumbnailFileName;
	}

	public void setThumbnailFileName(String thumbnailFileName) {
		this.thumbnailFileName = thumbnailFileName;
	}

	public String getSampleAlbumId() {
		return sampleAlbumId;
	}

	public void setSampleAlbumId(String sampleAlbumId) {
		this.sampleAlbumId = sampleAlbumId;
	}

	public String getSeriesDesc() {
		return seriesDesc;
	}

	public void setSeriesDesc(String seriesDesc) {
		this.seriesDesc = seriesDesc;
	}

	public String getSerCatId() {
		return serCatId;
	}
	
	public void setSerCatId(String serCatId) {
		this.serCatId = serCatId;
	}

	public String getRealHd() {
		return realHd;
	}

	public void setRealHd(String realHd) {
		this.realHd = realHd;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultType() {
		return resultType;
	}

	public void setResultType(String resultType) {
		this.resultType = resultType;
	}

	public String getContsId() {
		return contsId;
	}

	public void setContsId(String contsId) {
		this.contsId = contsId;
	}

	public String getCatType() {
		return catType;
	}

	public void setCatType(String catType) {
		this.catType = catType;
	}

	public String getSortNo() {
		return sortNo;
	}

	public void setSortNo(String sortNo) {
		this.sortNo = sortNo;
	}

	public String getHdContent() {
		return hdContent;
	}

	public void setHdContent(String hdContent) {
		this.hdContent = hdContent;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getSuggestedPrice() {
		return suggestedPrice;
	}

	public void setSuggestedPrice(String suggestedPrice) {
		this.suggestedPrice = suggestedPrice;
	}

	public String getGenreGb() {
		return genreGb;
	}

	public void setGenreGb(String genreGb) {
		this.genreGb = genreGb;
	}

	
}
