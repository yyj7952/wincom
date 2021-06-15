package kr.co.wincom.imcs.api.getNSMakeNodeList;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

@SuppressWarnings("serial")
public class GetNSMakeNodeListRequestVO extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * getNSContInfo API 전문 칼럼(순서 일치)
	********************************************************************/
	private String saId			  = "";	// 가입자정보
	private String stbMac		  = "";	// 가입자 STB MAC Address
	private String callType       = ""; // CDN IP 요청 구분
	private String reqServer      = ""; // 요청하는 서버 정보

	/********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
	private String testSbc        = "";
	private String serverFlag     = "0";
    private String pid			  = "";
    private String resultCode	  = "";
    private long tp_start 		  = 0;
    private String stringParam    = "";
    
    public GetNSMakeNodeListRequestVO(){}
    
    public GetNSMakeNodeListRequestVO(String szParam, String fullParam){
    	
    	this.stringParam = szParam; //넘겨받은 파라미터를 그래도 대입함.(다른vts에 전파할 URL을 만들때 사용)
    	
    	String[] arrStat = szParam.split("\\|");
		
		String key		= "";
		String value	= "";
		int    nStr		= 0;
		
		//CVTS 나중에 로직 정리 필요 (CVTS사라지면)
		if (fullParam.indexOf("internalizationIMCSCLIENT") > -1) {
			this.serverFlag = "1";
		} else {
			if (fullParam.indexOf("IMCSCLIENT") > -1) {
				this.serverFlag = "2";
			} else if (fullParam.indexOf("IMCSSERVER") > -1){
				this.serverFlag = "0";
			} else{
				this.serverFlag = "-1";
			}
			
		}
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		
		for(int i = 0; i < arrStat.length; i++){
			nStr	= arrStat[i].indexOf("=");
			
			if(nStr > 0) {
				key = arrStat[i].substring(0, nStr).toLowerCase().trim();
				value = arrStat[i].substring(nStr + 1, arrStat[i].length()).trim();
				
				paramMap.put(key, value);
				
				if(key.toLowerCase().equals("sa_id"))		this.saId    = value; 
				if(key.toLowerCase().equals("stb_mac"))		this.stbMac  = value;
				//if(key.toLowerCase().equals("call_type"))	this.callType = value;
				if(key.toLowerCase().equals("req_server"))	this.reqServer   = value;
				
			}
		}
		
		//가번이 없거나 길이가 맞지 않으면 안됨. 맥번호가 없거나 길이가 맞지 않으면 안됨. RQS_TYPE 없거나 길이가 맞지 않으면 안됨.
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null  
				//|| paramMap.get("call_type") == null 
				|| paramMap.get("req_server") == null)
		{
			throw new ImcsException();
		}
		
		if(this.saId.length() < 7 || this.saId.length() > 12)
	    {
	    	throw new ImcsException();
	    }
		
		if(this.stbMac.length() < 14 || this.stbMac.length() > 14)
	    {
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

	public String getServerFlag() {
		return serverFlag;
	}

	public void setServerFlag(String serverFlag) {
		this.serverFlag = serverFlag;
	}

	public String getStringParam() {
		return stringParam;
	}

	public void setStringParam(String stringParam) {
		this.stringParam = stringParam;
	}

	
}
