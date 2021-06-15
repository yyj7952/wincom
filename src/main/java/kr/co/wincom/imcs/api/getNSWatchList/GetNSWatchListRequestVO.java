package kr.co.wincom.imcs.api.getNSWatchList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import kr.co.wincom.curation.common.validation.ValidationParameter;
import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

@SuppressWarnings("serial")
public class GetNSWatchListRequestVO extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * getNSSeriesList API 전문 칼럼(순서 일치)
	********************************************************************/
    private String    saId = "";
    private String    macAddr = "";
    private String    pageNo = "";
    private String    pageCnt = "";
    private String    fxType = "";
    private String    rGrade = "";
    private String    expiredFlag = "";
    private String    seriesFlag = "";
    private String    nextFlag = "";
    private String    rqsFlag = "";
    private String    rqsFlagTmp = "";
    private String    nscList = "";
    private String    testSbc = "";
    private String    appType = "";
    private String    appFlag = "";
    private String    stbSaId = "";
    private String    stbMac = "";
    private String    stbPairing = "";
    private int    	  idxSa = 0;
    private String    sysdate = "";
    private String    conSaId = "";
    private String    conMac = "";
    
    //2020/01/03
    private int    	  idxSa2 = 0;
    private String    conSaId2 = "";
    private String    conMac2 = "";
    
    //2020/08/10
    private String	  possessionYn = "";

	/********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
	private String pid	 		  = "";
    private String resultCode	  = "";
    private long tp_start 		  = 0;
	private String  startNum      = ""; //조회 시작 RowNum
	private String  endNum        = ""; //조회 종료 RowNum

	private int inscCustProdCount	  = 0;
	private int iiptvCustProdCount	  = 0;
	private List<String> hca_prod;
	private List<String> iptv_prod;
	private List<String> hca_ser = new ArrayList<String>();
	
	// 목록 전체 개수를 확인 한다.
	private int iTotalCount = 0; 
	
	private String albumId = ""; // CATEGORY_TYPE(컨텐츠 시청 구분) 을 구하기 위해 쿼리 파라미터로 사용
    
    public GetNSWatchListRequestVO(){}
    
    public GetNSWatchListRequestVO(String szParam){
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
				
				if(key.toLowerCase().equals("sa_id"))			this.saId    = value; 
				if(key.toLowerCase().equals("stb_mac"))			this.macAddr  = value;
				if(key.toLowerCase().equals("page_no"))			this.pageNo   = value;
				if(key.toLowerCase().equals("page_cnt"))		this.pageCnt  = value;
				if(key.toLowerCase().equals("fx_type"))			this.fxType = value;
				if(key.toLowerCase().equals("r_grade"))			this.rGrade = value;
				if(key.toLowerCase().equals("expired_flag"))	this.expiredFlag = value;
				if(key.toLowerCase().equals("series_flag"))		this.seriesFlag = value;
				if(key.toLowerCase().equals("next_series_yn"))	this.nextFlag = value;
				if(key.toLowerCase().equals("nsc_list_yn"))		this.nscList = value;
				if(key.toLowerCase().equals("request_flag"))	this.rqsFlag = value;
				if(key.toLowerCase().equals("app_type"))		this.appType = value;
				if(key.toLowerCase().equals("possession_yn"))	this.possessionYn = value;
			}
		}
		
		//가번이 없거나 길이가 맞지 않으면 안됨. 맥번호가 없거나 길이가 맞지 않으면 안됨. RQS_TYPE 없거나 길이가 맞지 않으면 안됨.
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null  
		 || paramMap.get("page_no") == null || paramMap.get("page_cnt") == null
		 || paramMap.get("app_type") == null)
		{
			throw new ImcsException();
		}
	    
		if(!commonService.getValidParam(this.saId, 7, 12, 1))
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.macAddr, 14, 14, 1))
		{
			throw new ImcsException();
		}
	    
	    if((this.pageNo.length() < 1 || this.pageNo.length() > 5))
	    {	    	
	    	throw new ImcsException();
	    }
	    else
	    {
	    	try{
	    		Integer.parseInt(this.pageNo);
	    	}catch(NumberFormatException e)
	    	{
	    		throw new ImcsException();
	    	}
	    }
	    
	    if((this.pageCnt.length() < 1 || this.pageCnt.length() > 5))
	    {	    	
	    	throw new ImcsException();
	    }
	    else
	    {
	    	try{
	    		Integer.parseInt(this.pageCnt);
	    	}catch(NumberFormatException e)
	    	{
	    		throw new ImcsException();
	    	}
	    }
	    
	    if(this.fxType.length() > 1)
	    {	    	
	    	throw new ImcsException();
	    }

	    if(this.rGrade.length() > 1)
	    {	    	
	    	throw new ImcsException();
	    }
	    else
	    {
	    	if(Pattern.matches("^[a-zA-Z]*$", this.rGrade) == false)
	    	{
	    		throw new ImcsException();
	    	}
	    }
	    
	    if(this.expiredFlag.length() > 1)
	    {	    	
	    	throw new ImcsException();
	    }
	    
	    if(this.seriesFlag.length() > 1)
	    {	    	
	    	throw new ImcsException();
	    }
	    
	    if(this.nextFlag.length() > 1)
	    {	    	
	    	throw new ImcsException();
	    }
	    
	    if(this.nscList.length() > 1)
	    {	    	
	    	throw new ImcsException();
	    }
	    
	    if(this.rqsFlag.length() > 1)
	    {	    	
	    	throw new ImcsException();
	    }
	    
	    if(this.appType.length() != 4)
	    {	    	
	    	throw new ImcsException();
	    }
	    
	    if (this.possessionYn.length() > 1)
	    {
	    	throw new ImcsException();
	    }
	    
	    if (!this.rGrade.equals("Y") && !this.rGrade.equals("N")) {
	    	this.setrGrade("A");
	    }
	    
	    if (!this.expiredFlag.equals("Y")) {
	    	this.setExpiredFlag("N");
	    }

	    if (!this.seriesFlag.equals("Y")) {
	    	this.setSeriesFlag("N");
	    }
	    
	    if (!this.nextFlag.equals("Y")) {
	    	this.setNextFlag("N");
	    }
	    
	    if (!(this.nscList.equals("Y") || this.nscList.equals("A"))) {
	    	this.setNscList("N");
	    }
	    
	    if (this.rqsFlag.equals("H") || this.rqsFlag.equals("U") || this.rqsFlag.equals("K") || this.rqsFlag.equals("B") || this.rqsFlag.equals("C")) {
	    	if(this.rqsFlag.equals("C"))
	    	{
	    		this.setRqsFlag("K");
	    		this.setRqsFlagTmp("C");
	    	}
	    } else {
	    	this.setRqsFlag("N");
	    }
	    
	    if (this.possessionYn.equals("A") || this.possessionYn.equals("Y") || this.possessionYn.equals("") || this.possessionYn == null) {
	    } else {
	    	throw new ImcsException();
	    }
	    
	    //나중에 VR 앱 적용시 파라메터 받아서 처리해야 함 (우선 변수만 설정)
	    if (this.appType.substring(0, 1).equals("E")) {
	    	this.setAppFlag("V");
	    } else if (this.appType.substring(0, 1).equals("F")) {
			this.setAppFlag("F");
	    } else {
	    	this.setAppFlag("M");
	    }

//	    // [보정]
//	    // PAGE_NO(요청 페이지). 0 or 1:첫번째 페이지, 2~n:해당페이지
//	    if (this.pageNo == null) this.pageNo = "0";		     // 비워 들어왔을때 확인
//	    if (this.pageNo.equals("0")) this.pageNo = "0";      // 0 -> 1
//	    if (!ValidationParameter.CheckNumber(this.pageNo, true)) this.pageNo = "0"; // 숫자만 가능
//	    
//	    // [보정]
//	    // PAGE_CNT(요청 건수). 1 or null : 전체 건수 제공, 1~99 : 요청한 건수만큼 리스트 제공
//	    if (this.pageCnt == null) this.pageCnt = "0";		     // 비워 들어왔을때 확인
//	    if (!ValidationParameter.CheckNumber(this.pageCnt, true)) this.pageCnt = "0"; // 숫자만 가능
		
	}

	public String getSaId() {
		return saId;
	}

	public void setSaId(String saId) {
		this.saId = saId;
	}

	public String getMacAddr() {
		return macAddr;
	}

	public void setMacAddr(String macAddr) {
		this.macAddr = macAddr;
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

	public String getFxType() {
		return fxType;
	}

	public void setFxType(String fxType) {
		this.fxType = fxType;
	}

	public String getrGrade() {
		return rGrade;
	}

	public void setrGrade(String rGrade) {
		this.rGrade = rGrade;
	}

	public String getExpiredFlag() {
		return expiredFlag;
	}

	public void setExpiredFlag(String expiredFlag) {
		this.expiredFlag = expiredFlag;
	}

	public String getSeriesFlag() {
		return seriesFlag;
	}

	public void setSeriesFlag(String seriesFlag) {
		this.seriesFlag = seriesFlag;
	}

	public String getNextFlag() {
		return nextFlag;
	}

	public void setNextFlag(String nextFlag) {
		this.nextFlag = nextFlag;
	}

	public String getRqsFlag() {
		return rqsFlag;
	}

	public void setRqsFlag(String rqsFlag) {
		this.rqsFlag = rqsFlag;
	}

	public String getNscList() {
		return nscList;
	}

	public void setNscList(String nscList) {
		this.nscList = nscList;
	}

	public String getTestSbc() {
		return testSbc;
	}

	public void setTestSbc(String testSbc) {
		this.testSbc = testSbc;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public String getAppFlag() {
		return appFlag;
	}

	public void setAppFlag(String appFlag) {
		this.appFlag = appFlag;
	}

	public String getStbSaId() {
		return stbSaId;
	}

	public void setStbSaId(String stbSaId) {
		this.stbSaId = stbSaId;
	}

	public String getStbMac() {
		return stbMac;
	}

	public void setStbMac(String stbMac) {
		this.stbMac = stbMac;
	}

	public String getStbPairing() {
		return stbPairing;
	}

	public void setStbPairing(String stbPairing) {
		this.stbPairing = stbPairing;
	}

	public int getIdxSa() {
		return idxSa;
	}

	public void setIdxSa(int idxSa) {
		this.idxSa = idxSa;
	}

	public String getSysdate() {
		return sysdate;
	}

	public void setSysdate(String sysdate) {
		this.sysdate = sysdate;
	}

	public String getConSaId() {
		return conSaId;
	}

	public void setConSaId(String conSaId) {
		this.conSaId = conSaId;
	}

	public String getConMac() {
		return conMac;
	}

	public void setConMac(String conMac) {
		this.conMac = conMac;
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

	public long getTp_start() {
		return tp_start;
	}

	public void setTp_start(long tp_start) {
		this.tp_start = tp_start;
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

	public int getiTotalCount() {
		return iTotalCount;
	}

	public void setiTotalCount(int iTotalCount) {
		this.iTotalCount = iTotalCount;
	}

	public int getInscCustProdCount() {
		return inscCustProdCount;
	}

	public void setInscCustProdCount(int inscCustProdCount) {
		this.inscCustProdCount = inscCustProdCount;
	}

	public int getIiptvCustProdCount() {
		return iiptvCustProdCount;
	}

	public void setIiptvCustProdCount(int iiptvCustProdCount) {
		this.iiptvCustProdCount = iiptvCustProdCount;
	}

	public List<String> getHca_prod() {
		return hca_prod;
	}

	public void setHca_prod(List<String> hca_prod) {
		this.hca_prod = hca_prod;
	}

	public List<String> getIptv_prod() {
		return iptv_prod;
	}

	public void setIptv_prod(List<String> iptv_prod) {
		this.iptv_prod = iptv_prod;
	}

	public List<String> getHca_ser() {
		return hca_ser;
	}

	public void setHca_ser_List(List<String> hca_ser) {
		this.hca_ser = hca_ser;
	}
	
	public void setHca_ser(String hca_ser) {
		this.hca_ser.add(hca_ser);
	}

	public String getConSaId2() {
		return conSaId2;
	}

	public void setConSaId2(String conSaId2) {
		this.conSaId2 = conSaId2;
	}

	public String getConMac2() {
		return conMac2;
	}

	public void setConMac2(String conMac2) {
		this.conMac2 = conMac2;
	}

	public int getIdxSa2() {
		return idxSa2;
	}

	public void setIdxSa2(int idxSa2) {
		this.idxSa2 = idxSa2;
	}
	public String getAlbumId() {
		return albumId;
	}
	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}

	public String getPossessionYn() {
		return possessionYn;
	}

	public void setPossessionYn(String possessionYn) {
		this.possessionYn = possessionYn;
	}

	public String getRqsFlagTmp() {
		return rqsFlagTmp;
	}

	public void setRqsFlagTmp(String rqsFlagTmp) {
		this.rqsFlagTmp = rqsFlagTmp;
	}
	
}
