package kr.co.wincom.imcs.api.getNSDMPurDtl;

import java.io.Serializable;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.ComCpnVO;

public class GetNSDMPurDtlResponseVO implements Serializable
{
	private static final long serialVersionUID = 170116096565178079L;
	//= 무중단 일경우 : SUBSCRIPTION_YN == ""
	
	private String contsId						= "";			// 컨텐츠 ID - PKG 일때 PKG_ID
	private String buyingDate					= "";			// 구매날짜
	private String buyingType					= "";			// 컨텐츠 결제 수단 ( N:일반, A:인앱, W:페이나우, T:신용카드 )
	private String suggestedPrice				= "";			// 컨텐츠 제공 가격
	private String buyingPrice					= "";			// 컨텐츠 구매 금액
	private String datafreeBuyingType			= "";			// 데이터 프리 결제 수단
	private String datafreeSuggestedPrice		= "";			// 데이터 프리 제공 가격
	private String datafreeBuyingPrice			= "";			// 데이터 프리 구매 가격
	private String discountCoupon				= "";			// 쿠폰 할인 금액
	private String discountMembership			= "";			// 멤버쉽 할인 금액
	private String discountTvpoint				= "";			// TV포인트 할인 금액
	private String discountKlupoint				= "";			// KLU 할인 금액
	private String datafreeDiscountCoupon		= "";			// 데이터 프리 쿠폰 할인 금액
	private String datafreeDiscountMembership	= "";			// 데이터 프리 멤버쉽 할인 금액
	private String datafreeDiscountTvpoint		= "";			// 데이터 프리 TV포인트 할인 금액
	private String datafreeDiscountKlupoint		= "";			// 데이터 프리 KLU 할인 금액
	private String realSuggestedPrice			= "";			// 부가세 포함 컨텐츠 실구매금액
	private String realDatafreeSuggestedPrice	= "";			// 부가세 포함 데이터 프리 실구매금액
	
	/********************************************************************
	 * 추가 사용되는 속성값  start
	 ********************************************************************/
	
	private String productId					= "";
	private String dmUseFlag					= "";
	private String dmPrice						= "";
	private String buyAmt						= "";
	private String realPrice					= "";
	
