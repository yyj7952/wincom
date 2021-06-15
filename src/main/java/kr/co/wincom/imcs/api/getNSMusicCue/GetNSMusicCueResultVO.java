package kr.co.wincom.imcs.api.getNSMusicCue;

import java.io.Serializable;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.ComCueSheetItemVO;
import kr.co.wincom.imcs.common.vo.StatVO;

@SuppressWarnings("serial")
public class GetNSMusicCueResultVO extends StatVO implements Serializable {

	private List<GetNSMusicCueResponseVO> list;
	private List<ComCueSheetItemVO> cueItemList;
	private List<ComCueSheetItemVO> cueAddItemList;
	private List<ComCueSheetItemVO> cueOmniVList;
	private String resultHeader = "";
	
	public List<ComCueSheetItemVO> getCueOmniVList() {
		return cueOmniVList;
	}

	public void setCueOmniVList(List<ComCueSheetItemVO> cueOmniVList) {
		this.cueOmniVList = cueOmniVList;
	}

	public List<GetNSMusicCueResponseVO> getList() {
		return list;
	}

	public void setList(List<GetNSMusicCueResponseVO> list) {
		this.list = list;
	}

	public String getResultHeader() {
		return resultHeader;
	}

	public void setResultHeader(String resultHeader) {
		this.resultHeader = resultHeader;
	}
	
	public List<ComCueSheetItemVO> getCueItemList() {
		return cueItemList;
	}

	public void setCueItemList(List<ComCueSheetItemVO> cueItemList) {
		this.cueItemList = cueItemList;
	}
	
	public List<ComCueSheetItemVO> getCueAddItemList() {
		return cueAddItemList;
	}

	public void setCueAddItemList(List<ComCueSheetItemVO> cueAddItemList) {
		this.cueAddItemList = cueAddItemList;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		//결과 헤더 붙이기
		sb.append(this.getResultHeader());
		sb.append(ImcsConstants.ROWSEP); //행분리자
		
		
		//큐시트 아이템리스트 문자열로 변환
		if(this.getCueItemList() != null && this.getCueItemList().size() > 0) {
			
			for(ComCueSheetItemVO vo : this.getCueItemList()) {
				sb.append(vo.toString());
				sb.append(ImcsConstants.ROWSEP);
			}
		}

		//큐시트 부가아이템리스트 문자열로 변환
		if(this.getCueAddItemList() != null && this.getCueAddItemList().size() > 0) {
			
			for(ComCueSheetItemVO vo : this.getCueAddItemList()) {
				sb.append(vo.addInfoToString());
				sb.append(ImcsConstants.ROWSEP);
			}
		}
		
		//옴니뷰 정보 문자열로 변환
		if(this.getCueOmniVList() != null && this.getCueOmniVList().size() > 0) {
			
			for(ComCueSheetItemVO vo : this.getCueOmniVList()) {
				sb.append(vo.omniInfoToString());
				sb.append(ImcsConstants.ROWSEP);
			}
		}
		
		if(this.getList() != null && this.getList().size() > 0) {
			StringBuilder record = new StringBuilder();
			
			for(GetNSMusicCueResponseVO vo : this.getList()) {
				record.append(vo);
				record.append(ImcsConstants.ROWSEP);
			}
			
			sb.append(record.toString());
		}
		
		return sb.toString();
	}
}
