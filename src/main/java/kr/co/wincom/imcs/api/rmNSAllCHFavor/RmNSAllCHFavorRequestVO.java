package kr.co.wincom.imcs.api.rmNSAllCHFavor;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.api.addNSAlert.AddNSAlertController;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

@SuppressWarnings("serial")
public class RmNSAllCHFavorRequestVO  extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * addNSFavorite API 전문 칼럼(순서 일치)
	********************************************************************/
	private String saId			= "";	// [12] 가입자정보
	private String stbMac		= "";	// [14] 가입자 STB MAC Address
	
	/********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
    private String pid			= "";
    private String resultCode	= "";

    private String contsGb		= "";
    
    private Integer resultset	= 0;
    
    private long tp_start = 0;
    
    
    public RmNSAllCHFavorRequestVO(){}
    
    public RmNSAllCHFavorRequestVO(String szParam){
    	
    	String[] arrStat = szParam.split("\\|");
		
		String key		= "";
		String value	= "";
		int    nStr		= 0;
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		
		/*String szTemp = szParam.toLowerCase();
		
		if( szTemp.indexOf("sa_id") == -1 || szTemp.indexOf("stb_mac") == -1 )
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
				
				if(key.toLowerCase().equals("sa_id"))			this.saId = value;
				if(key.toLowerCase().equals("stb_mac"))			this.stbMac = value;
			}
		}
		
		//RmNSAllCHFavorController.saId = paramMap.get("sa_id");
		
		//RmNSAllCHFavorController.stbMac = paramMap.get("stb_mac");
		
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null )
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

	public Integer getResultSet() {
		return resultset;
	}

	public void setResultSet(Integer resultset) {
		this.resultset = resultset;
	}

	public long getTp_start() {
		return tp_start;
	}

	public void setTp_start(long tp_start) {
		this.tp_start = tp_start;
	}
        
}
