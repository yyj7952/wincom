package kr.co.wincom.imcs.api.getNSPSI;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;


@SuppressWarnings("serial")
public class GetNSPSIRequestVO  extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * GetNSVPSI API 전문 칼럼(순서 일치)
	********************************************************************/
    private String saId			= "";	// 가입자 정보
    private String stbMac		= "";	// 가입자 MAC ADDRESS
    private String nscType		= "";	// N스크린 단말타입
    private String tcIn			= "";	// 편성스케줄 요청 시작기간	(0-24)
    private String tcOut		= "";	// 편성스케줄 요청 종료기간 (0-24)
    private String youthYn		= "";	// 청소년 요금제 여부
    private String orderGb		= "";	// 정렬 구분
    
    /********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
    private String pid			= "";
    private String resultCode	= "";
    private String virtualId	= "";
    private String inGbn		= "";
    private String outGbn		= "";
    private String nscCode		= "";
    private String testSbc		= "";
    private String youthYnCom	= "";
    private String svcId		= "";
    
    private String virtualChFlag = "";
    
    private long tp_start = 0;
    
    public GetNSPSIRequestVO(){}
    
    public GetNSPSIRequestVO(String szParam){
    	
    	String[] arrStat = szParam.split("\\|");
		
		String key		= "";
		String value	= "";
		int    nStr		= 0;
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		
		/*String szTemp = szParam.toLowerCase();
		
		if( szTemp.indexOf("sa_id") == -1 || szTemp.indexOf("stb_mac") == -1
				|| szTemp.indexOf("nsc_type") == -1 )
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
				if(key.toLowerCase().equals("nsc_type"))		this.nscType = value;
				if(key.toLowerCase().equals("tc_in"))			this.tcIn = value;
				if(key.toLowerCase().equals("tc_out"))			this.tcOut = value;
				if(key.toLowerCase().equals("youth_yn"))		this.youthYn = value;
				if(key.toLowerCase().equals("order_gb"))		this.orderGb = value;
			}
		}
		
		//GetNSPSIController.saId = paramMap.get("sa_id");
		//GetNSPSIController.stbMac = paramMap.get("stb_mac");
		
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null
				|| paramMap.get("nsc_type") == null )
		{
			throw new ImcsException();
		}
		
		/*if(this.saId.length() > 12 || this.saId.length() < 7 ){
			throw new ImcsException();
		}
		
		if(this.stbMac.length() > 39 || this.stbMac.length() < 14 ){
			throw new ImcsException();
		}*/
		
		this.tcIn = StringUtil.replaceNull(this.tcIn, "A");
		this.tcOut = StringUtil.replaceNull(this.tcOut, "A");
		this.youthYn = StringUtil.replaceNull(this.youthYn, "N");
		
		/*switch (youthYn) {
		case "Y":
		case "N":
			break;

		default:
			throw new ImcsException();
		}*/
		
		if("Y".equals(this.youthYn) )	this.youthYnCom = "1";
		else							this.youthYnCom = "0";
		
		if("0".equals(this.tcIn))		this.inGbn = "";
		else							this.inGbn = "/24";
		
		if("0".equals(this.tcOut))		this.outGbn = "";
		else							this.outGbn = "/24";
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

	public String getTcIn() {
		return tcIn;
	}

	public void setTcIn(String tcIn) {
		this.tcIn = tcIn;
	}

	public String getTcOut() {
		return tcOut;
	}

	public void setTcOut(String tcOut) {
		this.tcOut = tcOut;
	}

	public String getNscCode() {
		return nscCode;
	}

	public void setNscCode(String nscCode) {
		this.nscCode = nscCode;
	}

	public String getTestSbc() {
		return testSbc;
	}

	public void setTestSbc(String testSbc) {
		this.testSbc = testSbc;
	}

	public String getYouthYn() {
		return youthYn;
	}

	public void setYouthYn(String youthYn) {
		this.youthYn = youthYn;
	}

	public String getYouthYnCom() {
		return youthYnCom;
	}

	public void setYouthYnCom(String youthYnCom) {
		this.youthYnCom = youthYnCom;
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

	public String getInGbn() {
		return inGbn;
	}

	public void setInGbn(String inGbn) {
		this.inGbn = inGbn;
	}

	public String getOutGbn() {
		return outGbn;
	}

	public void setOutGbn(String outGbn) {
		this.outGbn = outGbn;
	}

	public long getTp_start() {
		return tp_start;
	}

	public void setTp_start(long tp_start) {
		this.tp_start = tp_start;
	}

	public String getSvcId() {
		return svcId;
	}

	public void setSvcId(String svcId) {
		this.svcId = svcId;
	}

	public String getVirtualId() {
		return virtualId;
	}

	public void setVirtualId(String virtualId) {
		this.virtualId = virtualId;
	}

	public String getVirtualChFlag() {
		return virtualChFlag;
	}

	public void setVirtualChFlag(String virtualChFlag) {
		this.virtualChFlag = virtualChFlag;
	}
	
}
