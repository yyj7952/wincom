package kr.co.wincom.imcs.api.getFXContStat;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;

@SuppressWarnings("serial")
public class GetFXContStatResponseVO implements Serializable {
	/********************************************************************
	 * GetFXContStat API 전문 칼럼(순서 일치)
	********************************************************************/
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
	private String terrYn			= "";			// 지상파컨텐츠여부 (0:지상파, 1:일반PPV)
	private String terrEdDate		= "";			// 지상파컨텐츠 유료종료일
	private String licensingWindowEnd	= "";		// 라이센스종료일
	private String price			= "";			// 구매가
	private String subsYn			= "";			// SVOD/PPM 가입여부 (0:가입, 1:미가입)
	private String subsProdId		= "";			// SVOD/PPM 상품코드
	private String subsProdName		= "";			// SVOD/PPM 상품명
	private String subsProdPrice	= "";			// SVOD/PPM 상품가
	private String subsProdSub		= "";			// SVOD/PPM 소속상품 설명
	private String subsProdIsu		= "";			// SVOD/PPM ISU가입 가능여부
	private String subsProdType		= "";			// SVOD/PPM 소속상품 ISU 타입
	private String buyText			= "";			// 구매하기 팝업 텍스트
	private String pvodProdName		= "";			// PVOD(시리즈/패키지) 상품명
	private String pvodProdDesc		= "";			// PVOD(시리즈/패키지) 상품설명
	private String appType			= "";			// 어플타입
	private String is51ch			= "";			// 51채널
	private String inHouse			= "";			// 댁내에서만 시청가능
	private String b2bCustomerYn	= "";			// B2B 가입여부
	private String premiumYn		= "N";			// 프리미엄상품 가입여부
	
	/********************************************************************
	 * 추가 사용되는 속성값  start
	 ********************************************************************/
	private String pid				= "";
	private String uflixProdYn		= "";
	private String albumType		= "";
	private String cpnNouseYn		= "";
	private String eventYn			= "";
	private String expiredDate		= "";
	private String ppmstopYn		= "";
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(StringUtil.nullToSpace(this.resultType)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.contsId)).append(ImcsConstants.COLSEP);
		sb.append(this.contsType).append(ImcsConstants.COLSEP);
		sb.append(this.contsGb).append(ImcsConstants.COLSEP);
		sb.append(this.buyYn).append(ImcsConstants.COLSEP);
		sb.append(this.buyingDate).append(ImcsConstants.COLSEP);
		sb.append(this.billType).append(ImcsConstants.COLSEP);
		sb.append(this.expireTime).append(ImcsConstants.COLSEP);	// expireTime
		sb.append(this.salePrice).append(ImcsConstants.COLSEP);
		sb.append(this.eventValue).append(ImcsConstants.COLSEP);
		sb.append(this.terrYn).append(ImcsConstants.COLSEP);
		sb.append(this.terrEdDate).append(ImcsConstants.COLSEP);
		sb.append(this.licensingWindowEnd).append(ImcsConstants.COLSEP);
		sb.append(this.price).append(ImcsConstants.COLSEP);
		sb.append(this.subsYn).append(ImcsConstants.COLSEP);
		sb.append(this.subsProdId).append(ImcsConstants.COLSEP);
		sb.append(this.subsProdName).append(ImcsConstants.COLSEP);
		sb.append(this.subsProdPrice).append(ImcsConstants.COLSEP);
		sb.append(this.subsProdSub).append(ImcsConstants.COLSEP);
		sb.append(this.subsProdIsu).append(ImcsConstants.COLSEP);
		sb.append(this.subsProdType).append(ImcsConstants.COLSEP);
		sb.append(this.buyText).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.pvodProdName)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.pvodProdDesc)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.appType)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.is51ch)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.inHouse)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.b2bCustomerYn)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.premiumYn)).append(ImcsConstants.COLSEP);
		
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
		return contsType;
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
		return buyYn;
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
	public String getAppType() {
		return appType;
	}
	public void setAppType(String appType) {
		this.appType = appType;
	}
	public String getIs51ch() {
		return is51ch;
	}
	public void setIs51ch(String is51ch) {
		this.is51ch = is51ch;
	}
	public String getInHouse() {
		return inHouse;
	}
	public void setInHouse(String inHouse) {
		this.inHouse = inHouse;
	}
	public String getB2bCustomerYn() {
		return b2bCustomerYn;
	}
	public void setB2bCustomerYn(String b2bCustomerYn) {
		this.b2bCustomerYn = b2bCustomerYn;
	}
	public String getPremiumYn() {
		return premiumYn;
	}
	public void setPremiumYn(String premiumYn) {
		this.premiumYn = premiumYn;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getUflixProdYn() {
		return uflixProdYn;
	}
	public void setUflixProdYn(String uflixProdYn) {
		this.uflixProdYn = uflixProdYn;
	}
	public String getAlbumType() {
		return albumType;
	}
	public void setAlbumType(String albumType) {
		this.albumType = albumType;
	}
	public String getCpnNouseYn() {
		return cpnNouseYn;
	}
	public void setCpnNouseYn(String cpnNouseYn) {
		this.cpnNouseYn = cpnNouseYn;
	}
	public String getEventYn() {
		return eventYn;
	}
	public void setEventYn(String eventYn) {
		this.eventYn = eventYn;
	}


	public String getExpiredDate() {
		return expiredDate;
	}


	public void setExpiredDate(String expiredDate) {
		this.expiredDate = expiredDate;
	}


	public String getPpmstopYn() {
		return ppmstopYn;
	}


	public void setPpmstopYn(String ppmstopYn) {
		this.ppmstopYn = ppmstopYn;
	}


	
	
}
