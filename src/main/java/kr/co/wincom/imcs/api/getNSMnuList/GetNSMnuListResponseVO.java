package kr.co.wincom.imcs.api.getNSMnuList;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;


@SuppressWarnings("serial")
public class GetNSMnuListResponseVO implements Serializable {
	/********************************************************************
	 * GetNSMnuList API 전문 칼럼(순서 일치)
	********************************************************************/
	private String resultType = "";
	private String categoryId = "";
	private String categoryName = "";
	private String categoryType = "";
	//private String imgFileName = "";
	private String imageFileName = "";
	private String ptCategoryId = "";
	private String authYn = "";
	private String chaNum = "";
	private String categoryLevel = "";
	private String billFlag = "";
	private String rating = "";
	private String runTime = "";
	private String is51ch = "";
	private String isNew = "";
	private String isUpdate = "";
	private String isHot = "";
	private String parYn = "";
	private String sortNo = "";
	private int subCnt;
	private int albumSeq;
	private String isHd = "";
	private String point = "";
	private String closeYn = "";
	private String isOrder = "";
	private String noCache = "";
	private String serviceIcon = "";
	private String contentsCount = "";
	private String focusFileName = "";
	private String normalFileName = "";
	private String selectFileName = "";
	private String onairDate = "";
	private String seriesDesc = "";
	private String categoryGb = "";
	private String terrCh = "";
	private String overseeName = "";
	private String actor = "";
	private String ordnum = "";
	private String nameOrder = "";
	private String sampleYn = "";
	private String lastAlbumId = "";
	private String serCatId = "";
	private String serInfo = "";
	private String seriesNo = "";
	private String suggestedPrice = "";
	private String productType = "";
	private String subFileName = "";
	private String synopsis = "";
	// 본편앨범 변수 선언
	private String fpAlbumYn = "";
	private String fpAlbumId = "";
	// M3U8 파일 변수 선언
	private String castisM3u8File = "";
	private String onnetM3u8File = "";
	private String vrType = "";
	// golf 2.0 추가 응답 파라미터
	private String releaseDate = "";
	private String actorsDisplay = "";
	private String producer = "";
	private String subTitle = "";
	private String year = "";
	private String nfcCode = "";
	// 2019.02.11 4D Replay 추가 응답 파라미터
	private String vodType = "";
	// 2019.02.14 연동규격서 응답값 추가 대응
	private String player = "";
	private String studio = "";
	
	private String watchaPoint = "";
	
	private String albumBillFlag = "";
	private String stillFileName = "";
	private String parentCategoryId = "";
	private String lsortNo = "";
	
	private String imgUrl  = "";
	private int albCnt = 0;
	private int catCnt = 0;
	
	// 2020.03.04 - 모바일 아이들나라
	private String specialCatType		= "";
	private String categorySubName		= "";
	private String animaitonUrl			= "";
	private String animaitonFile		= "";
	private String recommendId			= "";
	private String kidsGrade			= "";
	private String categoryFlag			= "";
	
	// 2020.05.06 - 아이돌 라이브 2020-1Q
	private String mainProperty			= "";
	private String subProperty			= "";
	
	// 2020.12.17 - 골프 UX 개편
	private String genreUxten			= "";
	
