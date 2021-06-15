package kr.co.wincom.imcs.api.rmNSAlert;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.StatVO;

import org.apache.commons.lang.StringUtils;

public class RmNSAlertResultVO extends StatVO implements Serializable
{
	
	private static final long serialVersionUID = 170116096565178079L;
	
	private String flag;
	private String errMsg;
	

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


	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		
		//공통 메시지부
		if( !"".equals(this.getFlag())) {
			sb.append(StringUtils.defaultString(this.flag, "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtils.defaultString(this.errMsg, "")).append(ImcsConstants.COLSEP);
			sb.append(ImcsConstants.ROWSEP);
		}
		
		return sb.toString();
	}

}
