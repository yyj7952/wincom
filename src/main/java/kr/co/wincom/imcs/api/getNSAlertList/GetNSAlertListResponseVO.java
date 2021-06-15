package kr.co.wincom.imcs.api.getNSAlertList;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;

public class GetNSAlertListResponseVO implements Serializable
{
	private static final long serialVersionUID = 170116096565178079L;
	//= 무중단 일경우 : SUBSCRIPTION_YN == ""
	
	private String idx					= "";
	private String adiAlbumId			= "";		// ID
	private String contentsName			= "";		// NAME
	private String imgUrl				= "";
	private String imgFileName			= "";
	private String parentCatId			= "";
	private String regDate				= "";
	private String contsLevel			= "";		// CAT_LEVEL
	private String price				= "";
	private String prInfo				= "";
	private String runTime				= "";
	private String is51Ch				= "";
	private String isNew				= "";
	private String isUpdate				= "";
	private String isHot				= "";
	private String isCaption			= "";		// CAPTION_YN
	private String isHd					= "";		// HD_CONTENT
	private String is3D					= "";
	private String serviceIcon			= "";		// SERVICE_GB
	private String seriesDesc			= "";
	private String catGb				= "";
	private String seriesNo				= "";
	private String totalCnt				= "";		// TOTAL_CNT
	private String thumbnailFileName	= "";		// STILL_FILE_NAME
	private String realHd				= "";		// REALHD_YN
	private String serCatId				= "";
	private String reservedType			= "";
	private String suggestedPrice		= "";
	private String reservedPrice		= "";
	private String reservedDate			= "";
	private String imgUrl2				= "";
	

	/********************************************************************
	 * 추가 사용되는 속성값  start
	 ********************************************************************/
	private String pid					= "";
	private String resultCode			= "";
	
	private String contsId				= "";
	private String chaNum				= "";

	private String serInfo				= "";
	private String checkValue			= "";
	private String onairDate			= "";
	private String genreGb				= "";
	private String serviceYn			= "";
	private String terrYn				= "";
	private String productType			= "";
	private String imageFlag			= "";
	private String belongingName		= "";
	private String releaseDate			= "";
	private String point				= "";
	private String overseerName			= "";
	private String actor				= "";
	private String licensingWindowEnd	= "";
	private String filterGb				= "";
	private String visitFlag			= "";
	private String subCnt				= "";
	private String terrCh				= "";
	private String hdContent			= "";
	
	
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		
		this.setCatGb(this.getCatGb() + "\b" + this.getCatGb() + "\b" + this.getCatGb());
		 
