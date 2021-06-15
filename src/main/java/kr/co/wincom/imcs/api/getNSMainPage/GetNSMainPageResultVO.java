package kr.co.wincom.imcs.api.getNSMainPage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import kr.co.wincom.imcs.common.vo.StatVO;


@SuppressWarnings("serial")
public class GetNSMainPageResultVO extends StatVO implements Serializable {

    private List<GetNSMainPageMainVO> mainList		= new ArrayList<GetNSMainPageMainVO>();
    private List<GetNSMainPageSubVO> subList		= new ArrayList<GetNSMainPageSubVO>();
    private List<GetNSMainPageChannelVO> chnlList	= new ArrayList<GetNSMainPageChannelVO>();

	public List<GetNSMainPageMainVO> getMainList() {
		return mainList;
	}

	public void setMainList(List<GetNSMainPageMainVO> mainList) {
		this.mainList = mainList;
	}

	public List<GetNSMainPageSubVO> getSubList() {
		return subList;
	}

	public void setSubList(List<GetNSMainPageSubVO> subList) {
		this.subList = subList;
	}

	public List<GetNSMainPageChannelVO> getChnlList() {
		return chnlList;
	}

	public void setChnlList(List<GetNSMainPageChannelVO> chnlList) {
		this.chnlList = chnlList;
	}


	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		int cnt = 0;
		// 실시간 인기채널 프로그램
		if(this.getChnlList() != null && this.getChnlList().size() > 0) {
			StringBuilder record = new StringBuilder();
			
			for(GetNSMainPageChannelVO vo : this.getChnlList()) {
				if(cnt > 2) break;
				record.append(vo);
				
				cnt++;
			}
			
			sb.append(record.toString());
		}
		
		// 서브 카테고리 리스트
		if(this.getSubList() != null && this.getSubList().size() > 0) {
			StringBuilder record = new StringBuilder();
			
			for(GetNSMainPageSubVO vo : this.getSubList()) {
				record.append(vo);
			}
			
			sb.append(record.toString());
		}

		// 메인 카테고리 리스트
		if(this.getMainList() != null && this.getMainList().size() > 0) {
			StringBuilder record = new StringBuilder();
			
			for(GetNSMainPageMainVO vo : this.getMainList()) {
				record.append(vo);
			}
			
			sb.append(record.toString());
		}
		
		
		return sb.toString();
	}
}
