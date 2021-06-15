package kr.co.wincom.imcs.api.getNSSeriesStat;

import java.io.Serializable;


import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;


@SuppressWarnings("serial")
public class getNSSeriesStatResponseVO extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * getNSMultiView API 전문 칼럼(순서 일치)
	********************************************************************/
    
    private String albumId = "";
    private String subcriptionYn = "";
    private String isSvodOnly = "";
    private String isBuy = "";
    private String dataFreeBuy = "";
    private String buyDate = "";
    private String buyExpired = "";
    private String watchYn = "";
    private String linkTime = "";
    private String nscreenYn = "";
    private String nscreenType = "";
    private String nbuyYn = "";
    private String nbuyDate = "";
    private String nexpirdDate = "";
    private String nsubscribeYn = "";
    private String viewingLength = "";
    private String genreSmall = "";
    
    private String svodYn = "N";
    
    /********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
    
    private String productTypeMin = "";
    private String productTypeMax = "";
    private String nproductTypeMin = "";
    private String nproductTypeMax = "";
    private String resultType = "";
    
    
    private String cpPropertyBin = "";
    
    private String nscInfo		= "";
    
    @Override
	public String toString() {
    	StringBuffer sb = new StringBuffer();
		
    	sb.append(StringUtil.replaceNull(this.resultType, "ALB")).append(ImcsConstants.COLSEP);
    	sb.append(StringUtil.replaceNull(this.albumId, "")).append(ImcsConstants.COLSEP);
    	sb.append(StringUtil.replaceNull(this.subcriptionYn, "")).append(ImcsConstants.COLSEP);
    	sb.append(StringUtil.replaceNull(this.isSvodOnly, "")).append(ImcsConstants.COLSEP);
    	sb.append(StringUtil.replaceNull(this.isBuy, "")).append(ImcsConstants.COLSEP);
    	sb.append(StringUtil.replaceNull(this.dataFreeBuy, "")).append(ImcsConstants.COLSEP);
    	sb.append(StringUtil.replaceNull(this.buyDate, "")).append(ImcsConstants.COLSEP);
    	sb.append(StringUtil.replaceNull(this.buyExpired, "")).append(ImcsConstants.COLSEP);
    	sb.append(StringUtil.replaceNull(this.watchYn, "")).append(ImcsConstants.COLSEP);
    	sb.append(StringUtil.replaceNull(this.linkTime, "")).append(ImcsConstants.COLSEP);
    	sb.append(StringUtil.replaceNull(this.nscreenYn, "")).append(ImcsConstants.COLSEP);
    	sb.append(StringUtil.replaceNull(this.nbuyYn, "")).append(ImcsConstants.COLSEP);
    	sb.append(StringUtil.replaceNull(this.nbuyDate, "")).append(ImcsConstants.COLSEP);
    	sb.append(StringUtil.replaceNull(this.nexpirdDate, "")).append(ImcsConstants.COLSEP);
    	sb.append(StringUtil.replaceNull(this.nsubscribeYn, "")).append(ImcsConstants.COLSEP);
    	sb.append(StringUtil.replaceNull(this.viewingLength, "")).append(ImcsConstants.COLSEP);
    	sb.append(StringUtil.replaceNull(this.svodYn, "")).append(ImcsConstants.COLSEP);
    	
		return sb.toString();
	}

	public String getAlbumId() {
		return albumId;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}

	public String getSubcriptionYn() {
		return subcriptionYn;
	}

	public void setSubcriptionYn(String subcriptionYn) {
		this.subcriptionYn = subcriptionYn;
	}

	public String getIsSvodOnly() {
		return isSvodOnly;
	}

	public void setIsSvodOnly(String isSvodOnly) {
		this.isSvodOnly = isSvodOnly;
	}

	public String getIsBuy() {
		return isBuy;
	}

	public void setIsBuy(String isBuy) {
		this.isBuy = isBuy;
	}

	public String getDataFreeBuy() {
		return dataFreeBuy;
	}

	public void setDataFreeBuy(String dataFreeBuy) {
		this.dataFreeBuy = dataFreeBuy;
	}

	public String getBuyDate() {
		return buyDate;
	}

	public void setBuyDate(String buyDate) {
		this.buyDate = buyDate;
	}

	public String getBuyExpired() {
		return buyExpired;
	}

	public void setBuyExpired(String buyExpired) {
		this.buyExpired = buyExpired;
	}

	public String getWatchYn() {
		return watchYn;
	}

	public void setWatchYn(String watchYn) {
		this.watchYn = watchYn;
	}

	public String getLinkTime() {
		return linkTime;
	}

	public void setLinkTime(String linkTime) {
		this.linkTime = linkTime;
	}

	public String getNscreenYn() {
		return nscreenYn;
	}

	public void setNscreenYn(String nscreenYn) {
		this.nscreenYn = nscreenYn;
	}

	public String getNbuyYn() {
		return nbuyYn;
	}

	public void setNbuyYn(String nbuyYn) {
		this.nbuyYn = nbuyYn;
	}

	public String getNbuyDate() {
		return nbuyDate;
	}

	public void setNbuyDate(String nbuyDate) {
		this.nbuyDate = nbuyDate;
	}

	public String getNexpirdDate() {
		return nexpirdDate;
	}

	public void setNexpirdDate(String nexpirdDate) {
		this.nexpirdDate = nexpirdDate;
	}

	public String getNsubscribeYn() {
		return nsubscribeYn;
	}

	public void setNsubscribeYn(String nsubscribeYn) {
		this.nsubscribeYn = nsubscribeYn;
	}

	public String getViewingLength() {
		return viewingLength;
	}

	public void setViewingLength(String viewingLength) {
		this.viewingLength = viewingLength;
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

	public String getNproductTypeMin() {
		return nproductTypeMin;
	}

	public void setNproductTypeMin(String nproductTypeMin) {
		this.nproductTypeMin = nproductTypeMin;
	}

	public String getNproductTypeMax() {
		return nproductTypeMax;
	}

	public void setNproductTypeMax(String nproductTypeMax) {
		this.nproductTypeMax = nproductTypeMax;
	}

	public String getResultType() {
		return resultType;
	}

	public void setResultType(String resultType) {
		this.resultType = resultType;
	}

	public String getNscInfo() {
		return nscInfo;
	}

	public void setNscInfo(String nscInfo) {
		this.nscInfo = nscInfo;
	}

	public String getSvodYn() {
		return svodYn;
	}

	public void setSvodYn(String svodYn) {
		this.svodYn = svodYn;
	}

	public String getGenreSmall() {
		return genreSmall;
	}

	public void setGenreSmall(String genreSmall) {
		this.genreSmall = genreSmall;
	}

	public String getNscreenType() {
		return nscreenType;
	}

	public void setNscreenType(String nscreenType) {
		this.nscreenType = nscreenType;
	}

}
