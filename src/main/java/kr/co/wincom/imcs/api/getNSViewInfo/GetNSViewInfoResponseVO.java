package kr.co.wincom.imcs.api.getNSViewInfo;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;


@SuppressWarnings("serial")
public class GetNSViewInfoResponseVO extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * getNSViewInfo API 전문 칼럼(순서 일치)
	********************************************************************/
    
    private String catId			= "";
    private String contsId			= "";
    private String contsName 		= "";
    private String buyingDate 		= "";
    private String runTime 			= "";
    private String linkTime 		= "";
    private String seriesYn 		= "";
    private String seriesNo 		= "";
    private String nscSaId 			= "";
    private String nscMacAddr 		= "";
    private String nscreenYn		= "";
    
    /********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/

    
    private String buyFlag 		= "";
    private String bookYn		= "";
    
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(StringUtil.nullToSpace(this.catId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.contsId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.contsName)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.buyingDate)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.runTime)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.linkTime)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.seriesYn)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.seriesNo)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.nscSaId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.nscMacAddr)).append(ImcsConstants.COLSEP);
		
		return sb.toString();
	}

	public String getCatId() {
		return catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
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

	public String getBuyingDate() {
		return buyingDate;
	}

	public void setBuyingDate(String buyingDate) {
		this.buyingDate = buyingDate;
	}

	public String getRunTime() {
		return runTime;
	}

	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}

	public String getLinkTime() {
		return linkTime;
	}

	public void setLinkTime(String linkTime) {
		this.linkTime = linkTime;
	}

	public String getSeriesYn() {
		return seriesYn;
	}

	public void setSeriesYn(String seriesYn) {
		this.seriesYn = seriesYn;
	}

	public String getSeriesNo() {
		return seriesNo;
	}

	public void setSeriesNo(String seriesNo) {
		this.seriesNo = seriesNo;
	}

	public String getNscSaId() {
		return nscSaId;
	}

	public void setNscSaId(String nscSaId) {
		this.nscSaId = nscSaId;
	}

	public String getNscMacAddr() {
		return nscMacAddr;
	}

	public void setNscMacAddr(String nscMacAddr) {
		this.nscMacAddr = nscMacAddr;
	}

	public String getBuyFlag() {
		return buyFlag;
	}

	public void setBuyFlag(String buyFlag) {
		this.buyFlag = buyFlag;
	}

	public String getNscreenYn() {
		return nscreenYn;
	}

	public void setNscreenYn(String nscreenYn) {
		this.nscreenYn = nscreenYn;
	}

	public String getBookYn() {
		return bookYn;
	}

	public void setBookYn(String bookYn) {
		this.bookYn = bookYn;
	}


    
    
}
