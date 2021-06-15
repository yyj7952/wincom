package kr.co.wincom.imcs.api.authorizeVView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.regex.Pattern;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;


@SuppressWarnings("serial")
public class AuthorizeVViewRequestVO  extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * AuthorizeNSView API 전문 칼럼(순서 일치)
	********************************************************************/
    private String saId			= "";		// 가입자 정보
    private String stbMac		= "";		// 가입자 MAC ADDRESS
    private String albumId		= "";		// 앨범ID
    private String adapType		= "";		// Adaptive Streaming Type (HLS, DS, SS)
    private String baseGb		= "";		// 기지국 코드 구분
    private String baseCd		= "";		// 기지국 코드
    private String appType		= "";		// 어플타입 (RABC)
    private String definFlag	= "";		// 사용자 화질가능 FLAG
    private String decPosYn		= "";		// 다국어(암호화) 자막  복호화 가능 단말 여부
    
    /********************************************************************
   	 * 추가 사용 파라미터
   	********************************************************************/
    private String pid			= "";
    private String resultCode	= "";
    
    private String bitRate		= "";    
    private String svcNode	= "";
    
    // 2019.10.11 - IPv6듀얼스택 제공 변수 추가
    private String	ipv6Flag;
    private String	prefixInternet;
    private String	prefixUplushdtv;
    
    private int ppvYn = 0;    
    private Integer result_set = 0;
    
    private long tp_start = 0;
    
    public AuthorizeVViewRequestVO(){}
    
    public AuthorizeVViewRequestVO(String szParam){
    	CommonService commonService = new CommonService();
    	
    	String[] arrStat = szParam.split("\\|");
		
		String key		= "";
		String value	= "";
		int    nStr		= 0;
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		
		/*String szTemp = szParam.toLowerCase();
		
		if( szTemp.indexOf("sa_id") == -1 || szTemp.indexOf("stb_mac") == -1
				|| szTemp.indexOf("album_id") == -1 || szTemp.indexOf("adaptive_type") == -1 
				|| szTemp.indexOf("base_gb") == -1 || szTemp.indexOf("base_cd") == -1
				|| szTemp.indexOf("app_type") == -1 )
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
				if(key.toLowerCase().equals("album_id"))		this.albumId = value;
				if(key.toLowerCase().equals("adaptive_type"))	this.adapType = value;
				if(key.toLowerCase().equals("base_gb"))			this.baseGb = value;
				if(key.toLowerCase().equals("base_cd"))			this.baseCd = value;
				if(key.toLowerCase().equals("app_type"))		this.appType = value;
				if(key.toLowerCase().equals("defin_flag"))		this.definFlag = value;
				if(key.toLowerCase().equals("dec_pos_yn"))		this.decPosYn = value;
				if(key.toLowerCase().equals("ipv6_flag"))		this.ipv6Flag = value;
				if(key.toLowerCase().equals("svc_node"))		this.svcNode = value;
			}
		}
		
		//AuthorizeVViewController.saId = paramMap.get("sa_id");
		
		//AuthorizeVViewController.stbMac = paramMap.get("stb_mac");	
		
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null
				|| paramMap.get("album_id") == null || paramMap.get("adaptive_type") == null 
				|| paramMap.get("base_gb") == null
				|| paramMap.get("app_type") == null )
		{
			throw new ImcsException();
		}
		
		this.definFlag	= StringUtil.replaceNull(this.definFlag, "");
		this.decPosYn	= StringUtil.replaceNull(this.decPosYn, "N");
		this.svcNode	= StringUtil.replaceNull(this.svcNode, "N");
		this.baseGb		= StringUtil.replaceNull(this.baseGb, "N");
		this.ipv6Flag	= StringUtil.replaceNull(this.ipv6Flag, "N");
		
		// 2019.05.13 - BASE_CD 없이 올리는 케이스가 있어 X로 처리해서 오류가 안나게 대응
		this.baseCd		= StringUtil.replaceNull(this.baseCd, "X");
		
		if(!commonService.getValidParam(this.saId, 7, 12, 1))
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.stbMac, 14, 14, 1))
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.albumId, 15, 15, 1))
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.baseCd, 0, 50, 1))
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.ipv6Flag, 1, 1, 2))
		{
			throw new ImcsException();
		}
		
		if(this.baseGb.equals("Y"))
		{
			switch(this.baseCd.substring(0, 1))
			{
				case "W":
				case "L":
					break;
				case "G":
					this.baseCd = "L" + this.baseCd.substring(1);
					break;
				case "F":
					this.baseCd = "W" + this.baseCd.substring(1);
					break;
				case "V":
					this.baseCd = "L" + this.baseCd.substring(1);
					break;
				// 2020.11.11 - 운영팀 테스트용으로 T BASE_CD 허용 (황영환 책임님 요청)
				case "T":
					break;
				default:
					throw new ImcsException();
			}
		}
		
		switch (this.svcNode) {
			case "N":
				break;
//			case "R":
//				break;
			case "U":
				break;
			default:
				throw new ImcsException();
		}
				
		if("1".equals(this.definFlag))		this.bitRate = "M1";
		if("2".equals(this.definFlag))		this.bitRate = "M2";
		if("".equals(this.definFlag))		this.bitRate = "M2";
		if("".equals(this.decPosYn))		this.decPosYn = "N";
				
		switch(this.ipv6Flag)
		{
			case "Y":
			case "N":
				break;
			default:
				throw new ImcsException();
		}
		
		/*switch (baseGb) {
		case "Y":
		case "N":
			break;

		default:
			throw new ImcsException();
		}
		
		switch (decPosYn) {
		case "Y":
		case "N":
			break;

		default:
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

	public String getAlbumId() {
		return albumId;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}

	public String getAdapType() {
		return adapType;
	}

	public void setAdapType(String adapType) {
		this.adapType = adapType;
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

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public String getDefinFlag() {
		return definFlag;
	}

	public void setDefinFlag(String definFlag) {
		this.definFlag = definFlag;
	}

	public String getBitRate() {
		return bitRate;
	}

	public void setBitRate(String bitRate) {
		this.bitRate = bitRate;
	}
	
	public int getPpvYn() {
		return ppvYn;
	}

	public void setPpvYn(int ppvYn) {
		this.ppvYn = ppvYn;
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

	public Integer getResult_set() {
		return result_set;
	}

	public void setResult_set(Integer result_set) {
		this.result_set = result_set;
	}

	public long getTp_start() {
		return tp_start;
	}

	public void setTp_start(long tp_start) {
		this.tp_start = tp_start;
	}

	public String getDecPosYn() {
		return decPosYn;
	}

	public void setDecPosYn(String decPosYn) {
		this.decPosYn = decPosYn;
	}

	public String getSvcNode() {
		return svcNode;
	}

	public void setSvcNode(String svcNode) {
		this.svcNode = svcNode;
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
