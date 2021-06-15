package kr.co.wincom.imcs.api.getFXProdInfo;

import java.io.Serializable;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.StatVO;

@SuppressWarnings("serial")
public class GetFXProdInfoResultVO extends StatVO implements Serializable {
	
	private List<GetFXProdInfoResponseVO> list;
	private String totalCnt	= "";

	public List<GetFXProdInfoResponseVO> getList() {
		return list;
	}

	public void setList(List<GetFXProdInfoResponseVO> list) {
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
			
			for(GetFXProdInfoResponseVO vo : this.getList()) {
				record.append(vo);
				record.append(ImcsConstants.ROWSEP);
			}
			sb.append(record.toString());
		}
		
		return sb.toString();
	}

}
