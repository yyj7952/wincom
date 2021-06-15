package kr.co.wincom.imcs.api.getNSDMPurDtl;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.StatVO;


@SuppressWarnings("serial")
public class GetNSDMPurDtlResultVO extends StatVO implements Serializable {


	private GetNSDMPurDtlResponseVO resultVO = null;

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if(this.resultVO == null)	resultVO = new GetNSDMPurDtlResponseVO();
		
		sb.append(resultVO.toString()).append(ImcsConstants.ROWSEP);

		return sb.toString();
	}

	public GetNSDMPurDtlResponseVO getResultVO() {
		return resultVO;
	}

	public void setResultVO(GetNSDMPurDtlResponseVO resultVO) {
		this.resultVO = resultVO;
	}


}
