package kr.co.wincom.imcs.api.getNSContList;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;

public class GetNSContListResponseVO implements Serializable
{
	private static final long serialVersionUID = 170116096565178079L;
	//= 무중단 일경우 : SUBSCRIPTION_YN == ""
	
	private String albumId			= "";
	private String albumName		= "";
	private String chaNum			= "";
	private String imgUrl			= "";
	private String imgFileName		= "";
	private String price			= "";			// 구매가
	private String runTime			= "";
	private String prInfo			= "";
	private String audioType		= "";
	private String is51				= "";
	private String synopsis			= "";
	private String isNew			= "";
	private String isUpdate			= "";
	private String isHot			= "";
	private String overseerName		= "";
	private String actor			= "";
	private String isCaption		= "";
	private String isHd				= "";
	private String eventType		= "";
	private String point			= "";
	private String seriesNo			= "";
	private String onairDate		= "";
	private String seriesDesc		= "";
	private String contsType		= "";			// 컨텐츠타입 (0:FVOD, 1:PPV, 2:PVOD, 3:SVOD, 7:PPM)
	private String buyYn			= "";			// 구매여부 (0:구매, 1:미구매) - SVOD Only의 경우 사용 안함
	private String expireDate		= "";
	private String is3d				= "";
	private String eventValue		= "";			// 이벤트할인율 (30, 10)
	private String broadcastYn		= "";
	private String previewYn		= "";
	private String serviceGb		= "";
	private String buyDate			= "";
	private String terrCh			= "";
	private String releaseDate		= "";
	private String catGb			= "";
	private String setPointYn		= "";
	private String downloadYn		= "";
	private String stillFileName	= "";
	private String vodServer1		= "";
	private String vodFileName1		= "";
	private String vodServer2		= "";
	private String vodFileName2		= "";
	private String vodServer3		= "";
	private String vodFileName3		= "";
	private String thumbnailFileName	= "";
	private String genreMid			= "";
	private String imgUrl1			= "";
	private String tasteCatId		= "";
	private String tasteAlbumId		= "";
	private String realHd			= "";
	private String genreLarge		= "";
	private String genreInfo		= "";
	private String qdFlag			= "";
	private String fmYn				= "";
	private String assetId			= "";
	private String couponYn			= "";
	private String stampYn			= "";
	private String inappProdId		= "";			// 인앱용 구매ID
	private String inappPrice		= "";			// 인앱구매가격
	private String mycutYn			= "";
	private String smiLanguage		= "";
	private String previewFlag		= "";
	private String reservedPrice	= "";
	private String terrEdDate		= "";			// 지상파컨텐츠 유료종료일
	private String pointWatcha		= "";
	private String reservedDate		= "";
	private String pointCntWatcha	= "";
	private String rating01Watcha	= "";
	private String rating02Watcha	= "";
	private String rating03Watcha	= "";
	private String rating04Watcha	= "";
	private String rating05Watcha	= "";
	private String rating06Watcha	= "";
	private String rating07Watcha	= "";
	private String rating08Watcha	= "";
	private String rating09Watcha	= "";
	private String rating10Watcha	= "";
	private String commentCnt		= "";
	private String linkWatcha		= "";
	private String genreUxten		= "";
	private String promotionCopy	= "";
	private String cpProperty		= "";
	private String cpPropertyUfx	= "";
	private String presentYn		= "";			// 선물존재여부
	private String presentPrice		= "";
	private String datafreeBillFlag	= "";
	private String datafreePrice	= "";			// 데이터프리 구매가격
	private String datafreeInappPrice		= "";	// 데이터프리인앱 구매가격
	private String datafreeInappProdId		= "";	// 데이터프리인앱 구매ID 
	private String ppvDatafreeInappPrice	= "";	// PPV+데이터프리인앱 구매가격
	private String ppvDatafreeInappProdId	= "";	// PPV+데이터프리인앱 구매ID
	private String seasonYn	= "";
	
	private String maxViewingLength	= "";
	private String datafreeBillYn	= "";
	
	/********************************************************************
	 * 추가 사용되는 속성값  start
	 ********************************************************************/
	private String resultCode		= "";
	
