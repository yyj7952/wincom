package kr.co.wincom.imcs.api.buyNSPresent;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.StatVO;

import org.apache.commons.lang.StringUtils;


@SuppressWarnings("serial")
public class BuyNSPresentResultVO extends StatVO implements Serializable {
	
	private String flag		= "";
	private String errMsg	= "";
	private String errCode	= "";
	private String buyingDate	= "";
	
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
			    
		sb.append(StringUtils.defaultString(this.flag, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.errMsg, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.buyingDate, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.errCode, "")).append(ImcsConstants.COLSEP);
	    sb.append(ImcsConstants.ROWSEP);

		return sb.toString();
	}



	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
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

	public void setBuyingdate(String buyingDate) {
		this.buyingDate = buyingDate;
	}
	
    
}
