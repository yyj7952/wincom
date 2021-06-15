package kr.co.wincom.imcs.api.buyNSContent;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.ComCpnVO;
import kr.co.wincom.imcs.common.vo.StatVO;

import org.apache.commons.lang.StringUtils;


@SuppressWarnings("serial")
public class BuyNSContentResultVO extends StatVO implements Serializable {

	private String flag		= "";
	private String errMsg	= "";
	private String errCode	= "";
    private String result = "";
    private String buyingDate = "";
    private ComCpnVO cpnInfoVO	= null;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		StringBuffer tmpsndbuf = new StringBuffer();
		if(this.cpnInfoVO == null)	cpnInfoVO = new ComCpnVO();
			    
	    tmpsndbuf.append(StringUtils.defaultString(this.getFlag(), "")).append(ImcsConstants.COLSEP);
	    tmpsndbuf.append(StringUtils.defaultString(this.getErrMsg(), "")).append(ImcsConstants.COLSEP);
	    tmpsndbuf.append(StringUtils.defaultString(this.buyingDate, "")).append(ImcsConstants.COLSEP);
	    tmpsndbuf.append(StringUtils.defaultString(this.getErrCode(), "")).append(ImcsConstants.COLSEP)
	    .append(ImcsConstants.ROWSEP);
	    tmpsndbuf.append(cpnInfoVO.getStmInfo()).append(ImcsConstants.ROWSEP);
	    tmpsndbuf.append(cpnInfoVO.getCpnInfo()).append(ImcsConstants.ROWSEP);	    
	    tmpsndbuf.append(cpnInfoVO.getUseCpnInfo()).append(ImcsConstants.ROWSEP);

		
		//sb.append(this.getResult());
		
		return tmpsndbuf.toString();
	}
	
	public String cacheString()
	{
		StringBuffer sb = new StringBuffer();
		
		return sb.toString();
	}

	public String getBuyingDate() {
		return buyingDate;
	}

	public void setBuyingDate(String buyingDate) {
		this.buyingDate = buyingDate;
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

	public ComCpnVO getCpnInfoVO() {
		return cpnInfoVO;
	}

	public void setCpnInfoVO(ComCpnVO cpnInfoVO) {
		this.cpnInfoVO = cpnInfoVO;
	}
    
}
