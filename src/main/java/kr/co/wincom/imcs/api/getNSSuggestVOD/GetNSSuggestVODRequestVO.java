package kr.co.wincom.imcs.api.getNSSuggestVOD;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;


@SuppressWarnings("serial")
public class GetNSSuggestVODRequestVO  extends NoSqlLoggingVO implements Serializable {

	/********************************************************************
	 * GetNSSuggestVOD API 전문 칼럼(순서 일치)
	********************************************************************/
    private String saId 		= "";		// 가입자 정보 
    private String stbMac 		= "";		// 가입자 STB MAC Address
    private String catId 		= "";		// 카테고리ID
    private String definFlag 	= "";		// 사용자 화질 설정 FLAG (1:HD, 3:SD)
    private String nscType 		= "";		// N스크린 단말타입
    private String albumId 		= "";		// 앨범 ID
    private String quickDisYn 	= "";		// 퀵배포 컨텐츠 포함여부
    
    
    /********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/    
    private String pid			= "";
    private String resultCode	= "";
    private String cateGb 		= "";
    private String imgUrl 		= "";
    private String nscGb 		= "";
    private String viewFlag1 	= "";
    private String viewFlag2 	= "";
    
    private String genreLarge 	= "";
    private String genreMid 	= "";
    private String genreSmall 	= "";
	private String testSbc		= "";
	
    private int albumSeq;
   
    private long rankingStart;
    private long rankingEnd;
    private long tp_start = 0;
    
    public GetNSSuggestVODRequestVO(){}
    
    public GetNSSuggestVODRequestVO(String szParam){
    	
    	String[] arrStat = szParam.split("\\|");
		
		String key		= "";
		String value	= "";
		int    nStr		= 0;
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		
		/*String szTemp = szParam.toLowerCase();
		
		if( szTemp.indexOf("sa_id") == -1 || szTemp.indexOf("stb_mac") == -1
				|| szTemp.indexOf("cat_id") == -1 || szTemp.indexOf("defin_flag") == -1 )
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
				if(key.toLowerCase().equals("cat_id"))			this.catId = value;
				if(key.toLowerCase().equals("defin_flag"))		this.definFlag = value;
				if(key.toLowerCase().equals("nsc_type"))		this.nscType = value;
				if(key.toLowerCase().equals("album_id"))		this.albumId = value;
				if(key.toLowerCase().equals("quick_dis_yn"))	this.quickDisYn = value;
			}
		}
		
		//GetNSSuggestVODController.saId = paramMap.get("sa_id");
		
		//GetNSSuggestVODController.stbMac = paramMap.get("stb_mac");
		
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null
				|| paramMap.get("cat_id") == null || paramMap.get("defin_flag") == null )
		{
			throw new ImcsException();
		}
		
		/*if(this.saId.length() > 12 || this.saId.length() < 7 ){
			throw new ImcsException();
		}
		
		if(this.stbMac.length() > 39 || this.stbMac.length() < 14 ){
			throw new ImcsException();
		}*/
		
		this.quickDisYn	= StringUtil.replaceNull(this.quickDisYn, "N");
		
		/*switch (quickDisYn) {
		case "Y":
		case "N":
			break;

		default:
			throw new ImcsException();
		}*/
		
		if("Y".equals(this.quickDisYn))		this.viewFlag1 = "P";
		else								this.viewFlag1 = "V";

		
		this.albumSeq = 0;
		
		if("PAD".equals(this.nscType))		this.nscGb = "P";
		if("LTE".equals(nscType))			this.nscGb = "P";
		if("PAW".equals(nscType))			this.nscGb = "W";
			
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

	public String getCateGb() {
		return cateGb;
	}

	public void setCateGb(String cateGb) {
		this.cateGb = cateGb;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getCatId() {
		return catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
	}

	public String getDefinFlag() {
		return definFlag;
	}

	public void setDefinFlag(String definFlag) {
		this.definFlag = definFlag;
	}

	public int getAlbumSeq() {
		return albumSeq;
	}

	public void setAlbumSeq(int albumSeq) {
		this.albumSeq = albumSeq;
	}

	public String getNscType() {
		return nscType;
	}

	public void setNscType(String nscType) {
		this.nscType = nscType;
	}

	public String getNscGb() {
		return nscGb;
	}

	public void setNscGb(String nscGb) {
		this.nscGb = nscGb;
	}

	public String getAlbumId() {
		return albumId;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}

	public long getRankingStart() {
		return rankingStart;
	}

	public void setRankingStart(long rankingStart) {
		this.rankingStart = rankingStart;
	}

	public long getRankingEnd() {
		return rankingEnd;
	}

	public void setRankingEnd(long rankingEnd) {
		this.rankingEnd = rankingEnd;
	}

	public String getQuickDisYn() {
		return quickDisYn;
	}

	public void setQuickDisYn(String quickDisYn) {
		this.quickDisYn = quickDisYn;
	}

	public String getViewFlag1() {
		return viewFlag1;
	}

	public void setViewFlag1(String viewFlag1) {
		this.viewFlag1 = viewFlag1;
	}

	public String getViewFlag2() {
		return viewFlag2;
	}

	public void setViewFlag2(String viewFlag2) {
		this.viewFlag2 = viewFlag2;
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

	public String getGenreLarge() {
		return genreLarge;
	}

	public void setGenreLarge(String genreLarge) {
		this.genreLarge = genreLarge;
	}

	public String getGenreMid() {
		return genreMid;
	}

	public void setGenreMid(String genreMid) {
		this.genreMid = genreMid;
	}

	public String getGenreSmall() {
		return genreSmall;
	}

	public void setGenreSmall(String genreSmall) {
		this.genreSmall = genreSmall;
	}

	public String getTestSbc() {
		return testSbc;
	}

	public void setTestSbc(String testSbc) {
		this.testSbc = testSbc;
	}
	
	
}
