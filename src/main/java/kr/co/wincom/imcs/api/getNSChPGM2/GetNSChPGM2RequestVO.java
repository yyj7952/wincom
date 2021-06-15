package kr.co.wincom.imcs.api.getNSChPGM2;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;


@SuppressWarnings("serial")
public class GetNSChPGM2RequestVO  extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * GetNSChPGM API 전문 칼럼(순서 일치)
	********************************************************************/
    private String saId			= "";	// 가입자 정보
    private String stbMac		= "";	// 가입자 MAC ADDRESS
    private String requestTime  = "";   //
    private String callFlag     = "";   //
    private String testSbc		= "";
    private String testSbcQuery	= "";
    private String pooqYn		= "";	// POOQ 채널여부
    private String hdtvViewGb	= "";	// HDTV 채널노출 구분
    
//    private String nscType		= "";	// N스크린 단말타입
//    private String tcIn			= "";	// 편성스케줄 요청 시작기간	(0-24)
//    private String tcOut		= "";	// 편성스케줄 요청 종료기간 (0-24)
//    private String youthYn		= "";	// 청소년 요금제 여부
//    private String serviceId	= "";	// 서비스 ID
//    private String epgSdate		= "";	// 프로그램 정보요청 시작시간
//    private String epgEdate		= "";	// 프로그램 정보요청 종료시간
    
    /********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
    private String pid			= "";
    private String resultCode	= "";
    private String virtualId	= "";
    private String snap_server  = "";
    private String img_resize_server = "";
    private String img_still_server  = "";
    private String vchSql       = "";
    private String vchType      = "";
    private String pooqQuery1   = "";
    private String pooqQuery2   = "";
    
//    private String inGbn		= "";
//    private String outGbn		= "";
//    private String nscCode	= "";
//    private String youthYnCom	= "";
//    private String svcId		= "";
    
    private String virtualChFlag = "";
    
    private long tp_start = 0;
    
    public GetNSChPGM2RequestVO(){}
    
    public GetNSChPGM2RequestVO(String szParam){
    	
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
				if(key.toLowerCase().equals("request_time"))	this.requestTime = value;
				if(key.toLowerCase().equals("hdtv_view_gb"))	this.hdtvViewGb = value;
				if(key.toLowerCase().equals("call_flag"))		this.callFlag = value;
				if(key.toLowerCase().equals("pooq_yn"))			this.pooqYn = value;
			}
		}
		
		//GetNSChPGMController.saId = paramMap.get("sa_id");
		//GetNSChPGMController.stbMac = paramMap.get("stb_mac");
		
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null
				|| paramMap.get("request_time") == null || paramMap.get("hdtv_view_gb") == null )
		{
			throw new ImcsException();
		}
		
		if(this.saId.length() > 12 || this.saId.length() < 7 ){
			throw new ImcsException();
		}
		
		if(this.stbMac.length() > 39 || this.stbMac.length() < 14 ){
			throw new ImcsException();
		}
		
		
		if(this.pooqYn.length() > 1){
			throw new ImcsException();
		}
		
		if(this.hdtvViewGb.length() > 5){
			throw new ImcsException();
		}
		
		switch (hdtvViewGb) {
		case "R":
		case "P":
		case "G":
		case "M":
		case "K":
		case "C":
			break;

		default:
			throw new ImcsException();
		}

		switch (pooqYn) {
			case "Y":
				this.pooqQuery1 = "Y";
				this.pooqQuery2 = "N";
				break;
			case "N":
				this.pooqQuery1 = "Y";
				this.pooqQuery2 = "Y";
				break;				
			case "P":
				this.pooqQuery1 = "N";
				this.pooqQuery2 = "N";
				break;
				
			default:
				throw new ImcsException();
		}
		
		this.callFlag = StringUtil.replaceNull(this.callFlag, "4");
		if (Integer.parseInt(this.callFlag) < 0) { 
			throw new ImcsException();
		} else {
			if(this.callFlag.equals("0")) this.callFlag = "4";
			if(!this.callFlag.equals("4") && !this.callFlag.equals("24"))
			{
				throw new ImcsException();
			}
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

	public String getTestSbc() {
		return testSbc;
	}

	public void setTestSbc(String testSbc) {
		this.testSbc = testSbc;
	}

	public String getPooqYn() {
		return pooqYn;
	}

	public void setPooqYn(String pooqYn) {
		this.pooqYn = pooqYn;
	}

	public String getHdtvViewGb() {
		return hdtvViewGb;
	}

	public void setHdtvViewGb(String hdtvViewGb) {
		this.hdtvViewGb = hdtvViewGb;
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

	public String getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}

	public String getCallFlag() {
		return callFlag;
	}

	public void setCallFlag(String callFlag) {
		this.callFlag = callFlag;
	}

	public String getSnap_server() {
		return snap_server;
	}

	public void setSnap_server(String snap_server) {
		this.snap_server = snap_server;
	}

	public String getImg_resize_server() {
		return img_resize_server;
	}

	public void setImg_resize_server(String img_resize_server) {
		this.img_resize_server = img_resize_server;
	}

	public String getImg_still_server() {
		return img_still_server;
	}

	public void setImg_still_server(String img_still_server) {
		this.img_still_server = img_still_server;
	}

	public String getVchSql() {
		return vchSql;
	}

	public void setVchSql(String vchSql) {
		this.vchSql = vchSql;
	}

	public String getVchType() {
		return vchType;
	}

	public void setVchType(String vchType) {
		this.vchType = vchType;
	}

	public String getPooqQuery1() {
		return pooqQuery1;
	}

	public void setPooqQuery1(String pooqQuery1) {
		this.pooqQuery1 = pooqQuery1;
	}

	public String getPooqQuery2() {
		return pooqQuery2;
	}

	public void setPooqQuery2(String pooqQuery2) {
		this.pooqQuery2 = pooqQuery2;
	}

	public String getTestSbcQuery() {
		return testSbcQuery;
	}

	public void setTestSbcQuery(String testSbcQuery) {
		this.testSbcQuery = testSbcQuery;
	}
		
}
