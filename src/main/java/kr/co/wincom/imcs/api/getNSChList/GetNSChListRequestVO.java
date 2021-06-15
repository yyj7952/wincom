package kr.co.wincom.imcs.api.getNSChList;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;


@SuppressWarnings("serial")
public class GetNSChListRequestVO  extends NoSqlLoggingVO implements Serializable {

	/********************************************************************
	 *  GetNSVSI API 전문 칼럼(순서 일치)
	********************************************************************/	
    private String saId 				= "";	// 가입자 번호
    private String stbMac 				= "";	// 가입자 STB MAC Address
    private String nscType 				= "";	// N스크린 단말 타입
    private String baseGb 				= "";	// 기지국 코드 구분
    private String baseCd 				= "";	// 기지국 코드
    private String youthYn 				= "";	// 청소년 요금제 여부
    private String orderGb 				= "";	// 정렬 구분 (N:기본, H:인기순, A:제목순)
    private String pooqYn 				= "";	// Pooq 채널 여부
    private String hdtvViewGb 			= "";	// HDTV 채널노출 구분
    private String fiveChYn 			= "";	// 5채널 정보 제공
    private String svcNode 				= "";		// 서비스 노드 구분 (N or NULL : DEFAULT 노드 정보 제공, R : 프로야구&해외로밍 노드 정보 제공
    
    /********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
    private String pid					= "";
    private String resultCode			= "";
    private String rBaseCode 			= "";
    private String cBaseCode 			= "";
    private String nodeCd 				= "";
    private String baseOneCd 			= "";
    private String baseCondi 			= "";
    private String pooqYnCom 			= "";
    private String youthYnCom 			= "";
    private String mProdId 				= "";
    private String testSbc 				= "";
    private String serviceId 			= "";
    private String virtualChFlag        = "";
    private String genre1               = "";
    private String contentsId           = "";
    
    // 2017.02.23 - 모바일 골프APP 변수 추가
    private String hdtvGolfGb 			= "";	// HDTV 골프 구분
    
    //2019.08.27 APP_TYPE 추가 - 게임앱에서는 모바일TV와 동일하게 호출하여 모든 채널을 가져가지만 WEBUI에서 게입채널만 단말에 정보 제공하기로 함. IMCS에서는 게입앱 호출했는지만 확인할 수 있도록 한다.
    private String appType 				= "";	// HDTV 골프 구분
    
    // 2019.10.11 - IPv6듀얼스택 제공 변수 추가
    private String	ipv6Flag;
    private String	prefixInternet;
    private String	prefixUplushdtv;
    
    private String param	= "";

    
    private long tp_start = 0;
    
    public GetNSChListRequestVO(){}
    
    public GetNSChListRequestVO(String szParam){
    	CommonService commonService = new CommonService();
    	
    	String[] arrStat = szParam.split("\\|");
		
		String key		= "";
		String value	= "";
		int    nStr		= 0;
		
		this.param = szParam;
		
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
				if(key.toLowerCase().equals("pooq_yn"))			this.pooqYn = value;
				if(key.toLowerCase().equals("hdtv_view_gb"))	this.hdtvViewGb = value;
				if(key.toLowerCase().equals("five_ch_yn"))		this.fiveChYn = value;
				if(key.toLowerCase().equals("svc_node"))		this.svcNode = value;
				if(key.toLowerCase().equals("app_type"))		this.appType = value;
				if(key.toLowerCase().equals("ipv6_flag"))		this.ipv6Flag = value;
			}
		}			
		
		this.fiveChYn = StringUtil.replaceNull(this.fiveChYn, "N");
		this.svcNode = StringUtil.replaceNull(this.svcNode, "N");
		this.appType = StringUtil.replaceNull(this.appType, "RUSA");
		this.ipv6Flag	= StringUtil.replaceNull(this.ipv6Flag, "N");
		
		
		if(!commonService.getValidParam(this.saId, 7, 12, 1))
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.stbMac, 14, 14, 1))
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.pooqYn, 1, 1, 2))
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.hdtvViewGb, 1, 1, 2))
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.appType, 4, 4, 2))
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.ipv6Flag, 1, 1, 2))
		{
			throw new ImcsException();
		}
		
		switch (pooqYn) {
		case "Y":
		case "N":
		case "P":
			break;

		default:
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
		
		switch (fiveChYn) {
		case "Y":
		case "N":
			break;

		default:
			throw new ImcsException();
		}

		
		if("P".equals(this.pooqYn))		this.pooqYnCom = "Y";
		else							this.pooqYnCom = this.pooqYn;
		
		if(this.baseCd != null && !"".equals(this.baseCd)){
	    	this.baseOneCd = this.baseCd.substring(0, 1);
	    }
		
		if (this.svcNode.length() != 1) { //2018.07.31 권형도 (lgvod032.c line:554)
			throw new ImcsException();
		}
		
		switch (this.svcNode) {
			//프로야구 채널만 조회하도록 view_gb값 변경
			case "R":
				this.hdtvViewGb = "P";
				break;
			default:
				break;
		}
		
		switch(this.ipv6Flag)
		{
			case "Y":
			case "N":
				break;
			default:
				throw new ImcsException();
		}
							
//		Date today = new Date();
//		SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMDD");
//		
//		stampToday = sdf.format(today);
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

	public String getBaseOneCd() {
		return baseOneCd;
	}

	public void setBaseOneCd(String baseOneCd) {
		this.baseOneCd = baseOneCd;
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

	public String getOrderGb() {
		return orderGb;
	}

	public void setOrderGb(String orderGb) {
		this.orderGb = orderGb;
	}

	public long getTp_start() {
		return tp_start;
	}

	public void setTp_start(long tp_start) {
		this.tp_start = tp_start;
	}

	public String getmProdId() {
		return mProdId;
	}

	public void setmProdId(String mProdId) {
		this.mProdId = mProdId;
	}

	public String getTestSbc() {
		return testSbc;
	}

	public void setTestSbc(String testSbc) {
		this.testSbc = testSbc;
	}

	public String getrBaseCode() {
		return rBaseCode;
	}

	public void setrBaseCode(String rBaseCode) {
		this.rBaseCode = rBaseCode;
	}

	public String getBaseCondi() {
		return baseCondi;
	}

	public void setBaseCondi(String baseCondi) {
		this.baseCondi = baseCondi;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getcBaseCode() {
		return cBaseCode;
	}

	public void setcBaseCode(String cBaseCode) {
		this.cBaseCode = cBaseCode;
	}

	public String getNodeCd() {
		return nodeCd;
	}

	public void setNodeCd(String nodeCd) {
		this.nodeCd = nodeCd;
	}

	public String getFiveChYn() {
		return fiveChYn;
	}

	public void setFiveChYn(String fiveChYn) {
		this.fiveChYn = fiveChYn;
	}

	public String getSvcNode() {
		return svcNode;
	}

	public void setSvcNode(String svcNode) {
		this.svcNode = svcNode;
	}	

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getHdtvGolfGb() {
		return hdtvGolfGb;
	}

	public void setHdtvGolfGb(String hdtvGolfGb) {
		this.hdtvGolfGb = hdtvGolfGb;
	}

	public String getVirtualChFlag() {
		return virtualChFlag;
	}

	public void setVirtualChFlag(String virtualChFlag) {
		this.virtualChFlag = virtualChFlag;
	}

	public String getGenre1() {
		return genre1;
	}

	public void setGenre1(String genre1) {
		this.genre1 = genre1;
	}

	public String getContentsId() {
		return contentsId;
	}

	public void setContentsId(String contentsId) {
		this.contentsId = contentsId;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
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
	
}
