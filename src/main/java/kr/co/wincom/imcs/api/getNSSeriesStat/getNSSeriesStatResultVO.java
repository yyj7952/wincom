package kr.co.wincom.imcs.api.getNSSeriesStat;

import java.io.Serializable;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.StatVO;


@SuppressWarnings("serial")
public class getNSSeriesStatResultVO extends StatVO implements Serializable {
    private List<getNSSeriesStatResponseVO> List;
    private String resultHeader = "";
    
	public List<getNSSeriesStatResponseVO> getList() {
		return List;
	}

	public void setList(List<getNSSeriesStatResponseVO> list) {
		this.List = list;
	}
	
	public String getResultHeader() {
		return resultHeader;
	}

	public void setResultHeader(String resultHeader) {
		this.resultHeader = resultHeader;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		//결과 헤더 붙이기
		sb.append(this.getResultHeader());
		sb.append(ImcsConstants.ROWSEP); //행분리자
		
		if(this.getList() != null && this.getList().size() > 0) {
			StringBuilder record = new StringBuilder();
			
			for(getNSSeriesStatResponseVO vo : this.getList()) {
				record.append(vo);
				record.append(ImcsConstants.ROWSEP);
			}
			sb.append(record.toString());
		}
		
		return sb.toString();
	}

	
}
