package kr.co.wincom.imcs.api.getNSProdCpInfo;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

@SuppressWarnings("serial")
public class GetNSProdCpInfoRequestVO  extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * getNSProdCpInfo API 전문 칼럼(순서 일치)
	********************************************************************/
	private String saId			= "";	// 가입자정보
	private String prodId		= "";	// 상품코드
    private String uCubeProdIc		= "";	// 유큐브 상품코드
    private String appType		= "";	// 서비스명 (어플타입)
	
	/********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
    private String pid			= "";
    private String resultCode	= "";
    
    private long tp_start = 0;
    
    public GetNSProdCpInfoRequestVO(){}
    
    public GetNSProdCpInfoRequestVO(String szParam){
    	
    	String[] arrStat = szParam.split("\\|");
		
		String key		= "";
		String value	= "";
		int    nStr		= 0;
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		
		/*String szTemp = szParam.toLowerCase();
		
		if( paramMap.get("sa_id") == null || paramMap.get("prod_id") == null
				|| paramMap.get("u_cube_prod_ic") == null )
		{
			throw new ImcsException();
		}*/
		
		for(int i = 0; i < arrStat.length; i++){
			nStr	= arrStat[i].indexOf("=");
			
			if(nStr > 0) {
				key = arrStat[i].substring(0, nStr).toLowerCase().trim();
				value = arrStat[i].substring(nStr + 1, arrStat[i].length()).trim();
				
				paramMap.put(key, value);
				
				/*if( !value.matches(ImcsConstants.N_SP_PTN) && key.toLowerCase().indexOf("name") == null ){
					//특수문자 있음
					throw new ImcsException();
				}*/
				
				if(key.toLowerCase().equals("sa_id"))		this.saId = value;
				if(key.toLowerCase().equals("prod_id"))		this.prodId = value;
				if(key.toLowerCase().equals("u_cube_prod_ic"))	this.uCubeProdIc = value;
				if(key.toLowerCase().equals("app_type"))	this.appType = value;
			}
		}
		
		//GetNSProdCpInfoController.saId = paramMap.get("sa_id");
		
		//GetNSProdCpInfoController.stbMac = paramMap.get("stb_mac");
		
		if( paramMap.get("sa_id") == null || paramMap.get("prod_id") == null
				|| paramMap.get("u_cube_prod_ic") == null )
		{
			throw new ImcsException();
		}
		
		/*if(this.saId.length() > 12 || this.saId.length() < 7 ){
			throw new ImcsException();
		}*/
				
	}
    
    
	public String getSaId() {
		return saId;
	}
	public void setSaId(String saId) {
		this.saId = saId;
	}

	public String getProdId() {
		return prodId;
	}

	public void setProdId(String prodId) {
		this.prodId = prodId;
	}

	public String getuCubeProdIc() {
		return uCubeProdIc;
	}

	public void setuCubeProdIc(String uCubeProdIc) {
		this.uCubeProdIc = uCubeProdIc;
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

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}
}
