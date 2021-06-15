package kr.co.wincom.imcs.api.buyNSPresent;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

@SuppressWarnings("serial")
public class BuyNSPresentRequestVO  extends NoSqlLoggingVO implements Serializable {

	/********************************************************************
	 * BuyNSPresent API 전문 칼럼(순서 일치)
	********************************************************************/
    private String saId					= "";		// 가입자 정보
    private String stbMac				= "";		// 가입자 MAC ADDRESS
    private String albumId				= "";		// 앨범ID
    private String albumName			= "";		// 영화제목
    private String catId				= "";		// 컨텐츠가 포함된 카테고리 ID
    private String buyingPrice			= "";		// 컨텐츠 금액(할인전 실금액)
    private String presentPrice			= "";		// 선물하기 음액
    private String buyingType			= "";		// 구매타입 (B:일반구매, W:페이나우, 기타:Error) 
    private String appType				= "";		// 어플타입
    private String ctnNo				= "";		// CTN번호
    private String rcvSaId				= "";		// 수신자 가입자정보
    private String rcvStbMac			= "";		// 수신자 MAC ADDRESS
    private String rcvCtnNo				= "";		// 수신자 CTN번호
    private String smsMsg				= "";		// 전송 메세지
    private String realPresentPrice		= "";		// 선물하기 금액
    
    /********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
    private String pid			= "";
    private String resultCode	= "";
    private String cpUseYn		= "";
    private String alwnceCharge	= "";
    private String balace		= "";
    private String cIdxSa		= "";
    private String buyingDate	= "";
    private String contsGenre	= "";
    
    private Integer pIdxSa		= 0;
    private Integer resultSet	= 0;
    private Integer messageSet	= 0;
    private Integer sqlCode		= 0;
        
    // 2019.10.29 - VOD 정산 프로세스 개선 : NPT_VO_BUY_META 테이블 넣는 변수 추가
    private String prodType			= "";		// 2019.10.29 - NPT_VO_BUY_META 에도 사용
    private String assetName 		= "";		// PPV 콘텐츠 정보 세팅, PPS 구매시에는 구매하는 콘텐츠의 콘텐츠명 사용
    private String hdcontent 		= "";		// PPV 콘텐츠 정보 세팅, PPS 구매시에는 구매하는 콘텐츠의 HDCONTENT값 사용
    private String ratingCd 		= "";		// PPV 콘텐츠 정보 세팅, PPS 구매시에는 구매하는 콘텐츠의 연령정보 사용
    private String productId 		= "";		// PPV 콘텐츠 정보 세팅, PPS 구매시에는 별도로 저장하고 있던 패키지 상품ID 사용
    private String productName 		= "";		// PPV 콘텐츠 정보 세팅, PPS 구매시에는 별도로 저장하고 있던 패키지 상품명 사용
    private String productKind 		= "";		// PPV or PPS 상품 타입
    private String cpId 			= "";		// PPV 콘텐츠 정보 세팅, PPS 구매시에는 pps_cp_id의 우선순위에 따른 정보 사용
    private String maximumViewingLength 		= "";	// PPV 콘텐츠 정보 세팅, PPS 구매시에는 별도로 저장하고 있던 패키지 유효일자 사용
    private String seriesNo 		= "";		// PPV 콘텐츠 정보 세팅, PPS 구매시에는 해당변수에는 NULL(빈값)로 데이터를 넣는다.
    
    
    private long tp_start = 0;
    
    public BuyNSPresentRequestVO(){}
    
    public BuyNSPresentRequestVO(String szParam){
    	
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
				
				/*if( !value.matches(ImcsConstants.N_SP_PTN) && key.toLowerCase().indexOf("name") == -1 && key.toLowerCase().indexOf("msg") == -1 ){
					//특수문자 있음
					throw new ImcsException();
				}*/
				
