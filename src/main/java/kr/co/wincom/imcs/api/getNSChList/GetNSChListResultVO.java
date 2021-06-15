package kr.co.wincom.imcs.api.getNSChList;

import java.io.Serializable;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.StatVO;


@SuppressWarnings("serial")
public class GetNSChListResultVO extends StatVO implements Serializable {

    private String result = "";
    private List<GetNSChListResponseVO> list;
	private String resultHeader = "";
	private String resultCst    = "";
	private List<ConcertInfoVO> cstList;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public List<GetNSChListResponseVO> getList() {
		return list;
	}

	public void setList(List<GetNSChListResponseVO> list) {
		this.list = list;
	}

	public String getResultHeader() {
		return resultHeader;
	}

	public void setResultHeader(String resultHeader) {
		this.resultHeader = resultHeader;
	}
	
	public String getResultCst() {
		return resultCst;
	}

	public void setResultCst(String resultCst) {
		this.resultCst = resultCst;
	}
	
	public List<ConcertInfoVO> getCstList() {
		return cstList;
	}

	public void setCstList(List<ConcertInfoVO> cstList) {
		this.cstList = cstList;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		
		if(this.result != null && !"".equals(this.result)) {
			sb.append(this.result);
		} else {
			//결과 헤더 붙이기
			sb.append(this.getResultHeader());
			sb.append(ImcsConstants.ROWSEP); //행분리자
			if(this.getList() != null && this.getList().size() > 0) {
				StringBuilder record = new StringBuilder();
				
				for(GetNSChListResponseVO vo : this.getList()) {
					record.append(vo);
					record.append(ImcsConstants.ROWSEP);
				}
				
				sb.append(record.toString());
			}
			
			//유툐채널정보 붙이기
			if(this.getCstList() != null && this.getCstList().size() > 0) {
				StringBuilder record = new StringBuilder();
				
				for(ConcertInfoVO vo : this.getCstList()) {
					record.append(vo);
					record.append(ImcsConstants.ROWSEP);
				}
				
				sb.append(record.toString());
			}
			
			
		}
			
		return sb.toString();
	}
    
}
