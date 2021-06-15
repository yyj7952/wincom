package kr.co.wincom.imcs.api.getFXFavorGenre;

import java.io.Serializable;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.StatVO;

@SuppressWarnings("serial")
public class GetFXFavorGenreResultVO extends StatVO implements Serializable {
	
	private List<GetFXFavorGenreResponseVO> list;

	public List<GetFXFavorGenreResponseVO> getList() {
		return list;
	}

	public void setList(List<GetFXFavorGenreResponseVO> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		if(this.getList() != null && this.getList().size() > 0) {
			StringBuilder record = new StringBuilder();
			
			for(GetFXFavorGenreResponseVO vo : this.getList()) {
				record.append(vo);
				record.append(ImcsConstants.ROWSEP);
			}
			sb.append(record.toString());
		}
		
		return sb.toString();
	}
	
}
