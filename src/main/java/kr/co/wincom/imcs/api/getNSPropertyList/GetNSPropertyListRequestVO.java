package kr.co.wincom.imcs.api.getNSPropertyList;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

@SuppressWarnings("serial")
public class GetNSPropertyListRequestVO extends NoSqlLoggingVO implements Serializable {

	private String saId			= "";
	private String stbMac		= "";
	private String propGrpId	= "";
	
	private String pid			= "";
	private String resultCode	= "";
    
    public GetNSPropertyListRequestVO() {}
    
    public GetNSPropertyListRequestVO(String szParam) {
    	
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
				if(key.toLowerCase().equals("prop_grp_id"))		this.setPropGrpId(value);
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
		
		this.propGrpId = StringUtil.replaceNull(this.propGrpId, "N");

		if (!this.propGrpId.matches("[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝]*")) 
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

	public String getPropGrpId() {
		return propGrpId;
	}

	public void setPropGrpId(String propGrpId) {
		this.propGrpId = propGrpId;
	}
	
}
