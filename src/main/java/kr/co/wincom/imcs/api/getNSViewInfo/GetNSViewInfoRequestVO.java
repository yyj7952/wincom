package kr.co.wincom.imcs.api.getNSViewInfo;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

@SuppressWarnings("serial")
public class GetNSViewInfoRequestVO  extends NoSqlLoggingVO implements Serializable {

	/********************************************************************
	 * getNSViewInfo API 전문 칼럼(순서 일치)
	********************************************************************/
	private String saId				= "";		// 가입자정보
    private String stbMac			= "";		// 가입자 STB MAC Address
    private String albumId			= "";		// 앨범 id
    private String linkFlag			= "";		// 컨텐츠 권한 정보
    private String appType			= "";		// 어플 타입
	
	/********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
    
    private String catGb			= "";
    private String conSaId 			= "";
    private String conMac 			= "";
    private String testSbc 			= "";
    private String idxSa			= "";
    

	private String pid			= "";
    private String resultCode	= "";
    private long tp_start 		= 0;
    
    public GetNSViewInfoRequestVO(){}
    
    public GetNSViewInfoRequestVO(String szParam){
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
				
				if(key.toLowerCase().equals("sa_id"))		this.saId       = value; 
				if(key.toLowerCase().equals("stb_mac"))		this.stbMac     = value;
				if(key.toLowerCase().equals("album_id"))	this.albumId    = value;
				if(key.toLowerCase().equals("link_flag"))	this.linkFlag    = value;
				if(key.toLowerCase().equals("app_type"))	this.appType  = value;
			}
		}
		
		//가번이 없거나 길이가 맞지 않으면 안됨. 맥번호가 없거나 길이가 맞지 않으면 안됨. RQS_TYPE 없거나 길이가 맞지 않으면 안됨.
		if( paramMap.get("sa_id").length() == 0 || paramMap.get("stb_mac").length() == 0 || paramMap.get("album_id").length() == 0 || paramMap.get("link_flag").length() == 0 )
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.saId, 7, 12, 1))
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.stbMac, 14, 14, 1))
		{
			throw new ImcsException();
		}
		
		if (!this.linkFlag.equals("M") && !this.linkFlag.equals("S")) {
			throw new ImcsException();
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

	public String getAlbumId() {
		return albumId;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}

	public String getLinkFlag() {
		return linkFlag;
	}

	public void setLinkFlag(String linkFlag) {
		this.linkFlag = linkFlag;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
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

	public String getCatGb() {
		return catGb;
	}

	public void setCatGb(String catGb) {
		this.catGb = catGb;
	}

	public String getConSaId() {
		return conSaId;
	}

	public void setConSaId(String conSaId) {
		this.conSaId = conSaId;
	}

	public String getConMac() {
		return conMac;
	}

	public void setConMac(String conMac) {
		this.conMac = conMac;
	}

	public String getTestSbc() {
		return testSbc;
	}

	public void setTestSbc(String testSbc) {
		this.testSbc = testSbc;
	}
	
    public String getIdxSa() {
		return idxSa;
	}

	public void setIdxSa(String idxSa) {
		this.idxSa = idxSa;
	}
}
