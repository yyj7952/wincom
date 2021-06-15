package kr.co.wincom.imcs.api.getNSReposited;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;

@SuppressWarnings("serial")
public class GetNSRepositedResponseVO implements Serializable {

	private String contsType          = "";		// c_conts_type
	private String catId              = "";		// vc_cat_id
	private String imgUrl             = "";		// c_img_url
	private String imgFileName        = "";		// c_img_file_name
	private String contsId            = "";		// c_conts_id
	private String contsName          = "";		// c_conts_name
	private String chaNum             = "";		// c_cha_num
	private String belongingName      = "";		// c_belonging_name
	private String buyingDate         = "";		// c_buying_date
	private String buyingPrice        = "";		// c_buying_price
	private String expiredDate        = "";		// c_expired_date
	private String cpUseYn            = "";		// c_cp_use_yn
	private String authYn             = "";		// c_auth_yn
	private String prInfo             = "";		// c_rating
	private String isHd               = "";		// c_is_hd
	private String viewType           = "";		// c_view_type
	private String licensingWindowEnd	= "";	// c_licensing_end
	private String point              = "";		// c_point
	private String runtime            = "";		// c_runtime
	private String onairDate          = "";		// c_onair_date
	private String linkTime           = "";		// c_link_time
	private String catGb              = "";		// c_cat_gb1\bc_cat_gb2\bc_cat_gb3
	private String expiredYn          = "";		// c_expired_yn
	private String seriesDesc         = "";		// c_series_desc
	private String setPointYn         = "";		// c_set_point_yn
	private String totalCnt           = "";		// c_cnt
	private String thumbnailFileName  = "";		// c_still_file_name
	private String seriesYn           = "";		// c_series_yn
	private String isNew              = "";		// c_is_new
	private String genreGb            = "";		// c_genre_gb
	private String imgUrl2            = "";		// c_img_url
	private String realHd             = "";		// c_realHD_yn
	private String serCatId           = "";		// c_ser_cat_id
	private String seriesNo           = "";		// c_series_no
	private String serviceGb          = "";		// c_service_icon
	private String fmYn               = "";		// c_fm_info
	//private String assetId			  = "";		// ????
	//private String visitFlag = "";
	private String datafreeBuyYn = "";
	
	//2017.09.15 엔스크린(NSCREEN) 변수 추가
	private String nScreenYn		= "";	// nScreen 컨텐츠 여부
	private String nBuyYn			= "";	// nScreen(STB) 가입자 컨텐츠 구매 여부
	private String nBuyDate			= "";	// 엔스크린 컨텐츠 구매 날짜
	private String nExpiredDate		= "";	// 엔스크린 컨텐츠 만료 날짜
	
	private String ordNum           = "";
	
	
	/********************************************************************
	 * 추가 사용되는 속성값  start
	 ********************************************************************/
	private String tempCheck			= "";
	private String watchDate			= "";
	private String watchEdate           = "";
	private String uflixYn				= "";
	private String productId			= "";
	
	private String buyEndDate			= "";
	private String serviceIcon			= "";
	
	//2018.07.25 권형도
	private String cateGb4		    	= "";
	private String iptvProdChk          = "";
	private String nscProdChk           = "";
	private String iptvTestSbc          = "";
	private String nscTestSbc           = "";
	private String productType          = "";
	private String screenGubun          = "";
	
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		
		// fmYn 은 쿼리에서 '|' 을 만듦.
		// 그래서 nScreen(STB) 목록 조회 요청을 했는데 페이링이 되어 있지 않아서 바로 리턴하면
		// '|' 갯수가 41개가 됨. 정상적인 경우 42개가 되어야 함.
		// 그래서 fmYn 값에 '|' 이 없으면 강제로 세팅함.
		if(StringUtil.replaceNull(this.fmYn, "").indexOf("|") == -1)
			this.fmYn = "|";

		sb.append(StringUtil.replaceNull(this.contsType, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.catId, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.imgUrl, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.imgFileName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.contsId, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.contsName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.chaNum, "")).append(ImcsConstants.COLSEP);			//////

		sb.append(StringUtil.replaceNull(this.belongingName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.buyingDate, "")).append(ImcsConstants.COLSEP);	////// 원래 suggset? 걍 price
		sb.append(StringUtil.replaceNull(this.buyingPrice, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.expiredDate, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.cpUseYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.authYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.prInfo, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.isHd, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.viewType, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.licensingWindowEnd, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.point, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.runtime, "")).append(ImcsConstants.COLSEP);
		
		sb.append(StringUtil.replaceNull(this.onairDate, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.linkTime, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.catGb, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.expiredYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.seriesDesc, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.setPointYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.totalCnt, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.thumbnailFileName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.seriesYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.isNew, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.genreGb, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.imgUrl2, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.realHd, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.serCatId, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.seriesNo, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.serviceIcon, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.fmYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.datafreeBuyYn, "")).append(ImcsConstants.COLSEP);
		
