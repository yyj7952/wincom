package kr.co.wincom.imcs.api.getNSNodeList;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

@SuppressWarnings("serial")
public class GetNSNodeListRequestVO extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * getNSContInfo API 전문 칼럼(순서 일치)
	********************************************************************/
	private String saId			  = "";	// 가입자정보
	private String stbMac		  = "";	// 가입자 STB MAC Address
	private String callType       = ""; // CDN IP 요청 구분
	private String reqServer      = ""; // 요청하는 서버 정보
	
	// 2019.10.11 - IPv6듀얼스택 제공 변수 추가
    private String	prefixInternet;
    private String	prefixUplushdtv;

	/********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
	private String testSbc        = "";
    private String pid			  = "";
    private String resultCode	  = "";
    private long tp_start 		  = 0;
    
    public GetNSNodeListRequestVO(){}
    
    public GetNSNodeListRequestVO(String szParam){
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
				if(key.toLowerCase().equals("call_type"))	this.callType = value;
				if(key.toLowerCase().equals("req_server"))	this.reqServer   = value;
				
			}
		}
		
		if(!commonService.getValidParam(this.saId, 7, 12, 1))
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.stbMac, 14, 14, 1))
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.callType, 1, 1, 2))
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.reqServer, 1, 1, 3))
		{
			throw new ImcsException();
		}
				
		switch(this.callType)
		{
			case "C":
			case "V":
			case "A":
				break;
			default:
				throw new ImcsException();
		}
		
		switch(this.reqServer)
		{
			case "1":
			case "2":
				break;
			default:
				throw new ImcsException();
		}
		
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

	public String getTestSbc() {
		return testSbc;
	}

	public void setTestSbc(String testSbc) {
		this.testSbc = testSbc;
	}

	public String getCallType() {
		return callType;
	}

	public void setCallType(String callType) {
		this.callType = callType;
	}

	public String getReqServer() {
		return reqServer;
	}

	public void setReqServer(String reqServer) {
		this.reqServer = reqServer;
	}

	public String getPrefixInternet() {
		return prefixInternet;
	}

	public void setPrefixInternet(String prefixInternet) {
		this.prefixInternet = prefixInternet;
	}

	public String getPrefixUplushdtv() {
		return prefixUplushdtv;
	}

	public void setPrefixUplushdtv(String prefixUplushdtv) {
		this.prefixUplushdtv = prefixUplushdtv;
	}

}