package kr.co.wincom.imcs.api.buyNSContent;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

@SuppressWarnings("serial")
public class BuyNSContentRequestVO  extends NoSqlLoggingVO implements Serializable {

    private String saId = "";
    private String stbMac = "";
    private String pkgYn = "";
    private String albumId = "";
    private String albumName = "";
    private String belongingName = "";
    private String contsGenre = "";
    private String catId = "";
    private String buyingDate = "";
    private String buyDate = "";
    private String buyingPrice = "";
    private String suggestedPrice = "";
    private String buyingType = "";    
    private String productType = "";
    private String distributor = "";
    
    private String cpUseYn = "";
    private String expiredDate = "";
    private String statusFlag = "";    
    private String ynVodOpen = "";    
    private Long cpCnt;
    private String billType = "";
    private String categoryId = "";
    private String eventType = "";
    
    private String ofrSeq = "";
    private String offerCd = "";
    private String offerNm = "";
    private String alwnceUnt = "";
    private String alwnceAmnt = "";
    private String applStrtYmd = "";
    private String applEndYmd = "";
    private String adiProductId = "";
    
    private String eventValue = "";
    private String eventPrice = "";
    
    private String applType = "";
    private String pointAmt = "";
    
    private String cIdxSa = "";
    private Integer pIdxSa;
    private String pIdxDay = "";
        
    private String pid		= "";
    private String resultCode = "";
    
    private Integer result_set = 0;
    
    private String alwnceCharge = "";
    
    private String productId1 = "";
	private String productName1 = "";
    private String productPrice1 = "";
    private String expiredDate1 = "";
    private String productId2 = "";
    private String contsId2 = "";
    private String contsName2 = "";
    private String contsGenre2 = "";
    
    private String paymentId		= "";
    private String buyingGb			= "";
    
    private String balace			= "";
    private String approvalPrice 		= "";
    
    private String reservedDate			= "";
    private String reservedPrice 		= "";
    
    private String genreInfo			= "";
    private String strmpId 				= "";
    private String cpevtId				= "";
    
    private Integer resultSet		= 0;
    private Integer sqlCode		= 0;
    private String blockflag = "";
    
    // 2018.12.20 - 쿠폰 함수 조회 시 VR앱일 경우에는 스크립 정보를 VR로 보내 주어야 한다.
    private String screenType = "";
    private String systemGb = "";
    
    private int	   szSurtaxRate = 10;
    private int	   udrCharge = 0;
    
    private long tp_start = 0;
    
    public BuyNSContentRequestVO(){}
    
