package kr.co.wincom.imcs.api.getNSWatchList;

import java.io.Serializable;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.StatVO;

@SuppressWarnings("serial")
public class GetNSWatchListResultVO extends StatVO implements Serializable {

	private List<GetNSWatchListResponseVO> list;
	
	private String resultHeader = "";
	
	private int iTotalCount = 0;
	
	public List<GetNSWatchListResponseVO> getList() {
		return list;
	}

	public void setList(List<GetNSWatchListResponseVO> list) {
		this.list = list;
	}

	public String getResultHeader() {
		return resultHeader;
	}

	public void setResultHeader(String resultHeader) {
		this.resultHeader = resultHeader;
	}

	public int getiTotalCount() {
		return iTotalCount;
	}

	public void setiTotalCount(int iTotalCount) {
		this.iTotalCount = iTotalCount;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		//결과 헤더 붙이기
		sb.append(this.getResultHeader());
		sb.append(ImcsConstants.ROWSEP); //행분리자
		
		if(this.getList() != null && this.getList().size() > 0) {
			StringBuilder record = new StringBuilder();
			
			for(GetNSWatchListResponseVO vo : this.getList()) {
				record.append(vo);
				record.append(ImcsConstants.ROWSEP);
			}
			
			sb.append(record.toString());
		}
		
		return sb.toString();
	}
}
