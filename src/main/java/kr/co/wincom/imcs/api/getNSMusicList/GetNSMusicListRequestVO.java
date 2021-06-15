package kr.co.wincom.imcs.api.getNSMusicList;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.curation.common.validation.ValidationParameter;
import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

@SuppressWarnings("serial")
public class GetNSMusicListRequestVO extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * getNSMusicList API 전문 칼럼(순서 일치)
	********************************************************************/
	private String saId			  = "";	// 가입자정보
	private String stbMac		  = "";	// 가입자 STB MAC Address
	private String rqsType		  = "";	// 
	private String pageNo		  = "";	// 페이지 넘버
	private String pageCnt		  = "";	// 요청 페이지 개수 (페이지 당 N개)
   
	/********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
	private String 	scheType      = "";
	private String 	albumId       = "";
	private String 	qsheetId      = "";
	private String 	concertName   = "";
	private String 	concertDate   = "";
	private String 	place         = "";
	private String 	omniviewYn    = "";
	private String 	imageUrl      = "";
	private String 	imageFileName = "";
	private String 	categoryId    = "";
	private String  serviceId     = "";
	private String  testSbc		  = "";
	private String  viewFlag	  = "";
	private String  startNum      = ""; //조회 시작 RowNum
	private String  endNum        = ""; //조회 종료 RowNum
	private String  nRqsType       = "";
	
    private String pid			= "";
    private String resultCode	= "";
    private long tp_start 		= 0;
    
    public GetNSMusicListRequestVO(){}
    
    public GetNSMusicListRequestVO(String szParam){
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
				if(key.toLowerCase().equals("rqs_type"))	this.rqsType = value;
				if(key.toLowerCase().equals("page_no"))		this.pageNo  = value;
				if(key.toLowerCase().equals("page_cnt"))	this.pageCnt = value;
			}
		}
		
		//가번이 없거나 길이가 맞지 않으면 안됨. 맥번호가 없거나 길이가 맞지 않으면 안됨. RQS_TYPE 없거나 길이가 맞지 않으면 안됨.
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null )
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
		
	    // [보정]
	    // RQS_TYPE(요청구분). A:전체, 0:공연(방송)예정, 1:공연(방송)중, 2:공연(방송)후
		// 허용하지 않는 값으로 호출시 Default "A"
	    if (this.rqsType == null || this.rqsType.equals("")) 
	    	throw new ImcsException();
	    
	    if ((!this.rqsType.equals("A")) && (!this.rqsType.equals("0")) && (!this.rqsType.equals("1")) && (!this.rqsType.equals("2")))
	    {
	        this.rqsType = "A";
	    }
	    
	    // [보정]
	    // PAGE_NO(요청 페이지). 0 or 1:첫번째 페이지, 2~n:해당페이지
	    if (this.pageNo == null) this.pageNo = "1";		     // 비워 들어왔을때 확인
	    if (this.pageNo.equals("0")) this.pageNo = "1";      // 0 -> 1
	    if (!ValidationParameter.CheckNumber(this.pageNo, true)) this.pageNo = "1"; // 숫자만 가능
	    
	    // [보정]
	    // PAGE_CNT(요청 건수). 1 or null : 전체 건수 제공, 1~99 : 요청한 건수만큼 리스트 제공
	    if (this.pageCnt == null) this.pageCnt = "0";		     // 비워 들어왔을때 확인
	    if (!ValidationParameter.CheckNumber(this.pageCnt, true)) this.pageCnt = "0"; // 숫자만 가능
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

	public String getRqsType() {
		return rqsType;
	}

	public void setRqsType(String rqsType) {
		this.rqsType = rqsType;
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

	public String getScheType() {
		return scheType;
	}

	public void setScheType(String scheType) {
		this.scheType = scheType;
	}

	public String getAlbumId() {
		return albumId;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}

	public String getQsheetId() {
		return qsheetId;
	}

	public void setQsheetId(String qsheetId) {
		this.qsheetId = qsheetId;
	}

	public String getConcertName() {
		return concertName;
	}

	public void setConcertName(String concertName) {
		this.concertName = concertName;
	}

	public String getConcertDate() {
		return concertDate;
	}

	public void setConcertDate(String concertDate) {
		this.concertDate = concertDate;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getOmniviewYn() {
		return omniviewYn;
	}

	public void setOmniviewYn(String omniviewYn) {
		this.omniviewYn = omniviewYn;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getTestSbc() {
		return testSbc;
	}

	public void setTestSbc(String testSbc) {
		this.testSbc = testSbc;
	}

	public String getViewFlag() {
		return viewFlag;
	}

	public void setViewFlag(String viewFlag) {
		this.viewFlag = viewFlag;
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

	public String getnRqsType() {
		return nRqsType;
	}

	public void setnRqsType(String nRqsType) {
		this.nRqsType = nRqsType;
	}


	
	
    
}
