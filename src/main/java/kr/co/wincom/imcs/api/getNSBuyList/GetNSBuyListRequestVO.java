package kr.co.wincom.imcs.api.getNSBuyList;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.curation.common.validation.ValidationParameter;
import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

@SuppressWarnings("serial")
public class GetNSBuyListRequestVO extends NoSqlLoggingVO implements Serializable
{

	/********************************************************************
	 * getNSPurchased API 전문 칼럼(순서 일치)
	********************************************************************/
    private String saId				= "";
    private String stbMac			= "";
    private String pageNo			= "";	// 페이지 넘버
	private String pageCnt			= "";	// 요청 페이지 개수 (페이지 당 N개)
	private String fxType			= "";
	private String possessionYn		= "";
	private String expiredFlag		= "";
	private String nscListYn		= "";
	
	private String applType			= "";
	private String requestFlag		= ""; // K : 키즈홈 마이메뉴, B : 키즈홈 책읽어주는 TV, M : 아이돌 라이브
	
	/********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
	private String nSaId			= "";
	private String nStbMac			= "";
	private String testSbc			= "";
	private String nscPairYn		= "";
	
	private String appFlag			= "";
	
	private String startNum     	= ""; //조회 시작 RowNum
	private String endNum        	= ""; //조회 종료 RowNum
	
	private int resultSet		= 0;
	
	private Integer pIdxSa			= 0;
    
    private String pid			= "";
    private String resultCode	= "";
    
    
	
	public GetNSBuyListRequestVO(){}
	
	public GetNSBuyListRequestVO(String szParam){
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
				
				/*if( !value.matches(ImcsConstants.N_SP_PTN) && key.toLowerCase().indexOf("name") == -1 ){
					//특수문자 있음
					throw new ImcsException();
				}*/
				
				if(key.toLowerCase().equals("sa_id"))			this.saId = value;
				if(key.toLowerCase().equals("stb_mac"))			this.stbMac = value;
				if(key.toLowerCase().equals("page_no"))			this.pageNo  = value;
				if(key.toLowerCase().equals("page_cnt"))		this.pageCnt = value;
				if(key.toLowerCase().equals("fx_type"))			this.fxType = value;
				if(key.toLowerCase().equals("possession_yn"))	this.possessionYn = value;
				if(key.toLowerCase().equals("expired_flag"))	this.expiredFlag  = value;
				if(key.toLowerCase().equals("nsc_list_yn"))		this.nscListYn = value;
				if(key.toLowerCase().equals("app_type"))		this.applType = value;
				if(key.toLowerCase().equals("request_flag"))	this.requestFlag = value;

			}
		}
		
		//GetNSMultiContsController.saId = paramMap.get("sa_id");
		//GetNSMultiContsController.stbMac = paramMap.get("stb_mac");
	    
		if(!commonService.getValidParam(this.saId, 7, 12, 1))
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.stbMac, 14, 14, 1))
		{
			throw new ImcsException();
		}
		
		if(this.pageNo.length() > 5 || this.pageNo.length() < 0 ){
			throw new ImcsException();
		}
		
		if(this.pageCnt.length() > 5 || this.pageCnt.length() < 0 ){
			throw new ImcsException();
		}
		if(this.fxType.length() > 1 || this.fxType.length() < 0 ){
			throw new ImcsException();
		}
		if(this.possessionYn.length() > 1 || this.possessionYn.length() < 0 ){
			throw new ImcsException();
		}
		if(this.expiredFlag.length() > 1 || this.expiredFlag.length() < 0 ){
			throw new ImcsException();
		}
		if(this.nscListYn.length() > 1 || this.nscListYn.length() < 0 ){
			throw new ImcsException();
		}
		if(this.applType.length() != 4 ){
			throw new ImcsException();
		}
		
		this.fxType	= StringUtil.replaceNull(this.fxType, "H");
		
		switch (fxType) {
		case "M":
		case "P":
		case "T":
		case "H":
			break;
		default:
			throw new ImcsException();
		}
		
		this.possessionYn	= StringUtil.replaceNull(this.possessionYn, "A");
		
		switch (possessionYn) {
		case "Y":
		case "N":
		case "A":
			break;
		default:
			throw new ImcsException();
		}
		
		this.expiredFlag	= StringUtil.replaceNull(this.expiredFlag, "N");
		
		switch (expiredFlag) {
		case "Y":
		case "N":
			break;
		default:
			throw new ImcsException();
		}
		
		this.nscListYn	= StringUtil.replaceNull(this.nscListYn, "N");
		
		switch (nscListYn) {
		case "A":
		case "Y":
		case "N":
			break;
		default:
			throw new ImcsException();
		}
		
		//this.applType	= applType.substring(0, 1);
		
		this.requestFlag = StringUtil.replaceNull(this.requestFlag, "X");
		
		// [보정]
	    // PAGE_NO(요청 페이지). 0 or 1:첫번째 페이지, 2~n:해당페이지
	    if (this.pageNo == null) this.pageNo = "1";		     // 비워 들어왔을때 확인
	    if (this.pageNo.equals("0")) this.pageNo = "1";      // 0 -> 1
	    if (!ValidationParameter.CheckNumber(this.pageNo, true)) this.pageNo = "1"; // 숫자만 가능
	    
	    // [보정]
	    // PAGE_CNT(요청 건수). 1 or null : 전체 건수 제공, 1~99 : 요청한 건수만큼 리스트 제공
	    if (this.pageCnt == null) this.pageCnt = "1";		     // 비워 들어왔을때 확인
	    if (this.pageCnt.equals("0")) this.pageCnt = "1";      // 0 -> 1
	    if (!ValidationParameter.CheckNumber(this.pageCnt, true)) this.pageCnt = "1"; // 숫자만 가능
	    
	    
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

	public int getResultSet() {
		return resultSet;
	}

	public void setResultSet(int resultSet) {
		this.resultSet = resultSet;
	}
	public Integer getpIdxSa() {
		return pIdxSa;
	}

	public void setpIdxSa(Integer pIdxSa) {
		this.pIdxSa = pIdxSa;
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

	public String getExpiredFlag() {
		return expiredFlag;
	}

	public void setExpiredFlag(String expiredFlag) {
		this.expiredFlag = expiredFlag;
	}

	public String getNscListYn() {
		return nscListYn;
	}

	public void setNscListYn(String nscListYn) {
		this.nscListYn = nscListYn;
	}

	public String getApplType() {
		return applType;
	}

	public void setApplType(String applType) {
		this.applType = applType;
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

	public String gettestSbc() {
		return testSbc;
	}

	public void settestSbc(String testSbc) {
		this.testSbc = testSbc;
	}

	public String getnscPairYn() {
		return nscPairYn;
	}

	public void setnscPairYn(String nscPairYn) {
		this.nscPairYn = nscPairYn;
	}

	public String getappFlag() {
		return appFlag;
	}

	public void setappFlag(String appFlag) {
		this.appFlag = appFlag;
	}
	public String getRequestFlag() {
		return requestFlag;
	}
	public void setRequestFlag(String requestFlag) {
		this.requestFlag = requestFlag;
	}
}
