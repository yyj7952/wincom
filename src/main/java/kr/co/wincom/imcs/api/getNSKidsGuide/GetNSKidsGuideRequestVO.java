package kr.co.wincom.imcs.api.getNSKidsGuide;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

public class GetNSKidsGuideRequestVO extends NoSqlLoggingVO implements Serializable
{
	private static final long serialVersionUID = -7847142155628036711L;
	
	private String saId = "";
	private String stbMac = "";
	private String catId = "";
	
	private String pid		= "";
	private String resultCode = "20000000";
	private String testSbc = "";
	private String viewFlag = "";
	
	private String still_url = "";
	private String current_level = "";
	private String parent_cat_id = "";
	private String sub_title = "";
	
	public GetNSKidsGuideRequestVO(String szParam)
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
				
				if(key.toLowerCase().equals("sa_id"))			this.setSaId(value);
				if(key.toLowerCase().equals("stb_mac"))			this.setStbMac(value);
				if(key.toLowerCase().equals("cat_id"))			this.setCatId(value);
			}
		}
		
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null 
				|| paramMap.get("cat_id") == null)
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.saId, 7, 12, 1))
			throw new ImcsException();
		
		if(!commonService.getValidParam(this.stbMac, 14, 14, 1))
			throw new ImcsException();
		
		if(!commonService.getValidParam(this.catId, 3, 5, 1))
			throw new ImcsException();
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

	public String getStill_url() {
		return still_url;
	}

	public void setStill_url(String still_url) {
		this.still_url = still_url;
	}

	public String getViewFlag() {
		return viewFlag;
	}

	public void setViewFlag(String viewFlag) {
		this.viewFlag = viewFlag;
	}

	public String getCurrent_level() {
		return current_level;
	}

	public void setCurrent_level(String current_level) {
		this.current_level = current_level;
	}

	public String getParent_cat_id() {
		return parent_cat_id;
	}

	public void setParent_cat_id(String parent_cat_id) {
		this.parent_cat_id = parent_cat_id;
	}

	public String getSub_title() {
		return sub_title;
	}

	public void setSub_title(String sub_title) {
		this.sub_title = sub_title;
	}
}















