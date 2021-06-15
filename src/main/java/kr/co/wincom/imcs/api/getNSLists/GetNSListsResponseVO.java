package kr.co.wincom.imcs.api.getNSLists;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;

@SuppressWarnings("serial")
public class GetNSListsResponseVO implements Serializable {
	
	/********************************************************************
	 * getNSLists API 전문 칼럼(순서 일치)
	********************************************************************/
	private String resultType		= "";
	private String catId			= "";
	private String catName			= "";
	private String catType			= "";
	private String imgUrl			= "";
	private String imgFileName		= "";
	private String parentCatId		= "";
	private String authYn			= "";
	private String chaNum			= "";
	private String catLevel			= "";
	private String price			= "";
	private String parYn			= "";
	private String prInfo			= "";
	private String runTime			= "";
	private String is51ch			= "";
	private String isNew			= "";
	private String isUpdate			= "";
	private String isHot			= "";
	private String isCaption		= "";
	private String isHd				= "";
	private String isContinue		= "";
	private String point			= "";
	private String subCnt			= "";
	private String closeYn			= "";
	private String svodYn			= "";
	private String is3D				= "";		// 3DYN
	private String tdYn				= "";		// 정체가 머냐????????????????????????????????????????
	private String serviceGb		= "";		// SERVICE_ICON
	private String filterGb			= "";
	private String ppsYn			= "";
	private String catDesc			= "";
	private String isOrder			= "";
	private String noCache			= "";
	private String cateFileName		= "";		// FOCUS_FILE_NAME ; NORMAL_FILE_NAME ; SELECT_FILE_NAME
	private String ppmYn			= "";
	private String ppmProdId		= "";
	private String terrCh			= "";
	private String overseerName		= "";
	private String actor			= "";
	private String releaseDate		= "";
	private String onairDate		= "";
	private String seriesDesc		= "";
	private String catGb			= "";
	private String catVer			= "";
	private String sampleYn			= "";
	private String sampleCatId		= "";
	private String vodServer1		= "";
	private String vodFileName1		= "";
	private String vodServer2		= "";
	private String vodFileName2		= "";
	private String vodServer3		= "";
	private String vodFileName3		= "";
	private String thumbnailFileName	= "";
	private String sampleAlbumId	= "";
	private String serCatId			= "";
	private String realHd			= "";
	private String seriesNo			= "";
	private String kidsFileName		= "";
	private String pointWatcha		= "";
	private String datafreeBillFlag	= "";
	private String imgUrl2			= "";

