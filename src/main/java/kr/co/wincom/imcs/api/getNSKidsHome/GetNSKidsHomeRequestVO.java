package kr.co.wincom.imcs.api.getNSKidsHome;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

public class GetNSKidsHomeRequestVO extends NoSqlLoggingVO implements Serializable
{
	private static final long serialVersionUID = -1737362695750753878L;
	
	private String saId		= "";
	private String stbMac	= "";
	private String requestFlag = "";
	private String lastUseMenu = "";
	
	private int p_idx_sa = 0;
	private int p_stb_idx_sa = 0;
	private int iCheckFlag = 0;
	
	private String pid		= "";
	private String resultCode = "20000000";
	private String testSbc = "";
	private String viewFlag = "";
	private String stbSaId = "";
	private String stbMacAddr = "";
	private String stbPairing = "";
	
	private String albumId = "";
	private String sugg_cat_id = "";
	private String temp_category_id = "";
	
	private String temp_stb_sa_id = "";
	
	public GetNSKidsHomeRequestVO(String szParam)
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
				if(key.toLowerCase().equals("request_flag")) this.setRequestFlag(value);
				if(key.toLowerCase().equals("last_use_menu")) this.setLastUseMenu(value);
			}
		}
		
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null 
				|| paramMap.get("request_flag") == null )
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
		
		if(!commonService.getValidParam(this.requestFlag, 2, 2, 1))
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.lastUseMenu, 0, 50, 0))
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
	public String getRequestFlag() {
		return requestFlag;
	}
	public void setRequestFlag(String requestFlag) {
		this.requestFlag = requestFlag;
	}
	public String getLastUseMenu() {
		return lastUseMenu;
	}
	public void setLastUseMenu(String lastUseMenu) {
		this.lastUseMenu = lastUseMenu;
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
	public int getP_idx_sa() {
		return p_idx_sa;
	}
	public void setP_idx_sa(int p_idx_sa) {
		this.p_idx_sa = p_idx_sa;
	}
	public String getViewFlag() {
		return viewFlag;
	}
	public void setViewFlag(String viewFlag) {
		this.viewFlag = viewFlag;
	}
	public String getAlbumId() {
		return albumId;
	}
	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}
	public String getSugg_cat_id() {
		return sugg_cat_id;
	}
	public void setSugg_cat_id(String sugg_cat_id) {
		this.sugg_cat_id = sugg_cat_id;
	}
	public String getTemp_category_id() {
		return temp_category_id;
	}
	public void setTemp_category_id(String temp_category_id) {
		this.temp_category_id = temp_category_id;
	}
	public String getStbSaId() {
		return stbSaId;
	}
	public void setStbSaId(String stbSaId) {
		this.stbSaId = stbSaId;
	}
	public String getStbMacAddr() {
		return stbMacAddr;
	}
	public void setStbMacAddr(String stbMacAddr) {
		this.stbMacAddr = stbMacAddr;
	}
	public String getStbPairing() {
		return stbPairing;
	}
	public void setStbPairing(String stbPairing) {
		this.stbPairing = stbPairing;
	}
	public int getP_stb_idx_sa() {
		return p_stb_idx_sa;
	}
	public void setP_stb_idx_sa(int p_stb_idx_sa) {
		this.p_stb_idx_sa = p_stb_idx_sa;
	}
	public int getiCheckFlag() {
		return iCheckFlag;
	}
	public void setiCheckFlag(int iCheckFlag) {
		this.iCheckFlag = iCheckFlag;
	}
	public String getTemp_stb_sa_id() {
		return temp_stb_sa_id;
	}
	public void setTemp_stb_sa_id(String temp_stb_sa_id) {
		this.temp_stb_sa_id = temp_stb_sa_id;
	}
	
	
}
