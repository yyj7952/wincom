package kr.co.wincom.imcs.api.getNSGoodsList;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

public class GetNSGoodsListRequestVO extends NoSqlLoggingVO implements Serializable
{	
	private static final long serialVersionUID = 1523478353149196755L;
	
	private String saId = "";
	private String stbMac = "";
	private String requestId = "";
	private String requestType = "";
	private String appType = "";
	
	private int p_idx_sa = 0;
	
	private String pid		= "";
	private String resultCode = "20000000";
	private String testSbc = "";
	private String viewFlag = "";
	
	private String poster_url = "";
	private String still_url = "";
	private String caption_url = "";
	private String category_url = "";
	private String product_url = "";
	
	public GetNSGoodsListRequestVO(String szParam)
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
				if(key.toLowerCase().equals("request_id"))		this.setRequestId(value);
				if(key.toLowerCase().equals("request_type"))	this.setRequestType(value);
				if(key.toLowerCase().equals("app_type"))		this.setAppType(value);
			}
		}
		
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null 
				|| paramMap.get("request_id") == null || paramMap.get("request_type") == null
				|| paramMap.get("app_type") == null)
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.saId, 7, 12, 1))
			throw new ImcsException();
		
		if(!commonService.getValidParam(this.stbMac, 14, 14, 1))
			throw new ImcsException();
		
		if(!commonService.getValidParam(this.requestId, 3, 20, 0))
			throw new ImcsException();
		
		if(!commonService.getValidParam(this.requestType, 1, 1, 1))
			throw new ImcsException();
		
		if(!commonService.getValidParam(this.appType, 4, 4, 1))
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
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public String getAppType() {
		return appType;
	}
	public void setAppType(String appType) {
		this.appType = appType;
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
	public String getViewFlag() {
		return viewFlag;
	}
	public void setViewFlag(String viewFlag) {
		this.viewFlag = viewFlag;
	}
	public int getP_idx_sa() {
		return p_idx_sa;
	}
	public void setP_idx_sa(int p_idx_sa) {
		this.p_idx_sa = p_idx_sa;
	}
	public String getPoster_url() {
		return poster_url;
	}
	public void setPoster_url(String poster_url) {
		this.poster_url = poster_url;
	}
	public String getStill_url() {
		return still_url;
	}
	public void setStill_url(String still_url) {
		this.still_url = still_url;
	}
	public String getCaption_url() {
		return caption_url;
	}
	public void setCaption_url(String caption_url) {
		this.caption_url = caption_url;
	}
	public String getCategory_url() {
		return category_url;
	}
	public void setCategory_url(String category_url) {
		this.category_url = category_url;
	}
	public String getProduct_url() {
		return product_url;
	}
	public void setProduct_url(String product_url) {
		this.product_url = product_url;
	}
}
