package kr.co.wincom.imcs.api.buyNSConts;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

@SuppressWarnings("serial")
public class BuyNSContsRequestVO  extends NoSqlLoggingVO implements Serializable {
	
	/********************************************************************
	 * BuyNSConts API 전문 칼럼(순서 일치)
	********************************************************************/
    private String saId				= "";
    private String stbMac			= "";
    private String pkgYn			= "";
    private String albumId			= "";
    private String albumName		= "";
    private String catId			= "";
    private String buyingPrice		= "";
    private String buyingType		= "";
    private String ofrSeq			= "";
    private String offerCd			= "";
    private String offerNm			= "";
    private String alwnceUnt		= "";
    private String alwnceAmnt		= "";
    private String applStrtYmd		= "";
    private String applEndYmd		= "";
    private String appType			= "";
    private String pointAmt			= "";
    private String paymentId		= "";
    private String buyingGb			= "";
    private String buyTypeFlag		= "";
    private String datafreeBuyPrice	= "";
    private String datafreeId		= "";		// datafreePaymentId
    private String ppvDatafreeBuyPrice	= "";
    private String ppvDatafreeId	= "";		//ppvDatafreePaymentId
    
    
    /********************************************************************
   	 * 추가 사용 파라미터
   	********************************************************************/
    private String pid				= "";
    private String resultCode		= "";
    
    private Integer resultSet		= 0;
    private Integer messageSet		= 0;
    private Integer pIdxSa			= 0;
    
    private String cpUseYn			= ""; 
    private String balace			= "";
    private String alwnceCharge		= "";
    private String cIdxSa			= "";
    private String approvalPrice	= "";
    private String prodType			= "";		// 2019.10.29 - NPT_VO_BUY_META 에도 사용
    private String buyDate			= "";
    private String buyingDate		= "";
    private String contsGenre		= "";
    private String eventType		= "";
    private String productId1		= "";
    private String productName1		= "";
    private String productPrice1	= "";
    private String expiredDate1		= "";
    private String productId2		= "";
    private String contsId2			= "";
    private String contsName2		= "";
    private String contsGenre2		= "";
    private String genreInfo		= "";
    private String cpevtId			= "";
    private String strmpId			= "";
    private String suggestedPrice	= "";
    
    private String datafreeCpUseYn = "";
    private String isLGU = "";
    private String isPayDatafree = "";
    private String suggestedDatafreePrice = "";
    private String suggestedDatafreeApprovalPrice = "";
    private String reservedPrice = "";
    private String possessionYn = "";
    private String screenType;
    private String systemGb;
    // 2021.03.12 - 아이들나라4.0 phase2 : 아이들나라일 때 발급가능한 쿠폰 조회 및 저장시 변수값 변경 (MIMS요청 - 황재호 선임)
  	private String screenType_cpnCondPossible	= "";
  	private String systemGb_cpnCondPossible		= "";
  	
    private int	   szSurtaxRate = 10;
    private int	   udrCharge = 0; 
    
    // 2019.10.29 - VOD 정산 프로세스 개선 : NPT_VO_BUY_META 테이블 넣는 변수 추가
    private String assetName 		= "";		// PPV 콘텐츠 정보 세팅, PPS 구매시에는 구매하는 콘텐츠의 콘텐츠명 사용
    private String hdcontent 		= "";		// PPV 콘텐츠 정보 세팅, PPS 구매시에는 구매하는 콘텐츠의 HDCONTENT값 사용
    private String ratingCd 		= "";		// PPV 콘텐츠 정보 세팅, PPS 구매시에는 구매하는 콘텐츠의 연령정보 사용
    private String productId 		= "";		// PPV 콘텐츠 정보 세팅, PPS 구매시에는 별도로 저장하고 있던 패키지 상품ID 사용
    private String productName 		= "";		// PPV 콘텐츠 정보 세팅, PPS 구매시에는 별도로 저장하고 있던 패키지 상품명 사용
    private String productKind 		= "";		// PPV or PPS 상품 타입
    private String cpId 			= "";		// PPV 콘텐츠 정보 세팅, PPS 구매시에는 pps_cp_id의 우선순위에 따른 정보 사용
    private String maximumViewingLength 		= "";	// PPV 콘텐츠 정보 세팅, PPS 구매시에는 별도로 저장하고 있던 패키지 유효일자 사용
    private String seriesNo 		= "";		// PPV 콘텐츠 정보 세팅, PPS 구매시에는 해당변수에는 NULL(빈값)로 데이터를 넣는다.
    
