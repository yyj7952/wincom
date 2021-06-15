package kr.co.wincom.imcs.api.buyNSDMConts;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

public class BuyNSDMContsRequestVO extends NoSqlLoggingVO implements Serializable
{
	private static final long serialVersionUID = 820027358203692775L;

	/********************************************************************
	 * BuyNSDMConts API 전문 칼럼(순서 일치)
	********************************************************************/
    private String saId							= "";
    private String stbMac						= "";
    private String pkgYn						= "";
    private String albumId						= "";
    private String albumName					= "";
    private String catId						= "";
    private String suggestedPrice				= "";
    private String buyingPrice					= "";
    private String buyingType					= "";
    private String discountDivOrigin			= "";
    private String discountPriceOrigin			= "";
    private String offerBuyingType				= "";
    private String alwnceUnt					= "";
    private String ofrSeq						= "";
    private String offerCd						= "";
    private String offerNm						= "";
    private String membershipSeq				= "";
    private String offerType					= "";
    private String appType						= "";
    private String buyingGb						= "";
    private String buyTypeFlag					= "";
    private String datafreePrice				= "";
    private String datafreeBuyPrice				= "";
    private String datafreeDiscountDivOrigin	= "";
    private String datafreeDiscountPriceOrigin	= "";
    private String validPayKey					= "";
    private String realBuyingPrice				= "";
    private String realDatafreeBuyPrice			= "";
    
