package kr.co.wincom.imcs.api.getNSMakePrefIP;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;


@SuppressWarnings("serial")
public class GetNSMakePrefIPResponseVO extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * getNSMakePrefIP API 전문 칼럼(순서 일치)
	********************************************************************/
	private String resultValue		  = "";

    @Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(StringUtils.defaultString(this.getResultValue(), "")).append(ImcsConstants.COLSEP);

		return sb.toString();
	}

	public String getResultValue() {
		return resultValue;
	}

	public void setResultValue(String resultValue) {
		this.resultValue = resultValue;
	}
	
}
