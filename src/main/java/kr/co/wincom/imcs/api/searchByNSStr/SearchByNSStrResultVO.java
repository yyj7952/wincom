package kr.co.wincom.imcs.api.searchByNSStr;

import java.io.Serializable;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.StatVO;


@SuppressWarnings("serial")
public class SearchByNSStrResultVO extends StatVO implements Serializable {

    List<SearchByNSStrResponseVO> list;
   
	
	public List<SearchByNSStrResponseVO> getList() {
		return list;
	}

	public void setList(List<SearchByNSStrResponseVO> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();

		if(this.getList() != null && this.getList().size() > 0) {
			StringBuilder record = new StringBuilder();
			
			for(SearchByNSStrResponseVO vo : this.getList()) {
				record.append(vo);
				//record.append(ImcsConstants.ROWSEP);
			}
			
			sb.append(record.toString());
		}
		
		return sb.toString();
	}

}
