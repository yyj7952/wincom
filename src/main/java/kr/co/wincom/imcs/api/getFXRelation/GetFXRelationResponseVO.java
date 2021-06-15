package kr.co.wincom.imcs.api.getFXRelation;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;

@SuppressWarnings("serial")
public class GetFXRelationResponseVO implements Serializable{
	/********************************************************************
	 * getFXRelation API 전문 칼럼(순서 일치)
	********************************************************************/	
	private String catId			= "";
	private String catName			= "";
	private String albumId			= "";
	private String albumName		= "";
	private String imgUrl			= "";		// 포스터URL
	private String imgFileName		= "";		// 카테고리 패키지 이미지 파일명
	private String chaNum			= "";		// 채널 번호
	private String price			= "";		// 유/무료
	private String prInfo			= "";		// 연령제한
	private String runTime			= "";		// 상영시간
	private String is51Ch			= "";		// 5.1Ch 여부
	private String isNew			= "";		// 신규 카테고리 등록 여부 (YYYYMMDDHH)
	private String isHot			= "";		// HOT 상품여부 (Y/N)
	private String isCaption		= "";		// 자막유무 (captionYn)
	private String isHd				= "";		// HD 영상 구분
	private String point			= "";		// 평점
	private String is3d				= "";		// 3D여부 (3DYN)
	private String onairDate		= "";		// 방영일
	private String seriesDesc		= "";		// 회차설명
	private String terrYn			= "";
	private String seriesYn			= "";
	private String serCatId			= "";		// 시리즈카테고리 ID
	private String realHd			= "";		// REAL HD 여부 (REALHD_YN)
	private String serviceIcon		= "";

	/********************************************************************
	 * 추가 사용되는 속성값  start
	 ********************************************************************/
	private String pid				= "";
	private String contsId			= "";
	private String catGb			= "";
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(StringUtil.replaceNull(this.catId, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.catName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.albumId, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.albumName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.imgUrl, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.imgFileName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.chaNum, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.price, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.prInfo, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.runTime, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.is51Ch, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.isNew, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.isHot, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.isCaption, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.isHd, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.point, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.is3d, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.onairDate, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.seriesDesc, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.terrYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.seriesYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.serCatId, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.realHd, "")).append(ImcsConstants.COLSEP);
		return sb.toString();
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

	public String getChaNum() {
		return chaNum;
	}

	public void setChaNum(String chaNum) {
		this.chaNum = chaNum;
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

	public String getIs3d() {
		return is3d;
	}

	public void setIs3d(String is3d) {
		this.is3d = is3d;
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

	public String getTerrYn() {
		return terrYn;
	}

	public void setTerrYn(String terrYn) {
		this.terrYn = terrYn;
	}

	public String getSeriesYn() {
		return seriesYn;
	}

	public void setSeriesYn(String seriesYn) {
		this.seriesYn = seriesYn;
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

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getContsId() {
		return contsId;
	}

	public void setContsId(String contsId) {
		this.contsId = contsId;
	}

	public String getCatGb() {
		return catGb;
	}

	public void setCatGb(String catGb) {
		this.catGb = catGb;
	}

	public String getServiceIcon() {
		return serviceIcon;
	}

	public void setServiceIcon(String serviceIcon) {
		this.serviceIcon = serviceIcon;
	}
	
	
}
