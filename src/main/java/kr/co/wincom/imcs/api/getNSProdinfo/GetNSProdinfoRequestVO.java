package kr.co.wincom.imcs.api.getNSProdinfo;

import java.io.Serializable;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;


@SuppressWarnings("serial")
public class GetNSProdinfoRequestVO  extends NoSqlLoggingVO implements Serializable {
	private Log imcsLogger = LogFactory.getLog("API_getNSProdInfo");
	
	StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
	String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
	String methodName = stackTraceElement.getMethodName();
	
	/********************************************************************
	 * GetNSProdinfo API 전문 칼럼(순서 일치)
	********************************************************************/
    private String saId			= "";
    private String stbMac		= "";
    private String youthYn		= "";
    private String fxType		= "";
    private String uflixYn		= "";
    private String productFlag	= "";
    //2018.12.06 - 신규 유플릭스 상품 추가
    private int customUflix;//가입자 유플릭스 가입 FLAG ( 0 : 미가입 / 1 : 구 유플릭스 가입 / 2 : 신규 유플릭스 가입 )
    
    /********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
    private String pid			= "";
    private String resultCode	= "";
    private String sortGb		= "";
    private String applGb		= "";
    private String ppmType		= "";
    private String atrctChnlDvCd	= "";
    private String pvsNscnCustNo	= "";
    private String testSbc		= "";
    private String subProdId	= "";
    //2020.08.27 - 아이들나라4.0 : 교재배송상품 대응 앱 버전 관리
    private String appVersion	= "";
    //2021.01.05 - 모바일 아이들나라4.0 Phase2 - 페어링된 IPTV 가입자 조회
    private String paringSaId			= "";
    private String paringStbMac		= "";
    //2020.01.28 - 가입상품 메뉴 코드 및 이름
    private String joinMenu		= "";
    
    private long tp_start = 0;
    
   
    
    /*    
    private String youthYnCom = "";
    private String mProdId = "";
    private String xProdId = "";
    private String hdtvSbc = "";
    private String hprod1Sbc = "";
    private String hprod2Sbc = "";
    private String hprod3Sbc = "";
    private String hprod4Sbc = "";
    private String hprod5Sbc = "";
    private String hprod6Sbc = "";
    private String hprod7Sbc = "";
    private String hprod8Sbc = "";
    private String hprod9Sbc = "";
    private String hprod0Sbc = "";
    */
    
    
    public GetNSProdinfoRequestVO(){}
    