		// 2017.11.30 엔스크린(NSCREEN) 내용 추가
		sb.append(StringUtil.replaceNull(this.nScreenYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.nBuyYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.nBuyDate, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.nExpiredDate, "")).append(ImcsConstants.COLSEP);
		
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

	public String getRuntime() {
		return runtime;
	}

	public void setRuntime(String runtime) {
		this.runtime = runtime;
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

	public String getCatGb() {
		return catGb;
	}

	public void setCatGb(String catGb) {
		this.catGb = catGb;
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

	public String getSeriesNo() {
		return seriesNo;
	}

	public void setSeriesNo(String seriesNo) {
		this.seriesNo = seriesNo;
	}

	public String getServiceGb() {
		return serviceGb;
	}

	public void setServiceGb(String serviceGb) {
		this.serviceGb = serviceGb;
	}

	public String getFmYn() {
		return fmYn;
	}

	public void setFmYn(String fmYn) {
		this.fmYn = fmYn;
	}

	public String getDatafreeBuyYn() {
		return datafreeBuyYn;
	}

	public void setDatafreeBuyYn(String datafreeBuyYn) {
		this.datafreeBuyYn = datafreeBuyYn;
	}
	
	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getUflixYn() {
		return uflixYn;
	}

	public void setUflixYn(String uflixYn) {
		this.uflixYn = uflixYn;
	}

	public String getBuyingPrice() {
		return buyingPrice;
	}

	public void setBuyingPrice(String buyingPrice) {
		this.buyingPrice = buyingPrice;
	}

	public String getSetPointYn() {
		return setPointYn;
	}

	public void setSetPointYn(String setPointYn) {
		this.setPointYn = setPointYn;
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

	public String getWatchDate() {
		return watchDate;
	}

	public void setWatchDate(String watchDate) {
		this.watchDate = watchDate;
	}

	public String getTempCheck() {
		return tempCheck;
	}

	public void setTempCheck(String tempCheck) {
		this.tempCheck = tempCheck;
	}

	public String getBuyEndDate() {
		return buyEndDate;
	}

	public void setBuyEndDate(String buyEndDate) {
		this.buyEndDate = buyEndDate;
	}

	public String getServiceIcon() {
		return serviceIcon;
	}

	public void setServiceIcon(String serviceIcon) {
		this.serviceIcon = serviceIcon;
	}

	public String getImgUrl2() {
		return imgUrl2;
	}

	public void setImgUrl2(String imgUrl2) {
		this.imgUrl2 = imgUrl2;
	}

	public String getnScreenYn() {
		return nScreenYn;
	}
	public void setnScreenYn(String nScreenYn) {
		this.nScreenYn = nScreenYn;
	}
	public String getnBuyYn() {
		return nBuyYn;
	}
	public void setnBuyYn(String nBuyYn) {
		this.nBuyYn = nBuyYn;
	}
	public String getnBuyDate() {
		return nBuyDate;
	}
	public void setnBuyDate(String nBuyDate) {
		this.nBuyDate = nBuyDate;
	}
	public String getnExpiredDate() {
		return nExpiredDate;
	}
	public void setnExpiredDate(String nExpiredDate) {
		this.nExpiredDate = nExpiredDate;
	}
	public String getCateGb4() {
		return cateGb4;
	}
	public void setCateGb4(String cateGb4) {
		this.cateGb4 = cateGb4;
	}
	public String getIptvProdChk() {
		return iptvProdChk;
	}
	public void setIptvProdChk(String iptvProdChk) {
		this.iptvProdChk = iptvProdChk;
	}
	public String getNscProdChk() {
		return nscProdChk;
	}
	public void setNscProdChk(String nscProdChk) {
		this.nscProdChk = nscProdChk;
	}
	public String getIptvTestSbc() {
		return iptvTestSbc;
	}
	public void setIptvTestSbc(String iptvTestSbc) {
		this.iptvTestSbc = iptvTestSbc;
	}
	public String getNscTestSbc() {
		return nscTestSbc;
	}
	public void setNscTestSbc(String nscTestSbc) {
		this.nscTestSbc = nscTestSbc;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getScreenGubun() {
		return screenGubun;
	}
	public void setScreenGubun(String screenGubun) {
		this.screenGubun = screenGubun;
	}

	public String getOrdNum() {
		return ordNum;
	}

	public void setOrdNum(String ordNum) {
		this.ordNum = ordNum;
	}

	public String getWatchEdate() {
		return watchEdate;
	}

	public void setWatchEdate(String watchEdate) {
		this.watchEdate = watchEdate;
	}
	
}
