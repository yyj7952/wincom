package kr.co.wincom.imcs.api.addNSCHFavor;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.StatVO;

import org.apache.commons.lang.StringUtils;


@SuppressWarnings("serial")
public class AddNSCHFavorResultVO extends StatVO implements Serializable {

	private String flag		= "";
	private String errMsg	= "";
	private String errCode	= "";

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
	
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		    
		sb.append(StringUtils.defaultString(this.getFlag(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getErrMsg(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getErrCode(), "")).append(ImcsConstants.COLSEP);
	    sb.append(ImcsConstants.ROWSEP);
		
		return sb.toString();
	}
}
