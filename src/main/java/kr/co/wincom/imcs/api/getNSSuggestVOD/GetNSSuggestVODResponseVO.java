package kr.co.wincom.imcs.api.getNSSuggestVOD;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;

import org.apache.commons.lang.StringUtils;


@SuppressWarnings("serial")
public class GetNSSuggestVODResponseVO implements Serializable {
	private String contsId 					= "";	// 앨범 ID
    private String contsName 				= "";	// 앨범이름
    private String imgUrl 					= "";	// 포스터 URL
    private String imgFileName 				= "";	// 포스터 이미지 파일명
    private String chaNum 					= "";	// 채널번호
    private String albumBillFlag			= "";
    private String prInfo 					= "";
    private String runTime 					= "";
    private String is51ch 					= "";
    private String isNew 					= "";
    private String isHot					= "";
    private String isCaption 				= "";	// CaptionYn
    private String isHd			 			= "";
    private String point 					= "";
    private String is3d 					= "";
    private String catName 					= "";
    private String onairDate 				= "";
    private String seriesDesc 				= "";
    private String terrYn 					= "";
    private String seriesYn 				= "";
    private String posterImgUrl				= "";
    private String serCatId					= "";
    private String realHDYn 				= "";
    private String suggestedPrice 			= "";
    
    
    /********************************************************************
	 * 추가 사용되는 속성값  start
	 ********************************************************************/
    private String catId 					= "";	
    private String hdContent 				= "";
    private String productType 				= "";
    private String suggestedPriceYn 		= "";
    private String price	 				= "";
    
    private long sortNo;
    
    
    
    

    @Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(StringUtils.defaultString(this.contsId, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.contsName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.imgUrl, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.imgFileName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.chaNum, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.albumBillFlag, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.prInfo, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.runTime, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.is51ch, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.isNew, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.isHot, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.isCaption, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.isHd, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.point, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.is3d, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.catName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.onairDate, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.seriesDesc, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.terrYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.seriesYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.posterImgUrl, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.serCatId, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.realHDYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.suggestedPrice, "")).append(ImcsConstants.COLSEP);
		return sb.toString();
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




	public String getChaNum() {
		return chaNum;
	}




	public void setChaNum(String chaNum) {
		this.chaNum = chaNum;
	}




	public String getPrice() {
		return price;
	}




	public void setPrice(String price) {
		this.price = price;
	}




	public String getPrInfo() {
		return prInfo;
	}




	public void setPrInfo(String prInfo) {
		this.prInfo = prInfo;
	}




	public String getRunTime() {
		return runTime;
	}




	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}




	public String getIs51ch() {
		return is51ch;
	}




	public void setIs51ch(String is51ch) {
		this.is51ch = is51ch;
	}




	public String getIsNew() {
		return isNew;
	}




	public void setIsNew(String isNew) {
		this.isNew = isNew;
	}




	public String getIsHot() {
		return isHot;
	}




	public void setIsHot(String isHot) {
		this.isHot = isHot;
	}




	public String getIsCaption() {
		return isCaption;
	}




	public void setIsCaption(String isCaption) {
		this.isCaption = isCaption;
	}




	public String getIsHd() {
		return isHd;
	}




	public void setIsHd(String isHd) {
		this.isHd = isHd;
	}




	public String getPoint() {
		return point;
	}




	public void setPoint(String point) {
		this.point = point;
	}




	public String getIs3d() {
		return is3d;
	}




	public void setIs3d(String is3d) {
		this.is3d = is3d;
	}




	public String getCatName() {
		return catName;
	}




	public void setCatName(String catName) {
		this.catName = catName;
	}




	public String getOnairDate() {
		return onairDate;
	}




	public void setOnairDate(String onairDate) {
		this.onairDate = onairDate;
	}




	public String getSeriesDesc() {
		return seriesDesc;
	}




	public void setSeriesDesc(String seriesDesc) {
		this.seriesDesc = seriesDesc;
	}




	public String getTerrYn() {
		return terrYn;
	}




	public void setTerrYn(String terrYn) {
		this.terrYn = terrYn;
	}




	public String getSeriesYn() {
		return seriesYn;
	}




	public void setSeriesYn(String seriesYn) {
		this.seriesYn = seriesYn;
	}




	public String getPosterImgUrl() {
		return posterImgUrl;
	}




	public void setPosterImgUrl(String posterImgUrl) {
		this.posterImgUrl = posterImgUrl;
	}




	public String getRealHDYn() {
		return realHDYn;
	}




	public void setRealHDYn(String realHDYn) {
		this.realHDYn = realHDYn;
	}




	public String getSuggestedPrice() {
		return suggestedPrice;
	}




	public void setSuggestedPrice(String suggestedPrice) {
		this.suggestedPrice = suggestedPrice;
	}




	public String getSerCatId() {
		return serCatId;
	}




	public void setSerCatId(String serCatId) {
		this.serCatId = serCatId;
	}




	public String getCatId() {
		return catId;
	}




	public void setCatId(String catId) {
		this.catId = catId;
	}




	public String getHdContent() {
		return hdContent;
	}




	public void setHdContent(String hdContent) {
		this.hdContent = hdContent;
	}




	public String getProductType() {
		return productType;
	}




	public void setProductType(String productType) {
		this.productType = productType;
	}




	public String getSuggestedPriceYn() {
		return suggestedPriceYn;
	}




	public void setSuggestedPriceYn(String suggestedPriceYn) {
		this.suggestedPriceYn = suggestedPriceYn;
	}




	public long getSortNo() {
		return sortNo;
	}




	public void setSortNo(long sortNo) {
		this.sortNo = sortNo;
	}




	public String getAlbumBillFlag() {
		return albumBillFlag;
	}




	public void setAlbumBillFlag(String albumBillFlag) {
		this.albumBillFlag = albumBillFlag;
	}
}
