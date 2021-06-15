package kr.co.wincom.imcs.api.getNSContStat;

import java.io.Serializable;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.ComCpnVO;

public class GetNSContStatResponseVO implements Serializable
{
	private static final long serialVersionUID = 170116096565178079L;
	//= 무중단 일경우 : SUBSCRIPTION_YN == ""
	
	private String resultType		= "CON";
	private String contsId			= "";			// 영화ID
	private String contsType		= "";			// 컨텐츠타입 (0:FVOD, 1:PPV, 2:PVOD, 3:SVOD, 7:PPM)
	private String contsGb			= "";			// 컨텐츠구분 (S:바로보기, D:다운로드)
	private String buyYn			= "";			// 구매여부 (0:구매, 1:미구매) - SVOD Only의 경우 사용 안함
	private String buyingDate		= "";			// 구매일자	- SVOD Only의 경우 사용 안함
	private String billType			= "";			// 구매타입 (0:정액, 1:종량) 
	private String expireTime		= "";			// 컨텐츠가용일(만료일)
	private String salePrice		= "";			// 할인가
	private String eventValue		= "";			// 이벤트할인율 (30, 10)
	private String eventYn			= "";			
	private String terrYn			= "";			// 지상파컨텐츠여부 (0:지상파, 1:일반PPV)
	private String terrEdDate		= "";			// 지상파컨텐츠 유료종료일
	private String licensingWindowEnd	= "";		// 라이센스종료일
	private String price			= "";			// 구매가
	private String subsYn			= "1";			// SVOD/PPM 가입여부 (0:가입, 1:미가입)
	private String subsProdId		= "";			// SVOD/PPM 상품코드
	private String subsProdName		= "";			// SVOD/PPM 상품명
	private String subsProdPrice	= "";			// SVOD/PPM 상품가
	private String subsProdSub		= "";			// SVOD/PPM 소속상품 설명
	private String subsProdIsu		= "";			// SVOD/PPM ISU가입 가능여부
	private String subsProdType		= "";			// SVOD/PPM 소속상품 ISU 타입
	private String buyText			= "";			// 구매하기 팝업 텍스트
	private String pvodProdName		= "";			// PVOD(시리즈/패키지) 상품명
	private String pvodProdDesc		= "";			// PVOD(시리즈/패키지) 상품설명
	private String pvodProdDisRate	= "";			// PVOD(시리즈/패키지) 상품할인율
	private String presentDisYn		= "";			// 선물구매여부
	private String presentDisRate	= "";			// 선물구매할인율
	private String inappProdId		= "";			// 인앱용 구매ID
	private String inappPrice		= "";			// 인앱구매가격
	private String expiredDate		= "";			// 상품만료시간 (일반구매:구매일자+가용시간, 예약구매:첫시청일+가용시간)
	private String premiumYn		= "";			// 프리미엄상품 가입여부
	private String presentYn		= "";			// 선물존재여부
	private String presentDate		= "";			// 선물받은 일자
	private String pExpiredDate		= "";			// 받은선물 유효기간
	private String datafreeBuyYn	= "";			// 데이터프리 구매여부
	private String datafreePrice	= "";			// 데이터프리 구매가격
	private String datafreeInappPrice	= "";		// 데이터프리인앱 구매가격
	private String datafreeInappProdId	= "";		// 데이터프리인앱 구매ID 
	private String ppvDatafreeInappPrice	= "";	// PPV+데이터프리인앱 구매가격
	private String ppvDatafreeInappProdId	= "";	// PPV+데이터프리인앱 구매ID
	private String favorYn			= "";			//
	private String reservedDate		= "";			//
	private String previewFlag		= "";			//
	private String nScreenYnTmp		= "";			// nScreen 함수 조회 정보
	private String nScreenYn		= "";			// nScreen 컨텐츠 여부
	private String nScreenType		= "";			// nScreen 컨텐츠일 때, 양방향 또는 단방향 타입
	private String nScreenProd		= "";			// nScreen 콘텐츠일 때, IPTV 상품 정보
	private String nBuyYn			= "";			// nScreen(STB) 가입자 컨텐츠 구매 여부
	private String nBuyDate			= "";			// 컨텐츠 구매 날짜
	private String nExpiredDate		= "";			// 컨텐츠 만료 날짜
	private String nSubscriptionYn	= "";			// nScreen(STB) 가입자 컨텐츠 가입 여부
	private String nSaId			= "";			// nScreen(STB) 가입자 번호
	private String nStbMac			= "";			// nScreen(STB) 가입자 맥주소
	
	
	/********************************************************************
	 * 추가 사용되는 속성값  start
	 ********************************************************************/
	private String resultCode		= "";
	