		sb.append(StringUtil.replaceNull(this.getIdx(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.getAdiAlbumId(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.getContentsName(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.getImgUrl(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.getImgFileName(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.getParentCatId(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.getRegDate(), "")).append(ImcsConstants.COLSEP);

		sb.append(StringUtil.replaceNull(this.getContsLevel(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.getPrice(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.getPrInfo(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.getRunTime(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.getIs51Ch(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.getIsNew(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.getIsUpdate(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.getIsHot(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.getIsCaption(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.getIsHd(), "")).append(ImcsConstants.COLSEP);
		
		sb.append(StringUtil.replaceNull(this.getIs3D(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.getServiceIcon(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.getSeriesDesc(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.getCatGb(), "")).append(ImcsConstants.COLSEP);
		
		sb.append(StringUtil.replaceNull(this.getSeriesNo(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.getTotalCnt(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.getThumbnailFileName(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.getImgUrl2(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.getRealHd(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.getSerCatId(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.getReservedType(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.getSuggestedPrice(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.getReservedPrice(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.getReservedDate(), "")).append(ImcsConstants.COLSEP);
		
		return sb.toString();
	}
	
	public String getIdx() {
		return idx;
	}
	public void setIdx(String idx) {
		this.idx = idx;
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
	public String getParentCatId() {
		return parentCatId;
	}
	public void setParentCatId(String parentCatId) {
		this.parentCatId = parentCatId;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
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
	public String getIs3D() {
		return is3D;
	}
	public void setIs3D(String is3d) {
		is3D = is3d;
	}
	public String getSeriesDesc() {
		return seriesDesc;
	}
	public void setSeriesDesc(String seriesDesc) {
		this.seriesDesc = seriesDesc;
	}
	public String getCatGb() {
		return catGb;
	}
	public void setCatGb(String catGb) {
		this.catGb = catGb;
	}
	public String getSeriesNo() {
		return seriesNo;
	}
	public void setSeriesNo(String seriesNo) {
		this.seriesNo = seriesNo;
	}
	public String getThumbnailFileName() {
		return thumbnailFileName;
	}
	public void setThumbnailFileName(String thumbnailFileName) {
		this.thumbnailFileName = thumbnailFileName;
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
	public String getReservedType() {
		return reservedType;
	}
	public void setReservedType(String reservedType) {
		this.reservedType = reservedType;
	}
	public String getSuggestedPrice() {
		return suggestedPrice;
	}
	public void setSuggestedPrice(String suggestedPrice) {
		this.suggestedPrice = suggestedPrice;
	}
	public String getReservedPrice() {
		return reservedPrice;
	}
	public void setReservedPrice(String reservedPrice) {
		this.reservedPrice = reservedPrice;
	}
	public String getReservedDate() {
		return reservedDate;
	}
	public void setReservedDate(String reservedDate) {
		this.reservedDate = reservedDate;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getAdiAlbumId() {
		return adiAlbumId;
	}
	public void setAdiAlbumId(String adiAlbumId) {
		this.adiAlbumId = adiAlbumId;
	}
	public String getContsId() {
		return contsId;
	}
	public void setContsId(String contsId) {
		this.contsId = contsId;
	}
	public String getTotalCnt() {
		return totalCnt;
	}
	public void setTotalCnt(String totalCnt) {
		this.totalCnt = totalCnt;
	}
	public String getContentsName() {
		return contentsName;
	}
	public void setContentsName(String contentsName) {
		this.contentsName = contentsName;
	}
	public String getContsLevel() {
		return contsLevel;
	}
	public void setContsLevel(String contsLevel) {
		this.contsLevel = contsLevel;
	}
	public String getSerInfo() {
		return serInfo;
	}
	public void setSerInfo(String serInfo) {
		this.serInfo = serInfo;
	}
	public String getCheckValue() {
		return checkValue;
	}
	public void setCheckValue(String checkValue) {
		this.checkValue = checkValue;
	}
	public String getOnairDate() {
		return onairDate;
	}
	public void setOnairDate(String onairDate) {
		this.onairDate = onairDate;
	}
	public String getGenreGb() {
		return genreGb;
	}
	public void setGenreGb(String genreGb) {
		this.genreGb = genreGb;
	}
	public String getServiceYn() {
		return serviceYn;
	}
	public void setServiceYn(String serviceYn) {
		this.serviceYn = serviceYn;
	}
	public String getTerrYn() {
		return terrYn;
	}
	public void setTerrYn(String terrYn) {
		this.terrYn = terrYn;
	}
	public String getServiceIcon() {
		return serviceIcon;
	}
	public void setServiceIcon(String serviceIcon) {
		this.serviceIcon = serviceIcon;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getImageFlag() {
		return imageFlag;
	}
	public void setImageFlag(String imageFlag) {
		this.imageFlag = imageFlag;
	}
	public String getChaNum() {
		return chaNum;
	}
	public void setChaNum(String chaNum) {
		this.chaNum = chaNum;
	}
	public String getBelongingName() {
		return belongingName;
	}
	public void setBelongingName(String belongingName) {
		this.belongingName = belongingName;
	}
	public String getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}
	public String getPoint() {
		return point;
	}
	public void setPoint(String point) {
		this.point = point;
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
	public String getLicensingWindowEnd() {
		return licensingWindowEnd;
	}
	public void setLicensingWindowEnd(String licensingWindowEnd) {
		this.licensingWindowEnd = licensingWindowEnd;
	}
	public String getFilterGb() {
		return filterGb;
	}
	public void setFilterGb(String filterGb) {
		this.filterGb = filterGb;
	}
	public String getVisitFlag() {
		return visitFlag;
	}
	public void setVisitFlag(String visitFlag) {
		this.visitFlag = visitFlag;
	}
	public String getSubCnt() {
		return subCnt;
	}
	public void setSubCnt(String subCnt) {
		this.subCnt = subCnt;
	}
	public String getTerrCh() {
		return terrCh;
	}
	public void setTerrCh(String terrCh) {
		this.terrCh = terrCh;
	}

	public String getHdContent() {
		return hdContent;
	}

	public void setHdContent(String hdContent) {
		this.hdContent = hdContent;
	}

	public String getImgUrl2() {
		return imgUrl2;
	}

	public void setImgUrl2(String imgUrl2) {
		this.imgUrl2 = imgUrl2;
	}

	
}
