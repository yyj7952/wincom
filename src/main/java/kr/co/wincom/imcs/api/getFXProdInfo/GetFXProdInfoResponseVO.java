package kr.co.wincom.imcs.api.getFXProdInfo;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;

@SuppressWarnings("serial")
public class GetFXProdInfoResponseVO implements Serializable {
	/********************************************************************
	 * GetNSProdinfo API 전문 칼럼(순서 일치)
	********************************************************************/
    private String subYn			= "";
    private String subProdId		= "";
    private String subProdName		= "";
    private String subProdPrice		= "";
    private String subProdSub		= "";
    private String subProdIsu		= "";
    private String subProdType		= "";
    private String subProdPriority	= "";
    private String prodType			= "";
    private String entrydate		= "";
    private String ucubeProdCd		= "";	// U-cube 상품코드
    private String subProdDesc		= "";
    private String screen			= "";
    private String imgUrl			= "";
    private String imgFileName		= "";
    
    
    /********************************************************************
   	 * 추가 사용 파라미터
   	********************************************************************/
    private String bgnDate			= "";
    private String endDate			= "";
    
    //2018.12.06 - 신규 유플릭스 상품 추가
    private String uflixPop			= "";
    private String uflixPopMsg		= "";
    private String uflixProdYn		= "";
    private String concurrentCount = "";
    
    @Override
    public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(StringUtil.nullToSpace(this.subYn)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.subProdId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.subProdName)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.subProdPrice)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.subProdSub)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.subProdIsu)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.subProdType)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.subProdPriority)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.prodType)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.entrydate)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.ucubeProdCd)).append(ImcsConstants.COLSEP);
		sb.append(this.subProdDesc).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.screen)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.imgUrl)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.imgFileName)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.concurrentCount)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.uflixPop)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.uflixPopMsg)).append(ImcsConstants.COLSEP);
		return sb.toString();
    }
    
    
    
	public String getSubYn() {
		return subYn;
	}
	public void setSubYn(String subYn) {
		this.subYn = subYn;
	}
	public String getSubProdId() {
		return subProdId;
	}
	public void setSubProdId(String subProdId) {
		this.subProdId = subProdId;
	}
	public String getSubProdName() {
		return subProdName;
	}
	public void setSubProdName(String subProdName) {
		this.subProdName = subProdName;
	}
	public String getSubProdPrice() {
		return subProdPrice;
	}
	public void setSubProdPrice(String subProdPrice) {
		this.subProdPrice = subProdPrice;
	}
	public String getSubProdSub() {
		return subProdSub;
	}
	public void setSubProdSub(String subProdSub) {
		this.subProdSub = subProdSub;
	}
	public String getSubProdIsu() {
		return subProdIsu;
	}
	public void setSubProdIsu(String subProdIsu) {
		this.subProdIsu = subProdIsu;
	}
	public String getSubProdType() {
		return subProdType;
	}
	public void setSubProdType(String subProdType) {
		this.subProdType = subProdType;
	}
	public String getSubProdPriority() {
		return subProdPriority;
	}
	public void setSubProdPriority(String subProdPriority) {
		this.subProdPriority = subProdPriority;
	}
	public String getProdType() {
		return prodType;
	}
	public void setProdType(String prodType) {
		this.prodType = prodType;
	}
	public String getEntrydate() {
		return entrydate;
	}
	public void setEntrydate(String entrydate) {
		this.entrydate = entrydate;
	}
	public String getSubProdDesc() {
		return subProdDesc;
	}
	public void setSubProdDesc(String subProdDesc) {
		this.subProdDesc = subProdDesc;
	}
	public String getScreen() {
		return screen;
	}
	public void setScreen(String screen) {
		this.screen = screen;
	}
	public String getUcubeProdCd() {
		return ucubeProdCd;
	}
	public void setUcubeProdCd(String ucubeProdCd) {
		this.ucubeProdCd = ucubeProdCd;
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
	public String getBgnDate() {
		return bgnDate;
	}
	public void setBgnDate(String bgnDate) {
		this.bgnDate = bgnDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}



	public String getUflixPop() {
		return uflixPop;
	}



	public void setUflixPop(String uflixPop) {
		this.uflixPop = uflixPop;
	}



	public String getUflixPopMsg() {
		return uflixPopMsg;
	}



	public void setUflixPopMsg(String uflixPopMsg) {
		this.uflixPopMsg = uflixPopMsg;
	}



	public String getUflixProdYn() {
		return uflixProdYn;
	}



	public void setUflixProdYn(String uflixProdYn) {
		this.uflixProdYn = uflixProdYn;
	}



	public String getConcurrentCount() {
		return concurrentCount;
	}



	public void setConcurrentCount(String concurrentCount) {
		this.concurrentCount = concurrentCount;
	}
}