	private String maxViewingLen	= "";			// 가용시간
	private String cpNouseYn		= "";			// 쿠폰사용여부
	private String uflixProdYn		= "";			// 유플릭스 상품여부
	private String contsNameSt		= "";			// 컨텐츠명 - STAT용
	private String genreInfo		= "";			// 장르정보 - 쿠폰조회 시 사용
	private String cpPropertyBin	= "";			// 쿠폰?
	
	//2018.07.25 권형도
	private String iptvAlbumId      = "";
	private String iptvProdChk      = "";
	private String iptvTestSbc      = "";
	private String nscTestSbc       = "";
	private String mProdId          = "";           //주상품가입여부 시 리턴 상품ID
	private String testSbc          = "";           //주상품가입여부 시 리턴 TEST_SBC
	
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
		sb.append(StringUtil.nullToSpace(this.contsGb)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.buyYn)).append(ImcsConstants.COLSEP);
		sb.append(this.buyingDate).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.billType)).append(ImcsConstants.COLSEP);
		//sb.append(StringUtil.nullToSpace(this.maxViewingLen)).append(ImcsConstants.COLSEP);	// expireTime
		sb.append(this.maxViewingLen).append(ImcsConstants.COLSEP);	// expireTime
		//sb.append(StringUtil.nullToSpace(this.expireTime)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.salePrice)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.eventValue)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.terrYn)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.terrEdDate)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.licensingWindowEnd)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.price)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.subsYn)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.subsProdId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.subsProdName)).append(ImcsConstants.COLSEP);
		/*sb.append(StringUtil.nullToSpace(this.subsProdPrice)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.subsProdSub)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.subsProdIsu)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.subsProdType)).append(ImcsConstants.COLSEP);*/
		sb.append(this.subsProdPrice).append(ImcsConstants.COLSEP);
		sb.append(this.subsProdSub).append(ImcsConstants.COLSEP);
		sb.append(this.subsProdIsu).append(ImcsConstants.COLSEP);
		sb.append(this.subsProdType).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.buyText)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.pvodProdName)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.pvodProdDesc)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.pvodProdDisRate)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.presentDisYn)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.presentDisRate)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.inappProdId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.inappPrice)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.expiredDate)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.premiumYn)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.presentYn)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.presentDate)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.pExpiredDate)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.datafreeBuyYn)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.datafreePrice)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.datafreeInappPrice)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.datafreeInappProdId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.ppvDatafreeInappPrice)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.ppvDatafreeInappProdId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.favorYn)).append(ImcsConstants.COLSEP);
		
		sb.append(StringUtil.nullToSpace(this.nScreenYn)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.nBuyYn)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.nBuyDate)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.nExpiredDate)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.nSubscriptionYn)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.nSaId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.nStbMac)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.tempUflixProdYn)).append(ImcsConstants.COLSEP);
		sb.append(this.surtaxRate == null || this.surtaxRate.equals("") ? "10" : this.surtaxRate).append(ImcsConstants.COLSEP);

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


	public String getContsGb() {
		return contsGb;
	}


	public void setContsGb(String contsGb) {
		this.contsGb = contsGb;
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


	public String getBillType() {
		return billType;
	}


	public void setBillType(String billType) {
		this.billType = billType;
	}


	public String getExpireTime() {
		return expireTime;
	}


	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}


	public String getSalePrice() {
		return salePrice;
	}


	public void setSalePrice(String salePrice) {
		this.salePrice = salePrice;
	}


	public String getEventValue() {
		return eventValue;
	}


	public void setEventValue(String eventValue) {
		this.eventValue = eventValue;
	}

	

	public String getEventYn() {
		return eventYn;
	}


	public void setEventYn(String eventYn) {
		this.eventYn = eventYn;
	}


	public String getTerrYn() {
		return terrYn;
	}


	public void setTerrYn(String terrYn) {
		this.terrYn = terrYn;
	}


	public String getTerrEdDate() {
		return terrEdDate;
	}


	public void setTerrEdDate(String terrEdDate) {
		this.terrEdDate = terrEdDate;
	}


	public String getLicensingWindowEnd() {
		return licensingWindowEnd;
	}


	public void setLicensingWindowEnd(String licensingWindowEnd) {
		this.licensingWindowEnd = licensingWindowEnd;
	}


	public String getPrice() {
		return price;
	}


	public void setPrice(String price) {
		this.price = price;
	}


	public String getSubsYn() {
		return subsYn;
	}


	public void setSubsYn(String subsYn) {
		this.subsYn = subsYn;
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


	public String getSubsProdPrice() {
		return subsProdPrice;
	}


	public void setSubsProdPrice(String subsProdPrice) {
		this.subsProdPrice = subsProdPrice;
	}


	public String getSubsProdSub() {
		return subsProdSub;
	}


	public void setSubsProdSub(String subsProdSub) {
		this.subsProdSub = subsProdSub;
	}


	public String getSubsProdIsu() {
		return subsProdIsu;
	}


	public void setSubsProdIsu(String subsProdIsu) {
		this.subsProdIsu = subsProdIsu;
	}


	public String getSubsProdType() {
		return subsProdType;
	}


	public void setSubsProdType(String subsProdType) {
		this.subsProdType = subsProdType;
	}


	public String getBuyText() {
		return buyText;
	}


	public void setBuyText(String buyText) {
		this.buyText = buyText;
	}


	public String getPvodProdName() {
		return pvodProdName;
	}


	public void setPvodProdName(String pvodProdName) {
		this.pvodProdName = pvodProdName;
	}


	public String getPvodProdDesc() {
		return pvodProdDesc;
	}


	public void setPvodProdDesc(String pvodProdDesc) {
		this.pvodProdDesc = pvodProdDesc;
	}


	public String getPvodProdDisRate() {
		return pvodProdDisRate;
	}


	public void setPvodProdDisRate(String pvodProdDisRate) {
		this.pvodProdDisRate = pvodProdDisRate;
	}


	public String getPresentDisYn() {
		return presentDisYn;
	}


	public void setPresentDisYn(String presentDisYn) {
		this.presentDisYn = presentDisYn;
	}


	public String getPresentDisRate() {
		return presentDisRate;
	}


	public void setPresentDisRate(String presentDisRate) {
		this.presentDisRate = presentDisRate;
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


	public String getExpiredDate() {
		return expiredDate;
	}


	public void setExpiredDate(String expiredDate) {
		this.expiredDate = expiredDate;
	}


	public String getPremiumYn() {
		return premiumYn;
	}


	public void setPremiumYn(String premiumYn) {
		this.premiumYn = premiumYn;
	}


	public String getPresentYn() {
		return presentYn;
	}


	public void setPresentYn(String presentYn) {
		this.presentYn = presentYn;
	}


	public String getPresentDate() {
		return presentDate;
	}


	public void setPresentDate(String presentDate) {
		this.presentDate = presentDate;
	}


	public String getpExpiredDate() {
		return pExpiredDate;
	}


	public void setpExpiredDate(String pExpiredDate) {
		this.pExpiredDate = pExpiredDate;
	}


	public String getDatafreeBuyYn() {
		return datafreeBuyYn;
	}


	public void setDatafreeBuyYn(String datafreeBuyYn) {
		this.datafreeBuyYn = datafreeBuyYn;
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


	public String getCpNouseYn() {
		return cpNouseYn;
	}


	public void setCpNouseYn(String cpNouseYn) {
		this.cpNouseYn = cpNouseYn;
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


	public String getFavorYn() {
		return favorYn;
	}


	public void setFavorYn(String favorYn) {
		this.favorYn = favorYn;
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


	public String getnScreenYn() {
		return nScreenYn;
	}


	public void setnScreenYn(String nScreenYn) {
		this.nScreenYn = nScreenYn;
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


	public String getnSubscriptionYn() {
		return nSubscriptionYn;
	}


	public void setnSubscriptionYn(String nSubscriptionYn) {
		this.nSubscriptionYn = nSubscriptionYn;
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


	public String getnScreenType() {
		return nScreenType;
	}


	public void setnScreenType(String nScreenType) {
		this.nScreenType = nScreenType;
	}
	
}
