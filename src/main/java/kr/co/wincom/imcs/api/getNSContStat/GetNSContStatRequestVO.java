package kr.co.wincom.imcs.api.getNSContStat;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

@SuppressWarnings("serial")
public class GetNSContStatRequestVO extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * GetNSContStat API 전문 칼럼(순서 일치)
	********************************************************************/
	private String saId			= "";	// 가입자정보
	private String stbMac		= "";	// 가입자 STB MAC Address
	private String albumId		= "";	// 앨범ID
	private String albumIdD		= "";	// 데이터프리 앨범ID
	private String definFlag	= "";	// 사용자 화질 설정 (1:HD, 3:SD)
	private String youthYn		= "";	// 청소년요금제여부 
	private String fxType		= "";	// 유플릭스 타입 (M : MobileWeb, P : PcWeb, T : TvgApp, H : HDTV)
	private String catId		= "";	// 카테고리ID
	private String appType		= "";	// 서비스명
	private String firmFlag		= "";	// 추가 기능FLAG(P : 구매연동(NSCREEN) 가능 단말, N or NULL : 구매연동(NSCREEN) 불가능 단말)
	
	private String fxTypeTemp	= "";	// 유플릭스 접속 여부
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
	private String svodProdId	= "";
	private String svodOnly		= "";
	private String svodYn		= "";
	private String pkgYn		= "";	// 시리즈/패키지 여부
	private String shortYn		= "";	// 단편 여부
	private String contsType	= "";
	
	private String uflixBuyYn	= "";
	
	private String nSaId		= "";	// nScreen(STB) 가입자 번호
	private String nStbMac		= "";	// nScreen(STB) 가입자 맥주소
	private String testSbc		= "";	//
	
    //2018.05.15 TV앱 관련 필드 추가
	private String freeFlag     = "";
	
	//2019.09.06 - 신규 유플릭스 상품 추가
	private String concurrentCnt= "";
	private String customUflix 	= "";	//가입자 유플릭스 가입 FLAG ( 0 : 미가입 / 1 : 구 유플릭스 가입 / 2 : 신규 유플릭스 가입 )
	private String tempAlbumId  = "";
	private String subMobileUflixYn ="";
	private String tempUflixProdYn ="";
	private String buyPossibleYn ="";	// 모바일 유플릭스 미가입(IPTV 유플릭스 가입)일 경우에는 구매 가능한 콘텐츠면 영화월정액 상품을 보여주지 않고, 구매 가능하지 않는 콘텐츠만 영화월정액 상품을 노출 시켜서, 상세페이지 진입은 가능하도록 한다.
										// 사유 : 영화월정액 상품만 걸려있는 콘텐츠일 때 모바일 유플릭스 미가입(IPTV 유플릭스 가입)일 때, 영화월정액 상품을 다 빼고 주면 상세 페이지 진입시 구매/가입할 수 있는 상품이 없으므로 오류 발생
	//2020.08.18 - 모바일TV 11월향 : 라이센스가 끝났을 때 FVOD는 응답값에서 뺴줘야 한다.
	private String licensingEndYn ="";
	//2020.08.27 - 아이들나라4.0 : 교재배송상품 대응 앱 버전 관리
    private String appVersion	= "";
    
    //2020.11.24 - 인앱 결제 지원
    private String kidFlag	= "";
	
	public GetNSContStatRequestVO(){}
	
	public GetNSContStatRequestVO(String szParam){
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
		this.fxType = "N";
		
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
				
				if(key.toLowerCase().equals("sa_id"))		this.saId = value;
				if(key.toLowerCase().equals("stb_mac"))		this.stbMac = value;
				if(key.toLowerCase().equals("album_id"))	this.albumId = value;
				if(key.toLowerCase().equals("defin_flag"))	this.definFlag = value;
				if(key.toLowerCase().equals("youth_yn"))	this.youthYn = value;
				if(key.toLowerCase().equals("fx_type"))		this.fxType = value;
				if(key.toLowerCase().equals("cat_id"))		this.catId = value;
				if(key.toLowerCase().equals("app_type"))	this.appType = value;
				if(key.toLowerCase().equals("firm_flag"))	this.firmFlag = value;
				if(key.toLowerCase().equals("free_flag"))   this.freeFlag = value; //2018.07.26 권형도
				if(key.toLowerCase().equals("app_version"))		this.appVersion = value;
			}
		}
		
		//GetNSContStatController.saId = paramMap.get("sa_id");
		//GetNSContStatController.stbMac = paramMap.get("stb_mac");
		
		if( paramMap.get("sa_id") == null 
			|| paramMap.get("stb_mac") == null
			|| paramMap.get("album_id") == null 
			//|| paramMap.get("defin_flag") == null 
		)
		{
			throw new ImcsException();
		}
		
		if(paramMap.get("app_version") == null)
		{
			this.appVersion = "000";
		}
	
		if(!commonService.getValidParam(this.saId, 7, 12, 1))
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.stbMac, 14, 14, 1))
		{
			throw new ImcsException();
		}
		
		if(this.albumId.length() > 15 || this.albumId.length() < 15 ){
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.appVersion, 3, 3, 3))
		{
			throw new ImcsException();
		}
	
		// 2020.02.19 - Fortify 보안 툴에서 SQL에서 $를 사용하면 안된다고 하여 ${album_id}_D 를 데이터프리 변수 새로 만들어서 앨범ID_D로 넣어서 사용한다.
		this.albumIdD	= this.albumId + "_D";
		
		// 파라미터 null 체크 및 null일때 Default값 지정
		this.youthYn	= StringUtil.replaceNull(this.youthYn, "N");
		this.catId		= StringUtil.replaceNull(this.catId, "");
		this.fxType		= StringUtil.replaceNull(this.fxType, "X");
		this.firmFlag	= StringUtil.replaceNull(this.firmFlag, "N");
		this.freeFlag   = StringUtil.replaceNull(this.freeFlag, "N");
		
		/*switch (youthYn) {
		case "Y":
		case "N":
			break;

		default:
			throw new ImcsException();
		}*/
		
		//fx_type = "" 일 경우 아래와 같이 해주고 다시  ""를 넣어줌... 수정필요할듯
		/*if(this.getFxType().equals("")) {
			this.fxType		= "N";
			this.uflixYn1	= "N";
			this.uflixYn2	= "Y";
		}*/
		
		this.fxTypeTemp = "N";
		if(this.fxType.equals("H")) {
			this.uflixYn1	= "N";
			this.uflixYn2	= "Y";
		} else if(this.fxType.equals("M")) {
			/* 20161211 - 유플릭스 F/O + 유플릭스에서 비디오포털 컨텐츠도 노출하는 것이 맞다고 하여 수정 */
			this.fxType		= "H";
			this.fxTypeTemp = "Y";
			this.uflixYn1	= "N";
			this.uflixYn2	= "Y";
		} else if(this.fxType.equals("N")) {
			this.uflixYn1	= "N";
			this.uflixYn2	= "Y";
		} else {
			this.uflixYn1	= "N";
			this.uflixYn2	= "N";
		}
		
		// 2019.04.23 - JAVA의 경우 null or 빈값일 떄 데이터 조작을 하려고 하면 오류가 발생하기 때문에 디폴트 값을 넣어준다.
		if(this.appType == null || this.appType.equals(""))
		{
			this.appType = "RUSA";
		}
		
		if(this.appType != null && this.appType.equals(""))
			this.screenStr = this.appType.substring(0,1);
		
		if(this.screenStr.equals("X"))	this.screenStr = "UFLIX";
		else							this.screenStr = "NSC";
		
		if((this.saId.equals("") && this.stbMac.equals("")) || this.definFlag.equals("u"))
			throw new ImcsException();
		
		if( !"".equals(this.saId) ){
			this.cIdxSa = this.saId.substring(this.saId.length()-2, this.saId.length());
			try {
				this.pIdxSa = Integer.parseInt(this.cIdxSa);
			} catch (Exception e) {
				this.pIdxSa = 0;
			}
			
		}
		
		switch (appVersion) {
			case "000":
			case "001":
				break;
			default:
				throw new ImcsException();
		}
		
		if(this.appVersion.equals("001") && !this.appType.substring(0,1).equals("A"))
		{
			this.appVersion = "000";
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

	public String getDefinFlag() {
		return definFlag;
	}

	public void setDefinFlag(String definFlag) {
		this.definFlag = definFlag;
	}

	public String getYouthYn() {
		return youthYn;
	}

	public void setYouthYn(String youthYn) {
		this.youthYn = youthYn;
	}

	public String getFxType() {
		return fxType;
	}

	public void setFxType(String fxType) {
		this.fxType = fxType;
	}

	public String getCatId() {
		return catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
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

	public String getSvodProdId() {
		return svodProdId;
	}

	public void setSvodProdId(String svodProdId) {
		this.svodProdId = svodProdId;
	}

	public String getSvodOnly() {
		return svodOnly;
	}

	public void setSvodOnly(String svodOnly) {
		this.svodOnly = svodOnly;
	}

	public String getSvodYn() {
		return svodYn;
	}

	public void setSvodYn(String svodYn) {
		this.svodYn = svodYn;
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

	public String getFxTypeTemp() {
		return fxTypeTemp;
	}

	public void setFxTypeTemp(String fxTypeTemp) {
		this.fxTypeTemp = fxTypeTemp;
	}

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

	public String getFirmFlag() {
		return firmFlag;
	}

	public void setFirmFlag(String firmFlag) {
		this.firmFlag = firmFlag;
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

	public String getFreeFlag() {
		return freeFlag;
	}

	public void setFreeFlag(String freeFlag) {
		this.freeFlag = freeFlag;
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

	public String getLicensingEndYn() {
		return licensingEndYn;
	}

	public void setLicensingEndYn(String licensingEndYn) {
		this.licensingEndYn = licensingEndYn;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getKidFlag() {
		return kidFlag;
	}

	public void setKidFlag(String kidFlag) {
		this.kidFlag = kidFlag;
	}
	
}
