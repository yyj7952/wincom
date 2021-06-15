package kr.co.wincom.imcs.api.getNSMapInfo;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

public class GetNSMapInfoRequestVO extends NoSqlLoggingVO implements Serializable
{
	private static final long serialVersionUID = 3264421900461284108L;
	
	private String saId		= "";
	private String stbMac	= "";
	private String multiAlbumId	= "";
	
	private String pid		= "";
	private String resultCode = "20000000";
	private String testSbc = "";
	
	private String[] arrAlbumIds = null;
	
	public GetNSMapInfoRequestVO(String szParam)
	{
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
			}
		}
		
//		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null 
//				|| paramMap.get("multi_album_id") == null )
//		{
//			throw new ImcsException();
//		}
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
}
