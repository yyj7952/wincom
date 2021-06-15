package kr.co.wincom.imcs.api.buyNSDMConts;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.ComCpnVO;
import kr.co.wincom.imcs.common.vo.StatVO;

import org.apache.commons.lang.StringUtils;


@SuppressWarnings("serial")
public class BuyNSDMContsResultVO extends StatVO implements Serializable {

	private String flag	= "";
	private String errMsg	= "";
	private String errCode	= "";
	private String buyingDate	= "";
	private ComCpnVO cpnInfoVO	= null;

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if(this.cpnInfoVO == null)	cpnInfoVO = new ComCpnVO();
		
		sb.append(StringUtils.defaultString(this.flag, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.errMsg, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.buyingDate, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.errCode, "")).append(ImcsConstants.COLSEP);
		sb.append(ImcsConstants.ROWSEP);
		sb.append(cpnInfoVO.getStmInfo()).append(ImcsConstants.ROWSEP);
		sb.append(cpnInfoVO.getCpnInfo()).append(ImcsConstants.ROWSEP);	    
	    sb.append(cpnInfoVO.getUseCpnInfo()).append(ImcsConstants.ROWSEP);

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

	public void setBuyingDate(String buyingDate) {
		this.buyingDate = buyingDate;
	}
	
	public ComCpnVO getCpnInfoVO() {
		return cpnInfoVO;
	}

	public void setCpnInfoVO(ComCpnVO cpnInfoVO) {
		this.cpnInfoVO = cpnInfoVO;
	}

}
