package kr.co.wincom.imcs.api.getNSReposited;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

@SuppressWarnings("serial")
public class GetNSRepositedRequestVO extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * GetNSReposited API 전문 칼럼(순서 일치)
	********************************************************************/
	private String saId			= "";	// [12] 가입자정보
	private String stbMac		= "";	// [14] 가입자 STB MAC Address
	private String contsGb		= "";	// [ 3] 컨텐츠 구분(NSC or PAD, I20, PCT)
	private String pageNo		= "";	// [ 5] 페이지 넘버
	private String pageCnt		= "";	// [ 5] 페이지 개수 (페이지 당 N개)
	private String orderGb		= "";	// [ 1] 정렬 (N:최신순, P:별점순, W:별점순, R:개봉일순)
	private String fxType		= "";	// [ 1] 유플릭스 타입 (M : MobileWeb, P : PcWeb, T : TvgApp, H : HDTV)
	private String quickDisYn	= "";	// [ 1] 퀵배포 컨텐츠 포함 여부
	private String rGrade		= "";	// [ 1] R등급보기여부 (Y:R등급만보기, N:R등급제외보기, A:전체보기)
	private String nscListYn	= "";	// nScreen(STB) 목록 조회 여부
	
	
	/********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
	private String pid			= "";
	private String resultCode	= "";
	private String pIdxSa		= "0";
	private String tempCheck	= "";
	
	private String productId	= "";
	private String buyingDate	= "";
	private String contsType	= "";
	
	private String id			= "";	// 구매정보 조회 시 케이스에 따라 productId 또는 contsId 로 같은 쿼리 조회 시 사용
	private Boolean	bCompareId	= false;
	
	
	
	private String nscGb		= "";
	private String uflixCheck	= "";
	private String contsId		= "";
	private String buyEndDate	= "";
	private String viewType		= "";
	private String uflixYn		= "";
	private String contsName	= "";
	
	private String setPointYn	= "";
	private String nSaId		= "";	// 엔스크린 가입자 정보
	private String nStbMac		= "";	// 엔스트린 가입자 STB MAC Address
	private String testSbc		= "";
	private int nIdxSa			= 0;
	private String sysDate		= "";	// 날짜 비교를 위한 변수
	
	// 이어보기 쿼리 조회시 사용
	// n_Expired_Date 조건에 따라서 saId, nSaId 값을 세팅함.
	private String linkTimeNScreenYn = "N";


	public GetNSRepositedRequestVO(){}
	
	public GetNSRepositedRequestVO(String szParam){
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
				
				if(key.toLowerCase().equals("sa_id"))		this.setSaId(value);
				if(key.toLowerCase().equals("stb_mac"))		this.setStbMac(value);
				if(key.toLowerCase().equals("contents_gb"))	contentsGb = value;
				if(key.toLowerCase().equals("conts_gb"))	this.setContsGb(value);
				if(key.toLowerCase().equals("page_no"))		this.setPageNo(value);
				if(key.toLowerCase().equals("page_cnt"))	this.setPageCnt(value);
				if(key.toLowerCase().equals("fx_type"))		this.setFxType(value);
				if(key.toLowerCase().equals("order_gb"))	this.setOrderGb(value);
				if(key.toLowerCase().equals("quick_dis_yn"))	this.setQuickDisYn(value);
				if(key.toLowerCase().equals("r_grade"))		this.setrGrade(value);
				if(key.toLowerCase().equals("nsc_list_yn"))	this.setNscListYn(value);
			}
		}
		
		//GetNSRepositedController.saId = paramMap.get("sa_id");
		
		//GetNSRepositedController.stbMac = paramMap.get("stb_mac");
		
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null
				|| (paramMap.get("contents_gb") == null
				&& paramMap.get("conts_gb") == null 
				) )
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
		
		if( !"".equals(this.saId) ) {
			this.pIdxSa = this.getSaId().substring(this.getSaId().length() - 2, this.getSaId().length());
			try {
				this.pIdxSa = String.valueOf(Integer.parseInt(this.pIdxSa) % 33);
			} catch (Exception e) {
				this.pIdxSa = "0";
			}
			
		}
		
		// CONTS_GB에 따른 NSC_GB
		if(this.getContsGb() != null && !this.getContsGb().equals("")) {
			if (this.getContsGb().equals("PAD")){
				this.setContsGb("NSC");
				this.setNscGb("PAD");
			} else if(this.getContsGb().equals("NSC")){
				this.setNscGb("LTE");
			} else {
				this.setContsGb("NSC");
				this.setNscGb("LTE");
			}
		} else {
			this.setContsGb("NSC");
			this.setNscGb("LTE");
		}
	
		// 파라미터 null 체크 및 null일때 Default값 지정
		this.pageNo		= StringUtil.replaceNull(this.getPageNo(), "A");
		this.pageCnt	= StringUtil.replaceNull(this.getPageCnt(), "A");
		this.orderGb	= StringUtil.replaceNull(this.getOrderGb(), "N");
		this.rGrade		= StringUtil.replaceNull(this.getrGrade(), "A");
		this.fxType		= StringUtil.replaceNull(this.getFxType(), "N");
		this.nscListYn	= StringUtil.replaceNull(this.getNscListYn(), "N");
		
		if(this.getFxType().equals("M") || this.getFxType().equals("H"))
			this.setContsGb("NSC");
		
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

	public String getOrderGb() {
		return orderGb;
	}

	public void setOrderGb(String orderGb) {
		this.orderGb = orderGb;
	}

	public String getFxType() {
		return fxType;
	}

	public void setFxType(String fxType) {
		this.fxType = fxType;
	}

	public String getQuickDisYn() {
		return quickDisYn;
	}

	public void setQuickDisYn(String quickDisYn) {
		this.quickDisYn = quickDisYn;
	}

	public String getrGrade() {
		return rGrade;
	}

	public void setrGrade(String rGrade) {
		this.rGrade = rGrade;
	}
	
	public String getNscGb() {
		return nscGb;
	}

	public void setNscGb(String nscGb) {
		this.nscGb = nscGb;
	}

	public String getpIdxSa() {
		return pIdxSa;
	}

	public void setpIdxSa(String pIdxSa) {
		this.pIdxSa = pIdxSa;
	}

	public String getContsId() {
		return contsId;
	}

	public void setContsId(String contsId) {
		this.contsId = contsId;
	}

	public String getBuyingDate() {
		return buyingDate;
	}

	public void setBuyingDate(String buyingDate) {
		this.buyingDate = buyingDate;
	}

	public String getBuyEndDate() {
		return buyEndDate;
	}

	public void setBuyEndDate(String buyEndDate) {
		this.buyEndDate = buyEndDate;
	}

	public String getViewType() {
		return viewType;
	}

	public void setViewType(String viewType) {
		this.viewType = viewType;
	}

	public String getUflixYn() {
		return uflixYn;
	}

	public void setUflixYn(String uflixYn) {
		this.uflixYn = uflixYn;
	}

	public String getContsName() {
		return contsName;
	}

	public void setContsName(String contsName) {
		this.contsName = contsName;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getSetPointYn() {
		return setPointYn;
	}

	public void setSetPointYn(String setPointYn) {
		this.setPointYn = setPointYn;
	}

	public String getTempCheck() {
		return tempCheck;
	}

	public void setTempCheck(String tempCheck) {
		this.tempCheck = tempCheck;
	}

	public String getUflixCheck() {
		return uflixCheck;
	}

	public void setUflixCheck(String uflixCheck) {
		this.uflixCheck = uflixCheck;
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

	public String getContsType() {
		return contsType;
	}

	public void setContsType(String contsType) {
		this.contsType = contsType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Boolean getbCompareId() {
		return bCompareId;
	}

	public void setbCompareId(Boolean bCompareId) {
		this.bCompareId = bCompareId;
	}

	public String getNscListYn() {
		return nscListYn;
	}

	public void setNscListYn(String nscListYn) {
		this.nscListYn = nscListYn;
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

	public String getTestSbc() {
		return testSbc;
	}

	public void setTestSbc(String testSbc) {
		this.testSbc = testSbc;
	}

	public int getnIdxSa() {
		return nIdxSa;
	}

	public void setnIdxSa(int nIdxSa) {
		this.nIdxSa = nIdxSa;
	}

	public String getSysDate() {
		return sysDate;
	}

	public void setSysDate(String sysDate) {
		this.sysDate = sysDate;
	}

	public String getLinkTimeNScreenYn() {
		return linkTimeNScreenYn;
	}

	public void setLinkTimeNScreenYn(String linkTimeNScreenYn) {
		this.linkTimeNScreenYn = linkTimeNScreenYn;
	}
}