	//2021.04.07 아이돌 라이브 유료콘서트
	private String cancelLinkUrl				= "";
	private String idolBuyYn					= "";

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(StringUtil.nullToSpace(this.contsId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.buyingDate)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.buyingType)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.suggestedPrice)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.buyingPrice)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.datafreeBuyingType)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.datafreeSuggestedPrice)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.datafreeBuyingPrice)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.discountCoupon)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.discountMembership)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.discountTvpoint)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.discountKlupoint)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.datafreeDiscountCoupon)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.datafreeDiscountMembership)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.datafreeDiscountTvpoint)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.datafreeDiscountKlupoint)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.realDatafreeSuggestedPrice)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.realSuggestedPrice)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.cancelLinkUrl)).append(ImcsConstants.COLSEP);

		return sb.toString();
	}

	public String getContsId() {
		return contsId;
	}

	public void setContsId(String contsId) {
		this.contsId = contsId;
	}

	public String getBuyingDate() {
		return buyingDate;
	}

	public void setBuyingDate(String buyingDate) {
		this.buyingDate = buyingDate;
	}

	public String getBuyingType() {
		return buyingType;
	}

	public void setBuyingType(String buyingType) {
		this.buyingType = buyingType;
	}

	public String getSuggestedPrice() {
		return suggestedPrice;
	}

	public void setSuggestedPrice(String suggestedPrice) {
		this.suggestedPrice = suggestedPrice;
	}

	public String getBuyingPrice() {
		return buyingPrice;
	}

	public void setBuyingPrice(String buyingPrice) {
		this.buyingPrice = buyingPrice;
	}

	public String getDatafreeBuyingType() {
		return datafreeBuyingType;
	}

	public void setDatafreeBuyingType(String datafreeBuyingType) {
		this.datafreeBuyingType = datafreeBuyingType;
	}

	public String getDatafreeSuggestedPrice() {
		return datafreeSuggestedPrice;
	}

	public void setDatafreeSuggestedPrice(String datafreeSuggestedPrice) {
		this.datafreeSuggestedPrice = datafreeSuggestedPrice;
	}

	public String getDatafreeBuyingPrice() {
		return datafreeBuyingPrice;
	}

	public void setDatafreeBuyingPrice(String datafreeBuyingPrice) {
		this.datafreeBuyingPrice = datafreeBuyingPrice;
	}

	public String getDiscountCoupon() {
		return discountCoupon;
	}

	public void setDiscountCoupon(String discountCoupon) {
		this.discountCoupon = discountCoupon;
	}

	public String getDiscountMembership() {
		return discountMembership;
	}

	public void setDiscountMembership(String discountMembership) {
		this.discountMembership = discountMembership;
	}

	public String getDiscountTvpoint() {
		return discountTvpoint;
	}

	public void setDiscountTvpoint(String discountTvpoint) {
		this.discountTvpoint = discountTvpoint;
	}

	public String getDiscountKlupoint() {
		return discountKlupoint;
	}

	public void setDiscountKlupoint(String discountKlupoint) {
		this.discountKlupoint = discountKlupoint;
	}

	public String getDatafreeDiscountCoupon() {
		return datafreeDiscountCoupon;
	}

	public void setDatafreeDiscountCoupon(String datafreeDiscountCoupon) {
		this.datafreeDiscountCoupon = datafreeDiscountCoupon;
	}

	public String getDatafreeDiscountMembership() {
		return datafreeDiscountMembership;
	}

	public void setDatafreeDiscountMembership(String datafreeDiscountMembership) {
		this.datafreeDiscountMembership = datafreeDiscountMembership;
	}

	public String getDatafreeDiscountTvpoint() {
		return datafreeDiscountTvpoint;
	}

	public void setDatafreeDiscountTvpoint(String datafreeDiscountTvpoint) {
		this.datafreeDiscountTvpoint = datafreeDiscountTvpoint;
	}

	public String getDatafreeDiscountKlupoint() {
		return datafreeDiscountKlupoint;
	}

	public void setDatafreeDiscountKlupoint(String datafreeDiscountKlupoint) {
		this.datafreeDiscountKlupoint = datafreeDiscountKlupoint;
	}

	public String getRealSuggestedPrice() {
		return realSuggestedPrice;
	}

	public void setRealSuggestedPrice(String realSuggestedPrice) {
		this.realSuggestedPrice = realSuggestedPrice;
	}

	public String getRealDatafreeSuggestedPrice() {
		return realDatafreeSuggestedPrice;
	}

	public void setRealDatafreeSuggestedPrice(String realDatafreeSuggestedPrice) {
		this.realDatafreeSuggestedPrice = realDatafreeSuggestedPrice;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getDmUseFlag() {
		return dmUseFlag;
	}

	public void setDmUseFlag(String dmUseFlag) {
		this.dmUseFlag = dmUseFlag;
	}

	public String getDmPrice() {
		return dmPrice;
	}

	public void setDmPrice(String dmPrice) {
		this.dmPrice = dmPrice;
	}

	public String getBuyAmt() {
		return buyAmt;
	}

	public void setBuyAmt(String buyAmt) {
		this.buyAmt = buyAmt;
	}

	public String getRealPrice() {
		return realPrice;
	}

	public void setRealPrice(String realPrice) {
		this.realPrice = realPrice;
	}

	public String getCancelLinkUrl() {
		return cancelLinkUrl;
	}

	public void setCancelLinkUrl(String cancelLinkUrl) {
		this.cancelLinkUrl = cancelLinkUrl;
	}

	public String getIdolBuyYn() {
		return idolBuyYn;
	}

	public void setIdolBuyYn(String idolBuyYn) {
		this.idolBuyYn = idolBuyYn;
	}

	
}
