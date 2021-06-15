package kr.co.wincom.imcs.api.getNSChnlPlayIP;

import java.io.Serializable;
import java.util.HashMap;

import org.hamcrest.core.Is;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

@SuppressWarnings("serial")
public class GetNSChnlPlayIPRequestVO extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * getNSContInfo API 전문 칼럼(순서 일치)
	********************************************************************/
	private String saId			  = "";	// 가입자정보
	private String stbMac		  = "";	// 가입자 STB MAC Address
	private String baseCd       = ""; // CDN IP 요청 구분
	private String svcNode      = ""; // 요청하는 서버 정보

	/********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
	private String testSbc        = "";
	private String baseCdTmp      = "";
    private String pid			  = "";
    private String resultCode	  = "";
    private long   tp_start 	  = 0;
    private String baseCd5g		  = "";
    private String baseCd4d		  = "";
    // 2021.04.15 - 아이돌라이브 유료채널
    private String baseCdpcweb		  = "";
    private String baseCdTmp5g	  = "";
    private String baseCdTmp4d	  = "";
 // 2021.04.15 - 아이돌라이브 유료채널
    private String baseCdTmppcweb	  = "";
    private String c4dYn		  = "";
    private String callFlag		  = "";
    private String appType;
    
    // 2019.10.11 - IPv6듀얼스택 제공 변수 추가
    private String	ipv6Flag;
    private String	prefixInternet;
    private String	prefixUplushdtv;
	
	
    public GetNSChnlPlayIPRequestVO(){}
    
    public GetNSChnlPlayIPRequestVO(String szParam){
    	CommonService commonService = new CommonService();
    	
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
				
				if(key.toLowerCase().equals("sa_id"))		this.saId    = value; 
				if(key.toLowerCase().equals("stb_mac"))		this.stbMac  = value;
				if(key.toLowerCase().equals("base_cd"))		this.baseCd  = value;
				if(key.toLowerCase().equals("svc_node"))	this.svcNode = value;
				if(key.toLowerCase().equals("ipv6_flag"))	this.ipv6Flag = value;
				if(key.toLowerCase().equals("app_type"))	this.appType = value;
				
			}
		}
		
		this.svcNode	= StringUtil.replaceNull(this.svcNode, "N");
		this.ipv6Flag	= StringUtil.replaceNull(this.ipv6Flag, "N");
		
		if(!commonService.getValidParam(this.saId, 7, 12, 1))
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.stbMac, 14, 14, 1))
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.baseCd, 1, 50, 1))
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.ipv6Flag, 1, 1, 2))
		{
			throw new ImcsException();
		}
		
		if(this.appType != null && !commonService.getValidParam(this.appType, 4, 4, 2))
		{
			throw new ImcsException();
		}
		else if(this.appType == null)
		{
			this.appType = "RUSA";
		}
		
		this.baseCd5g = baseCd;
		
		switch (baseCd.substring(0, 1)) {
		case "W":
			this.baseCd5g = "F" + baseCd5g.substring(1);
			break;
		case "L":
			this.baseCd5g = "V" + baseCd5g.substring(1);
			break;
		case "F":
			this.baseCd = "W" + baseCd5g.substring(1);
			break;
		case "V":
			this.baseCd = "L" + baseCd5g.substring(1);
			break;
		case "T":
			this.baseCd5g = "T" + baseCd5g.substring(1);
			break;
		default:
			throw new ImcsException();
		}
					
		
		if (this.svcNode.equals("D")) {
			this.svcNode = "N";
			this.callFlag = "D";
		}
		// 2021.04.15 - 아이돌라이브 유료채널
		else if (this.svcNode.equals("P") && !this.appType.substring(0, 1).equals("P") && (this.appType.substring(2, 3).equals("C") || this.appType.substring(2, 3).equals("B"))) {
			this.svcNode = "P";
			this.callFlag = "P";
		}
		// 2021.05.20 - 프로야구 웹
		else if (this.svcNode.equals("P") && this.appType.substring(0, 1).equals("P") && (this.appType.substring(2, 3).equals("C") || this.appType.substring(2, 3).equals("B"))) {
			this.svcNode = "P";
			this.callFlag = "B";
		}
		
		if(this.callFlag.equals("D")) {
			switch (baseCd.substring(0, 1)) {
			case "W":
				this.baseCd4d = "D" + baseCd.substring(1);
				break;
			case "L":
				this.baseCd4d = "R" + baseCd.substring(1);
				break;
			default:
				break;
			}
		}
		// 2021.04.15 - 아이돌라이브 유료채널
		else if(this.callFlag.equals("P")) {
			this.baseCdpcweb = "PP";
		}
		// 2021.04.15 - 아이돌라이브 유료채널
		else if(this.callFlag.equals("B")) {
			this.baseCdpcweb = "PB";
		}
		
		switch (svcNode) {
		case "N":
			this.baseCdTmp = baseCd;
			this.baseCdTmp5g  = baseCd5g;
			this.baseCdTmp4d  = baseCd4d;
			break;
		case "R":
			this.svcNode = "A";
			switch (baseCd.substring(0, 1)) {
			case "W":
			case "L":
				this.baseCdTmp = "A"+baseCd.substring(0);
				this.baseCdTmp5g  = "A"+baseCd5g.substring(0);
				break;
			case "T":
				this.svcNode = "N";
				this.baseCdTmp = baseCd;
				this.baseCdTmp5g  = baseCd5g;
				break;
			default:
				break;
			}
			break;
		case "T":
			switch (baseCd.substring(0, 1)) {
			case "W":
			case "L":
				this.baseCdTmp = "T"+baseCd.substring(0);
				this.baseCdTmp5g  = "T"+baseCd5g.substring(0);
				break;
			case "T":
				this.svcNode = "N";
				this.baseCdTmp = baseCd;
				this.baseCdTmp5g  = baseCd5g;
				break;
			default:
				break;
			}
			break;
		case "M":
			switch (baseCd.substring(0, 1)) {
			case "W":
			case "L":
				this.baseCdTmp = "M"+baseCd.substring(0);
				this.baseCdTmp5g  = "M"+baseCd5g.substring(0);
				break;
			case "T":
				this.svcNode = "N";
				this.baseCdTmp = baseCd;
				this.baseCdTmp5g  = baseCd5g;
				break;
			default:
				break;
			}
			break;
		case "P":
			switch (baseCd.substring(0, 1)) {
			case "W":
			case "L":
				this.baseCdTmp = "P"+baseCd.substring(0);
				this.baseCdTmp5g  = "P"+baseCd5g.substring(0);
				break;
			case "T":
				this.svcNode = "N";
				this.baseCdTmp = baseCd;
				this.baseCdTmp5g  = baseCd5g;
				break;
			default:
				break;
			}
			// 2021.04.15 - 아이돌라이브 유료채널
			this.baseCdTmppcweb  = baseCdpcweb;
			break;
		default:
			break;
		}
		
		this.baseCd	= StringUtil.replaceNull(this.baseCd, "0");
		this.baseCd5g	= StringUtil.replaceNull(this.baseCd5g, "0");
		this.baseCd4d	= StringUtil.replaceNull(this.baseCd4d, "0");
		// 2021.04.15 - 아이돌라이브 유료채널
		this.baseCdpcweb	= StringUtil.replaceNull(this.baseCdpcweb, "0");
		this.baseCdTmp	= StringUtil.replaceNull(this.baseCdTmp, "0");
		this.baseCdTmp5g	= StringUtil.replaceNull(this.baseCdTmp5g, "0");
		this.baseCdTmp4d	= StringUtil.replaceNull(this.baseCdTmp4d, "0");
		// 2021.04.15 - 아이돌라이브 유료채널
		this.baseCdTmppcweb	= StringUtil.replaceNull(this.baseCdTmppcweb, "0");
		
		switch(this.ipv6Flag)
		{
			case "Y":
			case "N":
				break;
			default:
				throw new ImcsException();
		}

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

	public String getBaseCd() {
		return baseCd;
	}

	public void setBaseCd(String baseCd) {
		this.baseCd = baseCd;
	}

	public String getSvcNode() {
		return svcNode;
	}

	public void setSvcNode(String svcNode) {
		this.svcNode = svcNode;
	}

	public String getBaseCdTmp() {
		return baseCdTmp;
	}

	public void setBaseCdTmp(String baseCdTmp) {
		this.baseCdTmp = baseCdTmp;
	}

	public String getBaseCd5g() {
		return baseCd5g;
	}

	public void setBaseCd5g(String baseCd5g) {
		this.baseCd5g = baseCd5g;
	}

	public String getBaseCd4d() {
		return baseCd4d;
	}

	public void setBaseCd4d(String baseCd4d) {
		this.baseCd4d = baseCd4d;
	}

	public String getBaseCdTmp5g() {
		return baseCdTmp5g;
	}

	public void setBaseCdTmp5g(String baseCdTmp5g) {
		this.baseCdTmp5g = baseCdTmp5g;
	}

	public String getBaseCdTmp4d() {
		return baseCdTmp4d;
	}

	public void setBaseCdTmp4d(String baseCdTmp4d) {
		this.baseCdTmp4d = baseCdTmp4d;
	}

	public String getC4dYn() {
		return c4dYn;
	}

	public void setC4dYn(String c4dYn) {
		this.c4dYn = c4dYn;
	}

	public String getCallFlag() {
		return callFlag;
	}

	public void setCallFlag(String callFlag) {
		this.callFlag = callFlag;
	}

	public String getIpv6Flag() {
		return ipv6Flag;
	}

	public void setIpv6Flag(String ipv6Flag) {
		this.ipv6Flag = ipv6Flag;
	}

	public String getPrefixInternet() {
		return prefixInternet;
	}

	public void setPrefixInternet(String prefixInternet) {
		this.prefixInternet = prefixInternet;
	}

	public String getPrefixUplushdtv() {
		return prefixUplushdtv;
	}

	public void setPrefixUplushdtv(String prefixUplushdtv) {
		this.prefixUplushdtv = prefixUplushdtv;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public String getBaseCdpcweb() {
		return baseCdpcweb;
	}

	public void setBaseCdpcweb(String baseCdpcweb) {
		this.baseCdpcweb = baseCdpcweb;
	}

	public String getBaseCdTmppcweb() {
		return baseCdTmppcweb;
	}

	public void setBaseCdTmppcweb(String baseCdTmppcweb) {
		this.baseCdTmppcweb = baseCdTmppcweb;
	}

}
