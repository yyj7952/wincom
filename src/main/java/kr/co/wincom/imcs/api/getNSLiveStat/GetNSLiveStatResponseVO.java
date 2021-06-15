package kr.co.wincom.imcs.api.getNSLiveStat;

import java.io.Serializable;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.ComCpnVO;

public class GetNSLiveStatResponseVO implements Serializable
{
	private static final long serialVersionUID = 170116096565178079L;
	//= 무중단 일경우 : SUBSCRIPTION_YN == ""
	
	private String resultType		= "CON";
	private String contsId			= "";					// 영화ID
	private String contsType		= "";					// 컨텐츠타입 (0:FVOD, 1:PPV, 2:PVOD, 3:SVOD, 7:PPM)
	private String buyYn			= "";					// 구매여부 (0:구매, 1:미구매) - SVOD Only의 경우 사용 안함
	private String buyingDate		= "";					// 구매일자	- SVOD Only의 경우 사용 안함 
	private String expireDate		= "";					// 공연 종료시각 (시청권한만료일시)
	private String price			= "";					// 구매가
	private String pvodProdId		= "";					// PVOD(시리즈/패키지) ID
	private String pvodProdName		= "";					// PVOD(시리즈/패키지) 상품명
	private String subsProdId		= "";					// SVOD/PPM 상품코드
	private String subsProdName		= "";					// SVOD/PPM 상품명
	private String inappProdId		= "";					// 인앱용 구매ID
	private String inappPrice		= "";					// 인앱구매가격
	private String expiredDate		= "";					// 상품만료시간 (일반구매:구매일자+가용시간, 예약구매:첫시청일+가용시간)
	private String reservedDate		= "";					//
	private String previewFlag		= "";					//
	private String nScreenYnTmp		= "";					// nScreen 함수 조회 정보
	private String nScreenYn		= "";					// nScreen 컨텐츠 여부
	private String nScreenProd		= "";					// nScreen 콘텐츠일 때, IPTV 상품 정보
	private String nBuyYn			= "";					// nScreen(STB) 가입자 컨텐츠 구매 여부
	private String nBuyDate			= "";					// 컨텐츠 구매 날짜
	private String nExpiredDate		= "";					// 컨텐츠 만료 날짜
	private String nSaId			= "";					// nScreen(STB) 가입자 번호
	private String nStbMac			= "";					// nScreen(STB) 가입자 맥주소
	private String watchDate		= "";					// 마지막 시청 일시
	private String datafreeBuyYn	= "";					// 데이터프리 구매여부
	private String payInfo			= "";
	
	/********************************************************************
	 * 추가 사용되는 속성값  start
	 ********************************************************************/
	private String resultCode		= "";
	
	private String maxViewingLen	= "";					// 가용시간
	private String uflixProdYn		= "";					// 유플릭스 상품여부
	private String contsNameSt		= "";					// 컨텐츠명 - STAT용
	private String genreInfo		= "";					// 장르정보 - 쿠폰조회 시 사용
	private String cpPropertyBin	= "";					// 쿠폰?
	
	//2018.07.25 권형도
	private String iptvAlbumId      = "";
	private String iptvProdChk      = "";
	private String iptvTestSbc      = "";
	private String nscTestSbc       = "";
	private String mProdId          = "";           		//주상품가입여부 시 리턴 상품ID
	private String testSbc          = "";           		//주상품가입여부 시 리턴 TEST_SBC
	
	//2019.10.21 김병권
	private String tempUflixProdYn 	= "";
	private String currentCount		= "";
	
	//2020.03.18 - 모바일 아이들나라 (상세 진입하지 않고, 구매하는 Scene으로 인해 부가세 요율 전달)
	private String surtaxRate 	= "";
	
	// 2020.11.09 - Seamless 단방향
	private String genreSmall	= "";
	
	// 2021.02.24 - 모바일TV 기능개선 4차수 (평생소장 콘텐츠는 인앱정보 제공하지 않기 위해 평생소장 콘텐츠인지 확인하기 위해 변수 추가)
	private String maxViewingLenCon	= "";			// 가용시간
	
	private List<ComCpnVO>	cpnInfo	= null;
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(StringUtil.nullToSpace(this.resultType)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.contsId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.contsType)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.buyYn)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.buyingDate)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.expireDate)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.price)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.pvodProdId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.pvodProdName)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.inappProdId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.inappPrice)).append(ImcsConstants.COLSEP);		
		sb.append(StringUtil.nullToSpace(this.nScreenYn)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.nBuyYn)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.nBuyDate)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.nExpiredDate)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.nSaId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.nStbMac)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.watchDate)).append(ImcsConstants.COLSEP);