	//2019.09.04 영화월정액 내재화 라이센스 만료된 컨텐츠 구매 불가 로직 구현을 위하여 추가
    private String licensingStart = "";
    private String licensingEnd   = "";
    private String licensingValidYn = "";
    

	private long tp_start = 0;
    
    private Integer sqlCode	= 0;
    
    public BuyNSContsRequestVO(){}
    
    public BuyNSContsRequestVO(String szParam){
    	CommonService commonService = new CommonService();
    	
    	String[] arrStat = szParam.split("\\|");
		
		String key		= "";
		String value	= "";
		int    nStr		= 0;
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		
		/*String szTemp = szParam.toLowerCase();
		
		if( szTemp.indexOf("sa_id") == -1 || szTemp.indexOf("stb_mac") == -1
				|| szTemp.indexOf("pkg_yn") == -1 || szTemp.indexOf("album_id") == -1 
				|| szTemp.indexOf("album_name") == -1 || szTemp.indexOf("cat_id") == -1
				|| szTemp.indexOf("buying_price") == -1 || szTemp.indexOf("buying_type") == -1 )
		{
			throw new ImcsException();
		}*/
		
		for(int i = 0; i < arrStat.length; i++){
			nStr	= arrStat[i].indexOf("=");
			
			if(nStr > 0) {
				key = arrStat[i].substring(0, nStr).toLowerCase().trim();
				value = arrStat[i].substring(nStr + 1, arrStat[i].length()).trim();
				
				paramMap.put(key, value);
				
				/*if( !value.matches(ImcsConstants.N_SP_PTN) && key.toLowerCase().indexOf("name") == -1 && key.toLowerCase().indexOf("nm") == -1 ){
					//특수문자 있음
					throw new ImcsException();
				}*/
				
				if(key.toLowerCase().equals("sa_id"))			this.saId = value;
				if(key.toLowerCase().equals("stb_mac"))			this.stbMac = value;
				if(key.toLowerCase().equals("pkg_yn"))			this.pkgYn = value;
				if(key.toLowerCase().equals("album_id"))		this.albumId = value;
				if(key.toLowerCase().equals("album_name"))		this.albumName = value;
				if(key.toLowerCase().equals("cat_id"))			this.catId = value;
				if(key.toLowerCase().equals("buying_price"))	this.buyingPrice = value;
				if(key.toLowerCase().equals("buying_type"))		this.buyingType = value;
				if(key.toLowerCase().equals("ofr_seq"))			this.ofrSeq = value;
				if(key.toLowerCase().equals("offer_cd"))		this.offerCd = value;
				if(key.toLowerCase().equals("offer_nm"))		this.offerNm = value;
				if(key.toLowerCase().equals("alwnce_unt"))		this.alwnceUnt = value;
				if(key.toLowerCase().equals("alwnce_amnt"))		this.alwnceAmnt = value;
				if(key.toLowerCase().equals("appl_strt_ymd"))	this.applStrtYmd = value;
				if(key.toLowerCase().equals("appl_end_ymd"))	this.applEndYmd = value;
				if(key.toLowerCase().equals("app_type"))		this.appType = value;
				if(key.toLowerCase().equals("point_amt"))		this.pointAmt = value;
				if(key.toLowerCase().equals("payment_id"))		this.paymentId = value;
				if(key.toLowerCase().equals("buying_gb"))		this.buyingGb = value;
				if(key.toLowerCase().equals("buy_type_flag"))	this.buyTypeFlag = value;
				if(key.toLowerCase().equals("datafree_buy_price"))	this.datafreeBuyPrice = value;
				if(key.toLowerCase().equals("datafree_payment_id"))	this.datafreeId = value;
				if(key.toLowerCase().equals("ppv_datafree_buy_price"))	this.ppvDatafreeBuyPrice = value;
				if(key.toLowerCase().equals("ppv_datafree_payment_id"))	this.ppvDatafreeId = value;
				if(key.toLowerCase().equals("licensing_valid_yn"))	this.licensingValidYn = value;
			}
		}	
		
		//BuyNSContsController.saId = paramMap.get("sa_id");
		
		//BuyNSContsController.stbMac = paramMap.get("stb_mac");
		
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null
				|| paramMap.get("pkg_yn") == null || paramMap.get("album_id") == null 
				|| paramMap.get("album_name") == null || paramMap.get("cat_id") == null
				|| paramMap.get("buying_price") == null || paramMap.get("buying_type") == null
				|| paramMap.get("app_type") == null )
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
		
