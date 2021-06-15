package kr.co.wincom.imcs.api.chkSubscribeProd;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.StatVO;

import org.apache.commons.lang.StringUtils;

@SuppressWarnings("serial")
public class ChkSubscribeProdResultVO extends StatVO implements Serializable {
	
	private String flag		= "";
	private String errMsg	= "";
	private String prodId	= "";
	private String prodName = "";
	private String errCode	= "";
	private String buyingDate	= "";
	
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
			    
		sb.append(StringUtils.defaultString(this.getFlag(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getErrMsg(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getProdId(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getProdName(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getBuyingDate(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getErrCode(), "")).append(ImcsConstants.COLSEP);
	    sb.append(ImcsConstants.ROWSEP);

		return sb.toString();
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getBuyingDate() {
		return buyingDate;
	}

	public void setBuyingDate(String buyingDate) {
		this.buyingDate = buyingDate;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getProdId() {
		return prodId;
	}

	public void setProdId(String prodId) {
		this.prodId = prodId;
	}

	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}
	
}
