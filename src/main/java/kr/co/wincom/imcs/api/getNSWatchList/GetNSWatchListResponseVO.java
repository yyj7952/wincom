package kr.co.wincom.imcs.api.getNSWatchList;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;


@SuppressWarnings("serial")
public class GetNSWatchListResponseVO extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * getNSWatchList API 전문 칼럼(순서 일치)
	********************************************************************/
	private String resultType = "";
	private String albumId = "";
	private String albumName = "";
	private String seriesDesc = "";
	private String watchDate = "";
	private String linkTime = "";
	private String prInfo = "";
	private String runtime = "";
	private String nscreenYn = "";
	private String cpNscreenYn = "";
	private String productType = "";
	private String svodYn = "";
	private String categoryId = "";
	private String seriesNo = "";
	private String seriesYn = "";
	private String posterP = "";
	private String stillFileName = "";
	private String buyYn = "";
	private String buyDate = "";
	private String subscribeYn = "";
	private String expiredYn = "";
	private String expiredDate = "";
	private String viewType = "";
	private String datafreeBuyYn = "";
	private String rankNo = "";
	private String genreMid = "";
	private String genreSmall = "";
	private String genreLarge = "";
	private String testSbc = "";
	
    //2019.03.18 - VR앱 개발사 요청으로 VR_TYPE 추가
	private String vrType = "";
	
    // 2019.10.22 - 골프앱 고객체험단개선 변수 추가
	private String releaseDate = "";
	private String subTitle    = "";
	private String round       = "";
	private String vodType     = "";
	
	private String rowNo                = "";
	private String totalCount           = "";
	private String status               = "";
	
	//추가
	private String tempId           = "";
	private String chkSaId           = "";
	private String chkStbSaId           = "";
	private String chkMacAddr           = "";
	
	// 2021.02.26 - 라이센스 정보 제공
	private String licensingWindowEnd = "";
	
	
	private iptvBuyCheckVO nscreen;
	
	// 2019.01.03
	private String screenType = "";
	private String nscInfo	  = "";
	
	private String categoryType = "";
	
	// 2020.09.10 - rqs_flag = C 대응 단편 유효기간 남은 콘텐츠만 노출
	private String seriesContsYn = "";
	
	private String bookYn		= "";
	
    @Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(StringUtils.defaultString(this.getResultType(), "LIST")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getProductType(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getAlbumId(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getAlbumName(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getCategoryId(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getSeriesYn(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getSeriesNo(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getSeriesDesc())).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getBuyDate(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getExpiredDate(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getExpiredYn(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getPrInfo(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getViewType(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getRuntime(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getPosterP(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getStillFileName(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getLinkTime(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getDatafreeBuyYn(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getNscreenYn(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getNscreen()==null?"":this.getNscreen().getBuyYn(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getNscreen()==null?"":this.getNscreen().getBuyDate(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getNscreen()==null?"":this.getNscreen().getExpiredDate(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getVrType(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getReleaseDate(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getSubTitle(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getRound(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getVodType(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getScreenType(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getCategoryType(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getWatchDate(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getLicensingWindowEnd(), "")).append(ImcsConstants.COLSEP);
		
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

	public String getSeriesDesc() {
		return seriesDesc;
	}

	public void setSeriesDesc(String seriesDesc) {
		this.seriesDesc = seriesDesc;
	}

	public String getWatchDate() {
		return watchDate;
	}

	public void setWatchDate(String watchDate) {
		this.watchDate = watchDate;
	}

	public String getLinkTime() {
		return linkTime;
	}

	public void setLinkTime(String linkTime) {
		this.linkTime = linkTime;
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

	public String getNscreenYn() {
		return nscreenYn;
	}

	public void setNscreenYn(String nscreenYn) {
		this.nscreenYn = nscreenYn;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getSvodYn() {
		return svodYn;
	}

	public void setSvodYn(String svodYn) {
		this.svodYn = svodYn;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getSeriesNo() {
		return seriesNo;
	}

	public void setSeriesNo(String seriesNo) {
		this.seriesNo = seriesNo;
	}

	public String getSeriesYn() {
		return seriesYn;
	}

	public void setSeriesYn(String seriesYn) {
		this.seriesYn = seriesYn;
	}

	public String getPosterP() {
		return posterP;
	}

	public void setPosterP(String posterP) {
		this.posterP = posterP;
	}


	public String getCpNscreenYn() {
		return cpNscreenYn;
	}

	public void setCpNscreenYn(String cpNscreenYn) {
		this.cpNscreenYn = cpNscreenYn;
	}

	public String getStillFileName() {
		return stillFileName;
	}

	public void setStillFileName(String stillFileName) {
		this.stillFileName = stillFileName;
	}

	public String getBuyYn() {
		return buyYn;
	}

	public void setBuyYn(String buyYn) {
		this.buyYn = buyYn;
	}

	public String getBuyDate() {
		return buyDate;
	}

	public void setBuyDate(String buyDate) {
		this.buyDate = buyDate;
	}

	public String getSubscribeYn() {
		return subscribeYn;
	}

	public void setSubscribeYn(String subscribeYn) {
		this.subscribeYn = subscribeYn;
	}

	public String getExpiredYn() {
		return expiredYn;
	}

	public void setExpiredYn(String expiredYn) {
		this.expiredYn = expiredYn;
	}

	public String getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(String expiredDate) {
		this.expiredDate = expiredDate;
	}

	public String getViewType() {
		return viewType;
	}

	public void setViewType(String viewType) {
		this.viewType = viewType;
	}

	public String getDatafreeBuyYn() {
		return datafreeBuyYn;
	}

	public void setDatafreeBuyYn(String datafreeBuyYn) {
		this.datafreeBuyYn = datafreeBuyYn;
	}

	public String getRankNo() {
		return rankNo;
	}

	public void setRankNo(String rankNo) {
		this.rankNo = rankNo;
	}

	public String getGenreMid() {
		return genreMid;
	}

	public void setGenreMid(String genreMid) {
		this.genreMid = genreMid;
	}

	public String getGenreSmall() {
		return genreSmall;
	}

	public void setGenreSmall(String genreSmall) {
		this.genreSmall = genreSmall;
	}

	public String getRowNo() {
		return rowNo;
	}

	public void setRowNo(String rowNo) {
		this.rowNo = rowNo;
	}

	public String getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTempId() {
		return tempId;
	}

	public iptvBuyCheckVO getNscreen() {
		return nscreen;
	}

	public void setNscreen(iptvBuyCheckVO nscreen) {
		this.nscreen = nscreen;
	}

	public void setTempId(String tempId) {
		this.tempId = tempId;
	}

	public String getGenreLarge() {
		return genreLarge;
	}

	public void setGenreLarge(String genreLarge) {
		this.genreLarge = genreLarge;
	}

	public String getChkSaId() {
		return chkSaId;
	}

	public void setChkSaId(String chkSaId) {
		this.chkSaId = chkSaId;
	}

	public String getChkStbSaId() {
		return chkStbSaId;
	}

	public void setChkStbSaId(String chkStbSaId) {
		this.chkStbSaId = chkStbSaId;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getRound() {
		return round;
	}

	public void setRound(String round) {
		this.round = round;
	}

	public String getVodType() {
		return vodType;
	}

	public void setVodType(String vodType) {
		this.vodType = vodType;
	}

	public String getVrType() {
		return vrType;
	}

	public void setVrType(String vrType) {
		this.vrType = vrType;
	}

	public String getNscInfo() {
		return nscInfo;
	}

	public void setNscInfo(String nscInfo) {
		this.nscInfo = nscInfo;
	}

	public String getScreenType() {
		return screenType;
	}

	public void setScreenType(String screenType) {
		this.screenType = screenType;
	}
	public String getCategoryType() {
		return categoryType;
	}
	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}

	public String getSeriesContsYn() {
		return seriesContsYn;
	}

	public void setSeriesContsYn(String seriesContsYn) {
		this.seriesContsYn = seriesContsYn;
	}

	public String getTestSbc() {
		return testSbc;
	}

	public void setTestSbc(String testSbc) {
		this.testSbc = testSbc;
	}

	public String getLicensingWindowEnd() {
		return licensingWindowEnd;
	}

	public void setLicensingWindowEnd(String licensingWindowEnd) {
		this.licensingWindowEnd = licensingWindowEnd;
	}

	public String getBookYn() {
		return bookYn;
	}

	public void setBookYn(String bookYn) {
		this.bookYn = bookYn;
	}

	public String getChkMacAddr() {
		return chkMacAddr;
	}

	public void setChkMacAddr(String chkMacAddr) {
		this.chkMacAddr = chkMacAddr;
	}

}
