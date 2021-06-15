package kr.co.wincom.imcs.api.getNSLiveStat;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

@SuppressWarnings("serial")
public class GetNSLiveStatRequestVO extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * GetNSLiveStat API 전문 칼럼(순서 일치)
	********************************************************************/
	private String saId			= "";	// 가입자정보
	private String stbMac		= "";	// 가입자 STB MAC Address
	private String albumId		= "";	// 앨범ID
	private String albumIdD		= "";	// 데이터프리 앨범ID
	private String appType		= "";	// 서비스명

	private String currentDate	= "";	// 오늘 날짜
	
	/********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
	private String pid			= "";
	private String resultCode	= "";
	
	private String uflixYn1		= "";
	private String uflixYn2		= "";
	private String screenStr	= "";
	private String cIdxSa		= "";
	private int pIdxSa			= 0;
	private String svodProdBuyYn	= "1";
	private String premiumYn	= "N";
	private String mProdId		= "";
	
	private String genreInfo	= "";
	private String suggestedPrice	= "";
	private String prodType		= "";
	private String pkgYn		= "";	// 시리즈/패키지 여부
	private String shortYn		= "";	// 단편 여부
	private String contsType	= "";
	
	private String uflixBuyYn	= "";
	
	private String nSaId		= "";	// nScreen(STB) 가입자 번호
	private String nStbMac		= "";	// nScreen(STB) 가입자 맥주소
	private String testSbc		= "";	//
	
	//2019.09.06 - 신규 유플릭스 상품 추가
	private String concurrentCnt= "";
	private String customUflix 	= "";	//가입자 유플릭스 가입 FLAG ( 0 : 미가입 / 1 : 구 유플릭스 가입 / 2 : 신규 유플릭스 가입 )
	private String tempAlbumId  = "";
	private String subMobileUflixYn ="";
	private String tempUflixProdYn ="";
	private String buyPossibleYn ="";	// 모바일 유플릭스 미가입(IPTV 유플릭스 가입)일 경우에는 구매 가능한 콘텐츠면 영화월정액 상품을 보여주지 않고, 구매 가능하지 않는 콘텐츠만 영화월정액 상품을 노출 시켜서, 상세페이지 진입은 가능하도록 한다.
										// 사유 : 영화월정액 상품만 걸려있는 콘텐츠일 때 모바일 유플릭스 미가입(IPTV 유플릭스 가입)일 때, 영화월정액 상품을 다 빼고 주면 상세 페이지 진입시 구매/가입할 수 있는 상품이 없으므로 오류 발생
    //2020.11.24 - 인앱 결제 지원
    private String kidFlag	= "";
    
    private String cuesheetId = "";
    private String cacheFlag = "";
    private Boolean cacheDataFlag = false;
	private Boolean cacheBuyFlag = false;
    
	public GetNSLiveStatRequestVO(){}
	
	public GetNSLiveStatRequestVO(String szParam){
		CommonService commonService = new CommonService();
		
		String[] arrStat = szParam.split("\\|");
		
		String key		= "";
		String value	= "";
		int    nStr		= 0;
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		
		/*String szTemp = szParam.toLowerCase();
		
		if( paramMap.get("sa_id") == -1 || paramMap.get("stb_mac") == -1
				|| paramMap.get("album_id") == -1 || paramMap.get("defin_flag") == -1 )
		{
			throw new ImcsException();
		}*/
		
		for(int i = 0; i < arrStat.length; i++){
			nStr	= arrStat[i].indexOf("=");
			
			if(nStr > 0) {
				key = arrStat[i].substring(0, nStr).toLowerCase();
				value = arrStat[i].substring(nStr + 1, arrStat[i].length());
				
				paramMap.put(key, value);
				
				/*if( !value.matches(ImcsConstants.N_SP_PTN) && key.toLowerCase().indexOf("name") == -1 ){
					//특수문자 있음
					throw new ImcsException();
				}*/
				
				if(key.toLowerCase().equals("sa_id"))		this.saId = value;
				if(key.toLowerCase().equals("stb_mac"))		this.stbMac = value;
				if(key.toLowerCase().equals("album_id"))	this.albumId = value;
				if(key.toLowerCase().equals("app_type"))	this.appType = value;
				if(key.toLowerCase().equals("cache_flag"))  this.setCacheFlag(value);
			}
		}
		
		if( paramMap.get("sa_id") == null 
			|| paramMap.get("stb_mac") == null
			|| paramMap.get("album_id") == null 
		)
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
		
		if(!commonService.getValidParam(this.albumId, 15, 15, 1))
		{
			throw new ImcsException();
		}
	
		// 2020.02.19 - Fortify 보안 툴에서 SQL에서 $를 사용하면 안된다고 하여 ${album_id}_D 를 데이터프리 변수 새로 만들어서 앨범ID_D로 넣어서 사용한다.
		this.albumIdD	= this.albumId + "_D";
		
		// 2019.04.23 - JAVA의 경우 null or 빈값일 떄 데이터 조작을 하려고 하면 오류가 발생하기 때문에 디폴트 값을 넣어준다.
		if(this.appType == null || this.appType.equals(""))
		{
			this.appType = "RUSA";
		}
		
		if(this.appType != null && this.appType.equals(""))
			this.screenStr = this.appType.substring(0,1);
		
		if(this.screenStr.equals("X"))	this.screenStr = "UFLIX";
		else							this.screenStr = "NSC";
		
		if((this.saId.equals("") && this.stbMac.equals("")))
			throw new ImcsException();
		
		if( !"".equals(this.saId) ){
			this.cIdxSa = this.saId.substring(this.saId.length()-2, this.saId.length());
			try {
				this.pIdxSa = Integer.parseInt(this.cIdxSa);
			} catch (Exception e) {
				this.pIdxSa = 0;
			}
			
		}
		
		if(this.appType.substring(0,1).equals("A")) {
			this.kidFlag = "A";
		} else {
			this.kidFlag = "N";
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

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
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

	public String getUflixYn1() {
		return uflixYn1;
	}

	public void setUflixYn1(String uflixYn1) {
		this.uflixYn1 = uflixYn1;
	}

	public String getUflixYn2() {
		return uflixYn2;
	}

	public void setUflixYn2(String uflixYn2) {
		this.uflixYn2 = uflixYn2;
	}

	public String getScreenStr() {
		return screenStr;
	}

	public void setScreenStr(String screenStr) {
		this.screenStr = screenStr;
	}

	public String getcIdxSa() {
		return cIdxSa;
	}

	public void setcIdxSa(String cIdxSa) {
		this.cIdxSa = cIdxSa;
	}

	public int getpIdxSa() {
		return pIdxSa;
	}

	public void setpIdxSa(int pIdxSa) {
		this.pIdxSa = pIdxSa;
	}

	public String getSvodProdBuyYn() {
		return svodProdBuyYn;
	}

	public void setSvodProdBuyYn(String svodProdBuyYn) {
		this.svodProdBuyYn = svodProdBuyYn;
	}

	public String getPremiumYn() {
		return premiumYn;
	}

	public void setPremiumYn(String premiumYn) {
		this.premiumYn = premiumYn;
	}

	public String getmProdId() {
		return mProdId;
	}

	public void setmProdId(String mProdId) {
		this.mProdId = mProdId;
	}

	public String getGenreInfo() {
		return genreInfo;
	}

	public void setGenreInfo(String genreInfo) {
		this.genreInfo = genreInfo;
	}

	public String getSuggestedPrice() {
		return suggestedPrice;
	}

	public void setSuggestedPrice(String suggestedPrice) {
		this.suggestedPrice = suggestedPrice;
	}

	public String getProdType() {
		return prodType;
	}

	public void setProdType(String prodType) {
		this.prodType = prodType;
	}

	public String getPkgYn() {
		return pkgYn;
	}

	public void setPkgYn(String pkgYn) {
		this.pkgYn = pkgYn;
	}

	public String getShortYn() {
		return shortYn;
	}

	public void setShortYn(String shortYn) {
		this.shortYn = shortYn;
	}

	public String getUflixBuyYn() {
		return uflixBuyYn;
	}

	public void setUflixBuyYn(String uflixBuyYn) {
		this.uflixBuyYn = uflixBuyYn;
	}

	public String getContsType() {
		return contsType;
	}

	public void setContsType(String contsType) {
		this.contsType = contsType;
	}

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
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

	public String getTestSbc() {
		return testSbc;
	}

	public void setTestSbc(String testSbc) {
		this.testSbc = testSbc;
	}

	public String getTempAlbumId() {
		return tempAlbumId;
	}

	public void setTempAlbumId(String tempAlbumId) {
		this.tempAlbumId = tempAlbumId;
	}

	public String getCustomUflix() {
		return customUflix;
	}

	public void setCustomUflix(String customUflix) {
		this.customUflix = customUflix;
	}

	public String getConcurrentCnt() {
		return concurrentCnt;
	}

	public void setConcurrentCnt(String concurrentCnt) {
		this.concurrentCnt = concurrentCnt;
	}

	public String getTempUflixProdYn() {
		return tempUflixProdYn;
	}

	public void setTempUflixProdYn(String tempUflixProdYn) {
		this.tempUflixProdYn = tempUflixProdYn;
	}

	public String getSubMobileUflixYn() {
		return subMobileUflixYn;
	}

	public void setSubMobileUflixYn(String subMobileUflixYn) {
		this.subMobileUflixYn = subMobileUflixYn;
	}

	public String getBuyPossibleYn() {
		return buyPossibleYn;
	}

	public void setBuyPossibleYn(String buyPossibleYn) {
		this.buyPossibleYn = buyPossibleYn;
	}

	public String getAlbumIdD() {
		return albumIdD;
	}

	public void setAlbumIdD(String albumIdD) {
		this.albumIdD = albumIdD;
	}

	public String getKidFlag() {
		return kidFlag;
	}

	public void setKidFlag(String kidFlag) {
		this.kidFlag = kidFlag;
	}

	public String getCuesheetId() {
		return cuesheetId;
	}

	public void setCuesheetId(String cuesheetId) {
		this.cuesheetId = cuesheetId;
	}

	public String getCacheFlag() {
		return cacheFlag;
	}

	public void setCacheFlag(String cacheFlag) {
		this.cacheFlag = cacheFlag;
	}

	public Boolean getCacheDataFlag() {
		return cacheDataFlag;
	}

	public void setCacheDataFlag(Boolean cacheDataFlag) {
		this.cacheDataFlag = cacheDataFlag;
	}

	public Boolean getCacheBuyFlag() {
		return cacheBuyFlag;
	}

	public void setCacheBuyFlag(Boolean cacheBuyFlag) {
		this.cacheBuyFlag = cacheBuyFlag;
	}
	
}