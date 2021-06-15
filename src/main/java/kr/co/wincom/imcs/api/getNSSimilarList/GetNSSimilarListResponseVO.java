package kr.co.wincom.imcs.api.getNSSimilarList;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;


@SuppressWarnings("serial")
public class GetNSSimilarListResponseVO extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * getNSSimilarList API 전문 칼럼(순서 일치)
	********************************************************************/
	private String contsId		= "";
	private String contsName	= "";
	private String imgUrl		= "";
    private String imgFileName	= "";
    private String chaNum		= "";
    private String price		= "";
    private String prInfo		= "";
    private String runtime		= "";
    private String is51Ch		= "";
    private String isNew		= "";
    private String isBest		= "";
    private String isCaption	= "";
    private String ishd			= "";
    private String point		= "";
    private String is3d			= "";
    private String catName		= "";
    private String onairDate	= "";
    private String seriesDesc	= "";
    private String terrYn		= "";
    private String seriesYn		= "";
    private String imgSvcUrl	= "";
    private String seriesCatId	= "";
    private String realHDYn		= "";
    private String suggestedPrice	= "";
    
    @Override
    public String toString(){
    	StringBuffer sb = new StringBuffer();
    	
    	sb.append(StringUtil.nullToSpace(this.contsId)).append(ImcsConstants.COLSEP);
    	sb.append(StringUtil.nullToSpace(this.contsName)).append(ImcsConstants.COLSEP);
    	sb.append(StringUtil.nullToSpace(this.imgUrl)).append(ImcsConstants.COLSEP);
    	sb.append(StringUtil.nullToSpace(this.imgFileName)).append(ImcsConstants.COLSEP);
    	sb.append(StringUtil.nullToSpace(this.chaNum)).append(ImcsConstants.COLSEP);
    	sb.append(StringUtil.nullToSpace(this.price)).append(ImcsConstants.COLSEP);
    	sb.append(StringUtil.nullToSpace(this.prInfo)).append(ImcsConstants.COLSEP);
    	sb.append(StringUtil.nullToSpace(this.runtime)).append(ImcsConstants.COLSEP);
    	sb.append(StringUtil.nullToSpace(this.is51Ch)).append(ImcsConstants.COLSEP);
    	sb.append(StringUtil.nullToSpace(this.isNew)).append(ImcsConstants.COLSEP);
    	sb.append(StringUtil.nullToSpace(this.isBest)).append(ImcsConstants.COLSEP);
    	sb.append(StringUtil.nullToSpace(this.isCaption)).append(ImcsConstants.COLSEP);
    	sb.append(StringUtil.nullToSpace(this.ishd)).append(ImcsConstants.COLSEP);
    	sb.append(StringUtil.nullToSpace(this.point)).append(ImcsConstants.COLSEP);
    	sb.append(StringUtil.nullToSpace(this.is3d)).append(ImcsConstants.COLSEP);
    	sb.append(StringUtil.nullToSpace(this.catName)).append(ImcsConstants.COLSEP);
    	sb.append(StringUtil.nullToSpace(this.onairDate)).append(ImcsConstants.COLSEP);
    	sb.append(StringUtil.nullToSpace(this.seriesDesc)).append(ImcsConstants.COLSEP);
    	sb.append(StringUtil.nullToSpace(this.terrYn)).append(ImcsConstants.COLSEP);
    	sb.append(StringUtil.nullToSpace(this.seriesYn)).append(ImcsConstants.COLSEP);
    	sb.append(StringUtil.nullToSpace(this.imgSvcUrl)).append(ImcsConstants.COLSEP);
    	sb.append(StringUtil.nullToSpace(this.seriesCatId)).append(ImcsConstants.COLSEP);
    	sb.append(StringUtil.nullToSpace(this.realHDYn)).append(ImcsConstants.COLSEP);
    	sb.append(StringUtil.nullToSpace(this.suggestedPrice)).append(ImcsConstants.COLSEP);

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
	public String getRuntime() {
		return runtime;
	}
	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}
	public String getIs51Ch() {
		return is51Ch;
	}
	public void setIs51Ch(String is51Ch) {
		this.is51Ch = is51Ch;
	}
	public String getIsNew() {
		return isNew;
	}
	public void setIsNew(String isNew) {
		this.isNew = isNew;
	}
	public String getIsBest() {
		return isBest;
	}
	public void setIsBest(String isBest) {
		this.isBest = isBest;
	}
	public String getIsCaption() {
		return isCaption;
	}
	public void setIsCaption(String isCaption) {
		this.isCaption = isCaption;
	}
	public String getIshd() {
		return ishd;
	}
	public void setIshd(String ishd) {
		this.ishd = ishd;
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
	public String getImgSvcUrl() {
		return imgSvcUrl;
	}
	public void setImgSvcUrl(String imgSvcUrl) {
		this.imgSvcUrl = imgSvcUrl;
	}
	public String getSeriesCatId() {
		return seriesCatId;
	}
	public void setSeriesCatId(String seriesCatId) {
		this.seriesCatId = seriesCatId;
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
}