	private String catName			= "";			// 메인쿼리 내 결과 이나 쓰이지 않는 것으로 보임
	private String viewingFlag		= "";			// 메인쿼리 내 결과
	private String nscGb			= "";			// 메인쿼리 내 결과
	private String adiProdId		= "";
	// private String smiYn			= "";			// 사용안함
	// private String smiImpYn		= "";			// 사용안함
	// private String distributor	= "";			// 사용안함
	private String suggestedPrice	= "";
	private String downCnt			= "";
	private String contentsId		= "";
	private String contentsName		= "";
	//private String vodFileSize	= "";			// 사용안함
	private String cpPropertyBin	= "";
	private String productType		= "";
	private String priceDesc		= "";
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(StringUtil.nullToSpace(this.albumId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.albumName)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.chaNum)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.imgUrl)).append(ImcsConstants.COLSEP);
		sb.append(this.imgFileName).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.price)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.runTime)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.prInfo)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.is51)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.synopsis)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.isNew)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.isUpdate)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.isHot)).append(ImcsConstants.COLSEP);
		//sb.append(StringUtil.nullToSpace(this.price)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.overseerName)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.actor)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.isCaption)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.isHd)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.eventType)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.point)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.seriesNo)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.onairDate)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.seriesDesc)).append(ImcsConstants.COLSEP);
		sb.append(this.contsType).append(ImcsConstants.COLSEP);
		sb.append(this.buyYn).append(ImcsConstants.COLSEP);
		sb.append(this.expireDate).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.is3d)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToZero(this.eventValue)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.broadcastYn)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.previewYn)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.serviceGb)).append(ImcsConstants.COLSEP);
		sb.append(this.buyDate).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.terrCh)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.releaseDate)).append(ImcsConstants.COLSEP);
		sb.append(this.catGb).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.setPointYn)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.downloadYn)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.stillFileName)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.vodServer1)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.vodFileName1)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.vodServer2)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.vodFileName2)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.vodServer3)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.vodFileName3)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.thumbnailFileName)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.genreMid)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.imgUrl1)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.tasteCatId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.tasteAlbumId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.realHd)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.genreLarge)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.genreInfo)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.qdFlag)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.fmYn)).append(ImcsConstants.COLSEP);
		//sb.append(StringUtil.nullToSpace(this.assetId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.couponYn)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.stampYn)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.inappProdId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.inappPrice)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.mycutYn)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.smiLanguage)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.previewFlag)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.reservedPrice)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.terrEdDate)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.pointWatcha)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.reservedDate)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.pointCntWatcha)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.rating01Watcha)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.rating02Watcha)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.rating03Watcha)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.rating04Watcha)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.rating05Watcha)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.rating06Watcha)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.rating07Watcha)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.rating08Watcha)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.rating09Watcha)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.rating10Watcha)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.commentCnt)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.linkWatcha)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.genreUxten)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.promotionCopy)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.cpProperty)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.cpPropertyUfx)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.presentYn)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.presentPrice)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.datafreeBillFlag)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.datafreePrice)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.datafreeInappPrice)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.datafreeInappProdId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.ppvDatafreeInappPrice)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.ppvDatafreeInappProdId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.seasonYn)).append(ImcsConstants.COLSEP);
		
		return sb.toString();
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



	public String getPrice() {
		return price;
	}



	public void setPrice(String price) {
		this.price = price;
	}



	public String getRunTime() {
		return runTime;
	}



	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}



	public String getPrInfo() {
		return prInfo;
	}



	public void setPrInfo(String prInfo) {
		this.prInfo = prInfo;
	}



	public String getAudioType() {
		return audioType;
	}



	public void setAudioType(String audioType) {
		this.audioType = audioType;
	}



	public String getIs51() {
		return is51;
	}



	public void setIs51(String is51) {
		this.is51 = is51;
	}



	public String getSynopsis() {
		return synopsis;
	}



	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
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



	public String getEventType() {
		return eventType;
	}



	public void setEventType(String eventType) {
		this.eventType = eventType;
	}



	public String getPoint() {
		return point;
	}



	public void setPoint(String point) {
		this.point = point;
	}



	public String getSeriesNo() {
		return seriesNo;
	}



	public void setSeriesNo(String seriesNo) {
		this.seriesNo = seriesNo;
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



	public String getContsType() {
		return contsType;
	}



	public void setContsType(String contsType) {
		this.contsType = contsType;
	}



	public String getBuyYn() {
		return buyYn;
	}



	public void setBuyYn(String buyYn) {
		this.buyYn = buyYn;
	}



	public String getExpireDate() {
		return expireDate;
	}



	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}



	public String getIs3d() {
		return is3d;
	}



	public void setIs3d(String is3d) {
		this.is3d = is3d;
	}



	public String getEventValue() {
		return eventValue;
	}



	public void setEventValue(String eventValue) {
		this.eventValue = eventValue;
	}



	public String getBroadcastYn() {
		return broadcastYn;
	}



	public void setBroadcastYn(String broadcastYn) {
		this.broadcastYn = broadcastYn;
	}



	public String getPreviewYn() {
		return previewYn;
	}



	public void setPreviewYn(String previewYn) {
		this.previewYn = previewYn;
	}



	public String getServiceGb() {
		return serviceGb;
	}



	public void setServiceGb(String serviceGb) {
		this.serviceGb = serviceGb;
	}



	public String getBuyDate() {
		return buyDate;
	}



	public void setBuyDate(String buyDate) {
		this.buyDate = buyDate;
	}



	public String getTerrCh() {
		return terrCh;
	}



	public void setTerrCh(String terrCh) {
		this.terrCh = terrCh;
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



	public String getSetPointYn() {
		return setPointYn;
	}



	public void setSetPointYn(String setPointYn) {
		this.setPointYn = setPointYn;
	}



	public String getDownloadYn() {
		return downloadYn;
	}



	public void setDownloadYn(String downloadYn) {
		this.downloadYn = downloadYn;
	}



	public String getStillFileName() {
		return stillFileName;
	}



	public void setStillFileName(String stillFileName) {
		this.stillFileName = stillFileName;
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



	public String getGenreMid() {
		return genreMid;
	}



	public void setGenreMid(String genreMid) {
		this.genreMid = genreMid;
	}



	public String getTasteCatId() {
		return tasteCatId;
	}



	public void setTasteCatId(String tasteCatId) {
		this.tasteCatId = tasteCatId;
	}



	public String getTasteAlbumId() {
		return tasteAlbumId;
	}



	public void setTasteAlbumId(String tasteAlbumId) {
		this.tasteAlbumId = tasteAlbumId;
	}



	public String getRealHd() {
		return realHd;
	}



	public void setRealHd(String realHd) {
		this.realHd = realHd;
	}



	public String getGenreLarge() {
		return genreLarge;
	}



	public void setGenreLarge(String genreLarge) {
		this.genreLarge = genreLarge;
	}



	public String getGenreInfo() {
		return genreInfo;
	}



	public void setGenreInfo(String genreInfo) {
		this.genreInfo = genreInfo;
	}



	public String getQdFlag() {
		return qdFlag;
	}



	public void setQdFlag(String qdFlag) {
		this.qdFlag = qdFlag;
	}



	public String getFmYn() {
		return fmYn;
	}



	public void setFmYn(String fmYn) {
		this.fmYn = fmYn;
	}



	public String getAssetId() {
		return assetId;
	}



	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}



	public String getCouponYn() {
		return couponYn;
	}



	public void setCouponYn(String couponYn) {
		this.couponYn = couponYn;
	}



	public String getStampYn() {
		return stampYn;
	}



	public void setStampYn(String stampYn) {
		this.stampYn = stampYn;
	}



	public String getInappProdId() {
		return inappProdId;
	}



	public void setInappProdId(String inappProdId) {
		this.inappProdId = inappProdId;
	}



	public String getInappPrice() {
		return inappPrice;
	}



	public void setInappPrice(String inappPrice) {
		this.inappPrice = inappPrice;
	}



	public String getMycutYn() {
		return mycutYn;
	}



	public void setMycutYn(String mycutYn) {
		this.mycutYn = mycutYn;
	}



	public String getSmiLanguage() {
		return smiLanguage;
	}



	public void setSmiLanguage(String smiLanguage) {
		this.smiLanguage = smiLanguage;
	}



	public String getPreviewFlag() {
		return previewFlag;
	}



	public void setPreviewFlag(String previewFlag) {
		this.previewFlag = previewFlag;
	}



	public String getReservedPrice() {
		return reservedPrice;
	}



	public void setReservedPrice(String reservedPrice) {
		this.reservedPrice = reservedPrice;
	}



	public String getTerrEdDate() {
		return terrEdDate;
	}



	public void setTerrEdDate(String terrEdDate) {
		this.terrEdDate = terrEdDate;
	}



	public String getPointWatcha() {
		return pointWatcha;
	}



	public void setPointWatcha(String pointWatcha) {
		this.pointWatcha = pointWatcha;
	}



	public String getReservedDate() {
		return reservedDate;
	}



	public void setReservedDate(String reservedDate) {
		this.reservedDate = reservedDate;
	}



	public String getPointCntWatcha() {
		return pointCntWatcha;
	}



	public void setPointCntWatcha(String pointCntWatcha) {
		this.pointCntWatcha = pointCntWatcha;
	}



	public String getRating01Watcha() {
		return rating01Watcha;
	}



	public void setRating01Watcha(String rating01Watcha) {
		this.rating01Watcha = rating01Watcha;
	}



	public String getRating02Watcha() {
		return rating02Watcha;
	}



	public void setRating02Watcha(String rating02Watcha) {
		this.rating02Watcha = rating02Watcha;
	}



	public String getRating03Watcha() {
		return rating03Watcha;
	}



	public void setRating03Watcha(String rating03Watcha) {
		this.rating03Watcha = rating03Watcha;
	}



	public String getRating04Watcha() {
		return rating04Watcha;
	}



	public void setRating04Watcha(String rating04Watcha) {
		this.rating04Watcha = rating04Watcha;
	}



	public String getRating05Watcha() {
		return rating05Watcha;
	}



	public void setRating05Watcha(String rating05Watcha) {
		this.rating05Watcha = rating05Watcha;
	}



	public String getRating06Watcha() {
		return rating06Watcha;
	}



	public void setRating06Watcha(String rating06Watcha) {
		this.rating06Watcha = rating06Watcha;
	}



	public String getRating07Watcha() {
		return rating07Watcha;
	}



	public void setRating07Watcha(String rating07Watcha) {
		this.rating07Watcha = rating07Watcha;
	}



	public String getRating08Watcha() {
		return rating08Watcha;
	}



	public void setRating08Watcha(String rating08Watcha) {
		this.rating08Watcha = rating08Watcha;
	}



	public String getRating09Watcha() {
		return rating09Watcha;
	}



	public void setRating09Watcha(String rating09Watcha) {
		this.rating09Watcha = rating09Watcha;
	}



	public String getRating10Watcha() {
		return rating10Watcha;
	}



	public void setRating10Watcha(String rating10Watcha) {
		this.rating10Watcha = rating10Watcha;
	}



	public String getCommentCnt() {
		return commentCnt;
	}



	public void setCommentCnt(String commentCnt) {
		this.commentCnt = commentCnt;
	}



	public String getLinkWatcha() {
		return linkWatcha;
	}



	public void setLinkWatcha(String linkWatcha) {
		this.linkWatcha = linkWatcha;
	}



	public String getGenreUxten() {
		return genreUxten;
	}



	public void setGenreUxten(String genreUxten) {
		this.genreUxten = genreUxten;
	}



	public String getPromotionCopy() {
		return promotionCopy;
	}



	public void setPromotionCopy(String promotionCopy) {
		this.promotionCopy = promotionCopy;
	}



	public String getCpProperty() {
		return cpProperty;
	}



	public void setCpProperty(String cpProperty) {
		this.cpProperty = cpProperty;
	}



	public String getCpPropertyUfx() {
		return cpPropertyUfx;
	}



	public void setCpPropertyUfx(String cpPropertyUfx) {
		this.cpPropertyUfx = cpPropertyUfx;
	}



	public String getPresentYn() {
		return presentYn;
	}



	public void setPresentYn(String presentYn) {
		this.presentYn = presentYn;
	}



	public String getPresentPrice() {
		return presentPrice;
	}



	public void setPresentPrice(String presentPrice) {
		this.presentPrice = presentPrice;
	}



	public String getDatafreeBillFlag() {
		return datafreeBillFlag;
	}



	public void setDatafreeBillFlag(String datafreeBillFlag) {
		this.datafreeBillFlag = datafreeBillFlag;
	}



	public String getDatafreePrice() {
		return datafreePrice;
	}



	public void setDatafreePrice(String datafreePrice) {
		this.datafreePrice = datafreePrice;
	}



	public String getDatafreeInappPrice() {
		return datafreeInappPrice;
	}



	public void setDatafreeInappPrice(String datafreeInappPrice) {
		this.datafreeInappPrice = datafreeInappPrice;
	}



	public String getDatafreeInappProdId() {
		return datafreeInappProdId;
	}



	public void setDatafreeInappProdId(String datafreeInappProdId) {
		this.datafreeInappProdId = datafreeInappProdId;
	}



	public String getPpvDatafreeInappPrice() {
		return ppvDatafreeInappPrice;
	}



	public void setPpvDatafreeInappPrice(String ppvDatafreeInappPrice) {
		this.ppvDatafreeInappPrice = ppvDatafreeInappPrice;
	}



	public String getPpvDatafreeInappProdId() {
		return ppvDatafreeInappProdId;
	}



	public void setPpvDatafreeInappProdId(String ppvDatafreeInappProdId) {
		this.ppvDatafreeInappProdId = ppvDatafreeInappProdId;
	}



	public String getSeasonYn() {
		return seasonYn;
	}



	public void setSeasonYn(String seasonYn) {
		this.seasonYn = seasonYn;
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


	public String getCatName() {
		return catName;
	}



	public void setCatName(String catName) {
		this.catName = catName;
	}



	public String getViewingFlag() {
		return viewingFlag;
	}



	public void setViewingFlag(String viewingFlag) {
		this.viewingFlag = viewingFlag;
	}



	public String getNscGb() {
		return nscGb;
	}



	public void setNscGb(String nscGb) {
		this.nscGb = nscGb;
	}



	public String getAdiProdId() {
		return adiProdId;
	}



	public void setAdiProdId(String adiProdId) {
		this.adiProdId = adiProdId;
	}



	public String getSuggestedPrice() {
		return suggestedPrice;
	}



	public void setSuggestedPrice(String suggestedPrice) {
		this.suggestedPrice = suggestedPrice;
	}



	public String getDownCnt() {
		return downCnt;
	}



	public void setDownCnt(String downCnt) {
		this.downCnt = downCnt;
	}



	public String getCpPropertyBin() {
		return cpPropertyBin;
	}



	public void setCpPropertyBin(String cpPropertyBin) {
		this.cpPropertyBin = cpPropertyBin;
	}



	public String getProductType() {
		return productType;
	}



	public void setProductType(String productType) {
		this.productType = productType;
	}



	public String getContentsId() {
		return contentsId;
	}



	public void setContentsId(String contentsId) {
		this.contentsId = contentsId;
	}



	public String getContentsName() {
		return contentsName;
	}



	public void setContentsName(String contentsName) {
		this.contentsName = contentsName;
	}



	public String getPriceDesc() {
		return priceDesc;
	}



	public void setPriceDesc(String priceDesc) {
		this.priceDesc = priceDesc;
	}



	public String getImgUrl1() {
		return imgUrl1;
	}



	public void setImgUrl1(String imgUrl1) {
		this.imgUrl1 = imgUrl1;
	}



	public String getMaxViewingLength() {
		return maxViewingLength;
	}



	public void setMaxViewingLength(String maxViewingLength) {
		this.maxViewingLength = maxViewingLength;
	}



	public String getDatafreeBillYn() {
		return datafreeBillYn;
	}



	public void setDatafreeBillYn(String datafreeBillYn) {
		this.datafreeBillYn = datafreeBillYn;
	}





}
