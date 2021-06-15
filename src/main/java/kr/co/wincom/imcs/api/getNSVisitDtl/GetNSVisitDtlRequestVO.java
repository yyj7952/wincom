package kr.co.wincom.imcs.api.getNSVisitDtl;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;


@SuppressWarnings("serial")
public class GetNSVisitDtlRequestVO  extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * getNSVisitDtl API 전문 칼럼(순서 일치)
	********************************************************************/
	private String saId			= "";		// 가입자 정보
    private String stbMac		= "";		// 가입자 MAC_ADDRESS
    private String albumId		= "";		// 앨범 ID
    private String catId		= "";		// 카테고리 ID
    
    
	/********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
    private String pid		= "";
    private String resultCode	= "";
    
    
    private long tp_start = 0;
    
    public GetNSVisitDtlRequestVO(){}
    
    public GetNSVisitDtlRequestVO(String szParam){
    	
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
				
				/*if( !value.matches(ImcsConstants.N_SP_PTN) && key.toLowerCase().indexOf("name") == -1 ){
					//특수문자 있음
					throw new ImcsException();
				}*/
				
				if(key.toLowerCase().equals("sa_id"))			this.saId = value;
				if(key.toLowerCase().equals("stb_mac"))			this.stbMac = value;
				if(key.toLowerCase().equals("cat_id"))			this.catId = value;
				if(key.toLowerCase().equals("album_id"))		this.albumId = value;
			}
		}
		
		//GetNSVisitDtlController.saId = paramMap.get("sa_id");
		//GetNSVisitDtlController.stbMac = paramMap.get("stb_mac");
		
		/*if(this.saId.length() > 12 || this.saId.length() < 7 ){
			throw new ImcsException();
		}
		
		if(this.stbMac.length() > 39 || this.stbMac.length() < 14 ){
			throw new ImcsException();
		}*/
		
		/*String szTempCatId	= "";
		if(this.catId != null && this.catId.length() > 5)
			szTempCatId	= this.catId.substring(0, 4);*/
		
		if( "".equals(this.saId) && "".equals(this.stbMac) || this.catId.indexOf("undef") != -1){
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
	public String getCatId() {
		return catId;
	}
	public void setCatId(String catId) {
		this.catId = catId;
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
}