		this.pkgYn = StringUtil.replaceNull(this.pkgYn, "N");
		switch (this.pkgYn) {
		case "Y":
		case "N":
			break;
		default:
			throw new ImcsException();
		}
		
		if(this.albumId.length() > 15 || this.albumId.length() < 15 ){
			throw new ImcsException();
		}
		
		if(this.buyingPrice == null || this.buyingPrice.equals(""))
		{
			throw new ImcsException();
		}
		
		if(this.buyingType == null || this.buyingType.equals(""))
		{
			this.buyingType = "B";
		}
		
		if(this.appType == null || this.appType.equals(""))
		{
			this.appType = "RUSA";
		}

		
		this.pointAmt	= StringUtil.replaceNull(this.pointAmt, "0");
		this.paymentId	= StringUtil.nullToSpace(this.paymentId);
		this.buyingGb	= StringUtil.replaceNull(this.buyingGb, "N");
		this.buyTypeFlag	= StringUtil.replaceNull(this.buyTypeFlag, "1");
		this.datafreeBuyPrice	= StringUtil.replaceNull(this.datafreeBuyPrice, "0");
		this.datafreeId	= StringUtil.nullToSpace(this.datafreeId);
		this.ppvDatafreeBuyPrice	= StringUtil.replaceNull(this.ppvDatafreeBuyPrice, "0.00");
		this.ppvDatafreeId	= StringUtil.nullToSpace(this.ppvDatafreeId);
		
		//2018.12.20 - VR에서 API 호출 시 쿠폰 조회 및 쿠폰정보 등록시에 VR로 정보 조회 및 등록해야 한다.
		if(!(this.getAppType().equals("") || this.getAppType() == null) && this.appType.substring(0, 1).equals("E"))
		{
			this.screenType = "VR";
			this.systemGb = "4";
			this.screenType_cpnCondPossible = "VR";
			this.systemGb_cpnCondPossible = "4";
		}
		else if(!(this.getAppType().equals("") || this.getAppType() == null) && this.appType.substring(0, 1).equals("A"))
		{
			this.screenType = "NSC";
			this.systemGb = "2";
			this.screenType_cpnCondPossible = "MKID";
			this.systemGb_cpnCondPossible = "6";
		}
		else
		{
			this.screenType = "NSC";
			this.systemGb = "2";
			this.screenType_cpnCondPossible = "NSC";
			this.systemGb_cpnCondPossible = "2";
		}

		
		/*switch (buyingGb) {
		case "N":
		case "R":
			break;

		default:
			throw new ImcsException();
		}
				
		switch (buyTypeFlag) {
		case "1":
		case "2":
		case "3":
			break;

		default:
			throw new ImcsException();
		}*/
		