    @Override
    public String toString() {
		StringBuffer sb = new StringBuffer();
		// 몇몇 row의 adult_yn이 " "로 나타나는 경우가 있는데, 모르겠음
		sb.append(StringUtil.nullToSpace(this.getResultType())).append(ImcsConstants.COLSEP);	
		sb.append(StringUtil.nullToSpace(this.getCategoryId())).append(ImcsConstants.COLSEP);	
		sb.append(StringUtil.nullToSpace(this.getCategoryName())).append(ImcsConstants.COLSEP);
		
		sb.append(StringUtil.nullToSpace(this.getCategoryType())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getImgUrl())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getImageFileName())).append(ImcsConstants.COLSEP);
		
		sb.append(StringUtil.nullToSpace(this.getAuthYn())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getAlbumBillFlag())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getRating())).append(ImcsConstants.COLSEP);
		
		sb.append(StringUtil.nullToSpace(this.getRunTime())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getIsNew())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getIsUpdate())).append(ImcsConstants.COLSEP);
		
		sb.append(StringUtil.nullToSpace(this.getCloseYn())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getServiceIcon())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getTerrCh())).append(ImcsConstants.COLSEP);

		sb.append(StringUtil.nullToSpace(this.getSeriesDesc())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getStillFileName())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getSerCatId())).append(ImcsConstants.COLSEP);
		
		sb.append(StringUtil.nullToSpace(this.getSeriesNo())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getSubFileName())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getVrType())).append(ImcsConstants.COLSEP);

		sb.append(StringUtil.nullToSpace(this.getOnairDate())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getReleaseDate())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getProducer())).append(ImcsConstants.COLSEP);
		
		sb.append(StringUtil.nullToSpace(this.getActorsDisplay())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getSubTitle())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getYear())).append(ImcsConstants.COLSEP);
		
		sb.append(StringUtil.nullToSpace(this.getNfcCode())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getVodType())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getPlayer())).append(ImcsConstants.COLSEP);

		sb.append(StringUtil.nullToSpace(this.getStudio())).append(ImcsConstants.COLSEP);
		
		// 2020.03.04 - 모바일 아이들나라
		sb.append(StringUtil.nullToSpace(this.getSpecialCatType())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getCategorySubName())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getAnimaitonUrl())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getAnimaitonFile())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getRecommendId())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getKidsGrade())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getParentCategoryId())).append(ImcsConstants.COLSEP);		
		sb.append(StringUtil.nullToSpace(this.getMainProperty())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getSubProperty().replaceAll("\\\\b", ImcsConstants.ARRSEP))).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getGenreUxten())).append(ImcsConstants.COLSEP);
		
		return sb.toString();
	}

	public String getResultType() {
		return resultType;
	}

	public void setResultType(String resultType) {
		this.resultType = resultType;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}

	public String getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}

	public String getPtCategoryId() {
		return ptCategoryId;
	}

	public void setPtCategoryId(String ptCategoryId) {
		this.ptCategoryId = ptCategoryId;
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

	public String getCategoryLevel() {
		return categoryLevel;
	}

	public void setCategoryLevel(String categoryLevel) {
		this.categoryLevel = categoryLevel;
	}

	public String getBillFlag() {
		return billFlag;
	}

	public void setBillFlag(String billFlag) {
		this.billFlag = billFlag;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
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

	public String getParYn() {
		return parYn;
	}

	public void setParYn(String parYn) {
		this.parYn = parYn;
	}

	public String getSortNo() {
		return sortNo;
	}

	public void setSortNo(String sortNo) {
		this.sortNo = sortNo;
	}

	public int getSubCnt() {
		return subCnt;
	}

	public void setSubCnt(int subCnt) {
		this.subCnt = subCnt;
	}
	
	public int getAlbumSeq() {
		return albumSeq;
	}

	public void setAlbumSeq(int albumSeq) {
		this.albumSeq = albumSeq;
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

	public String getCloseYn() {
		return closeYn;
	}

	public void setCloseYn(String closeYn) {
		this.closeYn = closeYn;
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

	public String getServiceIcon() {
		return serviceIcon;
	}

	public void setServiceIcon(String serviceIcon) {
		this.serviceIcon = serviceIcon;
	}

	public String getContentsCount() {
		return contentsCount;
	}

	public void setContentsCount(String contentsCount) {
		this.contentsCount = contentsCount;
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

	public String getCategoryGb() {
		return categoryGb;
	}

	public void setCategoryGb(String categoryGb) {
		this.categoryGb = categoryGb;
	}

	public String getTerrCh() {
		return terrCh;
	}

	public void setTerrCh(String terrCh) {
		this.terrCh = terrCh;
	}

	public String getOverseeName() {
		return overseeName;
	}

	public void setOverseeName(String overseeName) {
		this.overseeName = overseeName;
	}

	public String getActor() {
		return actor;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}

	public String getOrdnum() {
		return ordnum;
	}

	public void setOrdnum(String ordnum) {
		this.ordnum = ordnum;
	}

	public String getNameOrder() {
		return nameOrder;
	}

	public void setNameOrder(String nameOrder) {
		this.nameOrder = nameOrder;
	}

	public String getSampleYn() {
		return sampleYn;
	}

	public void setSampleYn(String sampleYn) {
		this.sampleYn = sampleYn;
	}

	public String getLastAlbumId() {
		return lastAlbumId;
	}

	public void setLastAlbumId(String lastAlbumId) {
		this.lastAlbumId = lastAlbumId;
	}

	public String getSerCatId() {
		return serCatId;
	}

	public void setSerCatId(String serCatId) {
		this.serCatId = serCatId;
	}

	public String getSerInfo() {
		return serInfo;
	}

	public void setSerInfo(String serInfo) {
		this.serInfo = serInfo;
	}

	public String getSeriesNo() {
		return seriesNo;
	}

	public void setSeriesNo(String seriesNo) {
		this.seriesNo = seriesNo;
	}

	public String getSuggestedPrice() {
		return suggestedPrice;
	}

	public void setSuggestedPrice(String suggestedPrice) {
		this.suggestedPrice = suggestedPrice;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getSubFileName() {
		return subFileName;
	}

	public void setSubFileName(String subFileName) {
		this.subFileName = subFileName;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	public String getFpAlbumYn() {
		return fpAlbumYn;
	}

	public void setFpAlbumYn(String fpAlbumYn) {
		this.fpAlbumYn = fpAlbumYn;
	}

	public String getFpAlbumId() {
		return fpAlbumId;
	}

	public void setFpAlbumId(String fpAlbumId) {
		this.fpAlbumId = fpAlbumId;
	}

	public String getCastisM3u8File() {
		return castisM3u8File;
	}

	public void setCastisM3u8File(String castisM3u8File) {
		this.castisM3u8File = castisM3u8File;
	}

	public String getOnnetM3u8File() {
		return onnetM3u8File;
	}

	public void setOnnetM3u8File(String onnetM3u8File) {
		this.onnetM3u8File = onnetM3u8File;
	}

	public String getVrType() {
		return vrType;
	}

	public void setVrType(String vrType) {
		this.vrType = vrType;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getActorsDisplay() {
		return actorsDisplay;
	}

	public void setActorsDisplay(String actorsDisplay) {
		this.actorsDisplay = actorsDisplay;
	}

	public String getProducer() {
		return producer;
	}

	public void setProducer(String producer) {
		this.producer = producer;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getNfcCode() {
		return nfcCode;
	}

	public void setNfcCode(String nfcCode) {
		this.nfcCode = nfcCode;
	}

	public String getVodType() {
		return vodType;
	}

	public void setVodType(String vodType) {
		this.vodType = vodType;
	}

	public String getPlayer() {
		return player;
	}

	public void setPlayer(String player) {
		this.player = player;
	}

	public String getStudio() {
		return studio;
	}

	public void setStudio(String studio) {
		this.studio = studio;
	}

	public String getAlbumBillFlag() {
		return albumBillFlag;
	}

	public void setAlbumBillFlag(String albumBillFlag) {
		this.albumBillFlag = albumBillFlag;
	}

	public String getStillFileName() {
		return stillFileName;
	}

	public void setStillFileName(String stillFileName) {
		this.stillFileName = stillFileName;
	}

	public String getParentCategoryId() {
		return parentCategoryId;
	}

	public void setParentCategoryId(String parentCategoryId) {
		this.parentCategoryId = parentCategoryId;
	}

	public String getLsortNo() {
		return lsortNo;
	}

	public void setLsortNo(String lsortNo) {
		this.lsortNo = lsortNo;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public int getAlbCnt() {
		return albCnt;
	}

	public void setAlbCnt(int albCnt) {
		this.albCnt = albCnt;
	}

	public int getCatCnt() {
		return catCnt;
	}

	public void setCatCnt(int catCnt) {
		this.catCnt = catCnt;
	}

	public String getWatchaPoint() {
		return watchaPoint;
	}

	public void setWatchaPoint(String watchaPoint) {
		this.watchaPoint = watchaPoint;
	}

	public String getSpecialCatType() {
		return specialCatType;
	}

	public void setSpecialCatType(String specialCatType) {
		this.specialCatType = specialCatType;
	}

	public String getCategorySubName() {
		return categorySubName;
	}

	public void setCategorySubName(String categorySubName) {
		this.categorySubName = categorySubName;
	}

	public String getAnimaitonUrl() {
		return animaitonUrl;
	}

	public void setAnimaitonUrl(String animaitonUrl) {
		this.animaitonUrl = animaitonUrl;
	}

	public String getAnimaitonFile() {
		return animaitonFile;
	}

	public void setAnimaitonFile(String animaitonFile) {
		this.animaitonFile = animaitonFile;
	}

	public String getRecommendId() {
		return recommendId;
	}

	public void setRecommendId(String recommendId) {
		this.recommendId = recommendId;
	}

	public String getKidsGrade() {
		return kidsGrade;
	}

	public void setKidsGrade(String kidsGrade) {
		this.kidsGrade = kidsGrade;
	}

	public String getCategoryFlag() {
		return categoryFlag;
	}

	public void setCategoryFlag(String categoryFlag) {
		this.categoryFlag = categoryFlag;
	}

	public String getMainProperty() {
		return mainProperty;
	}

	public void setMainProperty(String mainProperty) {
		this.mainProperty = mainProperty;
	}

	public String getSubProperty() {
		return subProperty;
	}

	public void setSubProperty(String subProperty) {
		this.subProperty = subProperty;
	}

	public String getGenreUxten() {
		return genreUxten;
	}

	public void setGenreUxten(String genreUxten) {
		this.genreUxten = genreUxten;
	}
	
}
