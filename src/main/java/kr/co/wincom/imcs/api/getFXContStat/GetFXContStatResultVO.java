package kr.co.wincom.imcs.api.getFXContStat;

import java.io.Serializable;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.ComOfferVO;

@SuppressWarnings("serial")
public class GetFXContStatResultVO implements Serializable {
	
	private List<GetFXContStatResponseVO> list;
	private List<ComOfferVO> cpnInfo;
	private int resultSet = 0; 

	public List<GetFXContStatResponseVO> getList() {
		return list;
	}

	public void setList(List<GetFXContStatResponseVO> list) {
		this.list = list;
	}

	public List<ComOfferVO> getCpnInfo() {
		return cpnInfo;
	}

	public void setCpnInfo(List<ComOfferVO> cpnInfo) {
		this.cpnInfo = cpnInfo;
	}
	
	public int getResultSet() {
		return resultSet;
	}

	public void setResultSet(int resultSet) {
		this.resultSet = resultSet;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		if(this.getList() != null && this.getList().size() > 0) {
			StringBuilder record = new StringBuilder();
			
			for(GetFXContStatResponseVO vo : this.getList()) {
				record.append(vo);
				record.append(ImcsConstants.ROWSEP);
			}
			sb.append(record.toString());
			
			if(this.cpnInfo != null && !this.cpnInfo.isEmpty() && this.cpnInfo.size() > 0)
				sb.append(this.cpnInfo.toString()).append(ImcsConstants.ROWSEP);
			
			if(this.getResultSet() == -1)
			{
				sb.append(ImcsConstants.ROWSEP);
			}
		}
		
		return sb.toString();
	}


}
