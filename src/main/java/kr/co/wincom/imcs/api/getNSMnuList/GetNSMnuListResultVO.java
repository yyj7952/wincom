package kr.co.wincom.imcs.api.getNSMnuList;

import java.io.Serializable;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.StatVO;


@SuppressWarnings("serial")
public class GetNSMnuListResultVO extends StatVO implements Serializable {

    private String result = "";
    private List<GetNSMnuListResponseVO> list;
    private List<GetNSMnuDtlVO> listDtl;
	private String resultHeader = "";
	private String endStr = "";

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		//this.result = result + ImcsConstants.ROWSEP;
		this.result = result;
	}

	public List<GetNSMnuListResponseVO> getList() {
		return list;
	}

	public void setList(List<GetNSMnuListResponseVO> list) {
		this.list = list;
	}

	public String getResultHeader() {
		return resultHeader;
	}

	public void setResultHeader(String resultHeader) {
		this.resultHeader = resultHeader;
	}

	public String getEndStr() {
		return endStr;
	}

	public void setEndStr(String endStr) {
		this.endStr = endStr;
	}

//	public List<GetNSMnuDtlVO> getListDtl() {
//		return listDtl;
//	}
//
//	public void setListDtl(List<GetNSMnuDtlVO> listDtl) {
//		this.listDtl = listDtl;
//	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		
		if(this.result != null && !"".equals(this.result)) {
			sb.append(this.result);
			sb.append(endStr);
		} else {
			//결과 헤더 붙이기
			sb.append(this.getResultHeader());
			sb.append(ImcsConstants.ROWSEP); //행분리자
			if(this.getList() != null && this.getList().size() > 0) {
				StringBuilder record = new StringBuilder();
				
				for(GetNSMnuListResponseVO vo : this.getList()) {
					record.append(vo);
					record.append(ImcsConstants.ROWSEP);
				}
				
				sb.append(record.toString());
			}
			
//			sb.append(ImcsConstants.ROWSEP); //행분리자
//			if(this.getListDtl() != null && this.getListDtl().size() > 0) {
//				StringBuilder record = new StringBuilder();
//				
//				for(GetNSMnuDtlVO vo : this.getListDtl()) {
//					record.append(vo);
//					record.append(ImcsConstants.ROWSEP);
//				}
//				
//				sb.append(record.toString());
//			}
			sb.append(endStr);
			//sb.append(ImcsConstants.ROWSEP);
		}
			
		return sb.toString();
	}
	
	
    
}
