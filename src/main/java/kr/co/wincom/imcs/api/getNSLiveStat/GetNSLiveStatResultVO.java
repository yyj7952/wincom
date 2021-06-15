package kr.co.wincom.imcs.api.getNSLiveStat;

import java.io.Serializable;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.StatVO;

@SuppressWarnings("serial")
public class GetNSLiveStatResultVO extends StatVO implements Serializable {

	private String resultHeader = "";
	private List<GetNSLiveStatResponseVO> list;
	
	public String getResultHeader() {
		return resultHeader;
	}

	public void setResultHeader(String resultHeader) {
		this.resultHeader = resultHeader;
	}
	
	public List<GetNSLiveStatResponseVO> getList() {
		return list;
	}

	public void setList(List<GetNSLiveStatResponseVO> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(this.getResultHeader());
		sb.append(ImcsConstants.ROWSEP); //행분리자
	
		if(this.getList() != null && this.getList().size() > 0) {
			StringBuilder record = new StringBuilder();
			
			for(GetNSLiveStatResponseVO vo : this.getList()) {
				record.append(vo);
				record.append(ImcsConstants.ROWSEP);
			}
			
			sb.append(record.toString());
		}
		
		return sb.toString();
	}
	
}
