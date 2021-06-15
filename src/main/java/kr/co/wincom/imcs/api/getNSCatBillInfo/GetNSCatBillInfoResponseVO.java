package kr.co.wincom.imcs.api.getNSCatBillInfo;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;


@SuppressWarnings("serial")
public class GetNSCatBillInfoResponseVO extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * getNSMultiView API 전문 칼럼(순서 일치)
	********************************************************************/
    private String albumId = "";
    private String albumName = "";
    private String subcriptionYn = "";
    private String isSvodOnly = "";
    private String buyYn = "";
    private String price = "";
    private String inappPrice = "";
    private String surtaxRate = "";
    
    /********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
    
    private String productTypeMin = "";
    private String productTypeMax = "";
    private String cpPropertyBin = "";
    
    @Override
	public String toString() {
    	StringBuffer sb = new StringBuffer();
		
		sb.append(StringUtil.replaceNull(this.albumId, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.albumName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.subcriptionYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.isSvodOnly, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.buyYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.price, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.inappPrice, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.surtaxRate, "")).append(ImcsConstants.COLSEP);
		
		return sb.toString();
	}

	public String getAlbumId() {
		return albumId;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}

	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public String getSubcriptionYn() {
		return subcriptionYn;
	}

	public void setSubcriptionYn(String subcriptionYn) {
		this.subcriptionYn = subcriptionYn;
	}

	public String getBuyYn() {
		return buyYn;
	}

	public void setBuyYn(String buyYn) {
		this.buyYn = buyYn;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getInappPrice() {
		return inappPrice;
	}

	public void setInappPrice(String inappPrice) {
		this.inappPrice = inappPrice;
	}

	public String getSurtaxRate() {
		return surtaxRate;
	}

	public void setSurtaxRate(String surtaxRate) {
		this.surtaxRate = surtaxRate;
	}

	public String getIsSvodOnly() {
		return isSvodOnly;
	}

	public void setIsSvodOnly(String isSvodOnly) {
		this.isSvodOnly = isSvodOnly;
	}

	public String getProductTypeMin() {
		return productTypeMin;
	}

	public void setProductTypeMin(String productTypeMin) {
		this.productTypeMin = productTypeMin;
	}
	
	

	public String getProductTypeMax() {
		return productTypeMax;
	}

	public void setProductTypeMax(String productTypeMax) {
		this.productTypeMax = productTypeMax;
	}

	public String getCpPropertyBin() {
		return cpPropertyBin;
	}

	public void setCpPropertyBin(String cpPropertyBin) {
		this.cpPropertyBin = cpPropertyBin;
	}

}
