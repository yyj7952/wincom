package kr.co.wincom.imcs.api.getNSLinkTime;

import java.io.Serializable;
import java.util.List;

import kr.co.wincom.imcs.api.getNSSeriesList.GetNSSeriesListResponseVO;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.StatVO;


@SuppressWarnings("serial")
public class GetNSLinkTimeResultVO extends StatVO implements Serializable {
	
    private List<GetNSLinkTimeResponseVO> list;
    
    private GetNSLinkTimeResponseVO result;

	public List<GetNSLinkTimeResponseVO> getList() {
		return list;
	}

	public void setList(List<GetNSLinkTimeResponseVO> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		if(this.result != null) {
			StringBuilder record = new StringBuilder();
			record.append(this.result);
			record.append(ImcsConstants.ROWSEP);
			sb.append(record.toString());
		}
		
		return sb.toString();
	}

	public GetNSLinkTimeResponseVO getResult() {
		return result;
	}

	public void setResult(GetNSLinkTimeResponseVO result) {
		this.result = result;
	}

}
