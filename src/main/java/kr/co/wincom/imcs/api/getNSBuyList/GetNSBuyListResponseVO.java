package kr.co.wincom.imcs.api.getNSBuyList;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;

@SuppressWarnings("serial")
public class GetNSBuyListResponseVO implements Serializable
{
	
	private String contsType				= ""; //컨텐츠 타입 "0" = FVOD, "1" = PPV, "2" = PVOD, "3" = SVOD 
	private String contsId					= ""; //영화 ID
	private String contsName				= ""; //영화제목
	private String buyingDate				= ""; //구매날짜    buy_date
	private String expiredDate				= ""; //상품 만료 일시 
	private String rating					= ""; //나이제한   (PR_INFO)
	private String posterFileName			= ""; //포스터 이미지 파일명 (POSTER_IMG_FILE)
	private String stillFileName			= ""; //스틸컷 이미지 파일명 (STILL_IMG_FILE)
	private String catId					= ""; //카테고리 ID
	private String seriesDesc				= ""; //회차 설명
	private String seriesYn					= ""; //시리즈 여부
	private String seriesNo					= ""; //회차
	private String buyingType				= ""; //구매타입 (BUY_TYPE)
	private String presentYn				= ""; //선물여부
	private String datafreeYn				= ""; //데이터Free 구매 여부 (DATAFREE_BUY_YN)
	private String datafreeBuyingType		= ""; //데이터Free 구매타입 (DATAFREE_BUY_TYPE)
	private String realBuyingPrice			= ""; //부가세 포함 실구매금액 (BUY_PRICE)
	private String realDatafreeBuyingPrice	= ""; //부가세 포함 데이터Free 실구매금액(DATAFREE_BUY_PRICE)
	
	//2017.09.07 엔스크린(NSCREEN) 변수 추가
	private String nscreenYn				= "";  //nScreen 컨텐츠 여부
	
