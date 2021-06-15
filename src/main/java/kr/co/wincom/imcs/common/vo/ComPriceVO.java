package kr.co.wincom.imcs.common.vo;

import java.io.Serializable;


@SuppressWarnings("serial")
public class ComPriceVO  implements Serializable {

    private String billType			= "";
    private String productType		= "";	// 2019.10.29 - NPT_VO_BUY_META 에도 사용
    private String distributor		= "";
    private String expritedDate		= "";
    private String eventValue		= "";
    private String eventPrice		= "";
    private String eventType		= "";
    private String suggestedPrice	= "";
    private String approvalPrice	= "";
    private String presentPrice		= "";
    private String presentRate		= "";
    private String contsGenre		= "";
    private String reservedPrice	= "";
    private String reservedDate		= "";
    private String previewFlag		= "";
    
    private String datafreeBillYn	= "";
    private String possessionYn		= "";
    
    private String reviewFlag 		= "";
    
  //2019.09.04 영화월정액 내재화 라이센스 만료된 컨텐츠 구매 불가 로직 구현을 위하여 추가
    private String licenseStart     = "";
    private String licenseEnd       = "";
    private String licensingValidYn = "";
    
    // 2019.10.29 - VOD 정산 프로세스 개선 : NPT_VO_BUY_META 테이블 넣는 변수 추가
    private String assetName 		= "";
    private String hdcontent 		= "";
    private String ratingCd 		= "";
    private String productId 		= "";
    private String productName 		= "";
    private String productKind 		= "";
    private String cpId 			= "";
    private String maximumViewingLength 		= "";
    private String seriesNo 		= "";
    
    // 2019.12.27 - 인앱 결제 가능 여부 추가
    private String inappBuyYn 		= "";
    // 2021.04.14 - 아이돌라이브 유로콘서트
    private String payFlag			= "";
    
	public String getBillType() {
		return billType;
	}
	public void setBillType(String billType) {
		this.billType = billType;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getDistributor() {
		return distributor;
	}
	public void setDistributor(String distributor) {
		this.distributor = distributor;
	}
	public String getExpritedDate() {
		return expritedDate;
	}
	public void setExpritedDate(String expritedDate) {
		this.expritedDate = expritedDate;
	}
	public String getEventValue() {
		return eventValue;
	}
	public void setEventValue(String eventValue) {
		this.eventValue = eventValue;
	}
	public String getEventPrice() {
		return eventPrice;
	}
	public void setEventPrice(String eventPrice) {
		this.eventPrice = eventPrice;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getSuggestedPrice() {
		return suggestedPrice;
	}
	public void setSuggestedPrice(String suggestedPrice) {
		this.suggestedPrice = suggestedPrice;
	}
	public String getApprovalPrice() {
		return approvalPrice;
	}
	public void setApprovalPrice(String approvalPrice) {
		this.approvalPrice = approvalPrice;
	}
	public String getPresentPrice() {
		return presentPrice;
	}
	public void setPresentPrice(String presentPrice) {
		this.presentPrice = presentPrice;
	}
	public String getPresentRate() {
		return presentRate;
	}
	public void setPresentRate(String presentRate) {
		this.presentRate = presentRate;
	}
	public String getContsGenre() {
		return contsGenre;
	}
	public void setContsGenre(String contsGenre) {
		this.contsGenre = contsGenre;
	}
	public String getReservedPrice() {
		return reservedPrice;
	}
	public void setReservedPrice(String reservedPrice) {
		this.reservedPrice = reservedPrice;
	}
	public String getReservedDate() {
		return reservedDate;
	}
	public void setReservedDate(String reservedDate) {
		this.reservedDate = reservedDate;
	}
	public String getPossessionYn() {
		return possessionYn;
	}
	public void setPossessionYn(String possessionYn) {
		this.possessionYn = possessionYn;
	}
	public String getDatafreeBillYn() {
		return datafreeBillYn;
	}
	public void setDatafreeBillYn(String datafreeBillYn) {
		this.datafreeBillYn = datafreeBillYn;
	}
	public String getReviewFlag() {
		return reviewFlag;
	}
	public void setReviewFlag(String reviewFlag) {
		this.reviewFlag = reviewFlag;
	}
	public String getPreviewFlag() {
		return previewFlag;
	}
	public void setPreviewFlag(String previewFlag) {
		this.previewFlag = previewFlag;
	}
	public String getLicenseStart() {
		return licenseStart;
	}
	public void setLicenseStart(String licenseStart) {
		this.licenseStart = licenseStart;
	}
	public String getLicenseEnd() {
		return licenseEnd;
	}
	public void setLicenseEnd(String licenseEnd) {
		this.licenseEnd = licenseEnd;
	}
	public String getLicensingValidYn() {
		return licensingValidYn;
	}
	public void setLicensingValidYn(String licensingValidYn) {
		this.licensingValidYn = licensingValidYn;
	}
	public String getAssetName() {
		return assetName;
	}
	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}
	public String getHdcontent() {
		return hdcontent;
	}
	public void setHdcontent(String hdcontent) {
		this.hdcontent = hdcontent;
	}
	public String getRatingCd() {
		return ratingCd;
	}
	public void setRatingCd(String ratingCd) {
		this.ratingCd = ratingCd;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductKind() {
		return productKind;
	}
	public void setProductKind(String productKind) {
		this.productKind = productKind;
	}
	public String getCpId() {
		return cpId;
	}
	public void setCpId(String cpId) {
		this.cpId = cpId;
	}
	public String getMaximumViewingLength() {
		return maximumViewingLength;
	}
	public void setMaximumViewingLength(String maximumViewingLength) {
		this.maximumViewingLength = maximumViewingLength;
	}
	public String getSeriesNo() {
		return seriesNo;
	}
	public void setSeriesNo(String seriesNo) {
		this.seriesNo = seriesNo;
	}
	public String getInappBuyYn() {
		return inappBuyYn;
	}
	public void setInappBuyYn(String inappBuyYn) {
		this.inappBuyYn = inappBuyYn;
	}
	public String getPayFlag() {
		return payFlag;
	}
	public void setPayFlag(String payFlag) {
		this.payFlag = payFlag;
	}
    	
}
