package kr.co.wincom.imcs.api.getNSSuggestVOD;

import java.io.Serializable;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.StatVO;


@SuppressWarnings("serial")
public class GetNSSuggestVODResultVO extends StatVO implements Serializable {

	
	private List<GetNSSuggestVODResponseVO> list;

	public List<GetNSSuggestVODResponseVO> getList() {
		return list;
	}

	public void setList(List<GetNSSuggestVODResponseVO> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		if(this.getList() != null && this.getList().size() > 0) {
			StringBuilder record = new StringBuilder();
			
			for(GetNSSuggestVODResponseVO vo : this.getList()) {
				record.append(vo);
				record.append(ImcsConstants.ROWSEP);
			}
			
			sb.append(record.toString());
		}
		return sb.toString();
	}
}
