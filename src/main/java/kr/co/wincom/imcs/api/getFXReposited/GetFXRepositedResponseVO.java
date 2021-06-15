package kr.co.wincom.imcs.api.getFXReposited;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;

@SuppressWarnings("serial")
public class GetFXRepositedResponseVO implements Serializable{
	/********************************************************************
	 * GetFXReposited API 전문 칼럼(순서 일치)
	********************************************************************/
	private String contsType		= "";
	private String catId			= "";
	private String imgUrl			= "";		// 포스터URL
	private String imgFileName		= "";		// 카테고리 패키지 이미지 파일명
	private String albumId			= "";
	private String albumName		= "";
	private String chaNum			= "";		// 채널 번호
	private String belongingName	= "";
	private String buyingDate		= "";
	private String buyingPrice		= "";
	private String expiredDate		= "";
	private String cpUseYn			= "";
	private String authYn			= "";
	private String prInfo			= "";
	private String isHd				= "";
	private String viewType			= "";
	private String licensingWindowEnd	= "";
	private String point			= "";
	private String runTime			= "";		// 상영시간
	private String onairDate		= "";		// 방영일
	private String linkTime			= "";
	private String expiredYn		= "";
	private String seriesDesc		= "";		// 회차설명
	private String setPointYn		= "";
	private String totalCnt			= "";
	private String thumbnailFileName	= "";
	private String seriesYn			= "";
	private String isNew			= "";
	private String genreGb			= "";
	private String realHd			= "";		// REAL HD 여부 (REALHD_YN)
	private String serCatId			= "";		
	private String seriesNo			= "";
	private String contsId			= "";		

	/********************************************************************
	 * 추가 사용되는 속성값  start
	 ********************************************************************/
	private String pid				= "";
	private String srcGb			= "";

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(StringUtil.replaceNull(this.contsType, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.catId, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.imgUrl, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.imgFileName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.albumId, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.albumName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.chaNum, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.belongingName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.buyingDate, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.buyingPrice, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.expiredDate, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.cpUseYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.authYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.prInfo, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.isHd, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.viewType, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.licensingWindowEnd, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.point, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.runTime, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.onairDate, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.linkTime, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.expiredYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.seriesDesc, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.setPointYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.totalCnt, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.thumbnailFileName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.seriesYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.isNew, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.genreGb, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.realHd, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.serCatId, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.seriesNo, "")).append(ImcsConstants.COLSEP);
		//sb.append(StringUtil.replaceNull(this.contsId, "")).append(ImcsConstants.COLSEP); //연동정의서 상엔 들어있음
		
		return sb.toString();
	}


	public String getContsType() {
		return contsType;
	}


	public void setContsType(String contsType) {
		this.contsType = contsType;
	}


	public String getCatId() {
		return catId;
	}


	public void setCatId(String catId) {
		this.catId = catId;
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


	public String getBuyingDate() {
		return buyingDate;
	}


	public void setBuyingDate(String buyingDate) {
		this.buyingDate = buyingDate;
	}


	public String getBuyingPrice() {
		return buyingPrice;
	}


	public void setBuyingPrice(String buyingPrice) {
		this.buyingPrice = buyingPrice;
	}


	public String getExpiredDate() {
		return expiredDate;
	}


	public void setExpiredDate(String expiredDate) {
		this.expiredDate = expiredDate;
	}


	public String getCpUseYn() {
		return cpUseYn;
	}


	public void setCpUseYn(String cpUseYn) {
		this.cpUseYn = cpUseYn;
	}


	public String getAuthYn() {
		return authYn;
	}


	public void setAuthYn(String authYn) {
		this.authYn = authYn;
	}


	public String getPrInfo() {
		return prInfo;
	}


	public void setPrInfo(String prInfo) {
		this.prInfo = prInfo;
	}


	public String getIsHd() {
		return isHd;
	}


	public void setIsHd(String isHd) {
		this.isHd = isHd;
	}


	public String getViewType() {
		return viewType;
	}


	public void setViewType(String viewType) {
		this.viewType = viewType;
	}


	public String getLicensingWindowEnd() {
		return licensingWindowEnd;
	}


	public void setLicensingWindowEnd(String licensingWindowEnd) {
		this.licensingWindowEnd = licensingWindowEnd;
	}


	public String getPoint() {
		return point;
	}


	public void setPoint(String point) {
		this.point = point;
	}


	public String getRunTime() {
		return runTime;
	}


	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}


	public String getOnairDate() {
		return onairDate;
	}


	public void setOnairDate(String onairDate) {
		this.onairDate = onairDate;
	}


	public String getLinkTime() {
		return linkTime;
	}


	public void setLinkTime(String linkTime) {
		this.linkTime = linkTime;
	}


	public String getExpiredYn() {
		return expiredYn;
	}


	public void setExpiredYn(String expiredYn) {
		this.expiredYn = expiredYn;
	}


	public String getSeriesDesc() {
		return seriesDesc;
	}


	public void setSeriesDesc(String seriesDesc) {
		this.seriesDesc = seriesDesc;
	}


	public String getSetPointYn() {
		return setPointYn;
	}


	public void setSetPointYn(String setPointYn) {
		this.setPointYn = setPointYn;
	}


	public String getTotalCnt() {
		return totalCnt;
	}


	public void setTotalCnt(String totalCnt) {
		this.totalCnt = totalCnt;
	}


	public String getThumbnailFileName() {
		return thumbnailFileName;
	}


	public void setThumbnailFileName(String thumbnailFileName) {
		this.thumbnailFileName = thumbnailFileName;
	}


	public String getSeriesYn() {
		return seriesYn;
	}


	public void setSeriesYn(String seriesYn) {
		this.seriesYn = seriesYn;
	}


	public String getIsNew() {
		return isNew;
	}


	public void setIsNew(String isNew) {
		this.isNew = isNew;
	}


	public String getGenreGb() {
		return genreGb;
	}


	public void setGenreGb(String genreGb) {
		this.genreGb = genreGb;
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


	public String getContsId() {
		return contsId;
	}


	public void setContsId(String contsId) {
		this.contsId = contsId;
	}


	public String getPid() {
		return pid;
	}


	public void setPid(String pid) {
		this.pid = pid;
	}


	public String getSrcGb() {
		return srcGb;
	}


	public void setSrcGb(String srcGb) {
		this.srcGb = srcGb;
	}


	public String getSerCatId() {
		return serCatId;
	}


	public void setSerCatId(String serCatId) {
		this.serCatId = serCatId;
	}

}
