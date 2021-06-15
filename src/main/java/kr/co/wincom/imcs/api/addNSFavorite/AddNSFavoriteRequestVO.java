package kr.co.wincom.imcs.api.addNSFavorite;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

public class AddNSFavoriteRequestVO extends NoSqlLoggingVO implements Serializable {
	private static final long serialVersionUID = 820027358203692775L;

	
	/********************************************************************
	 * addNSFavorite API 전문 칼럼(순서 일치)
	********************************************************************/
	private String saId			= "";	// [ 12] 가입자정보
	private String stbMac		= "";	// [ 14] 가입자 STB MAC Address
	private String contsId		= "";	// [ 20] 컨텐츠 아이디 (패키지 ID or 컨텐츠 ID)
	private String contsName	= "";	// [100] 영화 제목
	private String chaNum		= "";	// [  5] 단축 채널 번호
	private String price		= "";	// [ 15] 금액
	private String prInfo		= "";	// [  5] 나이제한
	private String catId		= "";	// [  5] 소속 카테고리 ID
	
	private String contsGb		= "";
	
	/********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
	private String favIdx		= "";	// 찜목록 인덱스
	private String pid			= "";	// 프로세스ID - 로그용
	private String resultCode	= "";
	
	
	public AddNSFavoriteRequestVO(){}
	
	public AddNSFavoriteRequestVO(String szParam){
		String[] arrStat = szParam.split("\\|");
		
		String key		= "";
		String value	= "";
		int    nStr		= 0;
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		
		/*String szTemp = szParam.toLowerCase();
		
		if( szTemp.indexOf("sa_id") == -1 || szTemp.indexOf("stb_mac") == -1
				|| szTemp.indexOf("conts_id") == -1 || szTemp.indexOf("conts_name") == -1 
				|| szTemp.indexOf("cha_num") == -1 || szTemp.indexOf("price") == -1
				|| szTemp.indexOf("pr_info") == -1 || szTemp.indexOf("cat_id") == -1 )
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
				if(key.toLowerCase().equals("conts_name"))		this.setContsName(value);
				if(key.toLowerCase().equals("cha_num"))			this.setChaNum(value);
				if(key.toLowerCase().equals("buying_price"))	this.setPrice(value);
				if(key.toLowerCase().equals("pr_info"))			this.setPrInfo(value);
				if(key.toLowerCase().equals("cat_id"))			this.setCatId(value);
				//if(key.toLowerCase().equals("conts_gb"))		this.setContsGb(value);
			}
		}
		
		//AddNSFavoriteController.saId = paramMap.get("sa_id");
		
		//AddNSFavoriteController.stbMac = paramMap.get("stb_mac");
		
		if(  paramMap.get("sa_id") == null ||  paramMap.get("stb_mac") == null
				||  paramMap.get("conts_id") == null ||  paramMap.get("conts_name") == null 
				||  paramMap.get("cha_num") == null ||  paramMap.get("buying_price") == null
				||  paramMap.get("pr_info") == null ||  paramMap.get("cat_id") == null )
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

	public String getContsName() {
		return contsName;
	}

	public void setContsName(String contsName) {
		this.contsName = contsName;
	}

	public String getChaNum() {
		return chaNum;
	}

	public void setChaNum(String chaNum) {
		this.chaNum = chaNum;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getPrInfo() {
		return prInfo;
	}

	public void setPrInfo(String prInfo) {
		this.prInfo = prInfo;
	}

	public String getCatId() {
		return catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
	}

	public String getFavIdx() {
		return favIdx;
	}

	public void setFavIdx(String favIdx) {
		this.favIdx = favIdx;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getContsGb() {
		return contsGb;
	}

	public void setContsGb(String contsGb) {
		this.contsGb = contsGb;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
}
