package kr.co.wincom.imcs.api.getNSSeriesList;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.curation.common.validation.ValidationParameter;
import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

@SuppressWarnings("serial")
public class GetNSSeriesListRequestVO extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * getNSSeriesList API 전문 칼럼(순서 일치)
	********************************************************************/
	private String saId			  = "";	// 가입자정보
	private String stbMac		  = "";	// 가입자 STB MAC Address
	private String catId		  = "";	// 카테고리 ID
	private String pageNo		  = "";	// 페이지 넘버
	private String pageCnt		  = "";	// 요청 페이지 개수 (페이지 당 N개)
	private String rqsNo		  = "";	// 요청 시리즈 번호
	private String orderGb		  = "";	// 정렬옵션
	private String orderGbTmp	  = "";	// 정렬옵션 (현재회차 이동 용..)
	private String rqsType		  = "V";	// 요청구분

	/********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
	private String pid	 		  = "";
    private String resultCode	  = "";
    private long tp_start 		  = 0;
	private String  startNum      = ""; //조회 시작 RowNum
	private String  endNum        = ""; //조회 종료 RowNum
	
	private int  idxSa         	  = 0;
	private String  closeYn      = "";
	private String  seriesId     = "";
	private String  focusId      = "";
	private String  testSbc		  = "";
	private String  vatRate		  = "";
	private String  currPage	  = "";
	private String  viewFlag1	  = "";
	private String  viewFlag2	  = "";
	private String  orderType     = "";
	private String  bgnNo         = "";
	private String  endNo         = "";
	private String  serCatId      = "";
	private String  serCatNm      = "";
	
	private String serno_disp_yn	= "";
	private String pkg_yn			= "";
	
	// 2020.12.22 - 모바일 아이들나라 인앱결제 지원
	private String appType		= "";	// 서비스명
    
    public GetNSSeriesListRequestVO(){}
    
    public GetNSSeriesListRequestVO(String szParam){
    	CommonService commonService = new CommonService();
    	
    	String[] arrStat = szParam.split("\\|");
		
		String key		= "";
		String value	= "";
		int    nStr		= 0;
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		
		for(int i = 0; i < arrStat.length; i++){
			nStr	= arrStat[i].indexOf("=");
			
			if(nStr > 0) {
				key = arrStat[i].substring(0, nStr).toLowerCase().trim();
				value = arrStat[i].substring(nStr + 1, arrStat[i].length()).trim();
				
				paramMap.put(key, value);
				
				if(key.toLowerCase().equals("sa_id"))		this.saId    = value; 
				if(key.toLowerCase().equals("stb_mac"))		this.stbMac  = value;
				if(key.toLowerCase().equals("cat_id"))		this.catId   = value;
				if(key.toLowerCase().equals("page_no"))		this.pageNo  = value;
				if(key.toLowerCase().equals("page_cnt"))	this.pageCnt = value;
				if(key.toLowerCase().equals("rqs_no"))		this.rqsNo = value;
				if(key.toLowerCase().equals("order_gb"))	this.orderGb = value;
				if(key.toLowerCase().equals("rqs_type"))	this.rqsType = value;
				if(key.toLowerCase().equals("app_type"))	this.appType = value;
			}
		}
		
		this.rqsType	= StringUtil.replaceNull(this.rqsType, "V");
		
		if(!commonService.getValidParam(this.saId, 7, 12, 1))
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.stbMac, 14, 14, 1))
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.catId, 4, 5, 1))
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.rqsType, 1, 1, 2))
		{
			throw new ImcsException();
		}
		
		
	    if (this.rqsNo == null || this.rqsNo.equals("")) {
	    	this.setRqsNo("0");
	    }
		
	    if (this.orderGb == null || this.orderGb.equals("")) {
	    	this.setOrderGb("A");
	    }
	    
	    if(paramMap.get("app_type") == null)
		{
			this.appType = "RUSA";
		}
	    
	    if(!commonService.getValidParam(this.appType, 4, 4, 2))
		{
			throw new ImcsException();
		}
	    
	    switch (this.orderGb) {
			case "N":
			case "S":
			case "A":
				break;
			case "F":	// 2020.08.10 - 현재회차이동 버튼 클릭시 정렬 기준
				this.orderGb = "A";
				this.orderGbTmp = "F";
				break;
	
			default:
				throw new ImcsException();
		}
	    
	    // [보정]
	    // PAGE_NO(요청 페이지). 0 or 1:첫번째 페이지, 2~n:해당페이지
	    if (this.pageNo == null || this.pageNo.equals("")) this.pageNo = "0";		     // 비워 들어왔을때 확인	    
	    if (!ValidationParameter.CheckNumber(this.pageNo, true)) this.pageNo = "0"; // 숫자만 가능
	    if (Integer.parseInt(this.pageNo) < 0) this.pageNo = "0"; // 양수만 가능
	    
	    // [보정]
	    // PAGE_CNT(요청 건수). 1 or null : 전체 건수 제공, 1~99 : 요청한 건수만큼 리스트 제공
	    if (this.pageCnt == null || this.pageCnt.equals("")) this.pageCnt = "0";		     // 비워 들어왔을때 확인	    
	    if (!ValidationParameter.CheckNumber(this.pageCnt, true)) this.pageCnt = "0"; // 숫자만 가능
	    if (Integer.parseInt(this.pageCnt) < 0) this.pageCnt = "0"; // 양수만 가능
	    
	    // [보정]
	    // RQS_NO(시리즈 번호). null : 무시 , 1~99 : 요청한 시리즈 번호부터 리스트 제공
	    if (this.rqsNo == null || this.rqsNo.equals("")) this.rqsNo = "0";		     // 비워 들어왔을때 확인	    
	    if (!ValidationParameter.CheckNumber(this.rqsNo, true)) this.rqsNo = "0"; // 숫자만 가능
	    if (Integer.parseInt(this.rqsNo) < 0) this.rqsNo = "0"; // 양수만 가능
		
	    
	    switch (this.rqsType) {
			case "V":
			case "M":
				break;	
			default:
				throw new ImcsException();
		}
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

	public String getCatId() {
		return catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
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

	public String getRqsNo() {
		return rqsNo;
	}

	public void setRqsNo(String rqsNo) {
		this.rqsNo = rqsNo;
	}

	public String getOrderGb() {
		return orderGb;
	}

	public void setOrderGb(String orderGb) {
		this.orderGb = orderGb;
	}
	
	public String getOrderGbTmp() {
		return orderGbTmp;
	}

	public void setOrderGbTmp(String orderGbTmp) {
		this.orderGbTmp = orderGbTmp;
	}

	public String getRqsType() {
		return rqsType;
	}

	public void setRqsType(String rqsType) {
		this.rqsType = rqsType;
	}

	public long getTp_start() {
		return tp_start;
	}

	public void setTp_start(long tp_start) {
		this.tp_start = tp_start;
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

	public String getViewFlag1() {
		return viewFlag1;
	}

	public void setViewFlag1(String viewFlag1) {
		this.viewFlag1 = viewFlag1;
	}

	public String getStartNum() {
		return startNum;
	}

	public void setStartNum(String startNum) {
		this.startNum = startNum;
	}

	public String getEndNum() {
		return endNum;
	}

	public void setEndNum(String endNum) {
		this.endNum = endNum;
	}

	public int getIdxSa() {
		return idxSa;
	}

	public void setIdxSa(int idxSa) {
		this.idxSa = idxSa;
	}

	public String getCloseYn() {
		return closeYn;
	}

	public void setCloseYn(String closeYn) {
		this.closeYn = closeYn;
	}

	public String getVatRate() {
		return vatRate;
	}

	public void setVatRate(String vatRate) {
		this.vatRate = vatRate;
	}

	public String getCurrPage() {
		return currPage;
	}

	public void setCurrPage(String currPage) {
		this.currPage = currPage;
	}

	public String getViewFlag2() {
		return viewFlag2;
	}

	public void setViewFlag2(String viewFlag2) {
		this.viewFlag2 = viewFlag2;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getBgnNo() {
		return bgnNo;
	}
	
	

	public String getSeriesId() {
		return seriesId;
	}

	public void setSeriesId(String seriesId) {
		this.seriesId = seriesId;
	}

	public String getFocusId() {
		return focusId;
	}

	public void setFocusId(String focusId) {
		this.focusId = focusId;
	}

	public void setBgnNo(String bgnNo) {
		this.bgnNo = bgnNo;
	}

	public String getEndNo() {
		return endNo;
	}

	public void setEndNo(String endNo) {
		this.endNo = endNo;
	}

	public String getSerCatId() {
		return serCatId;
	}

	public void setSerCatId(String serCatId) {
		this.serCatId = serCatId;
	}

	public String getSerCatNm() {
		return serCatNm;
	}

	public void setSerCatNm(String serCatNm) {
		this.serCatNm = serCatNm;
	}
	public String getSerno_disp_yn() {
		return serno_disp_yn;
	}
	public void setSerno_disp_yn(String serno_disp_yn) {
		this.serno_disp_yn = serno_disp_yn;
	}
	public String getPkg_yn() {
		return pkg_yn;
	}
	public void setPkg_yn(String pkg_yn) {
		this.pkg_yn = pkg_yn;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}
  
}
