package kr.co.wincom.imcs.api.authorizeNView;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;


@SuppressWarnings("serial")
public class AuthorizeNViewRequestVO  extends NoSqlLoggingVO implements Serializable {
	
	/******************************************************************** 
	 * AuthorizeNSView API 전문 칼럼(순서 일치)
	********************************************************************/
    private String saId			= "";		// 가입자 정보
    private String stbMac		= "";		// 가입자 MAC ADDRESS
    private String albumId		= "";		// 앨범ID
    private String buyingDate	= "";		// 구매일자
    private String reqType		= "";		// 재생경로
    private String chaNo		= "";		// 가이드채널 채널번호
    private String viewType		= "";		// 시청타입 (S:바로보기, D:다운로드)
    private String adapType		= "";		// Adaptive Streaming Type (HLS, DS, SS)
    private String baseGb		= "";		// 기지국 코드 구분
    private String baseCd		= "";		// 기지국 코드
    private String applType		= "";		// 어플타입 (RABC)
    private String definFlag	= "";		// 사용자 화질가능 FLAG
    private String fxType		= "";		// 유플릭스 타입
    private String catId		= "";		// 카테고리 ID			// NPT_VO_WATCH_META에 저장
    private String datafreeUseYn	= "";	// 데이터프리여부
    private String sameCtn		= "";		// 가번CTN/단말CTN 일치 여부
    private String decPosYn		= "";		// 다국어자막 복호화 가능 단말 여부
    private String svcNode 		= "";		// 서비스 노드 구분 (N or NULL : DEFAULT 노드 정보 제공, R : 프로야구&해외로밍 노드 정보 제공
    private String nWatchYn		= "";		// nScreen(STB) 권한으로 시청 여부
    private String nSaId		= "";		// nScreen(STB) 가입자 번호
    private String nStbMac		= "";		// nScreen(STB) 가입자 맥주소
    private String nBuyDate		= "";		// nScreen(STB) 구매 날짜
    
    
    /********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
    private String pid			= "";
    private String resultCode	= "";
    
    private String productId	= "";
    private String adiProductId = "";
    private String bitRate		= "";
    private String nodeCd		= "";
    private String baseOneCd	= "";
    private String uflixYn		= "";
    private String pIdxSa		= "";
    private String modIdxSa		= "";
    
    private String contsType	= "";
    private String baseCondi	= "";
    private String eventType	= "";
    private String screenType	= "";
    private String systemGb		= "";
    // 2021.03.12 - 아이들나라4.0 phase2 : 아이들나라일 때 발급가능한 쿠폰 조회 및 저장시 변수값 변경 (MIMS요청 - 황재호 선임)
 	private String screenType_cpnCondPossible	= "";
 	private String systemGb_cpnCondPossible		= "";
 	
    private String presentYn	= "N";
    private String appStr		= "";
    private String screenStr	= "";
    private String refundYn		= "";
    private String genreInfo	= "";
    private String prodType		= "";		// 쿠폰 정보 조회를 위한 contsType의 한자리
	private String reservePrice	= "";
    private String expiredDate	= "";
    private String expiredDateUp	= "";
	private String cpnId		= "";
	
	private String datafreePrice	= "";
	private String datafreeBuyYn	= "";
	private String datafreeWatchYn	= "";
    
    private Integer resultSet = 0;    
    private long tp_start = 0;
    private String shareSaId	= "";		// saId와 nSaId를 하나로 사용하기 위한 변수
    private String udrTransYn	= "";
    private String assetId		= "";
    
//    private String assetIdAll	= "";
    
    //2018.07.26 권형도
    private String freeFlag        = "";
    private String suggestedPrice  = "";			// NPT_VO_WATCH_META에 저장
    private String cpId            = "";			// NPT_VO_WATCH_META에 저장
    private String contentFileSize = "";			// NPT_VO_WATCH_META에 저장
    private String linkTime        = "";
    private String linkChk         = "";
    
    //2018.12.06 - VR앱 (VR앱일 경우 PT_VO_SET_TIME_PTT 테이블 NSCN_CUST_NO 컬럼에 'V' 아닌경우 'M')
    private String nscnCustNo      = "";
    
    //2019.02.16 - 4D Replay 정보 조회
    private String replay4dYn      = "";
    private String replay4dContValue   = "";
    
    //2018.08.03 - 시청UDR 유/무료 전환
    private String  nscreenGenreSmall;
    private String  sysdateCurrent;
    private String  terrType;
    private String  sysdateYearago;
    private String  sysdate1yearago;
    private String  payYn;					// NPT_VO_WATCH_META에 저장
    private String  ppvProdFlag;
    private String  terrYn;					// NPT_VO_WATCH_META에 저장
    private String  previewPeriod;			// NPT_VO_WATCH_META에 저장
    private String  terrPeriod;				// NPT_VO_WATCH_META에 저장
    private String  terrEddate;
    private String  onairDate;				// NPT_VO_WATCH_META에 저장
    
    //2018.07.26 - 비디오포털 UX 개편 (오프닝 건너뛰기)
    private String  openingEndTime;
    private String  endingBgnTime;    
    
    // 2019.02.28 - M9화질일 때 , VR_TYPE, MUSIC_CONT_TYPE값을 보고 CDN 노드를 찾는다.
 	// 공연 : O , VR : O   -->   5G노드
 	// 공연 : X , VR : O   -->   AWS(아마존) 노드
 	// 그 외에는 일반 노드
 	// 2019.04.02 - MB,MC,MD 화질일 때 , VR_TYPE, MUSIC_CONT_TYPE값을 보고 CDN 노드를 찾는다.
 	// 공연 : O , VR : O   -->   5G노드
 	// 공연 : X , VR : O   -->   AWS(아마존) 노드
 	// 공연 : O , VR : X   -->   일반
 	// 공연 : X , VR : X   -->   5G
	private String	musicYn	= "";
	private String	vrYn	= "";
    
    private String  imgUrl;
    private String  imgFileName;
    private String  imgFileName6s;
    private String  timeInfo;
    private String  imgType;
    private String	tmpNodeGroup;
    
    // 2019.10.11 - IPv6듀얼스택 제공 변수 추가
    private String	ipv6Flag;
    private String	prefixInternet;
    private String	prefixUplushdtv;
        
    private String	nodeGroup	 = "";
    
    // 2019.11.01 - VOD 정산 프로세스 개선 : NPT_VO_WATCH_META 테이블 넣는 변수 추가
    private String assetName 			= "";
    private String ratingCd 			= "";    
    private String runTime 				= "";
    private String cpIdUflix 			= "";
    private String watchDate 			= "";			// 시청 시간 정보를 history 와 meta테이블을 동일하게 맞춰주기 위해 추가
    private String seriesNo 			= "";
    
    // 2020.03.03 -seamless
    private String nscreenYn			= "";
    private String fvodFlag				= "";
    
    // 2020.03.26 - 모바일 아이들 나라 : PT_VO_CATEGORY테이블 ACTORS_DISPLAY컬럼 값을 받아서 PT_VO_WATCH_HISTORY_NSC 테이블 ASSET_ID 컬럼에 넣는 변수 추가 
    private String kidsGb				= "";
    
    public AuthorizeNViewRequestVO(){}
    
    public AuthorizeNViewRequestVO(String szParam){
    	CommonService commonService = new CommonService();
    	
    	String[] arrStat = szParam.split("\\|");
		
		String key		= "";
		String value	= "";
		int    nStr		= 0;
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		
		/*String szTemp = szParam.toLowerCase();
		
		if( szTemp.indexOf("sa_id") == -1 || szTemp.indexOf("stb_mac") == -1
				|| szTemp.indexOf("album_id") == -1 || szTemp.indexOf("buying_date") == -1 )
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
				if(key.toLowerCase().equals("buying_date"))		this.buyingDate = value;
				if(key.toLowerCase().equals("request_type"))		this.reqType = value;
				if(key.toLowerCase().equals("cha_no"))			this.chaNo = value;
				if(key.toLowerCase().equals("view_type"))		this.viewType = value;
				if(key.toLowerCase().equals("adaptive_type"))	this.adapType = value;
				if(key.toLowerCase().equals("base_gb"))			this.baseGb = value;
				if(key.toLowerCase().equals("base_cd"))			this.baseCd = value;
				if(key.toLowerCase().equals("app_type"))		this.applType = value;
				if(key.toLowerCase().equals("defin_flag"))		this.definFlag = value;
				if(key.toLowerCase().equals("fx_type"))			this.fxType = value;
				if(key.toLowerCase().equals("cat_id"))			this.catId = value;
				if(key.toLowerCase().equals("datafree_use_yn"))		this.datafreeUseYn = value;
				if(key.toLowerCase().equals("same_ctn"))		this.sameCtn = value;
				if(key.toLowerCase().equals("dec_pos_yn"))		this.decPosYn = value;
				if(key.toLowerCase().equals("svc_node"))		this.svcNode = value;
				if(key.toLowerCase().equals("n_watch_yn"))		this.nWatchYn = value;
				if(key.toLowerCase().equals("n_sa_id"))			this.nSaId = value;
				if(key.toLowerCase().equals("n_stb_mac"))		this.nStbMac = value;
				if(key.toLowerCase().equals("n_buy_date"))		this.nBuyDate = value;
				if(key.toLowerCase().equals("free_flag"))		this.freeFlag = value;
				if(key.toLowerCase().equals("ipv6_flag"))		this.ipv6Flag = value;
				
			}
		}
			
		//AuthorizeNViewController.saId = paramMap.get("sa_id");
		
		//AuthorizeNViewController.stbMac = paramMap.get("stb_mac");
		
		this.fxType	= StringUtil.replaceNull(this.fxType, "N");
		this.datafreeUseYn	= StringUtil.replaceNull(this.datafreeUseYn, "N");
		this.sameCtn	= StringUtil.replaceNull(this.sameCtn, "N");
		this.decPosYn	= StringUtil.replaceNull(this.decPosYn, "N");
		this.nWatchYn	= StringUtil.replaceNull(this.nWatchYn, "N");
		this.applType	= StringUtil.replaceNull(this.applType, "RUSA");
		this.ipv6Flag	= StringUtil.replaceNull(this.ipv6Flag, "N");
		this.baseGb		= StringUtil.replaceNull(this.baseGb, "N");
		
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
		
		if(this.applType.length() == 4) {
			this.appStr		= this.applType.substring(3, 4);
			this.screenStr	= this.applType.substring(0, 1);
		}
		
		switch (this.viewType) {
		case "S":
			break;
		case "D":
			break;
		default:
			throw new ImcsException();
		}
		
		switch (this.baseGb) {
			case "N":
			case "Y":
			case "W":	// 연동규격서에는 없지만 2020.03월에 호출이 1건 있어, 혹시 몰라 추가함. 
				break;
			default:
				throw new ImcsException();
		}
		
		
		if( "Y".equals(this.baseGb) ) {
			switch(this.baseCd.substring(0, 1))
			{
				case "W":
				case "L":
					break;
				case "G":
					this.baseCd = "L" + this.baseCd.substring(1);
					break;
				case "F":
					this.baseCd = "W" + this.baseCd.substring(1);
					break;
				case "V":
					this.baseCd = "L" + this.baseCd.substring(1);
					break;
				// 2020.11.11 - 운영팀 테스트용으로 T BASE_CD 허용 (황영환 책임님 요청)
				case "T":
					break;
				default:
					throw new ImcsException();
			}
		}
		
		if("X".equals(this.screenStr)) {
			this.screenType	= "UFLIX";
			this.systemGb	= "3";
			this.screenType_cpnCondPossible = "UFLIX";
			this.systemGb_cpnCondPossible	= "3";
		} else if("E".equals(this.screenStr)) {
			this.screenType	= "VR";
			this.systemGb	= "4";
			this.screenType_cpnCondPossible = "VR";
			this.systemGb_cpnCondPossible	= "4";
		} else if("A".equals(this.screenStr)) {
			this.screenType	= "NSC";
			this.systemGb	= "2";
			this.screenType_cpnCondPossible = "MKID";
			this.systemGb_cpnCondPossible	= "6";
		} else {
			this.screenType	= "NSC";
			this.systemGb	= "2";
			this.screenType_cpnCondPossible = "NSC";
			this.systemGb_cpnCondPossible	= "2";
		}
		
		if("E".equals(this.applType.substring(0, 1))) {
			this.setNscnCustNo("V");
		} else {
			this.setNscnCustNo("M");
		}
		
		if("M".equals(this.fxType)) {
			this.uflixYn = "Y";
		} else if ("H".equals(this.fxType)) {
			this.uflixYn = "Y";
		} else {
			this.uflixYn = "N";
		}				
				
		if( "1".equals(this.definFlag) )	this.bitRate = "M1";
		if( "2".equals(this.definFlag) )	this.bitRate = "M2";
		if( "".equals(this.definFlag) )		this.bitRate = "M2";
		
		
		if( "M".equals(this.fxType) )		this.uflixYn = "Y";
		else if( "H".equals(this.fxType) )	this.uflixYn = "Y";
		else								this.uflixYn = "N";
		
		this.presentYn	= "N";
		
		// datafreeUseYn - Y, N 체크
		switch (this.datafreeUseYn) {
		case "Y":
		case "N":	
			break;
		default:
			throw new ImcsException();
		}
		
		// decPosYn - Y, N 체크
		switch (this.decPosYn) {
		case "Y":
		case "N":	
			break;
		default:
			throw new ImcsException();
		}
		
		// 엔스크린 일때 체크
		switch (this.nWatchYn) {
		case "Y":
			if("".equals(this.nSaId) || "".equals(this.nStbMac) || "".equals(this.nBuyDate)) {
				throw new ImcsException();
			} else {
				this.shareSaId = this.nSaId;
			}
			break;
		case "N":
			this.shareSaId = this.saId;
			break;
		default:
			throw new ImcsException();
		}
		
		// 파티션분리로 추가 (2014.09.29) - CDN 서버 분산하기 위함.
		if( !"".equals(this.shareSaId) ) {
			this.pIdxSa = this.shareSaId.substring(this.shareSaId.length() - 2, this.shareSaId.length());
			try {
				this.modIdxSa = String.valueOf(Integer.parseInt(StringUtil.nullToZero(this.pIdxSa)) % 33);
			} catch (Exception e) {
				this.modIdxSa = "0";
			}
		}
		
		if(this.baseCd != null && this.baseCd.length() > 0)
			this.baseOneCd	= this.baseCd.substring(0, 1);
		
		this.svcNode = StringUtil.replaceNull(this.svcNode, "N");
		
		switch (this.svcNode) {
		case "N":
			break;
		case "R":			
			break;
		case "U":			
			break;
		default:
			throw new ImcsException();
		}
		
		/* TV 앱 - TV 앱 사용자인지 구분 c_free_flag가 Y면 TV앱 사용자 N or NULL or 파라미터 없는 경우 일반 사용자	*/
		this.freeFlag = StringUtil.replaceNull(this.freeFlag, "N");
		if (!this.freeFlag.equals("Y") && !this.freeFlag.equals("N")) {
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
		
		this.udrTransYn = "Y";
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

	public String getBuyingDate() {
		return buyingDate;
	}

	public void setBuyingDate(String buyingDate) {
		this.buyingDate = buyingDate;
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

	public String getAdapType() {
		return adapType;
	}

	public void setAdapType(String adapType) {
		this.adapType = adapType;
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

	public String getApplType() {
		return applType;
	}

	public void setApplType(String appType) {
		this.applType = appType;
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

	public String getDatafreeUseYn() {
		return datafreeUseYn;
	}

	public void setDatafreeUseYn(String datafreeUseYn) {
		this.datafreeUseYn = datafreeUseYn;
	}

	public String getSameCtn() {
		return sameCtn;
	}

	public void setSameCtn(String sameCtn) {
		this.sameCtn = sameCtn;
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

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getAdiProductId() {
		return adiProductId;
	}

	public void setAdiProductId(String adiProductId) {
		this.adiProductId = adiProductId;
	}

	public String getBitRate() {
		return bitRate;
	}

	public void setBitRate(String bitRate) {
		this.bitRate = bitRate;
	}

	public String getNodeCd() {
		return nodeCd;
	}

	public void setNodeCd(String nodeCd) {
		this.nodeCd = nodeCd;
	}

	public String getBaseOneCd() {
		return baseOneCd;
	}

	public void setBaseOneCd(String baseOneCd) {
		this.baseOneCd = baseOneCd;
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

	public String getContsType() {
		return contsType;
	}

	public void setContsType(String contsType) {
		this.contsType = contsType;
	}

	public String getBaseCondi() {
		return baseCondi;
	}

	public void setBaseCondi(String baseCondi) {
		this.baseCondi = baseCondi;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
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

	public String getPresentYn() {
		return presentYn;
	}

	public void setPresentYn(String presentYn) {
		this.presentYn = presentYn;
	}

	public Integer getResultSet() {
		return resultSet;
	}

	public void setResultSet(Integer resultSet) {
		this.resultSet = resultSet;
	}

	public long getTp_start() {
		return tp_start;
	}

	public void setTp_start(long tp_start) {
		this.tp_start = tp_start;
	}

	public String getAppStr() {
		return appStr;
	}

	public void setAppStr(String appStr) {
		this.appStr = appStr;
	}

	public String getRefundYn() {
		return refundYn;
	}

	public void setRefundYn(String refundYn) {
		this.refundYn = refundYn;
	}

	public String getScreenStr() {
		return screenStr;
	}

	public void setScreenStr(String screenStr) {
		this.screenStr = screenStr;
	}

	public String getGenreInfo() {
		return genreInfo;
	}

	public void setGenreInfo(String genreInfo) {
		this.genreInfo = genreInfo;
	}

	public String getProdType() {
		return prodType;
	}

	public void setProdType(String prodType) {
		this.prodType = prodType;
	}
    public String getReservePrice() {
		return reservePrice;
	}

	public void setReservePrice(String reservePrice) {
		this.reservePrice = reservePrice;
	}

	public String getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(String expiredDate) {
		this.expiredDate = expiredDate;
	}

	public String getExpiredDateUp() {
		return expiredDateUp;
	}

	public void setExpiredDateUp(String expiredDateUp) {
		this.expiredDateUp = expiredDateUp;
	}

	public String getCpnId() {
		return cpnId;
	}

	public void setCpnId(String cpnId) {
		this.cpnId = cpnId;
	}

	public String getDatafreePrice() {
		return datafreePrice;
	}

	public void setDatafreePrice(String datafreePrice) {
		this.datafreePrice = datafreePrice;
	}

	public String getDatafreeBuyYn() {
		return datafreeBuyYn;
	}

	public void setDatafreeBuyYn(String datafreeBuyYn) {
		this.datafreeBuyYn = datafreeBuyYn;
	}

	public String getDatafreeWatchYn() {
		return datafreeWatchYn;
	}

	public void setDatafreeWatchYn(String datafreeWatchYn) {
		this.datafreeWatchYn = datafreeWatchYn;
	}

	public String getSvcNode() {
		return svcNode;
	}

	public void setSvcNode(String svcNode) {
		this.svcNode = svcNode;
	}

	public String getnWatchYn() {
		return nWatchYn;
	}

	public void setnWatchYn(String nWatchYn) {
		this.nWatchYn = nWatchYn;
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

	public String getnBuyDate() {
		return nBuyDate;
	}

	public void setnBuyDate(String nBuyDate) {
		this.nBuyDate = nBuyDate;
	}

	public String getShareSaId() {
		return shareSaId;
	}

	public void setShareSaId(String shareSaId) {
		this.shareSaId = shareSaId;
	}

	public String getUdrTransYn() {
		return udrTransYn;
	}

	public void setUdrTransYn(String udrTransYn) {
		this.udrTransYn = udrTransYn;
	}

	public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}

	public String getFreeFlag() {
		return freeFlag;
	}

	public void setFreeFlag(String freeFlag) {
		this.freeFlag = freeFlag;
	}

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

	public String getContentFileSize() {
		return contentFileSize;
	}

	public void setContentFileSize(String contentFileSize) {
		this.contentFileSize = contentFileSize;
	}

	public String getLinkTime() {
		return linkTime;
	}

	public void setLinkTime(String linkTime) {
		this.linkTime = linkTime;
	}

	public String getNscreenGenreSmall() {
		return nscreenGenreSmall;
	}

	public void setNscreenGenreSmall(String nscreenGenreSmall) {
		this.nscreenGenreSmall = nscreenGenreSmall;
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

	public String getSysdate1yearago() {
		return sysdate1yearago;
	}

	public void setSysdate1yearago(String sysdate1yearago) {
		this.sysdate1yearago = sysdate1yearago;
	}

	public String getPayYn() {
		return payYn;
	}

	public void setPayYn(String payYn) {
		this.payYn = payYn;
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

	public String getSysdateCurrent() {
		return sysdateCurrent;
	}

	public void setSysdateCurrent(String sysdateCurrent) {
		this.sysdateCurrent = sysdateCurrent;
	}

	public String getPpvProdFlag() {
		return ppvProdFlag;
	}

	public void setPpvProdFlag(String ppvProdFlag) {
		this.ppvProdFlag = ppvProdFlag;
	}

	public String getOpeningEndTime() {
		return openingEndTime;
	}

	public void setOpeningEndTime(String openingEndTime) {
		this.openingEndTime = openingEndTime;
	}

	public String getEndingBgnTime() {
		return endingBgnTime;
	}

	public void setEndingBgnTime(String endingBgnTime) {
		this.endingBgnTime = endingBgnTime;
	}

	public String getLinkChk() {
		return linkChk;
	}

	public void setLinkChk(String linkChk) {
		this.linkChk = linkChk;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getImgFileName() {
		return imgFileName;
	}

	public void setImgFileName(String imgFileName) {
		this.imgFileName = imgFileName;
	}

	public String getImgFileName6s() {
		return imgFileName6s;
	}

	public void setImgFileName6s(String imgFileName6s) {
		this.imgFileName6s = imgFileName6s;
	}

	public String getTimeInfo() {
		return timeInfo;
	}

	public void setTimeInfo(String timeInfo) {
		this.timeInfo = timeInfo;
	}

	public String getImgType() {
		return imgType;
	}

	public void setImgType(String imgType) {
		this.imgType = imgType;
	}

	public String getNscnCustNo() {
		return nscnCustNo;
	}

	public void setNscnCustNo(String nscnCustNo) {
		this.nscnCustNo = nscnCustNo;
	}

	public String getReplay4dYn() {
		return replay4dYn;
	}

	public void setReplay4dYn(String replay4dYn) {
		this.replay4dYn = replay4dYn;
	}

	public String getReplay4dContValue() {
		return replay4dContValue;
	}

	public void setReplay4dContValue(String replay4dContValue) {
		this.replay4dContValue = replay4dContValue;
	}

	public String getMusicYn() {
		return musicYn;
	}

	public void setMusicYn(String musicYn) {
		this.musicYn = musicYn;
	}

	public String getVrYn() {
		return vrYn;
	}

	public void setVrYn(String vrYn) {
		this.vrYn = vrYn;
	}

	public String getTmpNodeGroup() {
		return tmpNodeGroup;
	}

	public void setTmpNodeGroup(String tmpNodeGroup) {
		this.tmpNodeGroup = tmpNodeGroup;
	}

	public String getNodeGroup() {
		return nodeGroup;
	}

	public void setNodeGroup(String nodeGroup) {
		this.nodeGroup = nodeGroup;
	}

//	public String getAssetIdAll() {
//		return assetIdAll;
//	}
//
//	public void setAssetIdAll(String assetIdAll) {
//		this.assetIdAll = assetIdAll;
//	}

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

	public String getIpv6Flag() {
		return ipv6Flag;
	}

	public void setIpv6Flag(String ipv6Flag) {
		this.ipv6Flag = ipv6Flag;
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

	public String getNscreenYn() {
		return nscreenYn;
	}

	public void setNscreenYn(String nscreenYn) {
		this.nscreenYn = nscreenYn;
	}

	public String getFvodFlag() {
		return fvodFlag;
	}

	public void setFvodFlag(String fvodFlag) {
		this.fvodFlag = fvodFlag;
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
