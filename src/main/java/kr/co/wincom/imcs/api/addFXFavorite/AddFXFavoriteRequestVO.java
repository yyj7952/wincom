package kr.co.wincom.imcs.api.addFXFavorite;

import java.io.Serializable;
import java.util.HashMap;


import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

@SuppressWarnings("serial")
public class AddFXFavoriteRequestVO extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * addFXFavorite API 전문 칼럼(순서 일치)
	********************************************************************/
	private String saId			= "";	// [ 12] 가입자정보
	private String stbMac		= "";	// [ 14] 가입자 STB MAC Address
	private String albumId		= "";	// [ 20] 컨텐츠 아이디 (패키지 ID or 컨텐츠 ID)
	private String albumName	= "";	// [100] 영화 제목
	private String chaNum		= "";	// [  5] 단축 채널 번호
	private String price		= "";	// [ 15] 금액
	private String prInfo		= "";	// [  5] 나이제한
	private String catId		= "";	// [  5] 소속 카테고리 ID
	private String fxType		= "";
	
	/********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
	private String favIdx		= "";	// 찜목록 인덱스
	private String pid			= "";	// 프로세스ID - 로그용
	private String albumGb		= "";
	private long tp_start		= 0;
	public AddFXFavoriteRequestVO(){}
	
	public AddFXFavoriteRequestVO(String szParam){
		String[] arrStat = szParam.split("\\|");
		
		String key		= "";
		String value	= "";
		int    nStr		= 0;
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		
		/*String szTemp = szParam.toLowerCase();
		
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null
				|| paramMap.get("album_id") == null || paramMap.get("album_name") == null 
				|| paramMap.get("cha_num") == null || paramMap.get("buying_price") == null
				|| paramMap.get("pr_info") == null || paramMap.get("cat_id") == null )
		{
			throw new ImcsException();
		}*/
		
		for(int i = 0; i < arrStat.length; i++){
			nStr	= arrStat[i].indexOf("=");
			
			if(nStr > 0) {
				key = arrStat[i].substring(0, nStr).toLowerCase().trim();
				value = arrStat[i].substring(nStr + 1, arrStat[i].length()).trim();
				
				paramMap.put(key, value);
				
				/*if( !value.matches(ImcsConstants.N_SP_PTN) && key.toLowerCase().indexOf("name") == null ){
					//특수문자 있음
					throw new ImcsException();
				}*/
				
				if(key.toLowerCase().equals("sa_id"))			this.setSaId(value);
				if(key.toLowerCase().equals("stb_mac"))			this.setStbMac(value);
				if(key.toLowerCase().equals("album_id"))		this.setAlbumId(value);
				if(key.toLowerCase().equals("album_name"))		this.setAlbumName(value);
				if(key.toLowerCase().equals("cha_num"))			this.setChaNum(value);
				if(key.toLowerCase().equals("buying_price"))	this.setPrice(value);
				if(key.toLowerCase().equals("pr_info"))			this.setPrInfo(value);
				if(key.toLowerCase().equals("cat_id"))			this.setCatId(value);
				if(key.toLowerCase().equals("fx_type"))			this.setFxType(value);
			}
		}
		
		//AddFXFavoriteController.saId = paramMap.get("sa_id");
		
		//AddFXFavoriteController.stbMac = paramMap.get("stb_mac");
		
		
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null
				|| paramMap.get("album_id") == null || paramMap.get("album_name") == null 
				|| paramMap.get("cha_num") == null || paramMap.get("buying_price") == null
				|| paramMap.get("pr_info") == null || paramMap.get("cat_id") == null )
		{
			throw new ImcsException();
		}
		
		/*if(this.saId.length() > 12 || this.saId.length() < 7 ){
			throw new ImcsException();
		}
		
		if(this.stbMac.length() > 39 || this.stbMac.length() < 14 ){
			throw new ImcsException();
		}*/

		this.albumGb	= StringUtil.replaceNull(this.albumGb, "I20");
		this.fxType	= StringUtil.replaceNull(this.fxType, "N");
		
		/*switch (fxType) {
		case "N":
		case "M":
		case "P":
		case "T":
		case "H":
			break;

		default:
			throw new ImcsException();
		}*/
		
		
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

	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
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

	public String getFxType() {
		return fxType;
	}

	public void setFxType(String fxType) {
		this.fxType = fxType;
	}

	public String getAlbumGb() {
		return albumGb;
	}

	public void setAlbumGb(String albumGb) {
		this.albumGb = albumGb;
	}

	public long getTp_start() {
		return tp_start;
	}

	public void setTp_start(long tp_start) {
		this.tp_start = tp_start;
	}
}
