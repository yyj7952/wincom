package kr.co.wincom.imcs.api.getNSBuyList;

import java.io.Serializable;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.StatVO;

@SuppressWarnings("serial")
public class GetNSBuyListResultVO extends StatVO implements Serializable {
	
	private List<GetNSBuyListResponseVO> list;
	private String resHeader;
	
	public List<GetNSBuyListResponseVO> getList() {
		return list;
	}

	public void setList(List<GetNSBuyListResponseVO> list) {
		this.list = list;
	}
	
	public String getResHeader() {
		return resHeader;
	}

	public void setResHeader(String resHeader) {
		this.resHeader = resHeader;
	}
	
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();

		if(this.getResHeader() != null && this.getResHeader().length() > 0) {
			StringBuilder record = new StringBuilder();
			record.append(this.getResHeader());
			record.append(ImcsConstants.ROWSEP);
			sb.append(record.toString());
		}
		
		if(this.getList() != null && this.getList().size() > 0) {
			StringBuilder record = new StringBuilder();
			
			for(GetNSBuyListResponseVO vo : this.getList())
			{
				record.append(vo);
				record.append(ImcsConstants.ROWSEP);
			}
			
			sb.append(record.toString());
		}
		
		return sb.toString();
	}
}
