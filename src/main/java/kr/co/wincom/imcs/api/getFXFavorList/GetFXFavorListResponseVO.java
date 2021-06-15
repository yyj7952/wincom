package kr.co.wincom.imcs.api.getFXFavorList;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;

@SuppressWarnings("serial")
public class GetFXFavorListResponseVO implements Serializable {

	private String idx				  = "";		// 번호 (찜목록 리스트 번호)
	private String albumId			  = "";		// 앨범ID (ID)
	private String albumName		  = "";		// 앨범이름 (NAME)
	private String imgUrl             = "";		// 포스터URL
	private String imgFileName        = "";		// 카테고리 패키지 이미지 파일명
	private String parentCatId        = "";		// 상위카테고리 ID
	private String regDate            = "";		// 찜목록 등록 날짜
	private String belongingName      = "";		// 최상위 카테고리 명
	private String chaNum             = "";		// 채널 번호
	private String catLevel			  = "";		// 카테고리 레벨 정보(1, 2, 3, 4) (CAT_LEVEL)
	private String price              = "";		// 유/무료
	private String prInfo             = "";		// 연령제한
	private String runTime            = "";		// 상영시간
	private String is51Ch             = "";		// 5.1Ch 여부
	private String isNew              = "";		// 신규 카테고리 등록 여부 (YYYYMMDDHH)
	private String isUpdate           = "";		// 업데이트 여부 (YYYYMMDDHH) - 컨텐츠의 경우 N
	private String isHot              = "";		// HOT 상품 여부
	private String isCaption          = "";		// 자막유무 (captionYn)
	private String isHd               = "";		// HD 영상 구분
	private String point              = "";		// 평점
	private String subCnt             = "0";	// 하위 카테고리 및 앨범 개수
	private String is3D				  = "";		// 3D여부 (3DYN)
	private String serviceGb          = "";		// 서비스 구분
	private String filterGb           = "";		// 필터구분
	private String terrCh             = "";		// 지상파채널
	private String overseerName       = "";		// 감독
	private String actor              = "";		// 출연
	private String releaseDate        = "";		// 개봉일
	private String onairDate          = "";		// 방영일
	private String seriesDesc         = "";		// 회차설명
	private String licensingWindowEnd = "";		// 라이센싱계약종료일자 (YYYYMMDD) (licensingEnd)
	private String authYn				= "";
	private String seriesNo           = "";		// 회차
	private String totalCnt           = "";		// 총 목록 수
	private String thumbnailFileName  = "";		// 썸네일 이미지 파일명
	private String realHd             = "";		// REAL HD 여부 (REALHD_YN)
	private String serCatId           = "";		// 시리즈카테고리 ID
	

	/********************************************************************
	 * 추가 사용되는 속성값  start
	 ********************************************************************/
	private String pid				= "";
	private String contsId			= "";
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(StringUtil.replaceNull(this.idx, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.albumId, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.albumName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.imgUrl, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.imgFileName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.parentCatId, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.regDate, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.belongingName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.chaNum, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.catLevel, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.price, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.prInfo, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.runTime, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.is51Ch, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.isNew, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.isUpdate, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.isHot, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.isCaption, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.isHd, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.point, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.subCnt, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.is3D, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.serviceGb, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.filterGb, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.terrCh, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.overseerName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.actor, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.releaseDate, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.onairDate, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.seriesDesc, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.licensingWindowEnd, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.authYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.seriesNo, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.totalCnt, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.thumbnailFileName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.realHd, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.serCatId, "")).append(ImcsConstants.COLSEP);
		
		return sb.toString();
	}


	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
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

	public String getBelongingName() {
		return belongingName;
	}

	public void setBelongingName(String belongingName) {
		this.belongingName = belongingName;
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

	public String getIs3D() {
		return is3D;
	}

	public void setIs3D(String is3d) {
		is3D = is3d;
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

	public String getLicensingWindowEnd() {
		return licensingWindowEnd;
	}

	public void setLicensingWindowEnd(String licensingWindowEnd) {
		this.licensingWindowEnd = licensingWindowEnd;
	}

	public String getAuthYn() {
		return authYn;
	}

	public void setAuthYn(String authYn) {
		this.authYn = authYn;
	}

	public String getSeriesNo() {
		return seriesNo;
	}

	public void setSeriesNo(String seriesNo) {
		this.seriesNo = seriesNo;
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

}