	/********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
	private String sortNo			= "";
	private String conCnt			= "";
	private String focusFileName	= "";
	private String normalFileName	= "";
	private String selectFileName	= "";
	private String fileName			= "";
	private String lastAlbumId		= "";
	
	private String fileList			= "";
	private String tempCatGb		= "";

	//-----------------------------------------------------------------------//	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		this.catGb	= "" + "\b" + "" + "\b" + this.tempCatGb;
		
		if("CAT".equals(this.resultType)){
			this.fileName	= this.focusFileName + ";" + this.normalFileName + ";" + this.selectFileName;
		}
		
		
		sb.append(StringUtil.replaceNull(this.resultType, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.catId, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.catName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.catType, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.imgUrl, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.imgFileName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.parentCatId, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.authYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.chaNum, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.catLevel, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.price, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.parYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.prInfo, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.runTime, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.is51ch, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.isNew, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.isUpdate, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.isHot, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.isCaption, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.isHd, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.isContinue, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.point, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.subCnt, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.closeYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.svodYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.is3D, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.serviceGb, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.filterGb, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.ppsYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.catDesc, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.isOrder, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.noCache, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.fileName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.ppmYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.ppmProdId, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.terrCh, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.overseerName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.actor, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.releaseDate, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.onairDate, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.seriesDesc, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.catGb, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.catVer, "")).append(ImcsConstants.COLSEP);
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
		sb.append(StringUtil.replaceNull(this.imgUrl2, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.serCatId, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.realHd, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.seriesNo, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.kidsFileName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.pointWatcha, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.datafreeBillFlag, "")).append(ImcsConstants.COLSEP);
		
		return sb.toString();
	}

	
	
	public String getResultType() {
		return resultType;
	}

	public void setResultType(String resultType) {
		this.resultType = resultType;
	}

	public String getCatId() {
		return catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public String getCatType() {
		return catType;
	}

	public void setCatType(String catType) {
		this.catType = catType;
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

	public String getAuthYn() {
		return authYn;
	}

	public void setAuthYn(String authYn) {
		this.authYn = authYn;
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

	public String getIs51ch() {
		return is51ch;
	}

	public void setIs51ch(String is51ch) {
		this.is51ch = is51ch;
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

	public String getIsContinue() {
		return isContinue;
	}

	public void setIsContinue(String isContinue) {
		this.isContinue = isContinue;
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

	public String getCloseYn() {
		return closeYn;
	}

	public void setCloseYn(String closeYn) {
		this.closeYn = closeYn;
	}

	public String getSvodYn() {
		return svodYn;
	}

	public void setSvodYn(String svodYn) {
		this.svodYn = svodYn;
	}

	public String getIs3D() {
		return is3D;
	}

	public void setIs3D(String is3d) {
		is3D = is3d;
	}

	public String getTdYn() {
		return tdYn;
	}

	public void setTdYn(String tdYn) {
		this.tdYn = tdYn;
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

	public String getPpsYn() {
		return ppsYn;
	}

	public void setPpsYn(String ppsYn) {
		this.ppsYn = ppsYn;
	}

	public String getCatDesc() {
		return catDesc;
	}

	public void setCatDesc(String catDesc) {
		this.catDesc = catDesc;
	}

	public String getIsOrder() {
		return isOrder;
	}

	public void setIsOrder(String isOrder) {
		this.isOrder = isOrder;
	}

	public String getNoCache() {
		return noCache;
	}

	public void setNoCache(String noCache) {
		this.noCache = noCache;
	}

	public String getCateFileName() {
		return cateFileName;
	}

	public void setCateFileName(String cateFileName) {
		this.cateFileName = cateFileName;
	}

	public String getPpmYn() {
		return ppmYn;
	}

	public void setPpmYn(String ppmYn) {
		this.ppmYn = ppmYn;
	}

	public String getPpmProdId() {
		return ppmProdId;
	}

	public void setPpmProdId(String ppmProdId) {
		this.ppmProdId = ppmProdId;
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

	public String getOnairDate() {
		return onairDate;
	}

	public void setOnairDate(String onairDate) {
		this.onairDate = onairDate;
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

	public String getCatVer() {
		return catVer;
	}

	public void setCatVer(String catVer) {
		this.catVer = catVer;
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

	public String getSeriesNo() {
		return seriesNo;
	}

	public void setSeriesNo(String seriesNo) {
		this.seriesNo = seriesNo;
	}

	public String getKidsFileName() {
		return kidsFileName;
	}

	public void setKidsFileName(String kidsFileName) {
		this.kidsFileName = kidsFileName;
	}

	public String getPointWatcha() {
		return pointWatcha;
	}

	public void setPointWatcha(String pointWatcha) {
		this.pointWatcha = pointWatcha;
	}

	public String getDatafreeBillFlag() {
		return datafreeBillFlag;
	}

	public void setDatafreeBillFlag(String datafreeBillFlag) {
		this.datafreeBillFlag = datafreeBillFlag;
	}

	public String getSortNo() {
		return sortNo;
	}

	public void setSortNo(String sortNo) {
		this.sortNo = sortNo;
	}

	public String getConCnt() {
		return conCnt;
	}

	public void setConCnt(String conCnt) {
		this.conCnt = conCnt;
	}

	public String getFocusFileName() {
		return focusFileName;
	}

	public void setFocusFileName(String focusFileName) {
		this.focusFileName = focusFileName;
	}

	public String getNormalFileName() {
		return normalFileName;
	}

	public void setNormalFileName(String normalFileName) {
		this.normalFileName = normalFileName;
	}

	public String getSelectFileName() {
		return selectFileName;
	}

	public void setSelectFileName(String selectFileName) {
		this.selectFileName = selectFileName;
	}

	public String getLastAlbumId() {
		return lastAlbumId;
	}

	public void setLastAlbumId(String lastAlbumId) {
		this.lastAlbumId = lastAlbumId;
	}

	public String getFileList() {
		return fileList;
	}

	public void setFileList(String fileList) {
		this.fileList = fileList;
	}

	public String getParYn() {
		return parYn;
	}

	public void setParYn(String parYn) {
		this.parYn = parYn;
	}



	public String getImgUrl2() {
		return imgUrl2;
	}



	public void setImgUrl2(String imgUrl2) {
		this.imgUrl2 = imgUrl2;
	}



	public String getFileName() {
		return fileName;
	}



	public void setFileName(String fileName) {
		this.fileName = fileName;
	}



	public String getTempCatGb() {
		return tempCatGb;
	}



	public void setTempCatGb(String tempCatGb) {
		this.tempCatGb = tempCatGb;
	}
	
	
}
