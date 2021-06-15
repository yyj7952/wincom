package kr.co.wincom.imcs.api.getNSVisitDtl;

import java.io.Serializable;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.StatVO;


@SuppressWarnings("serial")
public class GetNSVisitDtlResultVO extends StatVO implements Serializable {

	private List<GetNSVisitDtlResponseVO> list;
  
	public List<GetNSVisitDtlResponseVO> getList() {
		return list;
	}

	public void setList(List<GetNSVisitDtlResponseVO> list) {
		this.list = list;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		if(this.getList() != null && this.getList().size() > 0) {
			StringBuilder record = new StringBuilder();
			
			for(GetNSVisitDtlResponseVO vo : this.getList()) {
				record.append(vo);
				record.append(ImcsConstants.ROWSEP);
			}
			
			sb.append(record.toString());
		}
		
		return sb.toString();
	}

	
	
}
