package kr.co.wincom.imcs.api.getNSPeriod;

import java.io.Serializable;
import java.util.List;

import kr.co.wincom.imcs.common.vo.StatVO;

@SuppressWarnings("serial")
public class GetNSPeriodResultVO extends StatVO implements Serializable {

	private List<GetNSPeriodResponseVO> list;
	private List<GetNSPeriodResponseVO> cachelist;

	public List<GetNSPeriodResponseVO> getList() {
		return list;
	}

	public void setList(List<GetNSPeriodResponseVO> list) {
		this.list = list;
	}
	
	public List<GetNSPeriodResponseVO> getCachelist() {
		return cachelist;
	}

	public void setCachelist(List<GetNSPeriodResponseVO> cachelist) {
		this.cachelist = cachelist;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		if(this.getList() != null && this.getList().size() > 0) {
			StringBuilder record = new StringBuilder();
			
			for(GetNSPeriodResponseVO vo : this.getList()) {
				record.append(vo);
			}
			sb.append(record.toString());
		}
		
		return sb.toString();
	}


	public String toCacheString() {
		StringBuffer sb = new StringBuffer();
		
		if(this.getList() != null && this.getCachelist().size() > 0) {
			StringBuilder record = new StringBuilder();
			
			for(GetNSPeriodResponseVO vo : this.getCachelist()) {
				record.append(vo);
			}
			sb.append(record.toString());
		}
		
		return sb.toString();
	}
}
