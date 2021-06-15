package kr.co.wincom.imcs.api.getNSKidsEStudy;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

public class GetNSKidsEStudyRequestVO extends NoSqlLoggingVO implements Serializable
{
	private static final long serialVersionUID = 4721883923081575106L;
	
	private String saId		= "";
	private String stbMac	= "";
	private String level	= "";
	
	private String pid		= "";
	private String resultCode = "20000000";
	private String testSbc = "";
	
	public GetNSKidsEStudyRequestVO(String szParam)
	{
		CommonService commonService = new CommonService();
		
		String[] arrStat = szParam.split("\\|");
		
		String key = "";
		String value = "";
		int nStr = 0;
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		
		for(int i = 0; i < arrStat.length; i++)
		{
			nStr = arrStat[i].indexOf("=");
			
			if(nStr > 0)
			{
				key = arrStat[i].substring(0, nStr).toLowerCase();
				value = arrStat[i].substring(nStr + 1, arrStat[i].length());
				
				paramMap.put(key, value);
				
				if(key.toLowerCase().equals("sa_id"))	this.setSaId(value);
				if(key.toLowerCase().equals("stb_mac"))	this.setStbMac(value);
				if(key.toLowerCase().equals("level"))	this.setLevel(value);
			}
		}
		
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null 
				|| paramMap.get("level") == null )
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
		
		if(!commonService.getValidParam(this.level, 1, 1, 3))
		{
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
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
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
}


















