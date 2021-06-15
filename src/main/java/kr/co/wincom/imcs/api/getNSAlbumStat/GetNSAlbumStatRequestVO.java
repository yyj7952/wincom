package kr.co.wincom.imcs.api.getNSAlbumStat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

public class GetNSAlbumStatRequestVO extends NoSqlLoggingVO implements Serializable
{
	private static final long serialVersionUID = -7542037652901211678L;
	
	private String saId		= "";
	private String stbMac	= "";
	private String multiAlbumId	= "";
	private String albumFlag = "";
	
	private String pid		= "";
	private String resultCode = "20000000";
	private String testSbc = "";
	private String ncn_sa_id = "";
	private String ncn_stb_mac = "";
	
	private String[] arrAlbumIds = null;
	private ArrayList<String> buy_arr = null;
	private ArrayList<String> arrSvodAlbumIds = null;
	private ArrayList<String> arrKidSvodIds = null;
	
	
	public GetNSAlbumStatRequestVO(String szParam)
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
				key = arrStat[i].substring(0, nStr).toLowerCase().trim();
				value = arrStat[i].substring(nStr + 1, arrStat[i].length()).trim();
				
				paramMap.put(key, value);
				
				if(key.toLowerCase().equals("sa_id"))	this.setSaId(value);
				if(key.toLowerCase().equals("stb_mac"))	this.setStbMac(value);
				if(key.toLowerCase().equals("multi_album_id"))	this.setMultiAlbumId(value);
				if(key.toLowerCase().equals("album_flag")) this.setAlbumFlag(value);
			}
		}
		
		if(paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null || paramMap.get("multi_album_id") == null )
		{
			throw new ImcsException();
		}
		
		this.albumFlag = StringUtil.replaceNull(this.getAlbumFlag(), "A");
		
		if(!commonService.getValidParam(this.saId, 7, 12, 1))
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.stbMac, 14, 14, 1))
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.multiAlbumId, 15, 479, 1))
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.albumFlag, 0, 1, 2))
		{
			throw new ImcsException();
		}
		
		switch(this.albumFlag)
		{
			case "A":
			case "P":
			case "R":
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
	public String getMultiAlbumId() {
		return multiAlbumId;
	}
	public void setMultiAlbumId(String multiAlbumId) {
		this.multiAlbumId = multiAlbumId;
	}
	public String getAlbumFlag() {
		return albumFlag;
	}
	public void setAlbumFlag(String albumFlag) {
		this.albumFlag = albumFlag;
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
	public String[] getArrAlbumIds() {
		return arrAlbumIds;
	}
	public void setArrAlbumIds(String[] arrAlbumIds) {
		this.arrAlbumIds = arrAlbumIds;
	}	
	public ArrayList<String> getBuy_arr() {
		return buy_arr;
	}
	public void setBuy_arr(ArrayList<String> buy_arr) {
		this.buy_arr = buy_arr;
	}
	public ArrayList<String> getArrSvodAlbumIds() {
		return arrSvodAlbumIds;
	}
	public void setArrSvodAlbumIds(ArrayList<String> arrSvodAlbumIds) {
		this.arrSvodAlbumIds = arrSvodAlbumIds;
	}
	public String getNcn_sa_id() {
		return ncn_sa_id;
	}
	public void setNcn_sa_id(String ncn_sa_id) {
		this.ncn_sa_id = ncn_sa_id;
	}
	public String getNcn_stb_mac() {
		return ncn_stb_mac;
	}
	public void setNcn_stb_mac(String ncn_stb_mac) {
		this.ncn_stb_mac = ncn_stb_mac;
	}

	public ArrayList<String> getArrKidSvodIds() {
		return arrKidSvodIds;
	}

	public void setArrKidSvodIds(ArrayList<String> arrKidSvodIds) {
		this.arrKidSvodIds = arrKidSvodIds;
	}
	
}



























