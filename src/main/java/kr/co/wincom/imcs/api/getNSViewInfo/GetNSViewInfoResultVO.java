package kr.co.wincom.imcs.api.getNSViewInfo;

import java.io.Serializable;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.StatVO;

@SuppressWarnings("serial")
public class GetNSViewInfoResultVO extends StatVO implements Serializable {

	private GetNSViewInfoResponseVO list;
	  
	public GetNSViewInfoResponseVO getList() {
		return list;
	}

	public void setList(GetNSViewInfoResponseVO list) {
		this.list = list;
	}
	
	private String resultHeader = "";
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(this.getResultHeader());
		sb.append(ImcsConstants.ROWSEP); //행분리자
		
		if(this.getList() != null) {
			StringBuilder record = new StringBuilder();
			
			record.append(this.getList());
			record.append(ImcsConstants.ROWSEP);
			
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

	public String getResultHeader() {
		return resultHeader;
	}

	public void setResultHeader(String resultHeader) {
		this.resultHeader = resultHeader;
	}
   
	
	
    
}
