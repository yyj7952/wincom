package kr.co.wincom.imcs.api.getNSComBotList;

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
public class GetNSComBotListRequestVO  extends NoSqlLoggingVO implements Serializable {
	private Log imcsLogger = LogFactory.getLog("API_getNSComBotList");
	
	StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
	String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
	String methodName = stackTraceElement.getMethodName();
	
	/********************************************************************
	 * GetNSProdinfo API 전문 칼럼(순서 일치)
	********************************************************************/
    private String saId			= "";
    private String stbMac		= "";
    
    /********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
    private String pid			= "";
    private String resultCode	= "";
    private String testSbc		= "";
    private String viewingFlag	= "";
    private String expInterval = "";
	private String expType = "";
	private String expMaxCount = "";
	private String audioUrl = "";
    
    private long tp_start = 0;
    
   

    
    public GetNSComBotListRequestVO(){}
    
    public GetNSComBotListRequestVO(String szParam){
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
				
				if(key.toLowerCase().equals("sa_id"))			this.saId = value;
				if(key.toLowerCase().equals("stb_mac"))			this.stbMac = value;
			}
		}
		
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null )
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

	public String getTestSbc() {
		return testSbc;
	}

	public void setTestSbc(String testSbc) {
		this.testSbc = testSbc;
	}

	public String getViewingFlag() {
		return viewingFlag;
	}

	public void setViewingFlag(String viewingFlag) {
		this.viewingFlag = viewingFlag;
	}

	public String getExpInterval() {
		return expInterval;
	}

	public void setExpInterval(String expInterval) {
		this.expInterval = expInterval;
	}

	public String getExpType() {
		return expType;
	}

	public void setExpType(String expType) {
		this.expType = expType;
	}

	public String getExpMaxCount() {
		return expMaxCount;
	}

	public void setExpMaxCount(String expMaxCount) {
		this.expMaxCount = expMaxCount;
	}

	public String getAudioUrl() {
		return audioUrl;
	}

	public void setAudioUrl(String audioUrl) {
		this.audioUrl = audioUrl;
	}
   
	
}
