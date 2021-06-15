package kr.co.wincom.imcs.api.getNSMnuList2;

import java.io.Serializable;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.StatVO;


@SuppressWarnings("serial")
public class GetNSMnuList2ResultVO extends StatVO implements Serializable {

    private String result = "";
	private String resultHeader = "";
	private String endStr = "";

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		//this.result = result + ImcsConstants.ROWSEP;
		this.result = result;
	}

	public String getResultHeader() {
		return resultHeader;
	}

	public void setResultHeader(String resultHeader) {
		this.resultHeader = resultHeader;
	}

	public String getEndStr() {
		return endStr;
	}

	public void setEndStr(String endStr) {
		this.endStr = endStr;
	}


	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.result);
		sb.append(endStr);
			
		return sb.toString();
	}
	
	
    
}
