package kr.co.wincom.imcs.api.getNSSimilarList;

import java.io.Serializable;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.StatVO;
import kr.co.wincom.imcs.common.vo.ComWatchaVO;


@SuppressWarnings("serial")
public class GetNSSimilarListResultVO extends StatVO implements Serializable {
    List<GetNSSimilarListResponseVO> list;
	List<ComWatchaVO> watchaList;


	public List<GetNSSimilarListResponseVO> getList() {
		return list;
	}

	public void setList(List<GetNSSimilarListResponseVO> list) {
		this.list = list;
	}

	public List<ComWatchaVO> getWatchaList() {
		return watchaList;
	}

	public void setWatchaList(List<ComWatchaVO> watchaList) {
		this.watchaList = watchaList;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		GetNSSimilarListResponseVO responseVO	= null;
		ComWatchaVO watchaVO	= null;
	
		for(int i = 0; i < this.getList().size(); i++) {
			responseVO = this.getList().get(i);
			watchaVO = this.getWatchaList().get(i);
			
			sb.append(responseVO.toString());
			sb.append(watchaVO.toWatchaString());
			sb.append(ImcsConstants.ROWSEP);
		}
		
		return sb.toString();
	}
}