    private String[] discountDiv				= new String[ImcsConstants.DISCOUNT_CNT];
    private String[] discountPrice				= new String[ImcsConstants.DISCOUNT_CNT];
    private String[] datafreeDiscountDiv		= new String[ImcsConstants.DISCOUNT_CNT];
    private String[] datafreeDiscountPrice		= new String[ImcsConstants.DISCOUNT_CNT];
    
	
	/********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
    private Integer pIdxSa		= 0;
    
    private String pid			= "";
    private String resultCode	= "";
    private String cIdxSa		= "";
	
	private Integer resultSet	= 0;
	private Integer messageSet = 0;
	private Integer sqlCode		= 0;
	
	private	int discountCnt = 0;
	private	int datafreeDiscountCnt = 0;
	private	int udrBuyPrice = 0;
	private	int udrDfBuyPrice = 0;
	private int surtaxRate = 0;
	
	private String alwnceCharge		= "";
	private String buyingDate		= "";
	private String isLGU			= "";
	private String prodType			= "";		// 2019.10.29 - NPT_VO_BUY_META 에도 사용
	private String reservedPrice	= "";
	private String contsGenre		= "";
	private String eventType		= "";
	
    private String productId2		= "";
    private String contsId2			= "";
    private String contsName2		= "";
    private String contsGenre2		= "";
    
    private String disCpYn			= "";
    private String disMbYn			= "";
    private String disTvYn			= "";
    private String disKuYn			= "";
    
    private String disCpPrice		= "";
    private String disMbPrice		= "";
    private String disTvPrice		= "";
    private String disKuPrice		= "";
    
    private String disDfCpPrice		= "";
    private String disDfMbPrice		= "";
    private String disDfTvPrice		= "";
    private String disDfKuPrice		= "";
    
    private String isPayDatafree	= "";
    
    private String productId1		= "";
    private String productName1		= "";
    private String productPrice1	= "";
    private String expiredDate1		= "";
    
    private String suggestedDatafreePrice		= "";
    private String genreInfo		= "";
    private String cpevtId			= "";
    private String strmpId			= "";
    
    private String billSuggestedPrice		= "";
    private String resultBuyDate	= "";
    
    private String screenType;
    private String systemGb;
    // 2021.03.12 - 아이들나라4.0 phase2 : 아이들나라일 때 발급가능한 쿠폰 조회 및 저장시 변수값 변경 (MIMS요청 - 황재호 선임)
   	private String screenType_cpnCondPossible	= "";
   	private String systemGb_cpnCondPossible		= "";
   	
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
    
    // 2020.03.26 모바일 아이들 나라
    private String nscGb			= "X";		// 키즈 카테고리 여부
    private String categoryType		= "X";		// 캐릭터관,책읽어주는TV 여부
    
    private String ppsId			= "";
	
	public BuyNSDMContsRequestVO(){}
	
	public BuyNSDMContsRequestVO(String szParam){
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
				|| szTemp.indexOf("suggested_price") == -1 || szTemp.indexOf("buying_price") == -1
				|| szTemp.indexOf("buying_type") == -1 || szTemp.indexOf("discount_div") == -1
				|| szTemp.indexOf("discount_price") == -1 || szTemp.indexOf("offer_buying_type") == -1
				|| szTemp.indexOf("alwnce_unt") == -1 || szTemp.indexOf("ofr_seq") == -1
				|| szTemp.indexOf("offer_cd") == -1 || szTemp.indexOf("offer_nm") == -1
				|| szTemp.indexOf("membership_seq") == -1 || szTemp.indexOf("offer_type") == -1
				|| szTemp.indexOf("app_type") == -1 || szTemp.indexOf("buying_gb") == -1
				|| szTemp.indexOf("buy_type_flag") == -1 || szTemp.indexOf("datafree_price") == -1
				|| szTemp.indexOf("datafree_buy_price") == -1 || szTemp.indexOf("datafree_discount_div") == -1
				|| szTemp.indexOf("datafree_discount_price") == -1 || szTemp.indexOf("valid_pay_key") == -1
				|| szTemp.indexOf("real_buying_price") == -1 || szTemp.indexOf("real_datafree_buy_price") == -1 )
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
				if(key.toLowerCase().equals("pkg_yn"))			this.pkgYn = value;
				if(key.toLowerCase().equals("album_id"))		this.albumId = value;
				if(key.toLowerCase().equals("album_name"))		this.albumName = value;
				if(key.toLowerCase().equals("cat_id"))		this.catId = value;
				if(key.toLowerCase().equals("suggested_price"))		this.suggestedPrice = value;
				if(key.toLowerCase().equals("buying_price"))		this.buyingPrice = value;
				if(key.toLowerCase().equals("buying_type"))		this.buyingType = value;
				if(key.toLowerCase().equals("discount_div"))		this.discountDivOrigin = value;
				if(key.toLowerCase().equals("discount_price"))		this.discountPriceOrigin = value;
				if(key.toLowerCase().equals("offer_buying_type"))		this.offerBuyingType = value;
				if(key.toLowerCase().equals("alwnce_unt"))		this.alwnceUnt = value;
				if(key.toLowerCase().equals("ofr_seq"))		this.ofrSeq = value;
				if(key.toLowerCase().equals("offer_cd"))		this.offerCd = value;
				if(key.toLowerCase().equals("offer_nm"))		this.offerNm = value;
				if(key.toLowerCase().equals("membership_seq"))		this.membershipSeq = value;
				if(key.toLowerCase().equals("offer_type"))		this.offerType = value;
				if(key.toLowerCase().equals("app_type"))		this.appType = value;
				if(key.toLowerCase().equals("buying_gb"))		this.buyingGb = value;
				if(key.toLowerCase().equals("buy_type_flag"))		this.buyTypeFlag = value;
				if(key.toLowerCase().equals("datafree_price"))		this.datafreePrice = value;
				if(key.toLowerCase().equals("datafree_buy_price"))		this.datafreeBuyPrice = value;
				if(key.toLowerCase().equals("datafree_discount_div"))		this.datafreeDiscountDivOrigin = value;
				if(key.toLowerCase().equals("datafree_discount_price"))		this.datafreeDiscountPriceOrigin = value;
				if(key.toLowerCase().equals("valid_pay_key"))		this.validPayKey = value;
				if(key.toLowerCase().equals("real_buying_price"))		this.realBuyingPrice = value;
				if(key.toLowerCase().equals("real_datafree_buy_price"))		this.realDatafreeBuyPrice = value;
				if(key.toLowerCase().equals("pps_id"))		this.ppsId = value;
			}
		}
		
		//BuyNSDMContsController.saId = paramMap.get("sa_id");
		
		//BuyNSDMContsController.stbMac = paramMap.get("stb_mac");
		
		
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null
				|| paramMap.get("pkg_yn") == null || paramMap.get("album_id") == null 
				|| paramMap.get("album_name") == null || paramMap.get("cat_id") == null
				|| paramMap.get("suggested_price") == null || paramMap.get("buying_price") == null
				|| paramMap.get("buying_type") == null || paramMap.get("discount_div") == null
				|| paramMap.get("discount_price") == null || paramMap.get("offer_buying_type") == null
				|| paramMap.get("alwnce_unt") == null || paramMap.get("ofr_seq") == null
				|| paramMap.get("offer_cd") == null || paramMap.get("offer_nm") == null
				|| paramMap.get("membership_seq") == null || paramMap.get("offer_type") == null
				|| paramMap.get("app_type") == null || paramMap.get("buying_gb") == null
				|| paramMap.get("buy_type_flag") == null || paramMap.get("datafree_price") == null
				|| paramMap.get("datafree_buy_price") == null || paramMap.get("datafree_discount_div") == null
				|| paramMap.get("datafree_discount_price") == null || paramMap.get("valid_pay_key") == null
				|| paramMap.get("real_buying_price") == null || paramMap.get("real_datafree_buy_price") == null )
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
		
		if(!commonService.getValidParam(this.pkgYn, 1, 1, 2))
		{
			throw new ImcsException();
		}
		
		if(this.albumId.length() > 15 || this.albumId.length() < 15 ){
			throw new ImcsException();
		}

		if(!commonService.getValidParam(this.suggestedPrice, 1, 15, 3))
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.buyingPrice, 1, 15, 3))
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.buyingType, 1, 2, 2))
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.buyingGb, 1, 1, 2))
		{
			throw new ImcsException();
		}
		
		if( !this.albumId.matches(ImcsConstants.N_SP_PTN) ){
			//특수문자 있음
			throw new ImcsException();
		}

		if((this.saId.equals("") && this.stbMac.equals("")))
			throw new ImcsException();
		
		this.appType	= StringUtil.replaceNull(this.appType, "RUSA");
		
		if(!commonService.getValidParam(this.appType, 4, 4, 2))
		{
			throw new ImcsException();
		}
		
		if( !"".equals(this.saId) ){
			this.cIdxSa = this.saId.substring(this.saId.length()-2, this.saId.length());
			try {
				this.pIdxSa = Integer.parseInt(this.cIdxSa);
			} catch (Exception e) {
				this.pIdxSa = 0;
			}
			
		}
		
		if(this.discountDivOrigin.length() != ImcsConstants.DISCOUNT_CNT){
			//System.out.println("[ERROR] 컨텐츠 DIV  Validation Fail");
			throw new ImcsException();
		}else{
			this.discountDiv[0] = discountDivOrigin.substring(0, 1);
			this.discountDiv[1] = discountDivOrigin.substring(1, 2);
			this.discountDiv[2] = discountDivOrigin.substring(2, 3);
			this.discountDiv[3] = discountDivOrigin.substring(3, 4);
		}
		
		try {
			discountPrice = discountPriceOrigin.split(",");
			for(int i = 0 ; i < discountPrice.length ; i++)
			{
				if(!commonService.getValidParam(discountPrice[i], 1, 10, 3))
				{
					throw new ImcsException();
				}
			}
		} catch (Exception e) {
			throw new ImcsException();
		}
		
		if(this.ofrSeq.length() > 14){
			//System.out.println("[ERROR] OFR_SEQ length Validation Fail");
			throw new ImcsException();
		}

		if(this.offerCd.length() > 10){
			//System.out.println("[ERROR] OFR_CD length Validation Fail");
			throw new ImcsException();
		}
		
		if(this.datafreeDiscountDivOrigin.length() != ImcsConstants.DISCOUNT_CNT){
			//System.out.println("[ERROR] 데이터프리 DIV  Validation Fail");
			throw new ImcsException();
		}else{
			this.datafreeDiscountDiv[0] = datafreeDiscountDivOrigin.substring(0, 1);
			this.datafreeDiscountDiv[1] = datafreeDiscountDivOrigin.substring(1, 2);
			this.datafreeDiscountDiv[2] = datafreeDiscountDivOrigin.substring(2, 3);
			this.datafreeDiscountDiv[3] = datafreeDiscountDivOrigin.substring(3, 4);
		}
		
		try {
			datafreeDiscountPrice = datafreeDiscountPriceOrigin.split(",");
		} catch (Exception e) {
			throw new ImcsException();
		}
		
		if("".equals(this.suggestedPrice)) this.suggestedPrice = "0";
		if("".equals(this.buyingPrice)) this.buyingPrice = "0";
		if("".equals(this.datafreePrice)) this.datafreePrice = "0";
		if("".equals(this.datafreeBuyPrice)) this.datafreeBuyPrice = "0";
		if("".equals(this.realBuyingPrice)) this.realBuyingPrice = "0";
		if("".equals(this.realDatafreeBuyPrice)) this.realDatafreeBuyPrice = "0";
		
		if(!commonService.getValidParam(this.realBuyingPrice, 1, 15, 3))
		{
			throw new ImcsException();
		}
		
		switch(this.buyingType)
		{
			case ImcsConstants.CP_USE_YN_NORMAL:
			case ImcsConstants.CP_USE_YN_PREPAY:
			case ImcsConstants.CP_USE_YN_INAPP:
			case ImcsConstants.CP_USE_YN_PAYNOW:
			case ImcsConstants.CP_USE_YN_CREDITCARD:
			case ImcsConstants.CP_USE_YN_OCULUS:
			case ImcsConstants.CP_USE_YN_NAVER:
			case ImcsConstants.CP_USE_YN_KAKAO:
				break;
			default:
				throw new ImcsException();
		}
		
		switch(this.buyingGb)
		{
			case "N":
			case "R":
				break;
			default:
				throw new ImcsException();
		}
		
		//2018.12.20 - VR에서 API 호출 시 쿠폰 조회 및 쿠폰정보 등록시에 VR로 정보 조회 및 등록해야 한다.
		if(!(this.getAppType().equals("") || this.getAppType() == null) && this.appType.substring(0, 1).equals("E"))
		{
			this.screenType = "VR";
			this.systemGb = "4";
			this.screenType_cpnCondPossible = "VR";
			this.systemGb_cpnCondPossible = "4";
			if(this.appType.length() < 4){
				this.appType = "EUSA";			// 혹시라도 한자리만 들어오거나 했을 때, Exception 대비
			}
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
		
		
		if(this.ppsId == null || this.ppsId.equals(""))
		{
			this.ppsId = "N";
		}
		
		if( "Y".equals(this.pkgYn) ){ 
			if (!commonService.getValidParam(this.ppsId, 0, 5, 1)) {
				throw new ImcsException();
			}
		}
	}
	

	public static long getSerialversionuid() {
		return serialVersionUID;
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

	public int getResultSet() {
		return resultSet;
	}

	public void setResultSet(int resultSet) {
		this.resultSet = resultSet;
	}

	public Integer getpIdxSa() {
		return pIdxSa;
	}

	public void setpIdxSa(Integer pIdxSa) {
		this.pIdxSa = pIdxSa;
	}

	public String getcIdxSa() {
		return cIdxSa;
	}

	public void setcIdxSa(String cIdxSa) {
		this.cIdxSa = cIdxSa;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getAlbumId() {
		return albumId;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public Integer getMessageSet() {
		return messageSet;
	}

	public void setMessageSet(Integer messageSet) {
		this.messageSet = messageSet;
	}

	public String getPkgYn() {
		return pkgYn;
	}

	public void setPkgYn(String pkgYn) {
		this.pkgYn = pkgYn;
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

	public String getSuggestedPrice() {
		return suggestedPrice;
	}

	public void setSuggestedPrice(String suggestedPrice) {
		this.suggestedPrice = suggestedPrice;
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


	public String getOfferBuyingType() {
		return offerBuyingType;
	}

	public void setOfferBuyingType(String offerBuyingType) {
		this.offerBuyingType = offerBuyingType;
	}

	public String getAlwnceUnt() {
		return alwnceUnt;
	}

	public void setAlwnceUnt(String alwnceUnt) {
		this.alwnceUnt = alwnceUnt;
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

	public String getMembershipSeq() {
		return membershipSeq;
	}

	public void setMembershipSeq(String membershipSeq) {
		this.membershipSeq = membershipSeq;
	}

	public String getOfferType() {
		return offerType;
	}

	public void setOfferType(String offerType) {
		this.offerType = offerType;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
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

	public String getDatafreePrice() {
		return datafreePrice;
	}

	public void setDatafreePrice(String datafreePrice) {
		this.datafreePrice = datafreePrice;
	}

	public String getDatafreeBuyPrice() {
		return datafreeBuyPrice;
	}

	public void setDatafreeBuyPrice(String datafreeBuyPrice) {
		this.datafreeBuyPrice = datafreeBuyPrice;
	}



	public String getValidPayKey() {
		return validPayKey;
	}

	public void setValidPayKey(String validPayKey) {
		this.validPayKey = validPayKey;
	}

	public String getRealBuyingPrice() {
		return realBuyingPrice;
	}

	public void setRealBuyingPrice(String realBuyingPrice) {
		this.realBuyingPrice = realBuyingPrice;
	}

	public String getRealDatafreeBuyPrice() {
		return realDatafreeBuyPrice;
	}

	public void setRealDatafreeBuyPrice(String realDatafreeBuyPrice) {
		this.realDatafreeBuyPrice = realDatafreeBuyPrice;
	}

	public void setResultSet(Integer resultSet) {
		this.resultSet = resultSet;
	}

	public int getDiscountCnt() {
		return discountCnt;
	}

	public void setDiscountCnt(int discountCnt) {
		this.discountCnt = discountCnt;
	}

	public int getDatafreeDiscountCnt() {
		return datafreeDiscountCnt;
	}

	public void setDatafreeDiscountCnt(int datafreeDiscountCnt) {
		this.datafreeDiscountCnt = datafreeDiscountCnt;
	}

	public int getUdrBuyPrice() {
		return udrBuyPrice;
	}

	public void setUdrBuyPrice(int udrBuyPrice) {
		this.udrBuyPrice = udrBuyPrice;
	}

	public int getUdrDfBuyPrice() {
		return udrDfBuyPrice;
	}

	public void setUdrDfBuyPrice(int udrDfBuyPrice) {
		this.udrDfBuyPrice = udrDfBuyPrice;
	}

	public int getSurtaxRate() {
		return surtaxRate;
	}

	public void setSurtaxRate(int surtaxRate) {
		this.surtaxRate = surtaxRate;
	}

	public String getDiscountDivOrigin() {
		return discountDivOrigin;
	}

	public void setDiscountDivOrigin(String discountDivOrigin) {
		this.discountDivOrigin = discountDivOrigin;
	}

	public String getDiscountPriceOrigin() {
		return discountPriceOrigin;
	}

	public void setDiscountPriceOrigin(String discountPriceOrigin) {
		this.discountPriceOrigin = discountPriceOrigin;
	}

	public String getDatafreeDiscountDivOrigin() {
		return datafreeDiscountDivOrigin;
	}

	public void setDatafreeDiscountDivOrigin(String datafreeDiscountDivOrigin) {
		this.datafreeDiscountDivOrigin = datafreeDiscountDivOrigin;
	}

	public String getDatafreeDiscountPriceOrigin() {
		return datafreeDiscountPriceOrigin;
	}

	public void setDatafreeDiscountPriceOrigin(String datafreeDiscountPriceOrigin) {
		this.datafreeDiscountPriceOrigin = datafreeDiscountPriceOrigin;
	}

	public String[] getDiscountDiv() {
		return discountDiv;
	}

	public void setDiscountDiv(String[] discountDiv) {
		this.discountDiv = discountDiv;
	}

	public String[] getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(String[] discountPrice) {
		this.discountPrice = discountPrice;
	}

	public String[] getDatafreeDiscountDiv() {
		return datafreeDiscountDiv;
	}

	public void setDatafreeDiscountDiv(String[] datafreeDiscountDiv) {
		this.datafreeDiscountDiv = datafreeDiscountDiv;
	}

	public String[] getDatafreeDiscountPrice() {
		return datafreeDiscountPrice;
	}

	public void setDatafreeDiscountPrice(String[] datafreeDiscountPrice) {
		this.datafreeDiscountPrice = datafreeDiscountPrice;
	}

	public String getAlwnceCharge() {
		return alwnceCharge;
	}

	public void setAlwnceCharge(String alwnceCharge) {
		this.alwnceCharge = alwnceCharge;
	}

	public String getBuyingDate() {
		return buyingDate;
	}

	public void setBuyingDate(String buyingDate) {
		this.buyingDate = buyingDate;
	}

	public String getIsLGU() {
		return isLGU;
	}

	public void setIsLGU(String isLGU) {
		this.isLGU = isLGU;
	}

	public String getProdType() {
		return prodType;
	}

	public void setProdType(String prodType) {
		this.prodType = prodType;
	}

	public String getReservedPrice() {
		return reservedPrice;
	}

	public void setReservedPrice(String reservedPrice) {
		this.reservedPrice = reservedPrice;
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

	public String getDisCpYn() {
		return disCpYn;
	}

	public void setDisCpYn(String disCpYn) {
		this.disCpYn = disCpYn;
	}

	public String getDisMbYn() {
		return disMbYn;
	}

	public void setDisMbYn(String disMbYn) {
		this.disMbYn = disMbYn;
	}

	public String getDisTvYn() {
		return disTvYn;
	}

	public void setDisTvYn(String disTvYn) {
		this.disTvYn = disTvYn;
	}

	public String getDisKuYn() {
		return disKuYn;
	}

	public void setDisKuYn(String disKuYn) {
		this.disKuYn = disKuYn;
	}

	public String getDisCpPrice() {
		return disCpPrice;
	}

	public void setDisCpPrice(String disCpPrice) {
		this.disCpPrice = disCpPrice;
	}

	public String getDisMbPrice() {
		return disMbPrice;
	}

	public void setDisMbPrice(String disMbPrice) {
		this.disMbPrice = disMbPrice;
	}

	public String getDisTvPrice() {
		return disTvPrice;
	}

	public void setDisTvPrice(String disTvPrice) {
		this.disTvPrice = disTvPrice;
	}

	public String getDisKuPrice() {
		return disKuPrice;
	}

	public void setDisKuPrice(String disKuPrice) {
		this.disKuPrice = disKuPrice;
	}

	public String getIsPayDatafree() {
		return isPayDatafree;
	}

	public void setIsPayDatafree(String isPayDatafree) {
		this.isPayDatafree = isPayDatafree;
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

	public String getSuggestedDatafreePrice() {
		return suggestedDatafreePrice;
	}

	public void setSuggestedDatafreePrice(String suggestedDatafreePrice) {
		this.suggestedDatafreePrice = suggestedDatafreePrice;
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

	public String getDisDfCpPrice() {
		return disDfCpPrice;
	}

	public void setDisDfCpPrice(String disDfCpPrice) {
		this.disDfCpPrice = disDfCpPrice;
	}

	public String getDisDfMbPrice() {
		return disDfMbPrice;
	}

	public void setDisDfMbPrice(String disDfMbPrice) {
		this.disDfMbPrice = disDfMbPrice;
	}

	public String getDisDfTvPrice() {
		return disDfTvPrice;
	}

	public void setDisDfTvPrice(String disDfTvPrice) {
		this.disDfTvPrice = disDfTvPrice;
	}

	public String getDisDfKuPrice() {
		return disDfKuPrice;
	}

	public void setDisDfKuPrice(String disDfKuPrice) {
		this.disDfKuPrice = disDfKuPrice;
	}

	public String getBillSuggestedPrice() {
		return billSuggestedPrice;
	}

	public void setBillSuggestedPrice(String billSuggestedPrice) {
		this.billSuggestedPrice = billSuggestedPrice;
	}

	public String getResultBuyDate() {
		return resultBuyDate;
	}

	public void setResultBuyDate(String resultBuyDate) {
		this.resultBuyDate = resultBuyDate;
	}

	public Integer getSqlCode() {
		return sqlCode;
	}

	public void setSqlCode(Integer sqlCode) {
		this.sqlCode = sqlCode;
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

	public String getNscGb() {
		return nscGb;
	}

	public void setNscGb(String nscGb) {
		this.nscGb = nscGb;
	}

	public String getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
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

	public String getPpsId() {
		return ppsId;
	}

	public void setPpsId(String ppsId) {
		this.ppsId = ppsId;
	}
	
}
