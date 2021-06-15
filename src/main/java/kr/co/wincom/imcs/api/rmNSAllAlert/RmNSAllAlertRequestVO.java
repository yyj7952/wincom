package kr.co.wincom.imcs.api.rmNSAllAlert;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

public class RmNSAllAlertRequestVO extends NoSqlLoggingVO implements Serializable
{
	private static final long serialVersionUID = 820027358203692775L;

	
	/********************************************************************
	 * addNSFavorite API 전문 칼럼(순서 일치)
	********************************************************************/
	private String saId			= "";	// [12] 가입자정보
	private String stbMac		= "";	// [14] 가입자 STB MAC Address
	private String contsGb		= "";	// [20] 영화 ID (앨범ID1\b앨범ID2\b앨범ID3(다중삭제 시 배열))
	
	/********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
	private String pid			= "";
	private String resultCode	= "";
	
	
	public RmNSAllAlertRequestVO(){}
	
	
	public RmNSAllAlertRequestVO(String szParam) {
    	String[] arrStat = szParam.split("\\|");
		
		String key		= "";
		String value	= "";
		int    nStr		= 0;
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		
		/*String szTemp = szParam.toLowerCase();
		
		if( szTemp.indexOf("sa_id") == -1 || szTemp.indexOf("stb_mac") == -1
				|| szTemp.indexOf("conts_gb") == -1 )
		{
			throw new ImcsException();
		}*/
		
		try{
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
					if(key.toLowerCase().equals("conts_gb"))		this.setContsGb(value);
					
				}
			}
			
			//RmNSAllAlertController.saId = paramMap.get("sa_id");
			//RmNSAllAlertController.stbMac = paramMap.get("stb_mac");
			
			if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null
					|| paramMap.get("conts_gb") == null )
			{
				throw new ImcsException();
			}
			
			/*if(this.saId.length() > 12 || this.saId.length() < 7 ){
				throw new ImcsException();
			}
			
			if(this.stbMac.length() > 39 || this.stbMac.length() < 14 ){
				throw new ImcsException();
			}*/
			
			if(contsGb.equals("PAD"))
				this.contsGb	 = "NSC";
			
			
		} catch (Exception e) {
			
		}
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	

	public String getSaId() {
		return saId;
	}

	
	public void setSaId(String saId) {
		if(saId == null || saId.equalsIgnoreCase("null")) saId = "";
		this.saId = saId;
	}

	
	public String getStbMac() {
		return stbMac;
	}

	
	public void setStbMac(String stbMac) {
		if(stbMac == null || stbMac.equalsIgnoreCase("null")) stbMac = "";
		this.stbMac = stbMac;
	}

	
	public String getContsGb() {
		return contsGb;
	}

	
	public void setContsGb(String contsGb) {
		this.contsGb = contsGb;
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