//		sb.append(StringUtil.nullToSpace(this.tempUflixProdYn)).append(ImcsConstants.COLSEP);
//		sb.append(this.surtaxRate == null || this.surtaxRate.equals("") ? "10" : this.surtaxRate).append(ImcsConstants.COLSEP);

		return sb.toString();
	}


	public String getResultType() {
		return resultType;
	}


	public void setResultType(String resultType) {
		this.resultType = resultType;
	}


	public String getContsId() {
		return contsId;
	}


	public void setContsId(String contsId) {
		this.contsId = contsId;
	}


	public String getContsType() {
		return StringUtil.nullToSpace(contsType);
	}


	public void setContsType(String contsType) {
		this.contsType = contsType;
	}


	public String getBuyYn() {
		return StringUtil.nullToSpace(buyYn);
	}


	public void setBuyYn(String buyYn) {
		this.buyYn = buyYn;
	}


	public String getBuyingDate() {
		return buyingDate;
	}


	public void setBuyingDate(String buyingDate) {
		this.buyingDate = buyingDate;
	}


	public String getExpireDate() {
		return expireDate;
	}


	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}


	public String getPrice() {
		return price;
	}


	public void setPrice(String price) {
		this.price = price;
	}
	
	
	public String getPvodProdId() {
		return pvodProdId;
	}


	public void setPvodProdId(String pvodProdId) {
		this.pvodProdId = pvodProdId;
	}


	public String getPvodProdName() {
		return pvodProdName;
	}


	public void setPvodProdName(String pvodProdName) {
		this.pvodProdName = pvodProdName;
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
	
	
	public String getReservedDate() {
		return reservedDate;
	}


	public void setReservedDate(String reservedDate) {
		this.reservedDate = reservedDate;
	}


	public String getPreviewFlag() {
		return previewFlag;
	}


	public void setPreviewFlag(String previewFlag) {
		this.previewFlag = previewFlag;
	}


	public String getnScreenYnTmp() {
		return nScreenYnTmp;
	}


	public void setnScreenYnTmp(String nScreenYnTmp) {
		this.nScreenYnTmp = nScreenYnTmp;
	}


	public String getnScreenProd() {
		return nScreenProd;
	}


	public void setnScreenProd(String nScreenProd) {
		this.nScreenProd = nScreenProd;
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


	public String getMaxViewingLen() {
		return maxViewingLen;
	}


	public void setMaxViewingLen(String maxViewingLen) {
		this.maxViewingLen = maxViewingLen;
	}


	public String getUflixProdYn() {
		return uflixProdYn;
	}


	public void setUflixProdYn(String uflixProdYn) {
		this.uflixProdYn = uflixProdYn;
	}


	public String getContsNameSt() {
		return contsNameSt;
	}


	public void setContsNameSt(String contsNameSt) {
		this.contsNameSt = contsNameSt;
	}


	public String getGenreInfo() {
		return genreInfo;
	}


	public void setGenreInfo(String genreInfo) {
		this.genreInfo = genreInfo;
	}


	public String getCpPropertyBin() {
		return cpPropertyBin;
	}


	public void setCpPropertyBin(String cpPropertyBin) {
		this.cpPropertyBin = cpPropertyBin;
	}


	public List<ComCpnVO> getCpnInfo() {
		return cpnInfo;
	}


	public void setCpnInfo(List<ComCpnVO> cpnInfo) {
		this.cpnInfo = cpnInfo;
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


	public String getIptvProdChk() {
		return iptvProdChk;
	}


	public void setIptvProdChk(String iptvProdChk) {
		this.iptvProdChk = iptvProdChk;
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


	public String getmProdId() {
		return mProdId;
	}


	public void setmProdId(String mProdId) {
		this.mProdId = mProdId;
	}


	public String getTestSbc() {
		return testSbc;
	}


	public void setTestSbc(String testSbc) {
		this.testSbc = testSbc;
	}


	public String getIptvAlbumId() {
		return iptvAlbumId;
	}


	public void setIptvAlbumId(String iptvAlbumId) {
		this.iptvAlbumId = iptvAlbumId;
	}


	public String getTempUflixProdYn() {
		return tempUflixProdYn;
	}


	public void setTempUflixProdYn(String tempUflixProdYn) {
		this.tempUflixProdYn = tempUflixProdYn;
	}


	public String getCurrentCount() {
		return currentCount;
	}


	public void setCurrentCount(String currentCount) {
		this.currentCount = currentCount;
	}


	public String getSurtaxRate() {
		return surtaxRate;
	}


	public void setSurtaxRate(String surtaxRate) {
		this.surtaxRate = surtaxRate;
	}


	public String getGenreSmall() {
		return genreSmall;
	}


	public void setGenreSmall(String genreSmall) {
		this.genreSmall = genreSmall;
	}


	public String getMaxViewingLenCon() {
		return maxViewingLenCon;
	}


	public void setMaxViewingLenCon(String maxViewingLenCon) {
		this.maxViewingLenCon = maxViewingLenCon;
	}


	public String getWatchDate() {
		return watchDate;
	}


	public void setWatchDate(String watchDate) {
		this.watchDate = watchDate;
	}

	
	public String getSubsProdId() {
		return subsProdId;
	}


	public void setSubsProdId(String subsProdId) {
		this.subsProdId = subsProdId;
	}


	public String getSubsProdName() {
		return subsProdName;
	}


	public void setSubsProdName(String subsProdName) {
		this.subsProdName = subsProdName;
	}


	public String getDatafreeBuyYn() {
		return datafreeBuyYn;
	}


	public void setDatafreeBuyYn(String datafreeBuyYn) {
		this.datafreeBuyYn = datafreeBuyYn;
	}


	public String getExpiredDate() {
		return expiredDate;
	}


	public void setExpiredDate(String expiredDate) {
		this.expiredDate = expiredDate;
	}


	public String getPayInfo() {
		return payInfo;
	}


	public void setPayInfo(String payInfo) {
		this.payInfo = payInfo;
	}
	
}
