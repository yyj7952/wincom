package kr.co.wincom.imcs.api.buyContsCp;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

@SuppressWarnings("serial")
public class BuyContsCpRequestVO  extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * BuyNSContsCp API 전문 칼럼(순서 일치)
	********************************************************************/
	private String saId			= "";
	private String stbMac 		= "";
	private String pkgYn		= "";
    private String albumId		= "";
    private String albumName	= "";
    private String catId		= "";
    private String buyingPrice	= "";
    private String buyingType	= "";
    private String ofrSeq		= "";
    private String offerCd		= "";
    private String offerNm		= "";
    private String appType		= "";
    private String alwnceCharge	= "";
    private String balace		= "";
    private String alwnceUnt	= "";
    
    private String offerType	= "";
    private String buyTypeFlag	= "";
    private String datafreeBuyPrice		= "";
    private String datafreeAlwnceCharge	= "";
    private String datafreeBalace		= "";
    
    private String suggestedDatafreeBuyPrice		= "";
      
    /********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
    private String pid			= "";
    private String resultCode	= "";
    private String buyingDate	= "";
    private String cIdxSa		= "";
    private String pIdxDay		= "";
    private String cpUseYn		= "";
    private String productType	= "";		// 2019.10.29 - NPT_VO_BUY_META 에도 사용
    private String buyDate		= "";
    private String contsGenre	= "";
    private String eventType	= "";
    
    private Integer pIdxSa		= 0;
    private Integer resultSet	= 0;
    private Integer messageSet	= 0;
    private Integer sqlCode		= 0;
    
    private String productId2	= "";
    private String contsId2		= "";
    private String contsName2	= "";
    private String contsGenre2	= "";
    
    private String productId1	= "";
	private String productName1	= "";
    private String productPrice1	= "";
    private String expiredDate1	= "";
    
    private String datafreeCpUseYn = "";
    private String isLGU = "";
    
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
   
/*    private String belongingName = "";
    
    		VALUES (#{saId}, #{stbMac}, #{buyingDate}, #{albumId}, REPLACE(#{albumName}, '', ' '), #{pkgYn}, TRIM(#{catId}), 
		    NVL(#{buyingPrice}, '0'), #{cpUseYn}, 
		    TO_CHAR(TO_DATE(SUBSTR(#{buyingDate}, 1, 12), 'yyyymmddhh24mi') + TO_NUMBER(#{eventType})/24, 'yyyymmddhh24mi')||SUBSTR(#{buyingDate}, 13, 2), #{applType}, 
		    <if test='productType == "0"'> #{pIdxSa} , #{pIdxDay} </if> 
		    <if test='productType != "0"'> #{alwnceCharge} , #{balace} , #{offerCd} , #{ofrSeq} , #{alwnceUnt} </if>)
    
    private String suggestedPrice = "";
    
    private String distributor = "";
    
    private String buyingGb = "";
    private String expiredDate = "";
    private String statusFlag = "";    
    private String ynVodOpen = "";    
    private Long cpCnt;
    private String billType = "";
    private String categoryId = "";
    
    private String alwnceAmnt = "";
    private String applStrtYmd = "";
    private String applEndYmd = "";
    private String adiProductId = "";
    private String eventValue = "";
    private String eventPrice = "";
    private String pointAmt = "";
    private String getCouponYn = "";
    private String getStampYn = "";
    
    */
    
    
    private long tp_start = 0;
    
    public BuyContsCpRequestVO(){}
    
    public BuyContsCpRequestVO(String szParam){
    	
    	String[] arrStat = szParam.split("\\|");
		
		String key		= "";
		String value	= "";
		int    nStr		= 0;
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		
		/*String szTemp = szParam.toLowerCase();
		
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null
				|| paramMap.get("pkg_yn") == null || paramMap.get("album_id") == null 
				|| paramMap.get("album_name") == null || paramMap.get("cat_id") == null
				|| paramMap.get("buying_price") == null || paramMap.get("buying_type") == null )
		{
			throw new ImcsException();
		}*/
		
		for(int i = 0; i < arrStat.length; i++){
			nStr	= arrStat[i].indexOf("=");
			
			if(nStr > 0) {
				key = arrStat[i].substring(0, nStr).toLowerCase().trim();
				value = arrStat[i].substring(nStr + 1, arrStat[i].length()).trim();
				
				paramMap.put(key, value);
				
				/*if( !value.matches(ImcsConstants.N_SP_PTN) && key.toLowerCase().indexOf("name") == null && key.toLowerCase().indexOf("nm") == null ){
					//특수문자 있음
					throw new ImcsException();
				}*/
				
				if(key.toLowerCase().equals("sa_id"))					this.saId = value;
				if(key.toLowerCase().equals("stb_mac"))					this.stbMac = value;
				if(key.toLowerCase().equals("pkg_yn"))					this.pkgYn = value;
				if(key.toLowerCase().equals("album_id"))				this.albumId = value;
				if(key.toLowerCase().equals("album_name"))				this.albumName = value;
				if(key.toLowerCase().equals("cat_id"))					this.catId = value;
				if(key.toLowerCase().equals("buying_price"))			this.buyingPrice = value;
				if(key.toLowerCase().equals("buying_type"))				this.buyingType = value;
				if(key.toLowerCase().equals("ofr_seq"))					this.ofrSeq = value;
				if(key.toLowerCase().equals("offer_cd"))				this.offerCd = value;
				if(key.toLowerCase().equals("offer_nm"))				this.offerNm = value;
				if(key.toLowerCase().equals("app_type"))				this.appType = value;
				if(key.toLowerCase().equals("alwnce_charge"))			this.alwnceCharge = value;
				if(key.toLowerCase().equals("balace"))					this.balace = value;
				if(key.toLowerCase().equals("alwnce_unt"))				this.alwnceUnt = value;
				if(key.toLowerCase().equals("offer_type"))				this.offerType = value;
				if(key.toLowerCase().equals("buy_type_flag"))			this.buyTypeFlag = value;
				if(key.toLowerCase().equals("datafree_buy_price"))		this.datafreeBuyPrice = value;
				if(key.toLowerCase().equals("datafree_alwnce_charge"))	this.datafreeAlwnceCharge = value;
				if(key.toLowerCase().equals("datafree_balace"))			this.datafreeBalace = value;
			}
		}
		
		//BuyContsCpController.saId = paramMap.get("sa_id");
		
		//BuyContsCpController.stbMac = paramMap.get("stb_mac");
		
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null
			|| paramMap.get("pkg_yn") == null || paramMap.get("album_id") == null 
			|| paramMap.get("album_name") == null || paramMap.get("cat_id") == null
			|| paramMap.get("buying_price") == null || paramMap.get("buying_type") == null )
		{
			throw new ImcsException();
		}
		
		/*if(this.saId.length() > 12 || this.saId.length() < 7 ){
			throw new ImcsException();
		}
		
		if(this.stbMac.length() > 39 || this.stbMac.length() < 14 ){
			throw new ImcsException();
		}*/

		
		if( !"".equals(this.saId) ){
			this.cIdxSa = this.saId.substring(this.saId.length()-2, this.saId.length());
			try {
				this.pIdxSa = Integer.parseInt(this.cIdxSa) % 33;
			} catch (Exception e) {
				this.pIdxSa = 0;
			}
			
		}
	
		this.offerType			= StringUtil.replaceNull(this.offerType, "1");
		this.buyTypeFlag		= StringUtil.replaceNull(this.buyTypeFlag, "1");
		this.datafreeBuyPrice	= StringUtil.replaceNull(this.datafreeBuyPrice, "0");
		this.datafreeAlwnceCharge	= StringUtil.replaceNull(this.datafreeAlwnceCharge, "0");
		this.datafreeBalace	= StringUtil.replaceNull(this.datafreeBalace, "0");
		
		/*switch (offerType) {
		case "1":
		case "2":
		case "3":
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

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public String getAlwnceCharge() {
		return alwnceCharge;
	}

	public void setAlwnceCharge(String alwnceCharge) {
		this.alwnceCharge = alwnceCharge;
	}

	public String getBalace() {
		return balace;
	}

	public void setBalace(String balace) {
		this.balace = balace;
	}

	public String getAlwnceUnt() {
		return alwnceUnt;
	}

	public void setAlwnceUnt(String alwnceUnt) {
		this.alwnceUnt = alwnceUnt;
	}

	public String getOfferType() {
		return offerType;
	}

	public void setOfferType(String offerType) {
		this.offerType = offerType;
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

	public String getDatafreeAlwnceCharge() {
		return datafreeAlwnceCharge;
	}

	public void setDatafreeAlwnceCharge(String datafreeAlwnceCharge) {
		this.datafreeAlwnceCharge = datafreeAlwnceCharge;
	}

	public String getDatafreeBalace() {
		return datafreeBalace;
	}

	public void setDatafreeBalace(String datafreeBalace) {
		this.datafreeBalace = datafreeBalace;
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

	public String getCpUseYn() {
		return cpUseYn;
	}

	public void setCpUseYn(String cpUseYn) {
		this.cpUseYn = cpUseYn;
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

	public long getTp_start() {
		return tp_start;
	}

	public void setTp_start(long tp_start) {
		this.tp_start = tp_start;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}

	public String getcIdxSa() {
		return cIdxSa;
	}

	public void setcIdxSa(String cIdxSa) {
		this.cIdxSa = cIdxSa;
	}

	public Integer getpIdxSa() {
		return pIdxSa;
	}

	public void setpIdxSa(Integer pIdxSa) {
		this.pIdxSa = pIdxSa;
	}

	public String getpIdxDay() {
		return pIdxDay;
	}

	public void setpIdxDay(String pIdxDay) {
		this.pIdxDay = pIdxDay;
	}

	public String getBuyingDate() {
		return buyingDate;
	}

	public void setBuyingDate(String buyingDate) {
		this.buyingDate = buyingDate;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
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

	public Integer getSqlCode() {
		return sqlCode;
	}

	public void setSqlCode(Integer sqlCode) {
		this.sqlCode = sqlCode;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
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

	public String getSuggestedDatafreeBuyPrice() {
		return suggestedDatafreeBuyPrice;
	}

	public void setSuggestedDatafreeBuyPrice(String suggestedDatafreeBuyPrice) {
		this.suggestedDatafreeBuyPrice = suggestedDatafreeBuyPrice;
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

}
