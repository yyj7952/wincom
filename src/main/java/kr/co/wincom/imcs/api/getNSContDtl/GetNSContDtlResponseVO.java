package kr.co.wincom.imcs.api.getNSContDtl;

import java.io.Serializable;

import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;


@SuppressWarnings("serial")
public class GetNSContDtlResponseVO extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * GetNSContDtl API 전문 칼럼(순서 일치)
	********************************************************************/
    private String contsId			= "";	// 앨범ID
    private String contsName		= "";	// 앨범명
    private String chaNum			= "";	// 단축채널번호
    private String imgUrl			= "";	// 포스터URL
    private String imgFileName		= "";	// 포스터파일명
    private String price			= "";	// 구매가
    private String runtime			= "";	// 상영시간
    private String prInfo			= "";	// 나이제한
    private String is51Ch			= "";	// 5.1ch
    private String synopsis			= "";	// 영화해설 (시놉시스)
    private String isNew			= "";	// 신규카테고리 등록여부
    private String isUpdate			= "";	// 카테고리 등록업데이트 여부
    private String isBest			= "";	// HOT 상품여부
    private String overseerName		= "";	// 감독
    private String actor			= "";	// 출연
    private String isCaption		= "";	// 자막유무
    private String isHd				= "";	// HD영상구분
    private String eventType		= "";	// 이벤트타입정보
    private String point			= "";	// 평점
    private String setPointYn		= "";
    
    private String audioType = "";
    private String hdcontent = "";
    private String distributor = "";
    private String smiYn = "";
    private String smiImpYn = "";
    
    private String is3d = "";
    private String adiProductId = "";
    private String svodYn = "";
    private String eventValue = "";
    private String previewYn = "";
    private String serviceIcon = "";
    private String onairDate = "";
    private String seriesDesc = "";
    private String catGb = "";
    private String releaseDate = "";
    private String terrCh = "";
    private String genreMid = "";
    private String genreGb = "";
    private String genreName	= "";
    private String ticketId = "";
    private String tasteCatId = "";
    private String tasteAlbumId = "";
    private String realHDYn = "";
    private String genreLarge = "";
    private String qdFlag = "";
    private String catName = "";
    
    private String smiLanguage = "";
    private String reserverPrice = "";
    private String previewFlag = "";
    private String mycutYn = "";
    private String reservedDate = "";
    private String viewingFlag = "";
    private String genreUxten = "";
    private String promotionCopy = "";
    private String cpProperty = "";
    private String cpPropertyUfx = "";
    private String presentYn = "";
    private String presentPrice = "";
    private String cpPropertyBin = "";
    
    
    private String approvalId = "";
    private String approvalPrice = "";
    
    private String imgUrl1 = "";
    private String imgUrl2 = "";
    private String imgUrl3 = "";
    private String stillImgUrl = "";
    private String thumbImgUrl = "";
    
    private String trailerUrl1	= "";
    private String trailerUrl2	= "";
    private String trailerUrl3	= "";
    private String trailerFileName1	= "";
    private String trailerFileSize1	= "";
    private String downYn			= "";
    private String fmYn				= "";
    private String assetId			= "";
    
    private String productType = "";
    private String suggestedPrice = "";
    private String buyYn = "";
    private String expiredDate = "";
    private String priceDesc = "";
    private String catId = "";
    private String isDolby = "";
    private String pkgFlag = "";
    
    private String albumType = "";
    private String buyDate = "";
    private String buyingDate = "";
    private String couponYn = "";
    private String stampYn = "";
    private String reservedPrice = "";
    private String terrEdDate = "";
    private int duplicChk;
    
    private String datafreeBillYn = "";
    private String maxViewingLength = "";
    private String surtaxrate = "";
    private String seasonYn = "";
    
    
	public String getContsId() {
		return contsId;
	}
	public void setContsId(String contsId) {
		this.contsId = contsId;
	}
	public String getContsName() {
		return contsName;
	}
	public void setContsName(String contsName) {
		this.contsName = contsName;
	}
	public String getChaNum() {
		return chaNum;
	}
	public void setChaNum(String chaNum) {
		this.chaNum = chaNum;
	}
	public String getImgFileName() {
		return imgFileName;
	}
	public void setImgFileName(String imgFileName) {
		this.imgFileName = imgFileName;
	}
	public String getSuggestedPrice() {
		return suggestedPrice;
	}
	public void setSuggestedPrice(String suggestedPrice) {
		this.suggestedPrice = suggestedPrice;
	}
	public String getRuntime() {
		return runtime;
	}
	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}
	public String getPrInfo() {
		return prInfo;
	}
	public void setPrInfo(String prInfo) {
		this.prInfo = prInfo;
	}
	public String getIs51Ch() {
		return is51Ch;
	}
	public void setIs51Ch(String is51Ch) {
		this.is51Ch = is51Ch;
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
	public String getIsBest() {
		return isBest;
	}
	public void setIsBest(String isBest) {
		this.isBest = isBest;
	}
	public String getIsUpdate() {
		return isUpdate;
	}
	public void setIsUpdate(String isUpdate) {
		this.isUpdate = isUpdate;
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
	public String getAudioType() {
		return audioType;
	}
	public void setAudioType(String audioType) {
		this.audioType = audioType;
	}
	public String getHdcontent() {
		return hdcontent;
	}
	public void setHdcontent(String hdcontent) {
		this.hdcontent = hdcontent;
	}
	public String getDistributor() {
		return distributor;
	}
	public void setDistributor(String distributor) {
		this.distributor = distributor;
	}
	public String getSmiYn() {
		return smiYn;
	}
	public void setSmiYn(String smiYn) {
		this.smiYn = smiYn;
	}
	public String getSmiImpYn() {
		return smiImpYn;
	}
	public void setSmiImpYn(String smiImpYn) {
		this.smiImpYn = smiImpYn;
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
	public String getAdiProductId() {
		return adiProductId;
	}
	public void setAdiProductId(String adiProductId) {
		this.adiProductId = adiProductId;
	}
	public String getSvodYn() {
		return svodYn;
	}
	public void setSvodYn(String svodYn) {
		this.svodYn = svodYn;
	}
	public String getEventValue() {
		return eventValue;
	}
	public void setEventValue(String eventValue) {
		this.eventValue = eventValue;
	}
	public String getPreviewYn() {
		return previewYn;
	}
	public void setPreviewYn(String previewYn) {
		this.previewYn = previewYn;
	}
	public String getServiceIcon() {
		return serviceIcon;
	}
	public void setServiceIcon(String serviceIcon) {
		this.serviceIcon = serviceIcon;
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
	public String getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}
	public String getTerrCh() {
		return terrCh;
	}
	public void setTerrCh(String terrCh) {
		this.terrCh = terrCh;
	}
	public String getGenreMid() {
		return genreMid;
	}
	public void setGenreMid(String genreMid) {
		this.genreMid = genreMid;
	}
	public String getGenreGb() {
		return genreGb;
	}
	public void setGenreGb(String genreGb) {
		this.genreGb = genreGb;
	}
	public String getTicketId() {
		return ticketId;
	}
	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
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
	public String getRealHDYn() {
		return realHDYn;
	}
	public void setRealHDYn(String realHDYn) {
		this.realHDYn = realHDYn;
	}
	public String getGenreLarge() {
		return genreLarge;
	}
	public void setGenreLarge(String genreLarge) {
		this.genreLarge = genreLarge;
	}
	public String getQdFlag() {
		return qdFlag;
	}
	public void setQdFlag(String qdFlag) {
		this.qdFlag = qdFlag;
	}
	public String getCatName() {
		return catName;
	}
	public void setCatName(String catName) {
		this.catName = catName;
	}
	public String getIsCaption() {
		return isCaption;
	}
	public void setIsCaption(String isCaption) {
		this.isCaption = isCaption;
	}
	public String getSmiLanguage() {
		return smiLanguage;
	}
	public void setSmiLanguage(String smiLanguage) {
		this.smiLanguage = smiLanguage;
	}
	public String getReserverPrice() {
		return reserverPrice;
	}
	public void setReserverPrice(String reserverPrice) {
		this.reserverPrice = reserverPrice;
	}
	public String getPreviewFlag() {
		return previewFlag;
	}
	public void setPreviewFlag(String previewFlag) {
		this.previewFlag = previewFlag;
	}
	public String getMycutYn() {
		return mycutYn;
	}
	public void setMycutYn(String mycutYn) {
		this.mycutYn = mycutYn;
	}
	public String getReservedDate() {
		return reservedDate;
	}
	public void setReservedDate(String reservedDate) {
		this.reservedDate = reservedDate;
	}
	public String getViewingFlag() {
		return viewingFlag;
	}
	public void setViewingFlag(String viewingFlag) {
		this.viewingFlag = viewingFlag;
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
	public String getCpPropertyBin() {
		return cpPropertyBin;
	}
	public void setCpPropertyBin(String cpPropertyBin) {
		this.cpPropertyBin = cpPropertyBin;
	}
	public String getApprovalId() {
		return approvalId;
	}
	public void setApprovalId(String approvalId) {
		this.approvalId = approvalId;
	}
	public String getApprovalPrice() {
		return approvalPrice;
	}
	public void setApprovalPrice(String approvalPrice) {
		this.approvalPrice = approvalPrice;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getImgUrl1() {
		return imgUrl1;
	}
	public void setImgUrl1(String imgUrl1) {
		this.imgUrl1 = imgUrl1;
	}
	public String getImgUrl2() {
		return imgUrl2;
	}
	public void setImgUrl2(String imgUrl2) {
		this.imgUrl2 = imgUrl2;
	}
	public String getImgUrl3() {
		return imgUrl3;
	}
	public void setImgUrl3(String imgUrl3) {
		this.imgUrl3 = imgUrl3;
	}
	public String getIsHd() {
		return isHd;
	}
	public void setIsHd(String isHd) {
		this.isHd = isHd;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getBuyYn() {
		return buyYn;
	}
	public void setBuyYn(String buyYn) {
		this.buyYn = buyYn;
	}
	public String getExpiredDate() {
		return expiredDate;
	}
	public void setExpiredDate(String expiredDate) {
		this.expiredDate = expiredDate;
	}
	public String getCatId() {
		return catId;
	}
	public void setCatId(String catId) {
		this.catId = catId;
	}
	public String getIsDolby() {
		return isDolby;
	}
	public void setIsDolby(String isDolby) {
		this.isDolby = isDolby;
	}
	public String getPkgFlag() {
		return pkgFlag;
	}
	public void setPkgFlag(String pkgFlag) {
		this.pkgFlag = pkgFlag;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getAlbumType() {
		return albumType;
	}
	public void setAlbumType(String albumType) {
		this.albumType = albumType;
	}
	public String getBuyDate() {
		return buyDate;
	}
	public void setBuyDate(String buyDate) {
		this.buyDate = buyDate;
	}
	public String getBuyingDate() {
		return buyingDate;
	}
	public void setBuyingDate(String buyingDate) {
		this.buyingDate = buyingDate;
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
	public int getDuplicChk() {
		return duplicChk;
	}
	public void setDuplicChk(int duplicChk) {
		this.duplicChk = duplicChk;
	}
	public String getPriceDesc() {
		return priceDesc;
	}
	public void setPriceDesc(String priceDesc) {
		this.priceDesc = priceDesc;
	}


	public String getStillImgUrl() {
		return stillImgUrl;
	}


	public void setStillImgUrl(String stillImgUrl) {
		this.stillImgUrl = stillImgUrl;
	}


	public String getTrailerUrl1() {
		return trailerUrl1;
	}


	public void setTrailerUrl1(String trailerUrl1) {
		this.trailerUrl1 = trailerUrl1;
	}


	public String getTrailerUrl2() {
		return trailerUrl2;
	}


	public void setTrailerUrl2(String trailerUrl2) {
		this.trailerUrl2 = trailerUrl2;
	}


	public String getTrailerUrl3() {
		return trailerUrl3;
	}


	public void setTrailerUrl3(String trailerUrl3) {
		this.trailerUrl3 = trailerUrl3;
	}


	public String getTrailerFileName1() {
		return trailerFileName1;
	}


	public void setTrailerFileName1(String trailerFileName1) {
		this.trailerFileName1 = trailerFileName1;
	}


	public String getTrailerFileSize1() {
		return trailerFileSize1;
	}


	public void setTrailerFileSize1(String trailerFileSize1) {
		this.trailerFileSize1 = trailerFileSize1;
	}


	public String getDownYn() {
		return downYn;
	}


	public void setDownYn(String downYn) {
		this.downYn = downYn;
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
	public String getSetPointYn() {
		return setPointYn;
	}
	public void setSetPointYn(String setPointYn) {
		this.setPointYn = setPointYn;
	}
	public String getThumbImgUrl() {
		return thumbImgUrl;
	}
	public void setThumbImgUrl(String thumbImgUrl) {
		this.thumbImgUrl = thumbImgUrl;
	}
	public String getGenreName() {
		return genreName;
	}
	public void setGenreName(String genreName) {
		this.genreName = genreName;
	}	
	public String getDatafreeBillYn() {
		return datafreeBillYn;
	}
	public void setDatafreeBillYn(String datafreeBillYn) {
		this.datafreeBillYn = datafreeBillYn;
	}
	public String getMaxViewingLength() {
		return maxViewingLength;
	}
	public void setMaxViewingLength(String maxViewingLength) {
		this.maxViewingLength = maxViewingLength;
	}
	public String getSurtaxrate() {
		return surtaxrate;
	}
	public void setSurtaxrate(String surtaxrate) {
		this.surtaxrate = surtaxrate;
	}
	public String getSeasonYn() {
		return seasonYn;
	}
	public void setSeasonYn(String seasonYn) {
		this.seasonYn = seasonYn;
	}    
	
}
