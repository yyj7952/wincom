package kr.co.wincom.imcs.api.moveNSFavorIdx;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

public class MoveNSFavorIdxRequestVO extends NoSqlLoggingVO implements Serializable {
	private static final long serialVersionUID = 820027358203692775L;

	
	/********************************************************************
	 * addNSFavorite API 전문 칼럼(순서 일치)
	********************************************************************/
	private String saId			= "";	// [ 12] 가입자정보
	private String stbMac		= "";	// [ 14] 가입자 STB MAC Address
	private String contsId		= "";	// [ 20] 컨텐츠 아이디 (패키지 ID or 컨텐츠 ID)
	private String prevIndex	= "";	
	private String nextIndex	= "";	
	
	/********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
	private String pid			= "";	// 프로세스ID - 로그용
	private String resultCode	= "";
	
	
	public MoveNSFavorIdxRequestVO(){}
	
	public MoveNSFavorIdxRequestVO(String szParam){
		String[] arrStat = szParam.split("\\|");
		
		String key		= "";
		String value	= "";
		int    nStr		= 0;
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		
		/*String szTemp = szParam.toLowerCase();
		
		if( szTemp.indexOf("sa_id") == -1 || szTemp.indexOf("stb_mac") == -1
				|| szTemp.indexOf("conts_id") == -1 || szTemp.indexOf("prev_index") == -1 
				|| szTemp.indexOf("next_index") == -1 )
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
				
				if(key.toLowerCase().equals("sa_id"))			this.setSaId(value);
				if(key.toLowerCase().equals("stb_mac"))			this.setStbMac(value);
				if(key.toLowerCase().equals("conts_id"))		this.setContsId(value);
				if(key.toLowerCase().equals("prev_index"))		this.setPrevIndex(value);
				if(key.toLowerCase().equals("next_index"))		this.setNextIndex(value);
			}
		}
		
		//MoveNSFavorIdxController.saId = paramMap.get("sa_id");
		//MoveNSFavorIdxController.stbMac = paramMap.get("stb_mac");
		
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null
				|| paramMap.get("conts_id") == null || paramMap.get("prev_index") == null 
				|| paramMap.get("next_index") == null )
		{
			throw new ImcsException();
		}
		
		/*if(this.saId.length() > 12 || this.saId.length() < 7 ){
			throw new ImcsException();
		}
		
		if(this.stbMac.length() > 39 || this.stbMac.length() < 14 ){
			throw new ImcsException();
		}*/

	}
	

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	

	public String getSaId() {
		return saId;
	}

	public void setSaId(String saId) {
		if(saId == null || saId.equalsIgnoreCase("null")) saId = "";
		this.saId = saId;
	}

	public String getStbMac() {
		return stbMac;
	}

	public void setStbMac(String stbMac) {
		if(stbMac == null || stbMac.equalsIgnoreCase("null")) stbMac = "";
		this.stbMac = stbMac;
	}

	public String getContsId() {
		return contsId;
	}

	public void setContsId(String contsId) {
		this.contsId = contsId;
	}
		
	public String getPrevIndex() {
		return prevIndex;
	}

	public void setPrevIndex(String prevIndex) {
		this.prevIndex = prevIndex;
	}

	public String getNextIndex() {
		return nextIndex;
	}

	public void setNextIndex(String nextIndex) {
		this.nextIndex = nextIndex;
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
}
