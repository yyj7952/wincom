package kr.co.wincom.imcs.api.getNSMusicCue;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

@SuppressWarnings("serial")
public class GetNSMusicCueRequestVO extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * getNSMusicList API 전문 칼럼(순서 일치)
	********************************************************************/
	private String saId			  = "";	// 가입자정보
	private String stbMac		  = "";	// 가입자 STB MAC Address
	private String eventId        = "";
	private String rqsType		  = "";	// 
	private String startTime      = "";
	private String testSbc        = "";
	
	/********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
	private String albumGb        = "";

	
    private String pid			= "";
    private String resultCode	= "";
    private long tp_start 		= 0;
    
    public GetNSMusicCueRequestVO(){}
    
    public GetNSMusicCueRequestVO(String szParam){
    	
    	
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
				if(key.toLowerCase().equals("event_id"))	this.eventId    = value;
				if(key.toLowerCase().equals("rqs_type"))	this.rqsType    = value;
				if(key.toLowerCase().equals("start_time"))	this.startTime  = value;
			}
		}
		
		//가번이 없거나 길이가 맞지 않으면 안됨. 맥번호가 없거나 길이가 맞지 않으면 안됨. RQS_TYPE 없거나 길이가 맞지 않으면 안됨.
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null )
		{
			throw new ImcsException();
		}
		
		if( paramMap.get("event_id") == null || paramMap.get("rqs_type") == null  || paramMap.get("start_time") == null )
		{
			throw new ImcsException();
		}
		
	    if (this.rqsType == null || this.rqsType.equals("")) 
	    	throw new ImcsException();
	    
	    if (!this.rqsType.equals("A"))
	    {
	        this.rqsType = "A";
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

	public String getRqsType() {
		return rqsType;
	}

	public void setRqsType(String rqsType) {
		this.rqsType = rqsType;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getTestSbc() {
		return testSbc;
	}

	public void setTestSbc(String testSbc) {
		this.testSbc = testSbc;
	}

	public String getAlbumGb() {
		return albumGb;
	}

	public void setAlbumGb(String albumGb) {
		this.albumGb = albumGb;
	}
    
	
}
