package kr.co.wincom.imcs.api.getNSMainPromo;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

public class GetNSMainPromoRequestVO extends NoSqlLoggingVO implements Serializable
{
	private static final long serialVersionUID = 820027358203692775L;

	/********************************************************************
	 * getNSMainPromo API 전문 칼럼(순서 일치)
	********************************************************************/
    private String saId			= "";
    private String stbMac		= "";
    private String screenType	= "";
    
	/********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/

    private String pid			= "";
    private String resultCode	= "";
	
	private int resultSet		= 0;
	
	public GetNSMainPromoRequestVO(){}
	
	public GetNSMainPromoRequestVO(String szParam){
		String[] arrStat = szParam.split("\\|");
		
		String key		= "";
		String value	= "";
		int    nStr		= 0;
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		
		/*String szTemp = szParam.toLowerCase();
		
		if( szTemp.indexOf("screen_type") == -1 )
		{
			throw new ImcsException();
		}*/
		
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
				
				if(key.toLowerCase().equals("screen_type"))		this.screenType = value;
				if(key.toLowerCase().equals("sa_id"))			this.saId = value;
				if(key.toLowerCase().equals("stb_mac"))			this.stbMac = value;
				
			}
		}
		
		//GetNSMainPromoController.saId = paramMap.get("sa_id");
		//GetNSMainPromoController.stbMac = paramMap.get("stb_mac");
		
		
		if( paramMap.get("screen_type") == null )
		{
			throw new ImcsException();
		}
		
		/*if(this.saId.length() > 12 || this.saId.length() < 7 ){
			throw new ImcsException();
		}
		
		if(this.stbMac.length() > 39 || this.stbMac.length() < 14 ){
			throw new ImcsException();
		}*/
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

	public String getScreenType() {
		return screenType;
	}

	public void setScreenType(String screenType) {
		this.screenType = screenType;
	}

	public int getResultSet() {
		return resultSet;
	}

	public void setResultSet(int resultSet) {
		this.resultSet = resultSet;
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
