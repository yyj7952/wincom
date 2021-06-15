package kr.co.wincom.imcs.api.getNSHighLight;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

@SuppressWarnings("serial")
public class GetNSHighLightRequestVO extends NoSqlLoggingVO implements Serializable {

	private String saId			= "";
	private String stbMac		= "";
	private String requestFlag	= "";
	
	private String pid			= "";
	private String resultCode	= "";
    
    public GetNSHighLightRequestVO() {}
    
    public GetNSHighLightRequestVO(String szParam) {
    	
    	CommonService commonService = new CommonService();
    	
    	String[] arrStat = szParam.split("\\|");
		
		String key		= "";
		String value	= "";
		int    nStr		= 0;
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		
		for(int i = 0; i < arrStat.length; i++) {
			nStr	= arrStat[i].indexOf("=");
			
			if(nStr > 0) {
				key = arrStat[i].substring(0, nStr).toLowerCase().trim();
				value = arrStat[i].substring(nStr + 1, arrStat[i].length()).trim();
				
				paramMap.put(key, value);
				
				if(key.toLowerCase().equals("sa_id"))		this.setSaId(value);
				if(key.toLowerCase().equals("stb_mac"))		this.setStbMac(value);
				if(key.toLowerCase().equals("request_flag"))	this.setRequestFlag(value);
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
		
		if(!commonService.getValidParam(this.requestFlag, 1, 1, 2))
		{
			throw new ImcsException();
		}
		
		switch(this.requestFlag)
		{
			case "P":
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
	public String getRequestFlag() {
		return requestFlag;
	}
	public void setRequestFlag(String requestFlag) {
		this.requestFlag = requestFlag;
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
	
}
