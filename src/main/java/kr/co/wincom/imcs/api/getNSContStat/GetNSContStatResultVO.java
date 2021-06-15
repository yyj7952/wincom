package kr.co.wincom.imcs.api.getNSContStat;

import java.io.Serializable;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.ComCpnVO;
import kr.co.wincom.imcs.common.vo.StatVO;

@SuppressWarnings("serial")
public class GetNSContStatResultVO extends StatVO implements Serializable {

	private List<GetNSContStatResponseVO> list;
	private ComCpnVO cpnInfo;
	
	public List<GetNSContStatResponseVO> getList() {
		return list;
	}

	public void setList(List<GetNSContStatResponseVO> list) {
		this.list = list;
	}
	
	public ComCpnVO getCpnInfo() {
		return cpnInfo;
	}

	public void setCpnInfo(ComCpnVO cpnInfo) {
		this.cpnInfo = cpnInfo;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		if(cpnInfo == null){
			cpnInfo = new ComCpnVO();
		}
	
		if(this.getList() != null && this.getList().size() > 0) {
			StringBuilder record = new StringBuilder();
			
			for(GetNSContStatResponseVO vo : this.getList()) {
				record.append(vo);
				record.append(ImcsConstants.ROWSEP);
			}
			
			sb.append(record.toString());
			sb.append(ImcsConstants.ROWSEP);
			sb.append(cpnInfo.getStmInfo().toString());
			sb.append(ImcsConstants.ROWSEP);
			sb.append(cpnInfo.getCpnInfo().toString());
			sb.append(ImcsConstants.ROWSEP);
			sb.append(cpnInfo.getUseCpnInfo().toString());
			sb.append(ImcsConstants.ROWSEP);
			sb.append(cpnInfo.getGblCpnInfo().toString());
			sb.append(ImcsConstants.ROWSEP);
		}
		
		return sb.toString();
	}


	
}
