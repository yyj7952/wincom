package kr.co.wincom.imcs.api.getNSPageList;

import java.io.Serializable;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.StatVO;

@SuppressWarnings("serial")
public class GetNSPageListResultVO extends StatVO implements Serializable {
	
	private List<GetNSPageListResponseVO> list;
	private int totCnt = 0;
	
	public List<GetNSPageListResponseVO> getList() {
		return list;
	}

	public void setList(List<GetNSPageListResponseVO> list) {
		this.list = list;
	}

	public int getTotCnt() {
		return totCnt;
	}

	public void setTotCnt(int totCnt) {
		this.totCnt = totCnt;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();

		if(this.getList() != null && this.getList().size() > 0) {
			StringBuilder record = new StringBuilder();
			
			record.append(totCnt);
			record.append(ImcsConstants.ROWSEP);
			
			for(GetNSPageListResponseVO vo : this.getList())
			{
				record.append(vo);
				record.append(ImcsConstants.ROWSEP);
			}
			
			sb.append(record.toString());
		} else {
			StringBuilder record = new StringBuilder();
			record.append(totCnt);
			sb.append(record);
		}
		
		return sb.toString();
	}

}
