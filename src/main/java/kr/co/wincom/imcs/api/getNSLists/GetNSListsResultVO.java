package kr.co.wincom.imcs.api.getNSLists;

import java.io.Serializable;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.StatVO;

@SuppressWarnings("serial")
public class GetNSListsResultVO extends StatVO implements Serializable {
	
	private List<GetNSListsResponseVO> list;
	private String result;
	private String endStr = "";

	public List<GetNSListsResponseVO> getList() {
		return list;
	}

	public void setList(List<GetNSListsResponseVO> list) {
		this.list = list;
	}
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	public String getEndStr() {
		return endStr;
	}

	public void setEndStr(String endStr) {
		this.endStr = endStr;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		//this.result = StringUtil.nullToSpace(this.result);
		
		if(!"".equals(this.result) && this.result != null) {
			sb.append(this.result);
		} else {
			if(this.getList() != null && this.getList().size() > 0) {
				StringBuilder record = new StringBuilder();
				
				for(GetNSListsResponseVO vo : this.getList()) {
					record.append(vo);
					record.append(ImcsConstants.ROWSEP);
				}
				
				sb.append(record.toString());
				sb.append(endStr);
				
			}else{
				sb.append(endStr);
				//sb.append(ImcsConstants.ROWSEP);
				//sb.append(ImcsConstants.ROWSEP);
			}

		}
		
		return sb.toString();
	}
}