    public BuyNSContentRequestVO(String szParam){
    	
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
				
				/*if( !value.matches(ImcsConstants.N_SP_PTN) && key.toLowerCase().indexOf("name") == null ){
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
				if(key.toLowerCase().equals("alwnce_unt"))		this.alwnceUnt= value;
				if(key.toLowerCase().equals("alwnce_amnt"))		this.alwnceAmnt = value;
				if(key.toLowerCase().equals("appl_strt_ymd"))	this.applStrtYmd = value;
				if(key.toLowerCase().equals("appl_end_ymd"))	this.applEndYmd = value;
				if(key.toLowerCase().equals("app_type"))		this.applType = value;
				if(key.toLowerCase().equals("point_amt"))		this.pointAmt = value;
				if(key.toLowerCase().equals("payment_id"))		this.paymentId = value;
				if(key.toLowerCase().equals("buying_gb"))		this.buyingGb = value;
				
			}
		}
		
		//BuyNSContentController.saId = paramMap.get("sa_id");
		
		//BuyNSContentController.stbMac = paramMap.get("stb_mac");
		
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
		
		int nLen = 0;
		
		if(paramMap.get("sa_id") == null ) throw new ImcsException();
		nLen = this.saId.length();
		if(nLen > 12 || nLen < 7) throw new ImcsException();
		if(isContainSpace(this.saId) ) throw new ImcsException();
		
		nLen = 0;
		if(paramMap.get("stb_mac") == null ) throw new ImcsException();
		nLen = this.stbMac.length();
		if(nLen > 39 || nLen < 14) throw new ImcsException();
		if(isContainSpace(this.stbMac) ) throw new ImcsException();
		
		this.pointAmt	= StringUtil.replaceNull(this.pointAmt, "0");
		this.paymentId	= StringUtil.nullToSpace(this.paymentId);
		this.buyingGb	= StringUtil.replaceNull(this.buyingGb, "N");
		
		if( "".equals(this.paymentId) && "A".equals(this.buyingType.toUpperCase()) ){
			throw new ImcsException();
		}
		
		/*switch (buyingGb) {
		case "N":
		case "R":
			break;

		default:
			throw new ImcsException();
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

	public String getBelongingName() {
		return belongingName;
	}

	public void setBelongingName(String belongingName) {
		this.belongingName = belongingName;
	}

	public String getContsGenre() {
		return contsGenre;
	}

	public void setContsGenre(String contsGenre) {
		this.contsGenre = contsGenre;
	}

	public String getCatId() {
		return catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
	}

	public String getBuyingDate() {
		return buyingDate;
	}

	public void setBuyingDate(String buyingDate) {
		this.buyingDate = buyingDate;
	}

	public String getBuyDate() {
		return buyDate;
	}

	public void setBuyDate(String buyDate) {
		this.buyDate = buyDate;
	}

	public String getBuyingPrice() {
		return buyingPrice;
	}

	public void setBuyingPrice(String buyingPrice) {
		this.buyingPrice = buyingPrice;
	}

	public String getSuggestedPrice() {
		return suggestedPrice;
	}

	public void setSuggestedPrice(String suggestedPrice) {
		this.suggestedPrice = suggestedPrice;
	}

	public String getBuyingType() {
		return buyingType;
	}

	public void setBuyingType(String buyingType) {
		this.buyingType = buyingType;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getDistributor() {
		return distributor;
	}

	public void setDistributor(String distributor) {
		this.distributor = distributor;
	}

	public String getCpUseYn() {
		return cpUseYn;
	}

	public void setCpUseYn(String cpUseYn) {
		this.cpUseYn = cpUseYn;
	}

	public String getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(String expiredDate) {
		this.expiredDate = expiredDate;
	}

	public String getStatusFlag() {
		return statusFlag;
	}

	public void setStatusFlag(String statusFlag) {
		this.statusFlag = statusFlag;
	}

	public String getYnVodOpen() {
		return ynVodOpen;
	}

	public void setYnVodOpen(String ynVodOpen) {
		this.ynVodOpen = ynVodOpen;
	}

	public Long getCpCnt() {
		return cpCnt;
	}

	public void setCpCnt(Long cpCnt) {
		this.cpCnt = cpCnt;
	}

	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
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

	public String getAdiProductId() {
		return adiProductId;
	}

	public void setAdiProductId(String adiProductId) {
		this.adiProductId = adiProductId;
	}

	public String getEventValue() {
		return eventValue;
	}

	public void setEventValue(String eventValue) {
		this.eventValue = eventValue;
	}

	public String getEventPrice() {
		return eventPrice;
	}

	public void setEventPrice(String eventPrice) {
		this.eventPrice = eventPrice;
	}

	public String getApplType() {
		return applType;
	}

	public void setApplType(String applType) {
		this.applType = applType;
	}

	public String getPointAmt() {
		return pointAmt;
	}

	public void setPointAmt(String pointAmt) {
		this.pointAmt = pointAmt;
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

	public Integer getResult_set() {
		return result_set;
	}

	public void setResult_set(Integer result_set) {
		this.result_set = result_set;
	}

	public long getTp_start() {
		return tp_start;
	}

	public void setTp_start(long tp_start) {
		this.tp_start = tp_start;
	}

	public String getpIdxDay() {
		return pIdxDay;
	}

	public void setpIdxDay(String pIdxDay) {
		this.pIdxDay = pIdxDay;
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

	public String getProductId2() {
		return productId2;
	}

	public void setProductId2(String productId2) {
		this.productId2 = productId2;
	}

	public String getAlwnceCharge() {
		return alwnceCharge;
	}

	public void setAlwnceCharge(String alwnceCharge) {
		this.alwnceCharge = alwnceCharge;
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

	public String getBalace() {
		return balace;
	}

	public void setBalace(String balace) {
		this.balace = balace;
	}

	public String getApprovalPrice() {
		return approvalPrice;
	}

	public void setApprovalPrice(String approvalPrice) {
		this.approvalPrice = approvalPrice;
	}

	public String getReservedDate() {
		return reservedDate;
	}

	public void setReservedDate(String reservedDate) {
		this.reservedDate = reservedDate;
	}

	public String getReservedPrice() {
		return reservedPrice;
	}

	public void setReservedPrice(String reservedPrice) {
		this.reservedPrice = reservedPrice;
	}

	public String getGenreInfo() {
		return genreInfo;
	}

	public void setGenreInfo(String genreInfo) {
		this.genreInfo = genreInfo;
	}

	public String getStrmpId() {
		return strmpId;
	}

	public void setStrmpId(String strmpId) {
		this.strmpId = strmpId;
	}

	public String getCpevtId() {
		return cpevtId;
	}

	public void setCpevtId(String cpevtId) {
		this.cpevtId = cpevtId;
	}

	public Integer getResultSet() {
		return resultSet;
	}

	public void setResultSet(Integer resultSet) {
		this.resultSet = resultSet;
	}

	public Integer getSqlCode() {
		return sqlCode;
	}

	public void setSqlCode(Integer sqlCode) {
		this.sqlCode = sqlCode;
	}

	public String getBlockflag() {
		return blockflag;
	}

	public void setBlockflag(String blockflag) {
		this.blockflag = blockflag;
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

	private boolean isContainSpace(String item) {
	      return  item.matches("^\\s*$");
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
	
	
}
