package kr.co.wincom.imcs.api.chkBuyNSPG;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;


@SuppressWarnings("serial")
public class ChkBuyNSPGRequestVO extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * ChkBuyNSPG API 전문 칼럼(순서 일치)
	********************************************************************/
	private String saId			= "";
    private String stbMac		= "";
    private String pkgYn		= ""; // Y:패키지구매, N:단품구매
    private String albumId		= "";
    private String albumName	= "";
    private String catId		= "";
    private String buyingPrice	= "";
    private String buyingType	= "";
    private String ofrSeq		= "";
    private String offerCd		= "";
    private String offerNm		= "";
    private String alwnceUnt	= "";
    private String alwnceAmnt	= "";
    private String applStrtYmd	= "";
    private String applEndYmd	= "";
    private String appType		= "";
    private String payGb		= "";
    
    //2019.09.04 영화월정액 내재화 라이센스 만료된 컨텐츠 구매 불가 로직 구현을 위하여 추가
    private String licensingStart   = "";
    private String licensingEnd     = "";
    private String licensingValidYn = "";
    
    /********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
    private String pid			= "";
    private String resultCode	= "";
    private String cpUseYn		= "";
    private String cIdxSa		= "";
    private String balace		= "";
    private String buyingDate	= "";
    private String approvalPrice	= "";
    private String prodType		= "";
    private String contsGenre	= "";
    private String buyDate		= "";
    
    private Integer resultSet	= 0;
    private Integer messageSet	= 0;
    private Integer	block_flag	= 0;
    
    private long tp_start = 0;
    private int pIdxSa;
    
    private String ppsId		= "";
 
    
    public ChkBuyNSPGRequestVO(){}
    
    public ChkBuyNSPGRequestVO(String szParam){
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
				if(key.toLowerCase().equals("pay_gb"))			this.payGb = value;
				if(key.toLowerCase().equals("licensing_valid_yn"))			this.licensingValidYn = value;
				if(key.toLowerCase().equals("pps_id"))			this.ppsId = value;
				
			}
		}
		
		//ChkBuyNSPGController.saId = paramMap.get("sa_id");
		
		//ChkBuyNSPGController.stbMac = paramMap.get("stb_mac");
		
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null
				|| paramMap.get("pkg_yn") == null || paramMap.get("album_id") == null 
				|| paramMap.get("album_name") == null || paramMap.get("cat_id") == null
				|| paramMap.get("buying_price") == null || paramMap.get("buying_type") == null )
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.saId, 7, 12, 1)) {
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.stbMac, 14, 14, 1)) {
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

		if(!commonService.getValidParam(this.albumId, 15, 20, 1)) {
			throw new ImcsException();
		}

		if(this.buyingPrice == null || this.buyingPrice.equals(""))
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.buyingPrice, 1, 15, 3)) {
			throw new ImcsException();
		}
		
		if(this.buyingType == null || this.buyingType.equals(""))
		{
			this.buyingType = "B";
		}
		
		if(!commonService.getValidParam(this.buyingType, 0, 1, 2)) {
			throw new ImcsException();
		}

		if(this.appType == null || this.appType.equals(""))
		{
			this.appType = "RUSA";
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

		this.payGb = StringUtil.replaceNull(this.payGb, "S");
		
		if("B".equals(buyingType.toUpperCase()))		this.cpUseYn = "N";		// 일반구매
		else if("C".equals(buyingType.toUpperCase()))	this.cpUseYn = "Y";		// 쿠폰구매
		else if("A".equals(buyingType.toUpperCase())){							// 인앱구매
			this.cpUseYn = "A";		
			this.balace	= this.buyingPrice;
		} else if("F".equals(buyingType.toUpperCase())){			
			this.buyingType = "B";	
			this.cpUseYn = "N";
		}
		
		if( !"".equals(this.saId) ){
			this.cIdxSa = this.saId.substring(this.saId.length()-2, this.saId.length());
			try {
				this.pIdxSa = Integer.parseInt(this.cIdxSa) % 33;
			} catch (Exception e) {
				this.pIdxSa = 0;
			}
			
		}
		
		/*switch (payGb) {
		case "N":
		case "S":
		case "R":
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

	public String getPayGb() {
		return payGb;
	}

	public void setPayGb(String payGb) {
		this.payGb = payGb;
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

	public String getcIdxSa() {
		return cIdxSa;
	}

	public void setcIdxSa(String cIdxSa) {
		this.cIdxSa = cIdxSa;
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

	public int getpIdxSa() {
		return pIdxSa;
	}

	public void setpIdxSa(int pIdxSa) {
		this.pIdxSa = pIdxSa;
	}

	public String getBalace() {
		return balace;
	}

	public void setBalace(String balace) {
		this.balace = balace;
	}

	public String getBuyingDate() {
		return buyingDate;
	}

	public void setBuyingDate(String buyingDate) {
		this.buyingDate = buyingDate;
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

	public String getContsGenre() {
		return contsGenre;
	}

	public void setContsGenre(String contsGenre) {
		this.contsGenre = contsGenre;
	}

	public String getBuyDate() {
		return buyDate;
	}

	public void setBuyDate(String buyDate) {
		this.buyDate = buyDate;
	}

	public Integer getBlock_flag() {
		return block_flag;
	}

	public void setBlock_flag(Integer block_flag) {
		this.block_flag = block_flag;
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

	public String getPpsId() {
		return ppsId;
	}

	public void setPpsId(String ppsId) {
		this.ppsId = ppsId;
	}
	

}
