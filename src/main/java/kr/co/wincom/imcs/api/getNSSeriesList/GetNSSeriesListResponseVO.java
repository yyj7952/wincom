package kr.co.wincom.imcs.api.getNSSeriesList;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;


@SuppressWarnings("serial")
public class GetNSSeriesListResponseVO extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * getNSSeriesList API 전문 칼럼(순서 일치)
	********************************************************************/
	private String resultType			= "ALB"; //결과값 구분
	private String albumId				= "";
	private String albumName			= "";
	private String runtime				= "";
	private String seriesNo				= "";
	private String seriesDesc			= "";
	private String onairDate			= "";
	private String productType          = "";
	private String contsType			= "";
	private String prInfo				= "";
	private String price				= "";
	private String synopsis				= "";
	private String serviceIcon          = "";
	private String serviceIconHdtv      = "";
	private String serviceIconUflix     = "";
	private String imgUrl				= "";
	private String imgFileName			= "";
	private String stillUrl				= "";
	private String stillFileName		= "";
	private String downloadYn			= "";
	private String musicOnairFlag		= "";
	private String omnivYn				= "";
	private String vrType               = "";
	private String hevcYn               = "";
	
	private String rowNo                = "";
	private String rankNo               = "";
	private String totalCount           = "";
	private String status               = "";
	
    // golf 2.0 추가 응답 파라미터
	private String releaseDate         = "";
	private String actorsDisplay       = "";
	private String producer             = "";
	private String subTitle            = "";
	private String year                 = "";
	private String nfcCode             = "";
    // 2019.02.11 4D Replay 추가 응답 파라미터
	private String vodType             = "";
    // 2019.02.14 연동 규격 응답값 추가에 따른 파라미터 추가
	private String player               = "";
	private String studio               = "";
	// 2019.06.07 5G 2차 고음질 어셋 추가
	private String hqAudioYn            = "";
	// 2019.08.20 - 모바일tv 기능개선
	private String promotionCopy        = "";
	// 2020.01.02 - inApp (인앱결제) 구매 가능 여부
	private String inappBuyYn 	        = "";
	private String inappPrice			= "";
	
	private String kidsGrade			= "";
	
	// 2020.04.29 - 아이돌라이브 2020 1Q
	private String mainProperty			= "";
	private String subProperty			= "";

	// 2020.12.17 - 골프 UX 개편
	private String genreUxten			= "";
	
	// 2021.02.24 - 모바일TV 기능개선 4차수 : 미편성 PPS의 경우 정보 제공시 구매는 불가능하도록  미편성 콘텐츠라는 Flag정볼르 제공 한다.
	private String serviceFlag         	= "";

	// 2021.04.07 - 아이돌 라이브 유료콘서트
	private String livePpvYn			= "";
	private String payFlag				= "";
	private String performEndDate		= "";
	private String performEndTime		= "";
    @Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(StringUtils.defaultString(this.getResultType(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getAlbumId(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getAlbumName(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getRuntime(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getSeriesNo(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getSeriesDesc(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getOnairDate(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getProductType())).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getPrInfo(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getPrice(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getSynopsis(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getImgUrl(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getImgFileName(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getStillUrl(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getStillFileName(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getDownloadYn(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getStatus(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getOmnivYn(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getVrType(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getHevcYn(), "")).append(ImcsConstants.COLSEP);
		//2019.03.20 추가
		sb.append(StringUtils.defaultString(this.getReleaseDate(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getProducer(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getActorsDisplay(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getSubTitle(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getYear(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getNfcCode(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getVodType(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getPlayer(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getStudio(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getHqAudioYn(), "N")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getPromotionCopy(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getInappPrice(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getKidsGrade(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getMainProperty(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getSubProperty().replaceAll("\\\\b", ImcsConstants.ARRSEP), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getGenreUxten(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getServiceFlag(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getLivePpvYn(), "")).append(ImcsConstants.COLSEP);
		
		return sb.toString();
	}
    
	public String getRowNo() {
		return rowNo;
	}



	public void setRowNo(String rowNo) {
		this.rowNo = rowNo;
	}



	public String getRankNo() {
		return rankNo;
	}



	public void setRankNo(String rankNo) {
		this.rankNo = rankNo;
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

	public String getRuntime() {
		return runtime;
	}

	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}

	public String getSeriesNo() {
		return seriesNo;
	}

	public void setSeriesNo(String seriesNo) {
		this.seriesNo = seriesNo;
	}

	public String getSeriesDesc() {
		return seriesDesc;
	}

	public void setSeriesDesc(String seriesDesc) {
		this.seriesDesc = seriesDesc;
	}

	public String getOnairDate() {
		return onairDate;
	}

	public void setOnairDate(String onairDate) {
		this.onairDate = onairDate;
	}

	public String getContsType() {
		return contsType;
	}

	public void setContsType(String contsType) {
		this.contsType = contsType;
	}

	public String getPrInfo() {
		return prInfo;
	}

	public void setPrInfo(String prInfo) {
		this.prInfo = prInfo;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
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

	public String getDownloadYn() {
		return downloadYn;
	}

	public void setDownloadYn(String downloadYn) {
		this.downloadYn = downloadYn;
	}

	public String getMusicOnairFlag() {
		return musicOnairFlag;
	}

	public void setMusicOnairFlag(String musicOnairFlag) {
		this.musicOnairFlag = musicOnairFlag;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getOmnivYn() {
		return omnivYn;
	}

	public void setOmnivYn(String omnivYn) {
		this.omnivYn = omnivYn;
	}

	public String getServiceIcon() {
		return serviceIcon;
	}

	public void setServiceIcon(String serviceIcon) {
		this.serviceIcon = serviceIcon;
	}

	public String getServiceIconHdtv() {
		return serviceIconHdtv;
	}

	public void setServiceIconHdtv(String serviceIconHdtv) {
		this.serviceIconHdtv = serviceIconHdtv;
	}

	public String getServiceIconUflix() {
		return serviceIconUflix;
	}

	public void setServiceIconUflix(String serviceIconUflix) {
		this.serviceIconUflix = serviceIconUflix;
	}

	public String getVrType() {
		return vrType;
	}

	public void setVrType(String vrType) {
		this.vrType = vrType;
	}

	public String getHevcYn() {
		return hevcYn;
	}

	public void setHevcYn(String hevcYn) {
		this.hevcYn = hevcYn;
	}

	public String getProducer() {
		return producer;
	}

	public void setProducer(String producer) {
		this.producer = producer;
	}
	
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
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

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
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

	public String getHqAudioYn() {
		return hqAudioYn;
	}

	public void setHqAudioYn(String hqAudioYn) {
		this.hqAudioYn = hqAudioYn;
	}

	public String getPromotionCopy() {
		return promotionCopy;
	}

	public void setPromotionCopy(String promotionCopy) {
		this.promotionCopy = promotionCopy;
	}

	public String getInappBuyYn() {
		return inappBuyYn;
	}

	public void setInappBuyYn(String inappBuyYn) {
		this.inappBuyYn = inappBuyYn;
	}

	public String getInappPrice() {
		return inappPrice;
	}

	public void setInappPrice(String inappPrice) {
		this.inappPrice = inappPrice;
	}
	public String getKidsGrade() {
		return kidsGrade;
	}
	public void setKidsGrade(String kidsGrade) {
		this.kidsGrade = kidsGrade;
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

	public String getServiceFlag() {
		return serviceFlag;
	}

	public void setServiceFlag(String serviceFlag) {
		this.serviceFlag = serviceFlag;
	}

	public String getLivePpvYn() {
		return livePpvYn;
	}

	public void setLivePpvYn(String livePpvYn) {
		this.livePpvYn = livePpvYn;
	}

	public String getPayFlag() {
		return payFlag;
	}

	public void setPayFlag(String payFlag) {
		this.payFlag = payFlag;
	}

	public String getPerformEndTime() {
		return performEndTime;
	}

	public void setPerformEndTime(String performEndTime) {
		this.performEndTime = performEndTime;
	}

	public String getPerformEndDate() {
		return performEndDate;
	}

	public void setPerformEndDate(String performEndDate) {
		this.performEndDate = performEndDate;
	}

}
