package kr.co.wincom.imcs.api.getNSEncryptVal;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

public class GetNSEncryptValRequestVO extends NoSqlLoggingVO implements Serializable
{
	private static final long serialVersionUID = 820027358203692775L;

	/********************************************************************
	 * getNSPurchased API 전문 칼럼(순서 일치)
	********************************************************************/
    private String saId			= "";
    private String stbMac		= "";
    private String encryptFlag	= "";
    private String albumId		= "";
    private String serviceId	= "";
	
	/********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
    private Integer pIdxSa		= 0;
    
    private String pid			= "";
    private String resultCode	= "";
    private String cIdxSa		= "";
	
	private int resultSet		= 0;
	
	public GetNSEncryptValRequestVO(){}
	
	public GetNSEncryptValRequestVO(String szParam){
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
				
				/*if( !value.matches(ImcsConstants.N_SP_PTN) && key.toLowerCase().indexOf("name") == -1 ){
					//특수문자 있음
					throw new ImcsException();
				}*/
				
				if(key.toLowerCase().equals("sa_id"))			this.saId = value;
				if(key.toLowerCase().equals("stb_mac"))			this.stbMac = value;
				if(key.toLowerCase().equals("encrypt_flag"))	this.encryptFlag = value;
				if(key.toLowerCase().equals("album_id"))		this.albumId = value;
				if(key.toLowerCase().equals("service_id"))		this.serviceId = value;
			}
		}
	    	    
		/*if(this.saId.length() > 12 || this.saId.length() < 7 ){
			throw new ImcsException();
		}
		
		if(this.stbMac.length() > 39 || this.stbMac.length() < 14 ){
			throw new ImcsException();
		}*/
		
		//GetNSEncryptValController.saId = paramMap.get("sa_id");
		
		//GetNSEncryptValController.stbMac = paramMap.get("stb_mac");
		
		//if(paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null ||(this.saId.equals("") && this.stbMac.equals("")))
		//	throw new ImcsException();
		
		if( !"".equals(this.saId) ){
			this.cIdxSa = this.saId.substring(this.saId.length()-2, this.saId.length());
			try {
				this.pIdxSa = Integer.parseInt(this.cIdxSa);
			} catch (Exception e) {
				this.pIdxSa = 0;
			}
			
		}
		
	}
	

	public static long getSerialversionuid() {
		return serialVersionUID;
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

	public int getResultSet() {
		return resultSet;
	}

	public void setResultSet(int resultSet) {
		this.resultSet = resultSet;
	}

	public Integer getpIdxSa() {
		return pIdxSa;
	}

	public void setpIdxSa(Integer pIdxSa) {
		this.pIdxSa = pIdxSa;
	}

	public String getcIdxSa() {
		return cIdxSa;
	}

	public void setcIdxSa(String cIdxSa) {
		this.cIdxSa = cIdxSa;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getAlbumId() {
		return albumId;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getEncryptFlag() {
		return encryptFlag;
	}

	public void setEncryptFlag(String encryptFlag) {
		this.encryptFlag = encryptFlag;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
		
	
}
