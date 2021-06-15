package kr.co.wincom.imcs.api.getNSMainPage;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;


@SuppressWarnings("serial")
public class GetNSMainPageMainVO extends NoSqlLoggingVO implements Serializable {
	
	private String genreGb			= "";
	private String catId			= "";
	private String catName			= "";
	private String catType			= "";
	private String imgUrl			= "";
	private String imgFileName		= "";
	private String parentCatId		= "";		// 카테고리ID (연동규격서 상 CatID)
	private String authYn			= "";
	private String chaNum			= "";
	private String catLevel			= "";
	private String price			= "";		// billFlag
	private String parentCatYn		= "";
	private String relayView		= "";
	private int nSubCnt;
	private String closeYn			= "";
	private String svodYn			= "";
	private String ppsYn			= "";
	private String categoryDesc		= "";
	private String isOrder			= "";
	private String noCache			= "";
	private String focusFileName	= "";
	private String normalFileName	= "";
	private String selectFileName	= "";
	private String catFileName		= "";
	private String ppmYn			= "";
	private String ppmProdId		= "";
	private String terrCh			= "";

	private String resultType		= "";
	private String contsCnt			= "";
	
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		this.catFileName	= focusFileName + "\b" + normalFileName + "\b" + selectFileName;
		
		sb.append("CAT").append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.genreGb, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.catId, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.catName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.catType, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.imgUrl, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.imgFileName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.parentCatId, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.authYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.chaNum, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.catLevel, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.price, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.parentCatYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.relayView, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(String.valueOf(this.nSubCnt), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.closeYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.svodYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.ppsYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.categoryDesc, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.isOrder, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.noCache, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.catFileName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.ppmYn, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.ppmProdId, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.terrCh, "")).append(ImcsConstants.COLSEP);
		sb.append(ImcsConstants.ROWSEP);
		
		return sb.toString();
	}
		

	public String getGenreGb() {
		return genreGb;
	}

	public void setGenreGb(String genreGb) {
		this.genreGb = genreGb;
	}

	public String getCatId() {
		return catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public String getCatType() {
		return catType;
	}

	public void setCatType(String catType) {
		this.catType = catType;
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

	public String getParentCatId() {
		return parentCatId;
	}

	public void setParentCatId(String parentCatId) {
		this.parentCatId = parentCatId;
	}

	public String getAuthYn() {
		return authYn;
	}

	public void setAuthYn(String authYn) {
		this.authYn = authYn;
	}

	public String getChaNum() {
		return chaNum;
	}

	public void setChaNum(String chaNum) {
		this.chaNum = chaNum;
	}

	public String getCatLevel() {
		return catLevel;
	}

	public void setCatLevel(String catLevel) {
		this.catLevel = catLevel;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getParentCatYn() {
		return parentCatYn;
	}

	public void setParentCatYn(String parentCatYn) {
		this.parentCatYn = parentCatYn;
	}

	public String getRelayView() {
		return relayView;
	}

	public void setRelayView(String relayView) {
		this.relayView = relayView;
	}

	public int getnSubCnt() {
		return nSubCnt;
	}

	public void setnSubCnt(int nSubCnt) {
		this.nSubCnt = nSubCnt;
	}

	public String getCloseYn() {
		return closeYn;
	}

	public void setCloseYn(String closeYn) {
		this.closeYn = closeYn;
	}

	public String getSvodYn() {
		return svodYn;
	}

	public void setSvodYn(String svodYn) {
		this.svodYn = svodYn;
	}

	public String getPpsYn() {
		return ppsYn;
	}

	public void setPpsYn(String ppsYn) {
		this.ppsYn = ppsYn;
	}

	public String getCategoryDesc() {
		return categoryDesc;
	}

	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}

	public String getIsOrder() {
		return isOrder;
	}

	public void setIsOrder(String isOrder) {
		this.isOrder = isOrder;
	}

	public String getNoCache() {
		return noCache;
	}

	public void setNoCache(String noCache) {
		this.noCache = noCache;
	}

	public String getFocusFileName() {
		return focusFileName;
	}

	public void setFocusFileName(String focusFileName) {
		this.focusFileName = focusFileName;
	}

	public String getNormalFileName() {
		return normalFileName;
	}

	public void setNormalFileName(String normalFileName) {
		this.normalFileName = normalFileName;
	}

	public String getSelectFileName() {
		return selectFileName;
	}

	public void setSelectFileName(String selectFileName) {
		this.selectFileName = selectFileName;
	}

	public String getCatFileName() {
		return catFileName;
	}

	public void setCatFileName(String catFileName) {
		this.catFileName = catFileName;
	}

	public String getPpmYn() {
		return ppmYn;
	}

	public void setPpmYn(String ppmYn) {
		this.ppmYn = ppmYn;
	}

	public String getPpmProdId() {
		return ppmProdId;
	}

	public void setPpmProdId(String ppmProdId) {
		this.ppmProdId = ppmProdId;
	}

	public String getTerrCh() {
		return terrCh;
	}

	public void setTerrCh(String terrCh) {
		this.terrCh = terrCh;
	}


	public String getResultType() {
		return resultType;
	}


	public void setResultType(String resultType) {
		this.resultType = resultType;
	}


	public String getContsCnt() {
		return contsCnt;
	}


	public void setContsCnt(String contsCnt) {
		this.contsCnt = contsCnt;
	}
	
	

	
	
}
