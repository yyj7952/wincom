package kr.co.wincom.imcs.api.getNSContInfo;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

@SuppressWarnings("serial")
public class GetNSContInfoRequestVO extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * getNSContInfo API 전문 칼럼(순서 일치)
	********************************************************************/
	private String saId			  = "";	// 가입자정보
	private String stbMac		  = "";	// 가입자 STB MAC Address
	private String albumId        = ""; // 앨범 ID
	private String catId          = ""; // 카테고리ID
	private String fxType         = ""; // 유플릭스 타입
	private String rqsType		  = "";	// 요청구분
   
	/********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
	private String testSbc        = "";
	private String catGb          = "";
	private String custFlag       = "";
	private String viewFlag       = "";
	private String nscGb          = "";
	private String isNew          = "";
	private String isUpdate       = "";
	private String vatRate        = "";
	private int catChkCnt         = 0;
	private String sysData        = "";
    private String pid			  = "";
    private String resultCode	  = "";
    private long tp_start 		  = 0;
    
    public GetNSContInfoRequestVO(){}
    
    public GetNSContInfoRequestVO(String szParam){
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
				if(key.toLowerCase().equals("album_id"))	this.albumId = value;
				if(key.toLowerCase().equals("cat_id"))		this.catId   = value;
				if(key.toLowerCase().equals("fx_type"))		this.fxType = value;
				if(key.toLowerCase().equals("rqs_type"))	this.rqsType = value;
				
			}
		}
		
		//가번이 없거나 길이가 맞지 않으면 안됨. 맥번호가 없거나 길이가 맞지 않으면 안됨. RQS_TYPE 없거나 길이가 맞지 않으면 안됨.
//		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null  
		if( paramMap.get("album_id") == null || paramMap.get("cat_id") == null 
				|| paramMap.get("fx_type") == null || paramMap.get("rqs_type") == null)
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
		
		if(!commonService.getValidParam(this.albumId, 15, 15, 1))
		{
			throw new ImcsException();
		}
		
		//요청구분이 공연이 아닐 경우 무조건 "V"
		if ( !this.rqsType.equals("M") ) 
			this.rqsType = "V";
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

	public String getAlbumId() {
		return albumId;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}

	public String getCatId() {
		return catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
	}

	public String getFxType() {
		return fxType;
	}

	public void setFxType(String fxType) {
		this.fxType = fxType;
	}

	public String getTestSbc() {
		return testSbc;
	}

	public void setTestSbc(String testSbc) {
		this.testSbc = testSbc;
	}

	public String getCatGb() {
		return catGb;
	}

	public void setCatGb(String catGb) {
		this.catGb = catGb;
	}

	public String getCustFlag() {
		return custFlag;
	}

	public void setCustFlag(String custFlag) {
		this.custFlag = custFlag;
	}

	public String getViewFlag() {
		return viewFlag;
	}

	public void setViewFlag(String viewFlag) {
		this.viewFlag = viewFlag;
	}

	public String getNscGb() {
		return nscGb;
	}

	public void setNscGb(String nscGb) {
		this.nscGb = nscGb;
	}

	public String getIsNew() {
		return isNew;
	}

	public void setIsNew(String isNew) {
		this.isNew = isNew;
	}

	public String getIsUpdate() {
		return isUpdate;
	}

	public void setIsUpdate(String isUpdate) {
		this.isUpdate = isUpdate;
	}

	public String getVatRate() {
		return vatRate;
	}

	public void setVatRate(String vatRate) {
		this.vatRate = vatRate;
	}

	public String getSysData() {
		return sysData;
	}

	public void setSysData(String sysData) {
		this.sysData = sysData;
	}

	public int getCatChkCnt() {
		return catChkCnt;
	}

	public void setCatChkCnt(int catChkCnt) {
		this.catChkCnt = catChkCnt;
	}

}
