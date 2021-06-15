package kr.co.wincom.imcs.api.getNSSVODInfo;

import java.io.Serializable;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.StatVO;

@SuppressWarnings("serial")
public class GetNSSVODInfoResultVO extends StatVO implements Serializable {

	private List<GetNSSVODInfoResponseVO> list;
	  
	public List<GetNSSVODInfoResponseVO> getList() {
		return list;
	}

	public void setList(List<GetNSSVODInfoResponseVO> list) {
		this.list = list;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		if(this.getList() != null && this.getList().size() > 0) {
			StringBuilder record = new StringBuilder();
			
			for(GetNSSVODInfoResponseVO vo : this.getList()) {
				record.append(vo);
				record.append(ImcsConstants.ROWSEP);
			}
			
			sb.append(record.toString());
		}
		
		return sb.toString();
	}
	
	
	private String uCubeProdId 		= "";
	
	private String subProdDesc		= "";

	
	public String getuCubeProdId() {
		return uCubeProdId;
	}

	public void setuCubeProdId(String uCubeProdId) {
		this.uCubeProdId = uCubeProdId;
	}

	public String getSubProdDesc() {
		return subProdDesc;
	}

	public void setSubProdDesc(String subProdDesc) {
		this.subProdDesc = subProdDesc;
	}
   
	
	
    
}
