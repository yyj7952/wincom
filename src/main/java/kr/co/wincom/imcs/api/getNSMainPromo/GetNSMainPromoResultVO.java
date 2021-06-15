package kr.co.wincom.imcs.api.getNSMainPromo;

import java.io.Serializable;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.StatVO;

@SuppressWarnings("serial")
public class GetNSMainPromoResultVO extends StatVO implements Serializable {
	
	private List<GetNSMainPromoResponseVO> list;
	private String notiMsg;
	
	public List<GetNSMainPromoResponseVO> getList() {
		return list;
	}

	public void setList(List<GetNSMainPromoResponseVO> list) {
		this.list = list;
	}
	
	public String getNotiMsg() {
		return notiMsg;
	}

	public void setNotiMsg(String notiMsg) {
		this.notiMsg = notiMsg;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();

		if(this.getList() != null && this.getList().size() > 0) {
			StringBuilder record = new StringBuilder();
			
			for(GetNSMainPromoResponseVO vo : this.getList())
			{
				record.append(vo);
				record.append(ImcsConstants.ROWSEP);
			}
			
			sb.append(record.toString());
		}
		
		sb.append(this.notiMsg);
		sb.append(ImcsConstants.COLSEP);
		
		return sb.toString();
	}


}
