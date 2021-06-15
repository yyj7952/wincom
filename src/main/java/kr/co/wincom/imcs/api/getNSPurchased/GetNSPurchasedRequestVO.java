package kr.co.wincom.imcs.api.getNSPurchased;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

public class GetNSPurchasedRequestVO extends NoSqlLoggingVO implements Serializable
{
	private static final long serialVersionUID = 820027358203692775L;
	
	/********************************************************************
	 * getNSPurchased API 전문 칼럼(순서 일치)
	********************************************************************/
	private String saId			= "";	// [12] 가입자정보
	private String stbMac		= "";	// [14] 가입자 MAC Address
	private String contsGb		= "";	// [  ] 컨텐츠 구분(NSC or PAD)
	private String pageNo		= "";	// [ 5] 페이지 넘버
	private String pageCnt		= "";	// [ 5] 페이지 개수 (페이지 당 N개)
	private String quickDisYn	= "";	// [ 1] 퀵배포 컨텐츠 포함 여부
	private String fxType		= "";	// [ 1] 유플릭스 타입 (M : MobileWeb, P : PcWeb, T : TvgApp, H : HDTV)
	private String possessionYn	= "";	// [ 1] 평생소장 여부 (Y : 평생소장O, N : 평생소장X, 기타 : 구매이력전체)	
	private String nscListYn	= "";	// [ 1] nScreen(STB) 목록 조회 여부 (Y : 페어링 된 nScreen(STB) 구매 목록 조회, N or null: 모바일 구매 목록 조회)
	
	/********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
	private String nscGb		= "";	// N스크린 - PAD, LTE
	private String catId		= "";	// 카테고리ID - 카테고리명 조회 시 사용
	private String contsId		= "";	// 컨텐츠ID - 컨텐츠구분 조회 시 사용
	
	private String pid			= "";	// 프로세스ID - 로그용
	private String resultCode	= "20000000";
	
	// 엔스크린 관련 필드 추가- 2017.12.02
	private String nSaId		= "";	// [12] STB 가입자 정보
	private String nStbMac		= "";	// [14] 가입자 STB MAC Address
	private String testSbc		= "";	//
	

	public GetNSPurchasedRequestVO(String szParam){
		CommonService commonService = new CommonService();
		
		String[] arrStat = szParam.split("\\|");
		
		String key		= "";
		String value	= "";
		int    nStr		= 0;
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		
		/*String szTemp = szParam.toLowerCase();
		
		if( szTemp.indexOf("sa_id") == -1 || szTemp.indexOf("stb_mac") == -1
				|| (szTemp.indexOf("contents_gb") == -1 && szTemp.indexOf("conts_gb") == -1 ) )
		{
			throw new ImcsException();
		}*/
		
		String contentsGb = "";
		
		for(int i = 0; i < arrStat.length; i++){
			nStr	= arrStat[i].indexOf("=");
			
			if(nStr > 0) {
				key = arrStat[i].substring(0, nStr).toLowerCase().trim();
				value = arrStat[i].substring(nStr + 1, arrStat[i].length()).trim();
				
				paramMap.put(key, value);
				
				/*if( !value.matches(ImcsConstants.N_SP_PTN) && key.toLowerCase().indexOf("name") == -1 ){
					//특수문자 있음
					throw new ImcsException();
				}*/
				
				if(key.toLowerCase().equals("sa_id"))			this.saId		= value;
				if(key.toLowerCase().equals("stb_mac"))			this.stbMac		= value;
				if(key.toLowerCase().equals("contents_gb"))		contentsGb	= value;
				if(key.toLowerCase().equals("conts_gb"))		this.contsGb	= value;
				if(key.toLowerCase().equals("page_no"))			this.pageNo		= value;
				if(key.toLowerCase().equals("page_cnt"))		this.pageCnt	= value;
				if(key.toLowerCase().equals("quick_dis_yn"))	this.quickDisYn	= value;
				if(key.toLowerCase().equals("fx_type"))			this.fxType		= value;
				if(key.toLowerCase().equals("possession_yn"))	this.possessionYn	= value;
				if(key.toLowerCase().equals("nsc_list_yn"))		this.nscListYn	= value;
			}
		}
		
