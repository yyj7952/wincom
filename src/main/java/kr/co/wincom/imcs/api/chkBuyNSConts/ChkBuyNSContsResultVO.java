package kr.co.wincom.imcs.api.chkBuyNSConts;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.StatVO;

import org.apache.commons.lang.StringUtils;

@SuppressWarnings("serial")
public class ChkBuyNSContsResultVO extends StatVO implements Serializable {
	
	private String flag		= "";
	private String errCode	= "";
	private String buyingDate	= "";
	
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
			    
		sb.append(StringUtils.defaultString(this.getFlag(), "")).append(ImcsConstants.COLSEP);
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
	
}
