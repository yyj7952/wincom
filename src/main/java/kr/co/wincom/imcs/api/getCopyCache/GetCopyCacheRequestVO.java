package kr.co.wincom.imcs.api.getCopyCache;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

@SuppressWarnings("serial")
public class GetCopyCacheRequestVO  extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * getNSGuideVod API 전문 칼럼(순서 일치)
	********************************************************************/
	private String apiName		= "";
	private String versionKey	= "";
	private String vtsId		= "";
	private String cacheVer		= "";
	private String vtsCrtVer	= "";
	private String totFileCnt	= "";
	private String delYn		= "";
	private String cacheMod		= "";
	

	/********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
	private String copyFlag		= "";
    private String pid			= "";
    private String resultCode	= "";
    
    private Integer nResultSet	= 0;
    private Integer nMessageSet	= 0;
    
    private String server99		= "";
    
    
    private long tp_start = 0;
    
    public GetCopyCacheRequestVO(){}
    
    public GetCopyCacheRequestVO(String szParam){
    	
    	String[] arrStat = szParam.split("\\|");
		
		String key		= "";
		String value	= "";
		int    nStr		= 0;
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		
		/*String szTemp = szParam.toLowerCase();
		
		if( szTemp.indexOf("sa_id") == -1 || szTemp.indexOf("stb_mac") == -1
				|| szTemp.indexOf("album_id") == -1 || szTemp.indexOf("recv_date") == -1 )
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
				if(key.toLowerCase().equals("api_name"))		this.apiName = value;
				if(key.toLowerCase().equals("version_key"))		this.versionKey = value;
				if(key.toLowerCase().equals("cache_ver"))		this.cacheVer = value;
				if(key.toLowerCase().equals("tot_file_cnt"))	this.totFileCnt = value;
				if(key.toLowerCase().equals("vts_id"))			this.vtsId = value;
				if(key.toLowerCase().equals("del_yn"))			this.delYn = value;
				if(key.toLowerCase().equals("cache_mod"))		this.cacheMod = value;
			}
		}
		
		//getNSCustInfoController.saId = paramMap.get("sa_id");
		
		//getNSCustInfoController.stbMac = paramMap.get("stb_mac");
		
		if(paramMap.get("api_name") == null || paramMap.get("version_key") == null || paramMap.get("cache_ver") == null || paramMap.get("tot_file_cnt") == null
				|| paramMap.get("vts_id") == null )
		{
			throw new ImcsException();
		}
		
		 this.delYn	= StringUtil.replaceNull(this.delYn, "N");
		 this.cacheMod	= StringUtil.replaceNull(this.cacheMod, "S");
			
	}
    
    
	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public Integer getResultSet() {
		return nResultSet;
	}

	public void setResultSet(Integer nResultSet) {
		this.nResultSet = nResultSet;
	}

	public Integer getMessageSet() {
		return nMessageSet;
	}

	public void setMessageSet(Integer nMessageSet) {
		this.nMessageSet = nMessageSet;
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

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public String getCacheVer() {
		return cacheVer;
	}

	public void setCacheVer(String cacheVer) {
		this.cacheVer = cacheVer;
	}

	public String getVersionKey() {
		return versionKey;
	}

	public void setVersionKey(String versionKey) {
		this.versionKey = versionKey;
	}

	public String getVtsId() {
		return vtsId;
	}

	public void setVtsId(String vtsId) {
		this.vtsId = vtsId;
	}

	public String getVtsCrtVer() {
		return vtsCrtVer;
	}

	public void setVtsCrtVer(String vtsCrtVer) {
		this.vtsCrtVer = vtsCrtVer;
	}

	public String getTotFileCnt() {
		return totFileCnt;
	}

	public void setTotFileCnt(String totFileCnt) {
		this.totFileCnt = totFileCnt;
	}

	public String getCopyFlag() {
		return copyFlag;
	}

	public void setCopyFlag(String copyFlag) {
		this.copyFlag = copyFlag;
	}
	
	public String getDelYn() {
		return delYn;
	}

	public void setDelYn(String delYn) {
		this.delYn = delYn;
	}

	public String getCacheMod() {
		return cacheMod;
	}

	public void setCacheMod(String cacheMod) {
		this.cacheMod = cacheMod;
	}

	public String getServer99() {
		return server99;
	}

	public void setServer99(String server99) {
		this.server99 = server99;
	}
	
}
