package kr.co.wincom.imcs.api.getNSVODRank;

import java.io.Serializable;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.StatVO;


@SuppressWarnings("serial")
public class GetNSVODRankResultVO extends StatVO implements Serializable {

    private String result = "";
    private List<GetNSVODRankResponseVO> list;
    
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public List<GetNSVODRankResponseVO> getList() {
		return list;
	}

	public void setList(List<GetNSVODRankResponseVO> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		if(this.result != null && !"".equals(this.result)) {
			sb.append(this.result);
		} else {
			if(this.getList() != null && this.getList().size() > 0) {
				StringBuilder record = new StringBuilder();
				
				for(GetNSVODRankResponseVO vo : this.getList()) {
					record.append(vo);
					record.append(ImcsConstants.ROWSEP);
					record.append(ImcsConstants.ROWSEP);
				}
				
				sb.append(record.toString());
			}
		}
			
		return sb.toString();
	}
}
