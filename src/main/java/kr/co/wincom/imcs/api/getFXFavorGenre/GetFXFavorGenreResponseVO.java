package kr.co.wincom.imcs.api.getFXFavorGenre;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;

@SuppressWarnings("serial")
public class GetFXFavorGenreResponseVO implements Serializable {
	private String idx				= "";		// 번호 (찜목록 리스트 번호)
	private String catId			= "";		// 카테고리 ID
	private String catName			= "";		// 카테고리명
	//private String parentCatId		= "";		// 상위카테고리 ID
	private String regDate			= "";		// 찜목록 등록 날짜
	//private String catLevel			= "";		// 카테고리 레벨 정보(1, 2, 3, 4) (CAT_LEVEL)
	//private String subCnt			= "";
	//private String totalCnt			= "";

	/********************************************************************
	 * 추가 사용되는 속성값  start
	 ********************************************************************/
	private String pid				= "";
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(StringUtil.replaceNull(this.idx, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.catId, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.catName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.regDate, "")).append(ImcsConstants.COLSEP);
		
		return sb.toString();
	}

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public String getCatId() {
		return catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}
}
