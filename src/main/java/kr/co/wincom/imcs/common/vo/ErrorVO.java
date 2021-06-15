package kr.co.wincom.imcs.common.vo;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;

@SuppressWarnings("serial")
public class ErrorVO implements Serializable {
	private String flag = "";
	private String errCode	= "";
	private String errMsg	= "";

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

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		
		if( !"".equals(this.flag) && this.flag != null )
			sb.append(StringUtil.replaceNull(this.flag,"")).append(ImcsConstants.COLSEP);
		
		if( !"".equals(this.errCode) && this.flag != null )
			sb.append(StringUtil.replaceNull(this.errCode,"")).append(ImcsConstants.COLSEP);
		
		if( !"".equals(this.errMsg) && this.flag != null )
			sb.append(StringUtil.replaceNull(this.errMsg,""));
		
		return sb.toString();
	}
	
}
