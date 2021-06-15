package kr.co.wincom.imcs.api.getNSContInfo;

import java.io.Serializable;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.ComCueSheetItemVO;
import kr.co.wincom.imcs.common.vo.StatVO;

@SuppressWarnings("serial")
public class GetNSContInfoResultVO extends StatVO implements Serializable {

	private List<GetNSContInfoResponseVO> list;
	
	private String resultHeader = "";
	private String resultOst    = "";
	private String resultImg    = "";
	private String resultCst    = "";
	
	public List<GetNSContInfoResponseVO> getList() {
		return list;
	}

	public void setList(List<GetNSContInfoResponseVO> list) {
		this.list = list;
	}

	public String getResultHeader() {
		return resultHeader;
	}

	public void setResultHeader(String resultHeader) {
		this.resultHeader = resultHeader;
	}
	
	public String getResultOst() {
		return resultOst;
	}

	public void setResultOst(String resultOst) {
		this.resultOst = resultOst;
	}
	
	public String getResultImg() {
		return resultImg;
	}

	public void setResultImg(String resultImg) {
		this.resultImg = resultImg;
	}
	
	public String getResultCst() {
		return resultCst;
	}

	public void setResultCst(String resultCst) {
		this.resultCst = resultCst;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		//결과 헤더 붙이기
		sb.append(this.getResultHeader());
		sb.append(ImcsConstants.ROWSEP); //행분리자
		
		if(this.getList() != null && this.getList().size() > 0) {
			StringBuilder record = new StringBuilder();
			
			for(GetNSContInfoResponseVO vo : this.getList()) {
				record.append(vo);
				record.append(ImcsConstants.ROWSEP);
			}
			
			sb.append(record.toString());
		}
		
		//OST정보 붙이기
		sb.append(this.getResultOst());
		
		//이미지정보 붙이기
		sb.append(this.getResultImg());
		
		//유툐채널정보 붙이기
		sb.append(this.getResultCst());
		
		return sb.toString();
	}
}
