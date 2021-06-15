package kr.co.wincom.imcs.api.getNSPurchased;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;

public class GetNSPurchasedResponseVO implements Serializable
{
	private static final long serialVersionUID = 170116096565178079L;
	//= 무중단 일경우 : SUBSCRIPTION_YN == ""
	
	/********************************************************************
	 * getNSPurchased API 전문 칼럼(순서 일치)
	********************************************************************/
	private String contsType          		= "";	// 컨텐츠 타입
	private String catId              		= "";	// 카테고리ID
	private String imgUrl             		= "";	// 포스터 URL
	private String imgFileName        		= "";	// 포스터 이미지 파일명
	private String contsId            		= "";	// 영화ID
	private String contsName          		= "";	// 영화제목
	private String chaNum             		= "";	// 단축채널 번호
	
	private String belongingName      		= "";	// 패키지/장르 이름
	private String buyingDate         		= "";	// 구매날짜
	private String buyingPrice        		= "";	// 구매금액
	private String expiredDate        		= "";	// 상품 만료시간 (구매일자 + 24시)
	private String cpUseYn            		= "";	// 쿠폰 사용여부
	private String authYn             		= "";	// 무조건 연령인증
	private String prInfo             		= "";	// 연령제한 (01:일반, 02:7세이상, 03:12세이상, 04:15세이상, 05:19세이상, 06:방송불가)
	private String isHd               		= "";	// HD영상 구분
	private String licensingWindowEnd	 	= "";	// 라이센스 계약종료일
	private String point              		= "";	// 평정
	private String catGb              		= "";	// 카테고리구분 (I20:smart7, PCT:PCTV, NSC:N스크린 - PAD, LTE)
	private String seriesDesc         		= "";	// 회차 설명
	private String totalCnt           		= "";	// 목록 총갯수
	private String genreGb            		= "";	// TV다시보기/VOD구분 (T:TV다시보기(인기), V:VOD(인기))
	private String imgServerUrl       		= "";	// 포스터URL
	private String realHd             		= "";	// REAL HD여부
	private String serCatId           		= "";	// 시리즈카테고리ID
	private String seriesNo           		= "";	// 회차
	private String serviceGb          		= "";	// 서비스구분		// serviceIcon
	private String buyingType         		= "";	// 구매타입
	private String balace             		= "";	// 공제잔액
	private String presentYn          		= "";	// 선물여부 - 선물여부가 2개임
	private String datafreeBuyYn      		= "";	// 데이터 프리 구매여부
	private String datafreeCpUseYn    		= "";	// 데이터 프리 쿠폰 사용여부
	private String datafreeBuyingType 		= "";	// 데이터 프리 구매타입
	private String datafreeBuyingPrice		= "";	// 데이터 프리 구매가격
	private String datafreeBalace	  		= "";	// 데이터 프리 공제잔액
	private String surtaxRate				= "";	// 부가게 요율
	private String realBuyingPrice	  		= "";	// 부가세 포함 실구매가격
	private String realDatafreeBuyingPrice	= "";	// 부가세 포함 데이터 프리 실구매가격
	private String nSaId					= "";	// [12] STB 가입자 정보
	private String nStbMac					= "";	// [14] 가입자 STB MAC Address
	private String nscreenYn				= "";	// [ 1] nScreen(STB) 목록 조회 여부 (Y : 페어링 된 nScreen(STB) 구매 목록 조회, N or null: 모바일 구매 목록 조회)
	private String alwnceCharge				= "";
	private String datafreePrice			= "";
	private String cnt                      = "";
	/********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
	private String catName			  = "";		// belongingName으로 사용됨
	private String cateId			  = "";		// 카테고리 조회 시 cat_id 위는 vc_cat_id
	private String catGb1			  = "";
	private String catGb2			  = "";
	private String catGb3			  = "";
	private String catGb4			  = "";
	private boolean isEmpty			  = false; // 24번째 PRESENT_YN 값을 공백으로 처리
	
	//2018.07.25 권형도
	private String iptvProdChk        = "";
	private String nscProdChk        = "";
	private String iptvTestSbc        = "";
	private String nscTestSbc        = "";

	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append(StringUtil.replaceNull(this.contsType, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.catId, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.imgUrl, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.imgFileName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.contsId, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.contsName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.chaNum, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.belongingName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.buyingDate, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.buyingPrice, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.expiredDate, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.cpUseYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.authYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.prInfo, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.isHd, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.licensingWindowEnd, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.point, "")).append(ImcsConstants.COLSEP);
		sb.append(this.catGb).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.seriesDesc, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.totalCnt, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.genreGb, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.imgServerUrl, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.realHd, "")).append(ImcsConstants.COLSEP);
		
		if(this.isEmpty == false)
			sb.append("N").append(ImcsConstants.COLSEP);
		else
			sb.append("").append(ImcsConstants.COLSEP);
		
		sb.append(StringUtil.replaceNull(this.serCatId, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.seriesNo, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.serviceGb, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.buyingType, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.balace, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.presentYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.datafreeBuyYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.datafreeCpUseYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.datafreeBuyingType, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.datafreeBuyingPrice, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.datafreeBalace, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.surtaxRate, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.realBuyingPrice, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.realDatafreeBuyingPrice, "")).append(ImcsConstants.COLSEP);
		
		sb.append(StringUtil.replaceNull(this.nscreenYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.nSaId, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.nStbMac, "")).append(ImcsConstants.COLSEP);
		
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

	public String getCatGb() {
		return catGb;
	}

	public void setCatGb(String catGb) {
		this.catGb = catGb;
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

	public String getGenreGb() {
		return genreGb;
	}

	public void setGenreGb(String genreGb) {
		this.genreGb = genreGb;
	}

	public String getImgServerUrl() {
		return imgServerUrl;
	}

	public void setImgServerUrl(String imgServerUrl) {
		this.imgServerUrl = imgServerUrl;
	}

	public String getRealHd() {
		return realHd;
	}

	public void setRealHd(String realHd) {
		this.realHd = realHd;
	}

	public String getPresentYn() {
		return presentYn;
	}

	public void setPresentYn(String presentYn) {
		this.presentYn = presentYn;
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

	public String getServiceGb() {
		return serviceGb;
	}

	public void setServiceGb(String serviceGb) {
		this.serviceGb = serviceGb;
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

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
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

	public String getCatGb3() {
		return catGb3;
	}

	public void setCatGb3(String catGb3) {
		this.catGb3 = catGb3;
	}

	public String getCatGb4() {
		return catGb4;
	}

	public void setCatGb4(String catGb4) {
		this.catGb4 = catGb4;
	}

	public String getCateId() {
		return cateId;
	}

	public void setCateId(String cateId) {
		this.cateId = cateId;
	}

	public String getDatafreeBuyYn() {
		return datafreeBuyYn;
	}

	public void setDatafreeBuyYn(String datafreeBuyYn) {
		this.datafreeBuyYn = datafreeBuyYn;
	}

	public String getDatafreeCpUseYn() {
		return datafreeCpUseYn;
	}

	public void setDatafreeCpUseYn(String datafreeCpUseYn) {
		this.datafreeCpUseYn = datafreeCpUseYn;
	}

	public String getDatafreeBuyingType() {
		return datafreeBuyingType;
	}

	public void setDatafreeBuyingType(String datafreeBuyingType) {
		this.datafreeBuyingType = datafreeBuyingType;
	}

	public String getDatafreeBuyingPrice() {
		return datafreeBuyingPrice;
	}

	public void setDatafreeBuyingPrice(String datafreeBuyingPrice) {
		this.datafreeBuyingPrice = datafreeBuyingPrice;
	}

	public String getDatafreeBalace() {
		return datafreeBalace;
	}

	public void setDatafreeBalace(String datafreeBalace) {
		this.datafreeBalace = datafreeBalace;
	}

	public String getSurtaxRate() {
		return surtaxRate;
	}

	public void setSurtaxRate(String surtaxRate) {
		this.surtaxRate = surtaxRate;
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
	
	public String getnSaId() {
		return nSaId;
	}

	public void setnSaId(String nSaId) {
		this.nSaId = nSaId;
	}

	public String getnStbMac() {
		return nStbMac;
	}

	public void setnStbMac(String nStbMac) {
		this.nStbMac = nStbMac;
	}

	public String getNscreenYn() {
		return nscreenYn;
	}

	public void setNscreenYn(String nscreenYn) {
		this.nscreenYn = nscreenYn;
	}

	public boolean isEmpty() {
		return isEmpty;
	}

	public void setEmpty(boolean isEmpty) {
		this.isEmpty = isEmpty;
	}

	public String getAlwnceCharge() {
		return alwnceCharge;
	}

	public void setAlwnceCharge(String alwnceCharge) {
		this.alwnceCharge = alwnceCharge;
	}

	public String getDatafreePrice() {
		return datafreePrice;
	}

	public void setDatafreePrice(String datafreePrice) {
		this.datafreePrice = datafreePrice;
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

	public String getCnt() {
		return cnt;
	}

	public void setCnt(String cnt) {
		this.cnt = cnt;
	}
	
}
