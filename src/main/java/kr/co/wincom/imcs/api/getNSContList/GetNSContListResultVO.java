package kr.co.wincom.imcs.api.getNSContList;

import java.io.Serializable;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.OstInfoVO;
import kr.co.wincom.imcs.common.vo.StatVO;

@SuppressWarnings("serial")
public class GetNSContListResultVO extends StatVO implements Serializable {
	
	private List<GetNSContListResponseVO> list;
	private List<OstInfoVO> ostList;
	
	
	public List<GetNSContListResponseVO> getList() {
		return list;
	}

	public void setList(List<GetNSContListResponseVO> list) {
		this.list = list;
	}	

	public List<OstInfoVO> getOstList() {
		return ostList;
	}

	public void setOstList(List<OstInfoVO> ostList) {
		this.ostList = ostList;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();

		if(this.getList() != null && this.getList().size() > 0) {
			StringBuilder record = new StringBuilder();
			
			for(GetNSContListResponseVO vo : this.getList())
			{
				record.append(vo);
				record.append(ImcsConstants.ROWSEP);
			}
						
			if(ostList != null){
				
				for(OstInfoVO vo : this.getOstList()){
					record.append(vo);
				}
			}
			
			sb.append(record.toString());
		}
		
		return sb.toString();
	}
}
