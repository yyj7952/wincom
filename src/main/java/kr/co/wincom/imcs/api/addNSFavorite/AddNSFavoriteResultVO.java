package kr.co.wincom.imcs.api.addNSFavorite;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.StatVO;

import org.apache.commons.lang.StringUtils;

@SuppressWarnings("serial")
public class AddNSFavoriteResultVO extends StatVO implements Serializable {
	
	private String flag;
	private String errCode;
	private String errMsg;
	

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
		
		//공통 메시지부
		sb.append(StringUtils.defaultString(this.getFlag(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getErrMsg(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getErrCode(), "")).append(ImcsConstants.COLSEP);

		sb.append(ImcsConstants.ROWSEP);
		
		return sb.toString();
	}
}
