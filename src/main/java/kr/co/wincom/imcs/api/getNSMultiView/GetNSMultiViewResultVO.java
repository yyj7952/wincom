package kr.co.wincom.imcs.api.getNSMultiView;

import java.io.Serializable;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.StatVO;


@SuppressWarnings("serial")
public class GetNSMultiViewResultVO extends StatVO implements Serializable {
    private List<GetNSMultiViewResponseVO> List;

	public List<GetNSMultiViewResponseVO> getList() {
		return List;
	}

	public void setList(List<GetNSMultiViewResponseVO> list) {
		this.List = list;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		if(this.getList() != null && this.getList().size() > 0) {
			StringBuilder record = new StringBuilder();
			
			for(GetNSMultiViewResponseVO vo : this.getList()) {
				record.append(vo);
				record.append(ImcsConstants.ROWSEP);
			}
			sb.append(record.toString());
		}
		
		return sb.toString();
	}
}