		/*if( "B".equals(this.buyingType.toUpperCase()))		this.cpUseYn = "N";		// 일반구매
		else if("C".equals(this.buyingType.toUpperCase()))	this.cpUseYn = "Y";		// 쿠폰구매 
		else if("S".equals(this.buyingType.toUpperCase())){			// PG구매
			this.cpUseYn = "S";
			this.alwnceCharge = buyingPrice;
		} else if("A".equals(this.buyingType.toUpperCase())){		// 인앱구매
			this.cpUseYn = "A";
			this.balace = buyingPrice;
		} else if("W".equals(this.buyingType.toUpperCase())){		// PAY NOW 구매
			this.cpUseYn = "W";
			this.alwnceCharge = this.buyingPrice;
		}else{
			this.cpUseYn = this.buyingType;
		}*/
		
		if( !"".equals(this.saId) ){
			this.cIdxSa = this.saId.substring(this.saId.length()-2, this.saId.length());
			try {
				this.pIdxSa = Integer.parseInt(this.cIdxSa);
			} catch (Exception e) {
				this.pIdxSa = 0;
			}
			
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

	public String getPkgYn() {
		return pkgYn;
	}

	public void setPkgYn(String pkgYn) {
		this.pkgYn = pkgYn;
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

	public String getCatId() {
		return catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
	}

	public String getBuyingPrice() {
		return buyingPrice;
	}

	public void setBuyingPrice(String buyingPrice) {
		this.buyingPrice = buyingPrice;
	}

	public String getBuyingType() {
		return buyingType;
	}

	public void setBuyingType(String buyingType) {
		this.buyingType = buyingType;
	}

	public String getOfrSeq() {
		return ofrSeq;
	}

	public void setOfrSeq(String ofrSeq) {
		this.ofrSeq = ofrSeq;
	}

	public String getOfferCd() {
		return offerCd;
	}

	public void setOfferCd(String offerCd) {
		this.offerCd = offerCd;
	}

	public String getOfferNm() {
		return offerNm;
	}

	public void setOfferNm(String offerNm) {
		this.offerNm = offerNm;
	}

	public String getAlwnceUnt() {
		return alwnceUnt;
	}

	public void setAlwnceUnt(String alwnceUnt) {
		this.alwnceUnt = alwnceUnt;
	}

	public String getAlwnceAmnt() {
		return alwnceAmnt;
	}

	public void setAlwnceAmnt(String alwnceAmnt) {
		this.alwnceAmnt = alwnceAmnt;
	}

	public String getApplStrtYmd() {
		return applStrtYmd;
	}

	public void setApplStrtYmd(String applStrtYmd) {
		this.applStrtYmd = applStrtYmd;
	}

	public String getApplEndYmd() {
		return applEndYmd;
	}

	public void setApplEndYmd(String applEndYmd) {
		this.applEndYmd = applEndYmd;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public String getPointAmt() {
		return pointAmt;
	}

	public void setPointAmt(String pointAmt) {
		this.pointAmt = pointAmt;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public String getBuyingGb() {
		return buyingGb;
	}

	public void setBuyingGb(String buyingGb) {
		this.buyingGb = buyingGb;
	}

	public String getBuyTypeFlag() {
		return buyTypeFlag;
	}

	public void setBuyTypeFlag(String buyTypeFlag) {
		this.buyTypeFlag = buyTypeFlag;
	}

	public String getDatafreeBuyPrice() {
		return datafreeBuyPrice;
	}

	public void setDatafreeBuyPrice(String datafreeBuyPrice) {
		this.datafreeBuyPrice = datafreeBuyPrice;
	}

	public String getDatafreeId() {
		return datafreeId;
	}

	public void setDatafreeId(String datafreeId) {
		this.datafreeId = datafreeId;
	}

	public String getPpvDatafreeBuyPrice() {
		return ppvDatafreeBuyPrice;
	}

	public void setPpvDatafreeBuyPrice(String ppvDatafreeBuyPrice) {
		this.ppvDatafreeBuyPrice = ppvDatafreeBuyPrice;
	}

	public String getPpvDatafreeId() {
		return ppvDatafreeId;
	}

	public void setPpvDatafreeId(String ppvDatafreeId) {
		this.ppvDatafreeId = ppvDatafreeId;
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

	public Integer getResultSet() {
		return resultSet;
	}

	public void setResultSet(Integer resultSet) {
		this.resultSet = resultSet;
	}

	public Integer getMessageSet() {
		return messageSet;
	}

	public void setMessageSet(Integer messageSet) {
		this.messageSet = messageSet;
	}

	public Integer getpIdxSa() {
		return pIdxSa;
	}

	public void setpIdxSa(Integer pIdxSa) {
		this.pIdxSa = pIdxSa;
	}

	public String getCpUseYn() {
		return cpUseYn;
	}

	public void setCpUseYn(String cpUseYn) {
		this.cpUseYn = cpUseYn;
	}

	public String getBalace() {
		return balace;
	}

	public void setBalace(String balace) {
		this.balace = balace;
	}

	public String getAlwnceCharge() {
		return alwnceCharge;
	}

	public void setAlwnceCharge(String alwnceCharge) {
		this.alwnceCharge = alwnceCharge;
	}

	public String getcIdxSa() {
		return cIdxSa;
	}

	public void setcIdxSa(String cIdxSa) {
		this.cIdxSa = cIdxSa;
	}

	public long getTp_start() {
		return tp_start;
	}

	public void setTp_start(long tp_start) {
		this.tp_start = tp_start;
	}

	public String getApprovalPrice() {
		return approvalPrice;
	}

	public void setApprovalPrice(String approvalPrice) {
		this.approvalPrice = approvalPrice;
	}

	public String getProdType() {
		return prodType;
	}

	public void setProdType(String prodType) {
		this.prodType = prodType;
	}

	public String getBuyDate() {
		return buyDate;
	}

	public void setBuyDate(String buyDate) {
		this.buyDate = buyDate;
	}

	public String getContsGenre() {
		return contsGenre;
	}

	public void setContsGenre(String contsGenre) {
		this.contsGenre = contsGenre;
	}

	public String getProductId2() {
		return productId2;
	}

	public void setProductId2(String productId2) {
		this.productId2 = productId2;
	}

	public String getContsId2() {
		return contsId2;
	}

	public void setContsId2(String contsId2) {
		this.contsId2 = contsId2;
	}

	public String getContsName2() {
		return contsName2;
	}

	public void setContsName2(String contsName2) {
		this.contsName2 = contsName2;
	}

	public String getContsGenre2() {
		return contsGenre2;
	}

	public void setContsGenre2(String contsGenre2) {
		this.contsGenre2 = contsGenre2;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getGenreInfo() {
		return genreInfo;
	}

	public void setGenreInfo(String genreInfo) {
		this.genreInfo = genreInfo;
	}

	public String getCpevtId() {
		return cpevtId;
	}

	public void setCpevtId(String cpevtId) {
		this.cpevtId = cpevtId;
	}

	public String getStrmpId() {
		return strmpId;
	}

	public void setStrmpId(String strmpId) {
		this.strmpId = strmpId;
	}

	public String getSuggestedPrice() {
		return suggestedPrice;
	}

	public void setSuggestedPrice(String suggestedPrice) {
		this.suggestedPrice = suggestedPrice;
	}

	public String getBuyingDate() {
		return buyingDate;
	}

	public void setBuyingDate(String buyingDate) {
		this.buyingDate = buyingDate;
	}

	public String getProductId1() {
		return productId1;
	}

	public void setProductId1(String productId1) {
		this.productId1 = productId1;
	}

	public String getProductName1() {
		return productName1;
	}

	public void setProductName1(String productName1) {
		this.productName1 = productName1;
	}

	public String getProductPrice1() {
		return productPrice1;
	}

	public void setProductPrice1(String productPrice1) {
		this.productPrice1 = productPrice1;
	}

	public String getExpiredDate1() {
		return expiredDate1;
	}

	public void setExpiredDate1(String expiredDate1) {
		this.expiredDate1 = expiredDate1;
	}

	public String getDatafreeCpUseYn() {
		return datafreeCpUseYn;
	}

	public void setDatafreeCpUseYn(String datafreeCpUseYn) {
		this.datafreeCpUseYn = datafreeCpUseYn;
	}

	public String getIsLGU() {
		return isLGU;
	}

	public void setIsLGU(String isLGU) {
		this.isLGU = isLGU;
	}

	public String getIsPayDatafree() {
		return isPayDatafree;
	}

	public void setIsPayDatafree(String isPayDatafree) {
		this.isPayDatafree = isPayDatafree;
	}

	public String getSuggestedDatafreePrice() {
		return suggestedDatafreePrice;
	}

	public void setSuggestedDatafreePrice(String suggestedDatafreePrice) {
		this.suggestedDatafreePrice = suggestedDatafreePrice;
	}

	public String getSuggestedDatafreeApprovalPrice() {
		return suggestedDatafreeApprovalPrice;
	}

	public void setSuggestedDatafreeApprovalPrice(
			String suggestedDatafreeApprovalPrice) {
		this.suggestedDatafreeApprovalPrice = suggestedDatafreeApprovalPrice;
	}

	public String getReservedPrice() {
		return reservedPrice;
	}

	public void setReservedPrice(String reservedPrice) {
		this.reservedPrice = reservedPrice;
	}

	public Integer getSqlCode() {
		return sqlCode;
	}

	public void setSqlCode(Integer sqlCode) {
		this.sqlCode = sqlCode;
	}

	public String getPossessionYn() {
		return possessionYn;
	}

	public void setPossessionYn(String possessionYn) {
		this.possessionYn = possessionYn;
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
	
	public int getSzSurtaxRate() {
		return szSurtaxRate;
	}

	public void setSzSurtaxRate(int szSurtaxRate) {
		this.szSurtaxRate = szSurtaxRate;
	}

	public int getUdrCharge() {
		return udrCharge;
	}

	public void setUdrCharge(int udrCharge) {
		this.udrCharge = udrCharge;
	}

	public String getAssetName() {
		return assetName;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	public String getHdcontent() {
		return hdcontent;
	}

	public void setHdcontent(String hdcontent) {
		this.hdcontent = hdcontent;
	}

	public String getRatingCd() {
		return ratingCd;
	}

	public void setRatingCd(String ratingCd) {
		this.ratingCd = ratingCd;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductKind() {
		return productKind;
	}

	public void setProductKind(String productKind) {
		this.productKind = productKind;
	}

	public String getCpId() {
		return cpId;
	}

	public void setCpId(String cpId) {
		this.cpId = cpId;
	}

	public String getMaximumViewingLength() {
		return maximumViewingLength;
	}

	public void setMaximumViewingLength(String maximumViewingLength) {
		this.maximumViewingLength = maximumViewingLength;
	}

	public String getSeriesNo() {
		return seriesNo;
	}

	public void setSeriesNo(String seriesNo) {
		this.seriesNo = seriesNo;
	}	

	public String getLicensingStart() {
		return licensingStart;
	}

	public void setLicensingStart(String licensingStart) {
		this.licensingStart = licensingStart;
	}

	public String getLicensingEnd() {
		return licensingEnd;
	}

	public void setLicensingEnd(String licensingEnd) {
		this.licensingEnd = licensingEnd;
	}

	public String getLicensingValidYn() {
		return licensingValidYn;
	}

	public void setLicensingValidYn(String licensingValidYn) {
		this.licensingValidYn = licensingValidYn;
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