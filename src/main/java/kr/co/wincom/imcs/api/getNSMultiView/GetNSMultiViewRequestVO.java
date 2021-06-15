package kr.co.wincom.imcs.api.getNSMultiView;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

@SuppressWarnings("serial")
public class GetNSMultiViewRequestVO  extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * getNSMultiView API 전문 칼럼(순서 일치)
	********************************************************************/
    private String saId		= "";	// 가입자 정보
    private String stbMac	= "";	// 가입자 MAC ADDRESS
    private String pooqYn	= "";	// POOQ 채널 여부

    /********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
    private String pooqYnCom	= "";
    private String pid			= "";
    private String resultCode	= "";
    
    private long tp_start = 0;
    
    public GetNSMultiViewRequestVO(){}
    
    public GetNSMultiViewRequestVO(String szParam){
    	
    	String[] arrStat = szParam.split("\\|");
		
		String key		= "";
		String value	= "";
		int    nStr		= 0;
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		
		/*String szTemp = szParam.toLowerCase();
		
		if( szTemp.indexOf("sa_id") == -1 || szTemp.indexOf("stb_mac") == -1
				|| szTemp.indexOf("pooq_yn") == -1 )
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
				if(key.toLowerCase().equals("pooq_yn"))			this.pooqYn = value;
			}
		}
		
		//GetNSMultiViewController.saId = paramMap.get("sa_id");
		//GetNSMultiViewController.stbMac = paramMap.get("stb_mac");
		
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null
				|| paramMap.get("pooq_yn") == null )
		{
			throw new ImcsException();
		}
		
		/*if(this.saId.length() > 12 || this.saId.length() < 7 ){
			throw new ImcsException();
		}
		
		if(this.stbMac.length() > 39 || this.stbMac.length() < 14 ){
			throw new ImcsException();
		}*/
		
		if(this.pooqYn == null || "".equals(this.pooqYn)){
			this.pooqYn = "Y";
		}
		
		/*switch (pooqYn) {
		case "Y":
		case "P":
		case "N":
			break;

		default:
			throw new ImcsException();
		}*/
				
		if("P".equals(this.pooqYn)){
			this.pooqYnCom	= "Y";
		}else{
			this.pooqYnCom	= this.pooqYn;
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
	public String getPooqYn() {
		return pooqYn;
	}

	public void setPooqYn(String pooqYn) {
		this.pooqYn = pooqYn;
	}

	public String getPooqYnCom() {
		return pooqYnCom;
	}

	public void setPooqYnCom(String pooqYnCom) {
		this.pooqYnCom = pooqYnCom;
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

	public long getTp_start() {
		return tp_start;
	}

	public void setTp_start(long tp_start) {
		this.tp_start = tp_start;
	}
}
