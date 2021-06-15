package kr.co.wincom.imcs.api.getNSProdinfo;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;


@SuppressWarnings("serial")
public class GetNSProdinfoResponseVO implements Serializable {
	/********************************************************************
	 * GetNSProdinfo API 전문 칼럼(순서 일치)
	********************************************************************/
    private String subYn					= "";
    private String subProdId				= "";
    private String subProdName				= "";
    private String subProdPrice				= "";
    private String subProdSub				= "";
    private String subProdIsu				= "";
    private String subProdType				= "";
    private String subProdPriority			= "";
    private String prodType					= "";
    private String entrydate				= "";
    private String ucubeProdCd				= "";	// U-cube 상품코드
    private String subProdDesc				= "";
    private String uflixYn					= "";
    private String screen					= "";
    private String imgUrl					= "";
    private String imgFileName				= "";
    private String prodDetailDesc			= "";
    private String customProdName			= "";
    private String rate						= "";
    private String cancelPossibleYn			= "";
    private String groupCd					= "";
    private String groupNm					= "";
    private String surtaxRate				= "";
    private String memDeductionYn			= "";
    private String memDeductionPrice		= "";
    private String memDeductionPriceLimit	= "";
    
    //2018.12.06 - 신규 유플릭스 상품 추가
    private String concurrentCount			= "";
    private String prodDelFlag				= "";
    private String uflixPop					= "";
    private String uflixPopMsg				= "";
    
    private String mimsPanelId				= "";
    
    private String memProdCd			= "";
    
    //2021.01.05 - 모바일 아이들나라4.0 Phase2 - IPTV가입상품 조회
    private String screenInfo			= "";
    
    /********************************************************************
   	 * 추가 사용 파라미터
   	********************************************************************/
    private String useScreenMobile	= "";
    private String expiredYn		= "";
    private String viewCtrl			= "";
    private String prodSubYn		= "";
    private String nscProdKind		= "";
    private String atrctChnlDvCd	= "";
    
    /*
    private String returnChk		= "";
    private String prodDetailDesc	= "";
    private String customProdName	= "";
    private String rate	= "";
    private String cancelPossYn	= "";
    private String groupCd	= "";
    private String groupNm	= "";
    
    private String atrctChnlDvCd = "";
    
    private String sortNo = "";
    */
    
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
		sb.append(StringUtil.nullToSpace(this.subProdDesc)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.uflixYn)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.screen)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.imgUrl)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.imgFileName)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.prodDetailDesc)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.customProdName)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.rate)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.expiredYn)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.groupCd)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.groupNm)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.surtaxRate)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.memDeductionYn)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.memDeductionPrice)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.memDeductionPriceLimit)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.mimsPanelId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.concurrentCount)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.uflixPop)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.uflixPopMsg)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.screenInfo)).append(ImcsConstants.COLSEP);
		
		
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
	public String getUflixYn() {
		return uflixYn;
	}
	public void setUflixYn(String uflixYn) {
		this.uflixYn = uflixYn;
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
	public String getUseScreenMobile() {
		return useScreenMobile;
	}
	public void setUseScreenMobile(String useScreenMobile) {
		this.useScreenMobile = useScreenMobile;
	}
	public String getExpiredYn() {
		return expiredYn;
	}
	public void setExpiredYn(String expiredYn) {
		this.expiredYn = expiredYn;
	}
	public String getViewCtrl() {
		return viewCtrl;
	}
	public void setViewCtrl(String viewCtrl) {
		this.viewCtrl = viewCtrl;
	}
	public String getProdSubYn() {
		return prodSubYn;
	}
	public void setProdSubYn(String prodSubYn) {
		this.prodSubYn = prodSubYn;
	}
	public String getNscProdKind() {
		return nscProdKind;
	}
	public void setNscProdKind(String nscProdKind) {
		this.nscProdKind = nscProdKind;
	}



	public String getAtrctChnlDvCd() {
		return atrctChnlDvCd;
	}



	public void setAtrctChnlDvCd(String atrctChnlDvCd) {
		this.atrctChnlDvCd = atrctChnlDvCd;
	}



	public String getProdDetailDesc() {
		return prodDetailDesc;
	}



	public void setProdDetailDesc(String prodDetailDesc) {
		this.prodDetailDesc = prodDetailDesc;
	}



	public String getCustomProdName() {
		return customProdName;
	}



	public void setCustomProdName(String customProdName) {
		this.customProdName = customProdName;
	}



	public String getRate() {
		return rate;
	}



	public void setRate(String rate) {
		this.rate = rate;
	}



	public String getCancelPossibleYn() {
		return cancelPossibleYn;
	}



	public void setCancelPossibleYn(String cancelPossibleYn) {
		this.cancelPossibleYn = cancelPossibleYn;
	}



	public String getGroupCd() {
		return groupCd;
	}



	public void setGroupCd(String groupCd) {
		this.groupCd = groupCd;
	}



	public String getGroupNm() {
		return groupNm;
	}



	public void setGroupNm(String groupNm) {
		this.groupNm = groupNm;
	}



	public String getSurtaxRate() {
		return surtaxRate;
	}



	public void setSurtaxRate(String surtaxRate) {
		this.surtaxRate = surtaxRate;
	}



	public String getMemDeductionYn() {
		return memDeductionYn;
	}



	public void setMemDeductionYn(String memDeductionYn) {
		this.memDeductionYn = memDeductionYn;
	}



	public String getMemDeductionPrice() {
		return memDeductionPrice;
	}



	public void setMemDeductionPrice(String memDeductionPrice) {
		this.memDeductionPrice = memDeductionPrice;
	}



	public String getMemDeductionPriceLimit() {
		return memDeductionPriceLimit;
	}



	public void setMemDeductionPriceLimit(String memDeductionPriceLimit) {
		this.memDeductionPriceLimit = memDeductionPriceLimit;
	}



	public String getMemProdCd() {
		return memProdCd;
	}



	public void setMemProdCd(String memProdCd) {
		this.memProdCd = memProdCd;
	}



	public String getMimsPanelId() {
		return mimsPanelId;
	}



	public void setMimsPanelId(String mimsPanelId) {
		this.mimsPanelId = mimsPanelId;
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



	public String getConcurrentCount() {
		return concurrentCount;
	}



	public void setConcurrentCount(String concurrentCount) {
		this.concurrentCount = concurrentCount;
	}



	public String getProdDelFlag() {
		return prodDelFlag;
	}



	public void setProdDelFlag(String prodDelFlag) {
		this.prodDelFlag = prodDelFlag;
	}



	public String getScreenInfo() {
		return screenInfo;
	}



	public void setScreenInfo(String screenInfo) {
		this.screenInfo = screenInfo;
	}

	
	
}
