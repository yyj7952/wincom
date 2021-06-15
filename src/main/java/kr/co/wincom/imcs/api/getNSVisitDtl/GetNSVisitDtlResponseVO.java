package kr.co.wincom.imcs.api.getNSVisitDtl;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;


@SuppressWarnings("serial")
public class GetNSVisitDtlResponseVO extends NoSqlLoggingVO implements Serializable {
	
	/********************************************************************
	 * getNSVisitDtl API 전문 칼럼(순서 일치)
	********************************************************************/
	private String imgUrl		= "";
	private String imgFileName	= "";
	private String vodServer1	= "";
	private String vodServer2	= "";
	private String vodServer3	= "";
	private String vodFileName1 = "";
	private String vodFileName2 = "";
	private String vodFileName3 = "";
	private String downYn		= "";
	private String promotionCopy	= "";
	private String latitude		= "";
	private String longitude	= "";
	private String telNo		= "";
	private String visitDesc	= "";
    private String storeName	= "";
    private String stillFileName	= "";
    private String dataFreeBillFlag	= "";
    
    /********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
    private String productId	= "";
    private String assetId		= "";
    
    
    @Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(StringUtil.nullToSpace(this.imgUrl)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.imgFileName)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.vodServer1)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.vodFileName1)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.vodServer2)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.vodFileName2)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.vodServer3)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.vodFileName3)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.downYn)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.promotionCopy)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.latitude)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.longitude)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.telNo)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.visitDesc)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.storeName)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.stillFileName)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.dataFreeBillFlag)).append(ImcsConstants.COLSEP);
		
		return sb.toString();
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
	public String getVodServer1() {
		return vodServer1;
	}
	public void setVodServer1(String vodServer1) {
		this.vodServer1 = vodServer1;
	}
	public String getVodServer2() {
		return vodServer2;
	}
	public void setVodServer2(String vodServer2) {
		this.vodServer2 = vodServer2;
	}
	public String getVodServer3() {
		return vodServer3;
	}
	public void setVodServer3(String vodServer3) {
		this.vodServer3 = vodServer3;
	}
	public String getVodFileName1() {
		return vodFileName1;
	}
	public void setVodFileName1(String vodFileName1) {
		this.vodFileName1 = vodFileName1;
	}
	public String getVodFileName2() {
		return vodFileName2;
	}
	public void setVodFileName2(String vodFileName2) {
		this.vodFileName2 = vodFileName2;
	}
	public String getVodFileName3() {
		return vodFileName3;
	}
	public void setVodFileName3(String vodFileName3) {
		this.vodFileName3 = vodFileName3;
	}
	public String getDownYn() {
		return downYn;
	}
	public void setDownYn(String downYn) {
		this.downYn = downYn;
	}
	public String getPromotionCopy() {
		return promotionCopy;
	}
	public void setPromotionCopy(String promotionCopy) {
		this.promotionCopy = promotionCopy;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getTelNo() {
		return telNo;
	}
	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}
	public String getVisitDesc() {
		return visitDesc;
	}
	public void setVisitDesc(String visitDesc) {
		this.visitDesc = visitDesc;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getStillFileName() {
		return stillFileName;
	}
	public void setStillFileName(String stillFileName) {
		this.stillFileName = stillFileName;
	}
	public String getDataFreeBillFlag() {
		return dataFreeBillFlag;
	}
	public void setDataFreeBillFlag(String dataFreeBillFlag) {
		this.dataFreeBillFlag = dataFreeBillFlag;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getAssetId() {
		return assetId;
	}
	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}
 
    
   
    
}
