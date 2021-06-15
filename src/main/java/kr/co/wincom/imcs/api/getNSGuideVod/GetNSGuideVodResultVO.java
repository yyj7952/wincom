package kr.co.wincom.imcs.api.getNSGuideVod;

import java.io.Serializable;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.StatVO;

@SuppressWarnings("serial")
public class GetNSGuideVodResultVO extends StatVO implements Serializable {

	private List<GetNSGuideVodResponseVO> list;
	
	public List<GetNSGuideVodResponseVO> getList() {
		return list;
	}

	public void setList(List<GetNSGuideVodResponseVO> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		if(this.getList() != null && this.getList().size() > 0) {
			StringBuilder record = new StringBuilder();
			
			for(GetNSGuideVodResponseVO vo : this.getList()) {
				record.append(vo);
				record.append(ImcsConstants.ROWSEP);
			}
			
			sb.append(record.toString());
		}
		return sb.toString();
	}
	
}