    public GetNSProdinfoRequestVO(String szParam){
    	CommonService commonService = new CommonService();
    	
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
				if(key.toLowerCase().equals("youth_yn"))		this.youthYn = value;
				if(key.toLowerCase().equals("fx_type"))			this.fxType = value;		
				if(key.toLowerCase().equals("uflix_yn"))		this.uflixYn = value;
				if(key.toLowerCase().equals("product_flag"))	this.productFlag = value;
				if(key.toLowerCase().equals("app_version"))		this.appVersion = value;
			}
		}
		
		//GetNSProdinfoController.saId = paramMap.get("sa_id");
		
		//GetNSProdinfoController.stbMac = paramMap.get("stb_mac");
		
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null )
		{
			throw new ImcsException();
		}
		
		if(paramMap.get("app_version") == null)
		{
			this.appVersion = "000";
		}
		
		if(!commonService.getValidParam(this.saId, 7, 12, 1))
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.stbMac, 14, 14, 1))
		{
			throw new ImcsException();
		}
		
		this.youthYn	= StringUtil.replaceNull(this.youthYn, "N");
		this.fxType		= StringUtil.replaceNull(this.fxType, "N");
		this.uflixYn	= StringUtil.replaceNull(this.uflixYn, "N");
		
		if(!commonService.getValidParam(this.youthYn, 1, 1, 2))
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.uflixYn, 1, 1, 2))
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.appVersion, 3, 3, 3))
		{
			throw new ImcsException();
		}
		
		if(fxType.length() > 1){
			throw new ImcsException();
		}
		
		if("Y".equals(uflixYn))		this.sortGb = "U";
		else if( "P".equals(uflixYn) || "V".equals(uflixYn) )		this.sortGb = "P";
		else						this.sortGb = "H";
		
		this.productFlag	= StringUtil.replaceNull(this.productFlag, "A");
		if (this.productFlag.equals("G")) this.productFlag = "P";
		
		switch (youthYn) {
		case "Y":
		case "N":
			break;

		default:
			throw new ImcsException();
		}
		
		switch (fxType) {
		case "N":
		case "M":
		case "P":
		case "T":
		case "H":
			break;

		default:
			throw new ImcsException();
		}
		
		switch (uflixYn) {
		case "Y":
		case "N":
		case "P":
		case "V":
			break;

		default:
			throw new ImcsException();
		}
		
		switch (productFlag) {
		case "A":
		case "P":
		case "K":
			break;
		default:
			this.productFlag = "A";
			break;
		}
		
		this.applGb		= "NSC";
		this.ppmType	= "NN";
		
		
		switch (appVersion) {
			case "000":
			case "001":
			case "002":
				break;
			default:
				throw new ImcsException();
		}
		
		if(this.appVersion.equals("001") && !this.productFlag.equals("K"))
		{
			this.appVersion = "000";
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

	public String getYouthYn() {
		return youthYn;
	}

	public void setYouthYn(String youthYn) {
		this.youthYn = youthYn;
	}

	public String getFxType() {
		return fxType;
	}

	public void setFxType(String fxType) {
		this.fxType = fxType;
	}

	public String getUflixYn() {
		return uflixYn;
	}

	public void setUflixYn(String uflixYn) {
		this.uflixYn = uflixYn;
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

	public String getSortGb() {
		return sortGb;
	}

	public void setSortGb(String sortGb) {
		this.sortGb = sortGb;
	}

	public String getApplGb() {
		return applGb;
	}

	public void setApplGb(String applGb) {
		this.applGb = applGb;
	}

	public String getPpmType() {
		return ppmType;
	}

	public void setPpmType(String ppmType) {
		this.ppmType = ppmType;
	}

	public long getTp_start() {
		return tp_start;
	}

	public void setTp_start(long tp_start) {
		this.tp_start = tp_start;
	}

	public String getAtrctChnlDvCd() {
		return atrctChnlDvCd;
	}

	public void setAtrctChnlDvCd(String atrctChnlDvCd) {
		this.atrctChnlDvCd = atrctChnlDvCd;
	}

	public String getPvsNscnCustNo() {
		return pvsNscnCustNo;
	}

	public void setPvsNscnCustNo(String pvsNscnCustNo) {
		this.pvsNscnCustNo = pvsNscnCustNo;
	}

	public String getTestSbc() {
		return testSbc;
	}

	public void setTestSbc(String testSbc) {
		this.testSbc = testSbc;
	}

	public String getSubProdId() {
		return subProdId;
	}

	public void setSubProdId(String subProdId) {
		this.subProdId = subProdId;
	}

	public String getProductFlag() {
		return productFlag;
	}

	public void setProductFlag(String productFlag) {
		this.productFlag = productFlag;
	}

	public int getCustomUflix() {
		return customUflix;
	}

	public void setCustomUflix(int customUflix) {
		this.customUflix = customUflix;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getParingSaId() {
		return paringSaId;
	}

	public void setParingSaId(String paringSaId) {
		this.paringSaId = paringSaId;
	}

	public String getParingStbMac() {
		return paringStbMac;
	}

	public void setParingStbMac(String paringStbMac) {
		this.paringStbMac = paringStbMac;
	}
   
	public String getJoinMenu() {
		return joinMenu;
	}

	public void setJoinMenu(String joinMenu) {
		this.joinMenu = joinMenu;
	}
	
}
