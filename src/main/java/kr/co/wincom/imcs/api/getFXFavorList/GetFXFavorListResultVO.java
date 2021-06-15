package kr.co.wincom.imcs.api.getFXFavorList;

import java.io.Serializable;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.StatVO;

@SuppressWarnings("serial")
public class GetFXFavorListResultVO extends StatVO implements Serializable {
	
	private List<GetFXFavorListResponseVO> list;
	private String totalCnt	= "";

	public List<GetFXFavorListResponseVO> getList() {
		return list;
	}

	public void setList(List<GetFXFavorListResponseVO> list) {
		this.list = list;
	}

	public String getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(String totalCnt) {
		this.totalCnt = totalCnt;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		if(this.getList() != null && this.getList().size() > 0) {
			StringBuilder record = new StringBuilder();
			
			for(GetFXFavorListResponseVO vo : this.getList()) {
				vo.setTotalCnt(totalCnt);
				record.append(vo);
				record.append(ImcsConstants.ROWSEP);
			}
			sb.append(record.toString());
		}
		
		return sb.toString();
	}

}
