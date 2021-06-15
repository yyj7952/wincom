package kr.co.wincom.imcs.api.getNSMusicList;

import java.io.Serializable;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.ComCueSheetItemVO;
import kr.co.wincom.imcs.common.vo.StatVO;

@SuppressWarnings("serial")
public class GetNSMusicListResultVO extends StatVO implements Serializable {

	private List<GetNSMusicListResponseVO> list;
	
	private String resultHeader = "";
	
	public List<GetNSMusicListResponseVO> getList() {
		return list;
	}

	public void setList(List<GetNSMusicListResponseVO> list) {
		this.list = list;
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
			
			for(GetNSMusicListResponseVO vo : this.getList()) {
				record.append(vo);
				record.append(ImcsConstants.ROWSEP);
			}
			
			sb.append(record.toString());
		}
		
		return sb.toString();
	}
}