		//GetNSPurchasedController.saId = paramMap.get("sa_id");
		
		//GetNSPurchasedController.stbMac = paramMap.get("stb_mac");
		
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null
				|| (paramMap.get("contents_gb") == null && paramMap.get("conts_gb") == null ) )
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.saId, 7, 12, 1))
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.stbMac, 14, 14, 1))
		{
			throw new ImcsException();
		}
				
		if("".equals(this.contsGb) || this.contsGb == null){
			this.contsGb = contentsGb;
		}
						
		if( this.contsGb.length() > 3 || this.pageNo.length() > 5 || this.pageCnt.length() > 5 || this.quickDisYn.length() > 1 || this.fxType.length() > 1 ){
			throw new ImcsException();
		}
		
		// 파라미터 null 체크 및 null일때 Default값 지정
		this.pageNo			= StringUtil.replaceNull(this.pageNo, "A");
		this.pageCnt		= StringUtil.replaceNull(this.pageCnt, "A");
		this.quickDisYn		= StringUtil.replaceNull(this.quickDisYn, "N");
		this.fxType			= StringUtil.replaceNull(this.fxType, "N");
		this.possessionYn	= StringUtil.replaceNull(this.possessionYn, "");
		this.nscListYn		= StringUtil.replaceNull(this.nscListYn, "N");
		
		switch (quickDisYn) {
		case "Y":
		case "N":
			break;

		default:
			throw new ImcsException();
		}
		
		switch (fxType) {
		case "N":
		case "M":
		case "P":
		case "T":
		case "H":
			break;

		default:
			throw new ImcsException();
		}
		
		if(this.contsGb.equals("NSC"))
			this.nscGb		= "LTE";
		else if(contsGb.equals("PAD")) {
			this.contsGb	= "NSC";
			this.nscGb		= "PAD";
		}
		
		switch(nscListYn)
		{
			case "Y":
			case "N":
				break;
			default:
				throw new ImcsException();
		}

	}
	

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getSaId() {
		return saId;
	}

	public void setSaId(String saId) {
		this.saId = saId;
	}

	public String getStbMac() {
		return stbMac;
	}

	public void setStbMac(String stbMac) {
		this.stbMac = stbMac;
	}

	public String getContsGb() {
		return contsGb;
	}

	public void setContsGb(String contsGb) {
		this.contsGb = contsGb;
	}

	public String getPageNo() {
		return pageNo;
	}

	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}

	public String getPageCnt() {
		return pageCnt;
	}

	public void setPageCnt(String pageCnt) {
		this.pageCnt = pageCnt;
	}

	public String getQuickDisYn() {
		return quickDisYn;
	}

	public void setQuickDisYn(String quickDisYn) {
		this.quickDisYn = quickDisYn;
	}

	public String getFxType() {
		return fxType;
	}

	public void setFxType(String fxType) {
		this.fxType = fxType;
	}

	public String getPossessionYn() {
		return possessionYn;
	}

	public void setPossessionYn(String possessionYn) {
		this.possessionYn = possessionYn;
	}

	public String getNscGb() {
		return nscGb;
	}

	public void setNscGb(String nscGb) {
		this.nscGb = nscGb;
	}

	public String getCatId() {
		return catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
	}

	public String getContsId() {
		return contsId;
	}

	public void setContsId(String contsId) {
		this.contsId = contsId;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getResultCode() {
		return resultCode;
	}


	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	
	public String getTestSbc() {
		return testSbc;
	}


	public void setTestSbc(String testSbc) {
		this.testSbc = testSbc;
	}
		

	public String getnSaId() {
		return nSaId;
	}


	public void setnSaId(String nSaId) {
		this.nSaId = nSaId;
	}


	public String getnStbMac() {
		return nStbMac;
	}


	public void setnStbMac(String nStbMac) {
		this.nStbMac = nStbMac;
	}

	
	public String getNscListYn() {
		return nscListYn;
	}

	public void setNscListYn(String nscListYn) {
		this.nscListYn = nscListYn;
	}
	
}