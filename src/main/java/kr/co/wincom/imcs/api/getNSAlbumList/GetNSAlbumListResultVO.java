package kr.co.wincom.imcs.api.getNSAlbumList;

import java.io.Serializable;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.StatVO;


@SuppressWarnings("serial")
public class GetNSAlbumListResultVO extends StatVO implements Serializable {

	private String result;
	private List<GetNSAlbumListResponseVO> list;
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	public List<GetNSAlbumListResponseVO> getList() {
		return list;
	}

	public void setList(List<GetNSAlbumListResponseVO> list) {
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
				
				for(GetNSAlbumListResponseVO vo : this.getList()) {
					record.append(vo);
					record.append(ImcsConstants.ROWSEP);
				}
				
				sb.append(record.toString());
			}
		}
			
		return sb.toString();
	}
}
