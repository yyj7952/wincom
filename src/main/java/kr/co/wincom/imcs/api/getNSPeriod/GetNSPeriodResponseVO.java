package kr.co.wincom.imcs.api.getNSPeriod;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;

@SuppressWarnings("serial")
public class GetNSPeriodResponseVO  extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * GetNSPeriod API 전문 칼럼(순서 일치)
	********************************************************************/
	private String lstUPeriode	= "";	// 리스트 업데이트 주기
    private String guideVer		= "";	// 가이드VOD 영상 정보
    private String guideCnt		= "";	// 가이드VOD 영상 다운로드 개수
    private String subUPeriode	= "";	// 서브리스트 업데이트 주기
    private String subVer		= "";	// 서브리스트 버전 정보
    private String lstVer		= "";	// 리스트 버전 정보
    private String prInfo		= "";	// 연령 
    private String siVer		= "";	// EPG 전체 채널정보 버전
    private String psiLstUPeriod	= "";	// EPG 전체 프로그램 정보 업데이트 주기
    private String psiVer		= "";	// EPG 전체 프로그램 정보 버전
    private String rankVer		= "";	// 추천 VOD 버전 정보
    
    
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(StringUtil.replaceNull(this.lstUPeriode, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.guideVer, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.guideCnt, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.subUPeriode, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.subVer, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.lstVer, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.prInfo, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.siVer, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.psiLstUPeriod, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.psiVer, "")).append(ImcsConstants.COLSEP);
	//	sb.append(StringUtil.replaceNull(this.rankVer, "")).append(ImcsConstants.COLSEP);
		sb.append(ImcsConstants.ROWSEP);
		
		return sb.toString();
	}


	public String getLstUPeriode() {
		return lstUPeriode;
	}


	public void setLstUPeriode(String lstUPeriode) {
		this.lstUPeriode = lstUPeriode;
	}


	public String getGuideVer() {
		return guideVer;
	}


	public void setGuideVer(String guideVer) {
		this.guideVer = guideVer;
	}


	public String getGuideCnt() {
		return guideCnt;
	}


	public void setGuideCnt(String guideCnt) {
		this.guideCnt = guideCnt;
	}


	public String getSubUPeriode() {
		return subUPeriode;
	}


	public void setSubUPeriode(String subUPeriode) {
		this.subUPeriode = subUPeriode;
	}


	public String getSubVer() {
		return subVer;
	}


	public void setSubVer(String subVer) {
		this.subVer = subVer;
	}


	public String getLstVer() {
		return lstVer;
	}


	public void setLstVer(String lstVer) {
		this.lstVer = lstVer;
	}


	public String getPrInfo() {
		return prInfo;
	}


	public void setPrInfo(String prInfo) {
		this.prInfo = prInfo;
	}


	public String getSiVer() {
		return siVer;
	}


	public void setSiVer(String siVer) {
		this.siVer = siVer;
	}


	public String getPsiLstUPeriod() {
		return psiLstUPeriod;
	}


	public void setPsiLstUPeriod(String psiLstUPeriod) {
		this.psiLstUPeriod = psiLstUPeriod;
	}


	public String getPsiVer() {
		return psiVer;
	}


	public void setPsiVer(String psiVer) {
		this.psiVer = psiVer;
	}


	public String getRankVer() {
		return rankVer;
	}


	public void setRankVer(String rankVer) {
		this.rankVer = rankVer;
	}

}
