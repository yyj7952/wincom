package kr.co.wincom.imcs.api.setNSCHWatchTime;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

@SuppressWarnings("serial")
public class SetNSCHWatchTimeRequestVO extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * setNSCHWatchTime API 전문 칼럼(순서 일치)
	********************************************************************/
	private String saId			= "";	// 가입자정보
	private String stbMac		= "";	// 가입자 STB MAC Address
	private String albumId		= "";	// 앨범 ID
	private String serviceId	= "";	// 채널 서비스 ID
	private String watchDate	= "";	// 시청시각
	private String runTime		= "";	// 총재생시간
	private String nWatchYn		= "";	// nScreen(STB) 권한으로 시청 여부
	private String nSaId		= "";	// nScreen(STB) 가입자 번호
    private String nStbMac		= "";	// nScreen(STB) 가입자 맥주소
    private String appType     	= "";	// 앱 타입
    
    /********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
    private String pid			= "";
    private String resultCode	= "";
    private long tp_start = 0;
    
    public SetNSCHWatchTimeRequestVO(){}
    
    public SetNSCHWatchTimeRequestVO(String szParam){
    	CommonService commonService = new CommonService();
    	
    	String[] arrStat = szParam.split("\\|");
		
		String key		= "";
		String value	= "";
		int    nStr		= 0;
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		
		/*String szTemp = szParam.toLowerCase();
		
		if( szTemp.indexOf("sa_id") == -1 || szTemp.indexOf("stb_mac") == -1
				|| szTemp.indexOf("album_id") == -1 || szTemp.indexOf("second") == -1 
				|| szTemp.indexOf("buying_date") == -1 )
		{
			throw new ImcsException();
		}*/
		
		for(int i = 0; i < arrStat.length; i++){
			nStr	= arrStat[i].indexOf("=");
			
			if(nStr > 0) {
				key = arrStat[i].substring(0, nStr).toLowerCase();
				value = arrStat[i].substring(nStr + 1, arrStat[i].length());
				
				paramMap.put(key, value);
				
				/*if( !value.matches(ImcsConstants.N_SP_PTN) && key.toLowerCase().indexOf("name") == -1 ){
					//특수문자 있음
					throw new ImcsException();
				}*/
				
				if(key.toLowerCase().equals("sa_id"))			this.saId = value;
				if(key.toLowerCase().equals("stb_mac"))			this.stbMac = value;
				if(key.toLowerCase().equals("album_id"))		this.albumId = value;
				if(key.toLowerCase().equals("service_id"))		this.serviceId = value;
				if(key.toLowerCase().equals("watch_date"))		this.watchDate = value;
				if(key.toLowerCase().equals("run_time"))		this.runTime = value;				
				if(key.toLowerCase().equals("n_watch_yn"))		this.nWatchYn = value;
				if(key.toLowerCase().equals("n_sa_id"))			this.nSaId = value;
				if(key.toLowerCase().equals("n_stb_mac"))		this.nStbMac = value;
				if(key.toLowerCase().equals("app_type"))		this.appType = value;				
			}
		}	
		
		if(!commonService.getValidParam(this.saId, 7, 12, 1))
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.stbMac, 14, 14, 1))
		{
			throw new ImcsException();
		}
		
		if(!(commonService.getValidParam(this.albumId, 15, 15, 1)))
		{
			throw new ImcsException();
		}
		
		if(!(commonService.getValidParam(this.serviceId, 3, 3, 3)))
		{
			throw new ImcsException();
		}
		
		if(!(commonService.getValidParam(this.watchDate, 14, 14, 3)))
		{
			throw new ImcsException();
		}
		
		if(!(commonService.getValidParam(this.runTime, 1, 15, 3)))
		{
			throw new ImcsException();
		}
		
		if(!(commonService.getValidParam(this.nWatchYn, 1, 1, 2)))
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.nSaId, 0, 12, 1))
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.nStbMac, 0, 14, 1))
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.appType, 4, 4, 2))
		{
			throw new ImcsException();
		}
		
		switch(this.nWatchYn)
		{
			case "N":
				this.nSaId = "X";
				this.nStbMac = "X";
				break;
			case "Y":
				if(!commonService.getValidParam(this.nSaId, 7, 12, 1))
				{
					throw new ImcsException();
				}
				
				if(!commonService.getValidParam(this.nStbMac, 14, 14, 1))
				{
					throw new ImcsException();
				}
				
				throw new ImcsException("1", "해당 채널은 엔스크린 기능을 지원하지 않습니다.", "", "");
			default:
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

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getWatchDate() {
		return watchDate;
	}

	public void setWatchDate(String watchDate) {
		this.watchDate = watchDate;
	}

	public String getRunTime() {
		return runTime;
	}

	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}

	public String getnWatchYn() {
		return nWatchYn;
	}

	public void setnWatchYn(String nWatchYn) {
		this.nWatchYn = nWatchYn;
	}

	public String getnSaId() {
		return nSaId;
	}

	public void setnSaId(String nSaId) {
		this.nSaId = nSaId;
	}

	public String getnStbMac() {
		return nStbMac;
	}

	public void setnStbMac(String nStbMac) {
		this.nStbMac = nStbMac;
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

	public long getTp_start() {
		return tp_start;
	}

	public void setTp_start(long tp_start) {
		this.tp_start = tp_start;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

}