	/********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
	private String buyingPrice				= ""; 
	private String catGb1					= "";
	private String catGb2					= "";
	private String cnt						= "";
	private String cntTmp					= "";
	private String balace					= "";
	private String serInfo					= "";
	private String serCatId					= "";
	private String datafreePrice			= "";
	private String datafreeBalace			= "";
	private String realPriceTmp				= "";
	private String realDfPriceTmp			= "";
	
	
	//2018.06.03 엔스크린2차 - 양쪽 스크린 모두 편성되어있는지 및 상품 확인하기 위한 변수 추가
	private String iptvProdChk				= "";
	private String nscProdChk				= "";
	private String iptvTestSbc				= "";
	private String nscTestSbc				= "";
	
	private String posterUrl				= "";
	private String stillUrl					= "";
	
	//2019.03.18 - VR앱 개발사 요청으로 VR_TYPE 추가
	private String vrType					= "";
	//2019.07.31 - VR1.5(OCULUS 결제) 어느 기기에서 구매한 것인지 정보 제공
	private String vrBuyInfo				= "";	 
	
	//2020.01.15 - seamless
	private String screenType				= "";
	private String nscInfo					= "";
	
	private String bookFlag					= "";
	private String categoryType				= "";
	
	// 2021.02.26 - 라이센스 정보 제공
	private String licensingWindowEnd 		= "";
	
	// 2021.04.07 - 아이돌 라이브 유료콘서트
	private String buyContsClass 			= "0";
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(StringUtil.nullToSpace(this.contsType)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.contsId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.contsName)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.catId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.seriesYn)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.seriesNo)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.seriesDesc)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.buyingDate)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.expiredDate)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.rating)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.posterFileName)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.stillFileName)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.buyingType)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.presentYn)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.datafreeYn)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.datafreeBuyingType)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.realBuyingPrice)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.realDatafreeBuyingPrice)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.nscreenYn)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.vrType)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.vrBuyInfo)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.screenType)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.categoryType)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.licensingWindowEnd)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.buyContsClass)).append(ImcsConstants.COLSEP);		
		
		return sb.toString();
	}


	public String getContsType() {
		return contsType;
	}


	public void setContsType(String contsType) {
		this.contsType = contsType;
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


	public String getRating() {
		return rating;
	}


	public void setRating(String rating) {
		this.rating = rating;
	}


	public String getPosterFileName() {
		return posterFileName;
	}


	public void setPosterFileName(String posterFileName) {
		this.posterFileName = posterFileName;
	}


	public String getStillFileName() {
		return stillFileName;
	}


	public void setStillFileName(String stillFileName) {
		this.stillFileName = stillFileName;
	}

	public String getCatId() {
		return catId;
	}


	public void setCatId(String catId) {
		this.catId = catId;
	}


	public String getSeriesDesc() {
		return seriesDesc;
	}


	public void setSeriesDesc(String seriesDesc) {
		this.seriesDesc = seriesDesc;
	}


	public String getCnt() {
		return cnt;
	}


	public void setCnt(String cnt) {
		this.cnt = cnt;
	}


	public String getCntTmp() {
		return cntTmp;
	}


	public void setCntTmp(String cntTmp) {
		this.cntTmp = cntTmp;
	}


	public String getSeriesYn() {
		return seriesYn;
	}


	public void setSeriesYn(String seriesYn) {
		this.seriesYn = seriesYn;
	}


	public String getSerInfo() {
		return serInfo;
	}


	public void setSerInfo(String serInfo) {
		this.serInfo = serInfo;
	}


	public String getSerCatId() {
		return serCatId;
	}


	public void setSerCatId(String serCatId) {
		this.serCatId = serCatId;
	}


	public String getSeriesNo() {
		return seriesNo;
	}


	public void setSeriesNo(String seriesNo) {
		this.seriesNo = seriesNo;
	}


	public String getBuyingType() {
		return buyingType;
	}


	public void setBuyingType(String buyingType) {
		this.buyingType = buyingType;
	}


	public String getBalace() {
		return balace;
	}


	public void setBalace(String balace) {
		this.balace = balace;
	}


	public String getPresentYn() {
		return presentYn;
	}


	public void setPresentYn(String presentYn) {
		this.presentYn = presentYn;
	}


	public String getDatafreeYn() {
		return datafreeYn;
	}


	public void setDatafreeYn(String datafreeYn) {
		this.datafreeYn = datafreeYn;
	}


	public String getDatafreePrice() {
		return datafreePrice;
	}


	public void setDatafreePrice(String datafreePrice) {
		this.datafreePrice = datafreePrice;
	}


	public String getDatafreeBalace() {
		return datafreeBalace;
	}


	public void setDatafreeBalace(String datafreeBalace) {
		this.datafreeBalace = datafreeBalace;
	}


	public String getDatafreeBuyingType() {
		return datafreeBuyingType;
	}


	public void setDatafreeBuyingType(String datafreeBuyingType) {
		this.datafreeBuyingType = datafreeBuyingType;
	}


	public String getRealBuyingPrice() {
		return realBuyingPrice;
	}


	public void setRealBuyingPrice(String realBuyingPrice) {
		this.realBuyingPrice = realBuyingPrice;
	}


	public String getRealDatafreeBuyingPrice() {
		return realDatafreeBuyingPrice;
	}


	public void setRealDatafreeBuyingPrice(String realDatafreeBuyingPrice) {
		this.realDatafreeBuyingPrice = realDatafreeBuyingPrice;
	}


	public String getRealPriceTmp() {
		return realPriceTmp;
	}


	public void setRealPriceTmp(String realPriceTmp) {
		this.realPriceTmp = realPriceTmp;
	}


	public String getRealDfPriceTmp() {
		return realDfPriceTmp;
	}


	public void setRealDfPriceTmp(String realDfPriceTmp) {
		this.realDfPriceTmp = realDfPriceTmp;
	}


	public String getNscreenYn() {
		return nscreenYn;
	}


	public void setNscreenYn(String nscreenYn) {
		this.nscreenYn = nscreenYn;
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


	public String getPosterUrl() {
		return posterUrl;
	}


	public void setPosterUrl(String posterUrl) {
		this.posterUrl = posterUrl;
	}


	public String getStillUrl() {
		return stillUrl;
	}


	public void setStillUrl(String stillUrl) {
		this.stillUrl = stillUrl;
	}


	public String getCatGb1() {
		return catGb1;
	}


	public void setCatGb1(String catGb1) {
		this.catGb1 = catGb1;
	}


	public String getCatGb2() {
		return catGb2;
	}


	public void setCatGb2(String catGb2) {
		this.catGb2 = catGb2;
	}


	public String getVrType() {
		return vrType;
	}


	public void setVrType(String vrType) {
		this.vrType = vrType;
	}


	public String getVrBuyInfo() {
		return vrBuyInfo;
	}


	public void setVrBuyInfo(String vrBuyInfo) {
		this.vrBuyInfo = vrBuyInfo;
	}


	public String getScreenType() {
		return screenType;
	}


	public void setScreenType(String screenType) {
		this.screenType = screenType;
	}


	public String getNscInfo() {
		return nscInfo;
	}


	public void setNscInfo(String nscInfo) {
		this.nscInfo = nscInfo;
	}
	public String getBookFlag() {
		return bookFlag;
	}
	public void setBookFlag(String bookFlag) {
		this.bookFlag = bookFlag;
	}
	public String getCategoryType() {
		return categoryType;
	}
	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}


	public String getLicensingWindowEnd() {
		return licensingWindowEnd;
	}


	public void setLicensingWindowEnd(String licensingWindowEnd) {
		this.licensingWindowEnd = licensingWindowEnd;
	}


	public String getBuyContsClass() {
		return buyContsClass;
	}


	public void setBuyContsClass(String buyContsClass) {
		this.buyContsClass = buyContsClass;
	}

	
}