				if(key.toLowerCase().equals("sa_id"))				this.saId = value;
				if(key.toLowerCase().equals("stb_mac"))				this.stbMac = value;
				if(key.toLowerCase().equals("album_id"))			this.albumId = value;
				if(key.toLowerCase().equals("album_name"))			this.albumName = value;
				if(key.toLowerCase().equals("cat_id"))				this.catId = value;
				if(key.toLowerCase().equals("buying_price"))		this.buyingPrice = value;
				if(key.toLowerCase().equals("present_price"))		this.presentPrice = value;
				if(key.toLowerCase().equals("buying_type"))			this.buyingType = value;
				if(key.toLowerCase().equals("app_type"))			this.appType = value;
				if(key.toLowerCase().equals("ctn_no"))				this.ctnNo = value;
				if(key.toLowerCase().equals("rcv_sa_id"))			this.rcvSaId = value;
				if(key.toLowerCase().equals("rcv_stb_mac"))			this.rcvStbMac = value;
				if(key.toLowerCase().equals("rcv_ctn_no"))			this.rcvCtnNo = value;
				if(key.toLowerCase().equals("sms_msg"))				this.smsMsg = value;
				if(key.toLowerCase().equals("real_present_price"))	this.setRealPresentPrice(value);
				
			}
		}
		
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null
				|| paramMap.get("album_id") == null || paramMap.get("album_name") == null 
				|| paramMap.get("cat_id") == null || paramMap.get("buying_price") == null
				|| paramMap.get("present_price") == null || paramMap.get("buying_type") == null
				|| paramMap.get("app_type") == null || paramMap.get("ctn_no") == null
				|| paramMap.get("rcv_sa_id") == null || paramMap.get("rcv_stb_mac") == null
				|| paramMap.get("rcv_ctn_no") == null )
		{
			throw new ImcsException();
		}
					
		/*if(this.saId.length() > 12 || this.saId.length() < 7 ){
			throw new ImcsException();
		}
		
		if(this.stbMac.length() > 39 || this.stbMac.length() < 14 ){
			throw new ImcsException();
		}*/
		
		//실제 선물 구매는 일반구매와 페이나우 구매만 존재함
		if( "B".equals(this.buyingType.toUpperCase()) )		this.cpUseYn = "N";		// 일반구매
		else if("W".equals(buyingType.toUpperCase())){								// PAY NOW 구매
			this.cpUseYn = "W";
			this.alwnceCharge = this.buyingPrice;
		}
		else{
			this.resultSet = -1;
			this.messageSet = 15;
		}
		
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

	public String getBuyingType() {
		return buyingType;
	}

	public void setBuyingType(String buyingType) {
		this.buyingType = buyingType;
	}

	public String getBuyingPrice() {
		return buyingPrice;
	}

	public void setBuyingPrice(String buyingPrice) {
		this.buyingPrice = buyingPrice;
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

	public String getCtnNo() {
		return ctnNo;
	}

	public void setCtnNo(String ctnNo) {
		this.ctnNo = ctnNo;
	}

	public String getRcvSaId() {
		return rcvSaId;
	}

	public void setRcvSaId(String rcvSaId) {
		this.rcvSaId = rcvSaId;
	}

	public String getRcvStbMac() {
		return rcvStbMac;
	}

	public void setRcvStbMac(String rcvStbMac) {
		this.rcvStbMac = rcvStbMac;
	}

	public String getRcvCtnNo() {
		return rcvCtnNo;
	}

	public void setRcvCtnNo(String rcvCtnNo) {
		this.rcvCtnNo = rcvCtnNo;
	}

	public String getSmsMsg() {
		return smsMsg;
	}

	public void setSmsMsg(String smsMsg) {
		this.smsMsg = smsMsg;
	}

	public String getPresentPrice() {
		return presentPrice;
	}

	public void setPresentPrice(String presentPrice) {
		this.presentPrice = presentPrice;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
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

	public String getBuyingDate() {
		return buyingDate;
	}

	public void setBuyingDate(String buyingDate) {
		this.buyingDate = buyingDate;
	}

	public String getContsGenre() {
		return contsGenre;
	}

	public void setContsGenre(String contsGenre) {
		this.contsGenre = contsGenre;
	}

	public Integer getSqlCode() {
		return sqlCode;
	}

	public void setSqlCode(Integer sqlCode) {
		this.sqlCode = sqlCode;
	}

	public String getRealPresentPrice() {
		return realPresentPrice;
	}

	public void setRealPresentPrice(String realPresentPrice) {
		this.realPresentPrice = realPresentPrice;
	}

	public String getProdType() {
		return prodType;
	}

	public void setProdType(String prodType) {
		this.prodType = prodType;
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
