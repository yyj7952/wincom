package kr.co.wincom.imcs.api.chkSubscribeProd;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;


@SuppressWarnings("serial")
public class ChkSubscribeProdRequestVO  extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * chkSubscribeProd API 전문 칼럼(순서 일치)
	********************************************************************/
    private String saId		= "";
    private String stbMac	= "";
    private String prodId	= "";
    
    /********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
    private String statusFlag	= "";
    private String ynVodOpen	= "";
    
    private String pid			= "";
    private String resultCode	= "";
    
    private Integer resultSet	= 0;
    private Integer messageSet	= 0;
    
    private long tp_start = 0;
    
    // 2017.12.05 엔스크린 관련 추가
    private String nSaId		= "";
    private String nStbMac		= "";
    
    public ChkSubscribeProdRequestVO(){}
    public ChkSubscribeProdRequestVO(String szParam){
    	
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
				if(key.toLowerCase().equals("prod_id"))			this.prodId = value;
			}
		}	
		
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null || paramMap.get("prod_id") == null)
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
		
		if(!commonService.getValidParam(this.prodId, 5, 5, 1))
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

	public String getYnVodOpen() {
		return ynVodOpen;
	}

	public void setYnVodOpen(String ynVodOpen) {
		this.ynVodOpen = ynVodOpen;
	}

	public String getStatusFlag() {
		return statusFlag;
	}

	public void setStatusFlag(String statusFlag) {
		this.statusFlag = statusFlag;
	}


	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public Integer getResultSet() {
		return resultSet;
	}

	public void setResultSet(Integer resultSet) {
		this.resultSet = resultSet;
	}

	public Integer getMessageSet() {
		return messageSet;
	}

	public void setMessageSet(Integer messageSet) {
		this.messageSet = messageSet;
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
	public String getProdId() {
		return prodId;
	}
	public void setProdId(String prodId) {
		this.prodId = prodId;
	}
	
}
