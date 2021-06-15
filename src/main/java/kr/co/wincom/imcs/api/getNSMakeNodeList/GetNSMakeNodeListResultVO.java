package kr.co.wincom.imcs.api.getNSMakeNodeList;

import java.io.Serializable;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.StatVO;

@SuppressWarnings("serial")
public class GetNSMakeNodeListResultVO extends StatVO implements Serializable {

	private String result = "";
	private List<GetNSMakeNodeListResponseVO> list;
	//private String resultHeader = "";
	
	public List<GetNSMakeNodeListResponseVO> getList() {
		return list;
	}

	public void setList(List<GetNSMakeNodeListResponseVO> list) {
		this.list = list;
	}

//	public String getResultHeader() {
//		return resultHeader;
//	}
//
//	public void setResultHeader(String resultHeader) {
//		this.resultHeader = resultHeader;
//	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		//결과 헤더 붙이기
//		sb.append(this.getResultHeader());
//		sb.append(ImcsConstants.ROWSEP); //행분리자
		
		if(this.getList() != null && this.getList().size() > 0) {
			StringBuilder record = new StringBuilder();
			
			for(GetNSMakeNodeListResponseVO vo : this.getList()) {
				record.append(vo);
				record.append(ImcsConstants.ROWSEP);
			}
			
			sb.append(record.toString());
		}
		
		return sb.toString();
	}
}
