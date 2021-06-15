package kr.co.wincom.imcs.api.authorizeNSView;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.common.vo.ComCpnVO;
import kr.co.wincom.imcs.handler.ImcsException;

@SuppressWarnings("serial")
public class AuthorizeNSViewRequestVO extends NoSqlLoggingVO implements Serializable {
	
	/********************************************************************
	 * AuthorizeNSView API 전문 칼럼(순서 일치)
	********************************************************************/
	private String saId			= "";	// 가입자정보
	private String stbMac		= "";	// 가입자 STB MAC Address
	private String albumId		= "";	// 앨범 ID
	private String reqType		= "";	// 재생경로확인
	private String chaNo		= "";	// 가이드채널 채널번호
	private String viewType		= "";	// 시청타입 (S:바로보기, D:다운로드하기)
	private String adaptiveType	= "";	// Adaptive Streaming Type (HLD, DS, SS)
	private String baseGb		= "";	// 기지국 코드 구분
	private String baseCd		= "";	// 기지국 코드 (W:WIFI, L:LTE, G:3G/4G)
	private String appType		= "";	// 어플타입(RABC)
	private String definFlag	= "";	// 사용자 화질 가능 FLAG (1:4M, 2:2M)
	private String fxType		= "";	// 유플릭스 타입 (M:MOBILE, P:PC_WEB, T:TVG_APP, H:HDTV, V:VOIP-070홈보이)
	private String catId		= "";	// 카테고리ID
	private String decPosYn		= "";	// 다국어자막복호화가능단말 여부
	
		
	/********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
//	private String assetId		= "";
	private String pid			= "";	// 프로세스ID - 로그용
	private String resultCode	= "";
	private String screenStr	= "";
	private String appStr		= "";
	private String screenType	= "";
	private String systemGb		= "";
	// 2021.03.12 - 아이들나라4.0 phase2 : 아이들나라일 때 발급가능한 쿠폰 조회 및 저장시 변수값 변경 (MIMS요청 - 황재호 선임)
	private String screenType_cpnCondPossible	= "";
	private String systemGb_cpnCondPossible		= "";
	
	private String bitRate		= "";
	private String uflixYn		= "";
	private String idxSa		= "";
	private String pIdxSa		= "";
	private String modIdxSa		= "";
	private String genreInfo	= "";	
	
//	private String prodId		= "";
	private String baseCondi	= "";
	private String baseOneCd	= ""; 
	private String nodeCd		= "";	// 지역노드
	private String cpnId		= "";	   
        
	//2018.12.06 VR앱 - APP_TYPE 첫번째가 E인 경우 VR앱 PT_VO_SET_TIME_PTT 테이블 NSCN_CUST_NO 테이블에 'V'로 저장 나머지는 'M'
    private String	nscnCustNo			= "";
    		
    private String	svcNode				= "";
    private String	productType			= "";
    private String	productIdSub		= "";
    private String	productId			= "";
    private String	uflixFlag			= "";
    private String	catWatermark		= "";
    private int		linkChk				= 0;
    private int		posVar				= 0;	
    
    private String	categoryId			= "";
    private String  categoryName		= "";
    private String  categoryIdSvod		= "";
    private String	categoryNameSvod	= "";

    private String fourdReplayYn		= "";
    private String nodeGroup			= "";
    private String cpevtId				= "";
    private String stmpId				= "";
    private ComCpnVO comCpnVO			= null;
    
    // 2019.10.11 - IPv6듀얼스택 제공 변수 추가
    private String	ipv6Flag;
    private String	prefixInternet;
    private String	prefixUplushdtv;
    
    // 2017.10.28 - 정산개선
  	private String  suggestedPrice		= "";			// NPT_VO_WATCH_META에 저장
  	private String  cpId 				= "";			// NPT_VO_WATCH_META에 저장
  	private String  contentFilesize  	= "";			// NPT_VO_WATCH_META에 저장
  	
  	// 2018.08.03 - 시청UDR 유/무료 전환
    private String  sysdateCurrent  	= "";
    private String  terrType  			= "";
    private String  sysdateYearago 		= "";
    private String  sysdate1yearago 	= "";
    private String  payYn 				= "";			// NPT_VO_WATCH_META에 저장
    private String  freeProdFlag 		= "";
    private String  ppmProdFlag 		= "";
    
    private String  genreSmall 			= "";
    private String  terrYn 				= "";			// NPT_VO_WATCH_META에 저장
    private String  previewPeriod 		= "";			// NPT_VO_WATCH_META에 저장
    private String  terrPeriod 			= "";			// NPT_VO_WATCH_META에 저장
    private String  terrEddate 			= "";
    private String  onairDate 			= "";			// NPT_VO_WATCH_META에 저장
    
    // 2019.10.31 - VOD 정산 프로세스 개선 : NPT_VO_WATCH_META 테이블 넣는 변수 추가
    private String assetName 			= "";
    private String ratingCd 			= "";    
    private String runTime 				= "";
    private String cpIdUflix 			= "";
    private String watchDate 			= "";			// 시청 시간 정보를 history 와 meta테이블을 동일하게 맞춰주기 위해 추가
    private String seriesNo 			= "";
    
    // 2020.03.26 - 모바일 아이들 나라 : PT_VO_CATEGORY테이블 ACTORS_DISPLAY컬럼 값을 받아서 PT_VO_WATCH_HISTORY_NSC 테이블 ASSET_ID 컬럼에 넣는 변수 추가 
    private String kidsGb				= "";
    
	
	public AuthorizeNSViewRequestVO(){}
	public AuthorizeNSViewRequestVO(String szParam){
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
				if(key.toLowerCase().equals("album_id"))		this.albumId = value;
				if(key.toLowerCase().equals("request_type"))	this.reqType = value;
				if(key.toLowerCase().equals("cha_no"))			this.chaNo = value;
				if(key.toLowerCase().equals("view_type"))		this.viewType = value;
				if(key.toLowerCase().equals("adaptive_type"))	this.adaptiveType = value;
				if(key.toLowerCase().equals("base_gb"))			this.baseGb = value;
				if(key.toLowerCase().equals("base_cd"))			this.baseCd = value;
				if(key.toLowerCase().equals("app_type"))		this.appType = value;
				if(key.toLowerCase().equals("defin_flag"))		this.definFlag = value;
				if(key.toLowerCase().equals("fx_type"))			this.fxType = value;
				if(key.toLowerCase().equals("cat_id"))			this.catId = value;
				if(key.toLowerCase().equals("dec_pos_yn"))		this.decPosYn = value;
				if(key.toLowerCase().equals("ipv6_flag"))		this.ipv6Flag = value;
				
			}
		}
		
		this.reqType	= StringUtil.replaceNull(this.reqType, "");
		this.chaNo		= StringUtil.replaceNull(this.chaNo, "");
		this.ipv6Flag	= StringUtil.replaceNull(this.ipv6Flag, "N");
		this.appType = StringUtil.replaceNull(this.appType, "RUSA");
		this.definFlag	= StringUtil.replaceNull(this.definFlag, "");
		
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
		
		if(!commonService.getValidParam(this.baseGb, 1, 1, 2))
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.baseCd, 0, 50, 1))
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.ipv6Flag, 1, 1, 2))
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.viewType, 1, 1, 2))
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.appType, 0, 4, 2))
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.catId, 0, 5, 1))
		{
			throw new ImcsException();
		}
		
		//
		// viewType, adaptiveType, baseGb는 C-Source 상에서 별다른 예외처리 코드가 없어 상기 코드상 그대로 값 설정 됨.
		//
		
		switch(this.baseCd.substring(0,  1)) {
			case "F": this.baseCd = "W" + this.baseCd.substring(1); break;
			case "V": this.baseCd = "L" + this.baseCd.substring(1); break;
			case "G": this.baseCd = "L" + this.baseCd.substring(1); break;
			default : break;
		}
		
		//20191001		
		if ("X".equals(this.appType.substring(0, 1))) {
			this.decPosYn = "N";
		}
		
		if(this.appType.length() == 4) {
			this.appStr		= this.appType.substring(3, 4);	//단말 확인을 위한 변수 선언
			this.screenStr	= this.appType.substring(0, 1); //스크린 타입 취득
		}
		
		if( "X".equals(this.screenStr) ) {
			
			this.screenType	= "UFLIX";
			this.systemGb	= "3";
			this.screenType_cpnCondPossible	= "UFLIX";
			this.systemGb_cpnCondPossible	= "3";
		} else if("E".equals(screenStr) ) {
			
			this.screenType	= "VR";
			this.systemGb	= "4";
			this.screenType_cpnCondPossible	= "VR";
			this.systemGb_cpnCondPossible	= "4";
		} else if("A".equals(screenStr) ) {
			
			this.screenType	= "NSC";
			this.systemGb	= "2";
			this.screenType_cpnCondPossible	= "MKID";
			this.systemGb_cpnCondPossible	= "6";
		}else {
			
			this.screenType	= "NSC";
			this.systemGb	= "2";
			this.screenType_cpnCondPossible	= "NSC";
			this.systemGb_cpnCondPossible	= "2";
		}
		
		//2018.12.06 VR앱 - APP_TYPE 첫번째가 E인 경우 VR앱 PT_VO_SET_TIME_PTT 테이블 NSCN_CUST_NO 테이블에 'V'로 저장 나머지는 'M'
		if("E".equals(appType))	{ this.nscnCustNo = "V"; }
		else					{ this.nscnCustNo = "M"; }		
		
		String fxType = paramMap.get("fx_type");
		if(null == fxType || fxType.isEmpty() ) { this.fxType = "N"; }
		else									{ this.fxType = fxType; }
		
		this.catId		= StringUtil.replaceNull(paramMap.get("cat_id"), "");
		
		switch(this.fxType ) {
			case "M": 
			case "H": this.uflixYn = "Y"; break;
			default	: 
				this.fxType = "N";
				switch(this.reqType.substring(0,  1)) {
					case "N": 
					//case "U":  this.uflixYn = "Y"; break;
					case "U":
					case "6": this.uflixYn = "Y"; break;
					default	: this.uflixYn = "N"; break;
				}
				break;
		}
		
		this.decPosYn = StringUtil.replaceNull(this.decPosYn, "N");
		this.svcNode = "N";
			
		//파티션분리로 추가 (2014.09.29)
		if(!this.saId.isEmpty() ) {
			
			int nPos = saId.length() - 2;
			this.idxSa = saId.substring(nPos, nPos + 2 );
			this.pIdxSa = new String(idxSa);
			
			try {
				this.modIdxSa = String.valueOf(Integer.parseInt(StringUtil.nullToZero(this.pIdxSa)) % 33);
			} catch (NumberFormatException e) {
				this.modIdxSa = "0";
			}
		}
		
		switch(this.baseGb)
		{
			case "Y":
			case "N":
				break;
			default:
				throw new ImcsException();
		}
		
		switch(this.viewType)
		{
			case "S":
			case "D":
				break;
			default:
				throw new ImcsException();
		}
		
		switch(this.ipv6Flag)
		{
			case "Y":
			case "N":
				break;
			default:
				throw new ImcsException();
		}

//		if(this.baseCd != null && this.baseCd.length() > 0)
//			this.baseOneCd = this.baseCd.substring(0, 1);
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

	public String getReqType() {
		return reqType;
	}

	public void setReqType(String reqType) {
		this.reqType = reqType;
	}

	public String getChaNo() {
		return chaNo;
	}

	public void setChaNo(String chaNo) {
		this.chaNo = chaNo;
	}

	public String getViewType() {
		return viewType;
	}

	public void setViewType(String viewType) {
		this.viewType = viewType;
	}

	public String getAdaptiveType() {
		return adaptiveType;
	}

	public void setAdaptiveType(String adaptiveType) {
		this.adaptiveType = adaptiveType;
	}

	public String getBaseGb() {
		return baseGb;
	}

	public void setBaseGb(String baseGb) {
		this.baseGb = baseGb;
	}

	public String getBaseCd() {
		return baseCd;
	}

	public void setBaseCd(String baseCd) {
		this.baseCd = baseCd;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public String getDefinFlag() {
		return definFlag;
	}

	public void setDefinFlag(String definFlag) {
		this.definFlag = definFlag;
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

	public String getDecPosYn() {
		return decPosYn;
	}

	public void setDecPosYn(String decPosYn) {
		this.decPosYn = decPosYn;
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

	public String getScreenStr() {
		return screenStr;
	}

	public void setScreenStr(String screenStr) {
		this.screenStr = screenStr;
	}

	public String getScreenType() {
		return screenType;
	}

	public void setScreenType(String screenType) {
		this.screenType = screenType;
	}

	public String getSystemGb() {
		return systemGb;
	}

	public void setSystemGb(String systemGb) {
		this.systemGb = systemGb;
	}

	public String getBitRate() {
		return bitRate;
	}

	public void setBitRate(String bitRate) {
		this.bitRate = bitRate;
	}

	public String getUflixYn() {
		return uflixYn;
	}

	public void setUflixYn(String uflixYn) {
		this.uflixYn = uflixYn;
	}

	public String getpIdxSa() {
		return pIdxSa;
	}

	public void setpIdxSa(String pIdxSa) {
		this.pIdxSa = pIdxSa;
	}

	public String getModIdxSa() {
		return modIdxSa;
	}

	public void setModIdxSa(String modIdxSa) {
		this.modIdxSa = modIdxSa;
	}

//	public String getProdId() {
//		return prodId;
//	}
//
//	public void setProdId(String prodId) {
//		this.prodId = prodId;
//	}

	public String getBaseCondi() {
		return baseCondi;
	}

	public void setBaseCondi(String baseCondi) {
		this.baseCondi = baseCondi;
	}

	public String getNodeCd() {
		return nodeCd;
	}

	public void setNodeCd(String nodeCd) {
		this.nodeCd = nodeCd;
	}

	public String getAppStr() {
		return appStr;
	}

	public void setAppStr(String appStr) {
		this.appStr = appStr;
	}

	public String getGenreInfo() {
		return genreInfo;
	}

	public void setGenreInfo(String genreInfo) {
		this.genreInfo = genreInfo;
	}

	public String getCpnId() {
		return cpnId;
	}

	public void setCpnId(String cpnId) {
		this.cpnId = cpnId;
	}

	public String getBaseOneCd() {
		return baseOneCd;
	}

	public void setBaseOneCd(String baseOneCd) {
		this.baseOneCd = baseOneCd;
	}

//	public String getAssetId() {
//		return assetId;
//	}
//
//	public void setAssetId(String assetId) {
//		this.assetId = assetId;
//	}

	public String getSuggestedPrice() {
		return suggestedPrice;
	}

	public void setSuggestedPrice(String suggestedPrice) {
		this.suggestedPrice = suggestedPrice;
	}

	public String getCpId() {
		return cpId;
	}

	public void setCpId(String cpId) {
		this.cpId = cpId;
	}

	public String getContentFilesize() {
		return contentFilesize;
	}

	public void setContentFilesize(String contentFilesize) {
		this.contentFilesize = contentFilesize;
	}

	public String getSysdateCurrent() {
		return sysdateCurrent;
	}

	public void setSysdateCurrent(String sysdateCurrent) {
		this.sysdateCurrent = sysdateCurrent;
	}

	public String getTerrType() {
		return terrType;
	}

	public void setTerrType(String terrType) {
		this.terrType = terrType;
	}

	public String getSysdateYearago() {
		return sysdateYearago;
	}

	public void setSysdateYearago(String sysdateYearago) {
		this.sysdateYearago = sysdateYearago;
	}

	public String getPayYn() {
		return payYn;
	}

	public void setPayYn(String payYn) {
		this.payYn = payYn;
	}

	public String getFreeProdFlag() {
		return freeProdFlag;
	}

	public void setFreeProdFlag(String freeProdFlag) {
		this.freeProdFlag = freeProdFlag;
	}

	public String getPpmProdFlag() {
		return ppmProdFlag;
	}

	public void setPpmProdFlag(String ppmProdFlag) {
		this.ppmProdFlag = ppmProdFlag;
	}

	public String getTerrYn() {
		return terrYn;
	}

	public void setTerrYn(String terrYn) {
		this.terrYn = terrYn;
	}

	public String getPreviewPeriod() {
		return previewPeriod;
	}

	public void setPreviewPeriod(String previewPeriod) {
		this.previewPeriod = previewPeriod;
	}

	public String getTerrPeriod() {
		return terrPeriod;
	}

	public void setTerrPeriod(String terrPeriod) {
		this.terrPeriod = terrPeriod;
	}

	public String getTerrEddate() {
		return terrEddate;
	}

	public void setTerrEddate(String terrEddate) {
		this.terrEddate = terrEddate;
	}

	public String getOnairDate() {
		return onairDate;
	}

	public void setOnairDate(String onairDate) {
		this.onairDate = onairDate;
	}

	public String getSysdate1yearago() {
		return sysdate1yearago;
	}

	public void setSysdate1yearago(String sysdate1yearago) {
		this.sysdate1yearago = sysdate1yearago;
	}
	
	public String getNscnCustNo()						{ return this.nscnCustNo; }
	public void setNscnCustNo(String nscnCustNo)		{ this.nscnCustNo = nscnCustNo; }  
	
	public String getSvcNode()							{ return this.svcNode; }
	public void setSvcNode(String svcNode)				{ this.svcNode = svcNode; }
	
	public String getIdxSa()							{ return this.idxSa; }
	public void setIdxSa(String idxSa)					{ this.idxSa = idxSa; }
	
	public String getProductType()						{ return this.productType; }
	public void setProductType(String productType)		{ this.productType = productType; }
	
	public String getProductId()						{ return this.productId; }
	public void setProductId(String productId)			{ this.productId = productId; }
	
	public String getProductIdSub()						{ return this.productIdSub; }
	public void setProductIdSub(String productIdSub)	{ this.productIdSub = productIdSub; }
	
	public String getUflixFlag()						{ return this.uflixFlag; }
	public void setUflixFlag(String uflixFlag)			{ this.uflixFlag = uflixFlag; }
	
	public String getCatWatermark()						{ return this.catWatermark; }
	public void setCatWatermark(String catWatermark)	{ this.catWatermark = catWatermark; }
	
	public int	getLinkChk()							{ return this.linkChk; }
	public void setLinkChk(int linkChk)					{ this.linkChk = linkChk; }

	public int	getPosVar()								{ return this.posVar; }
	public void setPosVar(int posVar)					{ this.posVar = posVar; }
	
	public String getCategoryId() 						{ return categoryId; }
	public void setCategoryId(String categoryId) 		{ this.categoryId = categoryId; }
	
	public String getCategoryName() 					{ return categoryName; }
	public void setCategoryName(String categoryName) 	{ this.categoryName = categoryName; }
	
	public String getCategoryIdSvod() 					{ return categoryIdSvod; }
	public void setCategoryIdSvod(String categoryIdSvod){ this.categoryIdSvod = categoryIdSvod; }
	
	public String getCategoryNameSvod() 						{ return categoryNameSvod; }
	public void setCategoryNameSvod(String categoryNameSvod) 	{ this.categoryNameSvod = categoryNameSvod; }
	
	public String getFourdReplayYn() 							{ return fourdReplayYn; }
	public void setFourdReplayYn(String fourdReplayYn) 			{ this.fourdReplayYn = fourdReplayYn; }
	
	public String getNodeGroup() 								{ return nodeGroup; }
	public void setNodeGroup(String nodeGroup) 					{ this.nodeGroup = nodeGroup; }
	
	public String getCpevtId() 									{ return cpevtId; }
	public void setCpevtId(String cpevtId) 						{ this.cpevtId = cpevtId; }
	
	public String getStmpId() 									{ return stmpId; }
	public void setStmpId(String stmpId) 						{ this.stmpId = stmpId; }
	
	public ComCpnVO getComCpnVO() 								{ return comCpnVO; }
	public void setComCpnVO(ComCpnVO comCpnVO) 					{ this.comCpnVO = comCpnVO; }
		
	public String getGenreSmall() 								{ return genreSmall; }
	public void setGenreSmall(String genreSmall) 				{ this.genreSmall = genreSmall; }
	
	
	
	public String getIpv6Flag() {
		return ipv6Flag;
	}
	public void setIpv6Flag(String ipv6Flag) {
		this.ipv6Flag = ipv6Flag;
	}
	public String getPrefixInternet() {
		return prefixInternet;
	}
	public void setPrefixInternet(String prefixInternet) {
		this.prefixInternet = prefixInternet;
	}
	public String getPrefixUplushdtv() {
		return prefixUplushdtv;
	}
	public void setPrefixUplushdtv(String prefixUplushdtv) {
		this.prefixUplushdtv = prefixUplushdtv;
	}
	public String getAssetName() {
		return assetName;
	}
	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}
	public String getRatingCd() {
		return ratingCd;
	}
	public void setRatingCd(String ratingCd) {
		this.ratingCd = ratingCd;
	}
	public String getRunTime() {
		return runTime;
	}
	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}
	public String getCpIdUflix() {
		return cpIdUflix;
	}
	public void setCpIdUflix(String cpIdUflix) {
		this.cpIdUflix = cpIdUflix;
	}
	public String getWatchDate() {
		return watchDate;
	}
	public void setWatchDate(String watchDate) {
		this.watchDate = watchDate;
	}
	public String getSeriesNo() {
		return seriesNo;
	}
	public void setSeriesNo(String seriesNo) {
		this.seriesNo = seriesNo;
	}
	public String getKidsGb() {
		return kidsGb;
	}
	public void setKidsGb(String kidsGb) {
		this.kidsGb = kidsGb;
	}
	public String getScreenType_cpnCondPossible() {
		return screenType_cpnCondPossible;
	}
	public void setScreenType_cpnCondPossible(String screenType_cpnCondPossible) {
		this.screenType_cpnCondPossible = screenType_cpnCondPossible;
	}
	public String getSystemGb_cpnCondPossible() {
		return systemGb_cpnCondPossible;
	}
	public void setSystemGb_cpnCondPossible(String systemGb_cpnCondPossible) {
		this.systemGb_cpnCondPossible = systemGb_cpnCondPossible;
	}	
}
