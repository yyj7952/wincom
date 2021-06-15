package kr.co.wincom.imcs.api.getNSMainPromo;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;

public class GetNSMainPromoResponseVO implements Serializable
{
	private static final long serialVersionUID = 170116096565178079L;
	//= 무중단 일경우 : SUBSCRIPTION_YN == ""
	
	private String type			= "";
	private String mainImgUrl	= "";
	private String thumbImgUrl	= "";
	private String catId		= "";
	private String contsId		= "";
	private String contsName	= "";
	private String contsDesc	= "";
	private String chaNum		= "";
	private String prInfo		= "";
	private String billFlag		= "";
	private String notiMessage	= "";
	
	/********************************************************************
	 * 추가 사용되는 속성값  start
	 ********************************************************************/
	private String resultCode	= "";
	
	private String rowNum		= "";
	private String pmoIdx		= "";
	private String imgType		= "";
	private String expiredDate	= "";
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		
		if("A".equals(this.type) && "1".equals(this.rowNum) && !"M".equals(this.imgType)){
			sb.append(ImcsConstants.COLSEP);
			sb.append(ImcsConstants.COLSEP);
			sb.append(ImcsConstants.COLSEP);
			sb.append(ImcsConstants.COLSEP);
			sb.append(ImcsConstants.COLSEP);
			sb.append(ImcsConstants.COLSEP);
			sb.append(ImcsConstants.COLSEP);
			sb.append(ImcsConstants.COLSEP);
			sb.append(ImcsConstants.COLSEP);
			sb.append(ImcsConstants.ROWSEP);
		}else{
			sb.append(StringUtil.nullToSpace(this.type)).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.mainImgUrl)).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.thumbImgUrl)).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.catId)).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.contsId)).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.contsName)).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.contsDesc)).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.chaNum)).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.prInfo)).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.billFlag)).append(ImcsConstants.ROWSEP);
		}
		
		//sb.append(StringUtil.nullToSpace(this.notiMessage)).append(ImcsConstants.COLSEP);
				
		return sb.toString();
	}



	public String getResultCode() {
		return resultCode;
	}
	
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public String getMainImgUrl() {
		return mainImgUrl;
	}



	public void setMainImgUrl(String mainImgUrl) {
		this.mainImgUrl = mainImgUrl;
	}



	public String getThumbImgUrl() {
		return thumbImgUrl;
	}



	public void setThumbImgUrl(String thumbImgUrl) {
		this.thumbImgUrl = thumbImgUrl;
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



	public String getContsDesc() {
		return contsDesc;
	}



	public void setContsDesc(String contsDesc) {
		this.contsDesc = contsDesc;
	}



	public String getChaNum() {
		return chaNum;
	}



	public void setChaNum(String chaNum) {
		this.chaNum = chaNum;
	}



	public String getPrInfo() {
		return prInfo;
	}



	public void setPrInfo(String prInfo) {
		this.prInfo = prInfo;
	}



	public String getBillFlag() {
		return billFlag;
	}



	public void setBillFlag(String billFlag) {
		this.billFlag = billFlag;
	}



	public String getNotiMessage() {
		return notiMessage;
	}



	public void setNotiMessage(String notiMessage) {
		this.notiMessage = notiMessage;
	}



	public String getRowNum() {
		return rowNum;
	}



	public void setRowNum(String rowNum) {
		this.rowNum = rowNum;
	}



	public String getPmoIdx() {
		return pmoIdx;
	}



	public void setPmoIdx(String pmoIdx) {
		this.pmoIdx = pmoIdx;
	}



	public String getImgType() {
		return imgType;
	}



	public void setImgType(String imgType) {
		this.imgType = imgType;
	}



	public String getExpiredDate() {
		return expiredDate;
	}



	public void setExpiredDate(String expiredDate) {
		this.expiredDate = expiredDate;
	}

	
	
}
