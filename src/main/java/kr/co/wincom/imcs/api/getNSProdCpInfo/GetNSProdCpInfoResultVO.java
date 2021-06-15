package kr.co.wincom.imcs.api.getNSProdCpInfo;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.ComCpnVO;
import kr.co.wincom.imcs.common.vo.StatVO;

import org.apache.commons.lang.StringUtils;


@SuppressWarnings("serial")
public class GetNSProdCpInfoResultVO extends StatVO implements Serializable {

    private ComCpnVO cpnInfoVO;
	
	public ComCpnVO getCpnInfoVO() {
		return cpnInfoVO;
	}

	public void setCpnInfoVO(ComCpnVO cpnInfoVO) {
		this.cpnInfoVO = cpnInfoVO;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(StringUtils.defaultString(cpnInfoVO.getCpnInfo(), "")).append(ImcsConstants.COLSEP).append(ImcsConstants.ROWSEP);
				
		return sb.toString();
	}
}
