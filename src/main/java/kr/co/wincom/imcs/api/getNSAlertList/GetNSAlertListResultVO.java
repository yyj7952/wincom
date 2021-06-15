package kr.co.wincom.imcs.api.getNSAlertList;

import java.io.Serializable;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.StatVO;

public class GetNSAlertListResultVO extends StatVO implements Serializable
{
	
	private static final long serialVersionUID = 170116096565178079L;

	private List<GetNSAlertListResponseVO> list;

	public List<GetNSAlertListResponseVO> getList() {
		return list;
	}

	public void setList(List<GetNSAlertListResponseVO> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		if(this.getList() != null && this.getList().size() > 0) {
			StringBuilder record = new StringBuilder();
			
			for(GetNSAlertListResponseVO vo : this.getList()) {
				record.append(vo);
				record.append(ImcsConstants.ROWSEP);
			}
			
			sb.append(record.toString());
		}
		
		return sb.toString();
	}
	
}
