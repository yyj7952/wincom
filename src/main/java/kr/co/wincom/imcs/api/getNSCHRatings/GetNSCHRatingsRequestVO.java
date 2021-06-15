package kr.co.wincom.imcs.api.getNSCHRatings;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

@SuppressWarnings("serial")
public class GetNSCHRatingsRequestVO  extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * getNSCHRRatings API 전문 칼럼(순서 일치)
	********************************************************************/
	private String saId			= "";	// 가입자정보
	private String stbMac		= "";	// 가입자 STB MAC Address
	private String nscType		= "";	// N스크린 단말타입 (PAD, LTE)
    private String baseGb		= "";	// 기지국 코드 구분
    private String baseCd		= "";	// 기지국 코드
    private String pooqYn		= "";	// pooq 채널 여부
   
	/********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
    private String pid			= "";
    private String resultCode	= "";

    private String baseOneCd	= "";
    private String nodeCd		= "";
    private String rbaseCode	= "";
    private String testSbc		= "";
    private String chnlCd		= "";
    private String baseCondi	= "";
    
    private long tp_start = 0;
    
    public GetNSCHRatingsRequestVO(){}
    
    public GetNSCHRatingsRequestVO(String szParam){
    	
    	String[] arrStat = szParam.split("\\|");
		
		String key		= "";
		String value	= "";
		int    nStr		= 0;
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		
		/*String szTemp = szParam.toLowerCase();
		
		if( szTemp.indexOf("sa_id") == -1 || szTemp.indexOf("stb_mac") == -1
				|| szTemp.indexOf("nsc_type") == -1 || szTemp.indexOf("base_gb") == -1 
				|| szTemp.indexOf("base_cd") == -1 )
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
				
				if(key.toLowerCase().equals("sa_id"))		this.saId = value;
				if(key.toLowerCase().equals("stb_mac"))		this.stbMac = value;
				if(key.toLowerCase().equals("nsc_type"))	this.nscType = value;
				if(key.toLowerCase().equals("base_gb"))		this.baseGb = value;
				if(key.toLowerCase().equals("base_cd"))		this.baseCd = value;
				if(key.toLowerCase().equals("pooq_yn"))		this.pooqYn = value;
				
			}
		}
		
		//GetNSCHRatingsController.saId = paramMap.get("sa_id");
		
		//GetNSCHRatingsController.stbMac = paramMap.get("stb_mac");
		
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null
				|| paramMap.get("nsc_type") == null || paramMap.get("base_gb") == null 
				|| paramMap.get("base_cd") == null )
		{
			throw new ImcsException();
		}
		
		/*if(this.saId.length() > 12 || this.saId.length() < 7 ){
			throw new ImcsException();
		}
		
		if(this.stbMac.length() > 39 || this.stbMac.length() < 14 ){
			throw new ImcsException();
		}*/
		
		this.pooqYn	= StringUtil.replaceNull(this.pooqYn, "Y");
		
		/*switch (pooqYn) {
		case "Y":
		case "N":
			break;

		default:
			throw new ImcsException();
		}	*/	

	    if(!"".equals(this.baseCd) && this.baseCd != null){
	    	this.baseOneCd	= baseCd.substring(0, 1);
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
	public String getNscType() {
		return nscType;
	}
	public void setNscType(String nscType) {
		this.nscType = nscType;
	}
	public String getBaseGb() {
		return baseGb;
	}
	public void setBaseGb(String baseGb) {
		this.baseGb = baseGb;
	}
	public String getBaseCd() {
		return baseCd;
	}
	public void setBaseCd(String baseCd) {
		this.baseCd = baseCd;
	}
	public String getPooqYn() {
		return pooqYn;
	}
	public void setPooqYn(String pooqYn) {
		this.pooqYn = pooqYn;
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
	public String getBaseOneCd() {
		return baseOneCd;
	}
	public void setBaseOneCd(String baseOneCd) {
		this.baseOneCd = baseOneCd;
	}
	public String getNodeCd() {
		return nodeCd;
	}

	public void setNodeCd(String nodeCd) {
		this.nodeCd = nodeCd;
	}

	public String getRbaseCode() {
		return rbaseCode;
	}

	public void setRbaseCode(String rbaseCode) {
		this.rbaseCode = rbaseCode;
	}

	public long getTp_start() {
		return tp_start;
	}
	public void setTp_start(long tp_start) {
		this.tp_start = tp_start;
	}

	public String getTestSbc() {
		return testSbc;
	}

	public void setTestSbc(String testSbc) {
		this.testSbc = testSbc;
	}

	public String getChnlCd() {
		return chnlCd;
	}

	public void setChnlCd(String chnlCd) {
		this.chnlCd = chnlCd;
	}

	public String getBaseCondi() {
		return baseCondi;
	}

	public void setBaseCondi(String baseCondi) {
		this.baseCondi = baseCondi;
	}
}
