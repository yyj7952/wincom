package kr.co.wincom.imcs.api.chkBuyNSConts;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;


@SuppressWarnings("serial")
public class ChkBuyNSContsRequestVO  extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * ChkBuyNSConts API 전문 칼럼(순서 일치)
	********************************************************************/
    private String saId		= "";
    private String stbMac	= "";
    private String albumId	= "";
    private String contsType	= "";
    
    /********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
    private String buyingDate	= "";
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
    
    public ChkBuyNSContsRequestVO(){}
    public ChkBuyNSContsRequestVO(String szParam){
    	CommonService commonService = new CommonService();
    	
    	String[] arrStat = szParam.split("\\|");
		
		String key		= "";
		String value	= "";
		int    nStr		= 0;
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		
		/*String szTemp = szParam.toLowerCase();
		
		if( szTemp.indexOf("sa_id") == -1 || szTemp.indexOf("stb_mac") == -1
				|| szTemp.indexOf("album_id") == -1 || szTemp.indexOf("conts_type") == -1 )
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
				if(key.toLowerCase().equals("conts_type"))		this.contsType = value;
			}
		}
		
		//ChkBuyNSContsController.saId = paramMap.get("sa_id");
		
		//ChkBuyNSContsController.stbMac = paramMap.get("stb_mac");	
		
		/*if(this.saId.length() > 12 || this.saId.length() < 7 ){
			throw new ImcsException();
		}
		
		if(this.stbMac.length() > 39 || this.stbMac.length() < 14 ){
			throw new ImcsException();
		}*/
		
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
		
		if(!commonService.getValidParam(this.contsType, 1, 1, 3))
		{
			throw new ImcsException();
		}
		
		if( "7".equals(this.contsType) ){
			this.contsType = "3";
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
	
	public String getContsType() {
		return contsType;
	}

	public void setContsType(String contsType) {
		this.contsType = contsType;
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

	public String getBuyingDate() {
		return buyingDate;
	}

	public void setBuyingDate(String buyingDate) {
		this.buyingDate = buyingDate;
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
	
}
